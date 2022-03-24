import bayesiannetwork.BayesianNetworkTest;
import bayesiannetwork.FactorColumn;
import bayesiannetwork.FactorColumnTest;
import bayesiannetwork.FactorRowKey;
import bayesiannetwork.FactorRowKeyTest;
import bayesiannetwork.FactorTest;
import bayesiannetwork.InducedGraphTest;
import bayesiannetwork.NodeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *  Test suite for all JUnit tests.
 **/
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AgentTest.class,
        AgentWithEvidenceTest.class,
        BayesianNetworkTest.class,
        FactorColumnTest.class,
        FactorTest.class,
        FactorRowKeyTest.class,
        GreedyMinEdgesTest.class,
        InducedGraphTest.class,
        IntegrationTest.class,
        MaxCardinalityTest.class,
        NodeTest.class,
        OrderAlgoFactoryTest.class,
})
public class TestSuite { }