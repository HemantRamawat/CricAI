package com.AI.cricket.cricket.service;

import com.AI.cricket.cricket.agent.DataAgent;
import com.AI.cricket.cricket.dto.MatchPredictionDTO;
import com.AI.cricket.cricket.service.AiExplanationService;
import com.AI.cricket.cricket.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AgentService {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private DataAgent dataAgent;

    @Autowired
    private AiExplanationService aiService;

    public String process(String message,
                          String matchName,
                          List<Map<String,String>> history){

        String lower = message.toLowerCase();

        // ===============================
        // 🔥 BUILD CONVERSATION CONTEXT
        // ===============================
        StringBuilder context = new StringBuilder();

        if(history != null){
            for(Map<String,String> m : history){
                context.append(m.get("role"))
                        .append(": ")
                        .append(m.get("text"))
                        .append("\n");
            }
        }

        context.append("user: ").append(message).append("\n");

        // ===============================
        // 🔥 RULE-BASED OVERRIDE (FAST)
        // ===============================
//        if(lower.contains("who will win") || lower.contains("predict")){
//
//            if(matchName != null && matchName.contains("vs")){
//
//                String[] teams = extractTeamsFromMatch(matchName);
//
//                MatchPredictionDTO m =
//                        predictionService.predictMatch(teams[0], teams[1]);
//
//                if(m == null) return "Match not found";
//
//                return formatPrediction(m);
//            }
//
//            return "Please select a match first.";
//        }
//
//        // 🔥 Explanation override
//        if(lower.contains("why") || lower.contains("explain")){
//
//            if(matchName != null && matchName.contains("vs")){
//
//                String[] teams = extractTeamsFromMatch(matchName);
//
//                MatchPredictionDTO m =
//                        predictionService.predictMatch(teams[0], teams[1]);
//
//                if(m == null) return "Match not found";
//
//                String prompt =
//                        "Conversation:\n" + context + "\n\n" +
//                                "Match: " + m.getMatchName() + "\n" +
//                                m.getTeamA() + " win probability: " + m.getTeamAProb() + "%\n" +
//                                m.getTeamB() + " win probability: " + m.getTeamBProb() + "%\n\n" +
//                                "Explain clearly in short in simple cricket terms.";
//
//                return aiService.generalChat(prompt);
//            }
//
//            return "Please select a match first.";
//        }

        // ===============================
        // 🔥 LLM INTENT DETECTION
        // ===============================
        String intent = aiService.detectIntent(message);

        switch (intent){

            case "PREDICT_MATCH":

                if(matchName != null && matchName.contains("vs")){

                    String[] teams = extractTeamsFromMatch(matchName);

                    MatchPredictionDTO m =
                            predictionService.predictMatch(teams[0], teams[1]);

                    if(m == null) return "Match not found";

                    return formatPrediction(m);
                }

                return "Please select a match first.";

            case "EXPLAIN_MATCH":

                if(matchName != null && matchName.contains("vs")){

                    String[] teams = extractTeamsFromMatch(matchName);

                    MatchPredictionDTO m =
                            predictionService.predictMatch(teams[0], teams[1]);

                    if(m == null) return "Match not found";

                    String prompt =
                            "Conversation:\n" + context + "\n\n" +
                                    "Match: " + m.getMatchName() + "\n" +
                                    m.getTeamA() + ": " + m.getTeamAProb() + "%\n" +
                                    m.getTeamB() + ": " + m.getTeamBProb() + "%\n\n" +
                                    "Answer the user's latest question clearly.";

                    return aiService.generalChat(prompt);
                }

                return "Please select a match first.";

            case "SHOW_STATS":
                return buildMatchesResponse(dataAgent.getMatches());

            case "GENERAL_CHAT":
            default:
                return aiService.generalChat(context.toString());
        }
    }

    private String[] extractTeamsFromMatch(String matchName){

        String title = matchName.split(",")[0]; // remove extra text

        String[] teams = title.split(" vs ");

        return new String[]{
                teams[0].trim(),
                teams[1].trim()
        };
    }

    private String formatPrediction(MatchPredictionDTO m){

        return "🏏 " + m.getMatchName() + "\n\n" +
                m.getTeamA() + ": " + m.getTeamAProb() + "%\n" +
                m.getTeamB() + ": " + m.getTeamBProb() + "%\n\n" +
                "📍 Venue: " + m.getVenue() + "\n" +
                "📅 Date: " + m.getDate() + "\n" +
                "📊 Status: " + m.getStatus();
    }

    private String buildMatchesResponse(List<Map<String,Object>> matches){

        StringBuilder sb = new StringBuilder("Today's Matches:\n\n");

        for(int i=0; i<Math.min(5, matches.size()); i++){
            sb.append(matches.get(i).get("name")).append("\n");
        }

        return sb.toString();
    }
}