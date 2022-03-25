package bayesiannetwork;

import java.util.Collection;

/**
 * This class is used to track metrics of the program.
 */
public class Tracker {
    // run time
    private long runTime = 0;
    private long startTime;
    private int maxFactorSize = 0;

    /**
     * First method to call during tracking.
     * */
    public void startTracker() {
        this.startTime = System.nanoTime();
    }

    /**
     * Last method to call during tracking.
     * */
    public void stopTracker() {
        this.runTime = System.nanoTime() - this.startTime;
    }

    public long getRunTime() {
        return this.runTime;
    }

    public int getMaxFactorSize() {
        return maxFactorSize;
    }

    /**
     * Get the maximum number of rows of the factors.
     *
     * @param factors the factors to track
     */
    public void trackMaxFactorSize(Collection<Factor> factors) {
        for (Factor factor : factors) {
            int size = factor.getColumns().size();
            if (size > maxFactorSize) {
                maxFactorSize = size;
            }
        }
    }
}
