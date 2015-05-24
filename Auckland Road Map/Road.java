public class Road {
	
	public int id,type,speed,rclass;
	public String name,city;
	public boolean oneway,car,ped,bike;
	
	public Road (int ID,String roadname /*int roadtype,String roadcity,boolean isoneway,int roadspeed,int roadclass,boolean iscar,boolean isped, boolean isbike*/) {
		id = ID;
		name = roadname;
		
		/*roadtype = type;
		roadspeed = speed;
		roadclass = rclass;
		roadcity = city;
		isoneway = oneway;
		iscar = car;
		isped = ped;
		isbike = bike;*/
	}
	
	public int getID(){
		return id;
	}
	
	public int getType(){
		return type;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public int getRClass(){
		return rclass;
	}
	
	public String getName(){
		return name;
	}
	
	public String getCity(){
		return city;
	}
	
	public boolean isOneWay(){
		return oneway;
	}
	
	public boolean isForCars(){
		return car;
	}
	
	public boolean isForPed(){
		return ped;
	}
	
	public boolean isForBike(){
		return bike;
	}
	
	public String toString(){
		return ("ID: "+ id+ " Name: "+name);
	}

}
