package main.java;

import java.util.Set;
import java.util.HashSet;

/**
 * Created by wintonnmj on 9/03/17.
 */
public class Node {

    private int ID;
    private Location location;
    private Set<Segment> segments = new HashSet<>();

    public Node(int id, Location loc){
        this.ID = id;
        this.location = loc;
    }

    public int getID() {
        return this.ID;
    }

    public Location getLocation() {
        return location;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public void addSegment(Segment s) {
        this.segments.add(s);
    }
}

