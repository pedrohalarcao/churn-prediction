package com.churninsight.api.client;

import com.churninsight.api.dto.CustomerInputDto;
import com.churninsight.api.dto.PredictionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MlPredictionClient {

    private final WebClient webClient;

    public PredictionResponseDto predict(CustomerInputDto dto) {

        return webClient.post()
                .uri("/predict")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(PredictionResponseDto.class)
                .block();
    }
}
