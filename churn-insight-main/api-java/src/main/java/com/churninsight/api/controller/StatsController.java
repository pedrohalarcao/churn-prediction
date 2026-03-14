package com.churninsight.api.controller;

import com.churninsight.api.dto.StatsResponseDto;
import com.churninsight.api.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<StatsResponseDto> stats() {
        return ResponseEntity.
        ok(statsService.snapshot());
    }
}
