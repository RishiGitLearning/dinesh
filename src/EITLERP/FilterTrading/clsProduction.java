/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */
package EITLERP.FilterTrading;

import EITLERP.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author nrpithva
 * @version
 */
public class clsProduction {

    public String LastError = "";
    private ResultSet rsData;
    private Connection Conn;
    private Statement Stmt;

    private HashMap props;
    public HashMap colMRItems;
    public boolean Ready = false;

    //History Related properties
    private boolean HistoryView = false;

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
     * Creates new clsBusinessObject
     */
    public clsProduction() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("PRODUCTION_NO", new Variant(""));
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));
        props.put("TYPE",new Variant(""));

        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("REVISION_NO", new Variant(0));
        props.put("CANCELLED", new Variant(false));

        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("FROM_REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("SEND_DOC_TO", new Variant(0));

        colMRItems = new HashMap();
    }

    public boolean LoadData(int pCompanyID) {
        HistoryView = false;
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsData = Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE COMPANY_ID=" + pCompanyID + " ORDER BY CREATED_DATE,SR_NO");
            Ready = true;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean LoadData(int pCompanyID, int pModuleID) {
        HistoryView = false;
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsData = Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE COMPANY_ID=" + pCompanyID + " AND MODULE_ID=" + pModuleID + " ORDER BY CREATED_DATE,SR_NO");
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
            //Stmt.close();
            //rsData.close();

        } catch (Exception e) {

        }

    }

    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsData = Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER_H WHERE COMPANY_ID=" + pCompanyID + " AND PRODUCTION_NO='" + pDocNo + "'");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getHistoryList(int pCompanyID, String pDocNo) {
        HashMap List = new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT DISTINCT PRODUCTION_NO,UPDATED_BY,REVISION_NO,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PRODUCTION_NO='" + pDocNo + "'");

            while (rsTmp.next()) {
                clsProduction ObjDoc = new clsProduction();

                //ObjDoc.setAttribute("PRODUCTION_NO", rsTmp.getString("PRODUCTION_NO"));
                //ObjDoc.setAttribute("PRODUCTION_DATE", rsTmp.getString("PRODUCTION_DATE"));
                //ObjDoc.setAttribute("QUALITY_CD", rsTmp.getString("QUALITY_CD"));
                //ObjDoc.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                //ObjDoc.setAttribute("WIDTH", rsTmp.getDouble("WIDTH"));
                //ObjDoc.setAttribute("METER", rsTmp.getDouble("GROSS_METER"));
                //ObjDoc.setAttribute("KGS", rsTmp.getDouble("KGS"));
                //ObjDoc.setAttribute("FLAG", rsTmp.getString("FLAG_CD"));
                //ObjDoc.setAttribute("SQ_METER", rsTmp.getDouble("SQ_METER"));
                //ObjDoc.setAttribute("NET_METER", rsTmp.getDouble("NET_METER"));
                //ObjDoc.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjDoc.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjDoc.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjDoc.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjDoc.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjDoc.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));

                List.put(Integer.toString(List.size() + 1), ObjDoc);
            }

            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;

        } catch (Exception e) {
            return List;
        }
    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsData.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }

    }

    public boolean MoveNext() {
        String mprd = "";
        try {
            if (rsData.isAfterLast() || rsData.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsData.last();
            } else {
                mprd = rsData.getString("PRODUCTION_NO");

                rsData.next();
                do {
                    if (rsData.getString("PRODUCTION_NO").equals(mprd)) {
                        rsData.next();
                    } else {
                        break;
                    }
                    if (rsData.isAfterLast()) {
                        rsData.last();
                        break;
                    }
                } while (!rsData.isAfterLast());
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean MovePrevious() {
        String mprd = "";
        try {
            if (rsData.isFirst() || rsData.isBeforeFirst()) {
                rsData.first();
            } else {
                mprd = rsData.getString("PRODUCTION_NO");

                rsData.previous();
                do {
                    if (rsData.getString("PRODUCTION_NO").equals(mprd)) {
                        rsData.previous();
                    } else {
                        break;
                    }
                    if (rsData.isBeforeFirst()) {
                        rsData.first();
                        break;
                    }
                } while (!rsData.isBeforeFirst());
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
            rsData.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Insert() {
        Statement stHistory, stTmp;
        ResultSet rsHistory, rsTmp;
        Statement stHeader;
        ResultSet rsHeader;

        try {

            // ---- History Related Changes ------ //
            Connection Conn1 = null;
            Conn1 = data.getConn();
            stHistory = Conn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER_H WHERE PRODUCTION_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();

//            Conn = data.getConn();
//            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            rsData = Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER");
//            
            //------------------------------------//
            clsFilterFabricApprovalFlow ObjFlow = new clsFilterFabricApprovalFlow();
            String sqlStr = "";
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsProductionItem ObjMRItems = (clsProductionItem) colMRItems.get(Integer.toString(i));

                sqlStr = "INSERT INTO FILTERFABRIC.FF_TRD_PIECE_REGISTER ("
                        + "COMPANY_ID,SR_NO,WH_CODE,PRODUCTION_NO,PRODUCTION_DATE,QUALITY_CD,PIECE_NO,WIDTH,GROSS_METER,"
                        + "SQ_METER,NET_METER,KGS,FLAG_CD,PIECE_TYPE,PIECE_STATUS,CREATED_BY,CREATED_DATE,HIERARCHY_ID) "
                        + "VALUES(" + ObjMRItems.getAttribute("COMPANY_ID").getInt() + ","+i+",3"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_NO").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_DATE").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("QUALITY_CD").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PIECE_NO").getString() + "'"
                        + "," + ObjMRItems.getAttribute("WIDTH").getDouble()
                        + "," + ObjMRItems.getAttribute("METER").getDouble()
                        + "," + ObjMRItems.getAttribute("SQ_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("NET_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("KGS").getDouble()
                        + ",'" + ObjMRItems.getAttribute("FLAG").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("TYPE").getString()+"'"
                        + ",'F'," + getAttribute("CREATED_BY").getLong()
                        + ",'" + getAttribute("CREATED_DATE").getString() + "'"
                        + "," + getAttribute("HIERARCHY_ID").getInt()
                        + ")";
                System.out.println(sqlStr);
                data.Execute(sqlStr);

//                rsData.moveToInsertRow();
//                rsData.updateInt("COMPANY_ID", (int) ObjMRItems.getAttribute("COMPANY_ID").getVal());
//                rsData.updateString("PRODUCTION_NO", (String) ObjMRItems.getAttribute("PRODUCTION_NO").getObj());
//                rsData.updateString("PRODUCTION_DATE", (String) ObjMRItems.getAttribute("PRODUCTION_DATE").getObj());
//                rsData.updateString("QUALITY_CD", (String) ObjMRItems.getAttribute("QUALITY_CD").getObj());
//                rsData.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
//                rsData.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
//                rsData.updateDouble("GROSS_METER", (double) ObjMRItems.getAttribute("METER").getVal());
//                rsData.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG").getObj());
//                rsData.updateDouble("KGS", (double) ObjMRItems.getAttribute("KGS").getVal());
//                rsData.updateDouble("SQ_METER", (double) ObjMRItems.getAttribute("SQ_METER").getVal());
//                rsData.updateDouble("NET_METER", (double) ObjMRItems.getAttribute("NET_METER").getVal());
//                rsData.updateString("PIECE_STATUS", "F");
//
//                rsData.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
//                rsData.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsData.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                //rsData.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
//                //rsData.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
//                rsData.updateBoolean("APPROVED", false);
//                rsData.updateString("APPROVED_DATE", "0000-00-00");
//                rsData.updateBoolean("REJECTED", false);
//                rsData.updateString("REJECTED_DATE", "0000-00-00");
//
//                rsData.insertRow();
                //========= Inserting Into History =================//
                sqlStr = "INSERT INTO FILTERFABRIC.FF_TRD_PIECE_REGISTER_H ("
                        + "COMPANY_ID,SR_NO,WH_CODE,PRODUCTION_NO,PRODUCTION_DATE,QUALITY_CD,PIECE_NO,WIDTH,GROSS_METER,"
                        + "SQ_METER,NET_METER,KGS,FLAG_CD,PIECE_TYPE,PIECE_STATUS,CREATED_BY,CREATED_DATE,HIERARCHY_ID,"
                        + "REVISION_NO,UPDATED_BY,APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS) "
                        + "VALUES(" + ObjMRItems.getAttribute("COMPANY_ID").getInt() + ","+i+",3"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_NO").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_DATE").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("QUALITY_CD").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PIECE_NO").getString() + "'"
                        + "," + ObjMRItems.getAttribute("WIDTH").getDouble()
                        + "," + ObjMRItems.getAttribute("METER").getDouble()
                        + "," + ObjMRItems.getAttribute("SQ_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("NET_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("KGS").getDouble()
                        + ",'" + ObjMRItems.getAttribute("FLAG").getString() + "'"
                         + ",'" + ObjMRItems.getAttribute("TYPE").getString()+"'"
                        + ",'F'," + getAttribute("CREATED_BY").getLong()
                        + ",'" + getAttribute("CREATED_DATE").getString() + "'"
                        + "," + getAttribute("HIERARCHY_ID").getInt()
                        + ",1," + getAttribute("CREATED_BY").getLong()
                        + ",'" + getAttribute("APPROVAL_STATUS").getString() + "'"
                        + ",'" + EITLERPGLOBAL.getCurrentDateDB() + "'"
                        + ",'" + getAttribute("FROM_REMARKS").getString() + "'"
                        + ")";
                System.out.println(sqlStr);
                data.Execute(sqlStr);

                //Get the Maximum Revision No.
//                rsHistory.moveToInsertRow();
//                rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
//                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
//                rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
//                rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
//                rsHistory.updateInt("COMPANY_ID", (int) ObjMRItems.getAttribute("COMPANY_ID").getVal());
//                rsHistory.updateString("PRODUCTION_NO", (String) ObjMRItems.getAttribute("PRODUCTION_NO").getObj());
//                rsHistory.updateString("PRODUCTION_DATE", (String) ObjMRItems.getAttribute("PRODUCTION_DATE").getObj());
//                rsHistory.updateString("QUALITY_CD", (String) ObjMRItems.getAttribute("QUALITY_CD").getObj());
//                rsHistory.updateString("PIECE_NO", (String) ObjMRItems.getAttribute("PIECE_NO").getObj());
//                rsHistory.updateDouble("WIDTH", (double) ObjMRItems.getAttribute("WIDTH").getVal());
//                rsHistory.updateDouble("GROSS_METER", (double) ObjMRItems.getAttribute("METER").getVal());
//                rsHistory.updateString("FLAG_CD", (String) ObjMRItems.getAttribute("FLAG").getObj());
//                rsHistory.updateDouble("KGS", (double) ObjMRItems.getAttribute("KGS").getVal());
//                rsHistory.updateDouble("SQ_METER", (double) ObjMRItems.getAttribute("SQ_METER").getVal());
//                rsHistory.updateDouble("NET_METER", (double) ObjMRItems.getAttribute("NET_METER").getVal());
//                rsHistory.updateString("PIECE_STATUS", "F");
//
//                rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
//                rsHistory.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
//                rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
//                //rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
//                //rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
//
//                rsHistory.updateBoolean("APPROVED", false);
//                rsHistory.updateString("APPROVED_DATE", "0000-00-00");
//                rsHistory.updateBoolean("REJECTED", false);
//                rsHistory.updateString("REJECTED_DATE", "0000-00-00");
//                rsHistory.insertRow();
            }

            //----------------------------------------------------------------//
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = 747; //Doc. Cancel Request
            ObjFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.DocNo = (String) getAttribute("PRODUCTION_NO").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "FILTERFABRIC.FF_TRD_PIECE_REGISTER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName = "PRODUCTION_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            //===Now Cancel the document =====//
            String AppStatus = (String) getAttribute("APPROVAL_STATUS").getObj();

            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {
        Statement stHistory;
        ResultSet rsHistory;
        Statement stHeader;
        ResultSet rsHeader;

        try {

            // ---- History Related Changes ------ //
//            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//
//            rsHistory = stHistory.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER_H WHERE PRODUCTION_NO='1'"); // '1' for restricting all data retrieval
//            rsHistory.first();
            //------------------------------------//
            String theDocNo = (String) getAttribute("PRODUCTION_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PRODUCTION_NO='" + (String) getAttribute("PRODUCTION_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("PRODUCTION_NO").getObj();

            clsFilterFabricApprovalFlow ObjFlow = new clsFilterFabricApprovalFlow();
            String sqlStr = "";
            sqlStr="DELETE FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE PRODUCTION_NO='"+theDocNo+"'";
            data.Execute(sqlStr);
            for (int i = 1; i <= colMRItems.size(); i++) {
                clsProductionItem ObjMRItems = (clsProductionItem) colMRItems.get(Integer.toString(i));

                sqlStr = "INSERT INTO FILTERFABRIC.FF_TRD_PIECE_REGISTER ("
                        + "COMPANY_ID,SR_NO,WH_CODE,PRODUCTION_NO,PRODUCTION_DATE,QUALITY_CD,PIECE_NO,WIDTH,GROSS_METER,"
                        + "SQ_METER,NET_METER,KGS,FLAG_CD,PIECE_TYPE,PIECE_STATUS,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID) "
                        + "VALUES(" + ObjMRItems.getAttribute("COMPANY_ID").getInt() + ","+i+",3"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_NO").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_DATE").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("QUALITY_CD").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PIECE_NO").getString() + "'"
                        + "," + ObjMRItems.getAttribute("WIDTH").getDouble()
                        + "," + ObjMRItems.getAttribute("METER").getDouble()
                        + "," + ObjMRItems.getAttribute("SQ_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("NET_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("KGS").getDouble()
                        + ",'" + ObjMRItems.getAttribute("FLAG").getString() + "'"
                         + ",'" + ObjMRItems.getAttribute("TYPE").getString()+"'"
                        + ",'F'," + getAttribute("MODIFIED_BY").getLong()
                        + ",'" + getAttribute("MODIFIED_DATE").getString() + "'"
                        + "," + getAttribute("HIERARCHY_ID").getInt()
                        + ")";
                System.out.println(sqlStr);
                data.Execute(sqlStr);

                sqlStr = "INSERT INTO FILTERFABRIC.FF_TRD_PIECE_REGISTER_H ("
                        + "COMPANY_ID,SR_NO,WH_CODE,PRODUCTION_NO,PRODUCTION_DATE,QUALITY_CD,PIECE_NO,WIDTH,GROSS_METER,"
                        + "SQ_METER,NET_METER,KGS,FLAG_CD,PIECE_TYPE,PIECE_STATUS,MODIFIED_BY,MODIFIED_DATE,HIERARCHY_ID,"
                        + "REVISION_NO,UPDATED_BY,APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS) "
                        + "VALUES(" + ObjMRItems.getAttribute("COMPANY_ID").getInt() + ","+i+",3"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_NO").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PRODUCTION_DATE").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("QUALITY_CD").getString() + "'"
                        + ",'" + ObjMRItems.getAttribute("PIECE_NO").getString() + "'"
                        + "," + ObjMRItems.getAttribute("WIDTH").getDouble()
                        + "," + ObjMRItems.getAttribute("METER").getDouble()
                        + "," + ObjMRItems.getAttribute("SQ_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("NET_METER").getDouble()
                        + "," + ObjMRItems.getAttribute("KGS").getDouble()
                        + ",'" + ObjMRItems.getAttribute("FLAG").getString() + "'"
                         + ",'" + ObjMRItems.getAttribute("TYPE").getString()+"'"
                        + ",'F'," + getAttribute("MODIFIED_BY").getLong()
                        + ",'" + getAttribute("MODIFIED_DATE").getString() + "'"
                        + "," + getAttribute("HIERARCHY_ID").getInt()
                        +","+RevNo
                        + "," + getAttribute("MODIFIED_BY").getLong()
                        + ",'" + getAttribute("APPROVAL_STATUS").getString() + "'"
                        + ",'" + EITLERPGLOBAL.getCurrentDateDB() + "'"
                        + ",'" + getAttribute("FROM_REMARKS").getString() + "'"
                        + ")";
                System.out.println(sqlStr);
                data.Execute(sqlStr);
            }
            sqlStr="UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER P,FILTERFABRIC.FF_TRD_PIECE_REGISTER_H H"
                    + " SET P.CREATED_BY=H.CREATED_BY,P.CREATED_DATE=H.CREATED_DATE"
                    + " WHERE P.PRODUCTION_NO=H.PRODUCTION_NO AND H.REVISION_NO=1";
            data.Execute(sqlStr);
//            rsData.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
//            rsData.updateString("QUALITY_CD", (String) getAttribute("QUALITY_CD").getObj());
//            rsData.updateString("DESCRIPTION", (String) getAttribute("DESCRIPTION").getObj());
//            rsData.updateDouble("WIDTH", (double) getAttribute("WIDTH").getVal());
//            rsData.updateDouble("RATE", (double) getAttribute("RATE").getVal());
//            //rsData.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
//            //rsData.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsData.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
//            rsData.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
//
//            rsData.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
//            rsHistory.moveToInsertRow();
//            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
//            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
//            rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
//            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
//            rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
//            rsHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
//            rsHistory.updateString("QUALITY_CD", (String) getAttribute("QUALITY_CD").getObj());
//            rsHistory.updateString("DESCRIPTION", (String) getAttribute("DESCRIPTION").getObj());
//            rsHistory.updateDouble("WIDTH", (double) getAttribute("WIDTH").getVal());
//            rsHistory.updateDouble("RATE", (double) getAttribute("RATE").getVal());
//            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
//            //rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
//            //rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsHistory.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
//            rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
//            rsHistory.updateBoolean("APPROVED", false);
//            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
//            rsHistory.updateBoolean("REJECTED", false);
//            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
//            rsHistory.insertRow();
            //----------------------------------------------------------------//
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = 747; //Doc. Cancel Request
            ObjFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.DocNo = (String) getAttribute("PRODUCTION_NO").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "FILTERFABRIC.FF_TRD_PIECE_REGISTER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName = "PRODUCTION_NO";

            //==== Handling Rejected Documents ==========//
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();

            if (AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from FILTERFABRIC.FF_TRD_DOC_DATA
                //data.Execute("DELETE FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");

                //ObjFlow.IsCreator=true;
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }

            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE FILTERFABRIC.FF_TRD_PIECE_REGISTER SET REJECTED=0 WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND PRODUCTION_NO='" + ObjFlow.DocNo + "'");
                //Remove Old Records from FILTERFABRIC.FF_TRD_DOC_DATA
                data.Execute("DELETE FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=747 AND DOC_NO='" + ObjFlow.DocNo + "'");

                ObjFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFlow.Status.equals("H")) {
                //Do nothing
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

            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    //Deletes current record
    public boolean Delete(int pUserID) {
        try {

            int lCompanyID = (int) getAttribute("COMPANY_ID").getVal();
            String lDocNo = (String) getAttribute("REQ_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM D_COM_DOC_CANCEL_REQUEST WHERE COMPANY_ID=" + lCompanyID + " AND REQ_NO='" + lDocNo + "'";
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

    public Object getObject(int pCompanyID, String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND QUALITY_CD='" + pDocNo + "'";
        clsProduction ObjDoc = new clsProduction();
        ObjDoc.Filter(strCondition, pCompanyID);
        return ObjDoc;
    }

    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0, Counter2 = 0, Counter3 = 0;

        try {
            tmpConn = data.getConn();

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsTmp = tmpStmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER " + pCondition);
            rsTmp.first();

            Counter = 0;
            while (!rsTmp.isAfterLast()) {
                Counter = Counter + 1;
                clsProduction ObjDoc = new clsProduction();
                ObjDoc.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                ObjDoc.setAttribute("PRODUCTION_NO", rsTmp.getString("PRODUCTION_NO"));

                ObjDoc.setAttribute("DEPT_ID", rsTmp.getInt("DEPT_ID"));
                ObjDoc.setAttribute("USER_ID", rsTmp.getInt("USER_ID"));
                ObjDoc.setAttribute("MODULE_ID", rsTmp.getInt("MODULE_ID"));
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("PURPOSE", rsTmp.getString("PURPOSE"));
                ObjDoc.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                ObjDoc.setAttribute("APPROVED", rsTmp.getInt("APPROVED"));
                ObjDoc.setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                ObjDoc.setAttribute("REJECTED", rsTmp.getBoolean("REJECTED"));
                ObjDoc.setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                ObjDoc.setAttribute("HIERARCHY_ID", rsTmp.getInt("HIERARCHY_ID"));
                ObjDoc.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                ObjDoc.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjDoc.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                ObjDoc.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));

                List.put(Long.toString(Counter), ObjDoc);
                rsTmp.next();
            }

            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

            return List;
        } catch (Exception e) {
            return List;
        }
    }

    public boolean Filter(String pCondition, long pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsData = Stmt.executeQuery(strSql);
            rsData.first();

            if (rsData.getRow() <= 0) {
                strSql = "SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND CREATED_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND CREATED_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY PRODUCTION_NO";
                rsData = Stmt.executeQuery(strSql);
                Ready = true;
                MoveLast();
                LastError = "No Records found";
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

    public boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        int Counter = 0, Counter2 = 0;
        int RevNo = 0;

        try {
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (HistoryView) {
                setAttribute("REVISION_NO", rsData.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }
            String mprodno;
            mprodno = rsData.getString("PRODUCTION_NO");
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER_H WHERE PRODUCTION_NO='" + mprodno + "' ORDER BY SR_NO");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE PRODUCTION_NO='" + mprodno + "' ORDER BY SR_NO");
            }
            rsTmp.first();
            Counter = 0;
            colMRItems.clear();
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {
                Counter = Counter + 1;
                clsProductionItem ObjMRItems = new clsProductionItem();
                setAttribute("PRODUCTION_NO", rsTmp.getString("PRODUCTION_NO"));
                ObjMRItems.setAttribute("TYPE", rsTmp.getString("PIECE_TYPE"));
                ObjMRItems.setAttribute("TYPE", rsTmp.getInt("SR_NO"));
                ObjMRItems.setAttribute("PRODUCTION_NO", rsTmp.getString("PRODUCTION_NO"));
                ObjMRItems.setAttribute("PRODUCTION_DATE", rsTmp.getString("PRODUCTION_DATE"));
                ObjMRItems.setAttribute("QUALITY_CD", rsTmp.getString("QUALITY_CD"));
                ObjMRItems.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("WIDTH", rsTmp.getDouble("WIDTH"));
                ObjMRItems.setAttribute("METER", rsTmp.getDouble("GROSS_METER"));
                ObjMRItems.setAttribute("KGS", rsTmp.getDouble("KGS"));
                ObjMRItems.setAttribute("FLAG", rsTmp.getString("FLAG_CD"));
                ObjMRItems.setAttribute("SQ_METER", rsTmp.getDouble("SQ_METER"));
                ObjMRItems.setAttribute("NET_METER", rsTmp.getDouble("NET_METER"));

                ObjMRItems.setAttribute("APPROVED", rsTmp.getInt("APPROVED"));
                ObjMRItems.setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                ObjMRItems.setAttribute("REJECTED", rsTmp.getBoolean("REJECTED"));
                ObjMRItems.setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                ObjMRItems.setAttribute("HIERARCHY_ID", rsTmp.getInt("HIERARCHY_ID"));
                ObjMRItems.setAttribute("CREATED_BY", rsTmp.getInt("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY", rsTmp.getInt("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                colMRItems.put(Long.toString(Counter), ObjMRItems);
                rsTmp.next();

            }

            if (HistoryView) {
                RevNo = rsData.getInt("REVISION_NO");
            }

            return true;
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
            //Deletion in History Records Not Allowed
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_CANCEL_REQUEST WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND REQ_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=44 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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

    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            //Updation in History Records Not Allowed
            if (HistoryView) {
                return false;
            }

            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PRODUCTION_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=747 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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

    public static HashMap getPendingApprovals(int pCompanyID, int pUserID, int pOrder, int FinYearFrom) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();

        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,D_COM_DOC_CANCEL_REQUEST.DEPT_ID,D_COM_DOC_CANCEL_REQUEST.MODULE_ID FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID=" + pUserID + " AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44 ORDER BY FILTERFABRIC.FF_TRD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,D_COM_DOC_CANCEL_REQUEST.DEPT_ID,D_COM_DOC_CANCEL_REQUEST.MODULE_ID FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID=" + pUserID + " AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44 ORDER BY D_COM_DOC_CANCEL_REQUEST.REQ_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,D_COM_DOC_CANCEL_REQUEST.DEPT_ID,D_COM_DOC_CANCEL_REQUEST.MODULE_ID FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=" + Integer.toString(pCompanyID) + " AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID=" + pUserID + " AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44 ORDER BY D_COM_DOC_CANCEL_REQUEST.REQ_NO";
            }

            //strSQL="SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID="+pUserID+" AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44";
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast()) {
                if (EITLERPGLOBAL.isWithinDate(rsTmp.getString("REQ_DATE"), FinYearFrom)) {
                    Counter = Counter + 1;
                    clsProduction ObjDoc = new clsProduction();

                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("REQ_NO", rsTmp.getString("REQ_NO"));
                    ObjDoc.setAttribute("REQ_DATE", rsTmp.getString("REQ_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("DEPT_ID", rsTmp.getInt("DEPT_ID"));
                    ObjDoc.setAttribute("MODULE_ID", rsTmp.getInt("MODULE_ID"));
                    // ----------------- End of Header Fields ------------------------------------//

                    //Put the prepared user object into list
                    List.put(Long.toString(Counter), ObjDoc);
                }

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

    public static boolean CanCancel(int pCompanyID, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;

        try {
            rsTmp = data.getResult("SELECT DOC_NO,APPROVED FROM D_COM_DOC_CANCEL_REQUEST WHERE COMPANY_ID=" + pCompanyID + " AND REQ_NO='" + pDocNo + "' AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                if (rsTmp.getBoolean("APPROVED")) {

                } else {
                    canCancel = true;
                }
            }

            rsTmp.close();
        } catch (Exception e) {

        }

        return canCancel;

    }

    public static boolean CancelDoc(int pCompanyID, String pDocNo) {

        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;

        try {
            if (CanCancel(pCompanyID, pDocNo)) {

                boolean ApprovedDoc = false;

                rsTmp = data.getResult("SELECT APPROVED FROM D_COM_DOC_CANCEL_REQUEST WHERE COMPANY_ID=" + pCompanyID + " AND REQ_NO='" + pDocNo + "'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    int ModuleID = 44;

                    data.Execute("DELETE FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pDocNo + "' AND MODULE_ID=" + (ModuleID));
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_COM_DOC_CANCEL_REQUEST SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND REQ_NO='" + pDocNo + "'");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }

    public static String[] getPiecedetail(String pKey) {
        Connection tmpConn = null;
        Statement stTmp = null;
        ResultSet rsTmp;
        String[] Piecedetail = new String[4];
        String[] error = {"error"};
        try {

            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //String strSQL="SELECT * FROM RETAIL_SHOP.ITEM_STOCK_MASTER WHERE CONCAT(QUALITY_CD,SHADE_CD,PIECE_NO,EXT,INVOICE_NO,SIZE)='"+pKey+"'";
            String strSQL = "SELECT WIDTH FROM FILTERFABRIC.FF_TRD_QUALITY_MASTER WHERE QUALITY_CD='" + pKey + "'";

            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Piecedetail[0] = rsTmp.getString("WIDTH");
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
