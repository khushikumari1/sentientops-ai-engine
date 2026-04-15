package com.sentientops.controller;

import com.sentientops.model.dto.PredictionResponse;
import com.sentientops.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
@Tag(name = "Predictions", description = "Failure prediction engine")
public class PredictionController {

    private final PredictionService predictionService;

    @GetMapping("/failures")
    @Operation(summary = "Predict potential failures based on historical patterns")
    public ResponseEntity<PredictionResponse> predictFailures() {
        return ResponseEntity.ok(predictionService.predictFailures());
    }
}
