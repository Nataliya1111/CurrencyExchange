package com.nataliya1111.exception;

public class NoSuchCurrencyException extends RuntimeException{

    public NoSuchCurrencyException(String message){
        super(message);
    }
}
