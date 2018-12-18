package sample;

import data_io.DataInput;
import data_structure.board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import solver.solver;

public class Main extends Application {

    public static void main(String[] args) {
        DataInput di = new DataInput();

        di.ReadBoard("examples/example.txt");
//        di.ReadBoard("examples/board_6x6.txt");

        board template = di.makeGameBoard();
        System.out.println("main:\n" + template.toString());
        solver solver = new solver(template, template.getCost()); // TODO compute initial value
        solver.solve();

//        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        //primaryStage.show();
    }
}
