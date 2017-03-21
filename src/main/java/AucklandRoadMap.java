package main.java;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.awt.geom.Arc2D;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.awt.Point;

/**
 * Created by wintonnmj on 8/03/17.
 */
public class AucklandRoadMap extends GUI {

    public Location origin;
    public double scale;

    public Map<Integer, Road> roads = new HashMap<Integer, Road>();
    public Map<Integer, Node> nodes = new HashMap<Integer, Node>();
    public List<Segments> segments = new ArrayList<>();
    public List<Segments> selectedSegment = new ArrayList<>();
    public Map<Integer,Node> selectedNode = new HashMap<Integer,Node>();

    public void scaling(){
        double top = Double.NEGATIVE_INFINITY;
        double right = Double.NEGATIVE_INFINITY;
        double bottom = Double.POSITIVE_INFINITY;
        double left = Double.POSITIVE_INFINITY;

        for(Node n: nodes.values()) {
            Location l = n.getLocation();
            if(l.x < left){
                left = l.x;
            }
            if(l.x > right){
                right = l.x;
            }
            if(l.y < bottom){
                bottom = l.y;
            }
            if(l.y > top){
                top = l.y;
            }
        }

        origin = new Location(left, top);
        scale = Math.min(getDrawingAreaDimension().getWidth()/(right-left), getDrawingAreaDimension().getHeight()/(top-bottom));
    }

    public void redraw(Graphics g){
        if(!nodes.isEmpty()){ //if the map of nodes is not empty
            for(Node n: nodes.values()){ //
                Point point = n.getLocation().asPoint(origin, scale);
                g.setColor(Color.RED);
                g.fillOval(point.x - 1, point.y -1 ,4,4);
            }
        }

        if(!segments.isEmpty()){
            for(Segments s: segments){
                for(int i = 0; i < s.getCoords().size()-1; i++){
                    Point point1 = s.getCoords().get(i).asPoint(origin, scale);
                    Point point2 = s.getCoords().get(i+1).asPoint(origin, scale);

                    g.setColor(Color.BLACK);
                    g.drawLine(point1.x, point1.y, point2.x, point2.y);
                }
            }
        }

        if(!selectedSegment.isEmpty()){
            for (Segments s: selectedSegment){
                Point pt1 = s.getNode1().getLocation().asPoint(origin, scale);
                Point pt2 = s.getNode2().getLocation().asPoint(origin, scale);
                g.setColor(Color.YELLOW);
                g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
            }
        }
        if(!selectedNode.isEmpty()) {
            for (Node n: selectedNode.values()){
                Point pt = n.getLocation().asPoint(origin, scale);
                g.setColor(Color.BLUE);
                g.fillOval(pt.x - 1, pt.y -1 ,4,4);
            }
        }
    }

    protected void onClick(MouseEvent e) {
        Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
        Node closest = null;
        selectedNode = new HashMap<Integer, Node>();
        double distToClosest = Double.POSITIVE_INFINITY;
        for(Node n: nodes.values()) {
            double dist = clicked.distance(n.getLocation());
            if(dist < distToClosest) {
                closest = n;
                distToClosest = dist;
            }
        }
        selectedNode.put(closest.getID(),closest);
        redraw();
    }

    protected void onSearch() {
        String name = getSearchBox().getText();
        for (Road r: roads.values()){
            if (r.getLabel().equals(name)){
                for (Segments s: segments){
                    if (s.getRoad().equals(r)){
                        selectedSegment.add(s);
                        redraw();
                    }
                }
            }
        }
    }

    public void loadRoads(File roadFile){
        try{
            BufferedReader roadFileReader = new BufferedReader(new FileReader(roadFile));
//http://stackoverflow.com/questions/13405822/using-bufferedreader-readline-in-a-while-loop-properly
            roadFileReader.readLine(); //Skip header
            for(String line = roadFileReader.readLine(); line != null; line = roadFileReader.readLine()){
                String [] roadInfo = line.split("\t");
                int roadID = Integer.parseInt(roadInfo[0]);
                int roadType = Integer.parseInt(roadInfo[1]);
                String roadLabel = roadInfo[2];
                String roadCity = roadInfo[3];
                boolean oneWay = Boolean.parseBoolean(roadInfo[4]);
                int speed = Integer.parseInt(roadInfo[5]);
                int roadClass = Integer.parseInt(roadInfo[6]);
                boolean notForCar = Boolean.parseBoolean(roadInfo[7]);
                boolean notForPede = Boolean.parseBoolean(roadInfo[8]);
                boolean notForBicy = Boolean.parseBoolean(roadInfo[9]);

                Road newRoad = new Road(roadID, roadType, roadLabel, roadCity, oneWay, speed, roadClass, notForCar, notForPede, notForBicy);
                roads.put(roadID, newRoad); //add the new road to road map with ID as the key
            }
            roadFileReader.close();
        }
        catch(IOException e){
            System.out.print("caught exception: " + e);
        }

    }

