import java.util.List;

public class Segment {
	public int id;
	public Node n1,n2;
	public Road r;
	public double length;
	public List<Location> loc;

	public Segment(int ID,double leng, Node node1, Node node2, Road road, List<Location> segLoc){
		id = ID;
		length = leng;
		n1 = node1;
		n2 = node2;
		r = road;
		loc = segLoc;
	}	
	
	public int getID(){
		return id;
	}
	
	public double getLength(){
		return length;
	}
	
	public Node getNode1(){
		return n1;
	}
	
	public Node getNode2(){
		return n2;
	}
	
	public Road getRoad(){
		return r;
	}
	
	public List<Location> getCoordLoc(){
		return loc;
	}
	
	
}
