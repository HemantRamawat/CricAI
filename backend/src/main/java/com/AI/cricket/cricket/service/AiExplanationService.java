package com.AI.cricket.cricket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiExplanationService {

    public String explainPrediction(String teamA,
                                    String teamB,
                                    double probA,
                                    double probB,
                                    String winner){

        RestTemplate rest = new RestTemplate();

        String prompt =
                "Match: " + teamA + " vs " + teamB + "\n" +
                        teamA + ": " + probA + "%\n" +
                        teamB + ": " + probB + "%\n\n" +
                        "Winner is: " + winner + "\n\n" +
                        "Explain in 1-2 lines why " + winner + " is more likely to win.";

        Map<String,Object> request = new HashMap<>();

        request.put("model","gemma3:1b");
        request.put("prompt",prompt);
        request.put("stream",false);

        Map response =
                rest.postForObject(
                        "http://localhost:11434/api/generate",
                        request,
                        Map.class
                );

        return (String) response.get("response");
    }

    public String generalChat(String message){

        RestTemplate rest = new RestTemplate();

        Map<String,Object> req = new HashMap<>();
        req.put("model","gemma3:1b");
        req.put("prompt", message);
        req.put("stream", false);

        Map res = rest.postForObject(
                "http://localhost:11434/api/generate",
                req,
                Map.class
        );

        return (String) res.get("response");
    }

    public String detectIntent(String message){

        RestTemplate rest = new RestTemplate();

        String prompt =
                "Classify the user intent into one of these ONLY:\n" +
                        "PREDICT_MATCH\n" +
                        "EXPLAIN_MATCH\n" +
                        "SHOW_STATS\n" +
                        "GENERAL_CHAT\n\n" +
                        "User query: \"" + message + "\"\n\n" +
                        "Return ONLY one word from above.";

        Map<String,Object> req = new HashMap<>();
        req.put("model","gemma3:1b"); // fast model
        req.put("prompt",prompt);
        req.put("stream",false);

        Map res = rest.postForObject(
                "http://localhost:11434/api/generate",
                req,
                Map.class
        );

        String response = (String) res.get("response");

        return response.trim().toUpperCase();
    }
}
