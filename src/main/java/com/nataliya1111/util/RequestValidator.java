package com.nataliya1111.util;

public class RequestValidator {

    private RequestValidator(){
    }

    public static boolean isCurrencyCodeValid(String code){
        if(code.matches("[A-Z]{3}")){
            return true;
        }
        return false;
    }

    public static boolean isCodesPairValid(String codesPair){
        if(codesPair.matches("[A-Z]{6}")){
            return true;
        }
        return false;
    }
}
