package com.nataliya1111.dto;

import com.nataliya1111.entity.Currency;

import java.math.BigDecimal;

public class ExchangeRateRequestDto {

    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private String rate;

    public ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
