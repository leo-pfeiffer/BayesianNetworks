import bayesiannetwork.FactorTest;
import bayesiannetwork.NodeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *  Test suite for all JUnit tests.
 **/
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FactorTest.class,
        NodeTest.class,
        StacsChecksTest.class,
        VariableEliminationTest.class
})
public class TestSuite { }