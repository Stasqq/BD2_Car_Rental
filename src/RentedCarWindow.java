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

    private DBConnection dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe", "\"bdproject\"", "\"bdproject\"");

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
            drivingTimeLabel.setText(drivingTime / 60 + ":" + drivingTime%60);
            distanceLabel.setText(distance+"km");
             cost = 80.0*distance +50.0 * drivingTime/60 + 20.0 * (rentTime - drivingTime)/60;
            cost = Math.round(cost)/100.0;
            costLabel.setText( cost+"zł");
        });


    }


    public void checkout()
    {
        timer1.cancel();
        dbConnection.addRental(userID,car,cost);

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
