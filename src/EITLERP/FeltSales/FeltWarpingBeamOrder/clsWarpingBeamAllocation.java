/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.FeltSales.FeltWarpingBeamOrder;

import java.util.*;
import java.sql.*;
import EITLERP.*;
import java.text.DecimalFormat;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author
 */
public class clsWarpingBeamAllocation {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;

    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 875; //72
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
    public clsWarpingBeamAllocation() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("REVISION_NO", new Variant(0));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));

        props.put("BA_DOC_NO", new Variant(""));
        props.put("FROM_IP", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));

        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.BEAM_ALLOCATION GROUP BY BA_DOC_NO ORDER BY BA_DOC_NO");
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
        return false;
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
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            Validate = true;

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHDetail = stHDetail.executeQuery("SELECT * FROM PRODUCTION.BEAM_ALLOCATION_H WHERE BA_DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//

            String theDocNo = getAttribute("BA_DOC_NO").getString();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT COALESCE(MAX(REVISION_NO),0) FROM PRODUCTION.BEAM_ALLOCATION_H WHERE BA_DOC_NO='" + (String) getAttribute("BA_DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("BA_DOC_NO").getObj();
            

                String mDocNo = (String) getAttribute("BA_DOC_NO").getObj();

                //data.Execute("DELETE FROM PRODUCTION.BEAM_ALLOCATION WHERE BA_DOC_NO='" + mDocNo + "'");
                Statement tmpStmt;

                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTmp1 = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.BEAM_ALLOCATION WHERE BA_DOC_NO='1'");

                //Now Insert records into detail table
                String sql;
                for (int i = 1; i <= colMRItems.size(); i++) {
                    clsWarpingBeamAllocationItem ObjMRItems = (clsWarpingBeamAllocationItem) colMRItems.get(Integer.toString(i));
                    sql = "UPDATE PRODUCTION.BEAM_ALLOCATION SET BA_PROCESS=" + (String) ObjMRItems.getAttribute("BA_PROCESS").getObj() + ","
                            + "BA_REMARK='" + (String) ObjMRItems.getAttribute("BA_REMARK").getObj() + "',"
                            + "MODIFIED_BY=" + getAttribute("MODIFIED_BY").getInt() + ",MODIFIED_DATE=NOW(),FROM_IP='" + str_split[1] + "' "
                            + " WHERE BA_PIECE_NO='" + (String) ObjMRItems.getAttribute("BA_PIECE_NO").getObj() + "' AND "
                            + "BA_DOC_NO='" + (String) getAttribute("BA_DOC_NO").getObj() + "'";
                    data.Execute(sql);

                    rsHDetail.moveToInsertRow();
                    rsHDetail.updateInt("REVISION_NO", RevNo);
                    rsHDetail.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                    rsHDetail.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                    rsHDetail.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                    rsHDetail.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
                    rsHDetail.updateString("BA_DOC_NO", (String) getAttribute("BA_DOC_NO").getObj());
                    rsHDetail.updateString("BA_PROCESS", (String) ObjMRItems.getAttribute("BA_PROCESS").getObj());
                    rsHDetail.updateString("BA_REMARK", (String) ObjMRItems.getAttribute("BA_REMARK").getObj());
                    rsHDetail.updateString("BA_PIECE_NO", (String) ObjMRItems.getAttribute("BA_PIECE_NO").getObj());
                    rsHDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsHDetail.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
                    rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                    rsHDetail.updateBoolean("CHANGED", true);
                    rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsHDetail.updateString("FROM_IP", "" + str_split[1]);
                    rsHDetail.insertRow();
                }

            

            //========= Inserting Into History =================//
            //First remove the old rows
            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            ObjFlow.ModuleID = clsWarpingBeamAllocation.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("BA_DOC_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.BEAM_ALLOCATION";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "BA_DOC_NO";

            //==== Handling Rejected Documents ==========//
            AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==========================================//

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.BEAM_ALLOCATION SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE BA_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + clsWarpingBeamAllocation.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

                ObjFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
                if (IsRejected) {
                    ObjFlow.Status = "A";
                    ObjFlow.To = ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            
            if (ObjFlow.Status.equals("F")) {
                sql = "UPDATE PRODUCTION.FELT_PROD_DOC_DATA SET STATUS='F' "
                        + "WHERE MODULE_ID=" + clsWarpingBeamAllocation.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'";
                data.Execute(sql);
                
            }
            //--------- Approval Flow Update complete -----------
            //LoadData(EITLERPGLOBAL.gCompanyID);
            return true;

        } catch (Exception e) {
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

            setAttribute("BA_DOC_NO", rsResultSet.getString("BA_DOC_NO"));
            

            setAttribute("HIERARCHY_ID", rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE", rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE", rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED", rsResultSet.getInt("CANCELED"));
            setAttribute("REJECTED_REMARKS", rsResultSet.getString("REJECTED_REMARKS"));

            colMRItems.clear();

            String mDocNo = (String) getAttribute("BA_DOC_NO").getObj();

            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {

                rsTmp = tmpStmt.executeQuery("SELECT BA_DOC_NO,BA_REMARK,BA_PIECE_NO, BA_LOOM_NO, COALESCE(BA_BEAM_TYPE,'') AS BA_BEAM_TYPE, BA_WEAVE_OUT_EXP_DATE, BA_WH_EXP_DATE, BA_PROCESS, "
                        + "BA_PARTY_CODE, BA_MACHINE_NO, BA_POSITION_NO, BA_UPN, BA_STYLE, BA_REED, BA_REED_SPACE, BA_LENGTH, BA_BEAM_DOC_NO, BA_GROUP,"
                        + "CASE WHEN COALESCE(BA_STYLE,'')='' THEN 'Design Master Missing' "
                        + "WHEN COALESCE(BA_LOOM_NO,'')='' THEN 'Loom Allocation Master Missing' "
                        + "WHEN COALESCE(BA_WEAVE_OUT_EXP_DATE,'')='' THEN 'MASTER MISMATCH' ELSE '' END AS SYSRMK,POSITION_DESC,"
                        + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE "
                        + " FROM PRODUCTION.BEAM_ALLOCATION_H "
                        + "LEFT JOIN  (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP  ON BA_POSITION_NO=MP.POSITION_NO"
                        + " WHERE  BA_DOC_NO='" + mDocNo + "' AND REVISION_NO=" + RevNo + " ");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT BA_DOC_NO,BA_REMARK,BA_PIECE_NO, BA_LOOM_NO, COALESCE(BA_BEAM_TYPE,'') AS BA_BEAM_TYPE, BA_WEAVE_OUT_EXP_DATE, BA_WH_EXP_DATE, BA_PROCESS, "
                        + "BA_PARTY_CODE, BA_MACHINE_NO, BA_POSITION_NO, BA_UPN, BA_STYLE, BA_REED, BA_REED_SPACE, BA_LENGTH, BA_BEAM_DOC_NO, BA_GROUP,"
                        + "CASE WHEN COALESCE(BA_STYLE,'')='' THEN 'Design Master Missing' "
                        + "WHEN COALESCE(BA_LOOM_NO,'')='' THEN 'Loom Allocation Master Missing' "
                        + "WHEN COALESCE(BA_WEAVE_OUT_EXP_DATE,'')='' THEN 'MASTER MISMATCH' ELSE '' END AS SYSRMK,POSITION_DESC,"
                        + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE "
                        + " FROM PRODUCTION.BEAM_ALLOCATION "
                        + "LEFT JOIN  (SELECT * FROM PRODUCTION.FELT_MACHINE_POSITION_MST) AS MP  ON BA_POSITION_NO=MP.POSITION_NO"
                        + " WHERE BA_DOC_NO='" + mDocNo + "' ");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsWarpingBeamAllocationItem ObjMRItems = new clsWarpingBeamAllocationItem();
                ObjMRItems.setAttribute("BA_DOC_NO", rsTmp.getString("BA_DOC_NO"));
                ObjMRItems.setAttribute("BA_PIECE_NO", rsTmp.getString("BA_PIECE_NO"));
                ObjMRItems.setAttribute("BA_LOOM_NO", rsTmp.getString("BA_LOOM_NO"));
                ObjMRItems.setAttribute("BA_BEAM_TYPE", rsTmp.getString("BA_BEAM_TYPE"));
                ObjMRItems.setAttribute("SYSRMK", rsTmp.getString("SYSRMK"));
                ObjMRItems.setAttribute("BA_PROCESS", rsTmp.getString("BA_PROCESS"));
                ObjMRItems.setAttribute("BA_REMARK", rsTmp.getString("BA_REMARK"));
                ObjMRItems.setAttribute("BA_WEAVE_OUT_EXP_DATE", rsTmp.getString("BA_WEAVE_OUT_EXP_DATE"));
                ObjMRItems.setAttribute("BA_WH_EXP_DATE", rsTmp.getString("BA_WH_EXP_DATE"));
                ObjMRItems.setAttribute("BA_BEAM_DOC_NO", rsTmp.getString("BA_BEAM_DOC_NO"));
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();
            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean IsEditable(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.BEAM_ALLOCATION WHERE BA_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND BA_DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                return rsTmp.getInt("COUNT") > 0; //Yes document is waiting for this user
                //Document is not editable by this user
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static HashMap getHistoryList(int CompanyID, String DocNo) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT BA_DOC_NO AS DOC_NO,CREATED_DATE,REVISION_NO,UPDATED_BY,APPROVAL_STATUS,DATE_FORMAT(ENTRY_DATE,'%d/%m/%Y %H:%i:%S') AS ENTRY_DATE,REJECTED_REMARKS AS APPROVER_REMARKS,FROM_IP FROM PRODUCTION.BEAM_ALLOCATION_H WHERE BA_DOC_NO='" + DocNo + "'";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsWarpingBeamAllocation objParty = new clsWarpingBeamAllocation();

                    objParty.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                    objParty.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                    objParty.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                    objParty.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                    objParty.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                    objParty.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                    objParty.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                    objParty.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

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

    public boolean Filter(String Condition) {
        Ready = false;
        try {
            String strSQL = "";
            strSQL += "SELECT * FROM PRODUCTION.BEAM_ALLOCATION WHERE " + Condition;
            System.out.println("Filter Query :" + strSQL);
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.BEAM_ALLOCATION GROUP BY BA_DOC_NO ORDER BY BA_DOC_NO";
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

    public boolean CanDelete(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.BEAM_ALLOCATION WHERE BA_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                return rsTmp.getInt("COUNT") > 0; //Yes document is waiting for this user
                //Document is not editable by this user
            }
        } catch (Exception e) {
            return false;
        }

    }

    //Deletes current record
    public boolean Delete(int pUserID) {
        return false;
    }

    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();

        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT PRODUCTION.BEAM_ALLOCATION.BA_DOC_NO AS DOC_NO,PRODUCTION.BEAM_ALLOCATION.CREATED_DATE,RECEIVED_DATE,BA_LOOM_NO FROM PRODUCTION.BEAM_ALLOCATION,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.BEAM_ALLOCATION.BA_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=" + ModuleID + " ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PRODUCTION.BEAM_ALLOCATION.BA_DOC_NO AS DOC_NO,PRODUCTION.BEAM_ALLOCATION.CREATED_DATE,RECEIVED_DATE,BA_LOOM_NO FROM PRODUCTION.BEAM_ALLOCATION,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.BEAM_ALLOCATION.BA_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=" + ModuleID + " ORDER BY PRODUCTION.BEAM_ALLOCATION.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PRODUCTION.BEAM_ALLOCATION.BA_DOC_NO AS DOC_NO,PRODUCTION.BEAM_ALLOCATION.CREATED_DATE,RECEIVED_DATE,BA_LOOM_NO FROM PRODUCTION.BEAM_ALLOCATION,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.BEAM_ALLOCATION.BA_DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=" + ModuleID + " ORDER BY PRODUCTION.BEAM_ALLOCATION.BA_DOC_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsWarpingBeamAllocation ObjItem = new clsWarpingBeamAllocation();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("BA_LOOM_NO", rsTmp.getInt("BA_LOOM_NO"));
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Long.toString(Counter), ObjItem);

                if (!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }

            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
        }

        return List;
    }

    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM PRODUCTION.BEAM_ALLOCATION_H WHERE BA_DOC_NO='" + pDocNo + "' ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
}
