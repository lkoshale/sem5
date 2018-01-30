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
			
			if((data.getEx() != null)) {
				
				Data d = new Data();
				
				//add parent data in order in d
				recurAdd(s,d,data.getEx());	

				//add its data at last
				for(String key : data.FnAr.keySet()) {
					
					Method value = data.FnAr.get(key);
					String str = s+"_"+value.name;
					
					// if not there (over riding if presnt)
					if(!d.FnAr.containsKey(str)) {
						Method nM = copyM(value,d.Moffset);
						d.Moffset+=4;
						
						//put as this classname+_+mName as to find it
						// with object of current class still  internal Cname is its class name
						
						d.FnAr.put(str, nM);
				
					}
					else {
						
						Method nM = d.FnAr.get(str);
						if(value.cName != nM.cName) {
							nM.cName = s;
						}
						
					}
					
				}
				
				for(int i=0;i<data.VarMap.size();i++) {
					
					String str = data.VarMap.get(i);
					int idx = str.indexOf("$");
					String varName = str.substring(idx+1);
					String newVname = s+"$"+varName;
					
					if(!d.VarMap.contains(newVname)) {
						d.VarMap.add(newVname);
					}
					
				}
				
				
				//change current objects data
				data.FnAr = new HashMap(d.FnAr);
				data.VarMap = new ArrayList(d.VarMap);
				
				//over
			}
			
		}
	
	}
	

	public static void recurAdd(String s,Data data,String Parent) {
		
		Data p = Table.get(Parent);
		
		
		//Base case
		if(p.getEx()==null) {
			
			
			for(String key : p.FnAr.keySet()) {
				
				Method value = p.FnAr.get(key);
				String str = s+"_"+value.name;
				
				// if not there (over riding if presnt)
				if(!data.FnAr.containsKey(str)) {
					Method nM = copyM(value,value.offset);
					data.Moffset+=4;
					
					//put as this classname+_+mName as to find it
					// with object of current class still  internal Cname is its class name
					
					data.FnAr.put(str, nM);
			
				}
				else {
					
					Method nM = data.FnAr.get(str);
					if(value.cName != nM.cName) {
						
						nM.cName = Parent;
					}
					
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
			
			
			return ;
			
		}
		
		recurAdd(s,data,p.getEx());	
		
		
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
			else {
				
				Method nM = data.FnAr.get(str);
				if(value.cName != nM.cName) {
					
					nM.cName = Parent;
				}
				
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
		
		
		
		
		
		
		
	}
	/*
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
	*/
	
	//added with Cname(actual)_Mname  //not the class which is extending
	public static Method copyM(Method mth,int off) {
		Method rt = new Method(mth.name,mth.cName,off);
		rt.Arg = new ArrayList(mth.Arg);
		rt.Mvar = new HashMap(mth.Mvar);
		return rt;
	}
	
	
}
