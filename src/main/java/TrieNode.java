package main.java;

import java.util.HashMap;

public class TrieNode<T> {
    private T markedValue;
    private HashMap<Character, TrieNode> children;

    public TrieNode() {
        this.markedValue = null;
        this.children = new HashMap<Character, TrieNode>();
    }

    public void addChild(Character child) {
        this.children.put(child, new TrieNode());
    }

    public TrieNode<T> getChild(Character child) {
        return this.children.get(child);
    }

    public boolean hasChild(Character child) {
        return this.children.containsKey(child);
    }

    public void setMarkedValue(T markedValue) {
        this.markedValue = markedValue;
    }

    public T getMarkedValue() {
        return this.markedValue;
    }

    public boolean isMarked() {
        return this.markedValue != null;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return this.children;
    }
}
