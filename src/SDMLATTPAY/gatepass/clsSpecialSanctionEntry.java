/*
 * clsTimeCorrectionEntry.java
 *
 * Created on April 15, 2013, 5:23 PM
 */

package SDMLATTPAY.gatepass;

import EITLERP.Production.FeltNeedling.*;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import javax.swing.JOptionPane;

/**
 *
 * @author Ashutosh
 * @version
 */

public class clsSpecialSanctionEntry {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Production Needling Details Collection
    public HashMap hmTimeCorrectionDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyProductionDate="";
    private String historyDocumentNo="";
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
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
    
    /** Creates new DataFeltProductionNeedling */
    public clsSpecialSanctionEntry() {
        LastError = "";
        props=new HashMap();
        props.put("SPECIAL_SANCTION_DOC_NO", new Variant(""));
        props.put("SPECIAL_SANCTION_DOC_DATE", new Variant(""));
        props.put("CORRECTION_TYPE", new Variant(""));
        
        
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("APPROVED", new Variant(""));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(""));
        props.put("CANCELED_DATE", new Variant(""));
        props.put("CHANGED", new Variant(""));
        props.put("CHANGED_DATE", new Variant(""));
        props.put("FROM_IP", new Variant(""));
        props.put("PREFIX", new Variant(""));

        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        hmTimeCorrectionDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //resultSet=statement.executeQuery("SELECT DISTINCT SPECIAL_SANCTION_DOC_NO  FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE PROD_DEPT = 'NEEDLING' AND PROD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROD_DATE");
            resultSet=statement.executeQuery("SELECT DISTINCT SPECIAL_SANCTION_DOC_NO FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND SPECIAL_SANCTION_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY SPECIAL_SANCTION_DOC_DATE");
            HistoryView=false;
            MoveLast();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public void Close() {
        try {
            statement.close();
            resultSet.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            resultSet.first();
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
            else setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MoveNext() {
        try {
            if(resultSet.isAfterLast()||resultSet.isLast()) {
                //Move pointer at last record if it is beyond eof
                resultSet.last();
            }
            else {
                resultSet.next();
                if(resultSet.isAfterLast()) {
                    resultSet.last();
                }
            }
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
            else setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MovePrevious() {
        try {
            if(resultSet.isFirst()||resultSet.isBeforeFirst()) {
                resultSet.first();
            }
            else {
                resultSet.previous();
                if(resultSet.isBeforeFirst()) {
                    resultSet.first();
                }
            }
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
            else setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            resultSet.last();
            if(HistoryView) setHistoryData(historyProductionDate, historyDocumentNo);
            else setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Insert() {
        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;
        try {
            // Production data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO=''");
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SPECIAL_SANCTION_H WHERE SPECIAL_SANCTION_DOC_NO=''");
            
            //Now Insert records into production tables
            for(int i=1;i<=hmTimeCorrectionDetails.size();i++) {
                clsTimeCorrectionDetails objTimeCorrectionDetails=(clsTimeCorrectionDetails) hmTimeCorrectionDetails.get(Integer.toString(i));
                
                //Insert records into production data table
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("SPECIAL_SANCTION_DOC_NO", getAttribute("SPECIAL_SANCTION_DOC_NO").getString());
                resultSetTemp.updateString("SPECIAL_SANCTION_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SPECIAL_SANCTION_DOC_DATE").getString()));                
                resultSetTemp.updateString("CORRECTION_TYPE", getAttribute("CORRECTION_TYPE").getString());
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("EMP_CODE",objTimeCorrectionDetails.getAttribute("EMP_CODE").getString());
                resultSetTemp.updateString("LC_TIME",objTimeCorrectionDetails.getAttribute("LC_TIME").getString());
                resultSetTemp.updateString("A_DATE", EITLERPGLOBAL.formatDateDB(objTimeCorrectionDetails.getAttribute("A_DATE").getString()));
                resultSetTemp.updateString("FIRST_HALF",objTimeCorrectionDetails.getAttribute("FIRST_HALF").getString());
                resultSetTemp.updateString("SECOND_HALF",objTimeCorrectionDetails.getAttribute("SECOND_HALF").getString());
                resultSetTemp.updateString("SANCTION_REMARKS",objTimeCorrectionDetails.getAttribute("SANCTION_REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetTemp.updateInt("MODIFIED_BY",0);
                resultSetTemp.updateString("MODIFIED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateString("SPECIAL_SANCTION_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SPECIAL_SANCTION_DOC_DATE").getString()));
                resultSetHistory.updateString("SPECIAL_SANCTION_DOC_NO", getAttribute("SPECIAL_SANCTION_DOC_NO").getString());
                resultSetHistory.updateString("CORRECTION_TYPE", getAttribute("CORRECTION_TYPE").getString());                
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("EMP_CODE",objTimeCorrectionDetails.getAttribute("EMP_CODE").getString());
                resultSetHistory.updateString("LC_TIME",objTimeCorrectionDetails.getAttribute("LC_TIME").getString());
                //resultSetHistory.updateFloat("A_DATE", (float)EITLERPGLOBAL.round(objTimeCorrectionDetails.getAttribute("WEIGHT").getVal(),2));
                resultSetHistory.updateString("A_DATE", EITLERPGLOBAL.formatDateDB(objTimeCorrectionDetails.getAttribute("A_DATE").getString()));
                resultSetHistory.updateString("FIRST_HALF",objTimeCorrectionDetails.getAttribute("FIRST_HALF").getString());
                resultSetHistory.updateString("SECOND_HALF",objTimeCorrectionDetails.getAttribute("SECOND_HALF").getString());
                resultSetHistory.updateString("SANCTION_REMARKS",objTimeCorrectionDetails.getAttribute("SANCTION_REMARKS").getString());
                //resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                ResultSet rsTmp=data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");
            
                resultSetHistory.updateString("FROM_IP",""+str_split[1]);
                
                
                resultSetHistory.insertRow();
            }
            
            //======== Update the Approval Flow =========
            SDMLATTPAY.ApprovalFlow objFlow = new SDMLATTPAY.ApprovalFlow();
            objFlow.ModuleID=819; //Time correction entry
            objFlow.DocNo=getAttribute("SPECIAL_SANCTION_DOC_NO").getString();
            objFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            objFlow.From=(int)getAttribute("FROM").getVal();
            objFlow.To=(int)getAttribute("TO").getVal();
            objFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            objFlow.TableName="ATT_SPECIAL_SANCTION";
            objFlow.IsCreator=true;
            objFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            objFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            objFlow.FieldName="SPECIAL_SANCTION_DOC_NO";
            objFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            
            if(objFlow.Status.equals("H")) {
                objFlow.Status="A";
                objFlow.To=objFlow.From;
                objFlow.UpdateFlow();
            }
            else {
                if(!objFlow.UpdateFlow()) {
                    LastError=objFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            LoadData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;        
        int revisionNo =1;
        try {
            //Data connection
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO=''");
            
            //Data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_SPECIAL_SANCTION_H WHERE SPECIAL_SANCTION_DOC_NO='"+getAttribute("SPECIAL_SANCTION_DOC_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from data table before insert
            statementHistory.execute("DELETE FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO='"+getAttribute("SPECIAL_SANCTION_DOC_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SPECIAL_SANCTION_H WHERE SPECIAL_SANCTION_DOC_NO=''");
            
            //Now Update records into production tables
            for(int i=1;i<=hmTimeCorrectionDetails.size();i++) {
                clsTimeCorrectionDetails objTimeCorrectionDetails=(clsTimeCorrectionDetails) hmTimeCorrectionDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("SPECIAL_SANCTION_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SPECIAL_SANCTION_DOC_DATE").getString()));
                resultSetTemp.updateString("SPECIAL_SANCTION_DOC_NO", getAttribute("SPECIAL_SANCTION_DOC_NO").getString());
                resultSetTemp.updateString("CORRECTION_TYPE", getAttribute("CORRECTION_TYPE").getString());                
                resultSetTemp.updateInt("SR_NO",i);
                resultSetTemp.updateString("EMP_CODE",objTimeCorrectionDetails.getAttribute("EMP_CODE").getString());
                resultSetTemp.updateString("LC_TIME",objTimeCorrectionDetails.getAttribute("LC_TIME").getString());
                resultSetTemp.updateString("A_DATE", EITLERPGLOBAL.formatDateDB(objTimeCorrectionDetails.getAttribute("A_DATE").getString()));
                resultSetTemp.updateString("FIRST_HALF",objTimeCorrectionDetails.getAttribute("FIRST_HALF").getString());
                resultSetTemp.updateString("SECOND_HALF",objTimeCorrectionDetails.getAttribute("SECOND_HALF").getString());
                resultSetTemp.updateString("SANCTION_REMARKS",objTimeCorrectionDetails.getAttribute("SANCTION_REMARKS").getString());
                resultSetTemp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetTemp.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                resultSetTemp.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                resultSetTemp.updateString("MODIFIED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
             if(getAttribute("APPROVAL_STATUS").getString().equals("F"))
            {
                resultSetTemp.updateBoolean("APPROVED",true);
                //resultSet.updateString("APPROVED_BY",EITLERPGLOBAL.gNewUserID+"");
                resultSetTemp.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            }
            else
            {
                resultSetTemp.updateBoolean("APPROVED",false);
                resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
            }
                //resultSetTemp.updateBoolean("APPROVED",false);
                //resultSetTemp.updateString("APPROVED_DATE","0000-00-00");
                resultSetTemp.updateBoolean("REJECTED",false);
                resultSetTemp.updateString("REJECTED_DATE","0000-00-00");
                resultSetTemp.updateInt("CANCELED",0);
                resultSetTemp.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetTemp.updateInt("CHANGED",1);
                resultSetTemp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetTemp.insertRow();
                
                //Insert records into production data history table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",revisionNo);
                resultSetHistory.updateString("SPECIAL_SANCTION_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("SPECIAL_SANCTION_DOC_DATE").getString()));
                resultSetHistory.updateString("SPECIAL_SANCTION_DOC_NO", getAttribute("SPECIAL_SANCTION_DOC_NO").getString());
                resultSetHistory.updateString("CORRECTION_TYPE", getAttribute("CORRECTION_TYPE").getString());                
                resultSetHistory.updateInt("SR_NO",i);
                resultSetHistory.updateString("EMP_CODE",objTimeCorrectionDetails.getAttribute("EMP_CODE").getString());
                resultSetHistory.updateString("LC_TIME",objTimeCorrectionDetails.getAttribute("LC_TIME").getString());
                resultSetHistory.updateString("A_DATE", EITLERPGLOBAL.formatDateDB(objTimeCorrectionDetails.getAttribute("A_DATE").getString()));
                resultSetHistory.updateString("FIRST_HALF",objTimeCorrectionDetails.getAttribute("FIRST_HALF").getString());
                resultSetHistory.updateString("SECOND_HALF",objTimeCorrectionDetails.getAttribute("SECOND_HALF").getString());
                resultSetHistory.updateString("SANCTION_REMARKS",objTimeCorrectionDetails.getAttribute("SANCTION_REMARKS").getString());
                resultSetHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
                resultSetHistory.updateString("ENTRY_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
                resultSetHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
                resultSetHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetHistory.updateInt("CHANGED",1);
                resultSetHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                ResultSet rsTmp=data.getResult("SELECT USER()");
                rsTmp.first();
                String str = rsTmp.getString(1);
                String str_split[] = str.split("@");
            
                resultSetHistory.updateString("FROM_IP",""+str_split[1]);
                
                
                resultSetHistory.insertRow();
                
                
                try {
                    if ("F".equals(getAttribute("APPROVAL_STATUS").getString())) {
                        if (getAttribute("CORRECTION_TYPE").getString().contains("SPECIAL SANCTION")) {
                            String strupdate = "UPDATE SDMLATTPAY.ATT_DATA_DAILY_SUMMARY SET "
                                    + "LATE_COMING_HRS_OLD=LATE_COMING_HRS, "
                                    + "PRESENT_FIRST_OLD=PRESENT_FIRST, "
                                    + "PRESENT_SECOND_OLD=PRESENT_SECOND "
                                    + "WHERE EMPID='"+objTimeCorrectionDetails.getAttribute("EMP_CODE").getString()+"' AND PUNCHDATE='"+EITLERPGLOBAL.formatDateDB(objTimeCorrectionDetails.getAttribute("A_DATE").getString())+"'";
                            System.out.println(strupdate);
                            data.Execute(strupdate);                            
                            String strupdatenew="UPDATE SDMLATTPAY.ATT_DATA_DAILY_SUMMARY SET "
                                    + "LATE_COMING_HRS='"+objTimeCorrectionDetails.getAttribute("LC_TIME").getString()+"', "
                                    + "PRESENT_FIRST='"+objTimeCorrectionDetails.getAttribute("FIRST_HALF").getString()+"', "
                                    + "PRESENT_SECOND='"+objTimeCorrectionDetails.getAttribute("SECOND_HALF").getString()+"' "
                                    + "WHERE EMPID='"+objTimeCorrectionDetails.getAttribute("EMP_CODE").getString()+"' AND PUNCHDATE='"+EITLERPGLOBAL.formatDateDB(objTimeCorrectionDetails.getAttribute("A_DATE").getString())+"'";
                            System.out.println(strupdatenew);
                            data.Execute(strupdatenew);                            
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }                
               
            }
            
            //======== Update the Approval Flow =========
            SDMLATTPAY.ApprovalFlow objFlow = new SDMLATTPAY.ApprovalFlow();
            
            objFlow.ModuleID=819; //Special Sanction Entry
            objFlow.DocNo=getAttribute("SPECIAL_SANCTION_DOC_NO").getString();
            //objFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            objFlow.DocDate=EITLERPGLOBAL.formatDateDB(getAttribute("SPECIAL_SANCTION_DOC_DATE").getString());
            objFlow.From=(int)getAttribute("FROM").getVal();
            objFlow.To=(int)getAttribute("TO").getVal();
            objFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            objFlow.TableName="SDMLATTPAY.ATT_SPECIAL_SANCTION";
            objFlow.IsCreator=false;
            objFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            objFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            objFlow.FieldName="SPECIAL_SANCTION_DOC_NO";
            objFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                objFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                objFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE SDMLATTPAY.ATT_SPECIAL_SANCTION SET REJECTED=0,CHANGED=1 WHERE SPECIAL_SANCTION_DOC_NO='"+getAttribute("SPECIAL_SANCTION_DOC_NO").getString()+"'");
                //Remove Old Records from DOCUMENT APPROVAL TABLE
                //data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=819 AND DOC_NO='"+getAttribute("SPECIAL_SANCTION_DOC_NO").getString()+"'");
                
                objFlow.IsCreator=true;
            }
            //==========================================//
            
            if(objFlow.Status.equals("H")) {
                if(IsRejected) {
                    objFlow.Status="A";
                    objFlow.To=objFlow.From;
                    objFlow.UpdateFlow();
                }
            }
            else {
                if(!objFlow.UpdateFlow()) {
                    LastError=objFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------

            
            setData();
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * This routine checks whether the item is deletable or not.
     * Criteria is Approved item cannot be delete,
     * and if not approved then user id is checked whether doucment
     * is created by the user. Only creator can delete the document.
     * After checking it deletes the record of selected production date and document no.
     */
    public boolean CanDelete(String documentNo,String stringDocDate,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=819 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE "
                    + " AND SPECIAL_SANCTION_DOC_DATE= '"+ EITLERPGLOBAL.formatDateDB(stringDocDate) +"'"
                    + " AND SPECIAL_SANCTION_DOC_NO='" + documentNo + "'";
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                }
                else {
                    return false;
                }
            }
        }catch(Exception e) {
            LastError = "Error occured while deleting."+e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * This routine checks and returns whether the item is editable or not.
     * Criteria is Approved item cannot be changed and if not approved then user id is
     * checked whether doucment is waiting for his approval.
     */
    public boolean IsEditable(String DocumentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO='"+ DocumentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=819 AND USER_ID="+Integer.toString(userID)+" AND DOC_NO='"+ DocumentNo +"' AND STATUS='W'";
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
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Filter(String stringFindQuery) {
        Ready=false;
        try {
            String strSql = "SELECT DISTINCT SPECIAL_SANCTION_DOC_NO FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE " + stringFindQuery + "ORDER BY SPECIAL_SANCTION_DOC_DATE";
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            if(!resultSet.first()) {
                LoadData();
                Ready=true;
                return false;
            }
            else {
                Ready=true;
                MoveLast();
                return true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean setData() {
    
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        
        
        try {
            /*
            if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
                setAttribute("SPECIAL_SANCTION_DOC_NO",resultSet.getString("SPECIAL_SANCTION_DOC_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
                setAttribute("SPECIAL_SANCTION_DOC_NO","");
            }
            */
            
            setAttribute("REVISION_NO",0);
            //setAttribute("SPECIAL_SANCTION_DOC_NO",resultSet.getString("SPECIAL_SANCTION_DOC_NO"));
            
            //if(resultSet!=null){
            //System.out.println(resultSet.getString("SPECIAL_SANCTION_DOC_NO"));
            //setAttribute("SPECIAL_SANCTION_DOC_NO",resultSet.getString("SPECIAL_SANCTION_DOC_NO"));
            //Now Populate the collection, first clear the collection
            hmTimeCorrectionDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);            
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO='"+resultSet.getString("SPECIAL_SANCTION_DOC_NO")+"' ORDER BY SR_NO");
            //resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_SPECIAL_SANCTION ORDER BY SPECIAL_SANCTION_DOC_NO,SR_NO");
            //if(resultSetTemp.next()){
            //    if(resultSetTemp.getRow()>0){
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("SPECIAL_SANCTION_DOC_NO",resultSetTemp.getString("SPECIAL_SANCTION_DOC_NO"));
                setAttribute("SPECIAL_SANCTION_DOC_DATE",resultSetTemp.getString("SPECIAL_SANCTION_DOC_DATE"));
                setAttribute("CORRECTION_TYPE",resultSetTemp.getString("CORRECTION_TYPE"));
                setAttribute("CREATED_BY",resultSetTemp.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED",resultSetTemp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED",resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("CANCELED",resultSetTemp.getInt("CANCELED"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsTimeCorrectionDetails objTimeCorrectionDetails = new clsTimeCorrectionDetails();
                
                objTimeCorrectionDetails.setAttribute("EMP_CODE",resultSetTemp.getString("EMP_CODE"));
                objTimeCorrectionDetails.setAttribute("A_DATE",resultSetTemp.getString("A_DATE"));
                objTimeCorrectionDetails.setAttribute("LC_TIME",resultSetTemp.getString("LC_TIME"));
                objTimeCorrectionDetails.setAttribute("FIRST_HALF",resultSetTemp.getString("FIRST_HALF"));
                objTimeCorrectionDetails.setAttribute("SECOND_HALF",resultSetTemp.getString("SECOND_HALF"));
                objTimeCorrectionDetails.setAttribute("SANCTION_REMARKS",resultSetTemp.getString("SANCTION_REMARKS"));
               
                
                hmTimeCorrectionDetails.put(Integer.toString(serialNoCounter),objTimeCorrectionDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            //}
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
           
    }
    
    public boolean setHistoryData(String pDate,String pDocNo) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        try {
            RevNo=resultSet.getInt("REVISION_NO");
            setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            
            //Now Populate the collection, first clear the collection
            hmTimeCorrectionDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.TIME_CORRECTION_H WHERE SPECIAL_SANCTION_DOC_DATE='"+ pDate+"' AND SPECIAL_SANCTION_DOC_NO='"+pDocNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("SPECIAL_SANCTION_DOC_NO",resultSetTemp.getString("SPECIAL_SANCTION_DOC_NO"));
                setAttribute("SPECIAL_SANCTION_DOC_DATE",resultSetTemp.getString("SPECIAL_SANCTION_DOC_DATE"));
                setAttribute("UPDATED_BY",resultSetTemp.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetTemp.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsTimeCorrectionDetails ObjTimeCorrectionDetails = new clsTimeCorrectionDetails();
                
                
                ObjTimeCorrectionDetails.setAttribute("EMP_CODE",resultSetTemp.getString("EMP_CODE"));
                ObjTimeCorrectionDetails.setAttribute("A_DATE",resultSetTemp.getString("A_DATE"));
                ObjTimeCorrectionDetails.setAttribute("MACHINE",resultSetTemp.getString("MACHINE"));
                ObjTimeCorrectionDetails.setAttribute("P_TIME",resultSetTemp.getFloat("P_TIME"));
                ObjTimeCorrectionDetails.setAttribute("NEW_TIME",resultSetTemp.getString("NEW_TIME"));
                
                hmTimeCorrectionDetails.put(Integer.toString(serialNoCounter),ObjTimeCorrectionDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public static HashMap getHistoryList(int CompanyID, String DocumentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM SDMLATTPAY.ATT_SPECIAL_SANCTION_H WHERE SPECIAL_SANCTION_DOC_NO='"+DocumentNo+"' GROUP BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsSpecialSanctionEntry ObjTimeCorrection=new clsSpecialSanctionEntry();
                
                ObjTimeCorrection.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjTimeCorrection.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjTimeCorrection.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjTimeCorrection.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjTimeCorrection.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                ObjTimeCorrection.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),ObjTimeCorrection);
            }
            
            rsTmp.close();
            stTmp.close();
            return hmHistoryList;
        }
        catch(Exception e) {
            e.printStackTrace();
            return hmHistoryList;
        }
    }
    
    public boolean ShowHistory(String pDate,String pDocNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM SDMLATTPAY.TIME_CORRECTION_H WHERE SPECIAL_SANCTION_DOC_DATE='"+ pDate+"' AND SPECIAL_SANCTION_DOC_DOC_NO='"+pDocNo+"'");
            Ready=true;
            historyProductionDate = pDate;
            historyDocumentNo = pDocNo;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    // Find Pending DOCUMENTS
    public static HashMap getPendingApprovals(int pUserID,int pOrder) {
        String strSQL="";
        
        HashMap List=new HashMap();
        int Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT DISTINCT SPECIAL_SANCTION_DOC_NO,SPECIAL_SANCTION_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SPECIAL_SANCTION,SDMLATTPAY.D_COM_DOC_DATA WHERE SPECIAL_SANCTION_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=819 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT SPECIAL_SANCTION_DOC_NO,SPECIAL_SANCTION_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SPECIAL_SANCTION,SDMLATTPAY.D_COM_DOC_DATA WHERE SPECIAL_SANCTION_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=819 AND CANCELED=0 ORDER BY SPECIAL_SANCTION_DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT SPECIAL_SANCTION_DOC_NO,SPECIAL_SANCTION_DOC_DATE,RECEIVED_DATE FROM SDMLATTPAY.ATT_SPECIAL_SANCTION,SDMLATTPAY.D_COM_DOC_DATA WHERE SPECIAL_SANCTION_DOC_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=819 AND CANCELED=0 ORDER BY SPECIAL_SANCTION_DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsSpecialSanctionEntry ObjDoc=new clsSpecialSanctionEntry();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("SPECIAL_SANCTION_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("SPECIAL_SANCTION_DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Integer.toString(Counter),ObjDoc);                
            }            
            rsTmp.close();
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public String getPartyCode(String pPieceNo) {
        //return data.getStringValueFromDB("SELECT PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO+0='"+pPieceNo+"'");        
        
        if(pPieceNo.contains("P") || pPieceNo.contains("V"))
        {
            pPieceNo = pPieceNo.substring(0,6);
        }
        else
        {
            pPieceNo = pPieceNo.substring(0,5);
        }

       
        String piece_stage = data.getStringValueFromDB("SELECT PR_PIECE_STAGE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+pPieceNo+"'"); 
        if(piece_stage.equals("DIVERTED"))
        {
                    JOptionPane.showMessageDialog(null, "This Piece is Diverted, Please use new Piece Number with P or V");
                    return "";
        }
        
        System.out.println("GetPartyName Needling: SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+pPieceNo+"'");
        return data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+pPieceNo+"'"); 
        
    }
    
    
    
    // it creates list of user name(felt) to be displayed
    public HashMap getUserNameList(int pHierarchyId, int pUserId, String pModule){
        HashMap hmUserNameList= new HashMap();
        char category=' ';
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            ResultSet rsHierarchyRights=stTmp.executeQuery("SELECT CREATOR, APPROVER, FINAL_APPROVER FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID=2 AND HIERARCHY_ID="+pHierarchyId+" AND USER_ID="+pUserId);
            while(rsHierarchyRights.next()){
                boolean creator=rsHierarchyRights.getBoolean("CREATOR");
                boolean approver=rsHierarchyRights.getBoolean("APPROVER");
                boolean finalApprover=rsHierarchyRights.getBoolean("FINAL_APPROVER");
                if(approver)category='A';
                if(creator)category='C';
                if(finalApprover)category='F';
            }
            
            int counter=1;
            ComboData cData=new ComboData();
            cData.Code=0;
            cData.Text="Select User";
            hmUserNameList.put(new Integer(counter++), cData);
            ResultSet rsTmp=stTmp.executeQuery("SELECT USER_ID,USER_NAME FROM PRODUCTION.`FELT_USER` WHERE USER_MODULE='"+pModule+"' AND USER_CATEG='"+category+"' ORDER BY USER_NAME");
            while(rsTmp.next()){
                ComboData aData=new ComboData();                
                aData.Code=rsTmp.getInt("USER_ID");
                aData.Text=rsTmp.getString("USER_NAME");
                hmUserNameList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){e.printStackTrace();}
        return hmUserNameList;
    }    
  
        public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            
            rsTmp=stTmp.executeQuery("SELECT SPECIAL_SANCTION_DOC_NO FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM SDMLATTPAY.ATT_SPECIAL_SANCTION WHERE SPECIAL_SANCTION_DOC_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=819");
                }
                data.Execute("UPDATE SDMLATTPAY.ATT_SPECIAL_SANCTION SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SPECIAL_SANCTION_DOC_NO='"+pAmendNo+"'");
              
            }
            catch(Exception e) {
               e.printStackTrace();
            }
        }
}
    
}
