package com.sentientops.model.neo4j;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@Node("Fix")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FixNode {

    @Id @GeneratedValue
    private Long id;

    private String description;
    private String actionType;
    private boolean automated;
    private int successCount;
}
