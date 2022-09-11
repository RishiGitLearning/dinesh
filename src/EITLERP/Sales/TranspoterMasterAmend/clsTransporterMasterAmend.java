/*
 * Created on Jul 29, 2017,
 */

package EITLERP.Sales.TranspoterMasterAmend;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

 
/**
 *
 * @author  Daxesh Prajapati
 * @version 
 */

    
 
public class clsTransporterMasterAmend {

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
    private static int ModuleID = 767;
    
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
    public clsTransporterMasterAmend() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        
        props.put("AMEND_ID",new Variant(""));
        props.put("TRANSPORTER_ID",new Variant(""));
        props.put("TRANSPORTER_NAME",new Variant(""));
        props.put("ADDRESS",new Variant(""));
        props.put("CITY_ID",new Variant(""));
        props.put("PIN_CODE",new Variant(""));
        props.put("PHONE_NO",new Variant(""));
        props.put("CONTACT_PERSON",new Variant(""));
        props.put("EMAIL",new Variant(""));
        props.put("TR_LET_NO",new Variant(""));
        props.put("TR_LET_DATE",new Variant(""));
        props.put("LOT_NO",new Variant(""));
        props.put("STATE",new Variant(""));
        props.put("GSTIN_NO",new Variant(""));
        props.put("STATE_CODE",new Variant(""));
        props.put("STATE_GST_CODE",new Variant(""));
                
                
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
        
