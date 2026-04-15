package com.sentientops.repository.neo4j;

import com.sentientops.model.neo4j.IncidentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;

public interface IncidentNodeRepository extends Neo4jRepository<IncidentNode, Long> {

    List<IncidentNode> findByServiceName(String serviceName);

    @Query("MATCH (i:Incident) WHERE i.serviceName = $serviceName " +
           "OPTIONAL MATCH (i)-[:CAUSED_BY]->(rc:RootCause) " +
           "OPTIONAL MATCH (i)-[:RESOLVED_BY]->(f:Fix) " +
           "RETURN i, collect(rc) as rootCauses, collect(f) as fixes " +
           "ORDER BY i.createdAt DESC LIMIT 10")
    List<IncidentNode> findSimilarIncidentsWithContext(String serviceName);

    @Query("MATCH (i:Incident) " +
           "WITH i.serviceName AS service, count(i) AS cnt " +
           "WHERE cnt > $threshold " +
           "RETURN service, cnt ORDER BY cnt DESC")
    List<Object[]> findRecurringPatterns(int threshold);

    @Query("MATCH (i1:Incident), (i2:Incident) " +
           "WHERE i1.postgresId = $incidentId AND i2.serviceName = i1.serviceName " +
           "AND i1 <> i2 " +
           "MERGE (i1)-[:SIMILAR_TO]->(i2)")
    void linkSimilarIncidents(Long incidentId);
}
