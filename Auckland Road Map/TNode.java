import java.util.List;
import java.util.ArrayList;

//https://community.oracle.com/message/8789521 - used for reference

public class TNode {
	private TNode parent;
	private TNode[] children;
	private boolean isLeaf;
	private boolean isWord;
	private char letter;

	public TNode(){
		children = new TNode[26];
		isLeaf = true;
		isWord = false;
	}
	
	public TNode(char ltr){
		this.letter = ltr;
	}

	protected void addName(String name){
		isLeaf = false;
		int pos = name.charAt(0) - 'a';//converts ascii to letter

		if (children[pos] == null){
			children[pos] = new TNode(name.charAt(0));
			children[pos].parent = this;
		}

		if (name.length()>1){
			children[pos].addName(name.substring(1));
		}
		else{
			children[pos].isWord = true;
		}		
	}

	protected TNode getNode (char c){
		return children[c - 'a'];
	}

	protected List getNames(){
		List list = new ArrayList();

		if (isWord){
			list.add(toString());
		}

		if(!isLeaf){
			for (int i=0;i<children.length;i++){
				if(children[i] != null){
					list.addAll(children[i].getNames());
				}
			}
		}
		return list;
	}
}
