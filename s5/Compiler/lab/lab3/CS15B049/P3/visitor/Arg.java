package visitor;

public class Arg {
	//scope className+Methodname 
		public String scope;
		public String Cname = null;
		public String Mname = null; 
		
		public String pass;
		public SymbolTable sym =null;
		public Arg() {pass="";scope="";}
		
		public void addSymbolTable(SymbolTable sy) {
			this.sym = sy;
		}
		
		public void addPass(String str) {
			if(pass.length()==0)
				this.pass+=str;
			else
				this.pass = this.pass+" "+str;
		}
		
		public void addCName(String str) {this.Cname = str;}
		
		public void addMname(String str) { this.Mname = str;}
		
		public void AddClassScope(String str) {
			if(this.scope.length()==0) {
				this.scope+=str;
			}
		}
		
		public void AddMethodScope(String method) {
			if(this.scope.length()>0) {
				this.scope=this.scope+"$"+method;
			}
		}
		
		public String getScope() {return this.scope; }
		public String getPass() {return this.pass; }
		
}
