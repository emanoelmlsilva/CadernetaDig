package com.naotem.emanoel.cadernetadig.exception;

public class NumeroInvalidoException extends Exception {

    public NumeroInvalidoException(){
        super();
    }

    public NumeroInvalidoException(String informacao){
        super(informacao);
    }
}
