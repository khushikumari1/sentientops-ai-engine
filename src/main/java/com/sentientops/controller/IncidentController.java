package com.sentientops.controller;

import com.sentientops.model.dto.IncidentRequest;
import com.sentientops.model.entity.Incident;
import com.sentientops.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@Tag(name = "Incidents", description = "Incident ingestion and retrieval")
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    @Operation(summary = "Ingest a new incident")
    public ResponseEntity<Incident> ingest(@Valid @RequestBody IncidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentService.ingest(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get incident by ID")
    public ResponseEntity<Incident> getById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all incidents")
    public ResponseEntity<List<Incident>> getAll() {
        return ResponseEntity.ok(incidentService.getAll());
    }

    @GetMapping("/service/{serviceName}")
    @Operation(summary = "Get incidents by service name")
    public ResponseEntity<List<Incident>> getByService(@PathVariable String serviceName) {
        return ResponseEntity.ok(incidentService.getByService(serviceName));
    }

    @GetMapping("/unresolved")
    @Operation(summary = "Get all unresolved incidents")
    public ResponseEntity<List<Incident>> getUnresolved() {
        return ResponseEntity.ok(incidentService.getUnresolved());
    }
}
