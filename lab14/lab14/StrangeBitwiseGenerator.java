package lab14;

public class StrangeBitwiseGenerator extends SawToothGenerator {

    public StrangeBitwiseGenerator(int p) {
        super(p);
    }

    @Override
    public double next() {
        state++;
        int weirdState = state & (state >> 7) % period;
        return normalize(weirdState);
    }
    
}
