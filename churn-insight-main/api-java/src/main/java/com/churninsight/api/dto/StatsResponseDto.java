package com.churninsight.api.dto;

import java.time.OffsetDateTime;

public record StatsResponseDto(
        long uptimeSeconds,
        long totalRequests,
        long predictionsSuccess,
        long validationErrors,
        long badJsonErrors,
        long modelServiceErrors,
        long internalErrors,
        OffsetDateTime lastRequestAt
) {}
