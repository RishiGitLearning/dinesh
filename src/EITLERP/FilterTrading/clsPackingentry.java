    /*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FilterTrading;

import java.util.*;
import java.lang.*;
import java.sql.*;
import EITLERP.*;
import java.text.DecimalFormat;

/**
 *
 * @author ashutosh
 */
public class clsPackingentry {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 748; //72
    public HashMap colMRItems;

    public Variant getAttribute(String PropName) {
        if (!props.containsKey(PropName)) {
            return new Variant("");
        } else {
            return (Variant) props.get(PropName);
        }
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
     * Creates a new instance of clsSales_Party
     */
    public clsPackingentry() {
        LastError = "";
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("PACKING_NOTE_NO", new Variant(""));
        props.put("PACKING_DATE", new Variant(""));
        props.put("WH_CODE", new Variant(""));
        props.put("PARTY_CODE", new Variant(""));
        props.put("STATION", new Variant(""));
        props.put("SALE_NOTE_NO", new Variant(""));
        props.put("MODE_OF_TRANSPORT", new Variant(""));

        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));

        //Create a new object for MR Item collection
        colMRItems = new HashMap();

        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("SEND_DOC_TO", new Variant(0));
        props.put("APPROVER_REMARKS", new Variant(0));
    }

    /**
     * Load Data. This method loads data from database to Business Object*
     */
    public boolean LoadData(long pCompanyID) {
        Ready = false;
        try {

            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC");
            //rsResultSet1=Stmt.executeQuery("SELECT A.*,PRIORITY_DESC FROM (SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC) AS A LEFT JOIN (SELECT PRIORITY_DESC,PRIORITY_ID FROM PRODUCTION.FELT_PRIORITY_MASTER ) AS B ON A.PRIORITY=B.PRIORITY_ID");
            rsResultSet = Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_HEADER WHERE PACKING_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PACKING_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PACKING_NOTE_NO");
            HistoryView = false;
            Ready = true;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        } catch (Exception e) {

        }
    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MoveNext() {
        try {
            if (rsResultSet.isAfterLast() || rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            } else {
                rsResultSet.next();
                if (rsResultSet.isAfterLast()) {
                    rsResultSet.last();
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
            if (rsResultSet.isFirst() || rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            } else {
                rsResultSet.previous();
                if (rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
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
            rsResultSet.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Insert() {

        Statement stHistory, stHeader, stHDetail;
        ResultSet rsHistory, rsHeader, rsHDetail;

        try {

            //=====================================================//
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_HEADER_H WHERE PACKING_NOTE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_DETAIL_H WHERE PACKING_NOTE_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("PACKING_NOTE_NO", "F" + clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 748, (int) getAttribute("FFNO").getVal(), true));
            //-------------------------------------------------

            rsResultSet.first();
            rsResultSet.moveToInsertRow();

            rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("PACKING_NOTE_NO", getAttribute("PACKING_NOTE_NO").getString());
            rsResultSet.updateString("PACKING_DATE", getAttribute("PACKING_DATE").getString());
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("WH_CODE", getAttribute("WH_CODE").getString());

            rsResultSet.updateString("STATION", getAttribute("STATION").getString());
            rsResultSet.updateString("SALE_NOTE_NO", getAttribute("SALE_NOTE_NO").getString());

            rsResultSet.updateString("MODE_OF_TRANSPORT", getAttribute("MODE_OF_TRANSPORT").getString());

            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY", (int) getAttribute("ORDER_BY").getVal());
            rsResultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            //rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            //rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELLED", false);
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");

            rsResultSet.insertRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());

            rsHistory.updateString("PACKING_NOTE_NO", getAttribute("PACKING_NOTE_NO").getString());
            rsHistory.updateString("PACKING_DATE", getAttribute("PACKING_DATE").getString());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("WH_CODE", getAttribute("WH_CODE").getString());

            rsHistory.updateString("STATION", getAttribute("STATION").getString());
            rsHistory.updateString("SALE_NOTE_NO", getAttribute("SALE_NOTE_NO").getString());
            rsHistory.updateString("MODE_OF_TRANSPORT", getAttribute("MODE_OF_TRANSPORT").getString());

            rsHistory.updateString("CREATED_BY", String.valueOf(EITLERPGLOBAL.gNewUserID));
            rsHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            //rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            //rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELLED", false);
            rsHistory.insertRow();

            System.out.println(1);

            ResultSet rsTmp;
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp = tmpStmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_DETAIL WHERE PACKING_NOTE_NO='1'");

            long nCompanyID = (long) getAttribute("COMPANY_ID").getVal();

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsPackingItem ObjMRItems = (clsPackingItem) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();

                rsTmp.updateLong("COMPANY_ID", (long) nCompanyID);
                rsTmp.updateString("PACKING_NOTE_NO", (String) getAttribute("PACKING_NOTE_NO").getObj());
                rsTmp.updateString("PACKING_DATE", (String) getAttribute("PACKING_DATE").getObj());
                rsTmp.updateString("WH_CODE", (String) getAttribute("WH_CODE").getObj());
                rsTmp.updateString("QUALITY_CD", (String) ObjMRItems.getAttribute("QUALITY_CD").getObj());
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());                
                rsTmp.updateDouble("GROSS_METER", (double) ObjMRItems.getAttribute("GROSS_METER").getVal());
                rsTmp.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsTmp.updateDouble("NET_METER", (double) ObjMRItems.getAttribute("NET_METER").getVal());
                rsTmp.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsTmp.updateDouble("SQ_METER", (double) ObjMRItems.getAttribute("SQ_METER").getVal());
                rsTmp.updateDouble("KGS", (double) ObjMRItems.getAttribute("KGS").getVal());
                
                rsTmp.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                //rsTmp.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("PACKING_NOTE_NO", (String) getAttribute("PACKING_NOTE_NO").getObj());
                rsHDetail.updateString("PACKING_DATE", (String) getAttribute("PACKING_DATE").getObj());
                rsHDetail.updateString("WH_CODE", (String) getAttribute("WH_CODE").getObj());
                rsHDetail.updateString("QUALITY_CD", (String) ObjMRItems.getAttribute("QUALITY_CD").getObj());
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());                
                rsHDetail.updateDouble("GROSS_METER", (double) ObjMRItems.getAttribute("GROSS_METER").getVal());
                rsHDetail.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsHDetail.updateDouble("NET_METER", (double) ObjMRItems.getAttribute("NET_METER").getVal());
                rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsHDetail.updateDouble("SQ_METER", (double) ObjMRItems.getAttribute("SQ_METER").getVal());
                rsHDetail.updateDouble("KGS", (double) ObjMRItems.getAttribute("KGS").getVal());

                rsHDetail.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                //rsHDetail.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                //rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                
                rsHDetail.insertRow();
            }

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //clsFilterFabricApprovalFlow ObjFlow=new clsFilterFabricApprovalFlow();
            clsFilterFabricApprovalFlow ObjFlow = new clsFilterFabricApprovalFlow();

            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID = clsPackingentry.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("PACKING_NOTE_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("PACKING_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName = "FILTERFABRIC.FF_TRD_PACKING_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "PACKING_NOTE_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            String sql;
            if(ObjFlow.Status.equals("F")){
                
                sql="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PACKING_DETAIL D"
                        + " SET P.PIECE_STATUS='P' "
                        + " WHERE D.QUALITY_CD=P.QUALITY_CD AND D.PIECE_NO=P.PIECE_NO AND D.PACKING_NOTE_NO='"+ObjFlow.DocNo+"'";
                data.Execute(sql);
            }
            else if(!ObjFlow.Status.equals("R")){
                sql="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PACKING_DETAIL D"
                        + " SET P.PIECE_STATUS='S' "
                        + " WHERE D.QUALITY_CD=P.QUALITY_CD AND D.PIECE_NO=P.PIECE_NO AND D.PACKING_NOTE_NO='"+ObjFlow.DocNo+"'";
                data.Execute(sql);
            }
            else{
                sql="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PACKING_DETAIL D"
                        + " SET P.PIECE_STATUS='S' "
                        + " WHERE D.QUALITY_CD=P.QUALITY_CD AND D.PIECE_NO=P.PIECE_NO AND D.PACKING_NOTE_NO='"+ObjFlow.DocNo+"'";
                data.Execute(sql);
            }

            //================= Approval Flow Update complete ===================//
            MoveLast();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();

            return false;
        }

    }

    public boolean Update() {

        Statement stHistory, stHeader, stHDetail;
        ResultSet rsHistory, rsHeader, rsHDetail;
        boolean Validate = true;

        try {

            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;
            //======= Check the requisition date ============//
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_HEADER_H WHERE PACKING_NOTE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_DETAIL_H WHERE PACKING_NOTE_NO='1'");
            rsHDetail.first();
            
            //------------------------------------//

            String theDocNo = getAttribute("PACKING_NOTE_NO").getString();

            

            rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("PACKING_NOTE_NO", getAttribute("PACKING_NOTE_NO").getString());
            rsResultSet.updateString("PACKING_DATE", getAttribute("PACKING_DATE").getString());
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("WH_CODE", getAttribute("WH_CODE").getString());

            rsResultSet.updateString("STATION", getAttribute("STATION").getString());
            rsResultSet.updateString("SALE_NOTE_NO", getAttribute("SALE_NOTE_NO").getString());

            rsResultSet.updateString("MODE_OF_TRANSPORT", getAttribute("MODE_OF_TRANSPORT").getString());

            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            //rsResultSet.updateInt("CREATED_BY", (int) getAttribute("ORDER_BY").getVal());
            //rsResultSet.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELLED", false);
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");

            rsResultSet.updateRow();

            

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FILTERFABRIC.FF_TRD_PACKING_HEADER_H WHERE PACKING_NOTE_NO='" + (String) getAttribute("PACKING_NOTE_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("PACKING_NOTE_NO").getObj();

            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert                       
            rsHistory.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());

            rsHistory.updateString("PACKING_NOTE_NO", getAttribute("PACKING_NOTE_NO").getString());
            rsHistory.updateString("PACKING_DATE", getAttribute("PACKING_DATE").getString());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("WH_CODE", getAttribute("WH_CODE").getString());

            rsHistory.updateString("STATION", getAttribute("STATION").getString());
            rsHistory.updateString("SALE_NOTE_NO", getAttribute("SALE_NOTE_NO").getString());
            rsHistory.updateString("MODE_OF_TRANSPORT", getAttribute("MODE_OF_TRANSPORT").getString());

            //rsHistory.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            //rsHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",String.valueOf(EITLERPGLOBAL.gNewUserID));
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELLED", false);
            rsHistory.insertRow();

            //First remove the old rows
            String mPackingNo = (String) getAttribute("PACKING_NOTE_NO").getObj();

            data.Execute("DELETE FROM FILTERFABRIC.FF_TRD_PACKING_DETAIL WHERE PACKING_NOTE_NO='" + mPackingNo + "'");

            ResultSet rsTmp;
            Statement tmpStmt;

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            //Now Insert records into detail table
            rsTmp = tmpStmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_DETAIL WHERE PACKING_NOTE_NO='1'");

            long nCompanyID = (long) getAttribute("COMPANY_ID").getVal();

            //Now Insert records into detail table
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsPackingItem ObjMRItems = (clsPackingItem) colMRItems.get(Integer.toString(i));

                rsTmp.moveToInsertRow();

                rsTmp.updateLong("COMPANY_ID", (long) nCompanyID);
                rsTmp.updateString("PACKING_NOTE_NO", (String) getAttribute("PACKING_NOTE_NO").getObj());
                rsTmp.updateString("PACKING_DATE", (String) getAttribute("PACKING_DATE").getObj());
                rsTmp.updateString("WH_CODE", (String) getAttribute("WH_CODE").getObj());
                rsTmp.updateString("QUALITY_CD", (String) ObjMRItems.getAttribute("QUALITY_CD").getObj());
                rsTmp.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());                
                rsTmp.updateDouble("GROSS_METER", (double) ObjMRItems.getAttribute("GROSS_METER").getVal());
                rsTmp.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsTmp.updateDouble("NET_METER", (double) ObjMRItems.getAttribute("NET_METER").getVal());
                rsTmp.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsTmp.updateDouble("SQ_METER", (double) ObjMRItems.getAttribute("SQ_METER").getVal());
                rsTmp.updateDouble("KGS", (double) ObjMRItems.getAttribute("KGS").getVal());
                
                rsTmp.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                //rsTmp.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("PACKING_NOTE_NO", (String) getAttribute("PACKING_NOTE_NO").getObj());
                rsHDetail.updateString("PACKING_DATE", (String) getAttribute("PACKING_DATE").getObj());
                rsHDetail.updateString("WH_CODE", (String) getAttribute("WH_CODE").getObj());
                rsHDetail.updateString("QUALITY_CD", (String) ObjMRItems.getAttribute("QUALITY_CD").getObj());
                rsHDetail.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());                
                rsHDetail.updateDouble("GROSS_METER", (double) ObjMRItems.getAttribute("GROSS_METER").getVal());
                rsHDetail.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG_CD").getObj());
                rsHDetail.updateDouble("NET_METER", (double) ObjMRItems.getAttribute("NET_METER").getVal());
                rsHDetail.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
                rsHDetail.updateDouble("SQ_METER", (double) ObjMRItems.getAttribute("SQ_METER").getVal());
                rsHDetail.updateDouble("KGS", (double) ObjMRItems.getAttribute("KGS").getVal());

                rsHDetail.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                //rsHDetail.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                //rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                
                rsHDetail.insertRow();
            }

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //clsFilterFabricApprovalFlow ObjFlow=new clsFilterFabricApprovalFlow();
            clsFilterFabricApprovalFlow ObjFlow = new clsFilterFabricApprovalFlow();

            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID = clsPackingentry.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("PACKING_NOTE_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("PACKING_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName = "FILTERFABRIC.FF_TRD_PACKING_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "PACKING_NOTE_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            String sql;
            if(ObjFlow.Status.equals("F")){
                
                sql="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PACKING_DETAIL D"
                        + " SET P.PIECE_STATUS='P' "
                        + " WHERE D.QUALITY_CD=P.QUALITY_CD AND D.PIECE_NO=P.PIECE_NO AND D.PACKING_NOTE_NO='"+ObjFlow.DocNo+"'";
                data.Execute(sql);
            }
            else if(!ObjFlow.Status.equals("R")){
                sql="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PACKING_DETAIL D"
                        + " SET P.PIECE_STATUS='S' "
                        + " WHERE D.QUALITY_CD=P.QUALITY_CD AND D.PIECE_NO=P.PIECE_NO AND D.PACKING_NOTE_NO='"+ObjFlow.DocNo+"'";
                data.Execute(sql);
            }
            else{
                sql="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PACKING_DETAIL D"
                        + " SET P.PIECE_STATUS='F' "
                        + " WHERE D.QUALITY_CD=P.QUALITY_CD AND D.PIECE_NO=P.PIECE_NO AND D.PACKING_NOTE_NO='"+ObjFlow.DocNo+"'";
                data.Execute(sql);
            }

            //================= Approval Flow Update complete ===================//
            MoveLast();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        tmpConn = data.getConn();

        long Counter = 0;
        int RevNo = 0;

        try {
            if (HistoryView) {
                RevNo = rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsResultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("COMPANY_ID", rsResultSet.getLong("COMPANY_ID"));
            setAttribute("PACKING_NOTE_NO", rsResultSet.getString("PACKING_NOTE_NO"));
            setAttribute("PACKING_DATE", rsResultSet.getString("PACKING_DATE"));
            setAttribute("PARTY_CODE", rsResultSet.getString("PARTY_CODE"));
            setAttribute("WH_CODE", rsResultSet.getString("WH_CODE"));
            setAttribute("STATION", rsResultSet.getString("STATION"));
            setAttribute("SALE_NOTE_NO", rsResultSet.getString("SALE_NOTE_NO"));
            setAttribute("MODE_OF_TRANSPORT", rsResultSet.getString("MODE_OF_TRANSPORT"));

            

            setAttribute("HIERARCHY_ID", rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE", rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", rsResultSet.getBoolean("REJECTED"));            
            setAttribute("REJECTED_DATE", rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELLED", rsResultSet.getInt("CANCELLED"));
            setAttribute("REJECTED_REMARKS", rsResultSet.getString("REJECTED_REMARKS"));

            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();

            //String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mPackingNo = (String) getAttribute("PACKING_NOTE_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_DETAIL_H WHERE PACKING_NOTE_NO='" + mPackingNo + "' AND REVISION_NO=" + RevNo);
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_DETAIL WHERE PACKING_NOTE_NO='" + mPackingNo +"'");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsPackingItem ObjMRItems = new clsPackingItem();
                ObjMRItems.setAttribute("PACKING_NOTE_NO", rsTmp.getString("PACKING_NOTE_NO"));                
                ObjMRItems.setAttribute("PACKING_DATE", rsTmp.getString("PACKING_DATE"));                
                ObjMRItems.setAttribute("WH_CODE", rsTmp.getString("WH_CODE"));
                ObjMRItems.setAttribute("QUALITY_CD", rsTmp.getString("QUALITY_CD"));
                ObjMRItems.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("FLAG_CD", rsTmp.getString("FLAG_CD"));
                ObjMRItems.setAttribute("GROSS_METER", rsTmp.getDouble("GROSS_METER"));
                ObjMRItems.setAttribute("NET_METER", rsTmp.getDouble("NET_METER"));
                ObjMRItems.setAttribute("WIDTH", rsTmp.getDouble("WIDTH"));
                ObjMRItems.setAttribute("SQ_METER", rsTmp.getDouble("SQ_METER"));
                ObjMRItems.setAttribute("KGS", rsTmp.getDouble("KGS"));
                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_PACKING_HEADER WHERE PACKING_NOTE_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE MODULE_ID=748 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    //Yes document is waiting for this user
                    return true;
                } else {
                    //Document is not editable by this user
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static HashMap getHistoryList(int CompanyID, String PackingNo) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_HEADER_H WHERE PACKING_NOTE_NO='" + PackingNo + "'";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsPackingentry objParty = new clsPackingentry();

                    objParty.setAttribute("PACKING_NOTE_NO", rsTmp.getString("PACKING_NOTE_NO"));
                    objParty.setAttribute("PACKING_DATE", rsTmp.getString("PACKING_DATE"));
                    objParty.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                    objParty.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                    objParty.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                    objParty.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                    objParty.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));

                    List.put(Integer.toString(List.size() + 1), objParty);
                    rsTmp.next();
                }
            }
            rsTmp.close();
            return List;
        } catch (Exception e) {
            return List;
        }
    }

    public boolean Abc(String Condition1) {
        System.out.println(Condition1);
        return true;
    }

    public boolean Filter(String Condition) {
        Ready = false;
        try {
            String strSQL = "";
            strSQL += "SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_HEADER WHERE " + Condition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_HEADER WHERE PACKING_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND PACKING_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PACKING_NOTE_NO";
                rsResultSet = Stmt.executeQuery(strSQL);
                Ready = true;
                MoveLast();
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

    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_PACKING_HEADER WHERE PACKING_NOTE_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE MODULE_ID=748 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    //Yes document is waiting for this user
                    return true;
                } else {
                    //Document is not editable by this user
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

    }

    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID = EITLERPGLOBAL.gCompanyID;
            String lDocNo = (String) getAttribute("PACKING_NOTE_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM FILTERFABRIC.FF_TRD_PACKING_HEADER WHERE PACKING_NOTE_NO='" + lDocNo + "'";
                data.Execute(strQry);
                strQry="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PACKING_DETAIL D" 
                        + " SET P.PIECE_STATUS='F' "
                        + " WHERE D.QUALITY_CD=P.QUALITY_CD AND D.PIECE_NO=P.PIECE_NO AND D.PACKING_NOTE_NO='"+lDocNo+"'";
                data.Execute(strQry);
                
                strQry = "DELETE FROM FILTERFABRIC.FF_TRD_PACKINGE_DETAIL WHERE PACKING_NOTE_NO='" + lDocNo + "'";
                data.Execute(strQry);

                LoadData(lCompanyID);
                return true;
            } else {
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

   

    public static HashMap getPendingApprovals(int pCompanyID, int pUserID, int pOrder) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();

        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO,FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_DATE,RECEIVED_DATE FROM FILTERFABRIC.FF_TRD_PACKING_HEADER,FILTERFABRIC.FF_TRD_DOC_DATA WHERE FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO=FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND MODULE_ID=748 ORDER BY FILTERFABRIC.FF_TRD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO,FILTERFABRIC.FF_TRD_PACKING.PACKING_DATE,RECEIVED_DATE FROM FILTERFABRIC.FF_TRD_PACKING_HEADER,FILTERFABRIC.FF_TRD_DOC_DATA WHERE FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO=FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND MODULE_ID=748 ORDER BY FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO,FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_DATE,RECEIVED_DATE FROM FILTERFABRIC.FF_TRD_PACKING_HEADER,FILTERFABRIC.FF_TRD_DOC_DATA WHERE FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO=FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND MODULE_ID=748 ORDER BY FILTERFABRIC.FF_TRD_PACKING_HEADER.PACKING_NOTE_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsPackingentry ObjItem = new clsPackingentry();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("PACKING_NOTE_NO", rsTmp.getString("PACKING_NOTE_NO"));
                ObjItem.setAttribute("PACKING_DATE", rsTmp.getString("PACKING_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("DEPT_ID", rsTmp.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//

                //Put the prepared user object into list
                List.put(Long.toString(Counter), ObjItem);

                if (!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While

            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
        }

        return List;
    }

    public boolean ShowHistory(int pCompanyID, String pPackingNo) {
        Ready = false;
        try {
            //Conn=data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PACKING_HEADER_H WHERE PACKING_NOTE_NO='" + pPackingNo + "' ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static String getParyName(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + pPartyID);
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

    public static String getStation(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Station = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Station = rsTmp.getString("DISPATCH_STATION");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return Station;
    }

    public static boolean IsPartyExistEx(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();

                return true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean IsPartyExist(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' AND BLOCKED='N' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();

                return true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getPartyName(int pCompanyID, String pPartyCode, String pURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn(pURL);
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return PartyName;
        } catch (Exception e) {
            return "";
        }
    }


    public static String getPartyName(int pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID=" + pCompanyID + " AND PARTY_CODE='" + pPartyCode + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return PartyName;
        } catch (Exception e) {
            return "";
        }
    }

    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;
        long Counter2 = 0;

        tmpConn = data.getConn();

        try {
            tmpStmt = tmpConn.createStatement();

            rsTmp = tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER " + pCondition);

            Counter = 0;
            while (rsTmp.next()) {
                Counter = Counter + 1;
                clsSales_Party ObjParty = new clsSales_Party();
                ObjParty.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                ObjParty.setAttribute("PARTY_CODE", rsTmp.getString("PARTY_CODE"));
                ObjParty.setAttribute("PARTY_TYPE", rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));

                ObjParty.setAttribute("SEASON_CODE", rsTmp.getString("SEASON_CODE"));
                ObjParty.setAttribute("REG_DATE", rsTmp.getString("REG_DATE"));

                ObjParty.setAttribute("AREA_ID", rsTmp.getString("AREA_ID"));
                ObjParty.setAttribute("ADDRESS1", rsTmp.getString("ADDRESS1"));
                ObjParty.setAttribute("ADDRESS2", rsTmp.getString("ADDRESS2"));
                ObjParty.setAttribute("CITY_ID", rsTmp.getString("CITY_ID"));
                ObjParty.setAttribute("CITY_NAME", rsTmp.getString("CITY_NAME"));
                ObjParty.setAttribute("DISPATCH_STATION", rsTmp.getString("DISPATCH_STATION"));
                ObjParty.setAttribute("DISTRICT", rsTmp.getString("DISTRICT"));
                ObjParty.setAttribute("PINCODE", rsTmp.getString("PINCODE"));
                ObjParty.setAttribute("PHONE_NO", rsTmp.getString("PHONE_NO"));

                ObjParty.setAttribute("REMARK1", rsTmp.getString("REMARK1"));
                ObjParty.setAttribute("REMARK2", rsTmp.getString("REMARK2"));
                ObjParty.setAttribute("REMARK3", rsTmp.getString("REMARK3"));
                ObjParty.setAttribute("REMARK4", rsTmp.getString("REMARK4"));
                ObjParty.setAttribute("REMARK5", rsTmp.getString("REMARK5"));

                ObjParty.setAttribute("MOBILE_NO", rsTmp.getString("MOBILE_NO"));
                ObjParty.setAttribute("EMAIL", rsTmp.getString("EMAIL"));
                ObjParty.setAttribute("URL", rsTmp.getString("URL"));
                ObjParty.setAttribute("CONTACT_PERSON", rsTmp.getString("CONTACT_PERSON"));
                ObjParty.setAttribute("BANK_ID", rsTmp.getLong("BANK_ID"));
                ObjParty.setAttribute("BANK_NAME", rsTmp.getString("BANK_NAME"));
                ObjParty.setAttribute("BANK_ADDRESS", rsTmp.getString("BANK_ADDRESS"));
                ObjParty.setAttribute("BANK_CITY", rsTmp.getString("BANK_CITY"));
                ObjParty.setAttribute("CST_NO", rsTmp.getString("CST_NO"));
                ObjParty.setAttribute("CST_DATE", rsTmp.getString("CST_DATE"));
                ObjParty.setAttribute("ECC_NO", rsTmp.getString("ECC_NO"));
                ObjParty.setAttribute("ECC_DATE", rsTmp.getString("ECC_DATE"));
                ObjParty.setAttribute("TIN_NO", rsTmp.getString("TIN_NO"));
                ObjParty.setAttribute("TIN_DATE", rsTmp.getString("TIN_DATE"));
                ObjParty.setAttribute("PAN_NO", rsTmp.getString("PAN_NO"));
                ObjParty.setAttribute("PAN_DATE", rsTmp.getString("PAN_DATE"));
                ObjParty.setAttribute("MAIN_ACCOUNT_CODE", rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjParty.setAttribute("CHARGE_CODE", rsTmp.getString("CHARGE_CODE"));
                ObjParty.setAttribute("CHARGE_CODE_II", rsTmp.getString("CHARGE_CODE_II"));
                ObjParty.setAttribute("CREDIT_DAYS", rsTmp.getDouble("CREDIT_DAYS"));
                ObjParty.setAttribute("DOCUMENT_THROUGH", rsTmp.getString("DOCUMENT_THROUGH"));
                ObjParty.setAttribute("TRANSPORTER_ID", rsTmp.getLong("TRANSPORTER_ID"));
                ObjParty.setAttribute("TRANSPORTER_NAME", rsTmp.getString("TRANSPORTER_NAME"));
                ObjParty.setAttribute("AMOUNT_LIMIT", rsTmp.getDouble("AMOUNT_LIMIT"));
                ObjParty.setAttribute("OTHER_BANK_ID", rsTmp.getLong("OTHER_BANK_ID"));
                ObjParty.setAttribute("OTHER_BANK_NAME", rsTmp.getString("OTHER_BANK_NAME"));
                ObjParty.setAttribute("OTHER_BANK_ADDRESS", rsTmp.getString("OTHER_BANK_ADDRESS"));
                ObjParty.setAttribute("OTHER_BANK_CITY", rsTmp.getString("OTHER_BANK_CITY"));
                ObjParty.setAttribute("CATEGORY", rsTmp.getString("CATEGORY"));
                ObjParty.setAttribute("INSURANCE_CODE", rsTmp.getString("INSURANCE_CODE"));
                ObjParty.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjParty.setAttribute("DELAY_INTEREST_PER", rsTmp.getDouble("DELAY_INTEREST_PER"));
                ObjParty.setAttribute("ACCOUNT_CODES", rsTmp.getString("ACCOUNT_CODES"));

                ObjParty.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                ObjParty.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjParty.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                ObjParty.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                ObjParty.setAttribute("HIERARCHY_ID", rsTmp.getLong("HIERARCHY_ID"));

            }//Outer while

            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();

        } catch (Exception e) {
            //LastError = e.getMessage();
        }

        return List;
    }

    public static Object getObjectEx(int pCompanyID, String pPartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode + "' ";
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }

    public static Object getObjectExN(int pCompanyID, String pPartyCode, String MainCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode + "' AND MAIN_ACCOUNT_CODE=" + MainCode;
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }
public static String[] getPiecedetail(String pKey) {
        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp;
        String[] Piecedetail = new String[10];
        String[] error = {"error"};
        try {

            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //String strSQL="SELECT * FROM RETAIL_SHOP.ITEM_STOCK_MASTER WHERE CONCAT(QUALITY_CD,SHADE_CD,PIECE_NO,EXT,INVOICE_NO,SIZE)='"+pKey+"'";
            String strSQL = "SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE CONCAT(QUALITY_CD,PIECE_NO)='" + pKey + "'";

            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Piecedetail[0] = rsTmp.getString("QUALITY_CD");
                Piecedetail[1] = rsTmp.getString("PIECE_NO");
                Piecedetail[2] = String.valueOf(rsTmp.getDouble("GROSS_METER"));
                Piecedetail[3] = rsTmp.getString("FLAG_CD");
                Piecedetail[4] = String.valueOf(rsTmp.getString("NET_METER"));
                Piecedetail[5] = String.valueOf(rsTmp.getString("WIDTH"));
                Piecedetail[6] = String.valueOf(rsTmp.getString("SQ_METER"));
                Piecedetail[7] = String.valueOf(rsTmp.getString("KGS"));
            }
            //tmpConn.close();
            //stTmp.close();
            //rsTmp.close();
            return Piecedetail;

        } catch (Exception e) {
            e.printStackTrace();
            return error;
        } finally {
            try {
                //stTmp.close();
                //tmpConn.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }
}
