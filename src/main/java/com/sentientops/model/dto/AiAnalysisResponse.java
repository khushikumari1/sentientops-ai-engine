package com.sentientops.model.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AiAnalysisResponse {
    private String rootCause;
    private String suggestedFix;
    private Double confidenceScore;
    private String reasoning;
}
