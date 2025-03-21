package com.example.artifact_catalog;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GUI_Manager extends Application {

    private File_Manager fileManager = new File_Manager();
    private static final String FILE_PATH = System.getProperty("user.home") + "/Documents/artifacts.json";
    private String selectedImagePath = "";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Artifact Manager");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        Button loadButton = new Button("Load Artifacts");
        ListView<String> listView = new ListView<>();
        loadButton.setOnAction(e -> {
            try {
                List<Artifact> artifacts = fileManager.readArtifactsFromFile(FILE_PATH);
                listView.getItems().clear();
                for (Artifact artifact : artifacts) {
                    listView.getItems().add(artifact.getArtifactName() + " (" + artifact.getArtifactId() + ")");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button addButton = new Button("Add Artifact");
        addButton.setOnAction(e -> showAddArtifactDialog());

        vbox.getChildren().addAll(loadButton, listView, addButton);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddArtifactDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add Artifact");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField categoryField = new TextField();
        TextField civilizationField = new TextField();
        TextField locationField = new TextField();
        TextField compositionField = new TextField();
        TextField dateField = new TextField();
        TextField placeField = new TextField();
        TextField widthField = new TextField();
        TextField lengthField = new TextField();
        TextField heightField = new TextField();
        TextField weightField = new TextField();
        TextField tagsField = new TextField();
        Button imageButton = new Button("Select Image");

        imageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
            );
            File selectedFile = fileChooser.showOpenDialog(dialog);
            if (selectedFile != null) {
                selectedImagePath = selectedFile.getAbsolutePath();
            }
        });

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryField, 1, 2);
        grid.add(new Label("Civilization:"), 0, 3);
        grid.add(civilizationField, 1, 3);
        grid.add(new Label("Location:"), 0, 4);
        grid.add(locationField, 1, 4);
        grid.add(new Label("Composition:"), 0, 5);
        grid.add(compositionField, 1, 5);
        grid.add(new Label("Discovery Date:"), 0, 6);
        grid.add(dateField, 1, 6);
        grid.add(new Label("Current Place:"), 0, 7);
        grid.add(placeField, 1, 7);
        grid.add(new Label("Width:"), 0, 8);
        grid.add(widthField, 1, 8);
        grid.add(new Label("Length:"), 0, 9);
        grid.add(lengthField, 1, 9);
        grid.add(new Label("Height:"), 0, 10);
        grid.add(heightField, 1, 10);
        grid.add(new Label("Weight:"), 0, 11);
        grid.add(weightField, 1, 11);
        grid.add(new Label("Tags (comma-separated):"), 0, 12);
        grid.add(tagsField, 1, 12);
        grid.add(new Label("Image:"), 0, 13);
        grid.add(imageButton, 1, 13);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            Artifact artifact = new Artifact();
            artifact.setArtifactId(idField.getText());
            artifact.setArtifactName(nameField.getText());
            artifact.setCategory(categoryField.getText());
            artifact.setCivilization(civilizationField.getText());
            artifact.setDiscoveryLocation(locationField.getText());
            artifact.setComposition(compositionField.getText());
            artifact.setDiscoveryDate(dateField.getText());
            artifact.setCurrentPlace(placeField.getText());
            artifact.setImagePath(selectedImagePath);
            Dimensions dimensions = new Dimensions();
            dimensions.setWidth(Double.parseDouble(widthField.getText()));
            dimensions.setLength(Double.parseDouble(lengthField.getText()));
            dimensions.setHeight(Double.parseDouble(heightField.getText()));
            artifact.setDimensions(dimensions);
            artifact.setWeight(Double.parseDouble(weightField.getText()));
            artifact.setTags(List.of(tagsField.getText().split(",")));

            try {
                fileManager.writeArtifactsToFile(FILE_PATH, artifact);
                dialog.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        grid.add(saveButton, 1, 14);

        Scene scene = new Scene(grid, 400, 450);
        dialog.setScene(scene);
        dialog.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}