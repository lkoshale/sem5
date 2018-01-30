//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class GJDepthFirst<R,A> implements GJVisitor<R,A> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
	
	private int tempNo = 35;
	
	// 11----> TEMP 0
	public HashMap<String,String> TempMap = new HashMap<>();
	
	public String genTemp() {
		
		return "TEMP "+String.valueOf(tempNo++);
	
	}

	
	
	List<String> Arg = new ArrayList<>();
	
	
   public R visit(NodeList n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n, A argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n, A argu) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println("MAIN");
      
      Pass p = new Pass(-1);
      n.f1.accept(this, (A)p);
      
      n.f2.accept(this, argu);
     
      System.out.println(" END");
      
      n.f3.accept(this, argu);
      
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      
      //TODO bug
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public R visit(Procedure n, A argu) {
      R _ret=null;
      
      
      Pass tp = new Pass(-1);
      
      n.f0.accept(this, (A)tp);
      
      n.f1.accept(this, argu);
      String IN = (String) n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      
      System.out.println(" [ "+IN+" ] ");
      
      System.out.println(" BEGIN ");
      
      int argC = Integer.parseInt(n.f2.f0.tokenImage);
      Pass p = new Pass(argC);
     
      this.TempMap = new HashMap<>();
      
      String stexp = (String) n.f4.accept(this, (A)p );
      
      System.out.println(" RETURN \n"+stexp+"\n END ");
      
      return _ret;
   
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public R visit(Stmt n, A argu) {
      R _ret=null;
      //from stmt list
      
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println(" NOOP ");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println(" ERROR ");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Exp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
     String exp = (String) n.f1.accept(this, argu);
     System.out.println(" CJUMP "+exp+" ");
     
     n.f2.accept(this, argu);
    
     System.out.println();
     
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println(" JUMP ");
      n.f1.accept(this, argu);
      
     System.out.println();
      
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Exp()
    * f2 -> IntegerLiteral()
    * f3 -> Exp()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
     String ex1 = (String) n.f1.accept(this, argu);
      n.f2.accept(this, argu);
     String ex2 = (String) n.f3.accept(this, argu);
     
     System.out.println(" HSTORE "+ex1+ " "+n.f2.f0.tokenImage+ " "+ ex2+" ");
     
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Exp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String temp = (String) n.f1.accept(this, argu);
      String ex = (String) n.f2.accept(this, argu);
      String IN =(String) n.f3.accept(this, argu);
      
      
      System.out.println(" HLOAD "+temp+" "+ex+" "+IN);
      
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String temp = (String)n.f1.accept(this, argu);
      String ex = (String) n.f2.accept(this, argu);
      
      System.out.println(" MOVE "+temp+ " "+ex+" ");
      
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> Exp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
     String ex = (String) n.f1.accept(this, argu);
     
     System.out.println(" PRINT "+ex);
     
     return _ret;
   }

   /**
    * f0 -> StmtExp()
    *       | Call()
    *       | HAllocate()
    *       | BinOp()
    *       | Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(Exp n, A argu) {
      R _ret=null;
     
      
      Pass p = (Pass)argu;
      
      boolean cll = p.fromCall;
      
      p.fromExp = true;
     
      p.fromCall = false;
  
      String rt = (String) n.f0.accept(this, (A)p);
      
      p.fromCall = cll;
      
      String out = rt;
      
      if(n.f0.which==4) {
    	  out = rt;
      }
      else if(n.f0.which==5) {
    	  String t = this.genTemp();
    	  System.out.println(" MOVE "+t+" "+rt+" ");
    	  out = t;
      }
      else if(n.f0.which==6) {
    	  String t = this.genTemp();
    	  System.out.println(" MOVE "+t+" "+rt+" ");
    	  out = t;
      }
      
      
      //TODO bug exp -> exp internally
      if(cll) {
    	  
    	  this.Arg.add(out);
      
      }
      
      p.fromExp = false;
      
      return (R)out;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> Exp()
    * f4 -> "END"
    */
   public R visit(StmtExp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      
      n.f1.accept(this, argu);
      
      n.f2.accept(this, argu);
      
      String ex = (String) n.f3.accept(this, argu);
      
      n.f4.accept(this, argu);
      
      String temp = this.genTemp();
      
      System.out.println(" MOVE "+temp+" "+ex);
      
      return (R)temp;
  
   }

   /**
    * f0 -> "CALL"
    * f1 -> Exp()
    * f2 -> "("
    * f3 -> ( Exp() )*
    * f4 -> ")"
    */
   
   public R visit(Call n, A argu) {
      
	  R _ret=null;
      
	  n.f0.accept(this, argu);
      
      String ex = (String) n.f1.accept(this, argu);
      
      n.f2.accept(this, argu);
      
      List<String> t = new ArrayList(Arg);
      Arg.clear();
      
      
      Pass p = (Pass)argu;
      p.fromCall = true;
     
      n.f3.accept(this, (A)p);
      
      n.f4.accept(this, argu);
      
      p.fromCall = false;
      
      
      String out = " CALL "+ex+" ( ";
      
      for(int i=0;i<Arg.size();i++) {
    	  out+= Arg.get(i)+ " ";
      }
      
      out+= " ) ";
      
      
      String temp = this.genTemp();
      
      System.out.println(" MOVE "+temp+" " +out );
      
      
      //restore val
      Arg = t;
      
      return (R)temp;
      
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> Exp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String ex = (String)n.f1.accept(this, argu);
      
      String t = this.genTemp();
      
      System.out.print("MOVE "+ t +" HALLOCATE "+ex+"\n");
     
      
      return (R)t;
   
   }

   /**
    * f0 -> Operator()
    * f1 -> Exp()
    * f2 -> Exp()
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      String op = (String) n.f0.accept(this, argu);
      String ex1 = (String) n.f1.accept(this, argu);
      String ex2 = (String) n.f2.accept(this, argu);
      
      String t = this.genTemp();
      
      System.out.print(" MOVE "+t+" "+op+" "+ex1+" "+ex2+"\n" );
      
      return (R)t;
   
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      
      switch(n.f0.which) {
	      case 0 : return (R)"LE";
	      case 1 : return (R)"NE";
	      case 2 : return (R)"PLUS";
	      case 3 : return (R)"MINUS";
	      case 4 : return (R)"TIMES";
	      case 5 : return (R)"DIV";
      }
      
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      
      Pass p = (Pass)argu;
      int val = Integer.parseInt(n.f1.f0.tokenImage);
      
      if( p.argCount >= 0 && val<= p.argCount ) {
    	  return (R)("TEMP "+n.f1.f0.tokenImage);
      }
      else if(TempMap.containsKey(n.f1.f0.tokenImage)) {
    	return (R)TempMap.get(n.f1.f0.tokenImage);  
      }
      else {
    	  String newT = this.genTemp();
    	  TempMap.put(n.f1.f0.tokenImage, newT);
    	  return (R)newT;
      }
      

   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
    
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      
      Pass p = (Pass)argu;
      
      if(p.fromExp==false) {
    	  System.out.print(" "+n.f0.tokenImage+" ");
      }
      
      return (R)(n.f0.tokenImage);
   }

}