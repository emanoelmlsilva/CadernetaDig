package com.naotem.emanoel.cadernetadig.model;

public class Hq implements Geek{

    private String nome,site;
    private double quantidade,capitulo;
    private int id;

    public Hq(int id,String nome,double capitulo,double quantidade,String site){
        this.id = id;
        this.nome = nome;
        this.capitulo = capitulo;
        this.quantidade = quantidade;
        this.site = site;
    }

    public Hq(String nome,double capitulo,double quantidade,String site){
        this(0,nome,capitulo,quantidade,site);
    }

    @Override
    public String getNome(){
        return this.nome;
    }

    @Override
    public void setNome(String nome){
        this.nome = nome;
    }

    @Override
    public double getAtual(){
        return this.capitulo;
    }

    @Override
    public void setAtual(double capitulo){
        this.capitulo = capitulo;
    }

    @Override
    public double getTotal(){
        return this.quantidade;
    }

    @Override
    public void setTotal(double quantidade){
        this.quantidade = quantidade;
    }

    @Override
    public String getSite(){
        return this.site;
    }

    @Override
    public void setSite(String site){
        this.site = site;
    }

    @Override
    public int getId(){
        return  this.id;
    }

    public void setId(int id){
        this.id = id;
    }


}
