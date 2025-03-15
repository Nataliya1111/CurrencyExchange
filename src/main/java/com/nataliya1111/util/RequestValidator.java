package com.nataliya1111.util;

import java.math.BigDecimal;

public class RequestValidator {

    private RequestValidator(){
    }

    public static boolean isCurrencyCodeValid(String code){
        return code.matches("[A-Z]{3}");
    }

    public static boolean isCurrencyNameValid(String name){
        return name.matches("[a-zA-Z ]+") && name.length() <= 30;
    }

    public static boolean isCurrencySignValid(String sign){
        return sign.length() <= 3;
    }

    public static boolean isCurrencyParametersMissed(String code, String name, String sign){
        return code.isBlank() || code == null || name.isBlank() || name == null || sign.isBlank() || sign == null;
    }

    public static boolean isCodesPairValid(String codesPair){
        return codesPair.matches("[A-Z]{6}");
    }

    public static boolean isCodesInPairEquals(String codesPair){
        String baseCurrencyCode = codesPair.substring(0, 3);
        String targetCurrencyCode = codesPair.substring(3);
        return baseCurrencyCode.equals(targetCurrencyCode);
    }

    public static boolean isExchangeRateParametersMissed(String baseCurrencyCode, String targetCurrencyCode, String rate){
        return baseCurrencyCode.isBlank() || baseCurrencyCode == null || targetCurrencyCode.isBlank() || targetCurrencyCode == null || rate .isBlank() || rate  == null;
    }

    public static boolean isRateValid(String rate){
        return rate.length() <= 10 && rate.matches("\\d+(\\.\\d+)?") && new BigDecimal(rate).compareTo(BigDecimal.ZERO) > 0;
    }

}
