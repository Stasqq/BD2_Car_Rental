import DBConnection.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountInfoController {


    private User user;

    @FXML
    private Label email;
    @FXML
    private Label addDate;
    @FXML
    private Label position;
    @FXML
    private Label firstName;
    @FXML
    private Label lastName;
    @FXML
    private Label nick;

    @FXML
    private void initialize() {

    }

    public AccountInfoController(){

    }

    public void initUser(User user){
        this.user = user;
        email.setText(user.getEmail());
        addDate.setText(user.getAddDate().toString());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        nick.setText(user.getNick());
        if(user.getLongitude() != -181 && user.getLattitude() != -181)
            position.setText(user.getLongitude()+"/"+user.getLattitude());
        else
            position.setText("Nie podano");
    }


}
