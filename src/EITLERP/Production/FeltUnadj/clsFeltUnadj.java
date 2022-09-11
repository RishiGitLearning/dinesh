    /*
     *
     */

package EITLERP.Production.FeltUnadj;

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
import EITLERP.Production.FeltCreditNote.clsFeltCNAutoPosting;


/**
 *
 * @author  Dharmendra
 */
public class clsFeltUnadj extends clsFeltCNAutoPosting {
    
    public String LastError="";
    //private ResultSet rsResultSet,rsResultSet1;
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=732; //72
    public HashMap colMRItems;
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
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
    public clsFeltUnadj() {
        LastError = "";
        props=new HashMap();
        props.put("UNADJ_ID",new Variant(""));
        props.put("UNADJ_DATE",new Variant(""));
        props.put("UNADJ_FROM_DATE",new Variant(""));
        props.put("UNADJ_TO_DATE",new Variant(""));
        props.put("H_REMARK1",new Variant(""));
        props.put("H_REMARK2",new Variant(""));
      
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(0));
       // props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        colMRItems=new HashMap();
      
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
          rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER ORDER BY UNADJ_DATE,UNADJ_ID");
          //  rsResultSet=Stmt.executeQuery("SELECT *, B.F6_ID,B.F6_FROM_DATE,B.F6_TO_DATE,A.INVOICE_NO,A.INVOICE_DATE,A.PARTY_CODE,A.PARTY_NAME,A.INVOICE_AMT,A.EXT1,A.EXT2,A.EXT3,A.EXT4,A.EXT5,A.EXT6 FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER B,PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL A WHERE A.F6_ID=B.F6_ID");
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
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            
            frmFeltUnadj fms=new frmFeltUnadj();
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H WHERE UNADJ_ID=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL_H WHERE UNADJ_ID='' ORDER BY PARTY_CODE");
            rsHDetail.first();
            
            //setAttribute("UNADJ_ID",getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,732,189,true));

            
            String adj = getAttribute("UNADJ_ID").getString().substring(0,2);
            if(adj.matches("UD")){
                setAttribute("UNADJ_ID",getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,732,188,true));
            }
            if(adj.matches("SD")){
                setAttribute("UNADJ_ID",getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,732,189,true));
            }
            
                
            
