package com.AI.cricket.cricket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MatchPrediction {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String matchName;
   private String teamA;
   private String teamB;

   private String venue;
   private String date;
   private String status;

   private double teamAProb;
   private double teamBProb;

   public MatchPrediction(){}

   public MatchPrediction(String matchName,
                          String teamA,
                          String teamB,
                          String venue,
                          String date,
                          String status,
                          double teamAProb,
                          double teamBProb) {

      this.matchName = matchName;
      this.teamA = teamA;
      this.teamB = teamB;
      this.venue = venue;
      this.date = date;
      this.status = status;
      this.teamAProb = teamAProb;
      this.teamBProb = teamBProb;
   }
}