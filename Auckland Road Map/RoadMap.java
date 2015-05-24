import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import javax.security.auth.callback.TextOutputCallback;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;
import javax.swing.plaf.basic.BasicTreeUI.MouseInputHandler;

public class RoadMap extends GUI {

	public Location origin ;//= new Location(0,0);
	public double scale;

	public Map<Integer,Node> nodes = new HashMap<Integer,Node>();
	public Map<Integer,Road> roads = new HashMap<Integer,Road>();
	public Map<Integer,Segment> segments = new HashMap<Integer,Segment>();

	public HashSet<Segment> segs = null;
	public ArrayList<Location> segLoc = null;


	public Map<Integer,Segment> selectedSeg = new HashMap<Integer,Segment>();
	public Map<Integer,Node> selectedNod = new HashMap<Integer,Node>();

	private TrieStructure trie = new TrieStructure(); //i made the classes for this but have no idea how to use them

	protected void scaling() {
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


	protected void redraw(Graphics g) {
		//System.out.println("Origin: "+origin+" Scale: "+scale);
		if(!nodes.isEmpty()) {//while there are nodes to redraw
			for (Node n: nodes.values()){
				Point pt = n.getLocation().asPoint(origin, scale);//make into point//
				//System.out.print("pt: "+pt);
				g.setColor(Color.BLACK);
				g.fillOval(pt.x - 1, pt.y -1 ,4,4);//and draw it
			}
		}
		
		if (!segments.isEmpty()){
			for (Segment s: segments.values()){ //while there are segments..
				//System.out.println("list: "+s.getCoordLoc().size());
				for(int i=0; i<s.getCoordLoc().size()-1; i=i+2){
					//System.out.print("s: "+s.getCoordLoc().get(i) + " i+1: "+s.getCoordLoc().get(i+1));
					Point p1 = s.getCoordLoc().get(i).asPoint(origin, scale);
					Point p2 = s.getCoordLoc().get(i+1).asPoint(origin, scale);
					//System.out.println("p1: "+p1.toString()+" p2: "+p2.toString() );
					g.setColor(Color.PINK);
					g.drawLine(p1.x, p1.y, p2.x, p2.y);
					//System.out.println("asfhklsj");
					
				}	
				
				//				Point pt1 = s.getNode1().getLocation().asPoint(origin, scale);//make both ends into points
				//				Point pt2 = s.getNode2().getLocation().asPoint(origin, scale);
				//				g.setColor(Color.PINK);
				//				g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);//and draw a line between them
			}
		}
		if(!selectedSeg.isEmpty()){
			for (Segment s: selectedSeg.values()){ 
				Point pt1 = s.getNode1().getLocation().asPoint(origin, scale);
				Point pt2 = s.getNode2().getLocation().asPoint(origin, scale);
				g.setColor(Color.YELLOW);
				g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
			}
		}
		if(!selectedNod.isEmpty()) {
			for (Node n: selectedNod.values()){
				Point pt = n.getLocation().asPoint(origin, scale);
				g.setColor(Color.BLUE);
				g.fillOval(pt.x - 1, pt.y -1 ,4,4);
			}
		}
	}

	protected void onClick(MouseEvent e) {
		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		Node closest = null;
		selectedNod = new HashMap<Integer, Node>();
		double distToClosest = Double.POSITIVE_INFINITY;
		for(Node n: nodes.values()) {
			double dist = clicked.distance(n.getLocation());
			if(dist < distToClosest) {
				closest = n;
				distToClosest = dist;
			}
		}
		selectedNod.put(closest.getid(),closest);
		//redraw();		
//		for (Segment s:segments.values()){
//			if	(s.getNode1().getid() == closest.getid() || s.getNode2().getid() == closest.getid()) {
//				getTextOutputArea().setText("ID: "+closest.getid()+" Roads: "+s.getRoad().getName());
//			}
//		}
	}

	protected void onSearch() {	
		String name = getSearchBox().getText();
		for (Road r: roads.values()){
			if (r.getName().equals(name)){
				for (Segment s: segments.values()){
					if (s.getRoad().equals(r)){
						//System.out.print("Name: "+name+" S ID: "+s.getID()+" R ID: "+r.getID());
						selectedSeg.put(s.getID(),s);
						redraw();
					}
				}
			}
			/*else if (!r.getName().equals(name)){
				getTextOutputArea().setText(name+" is not a valid road name");//hits this statement no matter what???
			}*/
		}
	}

	protected void onMove(Move m) {
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

	public void loadNodes(File file){
		try {
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = buff.readLine();
			while((line = buff.readLine()) != null){
				String[] data = line.split("\t");
				int id = Integer.parseInt(data[0]);
				double lat = Double.parseDouble(data[1]);
				double lon = Double.parseDouble(data[2]);

				for(Segment s:segments.values()){
					if(s.getNode1().equals(s)||s.getNode2().equals(s)){
						segs.add(s);
					}
				}
				Node n = new Node(id,lat,lon,segs);
				nodes.put(n.id, n);
				//System.out.println(nodes.size());
			}
			//System.out.println("Nodes: "+nodes.size());
			buff.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadRoads (File file){
		try {
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = buff.readLine();
			while((line = buff.readLine()) != null){
				String [] data = line.split("\t");
				int id = Integer.parseInt(data[0]);//parse everything into appropriate type
				//int type = Integer.parseInt(data[1]);
				String name = data[2];
				/*String city = data[3];

				int intoneway = Integer.parseInt(data[4]);
				boolean oneway = true;
				if (intoneway == 1){
					oneway = true;//turn into correct type	
				}
				else{
					oneway = false;
				}

				int speed = Integer.parseInt(data[5]);
				int roadclass = Integer.parseInt(data[6]);

				int intcar = Integer.parseInt(data[7]);
				boolean car = true;
				if (intcar == 1){
					car = true;						
				}
				else{
					car = false;
				}

				int intped = Integer.parseInt(data[8]);
				boolean ped = true;
				if (intped == 1){
					ped = true;						
				}
				else{
					ped = false;
				}

				int intbike = Integer.parseInt(data[9]);
				boolean bike = true;	       	           
				if (intbike == 1){
					bike = true;
				}
				else{
					bike = false;
				}*/					


				Road r = new Road(id, name/*,type, city, oneway, speed, roadclass, car, ped, bike*/);
				//System.out.println(name+id);
				//System.out.println("r: "+r.getName()+r.getID());
				//System.out.println(name);
				roads.put(id, r);
				//System.out.print(roads.values());
				//System.out.print("Roads: "+roads.size());
			}
			buff.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void loadSegments(File file){
		try {
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = buff.readLine();
			while((line = buff.readLine()) != null){
				String [] data = line.split("\t",5);
				int id = Integer.parseInt(data[0]);
				double length = Double.parseDouble(data[1]);
				int node1 = Integer.parseInt(data[2]);
				int node2 = Integer.parseInt(data[3]);

				String[] coordsArray = data[4].split("\t");
				//System.out.println(Arrays.toString(coordsArray));
				double[] doubleCoord=new double[coordsArray.length];//-36.88853	174.72218	-36.88954	174.72361	-36.88992	174.72398
				for (int i=0;i<coordsArray.length;i++){
					doubleCoord[i] = Double.parseDouble(coordsArray[i]);
					//System.out.println("dc: "+doubleCoord.length);
				}
				
				segLoc = new ArrayList<Location>();
				for(int i=0;i<doubleCoord.length;i=i+2){
					//System.out.println("doubleCoord["+i+"]:"+doubleCoord[i]+" doubleCoord["+(i+1)+"]:"+doubleCoord[i+1]);
					Location location = Location.newFromLatLon(doubleCoord[i],doubleCoord[i+1]);
					segLoc.add(location);
					//System.out.println("segLoc: "+segLoc.size());
				}

				Node n1 = nodes.get(node1);
				Node n2 = nodes.get(node2);
				

				Road road = null;

				for (Road r: roads.values()){
					if(r.getID() == id){
						road = r;
						break;
					}
				}

				Segment s = new Segment(id,length,n1,n2,road,segLoc);
				//System.out.println(s.getID());
				segments.put(id, s);

				//System.out.println("Seg: " + segments.size()+" Road: "+road.name);
			}
			buff.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		loadRoads(roads);
		loadNodes(nodes);
		loadSegments(segments);
		scaling();
	}

	public static void main (String[] args){
		new RoadMap();
	}


}
