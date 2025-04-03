package com.example.artifact_catalog;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class GUI_Manager extends Application {
    private Catalog catalog = new Catalog();
    private final String[] tags = {"tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8", "tag9"};
    private CheckBox[] tagCheckBoxes = new CheckBox[tags.length];
    private final String  path = System.getProperty("user.home") + "/Documents/artifacts.json";
    private final String downloadsPath = System.getProperty("user.home") + "/Downloads/exported_artifacts.json";
    //private SearchManager searchManager = new SearchManager();

    @Override
    public void start(Stage primaryStage) {
        showWelcomeScreen(primaryStage);
    }

    private void showWelcomeScreen(Stage primaryStage) {
        Stage welcomeStage = new Stage();
        welcomeStage.setResizable(false);
        welcomeStage.setTitle("Artifact Catalog");

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("ARTIFACT CATALOG PROJECT");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Times New Roman'; -fx-text-fill: #2c3e50;");
        titleLabel.setPadding(new Insets(10));

        BorderPane titlePane = new BorderPane(titleLabel);
        titlePane.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        titlePane.setPadding(new Insets(10));

        Label welcomeLabel = new Label("WELCOME");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Vladimir Script'; -fx-text-fill: #066fa0; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);");

        Label authorsLabel = new Label("PREPARED BY:\nOrkun Efe Özdemir\nCem Başar Ceylani\nBetül Özsan\nAleyna Kök");
        authorsLabel.setStyle("-fx-font-size: 14px; -fx-font-family: 'Times New Roman'; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        authorsLabel.setTextAlignment(TextAlignment.CENTER);
        authorsLabel.setPadding(new Insets(10));

        BorderPane authorsPane = new BorderPane(authorsLabel);
        authorsPane.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        authorsPane.setPadding(new Insets(10));
        authorsPane.setMaxWidth(250);

        Button continueButton = new Button("CONTINUE");
        continueButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-font-family: 'Times New Roman';");
        continueButton.setOnAction(e -> {
            welcomeStage.close();
            showMainScreen(primaryStage);
        });

        vbox.getChildren().addAll(titlePane, welcomeLabel, authorsPane, continueButton);

        Scene scene = new Scene(vbox, 400, 400);
        welcomeStage.setScene(scene);
        welcomeStage.show();
    }

    private void showMainScreen(Stage primaryStage) {
        File_Manager fileManager = new File_Manager();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Artifact Manager");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        Button importButton = new Button("Import data"); //Import Button yerini değiştirebilirsiniz

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();  //File Chooser ile yeni aktarılacak dosya seçiliyor
            fileChooser.setTitle("Select JSON File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                try {
                    List<Artifact> ImportedData = fileManager.readArtifactsFromFile(selectedFile.getAbsolutePath()); //Pathı al ve Pathteki dosya içeriğini listeye aktar
                    fileManager.writeAllArtifactsToFile(path, ImportedData); //Listeyi belgelerde zaten hazır açılı olan json dosyasına aktar
                    Files.delete(selectedFile.toPath());  //Seçilen dosyayı sil
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Button exportButton = new Button("Export data");

        exportButton.setOnAction(e -> {
            try {
                List<Artifact> exportedData = fileManager.readArtifactsFromFile(path); //Belgelerdeki json dosyasını oku ve listeye aktar
                fileManager.writeAllArtifactsToFile(downloadsPath, exportedData);   //Dosyayı Downloads'a aktar
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText(null);
                alert.setContentText("Data exported to: " + downloadsPath);
                alert.initModality(Modality.NONE);  //Modality.NONE olunca alerti kapatmana gerek yok
                alert.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button loadButton = new Button("Load Artifacts");
        ListView<String> listView = new ListView<>();

        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(200); // ScrollPane'in yüksekliğini ayarlıyoruz

        // ListView'a selection listener ekliyoruz
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  //Çift tıklama ile edit butonunu etkinleştirme eklendi
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
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
                List<Artifact> artifacts = catalog.getFilteredArtifacts(data, selectedTags); //Search Butonu yerine load butonuna search mantığı uygulandı
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

        vbox.getChildren().addAll(loadButton, importButton,exportButton, searchField, tagBox, scrollPane, buttonBox);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddArtifactDialog() {
        Stage dialog = new Stage();
        dialog.setResizable(false);
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
        TextField imagePathField = new TextField();
        Button selectImageButton = new Button("Select Image");

        selectImageButton.setOnAction(e -> {
            selectImage(dialog, imagePathField);
        });

        TextField[] fields = {idField, nameField, categoryField, civilizationField, locationField, compositionField, dateField, placeField, widthField, lengthField, heightField, weightField, imagePathField};

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
        grid.add(new Label("Image Path:"), 0, 12);
        grid.add(imagePathField, 1, 12);
        grid.add(selectImageButton, 2, 12);

        GridPane tagGrid = new GridPane();
        tagGrid.setPadding(new Insets(10));
        tagGrid.setHgap(10);
        tagGrid.setVgap(10);
        CheckBox[] tagCheckBoxes = new CheckBox[tags.length];
        for (int i = 0; i < tags.length; i++) {
            tagCheckBoxes[i] = new CheckBox(tags[i]);
            tagGrid.add(tagCheckBoxes[i], i % 3, i / 3);
        }
        grid.add(new Label("Tags:"), 0, 13);
        grid.add(tagGrid, 1, 13);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveArtifact(fields, tagCheckBoxes, dialog, false));

        grid.add(saveButton, 1, 14);

        ;

        Scene scene = new Scene(grid, 400, 650);
        dialog.setScene(scene);
        dialog.show();
    }


    private void showArtifactDetails(String artifactId) {
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.setTitle("Artifact Details");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Artifact selectedArtifact = catalog.findArtifactById(artifactId);

        if (selectedArtifact != null) {
            TextField idField = new TextField(selectedArtifact.getArtifactId());
            TextField nameField = new TextField(selectedArtifact.getArtifactName());
            TextField categoryField = new TextField(selectedArtifact.getCategory());
            TextField civilizationField = new TextField(selectedArtifact.getCivilization());
            TextField locationField = new TextField(selectedArtifact.getDiscoveryLocation());
            TextField compositionField = new TextField(selectedArtifact.getComposition());
            TextField dateField = new TextField(selectedArtifact.getDiscoveryDate());
            TextField placeField = new TextField(selectedArtifact.getCurrentPlace());
            TextField widthField = new TextField(String.valueOf(selectedArtifact.getDimensions().getWidth()));
            TextField lengthField = new TextField(String.valueOf(selectedArtifact.getDimensions().getLength()));
            TextField heightField = new TextField(String.valueOf(selectedArtifact.getDimensions().getHeight()));
            TextField weightField = new TextField(String.valueOf(selectedArtifact.getWeight()));
            TextField imagePathField = new TextField(selectedArtifact.getImagePath());
            Button selectImageButton = new Button("Select Image");

            selectImageButton.setOnAction(e -> {
                selectImage(dialog, imagePathField);
            });

            ImageView imageView = new ImageView();
            if (!selectedArtifact.getImagePath().isEmpty()) {
                imageView.setImage(new Image("file:" + selectedArtifact.getImagePath()));
            }
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);

            TextField[] fields = {idField, nameField, categoryField, civilizationField, locationField, compositionField, dateField, placeField, widthField, lengthField, heightField, weightField, imagePathField};

            for (TextField field : fields) {
                field.setEditable(false);
            }
            selectImageButton.setDisable(true);
            imagePathField.setEditable(false);

            CheckBox[] tagCheckBoxes = new CheckBox[tags.length];
            for (int i = 0; i < tags.length; i++) {
                tagCheckBoxes[i] = new CheckBox(tags[i]);
                tagCheckBoxes[i].setDisable(true);
                if (selectedArtifact.getTags().contains(tags[i])) {
                    tagCheckBoxes[i].setSelected(true);
                }
            }

            grid.add(imageView, 0, 0, 2, 1);
            grid.add(new Label("ID:"), 0, 1);
            grid.add(idField, 1, 1);
            grid.add(new Label("Name:"), 0, 2);
            grid.add(nameField, 1, 2);
            grid.add(new Label("Category:"), 0, 3);
            grid.add(categoryField, 1, 3);
            grid.add(new Label("Civilization:"), 0, 4);
            grid.add(civilizationField, 1, 4);
            grid.add(new Label("Location:"), 0, 5);
            grid.add(locationField, 1, 5);
            grid.add(new Label("Composition:"), 0, 6);
            grid.add(compositionField, 1, 6);
            grid.add(new Label("Discovery Date:"), 0, 7);
            grid.add(dateField, 1, 7);
            grid.add(new Label("Current Place:"), 0, 8);
            grid.add(placeField, 1, 8);
            grid.add(new Label("Width:"), 0, 9);
            grid.add(widthField, 1, 9);
            grid.add(new Label("Length:"), 0, 10);
            grid.add(lengthField, 1, 10);
            grid.add(new Label("Height:"), 0, 11);
            grid.add(heightField, 1, 11);
            grid.add(new Label("Weight:"), 0, 12);
            grid.add(weightField, 1, 12);
            grid.add(new Label("Image Path:"), 0, 13);
            grid.add(imagePathField, 1, 13);
            grid.add(selectImageButton, 2, 13);

            GridPane tagGrid = new GridPane();
            tagGrid.setPadding(new Insets(10));
            tagGrid.setHgap(10);
            tagGrid.setVgap(10);
            for (int i = 0; i < tags.length; i++) {
                tagGrid.add(tagCheckBoxes[i], i % 3, i / 3);
            }
            grid.add(new Label("Tags:"), 0, 14);
            grid.add(tagGrid, 1, 14);

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> {
                for (TextField field : fields) {
                    if (field != idField && field != imagePathField) {
                        field.setEditable(true);
                    }
                }
                selectImageButton.setDisable(false);
                for (CheckBox checkBox : tagCheckBoxes) {
                    checkBox.setDisable(false);
                }
                editButton.setText("Save");
                editButton.setOnAction(saveEvent -> {
                    saveArtifact(fields, tagCheckBoxes, dialog, true);
                });
            });

            grid.add(editButton, 1, 15);

            ScrollPane scrollPane = new ScrollPane(grid);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setPrefViewportWidth(400);
            scrollPane.setPrefViewportHeight(400);

            Scene scene = new Scene(scrollPane, 400, 650);
            dialog.setScene(scene);
            dialog.show();
        }
    }

    private void saveArtifact(TextField[] fields, CheckBox[] tagCheckBoxes, Stage dialog, boolean mode) {
        boolean isValid = true;
        for (int i = 0; i < fields.length; i++) {
            fields[i].setStyle("");  //her save a basıldığında kırmızı çerçeveyi kaldırıyor , eğer düzelltiyse kırmızı çerçeve gözükmesin diye
            if (i != 12 && fields[i].getText().isEmpty()) {
                fields[i].setStyle("-fx-border-color: red;"); //boş ise kutu kırmızı çerçeveli olacak
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
            artifact.setImagePath(fields[12].getText());

            List<String> selectedTags = new ArrayList<>();
            for (CheckBox checkBox : tagCheckBoxes) {
                if (checkBox.isSelected()) {
                    selectedTags.add(checkBox.getText());
                }
            }
            artifact.setTags(selectedTags);
            if (mode) {
                catalog.editArtifact(artifact);
            } else {
                catalog.addArtifact(artifact);
            }
            dialog.close();
        }
    }

    private void selectImage(Stage dialog, TextField imagePathField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(dialog);
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}