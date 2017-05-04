package queuesimulation;

/**
 * Created on 04.04.2013 22:41:22
 * @author Alexander Mikhailovich Kovshov
 * @date 04.04.2013 22:41:22
 */
public class ExponentialSequence  extends RandomSequence{

    private ExponentialSequence(double mean) {
        super(mean, mean * mean);
    }

    private ExponentialSequence(double mean, long seed) {
        super(mean, mean * mean, seed);
    }

    public static ExponentialSequence createByMean(double mean){
        return new ExponentialSequence(mean);
    }

    public static ExponentialSequence createByMeanWithSeed(double mean, long seed){
        return new ExponentialSequence(mean, seed);
    }

    @Override
    public double getNext() {
        return  -Math.log(1 - getRand().nextDouble()) * getMean();
    }

}
