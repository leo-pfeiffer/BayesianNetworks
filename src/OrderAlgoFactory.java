/**
 * Instantiate the correct order algorithm.
 * */
public abstract class OrderAlgoFactory {

    public static OrderAlgo create(String algo) {
        switch (algo) {
            case "MC":
                return new MaxCardinality();
            case "GME":
                return new GreedyMinEdges();
            default:
                throw new IllegalArgumentException("Invalid algorithm: " + algo);
        }
    }

}
