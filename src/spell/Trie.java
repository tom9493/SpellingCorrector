package spell;

public class Trie implements ITrie {

    private final Node root = new Node();
    private int nodeCount = 1;
    private int wordCount = 0;
    private int totalWordCount = 0;

    @Override
    public void add(String word) {
        String lWord = word.toLowerCase();
        char[] lWordChars = lWord.toCharArray();
        INode currentNode = root;

        for (int i = 0; i < lWordChars.length; ++i) {
            int index = (int) lWordChars[i] - 'a';
            if (currentNode.getChildren()[index] == null) {
                currentNode.getChildren()[index] = new Node();
                ++nodeCount;
            }
            currentNode = currentNode.getChildren()[index];
            if (i == lWordChars.length - 1) {
                if (currentNode.getValue() == 0) {
                    ++wordCount;
                }
                currentNode.incrementValue();
            }
        }
        ++totalWordCount;
    }

    @Override
    public INode find(String word) {
        String lWord = word.toLowerCase();
        char[] lWordChars = lWord.toCharArray();
        INode currentNode = root;

        for (int i = 0; i < lWordChars.length; ++i) {
            int index = (int) lWordChars[i] - 'a';
            if (currentNode.getChildren()[index] == null) { return null; }
            currentNode = currentNode.getChildren()[index];
            if (i == lWordChars.length - 1 & currentNode.getValue() != 0) { return currentNode; }
        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    public int getTotalWordCount() { return totalWordCount; }

    @Override
    public String toString() {
        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toString_Helper(root, curWord, output);

        return output.toString();
    }

    public void toString_Helper(INode n, StringBuilder curWord, StringBuilder output) {
        if (n.getValue() > 0) {
            output.append(curWord);
            output.append("\n");
        }

        for (int i = 0; i < n.getChildren().length; ++i) {
            INode child = n.getChildren()[i];
            if (child != null) {
                char childLetter = (char) (i + 'a');
                curWord.append(childLetter);
                toString_Helper(child, curWord, output);    // RECURSIVE STATEMENT
                curWord.deleteCharAt(curWord.length() - 1);
            }
        }
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) { return true; }
        if (o == null) { return false; }
        if (o.getClass() != this.getClass()) { return false; }

        Trie other = (Trie)o;

        if (other.getNodeCount() != this.getNodeCount()) { return false; }
        if (other.getWordCount() != this.getWordCount()) { return false; }
        if (other.getTotalWordCount() != this.getTotalWordCount()) { return false; }

        return equals_Helper(root, other.root);
    }

    public boolean equals_Helper(INode n1, INode n2) {
        if (n1.getValue() != n2.getValue()) { return false; }
        for (int i = 0; i < n1.getChildren().length; ++i) {
            if (n1.getChildren()[i] != null & n2.getChildren()[i] != null) {
                return equals_Helper(n1.getChildren()[i], n2.getChildren()[i]); // RECURSIVE STATEMENT
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash;
        hash = this.toString().toCharArray()[0];
        return (nodeCount * wordCount * hash);
    }
}
