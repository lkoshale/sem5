package visitor;

public class Pass {
	
	int argCount ;

	boolean fromExp ;
	
	boolean fromCall ;
	
	boolean fromStmtLst ;
	
	public Pass(int c) {
		
	this.argCount = c;
	
	fromExp = false;
		
	fromCall = false;
		
	fromStmtLst = false;
		
	}

	
	
}
