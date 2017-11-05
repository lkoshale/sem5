package visitor;


import java.util.*;

public class Analysis {

	//method ---> map of reg alloc
	public static HashMap< String,HashMap<String,String> > Reg = new HashMap<>();
	public static HashMap< String,HashMap<String,Integer> >SPILL = new HashMap<>();
	
	public static HashMap<String,Interval> live = new HashMap<>();
	
	
	public static HashMap<String, HashMap<String,String> > LABEL = new HashMap<>();
	
	public static void liveAnalysis() {
	
		for( Graph gp : GJVoidDepthFirst.CFG.values() ) {
		
			boolean over = false;
			
			while(!over) {
				
				boolean same = true;
				
				for(GNode g : gp.node ) {
					
					Set<String>in1 = new HashSet<>(g.in);
					Set<String>out1 = new HashSet<>(g.out);
					
					//cal g.out
					Set<String>Union = new HashSet<>();
					for(GNode adj : g.adj) {
						Union.addAll(adj.in);
					}
					//out -> union
					g.out = new HashSet<>(Union);
					
					Set<String>inUN = new HashSet<>(g.use);
					Set<String>setDiff = new HashSet<>(g.out);
					
					setDiff.removeAll(g.def);
					inUN.addAll(setDiff);
					//in
					g.in = new HashSet<>(inUN);
					
					if(g.in.equals(in1) && g.out.equals(out1) ) {
						same = (same && true );
					}
					else {
						same = ( same && false);
					}
					
				}				
				
				if(same==true) {
					over = true;
				}
				
			}
		}
	}
	
	public static List<GNode> sorted(Set<GNode> gn){
		List<GNode> list = new ArrayList<>(gn);
		
		list.sort(new Comparator<GNode>() {

			@Override
			public int compare(GNode o1, GNode o2) {
				// TODO Auto-generated method stub
				if(o1.stmtNo > o2.stmtNo )return 1;
				else if(o1.stmtNo < o2.stmtNo) return -1;
				else return 0;
			}
		});
		
		return list;
	}
	
	
	public static void buildInterval() {
		
		for( Graph gp : GJVoidDepthFirst.CFG.values() ) {
			
			Interval I = new Interval(gp.methodName);
			
			for(GNode gn : Analysis.sorted(gp.node) ){
				
				Set<String> Odef = new HashSet<>();
				
				for(String t : gn.in) {
					I.addR(t, gn.stmtNo);
					Odef.add(t);
				}
				
				for(String t : gn.out ) {
					I.addR(t, gn.stmtNo);
					Odef.add(t);
				}
				
				for(String t : gn.def) {
					if(!Odef.contains(t)) {
						I.addR(t, gn.stmtNo);
					}
				}
			}
			
			live.put(gp.methodName, I);
		}
		
	}
	
	
	public static void RegAlloc() {

		for( Graph gp : GJVoidDepthFirst.CFG.values() ) {
			
			Interval I = live.get(gp.methodName);
			
			int avRegSize = 18 - gp.argCount ; 
			
			//if spill all s are used so stored
			
			int argSpl = 0;
			
			if(gp.argCount > 4) {
				argSpl = 4 - gp.argCount;
			}
			
			int spillNo = 8 + argSpl;
			
			HashMap<String,String>regA = new HashMap<>();
			HashMap<String,Integer>sp = new HashMap<>();
			
			Stack<String> Ravail = new Stack<>();
			
			//TODO assumed  num of arg is always less than 8
			
			//stack to queue ---> change the order
			for(int i = 0 ; i<10 ;i++) { Ravail.push("t"+String.valueOf(9-i)); }
			for(int i = 7 ; i >= gp.argCount ;i--) { Ravail.push("s"+String.valueOf(i)); }
			
			//sorted by inc end 
			List<String>alive = new ArrayList<>();
			
			for(String st : sortSt(I.liveMap.keySet(),I.liveMap) ) {
				
				Range R = I.liveMap.get(st);
				
				List<String>active = new ArrayList<>(alive);
				//Expire old interval 
				for( String str : active ) {
					
					Range j = I.liveMap.get(str);
					
					if( j.end >= R.start )
						break;
					else {
						alive.remove(str);
						
						String register = regA.get(str);  //TODO can be null
						Ravail.push(register);
					}
				}
				
				//sort maybe removed
				sortEnd(alive,I.liveMap);
				
				if(alive.size() == avRegSize ) {
					//spill
					
					//sorted by end
					//last interval
					String spillT = alive.get(alive.size()-1);
					Range spillR = I.liveMap.get(spillT);
					
					if( spillR.end > R.end ) {
						String register =  regA.get(spillT);
						regA.put(st, register);
						
						sp.put(spillT, spillNo++);
						
						alive.remove(spillT);
						alive.add(st);
						
						regA.remove(spillT);
						
						sortEnd(alive,I.liveMap);
						
					}
					else {
						// its spilled
						sp.put(st,spillNo++);
					}
					
					
				}
				else {
					
					String register = Ravail.pop();
					regA.put(st,register);
					alive.add(st);
					sortEnd(alive,I.liveMap);
				}
				
			}
			
			//add in the map
			Reg.put(gp.methodName,regA);
			SPILL.put(gp.methodName,sp);
			
		}
		
	}
	
	public static List<String> sortSt ( Set<String>alive ,HashMap<String,Range>liveMap ){
		
		List<String> list = new ArrayList<>(alive);
		
		list.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				Range r1 = liveMap.get(o1);
				Range r2 = liveMap.get(o2);
				
				if(r1.start < r2.start ) {
					return -1;
				}else if (r1.start > r2.start ) {
					return 1;	
				}else 
					return 0;
			}
			
		});
		
		return list;
	
	}
	
	
	public static void sortEnd ( List<String>list ,HashMap<String,Range>liveMap ){
		
		
		list.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				Range r1 = liveMap.get(o1);
				Range r2 = liveMap.get(o2);
				
				if(r1.end < r2.end) {
					return -1;
				}else if (r1.end > r2.end ) {
					return 1;	
				}else 
					return 0;
			}
			
		});
		
	
	}
	
	public static void updateStack() {
		
		for(Graph gh : GJVoidDepthFirst.CFG.values() ) {
			
			HashMap<String,Integer>spill = SPILL.get(gh.methodName);
			HashMap<String,String> reg = Reg.get(gh.methodName);
			
			int k = 0;
			if(gh.argCount > 4 ) { k = gh.argCount-4; }
			
			if(!spill.isEmpty() ) {
				//save all s reg=8
				gh.stackCount = 8 + spill.size() + k;
			}
			else {
				
				Set<String>usedS = new HashSet<>();
				
				for(String st : reg.values() ) {
					if(st.charAt(0)== 's') usedS.add(st);
				}
				
				gh.usedScount = usedS.size() ;
				gh.stackCount = gh.argCount+usedS.size()+ k + 1;
				//keeping one extra stack
			}
			
		}
		
	}
	
	
	
	public static void globalLabel() {
		int num = 0;
		
		for(Graph gh : GJVoidDepthFirst.CFG.values() ) {
			HashMap<String,String>L = new HashMap<>();
			
			for(GNode g : gh.node) {
				
				if(g.label != null ) {
					String lb = "L"+String.valueOf(num++);
					L.put(g.label,lb);
				}
			}
			
			LABEL.put(gh.methodName,L);
			
		}
		
	}
	
	
}
