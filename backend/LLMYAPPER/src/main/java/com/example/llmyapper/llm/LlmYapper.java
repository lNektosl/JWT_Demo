package com.example.llmyapper.llm;

import com.example.llmyapper.schema.PlayerIntention;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Objects;

@Service
public class LlmYapper {
    private final RestClient restClient;
    private String model = "llama3.2:3b";

    public LlmYapper() {
        this.restClient = RestClient.builder()
                .baseUrl("http://100.93.56.65:11434")
                .build();
    }

    public String ask(String SystemPrompt, String prompt, Map<String, Object> schema) {
        OllamaResponse response = restClient.post()
                .uri("/api/generate")
                .body(Map.of("model",model,
                        "system",SystemPrompt,
                        "prompt",prompt,
                        "format", schema,
                        "stream",false))
                .retrieve()
                .body(OllamaResponse.class);
        return response.response();
    }
}
