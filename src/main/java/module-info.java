module com.example.artifact_catalog {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.artifact_catalog to javafx.fxml;
    exports com.example.artifact_catalog;
}