package com.sentientops.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class IncidentRequest {
    @NotBlank private String serviceName;
    @NotBlank private String logs;
    private Double cpuUsage;
    private Double memoryUsage;
    private String severity;
    private String description;
}
