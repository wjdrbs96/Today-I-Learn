package Java;

public class Test {
    public static void main(String[] args) {
        Car car = new Car();
        Car car2 = null;
        FireEngine fe = null;

        fe = (FireEngine)car;
    }
}

class Car { }

class FireEngine extends Car { }




