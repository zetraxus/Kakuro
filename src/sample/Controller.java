package sample;

import data_io.DataInput;
import data_structure.Board;
import data_structure.Field2D;
import data_structure.FieldWritable;
import generator.BoardGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import solver.Solver;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private GridPane kakuroGrid;
    @FXML
    private TextField sizeInput;

    private String generatedBoard;
    private Board board;
    private List<List<TextField>> gridFields;

    public Controller(){
    }

    @FXML
    private void initialize(){
    }

    @FXML
    private void generateBoard(){
        int size = 0;
        try {
            size = Integer.parseInt(sizeInput.getText());
        } catch (NumberFormatException exc){
            System.out.println("Size must be int");
            return;
        }

        BoardGenerator gen = new BoardGenerator(size);
        DataInput di = new DataInput();
        generatedBoard = gen.toString();
        di.readString(generatedBoard);
        board = di.makeGameBoard();

        fillGridBoard();
    }

    private void fillGridBoard(){
        kakuroGrid.getChildren().clear();
        gridFields = new ArrayList<>();

        for (int x = 0; x < board.getWidth(); ++x){
            gridFields.add(new ArrayList<>());
            for (int y = 0; y < board.getHeight(); ++y)
            {
                Field2D field = board.getGameBoard()[x][y];
                TextField gridField = new TextField();
                gridField.getStyleClass().add("gridField");

                if (field.getType() == Field2D.Type.WRITABLE)
                {
                    if (field.getWritable().getState() == FieldWritable.State.FILLED)
                        gridField.setText(String.valueOf(field.getWritable().getValue()));

                    gridField.setOnAction(event -> {
                        try {
                            int value = Integer.parseInt(gridField.getText());
                            if (value >= 1 && value <= 9){
                                field.getWritable().setValue(Integer.parseInt(gridField.getText()));
                                System.out.println(String.format("Set value %d for %s", Integer.parseInt(gridField.getText()), field.getWritable().toString()));
                            }
                        } catch (NumberFormatException exc){
                            System.out.println("Value not integer");
                        }
                    });
                }
                else if (field.getType() == Field2D.Type.BLANK)
                {
                    gridField.getStyleClass().add("blank");
                    gridField.setDisable(true);
                }
                else
                {
                    gridField.setDisable(true);
                    gridField.getStyleClass().add("info");
                }

                if (field.getType() == Field2D.Type.INFOCOLUMNANDROW)
                    gridField.setText(String.format("%d\\%d", field.getColumn().getSum(), field.getRow().getSum()));
                else if (field.getType() == Field2D.Type.INFOCOLUMN)
                    gridField.setText(String.format("%d\\ ", field.getColumn().getSum()));
                else if (field.getType() == Field2D.Type.INFOROW)
                    gridField.setText(String.format(" \\%d", field.getRow().getSum()));

                gridField.setPrefHeight(kakuroGrid.getHeight()/board.getHeight());
                gridField.setPrefWidth(kakuroGrid.getWidth()/board.getWidth());

                kakuroGrid.add(gridField, x, y);
                gridFields.get(x).add(gridField);
            }
        }
    }

    @FXML
    private void checkIsSolved(){
        System.out.println("Checking solved:");
        System.out.println(board.isSolved());
    }

    @FXML
    private void autoSolve(){
        if (generatedBoard == null)
            return;

        DataInput di = new DataInput();
        di.readString(generatedBoard);
        Solver solver = new Solver(di.makeGameBoard(), 0);
        board = solver.solve();
        fillGridBoard();
    }
}
