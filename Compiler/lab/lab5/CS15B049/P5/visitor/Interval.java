package visitor;

import java.util.*;

public class Interval {
	public String mName ;
	public HashMap<String,Range>liveMap = new HashMap<>();
	
	public Interval(String st) { this.mName = st ; }
	
	public void addR(String st,int stmtNo) {
		
		if(liveMap.containsKey(st)) {
			Range r = liveMap.get(st);
			r.end = stmtNo;
		}
		else {
			Range r = new Range();
			r.start = stmtNo;
			r.end = stmtNo;
			this.liveMap.put(st, r);
		}
		
	}
	
}







class ComparebyStart  implements Comparator<String> {

	HashMap<String,Range>liveMap ;
	
	public void setliveMap(HashMap<String,Range>l ) {
		this.liveMap = l;
	}
	
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		
		Range r1 = liveMap.get(o1);
		Range r2 = liveMap.get(o2);
		
		if(r1.start < r2.start ) {
			return -1;
		}else if (r1.start > r2.start ) {
			return 1;	
		}else 
			return 0;
		
	}
	
}

class ComparebyEnd  implements Comparator<String> {

	HashMap<String,Range>liveMap ;
	
	public void setliveMap(HashMap<String,Range>l ) {
		this.liveMap = l;
	}
	
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		
		Range r1 = liveMap.get(o1);
		Range r2 = liveMap.get(o2);
		
		if(r1.end < r2.end) {
			return -1;
		}else if (r1.end > r2.end ) {
			return 1;	
		}else 
			return 0;
		
	}
	
}

