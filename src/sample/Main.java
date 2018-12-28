package sample;

import data_io.DataInput;
import data_structure.board;
import generator.boardGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import solver.solver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main extends Application {

    public static void main(String[] args) {
        boardGenerator i = new boardGenerator(18);

        String inputFile = "examples/example.in";
        String outputFile = null;

        if (args.length == 2) {
            inputFile = args[0];
            outputFile = args[1];
        }

        DataInput di = new DataInput();

        di.readString(i.toString());
//        di.ReadBoard(inputFile);
//        di.ReadBoard("examples/board_6x6.txt");

        board template = di.makeGameBoard();
        System.out.println("main:\n" + template.toString());
        solver solver = new solver(template, template.getCost()); // TODO compute initial value
        //board result = solver.solve();

//        if (outputFile != null)
//            writeResultToFile(result, outputFile);

        System.exit(0);

//        launch(args);
    }

    private static void writeResultToFile(board resultBoard, String outputFile) {
        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(resultBoard);
            printWriter.close();
        } catch (IOException e) {
            System.out.println("Error on save to file");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        //primaryStage.show();
    }
}
