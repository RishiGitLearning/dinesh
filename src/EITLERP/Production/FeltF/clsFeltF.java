    /*
     * clsFeltF.java
     *
     * Created on March 1,2015
     */

package EITLERP.Production.FeltF;

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
 * @author  Dharmendra
 */
public class clsFeltF {
    
    public String LastError="";
    //private ResultSet rsResultSet,rsResultSet1;
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=731; //72
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
    public clsFeltF() {
        LastError = "";
        props=new HashMap();
        props.put("F6_ID",new Variant(""));
        props.put("F6_FROM_DATE",new Variant(""));
        props.put("F6_TO_DATE",new Variant(""));
      
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
          rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER");
          //  rsResultSet=Stmt.executeQuery("SELECT *, B.F6_ID,B.F6_FROM_DATE,B.F6_TO_DATE,A.INVOICE_NO,A.INVOICE_DATE,A.PARTY_CODE,A.PARTY_NAME,A.INVOICE_AMT,A.EXT1,A.EXT2,A.EXT3,A.EXT4,A.EXT5,A.EXT6 FROM PRODUCTION.FELT_F6_TRN_HEADER B,PRODUCTION.FELT_F6_TRN_DETAIL A WHERE A.F6_ID=B.F6_ID");
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
            
            frmFeltF fms=new frmFeltF();
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER_H WHERE F6_ID=''"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_DETAIL_H WHERE F6_ID='' ORDER BY INVOICE_NO");
            rsHDetail.first();
            
           // setAttribute("F6_ID",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,731, (int)getAttribute("FFNO").getVal(),true));
            setAttribute("F6_ID",getNextFreeNo(731,true));
            //------------------------------------//
            SelectFirstFree aList=new SelectFirstFree();
            aList.ModuleID=731;
            
            if(aList.ShowList()) {
                SelPrefix=aList.Prefix; //Selected Prefix;
                SelSuffix=aList.Suffix;
                FFNo=aList.FirstFreeNo;
            }
            
            System.out.println();
            setAttribute("F6_ID",getAttribute("F6_ID").getString());
          
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            
            rsResultSet.updateString("F6_ID",getAttribute("F6_ID").getString());
            rsResultSet.updateString("F6_FROM_DATE",getAttribute("F6_FROM_DATE").getString());
            rsResultSet.updateString("F6_TO_DATE",getAttribute("F6_TO_DATE").getString());
           
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
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
            rsHistory.updateString("F6_ID",getAttribute("F6_ID").getString());
            rsHistory.updateString("F6_FROM_DATE",getAttribute("F6_FROM_DATE").getString());
            rsHistory.updateString("F6_TO_DATE",getAttribute("F6_TO_DATE").getString());
             
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            // rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_DETAIL WHERE F6_ID='' ORDER BY INVOICE_NO ");            
            
            //   String tmpmachinests="";
            // String msurvydt="";
            //   String mdttm="";
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                
                clsFeltFitem ObjMRItems=(clsFeltFitem) colMRItems.get(Integer.toString(i));
                
                
                rsTmp.moveToInsertRow();
               
                
           
