package com.thoughtworks.lean.gocd.agentManager.controller;

import com.thoughtworks.lean.gocd.agentManager.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by yongliuli on 10/24/16.
 */
@RequestMapping("/ui/gocd-agent")
@Controller
public class AgentUIController {

    @Autowired
    AgentService agentService;

    @RequestMapping("hello")
    public ModelAndView hello() {
        return new ModelAndView("agent/hello");
    }

    @RequestMapping("list")
    public ModelAndView list() {
        return new ModelAndView("agent/agents", "agents", agentService.getAgentServices());
    }


    @RequestMapping("env")
    public ModelAndView env() {
        return new ModelAndView("agent/env", "env", agentService.getAgentManagerConfig());
    }

}
