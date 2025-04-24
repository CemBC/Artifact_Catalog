package com.example.artifact_catalog;

import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class File_Manager {

    private final List<String> validTags = List.of("domestic", "fragmented", "funerary", "inscribed", "jewelery", "mythological", "reconstructed", "religious", "weaponry");

    public List<Artifact> readArtifactsFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            Files.createFile(path);
            Files.write(path, "[]".getBytes());
        }

        try {
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            List<Artifact> artifacts = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                List<String> expectedKeys = List.of(
                        "artifactid", "artifactname", "category", "civilization", "discoverylocation",
                        "composition", "discoverydate", "currentplace", "imagePath", "dimensions", "weight", "tags"
                );
                for (String key : expectedKeys) {
                    if (!jsonObject.has(key)) {
                        if (key.equals("dimensions")) {
                            JSONObject emptyDimensions = new JSONObject();
                            emptyDimensions.put("width", JSONObject.NULL);
                            emptyDimensions.put("length", JSONObject.NULL);
                            emptyDimensions.put("height", JSONObject.NULL);
                            jsonObject.put("dimensions", emptyDimensions);
                        } else if (key.equals("tags")) {
                            jsonObject.put("tags", new JSONArray());
                        } else {
                            jsonObject.put(key, JSONObject.NULL);
                        }
                    }
                }

                Artifact artifact = new Artifact();
                artifact.setArtifactId(jsonObject.optString("artifactid", null));
                artifact.setArtifactName(jsonObject.optString("artifactname", null));
                artifact.setCategory(jsonObject.optString("category", null));
                artifact.setCivilization(jsonObject.optString("civilization", null));
                artifact.setDiscoveryLocation(jsonObject.optString("discoverylocation", null));
                artifact.setComposition(jsonObject.optString("composition", null));
                artifact.setDiscoveryDate(jsonObject.optString("discoverydate", null));
                artifact.setCurrentPlace(jsonObject.optString("currentplace", null));
                artifact.setImagePath(jsonObject.optString("imagePath", ""));

                JSONObject dimensionsObject = jsonObject.getJSONObject("dimensions");
                Dimensions dimensions = new Dimensions();
                dimensions.setWidth(dimensionsObject.optDouble("width", 0.0));
                dimensions.setLength(dimensionsObject.optDouble("length", 0.0));
                dimensions.setHeight(dimensionsObject.optDouble("height", 0.0));
                artifact.setWeight(jsonObject.optDouble("weight", 0.0));
                artifact.setDimensions(dimensions);

                JSONArray tagsArray = jsonObject.getJSONArray("tags");
                List<String> tags = new ArrayList<>();
                for (int j = 0; j < tagsArray.length(); j++) {
                    String tag = tagsArray.optString(j);
                    if (validTags.contains(tag)) {
                        tags.add(tag);
                    }
                }
                artifact.setTags(tags);

                artifacts.add(artifact);
            }
            return artifacts;
        } catch (JSONException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Read Error");
            alert.setHeaderText("An error occurred while reading the file.The json format might be corrupted");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw e;
        }
    }


    public void writeArtifactsToFile(String filePath, Artifact artifact) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            Files.createFile(path);
            Files.write(path, "[]".getBytes());
        }

        String content = new String(Files.readAllBytes(path));
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
        jsonObject.put("imagePath", artifact.getImagePath());

        JSONObject dimensionsObject = new JSONObject();
        dimensionsObject.put("width", artifact.getDimensions().getWidth());
        dimensionsObject.put("length", artifact.getDimensions().getLength());
        dimensionsObject.put("height", artifact.getDimensions().getHeight());
        jsonObject.put("dimensions", dimensionsObject);
        jsonObject.put("weight", artifact.getWeight());

        JSONArray tagsArray = new JSONArray(artifact.getTags());
        jsonObject.put("tags", tagsArray);
        jsonArray.put(jsonObject);
        Files.write(path, (jsonArray.toString(4) + "\n").getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void writeAllArtifactsToFile(String filePath, List<Artifact> artifacts) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            Files.createFile(path);
        }

        JSONArray jsonArray = new JSONArray();
        for (Artifact artifact : artifacts) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("artifactid", artifact.getArtifactId());
            jsonObject.put("artifactname", artifact.getArtifactName());
            jsonObject.put("category", artifact.getCategory());
            jsonObject.put("civilization", artifact.getCivilization());
            jsonObject.put("discoverylocation", artifact.getDiscoveryLocation());
            jsonObject.put("composition", artifact.getComposition());
            jsonObject.put("discoverydate", artifact.getDiscoveryDate());
            jsonObject.put("currentplace", artifact.getCurrentPlace());
            jsonObject.put("imagePath", artifact.getImagePath());

            JSONObject dimensionsObject = new JSONObject();
            dimensionsObject.put("width", artifact.getDimensions().getWidth());
            dimensionsObject.put("length", artifact.getDimensions().getLength());
            dimensionsObject.put("height", artifact.getDimensions().getHeight());
            jsonObject.put("dimensions", dimensionsObject);
            jsonObject.put("weight", artifact.getWeight());

            JSONArray tagsArray = new JSONArray(artifact.getTags());
            jsonObject.put("tags", tagsArray);
            jsonArray.put(jsonObject);
        }

        Files.write(path, jsonArray.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }


}