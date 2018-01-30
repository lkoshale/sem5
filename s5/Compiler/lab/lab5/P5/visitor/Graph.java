package visitor;

import java.util.*;

public class Graph {
	
	public String methodName;
	public int argCount;
	
	public int usedScount;
	
	public int maxArg;
	
	public int stackCount ;
	
	public Set<GNode> node = new HashSet<>();
	
	public HashMap<Integer,GNode>Imap = new HashMap<>();
	
	public HashMap<String,GNode>Lmap = new HashMap<>();
	
	public Graph(String n,int a) {
		this.methodName = n;
		this.argCount = a;
		this.maxArg = 0;
		this.stackCount = 0;
		this.usedScount = 0;
	}
	
	public void add(GNode k) {
		this.node.add(k);
	}
	
	public void buildMap() {
		
		for(GNode n : node) {
			
			Imap.put(n.stmtNo,n);
			
			if(n.label != null) {
				Lmap.put(n.label,n);
			}
		}
	
	}
	
	public GNode getfromLabel(String L) {
		return (this.Lmap.get(L));
	}
	
	public GNode getfromLine(int l) {
		return (this.Imap.get(l));
	}
	
	
	public void addParg(int k) {
		if( k > this.maxArg){
			this.maxArg = k;
		}
	}
	
	
}
