import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OrderAlgoFactoryTest {

    @Test
    public void testGetGME() {
        assertTrue(OrderAlgoFactory.create("GME") instanceof GreedyMinEdges);
    }

    @Test
    public void testGetTestMC() {
        assertTrue(OrderAlgoFactory.create("MC") instanceof MaxCardinality);
    }

    @Test
    public void testGetNonExistent() {
        assertThrows(IllegalArgumentException.class, () -> OrderAlgoFactory.create("NONEXISTENT"));
    }
}
