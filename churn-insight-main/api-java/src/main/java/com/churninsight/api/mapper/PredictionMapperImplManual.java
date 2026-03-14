package com.churninsight.api.mapper;

import com.churninsight.api.dto.CustomerInputDto;
import com.churninsight.api.dto.PredictionResponseDto;
import com.churninsight.api.model.PredictionModel;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class PredictionMapperImplManual implements PredictionMapper {

    @Override
    public PredictionModel toModel(CustomerInputDto dto) {
        if (dto == null) {
            return null;
        }

        // Create empty model; prediction fields are populated later by the ML client
        return new PredictionModel();
    }

    @Override
    public PredictionResponseDto toDto(PredictionModel model) {
        if (model == null) {
            return null;
        }

        return new PredictionResponseDto(model.getId(), model.getPrediction(), model.getProbability());
    }
}
