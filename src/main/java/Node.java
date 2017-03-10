package main.java;


/**
 * Created by wintonnmj on 9/03/17.
 */
public class Node {

    int ID;
    double latitude;
    double longitude;

    public Node(int id, int lat, int lon){
        this.ID = id;
        this.latitude = lat;
        this.longitude = lon;
    }

    public int getID() {
        return this.ID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

