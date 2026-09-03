package com.example.llmyapper.controller;

import com.example.llmyapper.service.SceneService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/llm")
public class SceneController {
    private final SceneService sceneService;
    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @GetMapping("/story")
    public String generateStory(@RequestParam String prompt){
        return sceneService.getResponse(prompt);
    }

    @GetMapping("/intention")
    public String getIntent(@RequestParam String prompt){
        return sceneService.getIntent(prompt);
    }
@GetMapping("/generate")
    public String generate(@RequestParam String prompt){
        return sceneService.generate(prompt);
    }
}
