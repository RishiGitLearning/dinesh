/*
 * 
 *
 * 
 */
package EITLERP.FeltWH;

import java.net.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import EITLERP.*;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author nhpatel
 * @version
 */
public class clsWHInvGatepassEntry {

    public String LastError = "";
    private ResultSet rsNRGP;
    private Connection Conn;
    private static Connection connection;
    private Statement Stmt;

    public HashMap colLineItems;
    private HashMap props;

    public boolean Ready = false;

    //History Related properties
    private boolean HistoryView = false;

    //Flag Indicating whether user has entered the document no.
    public boolean UserDocNo = false;

    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }

    public void setAttribute(String PropName, Object Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, int Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, long Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, double Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, float Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, boolean Value) {
        props.put(PropName, new Variant(Value));
    }

    /**
     * Creates new clsNRGP
     */
    public clsWHInvGatepassEntry() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("GATEPASS_NO", new Variant(""));
        props.put("GATEPASS_DATE", new Variant(""));
        props.put("GATEPASS_TYPE", new Variant(""));
        
        props.put("WH_MODE_OF_TRANSPORT", new Variant(""));
        props.put("WH_CARTING_AGENT", new Variant(""));
        props.put("WH_TRANSPORTER", new Variant(""));
        props.put("WH_VEHICLE_NO", new Variant(""));
        props.put("WH_OUTWARD_NO", new Variant(""));
        props.put("WH_OUTWARD_DATE", new Variant(""));
        
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("REJECTED", new Variant(false));
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("FROM_REMARKS", new Variant(""));
        props.put("CANCELED", new Variant(false));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));

        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("PREFIX", new Variant(""));
        
        //Create a new object for line items
        colLineItems = new HashMap();
    }

    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsNRGP=Stmt.executeQuery("SELECT * FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GATEPASS_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND GATEPASS_TYPE='FGP' ORDER BY GATEPASS_NO");
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
            rsNRGP.close();
        } catch (Exception e) {

        }
    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsNRGP.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }

    }

    public boolean MoveNext() {
        try {
            if (rsNRGP.isAfterLast() || rsNRGP.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsNRGP.last();
            } else {
                rsNRGP.next();
                if (rsNRGP.isAfterLast()) {
                    rsNRGP.last();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MovePrevious() {
        try {
            if (rsNRGP.isFirst() || rsNRGP.isBeforeFirst()) {
                rsNRGP.first();
            } else {
                rsNRGP.previous();
                if (rsNRGP.isBeforeFirst()) {
                    rsNRGP.first();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MoveLast() {
        try {
            rsNRGP.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Insert(String pPrefix, String pDocno) {
        Statement stTmp,stHistory,stHDetail,stHLot;
        ResultSet rsTmp,rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "",Gatepassno = "",Declno = "";
        String RJNNo = "";
        int RJNSrno = 0,GatepassSrno = 0,DeclSrno = 0;
        
        try {
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_NRGP_HEADER WHERE GATEPASS_NO='1'");
            //rsHeader.first();
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            
            setAttribute("GATEPASS_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,803, (String)getAttribute("PREFIX").getObj(),(String)getAttribute("SUFFIX").getObj(),true));
            //-------------------------------------------------
            
            rsNRGP.moveToInsertRow();
            rsNRGP.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsNRGP.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsNRGP.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsNRGP.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            
            rsNRGP.updateString("WH_MODE_OF_TRANSPORT",(String) getAttribute("WH_MODE_OF_TRANSPORT").getObj());
            rsNRGP.updateString("WH_CARTING_AGENT",(String) getAttribute("WH_CARTING_AGENT").getObj());
            rsNRGP.updateString("WH_TRANSPORTER",(String) getAttribute("WH_TRANSPORTER").getObj());
            rsNRGP.updateString("WH_VEHICLE_NO",(String) getAttribute("WH_VEHICLE_NO").getObj());
            rsNRGP.updateString("WH_OUTWARD_NO",(String) getAttribute("WH_OUTWARD_NO").getObj());
            rsNRGP.updateString("WH_OUTWARD_DATE",(String) getAttribute("WH_OUTWARD_DATE").getObj());            
            
            rsNRGP.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsNRGP.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsNRGP.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsNRGP.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsNRGP.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsNRGP.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsNRGP.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsNRGP.updateBoolean("CHANGED",true);
            rsNRGP.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsNRGP.updateBoolean("APPROVED",false);
            rsNRGP.updateString("APPROVED_DATE","0000-00-00");
            rsNRGP.updateBoolean("REJECTED",false);
            rsNRGP.updateString("REJECTED_DATE","0000-00-00");
            rsNRGP.updateBoolean("CANCELED",false);
            rsNRGP.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            
            rsHistory.updateString("WH_MODE_OF_TRANSPORT",(String) getAttribute("WH_MODE_OF_TRANSPORT").getObj());
            rsHistory.updateString("WH_CARTING_AGENT",(String) getAttribute("WH_CARTING_AGENT").getObj());
            rsHistory.updateString("WH_TRANSPORTER",(String) getAttribute("WH_TRANSPORTER").getObj());
            rsHistory.updateString("WH_VEHICLE_NO",(String) getAttribute("WH_VEHICLE_NO").getObj());
            rsHistory.updateString("WH_OUTWARD_NO",(String) getAttribute("WH_OUTWARD_NO").getObj());
            rsHistory.updateString("WH_OUTWARD_DATE",(String) getAttribute("WH_OUTWARD_DATE").getObj());            
            
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.insertRow();
            
            
            // Inserting records in NRGP Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND GATEPASS_NO='1'");
            String nNRGPno=(String) getAttribute("GATEPASS_NO").getObj();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            String Gatepass_type=(String) getAttribute("GATEPASS_TYPE").getObj();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsWHInvGatepassEntryItem ObjNRGPItem=(clsWHInvGatepassEntryItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("GATEPASS_NO",nNRGPno);
                rsTmp.updateLong("SR_NO",i);
                
                rsTmp.updateString("WH_INVOICE_NO",(String) ObjNRGPItem.getAttribute("WH_INVOICE_NO").getObj());
                rsTmp.updateString("WH_INVOICE_DATE",(String) ObjNRGPItem.getAttribute("WH_INVOICE_DATE").getObj());
                rsTmp.updateString("WH_PARTY_CODE",(String) ObjNRGPItem.getAttribute("WH_PARTY_CODE").getObj());
                rsTmp.updateString("WH_PARTY_NAME",(String) ObjNRGPItem.getAttribute("WH_PARTY_NAME").getObj());
                rsTmp.updateString("WH_BALE_NO",(String) ObjNRGPItem.getAttribute("WH_BALE_NO").getObj());
                rsTmp.updateString("WH_BALE_DATE",(String) ObjNRGPItem.getAttribute("WH_BALE_DATE").getObj());
                rsTmp.updateString("WH_DISPATCH_STATION",(String) ObjNRGPItem.getAttribute("WH_DISPATCH_STATION").getObj());
                rsTmp.updateString("WH_ACTUAL_TRANSPORTER",(String) ObjNRGPItem.getAttribute("WH_ACTUAL_TRANSPORTER").getObj());
                rsTmp.updateString("WH_TYPE_OF_PACKING",(String) ObjNRGPItem.getAttribute("WH_TYPE_OF_PACKING").getObj());
                
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("GATEPASS_NO",nNRGPno);
                rsHDetail.updateLong("SR_NO",i);
                
                rsHDetail.updateString("WH_INVOICE_NO",(String) ObjNRGPItem.getAttribute("WH_INVOICE_NO").getObj());
                rsHDetail.updateString("WH_INVOICE_DATE",(String) ObjNRGPItem.getAttribute("WH_INVOICE_DATE").getObj());
                rsHDetail.updateString("WH_PARTY_CODE",(String) ObjNRGPItem.getAttribute("WH_PARTY_CODE").getObj());
                rsHDetail.updateString("WH_PARTY_NAME",(String) ObjNRGPItem.getAttribute("WH_PARTY_NAME").getObj());
                rsHDetail.updateString("WH_BALE_NO",(String) ObjNRGPItem.getAttribute("WH_BALE_NO").getObj());
                rsHDetail.updateString("WH_BALE_DATE",(String) ObjNRGPItem.getAttribute("WH_BALE_DATE").getObj());
                rsHDetail.updateString("WH_DISPATCH_STATION",(String) ObjNRGPItem.getAttribute("WH_DISPATCH_STATION").getObj());
                rsHDetail.updateString("WH_ACTUAL_TRANSPORTER",(String) ObjNRGPItem.getAttribute("WH_ACTUAL_TRANSPORTER").getObj());
                rsHDetail.updateString("WH_TYPE_OF_PACKING",(String) ObjNRGPItem.getAttribute("WH_TYPE_OF_PACKING").getObj());
                
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
            }
            
            ObjFlow.ModuleID = 803; //QUOTATION MODULE ID
            ObjFlow.DocNo = (String) getAttribute("GATEPASS_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("GATEPASS_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_INV_NRGP_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REMARKS").getObj();
            ObjFlow.FieldName = "GATEPASS_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            
            // Specified procedure over here
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Update() {
        Statement stTmp,stHistory,stHDetail,stHLot;
        ResultSet rsTmp,rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        String strSQL = "",Gatepassno = "",Declno = "";
        String RJNNo = "";
        int RJNSrno = 0,GatepassSrno = 0,DeclSrno = 0;
        
        try {
            
            String theDocNo=(String)getAttribute("GATEPASS_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            String GatepassNo=(String)getAttribute("GATEPASS_NO").getObj();
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //No Primary Keys will be updated & Updating of Header starts
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
            rsNRGP.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            
            rsNRGP.updateString("WH_MODE_OF_TRANSPORT",(String) getAttribute("WH_MODE_OF_TRANSPORT").getObj());
            rsNRGP.updateString("WH_CARTING_AGENT",(String) getAttribute("WH_CARTING_AGENT").getObj());
            rsNRGP.updateString("WH_TRANSPORTER",(String) getAttribute("WH_TRANSPORTER").getObj());
            rsNRGP.updateString("WH_VEHICLE_NO",(String) getAttribute("WH_VEHICLE_NO").getObj());
            rsNRGP.updateString("WH_OUTWARD_NO",(String) getAttribute("WH_OUTWARD_NO").getObj());
            rsNRGP.updateString("WH_OUTWARD_DATE",(String) getAttribute("WH_OUTWARD_DATE").getObj());            
            
            rsNRGP.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsNRGP.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsNRGP.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsNRGP.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsNRGP.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsNRGP.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsNRGP.updateBoolean("CHANGED",true);
            rsNRGP.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsNRGP.updateBoolean("CANCELED",false);
            rsNRGP.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_NRGP_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+(String)getAttribute("GATEPASS_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("GATEPASS_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            
            rsHistory.updateString("WH_MODE_OF_TRANSPORT",(String) getAttribute("WH_MODE_OF_TRANSPORT").getObj());
            rsHistory.updateString("WH_CARTING_AGENT",(String) getAttribute("WH_CARTING_AGENT").getObj());
            rsHistory.updateString("WH_TRANSPORTER",(String) getAttribute("WH_TRANSPORTER").getObj());
            rsHistory.updateString("WH_VEHICLE_NO",(String) getAttribute("WH_VEHICLE_NO").getObj());
            rsHistory.updateString("WH_OUTWARD_NO",(String) getAttribute("WH_OUTWARD_NO").getObj());
            rsHistory.updateString("WH_OUTWARD_DATE",(String) getAttribute("WH_OUTWARD_DATE").getObj());            
            
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("GATEPASS_TYPE").getObj();
            
            // Inserting records in NRGP Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String nNRGPno=(String) getAttribute("GATEPASS_NO").getObj();
            
            String Del_H = "DELETE FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+nNRGPno.trim()+"'";
            data.Execute(Del_H);
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='1'");
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            String Gatepass_type=(String) getAttribute("GATEPASS_TYPE").getObj();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsWHInvGatepassEntryItem ObjNRGPItem=(clsWHInvGatepassEntryItem) colLineItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("GATEPASS_NO",nNRGPno);
                rsTmp.updateLong("SR_NO",i);
                
                rsTmp.updateString("WH_INVOICE_NO",(String) ObjNRGPItem.getAttribute("WH_INVOICE_NO").getObj());
                rsTmp.updateString("WH_INVOICE_DATE",(String) ObjNRGPItem.getAttribute("WH_INVOICE_DATE").getObj());
                rsTmp.updateString("WH_PARTY_CODE",(String) ObjNRGPItem.getAttribute("WH_PARTY_CODE").getObj());
                rsTmp.updateString("WH_PARTY_NAME",(String) ObjNRGPItem.getAttribute("WH_PARTY_NAME").getObj());
                rsTmp.updateString("WH_BALE_NO",(String) ObjNRGPItem.getAttribute("WH_BALE_NO").getObj());
                rsTmp.updateString("WH_BALE_DATE",(String) ObjNRGPItem.getAttribute("WH_BALE_DATE").getObj());
                rsTmp.updateString("WH_DISPATCH_STATION",(String) ObjNRGPItem.getAttribute("WH_DISPATCH_STATION").getObj());
                rsTmp.updateString("WH_ACTUAL_TRANSPORTER",(String) ObjNRGPItem.getAttribute("WH_ACTUAL_TRANSPORTER").getObj());
                rsTmp.updateString("WH_TYPE_OF_PACKING",(String) ObjNRGPItem.getAttribute("WH_TYPE_OF_PACKING").getObj());
                
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("GATEPASS_NO",nNRGPno);
                rsHDetail.updateLong("SR_NO",i);
                
                rsHDetail.updateString("WH_INVOICE_NO",(String) ObjNRGPItem.getAttribute("WH_INVOICE_NO").getObj());
                rsHDetail.updateString("WH_INVOICE_DATE",(String) ObjNRGPItem.getAttribute("WH_INVOICE_DATE").getObj());
                rsHDetail.updateString("WH_PARTY_CODE",(String) ObjNRGPItem.getAttribute("WH_PARTY_CODE").getObj());
                rsHDetail.updateString("WH_PARTY_NAME",(String) ObjNRGPItem.getAttribute("WH_PARTY_NAME").getObj());
                rsHDetail.updateString("WH_BALE_NO",(String) ObjNRGPItem.getAttribute("WH_BALE_NO").getObj());
                rsHDetail.updateString("WH_BALE_DATE",(String) ObjNRGPItem.getAttribute("WH_BALE_DATE").getObj());
                rsHDetail.updateString("WH_DISPATCH_STATION",(String) ObjNRGPItem.getAttribute("WH_DISPATCH_STATION").getObj());
                rsHDetail.updateString("WH_ACTUAL_TRANSPORTER",(String) ObjNRGPItem.getAttribute("WH_ACTUAL_TRANSPORTER").getObj());
                rsHDetail.updateString("WH_TYPE_OF_PACKING",(String) ObjNRGPItem.getAttribute("WH_TYPE_OF_PACKING").getObj());
                
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            ObjFlow.ModuleID = 803;
            ObjFlow.DocNo = (String) getAttribute("GATEPASS_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("GATEPASS_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_INV_NRGP_HEADER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REMARKS").getObj();
            ObjFlow.FieldName = "GATEPASS_NO";

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_NRGP_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
//                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=803 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
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
            setData();
            // updataion process is over
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    

    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    
    public boolean CanDelete(int pCompanyID,String pGatepassno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_NO='"+pGatepassno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=803 AND DOC_NO='"+pGatepassno+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    
    public boolean IsEditable(int pCompanyID,String pGatepassno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
                
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_NO='"+pGatepassno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=803 AND DOC_NO='"+pGatepassno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            int lCompanyID=(int) getAttribute("COMPANY_ID").getVal();
            String lGatepassno=(String) getAttribute("GATEPASS_NO").getObj();
            
            if(CanDelete(lCompanyID,lGatepassno,pUserID)) {
                String strQry = "DELETE FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
                data.Execute(strQry);
                
                strQry = "DELETE FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
                data.Execute(strQry);
            }
            
            rsNRGP.refreshRow();
            return MoveLast();
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GATEPASS_TYPE='FGP' AND GATEPASS_NO='"+pDocNo+"'";
        clsWHInvGatepassEntry ObjNRGP = new clsWHInvGatepassEntry();
        ObjNRGP.Filter(strCondition,pCompanyID);
        return ObjNRGP;
    }

    public boolean Filter(String pCondition, long pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_INV_NRGP_HEADER " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsNRGP = Stmt.executeQuery(strSql);

            if (!rsNRGP.first()) {
//                strSql = "SELECT * FROM PRODUCTION.GIDC_FELT_NRGP_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GATEPASS_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GATEPASS_NO";
//                rsNRGP=Stmt.executeQuery(strSql);
//                Ready=true;
//                MoveLast();
                JOptionPane.showMessageDialog(null, "No Record found.");
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    private boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsNRGP.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsNRGP.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsNRGP.getLong("COMPANY_ID"));
            setAttribute("GATEPASS_NO",rsNRGP.getString("GATEPASS_NO"));
            setAttribute("GATEPASS_DATE",rsNRGP.getString("GATEPASS_DATE"));
            setAttribute("GATEPASS_TYPE",rsNRGP.getString("GATEPASS_TYPE"));
            
            setAttribute("WH_MODE_OF_TRANSPORT",rsNRGP.getString("WH_MODE_OF_TRANSPORT"));
            setAttribute("WH_CARTING_AGENT",rsNRGP.getString("WH_CARTING_AGENT"));
            setAttribute("WH_TRANSPORTER",rsNRGP.getString("WH_TRANSPORTER"));
            setAttribute("WH_VEHICLE_NO",rsNRGP.getString("WH_VEHICLE_NO"));
            setAttribute("WH_OUTWARD_NO",rsNRGP.getString("WH_OUTWARD_NO"));
            setAttribute("WH_OUTWARD_DATE",rsNRGP.getString("WH_OUTWARD_DATE"));
            
            setAttribute("APPROVED",rsNRGP.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsNRGP.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsNRGP.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsNRGP.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsNRGP.getString("REJECTED_REMARKS"));
            setAttribute("REMARKS",rsNRGP.getString("REMARKS"));
            setAttribute("CANCELED",rsNRGP.getInt("CANCELED"));
            setAttribute("CREATED_BY",rsNRGP.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsNRGP.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsNRGP.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsNRGP.getString("MODIFIED_DATE"));
            setAttribute("HIERARCHY_ID",rsNRGP.getInt("HIERARCHY_ID"));
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            String mCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mGatepassno=(String) getAttribute("GATEPASS_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' ORDER BY SR_NO");
            }
            Counter=0;
            rsTmp.first();
            while(! rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsWHInvGatepassEntryItem ObjNRGPItem=new clsWHInvGatepassEntryItem();
                
                ObjNRGPItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjNRGPItem.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjNRGPItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                
                ObjNRGPItem.setAttribute("WH_INVOICE_NO",rsTmp.getString("WH_INVOICE_NO"));
                ObjNRGPItem.setAttribute("WH_INVOICE_DATE",rsTmp.getString("WH_INVOICE_DATE"));
                ObjNRGPItem.setAttribute("WH_PARTY_CODE",rsTmp.getString("WH_PARTY_CODE"));
                ObjNRGPItem.setAttribute("WH_PARTY_NAME",rsTmp.getString("WH_PARTY_NAME"));
                ObjNRGPItem.setAttribute("WH_BALE_NO",rsTmp.getString("WH_BALE_NO"));
                ObjNRGPItem.setAttribute("WH_BALE_DATE",rsTmp.getString("WH_BALE_DATE"));
                ObjNRGPItem.setAttribute("WH_DISPATCH_STATION",rsTmp.getString("WH_DISPATCH_STATION"));
                ObjNRGPItem.setAttribute("WH_ACTUAL_TRANSPORTER",rsTmp.getString("WH_ACTUAL_TRANSPORTER"));
                ObjNRGPItem.setAttribute("WH_TYPE_OF_PACKING",rsTmp.getString("WH_TYPE_OF_PACKING"));
                
                ObjNRGPItem.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjNRGPItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjNRGPItem.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjNRGPItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                colLineItems.put(Long.toString(Counter),ObjNRGPItem);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE,RECEIVED_DATE FROM D_INV_NRGP_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=803 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE,RECEIVED_DATE FROM D_INV_NRGP_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=803 ORDER BY D_INV_NRGP_HEADER.GATEPASS_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE,RECEIVED_DATE FROM D_INV_NRGP_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=803 ORDER BY D_INV_NRGP_HEADER.GATEPASS_NO";
            }
            
            //strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE FROM D_INV_NRGP_HEADER,D_COM_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=11";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("GATEPASS_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsWHInvGatepassEntry ObjDoc=new clsWHInvGatepassEntry();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjDoc.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("DEPT_ID",0);
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjDoc);
                }
                
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
    
    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsNRGP=Stmt.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsWHInvGatepassEntry ObjNRGP=new clsWHInvGatepassEntry();
                
                ObjNRGP.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjNRGP.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
                ObjNRGP.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjNRGP.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjNRGP.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjNRGP.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjNRGP.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjNRGP);
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
    

    public static String getDocStatus(int pCompanyID, String pDocNo) {
        ResultSet rsTmp;
        String strMessage = "";

        try {
            //First check that Document exist
            rsTmp = data.getResult("SELECT GATEPASS_NO,APPROVED,CANCELED FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                if (rsTmp.getBoolean("APPROVED")) {
                    if (rsTmp.getBoolean("CANCELED")) {
                        strMessage = "Document is cancelled";
                    } else {
                        strMessage = "";
                    }
                } else {
                    strMessage = "Document is created but is under approval";
                }

            } else {
                strMessage = "Document does not exist";
            }
            rsTmp.close();
        } catch (Exception e) {
        }

        return strMessage;

    }

    public static boolean IsNRGPExist(int pCompanyID, String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                isExist = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return isExist;

        } catch (Exception e) {
            return isExist;
        }
    }

    public static boolean CanCancel(int pCompanyID, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp=data.getResult("SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"' AND APPROVED=0 AND CANCELED=0");            
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }

            rsTmp.close();
        } catch (Exception e) {

        }

        return canCancel;

    }
    
    public static boolean CancelNRGP(int pCompanyID, String pDocNo) {

        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;

        try {
            Connection tmpConn;
            Statement stSTM;

            if (CanCancel(pCompanyID, pDocNo)) {
                boolean ApprovedDoc = false;

                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    //Remove it from Approval Hierarchy
                    //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pDocNo + "' AND MODULE_ID=793");
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=803");
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_NRGP_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }
    
}
