package main.java;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.awt.Point;

public class AucklandRoadMap extends GUI {

    private Location origin = new Location(-36.849920, 174.760981);
    private double scale = 0.1;

    private Map<Integer, Road> roads = new HashMap<Integer, Road>();
    private Map<Integer, Node> nodes = new HashMap<Integer, Node>();
    private List<Segment> segments = new ArrayList<>();
    private Trie<Road> roadsTrie = new Trie<Road>();
    private Set<Polygon> polygons = new HashSet<>();

    private Map<Integer, Node> selectedNode = new HashMap<Integer, Node>();
    private List<Road> searchedRoad = new ArrayList<>();

    public static void main(String[] args) {
        new AucklandRoadMap();
    }

    public void onLoad(File nodeFile, File roadFile, File segmentsFile, File polygons){
        loadRoads(roadFile);
        loadNodes(nodeFile);
        loadSegments(segmentsFile);
        loadTrie();
        loadPolygons(polygons);
        scaling();
    }

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

    public void onSearch() {
        JTextField jTextField = getSearchBox();
        String input = jTextField.getText();

        if(input.equals("")) return;

        searchedRoad.clear();
        searchedRoad.addAll(roadsTrie.getAll(input));
    }

    public void redraw(Graphics g){
        g.setColor(Color.DARK_GRAY);
        for (Polygon polygon: polygons) {
            int x[] = polygon.getX();
            int y[] = polygon.getY();
            g.fillPolygon(x,y, x.length);
        }

        if(!nodes.isEmpty()){ //if the map of nodes is not empty
            g.setColor(Color.RED);
            for(Node n: nodes.values()){ //iterate through the whole map
                drawNode(g, n);
            }
        }

        g.setColor(Color.BLACK);
        for(Road r : roads.values()) {
            drawRoad(g, r);
        }

        if(!searchedRoad.isEmpty()){
            g.setColor(Color.BLUE);
            for(Road r : searchedRoad) {
                drawRoad(g, r);
            }
        }

        if(!selectedNode.isEmpty()) {
            g.setColor(Color.BLUE);
            for (Node n: selectedNode.values()){
                drawNode(g, n);
            }
        }
    }

    private void drawRoad(Graphics g, Road road) {
        if (road == null) {
            return;
        }

        for(Segment s: road.getSegments()){ //for each segment in the map
            for(int i = 0; i < s.getCoords().size()-1; i++){ //get the size of the CoOrdinate array and iterate for that long
                Point point1 = s.getCoords().get(i).asPoint(origin, scale); //make a point using the first location of the segment
                Point point2 = s.getCoords().get(i+1).asPoint(origin, scale); //make a point using the second point of the segment

                g.drawLine(point1.x, point1.y, point2.x, point2.y); //draw a line from first to second point
            }
        }
    }

    private void drawNode(Graphics g, Node n){
        Point pt = n.getLocation().asPoint(origin, scale);
        g.fillOval(pt.x - 1, pt.y -1,4,4);
    }

    protected void onClick(MouseEvent e) {
        Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
        Node closest = null;
        selectedNode = new HashMap<Integer, Node>();
        double distToClosest = Double.POSITIVE_INFINITY;
        for (Node n : nodes.values()) {
            double dist = clicked.distance(n.getLocation());
            if (dist < distToClosest) {
                closest = n;
                distToClosest = dist;
            }
        }

        if (closest != null) {
            selectedNode.put(closest.getID(), closest);
            //System.out.print(roads.get(selectedNode.get().getID()).getLabel());
            redraw();
        }


//        Road temp = roads.get(closest.getID());
//        String roadName = temp.getLabel();
//        JTextArea output = getTextOutputArea();
//        output.append(roadName);
//        output.append("\n");
//        for (String s:roadNames) {
//            output.append(s);
//            output.append("\n");
//        }
    }

    protected void onWheel(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        if (rotation > 0) {scale  *= rotation * 1.5;}
        else if (rotation < 0) {scale /= -rotation * 1.5;}
    }

    protected void onDrag(int x, int y) {
        origin = new Location(origin.x + x, origin.y + y);
    }

    public void loadRoads(File roadFile){
        try{
            BufferedReader roadFileReader = new BufferedReader(new FileReader(roadFile)); //create buffered reader, pass it a file reader, pass that the file (from on load)
//http://stackoverflow.com/questions/13405822/using-bufferedreader-readline-in-a-while-loop-properly
            roadFileReader.readLine(); //Skip header
            for(String line = roadFileReader.readLine(); line != null; line = roadFileReader.readLine()){ //while there are still lines
                String [] roadInfo = line.split("\t"); //split the string into a string Array by tabs
                int roadID = Integer.parseInt(roadInfo[0]); //define roadID
                int roadType = Integer.parseInt(roadInfo[1]); //define roadType
                String roadLabel = roadInfo[2]; //define roadLabel
                String roadCity = roadInfo[3]; //define
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

                Node newNode = new Node(nodeID, location);
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

                Segment newSegment = new Segment(segmentLength, node1, node2, locations, road);
                node1.addSegment(newSegment);
                node2.addSegment(newSegment);
                road.addSegment(newSegment);
                segments.add(newSegment);
            }
        }
        catch(IOException e){
            System.out.print("caught exception: " + e);
        }
    }

    public void loadTrie() {
        for (Road road : roads.values()) {
            roadsTrie.put(road.getLabel(), road);
        }
    }

    public void loadPolygons(File polygonFile){
        if(polygonFile == null) { return; }

        try {
            FileReader fileReader = new FileReader(polygonFile);
            BufferedReader polygonFileReader = new BufferedReader(fileReader); //make reader
            while(polygonFileReader.ready()) { //while it had data in the file
                String line = polygonFileReader.readLine();
                if (!line.startsWith("Data0")) {
                    continue;
                }
                line = line.substring(7,line.length()-2);
                String[] polygonData = line.split("\\),\\("); //ignore brackets, split by commas
                Polygon polygon = new Polygon(); //make a new polygon object
                for (int i = 0; i < polygonData.length; i++) { //
                    String[] latLongStringArray = polygonData[i].split(","); //split the vector into the x/y coordinates by splitting with comma
                    double[] latLongDoubleArray = new double[2]; //double array that will hold the x/y positions
                    latLongDoubleArray[0] = Double.parseDouble(latLongStringArray[0]); //x pos == parsed double from xyPoints
                    latLongDoubleArray[1] = Double.parseDouble(latLongStringArray[1]); //y pos == parsed double from xyPoints
                    Location point = Location.newFromLatLon(latLongDoubleArray[0], latLongDoubleArray[1]);
                    polygon.addPoint(point); //add the vector into the polygon object as a point
                }
                this.polygons.add(polygon); //add the polygon object to the polygons set.
            }
        } catch (IOException e) {
            System.out.println("caught exception: " + e);
        }
    }

    public void onMove(Move m){
        if (m.equals(Move.ZOOM_IN)){
            scale += (scale*0.1);
        }
        else if (m.equals(Move.ZOOM_OUT)){
            scale -= (scale*0.1);
        }
        else if (m.equals(Move.NORTH)){
            origin = new Location(origin.x,origin.y+1);
        }
        else if (m.equals(Move.EAST)){
            origin = new Location(origin.x+1,origin.y);
        }
        else if (m.equals(Move.SOUTH)){
            origin = new Location(origin.x,origin.y-1);
        }
        else if (m.equals(Move.WEST)){
            origin = new Location(origin.x-1,origin.y);
        }
        redraw();
    }
}
