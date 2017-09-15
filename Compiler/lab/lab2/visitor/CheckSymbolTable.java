package visitor;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class CheckSymbolTable {
	
	
	
	public static  Hashtable<String,SymbolTable>Table = new  Hashtable<String,SymbolTable>();
	
	public static void check() {
		if(Table.size()==0) return;
		CirclurInheritence();
		OverloadingBase();
		
	}
	
	public static void CirclurInheritence() {
		
		for(String s : Table.keySet()) {
			Set<String> set = new HashSet<>();
			set.add(s);
			SymbolTable t = Table.get(s);
			
			while(t.Extend != null){
				if(set.contains(t.Extend)) {
					System.out.println("Type error");  //TODO change
			    	System.exit(0);
				}
				set.add(t.Extend);
				t = Table.get(t.Extend);
			}
			
		}
		
	}
	
	public static void OverloadingBase() {
		// for each class
		for(String c : Table.keySet()) {
			
			SymbolTable  t = Table.get(c);
			if(t.Extend!=null) {
				SymbolTable ext = Table.get(t.Extend);
				//for each method
				for(String m : t.MethodDecl.keySet()) {
					MethodD  meth = t.MethodDecl.get(m);
					//recursive check
					methodCheck(meth,ext);
				}
			}
			
			
		}
	}
	
	public static void methodCheck(MethodD method,SymbolTable Extnd) {
		
		if(Extnd.MethodDecl.containsKey(method.name)) {
			
			MethodD m2 = Extnd.MethodDecl.get(method.name);
			
			if(m2.arg.size()!=method.arg.size()) {
				System.out.println("Type error");  //TODO change
		    	System.exit(0);
			}
			else {
				
				 for(int i=0;i<m2.arg.size();i++) {
					 //TODO can be class subtype !!!
					 if(m2.arg.get(i).compareTo(method.arg.get(i))!=0) {
						 System.out.println("Type error"); 
						 System.exit(0);
					 }
				 }
				 
				 if(m2.returnType.compareTo(method.returnType)!=0) {
					 System.out.println("Type error"); 
					 System.exit(0);
				 }
				
			}
			
			
		}
		
		
		if(Extnd.Extend!=null ) {
			SymbolTable ex = Table.get(Extnd.Extend);
			methodCheck(method,ex);
		}
	
	}

}
