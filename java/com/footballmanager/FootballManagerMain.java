package com.footballmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FootballManagerMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FootballManager.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Football Manager");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {

        launch();
    }
}