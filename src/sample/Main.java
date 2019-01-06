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
//        BoardGenerator i = new BoardGenerator(30);

        String inputFile = null;
        String outputFile = null;

        if (args.length >= 1)
            inputFile = args[0];
        if (args.length == 2)
            outputFile = args[1];

        DataInput di = new DataInput();
//        di.readString(i.toString());
        if (inputFile != null)
            di.ReadBoard(inputFile);

        Board template = di.makeGameBoard();
        if (template != null){
            System.out.println("main:\n" + template.toString());
            Solver solver = new Solver(template, 0); // TODO compute initial value
            long start = System.nanoTime();
            Board result = solver.solve();
            long end = System.nanoTime();
            System.out.println("time: " + (double) (end - start) / 1000000000);

            if (outputFile != null)
                writeResultToFile(result, outputFile);

            System.exit(0);
        }

        launch(args);
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
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("sample/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
