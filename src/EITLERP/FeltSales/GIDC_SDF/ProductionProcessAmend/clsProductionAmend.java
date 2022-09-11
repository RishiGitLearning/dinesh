/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.GIDC_SDF.ProductionProcessAmend;

import EITLERP.ComboData;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import EITLERP.Variant;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class clsProductionAmend {
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    //private ResultSet rsResultSet,
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmProductionAmend;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    private static int ModuleID = 795;
    
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
    
    /** Creates new Data Felt Order Updation */
    public clsProductionAmend() {
        LastError = "";
        props=new HashMap();
        props.put("PA_DOC_NO", new Variant(""));
        
        props.put("PIECE_NO",new Variant(""));
        props.put("PA_REMARK",new Variant(""));
        
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        
        props.put("REJECTED",new Variant(""));
        props.put("REJECTED_BY",new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(""));
        props.put("APPROVED_BY",new Variant(""));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("CANCELED",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(""));
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("MODULE_ID",new Variant(""));
        props.put("USER_ID",new Variant(""));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("APPROVER_REMARKS",new Variant(0));
        props.put("ENTRY_DATE",new Variant(0));
        
        hmProductionAmend=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND ORDER BY PA_DOC_NO");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
                setData();
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
                setData();
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
                setData();
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
//            if(HistoryView) setHistoryData(historyAmendDate, historyAmendID);
//            else 
                
                setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Insert() {
        ResultSet  resultSet,resultSetH;
        Statement  statement, statementH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE PA_DOC_NO=''");
            
            statementH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetH=statementH.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO=''");
    
            resultSet.first();
            resultSet.moveToInsertRow();
            
            resultSet.updateString("PA_DOC_NO",getAttribute("PA_DOC_NO").getString());
            resultSet.updateString("PA_DOC_DATE",getAttribute("PA_DOC_DATE").getString());
            
            resultSet.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            resultSet.updateString("PA_REMARK",getAttribute("PA_REMARK").getString());
            resultSet.updateString("PA_FROM_STAGE",getAttribute("PA_FROM_STAGE").getString());
            resultSet.updateString("PA_TO_STAGE",getAttribute("PA_TO_STAGE").getString());
            
            resultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY",0);
            resultSet.updateString("MODIFIED_DATE","0000-00-00");
            resultSet.updateBoolean("CHANGED",false);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_BY","");
            resultSet.updateString("REJECTED_DATE","0000-00-00");
            resultSet.updateBoolean("APPROVED",false);
            resultSet.updateString("APPROVED_BY","");
            resultSet.updateString("APPROVED_DATE","0000-00-00");
            resultSet.updateBoolean("CANCELED",false);
            resultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            
            resultSet.insertRow();
          
            resultSetH.first();
            resultSetH.moveToInsertRow();
            
            resultSetH.updateInt("REVISION_NO",1);
            resultSetH.updateString("PA_DOC_NO",getAttribute("PA_DOC_NO").getString());
            
            resultSetH.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            resultSetH.updateString("PA_REMARK",getAttribute("PA_REMARK").getString());
            resultSetH.updateString("PA_DOC_DATE",getAttribute("PA_DOC_DATE").getString());
            resultSetH.updateString("PA_FROM_STAGE",getAttribute("PA_FROM_STAGE").getString());
            resultSetH.updateString("PA_TO_STAGE",getAttribute("PA_TO_STAGE").getString());
            
            resultSetH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            resultSetH.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetH.updateInt("MODIFIED_BY",0);
            resultSetH.updateString("MODIFIED_DATE","0000-00-00");
            resultSetH.updateBoolean("CHANGED",false);
            resultSetH.updateString("CHANGED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSetH.updateBoolean("REJECTED",false);
            resultSetH.updateString("REJECTED_BY","");
            resultSetH.updateString("REJECTED_DATE","0000-00-00");
            resultSetH.updateBoolean("APPROVED",false);
            resultSetH.updateString("APPROVED_BY","");
            resultSetH.updateString("APPROVED_DATE","0000-00-00");
            resultSetH.updateBoolean("CANCELED",false);
            resultSetH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSetH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getObj()+"");
            resultSetH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSetH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            resultSetH.updateString("FROM_IP",""+str_split[1]);
            
            resultSetH.insertRow();
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; 
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("PA_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.GIDC_FELT_PROD_AMEND";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="PA_DOC_NO";
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                ObjFeltProductionApprovalFlow.Status="A";
                ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                ObjFeltProductionApprovalFlow.UpdateFlow();
            }
            else {
                if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError=ObjFeltProductionApprovalFlow.LastError;
                }
            }
            
         
            //--------- Approval Flow Update complete -----------
            
            // Update  in Order Master Table To confirm that Weaving has completed
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                
                
                ObjFeltProductionApprovalFlow.finalApproved=false;
            }
           
            
            LoadData();
          
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
        
        ResultSet  resultSetH;
        Statement  statement, statementH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            
         //   statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        //    resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE PA_DOC_NO=''");
            
            statementH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetH=statementH.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO=''");
    
          //  resultSet.first();
          //  resultSet.moveToCurrentRow();
            
            resultSet.updateString("PA_DOC_NO",getAttribute("PA_DOC_NO").getString());
           
            resultSet.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            resultSet.updateString("PA_REMARK",getAttribute("PA_REMARK").getString());
            
            resultSet.updateString("PA_DOC_DATE",getAttribute("PA_DOC_DATE").getString());
            resultSet.updateString("PA_FROM_STAGE",getAttribute("PA_FROM_STAGE").getString());
            resultSet.updateString("PA_TO_STAGE",getAttribute("PA_TO_STAGE").getString());
            
            
            resultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            resultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateInt("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSet.updateBoolean("CHANGED",true);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_BY","");
            resultSet.updateString("REJECTED_DATE","0000-00-00");
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("F"))
            {
                resultSet.updateBoolean("APPROVED",true);
                resultSet.updateString("APPROVED_BY",EITLERPGLOBAL.gNewUserID+"");
                resultSet.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            }
            else
            {
                resultSet.updateBoolean("APPROVED",false);
                resultSet.updateString("APPROVED_DATE","0000-00-00");
            }
            
            
            resultSet.updateBoolean("CANCELED",false);
            resultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            
            try
            {
                resultSet.updateRow();
            }catch(Exception e)
            {
              // JOptionPane.showMessageDialog(null, "Came To Error");
               e.printStackTrace();
            }
            
            resultSetH.first();
            resultSetH.moveToInsertRow();
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO='"+getAttribute("PA_DOC_NO").getString()+"'");
            
            RevNo++;
            //JOptionPane.showMessageDialog(null, "DOC No = "+getAttribute("PA_DOC_NO").getInt() + " ,New Rev No = "+RevNo);
            resultSetH.updateInt("REVISION_NO",RevNo);
            
            resultSetH.updateString("PA_DOC_NO",getAttribute("PA_DOC_NO").getString());
            
            resultSetH.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            resultSetH.updateString("PA_REMARK",getAttribute("PA_REMARK").getString());
            
            resultSetH.updateString("PA_DOC_DATE",getAttribute("PA_DOC_DATE").getString());
            resultSetH.updateString("PA_FROM_STAGE",getAttribute("PA_FROM_STAGE").getString());
            resultSetH.updateString("PA_TO_STAGE",getAttribute("PA_TO_STAGE").getString());
            
            resultSetH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            resultSetH.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetH.updateInt("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            resultSetH.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            resultSetH.updateBoolean("CHANGED",true);
            resultSetH.updateString("CHANGED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
            resultSetH.updateBoolean("REJECTED",false);
            resultSetH.updateString("REJECTED_BY","");
            resultSetH.updateString("REJECTED_DATE","0000-00-00");
           
            if(getAttribute("APPROVAL_STATUS").getString().equals("F"))
            {
                resultSetH.updateBoolean("APPROVED",true);
                resultSetH.updateString("APPROVED_BY",EITLERPGLOBAL.gNewUserID+"");
                resultSetH.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            }
            else
            {
                resultSetH.updateBoolean("APPROVED",false);
                resultSetH.updateString("APPROVED_DATE","0000-00-00");
            }
            
            resultSetH.updateBoolean("CANCELED",false);
            resultSetH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            
            resultSetH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            resultSetH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            resultSetH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            resultSetH.updateString("FROM_IP",""+str_split[1]);
            
            resultSetH.insertRow();
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("PA_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.GIDC_FELT_PROD_AMEND";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="PA_DOC_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.GIDC_FELT_PROD_AMEND SET REJECTED=0,CHANGED=1 WHERE PA_DOC_NO ='"+getAttribute("PA_DOC_NO").getString()+"'");
                ObjFeltProductionApprovalFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFeltProductionApprovalFlow.Status.equals("H")) {
                if(IsRejected) {
                    ObjFeltProductionApprovalFlow.Status="A";
                    ObjFeltProductionApprovalFlow.To=ObjFeltProductionApprovalFlow.From;
                    ObjFeltProductionApprovalFlow.UpdateFlow();
                }
            }
            else {
                if(!ObjFeltProductionApprovalFlow.UpdateFlow()) {
                    LastError=ObjFeltProductionApprovalFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            // Update  in Order Master Table To confirm that Weaving has completed
            if(ObjFeltProductionApprovalFlow.Status.equals("F") && ObjFeltProductionApprovalFlow.finalApproved) {
                
               
                ObjFeltProductionApprovalFlow.finalApproved=false;
            }
           
            setData();
           
            
            return true;
        }catch(Exception e) {
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
    public boolean CanDelete(String documentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE  PA_DOC_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE "
                    + " AND PA_DOC_NO ='" + documentNo + "'";
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
    public boolean IsEditable(String orderupdDocumentNo,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        try {
            if(HistoryView) {
                return false;
            }
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE PA_DOC_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND USER_ID="+userID+" AND DOC_NO='"+ orderupdDocumentNo +"' AND STATUS='W'";
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
           // String strSql = "SELECT DISTINCT AMEND_DATE FROM PRODUCTION.FELT_CN_TEMP_HEADER WHERE  " + stringFindQuery + " ORDER BY CN_DATE";
            String strSql = "SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE  " + stringFindQuery + "";
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
        }catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        int RevNo=0;
        try 
        {
            if(resultSet.getRow()==0)
            {
                    return false;
            }
           
            setAttribute("REVISION_NO",0);
            
            setAttribute("PA_DOC_NO",resultSet.getString("PA_DOC_NO"));
            setAttribute("PIECE_NO",resultSet.getString("PIECE_NO"));
            setAttribute("PA_REMARK",resultSet.getString("PA_REMARK"));
            setAttribute("PA_DOC_DATE",resultSet.getString("PA_DOC_DATE"));
            setAttribute("PA_FROM_STAGE",resultSet.getString("PA_FROM_STAGE"));
            setAttribute("PA_TO_STAGE",resultSet.getString("PA_TO_STAGE"));
            setAttribute("CREATED_BY",resultSet.getString("CREATED_BY"));
            setAttribute("CREATED_DATE",resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",resultSet.getString("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",resultSet.getString("MODIFIED_DATE"));
            setAttribute("CHANGED",resultSet.getString("CHANGED"));
            setAttribute("CHANGED_DATE",resultSet.getString("CHANGED_DATE"));
            setAttribute("REJECTED",resultSet.getString("REJECTED"));
            setAttribute("REJECTED_BY",resultSet.getString("REJECTED_BY"));
            setAttribute("REJECTED_DATE",resultSet.getString("REJECTED_DATE"));
            setAttribute("APPROVED",resultSet.getString("APPROVED"));
            setAttribute("APPROVED_BY",resultSet.getString("APPROVED_BY"));
            setAttribute("APPROVED_DATE",resultSet.getString("APPROVED_DATE"));
            setAttribute("CANCELED",resultSet.getString("CANCELED"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));

            
             if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
//    public boolean ShowHistory(String pDocNo) {
//        Ready=false;
//        try {
//            connection=data.getConn();
//            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO ='"+pDocNo+"'");
//            Ready=true;
//           
//            historyAmendID = pDocNo;
//            HistoryView=true;
//            MoveFirst();
//            return true;
//        }
//        catch(Exception e) {
//            LastError=e.getMessage();
//            return false;
//        }
//    }
    public boolean setHistoryData(String pProductionDate,String pDocNo) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        try {
           
            //Now Populate the collection, first clear the collection
            hmProductionAmend.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO='"+pDocNo+"'");
            
            resultSetTemp.first();
            setAttribute("REVISION_NO",resultSetTemp.getString("REVISION_NO"));
            setAttribute("PA_DOC_NO",resultSetTemp.getString("PA_DOC_NO"));
            setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
            setAttribute("CREATED_BY",resultSetTemp.getString("CREATED_BY"));
            setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",resultSetTemp.getString("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
            setAttribute("CHANGED",resultSetTemp.getString("CHANGED"));
            setAttribute("CHANGED_DATE",resultSetTemp.getString("CHANGED_DATE"));
            setAttribute("REJECTED",resultSetTemp.getString("REJECTED"));
            setAttribute("REJECTED_BY",resultSetTemp.getString("REJECTED_BY"));
            setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
            setAttribute("APPROVED",resultSetTemp.getString("APPROVED"));
            setAttribute("APPROVED_BY",resultSetTemp.getString("APPROVED_BY"));
            setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
            setAttribute("CANCELED",resultSetTemp.getString("CANCELED"));
            setAttribute("HIERARCHY_ID",resultSetTemp.getString("HIERARCHY_ID"));
            setAttribute("APPROVAL_STATUS",resultSetTemp.getString("APPROVAL_STATUS"));
            setAttribute("REJECTED_REMARKS",resultSetTemp.getString("REJECTED_REMARKS"));
            
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
    
    public static HashMap getHistoryList(String stringProductionDate, String productionDocumentNo) {
        HashMap hmHistoryList=new HashMap();
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO='"+productionDocumentNo+"'");
            
            while(rsTmp.next()) {
                clsProductionAmend felt_orderD = new clsProductionAmend();
                
                felt_orderD.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                felt_orderD.setAttribute("UPDATED_BY",rsTmp.getString("MODIFIED_BY"));
                felt_orderD.setAttribute("UPDATED_DATE",rsTmp.getString("MODIFIED_DATE"));
                felt_orderD.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                felt_orderD.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                felt_orderD.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                felt_orderD.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                felt_orderD.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),felt_orderD);
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
    
    public boolean ShowHistory(String pDocNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO ='"+pDocNo+"'");
            System.out.println("SELECT * FROM PRODUCTION.GIDC_FELT_PROD_AMEND_H WHERE PA_DOC_NO ='"+pDocNo+"'");
            Ready=true;
            
            historyAmendID = pDocNo;
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
                strSQL="SELECT DISTINCT PA_DOC_NO,CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_FELT_PROD_AMEND, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PA_DOC_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,PA_DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT PA_DOC_NO,CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_FELT_PROD_AMEND, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PA_DOC_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,PA_DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT PA_DOC_NO,CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_FELT_PROD_AMEND, PRODUCTION.FELT_PROD_DOC_DATA   WHERE PA_DOC_NO=DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY PA_DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsProductionAmend ObjDoc=new clsProductionAmend();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("PA_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("CREATED_DATE"));
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
 
       
  public static String getNextFreeNo(int pCompanyId,int pModuleID,int pFirstFree,boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        String strNewNo="";
        int lnNewNo=0;
        String Prefix="";
        String Suffix="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
           //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyId+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFree;
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lnNewNo=rsTmp.getInt("LAST_USED_NO")+1;
                strNewNo=EITLERPGLOBAL.Padding(Integer.toString(lnNewNo),rsTmp.getInt("NO_LENGTH"),rsTmp.getString("PADDING_BY"));
                Prefix=rsTmp.getString("PREFIX_CHARS");
                Suffix=rsTmp.getString("SUFFIX_CHARS");
                
                if(UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyId+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFree);
                }
                
                strNewNo = Prefix+ strNewNo+Suffix;
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return strNewNo;
            }
            else {
                return "";
            }
        }
        catch(Exception e) {
            return "";
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
            
            System.out.println("SELECT PA_DOC_NO FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE PA_DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            rsTmp=stTmp.executeQuery("SELECT PA_DOC_NO FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE PA_DOC_NO='"+pDocNo+"'  AND APPROVED=0 AND CANCELED=0");
            
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
                //PA_DOC_NO,CREATED_DATE,RECEIVED_DATE FROM PRODUCTION.GIDC_FELT_PROD_AMEND
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.GIDC_FELT_PROD_AMEND WHERE PA_DOC_NO='"+pAmendNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                if(!Approved) {
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pAmendNo+"' AND MODULE_ID=795");
                }
                data.Execute("UPDATE PRODUCTION.GIDC_FELT_PROD_AMEND SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PA_DOC_NO='"+pAmendNo+"'");
                
            }
            catch(Exception e) {
                
            }
        }
        
    }
}
