/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY;

import EITLERP.data;
import java.sql.*;

/**
 *
 * @author root
 */
public class clsRunStoredProceduresRepeat {
    
    public static void main(String[] args) {
        
        //0 - month
        //1 - year
        
        if(args.length<2) {
            System.out.println("Insufficient arguments. Please specify \n 1. Month \n2. Year ");
            return;
        }
        
        int month=Integer.parseInt(args[0]);
        int year=Integer.parseInt(args[1]);
        
        
        data.Execute("{CALL SDMLATTPAY.DAILY_RAW_DATA_TO_DAILY_ATT_TEMP("+month+", "+year+")}");        
        data.Execute("{CALL SDMLATTPAY.ATT_LEAVE_BALANCE("+year+")}");        
        
        
        
    }
    
}
