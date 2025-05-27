package io.celox.soundboard3k1.models;

public class SoundCategory {
    private String name;
    private String displayName;
    private String folderPath;

    public SoundCategory(String name, String displayName, String folderPath) {
        this.name = name;
        this.displayName = displayName;
        this.folderPath = folderPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
}