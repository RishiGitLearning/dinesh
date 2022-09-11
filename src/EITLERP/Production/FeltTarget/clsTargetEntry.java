    /*
     * clsSales_Party.java
     *
     * Created on April 3, 2009, 10:22 AM
     */

package EITLERP.Production.FeltTarget;

//import EITLERP.Production.FeltDiscRateMaster.*;
import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;
import java.text.DecimalFormat;
import java.lang.Double;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author  ashutosh
 */
public class clsTargetEntry {
    
    public String LastError="";
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=756; //72
    public HashMap colTGItems;
    
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
    
    /** Creates a new instance of clsSales_Party */
    public clsTargetEntry() {
        LastError = "";
        props=new HashMap();
        
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("FROM_TO_YEAR", new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
        //Create a new object for MR Item collection
        colTGItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("SEND_DOC_TO",new Variant(0));
        props.put("APPROVER_REMARKS",new Variant(0));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER ORDER BY DOC_NO");
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
        
        Statement stHistory,stHeader,stHDetail;
        ResultSet rsHistory,rsHeader,rsHDetail;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //--------- Generate Target Document no.  ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,756,(int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            
            rsResultSet.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsResultSet.updateString("FROM_TO_YEAR", getAttribute("FROM_TO_YEAR").getString());
            rsResultSet.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            rsResultSet.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","");
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsHistory.updateString("FROM_TO_YEAR", getAttribute("FROM_TO_YEAR").getString());
            rsHistory.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            rsHistory.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            System.out.println(1);
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL WHERE DOC_NO='1'");
            
            //long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colTGItems.size();i++) {
                clsTargetEntryItem ObjTGItems=(clsTargetEntryItem) colTGItems.get(Integer.toString(i));
                
                //ObjTGItems.setAttribute("BAL_QTY",(float)ObjTGItems.getAttribute("REQ_QTY").getVal());
                rsTmp.moveToInsertRow();
                
                //rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
                rsTmp.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
                
                rsTmp.updateString("PRODUCT_CODE",(String)ObjTGItems.getAttribute("PRODUCT_CODE").getObj());
                rsTmp.updateString("MACHINE_POSITION",(String)ObjTGItems.getAttribute("MACHINE_POSITION").getObj());
                rsTmp.updateString("TGT_Q1",Double.toString(ObjTGItems.getAttribute("TGT_Q1").getVal()));
                rsTmp.updateString("TGT_Q2",Double.toString(ObjTGItems.getAttribute("TGT_Q2").getVal()));
                rsTmp.updateString("TGT_Q3",Double.toString(ObjTGItems.getAttribute("TGT_Q3").getVal()));
                rsTmp.updateString("TGT_Q4",Double.toString(ObjTGItems.getAttribute("TGT_Q4").getVal()));
                rsTmp.updateString("TOTAL_TARGET",Double.toString(ObjTGItems.getAttribute("TOTAL_TARGET").getVal()));
                
                for(int j=1;j<=20;j++)
                {
                    rsTmp.updateString("TGT_Q1_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD_R"+j).getVal()));
                    rsTmp.updateString("TGT_Q2_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD_R"+j).getVal()));
                    rsTmp.updateString("TGT_Q3_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD_R"+j).getVal()));
                    rsTmp.updateString("TGT_Q4_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R"+j).getVal()));
                }
                
                /*
                rsTmp.updateString("TGT_Q1_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD").getVal()));
                rsTmp.updateString("TGT_Q2_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD").getVal()));
                rsTmp.updateString("TGT_Q3_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD").getVal()));
                rsTmp.updateString("TGT_Q4_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD").getVal()));
                */
                rsTmp.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsTmp.updateString("MODIFIED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("APPROVED",false);
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.updateBoolean("REJECTED",false);
                
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
                rsHDetail.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
                
                rsHDetail.updateString("PRODUCT_CODE",(String)ObjTGItems.getAttribute("PRODUCT_CODE").getObj());
                rsHDetail.updateString("MACHINE_POSITION",(String)ObjTGItems.getAttribute("MACHINE_POSITION").getObj());
                rsHDetail.updateString("TGT_Q1",Double.toString(ObjTGItems.getAttribute("TGT_Q1").getVal()));
                rsHDetail.updateString("TGT_Q2",Double.toString(ObjTGItems.getAttribute("TGT_Q2").getVal()));
                rsHDetail.updateString("TGT_Q3",Double.toString(ObjTGItems.getAttribute("TGT_Q3").getVal()));
                rsHDetail.updateString("TGT_Q4",Double.toString(ObjTGItems.getAttribute("TGT_Q4").getVal()));
                rsHDetail.updateString("TOTAL_TARGET",Double.toString(ObjTGItems.getAttribute("TOTAL_TARGET").getVal()));
                
                for(int j=1;j<=20;j++)
                {
                    rsHDetail.updateString("TGT_Q1_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD_R"+j).getVal()));
                    rsHDetail.updateString("TGT_Q2_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD_R"+j).getVal()));
                    rsHDetail.updateString("TGT_Q3_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD_R"+j).getVal()));
                    rsHDetail.updateString("TGT_Q4_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R"+j).getVal()));
                }
                
                /*
                rsHDetail.updateString("TGT_Q1_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD").getVal()));
                rsHDetail.updateString("TGT_Q2_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD").getVal()));
                rsHDetail.updateString("TGT_Q3_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD").getVal()));
                rsHDetail.updateString("TGT_Q4_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD").getVal()));
                */
                rsHDetail.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("APPROVED",false);
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.updateBoolean("REJECTED",false);
           
                rsHDetail.insertRow();
            }
            
            
            //===================== Update the Approval Flow ======================//
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
            
            ObjFlow.ModuleID=clsTargetEntry.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_TARGET_ENTRY_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
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
            
