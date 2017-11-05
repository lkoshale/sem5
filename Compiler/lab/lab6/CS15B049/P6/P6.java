import syntaxtree.*;
import visitor.*;

public class P6 {
   public static void main(String [] args) {
      try {
         Node root = new MiniRAParser(System.in).Goal();
         
         root.accept(new GJNoArguDepthFirst()); // Your assignment part is invoked here.
         
         
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



