/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package SDMLATTPAY.leave;

import SDMLATTPAY.leave.*;
import SDMLATTPAY.Shift.*;
import EITLERP.*;
import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.lang.Double;

/**
 *
 * @author ashutosh
 */
public class clsLeaveApplication { 

    public String LastError = "";
    private Statement statement;
    private ResultSet rsResultSet, rsResultSet1;
    private ResultSet resultSet;
    private Connection Conn;
    private static Connection connection;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;

    private boolean HistoryView = false;
    public static int ModuleID = 811; //    
    public HashMap hmcolLeaveDetails;

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
    public clsLeaveApplication() {
        LastError = "";
        props = new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("LVT_DOC_NO", new Variant(""));
        props.put("LVT_DOC_DATE",new Variant(""));
        props.put("LVT_YEAR", new Variant(""));
        props.put("LVT_EMPID", new Variant(""));
        props.put("LVT_PAY_EMPID", new Variant(""));
        props.put("LVT_REMARK", new Variant(""));
        props.put("LVT_LEAVE_TYPE", new Variant(""));
        
        
        props.put("CREATED_BY", new Variant(""));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(""));
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

        //Create a new object for Leave collection
        hmcolLeaveDetails = new HashMap();

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
            //rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY ORDER BY LVT_DOC_DATE,LVT_DOC_NO  ");
            String str="SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY GROUP BY LVT_DOC_NO ORDER BY LVT_DOC_NO";
            rsResultSet = Stmt.executeQuery(str);
            //rsResultSet = Stmt.executeQuery("SELECT DISTINCT LVT_DOC_NO FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND LVT_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY LVT_DOC_DATE");
            
            connection = data.getConn();            
            //statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);            
            //resultSet=statement.executeQuery("SELECT DISTINCT LVT_DOC_NO FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND LVT_DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY LVT_DOC_DATE");
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
            //statement.close();
            //resultSet.close();

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
    
    public Object getObject(int pCompanyID,String pLeaveNo) {
        String strCondition = " LVT_DOC_NO='"+pLeaveNo+"' ";
        
        clsLeaveApplication ObjLeave = new clsLeaveApplication();
        //ObjLeave.Filter(strCondition,pCompanyID);
        ObjLeave.Filter(strCondition); 
        return ObjLeave;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY " + pCondition +" ORDER BY LVT_DOC_DATE";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if(rsResultSet.getRow()>0) {
                // (1.) strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+POType+" AND PO_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PO_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PO_DATE ";
                //strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+POType+" ";
                //(2.)rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return true; //(3.) return false;
            }
            else {
                Ready=true;
                MoveLast();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }

    public boolean Insert() {

        //Statement stHistory, stHeader, stHDetail;
        //ResultSet rsHistory, rsHeader, rsHDetail;

        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;
        try {
            
            //Leave data connection
            
            statementTemp = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO=''");
            
            //Leave data history connection
            statementHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO=''");
            
            
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

           /* // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            */
            
            rsResultSet.first();    
            for(int i=1;i<=hmcolLeaveDetails.size();i++) {
            clsLeaveApplicationDetails objcolLeaveDetails=(clsLeaveApplicationDetails) hmcolLeaveDetails.get(Integer.toString(i));
            resultSetTemp.moveToInsertRow();
            resultSetTemp.updateString("LVT_DOC_NO", getAttribute("LVT_DOC_NO").getString());
            resultSetTemp.updateString("LVT_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("LVT_DOC_DATE").getString()));            
            resultSetTemp.updateString("LVT_YEAR", objcolLeaveDetails.getAttribute("LVT_YEAR").getString());
                        
            resultSetTemp.updateString("LVT_PAY_EMPID", getAttribute("LVT_PAY_EMPID").getString());
            resultSetTemp.updateString("LVT_EMPID", getAttribute("LVT_EMPID").getString());
            
            resultSetTemp.updateString("LVT_REMARK", getAttribute("LVT_REMARK").getString());
            resultSetTemp.updateString("LVT_LEAVE_TYPE", "3"); //3 FOR AVAIL LEAVE
            
            resultSetTemp.updateInt("SR_NO",i);
            resultSetTemp.updateString("LVT_LEAVE_CODE", objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString());
            resultSetTemp.updateString("LVT_MENTION_TIME", objcolLeaveDetails.getAttribute("LVT_MENTION_TIME").getString());
            resultSetTemp.updateDouble("LVT_DAYS", objcolLeaveDetails.getAttribute("LVT_DAYS").getVal());
            resultSetTemp.updateString("LVT_FROMDATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_FROMDATE").getString()));
            resultSetTemp.updateString("LVT_TODATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_TODATE").getString()));
            //resultSetTemp.updateString("LVT_REMARK", getAttribute("LVT_REMARK").getString());
            
            resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetTemp.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetTemp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            resultSetTemp.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSetTemp.updateBoolean("APPROVED", false);
            resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("CANCELED", false);
            resultSetTemp.updateBoolean("REJECTED", false);
            resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("CHANGED", true);
            resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetTemp.updateString("FROM_IP", "" + str_split[1]);

            resultSetTemp.insertRow();

            //========= Inserting Into History =================//
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO='" + (String) getAttribute("LVT_DOC_NO").getObj() + "'");
            RevNo++;
            //Get the Maximum Revision No.
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            
            resultSetHistory.updateString("LVT_DOC_NO", getAttribute("LVT_DOC_NO").getString());
            resultSetHistory.updateString("LVT_DOC_DATE", getAttribute("LVT_DOC_DATE").getString());
            resultSetHistory.updateString("LVT_PAY_EMPID", getAttribute("LVT_PAY_EMPID").getString());
            resultSetHistory.updateString("LVT_REMARK", getAttribute("LVT_REMARK").getString());
            resultSetHistory.updateString("LVT_LEAVE_TYPE", "3");  //3  FOR AVAIL LEAVE
            
            resultSetHistory.updateString("LVT_YEAR", objcolLeaveDetails.getAttribute("LVT_YEAR").getString());
            resultSetHistory.updateString("LVT_EMPID", getAttribute("LVT_EMPID").getString());
            
            
            resultSetHistory.updateInt("SR_NO",i);
            resultSetHistory.updateString("LVT_LEAVE_CODE", objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString());            
            resultSetHistory.updateString("LVT_MENTION_TIME", objcolLeaveDetails.getAttribute("LVT_MENTION_TIME").getString());
            resultSetHistory.updateDouble("LVT_DAYS", objcolLeaveDetails.getAttribute("LVT_DAYS").getVal());            
            resultSetHistory.updateString("LVT_FROMDATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_FROMDATE").getString()));
            resultSetHistory.updateString("LVT_TODATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_TODATE").getString()));
            
            resultSetHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSetHistory.updateBoolean("APPROVED", false);
            resultSetHistory.updateString("APPROVED_DATE", "0000-00-00");
            resultSetHistory.updateBoolean("REJECTED", false);
            resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
            resultSetHistory.updateBoolean("CANCELED", false);
            resultSetHistory.updateBoolean("CHANGED", true);
            resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

            resultSetHistory.updateString("FROM_IP", "" + str_split[1]);

            resultSetHistory.insertRow();
            
            
          try {            
                    if ("F".equals(getAttribute("APPROVAL_STATUS").getString())) {
                        //if (getAttribute("CORRECTION_TYPE").getString().contains("MIS-PUNCH")) {
                            String strInsert="INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN (LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,LVT_DOC_NO,LVT_DOC_DATE,SR_NO,LVT_MENTION_TIME)"                                        
                                    + " VALUES ('"+objcolLeaveDetails.getAttribute("LVT_YEAR").getString()+"',"
                                    + "'"+getAttribute("LVT_EMPID").getString()+"',"
                                    + "'"+getAttribute("LVT_PAY_EMPID").getString()+"',"
                                    + "'"+objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString()+"', 3,"
                                    + "'"+objcolLeaveDetails.getAttribute("LVT_DAYS").getVal()+"',"
                                    +"'"+EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_FROMDATE").getString())+"', "
                                    +"'"+EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_TODATE").getString())+"', "
                                    + "'"+getAttribute("LVT_REMARK").getString()+"',"
                                    + "'"+getAttribute("LVT_DOC_NO").getString()+"',"
                                    + "'"+EITLERPGLOBAL.formatDateDB(getAttribute("LVT_DOC_DATE").getString())+"',"
                                    + ""+i+",'"+objcolLeaveDetails.getAttribute("LVT_MENTION_TIME").getString()+"')"
                                    ;                                            
                            System.out.println(strInsert);
                            data.Execute(strInsert);                                                        
                        //}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsLeaveApplication.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("LVT_DOC_NO").getObj();
            //ObjFlow.DocDate = (String) getAttribute("LVT_DOC_DATE").getObj();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("LVT_DOC_DATE").getString());            
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_LEAVE_ENTRY";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "LVT_DOC_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

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
            //MoveLast();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();

            return false;
        }

    }

    public boolean Update() {       
        
        //Statement stHistory, stHeader, stHDetail;
        //ResultSet rsHistory, rsHeader, rsHDetail;
        
        ResultSet  resultSetTemp,resultSetHistory;
        Statement  statementTemp, statementHistory;        
        int revisionNo =1;
        
        boolean Validate = true;

        try {
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            Validate = true;
            
           /*// ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            */
            //statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statementTemp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO=''");
            
            //Data history connection
            //statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            statementHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO='"+getAttribute("LVT_DOC_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from data table before insert
            statementHistory.execute("DELETE FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO='"+getAttribute("LVT_DOC_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO=''");              
            
            
            //String theDocNo = getAttribute("LVT_DOC_NO").getString();
           for(int i=1;i<=hmcolLeaveDetails.size();i++) {
            clsLeaveApplicationDetails objcolLeaveDetails=(clsLeaveApplicationDetails) hmcolLeaveDetails.get(Integer.toString(i));
            resultSetTemp.moveToInsertRow();
            resultSetTemp.updateString("LVT_DOC_NO", getAttribute("LVT_DOC_NO").getString());
            resultSetTemp.updateString("LVT_DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("LVT_DOC_DATE").getString()));
            
            resultSetTemp.updateString("LVT_PAY_EMPID", getAttribute("LVT_PAY_EMPID").getString());
            resultSetTemp.updateString("LVT_REMARK", getAttribute("LVT_REMARK").getString());
            resultSetTemp.updateString("LVT_LEAVE_TYPE", "3"); //3 FOR AVAIL
            
            resultSetTemp.updateString("LVT_YEAR", objcolLeaveDetails.getAttribute("LVT_YEAR").getString());
            resultSetTemp.updateString("LVT_EMPID", getAttribute("LVT_EMPID").getString());
            
            
            resultSetTemp.updateInt("SR_NO",i);
            resultSetTemp.updateString("LVT_LEAVE_CODE", objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString());
            resultSetTemp.updateString("LVT_MENTION_TIME", objcolLeaveDetails.getAttribute("LVT_MENTION_TIME").getString());
            resultSetTemp.updateDouble("LVT_DAYS", objcolLeaveDetails.getAttribute("LVT_DAYS").getVal());
            resultSetTemp.updateString("LVT_FROMDATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_FROMDATE").getString()));
            resultSetTemp.updateString("LVT_TODATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_TODATE").getString()));
            //resultSetTemp.updateString("LVT_REMARK", getAttribute("LVT_REMARK").getString());
            
            
            resultSetTemp.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            resultSetTemp.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetTemp.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());            
            resultSetTemp.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSetTemp.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            resultSetTemp.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSetTemp.updateBoolean("APPROVED", false);
            resultSetTemp.updateString("APPROVED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("CANCELED", false);
            resultSetTemp.updateBoolean("REJECTED", false);
            resultSetTemp.updateString("REJECTED_DATE", "0000-00-00");
            resultSetTemp.updateBoolean("CHANGED", true);
            resultSetTemp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetTemp.updateString("FROM_IP", "" + str_split[1]);
            //rsResultSet.updateString("FROM_IP", "" + str_split[1]);
            //resultSetTemp.updateRow();
            resultSetTemp.insertRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM SDMLATTPAY.LVT_LEAVE_TRN_H WHERE LVT_DOC_NO='" + (String) getAttribute("LVT_DOC_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("LVT_DOC_NO").getObj();

            resultSetHistory.moveToInsertRow();
            //resultSetHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            resultSetHistory.updateInt("REVISION_NO", revisionNo); //Revision No. 1 in case of Insert
            resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            
            resultSetHistory.updateString("LVT_DOC_NO", getAttribute("LVT_DOC_NO").getString());
            resultSetHistory.updateString("LVT_DOC_DATE", getAttribute("LVT_DOC_DATE").getString());
            
            resultSetHistory.updateString("LVT_PAY_EMPID", getAttribute("LVT_PAY_EMPID").getString());
            resultSetHistory.updateString("LVT_REMARK", getAttribute("LVT_REMARK").getString());
            resultSetHistory.updateString("LVT_LEAVE_TYPE", "3");
            
            resultSetHistory.updateString("LVT_YEAR", objcolLeaveDetails.getAttribute("LVT_YEAR").getString());
            resultSetHistory.updateString("LVT_EMPID", getAttribute("LVT_EMPID").getString());            
            
            resultSetHistory.updateInt("SR_NO",i);
            resultSetHistory.updateString("LVT_LEAVE_CODE", objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString());            
            resultSetHistory.updateString("LVT_MENTION_TIME", objcolLeaveDetails.getAttribute("LVT_MENTION_TIME").getString());
            resultSetHistory.updateDouble("LVT_DAYS", objcolLeaveDetails.getAttribute("LVT_DAYS").getVal());            
            resultSetHistory.updateString("LVT_FROMDATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_FROMDATE").getString()));
            resultSetHistory.updateString("LVT_TODATE", EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_TODATE").getString()));
            
            
            resultSetHistory.updateInt("MODIFIED_BY", getAttribute("MODIFIED_BY").getInt());
            resultSetHistory.updateInt("CREATED_BY", getAttribute("CREATED_BY").getInt());
            resultSetHistory.updateString("CREATED_DATE", getAttribute("CREATED_DATE").getString());
            
            resultSetHistory.updateString("MODIFIED_DATE", getAttribute("MODIFIED_DATE").getString());
            resultSetHistory.updateInt("HIERARCHY_ID", getAttribute("HIERARCHY_ID").getInt());
            resultSetHistory.updateString("REJECTED_REMARKS", getAttribute("REJECTED_REMARKS").getString());
            resultSetHistory.updateBoolean("APPROVED", false);
            resultSetHistory.updateString("APPROVED_DATE", "0000-00-00");
            resultSetHistory.updateBoolean("REJECTED", false);
            resultSetHistory.updateString("REJECTED_DATE", "0000-00-00");
            resultSetHistory.updateBoolean("CANCELED", false);
            resultSetHistory.updateBoolean("CHANGED", true);
            resultSetHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            resultSetHistory.updateString("FROM_IP", "" + str_split[1]);

            resultSetHistory.insertRow();

            //======== Update the Approval Flow =========
            setAttribute("FROM", EITLERPGLOBAL.gUserID);
            
            try {            
                    if ("F".equals(getAttribute("APPROVAL_STATUS").getString())) {
                        //if (getAttribute("CORRECTION_TYPE").getString().contains("MIS-PUNCH")) {
                            String strInsert="INSERT INTO SDMLATTPAY.ATT_LEAVE_TRN (LVT_YEAR,LVT_EMPID,LVT_PAY_EMPID,LVT_LEAVE_CODE,LVT_LEAVE_TYPE,LVT_DAYS,LVT_FROMDATE,LVT_TODATE,LVT_REMARK,LVT_DOC_NO,LVT_DOC_DATE,SR_NO,LVT_MENTION_TIME)"                                        
                                    + " VALUES ('"+objcolLeaveDetails.getAttribute("LVT_YEAR").getString()+"',"
                                    + "'"+getAttribute("LVT_EMPID").getString()+"',"
                                    + "'"+getAttribute("LVT_PAY_EMPID").getString()+"',"
                                    + "'"+objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString()+"', 3,"
                                    + "'"+objcolLeaveDetails.getAttribute("LVT_DAYS").getVal()+"',"
                                    +"'"+EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_FROMDATE").getString())+"', "
                                    +"'"+EITLERPGLOBAL.formatDateDB(objcolLeaveDetails.getAttribute("LVT_TODATE").getString())+"', "
                                    + "'"+getAttribute("LVT_REMARK").getString().replaceAll("'", "''")+"',"
                                    + "'"+getAttribute("LVT_DOC_NO").getString()+"',"
                                    + "'"+EITLERPGLOBAL.formatDateDB(getAttribute("LVT_DOC_DATE").getString())+"',"
                                    + ""+i+",'"+objcolLeaveDetails.getAttribute("LVT_MENTION_TIME").getString()+"')"
                                    ;                                            
                            System.out.println(strInsert);
                            data.Execute(strInsert);                                                        
                        //}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
           }
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            SDMLATTPAY.ApprovalFlow ObjFlow = new SDMLATTPAY.ApprovalFlow();

            ObjFlow.ModuleID = clsLeaveApplication.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("LVT_DOC_NO").getObj();
            //ObjFlow.DocDate = (String) getAttribute("LVT_DOC_DATE").getObj();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("LVT_DOC_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "SDMLATTPAY.ATT_LEAVE_ENTRY";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "LVT_DOC_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

            //==== Handling Rejected Documents ==========//
            AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            
            if (AStatus.equals("F")) {
                
            }
            
            
            if (AStatus.equals("R")) {
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==========================================//

            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();

            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE SDMLATTPAY.ATT_LEAVE_ENTRY SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE LVT_DOC_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=" + clsLeaveApplication.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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
            updateLeaveBalance();            
            return true;
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
                        
    }

    public boolean setData() {
        ResultSet rsTmp,resultSetTemp;
        Connection tmpConn,connection;
        Statement tmpStmt,statementTemp;

        tmpConn = data.getConn();
        connection=data.getConn();
        long Counter = 0;
        int RevNo = 0;
        int serialNoCounter=0;

        try {
            if (HistoryView) {
                RevNo = rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsResultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }

            setAttribute("LVT_DOC_NO", rsResultSet.getString("LVT_DOC_NO"));
            setAttribute("LVT_DOC_DATE", rsResultSet.getString("LVT_DOC_DATE"));            
            
            setAttribute("LVT_PAY_EMPID", rsResultSet.getString("LVT_PAY_EMPID"));
            setAttribute("LVT_REMARK",rsResultSet.getString("LVT_REMARK"));
            
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
            
            hmcolLeaveDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);            
            //statementTemp = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);            
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO='"+rsResultSet.getString("LVT_DOC_NO")+"' ORDER BY SR_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                //setAttribute("LVT_DOC_NO",resultSetTemp.getString("LVT_DOC_NO"));
                //setAttribute("LVT_DOC_DATE",resultSetTemp.getString("LVT_DOC_DATE"));
                
                //setAttribute("LVT_PAY_EMPID",resultSetTemp.getString("LVT_PAY_EMPID"));
//                setAttribute("LVT_LEAVE_CODE",resultSetTemp.getString("LVT_LEAVE_CODE"));
//                setAttribute("LVT_LEAVE_TYPE",resultSetTemp.getString("LVT_LEAVE_TYPE"));
//                setAttribute("LVT_DAYS",resultSetTemp.getDouble("LVT_DAYS"));
//                setAttribute("LVT_FROMDATE",resultSetTemp.getString("LVT_FROMDATE"));
//                setAttribute("LVT_TODATE",resultSetTemp.getString("LVT_TODATE"));
//                setAttribute("LVT_REMARK",resultSetTemp.getString("LVT_REMARK"));
                
//                setAttribute("CREATED_BY",resultSetTemp.getInt("CREATED_BY"));
//                setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
//                setAttribute("MODIFIED_BY",resultSetTemp.getInt("MODIFIED_BY"));
//                setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
//                setAttribute("APPROVED",resultSetTemp.getInt("APPROVED"));
//                setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
//                setAttribute("REJECTED",resultSetTemp.getBoolean("REJECTED"));
//                setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
//                setAttribute("CANCELED",resultSetTemp.getInt("CANCELED"));
//                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
//                setAttribute("REJECTED_REMARKS", rsResultSet.getString("REJECTED_REMARKS"));
                
                clsLeaveApplicationDetails objcolLeaveDetails = new clsLeaveApplicationDetails();
                
                objcolLeaveDetails.setAttribute("LVT_YEAR",resultSetTemp.getString("LVT_YEAR"));
                objcolLeaveDetails.setAttribute("LVT_LEAVE_CODE",resultSetTemp.getString("LVT_LEAVE_CODE"));
                objcolLeaveDetails.setAttribute("LVT_MENTION_TIME",resultSetTemp.getString("LVT_MENTION_TIME"));
                objcolLeaveDetails.setAttribute("LVT_DAYS",resultSetTemp.getDouble("LVT_DAYS"));
                objcolLeaveDetails.setAttribute("LVT_FROMDATE",resultSetTemp.getString("LVT_FROMDATE"));
                objcolLeaveDetails.setAttribute("LVT_TODATE",resultSetTemp.getString("LVT_TODATE"));
                
                
                hmcolLeaveDetails.put(Integer.toString(serialNoCounter),objcolLeaveDetails);
            }
            resultSetTemp.close();
            statementTemp.close();

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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=811 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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

    public static HashMap getHistoryList(int CompanyID, String DocNo) {
        HashMap List = new HashMap();
        ResultSet rsTmp;

        try {
            //String strSQL = "SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO='" + DocNo + "' ";
            String strSQL = "SELECT DISTINCT REVISION_NO,LVT_DOC_NO,LVT_DOC_DATE,UPDATED_BY "
                    + ",APPROVAL_STATUS,ENTRY_DATE,APPROVER_REMARKS,FROM_IP FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO='" + DocNo + "' ";
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsLeaveApplication objParty = new clsLeaveApplication();

                    objParty.setAttribute("LVT_DOC_NO", rsTmp.getString("LVT_DOC_NO"));
                    objParty.setAttribute("LVT_DOC_DATE", rsTmp.getString("LVT_DOC_DATE"));
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
            //strSQL += "SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE " + Condition + " ";
            strSQL += "SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE " + Condition + " GROUP BY LVT_DOC_NO ORDER BY LVT_DOC_NO ";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND LVT_DOC_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'     ORDER BY LVT_DOC_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.LVT_LEAVE_ENTRY WHERE LVT_DOC_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM SDMLATTPAY.D_COM_DOC_DATA WHERE MODULE_ID=811 AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
            String lDocNo = (String) getAttribute("LVT_DOC_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_NO,SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_DATE,RECEIVED_DATE,LVT_PAY_EMPID FROM SDMLATTPAY.ATT_LEAVE_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=811 ORDER BY SDMLATTPAY.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_NO,SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_DATE,RECEIVED_DATE,LVT_PAY_EMPID FROM SDMLATTPAY.ATT_LEAVE_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=811 ORDER BY SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_NO,SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_DATE,RECEIVED_DATE,LVT_PAY_EMPID FROM SDMLATTPAY.ATT_LEAVE_ENTRY,SDMLATTPAY.D_COM_DOC_DATA WHERE SDMLATTPAY.D_COM_DOC_DATA.DOC_NO=SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_NO AND SDMLATTPAY.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND SDMLATTPAY.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=811 ORDER BY SDMLATTPAY.ATT_LEAVE_ENTRY.LVT_DOC_NO";
            }
            //System.out.println(strSQL);
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsLeaveApplication ObjItem = new clsLeaveApplication();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("LVT_DOC_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("LVT_DOC_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("LVT_PAY_EMPID",rsTmp.getString("LVT_PAY_EMPID"));                
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM SDMLATTPAY.ATT_LEAVE_ENTRY_H WHERE LVT_DOC_NO='" + pDocNo + "'   ORDER BY REVISION_NO");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static boolean IsAmendmentUnderProcess(int pCompanyID, String pLeaveNo) {
        Connection tmpConn;
        Statement stTmp = null;
        ResultSet rsTmp = null;
        boolean IsPresent = false;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT * FROM SDMLATTPAY.ATT_LEAVE_UPDATION_ENTRY WHERE LVT_DOC_NO='" + pLeaveNo + "' AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                IsPresent = true;
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return IsPresent;
        } catch (Exception e) {
            return IsPresent;
        }
    }
    
    
    public static int getMaxAmendSrNo(int pCompanyID, String pLeaveNo) {
        Connection tmpConn;
        Statement stTmp = null;
        ResultSet rsTmp = null;
        int MaxSrNo = 0;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = stTmp.executeQuery("SELECT MAX(AMEND_SR_NO) AS MAXNO FROM SDMLATTTPAY.ATT_LEAVE_UPDATION_ENTRY WHERE COMPANY_ID=" + pCompanyID + " AND LVT_DOC_NO='" + pLeaveNo + "' ");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                MaxSrNo = rsTmp.getInt("MAXNO");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

            return MaxSrNo;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static boolean CanCancel(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            
            rsTmp=stTmp.executeQuery("SELECT LVT_DOC_NO FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
                rsTmp=data.getResult("SELECT APPROVED FROM SDMLATTPAY.ATT_LEAVE_ENTRY WHERE LVT_DOC_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM SDMLATTPAY.D_COM_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=811");
                }
                data.Execute("UPDATE SDMLATTPAY.ATT_LEAVE_ENTRY SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE LVT_DOC_NO='"+pAmendNo+"'");
              
            }
            catch(Exception e) {
               e.printStackTrace();
            }
        }
}
    
    public static int ValidateLeaveEntry(int EditMode, String VoucherDate) {

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (Integer.parseInt(VoucherDate.substring(5, 7)) == data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL")) {
                //return 0;
            } else {
                if (EITLERPGLOBAL.getCurrentDay() <= 2) {  //1//2//3//8
                    // if(EITLERPGLOBAL.getCurrentDay()<=10) {     // Sales Journal Voucher for Export Entry
                    if (Integer.parseInt(VoucherDate.substring(5, 7)) == data.getIntValueFromDB("SELECT MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))  FROM DUAL")) {
                        //return 0;
                    } else {
                        return EITLERPGLOBAL.ADD;
                    }
                } else {
                    //return EITLERPGLOBAL.ADD;
                    if (Integer.parseInt(VoucherDate.substring(5, 7)) == data.getIntValueFromDB("SELECT MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH))  FROM DUAL")) {
                        //return 0;
                    } else {
                        return EITLERPGLOBAL.ADD;
                    }
                }
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (Integer.parseInt(VoucherDate.substring(5, 7)) == data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL")) {
                //return 0;
            } else {
                if (EITLERPGLOBAL.getCurrentDay() <= 10) {  //7
                    if (Integer.parseInt(VoucherDate.substring(5, 7)) == data.getIntValueFromDB("SELECT MONTH(DATE_SUB(CURDATE(),INTERVAL 1 MONTH))  FROM DUAL")) {
                        //return 0;
                    } else {
                        return EITLERPGLOBAL.EDIT;
                    }
                } else {
                    //return EITLERPGLOBAL.EDIT;
                    if (Integer.parseInt(VoucherDate.substring(5, 7)) == data.getIntValueFromDB("SELECT MONTH(DATE_ADD(CURDATE(),INTERVAL 1 MONTH))  FROM DUAL")) {
                        //return 0;
                    } else {
                        return EITLERPGLOBAL.EDIT;
                    }
                }
            }
        }
        
        return 0;
    }


       public void updateLeaveBalance(){
        for(int i=1;i<=hmcolLeaveDetails.size();i++) {
       clsLeaveApplicationDetails objcolLeaveDetails=(clsLeaveApplicationDetails) hmcolLeaveDetails.get(Integer.toString(i));            
            try {            
                    if ("F".equals(getAttribute("APPROVAL_STATUS").getString())) {
                       String strUpdate="UPDATE SDMLATTPAY.ATT_LEAVE_BALANCE SET "                                        
                                    + " LVBAL_AVAIL=LVBAL_AVAIL+"+objcolLeaveDetails.getAttribute("LVT_DAYS").getVal()+""                                    
                                    +" WHERE LVBAL_YEAR='"+objcolLeaveDetails.getAttribute("LVT_YEAR").getString()+"' AND"
                                    +" LVBAL_PAYEMPCD='"+getAttribute("LVT_PAY_EMPID").getString()+"' AND LVBAL_LEAVE_CD='"+objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString()+"' ";                                                                        
                            System.out.println(strUpdate);
                            data.Execute(strUpdate);
                      String closingUpdate="UPDATE SDMLATTPAY.ATT_LEAVE_BALANCE SET LVBAL_CLOSING = LVBAL_OPENING + LVBAL_CREDIT - LVBAL_AVAIL - LVBAL_LAPSE - LVBAL_ENCASH "
                                    +" WHERE LVBAL_YEAR='"+objcolLeaveDetails.getAttribute("LVT_YEAR").getString()+"' AND"
                                    +" LVBAL_PAYEMPCD='"+getAttribute("LVT_PAY_EMPID").getString()+"' AND LVBAL_LEAVE_CD='"+objcolLeaveDetails.getAttribute("LVT_LEAVE_CODE").getString()+"' ";                                                                        
                        System.out.println(closingUpdate);
                      data.Execute(closingUpdate);                                                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
           }
        /*try{
            String closingUpdate="UPDATE ATT_LEAVE_BALANCE SET LVBAL_CLOSING = LVBAL_OPENING + LVBAL_CREDIT - LVBAL_AVAIL - LVBAL_LAPSE - LVBAL_ENCASH "
                                    +" WHERE LV_BAL_YEAR='"+objcolLeaveDetails.getAttribute("LVT_YEAR").getString()+"' AND"
                                    +" LVBAL_PAYEMPCD='"+getAttribute("LVT_PAY_EMPID").getString()+"' ";                                                                        
            data.Execute(closingUpdate);                                                        
        }catch(Exception e) {
            e.printStackTrace();
        }*/
        
    }
 

}
