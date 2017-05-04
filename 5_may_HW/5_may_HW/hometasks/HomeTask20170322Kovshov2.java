package hometasks;

import java.util.Locale;
import queuesimulation.Histogram;

/**
 * Пример выполнения домашнего задания на 22 марта 2017 года.
 * (Только вторая задача.)
 * @author Kovshov
 */
public class HomeTask20170322Kovshov2 {

    public static void main(String[] args) {
        int n = 10;
        //Гистограмма из десяти столбцов на промежутке [0, 1].
        Histogram histo = new Histogram(n, 0, 1);
        for (int i = 0; i < 10000000; i++) {
            histo.put(Math.random() * Math.random());
        }
        System.out.println("Число попаданий в заданные промежутки.");
        System.out.println(histo);
        double lastProbability = 0;
        double dx = 1 / (double) n;
        //Граница между столбцами гистограммы.
        for(double x = dx; x <= 1; x += dx) {
            //Вероятность попадания в каждый столбец,
            //вычисляемая по интегральной функции распределения.
            double probability = x - x * Math.log(x);
            System.out.println(String.format(Locale.US, "p(%6.4f <= t < %6.4f) = %6.4f", x - dx, x, probability - lastProbability));
            lastProbability = probability;
        }
        System.out.println("");
        System.out.println("Суммарные попадания в младшие промежутки.");
        System.out.println(histo.makeIntegratedHistogram());
        for(double x = 0; x <= 1; x += dx) {
            //Значения интегральной функции распределения.
            double probability = x <= 0 ? 0 : x >= 1 ? 1 : x - x * Math.log(x);
            System.out.println(String.format(Locale.US, "p(t < %6.4f) = %6.4f", x, probability));
        }
    }

}
