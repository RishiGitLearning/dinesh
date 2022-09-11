/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;



 
import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
 
 
/**
 *
 * @author  nrpatel
 * @version 
 */

public class clsMenu
{

    private HashMap props;    
    public boolean Ready = false;

    public Variant getAttribute(String PropName)
    {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,int Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,double Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,float Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,boolean Value)
    {
         props.put(PropName,new Variant(Value));
    }

    
    /** Creates new clsNoDataObject */
    public clsMenu() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("MENU_ID",new Variant(0));
        props.put("MENU_TYPE",new Variant(""));
        props.put("PARENT_ID",new Variant(0));
        props.put("MENU_CAPTION",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
        props.put("CLASS_NAME",new Variant(""));
    }
    
    
    public static String getClassName(int pMenuID)
    {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strClass="";
        
        try
        {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_MENU_MASTER WHERE MENU_ID="+Integer.toString(pMenuID));
            rsTmp.first();
            if(rsTmp.getRow()>0)
            {
                strClass=rsTmp.getString("CLASS_NAME");
            }
            
        //tmpConn.close();
        tmpStmt.close();
        rsTmp.close();
            
            return strClass;
        }
        catch(Exception e)
        {
            return "";
        }
    }
    
    
    //Returns List of Menu Objects which can be used
    // to generate the Tree
    public static HashMap getMenuList(long pCompanyID,String PACKAGE)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
             System.out.println("SELECT * FROM DINESHMILLS.D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PACKAGE=\""+PACKAGE+"\" ORDER BY MENU_ID");
           
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM DINESHMILLS.D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PACKAGE=\""+PACKAGE+"\" ORDER BY MENU_ID");
            rsTmp.first();
            
            Counter=0;
            
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsMenu ObjMenu=new clsMenu();

                ObjMenu.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));            
                ObjMenu.setAttribute("MENU_ID",rsTmp.getLong("MENU_ID"));
                ObjMenu.setAttribute("MENU_TYPE",rsTmp.getString("MENU_TYPE"));
                ObjMenu.setAttribute("PARENT_ID",rsTmp.getLong("PARENT_ID"));
                ObjMenu.setAttribute("MENU_CAPTION",rsTmp.getString("MENU_CAPTION"));
                ObjMenu.setAttribute("MODULE_ID",rsTmp.getLong("MODULE_ID"));
                ObjMenu.setAttribute("CLASS_NAME",rsTmp.getString("CLASS_NAME"));
                ObjMenu.setAttribute("PACKAGE",rsTmp.getString("PACKAGE"));
                
                
                List.put(Long.toString(Counter),ObjMenu);
                rsTmp.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Menu list error "+e.getMessage());
        }
        
        return List; 
    }
    

    
    //Returns List of Menu Objects which can be used
    // to generate the Tree
    public static HashMap getFunctionList(long pCompanyID,long pMenuID)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MENU_ID="+Long.toString(pMenuID));

            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsMenuFunction ObjFunction=new clsMenuFunction();

                ObjFunction.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));            
                ObjFunction.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjFunction.setAttribute("MENU_ID",rsTmp.getLong("MENU_ID"));
                ObjFunction.setAttribute("FUNCTION_ID",rsTmp.getLong("FUNCTION_ID"));
                ObjFunction.setAttribute("FUNCTION_NAME",rsTmp.getString("FUNCTION_NAME"));
                
                List.put(Long.toString(Counter),ObjFunction);
                rsTmp.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
        }
        catch(Exception e)
        {
        }
        
        return List; 
    }
 
    
    //Returns the name of menu function name
    public static String getMenuFunctionName(long pCompanyID,long pMenuID,long pFunctionID)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        try
        {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT FUNCTION_NAME FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MENU_ID="+Long.toString(pMenuID)+" AND FUNCTION_ID="+Long.toString(pFunctionID));
           
            rsTmp.first();
            
            String FunctionName=rsTmp.getString("FUNCTION_NAME");
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
            return FunctionName;
        }
        catch(Exception e)
        {
            return "";
        }
    }
    

    //Returns the name of menu name
    public static String getMenuName(long pCompanyID,long pMenuID)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        try
        {
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT MENU_CAPTION FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MENU_ID="+Long.toString(pMenuID));
            rsTmp.first();
            String MenuCaption= rsTmp.getString("MENU_CAPTION");
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
            return MenuCaption;
        }
        catch(Exception e)
        {
            return "";
        }
    }
    

        //Returns the name of menu name
    public static int getModuleID(long pCompanyID,int pMenuID)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        try
        {
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT MODULE_ID FROM DINESHMILLS.D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MENU_ID="+Long.toString(pMenuID));
            rsTmp.first();
            int ModuleID= rsTmp.getInt("MODULE_ID");
            //JOptionPane.showMessageDialog(null, "Module Id = "+ModuleID+", COMP Id = "+pCompanyID+", Menu Id = "+pMenuID);
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
            return ModuleID;
        }
        catch(Exception e)
        {
            return 0;
        }
    }

    
    public static int getMenuIDFromFunctionID(int pCompanyID,int pFunctionID)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        try
        {
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT MENU_ID FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID="+pCompanyID+" AND FUNCTION_ID="+pFunctionID);
            rsTmp.first();
            int MenuID= rsTmp.getInt("MENU_ID");

        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
            return MenuID;
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    
    
    public static int getMenuIDFromModule(long pCompanyID,int pModuleID)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        try
        {
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT MENU_ID FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MODULE_ID="+Long.toString(pModuleID));
            rsTmp.first();
            int MenuID= rsTmp.getInt("MENU_ID");
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
            return MenuID;
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    
}
