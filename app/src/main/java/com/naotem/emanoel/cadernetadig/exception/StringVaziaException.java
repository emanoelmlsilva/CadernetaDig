package com.naotem.emanoel.cadernetadig.exception;

public class StringVaziaException extends Exception {

    public StringVaziaException(){
        super();
    }

    public StringVaziaException(String informacao){
        super(informacao);
    }
}
