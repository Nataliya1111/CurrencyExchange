package com.nataliya1111.dto;

public class ExchangeRateRequestDto {

    private final String baseCurrencyCode;
    private final String targetCurrencyCode;
    private final String rate;

    public ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public String getRate() {
        return rate;
    }

}
