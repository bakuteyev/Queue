package queuesimulation;

public class SequenceProduct extends RandomSequence {

    private RandomSequence firstSeq;
    private RandomSequence secondSeq;

    private SequenceProduct(RandomSequence firstSeq, RandomSequence secondSeq) {
        this.firstSeq = firstSeq;
        this.secondSeq = secondSeq;
    }

    @Override
    public double getNext() {
        return firstSeq.getNext() * secondSeq.getNext();
    }

}
