package com.moubassher.updaterfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends UpdatableApplication {

    @Override
    protected void showMainWindow(Stage stage) throws Exception {
        Pane root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        stage.setScene(new Scene(root, 400, 400));
    }
}
