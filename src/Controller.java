import java.util.ArrayList;

import Car.*;
import DBConnection.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        if(onlyFreeCheckBox.isSelected())
            updateTableContent(dbConnection.getFreeCars());
        else
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

    @FXML
    private void showFromRange() {

    }

    private void updateTableContent(ArrayList<Car> newContent){
        table = new TableView<Car>();

        data = FXCollections.observableArrayList(newContent);

        TableColumn<Car,String>  company = new TableColumn<Car,String>("Company");
        company.setMinWidth(100);
        company.setCellValueFactory(new PropertyValueFactory<Car,String>("company"));

        TableColumn<Car,String>  model = new TableColumn<Car,String> ("Model");
        model.setMinWidth(100);
        model.setCellValueFactory(new PropertyValueFactory<Car,String>("model"));

        TableColumn<Car, Double>  longitude = new TableColumn<Car, Double> ("Longitude");
        longitude.setMinWidth(90);
        longitude.setCellValueFactory(new PropertyValueFactory<Car,Double>("longitude"));

        TableColumn<Car, Double>  lattitude = new TableColumn<Car, Double> ("Lattitude");
        lattitude.setMinWidth(90);
        lattitude.setCellValueFactory(new PropertyValueFactory<Car,Double>("lattitude"));

        TableColumn<Car, Status>  status = new TableColumn<Car, Status> ("Status");
        status.setMinWidth(65);
        status.setCellValueFactory(new PropertyValueFactory<Car,Status>("status"));

        table.setMinWidth(centerPane.getWidth());
        table.setMinHeight(centerPane.getHeight());

        table.setItems(data);

        table.getColumns().add(company);
        table.getColumns().add(model);
        table.getColumns().add(longitude);
        table.getColumns().add(lattitude);
        table.getColumns().add(status);

        centerPane.getChildren().clear();
        centerPane.getChildren().add(table);
    }

}
