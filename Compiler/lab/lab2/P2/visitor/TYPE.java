package visitor;

import java.util.Hashtable;

// "ArrayType"  "Boolean" "Integer" "This"

public class TYPE {
	
	public static  Hashtable<String,SymbolTable>Table;
	
	public String type;

	public TYPE(String t) {this.type=t;}
	
	public boolean Match(TYPE t2) {
		boolean ans = false;
		
		if(this.type.compareTo(t2.type.trim())==0) {
			return true;
		}
		else if( (!isPrimitive(this) ) && (!isPrimitive(t2))) {
			return checkSuperClass(this.type,t2.type);
		}
		
		return ans;
	}
	
	public boolean isPrimitive(TYPE t) {
		if(t.type.compareTo("ArrayType")==0||t.type.compareTo("Boolean")==0||t.type.compareTo("Integer")==0) {
			return true;
		}
		return false;
	}
	
	public boolean checkSuperClass(String t1,String t2) {
		boolean ans = false;
		if(Table.containsKey(t2) && Table.containsKey(t1)) {
		
			SymbolTable sy2 = Table.get(t2);
			
			if(sy2.Extend!=null ) {
				
				if(sy2.Extend.compareTo(t1)==0) {
					return true;
				}
				else return checkSuperClass(t1,sy2.Extend);
			}
			
			return false;
		}
		
		return ans;
	}

}
