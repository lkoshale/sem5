import java.util.Hashtable;

import syntaxtree.*;
import visitor.*;

public class P2 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();

	 //SymbolTable filling
         GJVoidDepthFirst<Arg> createTable = new GJVoidDepthFirst<Arg>();
         Arg a = new Arg();
         root.accept(createTable,a);
         Hashtable<String,SymbolTable>Table = new  Hashtable<String,SymbolTable>();
         Table = createTable.getTable();
      	
      	 //checks on symbol table for circular inheritence ...
         CheckSymbolTable.Table = Table;
         CheckSymbolTable.check();
         
         //type checking
         GJDepthFirst<TYPE,Car> Parse = new GJDepthFirst<>();
         Parse.setTable(Table);
         Car c = new Car();
         root.accept(Parse,c);

         
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}
