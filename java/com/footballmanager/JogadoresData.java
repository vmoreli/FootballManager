package com.footballmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class JogadoresData {
    private static JogadoresData instance = new JogadoresData();
    private ObservableList<Jogador> jogadores;
    private JogadoresData(){
        jogadores = FXCollections.observableArrayList();
    }
    public static JogadoresData getInstance() {
        return instance;
    }
    public void add(Jogador jogador){
        jogadores.add(jogador);
    }
    public  void clear(){
        jogadores.clear();
    }
    public ObservableList<Jogador> getJogadores(){
        return jogadores;
    }
    public void loadJogadores(ArrayList<String> list){
        String[] splitStrJog;
        for (String strJog : list) {
            splitStrJog =  strJog.split(",");
            Jogador jogador = new Jogador(Integer.parseInt(splitStrJog[0])
                    , Integer.parseInt(splitStrJog[1])
                    , splitStrJog[2]
                    , splitStrJog[3]
                    , splitStrJog[4]);

            jogadores.add(jogador);
        }
    }
}
