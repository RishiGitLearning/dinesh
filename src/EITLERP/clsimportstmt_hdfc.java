package EITLERP;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.text.DecimalFormat;

public class clsimportstmt_hdfc {

    public String LastError = "";
    private ResultSet rsResultSet, rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 758; //
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
    public clsimportstmt_hdfc() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("STATEMENT_ID", new Variant(""));
        props.put("BOOK_DATE", new Variant(""));
        props.put("DESCRIPTION", new Variant(""));
        props.put("LEDGER_BALANCE", new Variant(0));
        props.put("CREDIT", new Variant(0));
        props.put("DEBIT", new Variant(0));
        props.put("VALUE_DATE", new Variant(""));
        props.put("REFERANCE_NO", new Variant(""));
        props.put("TRANSACTION_BRANCH", new Variant(""));
        props.put("BOOK_CODE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CREATED_BY", new Variant(0));
        props.put("APPROVED", new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("PREFIX", new Variant(""));
        props.put("APPEND_DATE", new Variant(""));

        //Create a new object for MR Item collection
        colMRItems = new HashMap();

        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REASON", new Variant(""));
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM BANK_RECO.BANK_STATEMENT WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + " 00:00:00" + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + " 23:59:59' GROUP BY STATEMENT_ID");
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

            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date AppendDate = java.sql.Date.valueOf(getAttribute("CREATED_DATE").getObj().toString().substring(0, 10));

