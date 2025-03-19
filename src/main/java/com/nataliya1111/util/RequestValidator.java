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
        return code == null || code.isBlank() || name == null || name.isBlank() || sign == null || sign.isBlank();
    }

    public static boolean isCodesPairValid(String codesPair){
        return codesPair.matches("[A-Z]{6}");
    }

    public static boolean isCodesEqual(String codesPair){
        String baseCurrencyCode = codesPair.substring(0, 3);
        String targetCurrencyCode = codesPair.substring(3);
        return baseCurrencyCode.equals(targetCurrencyCode);
    }

    public static boolean isCodesEqual(String code1, String code2){
        return code1.equals(code2);
    }

    public static boolean isExchangeRateParametersMissed(String baseCurrencyCode, String targetCurrencyCode, String rate){
        return  baseCurrencyCode == null || baseCurrencyCode.isBlank() || targetCurrencyCode == null || targetCurrencyCode.isBlank() || rate  == null || rate .isBlank();
    }

    public static boolean isRateValid(String rate){
        return rate.length() <= 10 && rate.matches("\\d+(\\.\\d+)?") && new BigDecimal(rate).compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isExchangeParametersMissed(String from, String to, String amount){
        return  from == null || from.isBlank() || to == null || to.isBlank() || amount == null || amount.isBlank();
    }

    public static boolean isAmountValid(String amount){
        return amount.length() <= 16 && amount.matches("\\d+(\\.\\d+)?")  && new BigDecimal(amount).compareTo(BigDecimal.ZERO) > 0;
    }

}
