import java.util.HashSet;;

public class Node {
	public int id;
	public double lat, lon;
	private Location location;
	public HashSet<Segment>segs;

	public Node (int ID,double x, double y, HashSet <Segment> segments){
		id = ID;
		lat = x;
		lon = y;
		location = Location.newFromLatLon(x, y);
		segs = segments;
	}
	
	public int getid(){
		return id;
	}
	
	public double getLat(){
		return lat;
	}
	
	public double getLon(){
		return lon;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public HashSet<Segment> getRoads(){
		return segs;
	}
	
	public String toString(){
		return ("Node ID: "+id+" Latitude: "+lat+" Longitude: "+lon+" Location: "+location);
	}
	
}
