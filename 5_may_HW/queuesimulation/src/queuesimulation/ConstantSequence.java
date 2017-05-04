package queuesimulation;

/**
 * Возвращает постоянную величину.
 *
 * Created on 04.04.2013 21:25:40
 * @author Alexander Mikhailovich Kovshov
 * @date 04.04.2013 21:25:40
 */
public class ConstantSequence  extends RandomSequence{

    static RandomSequence createByMean(double mean) {
        return new ConstantSequence(mean);
    }

    public ConstantSequence(double mean) {
        super(mean, 0);
    }


    @Override
    public double getNext() {
        return getMean();
    }

}
