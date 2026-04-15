package com.sentientops.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AiAnalysisRequest {
    @NotBlank private String logs;
    private String serviceName;
    private Long incidentId;
}
