package main.java;


import java.util.HashSet;

/**
 * Created by wintonnmj on 9/03/17.
 */
public class Node {

    int ID;
    double latitude;
    double longitude;
    Location location;
    HashSet<Segments> segments = new HashSet<>();

    public Node(int id, double lat, double lon, Location loc){
        this.ID = id;
        this.latitude = lat;
        this.longitude = lon;
        this.location = loc;
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

    public Location getLocation() {
        return location;
    }

    public HashSet<Segments> getSegments() {
        return segments;
    }
}

