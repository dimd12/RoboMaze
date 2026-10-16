package com.robomaze.controllers;

import com.robomaze.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HowToPlayController {

    @FXML
    Button btnQuit;
    Button btnHTP;
    Button btnPlay;

    @FXML
    private void initialize() {
        setActions();
    }

    @FXML
    private void switchToGame() throws IOException {
        App.setRoot("GameMenu");
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("MainMenu");
    }

    private void setActions() {
        btnQuit.setOnAction(e -> {
            System.exit(0);
        });

        btnHTP.setOnAction(e -> {
            try {
                switchToMenu();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnPlay.setOnAction(e -> {
            try {
                switchToGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
