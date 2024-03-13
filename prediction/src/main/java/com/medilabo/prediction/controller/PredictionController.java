package com.medilabo.prediction.controller;

import com.medilabo.prediction.service.PredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PredictionController {

    @Autowired
    PredictionService predictionService;

    private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);

    /**
     * This method receives requests for risk prediction of a certain patient having diabetes,
     * and returns a description of the risk level.
     * @param id The id of the patient
     * @return A ResponseEntity with a String in its body
     */
    @GetMapping("get")
    public ResponseEntity getPrediction(int id) {

        logger.info("prediction request for id " + id);
        return ResponseEntity.ok(predictionService.getPrediction(id));

    }

}