//            //------------------------------------//
//            SelectFirstFree aList=new SelectFirstFree();
//            aList.ModuleID=732;
//            
//            if(aList.ShowList()) {
//                SelPrefix=aList.Prefix; //Selected Prefix;
//                SelSuffix=aList.Suffix;
//                FFNo=aList.FirstFreeNo;
//            }
//            
//            System.out.println();
//            setAttribute("UNADJ_ID",getAttribute("UNADJ_ID").getString());
//          
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            
            rsResultSet.updateString("UNADJ_ID",getAttribute("UNADJ_ID").getString());
            rsResultSet.updateString("UNADJ_DATE",getAttribute("UNADJ_DATE").getString());
            rsResultSet.updateString("UNADJ_FROM_DATE",getAttribute("UNADJ_FROM_DATE").getString());
            rsResultSet.updateString("UNADJ_TO_DATE",getAttribute("UNADJ_TO_DATE").getString());
            rsResultSet.updateString("H_REMARK1",getAttribute("H_REMARK1").getString());
            rsResultSet.updateString("H_REMARK2",getAttribute("H_REMARK2").getString());
           
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
             rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
        //    rsHistory.updateString("UPDATE_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATE_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            rsHistory.updateString("UNADJ_ID",getAttribute("UNADJ_ID").getString());
            rsHistory.updateString("UNADJ_DATE",getAttribute("UNADJ_DATE").getString());
            rsHistory.updateString("UNADJ_FROM_DATE",getAttribute("UNADJ_FROM_DATE").getString());
            rsHistory.updateString("UNADJ_TO_DATE",getAttribute("UNADJ_TO_DATE").getString());
            rsHistory.updateString("H_REMARK1",getAttribute("H_REMARK1").getString());
            rsHistory.updateString("H_REMARK2",getAttribute("H_REMARK2").getString());
             
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            // rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
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
            
            
            
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='' ORDER BY INVOICE_NO ");            
            
            //   String tmpmachinests="";
            // String msurvydt="";
            //   String mdttm="";
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                
                clsFeltUnadjitem ObjMRItems=(clsFeltUnadjitem) colMRItems.get(Integer.toString(i));
                
                
                rsTmp.moveToInsertRow();
               
                
           
                rsTmp.updateString("UNADJ_ID",getAttribute("UNADJ_ID").getString());
                
                rsTmp.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getString());
                rsTmp.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getString());
                rsTmp.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getString());
                rsTmp.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getString());
                rsTmp.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",(String)ObjMRItems.getAttribute("INVOICE_DATE").getString());
                rsTmp.updateString("KG",(String)ObjMRItems.getAttribute("KG").getString());
                rsTmp.updateString("SQR_MTR",(String)ObjMRItems.getAttribute("SQR_MTR").getString());
                rsTmp.updateString("WIDTH",(String)ObjMRItems.getAttribute("WIDTH").getString());
                rsTmp.updateString("LENGTH",(String)ObjMRItems.getAttribute("LENGTH").getString());
                rsTmp.updateString("RATE",(String)ObjMRItems.getAttribute("RATE").getString());
                rsTmp.updateString("INV_BASIC_AMT",(String)ObjMRItems.getAttribute("INV_BASIC_AMT").getString());
                rsTmp.updateString("INV_DISC_PER",(String)ObjMRItems.getAttribute("INV_DISC_PER").getString());
                rsTmp.updateString("SANC_DISC_PER",(String)ObjMRItems.getAttribute("SANC_DISC_PER").getString());
                rsTmp.updateString("WORK_DISC_PER",(String)ObjMRItems.getAttribute("WORK_DISC_PER").getObj());
                rsTmp.updateString("INV_SEAM_CHARGES",(String)ObjMRItems.getAttribute("INV_SEAM_CHARGES").getString());
                rsTmp.updateString("SANC_SEAM_CHARGES",(String)ObjMRItems.getAttribute("SANC_SEAM_CHARGES").getString());
                rsTmp.updateString("SEAM_PER",(String)ObjMRItems.getAttribute("SEAM_PER").getString());
                rsTmp.updateString("DISC_AMT",(String)ObjMRItems.getAttribute("DISC_AMT").getObj());
                rsTmp.updateString("D_REMARK1",(String)ObjMRItems.getAttribute("D_REMARK1").getObj());
                rsTmp.updateString("D_REMARK2",(String)ObjMRItems.getAttribute("D_REMARK2").getObj());
                
                rsTmp.updateString("IGST_PER",(String)ObjMRItems.getAttribute("IGST_PER").getObj());
                rsTmp.updateString("IGST_AMT",(String)ObjMRItems.getAttribute("IGST_AMT").getObj());
                rsTmp.updateString("CGST_PER",(String)ObjMRItems.getAttribute("CGST_PER").getObj());
                rsTmp.updateString("CGST_AMT",(String)ObjMRItems.getAttribute("CGST_AMT").getObj());
                rsTmp.updateString("SGST_PER",(String)ObjMRItems.getAttribute("SGST_PER").getObj());
                rsTmp.updateString("SGST_AMT",(String)ObjMRItems.getAttribute("SGST_AMT").getObj());
                rsTmp.updateString("TOTAL_DISC_AMT",(String)ObjMRItems.getAttribute("TOTAL_DISC_AMT").getObj());
                
                rsTmp.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("MODIFIED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsTmp.updateString("MODIFIED_DATE",(String)ObjMRItems.getAttribute("MODIFIED_DATE").getString());
                rsTmp.insertRow();
                
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                
          
                rsHDetail.updateString("UNADJ_ID",getAttribute("UNADJ_ID").getString());
                
                rsHDetail.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getString());
                rsHDetail.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getString());
                rsHDetail.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getString());
                rsHDetail.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getString());
                rsHDetail.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",(String)ObjMRItems.getAttribute("INVOICE_DATE").getString());
                rsHDetail.updateString("KG",(String)ObjMRItems.getAttribute("KG").getString());
                rsHDetail.updateString("SQR_MTR",(String)ObjMRItems.getAttribute("SQR_MTR").getString());
                rsHDetail.updateString("WIDTH",(String)ObjMRItems.getAttribute("WIDTH").getString());
                rsHDetail.updateString("LENGTH",(String)ObjMRItems.getAttribute("LENGTH").getString());
                rsHDetail.updateString("RATE",(String)ObjMRItems.getAttribute("RATE").getString());
                rsHDetail.updateString("INV_BASIC_AMT",(String)ObjMRItems.getAttribute("INV_BASIC_AMT").getString());
                rsHDetail.updateString("INV_DISC_PER",(String)ObjMRItems.getAttribute("INV_DISC_PER").getString());
                rsHDetail.updateString("SANC_DISC_PER",(String)ObjMRItems.getAttribute("SANC_DISC_PER").getString());
                rsHDetail.updateString("WORK_DISC_PER",(String)ObjMRItems.getAttribute("WORK_DISC_PER").getObj());
                rsHDetail.updateString("INV_SEAM_CHARGES",(String)ObjMRItems.getAttribute("INV_SEAM_CHARGES").getString());
                rsHDetail.updateString("SANC_SEAM_CHARGES",(String)ObjMRItems.getAttribute("SANC_SEAM_CHARGES").getString());
                rsHDetail.updateString("SEAM_PER",(String)ObjMRItems.getAttribute("SEAM_PER").getString());
                rsHDetail.updateString("DISC_AMT",(String)ObjMRItems.getAttribute("DISC_AMT").getObj());
                rsHDetail.updateString("D_REMARK1",(String)ObjMRItems.getAttribute("D_REMARK1").getObj());
                rsHDetail.updateString("D_REMARK2",(String)ObjMRItems.getAttribute("D_REMARK2").getObj());
                
                rsHDetail.updateString("IGST_PER",(String)ObjMRItems.getAttribute("IGST_PER").getObj());
                rsHDetail.updateString("IGST_AMT",(String)ObjMRItems.getAttribute("IGST_AMT").getObj());
                rsHDetail.updateString("CGST_PER",(String)ObjMRItems.getAttribute("CGST_PER").getObj());
                rsHDetail.updateString("CGST_AMT",(String)ObjMRItems.getAttribute("CGST_AMT").getObj());
                rsHDetail.updateString("SGST_PER",(String)ObjMRItems.getAttribute("SGST_PER").getObj());
                rsHDetail.updateString("SGST_AMT",(String)ObjMRItems.getAttribute("SGST_AMT").getObj());
                rsHDetail.updateString("TOTAL_DISC_AMT",(String)ObjMRItems.getAttribute("TOTAL_DISC_AMT").getObj());
                
                rsHDetail.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                //  mdttm=EITLERPGLOBAL.getCurrentDate()+""+EITLERPGLOBAL.getCurrentTime();
                //  rsHDetail.updateString("CREATED_DATE",mdttm);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsHDetail.insertRow();
            }
            
            
            //===================== Update the Approval Flow ======================//
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
            
            ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsFeltUnadj.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("UNADJ_ID").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName="PRODUCTION.FELT_UNADJUSTED_TRN_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="UNADJ_ID";
            
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
            /*
             if(ObjFlow.Status.equals("F")){
             
                 //data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MM_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_POSITION_AMEND SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MM_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_POSITION_AMEND_H SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER_H WHERE MM_PARTY_CODE='"+ObjFlow.DocNo+"'");
             }*/
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
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H WHERE UNADJ_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL_H WHERE UNADJ_ID='1' ORDER BY PARTY_CODE");
            rsHDetail.first();
            
            //------------------------------------//
            
            String theDocNo=getAttribute("UNADJ_ID").getString();
           
            //** --------------------------------**//
            
            
            // rsResultSet.updateString("F6_ID", getAttribute("F6_ID").getString());
            rsResultSet.updateString("UNADJ_ID", getAttribute("UNADJ_ID").getString());
            rsResultSet.updateString("UNADJ_DATE", getAttribute("UNADJ_DATE").getString());
            rsResultSet.updateString("UNADJ_FROM_DATE",getAttribute("UNADJ_FROM_DATE").getString());
            rsResultSet.updateString("UNADJ_TO_DATE",getAttribute("UNADJ_TO_DATE").getString());
            rsResultSet.updateString("H_REMARK1",getAttribute("H_REMARK1").getString());
            rsResultSet.updateString("H_REMARK2",getAttribute("H_REMARK2").getString());
            
            
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
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H WHERE UNADJ_ID='"+(String)getAttribute("UNADJ_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("UNADJ_ID").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATE_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATE_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHistory.updateString("UNADJ_ID", getAttribute("UNADJ_ID").getString());
            rsHistory.updateString("UNADJ_DATE", getAttribute("UNADJ_DATE").getString());
            rsHistory.updateString("UNADJ_FROM_DATE", getAttribute("UNADJ_FROM_DATE").getString());
            rsHistory.updateString("UNADJ_TO_DATE",getAttribute("UNADJ_TO_DATE").getString());
            rsHistory.updateString("H_REMARK1",getAttribute("H_REMARK1").getString());
            rsHistory.updateString("H_REMARK2",getAttribute("H_REMARK2").getString());
            
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mProformaNo=(String)getAttribute("UNADJ_ID").getObj();
            
            data.Execute("DELETE FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='"+mProformaNo+"'");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE F6_ID='1'");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='1' ORDER BY INVOICE_NO");
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsFeltUnadjitem ObjMRItems=(clsFeltUnadjitem) colMRItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                
                rsTmp.updateString("UNADJ_ID",(String)getAttribute("UNADJ_ID").getObj());
                
                rsTmp.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getObj());
                rsTmp.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                rsTmp.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getObj());
                rsTmp.updateString("INVOICE_DATE",(String)ObjMRItems.getAttribute("INVOICE_DATE").getObj());
                rsTmp.updateString("KG",(String)ObjMRItems.getAttribute("KG").getObj());
                rsTmp.updateString("SQR_MTR",(String)ObjMRItems.getAttribute("SQR_MTR").getObj());
                rsTmp.updateString("WIDTH",(String)ObjMRItems.getAttribute("WIDTH").getObj());
                rsTmp.updateString("LENGTH",(String)ObjMRItems.getAttribute("LENGTH").getObj());
                rsTmp.updateString("RATE",(String)ObjMRItems.getAttribute("RATE").getObj());
                rsTmp.updateString("INV_BASIC_AMT",(String)ObjMRItems.getAttribute("INV_BASIC_AMT").getObj());
                rsTmp.updateString("INV_DISC_PER",(String)ObjMRItems.getAttribute("INV_DISC_PER").getObj());
                rsTmp.updateString("SANC_DISC_PER",(String)ObjMRItems.getAttribute("SANC_DISC_PER").getObj());
                rsTmp.updateString("WORK_DISC_PER",(String)ObjMRItems.getAttribute("WORK_DISC_PER").getObj());
                rsTmp.updateString("INV_SEAM_CHARGES",(String)ObjMRItems.getAttribute("INV_SEAM_CHARGES").getObj());
                rsTmp.updateString("SANC_SEAM_CHARGES",(String)ObjMRItems.getAttribute("SANC_SEAM_CHARGES").getObj());
                rsTmp.updateString("SEAM_PER",(String)ObjMRItems.getAttribute("SEAM_PER").getObj());
                rsTmp.updateString("DISC_AMT",(String)ObjMRItems.getAttribute("DISC_AMT").getObj());
                rsTmp.updateString("D_REMARK1",(String)ObjMRItems.getAttribute("D_REMARK1").getObj());
                rsTmp.updateString("D_REMARK2",(String)ObjMRItems.getAttribute("D_REMARK2").getObj());
                
                rsTmp.updateString("IGST_PER",(String)ObjMRItems.getAttribute("IGST_PER").getObj());
                rsTmp.updateString("IGST_AMT",(String)ObjMRItems.getAttribute("IGST_AMT").getObj());
                rsTmp.updateString("CGST_PER",(String)ObjMRItems.getAttribute("CGST_PER").getObj());
                rsTmp.updateString("CGST_AMT",(String)ObjMRItems.getAttribute("CGST_AMT").getObj());
                rsTmp.updateString("SGST_PER",(String)ObjMRItems.getAttribute("SGST_PER").getObj());
                rsTmp.updateString("SGST_AMT",(String)ObjMRItems.getAttribute("SGST_AMT").getObj());
                rsTmp.updateString("TOTAL_DISC_AMT",(String)ObjMRItems.getAttribute("TOTAL_DISC_AMT").getObj());
                
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateString("UNADJ_ID",(String)getAttribute("UNADJ_ID").getObj());
                
                rsHDetail.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getObj());
                rsHDetail.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                rsHDetail.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getObj());
                rsHDetail.updateString("INVOICE_DATE",(String)ObjMRItems.getAttribute("INVOICE_DATE").getObj());
                rsHDetail.updateString("KG",(String)ObjMRItems.getAttribute("KG").getObj());
                rsHDetail.updateString("SQR_MTR",(String)ObjMRItems.getAttribute("SQR_MTR").getObj());
                rsHDetail.updateString("WIDTH",(String)ObjMRItems.getAttribute("WIDTH").getObj());
                rsHDetail.updateString("LENGTH",(String)ObjMRItems.getAttribute("LENGTH").getObj());
                rsHDetail.updateString("RATE",(String)ObjMRItems.getAttribute("RATE").getObj());
                rsHDetail.updateString("INV_BASIC_AMT",(String)ObjMRItems.getAttribute("INV_BASIC_AMT").getObj());
                rsHDetail.updateString("INV_DISC_PER",(String)ObjMRItems.getAttribute("INV_DISC_PER").getObj());
                rsHDetail.updateString("SANC_DISC_PER",(String)ObjMRItems.getAttribute("SANC_DISC_PER").getObj());
                rsHDetail.updateString("WORK_DISC_PER",(String)ObjMRItems.getAttribute("WORK_DISC_PER").getObj());
                rsHDetail.updateString("INV_SEAM_CHARGES",(String)ObjMRItems.getAttribute("INV_SEAM_CHARGES").getObj());
                rsHDetail.updateString("SANC_SEAM_CHARGES",(String)ObjMRItems.getAttribute("SANC_SEAM_CHARGES").getObj());
                rsHDetail.updateString("SEAM_PER",(String)ObjMRItems.getAttribute("SEAM_PER").getObj());
                rsHDetail.updateString("DISC_AMT",(String)ObjMRItems.getAttribute("DISC_AMT").getObj());
                rsHDetail.updateString("D_REMARK1",(String)ObjMRItems.getAttribute("D_REMARK1").getObj());
                rsHDetail.updateString("D_REMARK2",(String)ObjMRItems.getAttribute("D_REMARK2").getObj());
                
                rsHDetail.updateString("IGST_PER",(String)ObjMRItems.getAttribute("IGST_PER").getObj());
                rsHDetail.updateString("IGST_AMT",(String)ObjMRItems.getAttribute("IGST_AMT").getObj());
                rsHDetail.updateString("CGST_PER",(String)ObjMRItems.getAttribute("CGST_PER").getObj());
                rsHDetail.updateString("CGST_AMT",(String)ObjMRItems.getAttribute("CGST_AMT").getObj());
                rsHDetail.updateString("SGST_PER",(String)ObjMRItems.getAttribute("SGST_PER").getObj());
                rsHDetail.updateString("SGST_AMT",(String)ObjMRItems.getAttribute("SGST_AMT").getObj());
                rsHDetail.updateString("TOTAL_DISC_AMT",(String)ObjMRItems.getAttribute("TOTAL_DISC_AMT").getObj());
                
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
            
            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsFeltUnadj.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocNo=(String)getAttribute("UNADJ_ID").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_UNADJUSTED_TRN_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="UNADJ_ID";
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
                data.Execute("UPDATE PRODUCTION.FELT_UNADJUSTED_TRN_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE UNADJ_ID='"+ObjFlow.DocNo+"' ");
                 
                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+clsFeltUnadj.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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

	if(ObjFlow.Status.equals("F")){
        	//data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MM_PARTY_CODE='"+ObjFlow.DocNo+"'");
                data.Execute("INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_H SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='"+ObjFlow.DocNo+"'");
                data.Execute("INSERT INTO PRODUCTION.FELT_UNADJUSTED_TRN_D SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='"+ObjFlow.DocNo+"'");                 
                
                GenerateUnadjGSTCN(getAttribute("UNADJ_ID").getString(),getAttribute("UNADJ_FROM_DATE").getString(),getAttribute("UNADJ_TO_DATE").getString());
                UnadjGSTCNVoucherPosting(getAttribute("UNADJ_ID").getString(),getAttribute("UNADJ_FROM_DATE").getString(),getAttribute("UNADJ_TO_DATE").getString());
      	}

            //--------- Approval Flow Update complete -----------
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
            setAttribute("UNADJ_ID",rsResultSet.getString("UNADJ_ID"));
            setAttribute("UNADJ_DATE",rsResultSet.getString("UNADJ_DATE"));
            setAttribute("UNADJ_FROM_DATE",rsResultSet.getString("UNADJ_FROM_DATE"));
            setAttribute("UNADJ_TO_DATE",rsResultSet.getString("UNADJ_TO_DATE"));
            setAttribute("H_REMARK1",rsResultSet.getString("H_REMARK1"));
            setAttribute("H_REMARK2",rsResultSet.getString("H_REMARK2"));
            
            
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDateDB(rsResultSet.getString("CREATED_DATE")));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));
            setAttribute("CHANGED",rsResultSet.getBoolean("CHANGED"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
           
            
            
            
            
            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();
            String mdocno=(String) getAttribute("UNADJ_ID").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL_H WHERE UNADJ_ID='"+mdocno+"' AND REVISION_NO="+RevNo+" ORDER BY PARTY_CODE");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='"+mdocno+"' ORDER BY PARTY_CODE");
            }
            
            /*if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H AS AA WHERE F6_ID='"+mdocno+"' AND REVISION_NO="+RevNo+" ORDER BY AA.F6_ID");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER AS AA WHERE F6_ID='"+mdocno+"' ORDER BY AA.F6_ID");
            }*/
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFeltUnadjitem ObjMRItems = new clsFeltUnadjitem();
          
            
                ObjMRItems.setAttribute("UNADJ_ID",rsTmp.getString("UNADJ_ID"));
                
                ObjMRItems.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                ObjMRItems.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjMRItems.setAttribute("PIECE_NO",rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("PRODUCT_CODE",rsTmp.getString("PRODUCT_CODE"));
                ObjMRItems.setAttribute("INVOICE_NO",rsTmp.getString("INVOICE_NO"));
                ObjMRItems.setAttribute("INVOICE_DATE",rsTmp.getString("INVOICE_DATE"));
                ObjMRItems.setAttribute("KG",rsTmp.getString("KG"));
                ObjMRItems.setAttribute("SQR_MTR",rsTmp.getString("SQR_MTR"));
                ObjMRItems.setAttribute("WIDTH",rsTmp.getString("WIDTH"));
                ObjMRItems.setAttribute("LENGTH",rsTmp.getString("LENGTH"));
                ObjMRItems.setAttribute("RATE",rsTmp.getString("RATE"));
                ObjMRItems.setAttribute("INV_BASIC_AMT",rsTmp.getString("INV_BASIC_AMT"));
                ObjMRItems.setAttribute("INV_DISC_PER",rsTmp.getString("INV_DISC_PER"));
                ObjMRItems.setAttribute("SANC_DISC_PER",rsTmp.getString("SANC_DISC_PER"));
                ObjMRItems.setAttribute("WORK_DISC_PER",rsTmp.getString("WORK_DISC_PER"));
                ObjMRItems.setAttribute("INV_SEAM_CHARGES",rsTmp.getString("INV_SEAM_CHARGES"));
                ObjMRItems.setAttribute("SANC_SEAM_CHARGES",rsTmp.getString("SANC_SEAM_CHARGES"));
                ObjMRItems.setAttribute("SEAM_PER",rsTmp.getString("SEAM_PER"));
                ObjMRItems.setAttribute("DISC_AMT",rsTmp.getString("DISC_AMT"));
                ObjMRItems.setAttribute("D_REMARK1",rsTmp.getString("D_REMARK1"));
                ObjMRItems.setAttribute("D_REMARK2",rsTmp.getString("D_REMARK2"));
                
                ObjMRItems.setAttribute("IGST_PER",rsTmp.getString("IGST_PER"));
                ObjMRItems.setAttribute("IGST_AMT",rsTmp.getString("IGST_AMT"));
                ObjMRItems.setAttribute("CGST_PER",rsTmp.getString("CGST_PER"));
                ObjMRItems.setAttribute("CGST_AMT",rsTmp.getString("CGST_AMT"));
                ObjMRItems.setAttribute("SGST_PER",rsTmp.getString("SGST_PER"));
                ObjMRItems.setAttribute("SGST_AMT",rsTmp.getString("SGST_AMT"));
                ObjMRItems.setAttribute("TOTAL_DISC_AMT",rsTmp.getString("TOTAL_DISC_AMT"));
                
                
                ObjMRItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                colMRItems.put(Long.toString(Counter),ObjMRItems);
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
    
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=732 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    public static HashMap getHistoryList(int CompanyID,String Docno) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H WHERE UNADJ_ID='"+Docno+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsFeltUnadj objParty=new clsFeltUnadj();
                    
                    objParty.setAttribute("UNADJ_ID",rsTmp.getString("UNADJ_ID"));
                    objParty.setAttribute("UNADJ_DATE",rsTmp.getString("UNADJ_DATE"));
                    objParty.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                    objParty.setAttribute("UPDATE_BY",rsTmp.getInt("UPDATE_BY"));
                    objParty.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                    objParty.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                    objParty.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                    
                    List.put(Integer.toString(List.size()+1),objParty);
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
            //strSQL+= "SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MM_PARTY_CODE=BB.PARTY_CODE WHERE " + Condition ;
            strSQL+= "SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(rsResultSet.first()) {
                Ready=true;
                MoveLast();
                return true;
            }
            else{
                return false;
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=732 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            String lDocNo=(String)getAttribute("UNADJ_ID").getObj();
            String strSQL="";
         
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL WHERE UNADJ_ID='"+lDocNo+"'";
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
        //return false;
    }
    
    
    public Object getObject(int CompanyID, String PartyCode) {
        String strCondition = " WHERE MM_PARTY_CODE='" + PartyCode + "' ";
        clsSales_Party objParty = new clsSales_Party();
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
    
    /*
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID),PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID),PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732 ORDER BY PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.CREATED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID),PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732 ORDER BY PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltUnadj ObjDoc=new clsFeltUnadj();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("UNADJ_ID",rsTmp.getString("UNADJ_ID"));
                ObjDoc.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
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
    
    */
    
    
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
                strSQL="SELECT PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732 ORDER BY PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.CREATED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=732 ORDER BY PRODUCTION.FELT_UNADJUSTED_TRN_HEADER.UNADJ_ID";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsFeltUnadj ObjItem = new clsFeltUnadj();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("UNADJ_ID"));
                //ObjItem.setAttribute("DOC_DATE",rsTmp.getString("UNADJ_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                //ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
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
    
    public boolean ShowHistory(int pCompanyID,String pdocno) {
        Ready=false;
        try {
            //Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER_H WHERE UNADJ_ID='"+pdocno+"' ORDER BY REVISION_NO");
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
    
//    public static String[] getPiecedetail(String pPartyCode){
//        Connection tmpConn;
//        Statement stTmp;
//        ResultSet rsTmp;
//        String []Piecedetail = new String[40];
//        String []error = {"error"};
//        
//        try {
//            
//            tmpConn=data.getConn();
//            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            //String strSQL="SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MM_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MM_POSITION = B.POSITION_NO WHERE ";
//            String strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SEAM_CHARGE,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMOUNT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '2015-01-01' AND F.INVOICE_DATE<='2015-01-31' AND F.PIECE_NO='013221' AND F.INVOICE_NO='F002261' AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)";
////            if(!pPartyCode.equals("")){
////                strSQL+="AA.MM_PARTY_CODE='"+pPartyCode+"'";
////            }
//            System.out.println("Piece Detail Query :");
//            System.out.println(strSQL);
//            rsTmp=stTmp.executeQuery(strSQL);
//            rsTmp.first();
//            
//            if(rsTmp.getRow() > 0) {
//                Piecedetail[0]=rsTmp.getString("PARTY_NAME");
//                Piecedetail[1]=rsTmp.getString("PARTY_CODE");
//                Piecedetail[2]=rsTmp.getString("PIECE_NO");
//                Piecedetail[3]=rsTmp.getString("QUALITY_NO");
//                Piecedetail[4]=rsTmp.getString("INVOICE_NO");
//                Piecedetail[5]=rsTmp.getString("INVOICE_DATE");
//                Piecedetail[6]=rsTmp.getString("GROSS_KG");
//                Piecedetail[7]=rsTmp.getString("GROSS_SQ_MTR");
//                Piecedetail[8]=rsTmp.getString("WIDTH");
//                Piecedetail[9]=rsTmp.getString("LENGTH");
//                Piecedetail[10]=rsTmp.getString("RATE");
//                Piecedetail[11]=rsTmp.getString("GROSS_AMOUNT");
//                Piecedetail[12]=rsTmp.getString("INV_DISC_PER");
//                Piecedetail[13]=rsTmp.getString("SANC_DISC_PER");
//                Piecedetail[14]=rsTmp.getString("WORK_DISC_PER");
//                Piecedetail[15]=rsTmp.getString("SEAM_CHARGE");
//                Piecedetail[17]=rsTmp.getString("DISC_AMOUNT");
////                Piecedetail[18]=rsTmp.getString("MM_SPEED");
////                Piecedetail[19]=rsTmp.getString("MM_SURVEY_DATE");
////                Piecedetail[20]=rsTmp.getString("MM_WIRE_LENGTH");
////                Piecedetail[21]=rsTmp.getString("MM_WIRE_WIDTH");
////                Piecedetail[22]=rsTmp.getString("MM_WIRE_TYPE");
////                Piecedetail[23]=rsTmp.getString("MM_TECH_REP");
////                Piecedetail[24]=rsTmp.getString("MM_TYPE_OF_FILLER");
////                Piecedetail[25]=rsTmp.getString("MM_PAPER_DECKLE");
////                Piecedetail[26]=rsTmp.getString("MM_MCH_ACTIVE");
////                Piecedetail[27]=rsTmp.getString("MM_LIFE_OF_FELT");
////                Piecedetail[28]=rsTmp.getString("MM_CONSUMPTION");
////                Piecedetail[29]=rsTmp.getString("MM_DINESH_SHARE");
////                
//                //Piecedetail[27]=rsTmp.getString("CREATED_BY");
//                //Piecedetail[28]=EITLERPGLOBAL.formatDate(rsTmp.getString("CREATED_DATE"));
//                //Piecedetail[28]=rsTmp.getString("MODIFIED_BY");
//                //Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("MODIFIED_DATE"));
//                //Piecedetail[30]=rsTmp.getString("MM_PAPERGRADE");
//            }
//            //tmpConn.close();
//            stTmp.close();
//            rsTmp.close();
//            return Piecedetail;
//            
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            return error;
//        }
//    }
    
    public static String[] getPiecedetail(String pUnadjId,String pPieceNo,String pInvNo, String pFromDate, String pToDate){
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String []Piecedetail = new String[40];
        String []error = {"error"};
        String strSQL="";
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //String strSQL="SELECT * FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MM_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MM_POSITION = B.POSITION_NO WHERE ";
            
            if(pUnadjId.matches("UD"))
            {
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SPIRAL_CHG AS INV_SEAM_CHARGES,D.SEAM_VALUE AS SANC_SEAM_CHARGES,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMOUNT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+pFromDate+"' AND F.INVOICE_DATE<='"+pToDate+"' AND F.PIECE_NO='"+pPieceNo+"' AND F.INVOICE_NO='"+pInvNo+"' AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)";
                //strSQL="SELECT PARTY_NAME,PARTY_CODE,PIECE_NO,QUALITY_NO,INVOICE_NO,INVOICE_DATE,GROSS_KG,GROSS_SQ_MTR,WIDTH,LENGTH,RATE,GROSS_AMOUNT,ROUND((TRD_DISCOUNT*100/GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(((SPIRAL_CHG/WIDTH)/4899)*100,2) AS INV_SEAM_CHARGES  FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+pFromDate+"' AND INVOICE_DATE<='"+pToDate+"' AND PIECE_NO='"+pPieceNo+"' AND INVOICE_NO='"+pInvNo+"' AND INVOICE_NO NOT IN (SELECT INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL GROUP BY INVOICE_NO,PIECE_NO)";
                strSQL="SELECT H.PARTY_NAME,H.PARTY_CODE,H.PIECE_NO,H.PRODUCT_CODE AS QUALITY_NO,H.INVOICE_NO,H.INVOICE_DATE,H.ACTUAL_WEIGHT AS GROSS_KG,H.SQMTR AS GROSS_SQ_MTR,H.WIDTH,H.LENGTH,H.RATE,H.BAS_AMT AS GROSS_AMOUNT,ROUND((H.DISC_AMT*100/H.BAS_AMT),2) AS INV_DISC_PER,ROUND(((H.SEAM_CHG/H.WIDTH)/4899)*100,2) AS INV_SEAM_CHARGES  FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND H.INVOICE_DATE>= '"+pFromDate+"' AND H.INVOICE_DATE<='"+pToDate+"' AND D.PIECE_NO='"+pPieceNo+"' AND H.INVOICE_NO='"+pInvNo+"' AND H.INVOICE_NO NOT IN (SELECT INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL GROUP BY INVOICE_NO,PIECE_NO) AND H.APPROVED=1 AND H.CANCELLED=0";
            }
            if(pUnadjId.matches("SD"))
            {
                //strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SPIRAL_CHG AS INV_SEAM_CHARGES,D.SEAM_VALUE AS SANC_SEAM_CHARGES,F.SPIRAL_CHG AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+pFromDate+"' AND F.INVOICE_DATE<='"+pToDate+"' AND F.PIECE_NO='"+pPieceNo+"' AND F.INVOICE_NO='"+pInvNo+"' AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)";
                //strSQL="SELECT PARTY_NAME,PARTY_CODE,PIECE_NO,QUALITY_NO,INVOICE_NO,INVOICE_DATE,GROSS_KG,GROSS_SQ_MTR,WIDTH,LENGTH,RATE,GROSS_AMOUNT,ROUND((TRD_DISCOUNT*100/GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(((SPIRAL_CHG/WIDTH)/4899)*100,2) AS INV_SEAM_CHARGES  FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+pFromDate+"' AND INVOICE_DATE<='"+pToDate+"' AND PIECE_NO='"+pPieceNo+"' AND INVOICE_NO='"+pInvNo+"' AND INVOICE_NO NOT IN (SELECT INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL GROUP BY INVOICE_NO,PIECE_NO)";
                strSQL="SELECT H.PARTY_NAME,H.PARTY_CODE,H.PIECE_NO,H.PRODUCT_CODE AS QUALITY_NO,H.INVOICE_NO,H.INVOICE_DATE,H.ACTUAL_WEIGHT AS GROSS_KG,H.SQMTR AS GROSS_SQ_MTR,H.WIDTH,H.LENGTH,H.RATE,H.BAS_AMT AS GROSS_AMOUNT,ROUND((H.DISC_AMT*100/H.BAS_AMT),2) AS INV_DISC_PER,ROUND(((H.SEAM_CHG/H.WIDTH)/4899)*100,2) AS INV_SEAM_CHARGES  FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H, PRODUCTION.FELT_SAL_INVOICE_DETAIL D WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) AND H.INVOICE_DATE>= '"+pFromDate+"' AND H.INVOICE_DATE<='"+pToDate+"' AND D.PIECE_NO='"+pPieceNo+"' AND H.INVOICE_NO='"+pInvNo+"' AND H.INVOICE_NO NOT IN (SELECT INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL GROUP BY INVOICE_NO,PIECE_NO) AND H.APPROVED=1 AND H.CANCELLED=0";
            }
//            if(!pPartyCode.equals("")){
//                strSQL+="AA.MM_PARTY_CODE='"+pPartyCode+"'";
//            }
            System.out.println("Detail of Piece NO & Invoice No :");
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow() > 0) {
                Piecedetail[0]=rsTmp.getString("PARTY_NAME");
                Piecedetail[1]=rsTmp.getString("PARTY_CODE");
                Piecedetail[2]=rsTmp.getString("PIECE_NO");
                Piecedetail[3]=rsTmp.getString("QUALITY_NO");
                Piecedetail[4]=rsTmp.getString("INVOICE_NO");
                Piecedetail[5]=EITLERPGLOBAL.formatDate(rsTmp.getString("INVOICE_DATE"));
                Piecedetail[6]=rsTmp.getString("GROSS_KG");
                Piecedetail[7]=rsTmp.getString("GROSS_SQ_MTR");
                Piecedetail[8]=rsTmp.getString("WIDTH");
                Piecedetail[9]=rsTmp.getString("LENGTH");
                Piecedetail[10]=rsTmp.getString("RATE");
                Piecedetail[11]=rsTmp.getString("GROSS_AMOUNT");
                Piecedetail[12]=rsTmp.getString("INV_DISC_PER");
                //Piecedetail[13]=rsTmp.getString("SANC_DISC_PER");
                //Piecedetail[14]=rsTmp.getString("WORK_DISC_PER");
                Piecedetail[13]="0.00";
                Piecedetail[14]="0.00";
                Piecedetail[15]=rsTmp.getString("INV_SEAM_CHARGES");
                //Piecedetail[16]=rsTmp.getString("SANC_SEAM_CHARGES");
                //Piecedetail[17]=rsTmp.getString("SEAM_PER");
                //Piecedetail[18]=rsTmp.getString("DISC_AMOUNT");
                Piecedetail[16]="0.00";
                Piecedetail[17]="0.00";
                Piecedetail[18]="0.00";
                
//                Piecedetail[18]=rsTmp.getString("MM_SPEED");
//                Piecedetail[19]=rsTmp.getString("MM_SURVEY_DATE");
//                Piecedetail[20]=rsTmp.getString("MM_WIRE_LENGTH");
//                Piecedetail[21]=rsTmp.getString("MM_WIRE_WIDTH");
//                Piecedetail[22]=rsTmp.getString("MM_WIRE_TYPE");
//                Piecedetail[23]=rsTmp.getString("MM_TECH_REP");
//                Piecedetail[24]=rsTmp.getString("MM_TYPE_OF_FILLER");
//                Piecedetail[25]=rsTmp.getString("MM_PAPER_DECKLE");
//                Piecedetail[26]=rsTmp.getString("MM_MCH_ACTIVE");
//                Piecedetail[27]=rsTmp.getString("MM_LIFE_OF_FELT");
//                Piecedetail[28]=rsTmp.getString("MM_CONSUMPTION");
//                Piecedetail[29]=rsTmp.getString("MM_DINESH_SHARE");
//                
                //Piecedetail[27]=rsTmp.getString("CREATED_BY");
                //Piecedetail[28]=EITLERPGLOBAL.formatDate(rsTmp.getString("CREATED_DATE"));
                //Piecedetail[28]=rsTmp.getString("MODIFIED_BY");
                //Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("MODIFIED_DATE"));
                //Piecedetail[30]=rsTmp.getString("MM_PAPERGRADE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Piecedetail;
            
        }
        catch(Exception e) {
            e.printStackTrace();
            return error;
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
    
    public static String getpositiondesc(String pposition) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String positiondesc="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION_DESC FROM PRODUCTION.FELT_MACHINE_POSITION_MST WHERE POSITION_NO='"+pposition+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                positiondesc=rsTmp.getString("POSITION_DESC");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return positiondesc;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getpressStyle(String pposition) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String pressStyle="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                pressStyle=rsTmp.getString("PARA_DESC");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return pressStyle;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getcombinationcode(String pmachine,String pposition) {
        try{
            String mcmbcode="";
            int mmachine=100+(Integer.parseInt(pmachine));
            int mposition=100+(Integer.parseInt(pposition));
            mcmbcode=(Integer.toString(mposition).substring(1,3))+""+(Integer.toString(mmachine).substring(1,3));
            return mcmbcode;
        }catch(Exception e){
            return "";
        }
        
    }
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        long Counter2=0;
        
        tmpConn=data.getConn();
        
        try {
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSales_Party ObjParty =new clsSales_Party();
                ObjParty.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjParty.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                //ObjParty.setAttribute("PARTY_TYPE",rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                ObjParty.setAttribute("HIERARCHY_ID",rsTmp.getLong("HIERARCHY_ID"));
                
            }//Outer while
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            //LastError = e.getMessage();
        }
        
        return List;
    }
    
    public static Object getObjectEx(int pCompanyID,String pPartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode+"' ";
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }
    
    public static Object getObjectExN(int pCompanyID,String pPartyCode, String MainCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode+"' AND MAIN_ACCOUNT_CODE="+MainCode;
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }
    
    
    
    public static boolean deleteParty(String PartyCode) {
        try {
            //data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE DOC_NO='"+PartyCode+"' AND MODULE_ID='"+clsOrderParty.ModuleID+"' ");
            //data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
            //data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    
    public static String getSeasonCode() {
        String SeasonCode = "";
        try {
            SeasonCode = data.getStringValueFromDB("SELECT SEASON_ID FROM D_SAL_SEASON_MASTER WHERE CURDATE() BETWEEN DATE_FROM AND DATE_TO");
        } catch(Exception e) {
            return SeasonCode;
        }
        return SeasonCode;
    }
//    
//   public static String getNextFreeNo(int pModuleID,boolean UpdateLastNo) {
//        Connection tmpConn;
//        Statement tmpStmt;
//        ResultSet rsTmp;
//        String strSQL="";
//        String strNewNo="";
//        int lnNewNo=0;
//        String Prefix="";
//        String Suffix="";
//        
//        try {
//            tmpConn=data.getConn();
//            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            
//            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
//            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=732 AND FIRSTFREE_NO=182";
//            rsTmp=tmpStmt.executeQuery(strSQL);
//            
//            rsTmp.first();
//            if(rsTmp.getRow()>0) {
//                lnNewNo=rsTmp.getInt("LAST_USED_NO")+1;
//                strNewNo=EITLERPGLOBAL.Padding(Integer.toString(lnNewNo),rsTmp.getInt("NO_LENGTH"),rsTmp.getString("PADDING_BY"));
//                Prefix=rsTmp.getString("PREFIX_CHARS");
//                Suffix=rsTmp.getString("SUFFIX_CHARS");
//                
//                if(UpdateLastNo) {
//                    //Update last no. in database
//                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
//                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=732 AND FIRSTFREE_NO=182");
//                }
//                
//                strNewNo = Prefix+ strNewNo+Suffix;
//                
//                //tmpConn.close();
//                tmpStmt.close();
//                rsTmp.close();
//                
//                return strNewNo;
//            }
//            else {
//                return "";
//            }
//        }
//        catch(Exception e) {
//            return "";
//        }
//    }
//   
   public static String getNewDisamt(String pDiscper,String pBasamt) {
        
        double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);
        
        double newDisamt=0.00;
        newDisamt=(Basamt)*(Discper/100);
        newDisamt=EITLERPGLOBAL.round(newDisamt, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newDisamt);
        
    }
   
   public static String getSeamDisamt(String pWidth,String pDiscper,String pSpiralChg) {
        
        double Width = Double.parseDouble(pWidth);
        double Discper = Double.parseDouble(pDiscper);
        double SpiralChg = EITLERPGLOBAL.round(((Double.parseDouble(pSpiralChg)*4899)/100),0)*Width;
        
        double newDisamt=0.00;
        newDisamt=(SpiralChg/100)*(Discper);
        newDisamt=EITLERPGLOBAL.round(newDisamt, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newDisamt);
        
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
   
   public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            System.out.println("SELECT UNADJ_ID FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='"+pDocNo+"'  AND APPROVED=0 AND CANCELLED=0");
            rsTmp=stTmp.executeQuery("SELECT UNADJ_ID FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='"+pDocNo+"'  AND APPROVED=0 AND CANCELLED=0");
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return CanCancel;
    }
    
   public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pDocNo)) {
            
            boolean Approved=false;
            //PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER WHERE PIECE_AMEND_STOCK_NO
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_UNADJUSTED_TRN_HEADER WHERE UNADJ_ID='"+pDocNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pDocNo+"' AND MODULE_ID=732");
                }
                data.Execute("UPDATE PRODUCTION.FELT_UNADJUSTED_TRN_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE UNADJ_ID='"+pDocNo+"'");
              
            }
            catch(Exception e) {
                
            }
        }
        
    }
}
