package com.sentientops.repository.neo4j;

import com.sentientops.model.neo4j.RootCauseNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RootCauseNodeRepository extends Neo4jRepository<RootCauseNode, Long> {
}
