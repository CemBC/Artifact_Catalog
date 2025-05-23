package com.example.artifact_catalog;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class GUI_Manager extends Application {
    private Catalog catalog = new Catalog();
    private final String[] tags = {"domestic", "fragmented", "funerary", "inscribed", "jewelery", "mythological", "reconstructed", "religious", "weaponry"};
    private CheckBox[] tagCheckBoxes = new CheckBox[tags.length];
    private static final String path = "artifacts.json";
    private final String downloadsPath = System.getProperty("user.home") + "/Downloads/exported_artifacts.json";
    private final String datePattern = "^(0[1-9]|[12][0-9]|3[01]).(0[1-9]|1[0-2]).\\d{4}$";  //date için regex 01.01.2001
    @Override
    public void start(Stage primaryStage) {
        System.out.println("Control statement application started...");
        showWelcomeScreen(primaryStage);
    }

    private void showWelcomeScreen(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Artifact Catalog");

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
            primaryStage.close();
            showMainScreen(primaryStage);
        });


        Button helpButton = new Button("HELP");
        helpButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-font-family: 'Times New Roman';");
        helpButton.setOnAction(e -> {
            try (BufferedReader rd = new BufferedReader(
                    new InputStreamReader(getClass().getClassLoader().getResourceAsStream("orkun.txt")))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
                Stage stage2 = new Stage();
                stage2.setTitle("Help");

                TextArea textArea = new TextArea(content.toString());
                textArea.setEditable(false);
                textArea.setWrapText(true);
                ScrollPane scrollPane = new ScrollPane(textArea);
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);

                BorderPane root2 = new BorderPane(scrollPane);
                Scene scene2 = new Scene(root2, 600, 400);
                stage2.setResizable(false);
                stage2.setScene(scene2);
                stage2.show();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not load help file");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });




        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(continueButton, helpButton);

        vbox.getChildren().addAll(titlePane, welcomeLabel, authorsPane, buttonBox);






        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainScreen(Stage primaryStage) {
        File_Manager fileManager = new File_Manager();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Artifact Manager");

        BorderPane root = new BorderPane();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        root.setCenter(vbox);

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem importButton = new MenuItem("Import Data");
        MenuItem exportButton= new MenuItem("Export Data");
        root.setTop(menuBar);




        importButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Import Warning");
            alert.setHeaderText(null);
            alert.setContentText("Invalid tags that are in the imported json file will be deleted.");
            ButtonType agreeButton = new ButtonType("I Agree", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(agreeButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == agreeButton) {
                    FileChooser fileChooser = new FileChooser();  // File Chooser ile yeni aktarılacak dosya seçiliyor
                    fileChooser.setTitle("Select JSON File");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                    File selectedFile = fileChooser.showOpenDialog(primaryStage);
                    if (selectedFile != null) {
                        try {
                            List<Artifact> ImportedData = fileManager.readArtifactsFromFile(selectedFile.getAbsolutePath()); // Path'i al ve Path'teki dosya içeriğini listeye aktar
                            fileManager.writeAllArtifactsToFile(path, ImportedData); // Listeyi belgelerde zaten hazır açılı olan json dosyasına aktar
                            Files.delete(selectedFile.toPath());  // Seçilen dosyayı sil
                            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

                            showNotification(root , "Import successful!");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        });



        exportButton.setOnAction(e -> {
            try {
                List<Artifact> exportedData = fileManager.readArtifactsFromFile(path); //Belgelerdeki json dosyasını oku ve listeye aktar
                fileManager.writeAllArtifactsToFile(downloadsPath, exportedData);   //Dosyayı Downloads'a aktar
                showNotification(root,"Export successful!\nExported to (" + downloadsPath + ")");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button loadButton = new Button("Load Artifacts");

        TableView<Artifact> tableView = new TableView<>();

        TableColumn<Artifact, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("artifactId"));

        TableColumn<Artifact, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("artifactName"));

        TableColumn<Artifact, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("discoveryDate"));

        tableView.getColumns().addAll(idColumn, nameColumn, dateColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(200); // ScrollPane'in yüksekliğini ayarlıyoruz

        // tableView'a selection listener ekliyoruz
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  //Çift tıklama ile edit butonunu etkinleştirme eklendi
                Artifact selectedArtifact = tableView.getSelectionModel().getSelectedItem();
                if (selectedArtifact != null) {
                    showArtifactDetails(selectedArtifact.getArtifactId());
                }
            }
        });

        TextField searchField = new TextField(); //SearchField LoadButton dan önce oluşturuldu ki datayı çekebilelim
        searchField.setPromptText("Enter search terms (comma-separated)");


        loadButton.setOnAction(e -> {
            String data = searchField.getText();
            List<String> selectedTags = new ArrayList<>();
            for (CheckBox checkBox : tagCheckBoxes) {
                if (checkBox.isSelected()) {
                    selectedTags.add(checkBox.getText());
                }
            }
            try {
                catalog.loadArtifactsFromFile();
                List<Artifact> artifacts = catalog.getFilteredArtifacts(data, selectedTags);
                tableView.getItems().clear();
                tableView.getItems().addAll(artifacts);
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
        addButton.setOnAction(e -> showAddArtifactDialog());

        Button removeButton = new Button("Remove Artifact");
        removeButton.setDisable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeButton.setDisable(newValue == null);
        });

        removeButton.setOnAction(e -> {
            Artifact selectedArtifact = tableView.getSelectionModel().getSelectedItem();
            if (selectedArtifact != null) {
                catalog.removeArtifact(selectedArtifact.getArtifactId());
                tableView.getItems().remove(selectedArtifact);
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            primaryStage.close();
            showWelcomeScreen(primaryStage);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, removeButton, spacer, backButton);

        HBox topButtonBox = new HBox(10);
        topButtonBox.getChildren().addAll(loadButton);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        fileMenu.getItems().addAll(importButton, exportButton);
        menuBar.getMenus().add(fileMenu);
        vbox.getChildren().addAll(topButtonBox, searchField, tagBox, scrollPane, buttonBox);

        Scene scene = new Scene(root, 830, 500);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void configureErrorLabel(Label errorLabel) {
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 0.9em;");
            errorLabel.setManaged(false);
            errorLabel.setVisible(false);
            errorLabel.setPadding(new Insets(2, 0, 0, 0));
    }


        private void addNumericValidationListener(TextField textField, Label errorLabel) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                String text = (newValue == null) ? "" : newValue.trim();
                if (text.isEmpty()) {
                    textField.setStyle("");
                    errorLabel.setText("");
                    errorLabel.setManaged(false);
                    errorLabel.setVisible(false);
                    return;
                }
                try {
                    Double.parseDouble(text);
                    textField.setStyle("");
                    errorLabel.setText("");
                    errorLabel.setManaged(false);
                    errorLabel.setVisible(false);
                } catch (NumberFormatException e) {
                    textField.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                    errorLabel.setText("Must be a numeric value.");
                    errorLabel.setManaged(true);
                    errorLabel.setVisible(true);
                }
            });
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
        idField.setEditable(false);
        TextField nameField = new TextField();
        TextField categoryField = new TextField();
        TextField civilizationField = new TextField();
        TextField locationField = new TextField();
        TextField compositionField = new TextField();
        TextField dateField = new TextField();
        DatePicker datePicker = new DatePicker();
        datePicker.setEditable(false);
        TextField placeField = new TextField();

        TextField widthField = new TextField();
        Label widthErrorLabel = new Label();
        configureErrorLabel(widthErrorLabel);
        addNumericValidationListener(widthField, widthErrorLabel);

        TextField lengthField = new TextField();
        Label lengthErrorLabel = new Label();
        configureErrorLabel(lengthErrorLabel);
        addNumericValidationListener(lengthField, lengthErrorLabel);

        TextField heightField = new TextField();
        Label heightErrorLabel = new Label();
        configureErrorLabel(heightErrorLabel);
        addNumericValidationListener(heightField, heightErrorLabel);

        TextField weightField = new TextField();
        Label weightErrorLabel = new Label();
        configureErrorLabel(weightErrorLabel);
        addNumericValidationListener(weightField, weightErrorLabel);

        TextField imagePathField = new TextField();
        Button selectImageButton = new Button("Select Image");

        selectImageButton.setOnAction(e -> {
            selectImage(dialog, imagePathField);
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dateField.setText(newValue.format(formatter));
            }
        });
        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setOpacity(0);


        nameField.textProperty().addListener((observable, oldValue, newValue) -> updateIdField(nameField, dateField, idField));
        dateField.textProperty().addListener((observable, oldValue, newValue) -> updateIdField(nameField, dateField, idField));


        TextField[] fields = {idField, nameField, categoryField, civilizationField, locationField, compositionField, placeField, widthField, lengthField, heightField, weightField, imagePathField , dateField};

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
        grid.add(datePicker , 1 , 6);
        grid.add(new Label("Discovery Date:"), 0, 7);
        grid.add(dateField, 1, 7);
        grid.add(new Label("Current Place:"), 0, 8);
        grid.add(placeField, 1, 8);


        grid.add(new Label("Width:"), 0, 9);
        VBox widthBox = new VBox(2, widthField, widthErrorLabel);
        grid.add(widthBox, 1, 9);

        grid.add(new Label("Length:"), 0, 10);
        VBox lengthBox = new VBox(2, lengthField, lengthErrorLabel);
        grid.add(lengthBox, 1, 10);

        grid.add(new Label("Height:"), 0, 11);
        VBox heightBox = new VBox(2, heightField, heightErrorLabel);
        grid.add(heightBox, 1, 11);

        grid.add(new Label("Weight:"), 0, 12);
        VBox weightBox = new VBox(2, weightField, weightErrorLabel);
        grid.add(weightBox, 1, 12);
        // --- DÜZELTİLMİŞ KISIM SONU ---

        grid.add(new Label("Image Path:"), 0, 13);
        grid.add(imagePathField, 1, 13);
        grid.add(selectImageButton, 2, 13);

        datePicker.prefWidthProperty().bind(dateField.widthProperty());

        GridPane tagGrid = new GridPane();
        tagGrid.setPadding(new Insets(10));
        tagGrid.setHgap(10);
        tagGrid.setVgap(10);
        CheckBox[] tagCheckBoxes = new CheckBox[tags.length];
        for (int i = 0; i < tags.length; i++) {
            tagCheckBoxes[i] = new CheckBox(tags[i]);
            tagGrid.add(tagCheckBoxes[i], i % 2, i / 2);
        }
        grid.add(new Label("Tags:"), 0, 14);
        grid.add(tagGrid, 1, 14);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveArtifact(fields, tagCheckBoxes, dialog, false));

        grid.add(saveButton, 1, 15);
        Scene scene = new Scene(grid, 430, 730);

        dialog.setScene(scene);
        dialog.show();
    }



    private void updateIdField(TextField nameField, TextField dateField, TextField idField) {
        String name = nameField.getText().trim();
        String date = dateField.getText().trim();
        if (!name.isEmpty() && !date.isEmpty() && date.matches(datePattern)) {
            try {
                String year = date.split("\\.")[2];
                idField.setText(year + "_" + name);
            } catch (Exception e) {
                idField.setText("");
            }
        } else {
            idField.setText("");
        }
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
            DatePicker datePicker = new DatePicker();
            TextField dateField = new TextField(selectedArtifact.getDiscoveryDate());

            //datePicker.setValue(LocalDate.parse(selectedArtifact.getDiscoveryDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            TextField placeField = new TextField(selectedArtifact.getCurrentPlace());
            TextField widthField = new TextField(String.valueOf(selectedArtifact.getDimensions().getWidth()));
            TextField lengthField = new TextField(String.valueOf(selectedArtifact.getDimensions().getLength()));
            TextField heightField = new TextField(String.valueOf(selectedArtifact.getDimensions().getHeight()));
            TextField weightField = new TextField(String.valueOf(selectedArtifact.getWeight()));
            TextField imagePathField = new TextField(selectedArtifact.getImagePath());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    dateField.setText(newValue.format(formatter));
                }
            });

            datePicker.getEditor().setOpacity(0);


            Button selectImageButton = new Button("Select Image");

            selectImageButton.setOnAction(e -> {
                selectImage(dialog, imagePathField);
            });

            Button showImageButton = new Button("Image"); //Image butonu eklendi
            showImageButton.setOnAction(e -> {
                showImage(selectedArtifact.getImagePath());
            });
            showImageButton.setDisable(selectedArtifact.getImagePath().isEmpty());

            TextField[] fields = {idField, nameField, categoryField, civilizationField, locationField, compositionField, placeField, widthField, lengthField, heightField, weightField, imagePathField , dateField};

            for (TextField field : fields) {
                field.setEditable(false);
            }
            datePicker.setDisable(true);
            datePicker.setEditable(false);
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

            grid.add(showImageButton, 0, 0, 2, 1);
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
            grid.add(datePicker , 1, 7);
            grid.add(new Label("Discovery Date:"), 0, 8);
            grid.add(dateField, 1, 8);
            grid.add(new Label("Current Place:"), 0, 9);
            grid.add(placeField, 1, 9);
            grid.add(new Label("Width:"), 0, 10);
            grid.add(widthField, 1, 10);
            grid.add(new Label("Length:"), 0, 11);
            grid.add(lengthField, 1, 11);
            grid.add(new Label("Height:"), 0, 12);
            grid.add(heightField, 1, 12);
            grid.add(new Label("Weight:"), 0, 13);
            grid.add(weightField, 1, 13);
            grid.add(new Label("Image Path:"), 0, 14);
            grid.add(imagePathField, 1, 14);
            grid.add(selectImageButton, 2, 14);



            GridPane tagGrid = new GridPane();
            tagGrid.setPadding(new Insets(10));
            tagGrid.setHgap(10);
            tagGrid.setVgap(10);
            for (int i = 0; i < tags.length; i++) {
                tagGrid.add(tagCheckBoxes[i], i % 2, i / 2);
            }
            grid.add(new Label("Tags:"), 0, 15);
            grid.add(tagGrid, 1, 15);

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
                datePicker.setDisable(false);
                editButton.setText("Save");
                editButton.setOnAction(saveEvent -> {
                    saveArtifact(fields, tagCheckBoxes, dialog, true);
                });
            });

            grid.add(editButton, 1, 16);

            datePicker.prefWidthProperty().bind(dateField.widthProperty());

            ScrollPane scrollPane = new ScrollPane(grid);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setPrefViewportWidth(400);
            scrollPane.setPrefViewportHeight(400);

            Scene scene = new Scene(scrollPane, 450, 750);
            dialog.setScene(scene);
            dialog.show();
        }
    }

    private void showImage(String imagePath) { //Image butonuna basınca açılan pencere
        File imageFile = new File(imagePath);
        if (!imageFile.exists() || imageFile.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Image Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No image found on the specified path.");
            alert.showAndWait();
            return;
        }
        Stage imageStage = new Stage();
        imageStage.setTitle("Image Viewer");

        ImageView imageView = new ImageView();
        Image image = new Image("file:" + imagePath);
        imageView.setImage(image);

        double imageWidth = image.getWidth();    //Pixel boyutunu alıyoruz
        double imageHeight = image.getHeight();

        imageView.setFitWidth(imageWidth);   //Ekranın pixel boyutunu image in pixel boyutuna eşitliyoruz
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(true);



        imageView.setOnScroll(event -> {
            double delta = event.getDeltaY();  //Fare tekerliğinin hangi yönde ne kadar döndüğü
            double scale = imageView.getScaleX() + delta / 300;  //Scroll hızını ayarlıyoruz
            scale = Math.max(0.1, Math.min(scale, 10));  //ölçeklendirme sınırları maximum nereye kadar zoom in zoom out yapabileceğimiz yani
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
        });
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                imageView.setScaleX(1);
                imageView.setScaleY(1);
            }
        });

        Scene scene = new Scene(new StackPane(imageView), imageWidth, imageHeight);
        imageStage.setScene(scene);
        imageStage.show();
    }

    private void saveArtifact(TextField[] fields, CheckBox[] tagCheckBoxes, Stage dialog ,  boolean mode) {
        boolean isValid = true;
        for (int i = 0; i < fields.length; i++) {
            fields[i].setStyle("");  //her save a basıldığında kırmızı çerçeveyi kaldırıyor , eğer düzelltiyse kırmızı çerçeve gözükmesin diye
            if (i != 11 && fields[i].getText().isEmpty()) {
                fields[i].setStyle("-fx-border-color: red;"); //boş ise kutu kırmızı çerçeveli olacak
                isValid = false;
            }
        }
        if (fields[12].getText().isEmpty() || !fields[12].getText().matches(datePattern)) {//|| !datePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).matches(datePattern)) {
            fields[12].setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            fields[12].setStyle("");
        }
        if(!mode) {
            Artifact selectedArtifact = catalog.findArtifactById(fields[0].getText());
            if (selectedArtifact != null) {
                fields[0].setStyle("-fx-border-color: red;");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ID Conflict");
                alert.setHeaderText(null);
                alert.setContentText("There is already exist a artifact with ID = " + fields[0].getText());
                alert.showAndWait();
                isValid = false;
            } else {
                fields[0].setStyle("");
            }
        }

        int[] floatFieldIndices = {7, 8, 9, 10};  //width,length,height,weight ' in float değer olup olmadığını kontrol ediyor
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
            artifact.setDiscoveryDate(fields[12].getText());
            artifact.setCurrentPlace(fields[6].getText());
            Dimensions dimensions = new Dimensions();
            dimensions.setWidth(Double.parseDouble(fields[7].getText()));
            dimensions.setLength(Double.parseDouble(fields[8].getText()));
            dimensions.setHeight(Double.parseDouble(fields[9].getText()));
            artifact.setDimensions(dimensions);
            artifact.setWeight(Double.parseDouble(fields[10].getText()));
            artifact.setImagePath(fields[11].getText());

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
            Image image = new Image("file:" + selectedFile.getAbsolutePath());
            double maxWidth = 1280; // Yeni maksimum genişlik
            double maxHeight = 720;// Maksimum yükseklik
            if (image.getWidth() > maxWidth || image.getHeight() > maxHeight) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Image Size Warning");
                alert.setHeaderText(null);
                alert.setContentText("Selected image is too large. Please select an image smaller than " + (int)maxWidth + "x" + (int)maxHeight + " pixels.");
                alert.showAndWait();
            } else {
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showNotification(Node node, String message) {
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setStyle(""" 
        -fx-background-color: #27ae60;
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-padding: 10px 20px;
        -fx-background-radius: 8px;
        -fx-border-radius: 8px;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 4, 0.5, 2, 2);
        """);

        popup.getContent().add(label);

        Window window = node.getScene().getWindow();
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();

        double x = screenBounds.getMaxX() - 320;
        double y = screenBounds.getMinY() + 40;

        popup.show(window, x, y);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(2));
        fade.setOnFinished(e -> popup.hide());
        fade.play();
    }



}




