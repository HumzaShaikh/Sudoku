module com.example.sudoku2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudoku2 to javafx.fxml;
    exports com.example.sudoku2;
}