package com.robomaze.controllers;

import com.robomaze.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class MenuController {

    @FXML
    Button btnPlay;

    @FXML
    Button btnHTP;

    @FXML
    Button btnQuit;

    @FXML
    private ImageView robotBody;


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

        RotateTransition wobble = new RotateTransition(
                Duration.millis(450),
                robotBody
        );

        wobble.setFromAngle(-4);
        wobble.setToAngle(4);
        wobble.setCycleCount(Animation.INDEFINITE);
        wobble.setAutoReverse(true);
        wobble.play();
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
