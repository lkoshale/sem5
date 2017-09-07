import syntaxtree.*;
import visitor.*;

public class Main {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         System.out.println("Program parsed successfully");
         root.accept(new GJNoArguDepthFirst()); // Your assignment part is invoked here.
         System.out.println();
      
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}
