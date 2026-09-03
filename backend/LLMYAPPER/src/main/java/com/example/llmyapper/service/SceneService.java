package com.example.llmyapper.service;

import com.example.llmyapper.intention.Intention;
import com.example.llmyapper.llm.LlmYapper;
import com.example.llmyapper.response.IntentResponse;
import com.example.llmyapper.schema.PlayerIntention;
import com.example.llmyapper.schema.SceneSchema;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SceneService {
private final LlmYapper llmYapper;
private List<String> history = new ArrayList<>();

    public SceneService(LlmYapper llmYapper) {
        this.llmYapper = llmYapper;
    }

    public String getIntent(String prompt){

        String system ="You are an RPG intention analyzer.\n" +
                "\n" +
                "Analyze the player's message and determine what action they intend to perform.\n" +
                "\n" +
                "Return only valid JSON.\n" +
                "Do not generate story.\n" +
                "Do not invent world state.\n" +
                "\n" +
                "Possible intentions:\n"+
                Arrays.stream(Intention.values())
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));

        return llmYapper.ask(system, prompt, PlayerIntention.SCHEMA);
    }
    public String getResponse(String prompt){
        String system ="You are the narrator of an RPG world.\n" +
                "Describe what happens in the game based only on the provided world state and player action.\n" +
                "Stay consistent with the provided facts.\n" +
                "Write immersive but concise narration." +
                "Possible intentions:\n" +
                Arrays.stream(Intention.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        return llmYapper.ask(system, prompt, SceneSchema.SCHEMA);
    }

    public String generate(String prompt){
        if(history.size()==5){
            history.removeFirst();
        }
        history.add(prompt);
        String intent = getIntent(prompt);
        ObjectMapper  mapper = new ObjectMapper();
        IntentResponse response = mapper.readValue(intent, IntentResponse.class);
        Intention intention = Intention.valueOf(response.intention);

        if(intention.requiresRoll()){
            prompt = prompt + "Say 'Rolled' in the beginning.";
        }
        else {
            prompt = prompt + "Say 'Don' in the beginning.";
        }

        return getResponse(prompt);
    }
}
