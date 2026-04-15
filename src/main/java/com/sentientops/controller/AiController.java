package com.sentientops.controller;

import com.sentientops.ai.GroqAiService;
import com.sentientops.memory.MemoryService;
import com.sentientops.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Reasoning", description = "AI-powered incident analysis using Groq LLM")
public class AiController {

    private final GroqAiService groqAiService;
    private final MemoryService memoryService;

    @PostMapping("/analyze")
    @Operation(summary = "Analyze incident logs using AI")
    public ResponseEntity<AiAnalysisResponse> analyze(@Valid @RequestBody AiAnalysisRequest request) {
        AiAnalysisResponse response = groqAiService.analyze(request.getLogs(), request.getServiceName());

        // Store results in memory graph if incident ID provided
        if (request.getIncidentId() != null) {
            memoryService.storeRootCause(request.getIncidentId(),
                    response.getRootCause(), "AI_DETECTED", response.getConfidenceScore());
            memoryService.storeFix(request.getIncidentId(),
                    response.getSuggestedFix(), "AI_SUGGESTED", false);
        }

        return ResponseEntity.ok(response);
    }
}
