package com.example.artifact_catalog;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Catalog {
    List<Artifact> artifacts;
    File_Manager fileManager;
    private static final String FILE_PATH = System.getProperty("user.home") + "/Documents/artifacts.json";

    public Catalog() {
        this.artifacts =new ArrayList<>();
        this.fileManager = new File_Manager(); // Initialize fileManager
    }

    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
        try {
            fileManager.writeArtifactsToFile(FILE_PATH, artifact);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadArtifactsFromFile() throws IOException {
        List<Artifact> artifacts = fileManager.readArtifactsFromFile(FILE_PATH);
        setArtifacts(artifacts);
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
}
