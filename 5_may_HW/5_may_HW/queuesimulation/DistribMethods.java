package queuesimulation;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created on 15.03.2013 1:48:38
 *
 * @author Alexander Mikhailovich Kovshov
 * @date 15.03.2013 1:48:38
 */
public class DistribMethods {
    /**
     * Возвращает массив заданной длины упорядоченных в порядке возрастания
     * равномерно распределённых случайных чисел на отрезке от 0 до 1.
     *
     * @param dim dimention of array
     * @return sorted in ascending order array of random double numbers between
     * 0 and 1 with dimension specified.
     */
    public static double[] randomArray(int dim) {
        double[] ret = new double[dim];
        Random rndm = new Random(new Date().getTime());
        for (int i = 0; i < ret.length; i++) {
            //ret[i] = rndm.nextDouble();
            ret[i] = Math.random();
        }
        Arrays.sort(ret);
        return ret;
    }

    /**
     * Проверяет работу метода, выдающего случайный упорядоенный массив.
     */
    public static void test_randomArray() {
        double[] array = randomArray(100000);
        for (int i = 0; i < array.length; i++) {
            double d = array[i];
            System.out.println(""
                    + String.format(Locale.US, "% 3d", i) + " :   "
                    + String.format(Locale.US, "% 5.3f", new Double(d)));
        }
    }

