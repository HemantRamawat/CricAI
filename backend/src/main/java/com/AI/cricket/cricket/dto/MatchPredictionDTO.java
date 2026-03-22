package com.AI.cricket.cricket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MatchPredictionDTO {

    private Map<String,Object> rawMatchData;

    private String matchName;
    private String teamA;
    private String teamB;

    private String venue;
    private String date;
    private String status;

    private double teamAProb;
    private double teamBProb;
    private String aiExplanation;
}