import bayesiannetwork.CptTest;
import bayesiannetwork.NodeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *  Test suite for all JUnit tests.
 **/
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CptTest.class,
        NodeTest.class
})
public class TestSuite { }