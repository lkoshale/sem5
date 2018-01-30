package visitor;

import java.util.*;

public class GNode {
	public int stmtNo ;
	String label = null;
	
	public Set<String>in;
	public Set<String>out;
	
	Set<String>def;
	Set<String>use;
	
	public Set<GNode>adj;
	
	public GNode(int st) {
		this.stmtNo = st;
		in= new HashSet<>();
		out  = new HashSet<>();
		def = new HashSet<>();
		use = new HashSet<>();
		adj = new HashSet<>();
	}
	
	void addEdge(GNode n) {
		this.adj.add(n);
	}
	
	void addUse(String st, int n) {
		int ind = st.indexOf(" ");
		int num = Integer.parseInt(st.substring(ind+1));
		
		if(num>=n) {
			this.use.add(st);
		}
		
	}
	
	void addDef(String st, int n) {
		int ind = st.indexOf(" ");
		int num = Integer.parseInt(st.substring(ind+1));
		
		if(num>=n) {
			this.def.add(st);
		}
		
	}
	
	
}
