package io.celox.soundboard3k1.models;

public class VideoItem {
    private String name;
    private String path;

    public VideoItem(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}