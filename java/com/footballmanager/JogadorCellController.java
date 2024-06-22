package com.footballmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class JogadorCellController {
    @FXML
    Label id;
    @FXML
    Label nomeJogador;
    @FXML
    Label nomeClube;
    @FXML
    Button editar;
    @FXML
    Button remover;

    public void setJogador(Jogador jogador){
        id.setText(Integer.toString(jogador.getId()));
        nomeJogador.setText(jogador.getNomeJogador());
        nomeClube.setText(jogador.getNomeClube());
    }

}
