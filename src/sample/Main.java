package sample;

import java.sql.*;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Connection conn = null;
        PreparedStatement pst=null;
        ResultSet rs=null;

        try{
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","bdproject","bdproject");
            pst = conn.prepareStatement("select * from BRAND");
            rs = pst.executeQuery();
            while(rs.next())
                System.out.println(rs.getInt(1) +". "+rs.getString(2));
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
