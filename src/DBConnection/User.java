package DBConnection;

public class User {
    private double longitude;
    private double lattitude;

    public User(double longitude, double lattitude){
        this.longitude=longitude;
        this.lattitude=lattitude;
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
}
