/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsFASItemMapping {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colFASItem;
    static int ModuleID = 188;
    
    //History Related properties
    private boolean HistoryView=false;
    
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
    
    
    
    
    /** Creates new clsMaterialRequisition */
    public clsFASItemMapping() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("ITEM_ID", new Variant(""));
        props.put("METHOD_ID",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CANCELLED",new Variant(false));
        props.put("FFNO", new Variant(""));
        colFASItem=new HashMap();
        
        
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" ORDER BY DOC_NO");
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
            }
            else {
                rsResultSet.next();
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
        
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_HEADER_H LIMIT 1"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_DETAIL_H LIMIT 1");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASItemMapping.ModuleID, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("APPROVED",true);
            rsResultSet.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            //rsResultSet.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            //rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",getAttribute("CREATED_BY").getInt());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsHistory.updateBoolean("APPROVED",true);
            rsHistory.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            //rsHistory.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            //rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_DETAIL LIMIT 1");
            
            long nCompanyID=getAttribute("COMPANY_ID").getInt();
            
            //Now Insert records into detail table
            for(int i=1;i<=colFASItem.size();i++) {
                clsFASItem ObjItems=(clsFASItem) colFASItem.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("METHOD_ID",ObjItems.getAttribute("METHOD_ID").getString());
                rsTmp.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("FROM_DATE").getString()));
                rsTmp.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("TO_DATE").getString()));
                rsTmp.updateDouble("BOOK_PER",ObjItems.getAttribute("BOOK_PER").getDouble());
                rsTmp.updateDouble("IT_PER",ObjItems.getAttribute("IT_PER").getDouble());
                rsTmp.updateString("REMARKS",ObjItems.getAttribute("REMARKS").getString());
                rsTmp.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
//                rsTmp.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
//                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
//                
                
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("UPDATED_BY",getAttribute("CREATED_BY").getInt());
                rsHDetail.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("METHOD_ID",ObjItems.getAttribute("METHOD_ID").getString());
                rsHDetail.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("FROM_DATE").getString()));
                rsHDetail.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("TO_DATE").getString()));
                rsHDetail.updateDouble("BOOK_PER",ObjItems.getAttribute("BOOK_PER").getDouble());
                rsHDetail.updateDouble("IT_PER",ObjItems.getAttribute("IT_PER").getDouble());
                rsHDetail.updateString("REMARKS",ObjItems.getAttribute("REMARKS").getString());
                rsHDetail.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
//                rsHDetail.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
//                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.insertRow();
            }
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        boolean Validate=true;
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=getAttribute("DOC_NO").getString();
            
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("APPROVED",true);
            rsResultSet.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            rsResultSet.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
//            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FAS_ITEM_MASTER_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("DOC_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("ITEM_ID",getAttribute("ITEM_ID").getString());
//            rsHistory.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
//            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("APPROVED",true);
            rsHistory.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            data.Execute("DELETE FROM D_FAS_ITEM_MASTER_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_DETAIL WHERE DOC_NO='1'");
            
            //Now Insert records into detail table
            for(int i=1;i<=colFASItem.size();i++) {
                clsFASItem ObjItems=(clsFASItem) colFASItem.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsTmp.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("METHOD_ID",ObjItems.getAttribute("METHOD_ID").getString());
                rsTmp.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("FROM_DATE").getString()));
                rsTmp.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("TO_DATE").getString()));
                rsTmp.updateDouble("BOOK_PER",ObjItems.getAttribute("BOOK_PER").getDouble());
                rsTmp.updateDouble("IT_PER",ObjItems.getAttribute("IT_PER").getDouble());
                rsTmp.updateString("REMARKS",ObjItems.getAttribute("REMARKS").getString());
//                rsTmp.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
//                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsTmp.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("UPDATED_BY",getAttribute("MODIFIED_BY").getInt());
                rsHDetail.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("METHOD_ID",ObjItems.getAttribute("METHOD_ID").getString());
                rsHDetail.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("FROM_DATE").getString()));
                rsHDetail.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(ObjItems.getAttribute("TO_DATE").getString()));
                rsHDetail.updateDouble("BOOK_PER",ObjItems.getAttribute("BOOK_PER").getDouble());
                rsHDetail.updateDouble("IT_PER",ObjItems.getAttribute("IT_PER").getDouble());
                rsHDetail.updateString("REMARKS",ObjItems.getAttribute("REMARKS").getString());
