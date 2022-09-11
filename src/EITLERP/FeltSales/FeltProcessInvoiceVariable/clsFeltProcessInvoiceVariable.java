/*
 * clsFeltProcessInvoiceVariable.java
 *
 * Created on July 19, 2013, 5:31 PM
 */

package EITLERP.FeltSales.FeltProcessInvoiceVariable;

import java.util.HashMap;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;


public class clsFeltProcessInvoiceVariable {


    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int FFNo=0;
    public HashMap props;
    public boolean Ready = false;
    public HashMap hmFeltReopenBaleDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    
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
    
    /** Creates new DataFeltPacking */
    public clsFeltProcessInvoiceVariable() {
        LastError = "";
        props=new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("STATION", new Variant(""));
        props.put("TRANSPORT_MODE", new Variant(""));
        props.put("MODE_REOPEN_BALE", new Variant(""));
        props.put("INSURANCE_CODE", new Variant(false));
        props.put("TRANSPORTER_CODE", new Variant(""));
        props.put("BOX_SIZE", new Variant(""));
        props.put("F6", new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("UPDATED_BY",new Variant(0));
        props.put("ENTRY_DATE",new Variant(""));
        props.put("APPROVER_REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("REJECTED",new Variant(0));
        
        hmFeltReopenBaleDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT GROUP_CONCAT(BALE_NO),DOC_NO,DOC_DATE,PARTY_CODE,PARTY_NAME,BALE_DATE,CHARGE_CODE,REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,F6 FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER GROUP BY DOC_NO;");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public void Close() {
        try {
            statement.close();
            resultSet.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean MoveFirst() {
        try {
            resultSet.first();
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MoveNext() {
        try {
            if(resultSet.isAfterLast()||resultSet.isLast()) {
                //Move pointer at last record if it is beyond eof
                resultSet.last();
            }else {
                resultSet.next();
                if(resultSet.isAfterLast()) {
                    resultSet.last();
                }
            }
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MovePrevious() {
        try {
            if(resultSet.isFirst()||resultSet.isBeforeFirst()) {
                resultSet.first();
            }else {
                resultSet.previous();
                if(resultSet.isBeforeFirst()) {
                    resultSet.first();
                }
            }
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            resultSet.last();
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Insert() {
        ResultSet  resultSetDetail,resultSetH, resultSetDetailHistory, resultSetHistory;
        Statement  statementDetail,statementH, statementDetailHistory, statementHistory;
        try {
            
            String baleDate = "";
            
            // Packing data history connection
            statementH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statementH.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO=''");
            
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER_H WHERE DOC_NO=''");
            
            
             //--------- Generate Bale no. ---------------------
            setAttribute("DOC_NO",EITLERP.clsFirstFree.getNextFreeNo(2, 765, getAttribute("FFNO").getInt(), true));
            //-------------------------------------------------
            
            String str1 = getAttribute("BALE_NO").getString();
            String[] bno = str1.split(",");
            
            for (int i=0;i<bno.length;i++){
                bno[i] = bno[i].replaceAll("[^\\w]", ",");
                System.out.println("BALE_NO : "+bno[i]);
                if(bno[i]=="")
                {
                    bno[i] = "0";
                }
                
                baleDate = data.getStringValueFromDB("SELECT PKG_BALE_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+bno[i]+"' AND APPROVED=1 AND CANCELED=0 AND BALE_REOPEN_FLG=0 AND INVOICE_FLG=0 ORDER BY PKG_BALE_DATE DESC");
            
           
            
            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSet.updateString("BALE_NO", bno[i]);
            resultSet.updateString("BALE_DATE", baleDate);
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            resultSet.updateBoolean("F6", getAttribute("F6").getBool());
            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            resultSet.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateBoolean("APPROVED",false);
            resultSet.updateString("APPROVED_DATE","0000-00-00");
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_DATE","0000-00-00");
            resultSet.updateInt("CANCELED",0);
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.insertRow();
            
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER_H WHERE DOC_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString())+"' AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revisionNo++;
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSetHistory.updateString("BALE_NO", bno[i]);
            resultSetHistory.updateString("BALE_DATE", baleDate);
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            resultSetHistory.updateBoolean("F6", getAttribute("F6").getBool());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateBoolean("REJECTED",false);
            resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
            resultSetHistory.updateInt("CANCELED",0);
            resultSetHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
             ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            resultSetHistory.insertRow();
            }
           
//            String str1 = getAttribute("BALE_NO").getString();
//            String[] bno = str1.split(",");
//            
//            for (int i=0;i<bno.length;i++){
//                bno[i] = bno[i].replaceAll("[^\\w]", ",");
//                System.out.println("BALE_NO : "+bno[i]);
//                if(bno[i]=="")
//                {
//                    bno[i] = "0";
//                }
//                resultSetDetail.moveToInsertRow();
//            resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSetDetail.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
//            resultSetDetail.updateString("BALE_NO", bno[i]);
//            resultSetDetail.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
//            resultSetDetail.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
//            resultSetDetail.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
//            resultSetDetail.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
//            resultSetDetail.updateBoolean("INSURANCE_CODE", getAttribute("INSURANCE_CODE").getBool());
//            resultSetDetail.updateBoolean("WITHOUT_CRITICAL_LIMIT", getAttribute("WITHOUT_CRITICAL_LIMIT").getBool());
//            resultSetDetail.updateString("TRANSPORTER_CODE", getAttribute("TRANSPORTER_CODE").getString());
//            resultSetDetail.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSetDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
//            resultSetDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.updateBoolean("APPROVED",false);
//            resultSetDetail.updateString("APPROVED_DATE","0000-00-00");
//            resultSetDetail.updateBoolean("REJECTED",false);
//            resultSetDetail.updateString("REJECTED_DATE","0000-00-00");
//            resultSetDetail.updateInt("CANCELED",0);
//            resultSetDetail.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
//            resultSetDetail.updateInt("CHANGED",1);
//            resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.insertRow();
//            
//            }
            
//            resultSetDetail.moveToInsertRow();
//            resultSetDetail.updateString("DOC_NO", getAttribute("DOC_NO").getString());
//            resultSetDetail.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
//            resultSetDetail.updateInt("BALE_NO", getAttribute("BALE_NO").getInt());
//            resultSetDetail.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
//            resultSetDetail.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
//            resultSetDetail.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
//            resultSetDetail.updateFloat("CRITICAL_LIMIT_NEW",(float)getAttribute("CRITICAL_LIMIT_NEW").getVal());
//            resultSetDetail.updateString("REMARKS", getAttribute("REMARKS").getString());
//            resultSetDetail.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
//            resultSetDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.updateBoolean("APPROVED",false);
//            resultSetDetail.updateString("APPROVED_DATE","0000-00-00");
//            resultSetDetail.updateBoolean("REJECTED",false);
//            resultSetDetail.updateString("REJECTED_DATE","0000-00-00");
//            resultSetDetail.updateInt("CANCELED",0);
//            resultSetDetail.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
//            resultSetDetail.updateInt("CHANGED",1);
//            resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//            resultSetDetail.insertRow();
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=765; //Felt Change Bale No
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DOC_NO";
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status="A";
                ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
            }
            else {
                if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError=ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            MoveLast();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        ResultSet  resultSetDetail, resultSetDetailHistory, resultSetHistory;
        Statement  statementH, statementDetailHistory, statementHistory;
        
        try {
            
            String baleDate = "";
            // Packing detail connection
            statementH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statementH.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO=''");
//           
            // Packing data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM  PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER_H WHERE DOC_NO=''");
            
            data.Execute("DELETE FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            
            String str1 = getAttribute("BALE_NO").getString();
            String[] bno = str1.split(",");
            
            for (int i=0;i<bno.length;i++){
                bno[i] = bno[i].replaceAll("[^\\w]", ",");
                System.out.println("BALE_NO : "+bno[i]);
                if(bno[i]=="")
                {
                    bno[i] = "0";
                }
                
            baleDate = data.getStringValueFromDB("SELECT PKG_BALE_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+bno[i]+"' AND APPROVED=1 AND CANCELED=0 AND BALE_REOPEN_FLG=0 AND INVOICE_FLG=0 ORDER BY PKG_BALE_DATE DESC");
                
            resultSet.moveToInsertRow();
            resultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSet.updateString("DOC_DATE",getAttribute("DOC_DATE").getString());
            resultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSet.updateString("BALE_NO", bno[i]);
            resultSet.updateString("BALE_DATE", baleDate);
            resultSet.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSet.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            resultSet.updateBoolean("F6", getAttribute("F6").getBool());
            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            resultSet.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSet.updateInt("CHANGED",1);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.insertRow();
            
            int revisionNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER_H WHERE  DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
            revisionNo++;
            
            
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO",revisionNo);
            resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            resultSetHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            resultSetHistory.updateString("BALE_NO", bno[i]);
            resultSetHistory.updateString("BALE_DATE", baleDate);
            resultSetHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            resultSetHistory.updateString("PARTY_NAME", getAttribute("PARTY_NAME").getString());
            resultSetHistory.updateString("CHARGE_CODE", getAttribute("CHARGE_CODE").getString());
            resultSetHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            resultSetHistory.updateBoolean("F6", getAttribute("F6").getBool());
            resultSetHistory.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSetHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateBoolean("REJECTED",false);
            resultSetHistory.updateString("REJECTED_DATE","0000-00-00");
            resultSetHistory.updateInt("CANCELED",0);
            resultSetHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateInt("CHANGED",1);
            resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateInt("UPDATED_BY",getAttribute("UPDATED_BY").getInt());
            resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
             ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            resultSetHistory.updateString("FROM_IP",""+str_split[1]);
            resultSetHistory.insertRow();
            }
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=765; //Felt Change Bale No
            ObjFeltProductionApprovalFlow.DocNo=getDocumentNo(getAttribute("DOC_NO").getString());
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=false;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DOC_NO";
            
              
          
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER SET REJECTED=0,CHANGED=1 WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' ");
                ObjFeltProductionApprovalFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                if(IsRejected) {
                    ObjFeltProductionApprovalFlow.Status="A";
                    ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                    ObjFeltProductionApprovalFlow.UpdateFlow();
                }
            }
            else {
                if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError=ObjFeltProductionApprovalFlow.LastError;
                }
            }
            
            
            //--------- Approval Flow Update complete -----------
            setData();
            resultSet.close();
            statementH.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * This routine checks whether the item is deletable or not.
     * Criteria is Approved item cannot be delete,
     * and if not approved then user id is checked whether doucment
     * is created by the user. Only creator can delete the document.
     * After checking it deletes the record of selected production date and document no.
     */
    public boolean CanDelete(String baleNo,String baleDate,int userID) {
        if(HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FFELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO='"+baleNo +"' AND DOC_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate) +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Packing is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=765 AND USER_ID="+userID+" AND DOC_NO='"+ getAttribute("DOC_NO").getString()+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FFELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO='"+baleNo+"' AND DOC_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate)+"'";
                    tmpStmt.executeUpdate(strSQL);
                    strSQL = "DELETE FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE DOC_NO='"+baleNo+"' AND DOC_DATE='"+EITLERPGLOBAL.formatDateDB(baleDate)+"'";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                }else {
                    LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                    return false;
                }
            }
        }catch(Exception e) {
            LastError = "Error occured while deleting."+e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String baleNo, String baleDate, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO='"+ baleNo +"' AND DOC_DATE='"+ baleDate +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=765 AND USER_ID="+userID+" AND DOC_NO='"+baleNo+"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    //Yes document is waiting for this user
                    return true;
                }else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Filter(String stringFindQuery) {
        Ready=false;
        try {
            String strSql = "SELECT GROUP_CONCAT(BALE_NO),DOC_NO,DOC_DATE,PARTY_CODE,PARTY_NAME,BALE_DATE,CHARGE_CODE,REMARKS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,F6 FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE " + stringFindQuery;
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            if(!resultSet.first()) {
                LoadData();
                Ready=true;
                return false;
            }else {
                Ready=true;
                MoveLast();
                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        try {
            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            }else {
                setAttribute("REVISION_NO",0); 
                setAttribute("CREATED_BY",resultSet.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",resultSet.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSet.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSet.getString("MODIFIED_DATE"));
                setAttribute("APPROVED",resultSet.getInt("APPROVED"));
                setAttribute("APPROVED_DATE",resultSet.getString("APPROVED_DATE"));
                setAttribute("REJECTED",resultSet.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE",resultSet.getString("REJECTED_DATE"));
                setAttribute("CANCELED",resultSet.getInt("CANCELED"));
            }
            
            setAttribute("DOC_NO",resultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",resultSet.getString("DOC_DATE"));
            setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
            String blNo = data.getStringValueFromDB("SELECT GROUP_CONCAT(BALE_NO) FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER WHERE DOC_NO='"+resultSet.getString("DOC_NO")+"'");
//            setAttribute("BALE_NO",resultSet.getString("BALE_NO"));
            setAttribute("BALE_NO",blNo);
            setAttribute("BALE_DATE",resultSet.getString("BALE_DATE"));
            setAttribute("CHARGE_CODE",resultSet.getString("CHARGE_CODE"));
            setAttribute("F6",resultSet.getBoolean("F6"));
            setAttribute("REMARKS",resultSet.getString("REMARKS"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
        
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean ShowHistory(String baleDate,String baleNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER_H WHERE DOC_DATE='"+ baleDate+"' AND DOC_NO='"+baleNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public static HashMap getHistoryList(String dpDate, String dpNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER_H WHERE DOC_DATE='"+dpDate+"' AND DOC_NO='"+dpNo+"' GROUP BY DOC_NO,REVISION_NO");
            
            while(rsTmp.next()) {
                clsFeltProcessInvoiceVariable ObjFeltReopenBale=new clsFeltProcessInvoiceVariable();
                
                ObjFeltReopenBale.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFeltReopenBale.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFeltReopenBale.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFeltReopenBale.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltReopenBale.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltReopenBale.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjFeltReopenBale);
            }
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        }catch(Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT H.DOC_DATE,H.DOC_NO,H.PARTY_CODE,H.PARTY_NAME,D.RECEIVED_DATE FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=765 AND CANCELED=0 GROUP BY DOC_NO ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT H.DOC_DATE,H.DOC_NO,H.PARTY_CODE,H.PARTY_NAME,D.RECEIVED_DATE FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=765 AND CANCELED=0 GROUP BY DOC_NO ORDER BY DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT H.DOC_DATE,H.DOC_NO,H.PARTY_CODE,H.PARTY_NAME,D.RECEIVED_DATE FROM PRODUCTION.FELT_F6_INVOICE_MODIFICATION_HEADER H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=765 AND CANCELED=0 GROUP BY DOC_NO ORDER BY DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltProcessInvoiceVariable ObjDoc=new clsFeltProcessInvoiceVariable();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjDoc.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Long.toString(Counter),ObjDoc);
            }
            rsTmp.close();
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public String getDocumentNo(String dpNo){
        return   dpNo;
    }
    
    public String[] getPieceDetails(String pieceNo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String []pieceDetails = new String[6];
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT RCVD_MTR, RECD_WDTH, RECD_KG,STYLE,GSQ,SYN_PER FROM (SELECT PIECE_NO,RCVD_MTR, RECD_WDTH, RECD_KG, REPLACE(STYLE,' ','') STYLE,PRODUCT_CD FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO+0='"+pieceNo+"' AND REMARK='IN STOCK') P LEFT JOIN (SELECT PIECE_NO,GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pieceNo+"') O ON P.PIECE_NO+0=O.PIECE_NO+0 LEFT JOIN (SELECT SYN_PER,ITEM_CODE FROM PRODUCTION.FELT_RATE_MASTER) R ON P.PRODUCT_CD=R.ITEM_CODE");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                pieceDetails[0] = rsTmp.getString("RCVD_MTR");
                pieceDetails[1] = rsTmp.getString("RECD_WDTH");
                pieceDetails[2] = rsTmp.getString("RECD_KG");
                pieceDetails[3] = rsTmp.getString("GSQ");
                pieceDetails[4] = rsTmp.getString("SYN_PER");
                pieceDetails[5] = rsTmp.getString("STYLE");
            }
            Stmt.close();
            rsTmp.close();
            return pieceDetails;
        }
        catch(Exception e) {
            e.printStackTrace();
            return pieceDetails;
        }
    }
    
    // checks piece no already exist in db in add mode
    public boolean checkPieceNoInDB(String pieceNo){
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>0) return true;
        else return false;
    }
    
    // checks piece no already exist in db in edit mode
    public boolean checkPieceNoInDB(String pieceNo, String baleNo, String baleDate){
        int count=data.getIntValueFromDB("SELECT COUNT(PKG_PIECE_NO) FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
        if(count>=1){
            int counter=0;
            try{
                Connection Conn=data.getConn();
                Statement stTmp=Conn.createStatement();
                ResultSet rsTmp=stTmp.executeQuery("SELECT PKG_DOC_DATE,DOC_NO FROM PRODUCTION.FELT_REOPEN_BALE_DETAIL WHERE PKG_PIECE_NO='"+pieceNo+"' AND PKG_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PKG_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'");
                while(rsTmp.next()){
                    if(rsTmp.getString("PKG_DOC_DATE").equals(EITLERPGLOBAL.formatDateDB(baleDate)) && rsTmp.getString("DOC_NO").equals(baleNo)) counter++;
                }
            }catch(SQLException e){e.printStackTrace();}
            if(counter==1 && count>=2) return true;
            else if(counter==1) return false;
            else return true;
        }else return false;
    }
    
    public static String getStation(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_STATION FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_STATION");
            }            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getBaleDate(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_DOC_DATE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_DOC_DATE");
            }
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getPartyCode(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_PARTY_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO='"+pPartyID+"' ORDER BY PKG_BALE_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_PARTY_CODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getboxsize(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_BOX_SIZE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_BOX_SIZE");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    public static String getmode(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_MODE_PACKING FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_MODE_PACKING");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    
     public static String gettransport(int pCompanyID,String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PKG_TRANSPORT_MODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_DOC_NO="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PKG_TRANSPORT_MODE");
            }
            
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
     public static String getParyName(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return PartyName;
    }
     public static String getChargeCode(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("CHARGE_CODE");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
     public static String gettransportid(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT TRANSPORTER_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("TRANSPORTER_ID");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
     public static String getCriticalLimit(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT AMOUNT_LIMIT FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("AMOUNT_LIMIT");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
}
