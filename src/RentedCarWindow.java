import Car.Car;
import DBConnection.DBConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.util.Timer;

public class RentedCarWindow
{
    public Timer timer1 = new Timer();
    public MyTimerTask timer1_task = new MyTimerTask(this);
    private int drivingTime=0, rentTime=0;
    private double distance=0.0;
    private Car car;
    private int userID;
    private double cost;

    private DBConnection dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe", "bdproject", "bdproject");

    @FXML
    private Button checkoutButton;
    @FXML
    private Label rentTimeLabel;
    @FXML
    private Label drivingTimeLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private Label costLabel;
    @FXML
    private ToggleButton stopToggle;

    @FXML
    private Label drivingTimeTitleLabel;
    @FXML
    private Label distanceTitleLabel;
    @FXML
    private Label costTitleLabel;
    @FXML
    private Button openDoorsButton;

    private boolean drivingMode;




    @FXML
    private void initialize()
    {
        timer1.schedule (timer1_task, 0, 1000);
    }

    public void tick()
    {
        rentTime++;
        if(!stopToggle.isSelected())
        {
            drivingTime++;
            distance+= 0.1;
        }


        Platform.runLater(() -> {
            rentTimeLabel.setText(rentTime / 60 + ":" + rentTime%60);
            if(drivingMode) {
                drivingTimeLabel.setText(drivingTime / 60 + ":" + drivingTime % 60);
                distance *= 100;
                distance = Math.round(distance);
                distance /= 100;
                distanceLabel.setText(distance + "km");
                cost = 80.0 * distance + 50.0 * drivingTime / 60 + 20.0 * (rentTime - drivingTime) / 60;
                cost = Math.round(cost) / 100.0;
                costLabel.setText(cost + "zł");
            }
        });


    }
    public void setReservedMode()
    {
        drivingMode = false;

        drivingTimeTitleLabel.setDisable(true);
        drivingTimeTitleLabel.setOpacity(0.0);

        distanceTitleLabel.setDisable(true);
        distanceTitleLabel.setOpacity(0.0);

        costTitleLabel.setDisable(true);
        costTitleLabel.setOpacity(0.0);

        checkoutButton.setOpacity(0.0);
        checkoutButton.setDisable(true);

        stopToggle.setOpacity(0.0);
        stopToggle.setDisable(true);

        openDoorsButton.setDisable(false);
        openDoorsButton.setOpacity(1.0);
    }
    public void setDrivingMode()
    {
        drivingMode = true;

        drivingTimeTitleLabel.setDisable(false);
        drivingTimeTitleLabel.setOpacity(1.0);

        distanceTitleLabel.setDisable(false);
        distanceTitleLabel.setOpacity(1.0);

        costTitleLabel.setDisable(false);
        costTitleLabel.setOpacity(1.0);

        checkoutButton.setOpacity(1.0);
        checkoutButton.setDisable(false);

        stopToggle.setOpacity(1.0);
        stopToggle.setDisable(false);

        openDoorsButton.setDisable(true);
        openDoorsButton.setOpacity(0.0);
    }


    @FXML
    private void openDoors()
    {
        this.setDrivingMode();
    }


    public void checkout()
    {
        timer1.cancel();
        dbConnection.addRental(userID,car,cost);
        dbConnection.setCarFree(car);

        Stage stage = (Stage) checkoutButton.getScene().getWindow();
        stage.close();


    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
