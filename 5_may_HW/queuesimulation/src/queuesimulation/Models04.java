package queuesimulation;

import java.util.Arrays;

/**
 * Модель системы массового обслуживания с очередью. Добавлен расчёт средней
 * длины очереди и среднего времени нахождения в системе.
 * <p>
 * Created on 17.04.2013 16:50:04
 *
 * @author Alexander Mikhailovich Kovshov
 *
 *
 */
public class Models04 {

//Используя модель системы массового обслуживания Models04, решить следующую задачу.
//Система массового обслуживания имеет параметры:
//число обслуживающих приборов — 3,
//интенсивность входящего пуассонова потока — 1.1 заявок в минуту;
//среднее время обслуживания одной заявки каждым прибором — 2 минуты, при этом 
//время обслуживания распределено равномерно с наибольшим отклонением от 
//среднего значения на половину среднего значения;
//наибольшая допустимая длина очереди — 10 заявок.
//Найти с точностью до второго знака после запятой, какое значение должно иметь 
//среднее время обслуживания, чтобы вероятность застать все три прибора свободными 
//стала больше 0.3 (на распечатке это значение в самой нижней строчке правее знака «{3}»).
    public static QueueStatistics taskNunits(
            int attempts, int numOfUnits,
            Incoming inc, Waiting queue) {

        //Возвращаемое значение - сводные данные по системе массового обслуживания.
        QueueStatistics ret = new QueueStatistics();
        //Текущее время
        double curTime = 0.;
        //Суммарное время ожидания обслуживания.
        double sumWaitingTime = 0.;
        //Наибольшее время ожидания обслуживания.
        double maxWaitingTime = 0.;
        //Суммарное время нахождения заявки в системе (ожидания и обслуживания).
        double sumInRocessTime = 0.;
        //Наибольшее время нахождения заявки в системе (ожидания и обслуживания).
        double maxInRocessTime = 0.;
        //Суммарное время простоя приборов
        double[] sumUnitsFreeTime = new double[numOfUnits];
        Arrays.fill(sumUnitsFreeTime, 0.);
        //Суммарное взвешенное число свободных приборов
        double cumFreeUnits = 0;
        //Учётчик продолжительности состояний занятости системы.
        double[] cumFreeUnitsNumber = new double[numOfUnits + 1];
        Arrays.fill(cumFreeUnitsNumber, 0.);
        //Счётчик числа отказов в обслуживании.
        int busyCount = 0;
        //Время, которое осталось до освобождения каждого из приборов.
        double[] units = new double[numOfUnits];
        //Изначально все приборы свободны.
        Arrays.fill(units, 0.);
        //Начальное обращение (заявка на обслуживание) из входящего потока
        CallReq req = inc.nextCall();
        //Начальное обращение занимает начальный прибор на своё время обслуживания.
        units[0] = req.amount;
        //Счётчик свободных приборов
        int freeUnits;

        //Перебор всех обращений
        for (int i = 1; i < attempts; i++) {
            //Очередное обращение
            req = inc.nextCall();
            //Промежуток времени до следующего обращения.
            double interval = req.time;
            //Увеличение счётчика времени
            curTime += interval;
            //Для начала принимается, что свободных приборов нет.
            freeUnits = 0;
            //Учёт состояния
            cumFreeUnitsNumber[freeUnits] += interval;
            //Перебор всех приборов. Уменьшается время,
            //оставшееся до освобождения прибора на прошедший промежуток времени.
            for (int j = 0; j < units.length; j++) {
                units[j] -= interval;
            }
            //Переупорядочивание приборов в порядке освобождения
            int[] unitsNums = sortFreeUnits(units);
            //Блок поиска свободных приборов.
            blockBusy:
            {
                //Перебор всех приборов в порядке освобождения.
                for (int j = 0; j < unitsNums.length; j++) {
                    //Если время, оставшееся до освобождения прибора, неположительное,
                    //то прибор считается свободным.
                    if (units[unitsNums[j]] <= 0) {
                        if (queue.size() > 0) {
                            //В очереди есть заявки - заявка поступает в обработку
                            CallReq servReq = queue.get(0);
                            //Заявка удаляется из очереди
                            queue.remove(0, curTime + units[unitsNums[j]]);
                            //Прибору присваивается время обработки заявки.
                            units[unitsNums[j]] += servReq.amount;
                            //Время ожидания заявки добавляется к общему времени ожидания.
                            sumWaitingTime += servReq.getWaitingTime();
                            //Если ожидание наибольшее, оно сохраняется.
                            if (maxWaitingTime < servReq.getWaitingTime()) {
                                maxWaitingTime = servReq.getWaitingTime();
                            }
                            //Время заявки в системе добавляется к общему времени.
                            double inProc = servReq.getWaitingTime() + servReq.amount;
                            sumInRocessTime += inProc;
                            //Если время заявки в системе наибольшее, оно сохраняется.
                            if (maxInRocessTime < inProc) {
                                maxInRocessTime = inProc;
                            }
                        } else {
                            //Очередь пуста.
                            //Пришедшая заявка поступает в обработку.
                            //Учитывается время простоя прибора.
                            sumUnitsFreeTime[unitsNums[j]] -= units[unitsNums[j]];
                            //                            cumFreeUnits -= units[unitsNums[j]];
                            //Уменьшается суммарное время для состояния полной занятости, учтённого в начале цикла.
                            cumFreeUnitsNumber[freeUnits] += units[unitsNums[j]];
                            freeUnits++;
                            //Увеличивается - для другого, где число свободных приборов больше на 1.
                            cumFreeUnitsNumber[freeUnits] -= units[unitsNums[j]];
                            //Прибору присваивается время занятости.
                            units[unitsNums[j]] = req.amount;
                            //Время заявки в системе добавляется к общему времени.
                            sumInRocessTime += req.amount;
                            //Если время заявки в системе наибольшее, оно сохраняется.
                            if (maxInRocessTime < req.amount) {
                                maxInRocessTime = req.amount;
                            }
                            //Поскольку найден свободный прибор для пришедшей заявки,
                            //то производится выход из блока
                            break blockBusy;
                        }
                    }
                }
                //Если внутри цикла не было найдено свободного прибора для поступившей заявки,
                //то выхода из блока не было.
                //Делается попытка поставить заявку в очередь
                if (!queue.add(req, curTime)) {
                    //Если очередь заполнена, то счётчик отказов увеличивается на 1.
                    busyCount++;
                }
            }//blockBusy
            //Необходимо обнулить время свободных приборов, если такие есть и учесть состояние системы.
            for (int j = 0; j < units.length; j++) {
                if (units[unitsNums[j]] < 0) {
                    //Учитывается время простоя прибора
                    sumUnitsFreeTime[unitsNums[j]] -= units[unitsNums[j]];
                    //Уменьшается суммарное время для ранее учтённого состояния.
                    cumFreeUnitsNumber[freeUnits] += units[unitsNums[j]];
                    freeUnits++;
                    //Увеличивается - для другого, где число свободных приборов больше на 1.
                    cumFreeUnitsNumber[freeUnits] -= units[unitsNums[j]];
                    //Обнуляется время занятости свободного прибора до следующей заявки.
                    units[unitsNums[j]] = 0;
                }
            }
        }
        //Относительное время простоя всех прибора.
        double sumUnitsFreePartTime = 0;
        for (int i = 0; i < sumUnitsFreeTime.length; i++) {
            sumUnitsFreePartTime += (sumUnitsFreeTime[i] /= curTime);
        }
        //Вероятности состояний и среднее число свободных приборов.
        for (int i = 0; i < cumFreeUnitsNumber.length; i++) {
            cumFreeUnits += (double) i * (cumFreeUnitsNumber[i] /= curTime);
        }
        //Отношение числа отказов к числу обращений - вероятность отказов.
        ret.rejectProbability = (double) busyCount / (double) attempts;
        ret.avarageWaitingTime = sumWaitingTime / (double) attempts;
        ret.unitsFreePartTime = sumUnitsFreeTime;
        ret.unitsSumFreePartTime = sumUnitsFreePartTime;
        ret.maxQueueLength = queue.getMaxLength();
        ret.avarageQueueLength = queue.getAvarageLength();
        ret.maxWaitingTime = maxWaitingTime;
        ret.finalQueueLength = queue.size();
        ret.avarageInProcessTime = sumInRocessTime / (double) attempts;
        ret.maxInProcessTime = maxInRocessTime;
        ret.avarageNumberOfFreeUnits = cumFreeUnits;
        ret.unitsFreeConditionProbability = cumFreeUnitsNumber;
        return ret;
    }

