package com.sentientops.model.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PredictionResponse {
    private List<FailurePrediction> predictions;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class FailurePrediction {
        private String serviceName;
        private String pattern;
        private int occurrenceCount;
        private Double riskScore;
        private String recommendation;
    }
}
