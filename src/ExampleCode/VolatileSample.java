package ExampleCode;

/**
 * VolatileSample
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2024. 11. 18.
 */
public class VolatileSample extends Thread {
    private double instanceVariable = 0;

    public void setDouble(double value) {
        this.instanceVariable = value;
    }

    @Override
    public void run() {
        while (instanceVariable == 0) {
            System.out.println(instanceVariable);
        }
    }
}