            if ((AppendDate.after(FinFromDate) || AppendDate.compareTo(FinFromDate) == 0) && (AppendDate.before(FinToDate) || AppendDate.compareTo(FinToDate) == 0)) {
                //Within the year
            } else {
                LastError = "Bank Statement date is not within financial year.";
                return false;
            }
            //=====================================================//
            Conn = data.getConn();
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //rsHistory = stHistory.executeQuery("SELECT * FROM RETAIL_SHOP.IMPORT_RATE_MASTER_H WHERE FILE_NAME='1'"); // '1' for restricting all data retrieval
            //rsHistory.first();
            clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 758, (int) getAttribute("FFNO").getVal(), true);
            //------------------------------------//
            rsResultSet.first();
            //rsResultSet.moveToInsertRow();
            String mbookcd="";
            Conn.setAutoCommit(false);
            String query = "INSERT INTO BANK_RECO.BANK_STATEMENT(COMPANY_ID,STATEMENT_ID,BOOK_DATE,DESCRIPTION,LEDGER_BALANCE,CREDIT,DEBIT,VALUE_DATE,REFERANCE_NO,TRANSACTION_BRANCH,REMARKS,HIERARCHY_ID,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REASON,CANCELLED,BOOK_CODE) ";
            query = query + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String hquery = "INSERT INTO BANK_RECO.BANK_STATEMENT_H(COMPANY_ID,REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,STATEMENT_ID,BOOK_DATE,DESCRIPTION,LEDGER_BALANCE,CREDIT,DEBIT,VALUE_DATE,REFERANCE_NO,TRANSACTION_BRANCH,REMARKS,HIERARCHY_ID,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REASON,CANCELLED,BOOK_CODE) ";
            hquery = hquery + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement p = Conn.prepareStatement(query);
            PreparedStatement hp = Conn.prepareStatement(hquery);

            for (int i = 1; i <= colMRItems.size(); i++) {
                clsimportstmt_hdfcitem ObjMRItems = (clsimportstmt_hdfcitem) colMRItems.get(Integer.toString(i));
                p.setInt(1, EITLERPGLOBAL.gCompanyID);
                p.setString(2, ObjMRItems.getAttribute("STATEMENT_ID").getString());
                p.setString(3, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("BOOK_DATE").getString()));
                p.setString(4, ObjMRItems.getAttribute("DESCRIPTION").getString());
                p.setDouble(5, (double) ObjMRItems.getAttribute("LEDGER_BALANCE").getVal());
                p.setDouble(6, (double) ObjMRItems.getAttribute("CREDIT").getVal());
                p.setDouble(7, (double) ObjMRItems.getAttribute("DEBIT").getVal());
                p.setString(8, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("VALUE_DATE").getString()));
                p.setString(9, ObjMRItems.getAttribute("REFERANCE_NO").getString());
                p.setString(10, ObjMRItems.getAttribute("TRANSACTION_BRANCH").getString());
                p.setString(11, null);
                p.setInt(12, (int) getAttribute("HIERARCHY_ID").getVal());
                p.setInt(13, (int) getAttribute("CREATED_BY").getVal());
                p.setString(14, getAttribute("CREATED_DATE").getString());
                p.setString(15, null);
                p.setString(16, null);
                p.setBoolean(17, false);
                p.setString(18, null);
                p.setBoolean(19, false);
                p.setString(20, null);
                p.setBoolean(21, false);
                p.setString(22, null);
                p.setString(23, getAttribute("REJECTED_REASON").getString());
                p.setString(24, null);
                p.setString(25, ObjMRItems.getAttribute("BOOK_CODE").getString());
                mbookcd=ObjMRItems.getAttribute("BOOK_CODE").getString();

                p.addBatch();
                if ((i + 1) % 1000 == 0) {
                    p.executeBatch();
                    Conn.commit();
                }

                //========= Inserting Into History =================//
                hp.setInt(1, EITLERPGLOBAL.gCompanyID);
                hp.setInt(2, 1);
                hp.setInt(3, (int) getAttribute("CREATED_BY").getVal());
                hp.setString(4, EITLERPGLOBAL.getCurrentDateDB());
                hp.setString(5, getAttribute("APPROVAL_STATUS").getString());
                hp.setString(6, getAttribute("APPROVER_REMARKS").getString());
                hp.setString(7, ObjMRItems.getAttribute("STATEMENT_ID").getString());
                hp.setString(8, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("BOOK_DATE").getString()));
                hp.setString(9, ObjMRItems.getAttribute("DESCRIPTION").getString());
                hp.setDouble(10, (double) ObjMRItems.getAttribute("LEDGER_BALANCE").getVal());
                hp.setDouble(11, (double) ObjMRItems.getAttribute("CREDIT").getVal());
                hp.setDouble(12, (double) ObjMRItems.getAttribute("DEBIT").getVal());
                hp.setString(13, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("VALUE_DATE").getString()));
                hp.setString(14, getAttribute("REFERANCE_NO").getString());
                hp.setString(15, getAttribute("TRANSACTION_BRANCH").getString());
                hp.setString(16, null);
                hp.setInt(17, (int) getAttribute("HIERARCHY_ID").getVal());
                hp.setInt(18, EITLERPGLOBAL.gUserID);
                hp.setString(19, getAttribute("CREATED_DATE").getString());
                hp.setString(20, null);
                hp.setString(21, null);
                hp.setBoolean(22, false);
                hp.setString(23, null);
                hp.setBoolean(24, false);
                hp.setString(25, null);
                hp.setBoolean(26, false);
                hp.setString(27, null);
                hp.setString(28, getAttribute("REJECTED_REASON").getString());
                hp.setString(29, null);
                hp.setString(30, ObjMRItems.getAttribute("BOOK_CODE").getString());

                hp.addBatch();
                if ((i + 1) % 1000 == 0) {
                    hp.executeBatch();
                    Conn.commit();
                }
            }

            p.executeBatch();
            Conn.commit();
            hp.executeBatch();
            Conn.commit();
            p.close();
            hp.close();
            Conn.setAutoCommit(true);

            //Get the Maximum Revision No.
            System.out.println(1);

            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            ApprovalFlow ObjFlow = new ApprovalFlow();

            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID = clsimportstmt_hdfc.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("STATEMENT_ID").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName = "BANK_RECO.BANK_STATEMENT";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REASON").getObj();
            ObjFlow.FieldName = "STATEMENT_ID";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }

            //================= Approval Flow Update complete ===================//
            //----------Update to BANK STATEMENT----------------
            if (ObjFlow.Status.equalsIgnoreCase("F")) {
                process(ObjFlow.DocNo,mbookcd);
            }
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
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date AppendDate = java.sql.Date.valueOf(getAttribute("CREATED_DATE").getObj().toString().substring(0, 10));

            if ((AppendDate.after(FinFromDate) || AppendDate.compareTo(FinFromDate) == 0) && (AppendDate.before(FinToDate) || AppendDate.compareTo(FinToDate) == 0)) {
                //Withing the year
            } else {
                LastError = "Bank Statement date is not within financial year.";
                return false;
            }
            //=====================================================//

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //rsHistory = stHistory.executeQuery("SELECT * FROM RETAIL_SHOP.IMPORT_RATE_MASTER_H WHERE FILE_NAME='1'"); // '1' for restricting all data retrieval
            //rsHistory.first();
            //------------------------------------//
            String theDocNo = getAttribute("STATEMENT_ID").getString();
            data.Execute("DELETE FROM BANK_RECO.BANK_STATEMENT WHERE STATEMENT_ID='" + theDocNo + "'");

            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM BANK_RECO.BANK_STATEMENT_H WHERE STATEMENT_ID='" + (String) getAttribute("STATEMENT_ID").getObj() + "'");
            RevNo++;
            String mbookcd="";
            Conn.setAutoCommit(false);
            String query = "INSERT INTO BANK_RECO.BANK_STATEMENT(COMPANY_ID,STATEMENT_ID,BOOK_DATE,DESCRIPTION,LEDGER_BALANCE,CREDIT,DEBIT,VALUE_DATE,REFERANCE_NO,TRANSACTION_BRANCH,REMARKS,HIERARCHY_ID,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REASON,CANCELLED,BOOK_CODE) ";
            query = query + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String hquery = "INSERT INTO BANK_RECO.BANK_STATEMENT_H(COMPANY_ID,REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,STATEMENT_ID,BOOK_DATE,DESCRIPTION,LEDGER_BALANCE,CREDIT,DEBIT,VALUE_DATE,REFERANCE_NO,TRANSACTION_BRANCH,REMARKS,HIERARCHY_ID,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CHANGED,CHANGED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REASON,CANCELLED,BOOK_CODE) ";
            hquery = hquery + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement p = Conn.prepareStatement(query);
            PreparedStatement hp = Conn.prepareStatement(hquery);
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsimportstmt_hdfcitem ObjMRItems = (clsimportstmt_hdfcitem) colMRItems.get(Integer.toString(i));

                p.setInt(1, EITLERPGLOBAL.gCompanyID);
                p.setString(2, ObjMRItems.getAttribute("STATEMENT_ID").getString());
                p.setString(3, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("BOOK_DATE").getString()));
                p.setString(4, ObjMRItems.getAttribute("DESCRIPTION").getString());
                p.setDouble(5, (double) ObjMRItems.getAttribute("LEDGER_BALANCE").getVal());
                p.setDouble(6, (double) ObjMRItems.getAttribute("CREDIT").getVal());
                p.setDouble(7, (double) ObjMRItems.getAttribute("DEBIT").getVal());
                p.setString(8, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("VALUE_DATE").getString()));
                p.setString(9, ObjMRItems.getAttribute("REFERANCE_NO").getString());
                p.setString(10, ObjMRItems.getAttribute("TRANSACTION_BRANCH").getString());
                p.setString(11, null);
                p.setInt(12, (int) getAttribute("HIERARCHY_ID").getVal());
                p.setString(13, null);
                p.setString(14, null);
                p.setInt(15, (int) getAttribute("MODIFIED_BY").getVal());
                p.setString(16, getAttribute("MODIFIED_DATE").getString());
                p.setBoolean(17, false);
                p.setString(18, null);
                p.setBoolean(19, false);
                p.setString(20, null);
                p.setBoolean(21, false);
                p.setString(22, null);
                p.setString(23, getAttribute("REJECTED_REASON").getString());
                p.setString(24, null);
                p.setString(25, ObjMRItems.getAttribute("BOOK_CODE").getString());
                mbookcd=ObjMRItems.getAttribute("BOOK_CODE").getString();

                p.addBatch();
                if ((i + 1) % 1000 == 0) {
                    p.executeBatch();
                    Conn.commit();
                }

                //========= Inserting Into History =================//
                hp.setInt(1, EITLERPGLOBAL.gCompanyID);
                hp.setInt(2, RevNo);
                hp.setInt(3, (int) getAttribute("MODIFIED_BY").getVal());
                hp.setString(4, EITLERPGLOBAL.getCurrentDateDB());
                hp.setString(5, getAttribute("APPROVAL_STATUS").getString());
                hp.setString(6, getAttribute("APPROVER_REMARKS").getString());
                hp.setString(7, ObjMRItems.getAttribute("STATEMENT_ID").getString());
                hp.setString(8, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("BOOK_DATE").getString()));
                hp.setString(9, ObjMRItems.getAttribute("DESCRIPTION").getString());
                hp.setDouble(10, (double) ObjMRItems.getAttribute("LEDGER_BALANCE").getVal());
                hp.setDouble(11, (double) ObjMRItems.getAttribute("CREDIT").getVal());
                hp.setDouble(12, (double) ObjMRItems.getAttribute("DEBIT").getVal());
                hp.setString(13, EITLERPGLOBAL.formatDateDB(ObjMRItems.getAttribute("VALUE_DATE").getString()));
                hp.setString(14, getAttribute("REFERANCE_NO").getString());
                hp.setString(15, getAttribute("TRANSACTION_BRANCH").getString());
                hp.setString(16, null);
                hp.setInt(17, (int) getAttribute("HIERARCHY_ID").getVal());
                hp.setString(18, null);
                hp.setString(19, null);
                hp.setInt(20, EITLERPGLOBAL.gUserID);
                hp.setString(21, getAttribute("MODIFIED_DATE").getString());
                hp.setBoolean(22, false);
                hp.setString(23, null);
                hp.setBoolean(24, false);
                hp.setString(25, null);
                hp.setBoolean(26, false);
                hp.setString(27, null);
                hp.setString(28, getAttribute("REJECTED_REASON").getString());
                hp.setString(29, null);
                hp.setString(30, ObjMRItems.getAttribute("BOOK_CODE").getString());

                hp.addBatch();
                if ((i + 1) % 1000 == 0) {
                    hp.executeBatch();
                    Conn.commit();
                }
            }
            p.executeBatch();
            Conn.commit();
            hp.executeBatch();
            Conn.commit();
            p.close();
            hp.close();
            Conn.setAutoCommit(true);
            //First remove the old rows
            String mFilenm = (String) getAttribute("STATEMENT_ID").getObj();
            data.Execute("update BANK_RECO.BANK_STATEMENT a,BANK_RECO.BANK_STATEMENT_H b set a.created_by=b.created_by,a.created_date=b.created_date where a.STATEMENT_ID='" + mFilenm + "' and a.STATEMENT_ID=b.STATEMENT_ID and b.revision_no=1");
            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            ApprovalFlow ObjFlow = new ApprovalFlow();

            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID = clsimportstmt_hdfc.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocNo = (String) getAttribute("STATEMENT_ID").getObj();
            ObjFlow.DocDate = (String) getAttribute("CREATED_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "BANK_RECO.BANK_STATEMENT";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REASON").getObj();
            ObjFlow.FieldName = "STATEMENT_ID";
            //String qry = "UPDATE D_COM_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CODE").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsSales_Party.ModuleID;
            //String qry = "UPDATE D_COM_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CD").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsOrderParty.ModuleID;
            //data.Execute(qry);

            //==== Handling Rejected Documents ==========//
            AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");

                //ObjFlow.IsCreator=true;
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==========================================//

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE BANK_RECO.BANK_STATEMENT SET REJECTED=0,CHANGED=1,CHANGED_DATE=CONCAT(CURDATE(),\" \",CURTIME()) WHERE STATEMENT_ID='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID=" + clsimportstmt_hdfc.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
            //--------- Approval Flow Update complete -----------

            //----------Update to BANK STATEMENT----------------
            if (ObjFlow.Status.equalsIgnoreCase("F")) {
                process(ObjFlow.DocNo,mbookcd);

            }
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
        Connection tmpConn = null;
        Statement tmpStmt = null;

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
            setAttribute("STATEMENT_ID", rsResultSet.getString("STATEMENT_ID"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("UPDATED_BY", rsResultSet.getString("CREATED_BY"));
            colMRItems.clear();
            String mFileName = (String) getAttribute("STATEMENT_ID").getObj();
            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM BANK_RECO.BANK_STATEMENT_H WHERE STATEMENT_ID='" + mFileName + "' AND REVISION_NO=" + RevNo);
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM BANK_RECO.BANK_STATEMENT WHERE STATEMENT_ID='" + mFileName + "'");
            }
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsimportstmt_hdfcitem ObjMRItems = new clsimportstmt_hdfcitem();
                ObjMRItems.setAttribute("STATEMENT_ID", rsTmp.getString("STATEMENT_ID"));
                ObjMRItems.setAttribute("BOOK_DATE", rsTmp.getString("BOOK_DATE"));
                ObjMRItems.setAttribute("DESCRIPTION", rsTmp.getString("DESCRIPTION"));
                ObjMRItems.setAttribute("LEDGER_BALANCE", rsTmp.getDouble("LEDGER_BALANCE"));
                ObjMRItems.setAttribute("CREDIT", rsTmp.getDouble("CREDIT"));
                ObjMRItems.setAttribute("DEBIT", rsTmp.getDouble("DEBIT"));
                ObjMRItems.setAttribute("VALUE_DATE", rsTmp.getString("VALUE_DATE"));
                ObjMRItems.setAttribute("REFERANCE_NO", rsTmp.getString("REFERANCE_NO"));
                ObjMRItems.setAttribute("TRANSACTION_BRANCH", rsTmp.getString("TRANSACTION_BRANCH"));
                ObjMRItems.setAttribute("BOOK_CODE", rsTmp.getString("BOOK_CODE"));
                
                ObjMRItems.setAttribute("HIERARCHY_ID", rsTmp.getInt("HIERARCHY_ID"));
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                ObjMRItems.setAttribute("APPROVED", rsTmp.getInt("APPROVED"));
                ObjMRItems.setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                ObjMRItems.setAttribute("REJECTED", rsTmp.getBoolean("REJECTED"));
                ObjMRItems.setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                ObjMRItems.setAttribute("CANCELLED", rsTmp.getInt("CANCELLED"));
                ObjMRItems.setAttribute("REJECTED_REASON", rsTmp.getString("REJECTED_REASON"));

                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();
            }

            String mFilenm = (String) getAttribute("STATEMENT_ID").getObj();

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM BANK_RECO.BANK_STATEMENT WHERE STATEMENT_ID='" + pDocNo + "' AND (APPROVED=1)";
            Conn = data.getConn();
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=758 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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

    public static HashMap getHistoryList(int CompanyID, String FileName) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            String strSQL = "SELECT * FROM BANK_RECO.BANK_STATEMENT_H WHERE STATEMENT_ID='" + FileName + "' GROUP BY STATEMENT_ID,REVISION_NO,APPROVAL_STATUS";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsimportstmt_hdfc objParty = new clsimportstmt_hdfc();

                    objParty.setAttribute("STATEMENT_ID", rsTmp.getString("STATEMENT_ID"));
                    objParty.setAttribute("BOOK_DATE", rsTmp.getString("BOOK_DATE"));
                    objParty.setAttribute("DESCRIPTION", rsTmp.getString("DESCRIPTION"));
                    objParty.setAttribute("LEDGER_BALANCE", rsTmp.getDouble("LEDGER_BALANCE"));
                    objParty.setAttribute("CREDIT", rsTmp.getDouble("CREDIT"));
                    objParty.setAttribute("DEBIT", rsTmp.getDouble("DEBIT"));
                    objParty.setAttribute("VALUE_DATE", rsTmp.getString("VALUE_DATE"));
                    objParty.setAttribute("REFERANCE_NO", rsTmp.getString("REFERANCE_NO"));
                    objParty.setAttribute("TRANSACTION_BRANCH", rsTmp.getString("TRANSACTION_BRANCH"));

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
            strSQL += "SELECT * FROM BANK_RECO.BANK_STATEMENT WHERE " + Condition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM BANK_RECO.BANK_STATEMENT WHERE CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY STATEMENT_ID";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM BANK_RECO.BANK_STATEMENT WHERE STATEMENT_ID='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE MODULE_ID=758 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
            String lDocNo = (String) getAttribute("STATEMENT_ID").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM BANK_RECO.BANK_STATEMENT WHERE STATEMENT_ID='" + lDocNo + "'";
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

    public Object getObject(int CompanyID, String PartyCode) {
        String strCondition = " WHERE STATEMENT_ID='" + PartyCode + "' ";
        //clsSales_Party objParty = new clsSales_Party();
        //objParty.Filter(strCondition);
        //return objParty;
        return null;
    }

    public static HashMap getPendingApprovals(int pCompanyID, int pUserID, int pOrder) {
        String strSQL = "";
        Connection tmpConn = null;
        tmpConn = data.getConn();

        ResultSet rsTmp;
        Statement tmpStmt = null;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT BANK_RECO.BANK_STATEMENT.STATEMENT_ID,BANK_RECO.BANK_STATEMENT.CREATED_DATE,RECEIVED_DATE,DEPT_ID FROM BANK_RECO.BANK_STATEMENT,D_COM_DOC_DATA,D_COM_USER_MASTER WHERE D_COM_DOC_DATA.DOC_NO=BANK_RECO.BANK_STATEMENT.STATEMENT_ID AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.USER_ID=D_COM_USER_MASTER.USER_ID AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=758 GROUP BY STATEMENT_ID ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT BANK_RECO.BANK_STATEMENT.STATEMENT_ID,BANK_RECO.BANK_STATEMENT.CREATED_DATE,RECEIVED_DATE,DEPT_ID FROM BANK_RECO.BANK_STATEMENT,D_COM_DOC_DATA,D_COM_USER_MASTER WHERE D_COM_DOC_DATA.DOC_NO=BANK_RECO.BANK_STATEMENT.STATEMENT_ID AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.USER_ID=D_COM_USER_MASTER.USER_ID AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=758 GROUP BY STATEMENT_ID ORDER BY BANK_RECO.BANK_STATEMENT.CREATED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT BANK_RECO.BANK_STATEMENT.STATEMENT_ID,BANK_RECO.BANK_STATEMENT.CREATED_DATE,RECEIVED_DATE,DEPT_ID FROM BANK_RECO.BANK_STATEMENT,D_COM_DOC_DATA,D_COM_USER_MASTER WHERE D_COM_DOC_DATA.DOC_NO=BANK_RECO.BANK_STATEMENT.STATEMENT_ID AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.USER_ID=D_COM_USER_MASTER.USER_ID AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=758 GROUP BY STATEMENT_ID ORDER BY BANK_RECO.BANK_STATEMENT.STATEMENT_ID";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsimportstmt_hdfc ObjItem = new clsimportstmt_hdfc();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("STATEMENT_ID", rsTmp.getString("STATEMENT_ID"));
                ObjItem.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
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

    public boolean ShowHistory(int pCompanyID, String pFileName) {
        Ready = false;
        try {
            //Conn=data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM BANK_RECO.BANK_STATEMENT_H WHERE STATEMENT_ID='" + pFileName + "' ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static String getStatus(String pFileNm) {
        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp;
        String Status = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String strSQL = "select approval_status from BANK_RECO.BANK_STATEMENT_H  where STATEMENT_ID='" + pFileNm + "' and revision_no =(select max(revision_no) from BANK_RECO.BANK_STATEMENT_H where STATEMENT_ID='" + pFileNm + "')";
            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Status = rsTmp.getString("approval_status");
            }
            tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return Status;
        } catch (Exception e) {
            return "";
        }
    }

    private void process(String DocNo,String Bookcd) {
        try {
            int mmnth, myr;
            String sql;
            sql = "SELECT DISTINCT EXTRACT(MONTH FROM BOOK_DATE) FROM BANK_RECO.BANK_STATEMENT WHERE STATEMENT_ID='" + DocNo + "'";
            mmnth = data.getIntValueFromDB(sql);
            sql = "SELECT DISTINCT EXTRACT(YEAR FROM BOOK_DATE) FROM BANK_RECO.BANK_STATEMENT WHERE STATEMENT_ID='" + DocNo + "'";
            myr = data.getIntValueFromDB(sql);
            //1st.. Process
            String mcurdttm,mbanknm="";
            mcurdttm = data.getStringValueFromDB("SELECT NOW() FROM DUAL");
            mbanknm=bank_name(Bookcd);
            sql = "INSERT INTO BANK_RECO.D_BANK_STMT "
                    + "SELECT BOOK_DATE,'"+mbanknm+"',TRANSACTION_BRANCH,DESCRIPTION,REFERANCE_NO,VALUE_DATE,CREDIT,DEBIT,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"
                    + "'" + DocNo + "',null,0 "
                    + "FROM BANK_RECO.BANK_STATEMENT "
                    + "WHERE STATEMENT_ID='" + DocNo + "'";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("1st Process Complete");
            //2nd... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT  SET STMT_INST_NO =  RIGHT(TRIM(STMT_REF_NO),11) "
                    + "WHERE STMT_DESCRIPTION NOT IN ('CMS- CREDITORS','CMS- DEBITORS') AND STMT_PARTY_CODE =0";

            data.Execute(sql);
            System.out.println(sql);
            System.out.println("2nd Process Complete");
            //3rd... Process
            sql = "INSERT INTO BANK_RECO.BANK_VOUCHER_DETAIL  "
                    + "SELECT B.VOUCHER_NO,B.VOUCHER_DATE,CHEQUE_NO,CHEQUE_DATE,AVG(CHEQUE_AMOUNT),"
                    + "SUM(CASE WHEN EFFECT ='C'  THEN AMOUNT END),COUNT(CASE WHEN EFFECT ='C'  THEN AMOUNT END),"
                    + "SUM(CASE WHEN EFFECT ='D'  THEN AMOUNT END),COUNT(CASE WHEN EFFECT ='D'  THEN AMOUNT END),"
                    + "COALESCE(AVG(CASE WHEN SUB_ACCOUNT_CODE !='' THEN MAIN_ACCOUNT_CODE END),0), 0,"
                    + "COALESCE(AVG(CASE WHEN SUB_ACCOUNT_CODE !='' THEN SUB_ACCOUNT_CODE END),0), B.BOOK_CODE,B.REMARKS,0,"
                    + "'0000-00-00','',B.CUSTOMER_BANK,B.BANK_NAME "
                    + "FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B  "
                    + "WHERE A.VOUCHER_NO = B.VOUCHER_NO  AND BOOK_CODE = "+Bookcd+" AND VOUCHER_DATE >='2013-04-01' AND "
                    + "A.VOUCHER_NO NOT IN (SELECT VOUCHER_NO FROM BANK_VOUCHER_DETAIL ) AND B.APPROVED =1 AND B.CANCELLED =0 "
                    + "GROUP BY  B.VOUCHER_NO,B.VOUCHER_DATE";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("3rd Process Complete");
            //4th... Process
            sql = "UPDATE BANK_RECO.BANK_VOUCHER_DETAIL SET BANK_AMOUNT = CHEQUE_AMOUNT "
                    + "WHERE UP_FLAG =0";
            System.out.println("4th Process Complete");
            System.out.println(sql);
            data.Execute(sql);
            //5th... Process
            sql = "UPDATE BANK_RECO.BANK_VOUCHER_DETAIL SET BANK_AMOUNT = VOUCHER_CR_AMOUNT "
                    + "WHERE CHEQUE_AMOUNT =0  AND UP_FLAG =0";
            System.out.println("5th Process Complete");
            System.out.println(sql);
            data.Execute(sql);

            //6th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
                    + "STMT_PARTY_CODE =1,UP_FLAG =1,,STMT_DATE_UP =STMT_DATE,UPDATE_DATE='"+mcurdttm+"' "
                    + "WHERE STMT_INST_NO+0 = CHEQUE_NO+0 AND BOOK_CODE = "+Bookcd+" AND "
                    + "BANK_AMOUNT = (STMT_CREDITS+STMT_DEBITS) AND STMT_INST_NO != ''  AND CHEQUE_NO !='' AND "
                    + "STMT_DATE_UP = '0000-00-00' AND UP_FLAG ='0' AND STMT_PARTY_CODE ='0' ";

            data.Execute(sql);
            System.out.println(sql);
            System.out.println("6th Process Complete");
            //7th... Process
//            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
//                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),"
//                    + "STMT_VOUCHER_DATE= VOUCHER_DATE,STMT_PARTY_CODE =1,UP_FLAG =1,STMT_DATE_UP =STMT_DATE "
//                    + "WHERE STMT_INST_NO+0 = CHEQUE_NO+0 AND BOOK_CODE = 58  AND "
//                    + "BANK_AMOUNT = (STMT_CREDITS+STMT_DEBITS) AND STMT_INST_NO != ''  AND CHEQUE_NO !='' AND "
//                    + "STMT_DATE_UP = '0000-00-00' AND UP_FLAG ='0' AND STMT_PARTY_CODE ='0'";
//            data.Execute(sql);
//            System.out.println(sql);
//            System.out.println("7th Process Complete");
            //8th... Process
//            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
//                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
//                    + "STMT_PARTY_CODE =7,UP_FLAG =7,STMT_DATE_UP =STMT_DATE "
//                    + "WHERE STMT_INST_NO+0 = CHEQUE_NO+0 AND BOOK_CODE = 58  AND "
//                    + "BANK_AMOUNT != (STMT_CREDITS+STMT_DEBITS) AND STMT_INST_NO != ''  AND CHEQUE_NO !='' AND "
//                    + "STMT_DATE_UP = '0000-00-00' AND UP_FLAG ='0' AND STMT_PARTY_CODE ='0'";
//            data.Execute(sql);
//            System.out.println(sql);
//            System.out.println("8th Process Complete");
            //9th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
                    + "STMT_PARTY_CODE =2,UP_FLAG =2,STMT_DATE_UP = STMT_DATE,UPDATE_DATE='"+mcurdttm+"' "
                    + "WHERE BOOK_CODE = "+Bookcd+"  AND BANK_AMOUNT = (STMT_CREDITS+STMT_DEBITS) AND "
                    + "CHEQUE_DATE = STMT_VALUE_DATE AND UP_FLAG ='0' AND CHEQUE_NO ='' AND STMT_PARTY_CODE ='0'";

            data.Execute(sql);
            System.out.println(sql);
            System.out.println("9th Process Complete");
            //10th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
                    + "STMT_PARTY_CODE =2,UP_FLAG =2,STMT_DATE_UP = STMT_DATE,UPDATE_DATE='"+mcurdttm+"' "
                    + "WHERE BOOK_CODE = "+Bookcd+" AND BANK_AMOUNT = (STMT_CREDITS+STMT_DEBITS) AND VOUCHER_DATE = STMT_VALUE_DATE AND "
                    + "STMT_PARTY_CODE !=1 AND UP_FLAG !=1 ";

            data.Execute(sql);
            System.out.println(sql);
            System.out.println("10th Process Complete");
            //11th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
                    + "STMT_PARTY_CODE =1,UP_FLAG =1,STMT_DATE_UP =STMT_DATE,UPDATE_DATE='"+mcurdttm+"' "
                    + "WHERE STMT_INST_NO+0 = CHEQUE_NO+0 AND BOOK_CODE = "+Bookcd+" AND BANK_AMOUNT = (STMT_CREDITS+STMT_DEBITS) AND "
                    + "STMT_INST_NO != ''  AND CHEQUE_NO !=''  AND "
                    + "SUBSTRING(VOUCHER_NO,1,2) ='PY'";

            data.Execute(sql);
            System.out.println(sql);
            System.out.println("11th Process Complete");
            //12nd... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_EXTRA  "
                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
                    + "STMT_PARTY_CODE =3,UP_FLAG =3,STMT_DEPO_AMOUNT = (VOUCHER_CR_AMOUNT+VOUCHER_DR_AMOUNT),UPDATE_DATE='"+mcurdttm+"' "
                    + "WHERE BOOK_CODE = "+Bookcd+" AND BANK_RECO.D_BANK_STMT.AUTOINC=BANK_RECO.BANK_VOUCHER_EXTRA AND "
                    + "STMT_PARTY_CODE NOT IN (1,2,4) AND UP_FLAG NOT IN (1,2,4)";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("12th Process Complete");
            //13th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,"
                    + "(  SELECT STMT_DEPO_SLIP_NO  AS P FROM BANK_RECO.D_BANK_STMT  WHERE STMT_DEPO_SLIP_NO !=0 "
                    + "GROUP BY STMT_VALUE_DATE,STMT_DEPO_SLIP_NO HAVING COUNT(*) =2 ) AS A "
                    + "SET STMT_PARTY_CODE =4,STMT_VOUCHER_NO = 'CANCELLED',STMT_VOUCHER_DATE = STMT_VALUE_DATE "
                    + "WHERE STMT_DEPO_SLIP_NO = P AND STMT_DEPO_AMOUNT =   (STMT_CREDITS+STMT_DEBITS)";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("13th Process Complete");
            //14th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL "
                    + "SET STMT_DATE_UP = STMT_DATE  "
                    + "WHERE BOOK_CODE = "+Bookcd+" AND STMT_VOUCHER_NO = VOUCHER_NO  AND STMT_DATE_UP ='0000-00-00'";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("14th Process Complete");
            //15th... Process
            sql = "UPDATE BANK_RECO.BANK_VOUCHER_DETAIL SET CHEQUE_DATE ='1900-01-01' "
                    + "WHERE CHEQUE_DATE = '0000-00-00' AND UP_FLAG =0";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("15th Process Complete");
            //16th... Process
            sql = "UPDATE BANK_RECO.BANK_VOUCHER_DETAIL SET STMT_DATE_UP = '0000-00-00' "
                    + "WHERE UP_FLAG =0 AND STMT_DATE_UP != '0000-00-00'";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("16th Process Complete");
            //17th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL "
                    + "SET STMT_DATE_UP = STMT_DATE  "
                    + "WHERE BOOK_CODE = "+Bookcd+" AND STMT_VOUCHER_NO = VOUCHER_NO  AND STMT_DATE_UP ='0000-00-00'";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("17th Process Complete");
            //18th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL "
                    + "SET STMT_VOUCHER_DATE = VOUCHER_DATE  "
                    + "WHERE BOOK_CODE = "+Bookcd+" AND STMT_VOUCHER_NO != '0' AND  STMT_VOUCHER_NO = VOUCHER_NO  AND "
                    + "STMT_VOUCHER_DATE ='0000-00-00'";
            data.Execute(sql);
            System.out.println(sql);
            System.out.println("18th Process Complete...");
            //19th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
                    + "STMT_PARTY_CODE =2,UP_FLAG =2,STMT_DATE_UP = STMT_DATE,UPDATE_DATE='"+mcurdttm+"' "
                    + "WHERE BOOK_CODE = "+Bookcd+" AND  BANK_AMOUNT = (STMT_CREDITS+STMT_DEBITS) AND "
                    + " UP_FLAG ='0' AND CHEQUE_NO !='' AND STMT_PARTY_CODE ='0'  AND "
                    + "CHEQUE_NO=STMT_REF_NO";
            data.Execute(sql);
            System.out.println("19th Process Complete...");
            //20th... Process
            sql = "UPDATE BANK_RECO.D_BANK_STMT,BANK_RECO.BANK_VOUCHER_DETAIL  "
                    + "SET STMT_VOUCHER_NO = VOUCHER_NO,STMT_INVOICE_NO = CONCAT_WS(CHEQUE_NO,REMARKS),STMT_VOUCHER_DATE= VOUCHER_DATE,"
                    + "STMT_PARTY_CODE =2,UP_FLAG =2,STMT_DATE_UP = STMT_DATE,UPDATE_DATE='"+mcurdttm+"' "
                    + "WHERE BOOK_CODE = "+Bookcd+" AND  BANK_AMOUNT = (STMT_CREDITS+STMT_DEBITS) AND "
                    + " UP_FLAG ='0' AND CHEQUE_NO !='' AND STMT_PARTY_CODE ='0'  AND "
                    + "(CHEQUE_NO*1)=(STMT_REF_NO*1)";
            data.Execute(sql);
            System.out.println("20th Process Complete...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String bank_name(String Bookcd){
        String bnk="";
        switch(Bookcd){
            case "58":bnk="HDFC";
                    break;
            case "31":bnk="BOB";
                    break;
            case "36":bnk="CBI";
                    break;
            case "33":bnk="IOB";
                    break;
            case "54":bnk="IDBI";
                    break;
            case "37":bnk="SBI";
                    break;
            case "76":bnk="SIB";
                    break;
            case "92":bnk="YES";
                    break;
            case "75":bnk="HDFC ANK";
                    break;
            case "74":bnk="SBI ANK";
                    break;    
        }
        return bnk;
    }
}
