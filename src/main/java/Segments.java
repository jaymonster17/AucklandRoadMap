package main.java;

import java.util.ArrayList;

/**
 * Created by wintonnmj on 10/03/17.
 */

public class Segments {
    int roadID;
    double length;
    double nodeID1;
    double nodeID2;
    ArrayList<double> coords = new ArrayList<double>();

    public segments(int ID, double len, double nID1, double nID2, ArrayList<double> coordinates){
        this.roadID = ID;
        this.length = len;
        this.nodeID1 = nID1;
        this.nodeID2 = nID2;
        this.coords = coordinates;
    }

    public int getRoadID() {
        return roadID;
    }

    public double getLength() {
        return length;
    }

    public double getNodeID1() {
        return nodeID1;
    }

    public ArrayList<double> getCoords() {
        return coords;
    }
}
