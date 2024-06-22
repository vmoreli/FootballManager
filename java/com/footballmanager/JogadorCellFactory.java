package com.footballmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;

public class JogadorCellFactory implements Callback<ListView<Jogador>, ListCell<Jogador>> {
    @Override
    public ListCell<Jogador> call(ListView<Jogador> jogadorListView) {
        return new ListCell<>(){
            private FXMLLoader loader = null;
            private JogadorCellController controller;
            @Override
            protected void updateItem(Jogador jogador, boolean empty) {
                super.updateItem(jogador, empty);

                if (empty || jogador == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (this.loader == null) {
                        this.loader = new FXMLLoader(getClass().getResource("jogador_cell.fxml"));
                        try {
                            this.loader.load();
                            controller = this.loader.getController();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    controller.setJogador(jogador);
                    setGraphic(this.loader.getRoot());
                }
            }
        };
    }
}