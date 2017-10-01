import syntaxtree.*;
import visitor.*;



public class Main {
   public static void main(String [] args) {
      try {
         Node root = new MiniIRParser(System.in).Goal();
         
         root.accept(new GJNoArguDepthFirst<Object>()); 

	System.out.println("Program parsed successfully ");
         
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



