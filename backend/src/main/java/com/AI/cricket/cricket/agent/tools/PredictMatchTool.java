package com.AI.cricket.cricket.agent.tools;

import com.AI.cricket.cricket.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PredictMatchTool implements Tool {

    @Autowired
    private PredictionService predictionService;

    @Override
    public String getName() {
        return "predict_match";
    }

    @Override
    public String getDescription() {
        return "Predict match winner between two teams. Input format: teamA vs teamB";
    }

    @Override
    public String execute(String input) {

        if(input == null) return "Invalid input";

        // 🔥 Clean input
        input = input.toLowerCase()
                .replace("today", "")
                .replace("match", "")
                .replace("?", "")
                .trim();
        input = input.split(",")[0];
        // Normalize "vs"
        input = input.replaceAll("\\s+vs\\s+", " vs ");

        String[] teams = input.split(" vs ");

        if(teams.length < 2) return "Invalid input";

        String teamA = normalizeTeam(teams[0].trim());
        String teamB = normalizeTeam(teams[1].trim());

        var result = predictionService.predictMatch(teamA, teamB);

        if(result == null) return "Match not found";

        return result.getTeamA() + ": " + result.getTeamAProb() + "%, "
                + result.getTeamB() + ": " + result.getTeamBProb() + "%";
    }

    private String normalizeTeam(String team){

        switch(team.toLowerCase()){
            case "ban": return "Bangladesh";
            case "nz": return "New Zealand";
            case "ind": return "India";
            case "aus": return "Australia";
            case "pak": return "Pakistan";
            default: return team;
        }
    }
}
