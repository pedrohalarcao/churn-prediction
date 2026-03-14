package com.churninsight.api.service;

import com.churninsight.api.client.MlPredictionClient;
import com.churninsight.api.dto.CustomerInputDto;
import com.churninsight.api.dto.PredictionResponseDto;
import com.churninsight.api.mapper.PredictionMapper;
import com.churninsight.api.model.PredictionModel;
import com.churninsight.api.util.PredictionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionMapper predictionMapper;
    private final MlPredictionClient mlPredictionClient;

    private long counter;

    {
        counter = 1L;
    }

    public PredictionResponseDto predict(CustomerInputDto dto) {

        var ml = mlPredictionClient.predict(dto);
        var model = PredictionModel.builder()
                .id(counter++)
                .prediction(ml.prediction())
                .probability(ml.probability())
                .build();

        return predictionMapper.toDto(model);
    }
}
