package com.AI.cricket.cricket.service;

import com.AI.cricket.cricket.agent.tools.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class RealAgentService {

    @Autowired
    private AiExplanationService aiService;

    @Autowired
    private List<Tool> tools;

    public String process(String userMessage, String matchName) {

        String systemPrompt = buildSystemPrompt();

        String context = "";

        if(matchName != null && matchName.contains("vs")){
            context = "Current Match: " + matchName + "\n";
        }

        String conversation =
                systemPrompt +
                        "\n" + context +
                        "\nUser: " + userMessage;

        ObjectMapper mapper = new ObjectMapper();

        if(userMessage.toLowerCase().contains("who will win")
                || userMessage.toLowerCase().contains("prediction")){

            if(matchName != null && matchName.contains("vs")){
                Tool tool = findTool("predict_match");
                // 🔥 Clean match name
                String cleanMatch = matchName.split(",")[0];

                String[] teams = cleanMatch.split("\\s+vs\\s+");

                if(tool != null){
                    return tool.execute(teams[0] + " vs " + teams[1]);
                }
            }
        }
        for(int step = 0; step < 3; step++) {

            String response = aiService.generalChat(conversation);

            System.out.println("LLM RESPONSE:\n" + response);

            try {
                String cleaned = cleanJson(response);

                Map<String, String> map = mapper.readValue(cleaned, Map.class);

                String toolName = map.get("tool");
                String input = map.get("input");

                Tool tool = findTool(toolName);
                if(toolName.equalsIgnoreCase("predict_match")){
                    if(input == null || !input.contains("vs")){

                        if(matchName != null && matchName.contains("vs")){

                            String cleanMatch = matchName.split(",")[0];

                            System.out.println("⚠️ FIXING LLM INPUT → " + cleanMatch);

                            input = cleanMatch;
                        }
                    }
                }
                if(tool == null) return "Tool not found";

                String toolResult = tool.execute(input);

                // ✅ Stop after prediction
                if(toolName.equalsIgnoreCase("predict_match")){
                    double probA = extractProb(toolResult, 0);
                    double probB = extractProb(toolResult, 1);
                    String teamA = input.split(" vs ")[0];
                    String teamB = input.split(" vs ")[1];
                    String winner = probA > probB ? teamA : teamB;
                    String explanation = aiService.explainPrediction(
                            teamA,
                            teamB,
                            extractProb(toolResult, 0),
                            extractProb(toolResult, 1),
                            winner
                    );

                    return toolResult + "\n\n" + explanation;
                }

                // Continue loop
                conversation += "\nTool Result:\n" + toolResult +
                        "\nNow decide next step.";

            } catch (Exception e) {
                // ✅ Not JSON → final answer
                return response;
            }
        }

        return "Could not complete request";
    }

    private String buildSystemPrompt() {
        return """
            You are a cricket AI agent.
            
            Available tools:
            - predict_match
            - get_matches
            
            ⚠️ STRICT RULES (VERY IMPORTANT):
            
            1. predict_match input MUST ALWAYS be:
               "TeamA vs TeamB"
            
            2. NEVER pass single team like "Australia"
            
            3. ALWAYS extract BOTH teams from:
               - User message OR
               - Current Match context
            
            4. If Current Match is provided → ALWAYS use it
            
            5. If you do not follow this → system will fail
            
            ---
            
            Current Match Example:
            Australia vs South Africa
            
            Correct:
            {
              "tool": "predict_match",
              "input": "Australia vs South Africa"
            }
            
            Wrong:
            {
              "tool": "predict_match",
              "input": "Australia"
            }
            
            ---
            
            ONLY return JSON. No explanation.
            """;
    }

    private Tool findTool(String toolName) {

        for(Tool tool : tools){
            if(tool.getName().equalsIgnoreCase(toolName)){
                return tool;
            }
        }

        return null;
    }

    private String cleanJson(String response) {

        if(response == null) return null;

        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if(start != -1 && end != -1){
            return response.substring(start, end + 1);
        }

        return response;
    }

    private double extractProb(String result, int index){

        // Example: "Australia: 55.6%, South Africa: 44.4%"
        String[] parts = result.split(",");

        String value = parts[index].replaceAll("[^0-9.]", "");

        return Double.parseDouble(value);
    }
}
