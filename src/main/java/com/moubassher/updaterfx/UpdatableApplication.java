package com.moubassher.updaterfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class UpdatableApplication extends Application {

    private Pane overrideRoot;


    /**
     * A method for plugging in your custom Update UI. Leave empty if you'd like to
     * use the default UI.
     *
     */
    protected abstract UpdateUI createUpdateUI();

    protected abstract void showMainWindow(Stage stage) throws Exception;

    @Override
    public void init() throws IOException {
        // This runs BEFORE JavaFX starts
        UpdateService updateService = new UpdateService();

        Manifest local = updateService.readLocalManifest();
        Manifest remote = updateService.downloadManifest(local.getRemoteManifestPath());
        Update update = updateService.checkForUpdates(remote, local);
        if (update.available) {
            UpdateUI updateUI = createUpdateUI();
            if(updateUI != null) {
                overrideRoot = updateUI.getRoot();
            } else {    //use default Update UI:
                overrideRoot = loadDefaultUpdateUI();
            }
        }
    }

    private Pane loadDefaultUpdateUI() {
        FXMLLoader loader = new FXMLLoader(UpdateCtrl.class.getResource("update.fxml"));
        UpdateCtrl ctrl = new UpdateCtrl();
        loader.setController(ctrl);
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
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
