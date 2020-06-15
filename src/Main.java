import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;

import DBConnection.*;
import Car.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DBConnection dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe","bdproject","bdproject");

        ArrayList<Car> cars = dbConnection.getCars();

        System.out.println(cars.size());

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("cars_view.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
}
