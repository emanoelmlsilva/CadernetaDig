package com.naotem.emanoel.cadernetadig.model;

import android.support.design.widget.TextInputLayout;

import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;

public interface verificarCondicaoNumber{

    void checkInformationNumber(String input) throws NumeroVazioException, NumeroInvalidoException;

    void checkValorBigger(double valor1, double  valor2) throws VerificarMaiorExcpetion;

}
