    /*
     * clsmachinesurvey.java
     *
     * Created on March 1,2015
     */

package EITLERP.Production.FeltMachineSurvey;

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
public class clsmachinesurveyamend {
    
    public String LastError="";
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=725; //72
    public HashMap colMRItems;
    
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
    public clsmachinesurveyamend() {
        LastError = "";
        props=new HashMap();
        props.put("MP_AMD_PARTY_CODE",new Variant(""));
        props.put("MP_AMD_REASON",new Variant(""));
        props.put("MP_AMD_NO",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("DISPATCH_STATION",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("CREATED_BY",new Variant(0));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_AMD_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_AMD_POSITION = B.POSITION_NO ORDER BY AA.MP_AMD_NO");
            // WHERE PROFORMA_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROFORMA_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROFORMA_NO");
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
    
    public boolean LoadPartyData(long pCompanyID,String pPartyCode) {
        Ready=false;
        try {
            
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_POSITION = B.POSITION_NO WHERE AA.MP_PARTY_CODE='"+pPartyCode+"' ORDER BY AA.MP_PARTY_CODE");
            // WHERE PROFORMA_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROFORMA_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROFORMA_NO");
            HistoryView=false;
            Ready=true;
            MovePLast();
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
                
                String mpPartyCode,mnPartyCode;
                do {
                    mpPartyCode=(String) getAttribute("MP_AMD_PARTY_CODE").getObj();
                    rsResultSet.next();
                    
                    setData();
                    
                    
                    mnPartyCode=(String) getAttribute("MP_AMD_PARTY_CODE").getObj();
                    
                    if(rsResultSet.isAfterLast()) {
                        rsResultSet.last();
                        mnPartyCode="1";
                    }
                }while(mpPartyCode.equals(mnPartyCode));
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
                String mpPartyCode,mnPartyCode;
                do {
                    mpPartyCode=(String) getAttribute("MP_AMD_PARTY_CODE").getObj();
                    rsResultSet.previous();
                    
                    setData();
                    
                    
                    mnPartyCode=(String) getAttribute("MP_AMD_PARTY_CODE").getObj();
                    
                    if(rsResultSet.isBeforeFirst()) {
                        rsResultSet.first();
                        mnPartyCode="1";
                    }
                }while(mpPartyCode.equals(mnPartyCode));
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
     public boolean MovePLast() {
        try {
            rsResultSet.last();
            setPData();
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
            
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H AS AA WHERE AA.MP_AMD_PARTY_CODE='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H AS AA WHERE AA.MP_AMD_PARTY_CODE='1'");
            rsHDetail.first();
            //------------------------------------//
            
            
            
            
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_PARTY_CODE='1'");
            
            
            String tmpmachinests="";
            String msurvydt="";
            String mdttm="";
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                
                clsmachinesurveyitemamend ObjMRItems=(clsmachinesurveyitemamend) colMRItems.get(Integer.toString(i));
                tmpmachinests=(String)ObjMRItems.getAttribute("MP_AMD_MCH_ACTIVE").getObj();
                msurvydt=(String)ObjMRItems.getAttribute("MP_AMD_SURVEY_DATE").getObj();
                if(!EITLERPGLOBAL.isDate(msurvydt))
                {
                    LastError= "Survey Data is Not Valid Date...";                    
                    return false;
                }
                if(tmpmachinests.equalsIgnoreCase("ACTIVE") || tmpmachinests.equalsIgnoreCase("DEACTIVE"))
                {
                    
                }
                else
                {
                    LastError= "MACHINE STATUS MUST BE EITHER Active OR Deactive......";                    
                    return false;
                }
                String mconsumption=(String)ObjMRItems.getAttribute("MP_AMD_LIFE_OF_FELT").getObj();;
                
                
                double mcon=0.00;
                double mconsum=0.00;
                double mshrdin=0.00;
                int tcon=0;
                if(mconsumption.equals("")){
                    mcon=1;
                }else {
                    try{
                        
                        tcon=Integer.parseInt(mconsumption.substring(mconsumption.compareToIgnoreCase(".")));
                        
                        mcon=Double.parseDouble(mconsumption);
                        
                        if(tcon>0){
                            LastError="Life of Felt is in Days & Days is always in number only...";
                            return false;
                        }
                        
                    }catch(Exception e) {
                        
                    }
                }
                
                rsTmp.moveToInsertRow();
                
                
                rsTmp.updateString("MP_AMD_PARTY_CODE",(String)ObjMRItems.getAttribute("MP_AMD_PARTY_CODE").getObj());
                rsTmp.updateString("MP_AMD_MACHINE_NO",(String)ObjMRItems.getAttribute("MP_AMD_MACHINE_NO").getObj());
                rsTmp.updateString("MP_AMD_POSITION",(String)ObjMRItems.getAttribute("MP_AMD_POSITION").getObj());
                rsTmp.updateString("MP_AMD_POSITION_DESC",(String)ObjMRItems.getAttribute("MP_AMD_POSITION_DESC").getObj());
                rsTmp.updateString("MP_AMD_COMBINATION_CODE",(String)ObjMRItems.getAttribute("MP_AMD_COMBINATION_CODE").getObj());
                rsTmp.updateString("MP_AMD_ORDER_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_ORDER_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_WIDTH").getObj());
                rsTmp.updateString("MP_AMD_ORDER_SIZE",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_SIZE").getObj());
                rsTmp.updateString("MP_AMD_PRESS_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_PRESS_TYPE").getObj());
                rsTmp.updateString("MP_AMD_GSM_RANGE",(String)ObjMRItems.getAttribute("MP_AMD_GSM_RANGE").getObj());
                rsTmp.updateString("MP_AMD_MAX_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MAX_FELT_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_MIN_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MIN_FELT_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_LINEAR_NIP_LOAD",(String)ObjMRItems.getAttribute("MP_AMD_LINEAR_NIP_LOAD").getObj());
                rsTmp.updateString("MP_AMD_PAPERGRADE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE").getObj());
                rsTmp.updateString("MP_AMD_PAPERGRADE_CODE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_CODE").getObj());
                rsTmp.updateString("MP_AMD_PAPERGRADE_DESC",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_DESC").getObj());
                rsTmp.updateString("MP_AMD_FURNISH",(String)ObjMRItems.getAttribute("MP_AMD_FURNISH").getObj());
                rsTmp.updateString("MP_AMD_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_TYPE").getObj());
                rsTmp.updateString("MP_AMD_SPEED",(String)ObjMRItems.getAttribute("MP_AMD_SPEED").getObj());
                rsTmp.updateString("MP_AMD_SURVEY_DATE",EITLERPGLOBAL.formatDateDB(msurvydt));
                rsTmp.updateString("MP_AMD_WIRE_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_WIRE_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_WIDTH").getObj());
                rsTmp.updateString("MP_AMD_WIRE_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_TYPE").getObj());
                rsTmp.updateString("MP_AMD_TECH_REP",(String)ObjMRItems.getAttribute("MP_AMD_TECH_REP").getObj());
                rsTmp.updateString("MP_AMD_TYPE_OF_FILLER",(String)ObjMRItems.getAttribute("MP_AMD_TYPE_OF_FILLER").getObj());
                rsTmp.updateString("MP_AMD_PAPER_DECKLE",(String)ObjMRItems.getAttribute("MP_AMD_PAPER_DECKLE").getObj());
                rsTmp.updateString("MP_AMD_MCH_ACTIVE",(String)ObjMRItems.getAttribute("MP_AMD_MCH_ACTIVE").getObj());
                rsTmp.updateString("MP_AMD_NO", (String)ObjMRItems.getAttribute("MP_AMD_NO").getObj());
                rsTmp.updateString("MP_AMD_REASON", (String)ObjMRItems.getAttribute("MP_AMD_REASON").getObj());
                rsTmp.updateString("MP_AMD_LIFE_OF_FELT", (String)ObjMRItems.getAttribute("MP_AMD_LIFE_OF_FELT").getObj());
                rsTmp.updateString("MP_AMD_CONSUMPTION", (String)ObjMRItems.getAttribute("MP_AMD_CONSUMPTION").getObj());
                rsTmp.updateString("MP_AMD_DINESH_SHARE", (String)ObjMRItems.getAttribute("MP_AMD_DINESH_SHARE").getObj());
                rsTmp.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                
                //rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                //rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                //rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.insertRow();
                
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateString("MP_AMD_PARTY_CODE",(String)getAttribute("MP_AMD_PARTY_CODE").getObj());
                rsHDetail.updateString("MP_AMD_MACHINE_NO",(String)ObjMRItems.getAttribute("MP_AMD_MACHINE_NO").getObj());
                rsHDetail.updateString("MP_AMD_POSITION",(String)ObjMRItems.getAttribute("MP_AMD_POSITION").getObj());
                rsHDetail.updateString("MP_AMD_POSITION_DESC",(String)ObjMRItems.getAttribute("MP_AMD_POSITION_DESC").getObj());
                rsHDetail.updateString("MP_AMD_COMBINATION_CODE",(String)ObjMRItems.getAttribute("MP_AMD_COMBINATION_CODE").getObj());
                rsHDetail.updateString("MP_AMD_ORDER_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_ORDER_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_WIDTH").getObj());
                rsHDetail.updateString("MP_AMD_ORDER_SIZE",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_SIZE").getObj());
                rsHDetail.updateString("MP_AMD_PRESS_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_PRESS_TYPE").getObj());
                rsHDetail.updateString("MP_AMD_GSM_RANGE",(String)ObjMRItems.getAttribute("MP_AMD_GSM_RANGE").getObj());
                rsHDetail.updateString("MP_AMD_MAX_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MAX_FELT_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_MIN_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MIN_FELT_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_LINEAR_NIP_LOAD",(String)ObjMRItems.getAttribute("MP_AMD_LINEAR_NIP_LOAD").getObj());
                rsHDetail.updateString("MP_AMD_PAPERGRADE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE").getObj());
                rsHDetail.updateString("MP_AMD_PAPERGRADE_CODE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_CODE").getObj());
                rsHDetail.updateString("MP_AMD_PAPERGRADE_DESC",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_DESC").getObj());
                rsHDetail.updateString("MP_AMD_FURNISH",(String)ObjMRItems.getAttribute("MP_AMD_FURNISH").getObj());
                rsHDetail.updateString("MP_AMD_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_TYPE").getObj());
                rsHDetail.updateString("MP_AMD_SPEED",(String)ObjMRItems.getAttribute("MP_AMD_SPEED").getObj());
                rsHDetail.updateString("MP_AMD_SURVEY_DATE",EITLERPGLOBAL.formatDateDB(msurvydt));
                rsHDetail.updateString("MP_AMD_WIRE_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_WIRE_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_WIDTH").getObj());
                rsHDetail.updateString("MP_AMD_WIRE_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_TYPE").getObj());
                rsHDetail.updateString("MP_AMD_TECH_REP",(String)ObjMRItems.getAttribute("MP_AMD_TECH_REP").getObj());
                rsHDetail.updateString("MP_AMD_TYPE_OF_FILLER",(String)ObjMRItems.getAttribute("MP_AMD_TYPE_OF_FILLER").getObj());
                rsHDetail.updateString("MP_AMD_PAPER_DECKLE",(String)ObjMRItems.getAttribute("MP_AMD_PAPER_DECKLE").getObj());
                rsHDetail.updateString("MP_AMD_MCH_ACTIVE",(String)ObjMRItems.getAttribute("MP_AMD_MCH_ACTIVE").getObj());
                rsHDetail.updateString("MP_AMD_LIFE_OF_FELT",(String)ObjMRItems.getAttribute("MP_AMD_LIFE_OF_FELT").getObj());
                rsHDetail.updateString("MP_AMD_CONSUMPTION",(String)ObjMRItems.getAttribute("MP_AMD_CONSUMPTION").getObj());
                rsHDetail.updateString("MP_AMD_DINESH_SHARE",(String)ObjMRItems.getAttribute("MP_AMD_DINESH_SHARE").getObj());
                rsHDetail.updateString("UPDATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                mdttm=EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime();
                rsHDetail.updateString("ENTRY_DATE",mdttm);                
                rsHDetail.updateString("MP_AMD_NO",(String)ObjMRItems.getAttribute("MP_AMD_NO").getObj());
                rsHDetail.updateString("MP_AMD_REASON",(String)ObjMRItems.getAttribute("MP_AMD_REASON").getObj());
                rsHDetail.insertRow();
            }
            
            
            //===================== Update the Approval Flow ======================//
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
             
            ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsmachinesurveyamend.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("MP_AMD_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName="PRODUCTION.FELT_MACHINE_POSITION_AMEND";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="MP_AMD_NO";
             
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
             if(ObjFlow.Status.equals("F")){

                 data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MP_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_POSITION_MASTER SELECT MP_AMD_PARTY_CODE,MP_AMD_MACHINE_NO,MP_AMD_POSITION,MP_AMD_COMBINATION_CODE,MP_AMD_ORDER_SIZE,MP_AMD_ORDER_LENGTH,MP_AMD_ORDER_WIDTH,MP_AMD_TECH_REP,MP_AMD_TYPE,MP_AMD_SPEED,MP_AMD_WIRE_LENGTH,MP_AMD_WIRE_WIDTH,MP_AMD_WIRE_TYPE,MP_AMD_PAPERGRADE,MP_AMD_PAPERGRADE_CODE,MP_AMD_PAPERGRADE_DESC,MP_AMD_GSM_RANGE,MP_AMD_FURNISH,MP_AMD_TYPE_OF_FILLER,MP_AMD_PAPER_DECKLE,MP_AMD_PRESS_TYPE,MP_AMD_LINEAR_NIP_LOAD,MP_AMD_MIN_FELT_LENGTH,MP_AMD_MAX_FELT_LENGTH,MP_AMD_MCH_ACTIVE,MP_AMD_LIFE_OF_FELT,MP_AMD_CONSUMPTION,MP_AMD_DINESH_SHARE,MP_AMD_SURVEY_DATE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,MP_AMD_POSITION_DESC,MP_AMD_UPD_FLG FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER_H WHERE MP_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_POSITION_MASTER_H SELECT REVISION_NO,MP_AMD_PARTY_CODE,MP_AMD_MACHINE_NO,MP_AMD_POSITION,MP_AMD_COMBINATION_CODE,MP_AMD_ORDER_SIZE,MP_AMD_ORDER_LENGTH,MP_AMD_ORDER_WIDTH,MP_AMD_TECH_REP,MP_AMD_TYPE,MP_AMD_SPEED,MP_AMD_WIRE_LENGTH,MP_AMD_WIRE_WIDTH,MP_AMD_WIRE_TYPE,MP_AMD_PAPERGRADE_CODE,MP_AMD_PAPERGRADE_DESC,MP_AMD_GSM_RANGE,MP_AMD_FURNISH,MP_AMD_TYPE_OF_FILLER,MP_AMD_PAPER_DECKLE,MP_AMD_PRESS_TYPE,MP_AMD_LINEAR_NIP_LOAD,MP_AMD_MIN_FELT_LENGTH,MP_AMD_MAX_FELT_LENGTH,MP_AMD_MCH_ACTIVE,MP_AMD_LIFE_OF_FELT,MP_AMD_CONSUMPTION,MP_AMD_DINESH_SHARE,MP_AMD_SURVEY_DATE,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,HIERARCHY_ID,CHANGED,CHANGED_DATE,MP_AMD_PAPERGRADE,MP_AMD_POSITION_DESC,MP_AMD_UPD_FLG FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H WHERE MP_AMD_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 
             }
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
            
            //rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER_H AS AA WHERE AA.MP_PARTY_CODE='1'"); // '1' for restricting all data retrieval
            //rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H AS AA WHERE AA.MP_AMD_PARTY_CODE='1'");
            rsHDetail.first();
            //------------------------------------//
            
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H WHERE MP_AMD_PARTY_CODE='"+(String)getAttribute("MP_AMD_PARTY_CODE").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("MP_AMD_PARTY_CODE").getObj();
            
            String mPartyCode=(String)getAttribute("MP_AMD_PARTY_CODE").getObj();
            
            String tmpmachinests="";
            String msurvydt="";
            for(int i=1;i<=colMRItems.size();i++) {
                clsmachinesurveyitemamend ObjMRItems=(clsmachinesurveyitemamend) colMRItems.get(Integer.toString(i));
                
                msurvydt=(String)ObjMRItems.getAttribute("MP_AMD_SURVEY_DATE").getObj();
                if(!EITLERPGLOBAL.isDate(msurvydt))
                {
                    LastError= "Survey Data is Not Valid Date...";                    
                    return false;
                }
                tmpmachinests=(String)ObjMRItems.getAttribute("MP_AMD_MCH_ACTIVE").getObj();
                if(tmpmachinests.equalsIgnoreCase("ACTIVE") || tmpmachinests.equalsIgnoreCase("DEACTIVE"))
                {
                    
                }
                else
                {
                    LastError= "MACHINE STATUS SHOULD BE EITHER Active OR Deactive...";
                    return false;
                }
                
                String mconsumption=(String)ObjMRItems.getAttribute("MP_AMD_LIFE_OF_FELT").getObj();;
                
                
                double mcon=0.00;
                double mconsum=0.00;
                double mshrdin=0.00;
                int tcon=0;
                if(mconsumption.equals("")){
                    mcon=1;
                }else {
                    try{
                        
                        tcon=Integer.parseInt(mconsumption.substring(mconsumption.compareToIgnoreCase(".")));
                        
                        mcon=Double.parseDouble(mconsumption);
                        
                        if(tcon>0){
                            LastError="Life of Felt is in Days & Days is always in number only...";
                            return false;
                        }
                        
                    }catch(Exception e) {
                        
                    }
                }
            }
            
            
            data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_PARTY_CODE='"+mPartyCode+"'");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_PARTY_CODE='1'");
            
            String mdttm="";
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsmachinesurveyitemamend ObjMRItems=(clsmachinesurveyitemamend) colMRItems.get(Integer.toString(i));
                
               
                msurvydt=(String)ObjMRItems.getAttribute("MP_AMD_SURVEY_DATE").getObj();
                rsTmp.moveToInsertRow();
                
                
                
                
                rsTmp.updateString("MP_AMD_PARTY_CODE",(String)getAttribute("MP_AMD_PARTY_CODE").getObj());
                rsTmp.updateString("MP_AMD_MACHINE_NO",(String)ObjMRItems.getAttribute("MP_AMD_MACHINE_NO").getObj());
                rsTmp.updateString("MP_AMD_POSITION",(String)ObjMRItems.getAttribute("MP_AMD_POSITION").getObj());
                rsTmp.updateString("MP_AMD_POSITION_DESC",(String)ObjMRItems.getAttribute("MP_AMD_POSITION_DESC").getObj());
                rsTmp.updateString("MP_AMD_COMBINATION_CODE",(String)ObjMRItems.getAttribute("MP_AMD_COMBINATION_CODE").getObj());
                rsTmp.updateString("MP_AMD_ORDER_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_ORDER_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_WIDTH").getObj());
                rsTmp.updateString("MP_AMD_ORDER_SIZE",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_SIZE").getObj());
                rsTmp.updateString("MP_AMD_PRESS_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_PRESS_TYPE").getObj());
                rsTmp.updateString("MP_AMD_GSM_RANGE",(String)ObjMRItems.getAttribute("MP_AMD_GSM_RANGE").getObj());
                rsTmp.updateString("MP_AMD_MAX_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MAX_FELT_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_MIN_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MIN_FELT_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_LINEAR_NIP_LOAD",(String)ObjMRItems.getAttribute("MP_AMD_LINEAR_NIP_LOAD").getObj());
                rsTmp.updateString("MP_AMD_PAPERGRADE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE").getObj());
                rsTmp.updateString("MP_AMD_PAPERGRADE_CODE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_CODE").getObj());
                rsTmp.updateString("MP_AMD_PAPERGRADE_DESC",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_DESC").getObj());
                rsTmp.updateString("MP_AMD_FURNISH",(String)ObjMRItems.getAttribute("MP_AMD_FURNISH").getObj());
                rsTmp.updateString("MP_AMD_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_TYPE").getObj());
                rsTmp.updateString("MP_AMD_SPEED",(String)ObjMRItems.getAttribute("MP_AMD_SPEED").getObj());
                rsTmp.updateString("MP_AMD_SURVEY_DATE",EITLERPGLOBAL.formatDateDB(msurvydt));
                rsTmp.updateString("MP_AMD_WIRE_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_LENGTH").getObj());
                rsTmp.updateString("MP_AMD_WIRE_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_WIDTH").getObj());
                rsTmp.updateString("MP_AMD_WIRE_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_TYPE").getObj());
                rsTmp.updateString("MP_AMD_TECH_REP",(String)ObjMRItems.getAttribute("MP_AMD_TECH_REP").getObj());
                rsTmp.updateString("MP_AMD_TYPE_OF_FILLER",(String)ObjMRItems.getAttribute("MP_AMD_TYPE_OF_FILLER").getObj());
                rsTmp.updateString("MP_AMD_PAPER_DECKLE",(String)ObjMRItems.getAttribute("MP_AMD_PAPER_DECKLE").getObj());
                rsTmp.updateString("MP_AMD_MCH_ACTIVE",(String)ObjMRItems.getAttribute("MP_AMD_MCH_ACTIVE").getObj());
                rsTmp.updateString("MP_AMD_NO",(String)ObjMRItems.getAttribute("MP_AMD_NO").getObj());
                rsTmp.updateString("MP_AMD_REASON",(String)ObjMRItems.getAttribute("MP_AMD_REASON").getObj());
                rsTmp.updateString("MP_AMD_LIFE_OF_FELT",(String)ObjMRItems.getAttribute("MP_AMD_LIFE_OF_FELT").getObj());
                rsTmp.updateString("MP_AMD_CONSUMPTION",(String)ObjMRItems.getAttribute("MP_AMD_CONSUMPTION").getObj());
                rsTmp.updateString("MP_AMD_DINESH_SHARE",(String)ObjMRItems.getAttribute("MP_AMD_DINESH_SHARE").getObj());
                //rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                //rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                //rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateString("MP_AMD_PARTY_CODE",(String)getAttribute("MP_AMD_PARTY_CODE").getObj());
                rsHDetail.updateString("MP_AMD_MACHINE_NO",(String)ObjMRItems.getAttribute("MP_AMD_MACHINE_NO").getObj());
                rsHDetail.updateString("MP_AMD_POSITION",(String)ObjMRItems.getAttribute("MP_AMD_POSITION").getObj());
                rsHDetail.updateString("MP_AMD_POSITION_DESC",(String)ObjMRItems.getAttribute("MP_AMD_POSITION_DESC").getObj());
                rsHDetail.updateString("MP_AMD_COMBINATION_CODE",(String)ObjMRItems.getAttribute("MP_AMD_COMBINATION_CODE").getObj());
                rsHDetail.updateString("MP_AMD_ORDER_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_ORDER_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_WIDTH").getObj());
                rsHDetail.updateString("MP_AMD_ORDER_SIZE",(String)ObjMRItems.getAttribute("MP_AMD_ORDER_SIZE").getObj());
                rsHDetail.updateString("MP_AMD_PRESS_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_PRESS_TYPE").getObj());
                rsHDetail.updateString("MP_AMD_GSM_RANGE",(String)ObjMRItems.getAttribute("MP_AMD_GSM_RANGE").getObj());
                rsHDetail.updateString("MP_AMD_MAX_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MAX_FELT_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_MIN_FELT_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_MIN_FELT_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_LINEAR_NIP_LOAD",(String)ObjMRItems.getAttribute("MP_AMD_LINEAR_NIP_LOAD").getObj());
                rsHDetail.updateString("MP_AMD_PAPERGRADE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE").getObj());
                rsHDetail.updateString("MP_AMD_PAPERGRADE_CODE",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_CODE").getObj());
                rsHDetail.updateString("MP_AMD_PAPERGRADE_DESC",(String)ObjMRItems.getAttribute("MP_AMD_PAPERGRADE_DESC").getObj());
                rsHDetail.updateString("MP_AMD_FURNISH",(String)ObjMRItems.getAttribute("MP_AMD_FURNISH").getObj());
                rsHDetail.updateString("MP_AMD_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_TYPE").getObj());
                rsHDetail.updateString("MP_AMD_SPEED",(String)ObjMRItems.getAttribute("MP_AMD_SPEED").getObj());
                rsHDetail.updateString("MP_AMD_SURVEY_DATE",EITLERPGLOBAL.formatDateDB(msurvydt));
                rsHDetail.updateString("MP_AMD_WIRE_LENGTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_LENGTH").getObj());
                rsHDetail.updateString("MP_AMD_WIRE_WIDTH",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_WIDTH").getObj());
                rsHDetail.updateString("MP_AMD_WIRE_TYPE",(String)ObjMRItems.getAttribute("MP_AMD_WIRE_TYPE").getObj());
                rsHDetail.updateString("MP_AMD_TECH_REP",(String)ObjMRItems.getAttribute("MP_AMD_TECH_REP").getObj());
                rsHDetail.updateString("MP_AMD_TYPE_OF_FILLER",(String)ObjMRItems.getAttribute("MP_AMD_TYPE_OF_FILLER").getObj());
                rsHDetail.updateString("MP_AMD_PAPER_DECKLE",(String)ObjMRItems.getAttribute("MP_AMD_PAPER_DECKLE").getObj());
                rsHDetail.updateString("MP_AMD_MCH_ACTIVE",(String)ObjMRItems.getAttribute("MP_AMD_MCH_ACTIVE").getObj());
                rsHDetail.updateString("MP_AMD_LIFE_OF_FELT",(String)ObjMRItems.getAttribute("MP_AMD_LIFE_OF_FELT").getObj());
                rsHDetail.updateString("MP_AMD_CONSUMPTION",(String)ObjMRItems.getAttribute("MP_AMD_CONSUMPTION").getObj());
                rsHDetail.updateString("MP_AMD_DINESH_SHARE",(String)ObjMRItems.getAttribute("MP_AMD_DINESH_SHARE").getObj());
                rsHDetail.updateString("UPDATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                mdttm=EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime();
                rsHDetail.updateString("ENTRY_DATE",mdttm);
                rsHDetail.updateString("MP_AMD_NO",(String)ObjMRItems.getAttribute("MP_AMD_NO").getObj());
                rsHDetail.updateString("MP_AMD_REASON",(String)ObjMRItems.getAttribute("MP_AMD_REASON").getObj());
                /*
                rsHDetail.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                 **/
                rsHDetail.insertRow();
                RevNo++;
            }
            
            
            //======== Update the Approval Flow =========
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
             
            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsmachinesurveyamend.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocNo=(String)getAttribute("MP_AMD_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_MACHINE_POSITION_AMEND";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="MP_AMD_NO";
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
                data.Execute("UPDATE PRODUCTION.FELT_MACHINE_POSITION_AMEND SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE MP_AMD_PARTY_CODE='"+ObjFlow.DocNo+"' ");
             
                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+clsmachinesurveyamend.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
             
                ObjFlow.IsCreator=true;
            }
            //==========================================//
             
             ObjFlow.IsCreator=true;
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

                 data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MP_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_POSITION_MASTER SELECT MP_AMD_PARTY_CODE,MP_AMD_MACHINE_NO,MP_AMD_POSITION,MP_AMD_COMBINATION_CODE,MP_AMD_ORDER_SIZE,MP_AMD_ORDER_LENGTH,MP_AMD_ORDER_WIDTH,MP_AMD_TECH_REP,MP_AMD_TYPE,MP_AMD_SPEED,MP_AMD_WIRE_LENGTH,MP_AMD_WIRE_WIDTH,MP_AMD_WIRE_TYPE,MP_AMD_PAPERGRADE,MP_AMD_PAPERGRADE_CODE,MP_AMD_PAPERGRADE_DESC,MP_AMD_GSM_RANGE,MP_AMD_FURNISH,MP_AMD_TYPE_OF_FILLER,MP_AMD_PAPER_DECKLE,MP_AMD_PRESS_TYPE,MP_AMD_LINEAR_NIP_LOAD,MP_AMD_MIN_FELT_LENGTH,MP_AMD_MAX_FELT_LENGTH,MP_AMD_MCH_ACTIVE,MP_AMD_LIFE_OF_FELT,MP_AMD_CONSUMPTION,MP_AMD_DINESH_SHARE,MP_AMD_SURVEY_DATE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,HIERARCHY_ID,CHANGED,CHANGED_DATE,MP_AMD_POSITION_DESC,MP_AMD_UPD_FLG FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER_H WHERE MP_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 data.Execute("INSERT INTO PRODUCTION.FELT_MACHINE_POSITION_MASTER_H SELECT REVISION_NO,MP_AMD_PARTY_CODE,MP_AMD_MACHINE_NO,MP_AMD_POSITION,MP_AMD_COMBINATION_CODE,MP_AMD_ORDER_SIZE,MP_AMD_ORDER_LENGTH,MP_AMD_ORDER_WIDTH,MP_AMD_TECH_REP,MP_AMD_TYPE,MP_AMD_SPEED,MP_AMD_WIRE_LENGTH,MP_AMD_WIRE_WIDTH,MP_AMD_WIRE_TYPE,MP_AMD_PAPERGRADE_CODE,MP_AMD_PAPERGRADE_DESC,MP_AMD_GSM_RANGE,MP_AMD_FURNISH,MP_AMD_TYPE_OF_FILLER,MP_AMD_PAPER_DECKLE,MP_AMD_PRESS_TYPE,MP_AMD_LINEAR_NIP_LOAD,MP_AMD_MIN_FELT_LENGTH,MP_AMD_MAX_FELT_LENGTH,MP_AMD_MCH_ACTIVE,MP_AMD_LIFE_OF_FELT,MP_AMD_CONSUMPTION,MP_AMD_DINESH_SHARE,MP_AMD_SURVEY_DATE,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,HIERARCHY_ID,CHANGED,CHANGED_DATE,MP_AMD_PAPERGRADE,MP_AMD_POSITION_DESC,MP_AMD_UPD_FLG FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H WHERE MP_AMD_PARTY_CODE='"+ObjFlow.DocNo+"'");
                 
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
            
            setAttribute("MP_AMD_PARTY_CODE",rsResultSet.getString("MP_AMD_PARTY_CODE"));
            setAttribute("MP_AMD_NO",rsResultSet.getString("MP_AMD_NO"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("DISPATCH_STATION",rsResultSet.getString("DISPATCH_STATION"));
            
            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();
            String mPartyCode=(String) getAttribute("MP_AMD_PARTY_CODE").getObj();
            String mamdno=(String) getAttribute("MP_AMD_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_AMD_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_AMD_POSITION = B.POSITION_NO WHERE MP_AMD_NO='"+mamdno+"' AND REVISION_NO="+RevNo+" ORDER BY AA.MP_AMD_PARTY_CODE");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_AMD_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_AMD_POSITION = B.POSITION_NO WHERE MP_AMD_NO='"+mamdno+"' ORDER BY AA.MP_AMD_PARTY_CODE");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsmachinesurveyitemamend ObjMRItems = new clsmachinesurveyitemamend();
                ObjMRItems.setAttribute("MP_AMD_PARTY_CODE",rsTmp.getString("MP_AMD_PARTY_CODE"));
                ObjMRItems.setAttribute("SR_NO",Counter);
                ObjMRItems.setAttribute("MP_AMD_MACHINE_NO",rsTmp.getString("MP_AMD_MACHINE_NO"));
                ObjMRItems.setAttribute("MP_AMD_POSITION",rsTmp.getString("MP_AMD_POSITION"));
                ObjMRItems.setAttribute("MP_AMD_POSITION_DESC",rsTmp.getString("MP_AMD_POSITION_DESC"));
                ObjMRItems.setAttribute("MP_AMD_COMBINATION_CODE",rsTmp.getString("MP_AMD_COMBINATION_CODE"));
                ObjMRItems.setAttribute("MP_AMD_ORDER_LENGTH",rsTmp.getString("MP_AMD_ORDER_LENGTH"));
                ObjMRItems.setAttribute("MP_AMD_ORDER_WIDTH",rsTmp.getString("MP_AMD_ORDER_WIDTH"));
                ObjMRItems.setAttribute("MP_AMD_ORDER_SIZE",rsTmp.getString("MP_AMD_ORDER_SIZE"));
                ObjMRItems.setAttribute("MP_AMD_PRESS_TYPE",rsTmp.getString("MP_AMD_PRESS_TYPE"));
                ObjMRItems.setAttribute("MP_AMD_GSM_RANGE",rsTmp.getString("MP_AMD_GSM_RANGE"));
                ObjMRItems.setAttribute("MP_AMD_MAX_FELT_LENGTH",rsTmp.getString("MP_AMD_MAX_FELT_LENGTH"));
                ObjMRItems.setAttribute("MP_AMD_MIN_FELT_LENGTH",rsTmp.getString("MP_AMD_MIN_FELT_LENGTH"));
                ObjMRItems.setAttribute("MP_AMD_LINEAR_NIP_LOAD",rsTmp.getString("MP_AMD_LINEAR_NIP_LOAD"));
                ObjMRItems.setAttribute("MP_AMD_PAPERGRADE",rsTmp.getString("MP_AMD_PAPERGRADE"));
                ObjMRItems.setAttribute("MP_AMD_PAPERGRADE_CODE",rsTmp.getString("MP_AMD_PAPERGRADE_CODE"));
                ObjMRItems.setAttribute("MP_AMD_PAPERGRADE_DESC",rsTmp.getString("MP_AMD_PAPERGRADE_DESC"));
                ObjMRItems.setAttribute("MP_AMD_FURNISH",rsTmp.getString("MP_AMD_FURNISH"));
                ObjMRItems.setAttribute("MP_AMD_TYPE",rsTmp.getString("MP_AMD_TYPE"));
                ObjMRItems.setAttribute("MP_AMD_SPEED",rsTmp.getString("MP_AMD_SPEED"));
                ObjMRItems.setAttribute("MP_AMD_SURVEY_DATE",rsTmp.getString("MP_AMD_SURVEY_DATE"));
                ObjMRItems.setAttribute("MP_AMD_WIRE_LENGTH",rsTmp.getString("MP_AMD_WIRE_LENGTH"));
                ObjMRItems.setAttribute("MP_AMD_WIRE_WIDTH",rsTmp.getString("MP_AMD_WIRE_WIDTH"));
                ObjMRItems.setAttribute("MP_AMD_WIRE_TYPE",rsTmp.getString("MP_AMD_WIRE_TYPE"));
                ObjMRItems.setAttribute("MP_AMD_TECH_REP",rsTmp.getString("MP_AMD_TECH_REP"));
                ObjMRItems.setAttribute("MP_AMD_TYPE_OF_FILLER",rsTmp.getString("MP_AMD_TYPE_OF_FILLER"));
                ObjMRItems.setAttribute("MP_AMD_PAPER_DECKLE",rsTmp.getString("MP_AMD_PAPER_DECKLE"));
                ObjMRItems.setAttribute("MP_AMD_MCH_ACTIVE",rsTmp.getString("MP_AMD_MCH_ACTIVE"));
                ObjMRItems.setAttribute("MP_AMD_NO",rsTmp.getString("MP_AMD_NO"));
                ObjMRItems.setAttribute("MP_AMD_REASON",rsTmp.getString("MP_AMD_REASON"));
                ObjMRItems.setAttribute("MP_AMD_LIFE_OF_FELT",rsTmp.getString("MP_AMD_LIFE_OF_FELT"));
                ObjMRItems.setAttribute("MP_AMD_CONSUMPTION",rsTmp.getString("MP_AMD_CONSUMPTION"));
                ObjMRItems.setAttribute("MP_AMD_DINESH_SHARE",rsTmp.getString("MP_AMD_DINESH_SHARE"));
                //ObjMRItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                //ObjMRItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                //ObjMRItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                //ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
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
    
    
    public boolean setPData() {
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
            
            setAttribute("MP_PARTY_CODE",rsResultSet.getString("MP_PARTY_CODE"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("DISPATCH_STATION",rsResultSet.getString("DISPATCH_STATION"));
            
            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();
            String mPartyCode=(String) getAttribute("MP_PARTY_CODE").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER_H AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_POSITION = B.POSITION_NO WHERE MP_PARTY_CODE='"+mPartyCode+"' AND REVISION_NO="+RevNo+" ORDER BY AA.MP_PARTY_CODE");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_POSITION = B.POSITION_NO WHERE MP_PARTY_CODE='"+mPartyCode+"' ORDER BY AA.MP_PARTY_CODE");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsmachinesurveyitemamend ObjMRItems = new clsmachinesurveyitemamend();
                ObjMRItems.setAttribute("MP_PARTY_CODE",rsTmp.getString("MP_PARTY_CODE"));
                ObjMRItems.setAttribute("SR_NO",Counter);
                ObjMRItems.setAttribute("MP_MACHINE_NO",rsTmp.getString("MP_MACHINE_NO"));
                ObjMRItems.setAttribute("MP_POSITION",rsTmp.getString("MP_POSITION"));
                ObjMRItems.setAttribute("MP_POSITION_DESC",rsTmp.getString("MP_POSITION_DESC"));
                ObjMRItems.setAttribute("MP_COMBINATION_CODE",rsTmp.getString("MP_COMBINATION_CODE"));
                ObjMRItems.setAttribute("MP_ORDER_LENGTH",rsTmp.getString("MP_ORDER_LENGTH"));
                ObjMRItems.setAttribute("MP_ORDER_WIDTH",rsTmp.getString("MP_ORDER_WIDTH"));
                ObjMRItems.setAttribute("MP_ORDER_SIZE",rsTmp.getString("MP_ORDER_SIZE"));
                ObjMRItems.setAttribute("MP_PRESS_TYPE",rsTmp.getString("MP_PRESS_TYPE"));
                ObjMRItems.setAttribute("MP_GSM_RANGE",rsTmp.getString("MP_GSM_RANGE"));
                ObjMRItems.setAttribute("MP_MAX_FELT_LENGTH",rsTmp.getString("MP_MAX_FELT_LENGTH"));
                ObjMRItems.setAttribute("MP_MIN_FELT_LENGTH",rsTmp.getString("MP_MIN_FELT_LENGTH"));
                ObjMRItems.setAttribute("MP_LINEAR_NIP_LOAD",rsTmp.getString("MP_LINEAR_NIP_LOAD"));
                ObjMRItems.setAttribute("MP_PAPERGRADE",rsTmp.getString("MP_PAPERGRADE"));
                ObjMRItems.setAttribute("MP_PAPERGRADE_CODE",rsTmp.getString("MP_PAPERGRADE_CODE"));
                ObjMRItems.setAttribute("MP_PAPERGRADE_DESC",rsTmp.getString("MP_PAPERGRADE_DESC"));
                ObjMRItems.setAttribute("MP_FURNISH",rsTmp.getString("MP_FURNISH"));
                ObjMRItems.setAttribute("MP_TYPE",rsTmp.getString("MP_TYPE"));
                ObjMRItems.setAttribute("MP_SPEED",rsTmp.getString("MP_SPEED"));
                ObjMRItems.setAttribute("MP_SURVEY_DATE",rsTmp.getString("MP_SURVEY_DATE"));
                ObjMRItems.setAttribute("MP_WIRE_LENGTH",rsTmp.getString("MP_WIRE_LENGTH"));
                ObjMRItems.setAttribute("MP_WIRE_WIDTH",rsTmp.getString("MP_WIRE_WIDTH"));
                ObjMRItems.setAttribute("MP_WIRE_TYPE",rsTmp.getString("MP_WIRE_TYPE"));
                ObjMRItems.setAttribute("MP_TECH_REP",rsTmp.getString("MP_TECH_REP"));
                ObjMRItems.setAttribute("MP_TYPE_OF_FILLER",rsTmp.getString("MP_TYPE_OF_FILLER"));
                ObjMRItems.setAttribute("MP_PAPER_DECKLE",rsTmp.getString("MP_PAPER_DECKLE"));
                ObjMRItems.setAttribute("MP_MCH_ACTIVE",rsTmp.getString("MP_MCH_ACTIVE"));
                ObjMRItems.setAttribute("MP_LIFE_OF_FELT",rsTmp.getString("MP_LIFE_OF_FELT"));
                ObjMRItems.setAttribute("MP_CONSUMPTION",rsTmp.getString("MP_CONSUMPTION"));
                ObjMRItems.setAttribute("MP_DINESH_SHARE",rsTmp.getString("MP_DINESH_SHARE"));
                //ObjMRItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                //ObjMRItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                //ObjMRItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                //ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=725 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    public static HashMap getHistoryList(int CompanyID,String ProformaNo) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H WHERE MP_AMD_PARTY_CODE='"+ProformaNo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsmachinesurveyamend objParty=new clsmachinesurveyamend();
                    
                    objParty.setAttribute("MP_AMD_PARTY_CODE",rsTmp.getString("MP_AMD_PARTY_CODE"));
                    //objParty.setAttribute("PROFORMA_DATE",rsTmp.getString("PROFORMA_DATE"));
                    objParty.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                    objParty.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
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
            strSQL+= "SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_AMD_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_AMD_POSITION = B.POSITION_NO WHERE " + Condition ;
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND WHERE MP_AMD_PARTY_CODE='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=725 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            String lDocNo=(String)getAttribute("PROFORMA_NO").getObj();
            String strSQL="";
         
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='"+lDocNo+"'";
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
        String strCondition = " WHERE MP_AMD_PARTY_CODE='" + PartyCode + "' ";
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
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO),PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO),PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_PARTY_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT(PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO),PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_PARTY_CODE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_MASTER_AMEND_HEADER.MM_DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsmachinesurveyamend ObjDoc=new clsmachinesurveyamend();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("MM_DOC_NO"));
                ObjDoc.setAttribute("PRODUCT_CODE",rsTmp.getString("MM_PARTY_CODE"));
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
                strSQL="SELECT PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_PARTY_CODE,PRODUCTION.FELT_MACHINE_POSITION_AMEND.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_PARTY_CODE,PRODUCTION.FELT_MACHINE_POSITION_AMEND.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_PARTY_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_PARTY_CODE,PRODUCTION.FELT_MACHINE_POSITION_AMEND.CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=725 ORDER BY PRODUCTION.FELT_MACHINE_POSITION_AMEND.MP_AMD_PARTY_CODE";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsmachinesurveyamend ObjItem = new clsmachinesurveyamend();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("MP_AMD_PARTY_CODE",rsTmp.getString("MP_AMD_PARTY_CODE"));
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
    
    public boolean ShowHistory(int pCompanyID,String pProformaNo) {
        Ready=false;
        try {
            //Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND_H WHERE MP_AMD_PARTY_CODE='"+pProformaNo+"' ORDER BY REVISION_NO");
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
            String strSQL="SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND AS AA LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS BB ON AA.MP_AMD_PARTY_CODE=BB.PARTY_CODE LEFT JOIN (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS B ON AA.MP_AMD_POSITION = B.POSITION_NO WHERE ";
            if(!pPartyCode.equals("")){
                strSQL+="AA.MP_AMD_PARTY_CODE='"+pPartyCode+"'";
            }
            System.out.println("Piece Detail Query :");
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow() > 0) {
                Piecedetail[0]=rsTmp.getString("MP_AMD_PARTY_CODE");
                Piecedetail[1]=rsTmp.getString("MP_AMD_MACHINE_NO");
                Piecedetail[2]=rsTmp.getString("MP_AMD_POSITION");
                Piecedetail[3]=rsTmp.getString("MP_AMD_POSITION_DESC");
                Piecedetail[4]=rsTmp.getString("MP_AMD_COMBINATION_CODE");
                Piecedetail[5]=rsTmp.getString("MP_AMD_ORDER_LENGTH");
                Piecedetail[6]=rsTmp.getString("MP_AMD_ORDER_WIDTH");
                Piecedetail[7]=rsTmp.getString("MP_AMD_ORDER_SIZE");
                Piecedetail[8]=rsTmp.getString("MP_AMD_PRESS_TYPE");
                Piecedetail[9]=rsTmp.getString("MP_AMD_GSM_RANGE");
                Piecedetail[10]=rsTmp.getString("MP_AMD_MAX_FELT_LENGTH");
                Piecedetail[11]=rsTmp.getString("MP_AMD_MIN_FELT_LENGTH");
                Piecedetail[12]=rsTmp.getString("MP_AMD_LINEAR_NIP_LOAD");
                Piecedetail[13]=rsTmp.getString("MP_AMD_PAPERGRADE_CODE");
                Piecedetail[14]=rsTmp.getString("MP_AMD_PAPERGRADE_DESC");
                Piecedetail[15]=rsTmp.getString("MP_AMD_FURNISH");
                Piecedetail[17]=rsTmp.getString("MP_AMD_TYPE");
                Piecedetail[18]=rsTmp.getString("MP_AMD_SPEED");
                Piecedetail[19]=rsTmp.getString("MP_AMD_SURVEY_DATE");
                Piecedetail[20]=rsTmp.getString("MP_AMD_WIRE_LENGTH");
                Piecedetail[21]=rsTmp.getString("MP_AMD_WIRE_WIDTH");
                Piecedetail[22]=rsTmp.getString("MP_AMD_WIRE_TYPE");
                Piecedetail[23]=rsTmp.getString("MP_AMD_TECH_REP");
                Piecedetail[24]=rsTmp.getString("MP_AMD_TYPE_OF_FILLER");
                Piecedetail[25]=rsTmp.getString("MP_AMD_PAPER_DECKLE");
                Piecedetail[26]=rsTmp.getString("MP_AMD_MCH_ACTIVE");
                Piecedetail[27]=rsTmp.getString("MP_AMD_NO");
                Piecedetail[28]=rsTmp.getString("MP_AMD_REASON");
                Piecedetail[30]=rsTmp.getString("MP_AMD_LIFE_OF_FELT");
                Piecedetail[31]=rsTmp.getString("MP_AMD_CONSUMPTION");
                Piecedetail[32]=rsTmp.getString("MP_AMD_DINESH_SHARE");
                
                //Piecedetail[27]=rsTmp.getString("CREATED_BY");
                //Piecedetail[28]=EITLERPGLOBAL.formatDate(rsTmp.getString("CREATED_DATE"));
                //Piecedetail[28]=rsTmp.getString("MODIFIED_BY");
                //Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("MODIFIED_DATE"));
                Piecedetail[30]=rsTmp.getString("MP_AMD_PAPERGRADE");
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
    
   /*
    public static boolean getBackup(String PartyCode) {
        Connection tmpConn = null;
        Statement tmpStmt = null;
        ResultSet rsTmp = null;
        int SrNo = 0;
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_BKUP ORDER BY PARTY_CODE");
            clsSales_Party tmpParty = (clsSales_Party)clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PartyCode,"210027");
            SrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_PARTY_MASTER_BKUP")+1;
            rsTmp.first();
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("SR_NO", SrNo);
            rsTmp.updateInt("COMPANY_ID", tmpParty.getAttribute("COMAPNY_ID").getInt());
            rsTmp.updateString("PARTY_CODE", tmpParty.getAttribute("PARTY_CODE").getString());
            rsTmp.updateInt("PARTY_TYPE", tmpParty.getAttribute("PARTY_TYPE").getInt());
            rsTmp.updateString("PARENT_PARTY_CODE", " ");
            rsTmp.updateString("PARTY_NAME",tmpParty.getAttribute("PARTY_NAME").getString());
    
            rsTmp.updateString("SEASON_CODE",tmpParty.getAttribute("SEASON_CODE").getString());
            rsTmp.updateString("REG_DATE",tmpParty.getAttribute("REG_DATE").getString());
    
            rsTmp.updateString("AREA_ID",tmpParty.getAttribute("AREA_ID").getString());
            rsTmp.updateString("ADDRESS1",tmpParty.getAttribute("ADDRESS1").getString());
            rsTmp.updateString("ADDRESS2",tmpParty.getAttribute("ADDRESS2").getString());
            rsTmp.updateString("CITY_ID",tmpParty.getAttribute("CITY_ID").getString());
            rsTmp.updateString("CITY_NAME",tmpParty.getAttribute("CITY_NAME").getString());
            rsTmp.updateString("DISPATCH_STATION",tmpParty.getAttribute("DISPATCH_STATION").getString());
            rsTmp.updateString("DISTRICT",tmpParty.getAttribute("DISTRICT").getString());
            rsTmp.updateString("PINCODE",tmpParty.getAttribute("PINCODE").getString());
            rsTmp.updateString("PHONE_NO",tmpParty.getAttribute("PHONE_NO").getString());
            rsTmp.updateString("MOBILE_NO",tmpParty.getAttribute("MOBILE_NO").getString());
            rsTmp.updateString("EMAIL",tmpParty.getAttribute("EMAIL").getString());
            rsTmp.updateString("URL",tmpParty.getAttribute("URL").getString());
            rsTmp.updateString("CONTACT_PERSON",tmpParty.getAttribute("CONTACT_PERSON").getString());
            rsTmp.updateInt("BANK_ID",tmpParty.getAttribute("BANK_ID").getInt());
            rsTmp.updateString("BANK_NAME",tmpParty.getAttribute("BANK_NAME").getString());
            rsTmp.updateString("BANK_ADDRESS",tmpParty.getAttribute("BANK_ADDRESS").getString());
            rsTmp.updateString("BANK_CITY",tmpParty.getAttribute("BANK_CITY").getString());
            rsTmp.updateString("CHARGE_CODE",tmpParty.getAttribute("CHARGE_CODE").getString());
            rsTmp.updateString("CHARGE_CODE_II",tmpParty.getAttribute("CHARGE_CODE_II").getString());
            rsTmp.updateDouble("CREDIT_DAYS",tmpParty.getAttribute("CREDIT_DAYS").getDouble());
            rsTmp.updateString("DOCUMENT_THROUGH",tmpParty.getAttribute("DOCUMENT_THROUGH").getString());
            rsTmp.updateInt("TRANSPORTER_ID",tmpParty.getAttribute("TRANSPORTER_ID").getInt());
            rsTmp.updateString("TRANSPORTER_NAME",tmpParty.getAttribute("TRANSPORTER_NAME").getString());
            rsTmp.updateDouble("AMOUNT_LIMIT",tmpParty.getAttribute("AMOUNT_LIMIT").getDouble());
            rsTmp.updateString("CST_NO",tmpParty.getAttribute("CST_NO").getString());
            rsTmp.updateString("CST_DATE",tmpParty.getAttribute("CST_DATE").getString());
            rsTmp.updateString("ECC_NO",tmpParty.getAttribute("ECC_NO").getString());
            rsTmp.updateString("ECC_DATE",tmpParty.getAttribute("ECC_DATE").getString());
            rsTmp.updateString("TIN_NO",tmpParty.getAttribute("TIN_NO").getString());
            rsTmp.updateString("TIN_DATE",tmpParty.getAttribute("TIN_DATE").getString());
            rsTmp.updateString("PAN_NO",tmpParty.getAttribute("PAN_NO").getString());
            rsTmp.updateString("PAN_DATE",tmpParty.getAttribute("PAN_DATE").getString());
            rsTmp.updateString("MAIN_ACCOUNT_CODE",tmpParty.getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsTmp.updateString("CATEGORY",tmpParty.getAttribute("CATEGORY").getString());
            rsTmp.updateInt("OTHER_BANK_ID",tmpParty.getAttribute("OTHER_BANK_ID").getInt());
            rsTmp.updateString("OTHER_BANK_NAME",tmpParty.getAttribute("OTHER_BANK_NAME").getString());
            rsTmp.updateString("OTHER_BANK_ADDRESS",tmpParty.getAttribute("OTHER_BANK_ADDRESS").getString());
            rsTmp.updateString("OTHER_BANK_CITY",tmpParty.getAttribute("OTHER_BANK_CITY").getString());
            rsTmp.updateString("INSURANCE_CODE",tmpParty.getAttribute("INSURANCE_CODE").getString());
            rsTmp.updateString("REMARKS",tmpParty.getAttribute("REMARKS").getString());
            rsTmp.updateDouble("DELAY_INTEREST_PER",tmpParty.getAttribute("DELAY_INTEREST_PER").getDouble());
            rsTmp.updateString("ACCOUNT_CODES",tmpParty.getAttribute("ACCOUNT_CODES").getString());
    
            rsTmp.updateString("CREATED_BY",tmpParty.getAttribute("CREATED_BY").getString());
            rsTmp.updateString("CREATED_DATE",tmpParty.getAttribute("CREATED_DATE").getString());
            rsTmp.updateString("MODIFIED_BY",tmpParty.getAttribute("MODIFIED_DATE").getString());
            rsTmp.updateString("MODIFIED_DATE",tmpParty.getAttribute("MODIFIED_DATE").getString());
            rsTmp.updateInt("HIERARCHY_ID",tmpParty.getAttribute("HIERARCHY_ID").getInt());
            rsTmp.updateBoolean("APPROVED",tmpParty.getAttribute("APPROVED").getBool());
            rsTmp.updateString("APPROVED_DATE",tmpParty.getAttribute("APPROVED_DATE").getString());
            rsTmp.updateBoolean("CANCELLED",tmpParty.getAttribute("CANCELLED").getBool());
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateBoolean("REJECTED",false);
            rsTmp.updateString("REJECTED_DATE","0000-00-00");
            rsTmp.updateString("REJECTED_REMARKS",tmpParty.getAttribute("REJECTED_REMARKS").getString());
            rsTmp.insertRow();
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    */
    
    
}