                rsTmp.updateString("F6_ID",getAttribute("F6_ID").getString());
                rsTmp.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getString());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB((String)ObjMRItems.getAttribute("INVOICE_DATE").getString()));
                rsTmp.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getString());
                rsTmp.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getString());
                rsTmp.updateString("INVOICE_AMT",(String)ObjMRItems.getAttribute("INVOICE_AMT").getString());
                
                
                
                rsTmp.updateString("EXT1",(String)ObjMRItems.getAttribute("EXT1").getObj());
                rsTmp.updateString("EXT2",(String)ObjMRItems.getAttribute("EXT2").getObj());
                rsTmp.updateString("EXT3",(String)ObjMRItems.getAttribute("EXT3").getObj());
                rsTmp.updateString("EXT4",(String)ObjMRItems.getAttribute("EXT4").getObj());
                rsTmp.updateString("EXT5",(String)ObjMRItems.getAttribute("EXT5").getObj());
                rsTmp.updateString("EXT6",(String)ObjMRItems.getAttribute("EXT6").getObj());
                
                
                rsTmp.updateString("DD_CH_NO",(String)ObjMRItems.getAttribute("DD_CH_NO").getString());
                rsTmp.updateString("DD_CH_DATE",(String)ObjMRItems.getAttribute("DD_CH_DATE").getString());
                rsTmp.updateString("DD_CH_AMT",(String)ObjMRItems.getAttribute("DD_CH_AMT").getString());
                rsTmp.updateString("DD_CH_CLR_DATE",(String)ObjMRItems.getAttribute("DD_CH_CLR_DATE").getString());
                rsTmp.updateString("LATE_DAYS",(String)ObjMRItems.getAttribute("LATE_DAYS").getString());
                rsTmp.updateString("UNIT_GRP_VOL_PRE",(String)ObjMRItems.getAttribute("UNIT_GRP_VOL_PRE").getString());
                rsTmp.updateString("UNIT_GRP_VOL_CUR",(String)ObjMRItems.getAttribute("UNIT_GRP_VOL_CUR").getString());
                rsTmp.updateString("UNIT_GRP_VOL_EXP",(String)ObjMRItems.getAttribute("UNIT_GRP_VOL_EXP").getString());
                rsTmp.updateString("REMARKS",(String)ObjMRItems.getAttribute("REMARKS").getString());
                
                rsTmp.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("MODIFIED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsTmp.updateString("MODIFIED_DATE",(String)ObjMRItems.getAttribute("MODIFIED_DATE").getString());
                rsTmp.insertRow();
                
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                
          
                rsHDetail.updateString("F6_ID",getAttribute("F6_ID").getString());
                rsHDetail.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB((String)ObjMRItems.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getString());
                rsHDetail.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getString());
                rsHDetail.updateString("INVOICE_AMT",(String)ObjMRItems.getAttribute("INVOICE_AMT").getString());
                
                rsHDetail.updateString("EXT1",(String)ObjMRItems.getAttribute("EXT1").getObj());
                rsHDetail.updateString("EXT2",(String)ObjMRItems.getAttribute("EXT2").getObj());
                rsHDetail.updateString("EXT3",(String)ObjMRItems.getAttribute("EXT3").getObj());
                rsHDetail.updateString("EXT4",(String)ObjMRItems.getAttribute("EXT4").getObj());
                rsHDetail.updateString("EXT5",(String)ObjMRItems.getAttribute("EXT5").getObj());
                
                
                rsHDetail.updateString("DD_CH_NO",(String)ObjMRItems.getAttribute("DD_CH_NO").getString());
                rsHDetail.updateString("DD_CH_DATE",(String)ObjMRItems.getAttribute("DD_CH_DATE").getString());
                rsHDetail.updateString("DD_CH_AMT",(String)ObjMRItems.getAttribute("DD_CH_AMT").getString());
                rsHDetail.updateString("DD_CH_CLR_DATE",(String)ObjMRItems.getAttribute("DD_CH_CLR_DATE").getString());
                rsHDetail.updateString("LATE_DAYS",(String)ObjMRItems.getAttribute("LATE_DAYS").getString());
                rsHDetail.updateString("UNIT_GRP_VOL_PRE",(String)ObjMRItems.getAttribute("UNIT_GRP_VOL_PRE").getString());
                rsHDetail.updateString("UNIT_GRP_VOL_CUR",(String)ObjMRItems.getAttribute("UNIT_GRP_VOL_CUR").getString());
                rsHDetail.updateString("UNIT_GRP_VOL_EXP",(String)ObjMRItems.getAttribute("UNIT_GRP_VOL_EXP").getString());
                rsHDetail.updateString("REMARKS",(String)ObjMRItems.getAttribute("REMARKS").getString());
                
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
            ObjFlow.ModuleID=clsFeltF.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("F6_ID").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName="PRODUCTION.FELT_F6_TRN_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="F6_ID";
            
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
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER_H WHERE F6_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_DETAIL_H WHERE F6_ID='1' ORDER BY INVOICE_NO");
            rsHDetail.first();
            
            //------------------------------------//
            
            String theDocNo=getAttribute("F6_ID").getString();
           
            //** --------------------------------**//
            
            
            // rsResultSet.updateString("F6_ID", getAttribute("F6_ID").getString());
            rsResultSet.updateString("F6_ID", getAttribute("F6_ID").getString());
            rsResultSet.updateString("F6_FROM_DATE",getAttribute("F6_FROM_DATE").getString());
            rsResultSet.updateString("F6_TO_DATE",getAttribute("F6_TO_DATE").getString());
            
            
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_F6_TRN_HEADER_H WHERE F6_ID='"+(String)getAttribute("F6_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("F6_ID").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATE_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATE_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHistory.updateString("F6_ID", getAttribute("F6_ID").getString());
            rsHistory.updateString("F6_FROM_DATE", getAttribute("F6_FROM_DATE").getString());
            rsHistory.updateString("F6_TO_DATE",getAttribute("F6_TO_DATE").getString());
            
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
            String mProformaNo=(String)getAttribute("F6_ID").getObj();
            
            data.Execute("DELETE FROM PRODUCTION.FELT_F6_TRN_DETAIL WHERE F6_ID='"+mProformaNo+"'");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_F6_TRN_DETAIL WHERE F6_ID='1'");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_DETAIL WHERE F6_ID='1' ORDER BY INVOICE_NO");
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsFeltFitem ObjMRItems=(clsFeltFitem) colMRItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateString("F6_ID",(String)getAttribute("F6_ID").getObj());
                rsTmp.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getObj());
                rsTmp.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB((String)ObjMRItems.getAttribute("INVOICE_DATE").getObj()));
                rsTmp.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getObj());
                rsTmp.updateString("INVOICE_AMT",(String)ObjMRItems.getAttribute("INVOICE_AMT").getObj());
                
                rsTmp.updateString("EXT1",(String)ObjMRItems.getAttribute("EXT1").getObj());
                rsTmp.updateString("EXT2",(String)ObjMRItems.getAttribute("EXT2").getObj());
                rsTmp.updateString("EXT3",(String)ObjMRItems.getAttribute("EXT3").getObj());
                rsTmp.updateString("EXT4",(String)ObjMRItems.getAttribute("EXT4").getObj());
                rsTmp.updateString("EXT5",(String)ObjMRItems.getAttribute("EXT5").getObj());
                rsTmp.updateString("EXT6",(String)ObjMRItems.getAttribute("EXT6").getObj());
                
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateString("F6_ID",(String)getAttribute("F6_ID").getObj());
                rsHDetail.updateString("INVOICE_NO",(String)ObjMRItems.getAttribute("INVOICE_NO").getObj());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB((String)ObjMRItems.getAttribute("INVOICE_DATE").getObj()));
                rsHDetail.updateString("PARTY_CODE",(String)ObjMRItems.getAttribute("PARTY_CODE").getObj());
                
                rsHDetail.updateString("PARTY_NAME",(String)ObjMRItems.getAttribute("PARTY_NAME").getObj());
                rsHDetail.updateString("INVOICE_AMT",(String)ObjMRItems.getAttribute("INVOICE_AMT").getObj());
                rsHDetail.updateString("EXT1",(String)ObjMRItems.getAttribute("EXT1").getObj());
                rsHDetail.updateString("EXT2",(String)ObjMRItems.getAttribute("EXT2").getObj());
                rsHDetail.updateString("EXT3",(String)ObjMRItems.getAttribute("EXT3").getObj());
                rsHDetail.updateString("EXT4",(String)ObjMRItems.getAttribute("EXT4").getObj());
                rsHDetail.updateString("EXT5",(String)ObjMRItems.getAttribute("EXT5").getObj());
                rsHDetail.updateString("EXT6",(String)ObjMRItems.getAttribute("EXT6").getObj());
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
            
            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsFeltF.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocNo=(String)getAttribute("F6_ID").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_F6_TRN_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="F6_ID";
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
                data.Execute("UPDATE PRODUCTION.FELT_F6_TRN_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE F6_ID='"+ObjFlow.DocNo+"' ");
                 
                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+clsFeltF.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            setAttribute("F6_ID",rsResultSet.getString("F6_ID"));
            setAttribute("F6_FROM_DATE",rsResultSet.getString("F6_FROM_DATE"));
            setAttribute("F6_TO_DATE",rsResultSet.getString("F6_TO_DATE"));
            
            
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
            String mdocno=(String) getAttribute("F6_ID").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_DETAIL_H WHERE F6_ID='"+mdocno+"' AND REVISION_NO="+RevNo+" ORDER BY INVOICE_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_DETAIL WHERE F6_ID='"+mdocno+"' ORDER BY INVOICE_NO");
            }
            
            /*if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER_H AS AA WHERE F6_ID='"+mdocno+"' AND REVISION_NO="+RevNo+" ORDER BY AA.F6_ID");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER AS AA WHERE F6_ID='"+mdocno+"' ORDER BY AA.F6_ID");
            }*/
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFeltFitem ObjMRItems = new clsFeltFitem();
          
            
                ObjMRItems.setAttribute("F6_ID",rsTmp.getString("F6_ID"));
                ObjMRItems.setAttribute("INVOICE_NO",rsTmp.getString("INVOICE_NO"));
                ObjMRItems.setAttribute("INVOICE_DATE",rsTmp.getString("INVOICE_DATE"));
                ObjMRItems.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjMRItems.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                ObjMRItems.setAttribute("INVOICE_AMT",rsTmp.getString("INVOICE_AMT"));
                
                ObjMRItems.setAttribute("EXT1",rsTmp.getString("EXT1"));
                ObjMRItems.setAttribute("EXT2",rsTmp.getString("EXT2"));
                ObjMRItems.setAttribute("EXT3",rsTmp.getString("EXT3"));
                ObjMRItems.setAttribute("EXT4",rsTmp.getString("EXT4"));
                ObjMRItems.setAttribute("EXT5",rsTmp.getString("EXT5"));
                ObjMRItems.setAttribute("EXT6",rsTmp.getString("EXT6"));
                
              /*  
                ObjMRItems.setAttribute("DD_CH_NO",rsTmp.getString("DD_CH_NO"));
                ObjMRItems.setAttribute("DD_CH_DATE",rsTmp.getString("DD_CH_DATE"));
                ObjMRItems.setAttribute("DD_CH_AMT",rsTmp.getString("DD_CH_AMT"));
                ObjMRItems.setAttribute("DD_CH_CLR_DATE",rsTmp.getString("DD_CH_CLR_DATE"));
                ObjMRItems.setAttribute("LATE_DAYS",rsTmp.getString("LATE_DAYS"));
                ObjMRItems.setAttribute("UNIT_GRP_VOL_PRE",rsTmp.getString("UNIT_GRP_VOL_PRE"));
                ObjMRItems.setAttribute("UNIT_GRP_VOL_CUR",rsTmp.getString("UNIT_GRP_VOL_CUR"));
                ObjMRItems.setAttribute("UNIT_GRP_VOL_EXP",rsTmp.getString("UNIT_GRP_VOL_EXP"));
                ObjMRItems.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
               */
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_F6_TRN_HEADER WHERE F6_ID='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=731 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            String strSQL="SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER_H WHERE F6_ID='"+Docno+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsFeltF objParty=new clsFeltF();
                    
                    objParty.setAttribute("F6_ID",rsTmp.getString("F6_ID"));
                    //objParty.setAttribute("PROFORMA_DATE",rsTmp.getString("PROFORMA_DATE"));
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
            strSQL+= "SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MM_PARTY_CODE=BB.PARTY_CODE WHERE " + Condition ;
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_F6_TRN_HEADER WHERE F6_ID='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=731 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
        /*try {
            int lCompanyID=EITLERPGLOBAL.gCompanyID;
            String lDocNo=(String)getAttribute("F6_ID").getObj();
            String strSQL="";
         
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_F6_TRN_HEADER WHERE F6_ID='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_F6_TRN_DETAIL WHERE F6_ID='"+lDocNo+"'";
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
        }*/
        return false;
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
    
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_F6_TRN_HEADER.F6_ID),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_F6_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_F6_TRN_HEADER.F6_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=731 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_F6_TRN_HEADER.F6_ID),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_F6_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_F6_TRN_HEADER.F6_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=731 ORDER BY PRODUCTION.FELT_F6_TRN_HEADER.CREATED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_F6_TRN_HEADER.F6_ID),MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_F6_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_F6_TRN_HEADER.F6_ID=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=731 ORDER BY PRODUCTION.FELT_F6_TRN_HEADER.F6_ID";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltF ObjDoc=new clsFeltF();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("F6_ID",rsTmp.getString("F6_ID"));
                ObjDoc.setAttribute("MM_PARTY_CODE",rsTmp.getString("MM_PARTY_CODE"));
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
                strSQL="SELECT PRODUCTION.FELT_F6_TRN_HEADER.F6_ID,PRODUCTION.FELT_F6_TRN_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_F6_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_F6_TRN_HEADER.F6_ID AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=731 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_F6_TRN_HEADER.F6_ID,PRODUCTION.FELT_F6_TRN_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_F6_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_F6_TRN_HEADER.F6_ID AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=731 ORDER BY PRODUCTION.FELT_F6_TRN_HEADER.CREATED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_F6_TRN_HEADER.F6_ID,PRODUCTION.FELT_F6_TRN_HEADER.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_F6_TRN_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_F6_TRN_HEADER.F6_ID AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=731 ORDER BY PRODUCTION.FELT_F6_TRN_HEADER.F6_ID";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsFeltF ObjItem = new clsFeltF();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("F6_ID",rsTmp.getString("F6_ID"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER_H WHERE F6_ID='"+pdocno+"' ORDER BY REVISION_NO");
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
    
    public static String[] getPiecedetail(String pPartyCode){
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String []Piecedetail = new String[40];
        String []error = {"error"};
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT * FROM PRODUCTION.FELT_F6_TRN_HEADER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MM_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MM_POSITION = B.POSITION_NO WHERE ";
            if(!pPartyCode.equals("")){
                strSQL+="AA.MM_PARTY_CODE='"+pPartyCode+"'";
            }
            System.out.println("Piece Detail Query :");
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow() > 0) {
                Piecedetail[0]=rsTmp.getString("MM_PARTY_CODE");
                Piecedetail[1]=rsTmp.getString("MM_MACHINE_NO");
                Piecedetail[2]=rsTmp.getString("MM_POSITION");
                Piecedetail[3]=rsTmp.getString("MM_POSITION_DESC");
                Piecedetail[4]=rsTmp.getString("MM_COMBINATION_CODE");
                Piecedetail[5]=rsTmp.getString("MM_ORDER_LENGTH");
                Piecedetail[6]=rsTmp.getString("MM_ORDER_WIDTH");
                Piecedetail[7]=rsTmp.getString("MM_ORDER_SIZE");
                Piecedetail[8]=rsTmp.getString("MM_PRESS_TYPE");
                Piecedetail[9]=rsTmp.getString("MM_GSM_RANGE");
                Piecedetail[10]=rsTmp.getString("MM_MAX_FELT_LENGTH");
                Piecedetail[11]=rsTmp.getString("MM_MIN_FELT_LENGTH");
                Piecedetail[12]=rsTmp.getString("MM_LINEAR_NIP_LOAD");
                Piecedetail[13]=rsTmp.getString("MM_PAPERGRADE_CODE");
                Piecedetail[14]=rsTmp.getString("MM_PAPERGRADE_DESC");
                Piecedetail[15]=rsTmp.getString("MM_FURNISH");
                Piecedetail[17]=rsTmp.getString("MM_TYPE");
                Piecedetail[18]=rsTmp.getString("MM_SPEED");
                Piecedetail[19]=rsTmp.getString("MM_SURVEY_DATE");
                Piecedetail[20]=rsTmp.getString("MM_WIRE_LENGTH");
                Piecedetail[21]=rsTmp.getString("MM_WIRE_WIDTH");
                Piecedetail[22]=rsTmp.getString("MM_WIRE_TYPE");
                Piecedetail[23]=rsTmp.getString("MM_TECH_REP");
                Piecedetail[24]=rsTmp.getString("MM_TYPE_OF_FILLER");
                Piecedetail[25]=rsTmp.getString("MM_PAPER_DECKLE");
                Piecedetail[26]=rsTmp.getString("MM_MCH_ACTIVE");
                Piecedetail[27]=rsTmp.getString("MM_LIFE_OF_FELT");
                Piecedetail[28]=rsTmp.getString("MM_CONSUMPTION");
                Piecedetail[29]=rsTmp.getString("MM_DINESH_SHARE");
                
                //Piecedetail[27]=rsTmp.getString("CREATED_BY");
                //Piecedetail[28]=EITLERPGLOBAL.formatDate(rsTmp.getString("CREATED_DATE"));
                //Piecedetail[28]=rsTmp.getString("MODIFIED_BY");
                //Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("MODIFIED_DATE"));
                Piecedetail[30]=rsTmp.getString("MM_PAPERGRADE");
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
    
   public static String getNextFreeNo(int pModuleID,boolean UpdateLastNo) {
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
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID=2 AND MODULE_ID=731 AND FIRSTFREE_NO=181";
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
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=2 AND MODULE_ID=731 AND FIRSTFREE_NO=181");
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
