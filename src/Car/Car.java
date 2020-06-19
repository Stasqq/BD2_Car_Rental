package Car;


import javafx.beans.property.SimpleStringProperty;

public class Car {
    private int id;
    private int agency_id;

    private int power;
    private int airbags;
    private int mileage;

    private final SimpleStringProperty company;
    private final SimpleStringProperty model;

    private double longitude;
    private double lattitude;

    private Status status;
    private Type type;

    public Car(int id, int agency_id, int power, int airbags, int mileage,
    String company, String model, double longitude, double lattitude, String status,
               String type){
        this.id=id;
        this.agency_id=agency_id;
        this.power=power;
        this.airbags=airbags;
        this.mileage=mileage;
        this.company= new SimpleStringProperty(company);
        this.model= new SimpleStringProperty(model);
        this.longitude=longitude;
        this.lattitude=lattitude;
        switch (status) {
            case "free":
                this.status = Status.FREE;
                break;
            case "rented":
                this.status = Status.RENTED;
                break;
            case "broken":
                this.status = Status.BROKEN;
                break;
        }
        switch (type) {
            case "hybrid":
                this.type = Type.HYBRID;
                break;
            case "electric":
                this.type = Type.ELECTRIC;
                break;
            case "combustion":
                this.type = Type.COMBUSTION;
                break;
        }
    }



    public String getModel() {
        return model.get();
    }

    public String getCompany() {
        return company.get();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }

    public int getPower() {
        return power;
    }

    public int getMileage() {
        return mileage;
    }

    public int getId() {
        return id;
    }
}
