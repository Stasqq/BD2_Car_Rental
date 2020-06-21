package DBConnection;

import java.util.Date;

public class User {
    private int id;

    private double longitude;
    private double lattitude;

    private String nick;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Date addDate;

    public User(double longitude, double lattitude){
        this.longitude=longitude;
        this.lattitude=lattitude;
    }

    public User(int id, String nick, String email, String firstName, String lastName, String phone, Date addDate){
        this.id=id;
        this.nick=nick;
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.addDate=addDate;
        this.lattitude = -181;
        this.longitude = -181;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public int getId(){
        return this.id;
    }

    public String getNick() {
        return nick;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Date getAddDate() {
        return addDate;
    }
}
