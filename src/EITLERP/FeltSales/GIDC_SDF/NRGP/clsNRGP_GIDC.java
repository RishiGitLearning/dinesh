/*
 * 
 *
 * 
 */
package EITLERP.FeltSales.GIDC_SDF.NRGP;

import java.net.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import EITLERP.*;
import EITLERP.FeltSales.FeltInvReport.NumWord;
import EITLERP.FeltSales.GIDC_SDF.Despatch.frmDespatchGIDC_SDML;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author nhpatel
 * @version
 */
public class clsNRGP_GIDC {

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
    public clsNRGP_GIDC() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("GATEPASS_NO", new Variant(""));
        props.put("GATEPASS_DATE", new Variant(""));
        props.put("GATEPASS_TYPE", new Variant(""));
        props.put("MODE_TRANSPORT", new Variant(0));
        props.put("TRANPORTER", new Variant(0));
        props.put("NET_AMOUNT", new Variant(0));
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

        props.put("DESPATCH_MODE", new Variant(""));
        props.put("GROSS_WEIGHT", new Variant(""));

        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("PREFIX", new Variant(""));

        props.put("PARTY_NAME", new Variant(""));
        props.put("ADD1", new Variant(""));
        props.put("ADD2", new Variant(""));
        props.put("ADD3", new Variant(""));
        props.put("CITY", new Variant(""));

        props.put("PACKING", new Variant(""));
        props.put("PURPOSE", new Variant(""));

        //Create a new object for line items
        colLineItems = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsNRGP = Stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP WHERE GATEPASS_TYPE='MKP' GROUP BY GATEPASS_NO ORDER BY GATEPASS_NO");//ORDER BY GATEPASS_NO");
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
        Statement stTmp, stHistory, stHDetail, stHLot, statementTemp;
        ResultSet rsTmp, rsHistory, rsHDetail, rsHLot, resultSetTemp;
        Statement stHeader;
        ResultSet rsHeader;

        String strSQL = "", Gatepassno = "", Declno = "";
        String RJNNo = "";
        int RJNSrno = 0, GatepassSrno = 0, DeclSrno = 0;

        try {
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
            // Inserting records in Header
            long gCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());

            // Production data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP WHERE GATEPASS_NO='1'");

