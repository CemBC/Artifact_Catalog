package com.example.artifact_catalog;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;

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

        // ListView'a selection listener ekliyoruz
        listView.setOnMouseClicked(event->{
            if(event.getClickCount()==2){  //Çift tıklama ile edit butonunu etkinleştirme eklendi
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem!=null){
                    String artifactId = selectedItem.substring(selectedItem.indexOf('(') + 1, selectedItem.indexOf(')'));
                    showArtifactDetails(artifactId);
                }
            }
        });

        TextField searchField = new TextField(); //SearchField LoadButton dan önce oluşturuldu ki datayı çekebilelim
        searchField.setPromptText("Enter search terms (comma-separated)");


        loadButton.setOnAction(e -> {//load tuşuna basınca jsondaki verileri listview içine atıyor
            String data = searchField.getText(); //datayı String olarak alıyor
            List<String> selectedTags = new ArrayList<>(); //seçilen tagları alıyor
            for (CheckBox checkBox : tagCheckBoxes) {
                if (checkBox.isSelected()) {
                    selectedTags.add(checkBox.getText());
                }
            }
            try {
                catalog.loadArtifactsFromFile();  //burdaki methodda okuyor verileri
                List<Artifact> artifacts = catalog.getFilteredArtifacts(data , selectedTags); //Search Butonu yerine load butonuna search mantığı uygulandı
                listView.getItems().clear();
                for (Artifact artifact : artifacts) {
                    listView.getItems().add(artifact.getArtifactName() + " (" + artifact.getArtifactId() + ")");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });



        HBox tagBox = new HBox(10); // 10 piksel boşluk ile
        tagBox.setPadding(new Insets(10));
        for (int i = 0; i < tags.length; i++) {
            tagCheckBoxes[i] = new CheckBox(tags[i]);
            tagBox.getChildren().add(tagCheckBoxes[i]);
        }



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

        HBox buttonBox = new HBox(10); // 10 piksel boşluk ile
        buttonBox.getChildren().addAll(addButton, removeButton);

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

        TextField[] fields = {idField, nameField, categoryField, civilizationField, locationField, compositionField, dateField, placeField, widthField, lengthField, heightField, weightField};

        for (int i = 0; i < fields.length; i++) {
            final int index = i;
            fields[i].setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER:
                    case DOWN:
                        if (index < fields.length - 1) {
                            fields[index + 1].requestFocus();
                        }
                        break;
                    case UP:
                        if (index > 0) {
                            fields[index - 1].requestFocus();
                        }
                        break;
                    default:
                        break;
                }
            });
        }

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
        saveButton.setOnAction(e -> {  //Save butonuna basınca hata varsa kırmızı çerçeve oluşturuyor
            saveArtifact(fields, tagCheckBoxes, dialog);
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

    private void showArtifactDetails(String artifactId) {
        Stage dialog = new Stage();
        dialog.setTitle("Artifact Details");

        TableView<Map.Entry<String, String>> table = new TableView<>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(500);
        table.setPrefHeight(400);

        TableColumn<Map.Entry<String, String>, String> attributeColumn = new TableColumn<>("Attribute");
        attributeColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        attributeColumn.setStyle("-fx-font-weight: bold;");

        TableColumn<Map.Entry<String, String>, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(event -> {
            Map.Entry<String, String> entry = event.getRowValue();
            entry.setValue(event.getNewValue());
        });

        table.getColumns().addAll(attributeColumn, valueColumn);

        // Seçilen artifact'ı bul
        Artifact selectedArtifact = catalog.findArtifactById(artifactId);


        if (selectedArtifact != null) {
            ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList();
            data.add(new AbstractMap.SimpleEntry<>("ID", selectedArtifact.getArtifactId()));
            data.add(new AbstractMap.SimpleEntry<>("Name", selectedArtifact.getArtifactName()));
            data.add(new AbstractMap.SimpleEntry<>("Category", selectedArtifact.getCategory()));
            data.add(new AbstractMap.SimpleEntry<>("Civilization", selectedArtifact.getCivilization()));
            data.add(new AbstractMap.SimpleEntry<>("Location", selectedArtifact.getDiscoveryLocation()));
            data.add(new AbstractMap.SimpleEntry<>("Composition", selectedArtifact.getComposition()));
            data.add(new AbstractMap.SimpleEntry<>("Discovery Date", selectedArtifact.getDiscoveryDate()));
            data.add(new AbstractMap.SimpleEntry<>("Current Place", selectedArtifact.getCurrentPlace()));
            data.add(new AbstractMap.SimpleEntry<>("Dimensions", String.format("Width: %.2f, Length: %.2f, Height: %.2f",
                    selectedArtifact.getDimensions().getWidth(),
                    selectedArtifact.getDimensions().getLength(),
                    selectedArtifact.getDimensions().getHeight())));
            data.add(new AbstractMap.SimpleEntry<>("Weight", String.format("%.2f", selectedArtifact.getWeight())));
            data.add(new AbstractMap.SimpleEntry<>("Tags", String.join(", ", selectedArtifact.getTags())));

            table.setItems(data);
        }


        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            if (editButton.getText().equals("Edit")) {
                table.setEditable(true);
                editButton.setText("Save");
            } else {
                if (selectedArtifact != null) {
                    for (Map.Entry<String, String> entry : table.getItems()) {
                        switch (entry.getKey()) {
                            case "ID":
                                selectedArtifact.setArtifactId(entry.getValue());
                                break;
                            case "Name":
                                selectedArtifact.setArtifactName(entry.getValue());
                                break;
                            case "Category":
                                selectedArtifact.setCategory(entry.getValue());
                                break;
                            case "Civilization":
                                selectedArtifact.setCivilization(entry.getValue());
                                break;
                            case "Location":
                                selectedArtifact.setDiscoveryLocation(entry.getValue());
                                break;
                            case "Composition":
                                selectedArtifact.setComposition(entry.getValue());
                                break;
                            case "Discovery Date":
                                selectedArtifact.setDiscoveryDate(entry.getValue());
                                break;
                            case "Current Place":
                                selectedArtifact.setCurrentPlace(entry.getValue());
                                break;
                            case "Dimensions":
                                String[] dimensions = entry.getValue().split(", ");
                                selectedArtifact.getDimensions().setWidth(Double.parseDouble(dimensions[0].split(": ")[1]));
                                selectedArtifact.getDimensions().setLength(Double.parseDouble(dimensions[1].split(": ")[1]));
                                selectedArtifact.getDimensions().setHeight(Double.parseDouble(dimensions[2].split(": ")[1]));
                                break;
                            case "Weight":
                                selectedArtifact.setWeight(Double.parseDouble(entry.getValue()));
                                break;
                            case "Tags":
                                selectedArtifact.setTags(List.of(entry.getValue().split(", ")));
                                break;
                        }
                    }
                    catalog.editArtifact(selectedArtifact);
                }
                dialog.close();
            }
        });

        VBox vbox = new VBox(table, editButton);
        Scene scene = new Scene(vbox, 500, 450);
        dialog.setScene(scene);
        dialog.show();
    }

    private void saveArtifact(TextField[] fields, CheckBox[] tagCheckBoxes, Stage dialog) {
        boolean isValid = true;
        for (TextField field : fields) {
            field.setStyle("");  //her save a basıldığında kırmızı çerçeveyi kaldırıyor , eğer düzelltiyse kırmızı çerçeve gözükmesin diye
            if (field.getText().isEmpty()) {
                field.setStyle("-fx-border-color: red;"); //boş ise kutu kırmızı çerçeveli olacak
                isValid = false;
            }
        }
        String datePattern = "^(0[1-9]|[12][0-9]|3[01]).(0[1-9]|1[0-2]).\\d{4}$";  //date için regex 01.01.2001
        if (!fields[6].getText().matches(datePattern)) {
            fields[6].setStyle("-fx-border-color: red;");
            isValid = false;
        }
        int[] floatFieldIndices = {8, 9, 10, 11};  //width,length,height,weight ' in float değer olup olmadığını kontrol ediyor
        for (int index : floatFieldIndices) {
            try {
                Float.parseFloat(fields[index].getText());
            } catch (NumberFormatException e) {
                fields[index].setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }
        if (isValid) {  //eğer sorun yoksa saveleme işlemine geçiyor
            Artifact artifact = new Artifact();
            artifact.setArtifactId(fields[0].getText());
            artifact.setArtifactName(fields[1].getText());
            artifact.setCategory(fields[2].getText());
            artifact.setCivilization(fields[3].getText());
            artifact.setDiscoveryLocation(fields[4].getText());
            artifact.setComposition(fields[5].getText());
            artifact.setDiscoveryDate(fields[6].getText());
            artifact.setCurrentPlace(fields[7].getText());
            Dimensions dimensions = new Dimensions();
            dimensions.setWidth(Double.parseDouble(fields[8].getText()));
            dimensions.setLength(Double.parseDouble(fields[9].getText()));
            dimensions.setHeight(Double.parseDouble(fields[10].getText()));
            artifact.setDimensions(dimensions);
            artifact.setWeight(Double.parseDouble(fields[11].getText()));

            List<String> selectedTags = new ArrayList<>();
            for (CheckBox checkBox : tagCheckBoxes) {
                if (checkBox.isSelected()) {
                    selectedTags.add(checkBox.getText());
                }
            }
            artifact.setTags(selectedTags);

            catalog.addArtifact(artifact);
            dialog.close();
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}