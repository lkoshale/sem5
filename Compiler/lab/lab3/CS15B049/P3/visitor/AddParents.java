package visitor;

import java.util.ArrayList;
import java.util.HashMap;

public class AddParents {
	
	public static HashMap<String,Data> Table = GJNoArguDepthFirst.Table;

	public static void setTable() {
		AddParents.Table = GJNoArguDepthFirst.Table;
	}
	
	public static void Add() {
		
		for( String s : Table.keySet()) {
			Data data = Table.get(s);
			//recursively add the parent detail
			recurAdd(s,data);	
		}
	
	}
	
	
	
	public static void recurAdd(String s,Data data) {
		
		if(data.getEx() != null) {
			
			Data p = Table.get(data.getEx());
			
			for(String key : p.FnAr.keySet()) {
				
				Method value = p.FnAr.get(key);
				String str = s+"_"+value.name;
				
				// if not there (over riding if presnt)
				if(!data.FnAr.containsKey(str)) {
					Method nM = copyM(value,data.Moffset);
					data.Moffset+=4;
					
					//put as this classname+_+mName as to find it
					// with object of current class still  internal Cname is its class name
					
					data.FnAr.put(str, nM);
			
				}
				
			}
			
			
			for(int i=0;i<p.VarMap.size();i++) {
				
				String str = p.VarMap.get(i);
				int idx = str.indexOf("$");
				String varName = str.substring(idx+1);
				String newVname = s+"$"+varName;
				
				if(!data.VarMap.contains(newVname)) {
					data.VarMap.add(newVname);
				}
				
			}
			
			
			recurAdd(data.getEx(),p);
		}
		
		
	}
	
	
	//added with Cname(actual)_Mname  //not the class which is extending
	public static Method copyM(Method mth,int off) {
		Method rt = new Method(mth.name,mth.cName,off);
		rt.Arg = new ArrayList(mth.Arg);
		rt.Mvar = new HashMap(mth.Mvar);
		return rt;
	}
	
	
}
