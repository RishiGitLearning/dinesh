/*
 * 
 *
 * 
 */
package EITLERP.FeltSales.GIDC_SDF.Despatch;

import java.util.HashMap;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.FeltSales.FeltInvReport.NumWord;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.SelectFirstFree;
import EITLERP.clsFirstFree;
import static EITLERP.clsFirstFree.getNextFreeNo;
import javax.swing.JOptionPane;

/**
 * @author Jadeja Rajpalsinh
 */
public class clsDespatchGIDC_SDML {

    public String LastError = "";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int FFNo = 0;

    public HashMap props;
    public boolean Ready = false;
    public HashMap hmFeltGIDCSDFDetails;

    //History Related properties
    public boolean HistoryView = false;

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
     * Creates new DataFeltGroup
     */
    public clsDespatchGIDC_SDML() {
        LastError = "";
        props = new HashMap();
        props.put("DOC_NO", new Variant(""));
        props.put("DOC_DATE", new Variant(""));

        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("UPDATED_BY", new Variant(0));
        props.put("ENTRY_DATE", new Variant(""));
        props.put("APPROVER_REMARKS", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("APPROVED", new Variant(0));
        props.put("REJECTED", new Variant(0));
        props.put("FROM_IP", new Variant(""));

        hmFeltGIDCSDFDetails = new HashMap();
    }

    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML GROUP BY DOC_NO ORDER BY DOC_NO");
            HistoryView = false;
            Ready = true;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public void Close() {
        try {
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Navigation Methods
    public boolean MoveFirst() {
        try {
            resultSet.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MoveNext() {
        try {
            if (resultSet.isAfterLast() || resultSet.isLast()) {
                //Move pointer at last record if it is beyond eof
                resultSet.last();
            } else {
                resultSet.next();
                if (resultSet.isAfterLast()) {
                    resultSet.last();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MovePrevious() {
        try {
            if (resultSet.isFirst() || resultSet.isBeforeFirst()) {
                resultSet.first();
            } else {
                resultSet.previous();
                if (resultSet.isBeforeFirst()) {
                    resultSet.first();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean MoveLast() {
        try {
            resultSet.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert() {
        ResultSet resultSetTemp, resultSetHistory;
        Statement statementTemp, statementHistory;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML_H WHERE DOC_NO='1'");

            setAttribute("DOC_NO", getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 792, 281, true));

            //Now Insert records into production tables
            for (int i = 1; i <= hmFeltGIDCSDFDetails.size(); i++) {
                clsDespatchGIDC_SDMLDetails ObjFeltGIDCSDFDetails = (clsDespatchGIDC_SDMLDetails) hmFeltGIDCSDFDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateInt("SR_NO", i);

                resultSetTemp.updateString("PIECE_NO", ObjFeltGIDCSDFDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltGIDCSDFDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltGIDCSDFDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("STYLE_CODE", ObjFeltGIDCSDFDetails.getAttribute("STYLE_CODE").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltGIDCSDFDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltGIDCSDFDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("GSM", ObjFeltGIDCSDFDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltGIDCSDFDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("ACTUAL_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_LENGTH").getString());
                resultSetTemp.updateString("ACTUAL_WIDTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WIDTH").getString());
                resultSetTemp.updateString("ACTUAL_WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetTemp.updateString("WASTE_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("WASTE_LENGTH").getString());
                resultSetTemp.updateString("WASTE_WEFT", ObjFeltGIDCSDFDetails.getAttribute("WASTE_WEFT").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltGIDCSDFDetails.getAttribute("REMARKS").getString());

                resultSetTemp.updateString("RATE", ObjFeltGIDCSDFDetails.getAttribute("RATE").getString());
                resultSetTemp.updateString("NET_AMOUNT", ObjFeltGIDCSDFDetails.getAttribute("NET_AMOUNT").getString());
                NumWord num = new NumWord();
                String rsInWord = num.convertNumToWord(Math.round(Float.valueOf(ObjFeltGIDCSDFDetails.getAttribute("NET_AMOUNT").getString())));
                resultSetTemp.updateString("NET_AMOUNT_IN_WORDS", rsInWord);

                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY", 0);
                resultSetTemp.updateString("MODIFIED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", 1);

                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateInt("SR_NO", i);

                resultSetHistory.updateString("PIECE_NO", ObjFeltGIDCSDFDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", ObjFeltGIDCSDFDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltGIDCSDFDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("STYLE_CODE", ObjFeltGIDCSDFDetails.getAttribute("STYLE_CODE").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltGIDCSDFDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltGIDCSDFDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("GSM", ObjFeltGIDCSDFDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("SQMTR", ObjFeltGIDCSDFDetails.getAttribute("SQMTR").getString());
                resultSetHistory.updateString("ACTUAL_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_LENGTH").getString());
                resultSetHistory.updateString("ACTUAL_WIDTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WIDTH").getString());
                resultSetHistory.updateString("ACTUAL_WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetHistory.updateString("WASTE_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("WASTE_LENGTH").getString());
                resultSetHistory.updateString("WASTE_WEFT", ObjFeltGIDCSDFDetails.getAttribute("WASTE_WEFT").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltGIDCSDFDetails.getAttribute("REMARKS").getString());

                resultSetHistory.updateString("RATE", ObjFeltGIDCSDFDetails.getAttribute("RATE").getString());
                resultSetHistory.updateString("NET_AMOUNT", ObjFeltGIDCSDFDetails.getAttribute("NET_AMOUNT").getString());
                resultSetHistory.updateString("NET_AMOUNT_IN_WORDS", rsInWord);

                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                resultSetHistory.updateString("FROM_IP", "" + str_split[1]);

                resultSetHistory.insertRow();
            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 792;
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "DOC_NO";

            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status = "A";
                ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            // Update Finishing Date in Order Master Table To confirm that Finishing has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                try {
                    String sql = "";
                    ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO = '" + getAttribute("DOC_NO").getString() + "' ORDER BY DOC_NO,SR_NO");
                    while (rsTemp.next()) {
//                        String challanNo= EITLERP.clsFirstFree.getNextFreeNo(2, 792, 282, true);                
//                        data.Execute("UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML D SET P.GIDC_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "',P.GIDC_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "',P.GIDC_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "',P.GIDC_WASTE_LENGTH='" + rsTemp.getString("WASTE_LENGTH") + "',P.GIDC_WASTE_WEFT='" + rsTemp.getString("WASTE_WEFT") + "',P.GIDC_DELIVERY_CHALLAN_NO='" + challanNo + "',P.GIDC_DELIVERY_CHALLAN_DATE=CURDATE(),P.GIDC_STAGE='GIDC_TO_SDML' WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        data.Execute("UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML D SET P.GIDC_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "',P.GIDC_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "',P.GIDC_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "',P.GIDC_WASTE_LENGTH='" + rsTemp.getString("WASTE_LENGTH") + "',P.GIDC_WASTE_WEFT='" + rsTemp.getString("WASTE_WEFT") + "',P.GIDC_STAGE='GIDC_TO_SDML',P.GIDC_DESPATCH_DATE=CURDATE() WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        
                        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "', WIP_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "', WIP_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "', WIP_PIECE_STAGE='FINISHING', WIP_STATUS='GIDC', WIP_GIDC_STATUS='GIDC_TO_SDML' WHERE WIP_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "', PR_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "', PR_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "', PR_PIECE_STAGE='FINISHING', PR_WIP_STATUS='GIDC', PR_GIDC_STATUS='GIDC_TO_SDML' WHERE PR_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        
                        System.out.println("FINAL UPDATAION UPDATED OF PIECE NO : " + rsTemp.getString("PIECE_NO"));
                        sql = "UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_FELT_RM_STOCK S "
//                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_LENGTH,0),"
//                                + "S.AVAILABLE=S.AVAILABLE-COALESCE(P.GIDC_WASTE_LENGTH,0) "
                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_LENGTH,0) "
                                + " WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "' AND "
                                + " P.GIDC_RMCODE_LENGTH=S.ITEM_CODE";
                        data.Execute(sql);
                        sql = "UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_FELT_RM_STOCK S "
//                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_WEFT,0),"
//                                + "S.AVAILABLE=S.AVAILABLE-COALESCE(P.GIDC_WASTE_WEFT,0) "
                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_WEFT,0) "
                                + " WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "' AND "
                                + " P.GIDC_RMCODE_WEFT=S.ITEM_CODE";
                        data.Execute(sql);
                    }

                    rsTemp.close();
                } catch (Exception e) {
                    LastError = e.getMessage();
                    e.printStackTrace();
                }
            }

            LoadData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {
        ResultSet resultSetTemp, resultSetHistory;
        Statement statementTemp, statementHistory;
//        int revisionNo = 1;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='1'");

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
//            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"'");
//            resultSetHistory.first();
//            if(resultSetHistory.getRow()>0){
//                revisionNo=resultSetHistory.getInt(1);
//                revisionNo++;
//            }
//            
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML_H WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
            RevNo++;

            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");

            resultSetHistory = statementHistory.executeQuery("SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML_H WHERE DOC_NO='1'");

            //Now Update records into production tables
            for (int i = 1; i <= hmFeltGIDCSDFDetails.size(); i++) {
                clsDespatchGIDC_SDMLDetails ObjFeltGIDCSDFDetails = (clsDespatchGIDC_SDMLDetails) hmFeltGIDCSDFDetails.get(Integer.toString(i));

                //Insert records into production data table
                resultSetTemp.moveToInsertRow();

                resultSetTemp.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetTemp.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetTemp.updateInt("SR_NO", i);

                resultSetTemp.updateString("PIECE_NO", ObjFeltGIDCSDFDetails.getAttribute("PIECE_NO").getString());
                resultSetTemp.updateString("PARTY_CODE", ObjFeltGIDCSDFDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("PARTY_NAME", ObjFeltGIDCSDFDetails.getAttribute("PARTY_NAME").getString());
                resultSetTemp.updateString("STYLE_CODE", ObjFeltGIDCSDFDetails.getAttribute("STYLE_CODE").getString());
                resultSetTemp.updateString("LENGTH", ObjFeltGIDCSDFDetails.getAttribute("LENGTH").getString());
                resultSetTemp.updateString("WIDTH", ObjFeltGIDCSDFDetails.getAttribute("WIDTH").getString());
                resultSetTemp.updateString("WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("WEIGHT").getString());
                resultSetTemp.updateString("GSM", ObjFeltGIDCSDFDetails.getAttribute("GSM").getString());
                resultSetTemp.updateString("SQMTR", ObjFeltGIDCSDFDetails.getAttribute("SQMTR").getString());
                resultSetTemp.updateString("ACTUAL_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_LENGTH").getString());
                resultSetTemp.updateString("ACTUAL_WIDTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WIDTH").getString());
                resultSetTemp.updateString("ACTUAL_WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetTemp.updateString("WASTE_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("WASTE_LENGTH").getString());
                resultSetTemp.updateString("WASTE_WEFT", ObjFeltGIDCSDFDetails.getAttribute("WASTE_WEFT").getString());
                resultSetTemp.updateString("REMARKS", ObjFeltGIDCSDFDetails.getAttribute("REMARKS").getString());

                resultSetTemp.updateString("RATE", ObjFeltGIDCSDFDetails.getAttribute("RATE").getString());
                resultSetTemp.updateString("NET_AMOUNT", ObjFeltGIDCSDFDetails.getAttribute("NET_AMOUNT").getString());
                NumWord num = new NumWord();
                String rsInWord = num.convertNumToWord(Math.round(Float.valueOf(ObjFeltGIDCSDFDetails.getAttribute("NET_AMOUNT").getString())));
                resultSetTemp.updateString("NET_AMOUNT_IN_WORDS", rsInWord);

                resultSetTemp.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                resultSetTemp.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateBoolean("APPROVED", false);
                resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
                resultSetTemp.updateBoolean("REJECTED", false);
                resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
                resultSetTemp.updateInt("CANCELED", 0);
                resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED", 1);
                resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();

                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO", RevNo);

                resultSetHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
                resultSetHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
                resultSetHistory.updateInt("SR_NO", i);

                resultSetHistory.updateString("PIECE_NO", ObjFeltGIDCSDFDetails.getAttribute("PIECE_NO").getString());
                resultSetHistory.updateString("PARTY_CODE", ObjFeltGIDCSDFDetails.getAttribute("PARTY_CODE").getString());
                resultSetHistory.updateString("PARTY_NAME", ObjFeltGIDCSDFDetails.getAttribute("PARTY_NAME").getString());
                resultSetHistory.updateString("STYLE_CODE", ObjFeltGIDCSDFDetails.getAttribute("STYLE_CODE").getString());
                resultSetHistory.updateString("LENGTH", ObjFeltGIDCSDFDetails.getAttribute("LENGTH").getString());
                resultSetHistory.updateString("WIDTH", ObjFeltGIDCSDFDetails.getAttribute("WIDTH").getString());
                resultSetHistory.updateString("WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("WEIGHT").getString());
                resultSetHistory.updateString("GSM", ObjFeltGIDCSDFDetails.getAttribute("GSM").getString());
                resultSetHistory.updateString("SQMTR", ObjFeltGIDCSDFDetails.getAttribute("SQMTR").getString());
                resultSetHistory.updateString("ACTUAL_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_LENGTH").getString());
                resultSetHistory.updateString("ACTUAL_WIDTH", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WIDTH").getString());
                resultSetHistory.updateString("ACTUAL_WEIGHT", ObjFeltGIDCSDFDetails.getAttribute("ACTUAL_WEIGHT").getString());
                resultSetHistory.updateString("WASTE_LENGTH", ObjFeltGIDCSDFDetails.getAttribute("WASTE_LENGTH").getString());
                resultSetHistory.updateString("WASTE_WEFT", ObjFeltGIDCSDFDetails.getAttribute("WASTE_WEFT").getString());
                resultSetHistory.updateString("REMARKS", ObjFeltGIDCSDFDetails.getAttribute("REMARKS").getString());

                resultSetTemp.updateString("RATE", ObjFeltGIDCSDFDetails.getAttribute("RATE").getString());
                resultSetTemp.updateString("NET_AMOUNT", ObjFeltGIDCSDFDetails.getAttribute("NET_AMOUNT").getString());
                resultSetTemp.updateString("NET_AMOUNT_IN_WORDS", rsInWord);

                resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED", 1);
                resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                ResultSet rsTmp = data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");

                resultSetHistory.updateString("FROM_IP", "" + str_split[1]);

                resultSetHistory.insertRow();

            }

            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID = 792;
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString());
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "DOC_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML SET REJECTED=0,CHANGED=1 WHERE DOC_NO='" + getAttribute("DOC_NO").getString() + "'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=603 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");

                ObjFeltProductionApprovalFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjFeltProductionApprovalFlow.Status.equals("H")) {
                if (IsRejected) {
                    ObjFeltProductionApprovalFlow.Status = "A";
                    ObjFeltProductionApprovalFlow.To = ObjFeltProductionApprovalFlow.From;
                    ObjFeltProductionApprovalFlow.UpdateFlow();
                }
            } else {
                if (!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError = ObjFeltProductionApprovalFlow.LastError;
                }
            }

            //--------- Approval Flow Update complete -----------
            // Update Finishing Date in Order Master Table To confirm that Finishing has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                ObjFeltProductionApprovalFlow.finalApproved = false;
                try {
                    String sql = "";
                    ResultSet rsTemp = data.getConn().createStatement().executeQuery("SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO = '" + getAttribute("DOC_NO").getString() + "' ORDER BY DOC_NO,SR_NO");
                    while (rsTemp.next()) {
//                        String challanNo= EITLERP.clsFirstFree.getNextFreeNo(2, 792, 282, true);                
//                        data.Execute("UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML D SET P.GIDC_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "',P.GIDC_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "',P.GIDC_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "',P.GIDC_WASTE_LENGTH='" + rsTemp.getString("WASTE_LENGTH") + "',P.GIDC_WASTE_WEFT='" + rsTemp.getString("WASTE_WEFT") + "',P.GIDC_DELIVERY_CHALLAN_NO='" + challanNo + "',P.GIDC_DELIVERY_CHALLAN_DATE=CURDATE(),P.GIDC_STAGE='GIDC_TO_SDML' WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        data.Execute("UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML D SET P.GIDC_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "',P.GIDC_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "',P.GIDC_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "',P.GIDC_WASTE_LENGTH='" + rsTemp.getString("WASTE_LENGTH") + "',P.GIDC_WASTE_WEFT='" + rsTemp.getString("WASTE_WEFT") + "',P.GIDC_STAGE='GIDC_TO_SDML',P.GIDC_DESPATCH_DATE=CURDATE() WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        
                        data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "', WIP_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "', WIP_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "', WIP_PIECE_STAGE='FINISHING', WIP_STATUS='GIDC', WIP_GIDC_STATUS='GIDC_TO_SDML' WHERE WIP_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_ACTUAL_LENGTH='" + rsTemp.getString("ACTUAL_LENGTH") + "', PR_ACTUAL_WIDTH='" + rsTemp.getString("ACTUAL_WIDTH") + "', PR_ACTUAL_WEIGHT='" + rsTemp.getString("ACTUAL_WEIGHT") + "', PR_PIECE_STAGE='FINISHING', PR_WIP_STATUS='GIDC', PR_GIDC_STATUS='GIDC_TO_SDML' WHERE PR_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "'");
                        
                        System.out.println("FINAL UPDATAION UPDATED OF PIECE NO : " + rsTemp.getString("PIECE_NO"));
                        sql = "UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_FELT_RM_STOCK S "
//                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_LENGTH,0),"
//                                + "S.AVAILABLE=S.AVAILABLE-COALESCE(P.GIDC_WASTE_LENGTH,0) "
                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_LENGTH,0) "
                                + " WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "' AND "
                                + " P.GIDC_RMCODE_LENGTH=S.ITEM_CODE";
                        data.Execute(sql);
                        sql = "UPDATE PRODUCTION.GIDC_FELT_PIECE_REGISTER P,PRODUCTION.GIDC_FELT_RM_STOCK S "
//                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_WEFT,0),"
//                                + "S.AVAILABLE=S.AVAILABLE-COALESCE(P.GIDC_WASTE_WEFT,0) "
                                + "SET S.WASTE=S.WASTE+COALESCE(P.GIDC_WASTE_WEFT,0) "
                                + " WHERE P.GIDC_PIECE_NO='" + rsTemp.getString("PIECE_NO") + "' AND "
                                + " P.GIDC_RMCODE_WEFT=S.ITEM_CODE";
                        data.Execute(sql);
                    }

                    rsTemp.close();
                } catch (Exception e) {
                    LastError = e.getMessage();
                    e.printStackTrace();
                }
            }

            setData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /*
     * This routine checks whether the item is deletable or not.
     * Criteria is Approved item cannot be delete,
     * and if not approved then user id is checked whether doucment
     * is created by the user. Only creator can delete the document.
     * After checking it deletes the record of selected production date and document no.
     */
    public boolean CanDelete(String docNo, int userID) {
        if (HistoryView) {
            return false;
        }
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";

        try {
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + docNo + "' AND  APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if (rsTmp.getInt("COUNT") > 0) {  //Group is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=792 AND USER_ID=" + userID + " AND DOC_NO='" + getAttribute("DOC_NO").getString() + "' AND STATUS='W'";
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();

                if (rsTmp.getInt("COUNT") > 0) {
                    strSQL = "DELETE FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + docNo + "'";
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

    /*
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String docNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + docNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=792 AND USER_ID=" + userID + " AND DOC_NO='" + docNo + "' AND STATUS='W'";
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
            e.printStackTrace();
            return false;
        }
    }

    public boolean Filter(String stringFindQuery) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE " + stringFindQuery;
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(strSql);
            if (!resultSet.first()) {
                LoadData();
                Ready = true;
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean setData() {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;
        try {

            //Now Populate the collection, first clear the collection
            hmFeltGIDCSDFDetails.clear();

            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (HistoryView) {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML_H WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "'  AND REVISION_NO=" + RevNo);
            } else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + resultSet.getString("DOC_NO") + "' ORDER BY DOC_NO,SR_NO ");
            }
            resultSetTemp.first();

            while (!resultSetTemp.isAfterLast()) {
                serialNoCounter++;
                clsDespatchGIDC_SDMLDetails ObjFeltGIDCSDFDetails = new clsDespatchGIDC_SDMLDetails();

                if (HistoryView) {
                    RevNo = resultSetTemp.getInt("REVISION_NO");
                    setAttribute("REVISION_NO", resultSetTemp.getInt("REVISION_NO"));
                } else {
                    setAttribute("REVISION_NO", 0);
                    setAttribute("CREATED_BY", resultSetTemp.getInt("CREATED_BY"));
                    setAttribute("CREATED_DATE", resultSetTemp.getString("CREATED_DATE"));
                    setAttribute("MODIFIED_BY", resultSetTemp.getInt("MODIFIED_BY"));
                    setAttribute("MODIFIED_DATE", resultSetTemp.getString("MODIFIED_DATE"));
                    setAttribute("APPROVED", resultSetTemp.getInt("APPROVED"));
                    setAttribute("APPROVED_DATE", resultSetTemp.getString("APPROVED_DATE"));
                    setAttribute("REJECTED", resultSetTemp.getBoolean("REJECTED"));
                    setAttribute("REJECTED_DATE", resultSetTemp.getString("REJECTED_DATE"));
                    setAttribute("CANCELED", resultSetTemp.getInt("CANCELED"));
                }

                setAttribute("DOC_NO", resultSetTemp.getString("DOC_NO"));
                setAttribute("DOC_DATE", resultSetTemp.getString("DOC_DATE"));
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));

                ObjFeltGIDCSDFDetails.setAttribute("SR_NO", resultSetTemp.getString("SR_NO"));
                ObjFeltGIDCSDFDetails.setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                ObjFeltGIDCSDFDetails.setAttribute("PARTY_CODE", resultSetTemp.getString("PARTY_CODE"));
                ObjFeltGIDCSDFDetails.setAttribute("PARTY_NAME", resultSetTemp.getString("PARTY_NAME"));
                ObjFeltGIDCSDFDetails.setAttribute("STYLE_CODE", resultSetTemp.getString("STYLE_CODE"));
                ObjFeltGIDCSDFDetails.setAttribute("LENGTH", resultSetTemp.getString("LENGTH"));
                ObjFeltGIDCSDFDetails.setAttribute("WIDTH", resultSetTemp.getString("WIDTH"));
                ObjFeltGIDCSDFDetails.setAttribute("WEIGHT", resultSetTemp.getString("WEIGHT"));
                ObjFeltGIDCSDFDetails.setAttribute("GSM", resultSetTemp.getString("GSM"));
                ObjFeltGIDCSDFDetails.setAttribute("SQMTR", resultSetTemp.getString("SQMTR"));
                ObjFeltGIDCSDFDetails.setAttribute("ACTUAL_LENGTH", resultSetTemp.getString("ACTUAL_LENGTH"));
                ObjFeltGIDCSDFDetails.setAttribute("ACTUAL_WIDTH", resultSetTemp.getString("ACTUAL_WIDTH"));
                ObjFeltGIDCSDFDetails.setAttribute("ACTUAL_WEIGHT", resultSetTemp.getString("ACTUAL_WEIGHT"));
                ObjFeltGIDCSDFDetails.setAttribute("WASTE_LENGTH", resultSetTemp.getString("WASTE_LENGTH"));
                ObjFeltGIDCSDFDetails.setAttribute("WASTE_WEFT", resultSetTemp.getString("WASTE_WEFT"));
                ObjFeltGIDCSDFDetails.setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));
                ObjFeltGIDCSDFDetails.setAttribute("RATE", resultSetTemp.getString("RATE"));
                ObjFeltGIDCSDFDetails.setAttribute("NET_AMOUNT", resultSetTemp.getString("NET_AMOUNT"));

                hmFeltGIDCSDFDetails.put(Integer.toString(serialNoCounter), ObjFeltGIDCSDFDetails);
                resultSetTemp.next();
            }
            resultSetTemp.close();
            statementTemp.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    public boolean ShowHistory(String docNo) {
        Ready = false;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML_H WHERE DOC_NO='" + docNo + "'");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public static HashMap getHistoryList(String dpDate, String dpNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML_H WHERE DOC_NO='" + dpNo + "' GROUP BY REVISION_NO ");

            while (rsTmp.next()) {
                clsDespatchGIDC_SDML ObjFeltGroup = new clsDespatchGIDC_SDML();

                ObjFeltGroup.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjFeltGroup.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjFeltGroup.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjFeltGroup.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjFeltGroup.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                ObjFeltGroup.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), ObjFeltGroup);
            }
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }

    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";

        HashMap List = new HashMap();
        int Counter = 0;

        try {

            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=792 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=792 AND CANCELED=0 ORDER BY DOC_NO";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT H.DOC_NO,H.DOC_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML H,PRODUCTION.FELT_PROD_DOC_DATA D WHERE H.DOC_NO=D.DOC_NO AND USER_ID=" + pUserID + " AND STATUS='W' AND MODULE_ID=792 AND CANCELED=0 ORDER BY DOC_NO";
            }

            ResultSet rsTmp = data.getConn().createStatement().executeQuery(strSQL);

            Counter = 0;
            while (rsTmp.next()) {

                Counter = Counter + 1;
                clsDespatchGIDC_SDML ObjDoc = new clsDespatchGIDC_SDML();

                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO", rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE", rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Integer.toString(Counter), ObjDoc);
            }
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }

    public String getDocumentNo(String dpNo) {
        return dpNo;
    }

    public static void CancelDoc(String pDocNo) {
        ResultSet rsTmp = null;

        if (CanCancel(pDocNo)) {

            boolean Approved = false;

            try {
                rsTmp = data.getResult("SELECT APPROVED FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + pDocNo + "' ");
                rsTmp.first();

                if (rsTmp.getRow() > 0) {
                    Approved = rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();

                if (!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='" + pDocNo + "' AND MODULE_ID=792");
                }
                data.Execute("UPDATE PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='" + pDocNo + "'");

            } catch (Exception e) {

            }
        }

    }

    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            System.out.println("SELECT DOC_NO FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");
            rsTmp = stTmp.executeQuery("SELECT DOC_NO FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE DOC_NO='" + pDocNo + "'  AND APPROVED=0 AND CANCELED=0");

            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                CanCancel = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CanCancel;
    }

    // checks piece no already exist in db
    public boolean checkPieceNoInDB(String pPieceNo) {
        int count = data.getIntValueFromDB("SELECT COUNT(PIECE_NO) FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE PIECE_NO='" + pPieceNo + "' AND CANCELED=0");
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPieceNoInDB(String pPieceNo, String pDocDate) {
        int count = data.getIntValueFromDB("SELECT COUNT(PIECE_NO) FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE PIECE_NO='" + pPieceNo + "' AND DOC_DATE='" + EITLERPGLOBAL.formatDateDB(pDocDate) + "'");
        if (count >= 1) {
            int counter = 0;
            try {
                Connection Conn = data.getConn();
                Statement stTmp = Conn.createStatement();
                ResultSet rsTmp = stTmp.executeQuery("SELECT DOC_NO,DOC_DATE FROM PRODUCTION.GIDC_DESPATCH_FELT_FOR_SDML WHERE PIECE_NO='" + pPieceNo + "' AND DOC_DATE='" + EITLERPGLOBAL.formatDateDB(pDocDate) + "'");
                while (rsTmp.next()) {
                    if (rsTmp.getString("DOC_DATE").equals(EITLERPGLOBAL.formatDateDB(pDocDate))) {
                        counter++;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (counter == 1 && count >= 2) {
                return true;
            } else if (counter == 1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static HashMap getPieceList() {
        HashMap List = new HashMap();
        String SQL = "SELECT * FROM PRODUCTION.GIDC_FELT_PIECE_REGISTER WHERE GIDC_STAGE='READY_TO_DESPATCH' ORDER BY GIDC_PIECE_NO";
        try {
            ResultSet rsList = data.getResult(SQL);
            int Counter = 0;
            while (!rsList.isAfterLast()) {
                clsDespatchGIDC_SDMLDetails ObjItem = new clsDespatchGIDC_SDMLDetails();
                Counter++;
                ObjItem.setAttribute("SR_NO", Counter);
                ObjItem.setAttribute("PIECE_NO", rsList.getString("GIDC_PIECE_NO"));
                ObjItem.setAttribute("PARTY_CODE", rsList.getString("GIDC_PARTY_CODE"));
                ObjItem.setAttribute("STYLE_CODE", rsList.getString("GIDC_STYLE"));
                ObjItem.setAttribute("LENGTH", rsList.getString("GIDC_LENGTH"));
                ObjItem.setAttribute("WIDTH", rsList.getString("GIDC_WIDTH"));
                ObjItem.setAttribute("WEIGHT", rsList.getString("GIDC_THORITICAL_WEIGHT"));
                ObjItem.setAttribute("GSM", rsList.getString("GIDC_GSM"));
                ObjItem.setAttribute("SQMTR", rsList.getString("GIDC_SQMTR"));
                List.put(Integer.toString(Counter), ObjItem);
                rsList.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List;
        }
        return List;
    }
}
