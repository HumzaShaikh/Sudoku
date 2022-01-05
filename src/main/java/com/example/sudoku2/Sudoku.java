package com.example.sudoku2;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;

public class Sudoku extends Application {

    Board currentBoard;
    Font myFont = Font.loadFont("resources/futura_font.zip", 16);
    Group root = new Group();
    Group BOARD = new Group();
    Group grid = new Group();
    Group mainMenu = new Group();
    Group controls = new Group();
    Group prompts = new Group();

    int indexCol;
    int indexRow;
    Node[][] cells = new Node[9][9];


    public void makeMenu() {
        Label title = new Label("Sudoku");
        title.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 50));
        title.setUnderline(true);
        title.setStyle("-fx-text-fill: #4a483f");

        Label subtitle = new Label("Made by Humza Shaikh");
        subtitle.setFont(Font.font("Times new roman",FontWeight.SEMI_BOLD,25));


        Button startButton = new Button("Start");
        startButton.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 34));
        startButton.setPrefWidth(250);
        startButton.setStyle("-fx-background-color: NONE; -fx-background-radius: 19px; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #F4DFC0;" +
                "-fx-border-width: 4px;");
        startButton.setOnAction(actionEvent -> {
            difficultyMenu();
        });

        VBox menuElems = new VBox(title,subtitle,startButton);
        menuElems.setAlignment(Pos.CENTER);
        mainMenu.getChildren().addAll(menuElems);
    }

    public void difficultyMenu() {


        StackPane border = new StackPane();
        border.setStyle("-fx-border-width: 4px;" +
                "-fx-border-color: #4a483f;" +
                "-fx-background-color: NONE");
        border.setPrefWidth(725);
        border.setPrefHeight(775);
        border.setLayoutX(12.5);
        border.setLayoutY(12.5);

        Label title = new Label("Sudoku");
        title.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 55));
        title.setUnderline(true);
        title.setStyle("-fx-text-fill: #4a483f");
        title.setLayoutX(290);
        title.setLayoutY(45);

        Label subtitle = new Label("Made by Humza Shaikh");
        subtitle.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 20));
        subtitle.setStyle("-fx-text-fill: #4a483f");
        subtitle.setLayoutX(280);
        subtitle.setLayoutY(104);

        mainMenu.getChildren().clear();
        Button easy = new Button("Easy");
        diffButtonGraphics(easy, Difficulty.EASY);

        Button medium = new Button("Medium");
        diffButtonGraphics(medium, Difficulty.MEDIUM);

        Button hard = new Button("Hard");
        diffButtonGraphics(hard, Difficulty.HARD);

        Button instructions = new Button("Instructions");
        diffButtonGraphics(instructions, Difficulty.CUSTOM);
        VBox difficulties = new VBox();

        difficulties.getChildren().addAll(easy,medium,hard, instructions);
        difficulties.setSpacing(74);
        difficulties.setLayoutX(250);
        difficulties.setLayoutY(160);

        mainMenu.getChildren().add(border);
        controls.getChildren().addAll(difficulties,title, subtitle);
    }


    void diffButtonGraphics(Button button, Difficulty difficulty) {

        if (difficulty == Difficulty.CUSTOM) {
            Label instructions = new Label("Place the numbers 1 - 9 in the boxes \nsuch" +
                    " that all columns, rows and subsections \ncontain only one instance of each digit.");
            instructions.setFont(Font.font("Times new roman",FontWeight.NORMAL,20));

            instructions.setLayoutX(195);
            instructions.setLayoutY(675);
            instructions.setTextAlignment(TextAlignment.CENTER);

            button.setOnAction(actionEvent -> {
                if (controls.getChildren().contains(instructions)) {
                    controls.getChildren().remove(instructions);
                } else {
                    controls.getChildren().add(instructions);
                }

            });
        } else {
            button.setOnAction(actionEvent -> {
                makeGame(difficulty);
            });
        }

        button.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 34));
        button.setPrefWidth(250);
        button.setStyle("-fx-background-color: NONE; -fx-background-radius: 19px; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #F4DFC0;" +
                "-fx-border-width: 4px;");

        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
            button.setStyle("-fx-background-color: NONE;" +
                    "-fx-text-fill: #4a483f; " +
                    "-fx-border-color: #4a483f;" +
                    "-fx-border-width: 4px;");
                });

        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
            prompts.getChildren().clear();
            button.setStyle("-fx-background-color: NONE;-fx-background-radius: 19px; -fx-text-fill: #4a483f;" +
                    "-fx-border-color: #F4DFC0;" +
                    "-fx-border-width: 4px");
                });
    }

    void handleArrowNavigation(KeyEvent event) {
        Node source = (Node) event.getSource();
        GridPane gameGrid = (GridPane) source.getParent().getParent();
        Node node = source.getScene().getFocusOwner();
        Integer col = indexCol;
        Integer row = indexRow;

        if (cells[indexRow-1][indexCol-1].getClass() == Label.class) cells[indexRow-1][indexCol-1].setEffect(null);
        System.out.println("Key event initialised");
        if (event.getCode().isArrowKey() || event.getCode() == KeyCode.BACK_SPACE) {
            switch (event.getCode()) {
                case LEFT -> {
                    if (indexCol != 1) indexCol -= 1;
                }
                case RIGHT -> {
                    if (indexCol != 9) indexCol += 1;
                }
                case UP -> {
                    if (indexRow != 1) indexRow -= 1;
                }
                case DOWN -> {
                    if (indexRow != 9) indexRow += 1;
                }
                case BACK_SPACE -> {
                    if (cells[indexRow-1][indexCol-1].getClass() == TextField.class) {
                        TextField temp = (TextField) cells[indexRow - 1][indexCol - 1];
                        temp.setText("");
                        System.out.println("something worked");
                    }
                }
            }
            System.out.println("col: " + col + " and row: " + row);
            if (cells[indexRow-1][indexCol-1].getClass() == TextField.class) {
                cells[indexRow-1][indexCol-1].requestFocus();
            } else {
                cells[indexRow-1][indexCol-1].setEffect(new Glow());
                TextField temp = new TextField();
                temp.requestFocus();
            }
        }
        event.consume();
    }


    public void makeGame(Difficulty difficulty) {
        this.currentBoard = new Board(new Game(difficulty).mat,difficulty);
        controls.getChildren().clear();
        GridPane gameGrid = new GridPane();
        PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");
        gameGrid.setVgap(9);
        gameGrid.setHgap(9);
        gameGrid.setLayoutX(33);
        gameGrid.setLayoutY(23);
        double tmp = 1000;
        gameGrid.setMinWidth(tmp);
        gameGrid.setMaxWidth(tmp);
        gameGrid.setMinHeight(tmp);
        gameGrid.setMaxHeight(tmp);

        for (int row = 1; row < 10; row++) {
            for (int col = 1; col < 10; col++) {
                int curr = currentBoard.board[row - 1][col - 1];
                StackPane stackPane = new StackPane();
                if (curr == 0) {
                    TextField cell = makeCell();
                    cells[row - 1][col - 1] = cell;
                    stackPane.setStyle("-fx-background-color: #F9EDDC;" +
                            "-fx-background-radius: 9px;" +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-radius: 9px;" +
                            "-fx-border-width: 2px");
                    cell.setAlignment(Pos.CENTER);
                    cell.setPrefHeight(45);
                    cell.setPrefWidth(45);
                    cell.setFont(Font.font("KAI",FontWeight.BOLD,22));
                    cell.setStyle("-fx-background-color: #F9EDDC;-fx-display-caret: false;" +
                            "-fx-border-color: NONE;" +
                            "-fx-text-fill: #4A483F;" +
                            "-fx-background-radius: 9px;" +
                            "-fx-highlight-fill: NONE;" +
                            "-fx-highlight-text-fill: #4a483f");
                    FadeTransition fade = new FadeTransition(Duration.seconds(0.5),cell);
                    fade.setFromValue(1.0);
                    fade.setToValue(0.3);
                    fade.setCycleCount(Timeline.INDEFINITE);
                    fade.setAutoReverse(true);
                    cell.addEventHandler(KeyEvent.KEY_PRESSED, this::handleArrowNavigation);
                    cell.focusedProperty().addListener((observable, oldVal, newVal) -> {
                        if (cell.isFocused() && newVal) {
                            indexCol = GridPane.getColumnIndex(cell.getParent());
                            indexRow = GridPane.getRowIndex(cell.getParent());
                            cell.setStyle("-fx-background-color: #FFFFFF;-fx-display-caret: false;" +
                                    "-fx-border-color: NONE;" +
                                    "-fx-text-fill: BLACK;" +
                                    "-fx-background-radius: 9px;" +
                                    "-fx-highlight-fill: NONE;" +
                                    "-fx-highlight-text-fill: #4a483f");
                            fade.play();
                            cell.toFront();
                            stackPane.setEffect(new Glow());


                        } else {
                            fade.stop();
                            cell.setOpacity(1.0);
                            cell.setStyle("-fx-background-color: #F9EDDC;-fx-display-caret: false;" +
                                    "-fx-border-color: NONE;" +
                                    "-fx-text-fill: #4A483F;" +
                                    "-fx-background-radius: 9px;" +
                                    "-fx-highlight-fill: NONE;" +
                                    "-fx-highlight-text-fill: #4a483f");
                            stackPane.setEffect(null);
                        }
                    });
                    //gameGrid.add(cell,col,row,1,1);

                    stackPane.getChildren().add(cell);
                } else {
                    Label temp = new Label("" + curr);
                    temp.setPrefWidth(45);
                    temp.setPrefHeight(45);
                    temp.setAlignment(Pos.CENTER);
                    temp.setStyle("-fx-text-fill: #4a483f;");
                    temp.setFont(Font.font("KAI", FontWeight.BOLD,22));
                    //temp.setStyle("-fx-border-color: BLACK;");
                    //gameGrid.add(temp,col,row,1,1);
                    //GridPane.setHalignment(stackPane,HPos.CENTER);
                    //StackPane.setAlignment(temp,Pos.);
                    stackPane.setStyle("-fx-border-radius: 9px;" +
                            "-fx-background-radius: 9px;" +
                            "-fx-background-color: NONE;" +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 2px");
                    cells[row - 1][col - 1] = temp;
                    stackPane.getChildren().add(temp);
                }
                stackPane.getStyleClass().add("cell");
                //GridPane.setValignment(stackPane,VPos.CENTER);
                //GridPane.setHalignment(stackPane,HPos.CENTER);


                gameGrid.add(stackPane,col,row,1,1);
            }


            }
        evalNodes(gameGrid);



        Button checkButton = makeCheckButton();
        checkButton.setLayoutX(570);
        checkButton.setLayoutY(100);
        BOARD.getChildren().addAll(gameGrid, checkButton);

    }

    void evalNodes(GridPane gameGrid) {

        for (Node node: gameGrid.getChildren()) {
            Integer targetIndexCol = GridPane.getColumnIndex(node);
            Integer targetIndexRow = GridPane.getRowIndex(node);
            if (currentBoard.checks[targetIndexRow - 1][targetIndexCol - 1]) {
                node.setOnMouseEntered(e -> gameGrid.getChildren().forEach(c -> {


                    Integer boxColMin = (((targetIndexCol - 1) / 3) * 3) + 1;
                    Integer boxColMax = boxColMin + 2;

                    Integer boxRowMin = (((targetIndexRow - 1) / 3) * 3) + 1;
                    Integer boxRowMax = boxRowMin + 2;

                    Integer cCol = GridPane.getColumnIndex(c);
                    Integer cRow = GridPane.getRowIndex(c);
                    boolean check = (cCol >= boxColMin && cCol <= boxColMax && cRow >= boxRowMin && cRow <= boxRowMax);
                    if (currentBoard.checks[targetIndexRow - 1][targetIndexCol - 1]) {

                    }
                    if (cCol == targetIndexCol || cRow == targetIndexRow || check) {
                        c.setStyle("-fx-border-radius: 9px;" +
                                "-fx-background-radius: 9px;" +
                                "-fx-background-color: NONE;" +
                                "-fx-border-color: blue;" +
                                "-fx-border-width: 2px");
                    }
                }));
                node.setOnMouseExited(e -> gameGrid.getChildren().forEach(c -> {

                    Integer boxColMin = (((targetIndexCol - 1) / 3) * 3) + 1;
                    Integer boxColMax = boxColMin + 2;

                    Integer boxRowMin = (((targetIndexRow - 1) / 3) * 3) + 1;
                    Integer boxRowMax = boxRowMin + 2;

                    Integer cCol = GridPane.getColumnIndex(c);
                    Integer cRow = GridPane.getRowIndex(c);
                    boolean check = (cCol >= boxColMin && cCol <= boxColMax && cRow >= boxRowMin && cRow <= boxRowMax);
                    if (cCol == targetIndexCol || cRow == targetIndexRow || check) {
                        c.setStyle("-fx-border-radius: 9px;" +
                                "-fx-background-radius: 9px;" +
                                "-fx-background-color: NONE;" +
                                "-fx-border-color: #4a483f;" +
                                "-fx-border-width: 2px");
                    }
                }));

            }
        }
    }

    Button makeCheckButton() {
        Button button = new Button("Check");
        button.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 28));
        button.setPrefWidth(150);
        button.setStyle("-fx-background-color: NONE; -fx-background-radius: 19px; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #4a483f;" +
                "-fx-border-width: 3px;");

        button.setOnAction(actionEvent -> {
            int[][] nums = new int[9][9];
            for(int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Node currentNode = cells[i][j];
                    if (currentNode.getClass() == TextField.class) {
                        if (((TextField) currentNode).getText() != "") {
                            int curr = Character.getNumericValue(((TextField) currentNode).getText().charAt(0));
                            nums[i][j] = curr;
                        } else nums[i][j] = 0;
                    } else {
                        int curr = Character.getNumericValue(((Label) currentNode).getText().charAt(0));
                        nums[i][j] = curr;
                    }
                }
            }
            currentBoard.board = nums;
            System.out.println(Arrays.deepToString(nums));
            currentBoard.checkValid();
            printArr();
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (!currentBoard.checks[row][col] && cells[row][col].getClass() == TextField.class) {

                        cells[row][col].getParent().setStyle("-fx-border-radius: 9px;" +
                                "-fx-background-radius: 9px;" +
                                "-fx-background-color: NONE;" +
                                "-fx-border-color: #e36588;" +
                                "-fx-border-width: 2px");
                    }
                }
            }

        });
        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    button.setStyle("-fx-background-color: #f0d3a8;" +
                            "-fx-text-fill: #4a483f; " +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px;");
                    button.setEffect(new DropShadow());
                });

        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
                    prompts.getChildren().clear();
                    button.setStyle("-fx-background-color: NONE;-fx-background-radius: 19px; -fx-text-fill: #4a483f;" +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px");
                    button.setEffect(null);
                });

        return button;
    }

    void printArr() {
        for (boolean[] curr: currentBoard.checks
             ) {
            System.out.println(Arrays.toString(curr));
        }
    }


    TextField makeCell() {
        TextField rtn = new TextField();
        rtn.setPrefColumnCount(1);
        rtn.textProperty().addListener((observable, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                rtn.setText(newVal.replaceAll("[^\\d]",oldVal));
            }
        });
        rtn.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (rtn.getText().length() > 1) {

                    // if it's 11th character then just setText to previous
                    // one
                    rtn.setText(rtn.getText().substring(1, 2));
                }
            }
        });


        //rtn.setStyle("-fx-background-color: NONE; -fx-text-fill: BLACK; -fx-border-color: BLACK");
        return rtn;
    }




    @Override
    public void start(Stage stage) throws IOException {
        difficultyMenu();
        root.getChildren().addAll(mainMenu,controls, prompts, BOARD);
        FXMLLoader fxmlLoader = new FXMLLoader(Sudoku.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(root, 750, 800);
        scene.setFill(Color.web("#F4DFC0"));
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}