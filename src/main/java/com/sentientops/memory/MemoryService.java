package com.sentientops.memory;

import com.sentientops.model.neo4j.*;
import com.sentientops.repository.neo4j.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoryService {

    private final IncidentNodeRepository incidentNodeRepository;
    private final RootCauseNodeRepository rootCauseNodeRepository;
    private final FixNodeRepository fixNodeRepository;

    public List<IncidentNode> findSimilarIncidents(String serviceName) {
        try {
            return incidentNodeRepository.findSimilarIncidentsWithContext(serviceName);
        } catch (Exception e) {
            log.error("Neo4j error, skipping memory layer: {}", e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public void storeRootCause(Long incidentPostgresId, String description, String category, Double confidence) {
        var incidents = incidentNodeRepository.findByServiceName(
                getServiceNameByPostgresId(incidentPostgresId));

        incidents.stream()
                .filter(i -> i.getPostgresId().equals(incidentPostgresId))
                .findFirst()
                .ifPresent(incident -> {
                    RootCauseNode rootCause = RootCauseNode.builder()
                            .description(description)
                            .category(category)
                            .confidence(confidence)
                            .build();
                    rootCause = rootCauseNodeRepository.save(rootCause);
                    incident.getRootCauses().add(rootCause);
                    incidentNodeRepository.save(incident);
                    log.info("Stored root cause for incident {}", incidentPostgresId);
                });
    }

    public void storeFix(Long incidentPostgresId, String description, String actionType, boolean automated) {
        var incidents = incidentNodeRepository.findAll();
        incidents.stream()
                .filter(i -> i.getPostgresId() != null && i.getPostgresId().equals(incidentPostgresId))
                .findFirst()
                .ifPresent(incident -> {
                    FixNode fix = FixNode.builder()
                            .description(description)
                            .actionType(actionType)
                            .automated(automated)
                            .successCount(1)
                            .build();
                    fix = fixNodeRepository.save(fix);
                    incident.getFixes().add(fix);
                    incidentNodeRepository.save(incident);
                    log.info("Stored fix for incident {}", incidentPostgresId);
                });
    }

    public String buildContextForAi(String serviceName) {
        List<IncidentNode> similar = findSimilarIncidents(serviceName);
        if (similar.isEmpty()) return "No historical incidents found for this service.";

        StringBuilder ctx = new StringBuilder("Historical incidents for " + serviceName + ":\n");
        for (IncidentNode node : similar) {
            ctx.append("- Incident (severity: ").append(node.getSeverity())
               .append("): ").append(truncate(node.getLogs(), 200)).append("\n");
            for (RootCauseNode rc : node.getRootCauses()) {
                ctx.append("  Root cause: ").append(rc.getDescription())
                   .append(" (confidence: ").append(rc.getConfidence()).append(")\n");
            }
            for (FixNode fix : node.getFixes()) {
                ctx.append("  Fix: ").append(fix.getDescription())
                   .append(" (automated: ").append(fix.isAutomated()).append(")\n");
            }
        }
        return ctx.toString();
    }

    private String getServiceNameByPostgresId(Long postgresId) {
        return incidentNodeRepository.findAll().stream()
                .filter(i -> i.getPostgresId() != null && i.getPostgresId().equals(postgresId))
                .map(IncidentNode::getServiceName)
                .findFirst().orElse("");
    }

    private String truncate(String text, int maxLen) {
        return text != null && text.length() > maxLen ? text.substring(0, maxLen) + "..." : text;
    }
}