//                rsHDetail.updateLong("CREATED_BY",getAttribute("CREATED_BY").getInt());
//                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
                rsHDetail.updateLong("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
                rsHDetail.insertRow();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
 
    
    public Object getObject(int pCompanyID, String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND DOC_NO='" + pDocNo + "'" ;
        clsFASItemMapping ObjFASItemMapping = new clsFASItemMapping();
        ObjFASItemMapping.Filter(strCondition,pCompanyID);
        return ObjFASItemMapping;
    }
   
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_FAS_ITEM_MASTER_HEADER " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FAS_ITEM_MASTER_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            } else {
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
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        long Counter=0;
        int RevNo=0;
        
        try {
            
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet, "COMPANY_ID", 0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet, "DOC_NO", ""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsResultSet, "DOC_DATE","0000-00-00"));
            setAttribute("ITEM_ID",UtilFunctions.getString(rsResultSet, "ITEM_ID", ""));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet, "REMARKS", ""));
            setAttribute("CREATED_BY",UtilFunctions.getInt(rsResultSet, "CREATED_BY", 0));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet, "CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getInt(rsResultSet, "MODIFIED_BY",0));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet, "MODIFIED_DATE","0000-00-00"));
            setAttribute("APPROVED",UtilFunctions.getBoolean(rsResultSet, "APPROVED",false));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet, "APPROVED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet, "CANCELLED",false));
            
            //Now Populate the collection
            //first clear the collection
            colFASItem.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mReqNo=(String) getAttribute("DOC_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_DETAIL_H WHERE COMPANY_ID=" + mCompanyID + " AND DOC_NO='" + mReqNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_DETAIL WHERE COMPANY_ID=" + mCompanyID + " AND DOC_NO='" + mReqNo + "' ORDER BY SR_NO");
            }
            rsTmp.first();
            
            Counter=0;
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter=Counter+1;
                    clsFASItem ObjFASItems = new clsFASItem();
                    ObjFASItems.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    ObjFASItems.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                    ObjFASItems.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                    ObjFASItems.setAttribute("METHOD_ID",UtilFunctions.getString(rsTmp,"METHOD_ID",""));
                    ObjFASItems.setAttribute("FROM_DATE",UtilFunctions.getString(rsTmp,"FROM_DATE","0000-00-00"));
                    ObjFASItems.setAttribute("TO_DATE",UtilFunctions.getString(rsTmp,"TO_DATE","0000-00-00"));
                    ObjFASItems.setAttribute("BOOK_PER",UtilFunctions.getDouble(rsTmp,"BOOK_PER",0));
                    ObjFASItems.setAttribute("IT_PER",UtilFunctions.getDouble(rsTmp,"IT_PER",0));
                    ObjFASItems.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                    ObjFASItems.setAttribute("CREATED_BY",UtilFunctions.getInt(rsTmp,"CREATED_BY",0));
                    ObjFASItems.setAttribute("CREATED_DATE",UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00"));
                    ObjFASItems.setAttribute("MODIFIED_BY",UtilFunctions.getInt(rsTmp,"MODIFIED_BY",0));
                    ObjFASItems.setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00"));
                    colFASItem.put(Long.toString(Counter),ObjFASItems);
                    rsTmp.next();
                }
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
 
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FAS_ITEM_MASTER_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsFASItemMapping ObjFASItemMapping=new clsFASItemMapping();
                
                ObjFASItemMapping.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjFASItemMapping.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjFASItemMapping.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFASItemMapping.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFASItemMapping.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFASItemMapping.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjFASItemMapping);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
        }
    }
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELED FROM D_FAS_ITEM_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELED")) {
                        strMessage="Document is cancelled";
                    }
                    else {
                        strMessage="";
                    }
                }
                else {
                    strMessage="Document is created but is under approval";
                }
                
            }
            else {
                strMessage="Document does not exist";
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return strMessage;
        
    }
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FAS_ITEM_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND CANCELED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
            
        }
        
        return canCancel;
        
    }
    
    
    public static boolean CancelDoc(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pDocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FAS_ITEM_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=2;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_FAS_ITEM_MASTER_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public static String getItemDesc(int pCompanyID,String pDocNo,String Item_Id,String SrNo) {
        ResultSet rsTmp;
        String Desc="";
        
        try {
            //First check that Document exist
            String str = "SELECT ITEM_EXTRA_DESC FROM D_FAS_ITEM_MASTER_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND ITEM_ID='"+Item_Id+"' AND SR_NO='"+SrNo+"' ";
            rsTmp=data.getResult(str);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Desc = rsTmp.getString("ITEM_EXTRA_DESC");
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return Desc;
        
    }
}
