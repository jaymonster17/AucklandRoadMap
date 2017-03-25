package main.java;

import java.util.ArrayList;

/**
 * Created by wintonnmj on 22/03/17.
 */
public class Trie {

    TrieNode root = new TrieNode(null, null);

    public Trie(){

    }

    public void add(Road r){
        if(r == null){
            return;
        }

        TrieNode node = this.root; //making a cursor starting at root
        char[] name = r.getLabel().toCharArray(); //takes name, turns it into a char array

        for (int i = 0; i < name.length; i++) {
            if (node.children.containsKey(name[i])) { //if a child with the same key (char)
                node = node.children.get(name[i]); //cursor move down to that child
            } else { //no key exists yet
                TrieNode next = new TrieNode(null,null); //create a new TriNode to be passed in as a child of the current node
                node.children.put(name[i], next); //add a new child with the character and next node.
                node = next; //iterate the node down the trie
            }
        }
        node.setWord(r.getLabel()); //Put the road name in the node
        node.setRoad(r); //Puts the road in the node
    }


    public ArrayList<Road> withPrefix(String pfx){
        TrieNode node = root; //set cursor
        char[] prefix = pfx.toCharArray(); //char array of the prefix

        for (char c : prefix) { //for each char in prefix
            node = node.children.get(c); //cursor move down the trie to the child with the right key

            if (node == null) { //if the node is empty (no roads with the prefix)
                return new ArrayList<>(); //return an empty arraylist
            }
        }

        ArrayList<Road> names = new ArrayList<>(); //New arraylist that will hold all the names of the roads with the given prefix
        traverse(node, names); //traverse will pass in the node and list, filling in the list recursively with the children
        return names;
    }



    private static void traverse(TrieNode node, ArrayList<Road> roads){
        if(node.road != null) { //if the road connected to the node is not null
            roads.add(node.road); //add the current node's road to the roads list
        }

        for (Character c : node.children.keySet()){ //for each of the children in the current node
            traverse(node.children.get(c), roads); //call traverse function on the child, passing in the roads list
        }
    }

}
