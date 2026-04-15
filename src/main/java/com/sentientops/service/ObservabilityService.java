package com.sentientops.service;

import com.sentientops.repository.jpa.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObservabilityService {

    private final IncidentRepository incidentRepository;
    private final PredictionService predictionService;

    public Map<String, Object> getHealthOverview() {
        var unresolved = incidentRepository.findByResolvedFalseOrderByCreatedAtDesc();
        var serviceCounts = incidentRepository.countByServiceName();
        var predictions = predictionService.predictFailures();

        List<Map<String, Object>> serviceStatuses = serviceCounts.stream()
                .map(row -> {
                    String name = (String) row[0];
                    long count = (Long) row[1];
                    long unresolvedCount = unresolved.stream()
                            .filter(i -> i.getServiceName().equals(name)).count();
                    String status = unresolvedCount > 0 ? "DEGRADED" : "HEALTHY";
                    return Map.<String, Object>of(
                            "serviceName", name,
                            "totalIncidents", count,
                            "unresolvedIncidents", unresolvedCount,
                            "status", status
                    );
                })
                .collect(Collectors.toList());

        return Map.of(
                "services", serviceStatuses,
                "totalUnresolved", unresolved.size(),
                "predictedRisks", predictions.getPredictions(),
                "timestamp", Instant.now().toString()
        );
    }
}
