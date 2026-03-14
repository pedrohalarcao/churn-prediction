package com.churninsight.api.controller;

import com.churninsight.api.dto.CustomerInputDto;
import com.churninsight.api.dto.PredictionResponseDto;
import com.churninsight.api.service.PredictionService;
import com.churninsight.api.service.StatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;
    private final StatsService statsService;

    @PostMapping("/predict")
    public ResponseEntity<PredictionResponseDto> predict(
            @Valid @RequestBody
            CustomerInputDto input
            ) {

//        statsService.markRequest();

        var prediction = predictionService.predict(input);

//        statsService.markSuccess();

        return ResponseEntity.ok(prediction);
    }
}
