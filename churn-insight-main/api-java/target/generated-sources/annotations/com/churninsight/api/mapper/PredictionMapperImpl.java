package com.churninsight.api.mapper;

import com.churninsight.api.dto.CustomerInputDto;
import com.churninsight.api.dto.PredictionResponseDto;
import com.churninsight.api.model.PredictionModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-30T19:39:50-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class PredictionMapperImpl implements PredictionMapper {

    @Override
    public PredictionModel toModel(CustomerInputDto dto) {
        if ( dto == null ) {
            return null;
        }

        PredictionModel predictionModel = new PredictionModel();

        return predictionModel;
    }

    @Override
    public PredictionResponseDto toDto(PredictionModel model) {
        if ( model == null ) {
            return null;
        }

        Long id = null;
        String prediction = null;
        double probability = 0.0d;

        id = model.getId();
        prediction = model.getPrediction();
        probability = model.getProbability();

        PredictionResponseDto predictionResponseDto = new PredictionResponseDto( id, prediction, probability );

        return predictionResponseDto;
    }
}
