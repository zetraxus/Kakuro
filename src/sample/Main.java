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
import java.util.Vector;

public class Main extends Application {

//    public static void main(String[] args) {
////        BoardGenerator i = new BoardGenerator(7);
//
//        String inputFile = null;
//        String outputFile = null;
//
//        if (args.length >= 1)
//            inputFile = args[0];
//        if (args.length == 2)
//            outputFile = args[1];
//
//        DataInput di = new DataInput();
////        di.readString(i.toString());
//        if (inputFile != null)
//            di.ReadBoard(inputFile);
//
//        Board template = di.makeGameBoard();
//        if (template != null) {
////            System.out.println("main:\n" + template.toString());
//            Solver solver = new Solver(template, 0); // TODO compute initial value
//            long start = System.nanoTime();
//            Board result = solver.solve();
//            long end = System.nanoTime();
//            System.out.println("time: " + (double) (end - start) / 1000000000);
//
//            if (outputFile != null)
//                writeResultToFile(result, outputFile);
//
//            System.exit(0);
//        }
//
//        launch(args);
//    }

    public static void main(String[] args) {
        final boolean isTest = true; // if test - set true, if check your heuristic function- set false

        Vector<String> inputFiles = new Vector<>();
        inputFiles.add("examples/board_4x4.in");
        inputFiles.add("examples/board_4x4_2.in");
        inputFiles.add("examples/board_4x4_3.in");
        inputFiles.add("examples/board_4x4_4.in");
        inputFiles.add("examples/board_4x4_gen.in");
        inputFiles.add("examples/board_6x6.in");

        for (int size = 6; size < 7; ++size)
        {
            for (int i = 0; i < 20; i++){
                String name = String.format("examples/board_%d_%d.in", size-1, i);
                BoardGenerator bg = new BoardGenerator(size);
                try {
                    FileWriter fileWriter = new FileWriter(name);
                    PrintWriter printWriter = new PrintWriter(fileWriter);
                    printWriter.println(bg.toString());
                    printWriter.close();
                    inputFiles.add(name);
                } catch (IOException e) {
                    System.out.println("Error on save to file");
                }
            }
        }

//        inputFiles.add("examples/board_9x8_1");
//        inputFiles.add("examples/board_13x13_1");
        String inputFile = null;

        Vector<Double> results = new Vector<>();
        Vector<Long> analysed = new Vector<>();
        double summary = 0;

        int testCount;
        if(isTest)
            testCount = 15;
        else
            testCount = 1;

        for (int i = 0 ; i < inputFiles.size(); ++i){
            inputFile = inputFiles.elementAt(i);
            try {
                long analysedAll = 0;
                long analyzedRandom = 0;
                for (int j = 0 ; j < testCount; ++j){
                    DataInput di = new DataInput();

                    if (inputFile != null)
                        di.ReadBoard(inputFile);
                    Board template = di.makeGameBoard();
                    if (template != null){
                        Solver solver = new Solver(template, 0); // TODO compute initial value
                        Solver randomSolve = new Solver(template, 0);
                        if(isTest)
                        {
                            solver.solve();
                            randomSolve.radndomSolve();
                        }
                        else
                            solver.solve();
                        analysedAll += solver.getAnalysedCount();
                        analyzedRandom += randomSolve.getAnalysedCount();
                    }
                }
                analysed.add(analysedAll/testCount);
                System.out.println(String.format("%s : %d (random) vs. %d (heuristic)",
                        inputFile, analyzedRandom / testCount, analysedAll / testCount));
            } catch (Exception e)
            {
                System.out.println(String.format("%s ignored", inputFile));
            }
        }

        System.out.println("Results: ");
        for(int i = 0; i < inputFiles.size(); ++i){
            System.out.println(analysed.elementAt(i));
        }
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
        primaryStage.setTitle("Kakuro A* solver");
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("sample/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
