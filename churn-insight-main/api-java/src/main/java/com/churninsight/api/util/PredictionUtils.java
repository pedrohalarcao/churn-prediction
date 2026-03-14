package com.churninsight.api.util;

import com.churninsight.api.model.enums.*;

public class PredictionUtils {

    // Faixa teórica do score (ajuste fino depois de testar casos):
    private static final double MIN_SCORE = -0.40;
    private static final double MAX_SCORE =  1.40;

    private PredictionUtils() { }

    // 1) contractMonths (tenure)
    public static double scoreByContractMonths(Integer contractMonths) {
        if (contractMonths == null) return 0.0;

        if (contractMonths < 3) return 0.25;
        if (contractMonths < 6) return 0.15;
        if (contractMonths < 12) return 0.05;
        if (contractMonths < 24) return 0.00;
        return -0.05;
    }

    // 2) contractType
    public static double scoreByContractType(ContractType contractType) {
        if (contractType == null) return 0.0;

        return switch (contractType) {
            case MONTH_TO_MONTH -> 0.20;
            case ONE_YEAR -> -0.05;   // contrato maior tende a reter
            case TWO_YEAR -> -0.10;
        };
    }

    // 3) monthlyCharges
    public static double scoreByMonthlyCharges(Double monthlyCharges) {
        if (monthlyCharges == null) return 0.0;

        if (monthlyCharges < 35) return 0.05;
        if (monthlyCharges < 70) return 0.10;
        if (monthlyCharges < 95) return 0.15;
        return 0.20;
    }

    // 4) totalCharges (muito baixo pode indicar cliente novo ou pouco engajado)
    public static double scoreByTotalCharges(Double totalCharges) {
        if (totalCharges == null) return 0.0;

        if (totalCharges < 100) return 0.10;
        if (totalCharges < 1000) return 0.05;
        if (totalCharges < 3000) return 0.00;
        return -0.05;
    }

    // 5) paymentMethod
    public static double scoreByPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) return 0.0;

        return switch (paymentMethod) {
            case ELECTRONIC_CHECK -> 0.20;
            case MAILED_CHECK -> 0.10;
            case BANK_TRANSFER_AUTOMATIC -> -0.05;
            case CREDIT_CARD_AUTOMATIC -> -0.06;
        };
    }

    // 6) paperlessBilling
    public static double scoreByPaperlessBilling(Boolean paperlessBilling) {
        if (paperlessBilling == null) return 0.0;
        return paperlessBilling ? 0.05 : 0.0;
    }

    // 7) internetService
    public static double scoreByInternetService(InternetService internetService) {
        if (internetService == null) return 0.0;

        return switch (internetService) {
            case FIBER_OPTIC -> 0.12;
            case DSL -> 0.05;
            case NONE -> -0.03;
        };
    }

    // 8) phoneService
    public static double scoreByPhoneService(Boolean phoneService) {
        if (phoneService == null) return 0.0;
        return phoneService ? 0.01 : 0.0;
    }

    // 9) multipleLines (Yes/No/No phone service)
    public static double scoreByMultipleLines(MultipleLines multipleLines) {
        if (multipleLines == null) return 0.0;

        return switch (multipleLines) {
            case YES -> 0.02;
            case NO -> 0.00;
            case NO_PHONE_SERVICE -> 0.00;
        };
    }

    // 10) onlineSecurity
    public static double scoreByOnlineSecurity(Boolean onlineSecurity, InternetService internetService) {
        return scoreByAddon(onlineSecurity, internetService);
    }

    // 11) onlineBackup
    public static double scoreByOnlineBackup(Boolean onlineBackup, InternetService internetService) {
        return scoreByAddon(onlineBackup, internetService);
    }

    // 12) deviceProtection
    public static double scoreByDeviceProtection(Boolean deviceProtection, InternetService internetService) {
        return scoreByAddon(deviceProtection, internetService);
    }

    // 13) techSupport
    public static double scoreByTechSupport(Boolean techSupport, InternetService internetService) {
        return scoreByAddon(techSupport, internetService);
    }

    // 14) streamingTV
    public static double scoreByStreamingTV(Boolean streamingTV, InternetService internetService) {
        return scoreByAddon(streamingTV, internetService);
    }

    // 15) streamingMovies
    public static double scoreByStreamingMovies(Boolean streamingMovies, InternetService internetService) {
        return scoreByAddon(streamingMovies, internetService);
    }

    private static double scoreByAddon(Boolean hasAddon, InternetService internetService) {
        if (internetService == null) return 0.0;
        if (internetService == InternetService.NONE) return 0.0;
        if (hasAddon == null) return 0.0;
        return hasAddon ? -0.03 : 0.04;
    }

    // 16) seniorCitizen
    public static double scoreBySeniorCitizen(Boolean seniorCitizen) {
        if (seniorCitizen == null) return 0.0;
        return seniorCitizen ? 0.05 : 0.0;
    }

    // 17) partner
    public static double scoreByPartner(Boolean partner) {
        if (partner == null) return 0.0;
        return partner ? -0.02 : 0.02;
    }

    // 18) dependents
    public static double scoreByDependents(Boolean dependents) {
        if (dependents == null) return 0.0;
        return dependents ? -0.03 : 0.02;
    }

    // 19) gender
    public static double scoreByGender(Gender gender) {
        if (gender == null) return 0.0;

        return switch (gender) {
            case MALE -> 0.0;
            case FEMALE -> 0.02;
        };
    }


    public static double normalizeToProbability(double rawScore) {
        double clamped = Math.max(MIN_SCORE, Math.min(MAX_SCORE, rawScore));
        double probability = (clamped - MIN_SCORE) / (MAX_SCORE - MIN_SCORE);
        probability = Math.max(0.0, Math.min(1.0, probability));
        return Math.round(probability * 10_000.0) / 10_000.0;
    }
}
