import java.util.HashMap;

import syntaxtree.*;
import visitor.*;

public class Main {
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
         
         
//         for(Graph g : GJVoidDepthFirst.CFG.values() ) {
//        	 System.out.println(  g.methodName + "  " + g.node.size() );
//        	 for(GNode gn : Analysis.sorted(g.node) ){
//        		 System.out.println( gn.stmtNo );
//        	 }
//        	 
//        	 System.out.println();
//         }
         
        
//         for(Interval I : Analysis.live.values()  ) {
//        	 
//        	 System.out.println(  I.mName  );
//        	
//        	 for(String st : I.liveMap.keySet() ){
//        		 Range r = I.liveMap.get(st);
//        		 System.out.println( st+" "+r.start + " "+r.end );
//        	 }
//        	 
//        	 System.out.println();
//         }
//      
//         
//         for( String st :GJVoidDepthFirst.CFG.keySet() ) {
//        	 
//        	 System.out.println(st +" "+ (GJVoidDepthFirst.CFG.get(st)).argCount );
//        	 HashMap<String,String> h = Analysis.Reg.get(st);
//        	 
//        	 for(String t : h.keySet()) {
//        		 System.out.println( t +"  " +h.get(t) );
//        	 }
//        	 System.out.println();
//         }
//         
//         
//         
//         for( String st :GJVoidDepthFirst.CFG.keySet() ) {
//        	 
//        	 System.out.println(st +" "+ (GJVoidDepthFirst.CFG.get(st)).usedScount );
//        	 HashMap<String,Integer> h = Analysis.SPILL.get(st);
//        	 
//        	 for(String t : h.keySet()) {
//        		 System.out.println( t +"  " +h.get(t) );
//        	 }
//        	 System.out.println();
//         }
//         
         
         
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



