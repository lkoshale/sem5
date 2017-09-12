import syntaxtree.*;
import visitor.*;

public class Main {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
//         GJNoArguDepthFirst ob = new GJNoArguDepthFirst<String>();
//         root.accept(ob);
         GJVoidDepthFirst<Arg> ob = new GJVoidDepthFirst<Arg>();
         Arg a = new Arg();
         root.accept(ob,a);
         
         System.out.println("Program parsed successfully");
          
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}
