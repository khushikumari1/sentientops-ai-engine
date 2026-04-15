package com.sentientops.controller;

import com.sentientops.service.ObservabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Tag(name = "Observability", description = "Service health and observability")
public class ObservabilityController {

    private final ObservabilityService observabilityService;

    @GetMapping("/services")
    @Operation(summary = "Get health overview of all services")
    public ResponseEntity<Map<String, Object>> getHealthOverview() {
        return ResponseEntity.ok(observabilityService.getHealthOverview());
    }
}
