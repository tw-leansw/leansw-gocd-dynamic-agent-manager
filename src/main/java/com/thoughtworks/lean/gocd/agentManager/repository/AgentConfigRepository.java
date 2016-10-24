package com.thoughtworks.lean.gocd.agentManager.repository;

import com.thoughtworks.lean.gocd.agentManager.domain.AgentConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AgentConfigRepository extends MongoRepository<AgentConfig, Long> {
    AgentConfig findByName(String name);
}
