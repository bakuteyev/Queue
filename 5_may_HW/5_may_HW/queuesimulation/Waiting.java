package queuesimulation;


import java.util.ArrayList;

/**
 * Модель очереди заявок, ожидающих обслуживания.
 *
 * @author Alexander Mikhailovich Kovshov
 *  10.04.2013 17:45:35
 */
public class Waiting {

    public static final int UNLIMITED = -1;

    /**
     * Maximal length of reqs queue
     */
    public int capacity = 0;

    /**
     * Maximal waiting time in queue
     */
    public int duration;

    /**
     * Queue
     */
    private ArrayList<CallReq> reqs = new ArrayList<CallReq>();

    private double lastLengthChangeTime = 0;
    private double filling = 0;
    private double avarageLength = 0;
    private double maxLength = 0;
    /**
     * Добавляет заявку в очередь
     * @param req call-req to be added to the end of queue
     * @param curTime current time
     * @return
     */
    public boolean add(CallReq req, double curTime){
        if((capacity == UNLIMITED || reqs.size() < capacity) && (req.type > 0 /*|| reqs.size() == 0*/)) {
            if(!reqs.add(req)) {
                throw new RuntimeException("CallReq instance can't be added to queue");
            }
            if(reqs.size() > maxLength) maxLength = reqs.size();
            req.startWaitTime = curTime;
            filling += (reqs.size() - 1) * (curTime - lastLengthChangeTime);
            avarageLength = filling / curTime;
            lastLengthChangeTime = curTime;
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    private ArrayList<CallReq> getReqs() {
        return reqs;
    }

    /**
     * Удаляет заявку из очереди
     * @param i number of call-req in queue
     * @param curTime current time
     */
    public void remove(int i, double curTime) {
        reqs.get(i).finishWaitTime = curTime;
        reqs.remove(i);
        filling += (reqs.size() + 1) * (curTime - lastLengthChangeTime);
        avarageLength = filling / curTime;
        lastLengthChangeTime = curTime;
    }

    /**
     * Возвращает длину очереди
     * @return queue length
     */
    public int size(){
        return reqs.size();
    }

    public CallReq get(int i) {
        return reqs.get(i);
    }

    public void clear(){
        lastLengthChangeTime = 0;
        filling = 0;
        avarageLength = 0;
        maxLength = 0;
        reqs.clear();
    }

    public double getAvarageLength() {
        return avarageLength;
    }

    public double getMaxLength() {
        return maxLength;
    }

}
 