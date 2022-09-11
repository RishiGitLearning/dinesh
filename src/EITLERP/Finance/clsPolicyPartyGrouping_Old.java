package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsPolicyPartyGrouping_Old{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colChildPartyGrp=new HashMap();
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return new Variant("");
        }
        else {
            return (Variant) props.get(PropName);
        }
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
    
    
    
    /** Creates new clsMaterialRequisition */
    public clsPolicyPartyGrouping_Old() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SR_NO",new Variant(0));
        props.put("PARENT_PARTY_ID",new Variant(""));
        props.put("CHILD_PARTY_ID",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        
        colChildPartyGrp=new HashMap();
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING ORDER BY PARENT_PARTY_ID");
            Ready=true;
            MoveLast();
            return true;
            
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        }
        catch(Exception e) {
            
        }
    }
    
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsResultSet.isAfterLast()||rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            }
            else {
                String ParentParty = UtilFunctions.getString(rsResultSet,"PARENT_PARTY_ID", "");
                
                while(!rsResultSet.isAfterLast()) {
                    String ParentPartyID = UtilFunctions.getString(rsResultSet,"PARENT_PARTY_ID", "");
                    if (ParentParty.trim().equals(ParentPartyID)) {
                        rsResultSet.next();
                    }
                    else {
                        break;
                    }
                }
                
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            }
            else {
                String ParentParty = UtilFunctions.getString(rsResultSet,"PARENT_PARTY_ID", "");
                
                while(!rsResultSet.isBeforeFirst()) {
                    String ParentPartyID = UtilFunctions.getString(rsResultSet,"PARENT_PARTY_ID", "");
                    if (ParentParty.trim().equals(ParentPartyID)) {
                        rsResultSet.previous();
                    }
                    else {
                        break;
                    }
                }
                
                if(rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean Insert() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_H WHERE PARENT_PARTY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            for(int i=1;i<=colChildPartyGrp.size();i++) {
                clsPolicyChildPartyGrp objChild=(clsPolicyChildPartyGrp) colChildPartyGrp.get(Integer.toString(i));
                
                long SrNo=data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_SAL_POLICY_PARTY_GROUPING","SR_NO");
                //--------- Generate New Internal Deposit Type ID  ------------
                setAttribute("SR_NO",SrNo);
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsResultSet.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
                rsResultSet.updateString("CHILD_PARTY_ID",objChild.getAttribute("CHILD_PARTY_ID").getString());
                rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CHANGED",true);
                rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CANCELLED",false);
                rsResultSet.insertRow();
                
                
                //========= Inserting Into History =================//
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO",1);
                rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHistory.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsHistory.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
                rsHistory.updateString("CHILD_PARTY_ID",objChild.getAttribute("CHILD_PARTY_ID").getString());
                rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CHANGED",true);
                rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CANCELLED",false);
                
                rsHistory.insertRow();
                
            }
            
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
            }
            catch(Exception c){}
            //data.SetRollback();
            //data.SetAutoCommit(true);
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    
    
    public boolean Update() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
        
        try {
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_H WHERE PARENT_PARTY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_PARTY_GROUPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARENT_PARTY_ID='"+getAttribute("PARENT_PARTY_ID").getString()+"'");
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_PARTY_GROUPING_H WHERE PARENT_PARTY_ID='"+getAttribute("PARENT_PARTY_ID").getString()+"'");
            
            RevNo++;
            
            for(int i=1;i<=colChildPartyGrp.size();i++) {
                clsPolicyChildPartyGrp objChild=(clsPolicyChildPartyGrp) colChildPartyGrp.get(Integer.toString(i));
                
                long SrNo=data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_SAL_POLICY_PARTY_GROUPING","SR_NO");
                //--------- Generate New Internal Deposit Type ID  ------------
                setAttribute("SR_NO",SrNo);
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsResultSet.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
                rsResultSet.updateString("CHILD_PARTY_ID",objChild.getAttribute("CHILD_PARTY_ID").getString());
                rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CHANGED",true);
                rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CANCELLED",false);
                rsResultSet.insertRow();
                
                //========= Inserting Into History =================//
                
                
                String RevDocNo=getAttribute("PARENT_PARTY_ID").getString();
                //System.out.println("RevNo="+RevNo);
                //========= Inserting Into History =================//
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO",RevNo);
                rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHistory.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsHistory.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
                rsHistory.updateString("CHILD_PARTY_ID",objChild.getAttribute("CHILD_PARTY_ID").getString());
                rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
                rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
                
                rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CHANGED",true);
                rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CANCELLED",false);
                rsHistory.insertRow();
                
            }
            
            return true;
        }
        catch(Exception e) {
            try {
                
            }
            catch(Exception c){}
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean CanDelete(int CompanyID,String ParentPartyID,int UserID,int SrNo) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='"+ParentPartyID+"' AND SR_NO="+ SrNo +" ";
            int Count=data.getIntValueFromDB(strSQL);
            
            if(Count>0) {
                return true;
            }
            else {
                return false;
            }
            
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean Delete(int UserID) {
        try {
            String ParentPartyID=getAttribute("PARENT_PARTY_ID").getString();
            int SrNo = getAttribute("SR_NO").getInt();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,ParentPartyID,UserID,SrNo)) {
                String strSQL = "DELETE FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='"+ParentPartyID+"' AND SR_NO ="+SrNo;
                data.Execute(strSQL);
                
                LoadData(EITLERPGLOBAL.gCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted.";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int CompanyID, String ParentPartyID,int SrNo) {
        String strCondition = " WHERE PARENT_PARTY_ID='" + ParentPartyID + "' AND SR_NO="+SrNo;
        clsPolicyPartyGrouping_Old objGrouping = new clsPolicyPartyGrouping_Old();
        objGrouping.Filter(strCondition,CompanyID);
        return objGrouping;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_POLICY_PARTY_GROUPING " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_PARTY_GROUPING ORDER BY SR_NO ";
                rsResultSet=Stmt.executeQuery(strSQL);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                MoveLast();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        long Counter=0;
        int RevNo=0;
        
        try {
            
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("SR_NO",UtilFunctions.getInt(rsResultSet,"SR_NO", 0));
            setAttribute("PARENT_PARTY_ID",UtilFunctions.getString(rsResultSet,"PARENT_PARTY_ID", ""));
            //setAttribute("CHILD_PARTY_ID",UtilFunctions.getString(rsResultSet,"CHILD_PARTY_ID", ""));
            setAttribute("FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"FROM_DATE","0000-00-00")));
            setAttribute("TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"TO_DATE","0000-00-00")));
            
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            
            colChildPartyGrp.clear();
            
            String ParentId=getAttribute("PARENT_PARTY_ID").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PARENT_PARTY_ID='" + ParentId + "' ORDER BY SR_NO");
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicyChildPartyGrp objChild = new clsPolicyChildPartyGrp();
                
                objChild.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objChild.setAttribute("SR_NO",Counter);
                objChild.setAttribute("PARENT_PARTY_ID",UtilFunctions.getString(rsTmp,"PARENT_PARTY_ID",""));
                objChild.setAttribute("CHILD_PARTY_ID",UtilFunctions.getString(rsTmp,"CHILD_PARTY_ID",""));
                String Child_id=UtilFunctions.getString(rsTmp,"CHILD_PARTY_ID","");
                objChild.setAttribute("CHILD_PARTY_NAME",clsPolicyMaster.getPartyName(EITLERPGLOBAL.gCompanyID, Child_id));
                colChildPartyGrp.put(Long.toString(Counter),objChild);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
}
