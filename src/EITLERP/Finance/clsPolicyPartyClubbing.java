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

public class clsPolicyPartyClubbing{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colPartyGrp=new HashMap();
    
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
    public clsPolicyPartyClubbing() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("PARTY_ID",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        
        colPartyGrp=new HashMap();
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DOC_NO");
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
                int DocNo = UtilFunctions.getInt(rsResultSet,"DOC_NO", 0);
                
                while(!rsResultSet.isAfterLast()) {
                    int Doc_No = UtilFunctions.getInt(rsResultSet,"DOC_NO", 0);
                    if (DocNo==Doc_No) {
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
                int DocNo = UtilFunctions.getInt(rsResultSet,"DOC_NO", 0);
                
                while(!rsResultSet.isBeforeFirst()) {
                    int Doc_No = UtilFunctions.getInt(rsResultSet,"DOC_NO", 0);
                    if (DocNo==Doc_No) {
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING_H WHERE PARTY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            long DocNo=data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_SAL_POLICY_PARTY_CLUBBING","DOC_NO");
                //--------- Generate New Internal Deposit Type ID  ------------
             setAttribute("DOC_NO",DocNo);
                
            for(int i=1;i<=colPartyGrp.size();i++) {
                clsPolicyPartyClubbing objParty=(clsPolicyPartyClubbing) colPartyGrp.get(Integer.toString(i));
                
                
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("DOC_NO",getAttribute("DOC_NO").getInt());
                rsResultSet.updateInt("SR_NO",i);
                rsResultSet.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                
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
                rsHistory.updateInt("DOC_NO",getAttribute("DOC_NO").getInt());
                rsHistory.updateInt("SR_NO",i);
                rsHistory.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                
                
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING_H WHERE PARTY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            int DocNo = getAttribute("DOC_NO").getInt();
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_PARTY_CLUBBING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+DocNo+" ");
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_PARTY_CLUBBING_H WHERE DOC_NO="+DocNo+"");
            
            RevNo++;
            
            for(int i=1;i<=colPartyGrp.size();i++) {
                clsPolicyPartyClubbing objParty=(clsPolicyPartyClubbing) colPartyGrp.get(Integer.toString(i));
                
                setAttribute("SR_NO",getAttribute("DOC_NO").getInt());
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("DOC_NO",DocNo);
                rsResultSet.updateInt("SR_NO",i);
                rsResultSet.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                
                rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CHANGED",true);
                rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CANCELLED",false);
                rsResultSet.insertRow();
                
                //========= Inserting Into History =================//
                
                
                String RevDocNo=getAttribute("PARTY_ID").getString();
                //System.out.println("RevNo="+RevNo);
                //========= Inserting Into History =================//
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO",RevNo);
                rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHistory.updateInt("DOC_NO",getAttribute("DOC_NO").getInt());
                rsHistory.updateInt("SR_NO",i);
                rsHistory.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                
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
    
    
    public boolean CanDelete(int CompanyID,int UserID,int DocNo) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_PARTY_CLUBBING WHERE DOC_NO="+ DocNo +" AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+"";
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
            int DocNo = getAttribute("DOC_NO").getInt();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,UserID,DocNo)) {
                String strSQL = "DELETE FROM D_SAL_POLICY_PARTY_CLUBBING WHERE DOC_NO ="+DocNo + " AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+"";
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
    
    
    public Object getObject(int CompanyID, int DocNo) {
        String strCondition = " WHERE  DOC_NO="+DocNo;
        clsPolicyPartyClubbing objClubbing = new clsPolicyPartyClubbing();
        objClubbing.Filter(strCondition,CompanyID);
        return objClubbing;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DOC_NO ";
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
            setAttribute("DOC_NO",UtilFunctions.getInt(rsResultSet,"DOC_NO", 0));
            setAttribute("SR_NO",UtilFunctions.getInt(rsResultSet,"SR_NO", 0));
            setAttribute("FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"FROM_DATE","0000-00-00")));
            setAttribute("TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"TO_DATE","0000-00-00")));
            
            
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            
            colPartyGrp.clear();
            
            int DocNo=getAttribute("DOC_NO").getInt();
            
            tmpStmt=tmpConn.createStatement();
            
            String str = "SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND DOC_NO=" + DocNo + " ";
            rsTmp=tmpStmt.executeQuery(str);
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicyPartyClubbing objParty = new clsPolicyPartyClubbing();
                
                objParty.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objParty.setAttribute("SR_NO",Counter);
                objParty.setAttribute("PARTY_ID",UtilFunctions.getString(rsTmp,"PARTY_ID",""));
                String Party_id=UtilFunctions.getString(rsTmp,"PARTY_ID","");
                objParty.setAttribute("PARTY_NAME",clsPolicyMaster.getPartyName(EITLERPGLOBAL.gCompanyID, Party_id));
                colPartyGrp.put(Long.toString(Counter),objParty);
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
