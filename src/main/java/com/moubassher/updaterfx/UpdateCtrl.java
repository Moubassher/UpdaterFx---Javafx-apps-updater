package com.moubassher.updaterfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class UpdateCtrl implements Initializable {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button downloadBtn, remindMeBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        downloadBtn.setOnAction(x -> applyUpdateUI());
    }


    private void applyUpdate(Update update, Manifest remoteManifest, Path exeDir, Consumer<Double> progressCallback) {
        if (!update.available) return;

        try {
            String remoteUrl = remoteManifest.getRemoteUrl();
            List<String> toDownload = new ArrayList<>();
            toDownload.addAll(update.added);
            toDownload.addAll(update.changed);

            long totalBytes = toDownload.stream()
                    .map(path -> remoteManifest.getFiles().stream()
                            .filter(f -> f.getPath().equals(path))
                            .findFirst().orElseThrow()
                            .getSize())
                    .reduce(0L, Long::sum);

            long[] downloadedBytes = {0};

            for (String path : toDownload) {
                Manifest.FileEntry entry = remoteManifest.getFiles().stream()
                        .filter(f -> f.getPath().equals(path))
                        .findFirst().orElseThrow();

                URL fileUrl = new URL(remoteUrl + "/" + entry.getPath().replace("\\", "/"));
                Path dest = exeDir.resolve(entry.getPath());
                Files.createDirectories(dest.getParent());

                try (InputStream in = fileUrl.openStream();
                     OutputStream out = Files.newOutputStream(dest)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        downloadedBytes[0] += bytesRead;
                        double progress = (double) downloadedBytes[0] / totalBytes;
                        progressCallback.accept(progress);
                    }
                }
            }

            // Delete removed files
            for (String path : update.removed) {
                Files.deleteIfExists(exeDir.resolve(path));
            }

            // Write new manifest
            Path manifestPath = exeDir.resolve("manifest.json");
            Files.writeString(manifestPath,
                    remoteManifest.toJson().toString(2),
                    StandardCharsets.UTF_8);

            // Ensure 100% progress
            progressCallback.accept(1.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Simple UI hook for JavaFX
    private void applyUpdateUI() {
        // Example usage:
        // applyUpdate(update, remoteManifest, exeDir, progressBar::setProgress);
    }
}
