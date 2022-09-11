/*
 * clsCheckRecord.java
 *
 * Created on December 10, 2005, 12:32 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import EITLERP.*;
import java.sql.*;
import java.net.*;


public class clsCheckRecord {
    
    
    /** Creates a new instance of clsCheckRecord */
    public clsCheckRecord() {
    }
    
    
    public static Variant checkRecord(Connection theConn,String TableName,ResultSet rsSrc,String FieldName) {
        ResultSet rsTmp=null;
        Variant objValue=null;
        
        try {
            
            if(TableName.equals("D_INV_ITEM_MASTER")) {
                if(FieldName.equals("ITEM_SYS_ID")) {
    
                    long ItemSysID=rsSrc.getLong("ITEM_SYS_ID");
                    String ItemID=rsSrc.getString("ITEM_ID");
                    
                    Statement stTmp=theConn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT ITEM_SYS_ID FROM D_INV_ITEM_MASTER WHERE ITEM_SYS_ID="+ItemSysID+" AND ITEM_ID<>'"+ItemID+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        //Item Sys ID found
                        rsTmp=stTmp.executeQuery("SELECT IF(MAX(ITEM_SYS_ID) IS NULL,0,MAX(ITEM_SYS_ID)) AS MAXID FROM D_INV_ITEM_MASTER");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            objValue=new Variant(0);
                            objValue.set(rsTmp.getLong("MAXID")+1);
                        }
                        
                        //Delete old record
                        stTmp.execute("DELETE FROM D_INV_ITEM_MASTER WHERE ITEM_ID='"+ItemID+"'");
                        stTmp.execute("DELETE FROM D_INV_ITEM_MASTER_H WHERE ITEM_ID='"+ItemID+"'");
                        
                    }
                }
            }
            

            
            if(TableName.equals("D_INV_ITEM_MASTER_AMEND")) {
                if(FieldName.equals("AMEND_ID")) {
    
                    long ItemSysID=rsSrc.getLong("AMEND_ID");
                    String ItemID=rsSrc.getString("ITEM_ID");
                    
                    Statement stTmp=theConn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT AMEND_ID FROM D_INV_ITEM_MASTER_AMEND WHERE AMEND_ID="+ItemSysID+" AND ITEM_ID<>'"+ItemID+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        //Item Sys ID found
                        rsTmp=stTmp.executeQuery("SELECT IF(MAX(AMEND_ID) IS NULL,0,MAX(AMEND_ID)) AS MAXID FROM D_INV_ITEM_MASTER_AMEND");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            objValue=new Variant(0);
                            objValue.set(rsTmp.getLong("MAXID")+1);
                        }
                        
                    }
                }
            }
            
            
            
            if(TableName.equals("D_COM_SUPP_MASTER")) {
                if(FieldName.equals("SUPP_ID")) {
    
                    long SuppSysID=rsSrc.getLong("SUPP_ID");
                    String SupplierCode=rsSrc.getString("SUPPLIER_CODE");
                    
                    Statement stTmp=theConn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE SUPP_ID="+SuppSysID+" AND SUPPLIER_CODE<>'"+SupplierCode+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        //Supplier Sys ID found
                        rsTmp=stTmp.executeQuery("SELECT IF(MAX(SUPP_ID) IS NULL,0,MAX(SUPP_ID)) AS MAXID FROM D_COM_SUPP_MASTER");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            objValue=new Variant(0);
                            objValue.set(rsTmp.getLong("MAXID")+1);
                        }
                        
                        long CurrentSuppSysID=0;
                        
                        rsTmp=stTmp.executeQuery("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+SupplierCode+"'");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0)
                        {
                          CurrentSuppSysID=rsTmp.getLong("SUPP_ID");
                          
                          stTmp.execute("DELETE FROM D_COM_SUPP_TERMS WHERE SUPP_ID="+CurrentSuppSysID);
                          stTmp.execute("DELETE FROM D_COM_SUPP_MASTER WHERE SUPP_ID="+CurrentSuppSysID);
                                                    
                        }
                    }
                }
            }
            

            if(TableName.equals("D_COM_SUPP_AMEND_MASTER")) {
                if(FieldName.equals("AMEND_ID")) {
    
                    long SuppSysID=rsSrc.getLong("AMEND_ID");
                    String SupplierCode=rsSrc.getString("SUPPLIER_CODE");
                    
                    Statement stTmp=theConn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT AMEND_ID FROM D_COM_SUPP_AMEND_MASTER WHERE AMEND_ID="+SuppSysID+" AND SUPPLIER_CODE<>'"+SupplierCode+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        //Supplier Sys ID found
                        rsTmp=stTmp.executeQuery("SELECT IF(MAX(AMEND_ID) IS NULL,0,MAX(AMEND_ID)) AS MAXID FROM D_COM_SUPP_AMEND_MASTER");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            objValue=new Variant(0);
                            objValue.set(rsTmp.getLong("MAXID")+1);
                        }
                    }
                }
            }
            
            
            
        }
        catch(Exception e) {
            
        }
        
        return objValue;
        
    }
    
}
