/*
 * Created on Jul 29, 2017,
 */

package EITLERP.Sales.DebitMemoReceiptMapping;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

 
/**
 *
 * @author  Daxesh Prajapati
 * @version 
 */

    
 
public class clsDebitMemoReceiptMapping {

    private HashMap props;    
    public boolean Ready = false;

    private ResultSet resultSet;
    private static Connection connection;
    private Statement statement;
    public String LastError = "";

    //History Related properties
    public boolean HistoryView = false;
    private String historyAmendDate = "";
    private String historyAmendID = "";
    private static int ModuleID = 99;
    
    public HashMap colMappingDetail=new HashMap();
    
    public Variant getAttribute(String PropName)
    {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,double Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,float Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,boolean Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    /** Creates new clsMRItem */
    public clsDebitMemoReceiptMapping() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("DEBITMEMO_NO",new Variant(""));
        props.put("DEBITMEMO_DATE",new Variant(""));
        props.put("RECEIPT_VOUCHER_NO",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("INVOICE_DUE_DATE",new Variant(""));
        props.put("VALUE_DATE",new Variant(""));
        props.put("DAYS",new Variant(0));        
        props.put("DEBITNOTE_VOUCHER_NO",new Variant(""));
        props.put("DEBITNOTE_VOUCHER_DATE",new Variant(""));
        props.put("DEBIT_NOTE_AMOUNT",new Variant(0));
        props.put("INVOICE_AMOUNT",new Variant(""));
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_BY", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(""));
        props.put("APPROVED_BY", new Variant(""));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("CANCELLED", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(""));
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        props.put("APPROVER_REMARKS", new Variant(0));
        props.put("ENTRY_DATE", new Variant(0));
        props.put("PIECE_NO", new Variant(""));
        props.put("PURPOSE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("DEBITMEMO_TYPE", new Variant(""));
    }
    
    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    //        resultSet = statement.executeQuery("SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING where DEBITMEMO_NO like 'DM%'  ORDER BY DEBITMEMO_NO");
            resultSet = statement.executeQuery("SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING  ORDER BY DEBITMEMO_NO,DEBITMEMO_DATE");
    
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
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
//            if (HistoryView) {
//                setHistoryData(historyAmendDate, historyAmendID);
//            } else {
                setData();
//            }
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean setData() {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;
        try {

            setAttribute("REVISION_NO", 0);
            
            setAttribute("DEBITMEMO_NO", resultSet.getString("DEBITMEMO_NO"));
            setAttribute("DEBITMEMO_DATE", resultSet.getString("DEBITMEMO_DATE"));
            
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            setAttribute("RECEIPT_VOUCHER_NO", resultSet.getString("RECEIPT_VOUCHER_NO"));
            setAttribute("INVOICE_NO", resultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE", resultSet.getString("INVOICE_DATE"));
            setAttribute("INVOICE_AMOUNT", resultSet.getString("INVOICE_AMOUNT"));
            setAttribute("INVOICE_DUE_DATE", resultSet.getString("INVOICE_DUE_DATE"));
            setAttribute("VALUE_DATE", resultSet.getString("VALUE_DATE"));
            setAttribute("DAYS", resultSet.getString("DAYS"));
            setAttribute("IGST_PER", resultSet.getString("IGST_PER"));
            setAttribute("IGST_AMT", resultSet.getString("IGST_AMT"));
            setAttribute("CGST_PER", resultSet.getString("CGST_PER"));
            setAttribute("CGST_AMT", resultSet.getString("CGST_AMT"));
            setAttribute("SGST_PER", resultSet.getString("SGST_PER"));
            setAttribute("SGST_AMT", resultSet.getString("SGST_AMT"));
            setAttribute("INTEREST_PER", resultSet.getString("INTEREST_PER"));
            setAttribute("INTEREST_AMT", resultSet.getString("INTEREST_AMT"));
            setAttribute("DB_PARTY_CODE", resultSet.getString("DB_PARTY_CODE"));
            setAttribute("MAIN_ACCOUNT_CODE", resultSet.getString("MAIN_ACCOUNT_CODE"));
            setAttribute("INVOICE_AMOUNT", resultSet.getString("INVOICE_AMOUNT"));
            setAttribute("DEBITNOTE_VOUCHER_NO", resultSet.getString("DEBITNOTE_VOUCHER_NO"));
            setAttribute("DEBITNOTE_VOUCHER_DATE", resultSet.getString("DEBITNOTE_VOUCHER_DATE"));
            setAttribute("DEBIT_NOTE_AMOUNT", resultSet.getString("DEBIT_NOTE_AMOUNT"));
            
            setAttribute("PIECE_NO", resultSet.getString("PIECE_NO"));
            setAttribute("PURPOSE", resultSet.getString("PURPOSE"));
            setAttribute("REMARKS", resultSet.getString("REMARKS"));
            
            
            setAttribute("CREATED_BY", resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED", resultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE", resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED", resultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE", resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELLED", resultSet.getInt("CANCELLED"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            //setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));

            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            } 
            
            
            
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    public boolean setHistoryData(String pProductionDate, String pDocNo) {
        ResultSet resultSetTemp;
        Statement statementTemp;
        int serialNoCounter = 0;
        int RevNo = 0;
        try {

            //Now Populate the collection, first clear the collection
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING_H WHERE DEBITMEMO_NO='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("DEBITMEMO_NO", resultSetTemp.getString("DEBITMEMO_NO"));
                setAttribute("DEBITMEMO_DATE", resultSetTemp.getString("DEBITMEMO_DATE"));
                setAttribute("MODIFIED_BY", resultSetTemp.getString("MODIFIED_BY"));
                
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));
                setAttribute("RECEIPT_VOUCHER_NO", resultSetTemp.getString("RECEIPT_VOUCHER_NO"));
                setAttribute("INVOICE_NO", resultSetTemp.getString("INVOICE_NO"));
                setAttribute("INVOICE_DATE", resultSetTemp.getString("INVOICE_DATE"));
                setAttribute("INVOICE_DUE_DATE", resultSetTemp.getString("INVOICE_DUE_DATE"));
                setAttribute("VALUE_DATE", resultSetTemp.getString("VALUE_DATE"));
                setAttribute("DAYS", resultSetTemp.getString("DAYS"));
                setAttribute("IGST_PER", resultSetTemp.getString("IGST_PER"));
                setAttribute("IGST_AMT", resultSetTemp.getString("IGST_AMT"));
                setAttribute("CGST_PER", resultSetTemp.getString("CGST_PER"));
                setAttribute("CGST_AMT", resultSetTemp.getString("CGST_AMT"));
                setAttribute("SGST_PER", resultSetTemp.getString("SGST_PER"));
                setAttribute("SGST_AMT", resultSetTemp.getString("SGST_AMT"));
                setAttribute("INTEREST_PER", resultSetTemp.getString("INTEREST_PER"));
                setAttribute("INTEREST_AMT", resultSetTemp.getString("INTEREST_AMT"));
                setAttribute("DB_PARTY_CODE", resultSetTemp.getString("DB_PARTY_CODE"));
                setAttribute("MAIN_ACCOUNT_CODE", resultSetTemp.getString("MAIN_ACCOUNT_CODE"));
                setAttribute("INVOICE_AMOUNT", resultSetTemp.getString("INVOICE_AMOUNT"));
                setAttribute("DEBITNOTE_VOUCHER_NO", resultSetTemp.getString("DEBITNOTE_VOUCHER_NO"));
                setAttribute("DEBITNOTE_VOUCHER_DATE", resultSetTemp.getString("DEBITNOTE_VOUCHER_DATE"));
                setAttribute("DEBIT_NOTE_AMOUNT", resultSetTemp.getString("DEBIT_NOTE_AMOUNT"));
                setAttribute("PIECE_NO", resultSetTemp.getString("PIECE_NO"));
                setAttribute("PURPOSE", resultSetTemp.getString("PURPOSE"));
                setAttribute("REMARKS", resultSetTemp.getString("REMARKS"));

            }
            resultSetTemp.close();
            statementTemp.close();
            resultSet.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public static HashMap getHistoryList(String stringProductionDate, String DocumentNo) {
        HashMap hmHistoryList = new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            stTmp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            System.out.println("SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING_H WHERE DEBITMEMO_NO='" + DocumentNo + "'");
            rsTmp = stTmp.executeQuery("SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING_H WHERE DEBITMEMO_NO='" + DocumentNo + "'");
            while (rsTmp.next()) {
                clsDebitMemoReceiptMapping objDM = new clsDebitMemoReceiptMapping();

                objDM.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                objDM.setAttribute("MODIFIED_BY", rsTmp.getString("MODIFIED_BY"));
                objDM.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                objDM.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                objDM.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                objDM.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), objDM);
            }

            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
     public boolean IsEditable(String DocumentNo, int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        try {
            if (HistoryView) {
                return false;
            }
            tmpStmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE DEBITMEMO_NO='" + DocumentNo + "' AND APPROVED=1";
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) {  //Item is Approved
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleID + " AND DOC_NO='" + DocumentNo + "' AND USER_ID=" + userID + " AND STATUS='W'";
                System.out.println("cls : "+strSQL);
                //strSQL = "SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=" + ModuleID + " AND USER_ID=" + userID + " AND DOC_NO='" + DocumentNo + "' AND STATUS='W'";
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
     
     public boolean Insert() {
        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH;
        Statement statementTemp, statementHistory, stHeader, stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            
            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE DEBITMEMO_NO=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING_H WHERE DEBITMEMO_NO=''");

            rsHeader.first();
            rsHeader.moveToInsertRow();
            rsHeader.updateString("SR_NO", 1+"");
            String new_doc_number  = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID, clsFirstFree.getDefaultFirstFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID), false);
            rsHeader.updateString("DEBITMEMO_NO", new_doc_number);
            rsHeader.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEBITMEMO_DATE").getString()));
            rsHeader.updateString("RECEIPT_VOUCHER_NO", getAttribute("RECEIPT_VOUCHER_NO").getString());
            rsHeader.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsHeader.updateString("INVOICE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            rsHeader.updateString("INVOICE_DUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DUE_DATE").getString()));
            rsHeader.updateString("VALUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));
            rsHeader.updateString("DAYS", getAttribute("DAYS").getString());
            rsHeader.updateString("IGST_PER", getAttribute("IGST_PER").getString());
            rsHeader.updateString("IGST_AMT", getAttribute("IGST_AMT").getString());
            rsHeader.updateString("CGST_PER", getAttribute("CGST_PER").getString());
            rsHeader.updateString("CGST_AMT", getAttribute("CGST_AMT").getString());
            rsHeader.updateString("SGST_PER", getAttribute("SGST_PER").getString());
            rsHeader.updateString("SGST_AMT", getAttribute("SGST_AMT").getString());
            rsHeader.updateString("INTEREST_PER", getAttribute("INTEREST_PER").getString());
            rsHeader.updateString("INTEREST_AMT", getAttribute("INTEREST_AMT").getString());
            rsHeader.updateString("DB_PARTY_CODE", getAttribute("DB_PARTY_CODE").getString());
            rsHeader.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHeader.updateString("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getString());
            rsHeader.updateString("DEBITNOTE_VOUCHER_NO", getAttribute("DEBITNOTE_VOUCHER_NO").getString());
            rsHeader.updateString("DEBITNOTE_VOUCHER_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("DEBITNOTE_VOUCHER_DATE").getString()));
            rsHeader.updateString("DEBIT_NOTE_AMOUNT", getAttribute("DEBIT_NOTE_AMOUNT").getString());
            
            
            rsHeader.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsHeader.updateString("PURPOSE", getAttribute("PURPOSE").getString());
            rsHeader.updateString("REMARKS", getAttribute("REMARKS").getString());
          
            
            rsHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY", 0);
            rsHeader.updateString("MODIFIED_DATE", "0000-00-00");
            rsHeader.updateBoolean("APPROVED", false);
            rsHeader.updateString("APPROVED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CANCELLED", false);
            rsHeader.updateBoolean("REJECTED", false);
            rsHeader.updateString("REJECTED_DATE", "0000-00-00");
            rsHeader.updateBoolean("CHANGED", false);
            rsHeader.updateString("CHANGED_DATE", "0000-00-00");
            rsHeader.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsHeader.updateString("DEBITMEMO_TYPE", "MANUAL");
            rsHeader.insertRow();

            rsHeaderH.first();
            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateInt("REVISION_NO", 1);
            rsHeaderH.updateString("SR_NO", 1+"");
            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("DEBITMEMO_NO", new_doc_number);
            rsHeaderH.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEBITMEMO_DATE").getString()));
            rsHeaderH.updateString("RECEIPT_VOUCHER_NO", getAttribute("RECEIPT_VOUCHER_NO").getString());
            rsHeaderH.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsHeaderH.updateString("INVOICE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            rsHeaderH.updateString("INVOICE_DUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DUE_DATE").getString()));
            rsHeaderH.updateString("VALUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));
            rsHeaderH.updateString("DAYS", getAttribute("DAYS").getString());
            rsHeaderH.updateString("IGST_PER", getAttribute("IGST_PER").getString());
            rsHeaderH.updateString("IGST_AMT", getAttribute("IGST_AMT").getString());
            rsHeaderH.updateString("CGST_PER", getAttribute("CGST_PER").getString());
            rsHeaderH.updateString("CGST_AMT", getAttribute("CGST_AMT").getString());
            rsHeaderH.updateString("SGST_PER", getAttribute("SGST_PER").getString());
            rsHeaderH.updateString("SGST_AMT", getAttribute("SGST_AMT").getString());
            rsHeaderH.updateString("INTEREST_PER", getAttribute("INTEREST_PER").getString());
            rsHeaderH.updateString("INTEREST_AMT", getAttribute("INTEREST_AMT").getString());
            rsHeaderH.updateString("DB_PARTY_CODE", getAttribute("DB_PARTY_CODE").getString());
            rsHeaderH.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHeaderH.updateString("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getString());
            rsHeaderH.updateString("DEBITNOTE_VOUCHER_NO", getAttribute("DEBITNOTE_VOUCHER_NO").getString());
            rsHeaderH.updateString("DEBITNOTE_VOUCHER_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("DEBITNOTE_VOUCHER_DATE").getString()));
            rsHeaderH.updateString("DEBIT_NOTE_AMOUNT", getAttribute("DEBIT_NOTE_AMOUNT").getString());
            
            rsHeaderH.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsHeaderH.updateString("PURPOSE", getAttribute("PURPOSE").getString());
            rsHeaderH.updateString("REMARKS", getAttribute("REMARKS").getString());
            
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateInt("MODIFIED_BY", 0);
            rsHeaderH.updateString("MODIFIED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("APPROVED", false);
            rsHeaderH.updateString("APPROVED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CANCELLED", false);
            rsHeaderH.updateBoolean("REJECTED", false);
            rsHeaderH.updateString("REJECTED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CHANGED", false);
            rsHeaderH.updateString("CHANGED_DATE", "0000-00-00");

            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
            rsHeaderH.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsHeaderH.updateString("DEBITMEMO_TYPE", "MANUAL");
            rsHeaderH.insertRow();

            //======== Update the Approval Flow =========
            ApprovalFlow ObjFeltProductionApprovalFlow = new ApprovalFlow();
            ObjFeltProductionApprovalFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo = new_doc_number;
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName = "FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING";
            ObjFeltProductionApprovalFlow.IsCreator = true;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "DEBITMEMO_NO";
            
            //JOptionPane.showMessageDialog(null, "TO : "+(int)getAttribute("TO").getVal()+", Approval Status = "+(String)getAttribute("APPROVAL_STATUS").getObj());
            if ("A".equals((String) getAttribute("APPROVAL_STATUS").getObj())) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("PU_NO").getInt();
//                    String Message = "Document No : "+getAttribute("PU_NO").getInt()+" is added in your PENDING DOCUMENT"
//                             + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
//                 
//                    String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int)getAttribute("TO").getVal());
//
//                    System.out.println("USERID : "+(int)getAttribute("TO").getVal()+", send_to : "+send_to);
//                    try{
//                        JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
            }

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
            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F")) {

            }

            LoadData();

            
            return true;

        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    //Updates current record
    public boolean Update() {

        ResultSet resultSetTemp, resultSetHistory, rsHeader, rsHeaderH, rsRegister;
        Statement statementTemp, statementHistory, stHeader, stHeaderH, stRegister;
        int revisionNo;
        try {
            // Production data connection

            // Production data history connection
            statementHistory = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get the Maximum Revision No in History Table.
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING_H WHERE DEBITMEMO_NO='1'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL WHERE PU_NO='"+getAttribute("PU_NO").getString()+"'");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING_H WHERE DEBITMEMO_NO='1'");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("DEBITMEMO_NO", getAttribute("DEBITMEMO_NO").getString());
            resultSet.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEBITMEMO_DATE").getString()));
            resultSet.updateString("RECEIPT_VOUCHER_NO", getAttribute("RECEIPT_VOUCHER_NO").getString());
            resultSet.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            resultSet.updateString("INVOICE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            resultSet.updateString("INVOICE_DUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DUE_DATE").getString()));
            resultSet.updateString("VALUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));
            resultSet.updateString("DAYS", getAttribute("DAYS").getString());
            resultSet.updateString("IGST_PER", getAttribute("IGST_PER").getString());
            resultSet.updateString("IGST_AMT", getAttribute("IGST_AMT").getString());
            resultSet.updateString("CGST_PER", getAttribute("CGST_PER").getString());
            resultSet.updateString("CGST_AMT", getAttribute("CGST_AMT").getString());
            resultSet.updateString("SGST_PER", getAttribute("SGST_PER").getString());
            resultSet.updateString("SGST_AMT", getAttribute("SGST_AMT").getString());
            resultSet.updateString("INTEREST_PER", getAttribute("INTEREST_PER").getString());
            resultSet.updateString("INTEREST_AMT", getAttribute("INTEREST_AMT").getString());
            resultSet.updateString("DB_PARTY_CODE", getAttribute("DB_PARTY_CODE").getString());
            resultSet.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            resultSet.updateString("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getString());
            resultSet.updateString("DEBITNOTE_VOUCHER_NO", getAttribute("DEBITNOTE_VOUCHER_NO").getString());
            resultSet.updateString("DEBITNOTE_VOUCHER_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("DEBITNOTE_VOUCHER_DATE").getString()));
            resultSet.updateString("DEBIT_NOTE_AMOUNT", getAttribute("DEBIT_NOTE_AMOUNT").getString());

            resultSet.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            resultSet.updateString("PURPOSE", getAttribute("PURPOSE").getString());
            resultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            
            resultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            //resultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //resultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB((String)getAttribute("CREATED_DATE").getObj()));
            resultSet.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());

            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                resultSet.updateBoolean("APPROVED", true);
               // resultSet.updateString("APPROVED_BY", EITLERPGLOBAL.gNewUserID + "");
                resultSet.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                resultSet.updateBoolean("APPROVED", false);
                resultSet.updateString("APPROVED_DATE", "0000-00-00");
            }
            resultSet.updateBoolean("CANCELLED", false);
            resultSet.updateBoolean("REJECTED", false);
            resultSet.updateString("REJECTED_DATE", "0000-00-00");
            //resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("CHANGED", true);
            resultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            
            try {
                resultSet.updateRow();
            } catch (Exception e) {
                System.out.println("Header Updation Failed : " + e.getMessage());
            }
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING_H WHERE DEBITMEMO_NO='" + getAttribute("DEBITMEMO_NO").getString()+ "'");

            RevNo++;
            rsHeaderH.moveToInsertRow();
            rsHeaderH.updateString("REVISION_NO", RevNo + "");
            rsHeaderH.updateString("SR_NO", 1+"");
            rsHeaderH.updateString("DEBITMEMO_NO", getAttribute("DEBITMEMO_NO").getString());
            rsHeaderH.updateString("DEBITMEMO_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DEBITMEMO_DATE").getString()));
            rsHeaderH.updateString("RECEIPT_VOUCHER_NO", getAttribute("RECEIPT_VOUCHER_NO").getString());
            rsHeaderH.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsHeaderH.updateString("INVOICE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DATE").getString()));
            rsHeaderH.updateString("INVOICE_DUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("INVOICE_DUE_DATE").getString()));
            rsHeaderH.updateString("VALUE_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("VALUE_DATE").getString()));
            rsHeaderH.updateString("DAYS", getAttribute("DAYS").getString());
            rsHeaderH.updateString("IGST_PER", getAttribute("IGST_PER").getString());
            rsHeaderH.updateString("IGST_AMT", getAttribute("IGST_AMT").getString());
            rsHeaderH.updateString("CGST_PER", getAttribute("CGST_PER").getString());
            rsHeaderH.updateString("CGST_AMT", getAttribute("CGST_AMT").getString());
            rsHeaderH.updateString("SGST_PER", getAttribute("SGST_PER").getString());
            rsHeaderH.updateString("SGST_AMT", getAttribute("SGST_AMT").getString());
            rsHeaderH.updateString("INTEREST_PER", getAttribute("INTEREST_PER").getString());
            rsHeaderH.updateString("INTEREST_AMT", getAttribute("INTEREST_AMT").getString());
            rsHeaderH.updateString("DB_PARTY_CODE", getAttribute("DB_PARTY_CODE").getString());
            rsHeaderH.updateString("MAIN_ACCOUNT_CODE", getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHeaderH.updateString("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getString());
            rsHeaderH.updateString("DEBITNOTE_VOUCHER_NO", getAttribute("DEBITNOTE_VOUCHER_NO").getString());
            rsHeaderH.updateString("DEBITNOTE_VOUCHER_DATE",  EITLERPGLOBAL.formatDateDB(getAttribute("DEBITNOTE_VOUCHER_DATE").getString()));
            rsHeaderH.updateString("DEBIT_NOTE_AMOUNT", getAttribute("DEBIT_NOTE_AMOUNT").getString());
            
            rsHeaderH.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsHeaderH.updateString("PURPOSE", getAttribute("PURPOSE").getString());
            rsHeaderH.updateString("REMARKS", getAttribute("REMARKS").getString());
            
            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            if (getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                rsHeaderH.updateBoolean("APPROVED", true);
                rsHeaderH.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            } else {
                rsHeaderH.updateBoolean("APPROVED", false);
                rsHeaderH.updateString("APPROVED_DATE", "0000-00-00");
            }
            rsHeaderH.updateBoolean("CANCELLED", false);
            rsHeaderH.updateBoolean("REJECTED", false);
            rsHeaderH.updateString("REJECTED_DATE", "0000-00-00");
            rsHeaderH.updateBoolean("CHANGED", true);
            rsHeaderH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateInt("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            rsHeaderH.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
           
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
            rsHeaderH.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsHeaderH.insertRow();

            

            //======== Update the Approval Flow =========
            ApprovalFlow ObjFeltProductionApprovalFlow = new ApprovalFlow();
            ObjFeltProductionApprovalFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFeltProductionApprovalFlow.ModuleID = ModuleID; 
            ObjFeltProductionApprovalFlow.DocNo = getAttribute("DEBITMEMO_NO").getString()+ "";
            ObjFeltProductionApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName = "FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING";
            ObjFeltProductionApprovalFlow.IsCreator = false;
            ObjFeltProductionApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName = "DEBITMEMO_NO";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo = true;
            }
           

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                
                data.Execute("UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING SET REJECTED=0,CHANGED=1 WHERE DEBITMEMO_NO ='" + getAttribute("DEBITMEMO_NO").getString() + "'");
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

            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjFeltProductionApprovalFlow.Status.equals("F") ) {

                //ObjFeltProductionApprovalFlow.finalApproved = false;
            }

            setData();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    public boolean Filter(String stringFindQuery) {
        Ready = false;
        try {
            // String strSql = "SELECT DISTINCT AMEND_DATE FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE  " + stringFindQuery + " ORDER BY CN_DATE";
            String strSql = "SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE DEBITMEMO_NO!='' " + stringFindQuery + "";
            System.out.println("STR SQL UPDATION DEBIT : " + strSql);
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
                strSQL="SELECT FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_NO,FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING,D_COM_DOC_DATA WHERE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_NO=D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=99 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_NO,FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING,D_COM_DOC_DATA WHERE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_NO=D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=99 ORDER BY FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_NO,FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING,D_COM_DOC_DATA WHERE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_NO=D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=99 ORDER BY FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING.DEBITMEMO_NO";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("DEBITMEMO_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsDebitMemoReceiptMapping ObjDoc=new clsDebitMemoReceiptMapping();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DEBITMEMO_NO",rsTmp.getString("DEBITMEMO_NO"));
                    ObjDoc.setAttribute("DEBITMEMO_DATE",rsTmp.getString("DEBITMEMO_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
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
    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            System.out.println("SELECT DEBITMEMO_NO FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE DEBITMEMO_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELLED=0");
            rsTmp=stTmp.executeQuery("SELECT DEBITMEMO_NO FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE DEBITMEMO_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELLED=0");
            
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
    public static void CancelDoc(String pAmendNo) {
        ResultSet rsTmp=null;
        
        if(CanCancel(pAmendNo)) {
            
            boolean Approved=false;
            //PRODUCTION.FELT_SALES_PIECE_AMEND_APPROVAL_STOCK_HEADER WHERE PIECE_AMEND_STOCK_NO
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING WHERE DEBITMEMO_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=99");
		    data.Execute("DELETE FROM DINESHMILLS.D_COM_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=99");
                }
                data.Execute("UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DEBITMEMO_NO='"+pAmendNo+"'");
              
            }
            catch(Exception e) {
                
            }
        }
        
    }
}
