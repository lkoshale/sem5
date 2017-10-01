package visitor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SymbolTable {
	String ClassName = null; 
	 Hashtable<String,String>FieldVar= new  Hashtable<String,String>();
	 Hashtable<String,MethodD> MethodDecl= new  Hashtable<String,MethodD>();
	 String Extend = null;
	
	 
	 public SymbolTable() {}
	 
	 public SymbolTable(String str) {
		 this.ClassName = str;
	 }
	 
	 public SymbolTable(String str,String ext) {
		 this.ClassName = str;
		 this.Extend = ext;
	 }
	 
	 void setClassName(String str) {
		 this.ClassName = str;
	 }
	  
	 void AddFeild(String name,String Type) {
	 	 
	 	 if(this.FieldVar.containsKey(name)) {
			 System.out.println("Type error");//+ "dup f var"); //TODO change
			 System.exit(0);
		 }
	 
		 this.FieldVar.put(name, Type);
	 }
	 
	 String FieldVarTyp(String name) {
		 if(this.FieldVar.containsKey(name)) {
			 return this.FieldVar.get(name);
		 }
		 return null;
	 }
	 
	 public MethodD getMethodTable(String MethodName) {
		
		 if(MethodDecl.containsKey(MethodName)) {
			 return MethodDecl.get(MethodName);
		 }
		 
		 return null;
	 }
	 
	 void AddMethod(String MethodName,String returnTy) {
		 MethodD object = new MethodD(this.ClassName,MethodName,returnTy);
		 MethodDecl.put(MethodName, object);
		// System.out.println(this.ClassName+" :: "+MethodName+", return "+returnTy);
		 
	 }
	 
	 
	 void AddVar(String scope,String name,String typ) {
		 int k = scope.indexOf('$');
		// System.out.println("in addvar fn "+scope);
		 if(k!=-1) {
			 String cls = scope.substring(0,k);
			 String var = scope.substring(k+1);
		//	 System.out.println(cls+"::"+var+" : "+name+" ,"+typ);
			 MethodD m = getMethodTable(var);
			 if(m!=null) {
				 m.AddTempVar(name,typ);
				// System.out.println(scope+" :: "+name+" "+m.TempVar.get(name));
			 }
			 
			 
		 }
		 else {
			 if(scope.length()>0) {
				
				 this.AddFeild(name,typ);
				//System.out.println(scope+"::"+name+" "+FieldVar.get(name));
			 }
		 }
	 }
	 
	 
	 void addParmtoMethod(String Mname,String typ,String name) {
		 MethodD m = this.getMethodTable(Mname);

		 if(m!=null) {
			 m.Addarg(typ);
			 m.AddTempVar(name,typ);
			 //System.out.println(Mname+" param: "+name+" "+typ);
		 }
	 }
	 
	 
	 public static String LookupVar(Hashtable<String,SymbolTable> Table,String scope,String name) {
		 
		 String ans = null;
		 
		 int k = scope.indexOf('$');
			// System.out.println("in addvar fn "+scope);
		 
			 if(k!=-1) {
				 String cls = scope.substring(0,k);
				 String var = scope.substring(k+1);
				 
				 SymbolTable sym = Table.get(cls);	 
				 MethodD m = sym.getMethodTable(var);
				 
				 if(m!=null) {
					 if(m.TempVar.containsKey(name)) {
						 return m.TempVar.get(name);
					 }
					 else {
						 return LookupVar(Table,cls, name);
					 }
				 }
				 
				 
			 }
			 else {
				 
				 String cls = scope;
				 SymbolTable sym = Table.get(cls);	 
				 
				 if(scope.length()>0) {
					if(sym.FieldVar.containsKey(name)) {
						return sym.FieldVar.get(name);
					}
				
//					if(this.MethodDecl.containsKey(name)) {
//						MethodD mth = this.MethodDecl.get(name);
//						return mth.returnType;
//					}
//					
					if(sym.Extend != null) {
						if(Table.containsKey(sym.Extend)) {
							return LookupVar(Table,sym.Extend, name);
						}
					}
					
				 }
				 
				 return null;
			 }
		 
		 
		 return ans;
	 }
	 
	 /*
	 public String LookupMethod(Hashtable<String,SymbolTable> Table,List<String>mL,String classN,String Mname) {
		 String ans = null;
		 
		 if(classN==null) {
			 
		 }
		 
		 if(Table.containsKey(classN)) {
			 SymbolTable sym = Table.get(classN);
			 
			 if(sym.MethodDecl.containsKey(Mname)) {
				 
				 MethodD mth = sym.MethodDecl.get(Mname);
				 
				 if(mL.size()!=mth.arg.size()) {
					 
					 System.out.println("Type error");//+ " arg size"+mL+" "+mth.arg); //TODO change
					 System.exit(0);
					 return null;
				 }
				 
				 for(int i=0;i<mL.size();i++) {
					 //TODO can be class subtype !!!
					 TYPE mL1 = new TYPE(mL.get(i));
					 TYPE mth1 = new TYPE(mth.arg.get(i));
					 if(!mth1.Match(mL1)) {
						 
						 System.out.println("Type error"); //TODO change
						 System.exit(0);
						 return null;
					 }
				 }
				 
				 return mth.returnType;
				 
			 }
			 else if(sym.Extend!=null) {
				 return LookupMethod(Table,mL,sym.Extend,Mname);
			 }
		 }
		 
		 
		 return ans;
	 }*/
	 
}



class MethodD{
	String ClassName = null ;
	String name = null ;
	String returnType = null ;
	List<String> arg = new ArrayList();
	Hashtable<String,String>TempVar = new  Hashtable<String,String>();
	
	

	public MethodD(String classN,String name) {
		this.ClassName = classN;
		this.name = name;
	}
	
	public MethodD(String classN,String name,String rt) {
		this.ClassName = classN;
		this.name = name;
		this.returnType = rt;
	}
	
	void setReturn(String returnT) {
		this.returnType = returnT;
	}

	void setClassName(String str) {
		this.ClassName = str;
	}
	
	void setName(String str) {
		this.name = str;
	}

	void returnType(String str) {
		this.returnType = str;
	}
	
	void Addarg(String type) {
		this.arg.add(type);
	}
	
	void AddTempVar(String name,String Type) {
		if(TempVar.containsKey(name)) {
			System.out.println("Type error");//+" duplicate temvar"); //TODO change
			System.exit(0);
		}
		this.TempVar.put(name, Type);
	}
	
	String getTempType(String name) {
		if(TempVar.containsKey(name)) {
			String str = TempVar.get(name);
			return str;
		}
		
		return null;
	}
	
}