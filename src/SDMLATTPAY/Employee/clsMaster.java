/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.Employee;

import EITLERP.Variant;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsMaster {
    private HashMap props;
    public boolean Ready = false;
    
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
    
    /** Creates new clsFeltWeavingDetails */
    public clsMaster() {
        props=new HashMap();
        
        
   }
    
   public static HashMap getDepartmentList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            System.out.println("SELECT * FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER "+pCondition);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsMaster ObjDept =new clsMaster();
                
                ObjDept.setAttribute("DPTID",rsTmp.getInt("DPTID"));
                //ObjDept.setAttribute("DFLTDPT",rsTmp.getString("DFLTDPT"));//WRKDPT
                ObjDept.setAttribute("DPTCODE",rsTmp.getString("DPTCODE"));
                ObjDept.setAttribute("Name",rsTmp.getString("Name"));
                ObjDept.setAttribute("ShortName",rsTmp.getString("ShortName"));
                ObjDept.setAttribute("ColorCode",rsTmp.getString("ColorCode"));
                ObjDept.setAttribute("Descriptions",rsTmp.getString("Descriptions"));
                
                List.put(Long.toString(Counter),ObjDept);
            }
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
   }
   
   public static HashMap getMainCategoryList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            System.out.println("SELECT * FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER "+pCondition);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsMaster ObjDesignation =new clsMaster();
                
                ObjDesignation.setAttribute("SECID",rsTmp.getInt("SECID"));
                ObjDesignation.setAttribute("DFLTSEC",rsTmp.getString("DFLTSEC"));
                ObjDesignation.setAttribute("SECCODE",rsTmp.getString("SECCODE"));
                ObjDesignation.setAttribute("Name",rsTmp.getString("Name"));
                ObjDesignation.setAttribute("ShortName",rsTmp.getString("ShortName"));
                ObjDesignation.setAttribute("Descriptions",rsTmp.getString("Descriptions"));
                
                List.put(Long.toString(Counter),ObjDesignation);
            }
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }        
    
   public static HashMap getCategoryList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            System.out.println("SELECT * FROM SDMLATTPAY.ATT_CATEGORY_MASTER "+pCondition);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_CATEGORY_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsMaster ObjDesignation =new clsMaster();
                
                ObjDesignation.setAttribute("CTGID",rsTmp.getInt("CTGID"));
                ObjDesignation.setAttribute("DFLTCTG",rsTmp.getString("DFLTCTG"));
                ObjDesignation.setAttribute("CTGCODE",rsTmp.getString("CTGCODE"));
                ObjDesignation.setAttribute("Name",rsTmp.getString("Name"));
                ObjDesignation.setAttribute("ShortName",rsTmp.getString("ShortName"));
                ObjDesignation.setAttribute("Descriptions",rsTmp.getString("Descriptions"));
                
                List.put(Long.toString(Counter),ObjDesignation);
            }
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
   
   public static HashMap getShiftList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            System.out.println("SELECT * FROM SDMLATTPAY.ATT_SHIFT "+pCondition);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SHIFT "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsMaster ObjDesignation =new clsMaster();
                
                ObjDesignation.setAttribute("SHIFT_ID",rsTmp.getString("SHIFT_ID"));
                ObjDesignation.setAttribute("SHIFT_NAME",rsTmp.getString("SHIFT_NAME"));
                ObjDesignation.setAttribute("SHIFT_IN_TIME",rsTmp.getString("SHIFT_IN_TIME"));
                ObjDesignation.setAttribute("SHIFT_OUT_TIME",rsTmp.getString("SHIFT_OUT_TIME"));
                ObjDesignation.setAttribute("SHIFT_LUNCH_IN",rsTmp.getString("SHIFT_LUNCH_IN"));
                ObjDesignation.setAttribute("SHIFT_LUNCH_OUT",rsTmp.getString("SHIFT_LUNCH_OUT"));
                ObjDesignation.setAttribute("SHIFT_WRK_HRS",rsTmp.getString("SHIFT_WRK_HRS"));
                
                List.put(Long.toString(Counter),ObjDesignation);
            }
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
           
   public static HashMap getDesignationList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getCreatedConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            System.out.println("SELECT * FROM SDMLATTPAY.ATT_DESIGNATION_MASTER "+pCondition);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_DESIGNATION_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsMaster ObjDesignation =new clsMaster();
                
                ObjDesignation.setAttribute("DSGID",rsTmp.getInt("DSGID"));
                ObjDesignation.setAttribute("DFLTDSG",rsTmp.getString("DFLTDSG"));
                ObjDesignation.setAttribute("DSGCODE",rsTmp.getString("DSGCODE"));
                ObjDesignation.setAttribute("Name",rsTmp.getString("Name"));
                ObjDesignation.setAttribute("ShortName",rsTmp.getString("ShortName"));
                //ObjDesignation.setAttribute("WLID",rsTmp.getString("WLID"));
                //ObjDesignation.setAttribute("Descriptions",rsTmp.getString("Descriptions"));
                
                List.put(Long.toString(Counter),ObjDesignation);
            }
            
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
}