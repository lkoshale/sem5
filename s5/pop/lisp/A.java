import java.lang.*;
import java.util.Random;

public class A {

  public static void main(String[] args) {

    Random rand = new Random();

    String cu = "pass";
    String trainNo = "16508";
    String [] fair = { "750","1208","2130","250"};


    String[] station = { "BHP","BSP","GDA" ,"GND","JPR","KTA","KTN","MAS", "MMB","NDL", "NGP","NLR", "USL","WRG"};

    String[] type = {"M","L","U"};
    String[] r = {"Y","N"};


    int j=120;

    String str1="";
    String str2="";
    for(int i=0;i<18;i++){

      int seatNo = rand.nextInt(70)+1;
      int f = rand.nextInt(4);
      int next = rand.nextInt(10000000)+10000001;

      String PNR = String.valueOf(next);

      int st = rand.nextInt(station.length);
      int en = rand.nextInt(station.length);


  //    int compNo = rand.nextInt(20)+1;
      int nums = rand.nextInt(20)+70;


  //   str1+="insert into fare values ( "+"\'"+PNR+"\', "+trainNo+","+fair[f]+" );\n";

  //   str2+="insert into customer values ( "+"\'"+PNR+"\', \'"+cu+j+"\', \'"+station[st]+"\', \'"
    //  +station[en]+"\',"+seatNo+" ,\'17/12/2015\' ,"+fair[f]+" );\n";

     //str1+= "insert into compartment values ( \'"+"S"+i+"\', "+trainNo+", \'SL\', "+nums+" );\n";
     for(int k=0;k<nums;k++){
       int t = rand.nextInt(3);
       int rk = rand.nextInt(2);
       str1+= "insert into Berth values ( "+k+", "+trainNo+", \'S"+i+"\', \'"+type[t]+"\',\'"+r[rk]+"\' );\n";
    }

      j++;
    }

      System.out.println(str1+"\n\n\n");

  }

}
