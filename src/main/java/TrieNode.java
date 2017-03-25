package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

/**
 * Created by
 * wintonnmj on 22/03/17.
 */

public class TrieNode {


    String isWord = null;
    Road road = null;
    Map <Character,TrieNode> children = new HashMap<>();

    public TrieNode(String w, Map<Character, TrieNode> kids){
        this.children = kids;
        this.isWord = w;

    }

    public String isWord(){
        return isWord;
    }

    public void setWord(String value){
        this.isWord = value;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

}
