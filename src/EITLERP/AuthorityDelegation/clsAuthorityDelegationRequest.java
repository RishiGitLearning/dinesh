/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */
package EITLERP.AuthorityDelegation;

import SDMLATTPAY.leave.*;
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
public class clsAuthorityDelegationRequest { 

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
    public static int ModuleID = 206; //    
    public HashMap hmcolAuthDelReqDetails;

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
    public clsAuthorityDelegationRequest() {
        LastError = "";
        props = new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("AUTH_DEL_REQ_NO", new Variant(""));
        props.put("AUTH_DEL_REQ_DATE",new Variant(""));        
        props.put("AUTH_USER_ID", new Variant(0));
        
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
        hmcolAuthDelReqDetails = new HashMap();

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
            String str="SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST GROUP BY AUTH_DEL_REQ_NO ORDER BY AUTH_DEL_REQ_NO";
            rsResultSet = Stmt.executeQuery(str);
            //rsResultSet = Stmt.executeQuery("SELECT DISTINCT AUTH_DEL_REQ_NO FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND AUTH_DEL_REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY AUTH_DEL_REQ_DATE");
            
            connection = data.getConn();                        
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
        String strCondition = " AUTH_DEL_REQ_NO='"+pLeaveNo+"' ";
        
        clsAuthorityDelegationRequest ObjLeave = new clsAuthorityDelegationRequest();
        //ObjLeave.Filter(strCondition,pCompanyID);
        ObjLeave.Filter(strCondition); 
        return ObjLeave;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST " + pCondition +" ORDER BY AUTH_DEL_REQ_DATE";
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
            Conn = data.getConn();
            statementTemp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO=''");
            
            //Leave data history connection
            statementHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO=''");
            
            
            ResultSet rsTmp = data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");

           /* // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsHistory = stHistory.executeQuery("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            */
            
            //rsResultSet.first();    
            resultSetTemp.first();    
            for(int i=1;i<=hmcolAuthDelReqDetails.size();i++) {
            clsAuthorityDelegationRequestDetails objcolAuthDelReqDetails=(clsAuthorityDelegationRequestDetails) hmcolAuthDelReqDetails.get(Integer.toString(i));
            resultSetTemp.moveToInsertRow();
            
            resultSetTemp.updateString("AUTH_DEL_REQ_NO", getAttribute("AUTH_DEL_REQ_NO").getString());
            resultSetTemp.updateString("AUTH_DEL_REQ_DATE", getAttribute("AUTH_DEL_REQ_DATE").getString());            
            resultSetTemp.updateString("AUTH_USER_ID", getAttribute("AUTH_USER_ID").getString());
            resultSetTemp.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            
            resultSetTemp.updateInt("SR_NO",i);
            resultSetTemp.updateInt("AUTH_ACTIVE_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_ACTIVE_MODULE_ID").getInt());
            resultSetTemp.updateInt("AUTH_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_ID").getInt());
            resultSetTemp.updateString("AUTH_MODULE_DESC", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_DESC").getString());
            resultSetTemp.updateInt("AUTH_NOMINEE1_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1_ID").getInt());
            resultSetTemp.updateString("AUTH_NOMINEE1", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1").getString());            
            resultSetTemp.updateInt("AUTH_NOMINEE2_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2_ID").getInt());
            resultSetTemp.updateString("AUTH_NOMINEE2", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2").getString());
            resultSetTemp.updateInt("AUTH_NOMINEE3_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3_ID").getInt());
            resultSetTemp.updateString("AUTH_NOMINEE3", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3").getString());
            resultSetTemp.updateString("AUTH_FROM_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_FROM_DATE").getString()));
            resultSetTemp.updateString("AUTH_TO_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_TO_DATE").getString()));
            resultSetTemp.updateString("AUTH_PRIORITY", objcolAuthDelReqDetails.getAttribute("AUTH_PRIORITY").getString());            
            
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
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='" + (String) getAttribute("AUTH_DEL_REQ_NO").getObj() + "'");
            RevNo++;
            //Get the Maximum Revision No.
            resultSetHistory.moveToInsertRow();
            resultSetHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            
            resultSetHistory.updateString("AUTH_DEL_REQ_NO", getAttribute("AUTH_DEL_REQ_NO").getString());
            resultSetHistory.updateString("AUTH_DEL_REQ_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("AUTH_DEL_REQ_DATE").getString()));            
            resultSetHistory.updateString("AUTH_USER_ID", getAttribute("AUTH_USER_ID").getString());
            resultSetHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            
            resultSetHistory.updateInt("SR_NO",i);
            resultSetHistory.updateInt("AUTH_ACTIVE_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_ACTIVE_MODULE_ID").getInt());
            resultSetHistory.updateInt("AUTH_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_ID").getInt());
            resultSetHistory.updateString("AUTH_MODULE_DESC", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_DESC").getString());
            resultSetHistory.updateInt("AUTH_NOMINEE1_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1_ID").getInt());
            resultSetHistory.updateString("AUTH_NOMINEE1", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1").getString());            
            resultSetHistory.updateInt("AUTH_NOMINEE2_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2_ID").getInt());
            resultSetHistory.updateString("AUTH_NOMINEE2", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2").getString());
            resultSetHistory.updateInt("AUTH_NOMINEE3_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3_ID").getInt());
            resultSetHistory.updateString("AUTH_NOMINEE3", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3").getString());
            resultSetHistory.updateString("AUTH_FROM_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_FROM_DATE").getString()));
            resultSetHistory.updateString("AUTH_TO_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_TO_DATE").getString()));
            resultSetHistory.updateString("AUTH_PRIORITY", getAttribute("AUTH_PRIORITY").getString());            
            
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
            }  
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM", EITLERPGLOBAL.gUserID);

            //ApprovalFlow ObjFlow=new ApprovalFlow();
            EITLERP.ApprovalFlow ObjFlow = new EITLERP.ApprovalFlow();

            ObjFlow.ModuleID = clsAuthorityDelegationRequest.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("AUTH_DEL_REQ_NO").getObj();
            //ObjFlow.DocDate = (String) getAttribute("AUTH_DEL_REQ_DATE").getObj();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("AUTH_DEL_REQ_DATE").getString());            
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "AUTH_DEL_REQ_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

            /*if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }*/
            
            data.Execute("INSERT INTO DINESHMILLS.D_COM_DOC_DATA"
                    + " (COMPANY_ID,MODULE_ID, DOC_NO, DOC_DATE, USER_ID, STATUS, TYPE, REMARKS, SR_NO, FROM_USER_ID, FROM_REMARKS, RECEIVED_DATE, ACTION_DATE, CHANGED, CHANGED_DATE) "
                    + " VALUES "
                    + " ('2','206', '" + (String) getAttribute("AUTH_DEL_REQ_NO").getObj() + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '"+getAttribute("AUTH_USER_ID").getString()+"', 'A', 'C', '', '1', '', '', '0000-00-00', '0000-00-00', 0, '0000-00-00')");
                                
            
            //=========== Add HOD Users  -----------//
            //String strSQL="SELECT DISTINCT(USER_ID) FROM D_INV_ITEM_RND_APPROVERS A,D_INV_ITEM_MASTER B,D_INV_MIR_DETAIL C WHERE A.ITEM_SYS_ID=B.ITEM_SYS_ID AND A.COMPANY_ID=B.COMPANY_ID AND C.ITEM_ID=B.ITEM_ID AND C.COMPANY_ID=B.COMPANY_ID AND B.RND_APPROVAL=1 AND C.MIR_NO='"+(String)getAttribute("MIR_NO").getObj()+"' AND C.MIR_TYPE="+(int)getAttribute("MIR_TYPE").getVal();
            String strSQL="SELECT DISTINCT(USER_ID) FROM DINESHMILLS.D_COM_DOC_APPROVERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=206 AND DEPT_ID="+EITLERPGLOBAL.gUserDeptID+" ORDER BY SR_NO";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                HashMap hodUsers=new HashMap();
                
                while(!rsTmp.isAfterLast()) {
                    clsHierarchy ObjNewUser=new clsHierarchy();
                    
                    ObjNewUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjNewUser.setAttribute("MODULE_ID",206);
                    ObjNewUser.setAttribute("DOC_NO",(String)getAttribute("AUTH_DEL_REQ_NO").getObj());
                    ObjNewUser.setAttribute("DOC_DATE",(String)getAttribute("AUTH_DEL_REQ_DATE").getObj());
                    ObjNewUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    
                    hodUsers.put(Integer.toString(hodUsers.size()+1),ObjNewUser);
                    rsTmp.next();
                }
                
                AppendHODUsers(hodUsers);
            }
            //===============================================================================================//
            

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

            rsHistory = stHistory.executeQuery("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            */
            //statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statementTemp = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO=''");
            //resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='"+getAttribute("AUTH_DEL_REQ_NO").getString()+"'");
            //Data history connection
            //statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            statementHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='"+getAttribute("AUTH_DEL_REQ_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from data table before insert
            statementHistory.execute("DELETE FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='"+getAttribute("AUTH_DEL_REQ_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO=''");              
            
            
            //String theDocNo = getAttribute("AUTH_DEL_REQ_NO").getString();
           for(int i=1;i<=hmcolAuthDelReqDetails.size();i++) {
            clsAuthorityDelegationRequestDetails objcolAuthDelReqDetails=(clsAuthorityDelegationRequestDetails) hmcolAuthDelReqDetails.get(Integer.toString(i));
            resultSetTemp.moveToInsertRow();
            resultSetTemp.updateString("AUTH_DEL_REQ_NO", getAttribute("AUTH_DEL_REQ_NO").getString());
            resultSetTemp.updateString("AUTH_DEL_REQ_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("AUTH_DEL_REQ_DATE").getString()));
            
            resultSetTemp.updateString("AUTH_USER_ID", getAttribute("AUTH_USER_ID").getString());
            resultSetTemp.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            
            resultSetTemp.updateInt("SR_NO",i);
            resultSetTemp.updateInt("AUTH_ACTIVE_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_ACTIVE_MODULE_ID").getInt());
            resultSetTemp.updateInt("AUTH_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_ID").getInt());
            resultSetTemp.updateString("AUTH_MODULE_DESC", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_DESC").getString());
            resultSetTemp.updateInt("AUTH_NOMINEE1_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1_ID").getInt());
            resultSetTemp.updateString("AUTH_NOMINEE1", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1").getString());            
            resultSetTemp.updateInt("AUTH_NOMINEE2_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2_ID").getInt());
            resultSetTemp.updateString("AUTH_NOMINEE2", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2").getString());
            resultSetTemp.updateInt("AUTH_NOMINEE3_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3_ID").getInt());
            resultSetTemp.updateString("AUTH_NOMINEE3", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3").getString());
            resultSetTemp.updateString("AUTH_FROM_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_FROM_DATE").getString()));
            resultSetTemp.updateString("AUTH_TO_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_TO_DATE").getString()));
            resultSetTemp.updateString("AUTH_PRIORITY", objcolAuthDelReqDetails.getAttribute("AUTH_PRIORITY").getString());            
            
            
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
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='" + (String) getAttribute("AUTH_DEL_REQ_NO").getObj() + "'");
            RevNo++;
            String RevDocNo = (String) getAttribute("AUTH_DEL_REQ_NO").getObj();

            resultSetHistory.moveToInsertRow();
            //resultSetHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            resultSetHistory.updateInt("REVISION_NO", revisionNo); //Revision No. 1 in case of Insert
            //resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            resultSetHistory.updateInt("UPDATED_BY", (int) getAttribute("UPDATED_BY").getVal());
            resultSetHistory.updateString("APPROVAL_STATUS", getAttribute("APPROVAL_STATUS").getString());
            resultSetHistory.updateString("ENTRY_DATE", data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
            resultSetHistory.updateString("APPROVER_REMARKS", getAttribute("APPROVER_REMARKS").getString());
            
            resultSetHistory.updateString("AUTH_DEL_REQ_NO", getAttribute("AUTH_DEL_REQ_NO").getString());
            resultSetHistory.updateString("AUTH_DEL_REQ_DATE", getAttribute("AUTH_DEL_REQ_DATE").getString());
            
            resultSetHistory.updateString("AUTH_USER_ID", getAttribute("AUTH_USER_ID").getString());
            resultSetHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            
            resultSetHistory.updateInt("SR_NO",i);
            resultSetHistory.updateInt("AUTH_ACTIVE_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_ACTIVE_MODULE_ID").getInt());
            resultSetHistory.updateInt("AUTH_MODULE_ID", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_ID").getInt());
            resultSetHistory.updateString("AUTH_MODULE_DESC", objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_DESC").getString());
            resultSetHistory.updateInt("AUTH_NOMINEE1_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1_ID").getInt());
            resultSetHistory.updateString("AUTH_NOMINEE1", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1").getString());            
            resultSetHistory.updateInt("AUTH_NOMINEE2_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2_ID").getInt());
            resultSetHistory.updateString("AUTH_NOMINEE2", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2").getString());
            resultSetHistory.updateInt("AUTH_NOMINEE3_ID", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3_ID").getInt());
            resultSetHistory.updateString("AUTH_NOMINEE3", objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3").getString());
            resultSetHistory.updateString("AUTH_FROM_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_FROM_DATE").getString()));
            resultSetHistory.updateString("AUTH_TO_DATE", EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_TO_DATE").getString()));
            resultSetHistory.updateString("AUTH_PRIORITY", objcolAuthDelReqDetails.getAttribute("AUTH_PRIORITY").getString());            
            
            
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
                        if(data.IsRecordExist("SELECT * FROM DINESHMILLS.D_COM_DELEGATION_AUTHORITY WHERE AUTH_MODULE_ID=" + objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_ID").getInt() +"  AND  AUTH_USER_ID=" + getAttribute("AUTH_USER_ID").getString() + "")){
                             String strDel="DELETE FROM DINESHMILLS.D_COM_DELEGATION_AUTHORITY WHERE AUTH_MODULE_ID="+objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_ID").getInt()+" "
                                + " AND AUTH_USER_ID='"+getAttribute("AUTH_USER_ID").getString()+"' ";
                             data.Execute(strDel);                                                        
                        }
                       
                        //if (getAttribute("CORRECTION_TYPE").getString().contains("MIS-PUNCH")) {
                                String strInsert="INSERT INTO DINESHMILLS.D_COM_DELEGATION_AUTHORITY (COMPANY_ID, AUTH_MODULE_ID, AUTH_MODULE_DESC, AUTH_USER_ID, AUTH_NOMINEE1, AUTH_NOMINEE2, AUTH_NOMINEE3, AUTH_PRIORITY, CHANGED, CHANGED_DATE, MODIFIED_BY, MODIFIED_DATE, AUTH_NOMINEE1_ID, AUTH_NOMINEE2_ID, AUTH_NOMINEE3_ID, AUTH_FROM_DATE, AUTH_TO_DATE, AUTH_ACTIVE_MODULE_ID)"                                        
                                    + " VALUES (2,'"+objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_ID").getInt()+"',"
                                    + "'"+objcolAuthDelReqDetails.getAttribute("AUTH_MODULE_DESC").getString()+"',"
                                    + "'"+getAttribute("AUTH_USER_ID").getString()+"',"
                                    + "'"+objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1").getString()+"',"
                                    + "'"+objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2").getString()+"',"
                                    + "'"+objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3").getString()+"',"
                                    + "'"+objcolAuthDelReqDetails.getAttribute("AUTH_PRIORITY").getString()+"',1,'"+EITLERPGLOBAL.getCurrentDateDB()+"'," 
                                    + ""+getAttribute("MODIFIED_BY").getInt()+",'"+getAttribute("MODIFIED_DATE").getString()+"' ,"
                                    + ""+objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE1_ID").getInt()+","    
                                    + ""+objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE2_ID").getInt()+","                                            
                                    + ""+objcolAuthDelReqDetails.getAttribute("AUTH_NOMINEE3_ID").getInt()+","    
                                    +"'"+EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_FROM_DATE").getString())+"', "
                                    +"'"+EITLERPGLOBAL.formatDateDB(objcolAuthDelReqDetails.getAttribute("AUTH_TO_DATE").getString())+"', "
                                    + ""+objcolAuthDelReqDetails.getAttribute("AUTH_ACTIVE_MODULE_ID").getInt()+")"                                    
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
            EITLERP.ApprovalFlow ObjFlow = new EITLERP.ApprovalFlow();

            ObjFlow.ModuleID = clsAuthorityDelegationRequest.ModuleID;
            ObjFlow.DocNo = (String) getAttribute("AUTH_DEL_REQ_NO").getObj();
            //ObjFlow.DocDate = (String) getAttribute("AUTH_DEL_REQ_DATE").getObj();
            ObjFlow.DocDate = EITLERPGLOBAL.formatDateDB(getAttribute("AUTH_DEL_REQ_DATE").getString());
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName = "AUTH_DEL_REQ_NO";
            ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;

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
                data.Execute("UPDATE DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE AUTH_DEL_REQ_NO='" + ObjFlow.DocNo + "' ");

                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM DINESHMILLS.D_COM_DOC_DATA WHERE MODULE_ID=" + clsAuthorityDelegationRequest.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");

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

            setAttribute("AUTH_DEL_REQ_NO", rsResultSet.getString("AUTH_DEL_REQ_NO"));
            setAttribute("AUTH_DEL_REQ_DATE", rsResultSet.getString("AUTH_DEL_REQ_DATE"));            
            
            setAttribute("AUTH_USER_ID", rsResultSet.getString("AUTH_USER_ID"));
            
            
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
            
            hmcolAuthDelReqDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);            
            //statementTemp = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);            
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='"+rsResultSet.getString("AUTH_DEL_REQ_NO")+"' ORDER BY SR_NO");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                clsAuthorityDelegationRequestDetails objAuthDelReq = new clsAuthorityDelegationRequestDetails();
                
                objAuthDelReq.setAttribute("AUTH_ACTIVE_MODULE_ID",resultSetTemp.getInt("AUTH_ACTIVE_MODULE_ID"));
                objAuthDelReq.setAttribute("AUTH_MODULE_ID",resultSetTemp.getInt("AUTH_MODULE_ID"));
                objAuthDelReq.setAttribute("AUTH_MODULE_DESC",resultSetTemp.getString("AUTH_MODULE_DESC"));
                objAuthDelReq.setAttribute("AUTH_NOMINEE1_ID",resultSetTemp.getDouble("AUTH_NOMINEE1_ID"));
                objAuthDelReq.setAttribute("AUTH_NOMINEE1",resultSetTemp.getString("AUTH_NOMINEE1"));
                objAuthDelReq.setAttribute("AUTH_NOMINEE2_ID",resultSetTemp.getDouble("AUTH_NOMINEE2_ID"));
                objAuthDelReq.setAttribute("AUTH_NOMINEE2",resultSetTemp.getString("AUTH_NOMINEE2"));
                objAuthDelReq.setAttribute("AUTH_NOMINEE3_ID",resultSetTemp.getDouble("AUTH_NOMINEE3_ID"));
                objAuthDelReq.setAttribute("AUTH_NOMINEE3",resultSetTemp.getString("AUTH_NOMINEE3"));
                objAuthDelReq.setAttribute("AUTH_PRIORITY",resultSetTemp.getString("AUTH_PRIORITY"));
                objAuthDelReq.setAttribute("AUTH_FROM_DATE",resultSetTemp.getString("AUTH_FROM_DATE"));
                objAuthDelReq.setAttribute("AUTH_TO_DATE",resultSetTemp.getString("AUTH_TO_DATE"));
                
                
                hmcolAuthDelReqDetails.put(Integer.toString(serialNoCounter),objAuthDelReq);
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
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
            //String strSQL = "SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='" + DocNo + "' ";
            String strSQL = "SELECT DISTINCT REVISION_NO,AUTH_DEL_REQ_NO,AUTH_DEL_REQ_DATE,UPDATED_BY "
                    + ",APPROVAL_STATUS,DATE(ENTRY_DATE) ENTRY_DATE,APPROVER_REMARKS,FROM_IP FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='" + DocNo + "' ";
            System.out.println(strSQL);
            rsTmp = data.getResult(strSQL);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    clsAuthorityDelegationRequest objParty = new clsAuthorityDelegationRequest();

                    objParty.setAttribute("AUTH_DEL_REQ_NO", rsTmp.getString("AUTH_DEL_REQ_NO"));
                    objParty.setAttribute("AUTH_DEL_REQ_DATE", rsTmp.getString("AUTH_DEL_REQ_DATE"));
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
            strSQL += "SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE " + Condition + " ";
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSQL);

            if (!rsResultSet.first()) {
                strSQL = "SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND AUTH_DEL_REQ_DATE<='" + EITLERPGLOBAL.FinToDateDB + "'     ORDER BY AUTH_DEL_REQ_NO";
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
            strSQL = "SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='" + pDocNo + "' AND (APPROVED=1)";

            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();

            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
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
            String lDocNo = (String) getAttribute("AUTH_DEL_REQ_NO").getObj();
            String strSQL = "";

            //First check that record is editable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                String strQry = "DELETE FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='" + lDocNo + "'";
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
                strSQL = "SELECT DISTINCT DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_NO,DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_DATE,RECEIVED_DATE FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST,DINESHMILLS.D_COM_DOC_DATA WHERE DINESHMILLS.D_COM_DOC_DATA.DOC_NO=DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_NO AND DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.APPROVED=0 \n" +
"AND DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.CANCELED=0 AND DINESHMILLS.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND DINESHMILLS.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+ModuleID+" ORDER BY DINESHMILLS.D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_NO,DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_DATE,RECEIVED_DATE FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST,DINESHMILLS.D_COM_DOC_DATA WHERE DINESHMILLS.D_COM_DOC_DATA.DOC_NO=DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_NO AND DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.APPROVED=0 \n" +
"AND DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.CANCELED=0 AND DINESHMILLS.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND DINESHMILLS.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+ModuleID+" ORDER BY DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_NO,DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_DATE,RECEIVED_DATE FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST,DINESHMILLS.D_COM_DOC_DATA WHERE DINESHMILLS.D_COM_DOC_DATA.DOC_NO=DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_NO AND DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.APPROVED=0 \n" +
"AND DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.CANCELED=0 AND DINESHMILLS.D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND DINESHMILLS.D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+ModuleID+" ORDER BY DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST.AUTH_DEL_REQ_NO";
            }
            System.out.println(strSQL); 
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsAuthorityDelegationRequest ObjItem = new clsAuthorityDelegationRequest();

                //------------- Header Fields --------------------//
                ObjItem.setAttribute("DOC_NO", rsTmp.getString("AUTH_DEL_REQ_NO"));
                ObjItem.setAttribute("DOC_DATE", rsTmp.getString("AUTH_DEL_REQ_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                
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
            rsResultSet = Stmt.executeQuery("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST_H WHERE AUTH_DEL_REQ_NO='" + pDocNo + "'   ORDER BY REVISION_NO");
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
            rsTmp = stTmp.executeQuery("SELECT * FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='" + pLeaveNo + "' AND APPROVED=0 AND CANCELLED=0");
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
            rsTmp = stTmp.executeQuery("SELECT MAX(AMEND_SR_NO) AS MAXNO FROM SDMLATTTPAY.ATT_LEAVE_UPDATION_ENTRY WHERE COMPANY_ID=" + pCompanyID + " AND AUTH_DEL_REQ_NO='" + pLeaveNo + "' ");
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
            
            rsTmp=stTmp.executeQuery("SELECT DOC_NO FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
                rsTmp=data.getResult("SELECT APPROVED FROM DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST WHERE AUTH_DEL_REQ_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM DINESHMILLS.D_COM_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID="+ModuleID+"");
                }
                data.Execute("UPDATE DINESHMILLS.D_COM_AUTHORITY_DELEGATION_REQUEST SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE AUTH_DEL_REQ_NO='"+pAmendNo+"'");
              
            }
            catch(Exception e) {
               e.printStackTrace();
            }
        }
}
    
    public static HashMap getwithoutDelegationList(int pCompanyID,int pUserID) {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            //rsTmp=data.getResult("SELECT * FROM D_COM_DELEGATION_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND AUTH_USER_ID="+pUserID);
            //rsTmp=data.getResult("SELECT * FROM D_COM_DELEGATION_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND AUTH_USER_ID="+pUserID+" ORDER BY AUTH_NOMINEE3,AUTH_NOMINEE2,AUTH_NOMINEE1");
            /*String strSQL = "SELECT A.MODULE_ID,MODULE_DESC,USER_ID FROM\n"
                    + " (SELECT MODULE_ID,HR.USER_ID FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS HR,DINESHMILLS.D_COM_HIERARCHY H \n"
                    + " WHERE H.HIERARCHY_ID=HR.HIERARCHY_ID  AND (APPROVER=1 OR FINAL_APPROVER=1)  GROUP BY MODULE_ID,USER_ID  ORDER BY MODULE_ID,H.HIERARCHY_ID) A\n"
                    + " LEFT JOIN\n"
                    + " (SELECT MODULE_ID,MODULE_DESC FROM DINESHMILLS.D_COM_MODULES) B\n"
                    + " ON A.MODULE_ID=B.MODULE_ID";*/
            String strSQL1 = "SELECT A.MODULE_ID,MODULE_DESC,USER_ID,'' AUTH_NOMINEE1,''  AUTH_NOMINEE2,'' AUTH_NOMINEE3, '' AUTH_PRIORITY,'' AUTH_NOMINEE1_ID,'' AUTH_NOMINEE2_ID,'' AUTH_NOMINEE3_ID,'' AUTH_FROM_DATE,'' AUTH_TO_DATE,'' AUTH_ACTIVE_MODULE_ID FROM\n"
                    + " (SELECT MODULE_ID,HR.USER_ID FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS HR,DINESHMILLS.D_COM_HIERARCHY H \n"
                    + " WHERE H.HIERARCHY_ID=HR.HIERARCHY_ID  AND (APPROVER="+pUserID+" OR FINAL_APPROVER=1) AND USER_ID=1 \n"
                    + " AND MODULE_ID NOT IN (SELECT AUTH_MODULE_ID FROM DINESHMILLS.D_COM_DELEGATION_AUTHORITY WHERE AUTH_USER_ID="+pUserID+" AND (AUTH_NOMINEE1!='' OR AUTH_NOMINEE2!='' OR AUTH_NOMINEE3!='') ORDER BY AUTH_MODULE_ID)  GROUP BY MODULE_ID,USER_ID  ORDER BY MODULE_ID,H.HIERARCHY_ID) A\n"
                    + " LEFT JOIN\n"
                    + " (SELECT MODULE_ID,MODULE_DESC FROM DINESHMILLS.D_COM_MODULES) B\n"
                    + " ON A.MODULE_ID=B.MODULE_ID \n"
                    + " UNION \n"
                    + " SELECT AUTH_MODULE_ID, AUTH_MODULE_DESC, AUTH_USER_ID, AUTH_NOMINEE1, AUTH_NOMINEE2, AUTH_NOMINEE3, AUTH_PRIORITY, AUTH_NOMINEE1_ID, AUTH_NOMINEE2_ID, AUTH_NOMINEE3_ID, AUTH_FROM_DATE, AUTH_TO_DATE, AUTH_ACTIVE_MODULE_ID FROM DINESHMILLS.D_COM_DELEGATION_AUTHORITY WHERE AUTH_USER_ID="+pUserID+" AND (AUTH_NOMINEE1!='' OR AUTH_NOMINEE2!='' OR AUTH_NOMINEE3!='') ORDER BY MODULE_ID";
            rsTmp=data.getResult(strSQL1);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsAuthorityDelegationRequestDetails ObjAuthority=new clsAuthorityDelegationRequestDetails();
                    
                    //ObjAuthority.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    //ObjAuthority.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjAuthority.setAttribute("AUTH_USER_ID",rsTmp.getInt("USER_ID"));
                    ObjAuthority.setAttribute("AUTH_MODULE_ID",rsTmp.getInt("MODULE_ID"));
                    ObjAuthority.setAttribute("AUTH_MODULE_DESC",rsTmp.getString("MODULE_DESC"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE1_ID",rsTmp.getInt("AUTH_NOMINEE1_ID"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE2_ID",rsTmp.getInt("AUTH_NOMINEE2_ID"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE3_ID",rsTmp.getInt("AUTH_NOMINEE3_ID"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE1",rsTmp.getString("AUTH_NOMINEE1"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE2",rsTmp.getString("AUTH_NOMINEE2"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE3",rsTmp.getString("AUTH_NOMINEE3"));
                    ObjAuthority.setAttribute("AUTH_PRIORITY",rsTmp.getString("AUTH_PRIORITY"));
                    ObjAuthority.setAttribute("AUTH_FROM_DATE",rsTmp.getString("AUTH_FROM_DATE"));
                    ObjAuthority.setAttribute("AUTH_TO_DATE",rsTmp.getString("AUTH_TO_DATE"));
                    /*ObjAuthority.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    ObjAuthority.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                    ObjAuthority.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    */
                    List.put(Integer.toString(List.size()+1), ObjAuthority);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static HashMap getList(int pCompanyID,int pUserID) {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            //rsTmp=data.getResult("SELECT * FROM D_COM_DELEGATION_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND AUTH_USER_ID="+pUserID);
            rsTmp=data.getResult("SELECT * FROM D_COM_DELEGATION_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND AUTH_USER_ID="+pUserID+" ORDER BY AUTH_NOMINEE3,AUTH_NOMINEE2,AUTH_NOMINEE1");            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDelegationAuthority ObjAuthority=new clsDelegationAuthority();
                    
                    ObjAuthority.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    //ObjAuthority.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjAuthority.setAttribute("AUTH_USER_ID",rsTmp.getInt("AUTH_USER_ID"));
                    ObjAuthority.setAttribute("AUTH_MODULE_ID",rsTmp.getInt("AUTH_MODULE_ID"));
                    ObjAuthority.setAttribute("AUTH_MODULE_DESC",rsTmp.getString("AUTH_MODULE_DESC"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE1_ID",rsTmp.getInt("AUTH_NOMINEE1_ID"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE2_ID",rsTmp.getInt("AUTH_NOMINEE2_ID"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE3_ID",rsTmp.getInt("AUTH_NOMINEE3_ID"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE1",rsTmp.getString("AUTH_NOMINEE1"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE2",rsTmp.getString("AUTH_NOMINEE2"));
                    ObjAuthority.setAttribute("AUTH_NOMINEE3",rsTmp.getString("AUTH_NOMINEE3"));
                    ObjAuthority.setAttribute("AUTH_PRIORITY",rsTmp.getString("AUTH_PRIORITY"));
                    ObjAuthority.setAttribute("AUTH_FROM_DATE",rsTmp.getString("AUTH_FROM_DATE"));
                    ObjAuthority.setAttribute("AUTH_TO_DATE",rsTmp.getString("AUTH_TO_DATE"));
                    /*ObjAuthority.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    ObjAuthority.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                    ObjAuthority.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    */
                    List.put(Integer.toString(List.size()+1), ObjAuthority);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static void AppendHODUsers(HashMap pUsers) {
        Connection tmpConn;
        ResultSet rsDocData,rsTmp;
        Statement stDocData;
        String strSQL;
        boolean UserFound=false;
        
        try {
            tmpConn=data.getConn();
            stDocData=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA WHERE DOC_NO='1'");
            rsDocData.first();
            
            for(int i=1;i<=pUsers.size();i++) {
                clsHierarchy ObjDoc=(clsHierarchy) pUsers.get(Integer.toString(i));
                //Get the Record into Variables
                int CompanyID=(int)ObjDoc.getAttribute("COMPANY_ID").getVal();
                int ModuleID=(int)ObjDoc.getAttribute("MODULE_ID").getVal();
                String DocNo=(String)ObjDoc.getAttribute("DOC_NO").getObj();
                String DocDate=(String)ObjDoc.getAttribute("DOC_DATE").getObj();
                int UserID=(int)ObjDoc.getAttribute("USER_ID").getVal();
                int SrNo=0;
                
                //Now get the Maximum Sr. No.
                strSQL="SELECT MAX(SR_NO) AS SRNO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                    SrNo=rsTmp.getInt("SRNO")+1;
                }
                
                
                //Find the duplication
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND USER_ID="+UserID);
                rsTmp.first();
                
                UserFound=false;
                
                if(rsTmp.getRow()>0) //User Exist Do not do the entry
                {
                    UserFound=true;
                }
                
                if(!UserFound) {
                    //Inser the Records Into Table
                    rsDocData.moveToInsertRow();
                    rsDocData.updateInt("COMPANY_ID",CompanyID);
                    rsDocData.updateInt("MODULE_ID",ModuleID);
                    rsDocData.updateString("DOC_NO",DocNo);
                    rsDocData.updateString("DOC_DATE",DocDate);
                    rsDocData.updateInt("USER_ID",UserID);
                    rsDocData.updateInt("SR_NO",SrNo);
                    rsDocData.updateString("STATUS","W");
                    rsDocData.updateString("TYPE","A");
                    rsDocData.updateString("REMARKS","");
                    rsDocData.updateInt("FROM_USER_ID",EITLERPGLOBAL.gNewUserID);
                    rsDocData.updateString("FROM_REMARKS","");
                    rsDocData.updateString("RECEIVED_DATE","0000-00-00");
                    rsDocData.updateString("ACTION_DATE","0000-00-00");
                    rsDocData.updateBoolean("CHANGED",true);
                    rsDocData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDocData.insertRow();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
}