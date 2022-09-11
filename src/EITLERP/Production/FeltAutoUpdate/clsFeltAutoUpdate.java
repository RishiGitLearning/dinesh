/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.FeltAutoUpdate;

import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import TReportWriter.*;
import java.io.File;



import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URL;
import javax.swing.JOptionPane;

import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLComboModel;
import EITLERP.EITLTableModel;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.Production.FeltAutoUpdate.clsFeltAutoUpdate;
/**
 *
/**
 *
 * @author root
 */
public class clsFeltAutoUpdate {
       
 public static String FinURL="jdbc:mysql://200.0.0.227:3306/PRODUCTION?zeroDateTimeBehavior=convertToNull";    
    
    public static void main(String[] args) {
        // TODO code application logic here
        clsFeltAutoUpdate FeltAutoUpdate = new clsFeltAutoUpdate();
        FeltAutoUpdate.FeltAutoDiversionList();
    }
    
    
       private void FeltAutoDiversionList(){
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            
            
           data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_DIVERSION_FLAG = 'READY' WHERE PR_PIECE_STAGE LIKE ('%STOCK%') AND DATE_ADD(PR_RCV_DATE, INTERVAL + 60 DAY) < NOW() AND PR_RCV_DATE != '0000-00-00'",FinURL);
            
             
          
        }catch(SQLException e){e.printStackTrace();}
        
    }
    
    
    
    
}
