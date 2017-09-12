class Factorial{
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac((10+0)));
    }
}

class Fac  {

	int def;
	
    public int ComputeFac(int num){
    	
        int num_aux ;
        if ((num <= 1)&&(num != 1))
            num_aux = (1+0) ;
        else
            num_aux = num *(this.ComputeFac(num-1)) ;
        
     
        return num_aux ;
    }
}



class A extends Fac {

	int d;
	public int Comp(int num){
    	
        int nx ;
        if ((num <= 1)&&(num != 1))
            nx = (1+0) ;
        else
            nx = def*1; //(this.ComputeFac(num-1)) ;
        
     
        return nx ;
    }
	
 }
 
 
class B extends A {

	Fac f;
	public int Comp(){
    	
        int nx ;
        nx = def*1; //(this.ComputeFac(num-1)) ;
        f = new B();
     
        return 0 ;
    }
	
 }
 
