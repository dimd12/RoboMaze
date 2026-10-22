package com.robomaze.controllers;

import com.robomaze.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the how to play screen.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public class HowToPlayController {

    @FXML
    Button btnPlay;

    @FXML
    Button btnHTP;

    @FXML
    Button btnQuit;

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        setActions();
    }

    /**
     * Switches to the game screen.
     */
    @FXML
    private void switchToGame() throws IOException {
        App.setRoot("GameMenu");
    }

    /**
     * Switches to the main menu screen.
     */
    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("MainMenu");
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
                switchToMenu();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnQuit.setOnAction(e -> System.exit(0));
    }
}
