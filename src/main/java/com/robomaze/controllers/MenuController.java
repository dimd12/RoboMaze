package com.robomaze.controllers;

import com.robomaze.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML
    Button btnPlay;

    @FXML
    Button btnHTP;

    @FXML
    Button btnQuit;

    @FXML
    private void switchToGame() throws IOException {
        App.setRoot("GameMenu");
    }

    @FXML
    private void switchToHTP() throws IOException {
        App.setRoot("HowToPlay");
    }

    @FXML
    private void initialize() {
        setActions();
    }

    private void setActions() {
        btnPlay.setOnAction(e -> {
            try {
                switchToGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnHTP.setOnAction(e -> {
            try {
                switchToHTP();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnQuit.setOnAction(e -> {
            System.exit(0);
        });
    }
}
