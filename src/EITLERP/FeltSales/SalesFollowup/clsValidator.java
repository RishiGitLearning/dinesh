/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.SalesFollowup;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;

/**
 *
 * @author root
 */
public class clsValidator {

    public static boolean isCurrentSchMonthValid(String CurrentSchMonth,String OCMonth)
    {
        boolean flag = false;
        String CurrentSchMonth_Date = EITLERPGLOBAL.formatDateDB(get1stDate(CurrentSchMonth));
        String OCMonth_Date = EITLERPGLOBAL.formatDateDB(get1stDate(OCMonth));
        
        int date_diff = data.getIntValueFromDB("SELECT DATEDIFF('" + CurrentSchMonth_Date + "','" + OCMonth_Date + "')");
        if (date_diff >= 0) {
            flag = true;
        }
        
        return flag;
    }
    private static String get1stDate(String MonthName)
    {
        String Date = "";
        
        String Month = MonthName.substring(0, 3);
        String Year = MonthName.substring(6, 10);
        if(Month.equals("Jan"))
        {
            Date = "01/01/"+Year;
        }
        else if(Month.equals("Feb"))
        {
            Date = "01/02/"+Year;
        }
        else if(Month.equals("Mar"))
        {
            Date = "01/03/"+Year;
        }
        else if(Month.equals("Apr"))
        {
            Date = "01/04/"+Year;
        }
        else if(Month.equals("May"))
        {
            Date = "01/05/"+Year;
        }
        else if(Month.equals("Jun"))
        {
            Date = "01/06/"+Year;
        }
        else if(Month.equals("Jul"))
        {
            Date = "01/07/"+Year;
        }
        else if(Month.equals("Aug"))
        {
            Date = "01/08/"+Year;
        }
        else if(Month.equals("Sep"))
        {
            Date = "01/09/"+Year;
        }
        else if(Month.equals("Oct"))
        {
            Date = "01/10/"+Year;
        }
        else if(Month.equals("Nov"))
        {
            Date = "01/11/"+Year;
        }
        else if(Month.equals("Dec"))
        {
            Date = "01/12/"+Year;
        }
        return Date;
    }
}
