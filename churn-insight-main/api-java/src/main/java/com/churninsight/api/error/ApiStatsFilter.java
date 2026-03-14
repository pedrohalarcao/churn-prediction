package com.churninsight.api.error;

import com.churninsight.api.service.StatsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 10) // roda perto do final, mas antes de terminar a chain
public class ApiStatsFilter extends OncePerRequestFilter {

    private final StatsService statsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // só API
        if (!path.startsWith("/api")) return true;

        // não deixar /api/stats contaminar as métricas
        if (path.equals("/api/stats")) return true;

        // se você quiser também excluir docs:
        if (path.startsWith("/api-docs") || path.startsWith("/swagger-ui")) return true;

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Conta a request mesmo se falhar em validação/JSON (porque isso acontece dentro da chain)
        statsService.markRequest();

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Conta sucesso do predict sem acoplar controller
            String path = request.getRequestURI();
            if (path.equals("/api/predict") && response.getStatus() >= 200 && response.getStatus() < 300) {
                statsService.markSuccess();
            }
        }
    }
}
