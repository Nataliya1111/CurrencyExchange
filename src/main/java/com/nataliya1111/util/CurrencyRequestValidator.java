package com.nataliya1111.util;

public class CurrencyRequestValidator {

    private CurrencyRequestValidator(){
    }

    public static boolean isCodeValid(String code){
        if(code.matches("[A-Z]{3}")){
            return true;
        }
        return false;
    }
}
