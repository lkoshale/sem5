import java.util.Hashtable;

import syntaxtree.*;
import visitor.*;



public class Main {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         
         GJVoidDepthFirst<Arg> createTable = new GJVoidDepthFirst<Arg>();
         Arg a = new Arg();
         root.accept(createTable,a);
         Hashtable<String,SymbolTable>Table = new  Hashtable<String,SymbolTable>();
         Table = createTable.getTable();
         
         
         root.accept(new GJNoArguDepthFirst<Object>()); 
         
         //call Exten Algo
         
         //TODO generate the file
      //   GJDepthFirst return String
         
         GJDepthFirst<String,Pass> obj = new GJDepthFirst<>();
         obj.setTable();
         obj.setSTable(Table);
         root.accept(obj,null);
         
       //  System.out.println("Program parsed successfully "+GJNoArguDepthFirst.Table.size());
         
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



