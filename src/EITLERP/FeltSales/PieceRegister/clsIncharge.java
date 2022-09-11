/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceRegister;

import EITLERP.Variant;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class clsIncharge 
{
    public HashMap props;
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public clsIncharge() {
        props=new HashMap();
        props.put("INCHARGE_CD", new Variant(""));
        props.put("INCHARGE_NAME", new Variant(""));
        props.put("INCHARGE_ACTIVE", new Variant(""));
    }
    
    public static HashMap getIncgargeList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            System.out.println("SELECT * FROM PRODUCTION.FELT_INCHARGE "+pCondition);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_INCHARGE "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsIncharge ObjIncharge =new clsIncharge();
                
                ObjIncharge.setAttribute("INCHARGE_CD",rsTmp.getInt("INCHARGE_CD"));
                ObjIncharge.setAttribute("INCHARGE_NAME",rsTmp.getString("INCHARGE_NAME"));
                ObjIncharge.setAttribute("INCHARGE_ACTIVE",rsTmp.getString("INCHARGE_ACTIVE"));
                
                List.put(Long.toString(Counter),ObjIncharge);
            }
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public String getIncgargeName(String InchargeCode) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        System.out.println("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE where INCHARGE_CD = '"+InchargeCode+"'");
        String Incharge =data.getStringValueFromDB("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE where INCHARGE_CD = '"+InchargeCode+"'");
       
        return Incharge;
    }
}
