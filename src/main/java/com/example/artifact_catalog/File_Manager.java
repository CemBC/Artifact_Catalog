package com.example.artifact_catalog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class File_Manager {

    public List<Artifact> readArtifactsFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray jsonArray = new JSONArray(content);

        List<Artifact> artifacts = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Artifact artifact = new Artifact();
            artifact.setArtifactId(jsonObject.getString("artifactid"));
            artifact.setArtifactName(jsonObject.getString("artifactname"));
            artifact.setCategory(jsonObject.getString("category"));
            artifact.setCivilization(jsonObject.getString("civilization"));
            artifact.setDiscoveryLocation(jsonObject.getString("discoverylocation"));
            artifact.setComposition(jsonObject.getString("composition"));
            artifact.setDiscoveryDate(jsonObject.getString("discoverydate"));
            artifact.setCurrentPlace(jsonObject.getString("currentplace"));
            JSONObject dimensionsObject = jsonObject.getJSONObject("dimensions");
            Dimensions dimensions = new Dimensions();
            dimensions.setWidth(dimensionsObject.getDouble("width"));
            dimensions.setLength(dimensionsObject.getDouble("length"));
            dimensions.setHeight(dimensionsObject.getDouble("height"));
            artifact.setDimensions(dimensions);
            artifact.setWeight(jsonObject.getDouble("weight"));
            JSONArray tagsArray = jsonObject.getJSONArray("tags");
            List<String> tags = new ArrayList<>();
            for(int j=0;j<tagsArray.length();j++){
                tags.add(tagsArray.getString(j));
            }
            artifact.setTags(tags);
            artifacts.add(artifact);
        }
        return artifacts;
    }

    public void writeArtifactsToFile(String filePath, Artifact artifact) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray jsonArray = new JSONArray(content);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("artifactid", artifact.getArtifactId());
        jsonObject.put("artifactname", artifact.getArtifactName());
        jsonObject.put("category", artifact.getCategory());
        jsonObject.put("civilization", artifact.getCivilization());
        jsonObject.put("discoverylocation", artifact.getDiscoveryLocation());
        jsonObject.put("composition", artifact.getComposition());
        jsonObject.put("discoverydate", artifact.getDiscoveryDate());
        jsonObject.put("currentplace", artifact.getCurrentPlace());
        JSONObject dimensionsObject = new JSONObject();
        dimensionsObject.put("width", artifact.getDimensions().getWidth());
        dimensionsObject.put("length", artifact.getDimensions().getLength());
        dimensionsObject.put("height", artifact.getDimensions().getHeight());
        jsonObject.put("dimensions", dimensionsObject);
        jsonObject.put("weight", artifact.getWeight());
        JSONArray tagsArray = new JSONArray(artifact.getTags());
        jsonObject.put("tags", tagsArray);
        jsonArray.put(jsonObject);
        Files.write(Paths.get(filePath), (jsonArray.toString(4) + "\n").getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }
}