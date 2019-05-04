package com.naotem.emanoel.cadernetadig.model;

import android.support.design.widget.TextInputLayout;

import com.naotem.emanoel.cadernetadig.exception.NomeExistenteException;
import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.StringVaziaException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;

import java.util.List;

public interface verificarCondicoesNameNumber<Item> {

    void checkInformaçoesNome(TextInputLayout input) throws StringVaziaException;


    void checkInformationNumber(TextInputLayout input) throws NumeroVazioException,NumeroInvalidoException ;

    boolean checkValorBigger(double valor1, double  valor2) throws VerificarMaiorExcpetion;

    boolean checkNameEquals(String nome, List<Item> lista)throws NomeExistenteException;

}
