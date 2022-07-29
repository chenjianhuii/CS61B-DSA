package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private Percolation p;
    private double mean, stddev, confLow, confHigh;

    /** perform one experiments on an N-by-N grid. */
    private double simulation(int N, int T) {
        int upperBound = N * N;
        StdRandom.setSeed(T);
        while (!p.percolates()) {
            int index = StdRandom.uniform(upperBound);
            p.open(index / N, index % N);
        }
        return (double) p.numberOfOpenSites() / upperBound;
    }
    /** perform T independent experiments on an N-by-N grid. */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException("Both N and T should be greater than 0.");
        }
        double[] result = new double[T];
        for (int i = 0; i < T; i++) {
            p = pf.make(N);
            result[i] = simulation(N, i);
        }
        mean = StdStats.mean(result);
        stddev = StdStats.stddev(result);
        double width = 1.96 * stddev / Math.sqrt(T);
        confLow = mean - width;
        confHigh = mean + width;
    }
    /** sample mean of percolation threshold. */
    public double mean() {
        return mean;
    }
    /** sample standard deviation of percolation threshold. */
    public double stddev() {
        return stddev;
    }
    /** low endpoint of 95% confidence interval. */
    public double confidenceLow() {
        return confLow;
    }
    /** high endpoint of 95% confidence interval. */
    public double confidenceHigh() {
        return confHigh;
    }

}
