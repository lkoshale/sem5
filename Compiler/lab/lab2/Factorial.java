class Factorial{
    public static void main(String[] a){
        System.out.println(1);
    }
}

class Fac  {

	int def;
	
    public int ComputeFac(int num,Fact y){
    	
        int num_aux ;
        if ((num <= 1)&&(num != 1))
            num_aux = (1+0) ;
        else
            num_aux = num *4; //(this.ComputeFac(num-1)) ;
        
     
        return num_aux ;
    }
}


/*
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

	int f;
	public int Comp(){
    	
        int nx ;
        nx = def*1; //(this.ComputeFac(num-1)) ;
        
     
        return 0 ;
    }
	
 }
 */
 
