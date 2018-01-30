package visitor;

import java.util.*;

public class Car {
	public String scope;
	public String cName=null;
	public String mName=null;
	public String id = null;
	
	public void setID(String str) {
		this.id=str;
	}
	
	public SymbolTable sym;

	public Car() {}
	public List<String>mList = new ArrayList();
	
	public void addType(String str) {this.mList.add(str);}
	
	public void addSym(SymbolTable sym) {
		this.sym=sym;
	}
	
	public void reset() {
		this.mList.clear();
	}
	
}
