package com.sentientops.model.neo4j;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Node("Incident")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IncidentNode {

    @Id @GeneratedValue
    private Long id;

    private Long postgresId;
    private String serviceName;
    private String logs;
    private String severity;
    private Instant createdAt;

    @Relationship(type = "CAUSED_BY", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<RootCauseNode> rootCauses = new HashSet<>();

    @Relationship(type = "RESOLVED_BY", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<FixNode> fixes = new HashSet<>();

    @Relationship(type = "SIMILAR_TO", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<IncidentNode> similarIncidents = new HashSet<>();
}
