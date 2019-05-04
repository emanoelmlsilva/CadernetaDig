package com.naotem.emanoel.cadernetadig.model;

public class Anime extends GeekAnime{

    private String nome,site,season;
    private double episodioAtual, episodioTotal;
    private int id;

    public Anime(int id, String nome, double episodioAtual, double episodioTotal, String site, String season){
        this.id = id;
        this.nome = nome;
        this.episodioAtual = episodioAtual;
        this.episodioTotal = episodioTotal;
        this.season = season;
        this.site = site;
    }

    public Anime(String nome, double episodioAtual, double episodioTotal, String site, String season){
        this(0,nome,episodioAtual,episodioTotal, site, season);
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
        return this.episodioAtual;
    }

    @Override
    public void setAtual(double episodioAtual){
        this.episodioAtual = episodioAtual;
    }

    @Override
    public double getTotal(){
        return this.episodioTotal;
    }

    @Override
    public void setTotal(double episodioTotal){
        this.episodioTotal = episodioTotal;
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
    public String getSeason(){
        return this.season;
    }

    @Override
    public void setSeason(String season){
        this.season = season;
    }

    @Override
    public String toString(){

        return "Nome: "+this.nome+"\nEpisodio Atual: "+this.episodioAtual+"\nQuantidade Total: "+this.episodioTotal +"\nSite: "+this.site+"\n";
    }
}