//            if(ObjFlow.Status.equals("A")){
//                
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=subdate(B.EFFECTIVE_FROM,1) WHERE (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND A.MASTER_NO NOT IN ('" + ObjFlow.DocNo + "') AND A.PARTY_CODE=B.PARTY_CODE AND B.MASTER_NO='" + ObjFlow.DocNo + "' AND B.CANCELED=0");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=subdate(B.EFFECTIVE_FROM,1) WHERE (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND A.MASTER_NO NOT IN ('" + ObjFlow.DocNo + "') AND A.PARTY_CODE=B.PARTY_CODE AND B.MASTER_NO='" + ObjFlow.DocNo + "' AND B.CANCELED=0");
//                
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL A,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND A.EFFECTIVE_TO IS NULL AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL_H A,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND A.EFFECTIVE_TO IS NULL AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER A,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.EFFECTIVE_TO IS NULL AND A.MASTER_NO!='" + ObjFlow.DocNo + "' AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER_H A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.EFFECTIVE_TO IS NULL AND A.MASTER_NO!='" + ObjFlow.DocNo + "' AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //                 //data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MM_PARTY_CODE='"+ObjFlow.DocNo+"'");
//                //                 data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
//                //                 data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
//            }
            //================= Approval Flow Update complete ===================//
            
            MoveLast();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            LastError= e.getMessage();
            
            return false;
        }
        
    }
    
    public boolean Update() {
        
        Statement stHistory,stHeader,stHDetail;
        ResultSet rsHistory,rsHeader,rsHDetail;
        boolean Validate=true;
        
        try {
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            Validate=true;
            //======= Check the requisition date ============//
            //            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            //            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            //            java.sql.Date EffectiveDate=java.sql.Date.valueOf((String)getAttribute("EFFECTIVE_FROM").getObj());
            //
            //            if((EffectiveDate.after(FinFromDate)||EffectiveDate.compareTo(FinFromDate)==0)&&(EffectiveDate.before(FinToDate)||EffectiveDate.compareTo(FinToDate)==0)) {
            //                //Withing the year
            //            }
            //            else {
            //                LastError="Effective date is not within financial year.";
            //                return false;
            //            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=getAttribute("DOC_NO").getString();
            
            // rsResultSet.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsResultSet.updateString("FROM_TO_YEAR", getAttribute("FROM_TO_YEAR").getString());
            rsResultSet.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            rsResultSet.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER_H WHERE DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsHistory.updateString("FROM_TO_YEAR", getAttribute("FROM_TO_YEAR").getString());
            rsHistory.updateString("FROM_DATE", getAttribute("FROM_DATE").getString());
            rsHistory.updateString("TO_DATE", getAttribute("TO_DATE").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mDocNo=(String)getAttribute("DOC_NO").getObj();
            
            data.Execute("DELETE FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL WHERE DOC_NO='"+mDocNo+"'");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL WHERE DOC_NO='1'");
            //Now Insert records into detail table
            for(int i=1;i<=colTGItems.size();i++) {
                clsTargetEntryItem ObjTGItems=(clsTargetEntryItem) colTGItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
                rsTmp.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
                
                rsTmp.updateString("PRODUCT_CODE",(String)ObjTGItems.getAttribute("PRODUCT_CODE").getObj());
                rsTmp.updateString("MACHINE_POSITION",(String)ObjTGItems.getAttribute("MACHINE_POSITION").getObj());
                rsTmp.updateString("TGT_Q1",Double.toString(ObjTGItems.getAttribute("TGT_Q1").getVal()));
                rsTmp.updateString("TGT_Q2",Double.toString(ObjTGItems.getAttribute("TGT_Q2").getVal()));
                rsTmp.updateString("TGT_Q3",Double.toString(ObjTGItems.getAttribute("TGT_Q3").getVal()));
                rsTmp.updateString("TGT_Q4",Double.toString(ObjTGItems.getAttribute("TGT_Q4").getVal()));
                rsTmp.updateString("TOTAL_TARGET",Double.toString(ObjTGItems.getAttribute("TOTAL_TARGET").getVal()));
                
                for(int j=1;j<=20;j++)
                {
                    rsTmp.updateString("TGT_Q1_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD_R"+j).getVal()));
                    rsTmp.updateString("TGT_Q2_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD_R"+j).getVal()));
                    rsTmp.updateString("TGT_Q3_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD_R"+j).getVal()));
                    rsTmp.updateString("TGT_Q4_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R"+j).getVal()));
                }
                
                /*
                rsTmp.updateString("TGT_Q1_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD").getVal()));
                rsTmp.updateString("TGT_Q2_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD").getVal()));
                rsTmp.updateString("TGT_Q3_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD").getVal()));
                rsTmp.updateString("TGT_Q4_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD").getVal()));
                */
                rsTmp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                
                rsHDetail.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
                rsHDetail.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
                
                rsHDetail.updateString("PRODUCT_CODE",(String)ObjTGItems.getAttribute("PRODUCT_CODE").getObj());
                rsHDetail.updateString("MACHINE_POSITION",(String)ObjTGItems.getAttribute("MACHINE_POSITION").getObj());
                rsHDetail.updateString("TGT_Q1",Double.toString(ObjTGItems.getAttribute("TGT_Q1").getVal()));
                rsHDetail.updateString("TGT_Q2",Double.toString(ObjTGItems.getAttribute("TGT_Q2").getVal()));
                rsHDetail.updateString("TGT_Q3",Double.toString(ObjTGItems.getAttribute("TGT_Q3").getVal()));
                rsHDetail.updateString("TGT_Q4",Double.toString(ObjTGItems.getAttribute("TGT_Q4").getVal()));
                rsHDetail.updateString("TOTAL_TARGET",Double.toString(ObjTGItems.getAttribute("TOTAL_TARGET").getVal()));
                
                for(int j=1;j<=20;j++)
                {
                    rsHDetail.updateString("TGT_Q1_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD_R"+j).getVal()));
                    rsHDetail.updateString("TGT_Q2_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD_R"+j).getVal()));
                    rsHDetail.updateString("TGT_Q3_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD_R"+j).getVal()));
                    rsHDetail.updateString("TGT_Q4_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R"+j).getVal()));
                }
                
                /*
                rsHDetail.updateString("TGT_Q1_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD").getVal()));
                rsHDetail.updateString("TGT_Q2_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD").getVal()));
                rsHDetail.updateString("TGT_Q3_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD").getVal()));
                rsHDetail.updateString("TGT_Q4_OLD",Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD").getVal()));
                */
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsHDetail.insertRow();
            }
            
            
            //======== Update the Approval Flow =========
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
            
            ObjFlow.ModuleID=clsTargetEntry.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_TARGET_ENTRY_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            //String qry = "UPDATE FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CODE").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsSales_Party.ModuleID;
            //String qry = "UPDATE PRODUCTION.FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CD").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsOrderParty.ModuleID;
            //data.Execute(qry);
            
            //==== Handling Rejected Documents ==========//
            AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from FELT_DOC_DATA
                //data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_TARGET_ENTRY_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+ObjFlow.DocNo+"' ");
                data.Execute("UPDATE PRODUCTION.FELT_TARGET_ENTRY_DETAIL SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+ObjFlow.DocNo+"' ");
                
                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+clsTargetEntry.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
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
            
            
//            if(ObjFlow.Status.equals("A")){
//                
//                
//                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=subdate(B.EFFECTIVE_FROM,1) WHERE (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND A.MASTER_NO NOT IN ('" + ObjFlow.DocNo + "') AND A.PARTY_CODE=B.PARTY_CODE AND B.MASTER_NO='" + ObjFlow.DocNo + "' AND B.CANCELED=0");
//                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=subdate(B.EFFECTIVE_FROM,1) WHERE (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND A.MASTER_NO NOT IN ('" + ObjFlow.DocNo + "') AND A.PARTY_CODE=B.PARTY_CODE AND B.MASTER_NO='" + ObjFlow.DocNo + "' AND B.CANCELED=0");
//                
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL A,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND A.EFFECTIVE_TO IS NULL AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL_H A,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND A.EFFECTIVE_TO IS NULL AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER A,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM)  AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.EFFECTIVE_TO IS NULL AND A.MASTER_NO!='" + ObjFlow.DocNo + "' AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER_H A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.EFFECTIVE_TO IS NULL AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
//                //                 //data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MM_PARTY_CODE='"+ObjFlow.DocNo+"'");
//                //                 data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
//                //                 data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
//            }
            
            if(ObjFlow.Status.equals("F")){
                
                data.Execute("UPDATE PRODUCTION.FELT_TARGET_ENTRY_HEADER H,PRODUCTION.FELT_TARGET_ENTRY_DETAIL D SET D.APPROVED=H.APPROVED,D.CANCELLED=H.CANCELLED,D.REJECTED=H.REJECTED WHERE H.DOC_NO=D.DOC_NO AND D.DOC_NO='"+ObjFlow.DocNo+"' ");
                
                for(int i=1;i<=colTGItems.size();i++) {
                        clsTargetEntryItem ObjTGItems=(clsTargetEntryItem) colTGItems.get(Integer.toString(i));
                        rsTmp.moveToInsertRow();
                        rsTmp.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
                        rsTmp.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
                        rsTmp.updateInt("SR_NO",i);
                        rsTmp.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                        rsTmp.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());

                        rsTmp.updateString("PRODUCT_CODE",(String)ObjTGItems.getAttribute("PRODUCT_CODE").getObj());
                        rsTmp.updateString("MACHINE_POSITION",(String)ObjTGItems.getAttribute("MACHINE_POSITION").getObj());
                        rsTmp.updateString("TGT_Q1",Double.toString(ObjTGItems.getAttribute("TGT_Q1").getVal()));
                        rsTmp.updateString("TGT_Q2",Double.toString(ObjTGItems.getAttribute("TGT_Q2").getVal()));
                        rsTmp.updateString("TGT_Q3",Double.toString(ObjTGItems.getAttribute("TGT_Q3").getVal()));
                        rsTmp.updateString("TGT_Q4",Double.toString(ObjTGItems.getAttribute("TGT_Q4").getVal()));
                        rsTmp.updateString("TOTAL_TARGET",Double.toString(ObjTGItems.getAttribute("TOTAL_TARGET").getVal()));

                        for(int j=1;j<=20;j++)
                        {
                            rsTmp.updateString("TGT_Q1_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD_R"+j).getVal()));
                            rsTmp.updateString("TGT_Q2_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD_R"+j).getVal()));
                            rsTmp.updateString("TGT_Q3_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD_R"+j).getVal()));
                            rsTmp.updateString("TGT_Q4_OLD_R"+j,Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R"+j).getVal()));
                        }
                        
                        String update_query = "UPDATE PRODUCTION.FELT_PARTY_ITEM_POSITION_TARGET " +
                            " SET " +
                            " TGT_QTR_1 = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q1").getVal())+"'," +
                            " TGT_QTR_2 = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q2").getVal())+"'," +
                            " TGT_QTR_3 = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q3").getVal())+"'," +
                            " TGT_QTR_4 = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q4").getVal())+"'," +
                            " TOTAL_TGT = '"+Double.toString(ObjTGItems.getAttribute("TOTAL_TARGET").getVal())+"'," +
                            " TGT_QTR_1_OLD = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD_R1").getVal())+"'," +
                            " TGT_QTR_2_OLD = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD_R1").getVal())+"'," +
                            " TGT_QTR_3_OLD = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD_R1").getVal())+"'," +
                            " TGT_QTR_4_OLD = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R1").getVal())+"',";
                        
                        for(int j=2;j<=20;j++)
                        {
                            update_query = update_query + " TGT_QTR_1_OLD_R"+j+" = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q1_OLD_R"+j+"").getVal())+"',";
                            update_query = update_query + " TGT_QTR_2_OLD_R"+j+" = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q2_OLD_R"+j+"").getVal())+"',";
                            update_query = update_query + " TGT_QTR_3_OLD_R"+j+" = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q3_OLD_R"+j+"").getVal())+"',";
                            if(j!=20)
                            {
                                update_query = update_query + " TGT_QTR_4_OLD_R"+j+" = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R"+j+"").getVal())+"',";
                            }
                            else
                            {
                                update_query = update_query + " TGT_QTR_4_OLD_R"+j+" = '"+Double.toString(ObjTGItems.getAttribute("TGT_Q4_OLD_R"+j+"").getVal())+"'";
                            }
                        }
                                
                        update_query = update_query + " WHERE PARTY_CODE='"+(String)getAttribute("PARTY_CODE").getObj()+"' AND PRODUCT_CODE='"+(String)ObjTGItems.getAttribute("PRODUCT_CODE").getObj()+"' AND  TGT_FY_YR='"+getAttribute("FROM_TO_YEAR").getString()+"'";
                        System.out.println("UPDATE QUERY = "+update_query);
                        data.Execute(update_query);
                }           
                
                
                String get_total_query = "SELECT "
                        + "sum(TGT_QTR_1) AS TOTAL_TGT_QTR_1,"
                        + "sum(TGT_QTR_2) AS TOTAL_TGT_QTR_2,"
                        + "sum(TGT_QTR_3) AS TOTAL_TGT_QTR_3,"
                        + "sum(TGT_QTR_4) AS TOTAL_TGT_QTR_4,"
                        + "sum(TOTAL_TGT) AS TOTAL_TOTAL_TGT,"
                        + "sum(TGT_QTR_1_OLD) AS TOTAL_TGT_QTR_1_OLD,"
                        + "sum(TGT_QTR_2_OLD) AS TOTAL_TGT_QTR_2_OLD,"
                        + "sum(TGT_QTR_3_OLD) AS TOTAL_TGT_QTR_3_OLD,"
                        + "sum(TGT_QTR_4_OLD) AS TOTAL_TGT_QTR_4_OLD,";
                
                for(int i=2;i<=20;i++)
                {
                    get_total_query = get_total_query  + "sum(TGT_QTR_1_OLD_R"+i+") AS TOTAL_TGT_QTR_1_OLD_R"+i+",";
                    get_total_query = get_total_query  + "sum(TGT_QTR_2_OLD_R"+i+") AS TOTAL_TGT_QTR_2_OLD_R"+i+",";
                    get_total_query = get_total_query  + "sum(TGT_QTR_3_OLD_R"+i+") AS TOTAL_TGT_QTR_3_OLD_R"+i+",";
                    if(i!=20)
                    {
                        get_total_query = get_total_query  + "sum(TGT_QTR_4_OLD_R"+i+") AS TOTAL_TGT_QTR_4_OLD_R"+i+",";
                    }
                    else
                    {
                        get_total_query = get_total_query  + "sum(TGT_QTR_4_OLD_R"+i+") AS TOTAL_TGT_QTR_4_OLD_R"+i+" ";
                    }
                }
                
                get_total_query = get_total_query  + " FROM PRODUCTION.FELT_PARTY_ITEM_POSITION_TARGET where PARTY_CODE='"+(String)getAttribute("PARTY_CODE").getObj()+"' and TGT_FY_YR='"+getAttribute("FROM_TO_YEAR").getString()+"' AND PRODUCT_CODE != 'TOTAL'";
                System.out.println("GET TOTAL QUERY : "+get_total_query);   
                ResultSet rsTmp_total;
                
                rsTmp=data.getResult(get_total_query);
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                       
                    while(!rsTmp.isAfterLast()) {
                           
                        //Object[] rowData=new Object[109];
                        //rowData[1]=rsTmp.getString("PARTY_CODE");
                        //rowData[1]=rsTmp.getString("PARTY_CODE");
                        //rowData[2]=rsTmp.getString("PARTY_NAME");
                        //rowData[3]=rsTmp.getString("PRODUCT_CODE");
                
                         String update_query = "UPDATE PRODUCTION.FELT_PARTY_ITEM_POSITION_TARGET " +
                            " SET " +
                            " TGT_QTR_1 = '"+rsTmp.getString("TOTAL_TGT_QTR_1")+"'," +
                            " TGT_QTR_2 = '"+rsTmp.getString("TOTAL_TGT_QTR_2")+"'," +
                            " TGT_QTR_3 = '"+rsTmp.getString("TOTAL_TGT_QTR_3")+"'," +
                            " TGT_QTR_4 = '"+rsTmp.getString("TOTAL_TGT_QTR_4")+"'," +
                            " TOTAL_TGT = '"+rsTmp.getString("TOTAL_TOTAL_TGT")+"'," +
                            " TGT_QTR_1_OLD = '"+rsTmp.getString("TOTAL_TGT_QTR_1_OLD")+"'," +
                            " TGT_QTR_2_OLD = '"+rsTmp.getString("TOTAL_TGT_QTR_2_OLD")+"'," +
                            " TGT_QTR_3_OLD = '"+rsTmp.getString("TOTAL_TGT_QTR_3_OLD")+"'," +
                            " TGT_QTR_4_OLD = '"+rsTmp.getString("TOTAL_TGT_QTR_4_OLD")+"',";
                        
                        for(int j=2;j<=20;j++)
                        {
                            update_query = update_query + " TGT_QTR_1_OLD_R"+j+" = '"+rsTmp.getString("TOTAL_TGT_QTR_1_OLD_R"+j)+"',";
                            update_query = update_query + " TGT_QTR_2_OLD_R"+j+" = '"+rsTmp.getString("TOTAL_TGT_QTR_2_OLD_R"+j)+"',";
                            update_query = update_query + " TGT_QTR_3_OLD_R"+j+" = '"+rsTmp.getString("TOTAL_TGT_QTR_3_OLD_R"+j)+"',";
                            if(j!=20)
                            {
                                update_query = update_query + " TGT_QTR_4_OLD_R"+j+" = '"+rsTmp.getString("TOTAL_TGT_QTR_4_OLD_R"+j)+"',";
                            }
                            else
                            {
                                update_query = update_query + " TGT_QTR_4_OLD_R"+j+" = '"+rsTmp.getString("TOTAL_TGT_QTR_4_OLD_R"+j)+"'";
                            }
                        }
                                
                        update_query = update_query + " WHERE PARTY_CODE='"+(String)getAttribute("PARTY_CODE").getObj()+"' AND PRODUCT_CODE='TOTAL' AND  TGT_FY_YR='"+getAttribute("FROM_TO_YEAR").getString()+"'";
                        System.out.println("UPDATE QUERY = "+update_query);
                        data.Execute(update_query);
                        rsTmp.next();
                    }
                }
                
                
