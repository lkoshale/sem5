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
	

	public boolean isIDval = false;
	
   Hashtable<String,SymbolTable>Table = new  Hashtable<String,SymbolTable>();
   
   public void setTable( Hashtable<String,SymbolTable> Table) {
	   this.Table = Table;
	   TYPE.Table = Table;
	   
   }
   
   //
   // Auto class visitors--probably don't need to be overridden.
   //
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
      System.out.println("Program type checked successfully");
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
      
      n.f0.accept(this, argu);
      isIDval = true;
      n.f1.accept(this, argu);
      isIDval = false;
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      isIDval = true;
      n.f11.accept(this, argu);
      isIDval = false;
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      
      Car a = new Car();
      
      SymbolTable sm = null ;
      for(String s : Table.keySet()) {
    	  sm = Table.get(s);
    	//  System.out.println(s+" "+sm.ClassName);
    	  break;
      }
      
      a.sym = sm;
      
      n.f14.accept(this, (A)a);
      n.f15.accept(this, argu);
      n.f16.accept(this, argu);
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
      isIDval =true;
      n.f1.accept(this, argu);
      isIDval = false;
      
      SymbolTable st=null;
      if(Table.containsKey(n.f1.f0.tokenImage)) {
    	  st = Table.get(n.f1.f0.tokenImage);
      }
      else {
    	  System.out.println("Symbol not found");  //TODO 
    	  System.exit(0);
      }
      
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      Car a = new Car();
      a.cName = n.f1.f0.tokenImage;
      a.addSym(st);
      
      n.f4.accept(this, (A)a);
      n.f5.accept(this, argu);
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
      isIDval = true;
      n.f1.accept(this, argu);
      isIDval = false;
      
      SymbolTable st=null;
      if(Table.containsKey(n.f1.f0.tokenImage)) {
    	  st = Table.get(n.f1.f0.tokenImage);
      }
      else {
    	  System.out.println("Symbol not found");//TODO
    	  System.exit(0);
      }
      
      n.f2.accept(this, argu);
      isIDval = true;
      n.f3.accept(this, argu);
      isIDval = false;
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      Car a = new Car();
      a.cName = n.f1.f0.tokenImage;
      a.addSym(st);
      n.f6.accept(this,(A)a);
      n.f7.accept(this, argu);
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
      isIDval = true;
      n.f1.accept(this, argu);
      isIDval = false;
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
      Car a = (Car)argu;
      
      //id method name required
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      isIDval = true;
      n.f2.accept(this, argu);
      isIDval = false;
      a.mName = n.f2.f0.tokenImage;
      	
      n.f3.accept(this, argu);
      
      MethodD m = null;
      if(a.sym.MethodDecl.containsKey(n.f2.f0.tokenImage)) {
    	 m = a.sym.MethodDecl.get(n.f2.f0.tokenImage);
      } else {
    	  System.out.println("Symbol not found");  //TODO 
    	  System.exit(0);
      }
      
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      
      n.f8.accept(this,(A)a);
      
      n.f9.accept(this, argu);
      TYPE t = (TYPE)n.f10.accept(this, (A)a);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
    //  System.out.println(" "+m.returnType);
      TYPE t2 =  new TYPE(m.returnType);
      if(!t.Match(t2)) {
    	  System.out.println("Type error");//+"return type method my");  //TODO change statement
    	  System.exit(0);
      }
      
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
      isIDval=true;
      n.f1.accept(this, argu);
      isIDval = false;
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
      isIDval = true;
      n.f0.accept(this, argu);
      isIDval = false;
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
      //type of id req
  //    Car a = (Car)argu;
      
      TYPE t1 = (TYPE)n.f0.accept(this,argu);
      n.f1.accept(this, argu);
      TYPE t2 = (TYPE)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      
      Car a = (Car)argu;
	  String scope = a.cName+"$"+a.mName;
	  String typ = a.sym.LookupVar(Table, scope,t1.type);
	  if(typ==null) {
		  System.out.println("Symbol not found"); 
    	  System.exit(0);
	  }
	  TYPE t = new TYPE(typ);
      
      if(!t.Match(t2)){
    	  System.out.println("Type error");//+" assign ment"+ t.type+" "+t2.type);
    	  System.exit(0);
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
      
      TYPE t1 = (TYPE)n.f0.accept(this,argu);
      
      n.f1.accept(this, argu);
      TYPE t2 = (TYPE)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      TYPE t3 = (TYPE)n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      
      Car a = (Car)argu;
	  String scope = a.cName+"$"+a.mName;
	  String typ = a.sym.LookupVar(Table, scope,t1.type);
	  if(typ==null) {
		  System.out.println("Symbol not found"); 
    	  System.exit(0);
	  }
	  TYPE t = new TYPE(typ); 
      
      if(t.type.compareTo("ArrayType")!=0 || t2.type.compareTo("Integer")!=0 || t3.type.compareTo("Integer")!=0 ) {
    	  System.out.println("Type error");//+" arr assgn");
    	  System.exit(0);
      }
      
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
      TYPE t = (TYPE)n.f2.accept(this, argu);
     
      if(t.type.compareTo("Boolean")!=0) {
    	  System.out.println("Type error");//+" ifth");
    	  System.exit(0);
      }
      
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
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
      TYPE t = (TYPE)n.f2.accept(this, argu);
     
      if(t.type.compareTo("Boolean")!=0) {
    	  System.out.println("Type error");//+" ifthnels");
    	  System.exit(0);
      }
      
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
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
      TYPE t = (TYPE) n.f2.accept(this, argu);
      if(t.type.compareTo("Boolean")!=0) {
    	  System.out.println("Type error");//+" while");
    	  System.exit(0);
      }
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      
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
      TYPE t = (TYPE)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      
      if(t.type.compareTo("Integer")==0 || t.type.compareTo("Boolean")==0 ) {
    	  
      }else {
    	  System.out.println("Type error");//+" printstmnt "+t.type);
    	  System.exit(0);
      }
      
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
      TYPE t = (TYPE)n.f0.accept(this, argu);
      return (R)t;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n, A argu) {
      R _ret=null; 
      TYPE t1 =(TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 =(TYPE)n.f2.accept(this, argu);

      if(t1.type.compareTo("Boolean")!=0 || t2.type.compareTo("Boolean")!=0) {
    	  System.out.println("Type error");//+ " &&");
    	  System.exit(0);
      }
      return (R)t1;

   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public R visit(OrExpression n, A argu) {
      R _ret=null;
      TYPE t1 =(TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 =(TYPE)n.f2.accept(this, argu);

      if(t1.type.compareTo("Boolean")!=0 || t2.type.compareTo("Boolean")!=0) {
    	  System.out.println("Type error");//+ " or ");
    	  System.exit(0);
      }
      return (R)t1;

   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n, A argu) {
      R _ret=null;
      TYPE t1 =(TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 =(TYPE) n.f2.accept(this, argu);
      
      if(t1.type.compareTo("Integer")!=0 || t2.type.compareTo("Integer")!=0) {
    	  System.out.println("Type error");//+ "comp ");
    	  System.exit(0);
      }
      
      return (R)(new TYPE("Boolean"));
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public R visit(neqExpression n, A argu) {
      R _ret=null;
      TYPE t1 =(TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 =(TYPE) n.f2.accept(this, argu);
      
      if( (t1.type.compareTo("Integer")==0 && t2.type.compareTo("Integer")==0) || (t1.type.compareTo("Boolean")==0 && t2.type.compareTo("Boolean")==0)) {
    	  	//dont do anything its correct
      }
      else {
    	  System.out.println("Type error");//+ " ne");
    	  System.exit(0);
      }
      
      return (R)(new TYPE("Boolean"));
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n, A argu) {
      R _ret=null;
      TYPE t1 =(TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 =(TYPE)n.f2.accept(this, argu);

      if(t1.type.compareTo("Integer")!=0 || t2.type.compareTo("Integer")!=0) {
    	  System.out.println("Type error");//+ " plus");
    	  System.exit(0);
      }
      
      return (R)t1;

   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n, A argu) {
      R _ret=null;
      TYPE t1 =(TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 =(TYPE)n.f2.accept(this, argu);

      if(t1.type.compareTo("Integer")!=0 || t2.type.compareTo("Integer")!=0) {
    	  System.out.println("Type error");// + " - ");
    	  System.exit(0);
      }
      return (R)t1;

   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n, A argu) {
      R _ret=null;
      TYPE t1 =(TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 =(TYPE)  n.f2.accept(this, argu);

      if(t1.type.compareTo("Integer")!=0 || t2.type.compareTo("Integer")!=0) {
    	  System.out.println("Type error");//+" * ");
    	  System.exit(0);
      }
      return (R)t1;

   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public R visit(DivExpression n, A argu) {
      R _ret=null;
      TYPE t1 = (TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t2 = (TYPE)n.f2.accept(this, argu);

      if(t1.type.compareTo("Integer")!=0 || t2.type.compareTo("Integer")!=0) {
    	  System.out.println("Type error");//+ " / ");
    	  System.exit(0);
      }
      return (R)t1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n, A argu) {
      R _ret=null;
      
      TYPE ar = (TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      TYPE t =(TYPE)n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      
      if(ar.type.compareTo("ArrayType")!=0 || t.type.compareTo("Integer")!=0){
    	  System.out.println("Type error");//+ " arrlk ");
    	  System.exit(0);
      }
      
      return (R)(new TYPE("Integer"));
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n, A argu) {
      R _ret=null;
      TYPE t = (TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      
      if(t.type.compareTo("ArrayType")!=0){
    	  System.out.println("Type error");//+ " len");
    	  System.exit(0);
      }
      
      return (R)(new TYPE("Integer"));
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
      
      Car a = (Car)argu;
      
      //TYPE can be objects class or this
      TYPE pexp = (TYPE)n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      
      
      isIDval = true;
      n.f2.accept(this, argu);
      String mName = n.f2.f0.tokenImage;
      isIDval = false;
      
      n.f3.accept(this, argu);
      
      //for recursive save state
      List<String>mLst = new ArrayList(a.mList);
      a.mList.clear();
      
      //same argu but list will be added
      n.f4.accept(this, (A)a);
     // System.out.println(a.mList);
      
      n.f5.accept(this, argu);
      
      //method lookup
     // System.out.println("pass msg "+a.sym.ClassName);
      String typ = a.sym.LookupMethod(Table,a.mList,pexp.type,mName);
      
      if(typ==null) {
    	  System.out.println("Symbol not found"); //TODO change
    	  System.exit(0);
      }
      
      //restore state
    //  System.out.println("copy : "+mLst);
      a.mList = mLst;
      
    
      //return method typ;
      
      return (R)(new TYPE(typ));
   
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n, A argu) {
      R _ret=null;
      Car list = (Car)argu;
      TYPE t =(TYPE)n.f0.accept(this, argu);
      list.addType(t.type);
     // System.out.println(t.type+" "+list.mList);
      n.f1.accept(this, (A)list);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n, A argu) {
      R _ret=null;
      Car a = (Car)argu;
      n.f0.accept(this, argu);
      
      TYPE t =(TYPE)n.f1.accept(this, argu);
      
      a.addType(t.type);
      
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
      TYPE t = (TYPE)n.f0.accept(this, argu);
    //  System.out.println(n.f0.which);
      if(n.f0.which == 3) {
    	  //return its type
    	  Car a = (Car)argu;
    	  String scope = a.cName+"$"+a.mName;
    	  String typ = a.sym.LookupVar(Table, scope,t.type);
    	  if(typ==null) {
    		  System.out.println("Symbol not found"); 
        	  System.exit(0);
    	  }
    	 // System.out.println("inside ident val false -- "+n.f0.tokenImage+" "+typ); 
    	  return (R)(new TYPE(typ));
      }
      return (R)t;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)(new TYPE("Integer"));
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)(new TYPE("Boolean"));
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)(new TYPE("Boolean"));
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n, A argu) {
	   //val or check based on bool flag
	  //System.out.println("inside ident ");
      R _ret=null;
      n.f0.accept(this, argu);
//      if(isIDval) {
//    	  
//    	  return _ret;
//      
//      }
//      else {
//    	 
//    	//Do typcheck necessary  
//    	  Car a = (Car)argu;
//    	  if(a.cName==null || a.mName==null) {
//    		  System.out.println("Symbol not found");  //TODO change
//        	  System.exit(0);
//    	  }
//    	  
//    	  String scope = a.cName+"$"+a.mName;
//    	  String typ = a.sym.LookupVar(Table, scope,n.f0.toString());
//    	  if(typ==null) {
//    		  System.out.println("Symbol not found"); 
//        	  System.exit(0);
//    	  }
//    	 // System.out.println("inside ident val false -- "+n.f0.tokenImage+" "+typ); 
//    	  return (R)(new TYPE(typ));
//      }
      return (R)(new TYPE(n.f0.tokenImage));
      
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      Car a = (Car)argu;
      if(a.cName==null) {
    	  System.out.println("Type error");//+ " this");  //TODO change
    	  System.exit(0);
      }
      
      return (R)(new TYPE(a.cName));
   
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      TYPE ex =  (TYPE)n.f3.accept(this, argu);
      n.f4.accept(this, argu);
   
      if(ex.type.compareTo("Integer")==0) {
    	  return (R)(new TYPE("ArrayType"));
      }else {
    	  System.out.println("Type error");//+ " new arr");
    	  System.exit(0);
    	  return _ret;
      }
      
    // return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n, A argu) {
     // R _ret=null;
      n.f0.accept(this, argu);
      isIDval = true;
      n.f1.accept(this, argu);
      isIDval = false;
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      TYPE t = new TYPE(n.f1.f0.toString());
      
      return (R)t;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      TYPE t = (TYPE)n.f1.accept(this, argu);
      if(t.type.compareTo("Boolean")!=0) {
    	  System.out.println("Type error");//+" neg");  //TODO change
    	  System.exit(0);
      }
    	  
      return (R)(new TYPE("Boolean"));
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      TYPE t = (TYPE)n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return (R)t;
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
