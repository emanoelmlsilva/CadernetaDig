package com.naotem.emanoel.cadernetadig.model;

public class Manga implements Geek{

    private String nome,site;
    private double capituloAtual,quantidadeTotal;
    private int id;

    public Manga(int id,String nome,double capituloAtual,double quantidadeTotal,String site){

        this.id = id;
        this.nome = nome;
        this.capituloAtual = capituloAtual;
        this.quantidadeTotal = quantidadeTotal;
        this.site = site;
    }


    public Manga(String nome,double capituloAtual,double quantidadeTotal,String site){
        this(0,nome,capituloAtual,quantidadeTotal,site);
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
        return this.capituloAtual;
    }
    @Override
    public void setAtual(double capituloAtual){
        this.capituloAtual = capituloAtual;
    }
    @Override

    public double getTotal(){
        return this.quantidadeTotal;
    }
    @Override

    public void setTotal(double quantidadeTotal){
        this.quantidadeTotal = quantidadeTotal;
    }
    @Override

    public String getSite() {
        return this.site;
    }
    @Override

    public void setSite(String site) {
        this.site = site;
    }
    @Override

    public int getId() {
        return this.id;
    }

    @Override
    public String toString(){
        return "Nome: "+this.nome+"\nCapitulo Atual: "+this.capituloAtual+"\nQuantidade Total: "+this.quantidadeTotal+"\nSite: "+this.site+"\n";
    }
}
