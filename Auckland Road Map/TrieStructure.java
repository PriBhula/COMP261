import java.util.ArrayList;
import java.util.List;


public class TrieStructure {
	
	private TNode root;

	public TrieStructure(){
		root = new TNode();
	}
	
	public void addName (String name){
		if (!name.isEmpty()){
			root.addName(name.toLowerCase());
		}
	}
	
	public List getNames (String prefix){
		TNode last = root;
		
		for (int i=0;i<prefix.length();i++){
			last = last.getNode(prefix.charAt(i));
		}
		
		if (last == null){
			return new ArrayList();
		}
		
		return last.getNames();
	}
}


