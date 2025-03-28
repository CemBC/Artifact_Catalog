package com.example.artifact_catalog;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI_Manager extends Application {
    private Catalog catalog =new Catalog();
    private final String[] tags = {"tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8", "tag9"};
    private CheckBox[] tagCheckBoxes = new CheckBox[tags.length];
    //private SearchManager searchManager = new SearchManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Artifact Manager");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        Button loadButton = new Button("Load Artifacts");
        ListView<String> listView = new ListView<>();
        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(200); // ScrollPane'in yüksekliğini ayarlıyoruz

        loadButton.setOnAction(e -> {  //load tuşuna basınca jsondaki verileri listview içine atıyor
            try {
                catalog.loadArtifactsFromFile();  //burdaki methodda okuyor verileri
                List<Artifact> artifacts = catalog.getArtifacts();
                listView.getItems().clear();
                for (Artifact artifact : artifacts) {
                    listView.getItems().add(artifact.getArtifactName() + " (" + artifact.getArtifactId() + ")");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search terms (comma-separated)");

        HBox tagBox = new HBox(10); // 10 piksel boşluk ile
        tagBox.setPadding(new Insets(10));
        for (int i = 0; i < tags.length; i++) {
            tagCheckBoxes[i] = new CheckBox(tags[i]);
            tagBox.getChildren().add(tagCheckBoxes[i]);
        }

        Button searchButton = new Button("Search");

        Button addButton = new Button("Add Artifact");
        addButton.setOnAction(e -> showAddArtifactDialog());  //add penceresi açıyor

        Button removeButton = new Button("Remove Artifact");
        removeButton.setDisable(true); // Butonu devre dışı bırakıyoruz

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeButton.setDisable(newValue == null);
        });

        removeButton.setOnAction(e -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String artifactId = selectedItem.substring(selectedItem.indexOf('(') + 1, selectedItem.indexOf(')'));
                catalog.removeArtifact(artifactId);
                listView.getItems().remove(selectedItem);
            }
        });

        Button editButton = new Button("Edit Artifact");
        editButton.setDisable(true); // Butonu devre dışı bırakıyoruz

        HBox buttonBox = new HBox(10); // 10 piksel boşluk ile
        buttonBox.getChildren().addAll(addButton, removeButton, editButton);

        vbox.getChildren().addAll(loadButton, searchField, tagBox, scrollPane, buttonBox);

        Scene scene = new Scene(vbox, 600, 400);
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

        GridPane tagGrid = new GridPane();
        tagGrid.setPadding(new Insets(10));
        tagGrid.setHgap(10);
        tagGrid.setVgap(10);
        CheckBox[] tagCheckBoxes = new CheckBox[tags.length];
        for (int i = 0; i < tags.length; i++) {
            tagCheckBoxes[i] = new CheckBox(tags[i]);
            tagGrid.add(tagCheckBoxes[i], i % 3, i / 3);
        }
        grid.add(new Label("Tags:"), 0, 12);
        grid.add(tagGrid, 1, 12);

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
            Dimensions dimensions = new Dimensions();
            dimensions.setWidth(Double.parseDouble(widthField.getText()));
            dimensions.setLength(Double.parseDouble(lengthField.getText()));
            dimensions.setHeight(Double.parseDouble(heightField.getText()));
            artifact.setDimensions(dimensions);
            artifact.setWeight(Double.parseDouble(weightField.getText()));

            List<String> selectedTags = new ArrayList<>();
            for (CheckBox checkBox : tagCheckBoxes) {
                if (checkBox.isSelected()) {
                    selectedTags.add(checkBox.getText());
                }
            }
            artifact.setTags(selectedTags);

            catalog.addArtifact(artifact);  //catalogtaki listemizin içine eklenen artifactı ekliyor
            dialog.close();
        });

        grid.add(saveButton, 1, 13);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportWidth(400);
        scrollPane.setPrefViewportHeight(400);

        Scene scene = new Scene(scrollPane, 400, 400);
        dialog.setScene(scene);
        dialog.show();
    }


    public static void main(String[] args) {
        launch(args);

    }
}