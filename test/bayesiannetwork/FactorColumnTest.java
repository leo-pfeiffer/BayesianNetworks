package bayesiannetwork;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class FactorColumnTest {

    @Test
    public void testCreate() {
        FactorColumn fc = new FactorColumn(new Node("A"));
        assert fc.getNode().getLabel().equals("A");
    }

    @Test
    public void testSetTruthValues() {
        FactorColumn fc = new FactorColumn(new Node("A"));
        fc.setTruthValues(new int[] {1, 2});
        assertNotNull(fc.getTruthValues());
        assertEquals(fc.getTruthValues().toArray()[0], 1);
        assertEquals(fc.getTruthValues().toArray()[1], 2);
    }

    @Test
    public void testCopy() {
        FactorColumn fc = new FactorColumn(new Node("A"));
        fc.setTruthValues(new int[] {1, 2});
        FactorColumn fc2 = fc.copy();
        assertNotNull(fc2.getTruthValues());
        assertEquals(fc2.getTruthValues().toArray()[0], 1);
        assertEquals(fc2.getTruthValues().toArray()[1], 2);
        assertEquals(fc2.getNode().getLabel(), fc.getNode().getLabel());
    }

    @Test
    public void testDuplicate() {
        FactorColumn fc = new FactorColumn(new Node("A"));
        fc.setTruthValues(new int[] {1, 2});
        fc.duplicate(1);
        assertNotNull(fc.getTruthValues());
        assertEquals(fc.getTruthValues().toArray()[0], 1);
        assertEquals(fc.getTruthValues().toArray()[1], 2);
        assertEquals(fc.getTruthValues().toArray()[2], 1);
        assertEquals(fc.getTruthValues().toArray()[3], 2);
    }
}
