package main.java;

import java.util.ArrayList;

public class Segment {

    private double length;
    private Node node1;
    private Node node2;
    private ArrayList<Location> coords = new ArrayList<>();
    private Road road;

    public Segment(double len, Node n1, Node n2, ArrayList<Location> coordinates, Road r){
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

    public ArrayList<Location> getCoords() { return coords; }

    public String getRoadLabel() { return road.getLabel(); }
}
