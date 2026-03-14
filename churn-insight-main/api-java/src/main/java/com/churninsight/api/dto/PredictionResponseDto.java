package com.churninsight.api.dto;

public record PredictionResponseDto(

        Long id,
        String prediction,
        double probability
) {
}
