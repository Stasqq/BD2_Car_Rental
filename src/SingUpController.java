import DBConnection.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SingUpController {

    private DBConnection dbConnection;

    @FXML
    private Button singUp;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField password2;

    @FXML
    private TextField nick;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    public SingUpController(){

    }

    @FXML
    private void initialize() {
        dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe","bdproject","bdproject");
    }

    @FXML
    private void singUp(){
        if(checkIfValuesCorrect()){
            dbConnection.addAccount(nick.getText(),password.getText(),email.getText(),firstName.getText(),lastName.getText(),phone.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Poprawnie założono konto!");
            alert.show();
            Stage stage = (Stage) singUp.getScene().getWindow();
            stage.close();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Podano złe dane!");
            alert.show();
        }
    }

    private boolean checkIfValuesCorrect(){
        if(nick.getText().trim().isEmpty() || email.getText().trim().isEmpty() || password.getText().trim().isEmpty()
        || password2.getText().trim().isEmpty() || firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty()
        || phone.getText().trim().isEmpty()){
            System.out.println("puste");
            return false;
        }
        else if(!password.getText().equals(password2.getText())){
            return false;
        }
        else
            return true;
    }
}
