import java.util.HashMap;

import syntaxtree.*;
import visitor.*;

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         GJVoidDepthFirst<Graph> obj  = new GJVoidDepthFirst();
         root.accept(obj,null); // Your assignment part is invoked here.
         
         
         GJNoArguDepthFirst<Object> gp = new  GJNoArguDepthFirst<>();
         root.accept(gp);
         
         Analysis.liveAnalysis();
         Analysis.buildInterval();
         Analysis.RegAlloc();
         Analysis.updateStack();
         Analysis.globalLabel();
         
         GJDepthFirst<String,String> DF = new  GJDepthFirst<>();
         
        root.accept(DF,null);
          
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 




