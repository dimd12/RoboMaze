package com.robomaze.controllers;

import com.robomaze.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the menu screen.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public class MenuController {

    @FXML
    Button btnPlay;

    @FXML
    Button btnHTP;

    @FXML
    Button btnQuit;

    @FXML
    /*
    * Switches to the game screen.
     */
    private void switchToGame() throws IOException {
        App.setRoot("GameMenu");
    }

    /**
     * Switches to the how to play screen.
     */
    @FXML
    private void switchToHTP() throws IOException {
        App.setRoot("HowToPlay");
    }

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        setActions();
    }

    /**
     * Sets the actions for the buttons.
     */
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

        btnQuit.setOnAction(e -> System.exit(0));
    }
}
