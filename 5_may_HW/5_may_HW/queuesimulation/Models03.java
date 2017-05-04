package queuesimulation;

import java.util.Arrays;

/**
 * Created on 10.04.2013 17:40:42
 * @author Alexander Mikhailovich Kovshov
 */
public class Models03 {

    private static QueueStatistics taskNunits(
            int attempts, int numOfUnits,
            Incoming inc, Waiting queue) {

        //Возвращаемое значение - сводные данные по системе массового обслуживания.
        QueueStatistics ret =  new QueueStatistics();
        //Текущее время
        double curTime = 0.;
        //Суммарное время ожидания обслуживания.
        double sumWaitingTime = 0.;
        //Суммарное время простоя приборов
        double[] sumUnitsFreeTime = new double[numOfUnits];
        Arrays.fill(sumUnitsFreeTime, 0.);
        //Счётчик числа отказов в обслуживании.
        int busyCount = 0;
        //Время, которое осталось до освобождения каждого из приборов.
        double[] units = new double[numOfUnits];
        //Изначально все приборы свободны.
        Arrays.fill(units, 0.);
        //Начальное обращение (заявка на обслуживание) из входящего потока
        CallReq req = inc.nextCall();
        //Начальное обращение занимает начальный прибор на своё время обслуживания.
        units[0] = req.amount;// в предыдущих версиях ошибка: req.time;

        //Перебор всех обращений
        for (int i = 1; i < attempts; i++) {
            //Очередное обращение
            req = inc.nextCall();
            //Промежуток времени до следующего обращения.
            double interval = req.time;
            curTime += interval;
            //Перебор всех приборов. Уменьшается время,
            //оставшееся до освобождения прибора на прошедший промежуток времени.
            for (int j = 0; j < units.length; j++) {
                units[j] -= interval;
            }
            //Блок поиска свободных приборов.
            blockBusy:
            {
                //Переупорядочивание приборов в порядке освобождения
                int[] unitsNums = sortFreeUnits(units);
                //Перебор всех приборов в порядке освобождения.
                for (int j = 0; j < unitsNums.length; j++) {
                    //Если время, оставшееся до освобождения прибора, неположительное,
                    //то прибор считается свободным.
                    if (units[unitsNums[j]] <= 0) {
                        if(queue.size() > 0) {
                            //В очереди есть заявки - заявка поступает в обработку
                            CallReq servReq = queue.get(0);
                            units[unitsNums[j]] += servReq.amount;
                            //Заявка удаляется из очереди
                            queue.remove(0, curTime);
                            sumWaitingTime += servReq.getWaitingTime();
                        }
                        else {
                            //Очередь пуста.
                            //Пришедшая заявка поступает в обработку.
                            //Учитывается время простоя прибора.
                            sumUnitsFreeTime[unitsNums[j]] -= units[unitsNums[j]];
                            //Прибору присваивается время занятости.
                            units[unitsNums[j]] = req.amount;
                            //Поскольку найден свободный прибор для пришедшей заявки,
                            //то производится выход из блока
                            break blockBusy;
                        }
                    }
                }
                //Если внутри цикла не было найдено свободного прибора для поступившей заявки,
                //то выхода из блока не было.
                //Делается попытка поставить заявку в очередь
                if(!queue.add(req, curTime)){
                    //Если очередь заполнена, то счётчик отказов увеличивается на 1.
                    busyCount++;
                }
            }//blockBusy
            //Необходимо обнулить время свободных приборов, если такие есть.
            for (int j = 0; j < units.length; j++) {
                if(units[j] < 0) {
                    sumUnitsFreeTime[j] -= units[j];
                    units[j] = 0;
                }
            }
        }
        double sumUnitsFreePartTime = 0;
        for (int i = 0; i < sumUnitsFreeTime.length; i++) {
            sumUnitsFreePartTime += (sumUnitsFreeTime[i] /= curTime);
        }
        //Отношение числа отказов к числу обращений - вероятность отказов.
        ret.rejectProbability = (double)busyCount / (double)attempts;
        ret.avarageWaitingTime = sumWaitingTime /  (double)attempts;
        ret.unitsFreePartTime = sumUnitsFreeTime;
        ret.unitsSumFreePartTime = sumUnitsFreePartTime;
        return ret;
    }

    /**
     * Переупорядочивает приборы в порядке их освобождения
     * @param units
     * @return
     */
    public static int[] sortFreeUnits(double[] units) {
        int[] freeUnitsNums = new int[units.length];
        for (int i = 0; i < freeUnitsNums.length; i++) {
            freeUnitsNums[i] = i;
        }
        //Повторяется сортировка методом пузырька
        for (int j = 0; j < freeUnitsNums.length - 1; j++) {
            //Самый поздний прибор из оставшихся окажется в конце
            for (int i = 0; i < freeUnitsNums.length - j - 1; i++) {
                if (units[freeUnitsNums[i]] > units[freeUnitsNums[i + 1]]) {
                    int ti = freeUnitsNums[i];
                    freeUnitsNums[i] = freeUnitsNums[i + 1];
                    freeUnitsNums[i + 1] = ti;
                }
            }
        }
        return freeUnitsNums;
    }

    /**
     * Запускает модель системы массового обслуживания
     */
    private static void test_TaskNUnits(){
        int n = 1000000;
        int    nu = 5;
        double lambda = 12;
        double srv = 0.25;
        int waitingRoomSize = 0;
        Incoming income = new Incoming();
        income.timeIntervals = ExponentialSequence.createByMean(1. / lambda);
        income.reqAmounts = UniformSequence.createByMeanAndDeviation(srv, srv * 0.5);
        Waiting queue = new Waiting();
        queue.capacity = waitingRoomSize;
        QueueStatistics res = taskNunits(n, nu, income, queue);

        System.out.println("------------------------------------"
                + "\r\n Число обращений: " + n
                + "\r\n количество приборов: " + nu
                + "\r\n среднее число обращений в единицу времени: " + lambda   //интенсивность потока
                + "\r\n среднее время обслуживания: " + srv
                + "\r\n число мест для ожидания: " + waitingRoomSize
                + "\r\n среднее время ожидания: " + res.avarageWaitingTime
                + "\r\n вероятность отказа в обслуживании: " + res.rejectProbability
                + "\r\n относительный простой всех приборов: " + res.unitsSumFreePartTime
                + "\r\n средний относительный простой одного прибора: " + (res.unitsSumFreePartTime / nu)
        );
        for (int i = 0; i < res.unitsFreePartTime.length; i++) {
            System.out.println( "[" + i + "]  " + res.unitsFreePartTime[i]);

        }
    }

    public static void main(String[] args) {
        //test_sortFreeUnits();
        test_TaskNUnits();
    }

    public static class QueueStatistics {

        public double rejectProbability;
        public double avarageWaitingTime;
        public double[] unitsFreePartTime;
        public double unitsSumFreePartTime;
    }
//==============================================================================


    public static void test_sortFreeUnits(){
        double[] src = { 5, -5, -2, -3 , -4, 0, 0, 0, 3, 2, 1};
        int res[] = sortFreeUnits(src);
        for (int i = 0; i < res.length; i++) {
            System.out.print(src[res[i]] + ", ");
        }
        System.out.println("");
    }


}

