import java.util.Hashtable;

import syntaxtree.*;
import visitor.*;

public class Main {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
//         GJNoArguDepthFirst ob = new GJNoArguDepthFirst<String>();
//         root.accept(ob);
         
         GJVoidDepthFirst<Arg> createTable = new GJVoidDepthFirst<Arg>();
         Arg a = new Arg();
         root.accept(createTable,a);
         Hashtable<String,SymbolTable>Table = new  Hashtable<String,SymbolTable>();
         Table = createTable.getTable();
         
         GJDepthFirst<TYPE,Car> Parse = new GJDepthFirst<>();
         Parse.setTable(Table);
         Car c = new Car();
         root.accept(Parse,c);
         
         System.out.println("Program parsed successfully");
         
         for(String s : Table.keySet()) {
        	 System.out.println(s.toString());
         }
          
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}
