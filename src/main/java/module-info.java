module edu.cotarelo.audioavanzado {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens edu.cotarelo.audioavanzado to javafx.fxml;
    exports edu.cotarelo.audioavanzado;
}
