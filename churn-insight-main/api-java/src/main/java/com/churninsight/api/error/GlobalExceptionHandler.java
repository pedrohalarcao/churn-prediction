package com.churninsight.api.error;

import com.churninsight.api.service.StatsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final StatsService statsService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {

        if (isApiRequest(req)) {
            statsService.markValidationError();
        }

        List<ApiFieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .toList();

        log.info("Validation error: {} field(s) invalid", fieldErrors.size());

        ApiErrorResponse body = new ApiErrorResponse(
                "https://api.local/errors/validation",
                "Validation error",
                400,
                "Some fields are invalid.",
                req.getRequestURI(),
                MDC.get(RequestIdFilter.MDC_KEY),
                OffsetDateTime.now(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest req) {

        if (isApiRequest(req)) {
            statsService.markBadJson();
        }

        log.info("Malformed JSON / invalid enum: {}", ex.getMessage());

        ApiErrorResponse body = new ApiErrorResponse(
                "https://api.local/errors/bad-json",
                "Malformed JSON",
                400,
                "Request body is invalid JSON or contains an invalid enum value.",
                req.getRequestURI(),
                MDC.get(RequestIdFilter.MDC_KEY),
                OffsetDateTime.now(),
                List.of()
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ModelServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleModel(ModelServiceException ex, HttpServletRequest req) {

        if (isApiRequest(req)) {
            statsService.markServiceError();
        }

        log.warn("Model service error: status={} message={}", ex.getHttpStatus(), ex.getMessage());

        ApiErrorResponse body = new ApiErrorResponse(
                "https://api.local/errors/model-service",
                "Model service error",
                ex.getHttpStatus().value(),
                ex.getMessage(),
                req.getRequestURI(),
                MDC.get(RequestIdFilter.MDC_KEY),
                OffsetDateTime.now(),
                List.of()
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResource(NoResourceFoundException ex, HttpServletRequest req) {

        // favicon, robots.txt, etc (não é erro interno)
        log.debug("Static resource not found: {}", req.getRequestURI());

        ApiErrorResponse body = new ApiErrorResponse(
                "https://api.local/errors/not-found",
                "Not Found",
                404,
                "Resource not found.",
                req.getRequestURI(),
                MDC.get(RequestIdFilter.MDC_KEY),
                OffsetDateTime.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {

        if (isApiRequest(req)) {
            statsService.markInternalError();
        }

        log.error("Unexpected error", ex);

        ApiErrorResponse body = new ApiErrorResponse(
                "https://api.local/errors/internal",
                "Internal error",
                500,
                "Unexpected error.",
                req.getRequestURI(),
                MDC.get(RequestIdFilter.MDC_KEY),
                OffsetDateTime.now(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private boolean isApiRequest(HttpServletRequest req) {
        String path = req.getRequestURI();
        return path != null && path.startsWith("/api");
    }

    private ApiFieldError toFieldError(FieldError fe) {
        return new ApiFieldError(fe.getField(), fe.getDefaultMessage());
    }
}
