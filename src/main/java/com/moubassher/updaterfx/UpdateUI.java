package com.moubassher.updaterfx;


import javafx.scene.layout.Pane;

public interface UpdateUI {

    /** Must return the root node of the update dialog (custom or default). */
    Pane getRoot();

    /** Called when the user clicks "Update" — must start background download. */
    void downloadUpdateInBackground(Update update);

    /** Called when the user clicks "Download Now" on the dialog window. */
    void downloadDirect(Update update);

    /** Called when the user clicks "Remind Me Later". */
    void remindLater(Update update);

    /** Allows UpdateService or UI to query progress. */
    double getProgress();
}

