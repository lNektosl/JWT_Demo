package com.example.llmyapper.schema;

import java.util.List;
import java.util.Map;

public class PlayerIntention {
    public static final Map<String, Object> SCHEMA = Map.of(
            "type", "object",
            "properties", Map.of(
                    "intention", Map.of(
                            "type", "string",
                            "enum", List.of(
                                    "TALK",
                                    "EXAMINE",
                                    "MOVE",
                                    "ATTACK",
                                    "TAKE",
                                    "USE",
                                    "OTHER"
                            )
                    ),
                    "activeNPC", Map.of(
                            "type", List.of("string", "null")
                    )
            ),
            "required", List.of("intention", "activeNPC")
    );
}
