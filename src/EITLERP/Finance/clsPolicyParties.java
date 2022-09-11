
package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsPolicyParties {

    private HashMap props;    
    public boolean Ready = false;

    public HashMap colPolicyParties=new HashMap();
    
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

    public clsPolicyParties() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("POLICY_ID",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("PARTY_MAIN_CODE",new Variant(""));
        props.put("PARTY_ID",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));        
    }
    
    public static HashMap getList(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            rsTmp=data.getResult("SELECT * FROM D_SAL_POLICY_PARTIES "+pCondition+" ORDER BY SR_NO ");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPolicyParties ObjParties=new clsPolicyParties();
                
                //Populate the user
                ObjParties.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                ObjParties.setAttribute("POLICY_ID",rsTmp.getString("POLICY_ID"));
                ObjParties.setAttribute("SR_NO",Counter);
                ObjParties.setAttribute("PARTY_ID",rsTmp.getString("PARTY_ID"));
                ObjParties.setAttribute("PARTY_MAIN_CODE",rsTmp.getString("PARTY_MAIN_CODE"));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjParties);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }
    
    public static HashMap getListFromPartyMst(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            rsTmp=data.getResult("SELECT * FROM D_FIN_PARTY_MASTER "+pCondition+" "+
                //" AND MAIN_ACCOUNT_CODE IN ('210010','210027','210034','210072') "+
                " AND APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_CODE ",FinanceGlobal.FinURL);
                        
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPolicyParties ObjParties=new clsPolicyParties();
                
                //Populate the user
                ObjParties.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);                
                ObjParties.setAttribute("SR_NO",Counter);
                ObjParties.setAttribute("PARTY_ID",rsTmp.getString("PARTY_CODE"));
                ObjParties.setAttribute("PARTY_MAIN_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjParties);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }
    
    public static HashMap getListFromLCMst(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            String str = "";
            str = "SELECT PARTY_CODE FROM D_SAL_POLICY_LC_MASTER "+pCondition+" "+//AND EXP_DATE >= '"+ EITLERPGLOBAL.getCurrentDateDB() + "' " +                 
                " AND CANCELLED=0 ORDER BY PARTY_CODE ";
            System.out.println(str);
            rsTmp=data.getResult(str);
                        
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPolicyParties ObjParties=new clsPolicyParties();
                
                //Populate the user
                ObjParties.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);                
                ObjParties.setAttribute("SR_NO",Counter);
                ObjParties.setAttribute("PARTY_ID",rsTmp.getString("PARTY_CODE"));
                ObjParties.setAttribute("PARTY_MAIN_CODE","210027");
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjParties);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }


}
