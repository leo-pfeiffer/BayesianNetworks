package bayesiannetwork;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class FactorRowKeyTest {

    @Test
    public void testCreate() {
        FactorRowKey k = new FactorRowKey(new String[] {"A", "B"}, new int[] {1, 2});
        assertNotNull(k);
    }

    @Test
    public void testCreateFromList() {
        ArrayList<String> labels = new ArrayList<>(List.of(new String[]{"A", "B"}));
        ArrayList<Integer> values = new ArrayList<>(List.of(new Integer[] {1, 2}));
        FactorRowKey k = new FactorRowKey(labels, values);
        assertNotNull(k);
    }

    @Test
    public void testCreateFail() {
        assertThrows(IllegalArgumentException.class, () -> new FactorRowKey(new String[] {"A", "B"}, new int[] {1, 2, 3}));
    }

    @Test
    public void testCreateFromListFail() {
        ArrayList<String> labels = new ArrayList<>(List.of(new String[]{"A", "B"}));
        ArrayList<Integer> values = new ArrayList<>(List.of(new Integer[] {1, 2, 3}));
        assertThrows(IllegalArgumentException.class, () -> new FactorRowKey(labels, values));
    }

    @Test
    public void testToString() {
        FactorRowKey key = new FactorRowKey(new String[] {"A", "B"}, new int[] {1, 2});
        assertEquals("{A=1,B=2}", key.toString());
    }
}
