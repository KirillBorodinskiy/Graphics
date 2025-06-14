module org.borodkir.graphics {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens org.borodkir.graphics to javafx.fxml;
    exports org.borodkir.graphics;
}