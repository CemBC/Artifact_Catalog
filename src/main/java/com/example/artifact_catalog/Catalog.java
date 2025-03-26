package com.example.artifact_catalog;

import java.util.List;

public class Catalog {
    List<Artifact> artifacts;
    File_Manager fileManager;


    public Catalog(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }


}
