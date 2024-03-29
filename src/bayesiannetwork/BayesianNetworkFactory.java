package bayesiannetwork;

public abstract class BayesianNetworkFactory {

    public static BayesianNetwork create(String name) {
        switch (name) {
            case "BNA":
                return BayesianNetworkFactory.createBNA();
            case "BNB":
                return BayesianNetworkFactory.createBNB();
            case "BNC":
                return BayesianNetworkFactory.createBNC();
            case "CNX":
                return BayesianNetworkFactory.createCNX();
            default:
                throw new IllegalArgumentException("Invalid network name");
        }
    }

    /**
     * Create the BNA Bayesian Network.
     * */
    @SuppressWarnings("DuplicatedCode")
    public static BayesianNetwork createBNA() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.setName("BNA");

        // nodes
        Node a = bn.addNode("A");
        Node b = bn.addNode("B");
        Node c = bn.addNode("C");
        Node d = bn.addNode("D");

        // edges
        bn.addEdge(a, b);
        bn.addEdge(b, c);
        bn.addEdge(c, d);

        // probabilities
        a.getTable().setProbabilities(0.05, 0.95);
        b.getTable().setProbabilities(0.05, 0.95, 0.8, 0.2);
        c.getTable().setProbabilities(0.1, 0.9, 0.3, 0.7);
        d.getTable().setProbabilities(0.4, 0.6, 0.6, 0.4);

        return bn;
    }

    /**
     * Create the BNB Bayesian Network.
     * */
    @SuppressWarnings("DuplicatedCode")
    public static BayesianNetwork createBNB() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.setName("BNB");

        // nodes
        Node j = bn.addNode("J");
        Node k = bn.addNode("K");
        Node l = bn.addNode("L");
        Node m = bn.addNode("M");
        Node n = bn.addNode("N");
        Node o = bn.addNode("O");

        // edges
        bn.addEdge(j, k);
        bn.addEdge(k, m);
        bn.addEdge(l, m);
        bn.addEdge(m, n);
        bn.addEdge(m, o);

        // probabilities
        j.getTable().setProbabilities(0.05, 0.95);
        k.getTable().setProbabilities(0.9, 0.1, 0.7, 0.3);
        l.getTable().setProbabilities(0.7, 0.3);
        m.getTable().setProbabilities(0.6, 0.4, 0.2, 0.8, 0.7, 0.3, 0.1, 0.9);
        n.getTable().setProbabilities(0.6, 0.4, 0.2, 0.8);
        o.getTable().setProbabilities(0.05, 0.95, 0.8, 0.2);

        return bn;
    }

    /**
     * Create the BNC Bayesian Network.
     * */
    @SuppressWarnings("DuplicatedCode")
    public static BayesianNetwork createBNC() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.setName("BNC");

        // nodes
        Node p = bn.addNode("P");
        Node q = bn.addNode("Q");
        Node r = bn.addNode("R");
        Node s = bn.addNode("S");
        Node v = bn.addNode("V");
        Node u = bn.addNode("U");
        Node z = bn.addNode("Z");

        // edges
        bn.addEdge(p, q);
        bn.addEdge(q, v);
        bn.addEdge(q, s);
        bn.addEdge(r, v);
        bn.addEdge(r, s);
        bn.addEdge(v, z);
        bn.addEdge(s, z);
        bn.addEdge(s, u);

        // probabilities
        p.getTable().setProbabilities(0.05, 0.95);
        q.getTable().setProbabilities(0.9, 0.1, 0.7, 0.3);
        r.getTable().setProbabilities(0.7, 0.3);
        s.getTable().setProbabilities(0.6, 0.4, 0.2, 0.8, 0.7, 0.3, 0.1, 0.9);
        v.getTable().setProbabilities(0.7, 0.3, 0.15, 0.85, 0.55, 0.45, 0.1, 0.9);
        u.getTable().setProbabilities(0.05, 0.95, 0.8, 0.2);
        z.getTable().setProbabilities(0.65, 0.35, 0.4, 0.6, 0.7, 0.3, 0.2, 0.8);

        return bn;
    }

    /**
     * Create the CNX Bayesian Network.
     * */
    @SuppressWarnings("DuplicatedCode")
    public static BayesianNetwork createCNX() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.setName("CNX");

        // nodes
        Node holiday = bn.addNode("Hol");
        Node ddos = bn.addNode("D_R");
        Node clientActivity = bn.addNode("C_A");
        Node logged = bn.addNode("Log");
        Node maintenance = bn.addNode("Mai");
        Node maintenanceInfo = bn.addNode("M_I");
        Node firewall = bn.addNode("Fir");
        Node website = bn.addNode("Web");
        Node blocked = bn.addNode("Blo");
        Node alert = bn.addNode("Ale");

        // edges
        bn.addEdge(holiday, ddos);
        bn.addEdge(clientActivity, logged);
        bn.addEdge(maintenance, firewall);
        bn.addEdge(maintenanceInfo, firewall);
        bn.addEdge(website, blocked);
        bn.addEdge(ddos, alert);
        bn.addEdge(logged, alert);
        bn.addEdge(firewall, alert);
        bn.addEdge(blocked, alert);

        // probabilities
        holiday.getTable().setProbabilities(0.125, 0.875);
        ddos.getTable().setProbabilities(0.15, 0.85, 0.05, 0.95);
        clientActivity.getTable().setProbabilities(0.05, 0.95);
        logged.getTable().setProbabilities(0.7, 0.3, 0.3, 0.7);
        maintenance.getTable().setProbabilities(0.1, 0.9);
        maintenanceInfo.getTable().setProbabilities(0.02, 0.98);
        firewall.getTable().setProbabilities(0.5, 0.5, 0.5, 0.5, 0.03, 0.97, 0.99, 0.01);
        website.getTable().setProbabilities(0.05, 0.95);
        blocked.getTable().setProbabilities(0.85, 0.15, 0.05, 0.95);
        alert.getTable().setProbabilities(
                0.95, 0.05, 0.7125, 0.2875, 0.7125, 0.2875,
                0.475, 0.525, 0.7125, 0.2875, 0.475, 0.525,
                0.475, 0.525, 0.2375, 0.7625, 0.7125, 0.2875,
                0.475, 0.525, 0.475, 0.525, 0.2375, 0.7625,
                0.475, 0.525, 0.2375, 0.7625, 0.2375, 0.7625, 0, 1
        );

        return bn;
    }

}
