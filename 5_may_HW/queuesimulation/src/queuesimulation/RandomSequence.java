package queuesimulation;

import java.util.Random;

/**
 * Абстрактный класс для описания различных случайных величин в классах-потомках.
 *
 * Created on 04.04.2013 17:06:20
 * @author Alexander Mikhailovich Kovshov
 * @date 04.04.2013 17:06:20
 */
public abstract class RandomSequence {

    private Random rand;
    private double mean = 0;
    private double variance = 1;

    public RandomSequence() {
        rand = new Random();
        mean = 0;
    }

    public RandomSequence(double mean, double variance) {
        this.mean = mean;
        this.variance = variance;
        //rand = new Random(new java.util.Date().getTime());
        rand = new Random();
    }

    public RandomSequence(long seed) {
        rand = new Random(seed);
    }

    public RandomSequence(double mean, double variance, long seed) {
        rand = new Random(seed);
        this.mean = mean;
        this.variance = variance;
    }

    protected Random getRand() {
        return rand;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }



    public abstract double getNext() ;

}
