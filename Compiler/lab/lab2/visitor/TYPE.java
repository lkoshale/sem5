package visitor;

// "ArrayType"  "Boolean" "Integer" "This"

public class TYPE {
	
	public String type;

	public TYPE(String t) {this.type=t;}
	
	public boolean Match(TYPE t2) {
		boolean ans = false;
		if(this.type.compareTo(t2.type.trim())==0) {
			return true;
		}
		
		return ans;
	}

}
