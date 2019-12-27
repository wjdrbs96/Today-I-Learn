package inheritance.constructor;

public class Vehicle {
    String name = "차량";
    public double maxSpeed;
    public int seater;

    public Vehicle(){

    }

    public Vehicle(double maxSpeed, int seater){
        this.maxSpeed = maxSpeed;
        this.seater = seater;
    }
}
