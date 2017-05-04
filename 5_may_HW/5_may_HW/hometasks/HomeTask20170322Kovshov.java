package hometasks;

import queuesimulation.Histogram;

/**
 * Пример выполнения домашнего задания на 22 марта 2017 года.
 * (Только вторая задача.)
 * @author Kovshov
 */
public class HomeTask20170322Kovshov {

    public static void main(String[] args) {
        //Гистограмма из двух столбцов на промежутке [0, 1].
        Histogram histo = new Histogram(2, 0, 1);
        for (int i = 0; i < 10000000; i++) {
            histo.put(Math.random() * Math.random());
        }
        System.out.println(histo);
        //Граница между столбцами гистограммы.
        double x = 0.5;
        //Вероятность попадания в первый столбец,
        //вычисляемая по интегральной функции распределения.
        double probability = x - x * Math.log(x);
        System.out.println("p(t < 0.5) = " + probability);
    }

}
