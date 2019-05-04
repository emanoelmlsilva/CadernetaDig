package com.naotem.emanoel.cadernetadig.exception;

public class NomeExistenteException extends Exception {

    public NomeExistenteException(){
        super();
    }

    public NomeExistenteException(String mensagem){
        super(mensagem);
    }
}
