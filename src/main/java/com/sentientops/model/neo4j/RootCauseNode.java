package com.sentientops.model.neo4j;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@Node("RootCause")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RootCauseNode {

    @Id @GeneratedValue
    private Long id;

    private String description;
    private String category;
    private Double confidence;
}
