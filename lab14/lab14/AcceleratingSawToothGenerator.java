package lab14;

public class AcceleratingSawToothGenerator extends SawToothGenerator {

    private double factor;

    public AcceleratingSawToothGenerator(int p, double f) {
        super(p);
        factor = f;
    }

    @Override
    public double next() {
        state++;
        if (state % period == 0) {
            period = (int) (period * factor);
            state = 0;
        }
        return normalize(state);
    }


    
    
}
