package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {

    protected int period, state;

    public SawToothGenerator(int p) {
        period = p;
        state = 0;
    }

    protected double normalize(int s) {
        double temp = (double) s / (period - 1);
        return temp * 2 - 1;
    }

    @Override
    public double next() {
        state++;
        return normalize(state % period);
    }
    
}
