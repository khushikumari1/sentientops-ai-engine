package com.sentientops.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentientops.model.dto.AiAnalysisResponse;
import com.sentientops.memory.MemoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroqAiService {

    private final WebClient webClient;
    private final MemoryService memoryService;
    private final ObjectMapper objectMapper;

    private final String apiKey = System.getenv("GROQ_API_KEY");
    private final String apiUrl = "https://api.groq.com/openai/v1/chat/completions";
    private final String model = "llama3-8b-8192";

    public AiAnalysisResponse analyze(String logs, String serviceName) {

        String historicalContext = memoryService.buildContextForAi(serviceName);

        String systemPrompt = """
            You are an expert cloud infrastructure incident analyst. Analyze the provided logs and historical context.
            Respond ONLY in valid JSON with these fields:
            - rootCause: string describing the root cause
            - suggestedFix: string describing the recommended fix
            - confidenceScore: number between 0.0 and 1.0
            - reasoning: string explaining your analysis
            """;

        String userPrompt = String.format("""
            Current incident logs:
            %s

            Historical context:
            %s

            Analyze this incident and provide root cause, suggested fix, confidence score, and reasoning.
            """, truncate(logs, 2000), truncate(historicalContext, 2000));

        try {

            Map<String, Object> requestBody = new HashMap<>();

            requestBody.put("model", model);

            List<Map<String, String>> messages = new ArrayList<>();

            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt);

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userPrompt);

            messages.add(systemMsg);
            messages.add(userMsg);

            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 1024);

            String responseBody = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseGroqResponse(responseBody);

        } catch (Exception e) {
            log.error("Groq API call failed: {}", e.getMessage());
            return AiAnalysisResponse.builder()
                    .rootCause("Analysis unavailable - AI service error")
                    .suggestedFix("Manual investigation required")
                    .confidenceScore(0.0)
                    .reasoning("Error: " + e.getMessage())
                    .build();
        }
    }

    private AiAnalysisResponse parseGroqResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            content = content.trim();
            if (content.startsWith("```json")) content = content.substring(7);
            if (content.startsWith("```")) content = content.substring(3);
            if (content.endsWith("```")) content = content.substring(0, content.length() - 3);
            content = content.trim();

            JsonNode parsed = objectMapper.readTree(content);

            return AiAnalysisResponse.builder()
                    .rootCause(parsed.path("rootCause").asText("Unknown"))
                    .suggestedFix(parsed.path("suggestedFix").asText("Manual investigation needed"))
                    .confidenceScore(parsed.path("confidenceScore").asDouble(0.5))
                    .reasoning(parsed.path("reasoning").asText(""))
                    .build();

        } catch (Exception e) {
            log.warn("Failed to parse Groq response: {}", e.getMessage());
            return AiAnalysisResponse.builder()
                    .rootCause("Parse error")
                    .suggestedFix("Review raw AI output")
                    .confidenceScore(0.3)
                    .reasoning(responseBody)
                    .build();
        }
    }

    private String truncate(String text, int maxLen) {
        return text != null && text.length() > maxLen ? text.substring(0, maxLen) + "..." : text;
    }
}