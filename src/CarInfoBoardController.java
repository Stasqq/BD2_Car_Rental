import Car.Car;
import DBConnection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class CarInfoBoardController
{
    @FXML
    public ListView<String> opinionList;

    @FXML
    private Label brandLabel;

    @FXML
    private Label modelLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label mileageLabel;

    @FXML
    private Label powerLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label avgRateLabel;

    private DBConnection dbConnection;

    private RentedCarWindow rentedCarWindow;
    private Car car;
    private int userId;

    @FXML
    private void initialize() {
        dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe", "\"bdproject\"", "\"bdproject\"");

        //zawijanie tesktu
        opinionList.setCellFactory(param -> new ListCell<String >(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) {
                    setGraphic(null);
                    setText(null);
                    // other stuff to do...

                }else{

                    // set the width's
                    setMinWidth(param.getWidth()-15);
                    setMaxWidth(param.getWidth()-15);
                    setPrefWidth(param.getWidth()-15);

                    // allow wrapping
                    setWrapText(true);

                    setText(item.toString());


                }
            }
        });
    }
    @FXML
    private void rent()
    {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("RentedCarWindow.fxml"));
            root = loader.load();
            rentedCarWindow= loader.getController(); //pobiera kontroler
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Car Sharing");
            primaryStage.setScene(new Scene(root));
            rentedCarWindow.setCar(this.car);
            rentedCarWindow.setUserID(this.userId);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }









    }

      public  void fillCarInfo()
    {
        brandLabel.setText(car.getCompany());
        modelLabel.setText(car.getModel());
        switch (car.getType())
        {
            case HYBRID: typeLabel.setText("hybrydowy");
            break;
            case ELECTRIC: typeLabel.setText("elektryczny")     ;
            break;
            case COMBUSTION: typeLabel.setText("spalinowy");
        }

        mileageLabel.setText(String.valueOf(car.getMileage()));
        powerLabel.setText(String.valueOf(car.getPower()));
        locationLabel.setText(car.getLongitude() + "/" + car.getLattitude());
        avgRateLabel.setText(String.valueOf(dbConnection.getAvgCarRate(car)).substring(0,3));
    }

    public void fillOpinionTable()
    {
        ObservableList<String> observableList = FXCollections.observableList(dbConnection.getCarOpinions(car));
        opinionList.setItems(observableList);
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
