package com.churninsight.api.error;

import com.churninsight.api.error.ApiFieldError;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String requestId,
        OffsetDateTime timestamp,
        List<ApiFieldError> errors
) {}