    /**
     * Переупорядочивает приборы в порядке их освобождения
     *
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
    private static void test_TaskNUnits() {
        int n = 10000000;
        int nu = 3;
        double lambda = 1.1;

        QueueStatistics res;
        double srv = 0.84;
        // При srv = 2 вероятность того, что все приборы свободны равна0.0140 => нужно уменьшать srv
        // при шаге srv -= 0.01 получили вероятность 0.30273342428391126 при srv=0.819999999999999
        // стартуем с srv=0.83 с шагом srv -= 0.00001;
        int waitingRoomSize = 10;//Waiting.UNLIMITED;
        do {
            Incoming income = new Incoming();
            income.timeIntervals = UniformSequence.createByMean(1. / lambda);
            income.reqAmounts = UniformSequence.createByMeanAndDeviation(srv, srv * 0.5);
            Waiting queue = new Waiting();
            queue.capacity = waitingRoomSize;
            res = taskNunits(n, nu, income, queue);
            srv -= 0.001;
        } while (res.unitsFreeConditionProbability[nu] < 0.3);
        System.out.println("------------------------------------"
                + "\r\n число обращений: " + n
                + ";\r\n количество приборов: " + nu
                + ";\r\n среднее число обращений в единицу времени: " + lambda //интенсивность потока
                + ";\r\n среднее время обслуживания: " + srv
                + ";\r\n число мест для ожидания: " + waitingRoomSize
                + ";\r\n время нахождения в системе --- среднее:  " + res.avarageInProcessTime
                + ", наибольшее: " + res.maxInProcessTime
                + ";\r\n время ожидания --- среднее: " + res.avarageWaitingTime
                + ", наибольшее: " + res.maxWaitingTime
                + ";\r\n длина очереди ---  средняя: " + res.avarageQueueLength
                + ", наибольшая: " + res.maxQueueLength
                + ", конечная: " + res.finalQueueLength
                + ";\r\n вероятность отказа в обслуживании: " + res.rejectProbability
                + ";\r\n среднее число свободных приборов: " + res.avarageNumberOfFreeUnits
                + ";\r\n относительный простой всех приборов: " + res.unitsSumFreePartTime
                + ";\r\n средний относительный простой одного прибора: " + (res.unitsSumFreePartTime / nu)
                + ";\r\n вероятность числа свободных приборов и относительный простой каждого прибора"
        );
        for (int i = 0; i < res.unitsFreePartTime.length; i++) {
            String sout = "{" + i + "}";
            while (sout.length() < 8) {
                sout += " ";
            }
            sout += res.unitsFreeConditionProbability[i] + "          ";
            while (sout.length() < 40) {
                sout += " ";
            }
            sout += "[" + i + "]";
            while (sout.length() < 48) {
                sout += " ";
            }
            sout += res.unitsFreePartTime[i];
            System.out.println(sout);
        }
        String sout = "{" + res.unitsFreePartTime.length + "}";
        while (sout.length() < 8) {
            sout += " ";
        }
        sout += res.unitsFreeConditionProbability[res.unitsFreePartTime.length];
        System.out.println(sout);

    }

    public static void main(String[] args) {
        //test_sortFreeUnits();
        test_TaskNUnits();
        // ------------------------------------
        //        run:
        //------------------------------------
        // число обращений: 10000000;
        // количество приборов: 3;
        // среднее число обращений в единицу времени: 1.1;
        // среднее время обслуживания: 0.834;
        // число мест для ожидания: 10;
        // время нахождения в системе --- среднее:  0.8387491165652048, наибольшее: 3.1519530755849314;
        // время ожидания --- среднее: 0.0038216993768972955, наибольшее: 1.9408180303871632;
        // длина очереди ---  средняя: 0.004203123861040601, наибольшая: 5.0, конечная: 0.0;
        // вероятность отказа в обслуживании: 0.0;
        // среднее число свободных приборов: 2.081752402896035;
        // относительный простой всех приборов: 2.0817524028959915;
        // средний относительный простой одного прибора: 0.6939174676319971;
        // вероятность числа свободных приборов и относительный простой каждого прибора
        //{0}     0.030725632610354502            [0]     0.6288618967202468
        //{1}     0.15679782865896477             [1]     0.6751845826400712
        //{2}     0.5124750419548726              [2]     0.7777059235356734
        //{3}     0.30000149677577503
        //BUILD SUCCESSFUL (total time: 6 seconds)

    }

    public static class QueueStatistics {

        public double rejectProbability;
        public double avarageWaitingTime;
        public double avarageInProcessTime;
        public double[] unitsFreePartTime;
        public double[] unitsFreeConditionProbability;
        public double unitsSumFreePartTime;
        public double avarageNumberOfFreeUnits;
        public double avarageQueueLength;
        public double maxQueueLength;
        public double maxWaitingTime;
        public double maxInProcessTime;
        public double finalQueueLength;
    }
    //==============================================================================

    public static void test_sortFreeUnits() {
        double[] src = {5, -5, -2, -3, -4, 0, 0, 0, 3, 2, 1};
        int res[] = sortFreeUnits(src);
        for (int i = 0; i < res.length; i++) {
            System.out.print(src[res[i]] + ", ");
        }
        System.out.println("");
    }
}
