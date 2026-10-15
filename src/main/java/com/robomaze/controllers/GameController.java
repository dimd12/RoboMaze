package com.robomaze.controllers;

import com.robomaze.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class GameController {

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("MainMenu");
    }
}