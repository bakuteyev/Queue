package queuesimulation;

/**
 * Обращение-заявка на обслуживание.
 * Предполагается доработка класса.
 *
 * Created on 04.04.2013 21:50:32
 * @author Alexander Mikhailovich Kovshov
 */
public class CallReq {

    public double time;
    public int type;
    public double amount;
    public double startWaitTime;
    public double finishWaitTime;

    public double getWaitingTime(){
        return finishWaitTime - startWaitTime;
    }
}

