package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import data_io.DataInput;

import solver.solver;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        //primaryStage.show();
    }


    public static void main(String[] args) {
        DataInput di = new DataInput();

        //di.ReadBoard("examples/example.txt");
        di.ReadBoard("examples/board_6x6.txt");
        System.out.println(di.makeGameBoard().toString());
        solver solver = new solver(di.makeGameBoard(), 0); // TODO compute initial value
        System.out.println(solver.solve().toString());

        launch(args);
    }
}
