package bayesiannetwork;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeTest {

    @Test
    public void testHasAncestorIsParent() {
        Node node = new Node("A");
        Node ancestor = new Node("B");
        node.addParent(ancestor);
        assertTrue(node.hasAncestor(ancestor));
    }

    @Test
    public void testHasAncestorIsParentOfParent() {
        Node node = new Node("A");
        Node node2 = new Node("B");
        Node ancestor = new Node("C");
        node.addParent(node2);
        node2.addParent(ancestor);
        assertTrue(node.hasAncestor(ancestor));
    }

    @Test
    public void testHasAncestorIsParentOfMultipleParents() {
        Node node1 = new Node("A");
        Node node2 = new Node("C");
        Node node3 = new Node("D");
        Node node4 = new Node("E");
        Node node5 = new Node("F");
        Node ancestor = new Node("Z");

        node1.addParent(node2);
        node1.addParent(node3);
        node2.addParent(node4);
        node3.addParent(node5);
        node5.addParent(ancestor);

        assertTrue(node1.hasAncestor(ancestor));
    }

    @Test
    public void testHasAncestorCircular() {
        Node node1 = new Node("A");
        Node node2 = new Node("C");
        Node ancestor = new Node("Z");

        node1.addParent(node2);
        node2.addParent(ancestor);
        ancestor.addParent(node1);

        assertTrue(node1.hasAncestor(ancestor));
    }

    @Test
    public void testHasAncestorFalse() {
        Node node = new Node("A");
        Node node2 = new Node("B");
        Node ancestor = new Node("C");
        Node notAncestor = new Node("D");
        node.addParent(node2);
        node2.addParent(ancestor);
        assertFalse(node.hasAncestor(notAncestor));
    }
}
