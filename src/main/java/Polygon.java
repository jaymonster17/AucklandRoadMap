package main.java;

import java.util.List;
import java.util.ArrayList;


public class Polygon {
    private List<Location> coodinates = new ArrayList<>();;

    public Polygon(){}


    public void addPoint(Location p){
        coodinates.add(p);
    }

    public int[] getX() {
        int xCoordinates[] = new int[coodinates.size()];

        for(int i = 0; i < coodinates.size(); i++){
            int intX = (int)coodinates.get(i).x;
            xCoordinates[i] = intX;
        }
        return xCoordinates;
    }

    public int[] getY() {
        int yCoordinates[] = new int[coodinates.size()];

        for(int i = 0; i < coodinates.size(); i++){
            int intY = (int)coodinates.get(i).y;
            yCoordinates[i] = intY;
        }
        return yCoordinates;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < coodinates.size(); i++) {
            s += "("+ coodinates.get(i).x +"," + coodinates.get(i).y + ")" + " ";
        }
        return s;
    }
}

