package com.nataliya1111.dto;

import java.math.BigDecimal;

public class ExchangeRequestDto {

    private final String baseCurrencyCode;
    private final String targetCurrencyCode;
    private final BigDecimal amount;

    public ExchangeRequestDto(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.amount = amount;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
