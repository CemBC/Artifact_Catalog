package com.example.artifact_catalog;

import java.util.ArrayList;
import java.util.List;

public class SearchManager {

    public List<Artifact> searchAndFilterArtifacts(List<Artifact> artifacts, String data) {
        String[] splitted = data.toLowerCase().split(",");
        for (int i = 0; i < splitted.length; i++) {
            splitted[i] = splitted[i].trim();
        }

        List<Artifact> result = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (matched(artifact, splitted)) {
                result.add(artifact);
            }
        }
        return result;
    }
    private boolean matched(Artifact artifact, String[] splitted) {
        for (String term : splitted) {
            if (!artifactMatched(artifact, term)) {
                return false;
            }
        }
        return true;
    }

    private boolean artifactMatched(Artifact artifact, String term) {
        return artifact.getArtifactId().toLowerCase().contains(term) ||
                artifact.getArtifactName().toLowerCase().contains(term) ||
                artifact.getCategory().toLowerCase().contains(term) ||
                artifact.getCivilization().toLowerCase().contains(term) ||
                artifact.getDiscoveryLocation().toLowerCase().contains(term) ||
                artifact.getComposition().toLowerCase().contains(term) ||
                artifact.getDiscoveryDate().toLowerCase().contains(term) ||
                artifact.getCurrentPlace().toLowerCase().contains(term) ||
                artifact.getDimensions().toString().toLowerCase().contains(term) ||
                String.valueOf(artifact.getWeight()).contains(term) ||
                artifact.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(term));
    }





}