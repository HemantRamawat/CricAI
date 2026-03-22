package com.AI.cricket.cricket.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;

@Service
public class DataAgent {

    @Value("${cricapi.key}")
    private String apiKey;

    // 🔥 Toggle
    private static final boolean USE_MOCK = true;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    // ===============================
    // ✅ GET MATCHES
    // ===============================
    public List<Map<String, Object>> getMatches(){

        if(USE_MOCK){
            return getMockMatches();
        }

        String url =
                "https://api.cricapi.com/v1/currentMatches?apikey="
                        + apiKey + "&offset=0";

        RestTemplate restTemplate = new RestTemplate();

        Map<String,Object> response =
                restTemplate.getForObject(url, Map.class);

        if(response == null)
            return Collections.emptyList();

        Object data = response.get("data");

        if(data == null)
            return Collections.emptyList();

        return (List<Map<String,Object>>) data;
    }

    // ===============================
    // ✅ GET RECENT MATCHES
    // ===============================
    public List<Map<String,Object>> getRecentMatches(String team){

        if(USE_MOCK){
            return getMockRecentMatches(team);
        }

        String url =
                "https://api.cricapi.com/v1/matches?apikey="
                        + apiKey + "&offset=0";

        RestTemplate restTemplate = new RestTemplate();

        Map<String,Object> response =
                restTemplate.getForObject(url, Map.class);

        if(response == null)
            return Collections.emptyList();

        List<Map<String,Object>> matches =
                (List<Map<String,Object>>) response.get("data");

        if(matches == null)
            return Collections.emptyList();

        List<Map<String,Object>> teamMatches = new ArrayList<>();

        for(Map<String,Object> match : matches){

            List<String> teams = (List<String>) match.get("teams");

            if(teams != null && teams.contains(team)){
                teamMatches.add(match);
            }

            if(teamMatches.size() >= 5)
                break;
        }

        return teamMatches;
    }

    // ===============================
    // 🔥 MOCK METHODS
    // ===============================

    private List<Map<String,Object>> getMockMatches(){

        try {
            InputStream is = new ClassPathResource("mock/matches.json").getInputStream();

            return objectMapper.readValue(is, List.class);

        } catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Map<String,Object>> getMockRecentMatches(String team){

        // simple mock: alternate win/loss
        List<Map<String,Object>> recent = new ArrayList<>();

        for(int i=0; i<5; i++){

            Map<String,Object> match = new HashMap<>();

            if(i % 2 == 0){
                match.put("status", team + " won");
            } else {
                match.put("status", team + " lost");
            }

            recent.add(match);
        }

        return recent;
    }
}