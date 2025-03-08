package com.nataliya1111.exception;

public class NoSuchExchangeRateException extends RuntimeException{

    public NoSuchExchangeRateException(String message){
        super(message);
    }
}
