package com.AI.cricket.cricket.service;

import com.AI.cricket.cricket.agent.DataAgent;
import com.AI.cricket.cricket.dto.MatchPredictionDTO;
import com.AI.cricket.cricket.engine.EloEngine;
import com.AI.cricket.cricket.engine.StatsEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PredictionService {

    @Autowired
    private DataAgent dataAgent;

    @Autowired
    private EloEngine eloEngine;

    @Autowired
    private StatsEngine statsEngine;

    @Autowired
    private AiExplanationService aiExplanationService;

    private static final List<String> MAJOR_TEAMS = List.of(
            "India",
            "Pakistan",
            "Australia",
            "England",
            "South Africa",
            "New Zealand",
            "Sri Lanka",
            "Bangladesh"
    );

    public List<MatchPredictionDTO> generatePredictions(){

        List<Map<String,Object>> matches = dataAgent.getMatches();

        if(matches == null || matches.isEmpty()){
            return Collections.emptyList();
        }

        List<MatchPredictionDTO> results = new ArrayList<>();

        // 🔥 Cache to avoid multiple API calls
        Map<String, List<Map<String,Object>>> recentMatchesCache = new HashMap<>();

        for(Map<String,Object> match : matches){

            String name = (String) match.get("name");

            if(name == null || !name.contains("vs"))
                continue;

            String title = name;
            String matchTitle = name.split(",")[0];
            String[] teams = matchTitle.split(" vs ");

            if(teams.length != 2)
                continue;

            String teamA = teams[0].trim();
            String teamB = teams[1].trim();

            // Filter only major teams
            if(!MAJOR_TEAMS.contains(teamA) && !MAJOR_TEAMS.contains(teamB)){
                continue;
            }

            String venue = (String) match.get("venue");
            String date = (String) match.get("date");
            String status = (String) match.get("status");

            // 🔥 ELO
            int ratingA = eloEngine.getRating(teamA);
            int ratingB = eloEngine.getRating(teamB);

            double eloProbA =
                    1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));

            double eloProbB = 1.0 - eloProbA;

            // 🔥 Fetch recent matches using cache
            if(!recentMatchesCache.containsKey(teamA)){
                recentMatchesCache.put(teamA, dataAgent.getRecentMatches(teamA));
            }

            if(!recentMatchesCache.containsKey(teamB)){
                recentMatchesCache.put(teamB, dataAgent.getRecentMatches(teamB));
            }

            List<Map<String,Object>> teamAMatches = recentMatchesCache.get(teamA);
            List<Map<String,Object>> teamBMatches = recentMatchesCache.get(teamB);

            double formA = statsEngine.calculateWinRate(teamAMatches, teamA);
            double formB = statsEngine.calculateWinRate(teamBMatches, teamB);

            // 🔥 Combine ELO + Form
            double scoreA = eloProbA * 0.7 + formA * 0.3;
            double scoreB = eloProbB * 0.7 + formB * 0.3;

            double total = scoreA + scoreB;

            double teamAProb = Math.round((scoreA / total) * 1000) / 10.0;
            double teamBProb = Math.round((scoreB / total) * 1000) / 10.0;

            MatchPredictionDTO prediction = new MatchPredictionDTO();

            prediction.setRawMatchData(match);
            prediction.setMatchName(title);
            prediction.setTeamA(teamA);
            prediction.setTeamB(teamB);
            prediction.setVenue(venue);
            prediction.setDate(date);
            prediction.setStatus(status);
            prediction.setTeamAProb(teamAProb);
            prediction.setTeamBProb(teamBProb);

            results.add(prediction);
        }

        return results;
    }

    public MatchPredictionDTO predictMatch(String teamA, String teamB){

        List<Map<String,Object>> matches = dataAgent.getMatches();

        if(matches == null || matches.isEmpty()){
            return null;
        }

        Map<String,Object> selectedMatch = null;

        for(Map<String,Object> match : matches){

            String name = (String) match.get("name");

            if(name == null) continue;

            if(name.toLowerCase().contains(teamA.toLowerCase()) &&
                    name.toLowerCase().contains(teamB.toLowerCase())){

                selectedMatch = match;
                break;
            }
        }

        if(selectedMatch == null){
            return null;
        }

        String matchTitle = ((String) selectedMatch.get("name")).split(",")[0];
        String[] teams = matchTitle.split(" vs ");

        String tA = teams[0].trim();
        String tB = teams[1].trim();

        String venue = (String) selectedMatch.get("venue");
        String date = (String) selectedMatch.get("date");
        String status = (String) selectedMatch.get("status");

        // 🔥 ELO
        int ratingA = eloEngine.getRating(tA);
        int ratingB = eloEngine.getRating(tB);

        double eloProbA =
                1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));

        double eloProbB = 1.0 - eloProbA;

        // 🔥 Form
        List<Map<String,Object>> teamAMatches = dataAgent.getRecentMatches(tA);
        List<Map<String,Object>> teamBMatches = dataAgent.getRecentMatches(tB);

        double formA = statsEngine.calculateWinRate(teamAMatches, tA);
        double formB = statsEngine.calculateWinRate(teamBMatches, tB);

        double scoreA = eloProbA * 0.7 + formA * 0.3;
        double scoreB = eloProbB * 0.7 + formB * 0.3;

        double total = scoreA + scoreB;

        double teamAProb = Math.round((scoreA / total) * 1000) / 10.0;
        double teamBProb = Math.round((scoreB / total) * 1000) / 10.0;

        MatchPredictionDTO prediction = new MatchPredictionDTO();

        prediction.setMatchName((String) selectedMatch.get("name"));
        prediction.setTeamA(tA);
        prediction.setTeamB(tB);
        prediction.setVenue(venue);
        prediction.setDate(date);
        prediction.setStatus(status);
        prediction.setTeamAProb(teamAProb);
        prediction.setTeamBProb(teamBProb);

        return prediction;
    }

}