import java.util.Collection;
import bayesiannetwork.Factor;

public class Tracker {
    // run time
    private long runTime = 0;
    private long startTime;
    private int maxFactorSize = 0;
    private long startMemoryUsage;
    private long memoryUsage = 0;

    /**
     * First method to call during tracking.
     * */
    public void startTracker() {
        this.startTime = System.currentTimeMillis();
        this.startMemoryUsage = getCurrentMemoryUsage();
    }

    /**
     * Last method to call during tracking.
     * */
    public void stopTracker() {
        this.runTime = System.currentTimeMillis() - this.startTime;
        this.memoryUsage = getCurrentMemoryUsage() - this.startMemoryUsage;
    }

    public long getRunTime() {
        return this.runTime;
    }

    public long getMemoryUsage() {
        return this.memoryUsage;
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

    /**
     * Get the current memory usage.
     * This is a rough estimate of the current memory usage since the garbage collector isn't necessarily
     * called immediately after the method returns.
     * */
    private long getCurrentMemoryUsage() {
        long TO_MB = 1024 * 1024;
        System.gc();
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / TO_MB;
    }
}
