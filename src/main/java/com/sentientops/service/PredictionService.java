package com.sentientops.service;

import com.sentientops.model.dto.PredictionResponse;
import com.sentientops.model.dto.PredictionResponse.FailurePrediction;
import com.sentientops.repository.jpa.IncidentRepository;
import com.sentientops.repository.neo4j.IncidentNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredictionService {

    private final IncidentRepository incidentRepository;
    private final IncidentNodeRepository incidentNodeRepository;

    public PredictionResponse predictFailures() {
        Instant since = Instant.now().minus(7, ChronoUnit.DAYS);
        List<Object[]> recurring = incidentRepository.findRecurringFailures(since, 2);

        List<FailurePrediction> predictions = recurring.stream()
                .map(row -> {
                    String serviceName = (String) row[0];
                    long count = (Long) row[1];
                    double riskScore = Math.min(1.0, count / 10.0);

                    return FailurePrediction.builder()
                            .serviceName(serviceName)
                            .pattern("Recurring failure - " + count + " incidents in 7 days")
                            .occurrenceCount((int) count)
                            .riskScore(riskScore)
                            .recommendation(generateRecommendation(serviceName, count))
                            .build();
                })
                .collect(Collectors.toList());

        return PredictionResponse.builder().predictions(predictions).build();
    }

    private String generateRecommendation(String serviceName, long count) {
        if (count >= 5) return "Critical: Immediate investigation required for " + serviceName + ". Consider scaling or restarting.";
        if (count >= 3) return "Warning: " + serviceName + " showing degradation pattern. Review recent changes.";
        return "Monitor: " + serviceName + " has recurring issues. Track for escalation.";
    }
}