    /**
     * Возвращает упорядоченный в порядке возрастания массив вещественных чисел,
     * являющихся разностями между соседними элемнтами массива, полученного
     * методом @code{randomArray(int dim)}.
     *
     * @param dim dimension of output array
     * @return
     */
    public static double[] randomArrayDiff(int dim) {
        double[] arr = randomArray(dim + 1);
        double[] ret = new double[dim];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = arr[i + 1] - arr[i];
        }
        Arrays.sort(ret);
        return ret;
    }

    /**
     * Проверяет работу метода, выдающего случайный упорядоенный массив.
     */
    public static void test_randomArrayDiff() {
        double[] array = randomArrayDiff(100);
        for (int i = 0; i < array.length; i++) {
            double d = array[i];
            System.out.println(""
                    + String.format(Locale.US, "% 3d", i) + " :   "
                    + String.format(Locale.US, "% 8.3f", new Double(d)));
        }
    }

    /**
     * Проверяет работу метода, выдающего случайный упорядоенный массив с
     * построением гистограммы.
     */
    public static void test_randomArrayDiffHisto() {
        double[] array = randomArrayDiff(100);
        for (int i = 0; i < array.length; i++) {
            double d = array[i];
            System.out.println(""
                    + String.format(Locale.US, "% 3d", i) + " :   "
                    + String.format(Locale.US, "% 8.3f", new Double(d)));
        }

        double inf = 0;
        double sup = 0.05;
        int[] histo = makeHistogram(10, inf, sup, array);
        printHisto(inf, sup, histo);
    }

    /**
     * Строит гистограмму с заданным числом столбцов.
     * Возвращаемый массив содержит на два столбика больше для
     * значений, выходящих за границы допустимых значений.
     *
     * @param bins     number of histogram bins (columns)
     * @param minValue
     * @param maxValue
     * @param data     array of data to analyse
     * @return
     */
    public static int[] makeHistogram(int bins, double minValue, double maxValue, double[] data) {
        int[] histo = new int[bins + 2];
        double valDiff = maxValue - minValue;
        for (double aData : data) {
            int k = (int) ((aData - minValue) / valDiff * bins + 1);
            if (k < 0) k = 0;
            if (k >= histo.length) k = histo.length - 1;
            histo[k]++;
        }
        return histo;
    }

    public static void printHisto(double minValue, double maxValue, int[] histo) {
        double binDiff = (maxValue - minValue) / (histo.length - 2);
        System.out.println("          < " + "   ->   " + histo[0]);
        for (int i = 1; i < histo.length - 1; i++) {
            System.out.println("    " + String.format(Locale.US, "% 8.6f", new Double(minValue + binDiff * (i - 1))) + "   ->   " + histo[i]);
        }
        System.out.println(" >= " + String.format(Locale.US, "% 8.6f", new Double(maxValue)) + "   ->   " + histo[histo.length - 1]);

    }

    public static void testHisto() {
        double[] data = {0.1, 0.2, 0.3, 0.5, 0.8, 1.3, 2.1, 3.4, 5.5};
        int[] histo = makeHistogram(2, 1, 3, data);
        printHisto(1, 3, histo);
    }

    /**
     * запускает программу вычисления вероятности заданного числа обращений на заданном промежутке
     * времени при известной интенсивности входящего потока с разными значениями.
     */
    private static void testEventsProbabilityPerTimeInterval() {
        //Количество обращений.
        int n_k = 1000000;
        //Среднее время между обращениями.
        double k = 2.5;
        //Длина промежутка времени, на котором рассматриваются события.
        int timeIncr = 15;
        //Число событий, вероятность которых вычисляется на заданом промежутке времени.
        int evNum = 3;
        //Среднее число обращений за заданный промежуток времени.
        double evPerTime = (double) timeIncr / k;
        //eventProbabilityPerTimeInterval(n_k, k, timeIncr, lambda, evNum);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 0);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 1);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 2);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 3);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 4);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 5);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 6);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 7);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 8);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 9);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 10);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 11);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 12);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 13);
        eventsProbabilityPerTimeInterval(n_k, k, timeIncr, evPerTime, 14);

    }

    /**
     * Вычисляет вероятность заданного числа обращений на заданном промежутке
     * времени при известной интенсивности входящего потока.
     * Число вхдных параметров избыточно, поскольку @code{lambda = timeIncr / k}
     *
     * @param n_k       общее количество обращений
     * @param k         среднее время между обращениями
     * @param timeIncr  длина промежутка времени, на котором рассматриваются события
     * @param evPerTime среднее число обращений за заданный промежуток времени
     * @param evNum     число событий, вероятность которых вычисляется на заданом промежутке времени
     */
    private static void eventsProbabilityPerTimeInterval(int n_k, double k, int timeIncr, double evPerTime, int evNum) {
        //Выходные параметры
        double avK = 0, avL = 0, sgK = 0, sgL = 0;
        double eventsProbability = 0;
        //Получение упорядоченного по возрастанию массива случайных чисел от 0 до 1.
        double[] rnf = randomArray(n_k);
        //Приведение полученных случайных чисел к рассматриваемой длительности наблюдения.
        for (int i = 0; i < rnf.length; i++) {
            rnf[i] *= n_k * k;
        }

        //Значение текущего времени.
        int curTime = timeIncr;
        //Счётчик промежутков времени.
        int countIncr = 0;
        //Перебор всех промежутков времени, пока не выйдем за пределы длительности наблюдений.
        loop1:
        for (int cnt = 0; ; curTime += timeIncr, countIncr++) {
            //Счётчик обращений за промежуток времени.
            int events = 0;
            while (rnf[cnt] <= curTime) {
                //Пока есть обращения до текущего времени, увеличивается счётчик обращений.
                events++;
                //Переход к следующему обращению с проверкой достижения конца обращений.
                if (++cnt >= rnf.length) break loop1;
                //Время между двумя последовательными обращениями.
                double intrv = rnf[cnt] - rnf[cnt - 1];
                //Суммирование этих отрезков времени для последующей проверки в конце алгоритма.
                avK += intrv;
                //Суммирование квадрата отклонения этих отрезков времени от среднего значения.
                sgK += (intrv - k) * (intrv - k);
            }
            //Суммирование числа обращений (для проверки в конце).
            avL += events;
            //Суммирование квадрата отклонения числа обращений от среднего.
            sgL += (events - evPerTime) * (events - evPerTime);
            //Если число обращений на промежутке совпало с исследуемым,
            //то увеличивается счётчик таких событий.
            if (events == evNum) {
                eventsProbability++;
            }

        }
        //Вывод на консоль значений: общего числа обращений, длины рассматриваемого промежутка времени, 
        //вероятности заданного числа обращений за этот промежуток,...
        System.out.println(
                "число обращений: " + evNum + "\r\n" +
                        "временной отрезок: " + timeIncr + "\r\n" +
                        "вероятность заданного числа обращений за этот промежуток: " + (eventsProbability / countIncr) + "\r\n" +
                        "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
//        System.out.println(
//                " всего обращений: " + n_k + "\r\n" +  
//                " среднее время между обращениями: " + k + "\r\n" +  
//                " расчётное среднее число обращений за заданный промежуток (интенсивность): " + lambda + "\r\n" +  
//                " количество промежутков в рассматриваемой длительности: " + countIncr +  "\r\n" + 
//                " время окончания рассмотрения: " + curTime + "\r\n" + 
//                " получившееся среднее число обращений за заданный промежуток: " + (avL / countIncr) + "\r\n" +  
//                " средний квадрат отклонения числа событий за промежуток от расчётного среднего числа событий за промежуток: " + (sgL / countIncr) +  "\r\n" + 
//                " получившееся среднее время между обращениями: " + (avK / n_k) +  "\r\n" + 
//                " средний квадрат отклонения времени между обращениями от среднего времени между обращениями: " + (sgK / n_k));
    }

    public static void testPoissonDistribution() {
        //Число событий в единицу времени
        double lambda = 6. / 15.;  // 1 / 2.5
        double tau = 15;  //timeIncr
        long fact = 1;
        double lt = lambda * tau;
        double ltk = 1;
        for (int i = 0; i < 15; i++, fact *= i, ltk *= lt) {
            //ltk = Math.pow(lt, i);
            double exlt = Math.exp(-lt);
            double val = ltk / fact * exlt;
            System.out.println("" + i + "   |   " + val);
        }
    }

    public static void main(String[] args) {
//        test_randomArray();
        //test_randomArrayDiff();
//        test_randomArrayDiffHisto();
//        testHisto();
        //calcUnicNumberProbability(1000, 100);
        //testEventsProbabilityPerTimeInterval();
        testPoissonDistribution();
    }

    /**
     * Вычисляет вероятность того, что при повторяющемся случайном выборе числа
     * из заданного последовательного набора будут выбраны разные числа.
     *
     * @param setNum количество чисел
     * @param num    количество случайных выборов.
     */
    public static void calcUnicNumberProbability(int setNum, int num) {
        double sum = 1;
        for (int i = 0; i < num; i++) {
            double d = ((double) (setNum - i) / (double) setNum);
            System.out.println(d);
            sum *= d;
        }
        System.out.println(" n: " + num + ", N: " + setNum + " ;  p: " + sum);
    }


}


