package io.celox.soundboard3k1.models;

public class SoundItem {
    private String fileName;
    private String displayName;
    private String folderName;

    public SoundItem(String fileName, String displayName, String folderName) {
        this.fileName = fileName;
        this.displayName = displayName;
        this.folderName = folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}