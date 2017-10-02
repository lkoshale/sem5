class Factorial{
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac((10+0)));
    }
}

class Fac {
	
	int n;
	int diff;

    public int ComputeFac(int num){
         Fac b ;
         b = new B();
         
        System.out.println( 1000000 );
 	
 	b = new C();       
         
         
        System.out.println( b.test() );
	System.out.println( b.m(7) );
        
        return num ;
    }
    
      public int m(int k){
		return k;
	}
	
 	public int test(){
 	
 		return 0;
 	}
}


class B extends Fac {
	int n;
	int extd;
	
	//overrirde
	public int m(int k){
		return 99;
	}
	
	//diff
	public int z(){
		return 5;
	}

}


class C extends B {
	
	int ll;
	
	public int m(int k){
		return 5; 
	}

	public int xxx(){
		return 88;
	}
}



