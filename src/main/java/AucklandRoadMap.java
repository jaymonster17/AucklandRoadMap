package main.java;

import java.awt.geom.Arc2D;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

/**
* Created by wintonnmj on 8/03/17.
*/
public class AucklandRoadMap extends GUI {

public Map<Integer, Road> roads = new HashMap<Integer, Road>();
public Map<Integer, Node> nodes = new HashMap<Integer, Node>();
public Map<Integer, Segments> segments = new HashMap<Integer, Segments>();

public void loadRoads(File roadFile){
try{
BufferedReader roadFileReader = new BufferedReader(new FileReader(roadFile));
//http://stackoverflow.com/questions/13405822/using-bufferedreader-readline-in-a-while-loop-properly

for(String line = roadFileReader.readLine(); line != null; line = roadFileReader.readLine()){
String [] roadInfo = line.split("\t");
int roadID = Integer.parseInt(roadInfo[0]);
double roadType = Integer.parseInt(roadInfo[1]);
String roadLabel = roadInfo[2];
String roadCity = roadInfo[3];
boolean oneWay = Boolean.parseBoolean(roadInfo[4]);
int speed = Integer.parseInt(roadInfo[5]);
int roadClass = Integer.parseInt(roadInfo[6]);
boolean notForCar = Boolean.parseBoolean(roadInfo[7]);
boolean notForPede = Boolean.parseBoolean(roadInfo[8]);
boolean notForBicy = Boolean.parseBoolean(roadInfo[9]);

Road newRoad = new Road(roadID, roadType, roadLabel, roadCity, oneWay,
        speed, roadClass, notForCar, notForPede, notForBicy);
roads.put(roadID, newRoad);
}
roadDataReader.close();
}
catch(IOException e){
System.out.print("caught exception: " + e);
}

}

public void loadNodes(File nodeFile){
try{
    BufferedReader nodeFileReader = new BufferedReader(nodeFile);

    for(String line = nodeFileReader.readLine(); line != null; nodeFileReader.readLine()){
        String [] nodeInfo = line.split("\t");
        int nodeID = Integer.parseInt(nodeInfo[0]);
        double latitude = Double.parseDouble(nodeInfo[1]);
        double longititude = Double.parseDouble(nodeInfo[2]);

        //
        //
        //  ADD CODE TO ADD NODE TO SEGMENTS
        //
     Node newNode = new Node(nodeID, latitude, longititude);
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
        BufferedReader segmentsFileReader = new BufferedReader(segmentsFile);
        for(String line = segmentsFileReader.readLine(); line != null; line = segmentsFileReader.readLine()){
            String [] segmentInfo = line.split("\t");
            int segmentID = Integer.parseInt(segmentInfo[0]);
            double segmentLength = Double.parseDouble(segmentInfo[1]);
            int nodeID1 = Integer.parseInt(segmentInfo[2]);
            int nodeID2 = Integer.parseInt(segmentInfo[3]);

            //
            //maybe just a String array
            //
            //
            
            ArrayList<Double> coords = new ArrayList<>();

            int segmentInfoSize = segmentInfo.length;
            for(int i = 4; i < segmentInfoSize; i++){
                double coordinate = Double.parseDouble(segmentInfo[i]);
                coords.add(coordinate);
            }
            Segments newSegment = new Segments(segmentID, segmentLength, nodeID1, nodeID2, coords);
            segments.put(segmentID, newSegment);
        }
    }
}



public static void main(String[] args) {
new AucklandRoadMap();
}

public AucklandRoadMap() {

}

public void onLoad(File roadFile, File nodeFile, File segmentsFile){
loadRoads(roadFile);
loadNodes(nodeFile);
loadSegments(segmentsFile);
}
}
