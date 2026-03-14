package com.churninsight.api.dto;

import com.churninsight.api.model.enums.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record CustomerInputDto(

        // 1. GENDER (enum: MALE/FEMALE)
        @NotNull(message = "gender is required")
        @JsonProperty("gender")
        Gender gender,

        // 2. SENIOR CITIZEN (boolean: true/false)
        @NotNull(message = "seniorCitizen is required")
        @JsonProperty("seniorCitizen")
        Boolean seniorCitizen,

        // 3. PARTNER (boolean: true/false)
        @NotNull(message = "partner is required")
        @JsonProperty("partner")
        Boolean partner,

        // 4. DEPENDENTS (boolean: true/false)
        @NotNull(message = "dependents is required")
        @JsonProperty("dependents")
        Boolean dependents,

        // 5. TENURE = contractMonths (int: 0-72)
        @NotNull(message = "contractMonths is required")
        @Min(value = 0, message = "contractMonths must be at least 0")
        @Max(value = 72, message = "contractMonths must be at most 72")
        @JsonProperty("contractMonths")
        Integer contractMonths,

        // 6. PHONE SERVICE (boolean: true/false)
        @NotNull(message = "phoneService is required")
        @JsonProperty("phoneService")
        Boolean phoneService,

        // 7. MULTIPLE LINES (enum)
        @NotNull(message = "multipleLines is required")
        @JsonProperty("multipleLines")
        MultipleLines multipleLines,

        // 8. INTERNET SERVICE (enum)
        @NotNull(message = "internetService is required")
        @JsonProperty("internetService")
        InternetService internetService,

        // 9. ONLINE SECURITY (enum: YES/NO)
        @NotNull(message = "onlineSecurity is required")
        @JsonProperty("onlineSecurity")
        StateService onlineSecurity,

        // 10. ONLINE BACKUP (enum: YES/NO)
        @NotNull(message = "onlineBackup is required")
        @JsonProperty("onlineBackup")
        StateService onlineBackup,

        // 11. DEVICE PROTECTION (enum: YES/NO)
        @NotNull(message = "deviceProtection is required")
        @JsonProperty("deviceProtection")
        StateService deviceProtection,

        // 12. TECH SUPPORT (enum: YES/NO)
        @NotNull(message = "techSupport is required")
        @JsonProperty("techSupport")
        StateService techSupport,

        // 13. STREAMING TV (enum: YES/NO)
        @NotNull(message = "streamingTV is required")
        @JsonProperty("streamingTV")
        StateService streamingTV,

        // 14. STREAMING MOVIES (enum: YES/NO)
        @NotNull(message = "streamingMovies is required")
        @JsonProperty("streamingMovies")
        StateService streamingMovies,

        // 15. CONTRACT TYPE (enum)
        @NotNull(message = "contractType is required")
        @JsonProperty("contractType")
        ContractType contractType,

        // 16. PAPERLESS BILLING (boolean: true/false)
        @NotNull(message = "paperlessBilling is required")
        @JsonProperty("paperlessBilling")
        Boolean paperlessBilling,

        // 17. PAYMENT METHOD (enum)
        @NotNull(message = "paymentMethod is required")
        @JsonProperty("paymentMethod")
        PaymentMethod paymentMethod,

        // 18. MONTHLY CHARGES (double: >= 0)
        @NotNull(message = "monthlyCharges is required")
        @DecimalMin(value = "0.0", message = "monthlyCharges must be greater than or equal to 0")
        @DecimalMax(value = "200.0", message = "monthlyCharges is too high for telecom")
        @Digits(integer = 4, fraction = 2, message = "monthlyCharges must have at most 4 integer digits and 2 decimal digits")
        @JsonProperty("monthlyCharges")
        Double monthlyCharges,

        // 19. TOTAL CHARGES (double: >= 0)
        @NotNull(message = "totalCharges is required")
        @DecimalMin(value = "0.0", message = "totalCharges must be greater than or equal to 0")
        @DecimalMax(value = "10000.0", message = "totalCharges is too high")
        @Digits(integer = 6, fraction = 2, message = "totalCharges must have at most 6 integer digits and 2 decimal digits")
        @JsonProperty("totalCharges")
        Double totalCharges

) {}
