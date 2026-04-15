package com.sentientops.repository.neo4j;

import com.sentientops.model.neo4j.FixNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FixNodeRepository extends Neo4jRepository<FixNode, Long> {
}
