package com.thoughtworks.lean.gocd.agentManager.service;

import com.thoughtworks.lean.exception.ServiceErrorException;
import com.thoughtworks.lean.gocd.GoClient;
import com.thoughtworks.lean.gocd.agentManager.domain.AgentConfig;
import com.thoughtworks.lean.gocd.agentManager.domain.AgentManagerConfig;
import com.thoughtworks.lean.gocd.agentManager.domain.AgentServiceInfo;
import com.thoughtworks.lean.gocd.agentManager.domain.CompositeAgentInfo;
import com.thoughtworks.lean.gocd.agentManager.repository.AgentConfigRepository;
import com.thoughtworks.lean.gocd.agentManager.repository.AgentManagerConfigRepository;
import com.thoughtworks.lean.gocd.dto.AgentInfo;
import com.thoughtworks.lean.rancher.RancherClient;
import com.thoughtworks.lean.rancher.dto.ServiceInfo;
import com.thoughtworks.lean.rancher.dto.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AgentServiceImpl implements AgentService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static int MANAGER_CONFIG_DEFAULT_ID = 1;
    private static int AGENT_RESTART_RETRY = 3;
    private static long INSTANCE_STATUS_CHECK_INTERVAL = 5000;
    private static long AGENT_START_TIME_OUT = 30000;
    private String agentEnvironment;

    private String agentStack;


    ThreadPoolTaskExecutor taskExecutor;

    {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.initialize();
    }

    @Autowired
    private RancherClient rancherClient;

    @Autowired
    private GoClient goClient;

    @Autowired
    private AgentConfigRepository configRepository;

    @Autowired
    private AgentManagerConfigRepository managerConfigRepository;

    public AgentServiceImpl() {
    }

    @Override
    public List<AgentServiceInfo> getAgentServices() {
        // Environment in rancher UI === Project in rancher API
        // Stack in rancher UI === Environment in rancher API
        List<ServiceInfo> serviceInfos;
        try {
            serviceInfos = rancherClient.servicesByEnvironmentName(this.getAgentEnvironment(), this.getAgentStack());
        } catch (Exception e) {
            LOG.error("Can not get Agent Services from Rancher, Maybe Environment or Stack is wrong. Env: " + this.getAgentEnvironment() + " Stack: " + this.getAgentStack());
            return Collections.emptyList();
        }
        //
        return serviceInfos.stream()
                .map(this::getAgentServiceInfo)
                .collect(Collectors.toList());
    }

    private AgentServiceInfo getAgentServiceInfo(ServiceInfo serviceInfo) {
        AgentServiceInfo agentServiceInfo = new AgentServiceInfo();
        agentServiceInfo.setServiceName(serviceInfo.getName());
        agentServiceInfo.setServiceInfo(serviceInfo);
        agentServiceInfo.setConfig(getConfigByName(serviceInfo.getName()));
        agentServiceInfo.setCompositeAgentInfos(getCompositeAgentInfos(serviceInfo));
        agentServiceInfo.setAgentManagerConfig(managerConfigRepository.findOneById(MANAGER_CONFIG_DEFAULT_ID));
        return agentServiceInfo;
    }


    AgentInfo getAgentsByContainerId(List<AgentInfo> agentInfos, String containerId) {
        for (AgentInfo agentInfo : agentInfos) {
            if (containerId.startsWith(agentInfo.getHostname())) {
                return agentInfo;
            }
        }
        return null;
    }


    public List<CompositeAgentInfo> getCompositeAgentInfos(ServiceInfo serviceInfo) {
        List<ServiceInstance> serviceInstances = rancherClient.serviceInstances(serviceInfo.getAccountId(), serviceInfo.getId(), false);
        final List<AgentInfo> agentInfos = goClient.fetchAllAgents().getEmbedded().getAgents();
        return serviceInstances.stream().map(serviceInstance -> {
            CompositeAgentInfo agentInfoServiceInstancePair = new CompositeAgentInfo()
                    .setServiceInstance(serviceInstance);
            agentInfoServiceInstancePair.setAgentInfo(getAgentsByContainerId(agentInfos, serviceInstance.getExternalId()));
            return agentInfoServiceInstancePair;
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public AgentConfig getConfigByName(String serviceName) {
        return configRepository.findByName(serviceName);
    }

    @Override
    public List<AgentConfig> getAllConfig() {
        return configRepository.findAll();
    }

    @Override
    public AgentConfig updateConfig(AgentConfig config) {
        AgentConfig configInDB = configRepository.findByName(config.getName());
        BeanUtils.copyProperties(config, configInDB, "id", "name");
        return configRepository.save(config);
    }

    public AgentServiceImpl setRancherClient(RancherClient rancherClient) {
        this.rancherClient = rancherClient;
        return this;
    }

    public AgentServiceImpl setGoClient(GoClient goClient) {
        this.goClient = goClient;
        return this;
    }

    public AgentServiceImpl setConfigRepository(AgentConfigRepository configRepository) {
        this.configRepository = configRepository;
        return this;
    }

    public String getAgentEnvironment() {
        return agentEnvironment;
    }

    public AgentServiceImpl setAgentEnvironment(String agentEnvironment) {
        this.agentEnvironment = agentEnvironment;
        return this;
    }

    public String getAgentStack() {
        return agentStack;
    }

    public AgentServiceImpl setAgentStack(String agentStack) {
        this.agentStack = agentStack;
        return this;
    }

    public AgentServiceImpl setManagerConfigRepository(AgentManagerConfigRepository managerConfigRepository) {
        this.managerConfigRepository = managerConfigRepository;
        return this;
    }

    @Override
    public ServiceInfo scaleUpInstancesByName(String serviceName) {
        ServiceInfo serviceInfo = getServiceInfo(serviceName);
        scaleUp(getAgentServiceInfo(serviceInfo));
        return serviceInfo;
    }


    @Override
    public ServiceInfo scaleDownInstancesByName(String serviceName) {
        ServiceInfo serviceInfo = getServiceInfo(serviceName);
        scaleDown(getAgentServiceInfo(serviceInfo));
        return serviceInfo;
    }

    @Override
    public ServiceInfo getServiceInfo(String serviceName) {
        return rancherClient.serviceInfoByName(this.getAgentEnvironment(), serviceName);
    }


    @Override
    public AgentManagerConfig getAgentManagerConfig() {
        return managerConfigRepository.findOneById(MANAGER_CONFIG_DEFAULT_ID);
    }

    @Override
    @Transactional
    public AgentManagerConfig updateAgentManagerConfig(AgentManagerConfig config) {
        AgentManagerConfig configInDB = this.getAgentManagerConfig();
        if (config.equals(configInDB)) {
            return configInDB;
        }
        if (configInDB == null) {
            configInDB = new AgentManagerConfig();
        }
        List<ServiceInfo> services;
        try {
            services = rancherClient.servicesByEnvironmentName(config.getAgentEnvironment(), config.getAgentStack());
        } catch (Exception e) {
            LOG.error("Can not get Agent Services from Rancher, Maybe Environment or Stack is wrong. configEnv: " + config.getAgentEnvironment() + " configStack: " + config.getAgentStack());
            throw new ServiceErrorException("Can not get Agent Services from Rancher. Due to: " + e.getMessage());
        }

        this.setAgentEnvironment(config.getAgentEnvironment());
        this.setAgentStack(config.getAgentStack());
        // TODO minIdles / scaleStep / maxInstances are not changed.
        BeanUtils.copyProperties(config, configInDB, "id", "name ");
        configInDB.setId(MANAGER_CONFIG_DEFAULT_ID);
        configRepository.deleteAll();

        for (ServiceInfo serviceInfo : services) {
            AgentConfig agentConfig = new AgentConfig(
                    serviceInfo.getName(),
                    configInDB.getMinIdles(),
                    configInDB.getScaleStep(),
                    configInDB.getMaxInstances(),
                    false,
                    config.getAgentEnvironment());
            agentConfig.addResource(config.getManagedServiceResource());
            configRepository.save(agentConfig);
        }
        return managerConfigRepository.save(configInDB);
    }

    @Override
    public void deleteDisabledAgentAndStopInstance(AgentServiceInfo agentServiceInfo) {
        for (CompositeAgentInfo compositeAgentInfo : agentServiceInfo.getCompositeAgentInfos()) {
            //add reources to go-cd agent
            if (compositeAgentInfo.isAgentDisabled() && compositeAgentInfo.isAgentIdle()) {
                //stop instance
                if (compositeAgentInfo.isInstanceRunning()) {
                    rancherClient.instanceStop(compositeAgentInfo.getServiceInstance());
                }
                //delete agent info
                goClient.deleteAgent(compositeAgentInfo.getAgentInfo().getUuid());
            }

        }
    }

    @Override
    public void addResourcesAndEnablePendingAgent(AgentServiceInfo agentServiceInfo) {

        for (CompositeAgentInfo compositeAgentInfo : agentServiceInfo.getCompositeAgentInfos()) {
            enablePendingAgentAndAddResourcesAnd(agentServiceInfo, compositeAgentInfo);
        }
    }

    private void enablePendingAgentAndAddResourcesAnd(AgentServiceInfo agentServiceInfo, CompositeAgentInfo compositeAgentInfo) {
        //add reources to go-cd agent
        AgentInfo agentInfo = compositeAgentInfo.getAgentInfo();
        if (agentInfo == null) {
            return;
        }
        //
        if (compositeAgentInfo.isGoCDAgentPending()) {
            goClient.enableAgent(agentInfo.getUuid());
        }

        if (!compositeAgentInfo.isResourceAdded(agentServiceInfo.getAgentManagerConfig())) {
            goClient.addResourcesToAgent(agentInfo.getUuid(), agentServiceInfo.getResources());
        }

    }

    @Override
    public void scaleUp(AgentServiceInfo agentServiceInfo) {
        //scaleUp
        int scaleCount = 0;
        for (CompositeAgentInfo compositeAgentInfo : agentServiceInfo.getCompositeAgentInfos()) {
            //start stopped instance
            if (scaleCount >= agentServiceInfo.getScaleStep()) {
                continue;
            }
            int agentStartRetry = 0;
            if (!compositeAgentInfo.isAgentInstanceStopped()) {
                continue;
            }
            do {
                //instance
                compositeAgentInfo.setServiceInstance(rancherClient.instanceById(compositeAgentInfo.getServiceInstance().getAccountId(), compositeAgentInfo.getServiceInstance().getId()));
                if (compositeAgentInfo.isInstanceStarting()) {
                    sleep(INSTANCE_STATUS_CHECK_INTERVAL);
                    continue;
                }
                if (compositeAgentInfo.isAgentInstanceStopped()) {
                    ServiceInstance serviceInstance = rancherClient.instanceStart(compositeAgentInfo.getServiceInstance());
                    compositeAgentInfo.setServiceInstance(serviceInstance);
                    agentStartRetry++;
                    if (agentStartRetry > 1) {
                        LOG.warn("Agent Restart Retry! {}", compositeAgentInfo);
                    }

                    if (agentStartRetry > AGENT_RESTART_RETRY) {
                        LOG.error("Agent Restart Failed(Exceed Retry Times)! {} ", compositeAgentInfo);
                        break;
                    }
                }
                //gocd-agent
                sleep(INSTANCE_STATUS_CHECK_INTERVAL);
                String externalId = compositeAgentInfo.getServiceInstance().getExternalId();
                AgentInfo agentInfo = getAgentInfo(externalId);
                compositeAgentInfo.setAgentInfo(agentInfo);
                if (agentInfo == null) {
                    continue;
                }
                //add resource tag to gocd-agent
                enablePendingAgentAndAddResourcesAnd(agentServiceInfo, compositeAgentInfo);
                if (compositeAgentInfo.isAgentIdle() && compositeAgentInfo.getServiceInstance().isRunning()) {
                    LOG.warn("Agent Restart Succeed! {}", compositeAgentInfo);
                    scaleCount++;
                    break;
                }

            }
            while (true);
        }
        // change scale
        int scaleChange = Math.max(
                agentServiceInfo.getMaxInstances() - agentServiceInfo.getScale(),
                agentServiceInfo.getScaleStep() - scaleCount);
        if (scaleChange > 0) {
            LOG.debug("AgentInstance need scale up!!");
            rancherClient.serviceScaleChange(
                    agentServiceInfo.getServiceInfo(),
                    agentServiceInfo.getScale() + scaleChange);
            long timeout = 0;
            do {
                agentServiceInfo.setCompositeAgentInfos(getCompositeAgentInfos(agentServiceInfo.getServiceInfo()));
                sleep(INSTANCE_STATUS_CHECK_INTERVAL);
                timeout += INSTANCE_STATUS_CHECK_INTERVAL;
                if (timeout > AGENT_START_TIME_OUT) {
                    LOG.warn("Timeout when waiting all Agent started!!");
                    break;
                }
            } while (!agentServiceInfo.isAllAgentAvailable());
        }
        addResourcesAndEnablePendingAgent(agentServiceInfo);

    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            LOG.error("Sleep was interrupted");
        }
    }

    private AgentInfo getAgentInfo(String externalId) {
        return goClient.fetchAllAgents().getEmbedded().getAgents().stream().filter(agentInfo -> externalId.startsWith(agentInfo.getHostname())).findFirst().orElse(null);
    }

    @Override
    public void scaleDown(AgentServiceInfo agentServiceInfo) {
        int scaleCount = 0;
        for (CompositeAgentInfo compositeAgentInfo : agentServiceInfo.getCompositeAgentInfos()) {
            //disable idle agent
            if (!compositeAgentInfo.isAgentIdle()) {
                continue;
            }
            if (scaleCount >= agentServiceInfo.getScaleStep()) {
                break;
            }
            if (agentServiceInfo.getMinIdles() >= (agentServiceInfo.getIdle() - scaleCount)) {
                break;
            }

            if (compositeAgentInfo.isAgentIdle()) {
                //disabale Agent
                AgentInfo agentInfo = goClient.disableAgent(compositeAgentInfo.getAgentInfo().getUuid());
                compositeAgentInfo.setAgentInfo(agentInfo);

                //stop instance
                rancherClient.instanceStop(compositeAgentInfo.getServiceInstance());
                //delete instance
                goClient.deleteAgent(compositeAgentInfo.getAgentInfo().getUuid());
                scaleCount++;
            }


        }
    }


    @Override
    public void autoScaleGoCDAgent() {
        for (AgentServiceInfo agentServiceInfo : getAgentServices()) {
            //skip not auto scaled agent
            if(!agentServiceInfo.getConfig().isAutoConfig()){
                continue;
            }

            addResourcesAndEnablePendingAgent(agentServiceInfo);

            deleteDisabledAgentAndStopInstance(agentServiceInfo);


            if (agentServiceInfo.shouldScaleUp()) {
                scaleUp(agentServiceInfo);
            }

            if (agentServiceInfo.shouldScaleDown()) {
                scaleDown(agentServiceInfo);
            }

            //scaleUp/Down
            LOG.info("End GoAgent Scale Manage");
        }
    }
}
