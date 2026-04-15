package com.sentientops.controller;

import com.sentientops.k8s.KubernetesActionService;
import com.sentientops.model.dto.K8sActionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
@Tag(name = "Kubernetes Actions", description = "Execute Kubernetes recovery actions")
public class K8sActionController {

    private final KubernetesActionService k8sService;

    @PostMapping("/execute")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Execute a Kubernetes action (Admin only)")
    public ResponseEntity<Map<String, Object>> execute(@Valid @RequestBody K8sActionRequest request) {
        return ResponseEntity.ok(k8sService.executeAction(request));
    }
}
