package com.churninsight.api.service;

import com.churninsight.api.dto.StatsResponseDto;
import com.churninsight.api.model.enums.StatKey;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicLongArray;

@Service
public class StatsService {

    private final long startMillis = System.currentTimeMillis();
    private volatile OffsetDateTime lastRequestAt = null;
    private final AtomicLongArray counters = new AtomicLongArray(StatKey.values().length);

    // Low level - increment all counters
    public void inc(StatKey key) {
        counters.incrementAndGet(key.ordinal());
    }

    public void markRequest(){
        inc(StatKey.TOTAL_REQUESTS);
        lastRequestAt = OffsetDateTime.now();
    }

    public void markSuccess() {
        inc(StatKey.PREDICTIONS_SUCCESS);
    }

    public void markValidationError() {
        inc(StatKey.VALIDATION_ERRORS);
    }

    public void markBadJson() {
        inc(StatKey.BAD_JSON_ERRORS);
    }

    public void markServiceError() {
        inc(StatKey.MODEL_SERVICE_ERRORS);
    }

    public void markInternalError() {
        inc(StatKey.INTERNAL_ERRORS);
    }

    public StatsResponseDto snapshot() {
        long uptimeSeconds = (System.currentTimeMillis() - startMillis) / 1000;

        return new StatsResponseDto(
                uptimeSeconds,
                get(StatKey.TOTAL_REQUESTS),
                get(StatKey.PREDICTIONS_SUCCESS),
                get(StatKey.VALIDATION_ERRORS),
                get(StatKey.BAD_JSON_ERRORS),
                get(StatKey.MODEL_SERVICE_ERRORS),
                get(StatKey.INTERNAL_ERRORS),
                lastRequestAt
        );
    }

    // helper para leitura (mantém o snapshot limpo)
    private long get(StatKey key) {
        return counters.get(key.ordinal());
    }
}
