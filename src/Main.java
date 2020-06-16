import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;

import DBConnection.*;
import Car.*;

public class Main extends Application {

    private TableView<Car> table = new TableView<Car>();
    private ObservableList<Car> data;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DBConnection dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe","bdproject","bdproject");

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("cars_view.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = FXCollections.observableArrayList(dbConnection.getCars());

        TableColumn<Car,String>  tableColumn1 = new TableColumn<Car,String>("Company");
        tableColumn1.setMinWidth(100);
        tableColumn1.setCellValueFactory(new PropertyValueFactory<Car,String>("company"));

        TableColumn<Car,String>  tableColumn2 = new TableColumn<Car,String> ("Model");
        tableColumn2.setMinWidth(100);
        tableColumn2.setCellValueFactory(new PropertyValueFactory<Car,String>("model"));

        table.setItems(data);

        table.getColumns().add(tableColumn1);
        table.getColumns().add(tableColumn2);



        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        ScrollPane scrollPane;
        if (root != null) {
            scrollPane = (ScrollPane) root.lookup("#scrollPane");
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().add(table);
            scrollPane.setContent(anchorPane);
        }
    }
}