        props.put("REMARKS", new Variant(""));
        
    }
    
    public boolean LoadData() {
        Ready = false;
        try {
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    //        resultSet = statement.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER where AMEND_ID like 'DM%'  ORDER BY AMEND_ID");
            resultSet = statement.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND  ORDER BY AMEND_ID");
    
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
        
        int RevNo = 0;
        try {

            setAttribute("REVISION_NO", 0);
            
            setAttribute("TRANSPORTER_ID", resultSet.getString("TRANSPORTER_ID"));
            setAttribute("AMEND_ID", resultSet.getString("AMEND_ID"));
            setAttribute("HIERARCHY_ID", resultSet.getInt("HIERARCHY_ID"));
            setAttribute("COMPANY_ID",resultSet.getString("COMPANY_ID"));
            setAttribute("TRANSPORTER_NAME",resultSet.getString("TRANSPORTER_NAME"));
            setAttribute("ADDRESS",resultSet.getString("ADDRESS"));
            setAttribute("CITY_ID",resultSet.getString("CITY_ID"));
            setAttribute("PIN_CODE",resultSet.getString("PIN_CODE"));
            setAttribute("PHONE_NO",resultSet.getString("PHONE_NO"));
            setAttribute("CONTACT_PERSON",resultSet.getString("CONTACT_PERSON"));
            setAttribute("EMAIL",resultSet.getString("EMAIL"));
            setAttribute("TR_LET_NO",resultSet.getString("TR_LET_NO"));
            setAttribute("TR_LET_DATE",resultSet.getString("TR_LET_DATE"));
            setAttribute("LOT_NO",resultSet.getString("LOT_NO"));
            setAttribute("STATE",resultSet.getString("STATE"));
            setAttribute("GSTIN_NO",resultSet.getString("GSTIN_NO"));
            setAttribute("STATE_CODE",resultSet.getString("STATE_CODE"));
            setAttribute("STATE_GST_CODE",resultSet.getString("STATE_GST_CODE"));
            
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND_H WHERE AMEND_ID='" + pDocNo + "'");

            while (resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("AMEND_ID", resultSetTemp.getString("AMEND_ID"));
                setAttribute("TRANSPORTER_ID", resultSetTemp.getString("TRANSPORTER_ID"));
                
                setAttribute("MODIFIED_BY", resultSetTemp.getString("MODIFIED_BY"));
                
                setAttribute("HIERARCHY_ID", resultSetTemp.getInt("HIERARCHY_ID"));
                
                setAttribute("COMPANY_ID",resultSetTemp.getString("COMPANY_ID"));
                setAttribute("TRANSPORTER_NAME",resultSetTemp.getString("TRANSPORTER_NAME"));
                setAttribute("ADDRESS",resultSetTemp.getString("ADDRESS"));
                setAttribute("CITY_ID",resultSetTemp.getString("CITY_ID"));
                setAttribute("PIN_CODE",resultSetTemp.getString("PIN_CODE"));
                setAttribute("CONTACT_PERSON",resultSetTemp.getString("CONTACT_PERSON"));
                setAttribute("EMAIL",resultSetTemp.getString("EMAIL"));
                setAttribute("TR_LET_NO",resultSetTemp.getString("TR_LET_NO"));
                setAttribute("TR_LET_DATE",resultSetTemp.getString("TR_LET_DATE"));
                setAttribute("LOT_NO",resultSetTemp.getString("LOT_NO"));
                setAttribute("STATE",resultSetTemp.getString("STATE"));
                setAttribute("GSTIN_NO",resultSetTemp.getString("GSTIN_NO"));
                setAttribute("STATE_CODE",resultSetTemp.getString("STATE_CODE"));
                setAttribute("STATE_GST_CODE",resultSetTemp.getString("STATE_GST_CODE"));

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
            System.out.println("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND_H WHERE AMEND_ID='" + DocumentNo + "'");
            rsTmp = stTmp.executeQuery("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND_H WHERE AMEND_ID='" + DocumentNo + "'");
            while (rsTmp.next()) {
                clsTransporterMasterAmend objTM = new clsTransporterMasterAmend();

                objTM.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                objTM.setAttribute("MODIFIED_BY", rsTmp.getString("MODIFIED_BY"));
                objTM.setAttribute("MODIFIED_DATE", rsTmp.getString("MODIFIED_DATE"));
                objTM.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                objTM.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                objTM.setAttribute("FROM_IP", rsTmp.getString("FROM_IP"));

                hmHistoryList.put(Integer.toString(hmHistoryList.size() + 1), objTM);
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND WHERE AMEND_ID='" + DocumentNo + "' AND APPROVED=1";
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
        ResultSet  rsHeader, rsHeaderH;
        Statement  stHeader, stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            
            stHeader = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeader = stHeader.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND WHERE AMEND_ID=''");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND_H WHERE AMEND_ID=''");

            rsHeader.first();
            rsHeader.moveToInsertRow();
            String new_doc_number  = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID, clsFirstFree.getDefaultFirstFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID), false);
            rsHeader.updateString("AMEND_ID", new_doc_number);
            
            rsHeader.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHeader.updateString("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getString());
            rsHeader.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsHeader.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHeader.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            rsHeader.updateString("PIN_CODE",getAttribute("PIN_CODE").getString());
            rsHeader.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            rsHeader.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            rsHeader.updateString("EMAIL",getAttribute("EMAIL").getString());
            rsHeader.updateString("TR_LET_NO",getAttribute("TR_LET_NO").getString());
            rsHeader.updateString("TR_LET_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TR_LET_DATE").getString()));
            rsHeader.updateString("LOT_NO",getAttribute("LOT_NO").getString());
            rsHeader.updateString("STATE",getAttribute("STATE").getString());
            rsHeader.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            rsHeader.updateString("STATE_CODE",getAttribute("STATE_CODE").getString());
            rsHeader.updateString("STATE_GST_CODE",getAttribute("STATE_GST_CODE").getString());
                   
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
            
            rsHeader.insertRow();

            rsHeaderH.first();
            rsHeaderH.moveToInsertRow();

            rsHeaderH.updateInt("REVISION_NO", 1);
           
            rsHeaderH.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("AMEND_ID", new_doc_number);
            
            
            rsHeaderH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHeaderH.updateString("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getString());
            rsHeaderH.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsHeaderH.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHeaderH.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            rsHeaderH.updateString("PIN_CODE",getAttribute("PIN_CODE").getString());
            rsHeaderH.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            rsHeaderH.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            rsHeaderH.updateString("EMAIL",getAttribute("EMAIL").getString());
            rsHeaderH.updateString("TR_LET_NO",getAttribute("TR_LET_NO").getString());
            rsHeaderH.updateString("TR_LET_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TR_LET_DATE").getString()));
            rsHeaderH.updateString("LOT_NO",getAttribute("LOT_NO").getString());
            rsHeaderH.updateString("STATE",getAttribute("STATE").getString());
            rsHeaderH.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            rsHeaderH.updateString("STATE_CODE",getAttribute("STATE_CODE").getString());
            rsHeaderH.updateString("STATE_GST_CODE",getAttribute("STATE_GST_CODE").getString());
            
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateLong("CREATED_BY", (long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateInt("MODIFIED_BY", 0);
            rsHeaderH.updateInt("UPDATED_BY", 0);
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
           
            rsHeaderH.insertRow();

            //======== Update the Approval Flow =========
            ApprovalFlow ObjApprovalFlow = new ApprovalFlow();
            ObjApprovalFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjApprovalFlow.ModuleID = ModuleID; //Felt Weaving Entry
            ObjApprovalFlow.DocNo = new_doc_number;
            ObjApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjApprovalFlow.TableName = "DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND";
            ObjApprovalFlow.IsCreator = true;
            ObjApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjApprovalFlow.FieldName = "AMEND_ID";
            
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

            if (ObjApprovalFlow.Status.equals("H")) {
                ObjApprovalFlow.Status = "A";
                ObjApprovalFlow.To = ObjApprovalFlow.From;
                ObjApprovalFlow.UpdateFlow();
            } else {
                if (!ObjApprovalFlow.UpdateFlow()) {
                    LastError = ObjApprovalFlow.LastError;
                }
            }

            //--------- Approval Flow Update complete -----------
            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjApprovalFlow.Status.equals("F")) {

                data.Execute("UPDATE  DINESHMILLS.D_SAL_TRANSPORTER_MASTER SET TRANSPORTER_NAME ='" + getAttribute("TRANSPORTER_NAME").getString() + "',ADDRESS ='" + getAttribute("ADDRESS").getString() + "',CITY_ID ='" + getAttribute("CITY_ID").getString() + "',PIN_CODE ='" + getAttribute("PIN_CODE").getString() + "',CONTACT_PERSON ='" + getAttribute("CONTACT_PERSON").getString() + "',EMAIL ='" + getAttribute("EMAIL").getString() + "',TR_LET_NO ='" + getAttribute("TR_LET_NO").getString() + "',TR_LET_DATE ='" + EITLERPGLOBAL.formatDateDB(getAttribute("TR_LET_DATE").getString()) + "',LOT_NO ='" + getAttribute("LOT_NO").getString() + "',STATE ='" + getAttribute("STATE").getString() + "',GSTIN_NO ='" + getAttribute("GSTIN_NO").getString() + "',STATE_CODE ='" + getAttribute("STATE_CODE").getString() + "',STATE_GST_CODE ='" + getAttribute("STATE_GST_CODE").getString() + "' WHERE TRANSPORTER_ID ='" + getAttribute("TRANSPORTER_ID").getString() + "'");
                
                //ObjApprovalFlow.finalApproved = false;
                
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
            resultSetHistory = statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND_H WHERE AMEND_ID='1'");
            resultSetHistory.first();
            if (resultSetHistory.getRow() > 0) {
                revisionNo = resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER_TEMP_DETAIL WHERE PU_NO='"+getAttribute("PU_NO").getString()+"'");

            stHeaderH = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHeaderH = stHeaderH.executeQuery("SELECT * FROM  DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND_H WHERE AMEND_ID='1'");

            //Now Update records into FELT_CN_TEMP_HEADER tables
            resultSet.updateString("AMEND_ID", getAttribute("AMEND_ID").getString());
            
            resultSet.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            resultSet.updateString("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getString());
            resultSet.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            resultSet.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            resultSet.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            resultSet.updateString("PIN_CODE",getAttribute("PIN_CODE").getString());
            resultSet.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            resultSet.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            resultSet.updateString("EMAIL",getAttribute("EMAIL").getString());
            resultSet.updateString("TR_LET_NO",getAttribute("TR_LET_NO").getString());
            resultSet.updateString("TR_LET_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TR_LET_DATE").getString()));
            resultSet.updateString("LOT_NO",getAttribute("LOT_NO").getString());
            resultSet.updateString("STATE",getAttribute("STATE").getString());
            resultSet.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            resultSet.updateString("STATE_CODE",getAttribute("STATE_CODE").getString());
            resultSet.updateString("STATE_GST_CODE",getAttribute("STATE_GST_CODE").getString());
            
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
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND_H WHERE AMEND_ID='" + getAttribute("AMEND_ID").getString()+ "'");

            RevNo++;
            rsHeaderH.moveToInsertRow();
            rsHeaderH.updateString("REVISION_NO", RevNo + "");
            
            rsHeaderH.updateString("AMEND_ID", getAttribute("AMEND_ID").getString());
           
            
            rsHeaderH.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHeaderH.updateString("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getString());
            rsHeaderH.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsHeaderH.updateString("ADDRESS",getAttribute("ADDRESS").getString());
            rsHeaderH.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            rsHeaderH.updateString("PIN_CODE",getAttribute("PIN_CODE").getString());
            rsHeaderH.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            rsHeaderH.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            rsHeaderH.updateString("EMAIL",getAttribute("EMAIL").getString());
            rsHeaderH.updateString("TR_LET_NO",getAttribute("TR_LET_NO").getString());
            rsHeaderH.updateString("TR_LET_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TR_LET_DATE").getString()));
            rsHeaderH.updateString("LOT_NO",getAttribute("LOT_NO").getString());
            rsHeaderH.updateString("STATE",getAttribute("STATE").getString());
            rsHeaderH.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            rsHeaderH.updateString("STATE_CODE",getAttribute("STATE_CODE").getString());
            rsHeaderH.updateString("STATE_GST_CODE",getAttribute("STATE_GST_CODE").getString());
            
            
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
            rsHeaderH.updateInt("UPDATED_BY", EITLERPGLOBAL.gNewUserID);
            rsHeaderH.updateString("MODIFIED_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
           
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
            rsHeaderH.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsHeaderH.insertRow();

            

            //======== Update the Approval Flow =========
            ApprovalFlow ObjApprovalFlow = new ApprovalFlow();
            ObjApprovalFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjApprovalFlow.ModuleID = ModuleID; 
            ObjApprovalFlow.DocNo = getAttribute("AMEND_ID").getString()+ "";
            ObjApprovalFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            ObjApprovalFlow.From = (int) getAttribute("FROM").getVal();
            ObjApprovalFlow.To = (int) getAttribute("TO").getVal();
            ObjApprovalFlow.Status = (String) getAttribute("APPROVAL_STATUS").getString();
            ObjApprovalFlow.TableName = "DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND";
            ObjApprovalFlow.IsCreator = false;
            ObjApprovalFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjApprovalFlow.Remarks = getAttribute("REJECTED_REMARKS").getString();
            ObjApprovalFlow.FieldName = "AMEND_ID";

            if (getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjApprovalFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjApprovalFlow.ExplicitSendTo = true;
            }
           

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                
                data.Execute("UPDATE DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND SET REJECTED=0,CHANGED=1 WHERE AMEND_ID ='" + getAttribute("AMEND_ID").getString() + "'");
                ObjApprovalFlow.IsCreator = true;
            }
            //==========================================//

            if (ObjApprovalFlow.Status.equals("H")) {
                if (IsRejected) {
                    ObjApprovalFlow.Status = "A";
                    ObjApprovalFlow.To = ObjApprovalFlow.From;
                    ObjApprovalFlow.UpdateFlow();
                }
            } else {
                if (!ObjApprovalFlow.UpdateFlow()) {
                    LastError = ObjApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            // Update  in Order Master Table To confirm that Weaving has completed
            if (ObjApprovalFlow.Status.equals("F") ) {

                
                data.Execute("UPDATE  DINESHMILLS.D_SAL_TRANSPORTER_MASTER SET TRANSPORTER_NAME ='" + getAttribute("TRANSPORTER_NAME").getString() + "',ADDRESS ='" + getAttribute("ADDRESS").getString() + "',CITY_ID ='" + getAttribute("CITY_ID").getString() + "',PIN_CODE ='" + getAttribute("PIN_CODE").getString() + "',CONTACT_PERSON ='" + getAttribute("CONTACT_PERSON").getString() + "',EMAIL ='" + getAttribute("EMAIL").getString() + "',TR_LET_NO ='" + getAttribute("TR_LET_NO").getString() + "',TR_LET_DATE ='" + EITLERPGLOBAL.formatDateDB(getAttribute("TR_LET_DATE").getString()) + "',LOT_NO ='" + getAttribute("LOT_NO").getString() + "',STATE ='" + getAttribute("STATE").getString() + "',GSTIN_NO ='" + getAttribute("GSTIN_NO").getString() + "',STATE_CODE ='" + getAttribute("STATE_CODE").getString() + "',STATE_GST_CODE ='" + getAttribute("STATE_GST_CODE").getString() + "' WHERE TRANSPORTER_ID ='" + getAttribute("TRANSPORTER_ID").getString() + "'");
                
                //ObjApprovalFlow.finalApproved = false;
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
            String strSql = "SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND WHERE AMEND_ID!='' " + stringFindQuery + "";
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
                strSQL="SELECT DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.AMEND_ID,DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.CREATED_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND,D_COM_DOC_DATA WHERE DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=767 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.AMEND_ID,DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.CREATED_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND,D_COM_DOC_DATA WHERE DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=767 ORDER BY DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.CREATED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.AMEND_ID,DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.CREATED_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND,D_COM_DOC_DATA WHERE DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=767 ORDER BY DINESHMILLS.D_SAL_TRANSPORTER_MASTER_AMEND.AMEND_ID";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("CREATED_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsTransporterMasterAmend ObjDoc=new clsTransporterMasterAmend();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("AMEND_ID",rsTmp.getString("AMEND_ID"));
                    ObjDoc.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
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
}
