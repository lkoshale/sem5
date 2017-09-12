package visitor;

import java.util.*;

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
		 this.FieldVar.put(name, Type);
	 }
	 
	 String FieldVarTyp(String name) {
		 if(this.FieldVar.containsKey(name)) {
			 return this.FieldVar.get(name);
		 }
		 return null;
	 }
	 
	 MethodD getMethodTable(String MethodName) {
		
		 if(MethodDecl.containsKey(MethodName)) {
			 return MethodDecl.get(MethodName);
		 }
		 
		 return null;
	 }
	 
	 void AddMethod(String MethodName,String returnTy) {
		 MethodD object = new MethodD(this.ClassName,MethodName,returnTy);
		 MethodDecl.put(MethodName, object);
		 System.out.println(this.ClassName+" :: "+MethodName+", return "+returnTy);
		 
	 }
	 
	 
	 void AddVar(String scope,String name,String typ) {
		 int k = scope.indexOf('$');
		// System.out.println("in addvar fn "+scope);
		 if(k!=-1) {
			 String cls = scope.substring(0,k);
			 String var = scope.substring(k+1);
		//	 System.out.println(cls+"::"+var+" : "+name+" ,"+typ);
			 MethodD m = getMethodTable(var);
			 System.out.println("in ADD var "+scope);
			 if(m!=null) {
				 m.AddTempVar(name,typ);
				 System.out.println(scope+" :: "+name+" "+m.TempVar.get(name));
			 }
			 
			 
		 }
		 else {
			 if(scope.length()>0) {
				
				 this.AddFeild(name,typ);
				System.out.println(scope+"::"+name+" "+FieldVar.get(name));
			 }
		 }
	 }
	 
	 
	 void addParmtoMethod(String Mname,String typ,String name) {
		 MethodD m = this.getMethodTable(Mname);

		 if(m!=null) {
			 m.Addarg(typ);
			 m.AddTempVar(name,typ);
			 System.out.println(Mname+" param: "+name+" "+typ);
		 }
	 }
	 
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