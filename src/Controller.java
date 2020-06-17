import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Car.*;
import DBConnection.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class Controller {

    private DBConnection dbConnection;

    private User user;

    private TableView<Car> table;
    private ObservableList<Car> data;

    public Controller(){

    }

    @FXML
    private void initialize()
    {
        dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe","bdproject","bdproject");

        rangeText.setDisable(true);
        rangeShowButton.setDisable(true);
        carNSpinner.setDisable(true);
        nCarButton.setDisable(true);
        showAllButton.setDisable(true);
        onlyFreeCheckBox.setDisable(true);
    }

    @FXML
    private TextField nCordText;

    @FXML
    private TextField wCordText;

    @FXML
    private Button cordLoad;

    @FXML
    private TextField rangeText;

    @FXML
    private Button rangeShowButton;

    @FXML
    private Spinner carNSpinner;

    @FXML
    private Button nCarButton;

    @FXML
    private Button showAllButton;

    @FXML
    private CheckBox onlyFreeCheckBox;

    @FXML
    private Pane centerPane;

    @FXML
    private void showAllCars(){
        updateTableContent(dbConnection.getCars());
    }

    @FXML
    private void loadCords() {
        if (nCordText.getText().trim().isEmpty() || wCordText.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nie podano współrzędnych!");
            alert.show();
        } else if (Double.parseDouble(nCordText.getCharacters().toString()) >= -180.0 && Double.parseDouble(nCordText.getCharacters().toString()) <= 180.0
                && Double.parseDouble(wCordText.getCharacters().toString()) >= -180.0 && Double.parseDouble(wCordText.getCharacters().toString()) <= 180.0) {
            user = new User(Double.parseDouble(nCordText.getCharacters().toString()), Double.parseDouble(wCordText.getCharacters().toString()));
            rangeText.setDisable(false);
            rangeShowButton.setDisable(false);
            carNSpinner.setDisable(false);
            nCarButton.setDisable(false);
            showAllButton.setDisable(false);
            onlyFreeCheckBox.setDisable(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Podano złe współrzędne położenia!");
            alert.show();
        }
    }

    private void updateTableContent(ArrayList<Car> newContent){
        table = new TableView<Car>();

        data = FXCollections.observableArrayList(newContent);

        TableColumn<Car,String>  tableColumn1 = new TableColumn<Car,String>("Company");
        tableColumn1.setMinWidth(100);
        tableColumn1.setCellValueFactory(new PropertyValueFactory<Car,String>("company"));

        TableColumn<Car,String>  tableColumn2 = new TableColumn<Car,String> ("Model");
        tableColumn2.setMinWidth(100);
        tableColumn2.setCellValueFactory(new PropertyValueFactory<Car,String>("model"));

        table.setMinWidth(centerPane.getWidth());
        table.setMinHeight(centerPane.getHeight());

        table.setItems(data);

        table.getColumns().add(tableColumn1);
        table.getColumns().add(tableColumn2);

        centerPane.getChildren().clear();
        centerPane.getChildren().add(table);
    }

}
