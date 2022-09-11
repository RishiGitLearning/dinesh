/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP;

 
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

public class clsMenu {

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
    public static HashMap getMenuList(long pCompanyID)
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
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY MENU_ID");

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
    
public static HashMap getMenuList(long pCompanyID,int pUserID)    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY MENU_ID");
//            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MENU_ID IN "
//                    + "(SELECT DISTINCT MENU_ID FROM D_COM_USER_RIGHTS WHERE USER_ID="+Integer.toString(pUserID)+") "
//                    + " ORDER BY MENU_ID");
            rsTmp = tmpStmt.executeQuery("SELECT DISTINCT M.* FROM D_COM_MENU_MASTER M,D_COM_USER_RIGHTS R WHERE M.COMPANY_ID=R.COMPANY_ID "
                    + "AND M.MENU_ID=R.MENU_ID AND M.COMPANY_ID="+Long.toString(pCompanyID)+" AND R.USER_ID="+Integer.toString(pUserID)+" ORDER BY M.MENU_ID");

            
            rsTmp.first();
            
            Counter=0;
            if(rsTmp.first()){
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
                
                List.put(Long.toString(Counter),ObjMenu);
                rsTmp.next();
            }
            }
            System.out.println(Counter);
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
            rsTmp=tmpStmt.executeQuery("SELECT MODULE_ID FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MENU_ID="+Long.toString(pMenuID));
            rsTmp.first();
            int ModuleID= rsTmp.getInt("MODULE_ID");
            
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
    public static int getMenuIDFromFunctionID(int pCompanyID, int pFunctionID, long pUserId) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getConn();

        try {
            tmpStmt = tmpConn.createStatement();
            rsTmp = tmpStmt.executeQuery("SELECT MENU_ID FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID=" + pCompanyID + " AND FUNCTION_ID=" + pFunctionID);
            rsTmp.first();
            int MenuID = 0;
            while (!rsTmp.isAfterLast()) {
                MenuID = rsTmp.getInt("MENU_ID");
                if (data.getIntValueFromDB("SELECT COUNT(*) FROM D_COM_USER_RIGHTS "
                        + "WHERE COMPANY_ID=" + pCompanyID + " AND USER_ID=" + pUserId + " AND MENU_ID=" + MenuID + " AND FUNCTION_ID=" + pFunctionID) == 0) {
                    break;
                }
                rsTmp.next();
            }
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();

            return MenuID;
        } catch (Exception e) {
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
