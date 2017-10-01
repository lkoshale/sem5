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
	
	//TODO debug
	public boolean DBG = false;
	
	public int TempNo = 25;
	public int LabelNo = 0;
	
	
	public HashMap<String,Data> Table = new HashMap<>();
	
	public Hashtable<String,SymbolTable>STable = new  Hashtable<String,SymbolTable>();
	
	
	public void setSTable(Hashtable<String,SymbolTable>STable) {
		this.STable = STable;
	}
	
	public void setTable() {
		this.Table = GJNoArguDepthFirst.Table;
	}
	
	public String genTemp() {
		return "TEMP "+String.valueOf(TempNo++);
	}
	
	public String genLabel() {
		return "L"+String.valueOf(LabelNo++);
	}
	
	
	public String cName = null;
	public String mName = null;
	

	   public boolean msg = false;
	   public String msgTyp = null;
	   
	   public List<String> expList = new ArrayList();
	
	
	public String Lookup(String id,Data data,Method mth) {
		
		for(int i=0;i<mth.Arg.size();i++) {
			if( mth.Arg.get(i).compareTo(id)==0) {
				return "TEMP "+String.valueOf(i+1);  //Temp (1+)
			}
		}
		
		if(mth.Mvar.containsKey(id)) {
			return mth.Mvar.get(id);
		}
		
		//this is add of obj var
		if(data.VarMap.contains(mth.cName+"$"+id)) {
			int k = data.VarMap.indexOf(mth.cName+"$"+id);
			String rt = this.genTemp();
			String out ="BEGIN \n";
			out += "HLOAD "+rt+" TEMP 0 "+String.valueOf((k+1)*4);
			out+="\nRETURN \n"+rt+"\nEND\n";
			return out;
		}
		
		return null;
	}
	
	

	
	
	
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
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public R visit(MainClass n, A argu) {
      R _ret=null;
      
      System.out.println("MAIN");
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      n.f14.accept(this, argu);
      n.f15.accept(this, argu);
      n.f16.accept(this, argu);
      System.out.println("END");
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public R visit(ClassDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      cName = n.f1.f0.tokenImage;
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      cName = null;
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public R visit(ClassExtendsDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      cName = n.f1.f0.tokenImage;
      
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      
      cName = null;
      return _ret;
      
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      
     if(mName!=null) {
	      Pass p = (Pass)argu;
	      p.mth.Mvar.put(n.f1.f0.tokenImage,this.genTemp());
      }
      
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public R visit(MethodDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      
      mName = n.f2.f0.tokenImage;
      
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      
      Data data = Table.get(cName);
      Method mth = data.FnAr.get(cName+"_"+mName);
      
      Pass p = new Pass(data,mth);
      
      System.out.println(cName+"_"+mName+" [ "+String.valueOf(mth.Arg.size()+1)+" ] "+"\nBEGIN ");
      
      n.f7.accept(this, (A)p);
      
      n.f8.accept(this, (A)p);
      n.f9.accept(this, argu);
      
     String exp = (String) n.f10.accept(this,(A)p );
      
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      
      System.out.println("RETURN \n"+exp+"\nEND");
      mName = null;
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public R visit(FormalParameter n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public R visit(Statement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public R visit(Block n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String val = (String) n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      
      Pass p = (Pass)argu;
      Method mth = p.mth;
      Data data = p.data;
      
      String id = n.f0.f0.tokenImage;
      
      for(int i=0;i<mth.Arg.size();i++) {
			if( mth.Arg.get(i).compareTo(id)==0) {
				String t = "TEMP "+String.valueOf(i+1); 
				System.out.println("MOVE "+t+ " "+val+"\n");
				return _ret;
			}
		}
		
		if(mth.Mvar.containsKey(id)) {
			String t = mth.Mvar.get(id);
			System.out.println("MOVE "+t+ " "+val+"\n");
			return _ret;
		}
		
		if(data.VarMap.contains(mth.cName+"$"+id)) {
			int k = data.VarMap.indexOf(mth.cName+"$"+id);
			String out ="";
			out += "HSTORE "+" TEMP 0 "+String.valueOf((k+1)*4)+" "+val+"\n";
			System.out.print(out);
			return _ret;
		}
      

      
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public R visit(ArrayAssignmentStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String des = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String val = (String)n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      
      Pass p = (Pass)argu;
      String id = Lookup(n.f0.f0.tokenImage,p.data,p.mth);
      
      String t1 = this.genTemp();
      String t2 = this.genTemp();
      String len = this.genTemp();
      String lbl1 = this.genLabel();
      
      String out ="";
      
      out+= "MOVE "+t1+" TIMES "+des+" 4 \n";
      out+= "HLOAD "+len+" "+id+" 0\n";
      out+= "MOVE "+len+" TIMES "+len+" 4\n";
      
   		out+= "CJUMP MINUS  1 LE "+t1+ " MINUS "+len+" 1 "+lbl1+" \n ERROR\n";
   		out+= lbl1+" NOOP\n";
   	
      
      out+= "MOVE "+t2+" PLUS  PLUS "+t1+" 4 "+id+"\n";
      out+= "HSTORE "+ t2+ " 0 "+val+"\n";
   
      //TODO bound check
      
      System.out.print(out);
      return _ret;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public R visit(IfStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(IfthenStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String cond = (String)n.f2.accept(this, argu);
      	
      String L1 = this.genLabel();
      System.out.print(" CJUMP "+cond+" "+L1+"\n");
      n.f3.accept(this, argu);
      
      n.f4.accept(this, argu);
      System.out.print(L1+" NOOP\n" );
      
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public R visit(IfthenElseStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      
      n.f1.accept(this, argu);
      String cond = (String) n.f2.accept(this, argu);
      n.f3.accept(this, argu);
     
      String L2 = this.genLabel();
      String L3 = this.genLabel();
      
      System.out.print(" CJUMP "+cond+" "+L2+"\n");
      
      n.f4.accept(this, argu);
      System.out.print("JUMP "+L3+"\n"
    		  			+L2+" ");
      
      n.f5.accept(this, argu);
      
      n.f6.accept(this, argu);
      System.out.print(L3+" NOOP\n");
      
      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(WhileStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String cond = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      
      String lbl1 = this.genLabel();
      String lbl2 = this.genLabel();
      
      String out= lbl1+" CJUMP "+cond+" "+lbl2+"\n";
      System.out.println(out);
      
      n.f4.accept(this, argu);
      
      System.out.print("JUMP "+lbl1+"\n"+lbl2+" NOOP\n");
      
      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public R visit(PrintStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String str = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      System.out.print("PRINT "+str+"\n");
      return _ret;
   }

   /**
    * f0 -> OrExpression()
    *       | AndExpression()
    *       | CompareExpression()
    *       | neqExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | DivExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public R visit(Expression n, A argu) {
      R _ret=null;
      String str = (String)n.f0.accept(this, argu);
      return (R)str;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n, A argu) {
      R _ret=null;
      String op1 = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      String val = this.genTemp();
      String lbl1 = this.genLabel();
      String lbl2 = this.genLabel();
      String lbl3 = this.genLabel();
      
      String out ="BEGIN \n";
      
      out+= "MOVE "+ val+ " 0 \n";
        	out+= "CJUMP "+op1+" "+lbl1+"\n";
        	out+= "CJUMP "+op2+" "+lbl1+"\n";
        	out+= "JUMP "+lbl2+"\n";
        	out+= lbl1+" NOOP \n"+ "MOVE "+val+" 0\n"+" JUMP "+lbl3+"\n";
        	out+=lbl2+" NOOP \n "+"MOVE "+val+" 1\n";
        	out+=lbl3+" NOOP\n";
      out+="RETURN \n"+val+"\nEND\n";
      
      return (R)out;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public R visit(OrExpression n, A argu) {
      R _ret=null;
      String op1 = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      String val = this.genTemp();
      String lbl1 = this.genLabel();
      String lbl2 = this.genLabel();
      String lbl3 = this.genLabel();
      String lbl4 = this.genLabel();
      
      String out ="BEGIN \n";
      
      out+= "MOVE "+ val+ " 0\n";
        	out+= "CJUMP "+op1+" "+lbl3+"\n";
        	out+= "JUMP "+lbl2+"\n";
        	out+= lbl3+" CJUMP "+op2+" "+lbl1+"\n";
        	out+= "JUMP "+lbl2+"\n";
        	out+= lbl1+" NOOP\n "+ "MOVE "+val+" 0\n"+" JUMP "+lbl4+"\n";
        	out+= lbl2+" NOOP\n "+"MOVE "+val+" 1\n";
        	
        	out+= lbl4+ " NOOP\n";
        	
      out+="RETURN \n"+val+"\nEND\n";
      
      return (R)out;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n, A argu) {
      R _ret=null;
      String op1= (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      return (R)("LE "+op1+" "+op2);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public R visit(neqExpression n, A argu) {
      R _ret=null;
      String op1= (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      return (R)("NE "+op1+" "+op2);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n, A argu) {
      R _ret=null;
      String op1= (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      return (R)("PLUS "+op1+" "+op2);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n, A argu) {
      R _ret=null;
      String op1= (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      return (R)("MINUS "+op1+" "+op2);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n, A argu) {
      R _ret=null;
      String op1= (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      return (R)("TIMES "+op1+" "+op2);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public R visit(DivExpression n, A argu) {
      R _ret=null;
      String op1= (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String op2 = (String)n.f2.accept(this, argu);
      
      return (R)("DIV "+op1+" "+op2);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n, A argu) {
      R _ret=null;
      String arr = (String)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String off = (String)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      
      String rt = this.genTemp();
      String ctemp = this.genTemp();
      String len = this.genTemp();
      String lbl1 = this.genLabel();
      // TODO can store offset
      
      
//      String o = " BEGIN\n";
//      o+=  "HLOAD "+ rt+" PLUS "+arr+ " ";
//      o+=  " BEGIN \n MOVE "+ctemp+" TIMES "+off+" 4\n";
//      o+=  " MOVE "+ctemp+" PLUS "+ctemp+" 4\n";
//      o+= " RETURN \n"+ctemp+"\nEND\n";
//      o+= " 0 \n";
//      o+= "RETURN \n"+rt+"\nEND\n";
//      
      
	  String out ="BEGIN \n";
       out+= "HLOAD "+ rt+" PLUS "+arr+ " PLUS ";
		  
		  	out+= "\nBEGIN\n";
		  	out+="MOVE "+ctemp+" TIMES "+off+" 4\n";
		  	out+= "HLOAD "+len+" "+arr+" 0\n";
		  	out+= "MOVE "+len+" TIMES "+len+" 4\n";
		  	out+= "CJUMP MINUS  1 LE "+ctemp+ " MINUS "+len+" 1 "+lbl1+" \n ERROR\n";
		  	out+= lbl1+" NOOP\n";
		  	out+="RETURN \n"+ctemp+"\nEND\n";
		  	
	  	//BOUND check
	  	out+=" 4 0\n"; //plus and offset
		out+="RETURN \n"+rt+"\nEND\n";
      
      return (R)out;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n, A argu) {
      R _ret=null;
      String str = (String)  n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      
      String t1 = this.genTemp();
      String rt = this.genTemp();
      String t2 = this.genTemp();
      
	  String out ="BEGIN \n";
	  	out+= "MOVE "+t1+" "+str+"\n";
	  	out+= "HLOAD "+rt+" "+t1+" 0\n";
		out+="RETURN \n"+rt+"\nEND\n";
	  
      return (R)out;
   }
   
   
   

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public R visit(MessageSend n, A argu) {
      R _ret=null;
      
      this.msg = true;
      String str = (String) n.f0.accept(this, argu);
      this.msg = false;
      
       String t1 = this.genTemp();
       String t2 = this.genTemp();
       String t3 = this.genTemp();
       
      //str is wether a temp or a new object address
      String out = " CALL ";
      out+= "\nBEGIN\n";
      out+= "MOVE "+t1+" "+ str+" \n";
      
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      
      Pass p = (Pass)argu;
      Data d = Table.get(this.msgTyp);
      
      	if(this.DBG)
    	  System.out.println("--------- "+this.msgTyp+" ---------");
    	  
      	if(d!=null) {
    	  Method m = d.FnAr.get(this.msgTyp+"_"+n.f2.f0.tokenImage);
    	  
    	  out+= "HLOAD "+t2+" "+t1+" 0\n";
          out+= "HLOAD "+t3+" "+t2+ " "+String.valueOf(m.offset)+"\n";
      	}
      
      
      out+= "RETURN \n"+t3+"\nEND\n";
      
      
      n.f3.accept(this, argu);
      
      List<String> arr = new ArrayList(this.expList);
      this.expList.clear();
      n.f4.accept(this, argu);
      
      out+="( "+t1 + " ";
      
      for(int i=0;i<this.expList.size();i++) {
    	  out+= this.expList.get(i)+" ";
//    	  if(this.DBG) {
//    		  System.out.print(" $ "+this.expList.get(i)+" $");
//    	  }
      }
      
      out+= ")\n";
      n.f5.accept(this, argu);
      
      this.expList = arr;
      
     
      if(this.msgTyp != null) {
	      SymbolTable sym = STable.get(this.msgTyp);
	      
	      if(this.DBG )
	    	  System.out.println("###"+this.msgTyp);
	      
	      MethodD mthdd = sym.MethodDecl.get(n.f2.f0.tokenImage);
	      	
	    	  if(this.DBG)
		    	  System.out.println("******"+this.msgTyp+" "+n.f2.f0.tokenImage+"--"+mthdd.returnType);
	      
	   
	    String rt = mthdd.returnType;
	    if(rt.compareTo("ArrayType")==0 || rt.compareTo("Boolean")==0 || rt.compareTo("Integer")==0) {
	    	this.msgTyp = null;
	    }
	    else {
	    	this.msgTyp = rt;
	    }
	    
      }
      
      
      return (R)out;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n, A argu) {
      R _ret=null;
      String str = (String) n.f0.accept(this, argu);
      this.expList.add(str);
      n.f1.accept(this, argu);
      
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
     String str = (String) n.f1.accept(this, argu);
     
     this.expList.add(str);
      
     return _ret;
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public R visit(PrimaryExpression n, A argu) {
      R _ret=null;
      String str = (String)n.f0.accept(this, argu);
      
      String out = str;
      
      if(n.f0.which==3) {
    	  //str is identifier val lookup
    	  Data data = Table.get(cName);
    	  Method mth = data.FnAr.get(cName+"_"+mName);
    	  String id = Lookup(str,data,mth);
    	  out = id;
      }
      
      if(this.msg && n.f0.which==3) {
    	  this.msgTyp = SymbolTable.LookupVar(STable,cName+"$"+mName,str);
    	  if(this.DBG)
    		  System.out.println("----"+this.msgTyp+" "+str);
      }
      
      return (R)out;
      
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
    * f0 -> "true"
    */
   public R visit(TrueLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)("1"); //TODO change 
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)("0");
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)(n.f0.tokenImage);
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      
      if(this.msg) {
    	  msgTyp = cName;
      }
      
      return (R)("TEMP 0");  //TODO change
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n, A argu) {
   
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      String str = (String)n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      
      String arr = this.genTemp();
      String cntr = this.genTemp();
      String Lbl1 = this.genLabel();
      String Lbl2 = this.genLabel();
      
      String out ="BEGIN\n";
      out+= "MOVE "+arr+ " HALLOCATE TIMES  PLUS "+str+ " 1 4 \n";
      out+= "MOVE "+cntr+ " 4 \n";
      out+= Lbl1+" CJUMP LE "+cntr+ " MINUS  TIMES  PLUS "+str+" 1 4 1 "+Lbl2+" \n";
      out+= "HSTORE  PLUS "+arr+ " "+cntr + " 0 0 \n";
      out+= "MOVE "+cntr+" PLUS "+cntr + " 4\n";
      out+= "JUMP "+Lbl1+"\n";
      out+= Lbl2+" HSTORE "+arr+ " 0 "+str+" \n";
      out+="RETURN \n"+arr+"\nEND\n";
      
      return (R)out;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n, A argu) {
     
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      
      Data data = Table.get(n.f1.f0.tokenImage);

  
	      String mT = this.genTemp();
	      String cT = this.genTemp();
	      String print = "BEGIN\n";
	      print +="MOVE "+mT+ " HALLOCATE " + String.valueOf(data.FnAr.size()*4)+"\n";
	      print += "MOVE "+cT+ " HALLOCATE " + String.valueOf(data.getSize())+"\n" ;
	      
	      for(Method m : data.FnAr.values()) {
	    	  print +="HSTORE "+mT+ " " + String.valueOf(m.offset)+" "+m.cName+"_"+m.name+"\n";
	      }
	      
	      if(data.VarMap.size() > 0) {
	    	  String cTemp = this.genTemp();
	    	  print+="MOVE "+cTemp+" 4"+"\n";
	    	  
	    	  String Lbl1 = this.genLabel();
	    	  String Lbl2 = this.genLabel();
	    	  
	    	  print+=Lbl1+" CJUMP LE "+cTemp+" "+String.valueOf(data.getSize()-1)+" "+Lbl2+"\n";
	    	  print+= "HSTORE "+" PLUS "+cT+" "+cTemp+" 0 0\n"; 
	    	  print+= "MOVE "+cTemp+" PLUS "+cTemp+" 4"+"\n";
	    	  print+= "JUMP "+Lbl1+"\n";
	    	  print+= Lbl2+" HSTORE "+cT+ " 0 "+mT+"\n";
	      }
	      else {
	    	  print+="HSTORE "+cT+ " 0 "+mT+"\n";
	      }
	      
	      print+= "RETURN \n"+cT+"\nEND\n";
	      
	      if(this.msg) {
	    	  msgTyp = n.f1.f0.tokenImage; 
	      }
	      
	     return (R)print;
	      
    
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String str = (String)n.f1.accept(this, argu);
      
      String L1 = this.genLabel();
      String L2 = this.genLabel();
      String t = this.genTemp();
      
      String out = " BEGIN\n";
      out+= "CJUMP "+str+" "+L1+"\n";
      out+= "MOVE "+t+" 0\n";
      out+= "JUMP "+L2+"\n";
      out+= L1+" MOVE "+t+" 1\n";
      out+= L2+" NOOP\n";
      out+= "RETURN \n"+t+"\nEND\n";
      
      return (R)out;
      
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String str = (String)n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return (R)str;
   }

   /**
    * f0 -> Identifier()
    * f1 -> ( IdentifierRest() )*
    */
   public R visit(IdentifierList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Identifier()
    */
   public R visit(IdentifierRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

}
