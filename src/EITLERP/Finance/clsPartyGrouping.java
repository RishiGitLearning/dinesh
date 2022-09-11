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

public class clsPartyGrouping {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap colSubPartyGroup=new HashMap();
    private boolean HistoryView=false;
    
    
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
    public clsPartyGrouping() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("INVOICE_TYPE",new Variant(0));
        props.put("GROUP_MAIN_PARTY",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_HEADER ORDER BY GROUP_MAIN_PARTY");
            HistoryView=false;
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
            } else {
                rsResultSet.next();
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        } catch(Exception e) {
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
                rsResultSet.previous();
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
        
        Statement stHistory,stHeader,stHDetail,stDetail;
        ResultSet rsHistory,rsHeader,rsTmp,rsHDetail,rsDetail;
        int CompanyID=0;
        try {
            
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_HEADER_H LIMIT 1"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_DETAIL_H LIMIT 1");
            rsHDetail.first();
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateInt("INVOICE_TYPE",getAttribute("INVOICE_TYPE").getInt());
            rsResultSet.updateString("GROUP_MAIN_PARTY",getAttribute("GROUP_MAIN_PARTY").getString());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsHistory.updateInt("INVOICE_TYPE",getAttribute("INVOICE_TYPE").getInt());
            rsHistory.updateString("GROUP_MAIN_PARTY",getAttribute("GROUP_MAIN_PARTY").getString());
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_DETAIL LIMIT 1");
            rsDetail.first();
            
            for(int i=1;i<=colSubPartyGroup.size();i++) {
                clsPartyGroupingDetail ObjPartyGroupingDetail=(clsPartyGroupingDetail)colSubPartyGroup.get(Integer.toString(i));
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",ObjPartyGroupingDetail.getAttribute("COMPANY_ID").getInt());
                rsDetail.updateInt("SR_NO",ObjPartyGroupingDetail.getAttribute("SR_NO").getInt());
                rsDetail.updateInt("INVOICE_TYPE",ObjPartyGroupingDetail.getAttribute("INVOICE_TYPE").getInt());
                rsDetail.updateString("GROUP_MAIN_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_MAIN_PARTY").getString());
                rsDetail.updateString("GROUP_SUB_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_SUB_PARTY").getString());
                rsDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",ObjPartyGroupingDetail.getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateInt("SR_NO",ObjPartyGroupingDetail.getAttribute("SR_NO").getInt());
                rsHDetail.updateInt("INVOICE_TYPE",ObjPartyGroupingDetail.getAttribute("INVOICE_TYPE").getInt());
                rsHDetail.updateString("GROUP_MAIN_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_MAIN_PARTY").getString());
                rsHDetail.updateString("GROUP_SUB_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_SUB_PARTY").getString());
                rsHDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsHDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            return true;
        } catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        Statement stHistory,stHeader,stHDetail,stDetail;
        ResultSet rsHistory,rsHeader,rsTmp,rsHDetail,rsDetail;
        int CompanyID=0;
        boolean Validate=true;
        try {
            
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_HEADER_H WHERE GROUP_MAIN_PARTY='1' "); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_DETAIL_H WHERE GROUP_MAIN_PARTY='1'");
            rsHDetail.first();
            
            rsResultSet.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateInt("INVOICE_TYPE",getAttribute("INVOICE_TYPE").getInt());
            rsResultSet.updateString("GROUP_MAIN_PARTY",getAttribute("GROUP_MAIN_PARTY").getString());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_PARTY_GROUPING_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GROUP_MAIN_PARTY='"+getAttribute("GROUP_MAIN_PARTY").getString()+"' ",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("GROUP_MAIN_PARTY").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsHistory.updateInt("INVOICE_TYPE",getAttribute("INVOICE_TYPE").getInt());
            rsHistory.updateString("GROUP_MAIN_PARTY",getAttribute("GROUP_MAIN_PARTY").getString());
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            data.Execute("DELETE FROM D_FIN_PARTY_GROUPING_DETAIL WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" " +
            "AND GROUP_MAIN_PARTY='"+getAttribute("GROUP_MAIN_PARTY").getString()+"' ",FinanceGlobal.FinURL);
            
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_DETAIL WHERE GROUP_MAIN_PARTY='1'");
            rsDetail.first();
            
            for(int i=1;i<=colSubPartyGroup.size();i++) {
                clsPartyGroupingDetail ObjPartyGroupingDetail=(clsPartyGroupingDetail)colSubPartyGroup.get(Integer.toString(i));
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",ObjPartyGroupingDetail.getAttribute("COMPANY_ID").getInt());
                rsDetail.updateInt("SR_NO",ObjPartyGroupingDetail.getAttribute("SR_NO").getInt());
                rsDetail.updateInt("INVOICE_TYPE",ObjPartyGroupingDetail.getAttribute("INVOICE_TYPE").getInt());
                rsDetail.updateString("GROUP_MAIN_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_MAIN_PARTY").getString());
                rsDetail.updateString("GROUP_SUB_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_SUB_PARTY").getString());
                rsDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",ObjPartyGroupingDetail.getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateInt("SR_NO",ObjPartyGroupingDetail.getAttribute("SR_NO").getInt());
                rsHDetail.updateInt("INVOICE_TYPE",ObjPartyGroupingDetail.getAttribute("INVOICE_TYPE").getInt());
                rsHDetail.updateString("GROUP_MAIN_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_MAIN_PARTY").getString());
                rsHDetail.updateString("GROUP_SUB_PARTY",ObjPartyGroupingDetail.getAttribute("GROUP_SUB_PARTY").getString());
                rsHDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsHDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int CompanyID, String GroupMainParty) {
        String strCondition = " WHERE GROUP_MAIN_PARTY='" + GroupMainParty + "' ";
        clsPartyGrouping objGrouping = new clsPartyGrouping();
        objGrouping.Filter(strCondition,CompanyID);
        return objGrouping;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
        String strSQL = "SELECT * FROM D_FIN_PARTY_GROUPING_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_PARTY_GROUPING ORDER BY GROUP_MAIN_PARTY ";
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
    
    public boolean GroupDelete() {
        try {
            String GroupMainParty = getAttribute("GROUP_MAIN_PARTY").getString();
            String stmt = "DELETE FROM D_FIN_PARTY_GROUPING_HEADER WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            stmt = "DELETE FROM D_FIN_PARTY_GROUPING_HEADER_H WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            stmt = "DELETE FROM D_FIN_PARTY_GROUPING_DETAIL WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            stmt = "DELETE FROM D_FIN_PARTY_GROUPING_DETAIL_H WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        ResultSet rsTmp,rsDetail;
        Connection tmpConn;
        Statement tmpStmt,rstDetail;
        int CompanyID=0;
        tmpConn=data.getConn();
        
        long Counter=0;
        int RevNo=0;
        
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("INVOICE_TYPE",rsResultSet.getInt("INVOICE_TYPE"));
            setAttribute("GROUP_MAIN_PARTY",rsResultSet.getString("GROUP_MAIN_PARTY"));
            setAttribute("CREATED_BY",rsResultSet.getString("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getString("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("CHANGED",rsResultSet.getString("CHANGED"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            
            colSubPartyGroup.clear(); //Clear existing data
            String MainPartyCode=getAttribute("GROUP_MAIN_PARTY").getString();
            
            rstDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=rstDetail.executeQuery("SELECT * FROM D_FIN_PARTY_GROUPING_DETAIL WHERE GROUP_MAIN_PARTY='"+MainPartyCode+"' ORDER BY SR_NO");
            rsDetail.first();
            while(!rsDetail.isAfterLast()&&rsDetail.getRow()>0) {
                clsPartyGroupingDetail ObjPartyGroupingDetail=new clsPartyGroupingDetail();
                ObjPartyGroupingDetail.setAttribute("COMPANY_ID",rsDetail.getInt("COMPANY_ID"));
                ObjPartyGroupingDetail.setAttribute("INVOICE_TYPE",rsDetail.getInt("INVOICE_TYPE"));
                ObjPartyGroupingDetail.setAttribute("SR_NO",rsDetail.getInt("SR_NO"));
                ObjPartyGroupingDetail.setAttribute("GROUP_MAIN_PARTY",rsDetail.getString("GROUP_MAIN_PARTY"));
                ObjPartyGroupingDetail.setAttribute("GROUP_SUB_PARTY",rsDetail.getString("GROUP_SUB_PARTY"));
                ObjPartyGroupingDetail.setAttribute("CREATED_BY",rsDetail.getString("CREATED_BY"));
                ObjPartyGroupingDetail.setAttribute("CREATED_DATE",rsDetail.getString("CREATED_DATE"));
                ObjPartyGroupingDetail.setAttribute("MODIFIED_BY",rsDetail.getString("MODIFIED_BY"));
                ObjPartyGroupingDetail.setAttribute("MODIFIED_DATE",rsDetail.getString("MODIFIED_DATE"));
                ObjPartyGroupingDetail.setAttribute("CHANGED",rsDetail.getString("CHANGED"));
                ObjPartyGroupingDetail.setAttribute("CHANGED_DATE",rsDetail.getString("CHANGED_DATE"));
                colSubPartyGroup.put(Integer.toString(colSubPartyGroup.size()+1),ObjPartyGroupingDetail);
                rsDetail.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean Delete() {
        try {
            String GroupMainParty = getAttribute("GROUP_MAIN_PARTY").getString();
            String stmt = "DELETE FROM D_FIN_PARTY_GROUPING_HEADER WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            stmt = "DELETE FROM D_FIN_PARTY_GROUPING_HEADER_H WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            stmt = "DELETE FROM D_FIN_PARTY_GROUPING_DETAIL WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            stmt = "DELETE FROM D_FIN_PARTY_GROUPING_DETAIL_H WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' ";
            data.Execute(stmt,FinanceGlobal.FinURL);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
}
