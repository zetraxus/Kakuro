package sample;

import data_io.DataInput;
import data_structure.Board;
import generator.BoardGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import solver.Solver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main extends Application {

    public static void main(String[] args) {
        BoardGenerator i = new BoardGenerator(7);
        long start = System.nanoTime();
        String inputFile = "examples/board_6x6_2.in";
        String outputFile = null;

        if (args.length >= 1)
            inputFile = args[0];
        if (args.length == 2)
            outputFile = args[1];

        DataInput di = new DataInput();
//        di.readString(i.toString());
        di.ReadBoard(inputFile);

        Board template = di.makeGameBoard();
        System.out.println("main:\n" + template.toString());
        Solver solver = new Solver(template, 0); // TODO compute initial value
        Board result = solver.solve();
        long end=System.nanoTime();
        System.out.println("time: "+(double)(end-start)/1000000000);

        if (outputFile != null)
            writeResultToFile(result, outputFile);

        System.exit(0);
//        launch(args);
    }

    private static void writeResultToFile(Board resultBoard, String outputFile) {
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
