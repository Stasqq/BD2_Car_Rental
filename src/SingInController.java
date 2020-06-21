import DBConnection.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SingInController {

    private DBConnection dbConnection;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button newAccount;

    @FXML
    private Button login;

    public SingInController(){

    }

    @FXML
    private void initialize() {
        dbConnection = new DBConnection("jdbc:oracle:thin:@localhost:1521:xe","\"bdproject\"","\"bdproject\"");
    }

    @FXML
    private void loginAccount(){
        User user = isLoginSuccess();
        if(user != null){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cars_view.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Car Sharing");
                stage.setScene(new Scene((VBox)loader.load()));
                MainWindowController mainWindowController = loader.<MainWindowController>getController();
                mainWindowController.initUser(user);
                stage.show();
                Stage thisStage = (Stage) login.getScene().getWindow();
                thisStage.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nie udało się zalogować!");
            alert.show();
        }
    }

    @FXML
    private void openSingUp(){
        Parent root;
        try{
            root = FXMLLoader.load(getClass().getResource("sing_up.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Rejestracja");
            stage.setScene(new Scene(root));
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private User isLoginSuccess(){
        if(email.getText().trim().isEmpty() || password.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nie wprowadzono danych logowania!");
            alert.show();
        }else{
            User user = dbConnection.singIn(email.getText(),password.getText());

            if(user != null)
                return user;
        }
        return null;
    }

}
