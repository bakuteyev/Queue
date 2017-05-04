package queuesimulation;

/**
 * Модель входящего потока.
 *
 * @author Alexander Mikhailovich Kovshov
 *  02.04.2013 23:13:33
 */
public class Incoming {

    /**
     * Последовательность временных промежутков между заявками.
     */
    public RandomSequence timeIntervals;
    /**
     * Размер заявки, например, время, необходимое для её обработки.
     */
    public RandomSequence reqAmounts;

    /**
     * Вероятность того, что заявка будет ждать обслуживания при наличии очереди.
     */
    public double agreeToWaitProbability = 1;

    /**
     * Возвращает очередную заявку
     * @return заявка
     */
    public CallReq nextCall(){
        CallReq req = new CallReq();
        req.time = timeIntervals.getNext();
        req.amount = reqAmounts.getNext();
        req.type = Math.random() > agreeToWaitProbability ? 0 : 1;
        return req;
    };

}
