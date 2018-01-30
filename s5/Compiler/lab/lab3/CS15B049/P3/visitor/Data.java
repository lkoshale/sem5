package visitor;

import java.util.*;


public class Data {	
	
	public int Moffset = 0;
	//key = cName+_+Mname
	public HashMap<String,Method>FnAr = new HashMap<>();
	
	//iterate till first find
	public List<String>VarMap = new ArrayList<>();

	private String Extend = null;
	
	
	public String getEx() {return this.Extend;}
	public void setEx(String ex) {this.Extend = ex;}
	
	public void incMoff(int k) {
		Moffset += k;
	}
	
	public int getSize() {
		return (this.VarMap.size()+1)*4;
	}
	
	
}

class Method{
	
	public String name;
	public String cName;
	
	public int offset;
	

	public List<String>Arg = new ArrayList<>();
	
	public Method(String name,String cName,int offset) {
		this.cName = cName;
		this.name = name;
		this.offset = offset;
	}
	
	//store var--> temp
	public HashMap<String,String>Mvar = new HashMap<>();
	
//	public List<String>Arg
	
}
