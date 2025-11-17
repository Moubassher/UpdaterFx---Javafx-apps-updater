package com.moubassher.updaterfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class UpdatableApplication extends Application {

    private Pane overrideRoot;

    protected abstract void showMainWindow(Stage stage) throws Exception;

    @Override
    public void init() throws IOException {
        // This runs BEFORE JavaFX starts
        UpdateService updateService = new UpdateService();

        Manifest local = updateService.readLocalManifest();
        Manifest remote = updateService.downloadManifest(local.getRemoteManifestPath());
        Update update = updateService.checkForUpdates(remote, local);
        if (update.available) {

            System.out.println("Update is available!");
            //TODO: add logic for use of a custom update window.
            try {
                FXMLLoader loader = new FXMLLoader(UpdateCtrl.class.getResource("update.fxml"));
                UpdateCtrl ctrl = new UpdateCtrl();
                loader.setController(ctrl);
                overrideRoot = loader.load();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void start(Stage stage) throws Exception {
        if (overrideRoot != null) {
            stage.setScene(new Scene(overrideRoot));
        } else {
            showMainWindow(stage);
        }
        stage.show();
    }
}
