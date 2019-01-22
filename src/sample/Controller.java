package sample;

import data_io.DataInput;
import data_structure.Board;
import data_structure.Field2D;
import data_structure.FieldWritable;
import generator.BoardGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import solver.Solver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private GridPane kakuroGrid;
    @FXML
    private TextField sizeInput;
    @FXML
    private Label outputLabel;

    private String generatedBoard;
    private Board board;
    private Board originalBoard = null;
    private List<List<TextField>> gridFields;

    public Controller() {
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void generateBoard() {
        int size = 0;
        try {
            size = Integer.parseInt(sizeInput.getText());
        } catch (NumberFormatException exc) {
            System.out.println("Size must be int");
            return;
        }

        BoardGenerator gen = new BoardGenerator(size);
        DataInput di = new DataInput();
        generatedBoard = gen.toString();
        di.readString(generatedBoard);
        board = di.makeGameBoard();
        originalBoard = di.makeGameBoard();

        fillGridBoard(false);
    }

    private void fillGridBoard(boolean showFilled) {
        kakuroGrid.getChildren().clear();
        gridFields = new ArrayList<>();

        for (int x = 0; x < board.getWidth(); ++x) {
            gridFields.add(new ArrayList<>());
            for (int y = 0; y < board.getHeight(); ++y) {
                Field2D field = board.getGameBoard()[x][y];
                TextField gridField = new TextField();
                gridField.getStyleClass().add("gridField");

                if (field.getType() == Field2D.Type.WRITABLE) {
                    if (showFilled && field.getWritable().getState() == FieldWritable.State.FILLED)
                        gridField.setText(String.valueOf(field.getWritable().getValue()));
                } else if (field.getType() == Field2D.Type.BLANK) {
                    gridField.getStyleClass().add("blank");
                    gridField.setDisable(true);
                } else {
                    gridField.setDisable(true);
                    gridField.getStyleClass().add("info");
                }

                if (field.getType() == Field2D.Type.INFOCOLUMNANDROW)
                    gridField.setText(String.format("%d\\%d", field.getColumn().getSum(), field.getRow().getSum()));
                else if (field.getType() == Field2D.Type.INFOCOLUMN)
                    gridField.setText(String.format("%d\\ ", field.getColumn().getSum()));
                else if (field.getType() == Field2D.Type.INFOROW)
                    gridField.setText(String.format(" \\%d", field.getRow().getSum()));

                gridField.setPrefHeight(kakuroGrid.getHeight() / board.getHeight());
                gridField.setPrefWidth(kakuroGrid.getWidth() / board.getWidth());

                kakuroGrid.add(gridField, x, y);
                gridFields.get(x).add(gridField);
            }
        }
    }

    @FXML
    private void checkIsSolved() {
        if (!collectValues()) {
            setRedOutput("Set all fields before check");
            return;
        }
        if (board.isSolved())
            setGreenOutput("Board is correct solved");
        else
            setRedOutput("Board is not solved");
    }

    private boolean collectValues() {
        for (int y = 0; y < board.getHeight(); y++)
            for (int x = 0; x < board.getWidth(); x++) {
                if (board.getGameBoard()[x][y].getType() == Field2D.Type.WRITABLE) {
                    try {
                        int value = Integer.parseInt(gridFields.get(x).get(y).getText());
                        if (value < 1 || value > 9)
                            return false;
                        board.getGameBoard()[x][y].getWritable().setValue(value);
                    } catch (NumberFormatException exc) {
                        System.out.println(String.format("Value in [%d, %d] is not integer", x, y));
                        return false;
                    }
                }
            }
        return true;
    }

    private void setRedOutput(String message) {
        outputLabel.getStyleClass().clear();
        outputLabel.getStyleClass().add("red-label");
        outputLabel.setText(message);
    }

    private void setGreenOutput(String message) {
        outputLabel.getStyleClass().clear();
        outputLabel.getStyleClass().add("green-label");
        outputLabel.setText(message);
    }

    @FXML
    private void autoSolve() {
        if (originalBoard == null)
            return;

        Solver solver = new Solver(originalBoard, 0);
        Board solvedBoard = solver.solve();
        int[] history = solvedBoard.getHistoryAsArray();
        for (int i = 0; i != history.length; i += 2) {
            if (history[i] == 0)
                break;
            if (history[i] != -1)
                board.setField(history[i], history[i + 1], solvedBoard.getGameBoard()[history[i]][history[i + 1]].getWritable().getValue(), false);
            else {
                fillGridBoard(true);
                long start = System.nanoTime();
                while (System.nanoTime() < start + 1000000000) ;
            }
        }

        fillGridBoard(true);
        setGreenOutput("Kakuro auto solved");
    }

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./examples"));
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(outputLabel.getScene().getWindow());

        DataInput di = new DataInput();
        if (file != null)
            di.ReadBoard(file.getAbsolutePath());
        else
            di.ReadBoard(new File("./examples/board_4x4.in").getAbsolutePath());
        board = di.makeGameBoard();
        originalBoard = di.makeGameBoard();

        fillGridBoard(false);
    }
}
