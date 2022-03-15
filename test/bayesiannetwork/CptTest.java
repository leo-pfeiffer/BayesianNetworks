package bayesiannetwork;

import org.junit.Test;
import static org.junit.Assert.*;

public class CptTest {

    @Test
    public void testCreateWithNode() {
        Node node = new Node("A");
        CPT table = node.getTable();
        assertSame(table.getNode(), node);
        assertEquals(table.getColumns().size(), 1);
        assertArrayEquals(table.getColumns().get(0).getTruthValues().toArray(), new Integer[]{0, 1});
    }

    @Test
    public void testAddColumn() {
        Node node = new Node("A");
        CPT table = node.getTable();
        Node node2 = new Node("B");
        table.addColumn(node2);
        assertEquals(table.getColumns().size(), 2);
        assertEquals(table.getColumns().get(0).getNode(), node);
        assertEquals(table.getColumns().get(1).getNode(), node2);
        assertArrayEquals(table.getColumns().get(0).getTruthValues().toArray(), new Integer[]{0, 1, 0, 1});
        assertArrayEquals(table.getColumns().get(1).getTruthValues().toArray(), new Integer[]{0, 0, 1, 1});
    }

    @Test
    public void testSetProbabilitiesSingle() {
        Node node = new Node("A");
        CPT table = node.getTable();
        table.setProbabilities(0.1, 0.9);
        assertArrayEquals(table.getProbabilities().toArray(), new Double[]{0.1, 0.9});
    }

    @Test
    public void testSetProbabilitiesDouble() {
        Node node = new Node("A");
        CPT table = node.getTable();
        table.addColumn(new Node("B"));
        table.setProbabilities(0.1, 0.9, 0.2, 0.8);
        assertArrayEquals(table.getProbabilities().toArray(), new Double[]{0.1, 0.9, 0.2, 0.8});
    }

    @Test
    public void testSetWrongNumberOfProbabilities() {
        Node node = new Node("A");
        CPT table = node.getTable();
        table.addColumn(new Node("B"));
        assertThrows(IllegalArgumentException.class, () -> table.setProbabilities(0.1, 0.9, 0.2));
        assertThrows(IllegalArgumentException.class, () -> table.setProbabilities(0.1, 0.9, 0.2, 0.2, 0.3));
    }
}
