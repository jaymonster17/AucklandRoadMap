package main.java;

import java.util.ArrayList;

class Trie<T> {
    private TrieNode<T> root;

    public Trie() {
        this.root = new TrieNode<T>();
    }

    public void put(String key, T element) {
        TrieNode<T> node = root;

        for(Character character : key.toCharArray()) {
            if(!node.hasChild(character)) {
                node.addChild(character);
            }

            node = node.getChild(character);
        }

        node.setMarkedValue(element);
    }

    public boolean contains(String word) {
        TrieNode<T> node = root;

        for(Character character : word.toCharArray()) {
            if(!node.hasChild(character)) {
                return false;
            }

            node = node.getChild(character);
        }

        return node.isMarked();
    }

    public ArrayList<T> getAll(String prefix) {
        ArrayList<T> results = new ArrayList<T>();
        TrieNode node = root;

        for(Character character : prefix.toCharArray()) {
            if(!node.hasChild(character)) {
                return results;
            }

            node = node.getChild(character);
        }

        getAllValuesFromNode(node, results);

        return results;
    }

    private void getAllValuesFromNode(TrieNode node, ArrayList values) {
        if (node.isMarked()) {
            values.add(node.getMarkedValue());
        }

        for(Object child : node.getChildren().values()) {
            getAllValuesFromNode((TrieNode) child, values);
        }
    }
}