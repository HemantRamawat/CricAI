package com.AI.cricket.cricket.engine;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class StatsEngine {

    public double calculateWinRate(List<Map<String,Object>> matches, String team){

        int wins = 0;

        for(Map<String,Object> match : matches){

            String status = (String) match.get("status");

            if(status != null && status.contains(team + " won")){
                wins++;
            }
        }

        if(matches.size() == 0)
            return 0.5;

        return (double) wins / matches.size();
    }
}