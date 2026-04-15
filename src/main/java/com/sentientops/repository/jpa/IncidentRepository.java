package com.sentientops.repository.jpa;

import com.sentientops.model.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByServiceNameOrderByCreatedAtDesc(String serviceName);
    List<Incident> findByResolvedFalseOrderByCreatedAtDesc();
    List<Incident> findByCreatedAtBetween(Instant start, Instant end);

    @Query("SELECT i.serviceName, COUNT(i) FROM Incident i GROUP BY i.serviceName ORDER BY COUNT(i) DESC")
    List<Object[]> countByServiceName();

    @Query("SELECT i.serviceName, COUNT(i) FROM Incident i WHERE i.createdAt > :since GROUP BY i.serviceName HAVING COUNT(i) > :threshold ORDER BY COUNT(i) DESC")
    List<Object[]> findRecurringFailures(Instant since, long threshold);
}
