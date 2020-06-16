import java.net.URL;
import java.util.ResourceBundle;

import Car.*;
import DBConnection.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller implements EventHandler<ActionEvent> {

    private User user;

    public Controller(){

    }

    @FXML
    private void initialize()
    {
        cordLoad.setOnAction(this);

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

    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == cordLoad){
            if(nCordText.getText().trim().isEmpty() || wCordText.getText().trim().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nie podano współrzędnych!");
                alert.show();
            }else if(Double.parseDouble(nCordText.getCharacters().toString()) >= -180.0 && Double.parseDouble(nCordText.getCharacters().toString()) <= 180.0
            && Double.parseDouble(wCordText.getCharacters().toString()) >= -180.0 && Double.parseDouble(wCordText.getCharacters().toString()) <= 180.0){
                user = new User(Double.parseDouble(nCordText.getCharacters().toString()),Double.parseDouble(wCordText.getCharacters().toString()));
                rangeText.setDisable(false);
                rangeShowButton.setDisable(false);
                carNSpinner.setDisable(false);
                nCarButton.setDisable(false);
                showAllButton.setDisable(false);
                onlyFreeCheckBox.setDisable(false);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Podano złe współrzędne położenia!");
                alert.show();
            }
        }
    }
}
