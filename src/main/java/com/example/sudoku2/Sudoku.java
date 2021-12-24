package com.example.sudoku2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Sudoku extends Application {

    Board board;

    Group root = new Group();
    Group BOARD = new Group();
    Group grid = new Group();
    Group mainMenu = new Group();
    Group controls = new Group();


    public void makeMenu() {
        Button someButton = new Button("Easy");
        VBox gameOptions = new VBox(someButton);
        gameOptions.setLayoutX(0);
        gameOptions.setLayoutY(0);
        controls.getChildren().add(gameOptions);
    }


    @Override
    public void start(Stage stage) throws IOException {
        makeMenu();
        root.getChildren().add(controls);
        FXMLLoader fxmlLoader = new FXMLLoader(Sudoku.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}