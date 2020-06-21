package DBConnection;

import Car.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;

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

    public void addAccount(String nick, String password,String email, String firstName, String lastName, String phone){
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("insert into customer (Nick, Email, Pasword_Hash, Pasword_Hash_salt,first_Name,last_Name,Phone,Add_date) values (?,?,?,?,?,?,?,?)");
            SecureRandom random = new SecureRandom();
            byte[] hashedPassword;
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            pst.setString(1,nick);
            pst.setString(2,email);
            pst.setString(3,Base64.getEncoder().encodeToString(hashedPassword));
            pst.setString(4,Base64.getEncoder().encodeToString(salt));
            pst.setString(5,firstName);
            pst.setString(6,lastName);
            pst.setString(7,phone);
            pst.setDate(8, new Date(System.currentTimeMillis()));
            rs = pst.executeQuery();
            conn.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public User singIn(String email, String password){
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("select * from customer" +
                    " where Email = ?");
            pst.setString(1,email);
            rs = pst.executeQuery();
            if(rs.next()){
                String saltString = rs.getString("Pasword_Hash_salt");
                String hashString = rs.getString("Pasword_Hash");
                byte[] hashedPassword;
                byte[] salt = Base64.getDecoder().decode(saltString);
                MessageDigest md = null;
                md = MessageDigest.getInstance("SHA-512");
                md.update(salt);
                hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
                if(Base64.getEncoder().encodeToString(hashedPassword).equals(hashString)){
                    User user = new User(rs.getInt("id"),rs.getString("Nick"),rs.getString("Email"),
                            rs.getString("First_name"),rs.getString("Last_name"),
                            rs.getString("Phone"),new java.util.Date(rs.getDate("Add_Date").getTime()));
                    conn.close();
                    return user;
                }else{
                    conn.close();
                    return null;
                }
            }else{
                conn.close();
                return null;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
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

    public void addRental(int userId, Car car,double cost)
    {

        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("select Id from Employee where agency_id = ?");
            pst.setInt(1,car.getAgency_id());
            rs = pst.executeQuery();
            rs.next();
            int employeeId = rs.getInt("id");


            pst = conn.prepareStatement("INSERT INTO rental (Start_date, state,EMPLOYEE_ID,CAR_ID,CUSTOMER_ID,PAYMENT_ID,cost) values ('22/06/20','closed',?,?,?,1,?)");
            pst.setInt(2,car.getId());
            pst.setInt(1,employeeId);
            pst.setInt(3,userId);
            pst.setDouble(4,cost);

            rs = pst.executeQuery();

            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public boolean isFree(Car car)
    {
        String status = "";
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("select status from car where id = ?");
            pst.setInt(1,car.getId());
            rs = pst.executeQuery();
            rs.next();
             status = rs.getString("status");




            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return status.equals("free");


    }
    public void setCarFree(Car car)
    {
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("update car  set status = ? where id = ?");
            pst.setInt(2,car.getId());
            pst.setString(1,"free");
            pst.executeQuery();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setCarRented(Car car)
    {
        try{
            conn = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
            pst = conn.prepareStatement("update car  set status = ? where id = ?");
            pst.setInt(2,car.getId());
            pst.setString(1,"rented");
            pst.executeQuery();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}