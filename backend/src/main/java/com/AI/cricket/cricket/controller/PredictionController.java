package com.AI.cricket.cricket.controller;

import com.AI.cricket.cricket.dto.MatchPredictionDTO;
import com.AI.cricket.cricket.entity.MatchPrediction;
import com.AI.cricket.cricket.service.PredictionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService){
        this.predictionService = predictionService;
    }

    @GetMapping("/predictions")
    public List<MatchPredictionDTO> getPredictions(){
        return predictionService.generatePredictions();
    }

}
