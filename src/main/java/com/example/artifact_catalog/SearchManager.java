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


}