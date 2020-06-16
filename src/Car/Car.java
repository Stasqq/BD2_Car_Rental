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

    private String status;
    private String type;

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
        this.status=status;
        this.type=type;
    }

    public String getModel(){
        return model.get();
    }

    public String getCompany(){
        return company.get();
    }
}
