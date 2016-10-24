package com.thoughtworks.lean.gocd.agentManager.repository;

import com.thoughtworks.lean.gocd.agentManager.domain.AgentManagerConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AgentManagerConfigRepository extends MongoRepository<AgentManagerConfig, Integer> {
    AgentManagerConfig findOneById(int id);
}
