package main.java;

import java.util.ArrayList;

/**
 * Created by wintonnmj on 10/03/17.
 */

public class Segments {

    double length;
    Node node1;
    Node node2;
    ArrayList<Location> coords = new ArrayList<>();
    Road road;

    public Segments(double len, Node n1, Node n2, ArrayList<Location> coordinates, Road r){
        this.length = len;
        this.node1 = n1;
        this.node2 = n2;
        this.coords = coordinates;
        this.road = r;
    }

    public double getLength() {
        return length;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public ArrayList<Location> getCoords() {
        return coords;
    }

    public Road getRoad() {
        return road;
    }
}
