package com.thoughtworks.lean.gocd.agentManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by yongliuli on 10/24/16.
 */
@RequestMapping("/ui/agent")
@Controller
public class AgentUIController {
    @RequestMapping("hello")
    public ModelAndView hello() {
        return new ModelAndView("agent/hello");
    }
}
