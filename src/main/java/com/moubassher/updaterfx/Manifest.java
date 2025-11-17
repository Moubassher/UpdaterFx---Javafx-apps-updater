package com.moubassher.updaterfx;

import com.context.our_utilities.Jsonable;

import java.time.LocalDateTime;
import java.util.List;

public class Manifest implements Jsonable {
    private String appName, companyName, version, remoteUrl, remoteManifestPath, localDir;
    private LocalDateTime releaseTime;
    private List<FileEntry> files;

    public Manifest(String appName, String companyName, String version, String remoteUrl, String remoteManifestPath, String localDir, LocalDateTime releaseTime, List<FileEntry> files) {
        this.appName = appName;
        this.companyName = companyName;
        this.version = version;
        this.remoteUrl = remoteUrl;
        this.remoteManifestPath = remoteManifestPath;
        this.localDir = localDir;
        this.releaseTime = releaseTime;
        this.files = files;
    }

    public Manifest() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getRemoteManifestPath() {
        return remoteManifestPath;
    }

    public void setRemoteManifestPath(String remoteManifestPath) {
        this.remoteManifestPath = remoteManifestPath;
    }

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    public LocalDateTime getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(LocalDateTime releaseTime) {
        this.releaseTime = releaseTime;
    }

    public List<FileEntry> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntry> files) {
        this.files = files;
    }

    public static class FileEntry {
        private String path;
        private String sha256;
        private long size;


        public FileEntry() {
        }

        public FileEntry(String path, String sha256, long size) {
            this.path = path;
            this.sha256 = sha256;
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public String getSha256() {
            return sha256;
        }

        public long getSize() {
            return size;
        }
    }
}