package DBConnection;

import Car.*;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

 public class DBConnection {
    private String connectionURL;
    private String connectionUser;
    private String connectionPassword;


    private java.sql.Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    public DBConnection(String connectionURL, String connectionUser, String connectionPassword){
        this.connectionURL = connectionURL;
        this.connectionUser = connectionUser;
        this.connectionPassword = connectionPassword;
    }

    public ArrayList<Car> getCars(){
        ArrayList<Car> cars = new ArrayList<Car>();
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("select id from car");
            rs = pst.executeQuery();
            while(rs.next()){
                cars.add(getCarById(rs.getInt("id")));
            }
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return cars;
    }
    public double getAvgCarRate(Car car)
    {
        double result = -1.0;
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("select avg(RATE) from OPINION where CAR_ID = ? ");
            pst.setInt(1,car.getId());
            rs = pst.executeQuery();
            rs.next();
            result= rs.getDouble(1);
            conn.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;

    }

    public Car getCarById(int carId){
        try{
            ResultSet brandSet, locationSet, carSet;
            pst = conn.prepareStatement("select type, power, airbags, mileage, status, agency_id from car where id=?");
            pst.setInt(1,carId);
            carSet = pst.executeQuery();
            pst = conn.prepareStatement("select company, model from brand where car_id=?");
            pst.setInt(1,carId);
            brandSet = pst.executeQuery();
            pst = conn.prepareStatement("select longitude, lattitude from location where car_id=?");
            pst.setInt(1,carId);
            locationSet = pst.executeQuery();
            carSet.next();
            brandSet.next();
            locationSet.next();
            return new Car(carId,carSet.getInt("agency_id"),carSet.getInt("power"),
                    carSet.getInt("airbags"), carSet.getInt("mileage"),brandSet.getString("company"),
                    brandSet.getString("model"), locationSet.getDouble("longitude"), locationSet.getDouble("lattitude"),
                    carSet.getString("status"), carSet.getString("type"));
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Error in getCarById!");
        return null;
    }

    public ArrayList<Car> getFreeCars(){
        ArrayList<Car> cars = new ArrayList<Car>();
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("select id from car where status='free'");
            rs = pst.executeQuery();
            while(rs.next()){
                cars.add(getCarById(rs.getInt("id")));
            }
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return cars;
    }

    public ArrayList<String> getCarOpinions(Car car)
    {
        ArrayList<String> opinions = new ArrayList<>();
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("select opinion, opinion.\"Date\", opinion.rate,customer.first_name from OPINION  inner join customer on OPINION.CUSTOMER_ID = CUSTOMER.ID  where CAR_ID = ?");
            pst.setInt(1,car.getId());

            rs = pst.executeQuery();
            while(rs.next()){
               opinions.add(rs.getString("first_name") +" *"+rs.getInt("rate")+"*\t"+rs.getString("date").substring(0,10)+"\n"+rs.getString("opinion"));

            }
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return opinions;
    }

}