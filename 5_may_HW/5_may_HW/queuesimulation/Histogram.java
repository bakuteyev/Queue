package queuesimulation;

import java.util.Locale;

/**
 * Created on 23.03.2017 12:09:40
 *
 * @author Alexander Mikhailovich Kovshov
 */
public class Histogram {
    private final int[] bins;
    private final double minValue;
    private final double maxValue;
    private final double binDiff;

    /**
     * Creates new instance for single data set.
     *
     * @param bins     Number of histogram bins. All the bins have the same width.
     * @param minValue Right bound of the first bin.
     * @param maxValue Left bound of the last bin.
     */
    public Histogram(int bins, double minValue, double maxValue) {
        this.bins = new int[bins + 2];//Two additional bins for out-of-bounds values.
        this.minValue = minValue;
        this.maxValue = maxValue;
        binDiff = (maxValue - minValue) / bins;
    }

    /**
     * Constructor with precalculated array of bins.
     *
     * @param bins     Precalculated array of bins.
     * @param minValue Right bound of the first bin.
     * @param maxValue Left bound of the last bin.
     */
    private Histogram(int[] bins, double minValue, double maxValue) {
        this(bins.length - 2, minValue, maxValue);
        System.arraycopy(bins, 0, this.bins, 0, bins.length);
    }

    /**
     * Creates another histogram with integrated bins.
     *
     * @return Integrated histogram.
     */
    public Histogram makeIntegratedHistogram() {
        int[] inBins = new int[bins.length];
        inBins[0] = bins[0];
        for (int i = 1; i < inBins.length; i++) {
            inBins[i] = inBins[i - 1] + bins[i];
        }
        return new IntegratedHistogram(inBins, minValue, maxValue);
    }

    /**
     * Puts a value into corresponding histogram bin.
     *
     * @param value
     */
    public void put(double value) {
        int k = (int) ((value - minValue) / binDiff + 1);
        if (k < 0) k = 0;
        if (k >= bins.length) k = bins.length - 1;
        bins[k]++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "          <     ->   %d\r\n", bins[0]));
        for (int i = 1; i < bins.length - 1; i++) {
            sb.append(String.format(Locale.US, "    % 8.6f   ->   %d\r\n", minValue + binDiff * (i - 1), bins[i]));
        }
        sb.append(String.format(Locale.US, " >= % 8.6f   ->   %d\r\n", maxValue, bins[bins.length - 1]));
        return sb.toString();
    }

    private final static class IntegratedHistogram extends Histogram {

        private IntegratedHistogram(int[] bins, double minValue, double maxValue) {
            super(bins, minValue, maxValue);
        }

        @Override
        public void put(double value) {
            throw new UnsupportedOperationException("Put operation is not supported for IntegratedHistogram.");
        }
    }

}
