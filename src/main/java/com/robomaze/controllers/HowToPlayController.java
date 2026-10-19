package com.robomaze.controllers;

import com.robomaze.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HowToPlayController {

    @FXML
    Button btnQuit;

    @FXML
    Button btnHTP;

    @FXML
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
