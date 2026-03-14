package com.churninsight.api.mapper;

import com.churninsight.api.dto.CustomerInputDto;
import com.churninsight.api.dto.PredictionResponseDto;
import com.churninsight.api.model.PredictionModel;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface PredictionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prediction", ignore = true)
    @Mapping(target = "probability", ignore = true)
    PredictionModel toModel(CustomerInputDto dto);
    PredictionResponseDto toDto(PredictionModel model);
}