//                data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
//                data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
            }
            //--------- Approval Flow Update complete -----------
            
            //            if (ObjFlow.Status.equalsIgnoreCase("F")) {
            //                data.Execute("UPDATE RATE_MASTER a,IMPORT_RATE_MASTER b SET a.END_DT=SUBDATE(b.START_DT,1) WHERE a.QUALITY_CD=b.QUALITY_CD AND a.ITEM_TYPE=b.ITEM_TYPE AND a.RATE_TYPE='R' AND b.FILE_NAME='" + ObjFlow.DocNo + "'");
            //                data.Execute("UPDATE RATE_MASTER a,IMPORT_RATE_MASTER b SET a.END_DT=SUBDATE(b.START_DT,1) WHERE SUBSTRING(a.QUALITY_CD,2,5)=SUBSTRING(b.QUALITY_CD,2,5) AND a.ITEM_TYPE IN ('SF','SS') AND b.ITEM_TYPE='SF' AND a.RATE_TYPE='R' AND b.FILE_NAME='" + ObjFlow.DocNo + "'");
            //                data.Execute("UPDATE RATE_MASTER a,IMPORT_RATE_MASTER b SET a.END_DT=SUBDATE(b.START_DT,1) WHERE SUBSTRING(a.QUALITY_CD,2,5)=SUBSTRING(b.QUALITY_CD,2,5) AND a.ITEM_TYPE IN ('SS') AND b.ITEM_TYPE='SS' AND a.RATE_TYPE='R' AND b.FILE_NAME='" + ObjFlow.DocNo + "'");
            //
            //                //data.Execute("INSERT INTO RATE_MASTER SELECT ITEM_TYPE,QUALITY_CD,EX_MILL_RT,RETAIL_RT,DISC_PER,START_DT,END_DT,1,'R',NULL,NULL FROM IMPORT_RATE_MASTER WHERE FILE_NAME='" + ObjFlow.DocNo + "'");
            //                data.Execute("INSERT INTO RATE_MASTER SELECT ITEM_TYPE,QUALITY_CD,EX_MILL_RT,RETAIL_RT,DISC_PER,START_DT,END_DT,1,'R','1','"+RSGLOBAL.getCurrentDateDB()+"' FROM IMPORT_RATE_MASTER WHERE FILE_NAME='" + ObjFlow.DocNo + "'");
            return true;
            
        }
        catch(Exception e) {
            e.printStackTrace();
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
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("DOC_NO",rsResultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",rsResultSet.getString("DOC_DATE"));
            setAttribute("FROM_TO_YEAR",rsResultSet.getString("FROM_TO_YEAR"));
            setAttribute("FROM_DATE",rsResultSet.getString("FROM_DATE"));
            setAttribute("TO_DATE",rsResultSet.getString("TO_DATE"));
            setAttribute("PARTY_CODE",rsResultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED",rsResultSet.getInt("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));
            
            //Now Populate the collection
            //first clear the collection
            colTGItems.clear();
            
            //String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mDocNo=(String) getAttribute("DOC_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL_H WHERE DOC_NO='"+mDocNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL WHERE DOC_NO='"+mDocNo+"' ORDER BY SR_NO");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsTargetEntryItem ObjTGItems = new clsTargetEntryItem();
                ObjTGItems.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjTGItems.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjTGItems.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjTGItems.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjTGItems.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                ObjTGItems.setAttribute("PRODUCT_CODE",rsTmp.getString("PRODUCT_CODE"));
                ObjTGItems.setAttribute("MACHINE_POSITION",rsTmp.getString("MACHINE_POSITION"));
                ObjTGItems.setAttribute("TGT_Q1",rsTmp.getDouble("TGT_Q1"));
                ObjTGItems.setAttribute("TGT_Q2",rsTmp.getDouble("TGT_Q2"));
                ObjTGItems.setAttribute("TGT_Q3",rsTmp.getDouble("TGT_Q3"));
                ObjTGItems.setAttribute("TGT_Q4",rsTmp.getDouble("TGT_Q4"));
                ObjTGItems.setAttribute("TOTAL_TARGET",rsTmp.getDouble("TOTAL_TARGET")); //SEAM CHG
                
                for(int k=1;k<=20;k++)
                {
                    ObjTGItems.setAttribute("TGT_Q1_OLD_R"+k,rsTmp.getDouble("TGT_Q1_OLD_R"+k));
                    ObjTGItems.setAttribute("TGT_Q2_OLD_R"+k,rsTmp.getDouble("TGT_Q2_OLD_R"+k));
                    ObjTGItems.setAttribute("TGT_Q3_OLD_R"+k,rsTmp.getDouble("TGT_Q3_OLD_R"+k));
                    ObjTGItems.setAttribute("TGT_Q4_OLD_R"+k,rsTmp.getDouble("TGT_Q4_OLD_R"+k));
                }
                
                /*
                ObjTGItems.setAttribute("TGT_Q1_OLD",rsTmp.getDouble("TGT_Q1_OLD"));
                ObjTGItems.setAttribute("TGT_Q2_OLD",rsTmp.getDouble("TGT_Q2_OLD"));
                ObjTGItems.setAttribute("TGT_Q3_OLD",rsTmp.getDouble("TGT_Q3_OLD"));
                ObjTGItems.setAttribute("TGT_Q4_OLD",rsTmp.getDouble("TGT_Q4_OLD"));
                */
                
                
                ObjTGItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjTGItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjTGItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjTGItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                //ObjTGItems.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                colTGItems.put(Long.toString(Counter),ObjTGItems);
                rsTmp.next();
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
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER WHERE DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=756 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
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
    
    public static HashMap getHistoryList(int CompanyID,String DocNo) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER_H WHERE DOC_NO='"+DocNo+"' ORDER BY REVISION_NO";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsTargetEntry objTarget=new clsTargetEntry();
                    
                    objTarget.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    objTarget.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                    objTarget.setAttribute("FROM_TO_YEAR",rsTmp.getString("FROM_TO_YEAR"));
                    objTarget.setAttribute("FROM_DATE",rsTmp.getString("FROM_DATE"));
                    objTarget.setAttribute("TO_DATE",rsTmp.getString("TO_DATE"));
                    objTarget.setAttribute("TO_DATE",rsTmp.getString("TO_DATE"));
                    objTarget.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                    objTarget.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                    
                    objTarget.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                    objTarget.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                    objTarget.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                    objTarget.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                    objTarget.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                    
                    List.put(Integer.toString(List.size()+1),objTarget);
                    rsTmp.next();
                }
            }
            rsTmp.close();
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    
    public boolean Abc(String Condition1){
        System.out.println(Condition1);
        return true;
    }
    
    
    public boolean Filter(String Condition) {
        Ready=false;
        try {
            String strSQL="";
            strSQL+= "SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER WHERE " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER WHERE DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
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
    
    
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER WHERE DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=756 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
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
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=EITLERPGLOBAL.gCompanyID;
            String lDocNo=(String)getAttribute("DOC_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER WHERE DOC_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_TARGET_ENTRY_DETAIL WHERE DOC_NO='"+lDocNo+"'";
                data.Execute(strQry);
                
                LoadData(lCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int CompanyID, String DocNo) {
        String strCondition = " WHERE DOC_NO='" + DocNo + "' ";
        clsTargetEntry objParty = new clsTargetEntry();
        objParty.Filter(strCondition);
        return objParty;
    }
    
    public static void CancelParty(int pCompanyID,String pPartyCode,String MainCode) {
        ResultSet rsTmp=null;
        
        if(CanCancelParty(pCompanyID,pPartyCode,MainCode)) {
            boolean Approved=false;
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                /*if(!Approved) {
                    data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' AND MODULE_ID="+clsSales_Party.ModuleID);
                }*/
                
                data.Execute("UPDATE D_SAL_PARTY_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' ");
                data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' ");
            } catch(Exception e) {
            }
        }
    }
    
    public static boolean CanCancelParty(int pCompanyID,String pPartyCode, String MainCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND COMPANY_ID="+pCompanyID+" AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        
        return CanCancel;
    }
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO,PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE,PRODUCTION.FELT_TARGET_ENTRY_HEADER.FROM_TO_YEAR,RECEIVED_DATE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=756 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO,PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE,PRODUCTION.FELT_TARGET_ENTRY_HEADER.FROM_TO_YEAR,RECEIVED_DATE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=756 ORDER BY PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO,PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE,PRODUCTION.FELT_TARGET_ENTRY_HEADER.FROM_TO_YEAR,RECEIVED_DATE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=756 ORDER BY PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsTargetEntry ObjItem = new clsTargetEntry();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjItem.setAttribute("FROM_TO_YEAR",rsTmp.getInt("FROM_TO_YEAR"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO,PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE,PRODUCTION.FELT_TARGET_ENTRY_HEADER.FROM_TO_YEAR,RECEIVED_DATE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=756 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO,PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE,PRODUCTION.FELT_TARGET_ENTRY_HEADER.FROM_TO_YEAR,RECEIVED_DATE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=756 ORDER BY PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO,PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_DATE,PRODUCTION.FELT_TARGET_ENTRY_HEADER.FROM_TO_YEAR,RECEIVED_DATE FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=756 ORDER BY PRODUCTION.FELT_TARGET_ENTRY_HEADER.DOC_NO";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsTargetEntry ObjItem = new clsTargetEntry();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjItem.setAttribute("FROM_TO_YEAR",rsTmp.getString("FROM_TO_YEAR"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            //Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_TARGET_ENTRY_HEADER_H WHERE DOC_NO='"+pDocNo+"' ORDER BY REVISION_NO");
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
    
    public static boolean IsPartyExistEx(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsPartyExist(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND BLOCKED='N' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static String getPartyName(int pCompanyID,String pPartyCode,String pURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn(pURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PARTY_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PartyName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getItemName(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"' ");
            /*String strSQL="SELECT ITEM_DESC FROM PRODUCTION.FELT_RATE_MASTER WHERE SUBSTRING(ITEM_CODE,1,6)=(SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" ";
            if(!pPartyCode.equals("")){
                strSQL+="AND PARTY_CD='"+pPartyCode+"'";
            }
            strSQL+=")";
             */
            String strSQL="SELECT ITEM_DESC FROM PRODUCTION.FELT_RATE_MASTER WHERE SUBSTRING(ITEM_CODE,1,6)= ";
            strSQL+="(SELECT PRODUCT_CD FROM PRODUCTION.FELT_ORDER_MASTER  WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"' UNION ALL SELECT SUBSTRING(PRODUCT_CD,1,6) FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO='"+pPieceNo+"' AND PARTY_CODE='"+pPartyCode+"')";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("ITEM_DESC");
                
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PartyName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getItemPosition(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Position="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Position=rsTmp.getString("POSITION");
                
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Position;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static double ConvertToNextThousand(double pamount){
        double returnamt=0;
        if (pamount<=1000){
            return pamount+(1000-pamount);
        }else{
            if(pamount%1000==0){
                return pamount;
            }else{
                return pamount+(1000-(pamount%1000));
            }
        }
        
    }
    
    public static String getPartyName(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PARTY_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PartyName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getNextFreeNo(int pCompanyID,int pModuleID,int pFirstFreeNo,boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        String strNewNo="";
        int lnNewNo=0;
        String Prefix="";
        String Suffix="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFreeNo;
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lnNewNo=rsTmp.getInt("LAST_USED_NO")+1;
                strNewNo=EITLERPGLOBAL.Padding(Integer.toString(lnNewNo),rsTmp.getInt("NO_LENGTH"),rsTmp.getString("PADDING_BY"));
                Prefix=rsTmp.getString("PREFIX_CHARS");
                Suffix=rsTmp.getString("SUFFIX_CHARS");
                
                if(UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFreeNo);
                }
                
                strNewNo = Prefix+ strNewNo+Suffix;
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return strNewNo;
            }
            else {
                return "";
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
}
