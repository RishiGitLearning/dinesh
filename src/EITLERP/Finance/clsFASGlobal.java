/*
 * clsFASGlobal.java
 *
 * Created on April 20, 2011, 10:36 AM
 */

package EITLERP.Finance;

import EITLERP.*;
/**
 *
 * @author  root
 */
public class clsFASGlobal {
    
    /** Creates a new instance of clsFASGlobal */
    public static double DeprnLimit= 5; //In Percentage
    public clsFASGlobal() {
    }
    public static double WrittenDownMethod(double AssetCost,double Percentage)
    {
        double Depr=EITLERPGLOBAL.round((AssetCost*Percentage)/100,0);
        
        
        return Depr;
    }
    public static double WrittenDownMethod(double AssetCost,double Percentage,int month)
    {
        double Depr=EITLERPGLOBAL.round((AssetCost*Percentage)/100,0);
        Depr = EITLERPGLOBAL.round((Depr/12)*month,0);
        
        return Depr;
    }
    
}
