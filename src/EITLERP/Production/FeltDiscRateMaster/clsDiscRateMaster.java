    /*
     * clsSales_Party.java
     *
     * Created on April 3, 2009, 10:22 AM
     */

package EITLERP.Production.FeltDiscRateMaster;

import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import static EITLERP.FeltSales.OrderDiversion.clsFeltOrderDiversion.CanCancel;
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
public class clsDiscRateMaster {
    
    public String LastError="";
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=730; //72
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
    public clsDiscRateMaster() {
        LastError = "";
        props=new HashMap();
        
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("GROUP_CODE",new Variant(""));
        props.put("GROUP_NAME",new Variant(""));
        props.put("MASTER_NO", new Variant(""));
        props.put("TURN_OVER_TARGET",new Variant(""));
        props.put("SANCTION_DATE",new Variant(""));
        props.put("EFFECTIVE_FROM",new Variant(""));
        props.put("EFFECTIVE_TO",new Variant(""));
        
        props.put("REMARK1",new Variant(0));
        props.put("REMARK2",new Variant(0));
        props.put("REMARK3",new Variant(0));
        props.put("REMARK4",new Variant(0));
        props.put("REMARK5",new Variant(0));
        
        //props.put("DIVERSION_FLAG",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
        //Create a new object for MR Item collection
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
            //rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC");
            //rsResultSet1=Stmt.executeQuery("SELECT A.*,PRIORITY_DESC FROM (SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC) AS A LEFT JOIN (SELECT PRIORITY_DESC,PRIORITY_ID FROM PRODUCTION.FELT_PRIORITY_MASTER ) AS B ON A.PRIORITY=B.PRIORITY_ID");
            //rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROFORMA_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROFORMA_NO");
            //rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE EFFECTIVE_FROM>='"+EITLERPGLOBAL.FinFromDateDB+"' AND EFFECTIVE_FROM<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY MASTER_NO");
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER ORDER BY CREATED_DATE,MASTER_NO");
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
            //            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            //            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            //            java.sql.Date EffectiveDate=java.sql.Date.valueOf((String)getAttribute("EFFECTIVE_FROM").getObj());
            //
            //            if((EffectiveDate.after(FinFromDate)||EffectiveDate.compareTo(FinFromDate)==0)&&(EffectiveDate.before(FinToDate)||EffectiveDate.compareTo(FinToDate)==0)) {
            //                //Within the year
            //            }
            //            else {
            //                LastError="Requisition date is not within financial year.";
            //                return false;
            //            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER_H WHERE MASTER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL_H WHERE MASTER_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //--------- Generate Gatepass requisition no.  ------------
            
            String mst = getAttribute("MASTER_NO").getString().substring(0,2);
            if(mst.matches("FD")){
                setAttribute("MASTER_NO",getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,730,180,true));
            }
            if(mst.matches("PC")){
                setAttribute("MASTER_NO",getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,730,191,true));
            }
            if(mst.matches("GD")){
                setAttribute("MASTER_NO",getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,730,232,true));
            }
            
            //setAttribute("MASTER_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,730, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            
            //rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("MASTER_NO", getAttribute("MASTER_NO").getString());
            rsResultSet.updateString("SANCTION_DATE", getAttribute("SANCTION_DATE").getString());
            rsResultSet.updateString("EFFECTIVE_FROM", getAttribute("EFFECTIVE_FROM").getString());
            if(getAttribute("EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                rsResultSet.updateString("EFFECTIVE_TO",null);
            else
                rsResultSet.updateString("EFFECTIVE_TO", getAttribute("EFFECTIVE_TO").getString());
            
//            if (mst.startsWith("GD")) {
                rsResultSet.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
                rsResultSet.updateString("GROUP_NAME",getAttribute("GROUP_NAME").getString());            
//            } else {
                rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
                rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
//            }
            //rsResultSet.updateString("TURN_OVER_TARGET",getAttribute("TURN_OVER_TARGET").getString());
            
            rsResultSet.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsResultSet.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsResultSet.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsResultSet.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsResultSet.updateString("REMARK5",getAttribute("REMARK5").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","");
            rsResultSet.updateBoolean("CANCELED",false);
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
            
            rsHistory.updateString("MASTER_NO", getAttribute("MASTER_NO").getString());
            rsHistory.updateString("SANCTION_DATE", getAttribute("SANCTION_DATE").getString());
            rsHistory.updateString("EFFECTIVE_FROM", getAttribute("EFFECTIVE_FROM").getString());
            if(getAttribute("EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                rsHistory.updateString("EFFECTIVE_TO",null);
            else
                rsHistory.updateString("EFFECTIVE_TO", getAttribute("EFFECTIVE_TO").getString());
            
//            if (mst.startsWith("GD")) {
                rsHistory.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
                rsHistory.updateString("GROUP_NAME",getAttribute("GROUP_NAME").getString());            
//            } else {
                rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
                rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
//            }
//            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
//            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            //rsHistory.updateString("TURN_OVER_TARGET",getAttribute("TURN_OVER_TARGET").getString());
            
            rsHistory.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsHistory.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsHistory.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsHistory.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsHistory.updateString("REMARK5",getAttribute("REMARK5").getString());
            
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
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            System.out.println(1);
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='1'");
            
            //long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsDiscRateMasterItem ObjMRItems=(clsDiscRateMasterItem) colMRItems.get(Integer.toString(i));
                
                //ObjMRItems.setAttribute("BAL_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                rsTmp.moveToInsertRow();
                
                //rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("MASTER_NO",(String)getAttribute("MASTER_NO").getObj());
                rsTmp.updateInt("SR_NO",i);
//                if (mst.startsWith("GD")) {
                    rsTmp.updateString("GROUP_CODE",(String)getAttribute("GROUP_CODE").getObj());       
//                } else {
                    rsTmp.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
//                }
//                rsTmp.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("EFFECTIVE_FROM",(String)getAttribute("EFFECTIVE_FROM").getObj());
                rsTmp.updateString("EFFECTIVE_TO",(String)getAttribute("EFFECTIVE_TO").getObj());
                //rsTmp.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                
                if(mst.matches("FD") || mst.matches("GD"))
                    rsTmp.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                else
                    rsTmp.updateString("PRODUCT_CODE","null");
                
                if(mst.matches("PC"))
                    rsTmp.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                else
                    rsTmp.updateString("PIECE_NO","null");
                
                rsTmp.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsTmp.updateString("MACHINE_POSITION",(String)ObjMRItems.getAttribute("MACHINE_POSITION").getObj());
                rsTmp.updateString("DISC_PER",Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsTmp.updateString("SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("SEAM_VALUE").getVal()));
                rsTmp.updateString("YRED_DISC_PER",Double.toString(ObjMRItems.getAttribute("YRED_DISC_PER").getVal()));
                rsTmp.updateString("YRED_SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("YRED_SEAM_VALUE").getVal()));
                rsTmp.updateString("TURN_OVER_TARGET",(String)ObjMRItems.getAttribute("TURN_OVER_TARGET").getObj());
                
                rsTmp.updateString("DIVERSION_FLAG",(String)ObjMRItems.getAttribute("DIVERSION_FLAG").getObj());
                
                rsTmp.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsTmp.updateString("MODIFIED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("APPROVED",false);
                rsTmp.updateBoolean("CANCELED",false);
                rsTmp.updateBoolean("REJECTED",false);
                
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                
                rsHDetail.updateString("MASTER_NO",(String)getAttribute("MASTER_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
//                if (mst.startsWith("GD")) {
                    rsHDetail.updateString("GROUP_CODE",(String)getAttribute("GROUP_CODE").getObj());       
//                } else {
                    rsHDetail.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
//                }
//                rsHDetail.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("EFFECTIVE_FROM",(String)getAttribute("EFFECTIVE_FROM").getObj());
                rsHDetail.updateString("EFFECTIVE_TO",(String)getAttribute("EFFECTIVE_TO").getObj());
                //rsHDetail.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                
                if(mst.matches("FD") || mst.matches("GD"))
                    rsHDetail.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                else
                    rsHDetail.updateString("PRODUCT_CODE","null");
                if(mst.matches("PC"))
                    rsHDetail.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                else
                    rsHDetail.updateString("PIECE_NO","null");
                
                rsHDetail.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsHDetail.updateString("MACHINE_POSITION",(String)ObjMRItems.getAttribute("MACHINE_POSITION").getObj());
                rsHDetail.updateString("DISC_PER",Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsHDetail.updateString("SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("SEAM_VALUE").getVal()));
                rsHDetail.updateString("YRED_DISC_PER",Double.toString(ObjMRItems.getAttribute("YRED_DISC_PER").getVal()));
                rsHDetail.updateString("YRED_SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("YRED_SEAM_VALUE").getVal()));
                rsHDetail.updateString("TURN_OVER_TARGET",(String)ObjMRItems.getAttribute("TURN_OVER_TARGET").getObj());
                
                rsHDetail.updateString("DIVERSION_FLAG",(String)ObjMRItems.getAttribute("DIVERSION_FLAG").getObj());
                
                rsHDetail.updateString("CREATED_BY",Integer.toString(EITLERPGLOBAL.gNewUserID));
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB()+" "+EITLERPGLOBAL.getCurrentTime());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("APPROVED",false);
                rsHDetail.updateBoolean("CANCELED",false);
                rsHDetail.updateBoolean("REJECTED",false);
           
                
                rsHDetail.insertRow();
            }
            
            
            //===================== Update the Approval Flow ======================//
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
            
            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsDiscRateMaster.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("MASTER_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName="PRODUCTION.FELT_RATE_DISC_MASTER_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="MASTER_NO";
            
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
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER_H WHERE MASTER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL_H WHERE MASTER_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=getAttribute("MASTER_NO").getString();
            String mst = getAttribute("MASTER_NO").getString().substring(0,2);
            
            //** Open History Table Connections **//
            //  stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
            //  rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD='1'"); // '1' for restricting all data retrieval
            //  rsHistory.first();
            //** --------------------------------**//
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            // rsResultSet.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsResultSet.updateString("SANCTION_DATE", getAttribute("SANCTION_DATE").getString());
            rsResultSet.updateString("EFFECTIVE_FROM", getAttribute("EFFECTIVE_FROM").getString());
            if(getAttribute("EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                rsResultSet.updateString("EFFECTIVE_TO",null);
            else
                rsResultSet.updateString("EFFECTIVE_TO", getAttribute("EFFECTIVE_TO").getString());
            
//            if (mst.startsWith("GD")) {
                rsResultSet.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
                rsResultSet.updateString("GROUP_NAME",getAttribute("GROUP_NAME").getString());            
//            } else {
                rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
                rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
//            }
//            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
//            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            //rsResultSet.updateString("TURN_OVER_TARGET",getAttribute("TURN_OVER_TARGET").getString());
            
            rsResultSet.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsResultSet.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsResultSet.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsResultSet.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsResultSet.updateString("REMARK5",getAttribute("REMARK5").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            //rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsResultSet.updateString("PRIORITY",(String)getAttribute("PRIORITY").getObj());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER_H WHERE MASTER_NO='"+(String)getAttribute("MASTER_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("MASTER_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHistory.updateString("MASTER_NO", getAttribute("MASTER_NO").getString());
            rsHistory.updateString("SANCTION_DATE", getAttribute("SANCTION_DATE").getString());
            rsHistory.updateString("EFFECTIVE_FROM", getAttribute("EFFECTIVE_FROM").getString());
            if(getAttribute("EFFECTIVE_TO").getString().equalsIgnoreCase(""))
                rsHistory.updateString("EFFECTIVE_TO",null);
            else
                rsHistory.updateString("EFFECTIVE_TO", getAttribute("EFFECTIVE_TO").getString());
            
//            if (mst.startsWith("GD")) {
                rsHistory.updateString("GROUP_CODE",getAttribute("GROUP_CODE").getString());
                rsHistory.updateString("GROUP_NAME",getAttribute("GROUP_NAME").getString());            
//            } else {
                rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
                rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
//            }
//            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
//            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            //rsHistory.updateString("TURN_OVER_TARGET",getAttribute("TURN_OVER_TARGET").getString());
            
            rsHistory.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsHistory.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsHistory.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsHistory.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsHistory.updateString("REMARK5",getAttribute("REMARK5").getString());
            
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
            String mMasterNo=(String)getAttribute("MASTER_NO").getObj();
            
            data.Execute("DELETE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='"+mMasterNo+"'");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='1'");
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsDiscRateMasterItem ObjMRItems=(clsDiscRateMasterItem) colMRItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateString("MASTER_NO", (String) getAttribute("MASTER_NO").getObj());
                rsTmp.updateInt("SR_NO", i);
//                if (mst.startsWith("GD")) {
                    rsTmp.updateString("GROUP_CODE", (String) getAttribute("GROUP_CODE").getObj());
//                } else {
                    rsTmp.updateString("PARTY_CODE", (String) getAttribute("PARTY_CODE").getObj());
//                }
//                rsTmp.updateString("PARTY_CODE", (String) getAttribute("PARTY_CODE").getObj());
                rsTmp.updateString("EFFECTIVE_FROM", (String) getAttribute("EFFECTIVE_FROM").getObj());
                rsTmp.updateString("EFFECTIVE_TO",(String)getAttribute("EFFECTIVE_TO").getObj());
                
                //rsTmp.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                
                if(mst.matches("FD") || mst.matches("GD"))
                    rsTmp.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                else
                    rsTmp.updateString("PRODUCT_CODE","null");
                if(mst.matches("PC"))
                    rsTmp.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                else
                    rsTmp.updateString("PIECE_NO","null");
                
                rsTmp.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsTmp.updateString("MACHINE_POSITION",(String)ObjMRItems.getAttribute("MACHINE_POSITION").getObj());
                rsTmp.updateString("DISC_PER",Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsTmp.updateString("SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("SEAM_VALUE").getVal()));
                rsTmp.updateString("YRED_DISC_PER",Double.toString(ObjMRItems.getAttribute("YRED_DISC_PER").getVal()));
                rsTmp.updateString("YRED_SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("YRED_SEAM_VALUE").getVal()));
                rsTmp.updateString("TURN_OVER_TARGET",(String)ObjMRItems.getAttribute("TURN_OVER_TARGET").getObj());
                
                rsTmp.updateString("DIVERSION_FLAG",(String)ObjMRItems.getAttribute("DIVERSION_FLAG").getObj());
                
                rsTmp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateString("MASTER_NO",(String)getAttribute("MASTER_NO").getObj());
                rsHDetail.updateLong("SR_NO",i);
//                if (mst.startsWith("GD")) {
                    rsHDetail.updateString("GROUP_CODE", (String) getAttribute("GROUP_CODE").getObj());
//                } else {
                    rsHDetail.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
//                }
//                rsHDetail.updateString("PARTY_CODE",(String)getAttribute("PARTY_CODE").getObj());
                rsHDetail.updateString("EFFECTIVE_FROM",(String)getAttribute("EFFECTIVE_FROM").getObj());
                rsHDetail.updateString("EFFECTIVE_TO",(String)getAttribute("EFFECTIVE_TO").getObj());
                
                //rsHDetail.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                
                if(mst.matches("FD") || mst.matches("GD"))
                    rsHDetail.updateString("PRODUCT_CODE",(String)ObjMRItems.getAttribute("PRODUCT_CODE").getObj());
                else
                    rsHDetail.updateString("PRODUCT_CODE","null");
                if(mst.matches("PC"))
                    rsHDetail.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                else
                    rsHDetail.updateString("PIECE_NO","null");
                
                rsHDetail.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsHDetail.updateString("MACHINE_POSITION",(String)ObjMRItems.getAttribute("MACHINE_POSITION").getObj());
                rsHDetail.updateString("DISC_PER",Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsHDetail.updateString("SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("SEAM_VALUE").getVal()));
                rsHDetail.updateString("YRED_DISC_PER",Double.toString(ObjMRItems.getAttribute("YRED_DISC_PER").getVal()));
                rsHDetail.updateString("YRED_SEAM_VALUE",Double.toString(ObjMRItems.getAttribute("YRED_SEAM_VALUE").getVal()));
                rsHDetail.updateString("TURN_OVER_TARGET",(String)ObjMRItems.getAttribute("TURN_OVER_TARGET").getObj());
                
                rsHDetail.updateString("DIVERSION_FLAG",(String)ObjMRItems.getAttribute("DIVERSION_FLAG").getObj());
                
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
            ObjFlow.ModuleID=clsDiscRateMaster.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocNo=(String)getAttribute("MASTER_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("CREATED_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_RATE_DISC_MASTER_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="MASTER_NO";
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
                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE MASTER_NO='"+ObjFlow.DocNo+"' ");
                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE MASTER_NO='"+ObjFlow.DocNo+"' ");
                
                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+clsDiscRateMaster.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
                
                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D SET D.APPROVED=H.APPROVED,D.CANCELED=H.CANCELED,D.REJECTED=H.REJECTED WHERE H.MASTER_NO=D.MASTER_NO AND D.MASTER_NO='"+ObjFlow.DocNo+"' ");
                //
                //                 data.Execute("UPDATE FELT_RATE_DISC_MASTER A,FELT_RATE_DISC_MASTER_DETAIL B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_TO,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND A.EFFECTIVE_TO IS NULL AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM)  AND B.MASTER_NO='" + ObjFlow.DocNo + "' AND B.CANCELED=0");
                //                 data.Execute("UPDATE FELT_RATE_DISC_MASTER_HEAD A,FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_TO,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND A.PRODUCT_CODE=B.PRODUCT_CODE AND A.EFFECTIVE_TO IS NULL AND B.MASTER_NO='" + ObjFlow.DocNo + "'");
                //data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEAD A,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER B SET A.EFFECTIVE_TO=SUBDATE(B.EFFECTIVE_FROM,1) WHERE A.PARTY_CODE=B.PARTY_CODE AND (A.EFFECTIVE_TO IS NULL OR A.EFFECTIVE_TO>=B.EFFECTIVE_FROM) AND B.MASTER_NO='" + ObjFlow.DocNo + "' AND A.CANCELED=0");
                //                 //data.Execute("DELETE FROM PRODUCTION.FELT_MACHINE_POSITION_MASTER WHERE MM_PARTY_CODE='"+ObjFlow.DocNo+"'");
                data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
                data.Execute("INSERT INTO PRODUCTION.FELT_RATE_DISC_MASTER_HEAD SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+ObjFlow.DocNo+"'");
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
            
            setAttribute("MASTER_NO",rsResultSet.getString("MASTER_NO"));
            setAttribute("SANCTION_DATE",rsResultSet.getString("SANCTION_DATE"));
            setAttribute("EFFECTIVE_FROM",rsResultSet.getString("EFFECTIVE_FROM"));
            setAttribute("EFFECTIVE_TO",rsResultSet.getString("EFFECTIVE_TO"));
            setAttribute("PARTY_CODE",rsResultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("GROUP_CODE",rsResultSet.getString("GROUP_CODE"));
            setAttribute("GROUP_NAME",rsResultSet.getString("GROUP_NAME"));
            //setAttribute("TURN_OVER_TARGET",rsResultSet.getString("TURN_OVER_TARGET"));
            
            setAttribute("REMARK1",rsResultSet.getString("REMARK1"));
            setAttribute("REMARK2",rsResultSet.getString("REMARK2"));
            setAttribute("REMARK3",rsResultSet.getString("REMARK3"));
            setAttribute("REMARK4",rsResultSet.getString("REMARK4"));
            setAttribute("REMARK5",rsResultSet.getString("REMARK5"));
            
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
            setAttribute("CANCELED",rsResultSet.getInt("CANCELED"));
            setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));
            
            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();
            
            //String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mMasterNo=(String) getAttribute("MASTER_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL_H WHERE MASTER_NO='"+mMasterNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='"+mMasterNo+"' ORDER BY SR_NO");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsDiscRateMasterItem ObjMRItems = new clsDiscRateMasterItem();
                ObjMRItems.setAttribute("MASTER_NO",rsTmp.getString("MASTER_NO"));
                ObjMRItems.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjMRItems.setAttribute("SANCTION_DATE",rsTmp.getString("SANCTION_DATE"));
                ObjMRItems.setAttribute("EFFECTIVE_FROM",rsTmp.getString("EFFECTIVE_FROM"));
                ObjMRItems.setAttribute("EFFECTIVE_TO",rsTmp.getString("EFFECTIVE_TO"));
                ObjMRItems.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjMRItems.setAttribute("GROUP_CODE",rsTmp.getString("GROUP_CODE"));
                ObjMRItems.setAttribute("PRODUCT_CODE",rsTmp.getString("PRODUCT_CODE"));
                ObjMRItems.setAttribute("PIECE_NO",rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("TURN_OVER_TARGET",rsTmp.getString("TURN_OVER_TARGET"));
                ObjMRItems.setAttribute("MACHINE_NO",rsTmp.getString("MACHINE_NO"));
                ObjMRItems.setAttribute("MACHINE_POSITION",rsTmp.getString("MACHINE_POSITION"));
                ObjMRItems.setAttribute("DISC_PER",rsTmp.getDouble("DISC_PER"));
                ObjMRItems.setAttribute("SEAM_VALUE",rsTmp.getDouble("SEAM_VALUE")); //SEAM CHG
                ObjMRItems.setAttribute("YRED_DISC_PER",rsTmp.getDouble("YRED_DISC_PER"));
                ObjMRItems.setAttribute("YRED_SEAM_VALUE",rsTmp.getDouble("YRED_SEAM_VALUE")); //SEAM CHG
                
                ObjMRItems.setAttribute("DIVERSION_FLAG",rsTmp.getString("DIVERSION_FLAG"));
                
                ObjMRItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                //ObjMRItems.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                colMRItems.put(Long.toString(Counter),ObjMRItems);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=730 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    public static HashMap getHistoryList(int CompanyID,String MasterNo) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER_H WHERE MASTER_NO='"+MasterNo+"' ORDER BY REVISION_NO";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDiscRateMaster objParty=new clsDiscRateMaster();
                    
                    objParty.setAttribute("MASTER_NO",rsTmp.getString("MASTER_NO"));
                    objParty.setAttribute("EFFECTIVE_FROM",rsTmp.getString("EFFECTIVE_FROM"));
                    objParty.setAttribute("EFFECTIVE_TO",rsTmp.getString("EFFECTIVE_TO"));
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
            strSQL+= "SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE " + Condition + " ORDER BY MASTER_NO";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE EFFECTIVE_FROM>='"+EITLERPGLOBAL.FinFromDateDB+"' AND EFFECTIVE_FROM<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY MASTER_NO";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=730 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            String lDocNo=(String)getAttribute("MASTER_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD WHERE MASTER_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE MASTER_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_RATE_DISC_MASTER WHERE MASTER_NO='"+lDocNo+"'";
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
    
    
    public Object getObject(int CompanyID, String MasterCode) {
        String strCondition = " WHERE MASTER_NO='" + MasterCode + "' ";
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
                strSQL="SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=730 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=730 ORDER BY PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=730 ORDER BY PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsDiscRateMaster ObjItem = new clsDiscRateMaster();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("MASTER_NO",rsTmp.getString("MASTER_NO"));
                ObjItem.setAttribute("EFFECTIVE_FROM",rsTmp.getString("EFFECTIVE_FROM"));
                ObjItem.setAttribute("EFFECTIVE_TO",rsTmp.getString("EFFECTIVE_TO"));
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
                strSQL="SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.PARTY_CODE,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.PARTY_NAME,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.GROUP_CODE,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.GROUP_NAME,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=730 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.PARTY_CODE,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.PARTY_NAME,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.GROUP_CODE,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.GROUP_NAME,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=730 ORDER BY PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.EFFECTIVE_FROM,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.PARTY_CODE,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.PARTY_NAME,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.GROUP_CODE,PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.GROUP_NAME,RECEIVED_DATE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=730 ORDER BY PRODUCTION.FELT_RATE_DISC_MASTER_HEADER.MASTER_NO";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsDiscRateMaster ObjItem = new clsDiscRateMaster();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("MASTER_NO",rsTmp.getString("MASTER_NO"));
                ObjItem.setAttribute("EFFECTIVE_FROM",rsTmp.getString("EFFECTIVE_FROM"));
                //ObjItem.setAttribute("EFFECTIVE_TO",rsTmp.getString("EFFECTIVE_TO"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
//                ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE")+rsTmp.getString("GROUP_CODE"));
                ObjItem.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME")+rsTmp.getString("GROUP_NAME"));
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
            System.out.println("Error In Pending : doc : ");
            e.printStackTrace();
        }
        
        return List;
    }
    
    public boolean ShowHistory(int pCompanyID,String pMasterNo) {
        Ready=false;
        try {
            //Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER_H WHERE MASTER_NO='"+pMasterNo+"' ORDER BY REVISION_NO");
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
    
    public static String getItemLength(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Length="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT LNGTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Length=rsTmp.getString("LNGTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Length;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPinInd(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Pinind="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PIN_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Pinind=rsTmp.getString("PIN_IND");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Pinind;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getSPRInd(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Sprind="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SPR_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Sprind=rsTmp.getString("SPR_IND");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Sprind;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getChemTrtIn(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Chemtrtin="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CHEM_TRT_IN FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Chemtrtin=rsTmp.getString("CHEM_TRT_IN");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Chemtrtin;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static float getCharges(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float Charges=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CHARGES FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Charges=Float.parseFloat(rsTmp.getString("CHARGES"));
                //Charges=Float.valueOf(rsTmp.getString("CHEM_TRT_IN"));
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Charges;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static String getSqmind(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Sqmind="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SQM_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Sqmind=rsTmp.getString("SQM_IND");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Sqmind;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static float getSQMRate(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float sqmrate=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SQM_RATE FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                sqmrate=Float.parseFloat(rsTmp.getString("SQM_RATE"));
                //Charges=Float.valueOf(rsTmp.getString("CHEM_TRT_IN"));
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return sqmrate;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static float getWTRate(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float wtrate=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WT_RATE FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                wtrate=Float.parseFloat(rsTmp.getString("WT_RATE"));
                //Charges=Float.valueOf(rsTmp.getString("CHEM_TRT_IN"));
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return wtrate;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    
    
    public static double getItemWidth(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        double Width=0.00;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Width=rsTmp.getString("WIDTH");
                Width=rsTmp.getDouble("WIDTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Width;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static String getItemStyle(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT BALNK FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("BALNK");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getNewDisamt(String pDiscper,String pBasamt) {
        
        double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);
        
        double newDisamt=0.00;
        newDisamt=(Basamt)*(Discper/100);
        newDisamt=EITLERPGLOBAL.round(newDisamt, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newDisamt);
        /*
                kgsum += Double.parseDouble(rsTmp.getString("WEIGHT"));
                  invsum += Double.parseDouble(rsTmp.getString("INV_AMT"));
                  //txttotal.setText(Double.toString(Math.round(sum)));
                  //txttotal.setText(Double.toString(sum));
                   DecimalFormat df = new DecimalFormat("##.##");
                   txtkgtotal.setText(df.format(kgsum));
                   txtinvtotal.setText(df.format(invsum));
         
         */
    }
    
    public static String getWeight(String pLength,String pWidth,String pGsq,String pProduct,String pPieceNo,String pPartyCode){
        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Gsq = Double.parseDouble(pGsq);
        double Product = Double.parseDouble(pProduct);
        double newWeight=0.00;
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT RECD_KG FROM PRODUCTION.FELT_PIECE_REGISTER WHERE ORDER_CD="+pPieceNo+" AND PARTY_CODE='"+pPartyCode+"'");
            rsTmp.first();
            
            if(Product==7190110 || Product==7190210 || Product==7190310 || Product==7190410 || Product==7190510 || Product==7290000) {
                newWeight=((Length*Width));
            }
            else{
                if(rsTmp.getRow()>0) {
                    newWeight=rsTmp.getFloat("RECD_KG");
                }
                else {
                    newWeight=0;
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
        }
        catch(Exception e) {
            return "";
        }
        
        newWeight=EITLERPGLOBAL.round(newWeight, 2);
        return Double.toString(newWeight);
    }
    
    
    public static String getNewWeight(String pLength,String pWidth,String pGsq,String pProduct){
        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Gsq = Double.parseDouble(pGsq);
        double Product = Double.parseDouble(pProduct);
        double newWeight=0.00;
        
        if(Product==7190110 || Product==7190210 || Product==7190310 || Product==7190410 || Product==7190510 || Product==7290000) {
            newWeight=((Length*Width));
        }else {
            newWeight=(Length*Width*(Gsq/1000));
        }
        
        newWeight=EITLERPGLOBAL.round(newWeight, 2);
        return Double.toString(newWeight);
    }
    
    public static String getBasamt(String pSqmInd,String pLength,String pWidth,float pSQMrate,String pWeight,float pWTrate) {
        
        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Weight = Double.parseDouble(pWeight);
        double SQMrate = pSQMrate;
        double WTrate = pWTrate;
        
        double newBasamt=0.00;
        if(pSqmInd.equals("1")){
            newBasamt=(Length)*(Width)*(SQMrate);
            newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }
        else{
            newBasamt=0.00;
            newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }
        //return Double.toString(Math.round(newBasamt));
        return Double.toString(newBasamt);
        
    }
    
    
    public static String getNewBasamt(String pSqmInd,String pLength,String pWidth,float pSQMrate,String pWeight,float pWTrate) {
        
        double Length = Double.parseDouble(pLength);
        double Width = Double.parseDouble(pWidth);
        double Weight = Double.parseDouble(pWeight);
        double SQMrate = pSQMrate;
        double WTrate = pWTrate;
        
        double newBasamt=0.00;
        if(pSqmInd.equals("1")){
            newBasamt=(Length)*(Width)*(SQMrate);
            newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }
        else{
            newBasamt=(Weight)*(WTrate);
            newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }
        //return Double.toString(Math.round(newBasamt));
        return Double.toString(newBasamt);
        
    }
    
  /*  public static String getNewBasamtelse(String pWeight,float pWTrate) {
   
       double Weight = Double.parseDouble(pWeight);
       double WTrate = pWTrate;
   
        double newBasamt=0.00;
        newBasamt=(Weight)*(WTrate);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newBasamt);
   
    }
   */
    
    public static String getNewWPSC(String pChemTrtIn,String pPinInd,String pSprInd,String pWeight,float pCharges,String pWidth, String pChargesDisPer) {
        
        double Weight = Double.parseDouble(pWeight);
        double Width = Double.parseDouble(pWidth);
        double ChargesDisPer= Double.parseDouble(pChargesDisPer);
        double Charges = pCharges;
        Charges=Charges*((100-ChargesDisPer)/100);
        
        
        double newWPSC1=0.00;
        double newWPSC2=0.00;
        double newWPSC3=0.00;
        double newWPSC=0.00;
        if(pChemTrtIn.equals("1")){
            newWPSC1=(Weight)*(Charges);
        }
        else {
            newWPSC1=0;
        }
        if(pPinInd.equals("1")){
            newWPSC2=(Width)*(Charges);
        }
        else {
            newWPSC2=0;
        }
        if(pSprInd.equals("1")){
            newWPSC3=(Width)*(Charges);
        }
        else {
            newWPSC3=0;
        }
        
        newWPSC=newWPSC1+newWPSC2+newWPSC3;
        newWPSC=EITLERPGLOBAL.round(newWPSC, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newWPSC);
        
    }
    
    public static String getNewDisBasamt(String pDiscper,String pBasamt) {
        double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);
        
        double newDisBasamt=0.00;
        newDisBasamt=Basamt-((Basamt)*(Discper/100));
        newDisBasamt=EITLERPGLOBAL.round(newDisBasamt, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newDisBasamt);
        
    }
    
    public static String getNewExcise(String pDisbasamt,String pWpsc) {
        double Disbasamt = Double.parseDouble(pDisbasamt);
        double Wpsc = Double.parseDouble(pWpsc);
        
        double newExcise=0.00;
        //newExcise=((Disbasamt+Wpsc)*.12)+((Disbasamt+Wpsc)*.12)*.01+((Disbasamt+Wpsc)*.12)*.02;
        //New Changes in Budget 2015-16
        newExcise=((Disbasamt+Wpsc)*.125);
        //return Double.toString(Math.round(newDisamt));
        //return Double.toString(newExcise);
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(newExcise);
        
    }
    
    public static String getNewInsaccamt(String pInsInd,String pNewDisbasamt,String pNewExcise,String pWPSC) {
        double InsInd = Double.parseDouble(pInsInd);
        double WPSC = Double.parseDouble(pWPSC);
        double NewDisbasamt=Double.parseDouble(pNewDisbasamt);
        double NewExcise=Double.parseDouble(pNewExcise);
        
        double newInsaccamt=0.00;
        if(InsInd==1) {
            //newInsaccamt=Math.round((Math.round(NewDisbasamt+NewExcise+WPSC)+(Math.round(NewDisbasamt+NewExcise+WPSC)*.10))*.0039);
            newInsaccamt=(ConvertToNextThousand((Math.round(NewDisbasamt+NewExcise+WPSC)+(Math.round(NewDisbasamt+NewExcise+WPSC)*.10)))*0.0039)+0.05;
            return Double.toString(newInsaccamt);
        }
        else{
            return Double.toString(0);
        }
    }
    
    public static String getNewInvamt(String pInsInd,String pNewDisbasamt,String pNewExcise,String pWPSC,String pNewInsaccamt) {
        double InsInd = Double.parseDouble(pInsInd);
        double WPSC = Double.parseDouble(pWPSC);
        double NewDisbasamt=Double.parseDouble(pNewDisbasamt);
        double NewExcise=Double.parseDouble(pNewExcise);
        double NewInsaccamt=Double.parseDouble(pNewInsaccamt);
        
        double newInvamt=0.00;
        if(InsInd==1) {
            newInvamt=NewDisbasamt+NewExcise+WPSC+NewInsaccamt;
            DecimalFormat df1 = new DecimalFormat("##");
            return df1.format(newInvamt);
        }
        else{
            newInvamt=NewDisbasamt+NewExcise+WPSC;
            DecimalFormat df2 = new DecimalFormat("##");
            return df2.format(newInvamt);
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
    
    
    public static int getItemGsq(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        int Gsq=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Gsq=rsTmp.getInt("GSQ");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Gsq;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static String[] getPiecedetail(String pPartyCode,String pPieceNo){
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String []Piecedetail = new String[40];
        String []error = {"error"};
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            String strSQL="SELECT *,false as Calculate_Weight,0 as SEAM_CHG_PER FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(PARTY_NAME)),'PARTY DELETED IN COBOL') AS PARTY_NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE ";
            //String strSQL="SELECT * FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(NAME)),'PARTY DELETED IN COBOL') AS NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE ";
            //strSQL+="B.PIECE_NO="+pPieceNo+" AND ";
            //strSQL+="A.PIECE_NO='"+pPieceNo+"' AND ";
            if(!pPartyCode.equals("")){
                strSQL+="B.PARTY_CD='"+pPartyCode+"' AND ";
            }
            strSQL+="A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 )) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010') AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ";
            strSQL+="UNION ALL ";
            strSQL+="SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN  (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="PIECE_NO='"+pPieceNo+"' ";
            if(!pPartyCode.equals("")){
                strSQL+="PARTY_CD='"+pPartyCode+"' ";
            }
            strSQL+="AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5,6,7,8) AND MAIN_ACCOUNT_CODE='210010') AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ) AS P WHERE PIECE_NO='"+pPieceNo+"' ";
            //rsTmp=stTmp.executeQuery("SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(NAME)),'PARTY DELETED IN COBOL') AS NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1 FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1 FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE B.PIECE_NO="+pPieceNo+" AND B.PARTY_CD='"+pPartyCode+"' AND A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 AND ST_FLAG_1 !='P')) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD UNION ALL SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"' AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5) ) AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ");
            System.out.println("Piece Detail Query :");
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow() > 0) {
                Piecedetail[0]=rsTmp.getString("PARTY_CD");
                Piecedetail[1]=rsTmp.getString("PARTY_NAME");
                Piecedetail[2]=rsTmp.getString("PRIORITY_DESC");
                Piecedetail[3]=rsTmp.getString("PIECE_NO");
                Piecedetail[4]=rsTmp.getString("ORDER_DATE");
                Piecedetail[5]=rsTmp.getString("DELIV_DATE");
                Piecedetail[6]=rsTmp.getString("COMM_DATE");
                Piecedetail[7]=(rsTmp.getString("PRODUCT_CD")+'0').substring(0,7);
                Piecedetail[8]=rsTmp.getString("ITEM");
                Piecedetail[9]=rsTmp.getString("STYLE");
                Piecedetail[10]=rsTmp.getString("LNGTH");
                Piecedetail[11]=rsTmp.getString("WIDTH");
                Piecedetail[12]=rsTmp.getString("GSQ");
                //Piecedetail[13]=rsTmp.getString("WEIGHT");
                
                
                String Productcode=(Piecedetail[7]+'0').substring(0,7);
                String PinInd=clsDiscRateMaster.getPinInd(Productcode);
                String SprInd=clsDiscRateMaster.getSPRInd(Productcode);
                String ChemTrtIn=clsDiscRateMaster.getChemTrtIn(Productcode);
                float Charges=clsDiscRateMaster.getCharges(Productcode);
                String SqmInd=clsDiscRateMaster.getSqmind(Productcode);
                float SQMrate=clsDiscRateMaster.getSQMRate(Productcode);
                float WTrate=clsDiscRateMaster.getWTRate(Productcode);
                
                Piecedetail[13]=getWeight(Piecedetail[10], Piecedetail[11], Piecedetail[12], Piecedetail[7], Piecedetail[3], Piecedetail[0]);
                
                
                Piecedetail[14]=rsTmp.getString("RATE");
                //Piecedetail[15]=rsTmp.getString("BAS_AMT");
                
                
                //Piecedetail[15]=getBasamt(SprInd, Piecedetail[10], Piecedetail[11], SQMrate, Piecedetail[13], WTrate);
                
                Piecedetail[16]=rsTmp.getString("DISC_PER");
                
                
                if (Piecedetail[13].equals("0.0")){
                    Piecedetail[15]="0.00";
                    Piecedetail[17]="0.00";
                    Piecedetail[18]="0.00";
                    Piecedetail[19]="0.00";
                    Piecedetail[20]="0.00";
                    Piecedetail[21]="0.00";
                    Piecedetail[22]="0.00";
                }else{
                    Piecedetail[15]=rsTmp.getString("BAS_AMT");
                    Piecedetail[17]=rsTmp.getString("DISAMT");
                    Piecedetail[18]=rsTmp.getString("DISBASAMT");
                    //Piecedetail[18]=getNewDisBasamt(Piecedetail[16],Piecedetail[15]);
                    
                    Piecedetail[19]=rsTmp.getString("EXCISE");
                    
                    //Piecedetail[19]=getNewExcise(Piecedetail[16], getNewWPSC(ChemTrtIn, PinInd, SprInd, Piecedetail[13], Charges, Piecedetail[11],Piecedetail[34]));
                    
                    Piecedetail[20]=rsTmp.getString("SEAM_CHG");
                    Piecedetail[21]=rsTmp.getString("INSACC_AMT");
                    Piecedetail[22]=rsTmp.getString("INV_AMT");
                }
                //Piecedetail[17]=getNewDisamt(Piecedetail[16], Piecedetail[15]);
                
                Piecedetail[23]=rsTmp.getString("INSURANCE_CODE");
                Piecedetail[24]=rsTmp.getString("REF_NO");
                Piecedetail[25]=rsTmp.getString("CONF_NO");
                Piecedetail[26]=rsTmp.getString("MACHINE_NO");
                Piecedetail[27]=rsTmp.getString("POSITION");
                Piecedetail[28]=rsTmp.getString("ZONE");
                Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("PRIORITY_DATE"));
                Piecedetail[30]=rsTmp.getString("INCHARGE_NAME");
                
                Piecedetail[31]=rsTmp.getString("PO_NO");
                Piecedetail[32]=EITLERPGLOBAL.formatDate(rsTmp.getString("PO_DATE"));
                Piecedetail[33]=String.valueOf(rsTmp.getBoolean("Calculate_Weight"));
                Piecedetail[34]=rsTmp.getString("SEAM_CHG_PER");
                
                
                
                
                /*Piecedetail[31]=rsTmp.getString("CHEM_TRT_IN");
                Piecedetail[32]=rsTmp.getString("PIN_IND");
                Piecedetail[33]=rsTmp.getString("CHARGES");
                Piecedetail[34]=rsTmp.getString("SPR_IND");
                Piecedetail[35]=rsTmp.getString("SQM_IND");
                 */
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
    
    
    public static String[] getOtherPiecedetail(String pPartyCode,String pPieceNo){
        Connection tmpConnOther;
        Statement stTmpOther;
        ResultSet rsTmpOther;
        String []OtherPiecedetail = new String[40];
        String []error = {"error"};
        
        try {
            tmpConnOther=data.getConn();
            
            stTmpOther=tmpConnOther.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT *,false as Calculate_Weight,0 as SEAM_CHG_PER FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(PARTY_NAME)),'PARTY DELETED IN COBOL') AS PARTY_NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE ";
            if(!pPartyCode.equals("")){
                strSQL+="B.PARTY_CD='"+pPartyCode+"' AND ";
            }
            strSQL+="A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 )) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010') AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ";
            strSQL+="UNION ALL ";
            strSQL+="SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN  (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="PIECE_NO='"+pPieceNo+"' ";
            if(!pPartyCode.equals("")){
                strSQL+="PARTY_CD='"+pPartyCode+"' ";
            }
            strSQL+="AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5,6,7,8) AND MAIN_ACCOUNT_CODE='210010') AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ) AS P WHERE PIECE_NO='"+pPieceNo+"' ";
            System.out.println("Other Piece Detail Query:");
            System.out.print(strSQL);
            
            rsTmpOther=stTmpOther.executeQuery(strSQL);
            rsTmpOther.first();
            
            if(rsTmpOther.getRow() > 0) {
                OtherPiecedetail[0]=rsTmpOther.getString("PARTY_CD");
                OtherPiecedetail[1]=rsTmpOther.getString("PARTY_NAME");
                OtherPiecedetail[2]=rsTmpOther.getString("PRIORITY_DESC");
                OtherPiecedetail[3]=rsTmpOther.getString("PIECE_NO");
                OtherPiecedetail[4]=rsTmpOther.getString("ORDER_DATE");
                OtherPiecedetail[5]=rsTmpOther.getString("DELIV_DATE");
                OtherPiecedetail[6]=rsTmpOther.getString("COMM_DATE");
                OtherPiecedetail[7]=rsTmpOther.getString("PRODUCT_CD");
                OtherPiecedetail[8]=rsTmpOther.getString("ITEM");
                
                OtherPiecedetail[9]=rsTmpOther.getString("STYLE");
                OtherPiecedetail[10]=rsTmpOther.getString("LNGTH");
                OtherPiecedetail[11]=rsTmpOther.getString("WIDTH");
                OtherPiecedetail[12]=rsTmpOther.getString("GSQ");
                OtherPiecedetail[13]=rsTmpOther.getString("WEIGHT");
                OtherPiecedetail[14]=rsTmpOther.getString("RATE");
                OtherPiecedetail[15]=rsTmpOther.getString("BAS_AMT");
                OtherPiecedetail[16]=rsTmpOther.getString("DISC_PER");
                OtherPiecedetail[17]=rsTmpOther.getString("DISAMT");
                
                OtherPiecedetail[18]=rsTmpOther.getString("DISBASAMT");
                OtherPiecedetail[19]=rsTmpOther.getString("EXCISE");
                OtherPiecedetail[20]=rsTmpOther.getString("SEAM_CHG");
                OtherPiecedetail[21]=rsTmpOther.getString("INSACC_AMT");
                OtherPiecedetail[22]=rsTmpOther.getString("INV_AMT");
                
                OtherPiecedetail[23]=rsTmpOther.getString("INSURANCE_CODE");
                OtherPiecedetail[24]=rsTmpOther.getString("REF_NO");
                OtherPiecedetail[25]=rsTmpOther.getString("CONF_NO");
                OtherPiecedetail[26]=rsTmpOther.getString("MACHINE_NO");
                OtherPiecedetail[27]=rsTmpOther.getString("POSITION");
                OtherPiecedetail[28]=rsTmpOther.getString("ZONE");
                OtherPiecedetail[29]=EITLERPGLOBAL.formatDate(rsTmpOther.getString("PRIORITY_DATE"));
                OtherPiecedetail[30]=rsTmpOther.getString("INCHARGE_NAME");
                
                OtherPiecedetail[31]=rsTmpOther.getString("PO_NO");
                OtherPiecedetail[32]=EITLERPGLOBAL.formatDate(rsTmpOther.getString("PO_DATE"));
                OtherPiecedetail[33]=String.valueOf(rsTmpOther.getBoolean("Calculate_Weight"));
                OtherPiecedetail[34]=rsTmpOther.getString("SEAM_CHG_PER");
                
                
                /*
                OtherPiecedetail[31]=rsTmpOther.getString("CHEM_TRT_IN");
                OtherPiecedetail[32]=rsTmpOther.getString("PIN_IND");
                OtherPiecedetail[33]=rsTmpOther.getString("CHARGES");
                OtherPiecedetail[34]=rsTmpOther.getString("SPR_IND");
                OtherPiecedetail[35]=rsTmpOther.getString("SQM_IND");
                 */
            }
            //tmpConn.close();
            stTmpOther.close();
            rsTmpOther.close();
            return OtherPiecedetail;
            
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
                ObjParty.setAttribute("PARTY_TYPE",rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                
                ObjParty.setAttribute("SEASON_CODE",rsTmp.getString("SEASON_CODE"));
                ObjParty.setAttribute("REG_DATE",rsTmp.getString("REG_DATE"));
                
                ObjParty.setAttribute("AREA_ID",rsTmp.getString("AREA_ID"));
                ObjParty.setAttribute("ADDRESS1",rsTmp.getString("ADDRESS1"));
                ObjParty.setAttribute("ADDRESS2",rsTmp.getString("ADDRESS2"));
                ObjParty.setAttribute("CITY_ID",rsTmp.getString("CITY_ID"));
                ObjParty.setAttribute("CITY_NAME",rsTmp.getString("CITY_NAME"));
                ObjParty.setAttribute("DISPATCH_STATION",rsTmp.getString("DISPATCH_STATION"));
                ObjParty.setAttribute("DISTRICT",rsTmp.getString("DISTRICT"));
                ObjParty.setAttribute("PINCODE",rsTmp.getString("PINCODE"));
                ObjParty.setAttribute("PHONE_NO",rsTmp.getString("PHONE_NO"));
                
                ObjParty.setAttribute("REMARK1",rsTmp.getString("REMARK1"));
                ObjParty.setAttribute("REMARK2",rsTmp.getString("REMARK2"));
                ObjParty.setAttribute("REMARK3",rsTmp.getString("REMARK3"));
                ObjParty.setAttribute("REMARK4",rsTmp.getString("REMARK4"));
                ObjParty.setAttribute("REMARK5",rsTmp.getString("REMARK5"));
                
                ObjParty.setAttribute("MOBILE_NO",rsTmp.getString("MOBILE_NO"));
                ObjParty.setAttribute("EMAIL",rsTmp.getString("EMAIL"));
                ObjParty.setAttribute("URL",rsTmp.getString("URL"));
                ObjParty.setAttribute("CONTACT_PERSON",rsTmp.getString("CONTACT_PERSON"));
                ObjParty.setAttribute("BANK_ID",rsTmp.getLong("BANK_ID"));
                ObjParty.setAttribute("BANK_NAME",rsTmp.getString("BANK_NAME"));
                ObjParty.setAttribute("BANK_ADDRESS",rsTmp.getString("BANK_ADDRESS"));
                ObjParty.setAttribute("BANK_CITY",rsTmp.getString("BANK_CITY"));
                ObjParty.setAttribute("CST_NO",rsTmp.getString("CST_NO"));
                ObjParty.setAttribute("CST_DATE",rsTmp.getString("CST_DATE"));
                ObjParty.setAttribute("ECC_NO",rsTmp.getString("ECC_NO"));
                ObjParty.setAttribute("ECC_DATE",rsTmp.getString("ECC_DATE"));
                ObjParty.setAttribute("TIN_NO",rsTmp.getString("TIN_NO"));
                ObjParty.setAttribute("TIN_DATE",rsTmp.getString("TIN_DATE"));
                ObjParty.setAttribute("PAN_NO",rsTmp.getString("PAN_NO"));
                ObjParty.setAttribute("PAN_DATE",rsTmp.getString("PAN_DATE"));
                ObjParty.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjParty.setAttribute("CHARGE_CODE",rsTmp.getString("CHARGE_CODE"));
                ObjParty.setAttribute("CHARGE_CODE_II",rsTmp.getString("CHARGE_CODE_II"));
                ObjParty.setAttribute("CREDIT_DAYS",rsTmp.getDouble("CREDIT_DAYS"));
                ObjParty.setAttribute("DOCUMENT_THROUGH",rsTmp.getString("DOCUMENT_THROUGH"));
                ObjParty.setAttribute("TRANSPORTER_ID",rsTmp.getLong("TRANSPORTER_ID"));
                ObjParty.setAttribute("TRANSPORTER_NAME",rsTmp.getString("TRANSPORTER_NAME"));
                ObjParty.setAttribute("AMOUNT_LIMIT",rsTmp.getDouble("AMOUNT_LIMIT"));
                ObjParty.setAttribute("OTHER_BANK_ID",rsTmp.getLong("OTHER_BANK_ID"));
                ObjParty.setAttribute("OTHER_BANK_NAME",rsTmp.getString("OTHER_BANK_NAME"));
                ObjParty.setAttribute("OTHER_BANK_ADDRESS",rsTmp.getString("OTHER_BANK_ADDRESS"));
                ObjParty.setAttribute("OTHER_BANK_CITY",rsTmp.getString("OTHER_BANK_CITY"));
                ObjParty.setAttribute("CATEGORY",rsTmp.getString("CATEGORY"));
                ObjParty.setAttribute("INSURANCE_CODE",rsTmp.getString("INSURANCE_CODE"));
                ObjParty.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjParty.setAttribute("DELAY_INTEREST_PER",rsTmp.getDouble("DELAY_INTEREST_PER"));
                ObjParty.setAttribute("ACCOUNT_CODES",rsTmp.getString("ACCOUNT_CODES"));
                
                ObjParty.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjParty.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjParty.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjParty.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
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
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE DOC_NO='"+PartyCode+"' AND MODULE_ID='"+clsDiscRateMaster.ModuleID+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
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
            rsTmp.updateString("REJECTED_DATE","");
            rsTmp.updateString("REJECTED_REMARKS",tmpParty.getAttribute("REJECTED_REMARKS").getString());
            rsTmp.insertRow();
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    */
    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=730");
                }
                data.Execute("UPDATE PRODUCTION.FELT_RATE_DISC_MASTER_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE MASTER_NO='"+pAmendNo+"'");
                
            }
            catch(Exception e) {
                
            }
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
            System.out.println("SELECT MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE MASTER_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
    
    public static String getGroupName(String pGroupID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String GroupName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT GROUP_DESC FROM PRODUCTION.FELT_GROUP_MASTER_HEADER WHERE GROUP_CODE="+pGroupID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                GroupName=rsTmp.getString("GROUP_DESC");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return GroupName;
    }
     
}
