package com.AI.cricket.cricket.engine;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EloEngine {

    private static final Map<String,Integer> RATINGS = new HashMap<>();

    static {
        RATINGS.put("India",1800);
        RATINGS.put("Australia",1780);
        RATINGS.put("England",1760);
        RATINGS.put("New Zealand",1740);
        RATINGS.put("South Africa",1720);
        RATINGS.put("Pakistan",1700);
        RATINGS.put("Sri Lanka",1650);
        RATINGS.put("Bangladesh",1600);
    }

    public int getRating(String team){
        return RATINGS.getOrDefault(team,1500);
    }
}