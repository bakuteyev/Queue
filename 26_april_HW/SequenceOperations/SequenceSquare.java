package queuesimulation;

/**
 * This class can be realized with SequenceProduct class.
 */
public class SequenceSquare extends RandomSequence {

    private RandomSequence sequence;

    private SequenceSquare(RandomSequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public double getNext() {
    	double nextVal = sequence.getNext();
        return nextVal * nextVal;
    }

}
