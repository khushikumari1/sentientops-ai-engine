package com.sentientops.service;

import com.sentientops.exception.ApiException;
import com.sentientops.model.dto.IncidentRequest;
import com.sentientops.model.entity.Incident;
import com.sentientops.model.neo4j.IncidentNode;
import com.sentientops.repository.jpa.IncidentRepository;
import com.sentientops.repository.neo4j.IncidentNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentNodeRepository incidentNodeRepository;

    public Incident ingest(IncidentRequest request) {
        Incident incident = Incident.builder()
                .serviceName(request.getServiceName())
                .logs(request.getLogs())
                .cpuUsage(request.getCpuUsage())
                .memoryUsage(request.getMemoryUsage())
                .severity(parseSeverity(request.getSeverity()))
                .description(request.getDescription())
                .resolved(false)
                .build();
        incident = incidentRepository.save(incident);

        // Store in Neo4j graph
        IncidentNode node = IncidentNode.builder()
                .postgresId(incident.getId())
                .serviceName(incident.getServiceName())
                .logs(incident.getLogs())
                .severity(incident.getSeverity().name())
                .createdAt(incident.getCreatedAt())
                .build();
        incidentNodeRepository.save(node);

        // Link similar incidents
        try {
            incidentNodeRepository.linkSimilarIncidents(incident.getId());
        } catch (Exception e) {
            log.warn("Failed to link similar incidents: {}", e.getMessage());
        }

        log.info("Ingested incident {} for service {}", incident.getId(), incident.getServiceName());
        return incident;
    }

    public Incident getById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Incident not found: " + id));
    }

    public List<Incident> getByService(String serviceName) {
        return incidentRepository.findByServiceNameOrderByCreatedAtDesc(serviceName);
    }

    public List<Incident> getUnresolved() {
        return incidentRepository.findByResolvedFalseOrderByCreatedAtDesc();
    }

    public List<Incident> getAll() {
        return incidentRepository.findAll();
    }

    private Incident.Severity parseSeverity(String severity) {
        if (severity == null) return Incident.Severity.MEDIUM;
        try {
            return Incident.Severity.valueOf(severity.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Incident.Severity.MEDIUM;
        }
    }
}
