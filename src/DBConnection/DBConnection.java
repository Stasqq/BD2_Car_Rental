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

    private ArrayList<Car> dbCarRequest(String requestString){
        ArrayList<Car> cars = new ArrayList<Car>();
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement(requestString);
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

    public ArrayList<Car> getCars(){
        return dbCarRequest("select id from car");
    }

    public ArrayList<Car> getCarsInRange(double userLongitude, double userLattitude,double range){
        double minLongitude = userLongitude-range,
                maxLongitude = userLongitude+range,
                minLattitude = userLattitude-range,
                maxLattitude = userLattitude+range;
        if(minLattitude < -180)
            minLattitude += 360;
        if(maxLattitude > 180)
            maxLattitude -= 360;
        if(minLongitude < -180)
            minLongitude += 360;
        if(maxLongitude > 180)
            maxLongitude -= 360;
        if(maxLattitude <=  minLattitude && maxLongitude <=  minLongitude)
            return dbCarRequest("select id from car where id in (select car_id from location where " +
                    "(lattitude < "+ maxLattitude +" and longitude < "+ maxLongitude +")" +
                    "or (longitude > "+ minLongitude +" and lattitude > "+ minLattitude +") or " +
                    "(longitude > "+ minLongitude +" and lattitude < "+ maxLattitude +") or " +
                    "(longitude < "+ maxLongitude +" and lattitude > "+ minLattitude +"))");
        else if(maxLattitude <=  minLattitude)
            return dbCarRequest("select id from car where id in (select car_id from location where " +
                    "(longitude between "+ minLongitude +" and "+ maxLongitude +") and (lattitude > "+ minLattitude +" or " +
                    "lattitude < "+maxLattitude+"))");
        else if(maxLongitude <= minLongitude)
            return dbCarRequest("select id from car where id in (select car_id from location where " +
                    "(lattitude between "+ minLattitude +" and "+ maxLattitude +") and (longitude > "+ minLongitude +" or " +
                    "longitude < "+maxLongitude+"))");
        return dbCarRequest("select id from car where id in (select car_id from location where (lattitude between "+ minLattitude +" and "+ maxLattitude +")" +
                "and (longitude between "+ minLongitude +" and "+ maxLongitude +"))");
    }

    private Car getCarById(int carId){
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
}