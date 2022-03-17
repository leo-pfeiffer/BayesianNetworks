package bayesiannetwork;

import org.junit.Test;
import static org.junit.Assert.*;

public class FactorTest {

    @Test
    public void testCreateWithNode() {
        Node node = new Node("A");
        Factor table = node.getTable();
        assertSame(table.getNode(), node);
        assertEquals(table.getColumns().size(), 1);
        assertArrayEquals(table.getColumns().get(0).getTruthValues().toArray(), new Integer[]{1, 0});
    }

    @Test
    public void testAddColumn() {
        Node node = new Node("A");
        Factor table = node.getTable();
        Node node2 = new Node("B");
        table.addColumn(node2);
        assertEquals(table.getColumns().size(), 2);
        assertEquals(table.getColumns().get(0).getNode(), node);
        assertEquals(table.getColumns().get(1).getNode(), node2);
        assertArrayEquals(table.getColumns().get(0).getTruthValues().toArray(), new Integer[]{1, 0, 1, 0});
        assertArrayEquals(table.getColumns().get(1).getTruthValues().toArray(), new Integer[]{1, 1, 0, 0});
    }

    @Test
    public void testSetProbabilitiesSingle() {
        Node node = new Node("A");
        Factor table = node.getTable();
        table.setProbabilities(0.1, 0.9);
        assertArrayEquals(table.getProbabilities().toArray(), new Double[]{0.1, 0.9});
    }

    @Test
    public void testSetProbabilitiesDouble() {
        Node node = new Node("A");
        Factor table = node.getTable();
        table.addColumn(new Node("B"));
        table.setProbabilities(0.1, 0.9, 0.2, 0.8);
        assertArrayEquals(table.getProbabilities().toArray(), new Double[]{0.1, 0.9, 0.2, 0.8});
    }

    @Test
    public void testSetWrongNumberOfProbabilities() {
        Node node = new Node("A");
        Factor table = node.getTable();
        table.addColumn(new Node("B"));
        assertThrows(IllegalArgumentException.class, () -> table.setProbabilities(0.1, 0.9, 0.2));
        assertThrows(IllegalArgumentException.class, () -> table.setProbabilities(0.1, 0.9, 0.2, 0.2, 0.3));
    }

    @Test
    public void testCreateUnconditionalTable() {
        Factor table = new Factor();
        Node node1 = new Node("A");
        Node node2 = new Node("B");
        Node node3 = new Node("C");
        table.addColumn(node1);
        table.addColumn(node2);
        table.addColumn(node3);
        table.setProbabilities(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8);
        assertEquals(table.getColumns().size(), 3);
        assertEquals(table.getColumns().get(0).getNode(), node1);
        assertEquals(table.getColumns().get(1).getNode(), node2);
        assertEquals(table.getColumns().get(2).getNode(), node3);
    }

    @Test
    public void testRowIndex() {
        Node node1 = new Node("A");
        Factor table = node1.getTable();
        Node node2 = new Node("B");
        Node node3 = new Node("C");
        table.addColumn(node2);
        table.addColumn(node3);
        table.setProbabilities(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8);

        String[] cols = new String[]{"A", "B", "C"};
        FactorRowKey key1 = new FactorRowKey(cols, new int[]{1, 1, 1});
        FactorRowKey key2 = new FactorRowKey(cols, new int[]{0, 1, 1});
        FactorRowKey key3 = new FactorRowKey(cols, new int[]{1, 0, 1});
        FactorRowKey key4 = new FactorRowKey(cols, new int[]{0, 0, 1});
        FactorRowKey key5 = new FactorRowKey(cols, new int[]{1, 1, 0});
        FactorRowKey key6 = new FactorRowKey(cols, new int[]{0, 1, 0});
        FactorRowKey key7 = new FactorRowKey(cols, new int[]{1, 0, 0});
        FactorRowKey key8 = new FactorRowKey(cols, new int[]{0, 0, 0});

        assertEquals(0.1, table.getProbabilitiesByRowKey(key1), 0.0);
        assertEquals(0.2, table.getProbabilitiesByRowKey(key2), 0.0);
        assertEquals(0.3, table.getProbabilitiesByRowKey(key3), 0.0);
        assertEquals(0.4, table.getProbabilitiesByRowKey(key4), 0.0);
        assertEquals(0.5, table.getProbabilitiesByRowKey(key5), 0.0);
        assertEquals(0.6, table.getProbabilitiesByRowKey(key6), 0.0);
        assertEquals(0.7, table.getProbabilitiesByRowKey(key7), 0.0);
        assertEquals(0.8, table.getProbabilitiesByRowKey(key8), 0.0);
    }
}
