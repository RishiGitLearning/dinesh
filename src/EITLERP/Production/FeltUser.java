/*
 * FeltUser.java
 * This class is used to get details of FELT USERS(used for felt weaving, mending, needling and rejection
 * modules)
 *
 * Created on May 6, 2013, 2:02 PM
 */

package EITLERP.Production;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;

/**
 * @author  Vivek Kumar
 */

public class FeltUser {
    public HashMap props;
    public String LastError;
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public FeltUser() {
        LastError = "";
        props=new HashMap();
    }
    
    public static String getUserName(int pUserID) {
        return data.getStringValueFromDB("SELECT USER_NAME FROM PRODUCTION.FELT_USER WHERE USER_ID="+pUserID);
    }
    
    public static String getUserDept(int pUserID) {
        return data.getStringValueFromDB("SELECT USER_DEPT FROM PRODUCTION.FELT_USER WHERE USER_ID="+pUserID);
    }
    
    public static String getUserModule(int pUserID) {
        return data.getStringValueFromDB("SELECT USER_MODULE FROM PRODUCTION.FELT_USER WHERE USER_ID="+pUserID);
    }
    
    public HashMap getList(String pCondition) {
        Connection tmpConn=data.getCreatedConn();
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        int Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_USER_MASTER "+pCondition+" ORDER BY USER_NAME");
            
            rsTmp.first();
            while(!rsTmp.isAfterLast()) {
                Counter++;
                FeltUser ObjFeltUser=new FeltUser();
                
                //Populate the user
                ObjFeltUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                ObjFeltUser.setAttribute("USER_NAME",rsTmp.getString("USER_NAME"));
                ObjFeltUser.setAttribute("USER_DEPT",rsTmp.getString("USER_DEPT"));
                ObjFeltUser.setAttribute("USER_MODULE",rsTmp.getString("USER_MODULE"));
                ObjFeltUser.setAttribute("USER_CATEG",rsTmp.getString("USER_CATEG"));
                
                List.put(new Integer(Counter),ObjFeltUser);
            }
            rsTmp.close();
            tmpStmt.close();
        }catch(Exception e) {
            e.printStackTrace();
        }        
        return List;
    }
    
}
