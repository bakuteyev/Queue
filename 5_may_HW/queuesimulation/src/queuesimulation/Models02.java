package queuesimulation;

import java.util.Arrays;

/**
 * Created on 04.04.2013 11:33:52
 * @author Alexander Mikhailovich Kovshov
 * @date 04.04.2013 11:33:52
 */
public class Models02 {
    private static double taskNunits(
            int attempts, int numOfUnits,
            Incoming inc) {

        //Возвращаемое значение - вероятность отказа в обслуживании.
        double ret = 0.;
        //Счётчик числа отказов в обслуживании.
        int busyCount = 0;
        //Время, которое осталось до освобождения каждого из приборов.
        double[] units = new double[numOfUnits];
        //Изначально все приборы свободны.
        Arrays.fill(units, 0.);
        //Начальное обращение (заявка на обслуживание) из входящего потока
        CallReq req = inc.nextCall();
        //Начальное обращение занимает начальный прибор на своё время обслуживания.
        units[0] = req.time;
        //Номер очередного прибора для рассмотрения заявки
        int ku = 0;
        //Перебор всех обращений
        for (int i = 1; i < attempts; i++) {
            //Очередное обращение
            req = inc.nextCall();
            //Промежуток времени до следующего обращения.
            double interval = req.time;
            //Перебор всех приборов и, если прибор занят, то уменьшается время,
            //оставшееся до освобождения прибора на прошедший промежуток времени.
            for (int j = 0; j < units.length; j++) {
                if (units[j] > 0) {
                    units[j] -= interval;
                }
            }
            //Блок поиска свободных приборов.
            blockBusy:
            {
                //Перебор всех приборов.
                for (int j = 0; j < units.length; j++) {
                    int k = ku + j; if(k >= units.length) k -= units.length;
                    //Если время, оставшееся до освобождения прибора, неположительное,
                    //то прибор считается свободным.
                    if (units[k] <= 0) {
                        //Свободному прибору присваивается время занятости
                        units[k] = req.amount;
                        //ku = k + 1;
                        //ku++;
                        if(ku >= units.length) ku -= units.length;
                        //Если найден свободный прибор, то другие уже не рассматриваются,
                        //и производится выход из блока
                        break blockBusy;
                    }
                }
                //Если внутри цикла не было найдено свободного прибора, то выхода из блока не было.
                //Счётчик отказов увеличивается на 1.
                busyCount++;
            }
        }
        //Отношение числа отказов к числу обращений - вероятность отказов.
        ret = (double)busyCount / (double)attempts;
        return ret;
    }

    /**
     * Запускает модель системы массового обслуживания
     */
    private static void test_TaskNUnits(){
        int n = 1000000;
        int    nu = 1;
        double lambda = 1;
        double srv = 1;
        Incoming income = new Incoming();
//        income.timeIntervals = new ExponentialSequence(1. / lambda);
        income.timeIntervals = ExponentialSequence.createByMean(1. / lambda);
//        income.reqAmounts = new ExponentialSequence(srv);
        income.reqAmounts = ExponentialSequence.createByMean(srv);
        System.out.println("------------------------------------"
                + "\r\n Число обращений: " + n
                + "\r\n количество приборов: " + nu
                + "\r\n среднее число обращений в единицу времени: " + lambda   //интенсивность потока
                + "\r\n среднее время обслуживания: " + srv
                + "\r\n вероятность отказа в обслуживании: " + taskNunits(n, nu, income));
    }

    public static void main(String[] args) {
        test_TaskNUnits();
    }

}
