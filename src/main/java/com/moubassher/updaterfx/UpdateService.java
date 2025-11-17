package com.moubassher.updaterfx;

import com.context.our_utilities.Jsonable;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateService {


    public Manifest readLocalManifest() throws IOException {
        Path exeDir = getExecutableDir();
        Path manifestPath = exeDir.resolve("manifest.json");

        if (!Files.exists(manifestPath)) {
            return null;
        }

        String content = Files.readString(manifestPath);
        return Jsonable.fromJson(new JSONObject(content), Manifest.class);
    }

    Path getExecutableDir() {
        try {
            Path path = Paths.get(Launcher.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
            return Files.isRegularFile(path) ? path.getParent() : path;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot determine executable directory", e);
        }
    }

    Manifest downloadManifest(String urlString) {
        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol();

            if ("file".equalsIgnoreCase(protocol)) {

                Path path = Paths.get(url.toURI());
                if (!Files.exists(path)) {
                    System.out.println("Remote manifest not found: " + path);
                    return null;
                }

                String content = Files.readString(path, StandardCharsets.UTF_8);
                return Jsonable.fromJson(new JSONObject(content), Manifest.class);
            }

            if ("http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol)) {

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10_000);
                connection.setReadTimeout(10_000);

                int code = connection.getResponseCode();

                if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                    System.out.println("Remote manifest not found: " + url);
                    return null;
                }

                if (code != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Failed to download manifest: " + code);
                }

                try (var in = connection.getInputStream()) {
                    String jsonText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                    return Jsonable.fromJson(new JSONObject(jsonText), Manifest.class);
                } finally {
                    connection.disconnect();
                }
            }

            throw new IllegalArgumentException("Unsupported protocol: " + protocol);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Update checkForUpdates(Manifest remoteManifest, Manifest localManifest) {
        boolean available = isUpdateAvailable(remoteManifest, localManifest);
        if (!available) {
            return new Update(false, List.of(), List.of(), List.of());
        }

        List<Manifest.FileEntry> localFiles = localManifest.getFiles();
        List<Manifest.FileEntry> remoteFiles = remoteManifest.getFiles();

        List<String> added = new ArrayList<>();
        List<String> changed = new ArrayList<>();
        List<String> removed = new ArrayList<>();

        Map<String, Manifest.FileEntry> localMap = localFiles.stream()
                .collect(Collectors.toMap(Manifest.FileEntry::getPath, f -> f));

        Set<String> remotePaths = remoteFiles.stream()
                .map(Manifest.FileEntry::getPath)
                .collect(Collectors.toSet());

        for (Manifest.FileEntry remote : remoteFiles) {
            Manifest.FileEntry local = localMap.get(remote.getPath());
            if (local == null) added.add(remote.getPath());
            else if (!local.getSha256().equals(remote.getSha256())) changed.add(remote.getPath());
        }

        for (Manifest.FileEntry local : localFiles) {
            if (!remotePaths.contains(local.getPath())) removed.add(local.getPath());
        }

        return new Update(true, added, changed, removed);
    }

    private boolean isUpdateAvailable(Manifest remoteManifest, Manifest localManifest) {
        return remoteManifest.getReleaseTime().isAfter(localManifest.getReleaseTime());
    }

}
