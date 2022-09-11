/*
 * NumWords.java
 *
 * Created on December 19, 2007, 1:11 PM
 */

package TReportWriter;
import EITLERP.EITLERPGLOBAL;
import java.text.DecimalFormat;
public class NumWord {
    String string;
    String a[]={"",
    "One",
    "Two",
    "Three",
    "Four",
    "Five",
    "Six",
    "Seven",
    "Eight",
    "Nine",
    };
    
    String b[]={
        "Hundred",
        "Thousand",
        "Lakh",
        "Crore"
        
    };
    
    String c[]={"Ten",
    "Eleven",
    "Twelve",
    "Thirteen",
    "Fourteen",
    "Fifteen",
    "Sixteen",
    "Seventeen",
    "Eighteen",
    "Ninteen",
    };
    
    String d[]={
        
        "Twenty",
        "Thirty",
        "Forty",
        "Fifty",
        "Sixty",
        "Seventy",
        "Eighty",
        "Ninty"
    };
    
    
    public String convertNumToWord(int number){
        
        int c=1;
        int rm ;
        string="";
        while ( number != 0 ) {
            switch ( c ) {
                case 1 :
                    rm = number % 100 ;
                    pass( rm ) ;
                    if( number > 100 && number % 100 != 0 ) {
                        display( "" ) ;
                    }
                    number /= 100 ;
                    
                    break ;
                    
                case 2 :
                    rm = number % 10 ;
                    if ( rm != 0 ) {
                        display( " " ) ;
                        display( b[0] ) ;
                        display( " " ) ;
                        pass( rm ) ;
                    }
                    number /= 10 ;
                    break ;
                    
                case 3 :
                    rm = number % 100 ;
                    if ( rm != 0 ) {
                        display( " " ) ;
                        display( b[1] ) ;
                        display( " " ) ;
                        pass( rm ) ;
                    }
                    number /= 100 ;
                    break ;
                    
                case 4 :
                    rm = number % 100 ;
                    if ( rm != 0 ) {
                        display( " " ) ;
                        display( b[2] ) ;
                        display( " " ) ;
                        pass( rm ) ;
                    }
                    number /= 100 ;
                    break ;
                    
                case 5 :
                    rm = number % 100 ;
                    if ( rm != 0 ) {
                        display( " " ) ;
                        display( b[3] ) ;
                        display( " " ) ;
                        pass( rm ) ;
                    }
                    number /= 100 ;
                    break ;
                    
            }
            c++ ;
        }
        
        return string;
    }
    
    public String convertNumToWord(double number) {
        String AmountInWords="";
        DecimalFormat dFormat=new DecimalFormat("###0.00"); 
        try {
            int Rupees=0;
            int Paise=0;
            boolean lessThen10=false;
            double Number=number;
            //String strNumber=Double.toString(Number);
            String strNumber=dFormat.format(Number);
            if(strNumber.indexOf(".")!=-1) {
                String Rs=strNumber.substring(0,strNumber.indexOf("."));
                String Ps=strNumber.substring(strNumber.indexOf(".")+1);
                if(Ps.startsWith("0")) {
                    lessThen10=true;
                }
                Rupees=Integer.parseInt(Rs);
                Paise=Integer.parseInt(Ps);
            }
            else {
                Rupees=Integer.parseInt(strNumber);
                Paise=0;
            }
            
            if(Rupees>0&&Paise>0) {
                if(Paise<10 && !lessThen10) {
                    Paise=Paise*10;
                }
                
                if(Paise>99) {
                    Paise=Integer.parseInt(Double.toString(NumWord.round(Paise, 2)));
                }
                
                AmountInWords=convertNumToWord(Rupees)+" and "+convertNumToWord(Paise)+" Paise Only";
            }
            else {
                AmountInWords=convertNumToWord(Rupees)+" Only";
            }
           
        }
        catch(Exception e) {
            
        }
        
        return AmountInWords;
    }
    
    public void pass(int number) {
        int rm, q ;
        if ( number < 10 ) {
            display( a[number] ) ;
        }
        
        if ( number > 9 && number < 20 ) {
            display( c[number-10] ) ;
        }
        
        if ( number > 19 ) {
            rm = number % 10 ;
            if ( rm == 0 ) {
                q = number / 10 ;
                display( d[q-2] ) ;
            }
            else {
                q = number / 10 ;
                display( a[rm] ) ;
                display( " " ) ;
                display( d[q-2] ) ;
            }
        }
    }
    
    public void display(String s) {
        String t ;
        t= string ;
        string= s ;
        string+= t ;
    }
    
    public static void main(String args[]){
        
        NumWord num=new NumWord();
        System.out.println("num.convertNumToWord(0)"+num.convertNumToWord(0));
        System.out.println("num.convertNumToWord(0)"+num.convertNumToWord(150.37));
        
    }
    
    public static double round(double val, int places) {
        long factor = (long)Math.pow(10,places);
        
        // Shift the decimal the correct number of places
        // to the right.
        val = val * factor;
        
        // Round to the nearest integer.
        long tmp = Math.round(val);
        
        // Shift the decimal the correct number of places
        // back to the left.
        return (double)tmp / factor;
    }
    
    
}