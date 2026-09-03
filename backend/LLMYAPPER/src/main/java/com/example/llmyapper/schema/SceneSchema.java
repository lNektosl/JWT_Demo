package com.example.llmyapper.schema;

import java.util.List;
import java.util.Map;

public class SceneSchema {
    public static final Map<String, Object> SCHEMA = Map.of(
            "type", "object",
            "properties", Map.of(
                    "description", Map.of(
                            "type", "string"
                    ),
                    "activeNPC", Map.of(
                            "type", List.of("string", "null")
                    ),
                    "NPCIntent", Map.of("type",List.of("string","null")
                    )
            ),
            "required", List.of(
                    "description",
                    "activeNPC",
                    "NPCIntent"
            )
    );
}
