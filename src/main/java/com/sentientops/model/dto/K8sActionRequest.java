package com.sentientops.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class K8sActionRequest {
    @NotBlank private String action; // RESTART_POD, SCALE_DEPLOYMENT
    @NotBlank private String namespace;
    @NotBlank private String resourceName;
    private Integer replicas; // for scaling
}