    public void loadNodes(File nodeFile){
        try{
            BufferedReader nodeFileReader = new BufferedReader(new FileReader(nodeFile));
            for(String line = nodeFileReader.readLine(); line != null; line = nodeFileReader.readLine()){
                String [] nodeInfo = line.split("\t");
                int nodeID = Integer.parseInt(nodeInfo[0]);
                double latitude = Double.parseDouble(nodeInfo[1]);
                double longititude = Double.parseDouble(nodeInfo[2]);
                Location location = Location.newFromLatLon(latitude, longititude);
                HashSet<Segments> nodeSegments = new HashSet<>();

                Node newNode = new Node(nodeID, latitude, longititude, location, nodeSegments);
                nodes.put(nodeID, newNode);
            }
            nodeFileReader.close();
        }
        catch (IOException e){
            System.out.print("caught exception: " + e);
        }

    }

    public void loadSegments(File segmentsFile){
        try{
            BufferedReader segmentsFileReader = new BufferedReader(new FileReader(segmentsFile));
            segmentsFileReader.readLine(); //skip header
            for(String line = segmentsFileReader.readLine(); line != null; line = segmentsFileReader.readLine()){
                String [] segmentInfo = line.split("\t", 5); //first 5 tabs, up to the coordinates
                Road road = roads.get(Integer.parseInt(segmentInfo[0]));
                double segmentLength = Double.parseDouble(segmentInfo[1]);
                int nodeID1 = Integer.parseInt(segmentInfo[2]);
                int nodeID2 = Integer.parseInt(segmentInfo[3]);
                Node node1 = nodes.get(nodeID1); //getting node object relative to the ID in the nodes map
                Node node2 = nodes.get(nodeID2); //getting node object relative to the ID in the nodes map
                String [] coordinatesStringArray = segmentInfo[4].split("\t");
                int numberOfCoordinates = coordinatesStringArray.length;

                ArrayList<Location> locations = new ArrayList<>(); //new Arraylist for holding the locations
                for(int i = 0; i < numberOfCoordinates; i+=2){
                    Location newLocation = Location.newFromLatLon(
                            Double.parseDouble(coordinatesStringArray[i]),
                            Double.parseDouble(coordinatesStringArray[i+1]));
                    locations.add(newLocation);
                }

                Segments newSegment = new Segments(segmentLength, node1, node2, locations, road);
                node1.segements.add(newSegment);
                node2.segements.add(newSegment);
                segments.add(newSegment);
            }
        }
        catch(IOException e){
            System.out.print("caught exception: " + e);
        }
    }



    public static void main(String[] args) {
        new AucklandRoadMap();
    }

    public AucklandRoadMap() {

    }

    public void onMove(Move m){
        if (m.equals(Move.ZOOM_IN)){
            scale += (scale*0.1);
            redraw();
        }
        else if (m.equals(Move.ZOOM_OUT)){
            scale -= (scale*0.1);
            redraw();
        }
        else if (m.equals(Move.NORTH)){
            origin = new Location (origin.x,origin.y+0.5);
            redraw();
        }
        else if (m.equals(Move.EAST)){
            origin = new Location  (origin.x+0.5,origin.y);
            redraw();
        }
        else if (m.equals(Move.SOUTH)){
            origin = new Location  (origin.x,origin.y-0.5);
            redraw();
        }
        else if (m.equals(Move.WEST)){
            origin = new Location  (origin.x-0.5,origin.y);
            redraw();
        }
    }

    public void onLoad(File nodeFile, File roadFile, File segmentsFile, File x){
        loadRoads(roadFile);
        loadNodes(nodeFile);
        loadSegments(segmentsFile);
        scaling();
    }
}
