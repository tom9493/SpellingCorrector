package spell;

import java.io.IOException;
import java.util.*;
import java.io.File;

public class SpellCorrector implements ISpellCorrector {

    private Trie dictionary = new Trie();
    private List<String> foundWords = new ArrayList<>();
    private List<String> secondEditWords = new ArrayList<>();
    private int highestCount = 0;
    private int firstEditCheck = 0;

    public void reset() {
        dictionary = new Trie();
        foundWords = new ArrayList<>();
        secondEditWords = new ArrayList<>();
        highestCount = 0;
        firstEditCheck = 0;
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        reset();
        File dictionaryFile = new File(dictionaryFileName);
        Scanner scan = new Scanner(dictionaryFile);

        while(scan.hasNext()) { dictionary.add(scan.next().toLowerCase()); }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        highestCount = 0;
        firstEditCheck = 0;
        String word = inputWord.toLowerCase();
        if (dictionary.find(word) != null) { return word; }

        Edits(word);

        if (highestCount == 0) {
            firstEditCheck = 1;
            for (int i = 0; i < secondEditWords.size(); ++i) {
                Edits(secondEditWords.get(i));
            }
            if (highestCount == 0) { return null; }
        }
        Collections.sort(foundWords);
        return foundWords.get(0);
    }

    public void Edits(String word) {
        deletionEdit(word);
        transpositionEdit(word);
        alterationEdit(word);
        insertionEdit(word);
    }

    public void deletionEdit(String word) {
        String newWord;
        for (int i = 0; i < word.length(); ++i) {
            StringBuilder sb = new StringBuilder();
            sb.append(word);
            newWord = sb.deleteCharAt(i).toString();
            if (firstEditCheck == 0) { secondEditWords.add(newWord); }
            if (dictionary.find(newWord) != null) { checkCount(dictionary.find(newWord), newWord); }
        }
    }

    public void transpositionEdit(String word) {
        String newWord;
        for (int i = 0; i < word.length() - 1; ++i) {
            StringBuilder sb = new StringBuilder();
            sb.append(word);
            char a = sb.charAt(i);
            char b = sb.charAt(i+1);
            sb.setCharAt(i, b);
            sb.setCharAt(i+1, a);
            newWord = sb.toString();
            if (firstEditCheck == 0) { secondEditWords.add(newWord); }
            if (dictionary.find(newWord) != null) { checkCount(dictionary.find(newWord), newWord); }
        }
    }

    public void alterationEdit(String word) {
        String newWord;
        for (int i = 0; i < word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                StringBuilder sb = new StringBuilder();
                sb.append(word);
                sb.setCharAt(i, c);
                newWord = sb.toString();
                if (firstEditCheck == 0) {
                    secondEditWords.add(newWord);
                }
                if (dictionary.find(newWord) != null) {
                    checkCount(dictionary.find(newWord), newWord);
                }
            }
        }
    }

    public void insertionEdit(String word) {
        String newWord;
        for (int i = 0; i <= word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                StringBuilder sb = new StringBuilder();
                sb.append(word);
                sb.insert(i, c);
                newWord = sb.toString();
                if (firstEditCheck == 0) {
                    secondEditWords.add(newWord);
                }
                if (dictionary.find(newWord) != null) {
                    checkCount(dictionary.find(newWord), newWord);
                }
            }
        }
    }

    public void checkCount(INode n, String newWord) {
        if (n.getValue() > highestCount) {
            highestCount = n.getValue();
            foundWords.clear();
            foundWords.add(newWord);
        }
        else if (n.getValue() == highestCount) { foundWords.add(newWord); }
    }
}