//            stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            rsHeader=stHeader.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP WHERE GATEPASS_NO='1'");
            //rsHeader.first();
            // Update the Stock only after Final Approval //
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("GATEPASS_TYPE").getObj();

            // ---- History Related Changes ------ //
            Conn = data.getConn();
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//

            //--------- Generate New MIR No. ------------
            if (UserDocNo) {
                rsTmp = data.getResult("SELECT GATEPASS_NO FROM PRODUCTION.GIDC_FELT_NRGP WHERE GATEPASS_NO='" + ((String) getAttribute("GATEPASS_NO").getObj()).trim() + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    LastError = "Document no. already exist. Please specify other document no.";
                    return false;
                }
            } else {
                setAttribute("GATEPASS_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 793, 282, true));
            }
            //-------------------------------------------------

            for (int i = 1; i <= colLineItems.size(); i++) {
                clsNRGPItem_GIDC ObjNRGPItem = (clsNRGPItem_GIDC) colLineItems.get(Integer.toString(i));

                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
                resultSetTemp.updateString("GATEPASS_NO", (String) getAttribute("GATEPASS_NO").getObj());
                resultSetTemp.updateString("GATEPASS_DATE", (String) getAttribute("GATEPASS_DATE").getObj());
                resultSetTemp.updateString("GATEPASS_TYPE", (String) getAttribute("GATEPASS_TYPE").getObj());
                resultSetTemp.updateLong("MODE_TRANSPORT", (long) getAttribute("MODE_TRANSPORT").getVal());
                resultSetTemp.updateLong("TRANSPORTER", (long) getAttribute("TRANSPORTER").getVal());
                resultSetTemp.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
                resultSetTemp.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                resultSetTemp.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                resultSetTemp.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                resultSetTemp.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
                resultSetTemp.updateString("ADD1", (String) getAttribute("ADD1").getObj());
                resultSetTemp.updateString("ADD2", (String) getAttribute("ADD2").getObj());
                resultSetTemp.updateString("ADD3", (String) getAttribute("ADD3").getObj());
                resultSetTemp.updateString("CITY", (String) getAttribute("CITY").getObj());
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateBoolean("CHANGED", true);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("CANCELED", false);
                resultSetTemp.updateString("PACKING", (String) getAttribute("PACKING").getObj());
                resultSetTemp.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
                resultSetTemp.updateString("DESPATCH_MODE", (String) getAttribute("DESPATCH_MODE").getObj());
                resultSetTemp.updateString("GROSS_WEIGHT", (String) getAttribute("GROSS_WEIGHT").getObj());
                resultSetTemp.updateString("PIECE_NO", (String) ObjNRGPItem.getAttribute("PIECE_NO").getObj());
                resultSetTemp.updateDouble("LENGTH", ObjNRGPItem.getAttribute("LENGTH").getVal());
                resultSetTemp.updateDouble("WIDTH", ObjNRGPItem.getAttribute("WIDTH").getVal());
                resultSetTemp.updateDouble("WEIGHT", ObjNRGPItem.getAttribute("WEIGHT").getVal());
                resultSetTemp.updateDouble("SQMTR", ObjNRGPItem.getAttribute("SQMTR").getVal());
                resultSetTemp.updateString("NRGP_DESC", (String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                resultSetTemp.updateDouble("QTY", ObjNRGPItem.getAttribute("QTY").getVal());
                resultSetTemp.updateDouble("WASTE_LENGTH_QTY", ObjNRGPItem.getAttribute("WASTE_LENGTH_QTY").getVal());
                resultSetTemp.updateDouble("WASTE_WEFT_QTY", ObjNRGPItem.getAttribute("WASTE_WEFT_QTY").getVal());
                resultSetTemp.updateDouble("RATE", ObjNRGPItem.getAttribute("RATE").getVal());
                resultSetTemp.updateDouble("NET_AMOUNT", ObjNRGPItem.getAttribute("NET_AMOUNT").getVal());
                NumWord num = new NumWord();
                String rsInWord = num.convertNumToWord(Math.round(ObjNRGPItem.getAttribute("NET_AMOUNT").getVal()));
                resultSetTemp.updateString("NET_AMOUNT_IN_WORDS", rsInWord);
                resultSetTemp.updateString("DELIVERY_CHALLAN_NO", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_NO").getObj());
                resultSetTemp.updateString("DELIVERY_CHALLAN_DATE", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_DATE").getObj());
                //resultSetTemp.updateString("UNIT", "KGS");
                resultSetTemp.updateString("UNIT", "");
//                resultSetTemp.updateString("HSN", "5911");
                resultSetTemp.updateString("HSN", "59113290");
                resultSetTemp.updateString("WASTE_HSN", "5404");
                resultSetTemp.insertRow();

                //========= Inserting Into History =================//
                //Get the Maximum Revision No.
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
                rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
                rsHistory.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
                rsHistory.updateString("GATEPASS_NO", (String) getAttribute("GATEPASS_NO").getObj());
                rsHistory.updateString("GATEPASS_DATE", (String) getAttribute("GATEPASS_DATE").getObj());
                rsHistory.updateString("GATEPASS_TYPE", (String) getAttribute("GATEPASS_TYPE").getObj());
                rsHistory.updateLong("MODE_TRANSPORT", (long) getAttribute("MODE_TRANSPORT").getVal());
                rsHistory.updateLong("TRANSPORTER", (long) getAttribute("TRANSPORTER").getVal());
                rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
                rsHistory.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHistory.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHistory.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
                rsHistory.updateString("ADD1", (String) getAttribute("ADD1").getObj());
                rsHistory.updateString("ADD2", (String) getAttribute("ADD2").getObj());
                rsHistory.updateString("ADD3", (String) getAttribute("ADD3").getObj());
                rsHistory.updateString("CITY", (String) getAttribute("CITY").getObj());
                rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsHistory.updateBoolean("CHANGED", true);
                rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("APPROVED", false);
                rsHistory.updateString("APPROVED_DATE", "0000-00-00");
                rsHistory.updateBoolean("REJECTED", false);
                rsHistory.updateString("REJECTED_DATE", "0000-00-00");
                rsHistory.updateBoolean("CANCELED", false);
                rsHistory.updateString("PACKING", (String) getAttribute("PACKING").getObj());
                rsHistory.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
                rsHistory.updateString("DESPATCH_MODE", (String) getAttribute("DESPATCH_MODE").getObj());
                rsHistory.updateString("GROSS_WEIGHT", (String) getAttribute("GROSS_WEIGHT").getObj());
                rsHistory.updateString("PIECE_NO", (String) ObjNRGPItem.getAttribute("PIECE_NO").getObj());
                rsHistory.updateDouble("LENGTH", ObjNRGPItem.getAttribute("LENGTH").getVal());
                rsHistory.updateDouble("WIDTH", ObjNRGPItem.getAttribute("WIDTH").getVal());
                rsHistory.updateDouble("WEIGHT", ObjNRGPItem.getAttribute("WEIGHT").getVal());
                rsHistory.updateDouble("SQMTR", ObjNRGPItem.getAttribute("SQMTR").getVal());
                rsHistory.updateString("NRGP_DESC", (String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                rsHistory.updateDouble("QTY", ObjNRGPItem.getAttribute("QTY").getVal());
                rsHistory.updateDouble("WASTE_LENGTH_QTY", ObjNRGPItem.getAttribute("WASTE_LENGTH_QTY").getVal());
                rsHistory.updateDouble("WASTE_WEFT_QTY", ObjNRGPItem.getAttribute("WASTE_WEFT_QTY").getVal());
                rsHistory.updateDouble("RATE", ObjNRGPItem.getAttribute("RATE").getVal());
                rsHistory.updateDouble("NET_AMOUNT", ObjNRGPItem.getAttribute("NET_AMOUNT").getVal());
                rsHistory.updateString("NET_AMOUNT_IN_WORDS", rsInWord);
                rsHistory.updateString("DELIVERY_CHALLAN_NO", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_NO").getObj());
                rsHistory.updateString("DELIVERY_CHALLAN_DATE", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_DATE").getObj());
                //rsHistory.updateString("UNIT", "KGS");
                rsHistory.updateString("UNIT", "");
//                rsHistory.updateString("HSN", "5911");
                rsHistory.updateString("HSN", "59113290");
                rsHistory.updateString("WASTE_HSN", "5404");

                
                ResultSet rsTmpUser = data.getResult("SELECT USER()");
                rsTmpUser.first();
                String str = rsTmpUser.getString(1);
                String str_split[] = str.split("@");
                rsHistory.updateString("FROM_IP", "" + str_split[1]);

                rsHistory.insertRow();

                //} // Entries inserting into Line file
            }

            ObjFlow.ModuleID = 793; //QUOTATION MODULE ID
            ObjFlow.DocNo = (String) getAttribute("GATEPASS_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("GATEPASS_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.GIDC_FELT_NRGP";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REMARKS").getObj();
            ObjFlow.FieldName = "GATEPASS_NO";

            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
                frmDespatchGIDC_SDML.NRGPGatepassMSG = frmDespatchGIDC_SDML.NRGPGatepassMSG + ", " +ObjFlow.DocNo;
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }

            // Specified procedure over here
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Update() {
        Statement stTmp, stHistory, stHDetail, stHLot;
        ResultSet rsTmp, rsHistory, rsHDetail, rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        String strSQL = "", Gatepassno = "", Declno = "";
        String RJNNo = "";
        int RJNSrno = 0, GatepassSrno = 0, DeclSrno = 0;

        try {

            String theDocNo = (String) getAttribute("GATEPASS_NO").getObj();
            System.out.println("GATEPASSNO : " + rsNRGP.getString("GATEPASS_NO"));
            System.out.println("ConCur : " +rsNRGP.getConcurrency());
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsNRGP = Stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP WHERE GATEPASS_NO='"+rsNRGP.getString("GATEPASS_NO")+"'");//ORDER BY GATEPASS_NO");
            rsNRGP.first();
            System.out.println("ConCur : " +rsNRGP.getConcurrency());
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_NO)='"+theDocNo+"'");
            //rsHeader.first();

            String GatepassNo = (String) getAttribute("GATEPASS_NO").getObj();

            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//

            //No Primary Keys will be updated & Updating of Header starts
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.GIDC_FELT_NRGP_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND GATEPASS_NO='" + (String) getAttribute("GATEPASS_NO").getObj() + "'");
            RevNo++;

            // Update the Stock only after Final Approval //
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("GATEPASS_TYPE").getObj();
            System.out.println("dfgsdf"+(String) getAttribute("GATEPASS_TYPE").getObj());

            for (int i = 1; i <= colLineItems.size(); i++) {
                clsNRGPItem_GIDC ObjNRGPItem = (clsNRGPItem_GIDC) colLineItems.get(Integer.toString(i));

                rsNRGP.updateString("GATEPASS_TYPE", (String) getAttribute("GATEPASS_TYPE").getObj());
                rsNRGP.updateLong("MODE_TRANSPORT", (long) getAttribute("MODE_TRANSPORT").getVal());
                rsNRGP.updateLong("TRANSPORTER", (long) getAttribute("TRANSPORTER").getVal());
                rsNRGP.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
                rsNRGP.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsNRGP.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsNRGP.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                rsNRGP.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsNRGP.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
                rsNRGP.updateString("ADD1", (String) getAttribute("ADD1").getObj());
                rsNRGP.updateString("ADD2", (String) getAttribute("ADD2").getObj());
                rsNRGP.updateString("ADD3", (String) getAttribute("ADD3").getObj());
                rsNRGP.updateString("CITY", (String) getAttribute("CITY").getObj());
                rsNRGP.updateBoolean("CHANGED", true);
                rsNRGP.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsNRGP.updateString("PACKING", (String) getAttribute("PACKING").getObj());
                rsNRGP.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
                rsNRGP.updateString("DESPATCH_MODE", (String) getAttribute("DESPATCH_MODE").getObj());
                rsNRGP.updateString("GROSS_WEIGHT", (String) getAttribute("GROSS_WEIGHT").getObj());
                rsNRGP.updateBoolean("CANCELED", false);
                rsNRGP.updateString("PIECE_NO", (String) ObjNRGPItem.getAttribute("PIECE_NO").getObj());
                rsNRGP.updateDouble("LENGTH", ObjNRGPItem.getAttribute("LENGTH").getVal());
                rsNRGP.updateDouble("WIDTH", ObjNRGPItem.getAttribute("WIDTH").getVal());
                rsNRGP.updateDouble("WEIGHT", ObjNRGPItem.getAttribute("WEIGHT").getVal());
                rsNRGP.updateDouble("SQMTR", ObjNRGPItem.getAttribute("SQMTR").getVal());
                rsNRGP.updateString("NRGP_DESC", (String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                rsNRGP.updateDouble("QTY", ObjNRGPItem.getAttribute("QTY").getVal());
                rsNRGP.updateDouble("WASTE_LENGTH_QTY", ObjNRGPItem.getAttribute("WASTE_LENGTH_QTY").getVal());
                rsNRGP.updateDouble("WASTE_WEFT_QTY", ObjNRGPItem.getAttribute("WASTE_WEFT_QTY").getVal());
                rsNRGP.updateDouble("RATE", ObjNRGPItem.getAttribute("RATE").getVal());
                rsNRGP.updateDouble("NET_AMOUNT", ObjNRGPItem.getAttribute("NET_AMOUNT").getVal());
                NumWord num = new NumWord();
                String rsInWord = num.convertNumToWord(Math.round(ObjNRGPItem.getAttribute("NET_AMOUNT").getVal()));
                rsNRGP.updateString("NET_AMOUNT_IN_WORDS", rsInWord);
                rsNRGP.updateString("DELIVERY_CHALLAN_NO", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_NO").getObj());
                rsNRGP.updateString("DELIVERY_CHALLAN_DATE", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_DATE").getObj());
                //rsNRGP.updateString("UNIT", "KGS");                
                rsNRGP.updateString("UNIT", "");                
//                rsNRGP.updateString("HSN", "5911");
                rsNRGP.updateString("HSN", "59113290");
                rsNRGP.updateString("WASTE_HSN", "5404");
                rsNRGP.updateRow();

                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
                rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
                rsHistory.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
                rsHistory.updateString("GATEPASS_NO", (String) getAttribute("GATEPASS_NO").getObj());
                rsHistory.updateString("GATEPASS_DATE", (String) getAttribute("GATEPASS_DATE").getObj());
                rsHistory.updateString("GATEPASS_TYPE", (String) getAttribute("GATEPASS_TYPE").getObj());
                rsHistory.updateLong("MODE_TRANSPORT", (long) getAttribute("MODE_TRANSPORT").getVal());
                rsHistory.updateLong("TRANSPORTER", (long) getAttribute("TRANSPORTER").getVal());
                rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
                rsHistory.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHistory.updateLong("MODIFIED_BY", (long) getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHistory.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
                rsHistory.updateString("ADD1", (String) getAttribute("ADD1").getObj());
                rsHistory.updateString("ADD2", (String) getAttribute("ADD2").getObj());
                rsHistory.updateString("ADD3", (String) getAttribute("ADD3").getObj());
                rsHistory.updateString("CITY", (String) getAttribute("CITY").getObj());
                rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsHistory.updateBoolean("CHANGED", true);
                rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("PACKING", (String) getAttribute("PACKING").getObj());
                rsHistory.updateString("PURPOSE", (String) getAttribute("PURPOSE").getObj());
                rsHistory.updateString("DESPATCH_MODE", (String) getAttribute("DESPATCH_MODE").getObj());
                rsHistory.updateString("GROSS_WEIGHT", (String) getAttribute("GROSS_WEIGHT").getObj());
                rsHistory.updateString("PIECE_NO", (String) ObjNRGPItem.getAttribute("PIECE_NO").getObj());
                rsHistory.updateDouble("LENGTH", ObjNRGPItem.getAttribute("LENGTH").getVal());
                rsHistory.updateDouble("WIDTH", ObjNRGPItem.getAttribute("WIDTH").getVal());
                rsHistory.updateDouble("WEIGHT", ObjNRGPItem.getAttribute("WEIGHT").getVal());
                rsHistory.updateDouble("SQMTR", ObjNRGPItem.getAttribute("SQMTR").getVal());
                rsHistory.updateString("NRGP_DESC", (String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                rsHistory.updateDouble("QTY", ObjNRGPItem.getAttribute("QTY").getVal());
                rsHistory.updateDouble("WASTE_LENGTH_QTY", ObjNRGPItem.getAttribute("WASTE_LENGTH_QTY").getVal());
                rsHistory.updateDouble("WASTE_WEFT_QTY", ObjNRGPItem.getAttribute("WASTE_WEFT_QTY").getVal());
                rsHistory.updateDouble("RATE", ObjNRGPItem.getAttribute("RATE").getVal());
                rsHistory.updateDouble("NET_AMOUNT", ObjNRGPItem.getAttribute("NET_AMOUNT").getVal());
                rsHistory.updateString("NET_AMOUNT_IN_WORDS", rsInWord);
                rsHistory.updateString("DELIVERY_CHALLAN_NO", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_NO").getObj());
                rsHistory.updateString("DELIVERY_CHALLAN_DATE", (String) ObjNRGPItem.getAttribute("DELIVERY_CHALLAN_DATE").getObj());
                //rsHistory.updateString("UNIT", "KGS");                
                rsHistory.updateString("UNIT", "");                
//                rsHistory.updateString("HSN", "5911");
                rsHistory.updateString("HSN", "59113290");
                rsHistory.updateString("WASTE_HSN", "5404");

                ResultSet rsTmpUser = data.getResult("SELECT USER()");
                rsTmpUser.first();
                String str = rsTmpUser.getString(1);
                String str_split[] = str.split("@");
                rsHistory.updateString("FROM_IP", "" + str_split[1]);

                rsHistory.insertRow();
            }

            ObjFlow.ModuleID = 793; //QUOTATION MODULE ID
            ObjFlow.DocNo = (String) getAttribute("GATEPASS_NO").getObj();
            ObjFlow.DocDate = (String) getAttribute("GATEPASS_DATE").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "PRODUCTION.GIDC_FELT_NRGP";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REMARKS").getObj();
            ObjFlow.FieldName = "GATEPASS_NO";

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.GIDC_FELT_NRGP SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND GATEPASS_NO='" + ObjFlow.DocNo + "'");

                ObjFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFlow.Status.equals("H")) {
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
            
            if (ObjFlow.Status.equals("F") && ObjFlow.finalApproved) {
                ObjFlow.finalApproved = false;
                try {

                    ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP WHERE GATEPASS_NO='" + ObjFlow.DocNo + "'");
                    while (rsTemp.next()) {
                        String challanNo= EITLERP.clsFirstFree.getNextFreeNo(2, 793, 283, true);
                        data.Execute("UPDATE PRODUCTION.GIDC_FELT_NRGP SET DELIVERY_CHALLAN_NO='" + challanNo + "',DELIVERY_CHALLAN_DATE=CURDATE() WHERE GATEPASS_NO='" + ObjFlow.DocNo + "' AND PIECE_NO = '" + rsTemp.getString("PIECE_NO") + "' ");
                        data.Execute("UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_FELT_NRGP N SET P.GIDC_DELIVERY_CHALLAN_NO=N.DELIVERY_CHALLAN_NO,P.GIDC_DELIVERY_CHALLAN_DATE=N.DELIVERY_CHALLAN_DATE WHERE N.GATEPASS_NO='" + ObjFlow.DocNo + "' AND P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                    }
                    rsTemp.close();
                } catch (Exception e) {
                    LastError = e.getMessage();
                    e.printStackTrace();
                }
            }
            setData();
            // updataion process is over
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID, String pGatepassno, int userID) {
        if (HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pGatepassno + "' AND APPROVED=1";

            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Group is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=793 AND USER_ID=" + userID + " AND DOC_NO='" + pGatepassno + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.GIDC_FELT_NRGP WHERE GATEPASS_NO='" + pGatepassno + "' AND GATEPASS_TYPE='MKP' ";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                } else {
                    LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                    return false;
                }
            }
        } catch (Exception e) {
            LastError = "Error occured while deleting." + e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID, String pGatepassno, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pGatepassno + "' AND APPROVED=1";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=793 AND USER_ID=" + Integer.toString(pUserID) + " AND DOC_NO='" + pGatepassno + "' AND STATUS='W'";
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
            int lCompanyID = (int) getAttribute("COMPANY_ID").getVal();
            String lGatepassno = (String) getAttribute("GATEPASS_NO").getObj();

            if (CanDelete(lCompanyID, lGatepassno, pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + lGatepassno.trim() + "'";
                data.Execute(strQry);
            }

            rsNRGP.refreshRow();
            return MoveLast();
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public Object getObject(int pCompanyID, String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "'";
        clsNRGP_GIDC ObjNRGP = new clsNRGP_GIDC();
        ObjNRGP.Filter(strCondition, pCompanyID);
        return ObjNRGP;
    }

    public boolean Filter(String pCondition, long pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM PRODUCTION.GIDC_FELT_NRGP " + pCondition;
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

        tmpConn = data.getConn();

        HashMap List = new HashMap();
        long Counter = 0;
        int RevNo = 0;

        try {
            colLineItems.clear();

            //String mCompanyID = Long.toString((long) getAttribute("COMPANY_ID").getVal());
            //String mGatepassno = (String) getAttribute("GATEPASS_NO").getObj();
            String mCompanyID = rsNRGP.getString("COMPANY_ID");
            String mGatepassno = rsNRGP.getString("GATEPASS_NO");
            
            tmpStmt = tmpConn.createStatement();
            if (HistoryView) {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP_H WHERE COMPANY_ID=" + mCompanyID + " AND GATEPASS_NO='" + mGatepassno.trim() + "' AND REVISION_NO=" + RevNo + " ");
            } else {
                rsTmp = tmpStmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + mCompanyID + " AND GATEPASS_NO='" + mGatepassno.trim() + "' ");
            }
            Counter = 0;
            rsTmp.first();
            while (!rsTmp.isAfterLast()) {
                Counter = Counter + 1;
                clsNRGPItem_GIDC ObjNRGPItem = new clsNRGPItem_GIDC();

                if (HistoryView) {
                    RevNo = rsTmp.getInt("REVISION_NO");
                    setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                } else {
                    setAttribute("REVISION_NO", 0);
                }

                setAttribute("COMPANY_ID", rsTmp.getLong("COMPANY_ID"));
                setAttribute("GATEPASS_NO", rsTmp.getString("GATEPASS_NO"));
                setAttribute("GATEPASS_DATE", rsTmp.getString("GATEPASS_DATE"));
                setAttribute("GATEPASS_TYPE", rsTmp.getString("GATEPASS_TYPE"));
                setAttribute("MODE_TRANSPORT", rsTmp.getLong("MODE_TRANSPORT"));
                setAttribute("TRANSPORTER", rsTmp.getLong("TRANSPORTER"));
                setAttribute("APPROVED", rsTmp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE", rsTmp.getString("APPROVED_DATE"));
                setAttribute("REJECTED", rsTmp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE", rsTmp.getString("REJECTED_DATE"));
                setAttribute("REJECTED_REMARKS", rsTmp.getString("REJECTED_REMARKS"));
                setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                setAttribute("CANCELED", rsTmp.getInt("CANCELED"));
                setAttribute("CREATED_BY", rsTmp.getLong("CREATED_BY"));
                setAttribute("CREATED_DATE", rsTmp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY", rsTmp.getLong("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                setAttribute("PARTY_NAME", rsTmp.getString("PARTY_NAME"));
                setAttribute("ADD1", rsTmp.getString("ADD1"));
                setAttribute("ADD2", rsTmp.getString("ADD2"));
                setAttribute("ADD3", rsTmp.getString("ADD3"));
                setAttribute("CITY", rsTmp.getString("CITY"));
                setAttribute("HIERARCHY_ID", rsTmp.getInt("HIERARCHY_ID"));
                setAttribute("PACKING", rsTmp.getString("PACKING"));
                setAttribute("PURPOSE", rsTmp.getString("PURPOSE"));
                setAttribute("DESPATCH_MODE", rsTmp.getString("DESPATCH_MODE"));
                setAttribute("GROSS_WEIGHT", rsTmp.getString("GROSS_WEIGHT"));
                
                ObjNRGPItem.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                ObjNRGPItem.setAttribute("LENGTH", rsTmp.getDouble("LENGTH"));
                ObjNRGPItem.setAttribute("WIDTH", rsTmp.getDouble("WIDTH"));
                ObjNRGPItem.setAttribute("WEIGHT", rsTmp.getDouble("WEIGHT"));
                ObjNRGPItem.setAttribute("SQMTR", rsTmp.getDouble("SQMTR"));
                ObjNRGPItem.setAttribute("NRGP_DESC", rsTmp.getString("NRGP_DESC"));
                ObjNRGPItem.setAttribute("QTY", rsTmp.getDouble("QTY"));
                ObjNRGPItem.setAttribute("WASTE_LENGTH_QTY", rsTmp.getDouble("WASTE_LENGTH_QTY"));
                ObjNRGPItem.setAttribute("WASTE_WEFT_QTY", rsTmp.getDouble("WASTE_WEFT_QTY"));
                ObjNRGPItem.setAttribute("RATE", rsTmp.getDouble("RATE"));
                ObjNRGPItem.setAttribute("NET_AMOUNT", rsTmp.getDouble("NET_AMOUNT"));
                ObjNRGPItem.setAttribute("DELIVERY_CHALLAN_NO", rsTmp.getString("DELIVERY_CHALLAN_NO"));
                ObjNRGPItem.setAttribute("DELIVERY_CHALLAN_DATE", rsTmp.getString("DELIVERY_CHALLAN_DATE"));

                colLineItems.put(Long.toString(Counter), ObjNRGPItem);
                rsTmp.next();
            }
            return true;
        } catch (Exception e) {
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
                strSQL = "SELECT PRODUCTION.GIDC_FELT_NRGP.GATEPASS_NO,PRODUCTION.GIDC_FELT_NRGP.GATEPASS_DATE,PRODUCTION.GIDC_FELT_NRGP.PIECE_NO,RECEIVED_DATE FROM PRODUCTION.GIDC_FELT_NRGP,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.GIDC_FELT_NRGP.GATEPASS_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=793 AND CANCELED=0 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT PRODUCTION.GIDC_FELT_NRGP.GATEPASS_NO,PRODUCTION.GIDC_FELT_NRGP.GATEPASS_DATE,PRODUCTION.GIDC_FELT_NRGP.PIECE_NO,RECEIVED_DATE FROM PRODUCTION.GIDC_FELT_NRGP,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.GIDC_FELT_NRGP.GATEPASS_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=793 AND CANCELED=0 ORDER BY PRODUCTION.GIDC_FELT_NRGP.GATEPASS_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT PRODUCTION.GIDC_FELT_NRGP.GATEPASS_NO,PRODUCTION.GIDC_FELT_NRGP.GATEPASS_DATE,PRODUCTION.GIDC_FELT_NRGP.PIECE_NO,RECEIVED_DATE FROM PRODUCTION.GIDC_FELT_NRGP,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.GIDC_FELT_NRGP.GATEPASS_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=793 AND CANCELED=0 ORDER BY PRODUCTION.GIDC_FELT_NRGP.GATEPASS_NO";
            }

            //strSQL="SELECT PRODUCTION.GIDC_FELT_NRGP_HEADER.GATEPASS_NO,PRODUCTION.GIDC_FELT_NRGP_HEADER.GATEPASS_DATE FROM PRODUCTION.GIDC_FELT_NRGP_HEADER,D_COM_DOC_DATA WHERE PRODUCTION.GIDC_FELT_NRGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND PRODUCTION.GIDC_FELT_NRGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND PRODUCTION.GIDC_FELT_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=793";
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast()) {
                    Counter = Counter + 1;
                    clsNRGP_GIDC ObjDoc = new clsNRGP_GIDC();

                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("PIECE_NO", rsTmp.getString("PIECE_NO"));
                    ObjDoc.setAttribute("GATEPASS_NO", rsTmp.getString("GATEPASS_NO"));
                    ObjDoc.setAttribute("GATEPASS_DATE", rsTmp.getString("GATEPASS_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//

                    //Put the prepared user object into list
                    List.put(Long.toString(Counter), ObjDoc);
               
                    rsTmp.next();
            }//Out While

            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
        }

        return List;
    }

    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsNRGP = Stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP_H WHERE COMPANY_ID=" + pCompanyID + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "'");
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
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_NRGP_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "'");

            while (rsTmp.next()) {
                clsNRGP_GIDC ObjNRGP = new clsNRGP_GIDC();

                ObjNRGP.setAttribute("GATEPASS_NO", rsTmp.getString("GATEPASS_NO"));
                ObjNRGP.setAttribute("GATEPASS_DATE", rsTmp.getString("GATEPASS_DATE"));
                ObjNRGP.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjNRGP.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjNRGP.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjNRGP.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjNRGP.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));

                List.put(Integer.toString(List.size() + 1), ObjNRGP);
            }

            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;

        } catch (Exception e) {
            return List;
        }
    }

    public static String getDocStatus(int pCompanyID, String pDocNo) {
        ResultSet rsTmp;
        String strMessage = "";

        try {
            //First check that Document exist
            rsTmp = data.getResult("SELECT GATEPASS_NO,APPROVED,CANCELED FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + pCompanyID + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "'");
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
            rsTmp = stTmp.executeQuery("SELECT GATEPASS_NO FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + pCompanyID + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "'");
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
            rsTmp = data.getResult("SELECT GATEPASS_NO FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + pCompanyID + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "' AND APPROVED=0 AND CANCELED=0");
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

                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.GIDC_FELT_NRGP WHERE COMPANY_ID=" + pCompanyID + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "'");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }

                if (ApprovedDoc) {

                } else {
                    //Remove it from Approval Hierarchy
                    //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pDocNo + "' AND MODULE_ID=793");
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=793");
                }

                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.GIDC_FELT_NRGP SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND GATEPASS_TYPE='MKP' AND GATEPASS_NO='" + pDocNo + "'");

                Cancelled = true;
            }

            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {

        }

        return Cancelled;
    }
    
}
