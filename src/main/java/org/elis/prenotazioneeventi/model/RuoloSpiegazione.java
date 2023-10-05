package org.elis.prenotazioneeventi.model;

public enum RuoloSpiegazione {
    /*private static final RuoloSpiegazione */CLIENTE/*=new RuoloSpiegazione*/("cliente")/*;*/,
    /*private static final RuoloSpiegazione */VENDITORE/*=new RuoloSpiegazione*/("venditore");


    private RuoloSpiegazione(String nome){
        this.nome=nome;
    }


    private String nome;
}
