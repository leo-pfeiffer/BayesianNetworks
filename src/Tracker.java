import java.util.Collection;
import bayesiannetwork.Factor;

public class Tracker {
    // run time
    private long runTime = 0;
    private long startTime;
    private int maxFactorSize = 0;

    /**
     * First method to call during tracking.
     * */
    public void startTracker() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Last method to call during tracking.
     * */
    public void stopTracker() {
        this.runTime = System.currentTimeMillis() - this.startTime;
    }

    public long getRunTime() {
        return this.runTime;
    }

    public int getMaxFactorSize() {
        return maxFactorSize;
    }

    public void trackMaxFactorSize(Collection<Factor> factors) {
        for (Factor factor : factors) {
            int size = factor.getNumRows() * factor.getColumns().size();
            if (size > maxFactorSize) {
                maxFactorSize = size;
            }
        }
    }
}
