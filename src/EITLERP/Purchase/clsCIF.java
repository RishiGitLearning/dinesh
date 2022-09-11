/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */
 
package EITLERP.Purchase;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
  
/**
 *
 * @author  nrpithva
 * @version
 */

public class clsCIF {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colCIFItems=new HashMap();
    
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
    
    
    /** Creates new clsBusinessObject */
    public clsCIF() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("CIF_NO",new Variant(""));
        props.put("CIF_DATE",new Variant(""));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("PO_TYPE",new Variant(5));
        props.put("SUBJECT",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("PURPOSE",new Variant(""));
        props.put("PRIORITY",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("DECISION",new Variant(""));
        props.put("CIF_COST",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CANCELLED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("COLUMN_1_ID",new Variant(0));
        props.put("COLUMN_1_FORMULA",new Variant(""));
        props.put("COLUMN_1_PER",new Variant(0));
        props.put("COLUMN_1_AMT",new Variant(0));
        props.put("COLUMN_1_CAPTION",new Variant(""));
        props.put("COLUMN_2_ID",new Variant(0));
        props.put("COLUMN_2_FORMULA",new Variant(""));
        props.put("COLUMN_2_PER",new Variant(0));
        props.put("COLUMN_2_AMT",new Variant(0));
        props.put("COLUMN_2_CAPTION",new Variant(""));
        props.put("COLUMN_3_ID",new Variant(0));
        props.put("COLUMN_3_FORMULA",new Variant(""));
        props.put("COLUMN_3_PER",new Variant(0));
        props.put("COLUMN_3_AMT",new Variant(0));
        props.put("COLUMN_3_CAPTION",new Variant(""));
        props.put("COLUMN_4_ID",new Variant(0));
        props.put("COLUMN_4_FORMULA",new Variant(""));
        props.put("COLUMN_4_PER",new Variant(0));
        props.put("COLUMN_4_AMT",new Variant(0));
        props.put("COLUMN_4_CAPTION",new Variant(""));
        props.put("COLUMN_5_ID",new Variant(0));
        props.put("COLUMN_5_FORMULA",new Variant(""));
        props.put("COLUMN_5_PER",new Variant(0));
        props.put("COLUMN_5_AMT",new Variant(0));
        props.put("COLUMN_5_CAPTION",new Variant(""));
        props.put("COLUMN_6_ID",new Variant(0));
        props.put("COLUMN_6_FORMULA",new Variant(""));
        props.put("COLUMN_6_PER",new Variant(0));
        props.put("COLUMN_6_AMT",new Variant(0));
        props.put("COLUMN_6_CAPTION",new Variant(""));
        props.put("COLUMN_7_ID",new Variant(0));
        props.put("COLUMN_7_FORMULA",new Variant(""));
        props.put("COLUMN_7_PER",new Variant(0));
        props.put("COLUMN_7_AMT",new Variant(0));
        props.put("COLUMN_7_CAPTION",new Variant(""));
        props.put("COLUMN_8_ID",new Variant(0));
        props.put("COLUMN_8_FORMULA",new Variant(""));
        props.put("COLUMN_8_PER",new Variant(0));
        props.put("COLUMN_8_AMT",new Variant(0));
        props.put("COLUMN_8_CAPTION",new Variant(""));
        props.put("COLUMN_9_ID",new Variant(0));
        props.put("COLUMN_9_FORMULA",new Variant(""));
        props.put("COLUMN_9_PER",new Variant(0));
        props.put("COLUMN_9_AMT",new Variant(0));
        props.put("COLUMN_9_CAPTION",new Variant(""));
        props.put("COLUMN_10_ID",new Variant(0));
        props.put("COLUMN_10_FORMULA",new Variant(""));
        props.put("COLUMN_10_PER",new Variant(0));
        props.put("COLUMN_10_AMT",new Variant(0));
        props.put("COLUMN_10_CAPTION",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        props.put("REVISION_NO",new Variant(0));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_CIF_HEADER WHERE COMPANY_ID="+pCompanyID+" AND CIF_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CIF_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' "); 
            Ready=true;
            HistoryView=false;
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
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_CIF_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND CIF_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsCIF ObjCIF=new clsCIF();
                
                ObjCIF.setAttribute("CIF_NO",rsTmp.getString("CIF_NO"));
                ObjCIF.setAttribute("CIF_DATE",rsTmp.getString("CIF_DATE"));
                ObjCIF.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjCIF.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjCIF.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjCIF.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjCIF.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjCIF);
            }
            
            rsTmp.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
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
        Statement stTmp,stIndent,stItem,stHeader,stHistory,stHDetail;
        ResultSet rsTmp,rsIndent,rsItem,rsHeader,rsHistory,rsHDetail;
        String strSQL="";
        int CompanyID=0;
        
        
        try {
            stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_CIF_HEADER WHERE CIF_NO='1' "); // 1 is used to minimize the data load
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("CIF_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0))
            {
               //Withing the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }
            //=====================================================//

            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_CIF_HEADER_H WHERE CIF_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_CIF_DETAIL_H WHERE CIF_NO='1'");
            rsHDetail.first();
            //------------------------------------//
        
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //--------- Generate New CIF No. ------------
            //setAttribute("CIF_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,35,(String)getAttribute("PREFIX").getObj(),true));
            //-------------------------------------------------
            
            rsHeader.first();
            rsHeader.moveToInsertRow();
            rsHeader.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHeader.updateString("CIF_NO",(String)getAttribute("CIF_NO").getObj());
            rsHeader.updateString("CIF_DATE",(String)getAttribute("CIF_DATE").getObj());
            rsHeader.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHeader.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHeader.updateInt("PO_TYPE",(int)getAttribute("PO_TYPE").getVal());
            rsHeader.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsHeader.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHeader.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHeader.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHeader.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            rsHeader.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHeader.updateString("DECISION",(String)getAttribute("DECISION").getObj());
            rsHeader.updateDouble("CIF_COST",getAttribute("CIF_COST").getVal());
            rsHeader.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHeader.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHeader.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            //Now Custom Columns
            rsHeader.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsHeader.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsHeader.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsHeader.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsHeader.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsHeader.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsHeader.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsHeader.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsHeader.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsHeader.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsHeader.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsHeader.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsHeader.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsHeader.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsHeader.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsHeader.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsHeader.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsHeader.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsHeader.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsHeader.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsHeader.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsHeader.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsHeader.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsHeader.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsHeader.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsHeader.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsHeader.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsHeader.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsHeader.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsHeader.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsHeader.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHeader.insertRow();


            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("CIF_NO",(String)getAttribute("CIF_NO").getObj());
            rsHistory.updateString("CIF_DATE",(String)getAttribute("CIF_DATE").getObj());
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateInt("PO_TYPE",(int)getAttribute("PO_TYPE").getVal());
            rsHistory.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("DECISION",(String)getAttribute("DECISION").getObj());
            rsHistory.updateDouble("CIF_COST",getAttribute("CIF_COST").getVal());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            //Now Custom Columns
            rsHistory.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHistory.insertRow();
            
            
            
            //====== Now turn of CIF Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_CIF_DETAIL WHERE CIF_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colCIFItems.size();i++) {
                clsCIFItem ObjItem=(clsCIFItem)colCIFItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("CIF_NO",(String)getAttribute("CIF_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsItem.updateInt("ORDER_MODE",(int)ObjItem.getAttribute("ORDER_MODE").getVal());
                rsItem.updateDouble("AIR_FREIGHT",ObjItem.getAttribute("AIR_FREIGHT").getVal());
                rsItem.updateDouble("GROSS_WEIGHT",ObjItem.getAttribute("GROSS_WEIGHT").getVal());
                rsItem.updateDouble("CARTONS",ObjItem.getAttribute("CARTONS").getVal());
                rsItem.updateDouble("VOLUME_CM",ObjItem.getAttribute("VOLUME_CM").getVal());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("CIF_NO",(String)getAttribute("CIF_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateInt("ORDER_MODE",(int)ObjItem.getAttribute("ORDER_MODE").getVal());
                rsHDetail.updateDouble("AIR_FREIGHT",ObjItem.getAttribute("AIR_FREIGHT").getVal());
                rsHDetail.updateDouble("GROSS_WEIGHT",ObjItem.getAttribute("GROSS_WEIGHT").getVal());
                rsHDetail.updateDouble("CARTONS",ObjItem.getAttribute("CARTONS").getVal());
                rsHDetail.updateDouble("VOLUME_CM",ObjItem.getAttribute("VOLUME_CM").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=35; //CIF Entry
            ObjFlow.DocNo=(String)getAttribute("CIF_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_CIF_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="CIF_NO";
            
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------//

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
        Statement stTmp,stIndent,stItem,stHeader,stHistory,stHDetail;
        ResultSet rsTmp,rsIndent,rsItem,rsHeader,rsHistory,rsHDetail;
        String strSQL="";
        int CompanyID=0;
                
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("CIF_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0))
            {
               //Withing the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }
            //=====================================================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_CIF_HEADER_H WHERE CIF_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_CIF_DETAIL_H WHERE CIF_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String CIFNo=(String)getAttribute("CIF_NO").getObj();
            
            stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_CIF_HEADER WHERE COMPANY_ID="+CompanyID+" AND CIF_NO='"+CIFNo+"'"); // 1 is used to minimize the data load
            rsHeader.first();
                        
            rsHeader.updateString("CIF_DATE",(String)getAttribute("CIF_DATE").getObj());
            rsHeader.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHeader.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHeader.updateInt("PO_TYPE",(int)getAttribute("PO_TYPE").getVal());
            rsHeader.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsHeader.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHeader.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHeader.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHeader.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            rsHeader.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHeader.updateString("DECISION",(String)getAttribute("DECISION").getObj());
            rsHeader.updateDouble("CIF_COST",getAttribute("CIF_COST").getVal());
            rsHeader.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHeader.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHeader.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHeader.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            //Now Custom Columns
            rsHeader.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsHeader.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsHeader.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsHeader.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsHeader.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsHeader.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsHeader.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsHeader.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsHeader.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsHeader.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsHeader.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsHeader.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsHeader.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsHeader.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsHeader.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsHeader.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsHeader.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsHeader.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsHeader.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsHeader.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsHeader.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsHeader.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsHeader.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsHeader.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsHeader.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsHeader.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsHeader.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsHeader.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsHeader.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsHeader.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHeader.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsHeader.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsHeader.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHeader.updateRow();
            

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_CIF_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND CIF_NO='"+(String)getAttribute("CIF_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("CIF_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("CIF_NO",(String)getAttribute("CIF_NO").getObj());
            rsHistory.updateString("CIF_DATE",(String)getAttribute("CIF_DATE").getObj());
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateInt("PO_TYPE",(int)getAttribute("PO_TYPE").getVal());
            rsHistory.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("DECISION",(String)getAttribute("DECISION").getObj());
            rsHistory.updateDouble("CIF_COST",getAttribute("CIF_COST").getVal());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            //Now Custom Columns
            rsHistory.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHistory.insertRow();

            
            
             //=== Delete previous rows ===
            
            data.Execute("DELETE FROM D_PUR_CIF_DETAIL WHERE COMPANY_ID="+CompanyID+" AND CIF_NO='"+CIFNo+"'");
            
            //====== Now turn of CIF Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_CIF_DETAIL WHERE CIF_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colCIFItems.size();i++) {
                clsCIFItem ObjItem=(clsCIFItem)colCIFItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("CIF_NO",(String)getAttribute("CIF_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsItem.updateInt("ORDER_MODE",(int)ObjItem.getAttribute("ORDER_MODE").getVal());
                rsItem.updateDouble("AIR_FREIGHT",ObjItem.getAttribute("AIR_FREIGHT").getVal());
                rsItem.updateDouble("GROSS_WEIGHT",ObjItem.getAttribute("GROSS_WEIGHT").getVal());
                rsItem.updateDouble("CARTONS",ObjItem.getAttribute("CARTONS").getVal());
                rsItem.updateDouble("VOLUME_CM",ObjItem.getAttribute("VOLUME_CM").getVal());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("CIF_NO",(String)getAttribute("CIF_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateInt("ORDER_MODE",(int)ObjItem.getAttribute("ORDER_MODE").getVal());
                rsHDetail.updateDouble("AIR_FREIGHT",ObjItem.getAttribute("AIR_FREIGHT").getVal());
                rsHDetail.updateDouble("GROSS_WEIGHT",ObjItem.getAttribute("GROSS_WEIGHT").getVal());
                rsHDetail.updateDouble("CARTONS",ObjItem.getAttribute("CARTONS").getVal());
                rsHDetail.updateDouble("VOLUME_CM",ObjItem.getAttribute("VOLUME_CM").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=35; //CIF Entry
            ObjFlow.DocNo=(String)getAttribute("CIF_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_CIF_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="CIF_NO";

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_CIF_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND CIF_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=35 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Nothing to do
                if(IsRejected) {
                    ObjFlow.Status="A";
                    ObjFlow.To=ObjFlow.From;
                    ObjFlow.UpdateFlow();
                    
                }
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------//

            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }        
    }
    
    private void RevertStockEffect() {
        Statement stItemMaster,stItem,stTmp;
        ResultSet rsItemMaster,rsItem,rsTmp;
        String strSQL="",PONo="",IndentNo="";
        int CompanyID=0,IndentSrNo=0;
        double Qty=0;
        
        try {
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            PONo=(String)getAttribute("PO_NO").getObj();
            
            //Now give reverse effect to Indent Table
            strSQL="SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE=1";
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                IndentNo=rsTmp.getString("INDENT_NO");
                IndentSrNo=rsTmp.getInt("INDENT_SR_NO");
                Qty=rsTmp.getDouble("QTY");
                
                // Update GRN Received Qty
                data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY-"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo.trim()+"' AND SR_NO="+IndentSrNo+" ");
                
                rsTmp.next();
            }
        }
        catch(Exception e) {
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("CIF_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                data.Execute("DELETE FROM D_PUR_CIF_HEADER WHERE COMPANY_ID="+lCompanyID+" AND CIF_NO='"+lDocNo.trim()+"'");
                data.Execute("DELETE FROM D_PUR_CIF_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND CIF_NO='"+lDocNo.trim()+"'");
                
                LoadData(lCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted. Either it is Approved/Rejected. Only creator of the document can delete.";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID,String pCIFNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND CIF_NO='"+pCIFNo+"'";
        clsCIF ObjCIF = new clsCIF();
        ObjCIF.Filter(strCondition,pCompanyID);
        return ObjCIF;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_CIF_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_PUR_CIF_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND CIF_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND CIF_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveFirst();
                return false;
            }
            else {
                Ready=true;
                MoveFirst();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean setData() {
        Statement stItem,stTmp;
        ResultSet rsItem,rsTmp;
        String CIFNo="";
        int CompanyID=0,ItemCounter=0,SrNo=0;
        int RevNo=0;
        
        try {
            
            if(HistoryView)
            {
               RevNo=rsResultSet.getInt("REVISION_NO"); 
               setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO")); 
            }
            else
            {
               setAttribute("REVISION_NO",0);  
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            setAttribute("CIF_NO",rsResultSet.getString("CIF_NO"));
            setAttribute("CIF_DATE",rsResultSet.getString("CIF_DATE"));
            setAttribute("PO_NO",rsResultSet.getString("PO_NO"));
            setAttribute("PO_DATE",rsResultSet.getString("PO_DATE"));
            setAttribute("PO_TYPE",rsResultSet.getInt("PO_TYPE"));
            setAttribute("SUBJECT",rsResultSet.getString("SUBJECT"));
            setAttribute("INVOICE_NO",rsResultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE",rsResultSet.getString("INVOICE_DATE"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("PRIORITY",rsResultSet.getInt("PRIORITY"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("DECISION",rsResultSet.getString("DECISION"));
            setAttribute("CIF_COST",rsResultSet.getDouble("CIF_COST"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("COLUMN_1_ID",rsResultSet.getInt("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA",rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER",rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT",rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION",rsResultSet.getString("COLUMN_1_CAPTION"));
            setAttribute("COLUMN_2_ID",rsResultSet.getInt("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA",rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER",rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT",rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION",rsResultSet.getString("COLUMN_2_CAPTION"));
            setAttribute("COLUMN_3_ID",rsResultSet.getInt("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA",rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER",rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT",rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION",rsResultSet.getString("COLUMN_3_CAPTION"));
            setAttribute("COLUMN_4_ID",rsResultSet.getInt("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA",rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER",rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT",rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION",rsResultSet.getString("COLUMN_4_CAPTION"));
            setAttribute("COLUMN_5_ID",rsResultSet.getInt("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA",rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER",rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT",rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION",rsResultSet.getString("COLUMN_5_CAPTION"));
            setAttribute("COLUMN_6_ID",rsResultSet.getInt("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA",rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER",rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT",rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION",rsResultSet.getString("COLUMN_6_CAPTION"));
            setAttribute("COLUMN_7_ID",rsResultSet.getInt("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA",rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER",rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT",rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION",rsResultSet.getString("COLUMN_7_CAPTION"));
            setAttribute("COLUMN_8_ID",rsResultSet.getInt("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA",rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER",rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT",rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION",rsResultSet.getString("COLUMN_8_CAPTION"));
            setAttribute("COLUMN_9_ID",rsResultSet.getInt("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA",rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER",rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT",rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION",rsResultSet.getString("COLUMN_9_CAPTION"));
            setAttribute("COLUMN_10_ID",rsResultSet.getInt("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA",rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER",rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT",rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION",rsResultSet.getString("COLUMN_10_CAPTION"));
            
            colCIFItems.clear();
            
            CIFNo=(String)getAttribute("CIF_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            if(HistoryView)
            {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_CIF_DETAIL_H WHERE COMPANY_ID="+CompanyID+" AND CIF_NO='"+CIFNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else
            {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_CIF_DETAIL WHERE COMPANY_ID="+CompanyID+" AND CIF_NO='"+CIFNo+"' ORDER BY SR_NO");
            }
            rsItem.first();
            
            ItemCounter=0;
            
            while((!rsItem.isAfterLast())&&(rsItem.getRow()>0)) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsCIFItem ObjItem=new clsCIFItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("CIF_NO",rsItem.getString("CIF_NO"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_DESC",rsItem.getString("ITEM_DESC"));
                ObjItem.setAttribute("ORDER_MODE",rsItem.getInt("ORDER_MODE"));
                ObjItem.setAttribute("AIR_FREIGHT",rsItem.getDouble("AIR_FREIGHT"));
                ObjItem.setAttribute("GROSS_WEIGHT",rsItem.getDouble("GROSS_WEIGHT"));
                ObjItem.setAttribute("CARTONS",rsItem.getDouble("CARTONS"));
                ObjItem.setAttribute("VOLUME_CM",rsItem.getDouble("VOLUME_CM"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
                ObjItem.setAttribute("TOTAL_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("CREATED_BY",rsItem.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsItem.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsItem.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsItem.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("COLUMN_1_ID",rsItem.getInt("COLUMN_1_ID"));
                ObjItem.setAttribute("COLUMN_1_FORMULA",rsItem.getString("COLUMN_1_FORMULA"));
                ObjItem.setAttribute("COLUMN_1_PER",rsItem.getDouble("COLUMN_1_PER"));
                ObjItem.setAttribute("COLUMN_1_AMT",rsItem.getDouble("COLUMN_1_AMT"));
                ObjItem.setAttribute("COLUMN_1_CAPTION",rsItem.getString("COLUMN_1_CAPTION"));
                ObjItem.setAttribute("COLUMN_2_ID",rsItem.getInt("COLUMN_2_ID"));
                ObjItem.setAttribute("COLUMN_2_FORMULA",rsItem.getString("COLUMN_2_FORMULA"));
                ObjItem.setAttribute("COLUMN_2_PER",rsItem.getDouble("COLUMN_2_PER"));
                ObjItem.setAttribute("COLUMN_2_AMT",rsItem.getDouble("COLUMN_2_AMT"));
                ObjItem.setAttribute("COLUMN_2_CAPTION",rsItem.getString("COLUMN_2_CAPTION"));
                ObjItem.setAttribute("COLUMN_3_ID",rsItem.getInt("COLUMN_3_ID"));
                ObjItem.setAttribute("COLUMN_3_FORMULA",rsItem.getString("COLUMN_3_FORMULA"));
                ObjItem.setAttribute("COLUMN_3_PER",rsItem.getDouble("COLUMN_3_PER"));
                ObjItem.setAttribute("COLUMN_3_AMT",rsItem.getDouble("COLUMN_3_AMT"));
                ObjItem.setAttribute("COLUMN_3_CAPTION",rsItem.getString("COLUMN_3_CAPTION"));
                ObjItem.setAttribute("COLUMN_4_ID",rsItem.getInt("COLUMN_4_ID"));
                ObjItem.setAttribute("COLUMN_4_FORMULA",rsItem.getString("COLUMN_4_FORMULA"));
                ObjItem.setAttribute("COLUMN_4_PER",rsItem.getDouble("COLUMN_4_PER"));
                ObjItem.setAttribute("COLUMN_4_AMT",rsItem.getDouble("COLUMN_4_AMT"));
                ObjItem.setAttribute("COLUMN_4_CAPTION",rsItem.getString("COLUMN_4_CAPTION"));
                ObjItem.setAttribute("COLUMN_5_ID",rsItem.getInt("COLUMN_5_ID"));
                ObjItem.setAttribute("COLUMN_5_FORMULA",rsItem.getString("COLUMN_5_FORMULA"));
                ObjItem.setAttribute("COLUMN_5_PER",rsItem.getDouble("COLUMN_5_PER"));
                ObjItem.setAttribute("COLUMN_5_AMT",rsItem.getDouble("COLUMN_5_AMT"));
                ObjItem.setAttribute("COLUMN_5_CAPTION",rsItem.getString("COLUMN_5_CAPTION"));
                ObjItem.setAttribute("COLUMN_6_ID",rsItem.getInt("COLUMN_6_ID"));
                ObjItem.setAttribute("COLUMN_6_FORMULA",rsItem.getString("COLUMN_6_FORMULA"));
                ObjItem.setAttribute("COLUMN_6_PER",rsItem.getDouble("COLUMN_6_PER"));
                ObjItem.setAttribute("COLUMN_6_AMT",rsItem.getDouble("COLUMN_6_AMT"));
                ObjItem.setAttribute("COLUMN_6_CAPTION",rsItem.getString("COLUMN_6_CAPTION"));
                ObjItem.setAttribute("COLUMN_7_ID",rsItem.getInt("COLUMN_7_ID"));
                ObjItem.setAttribute("COLUMN_7_FORMULA",rsItem.getString("COLUMN_7_FORMULA"));
                ObjItem.setAttribute("COLUMN_7_PER",rsItem.getDouble("COLUMN_7_PER"));
                ObjItem.setAttribute("COLUMN_7_AMT",rsItem.getDouble("COLUMN_7_AMT"));
                ObjItem.setAttribute("COLUMN_7_CAPTION",rsItem.getString("COLUMN_7_CAPTION"));
                ObjItem.setAttribute("COLUMN_8_ID",rsItem.getInt("COLUMN_8_ID"));
                ObjItem.setAttribute("COLUMN_8_FORMULA",rsItem.getString("COLUMN_8_FORMULA"));
                ObjItem.setAttribute("COLUMN_8_PER",rsItem.getDouble("COLUMN_8_PER"));
                ObjItem.setAttribute("COLUMN_8_AMT",rsItem.getDouble("COLUMN_8_AMT"));
                ObjItem.setAttribute("COLUMN_8_CAPTION",rsItem.getString("COLUMN_8_CAPTION"));
                ObjItem.setAttribute("COLUMN_9_ID",rsItem.getInt("COLUMN_9_ID"));
                ObjItem.setAttribute("COLUMN_9_FORMULA",rsItem.getString("COLUMN_9_FORMULA"));
                ObjItem.setAttribute("COLUMN_9_PER",rsItem.getDouble("COLUMN_9_PER"));
                ObjItem.setAttribute("COLUMN_9_AMT",rsItem.getDouble("COLUMN_9_AMT"));
                ObjItem.setAttribute("COLUMN_9_CAPTION",rsItem.getString("COLUMN_9_CAPTION"));
                ObjItem.setAttribute("COLUMN_10_ID",rsItem.getInt("COLUMN_10_ID"));
                ObjItem.setAttribute("COLUMN_10_FORMULA",rsItem.getString("COLUMN_10_FORMULA"));
                ObjItem.setAttribute("COLUMN_10_PER",rsItem.getDouble("COLUMN_10_PER"));
                ObjItem.setAttribute("COLUMN_10_AMT",rsItem.getDouble("COLUMN_10_AMT"));
                ObjItem.setAttribute("COLUMN_10_CAPTION",rsItem.getString("COLUMN_10_CAPTION"));
                
                colCIFItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    

    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView)
            {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_CIF_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND CIF_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=35 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    //Yes document is waiting for this user
                    return true;
                }
                else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView)
            {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_CIF_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND CIF_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=35 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    //Yes document is waiting for this user
                    return true;
                }
                else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int FinYearFrom)    
{
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
      
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            strSQL="SELECT D_PUR_CIF_HEADER.CIF_NO,D_PUR_CIF_HEADER.CIF_DATE FROM D_PUR_CIF_HEADER,D_COM_DOC_DATA WHERE D_PUR_CIF_HEADER.CIF_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_CIF_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_CIF_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=35";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
            if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("CIF_DATE"),FinYearFrom))    
            {
                
            Counter=Counter+1;
            clsCIF ObjDoc=new clsCIF();
                
            //------------- Header Fields --------------------//
            ObjDoc.setAttribute("CIF_NO",rsTmp.getString("CIF_NO"));
            ObjDoc.setAttribute("CIF_DATE",rsTmp.getString("CIF_DATE"));
            // ----------------- End of Header Fields ------------------------------------//
            
            //Put the prepared user object into list    
            List.put(Long.toString(Counter),ObjDoc);
            }
            
            if(!rsTmp.isAfterLast())
            {
            rsTmp.next();
            }
           }//Out While
        }
        catch(Exception e)
        {
        }
        
      return List; 
}


    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_CIF_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND CIF_NO='"+pDocNo+"'");
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

    
}
