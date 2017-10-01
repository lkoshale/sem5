import java.util.Hashtable;

import syntaxtree.*;
import visitor.*;


public class P3 {
	
	public static void main(String [] args) {
	      try {
	         Node root = new MiniJavaParser(System.in).Goal();
	         
	         GJVoidDepthFirst<Arg> createTable = new GJVoidDepthFirst<Arg>();
	         Arg a = new Arg();
	         root.accept(createTable,a);
	         Hashtable<String,SymbolTable>Table = new  Hashtable<String,SymbolTable>();
	         Table = createTable.getTable();
	         
	         
	         root.accept(new GJNoArguDepthFirst<Object>()); 
	         
	         //recur for the extend
	         AddParents.setTable();
	         AddParents.Add();
	         
	         
	         GJDepthFirst<String,Pass> obj = new GJDepthFirst<>();
	         obj.setTable();
	         obj.setSTable(Table);
	         root.accept(obj,null);
	         
	         
	      }
	      catch (ParseException e) {
	         System.out.println(e.toString());
	      }
	   }

}
