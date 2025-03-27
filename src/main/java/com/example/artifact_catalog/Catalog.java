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
        this.fileManager = new File_Manager();
    }

    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
        try {
            fileManager.writeArtifactsToFile(FILE_PATH, artifact);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void removeArtifact(String artifactId) {
        for (int i = artifacts.size() - 1; i >= 0; i--) { // index kaymasını engellemek için sondan başa
            if (artifacts.get(i).getArtifactId().equals(artifactId)) {
                artifacts.remove(i);
            }
        }
        try {
            fileManager.writeAllArtifactsToFile(FILE_PATH, artifacts); //performans için???
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
