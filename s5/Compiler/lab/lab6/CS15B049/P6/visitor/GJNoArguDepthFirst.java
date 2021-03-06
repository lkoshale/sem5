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
public class GJNoArguDepthFirst<R> implements GJNoArguVisitor<R> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return null;
   }

   public R visit(NodeSequence n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n) { return null; }

   //
   // User-generated visitor methods below
   //
   
   

   /**
    * f0 -> "MAIN"
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( SpillInfo() )?
    * f13 -> ( Procedure() )*
    * f14 -> <EOF>
    */
   
 
   int exArgs = 0;
   
   public R visit(Goal n) {
      R _ret=null;
      
      System.out.println("\t  .text\n"+"\t   .globl           main");
      System.out.println("main:");
      
      System.out.println(" move $fp, $sp");
     
      
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      
      int numArgs = Integer.parseInt(n.f2.f0.tokenImage);
      
      if(numArgs>4) {
    	  this.exArgs = numArgs-4;
      }
      else {
    	  this.exArgs = 0;
      }
      
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      
      int stck = Integer.parseInt(n.f5.f0.tokenImage);
      
      int maxArgs = Integer.parseInt(n.f8.f0.tokenImage);
      
      if(maxArgs>4) {
    	  maxArgs = maxArgs-4;
      }
      else {
    	  maxArgs = 0;
      }
      
      System.out.println(" subu $sp, $sp, "+String.valueOf(4*(maxArgs+stck+1)));
      System.out.println(" sw $ra, -4($fp)");
      
      n.f10.accept(this);
      n.f11.accept(this);
      
      System.out.println(" lw $ra, -4($fp)\n" + 
      		" addu $sp, $sp, " +String.valueOf(4*(stck+1))+"\n"+ 
      		" jr $ra\n" + 
      		"");
      
      this.exArgs = 0;
      
      n.f12.accept(this);
      
      
      n.f13.accept(this);
      n.f14.accept(this);
      System.out.println(this.enddef);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   
   String lblRT = null;
   public R visit(StmtList n) {
      R _ret=null;
      this.lblRT = null;
      n.f0.accept(this);
      this.lblRT = null;
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( SpillInfo() )?
    */
   
   
   
   public R visit(Procedure n) {
      R _ret=null;
      
      System.out.print("\t  .text\n"+"\t   .globl\t");
    
      
      n.f0.accept(this);
      System.out.println(n.f0.f0.tokenImage);
      System.out.println(n.f0.f0.tokenImage+":");
      
      
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      
      int numArgs = Integer.parseInt(n.f2.f0.tokenImage);
      
      if(numArgs>4) {
    	  this.exArgs = numArgs-4;
      }
      else {
    	  this.exArgs = 0;
      }
      
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      
      int stck = Integer.parseInt(n.f5.f0.tokenImage);
      
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      
      int maxArgs = Integer.parseInt(n.f8.f0.tokenImage);
      
      if(maxArgs>4) {
    	  maxArgs = maxArgs-4;
      }
      else {
    	  maxArgs = 0;
      }
      
      
      System.out.println("sw $fp, -8($sp)\n" + 
      		"move $fp, $sp");
      
      System.out.println("subu $sp, $sp, "+String.valueOf(4*(maxArgs+stck+2)));
      System.out.println("sw $ra, -4($fp)");
      
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      
      System.out.println("lw $ra, -4($fp)");
      System.out.println("lw $fp, "+ String.valueOf(4*(maxArgs+stck))+"($sp)");
      
      System.out.println("addu $sp, $sp, "+String.valueOf(4*(maxArgs+stck+2)));
      System.out.println("jr $ra");
      
      this.exArgs = 0;
      
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
    *       | ALoadStmt()
    *       | AStoreStmt()
    *       | PassArgStmt()
    *       | CallStmt()
    */
   public R visit(Stmt n) {
      R _ret=null;
      
      if(this.lblRT!=null) {
    	  System.out.println(" "+this.lblRT+":");
      }
  
      n.f0.accept(this);
      this.lblRT = null;
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.println("nop");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.println("li $v0, 4\n" + 
      		"la $a0, str_er\n" + 
      		"syscall\n" + 
      		"li $v0, 10\n" + 
      		"syscall");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Reg()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String r = (String)n.f1.accept(this);
      n.f2.accept(this);
      
      System.out.println(" beqz "+r+", "+n.f2.f0.tokenImage);
      
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      
      System.out.println(" b "+n.f1.f0.tokenImage);
      
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Reg()
    * f2 -> IntegerLiteral()
    * f3 -> Reg()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String r1 = (String)n.f1.accept(this);
      n.f2.accept(this);
      String r2 = (String)n.f3.accept(this);
      
      System.out.println(" sw "+r2+", "+n.f2.f0.tokenImage+"("+r1+")");
      
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Reg()
    * f2 -> Reg()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String r1 = (String) n.f1.accept(this);
      String r2 = (String) n.f2.accept(this);
      n.f3.accept(this);
      
      System.out.println(" lw "+r1+", "+n.f3.f0.tokenImage+"("+r2+")");
      
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Reg()
    * f2 -> Exp()
    */
   
   
   String forExp = null;
   
   public R visit(MoveStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String r = (String)n.f1.accept(this);
      
      this.forExp = r;
      
      n.f2.accept(this);
      
      this.forExp =null;
      return _ret;
      
      
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String smExp = (String)n.f1.accept(this);
      
      char ch = smExp.charAt(0);
      String rest = smExp.substring(smExp.indexOf(" ")+1);
      
      switch(ch) {
      case '0' : System.out.println(" move $a0,"+rest);
      				break;
      case '1' : System.out.println(" li $a0, "+rest);
      				break;
      case '2' : System.out.println(" la $a0, "+rest);
      				break;
      }
    
      System.out.println("jal _print");
        
      return _ret;
   }

   /**
    * f0 -> "ALOAD"
    * f1 -> Reg()
    * f2 -> SpilledArg()
    */
   public R visit(ALoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
     String r = (String) n.f1.accept(this);
      n.f2.accept(this);
      
      
      int num = Integer.parseInt(n.f2.f1.f0.tokenImage);
      
      if(num < this.exArgs) {
    	  	System.out.println(" lw "+r+", "+String.valueOf(4*num)+"($fp)");
      }
      else {
    	  String off = String.valueOf( 4*(num) );
          
      	  System.out.println(" lw "+r+", "+off+"($sp)");
      }
      
      return _ret;
   }

   /**
    * f0 -> "ASTORE"
    * f1 -> SpilledArg()
    * f2 -> Reg()
    */
   public R visit(AStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
     String r = (String) n.f2.accept(this);
      
     int num = Integer.parseInt(n.f1.f1.f0.tokenImage);
     
     if(num < this.exArgs) {
 	  	System.out.println(" sw "+r+", "+String.valueOf(4*num)+"($fp)");
     }else {
	      String off = String.valueOf( 4*(num) );
	      
	      System.out.println(" sw "+r+", "+off+"($sp)");
	 }
     
      return _ret;
   }

   /**
    * f0 -> "PASSARG"
    * f1 -> IntegerLiteral()
    * f2 -> Reg()
    */
   public R visit(PassArgStmt n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String r = (String)n.f2.accept(this);
      
      System.out.println(" sw "+r+", "+String.valueOf(4*((Integer.parseInt(n.f1.f0.tokenImage)-1)))+"($sp)");
      
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    */
   public R visit(CallStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String smExp = (String)n.f1.accept(this);
      
      char ch = smExp.charAt(0);
      String rest = smExp.substring(smExp.indexOf(" ")+1);
      
      switch(ch) {
      case '0' : System.out.println(" jalr "+rest);
      				break;
      case '1' : return _ret;
      case '2' : System.out.println(" jal  "+rest);
      			    break;
      }
      
      return _ret;
   }

   /**
    * f0 -> HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   
   boolean fromExp = false;
   public R visit(Exp n) {
      R _ret=null;
      fromExp = true;
      n.f0.accept(this);
      fromExp = false;
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      fromExp = false;
      n.f0.accept(this);
      String smExp = (String)n.f1.accept(this);
      
      
      char ch = smExp.charAt(0);
      String rest = smExp.substring(smExp.indexOf(" ")+1);
      
      switch(ch) {
      case '0' : System.out.println(" move $a0,"+rest);
      			break;
      case '1' : System.out.println(" li $a0, "+rest);
      			break;
      case '2' : System.out.println(" la $a0, "+rest);
      			break;
      }
      
      System.out.println("jal _halloc");
      System.out.println("move "+ forExp + ", $v0");
      
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Reg()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n) {
      R _ret=null;
      
      fromExp = false;
      
     String op = (String) n.f0.accept(this);
     String r1 = (String) n.f1.accept(this);
     String smExp = (String) n.f2.accept(this);
      
      int OP = Integer.parseInt(op);
      char ch = smExp.charAt(0);
      int sm = ch - '0';
      String rest = smExp.substring(smExp.indexOf(" ")+1);
      
      switch(OP) {
      
      case 0: if(sm==0 || sm==1) {
    	  			System.out.println(" sle "+this.forExp+", "+r1+", "+rest);
      			}
      		break;
      case 1:if(sm==0 || sm==1) {
					System.out.println(" sne "+this.forExp+", "+r1+", "+rest);
				}
			break;
      case 2: if(sm==0) {
    	  				System.out.println(" add "+this.forExp+", "+r1+", "+rest);
      			}
      		else if(sm==1) { System.out.println(" addi "+this.forExp+", "+r1+", "+rest); }
      			break;
      			
      case 3: if(sm==0 || sm==1) {
							System.out.println(" sub "+this.forExp+", "+r1+", "+rest);
				}
					break;
      case 4: if(sm==0 || sm==1) {
					System.out.println(" mul "+this.forExp+", "+r1+", "+rest);
      		   }
      			break; 
      case 5: if(sm==0 || sm==1) {
					System.out.println(" div "+this.forExp+" , "+r1+", "+rest);
				}
					break;
      }
      
      
      
      return _ret;
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n) {
      R _ret=null;
      n.f0.accept(this);
      
      switch(n.f0.which) {
      case 0: return (R)"0";
      case 1: return (R)"1";
      case 2: return (R)"2";
      case 3: return (R)"3";
      case 4: return (R)"4";
      case 5: return (R)"5";
      }
      
      return _ret;
   }

   /**
    * f0 -> "SPILLEDARG"
    * f1 -> IntegerLiteral()
    */
   public R visit(SpilledArg n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Reg()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      String str =(String)n.f0.accept(this);
      
      String out = "";
      
      switch(n.f0.which) {
      case 0: out+="0 "+str;
      		break;
      case 1: out+="1 "+str;
      		break;
      case 2: out+="2 "+str; 
      		break;
      }
      
      if(fromExp) {
    	  switch(n.f0.which) {
          case 0: System.out.println(" move "+forExp+", "+str);
          			break;
          case 1: System.out.println(" li "+forExp+", "+str);
				break;
          case 2: System.out.println(" la "+forExp+", "+str);
				break;
          }
      }
      
      
      return (R)out;
   }

   /**
    * f0 -> "a0"
    *       | "a1"
    *       | "a2"
    *       | "a3"
    *       | "t0"
    *       | "t1"
    *       | "t2"
    *       | "t3"
    *       | "t4"
    *       | "t5"
    *       | "t6"
    *       | "t7"
    *       | "s0"
    *       | "s1"
    *       | "s2"
    *       | "s3"
    *       | "s4"
    *       | "s5"
    *       | "s6"
    *       | "s7"
    *       | "t8"
    *       | "t9"
    *       | "v0"
    *       | "v1"
    */
   public R visit(Reg n) {
      R _ret=null;
      n.f0.accept(this);
      
      switch(n.f0.which) {
      case 0: return (R)"$a0";
      case 1: return (R)"$a1";
      case 2: return (R)"$a2";
      case 3: return (R)"$a3";
      case 4: return (R)"$t0";
      case 5: return (R)"$t1";
      case 6: return (R)"$t2";
      case 7: return (R)"$t3";
      case 8: return (R)"$t4";
      case 9: return (R)"$t5";
      case 10: return (R)"$t6";
      case 11: return (R)"$t7";
      case 12: return (R)"$s0";
      case 13: return (R)"$s1";
      case 14: return (R)"$s2";
      case 15: return (R)"$s3";
      case 16: return (R)"$s4";
      case 17: return (R)"$s5";
      case 18: return (R)"$s6";
      case 19: return (R)"$s7";
      case 20: return (R)"$t8";
      case 21: return (R)"$t9";
      case 22: return (R)"$v0";
      case 23: return (R)"$v1";
      }
      
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;
      n.f0.accept(this);
      
      this.lblRT = n.f0.tokenImage;
      
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> "//"
    * f1 -> SpillStatus()
    */
   public R visit(SpillInfo n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> <SPILLED>
    *       | <NOTSPILLED>
    */
   public R visit(SpillStatus n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }
   
   

   String enddef = ".text\n" + 
   		"         .globl _halloc\n" + 
   		"_halloc:\n" + 
   		"         li $v0, 9\n" + 
   		"         syscall\n" + 
   		"         j $ra\n" + 
   		"\n" + 
   		"         .text\n" + 
   		"         .globl _print\n" + 
   		"_print:\n" + 
   		"         li $v0, 1\n" + 
   		"         syscall\n" + 
   		"         la $a0, newl\n" + 
   		"         li $v0, 4\n" + 
   		"         syscall\n" + 
   		"         j $ra\n" + 
   		"\n" + 
   		"         .data\n" + 
   		"         .align   0\n" + 
   		"newl:    .asciiz \"\\n\" \n" + 
   		"         .data\n" + 
   		"         .align   0\n" + 
   		"str_er:  .asciiz \" ERROR: abnormal termination\\n\" ";
   

}
