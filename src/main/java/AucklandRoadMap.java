package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wintonnmj on 8/03/17.
 */
public class AucklandRoadMap {

    ArrayList<Road> roads = new ArrayList<Road>();
    File roadFile = new File("roadID-roadInfo.tab");
    File nodeFile = new File("nodeID-lat-lon.tab");
    File segmentsFile = new File("roadSeg-roadID-length-nodeID-nodeID-coords.tab");

    BufferedReader roadData = new BufferedReader(new FileReader(roadFile));
    BufferedReader nodeData = new BufferedReader(new FileReader(nodeFile));
    BufferedReader segmentsData = new BufferedReader(new FileReader(segmentsFile));

    public static void main(String[] args) {
        new AucklandRoadMap();
    }

    public AucklandRoadMap() {

    }
}
