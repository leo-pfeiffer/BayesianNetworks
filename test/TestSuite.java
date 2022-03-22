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
        FactorTest.class,
        InducedGraphTest.class,
        NodeTest.class,
        IntegrationTest.class,
        VariableEliminationTest.class,
        VariableEliminationWithEvidenceTest.class
})
public class TestSuite { }