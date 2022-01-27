package com.example.sudoku2;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Sudoku extends Application {

    Board currentBoard;
    Font myFont = Font.loadFont("resources/futura_font.zip", 16);
    Group root = new Group();
    Group BOARD = new Group();
    Group grid = new Group();
    Group mainMenu = new Group();
    Group controls = new Group();
    Group prompts = new Group();
    boolean finished = false;

    int indexCol;
    int indexRow;
    Node[][] cells = new Node[9][9];
    boolean confirm = false;


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
        startButton.setOnAction(actionEvent -> difficultyMenu());

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
            Label instructions = new Label("""
                    Place the numbers 1 - 9 in the boxes\s
                    such that all columns, rows and subsections\s
                    contain only one instance of each digit.""");
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
            button.setOnAction(actionEvent -> makeGame(difficulty, true));
        }

        button.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 34));
        button.setPrefWidth(250);
        button.setStyle("-fx-background-color: NONE; -fx-background-radius: 19px; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #F4DFC0;" +
                "-fx-border-width: 4px;");

        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> button.setStyle("-fx-background-color: NONE;" +
                        "-fx-text-fill: #4a483f; " +
                        "-fx-border-color: #4a483f;" +
                        "-fx-border-width: 4px;"));

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
        int col = indexCol;
        int row = indexRow;

        if (col == 0 || row == 0) return;
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


    public void makeGame(Difficulty difficulty, boolean newGame) {
        long startTime = System.nanoTime();
        if (newGame) {
            Game game = new Game(difficulty);
            this.currentBoard = new Board(game,difficulty);
        }
        BOARD.getChildren().clear();
        controls.getChildren().clear();
        GridPane gameGrid = new GridPane();

        gameGrid.setVgap(16);
        gameGrid.setHgap(16);
        gameGrid.setLayoutX(53);
        gameGrid.setLayoutY(22);

        Rectangle Vline1 = new Rectangle(2,615);
        Rectangle VLine2 = new Rectangle(2,615);
        Rectangle HLine1 = new Rectangle(615,2);
        Rectangle HLIne2 = new Rectangle(615, 2);

        HBox Vlines = new HBox(Vline1, VLine2);
        VBox HLines = new VBox(HLine1, HLIne2);

        Vlines.setSpacing(208);
        Vlines.setLayoutX(271.5);
        Vlines.setLayoutY(38.5);

        HLines.setSpacing(208);
        HLines.setLayoutX(70);
        HLines.setLayoutY(240);

        long endTime4 = System.nanoTime();

        System.out.println("\n\nendtime4");
        System.out.println(endTime4 - startTime);


        for (int row = 1; row < 10; row++) {
            System.out.println("\nCurrRow: " + row);
            System.out.println(System.nanoTime() - startTime);
            for (int col = 1; col < 10; col++) {
                System.out.println("\nCurrCol: " + col);
                System.out.println(System.nanoTime() - startTime);

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
                    cell.setPrefHeight(50);
                    cell.setPrefWidth(50);
                    cell.setFont(Font.font("KAI", FontWeight.BOLD, 22));
                    cell.setStyle("-fx-background-color: #F9EDDC;-fx-display-caret: false;" +
                            "-fx-border-color: NONE;" +
                            "-fx-text-fill: #4A483F;" +
                            "-fx-background-radius: 9px;" +
                            "-fx-highlight-fill: NONE;" +
                            "-fx-highlight-text-fill: #4a483f");
                    FadeTransition fade = new FadeTransition(Duration.seconds(0.5), cell);
                    fade.setFromValue(1.0);
                    fade.setToValue(0.3);
                    fade.setCycleCount(Timeline.INDEFINITE);
                    fade.setAutoReverse(true);
                    cell.addEventHandler(KeyEvent.KEY_PRESSED, this::handleArrowNavigation);
                    cell.focusedProperty().addListener((observable, oldVal, newVal) -> {
                        if (cell.isFocused() && newVal && !finished) {
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
                    System.out.println(System.nanoTime() - startTime);


                } else {


                    Label temp = new Label("" + curr);
                    temp.setPrefWidth(50);
                    temp.setPrefHeight(50);
                    temp.setAlignment(Pos.CENTER);
                    temp.setStyle("-fx-text-fill: #4a483f;");
                    temp.setFont(Font.font("KAI", FontWeight.BOLD, 22));
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


                gameGrid.add(stackPane, col, row, 1, 1);
            }


        }
        long endTime1 = System.nanoTime();
        evalNodes(gameGrid);

        long endTime2 = System.nanoTime();

        System.out.println("\n Final timings as such");
        System.out.println(endTime1 - startTime);
        System.out.println(endTime2 - startTime);

        long endTime3 = System.nanoTime();
        HBox gameControls = new HBox();
        gameControls.setSpacing(35);
        gameControls.getChildren().addAll(makeClearButton(difficulty),makeCheckButton(),makeSolveButton());
        gameControls.setLayoutX(113);
        gameControls.setLayoutY(695);
        controls.getChildren().add(gameControls);
        BOARD.getChildren().addAll(Vlines, HLines, gameGrid, makeReturnButton());

    }

    void evalNodes(GridPane gameGrid) {

        for (Node node: gameGrid.getChildren()) {
            Integer targetIndexCol = GridPane.getColumnIndex(node);
            Integer targetIndexRow = GridPane.getRowIndex(node);
            if (currentBoard.checks[targetIndexRow - 1][targetIndexCol - 1]) {
                node.setOnMouseEntered(e -> gameGrid.getChildren().forEach(c -> {


                    Integer boxColMin = (((targetIndexCol - 1) / 3) * 3) + 1;
                    int boxColMax = boxColMin + 2;

                    Integer boxRowMin = (((targetIndexRow - 1) / 3) * 3) + 1;
                    int boxRowMax = boxRowMin + 2;

                    Integer cCol = GridPane.getColumnIndex(c);
                    Integer cRow = GridPane.getRowIndex(c);
                    boolean check = (cCol >= boxColMin && cCol <= boxColMax && cRow >= boxRowMin && cRow <= boxRowMax);
                    if (cCol.equals(targetIndexCol) || Objects.equals(cRow, targetIndexRow) || check) {
                        c.setStyle("-fx-border-radius: 9px;" +
                                "-fx-background-radius: 9px;" +
                                "-fx-background-color: NONE;" +
                                "-fx-border-color: blue;" +
                                "-fx-border-width: 2px");
                    }
                }));
                node.setOnMouseExited(e -> gameGrid.getChildren().forEach(c -> {

                    Integer boxColMin = (((targetIndexCol - 1) / 3) * 3) + 1;
                    int boxColMax = boxColMin + 2;

                    Integer boxRowMin = (((targetIndexRow - 1) / 3) * 3) + 1;
                    int boxRowMax = boxRowMin + 2;

                    Integer cCol = GridPane.getColumnIndex(c);
                    Integer cRow = GridPane.getRowIndex(c);
                    boolean check = (cCol >= boxColMin && cCol <= boxColMax && cRow >= boxRowMin && cRow <= boxRowMax);
                    if (cCol.equals(targetIndexCol) || Objects.equals(cRow, targetIndexRow) || check) {
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
        button.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #4a483f;" +
                "-fx-border-width: 3px;");

        button.setOnAction(actionEvent -> {
            confirm = false;
            prompts.getChildren().clear();
            int[][] nums = new int[9][9];
            for(int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Node currentNode = cells[i][j];
                    if (currentNode.getClass() == TextField.class) {
                        if (!Objects.equals(((TextField) currentNode).getText(), "")) {
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

            currentBoard.checkValid();

            boolean check = true;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (!currentBoard.checks[row][col] && cells[row][col].getClass() == TextField.class) {
                        check = false;
                        cells[row][col].getParent().setStyle("-fx-border-radius: 9px;" +
                                "-fx-background-radius: 9px;" +
                                "-fx-background-color: NONE;" +
                                "-fx-border-color: #e36588;" +
                                "-fx-border-width: 2px");
                    } else if (cells[row][col].getClass() == TextField.class) {
                        if (((TextField) cells[row][col]).getText().isEmpty()) {

                            check = false;
                        }
                    }
                }
            }
            if (check) {
                finishSequence(false);
            }
        });
        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    button.setStyle("-fx-background-color: #f0d3a8;" +
                            "-fx-text-fill: #4a483f; " +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px;" +
                            "-fx-background-insets: 2px");
                    button.setEffect(new DropShadow());
                });

        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {

                    button.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px");
                    button.setEffect(null);
                });

        return button;
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

    Button makeClearButton(Difficulty difficulty) {
        Button button = new Button("Clear");
        Label confirmText = new Label("Click again to confirm");
        confirmText.setFont(Font.font("Times new roman",FontWeight.NORMAL,16));
        confirmText.setLayoutY(672);
        confirmText.setLayoutX(116);
        button.setOnAction(actionEvent -> {
            /*if (confirm) {
                confirm = false;
                controls.getChildren().remove(confirmText);
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {

                        if (cells[row][col].getClass() == TextField.class) {
                            cells[row][col].getParent().setStyle("-fx-background-color: #F9EDDC;" +
                                    "-fx-background-radius: 9px;" +
                                    "-fx-border-color: #4a483f;" +
                                    "-fx-border-radius: 9px;" +
                                    "-fx-border-width: 2px");
                            ((TextField) cells[row][col]).setText("");
                        } else {
                            cells[row][col].getParent().setStyle("-fx-border-radius: 9px;" +
                                    "-fx-background-radius: 9px;" +
                                    "-fx-background-color: NONE;" +
                                    "-fx-border-color: #4a483f;" +
                                    "-fx-border-width: 2px");
                        }
                    }
                }

            } else {
                confirm = true;
                prompts.getChildren().add(confirmText);
            }

             */

            if (confirm) {
                confirm = false;
                prompts.getChildren().remove(confirmText);
                makeGame(difficulty, false);
            }
            else {
                confirm = true;
                prompts.getChildren().add(confirmText);
            }
        });
        button.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 28));
        button.setPrefWidth(150);
        button.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #4a483f;" +
                "-fx-border-width: 3px;");

        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    button.setStyle("-fx-background-color: #f0d3a8;" +
                            "-fx-text-fill: #4a483f; " +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px;" +
                            "-fx-background-insets: 2px");
                    button.setEffect(new DropShadow());
                });

        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
            prompts.getChildren().clear();
            confirm = false;
                    button.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px");
                    button.setEffect(null);
                });

        return button;
    }

    Button makeSolveButton() {
        int[][] sol = currentBoard.solution;

        Button button = new Button("Solve");
        Label confirmText = new Label("Click again to confirm");
        confirmText.setFont(Font.font("Times new roman",FontWeight.NORMAL,16));
        confirmText.setLayoutY(672);
        confirmText.setLayoutX(487);
        button.setOnAction(actionEvent -> {
            if (confirm) {
                prompts.getChildren().clear();
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        if (cells[row][col].getClass() == TextField.class) {
                            ((TextField) cells[row][col]).setText("" + sol[row][col]);
                        }
                    }
                }
                finishSequence(true);
            } else {
                confirm = true;
                prompts.getChildren().add(confirmText);
            }


        });
        button.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 28));
        button.setPrefWidth(150);
        button.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #4a483f;" +
                "-fx-border-width: 3px;");

        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    button.setStyle("-fx-background-color: #f0d3a8;" +
                            "-fx-text-fill: #4a483f; " +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px;" +
                            "-fx-background-insets: 2px");
                    button.setEffect(new DropShadow());
                });

        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
                    prompts.getChildren().clear();
                    confirm = false;
                    button.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                            "-fx-border-color: #4a483f;" +
                            "-fx-border-width: 3px");
                    button.setEffect(null);
                });

        return button;

    }

    Button makeReturnButton() {


        Button rtn = new Button("Menu");
        rtn.setFont(Font.font("Times new roman", FontWeight.SEMI_BOLD, 13));
        rtn.setLayoutX(20);
        rtn.setLayoutY(752.5);
        rtn.setOnAction(actionEvent -> {
            BOARD.getChildren().clear();
            prompts.getChildren().clear();
            controls.getChildren().clear();
            difficultyMenu();
        });
        rtn.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                "-fx-border-color: #4a483f;" +
                "-fx-border-width: 2px;");

        rtn.setOnMouseEntered(e -> {
            rtn.setStyle("-fx-background-color: #f0d3a8;" +
                    "-fx-text-fill: #4a483f; " +
                    "-fx-border-color: #4a483f;" +
                    "-fx-border-width: 2px;" +
                    "-fx-background-insets: 1px");
           rtn.setEffect(new DropShadow());
        });
        rtn.setOnMouseExited(e -> {
            rtn.setStyle("-fx-background-color: NONE; -fx-text-fill: #4a483f;" +
                    "-fx-border-color: #4a483f;" +
                    "-fx-border-width: 2px");
            rtn.setEffect(null);
        });
        return rtn;
    }

    void finishSequence(boolean skipped) {
        finished = true;
        controls.getChildren().clear();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Node parent = cells[row][col].getParent();
                Node node = cells[row][col];
                parent.setStyle("-fx-border-radius: 9px;" +
                        "-fx-background-radius: 9px;" +
                        "-fx-background-color: NONE;" +
                        "-fx-border-color: #3EE049;" +
                        "-fx-border-width: 2px");
                parent.setFocusTraversable(false);
                if (node.getClass() == TextField.class) {
                    ((TextField) node).setEditable(false);
                    node.setEffect(null);
                    node.setFocusTraversable(false);
                }
                parent.setOnMouseEntered(e -> parent.setStyle("-fx-border-radius: 9px;" +
                        "-fx-background-radius: 9px;" +
                        "-fx-background-color: NONE;" +
                        "-fx-border-color: #3EE049;" +
                        "-fx-border-width: 2px"));
                parent.setOnMouseExited(e -> parent.setStyle("-fx-border-radius: 9px;" +
                        "-fx-background-radius: 9px;" +
                        "-fx-background-color: NONE;" +
                        "-fx-border-color: #3EE049;" +
                        "-fx-border-width: 2px"));
            }
        }
        Label congrats = new Label("Congratulations");
        Button playAgain = new Button("Play Again");
        playAgain.setOnAction(actionEvent -> makeGame(currentBoard.Difficulty, true));
        prompts.getChildren().addAll(congrats,playAgain);
    }




    @Override
    public void start(Stage stage) {
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