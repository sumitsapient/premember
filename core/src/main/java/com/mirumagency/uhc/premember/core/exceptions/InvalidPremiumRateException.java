package com.mirumagency.uhc.premember.core.exceptions;

public class InvalidPremiumRateException extends Exception {
    private final String filePath;
    private final String planName;

    public InvalidPremiumRateException(String message, String filePath, String planName) {
        super(message);
        this.filePath = filePath;
        this.planName = planName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPlanName() {
        return planName;
    }
}
