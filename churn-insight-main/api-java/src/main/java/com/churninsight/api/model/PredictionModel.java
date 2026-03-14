package com.churninsight.api.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionModel {

    private Long id;
    private  String prediction;
    private  double probability;
}
