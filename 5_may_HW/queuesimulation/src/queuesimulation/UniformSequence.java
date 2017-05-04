package queuesimulation;

/**
 * Задаёт случайную последовательность чисел на отрезке [0,1).
 * Предполагается усовершенствование класса.
 *
 * Created on 04.04.2013 17:47:08
 * @author Alexander Mikhailovich Kovshov
 * @date 04.04.2013 17:47:08
 */
public class UniformSequence extends RandomSequence{

    /**
     * Множитель случайной величины
     */
    double multiplier = 1.;
    double increment = 0.;

    public UniformSequence() {
    }

    private UniformSequence(double mean) {
        super(mean, mean * mean / 12.);
        multiplier = mean * 2.;
    }

    private UniformSequence(double mean, double deviation) {
        super(mean, deviation * deviation / 3.);
        multiplier = deviation * 2.;
        increment = mean - deviation;
    }


    private UniformSequence(double mean, long seed) {
        super(mean, mean * mean / 12., seed);
        multiplier = mean * 2.;
    }

    private UniformSequence(double mean, double deviation, long seed) {
        super(mean, deviation * deviation / 3., seed);
        multiplier = deviation * 2.;
        increment = mean - deviation;
    }

    /**
     * Создание по мат.ожиданию (разброс от нуля до двух мат.ожиданий)
     * @param mean
     * @return
     */
    public static UniformSequence createByMean(double mean){
        return new UniformSequence(mean);
    }

    /**
     * Создание по мат.ожиданию и наибольшему отклонению от мат.ожидания
     * @param mean
     * @param deviation
     * @return
     */
    public static UniformSequence createByMeanAndDeviation(double mean, double deviation){
        return new UniformSequence(mean, deviation);
    }

    /**
     * Создание по мат.ожиданию и дисперсии
     * @param mean
     * @param variance
     * @return
     */
    public static UniformSequence createByMeanAndVariance(double mean, double variance){
        return new UniformSequence(mean, Math.sqrt(3 * variance));
    }

    /**
     * Создание по границам изменения
     * @param min
     * @param max
     * @return
     */
    public static UniformSequence createByInterval(double min, double max){
        return new UniformSequence((max + min) / 2., (max - min) / 2.);
    }


    public static UniformSequence createByMeanWithSeed(double mean, long seed){
        return new UniformSequence(mean, seed);
    }


    /**
     *
     * @return
     */
    @Override
    public double getNext() {
        return getRand().nextDouble() * multiplier + increment;
    }

}
