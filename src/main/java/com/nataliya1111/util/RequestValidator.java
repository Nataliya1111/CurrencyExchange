package com.nataliya1111.util;

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

    public static boolean isCodesPairValid(String codesPair){
        return codesPair.matches("[A-Z]{6}");
    }

    public static boolean isCodesInPairEquals(String codesPair){
        String baseCurrencyCode = codesPair.substring(0, 3);
        String targetCurrencyCode = codesPair.substring(3);
        return baseCurrencyCode.equals(targetCurrencyCode);
    }


}
