package spell;

public class Node implements INode {

    private int count = 0;
    private final Node[] nodeArray = new Node[26];

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        ++count;
    }

    @Override
    public INode[] getChildren() {
        return nodeArray;
    }
}
