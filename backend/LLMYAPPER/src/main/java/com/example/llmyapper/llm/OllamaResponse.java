package com.example.llmyapper.llm;

public record OllamaResponse (
    String model,
    String response,
    boolean done
){}
