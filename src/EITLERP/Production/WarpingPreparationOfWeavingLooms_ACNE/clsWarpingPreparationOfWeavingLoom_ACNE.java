/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.WarpingPreparationOfWeavingLooms_ACNE;

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

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsWarpingPreparationOfWeavingLoom_ACNE {
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet,rsHeader;
    //private ResultSet rsResultSet,
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltPerformanceTrackingDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyAmendDate="";
    private String historyAmendID="";
    private static int ModuleID = 847;
    
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
    public clsWarpingPreparationOfWeavingLoom_ACNE() {
        LastError = "";
        props=new HashMap();
        
        
        props.put("WP_ACNE_NO",new Variant(""));
        props.put("WP_ACNE_DATE",new Variant(""));
        props.put("MODULE_ID",new Variant(""));
        props.put("USER_ID",new Variant(""));
        props.put("RECEIVED_DATE",new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("APPROVER_REMARKS",new Variant(0));
        props.put("ENTRY_DATE",new Variant(0));
        
        props.put("WP_ACNE_NO",new Variant(""));
        props.put("WP_ACNE_DATE",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("NO_OF_DIVISION",new Variant(""));
        props.put("DIVISION_BY",new Variant(""));
        props.put("REMARK",new Variant(""));
        
        hmFeltPerformanceTrackingDetails=new HashMap();
        
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER ORDER BY WP_ACNE_NO");
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
        ResultSet  resultSetTemp,resultSetHistory,rsHeader,rsHeaderH;
        Statement  statementTemp, statementHistory,stHeader,stHeaderH;
        try {
            // Felt Order Updation data connection
            connection = data.getConn();
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL WHERE WP_ACNE_NO='1'");
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL_H WHERE WP_ACNE_NO='1'");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER WHERE WP_ACNE_NO='1'");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER_H WHERE WP_ACNE_NO='1'");

            //stTMP=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTMP=stTMP.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PERFORMANCE_TRACKING_SHEET_REGISTER WHERE PIECE_NO=''");
            
            //setAttribute("WP_ACNE_NO",);
                
            rsHeader.first();
            rsHeader.moveToInsertRow();
            
            rsHeader.updateString("WP_ACNE_NO",getAttribute("WP_ACNE_NO").getString());
            rsHeader.updateString("WP_ACNE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("WP_ACNE_DATE").getString()));
            rsHeader.updateString("AS_ON_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("AS_ON_DATE").getString()));
            rsHeader.updateString("REMARK",getAttribute("REMARK").getString());
            
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeader.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("MODIFIED_BY",0);
            rsHeader.updateString("MODIFIED_DATE","0000-00-00");
            rsHeader.updateBoolean("APPROVED",false);
            rsHeader.updateString("APPROVED_DATE","0000-00-00");
            rsHeader.updateBoolean("CANCELED",false);
            rsHeader.updateBoolean("REJECTED",false);
            rsHeader.updateString("REJECTED_DATE","0000-00-00");
            rsHeader.updateBoolean("CHANGED",false);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.formatDateDB("0000-00-00"));
            
            rsHeader.insertRow();
            
            rsHeaderH.first();
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateInt("REVISION_NO",1);
            
            rsHeaderH.updateString("WP_ACNE_NO",getAttribute("WP_ACNE_NO").getString());
            rsHeaderH.updateString("WP_ACNE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("WP_ACNE_DATE").getString()));
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeaderH.updateString("AS_ON_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("AS_ON_DATE").getString()));
            
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("WP_ACNE_NO",getAttribute("WP_ACNE_NO").getString());
            rsHeaderH.updateString("WP_ACNE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("WP_ACNE_DATE").getString()));
            
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHeaderH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHeaderH.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateInt("MODIFIED_BY",0);
            rsHeaderH.updateString("MODIFIED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("APPROVED",false);
            rsHeaderH.updateString("APPROVED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CANCELED",false);
            rsHeaderH.updateBoolean("REJECTED",false);
            rsHeaderH.updateString("REJECTED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CHANGED",false);
            rsHeaderH.updateString("CHANGED_DATE","0000-00-00");
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
           
            rsHeaderH.insertRow(); 
            
            //Now Insert records into FELT_CN_TEMP_HEADER & History tables
            for(int i=1;i<=hmFeltPerformanceTrackingDetails.size();i++) {
                clsWarpingPreparationOfWeavingLoom_ACNEDetails ObjFeltSalesOrderDetails = (clsWarpingPreparationOfWeavingLoom_ACNEDetails) hmFeltPerformanceTrackingDetails.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();
                
                resultSetTemp.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("WP_ACNE_NO", getAttribute("WP_ACNE_NO").getString());
                
                resultSetTemp.updateString("WP_ACNE_DATE", getAttribute("WP_ACNE_DATE").getString());
                resultSetTemp.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetTemp.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp.updateString("WARP_PREP_STATUS", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STATUS").getString());
                resultSetTemp.updateString("WARP_PREP_STARTED_ACTUAL", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STARTED_ACTUAL").getString());
                resultSetTemp.updateString("WARP_PREP_DATE_PLAN", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_DATE_PLAN").getString());
                resultSetTemp.updateString("TARGETTED_WARP_PREP_COMPLETE", ObjFeltSalesOrderDetails.getAttribute("TARGETTED_WARP_PREP_COMPLETE").getString());
                resultSetTemp.updateString("ACTUAL_WARP_PREP_COMPLETION", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION").getString());
                resultSetTemp.updateString("ACTUAL_WARP_PREP_COMPLETION_DATE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION_DATE").getString());
                //
                resultSetTemp.updateString("WINDING_CARDED_YARN_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ON_CONE").getString());
                resultSetTemp.updateString("PRIMARY_DOUBLING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING").getString());
                resultSetTemp.updateString("PRIMARY_TWISTING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING").getString());
                resultSetTemp.updateString("WINDING_PRIMARY_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE").getString());
                resultSetTemp.updateString("STREAMING_OF_CONES", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES").getString());
                
                resultSetTemp.updateString("ACTUAL_WARP_PREP_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_ACT_KGS").getString());
                resultSetTemp.updateString("ACTUAL_WARP_PREP_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_OUT_OF_KGS").getString());
                resultSetTemp.updateString("WINDING_CARDED_YARN_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ACT_KGS").getString());
                resultSetTemp.updateString("WINDING_CARDED_YARN_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_OUT_OF_KGS").getString());
                resultSetTemp.updateString("PRIMARY_DOUBLING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_ACT_KGS").getString());
                resultSetTemp.updateString("PRIMARY_DOUBLING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_OUT_OF_KGS").getString());
                resultSetTemp.updateString("PRIMARY_TWISTING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_ACT_KGS").getString());
                resultSetTemp.updateString("PRIMARY_TWISTING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_OUT_OF_KGS").getString());
                resultSetTemp.updateString("WINDING_PRIMARY_ON_CONE_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_ACT_KGS").getString());
                resultSetTemp.updateString("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS").getString());
                resultSetTemp.updateString("STREAMING_OF_CONES_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_ACT_KGS").getString());
                resultSetTemp.updateString("STREAMING_OF_CONES_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_OUT_OF_KGS").getString());
                
                resultSetTemp.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                
                resultSetTemp.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                
                resultSetHistory.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("WP_ACNE_NO", getAttribute("WP_ACNE_NO").getString());
                
                resultSetHistory.updateString("WP_ACNE_DATE", getAttribute("WP_ACNE_DATE").getString());
                resultSetHistory.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetHistory.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                resultSetHistory.updateString("WARP_PREP_STATUS", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STATUS").getString());
                resultSetHistory.updateString("WARP_PREP_STARTED_ACTUAL", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STARTED_ACTUAL").getString());
                resultSetHistory.updateString("WARP_PREP_DATE_PLAN", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_DATE_PLAN").getString());
                resultSetHistory.updateString("TARGETTED_WARP_PREP_COMPLETE", ObjFeltSalesOrderDetails.getAttribute("TARGETTED_WARP_PREP_COMPLETE").getString());
                resultSetHistory.updateString("ACTUAL_WARP_PREP_COMPLETION", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION").getString());
                resultSetHistory.updateString("ACTUAL_WARP_PREP_COMPLETION_DATE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION_DATE").getString());
                //
                resultSetHistory.updateString("WINDING_CARDED_YARN_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ON_CONE").getString());
                resultSetHistory.updateString("PRIMARY_DOUBLING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING").getString());
                resultSetHistory.updateString("PRIMARY_TWISTING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING").getString());
                resultSetHistory.updateString("WINDING_PRIMARY_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE").getString());
                resultSetHistory.updateString("STREAMING_OF_CONES", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES").getString());

                resultSetHistory.updateString("ACTUAL_WARP_PREP_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_ACT_KGS").getString());
                resultSetHistory.updateString("ACTUAL_WARP_PREP_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_OUT_OF_KGS").getString());
                resultSetHistory.updateString("WINDING_CARDED_YARN_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ACT_KGS").getString());
                resultSetHistory.updateString("WINDING_CARDED_YARN_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_OUT_OF_KGS").getString());
                resultSetHistory.updateString("PRIMARY_DOUBLING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_ACT_KGS").getString());
                resultSetHistory.updateString("PRIMARY_DOUBLING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_OUT_OF_KGS").getString());
                resultSetHistory.updateString("PRIMARY_TWISTING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_ACT_KGS").getString());
                resultSetHistory.updateString("PRIMARY_TWISTING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_OUT_OF_KGS").getString());
                resultSetHistory.updateString("WINDING_PRIMARY_ON_CONE_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_ACT_KGS").getString());
                resultSetHistory.updateString("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS").getString());
                resultSetHistory.updateString("STREAMING_OF_CONES_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_ACT_KGS").getString());
                resultSetHistory.updateString("STREAMING_OF_CONES_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_OUT_OF_KGS").getString());
                
                
                resultSetHistory.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                
                
                resultSetHistory.insertRow();
            }
            
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("WP_ACNE_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="WP_ACNE_NO";
            
            
            
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
      
            }
            
            
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
        
        ResultSet  resultSetTemp,resultSetHistory,rsHeader,rsHeaderH;
        Statement  statementTemp, statementHistory,stHeader,stHeaderH;
        int revisionNo;
        try {
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL_H WHERE WP_ACNE_NO='"+getAttribute("WP_ACNE_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL WHERE WP_ACNE_NO='"+getAttribute("WP_ACNE_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL_H WHERE WP_ACNE_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER_H WHERE WP_ACNE_NO=''");
            
            
            
            
            //Now Update records into FELT_CN_TEMP_HEADER tables
            
            resultSet.updateString("WP_ACNE_NO",getAttribute("WP_ACNE_NO").getString());
            resultSet.updateString("WP_ACNE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("WP_ACNE_DATE").getString()));
            resultSet.updateString("REMARK",getAttribute("REMARK").getString());
            resultSet.updateString("AS_ON_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("AS_ON_DATE").getString()));
            
            resultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            resultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            resultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            
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
            resultSet.updateBoolean("REJECTED",false);
            resultSet.updateString("REJECTED_DATE","0000-00-00");
            //resultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            resultSet.updateBoolean("CHANGED",true);
            resultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            try{
                    resultSet.updateRow();
            }catch(Exception e)
            {
                System.out.println("Header Updation Failed : "+e.getMessage());
            }
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER_H WHERE WP_ACNE_NO='"+getAttribute("WP_ACNE_NO").getString()+"'");
            
            RevNo++;
           
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateString("REVISION_NO",RevNo+"");
            
            rsHeaderH.updateString("WP_ACNE_NO",getAttribute("WP_ACNE_NO").getString());
            rsHeaderH.updateString("WP_ACNE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("WP_ACNE_DATE").getString()));
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeaderH.updateString("AS_ON_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("AS_ON_DATE").getString()));
            rsHeaderH.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHeaderH.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("MODIFIED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHeaderH.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHeaderH.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            if(getAttribute("APPROVAL_STATUS").getString().equals("F"))
            {
                rsHeaderH.updateBoolean("APPROVED",true);
                rsHeaderH.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            }
            else
            {
                rsHeaderH.updateBoolean("APPROVED",false);
                rsHeaderH.updateString("APPROVED_DATE","0000-00-00");
            }
            rsHeaderH.updateBoolean("CANCELED",false);
            rsHeaderH.updateBoolean("REJECTED",false);
            rsHeaderH.updateString("REJECTED_DATE","0000-00-00");
            rsHeaderH.updateBoolean("CHANGED",true);
            rsHeaderH.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            rsHeaderH.updateString("FROM_IP",""+str_split[1]);
            
            rsHeaderH.insertRow();
            String OrderNo=getAttribute("WP_ACNE_NO").getString();
            data.Execute("DELETE FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL WHERE WP_ACNE_NO='"+OrderNo+"'");
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL WHERE WP_ACNE_NO='1'");
             
            
            int RevNoH=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL_H WHERE WP_ACNE_NO='"+getAttribute("WP_ACNE_NO").getString()+"'");
            RevNoH++;

            resultSetHistory.moveToInsertRow();
            
            for(int i=1;i<=hmFeltPerformanceTrackingDetails.size();i++) {
                clsWarpingPreparationOfWeavingLoom_ACNEDetails ObjFeltSalesOrderDetails=(clsWarpingPreparationOfWeavingLoom_ACNEDetails) hmFeltPerformanceTrackingDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetTemp.updateString("WP_ACNE_NO", getAttribute("WP_ACNE_NO").getString());
                
                resultSetTemp.updateString("WP_ACNE_DATE", getAttribute("WP_ACNE_DATE").getString());
                resultSetTemp.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetTemp.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                resultSetTemp.updateString("WARP_PREP_STATUS", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STATUS").getString());
                resultSetTemp.updateString("WARP_PREP_STARTED_ACTUAL", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STARTED_ACTUAL").getString());
                resultSetTemp.updateString("WARP_PREP_DATE_PLAN", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_DATE_PLAN").getString());
                resultSetTemp.updateString("TARGETTED_WARP_PREP_COMPLETE", ObjFeltSalesOrderDetails.getAttribute("TARGETTED_WARP_PREP_COMPLETE").getString());
                resultSetTemp.updateString("ACTUAL_WARP_PREP_COMPLETION", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION").getString());
                resultSetTemp.updateString("ACTUAL_WARP_PREP_COMPLETION_DATE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION_DATE").getString());
                //
                resultSetTemp.updateString("WINDING_CARDED_YARN_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ON_CONE").getString());
                resultSetTemp.updateString("PRIMARY_DOUBLING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING").getString());
                resultSetTemp.updateString("PRIMARY_TWISTING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING").getString());
                resultSetTemp.updateString("WINDING_PRIMARY_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE").getString());
                resultSetTemp.updateString("STREAMING_OF_CONES", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES").getString());
                
                resultSetTemp.updateString("ACTUAL_WARP_PREP_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_ACT_KGS").getString());
                resultSetTemp.updateString("ACTUAL_WARP_PREP_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_OUT_OF_KGS").getString());
                resultSetTemp.updateString("WINDING_CARDED_YARN_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ACT_KGS").getString());
                resultSetTemp.updateString("WINDING_CARDED_YARN_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_OUT_OF_KGS").getString());
                resultSetTemp.updateString("PRIMARY_DOUBLING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_ACT_KGS").getString());
                resultSetTemp.updateString("PRIMARY_DOUBLING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_OUT_OF_KGS").getString());
                resultSetTemp.updateString("PRIMARY_TWISTING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_ACT_KGS").getString());
                resultSetTemp.updateString("PRIMARY_TWISTING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_OUT_OF_KGS").getString());
                resultSetTemp.updateString("WINDING_PRIMARY_ON_CONE_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_ACT_KGS").getString());
                resultSetTemp.updateString("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS").getString());
                resultSetTemp.updateString("STREAMING_OF_CONES_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_ACT_KGS").getString());
                resultSetTemp.updateString("STREAMING_OF_CONES_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_OUT_OF_KGS").getString());
                                
                resultSetTemp.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                
                
                resultSetTemp.insertRow();
                
                
                resultSetHistory.updateInt("REVISION_NO",RevNoH);
                
                resultSetHistory.updateString("SR_NO", ObjFeltSalesOrderDetails.getAttribute("SR_NO").getString());
                resultSetHistory.updateString("WP_ACNE_NO", getAttribute("WP_ACNE_NO").getString());
                
                resultSetHistory.updateString("WP_ACNE_DATE", getAttribute("WP_ACNE_DATE").getString());
                resultSetHistory.updateString("PRODUCT_GROUP", ObjFeltSalesOrderDetails.getAttribute("PRODUCT_GROUP").getString());
                resultSetHistory.updateString("LOOM_NO", ObjFeltSalesOrderDetails.getAttribute("LOOM_NO").getString());
                resultSetHistory.updateString("WARP_PREP_STATUS", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STATUS").getString());
                resultSetHistory.updateString("WARP_PREP_STARTED_ACTUAL", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_STARTED_ACTUAL").getString());
                resultSetHistory.updateString("WARP_PREP_DATE_PLAN", ObjFeltSalesOrderDetails.getAttribute("WARP_PREP_DATE_PLAN").getString());
                resultSetHistory.updateString("TARGETTED_WARP_PREP_COMPLETE", ObjFeltSalesOrderDetails.getAttribute("TARGETTED_WARP_PREP_COMPLETE").getString());
                resultSetHistory.updateString("ACTUAL_WARP_PREP_COMPLETION", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION").getString());
                resultSetHistory.updateString("ACTUAL_WARP_PREP_COMPLETION_DATE", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_COMPLETION_DATE").getString());
                //
                resultSetHistory.updateString("WINDING_CARDED_YARN_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ON_CONE").getString());
                resultSetHistory.updateString("PRIMARY_DOUBLING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING").getString());
                resultSetHistory.updateString("PRIMARY_TWISTING", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING").getString());
                resultSetHistory.updateString("WINDING_PRIMARY_ON_CONE", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE").getString());
                resultSetHistory.updateString("STREAMING_OF_CONES", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES").getString());
                
                resultSetHistory.updateString("ACTUAL_WARP_PREP_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_ACT_KGS").getString());
                resultSetHistory.updateString("ACTUAL_WARP_PREP_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("ACTUAL_WARP_PREP_OUT_OF_KGS").getString());
                resultSetHistory.updateString("WINDING_CARDED_YARN_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_ACT_KGS").getString());
                resultSetHistory.updateString("WINDING_CARDED_YARN_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_CARDED_YARN_OUT_OF_KGS").getString());
                resultSetHistory.updateString("PRIMARY_DOUBLING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_ACT_KGS").getString());
                resultSetHistory.updateString("PRIMARY_DOUBLING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_DOUBLING_OUT_OF_KGS").getString());
                resultSetHistory.updateString("PRIMARY_TWISTING_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_ACT_KGS").getString());
                resultSetHistory.updateString("PRIMARY_TWISTING_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("PRIMARY_TWISTING_OUT_OF_KGS").getString());
                resultSetHistory.updateString("WINDING_PRIMARY_ON_CONE_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_ACT_KGS").getString());
                resultSetHistory.updateString("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS").getString());
                resultSetHistory.updateString("STREAMING_OF_CONES_ACT_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_ACT_KGS").getString());
                resultSetHistory.updateString("STREAMING_OF_CONES_OUT_OF_KGS", ObjFeltSalesOrderDetails.getAttribute("STREAMING_OF_CONES_OUT_OF_KGS").getString());
                
                resultSetHistory.updateString("REMARKS", ObjFeltSalesOrderDetails.getAttribute("REMARKS").getString());
                
                resultSetHistory.insertRow();
                
                // Final Approval and save to PIECE REGISTER 
                if(getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals(""))
                {
                    
                }
            }
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("WP_ACNE_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="WP_ACNE_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
       
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER SET REJECTED=0,CHANGED=1 WHERE WP_ACNE_NO ='"+getAttribute("WP_ACNE_NO").getString()+"'");
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
            resultSetTemp.close();
            statementTemp.close();
            resultSetHistory.close();
            statementHistory.close();
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
    public boolean CanDelete(String documentNo,String stringProductionDate,int userID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            tmpStmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER WHERE  WP_ACNE_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND USER_ID="+userID+" AND WP_ACNE_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER WHERE "
                    + " WP_ACNE_DATE = '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND WP_ACNE_NO ='" + documentNo + "'";
                 
                    tmpStmt.executeUpdate(strSQL);
                    LoadData();
                    return true;
                }
                else {
                    LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER WHERE WP_ACNE_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER WHERE  " + stringFindQuery + "";
            System.out.println("STR SQL : "+strSql);
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
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
    
        try {
           
            setAttribute("REVISION_NO",0);
            
            setAttribute("WP_ACNE_NO",resultSet.getString("WP_ACNE_NO"));
            setAttribute("WP_ACNE_DATE",resultSet.getDate("WP_ACNE_DATE"));
            setAttribute("AS_ON_DATE",resultSet.getDate("AS_ON_DATE"));
            
            setAttribute("REMARK",resultSet.getString("REMARK"));
            
            if(HistoryView) {
                RevNo=resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            } 
           
            setAttribute("CREATED_BY",resultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",resultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",resultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",resultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED",resultSet.getInt("APPROVED"));
            //setAttribute("APPROVED_BY",resultSet.getInt("APPROVED_BY"));
            setAttribute("APPROVED_DATE",resultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",resultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED_BY",resultSet.getBoolean("REJECTED_BY"));
            setAttribute("REJECTED_DATE",resultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED",resultSet.getInt("CANCELED"));
            setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
           // setAttribute("REJECTED_REMARKS",resultSet.getString("REJECTED_REMARKS"));
            
            hmFeltPerformanceTrackingDetails.clear();
            
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            if(HistoryView) {
                resultSetTemp=statementTemp.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL_H WHERE WP_ACNE_NO='"+resultSet.getString("WP_ACNE_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL WHERE WP_ACNE_NO='"+resultSet.getString("WP_ACNE_NO")+"'  ORDER BY WP_ACNE_NO");
            }
            
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                clsWarpingPreparationOfWeavingLoom_ACNEDetails ObjFeltSalesOrderDetails = new clsWarpingPreparationOfWeavingLoom_ACNEDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("WP_ACNE_NO",resultSetTemp.getString("WP_ACNE_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("WP_ACNE_DATE",resultSetTemp.getString("WP_ACNE_DATE"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP",resultSetTemp.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("LOOM_NO",resultSetTemp.getString("LOOM_NO"));
                ObjFeltSalesOrderDetails.setAttribute("WARP_PREP_STATUS",resultSetTemp.getString("WARP_PREP_STATUS"));
                ObjFeltSalesOrderDetails.setAttribute("WARP_PREP_STARTED_ACTUAL",resultSetTemp.getString("WARP_PREP_STARTED_ACTUAL"));
                ObjFeltSalesOrderDetails.setAttribute("WARP_PREP_DATE_PLAN",resultSetTemp.getString("WARP_PREP_DATE_PLAN"));
                ObjFeltSalesOrderDetails.setAttribute("TARGETTED_WARP_PREP_COMPLETE",resultSetTemp.getString("TARGETTED_WARP_PREP_COMPLETE"));
                ObjFeltSalesOrderDetails.setAttribute("ACTUAL_WARP_PREP_COMPLETION",resultSetTemp.getString("ACTUAL_WARP_PREP_COMPLETION"));
                ObjFeltSalesOrderDetails.setAttribute("ACTUAL_WARP_PREP_COMPLETION_DATE",resultSetTemp.getString("ACTUAL_WARP_PREP_COMPLETION_DATE"));
                //
                ObjFeltSalesOrderDetails.setAttribute("WINDING_CARDED_YARN_ON_CONE",resultSetTemp.getString("WINDING_CARDED_YARN_ON_CONE"));
                ObjFeltSalesOrderDetails.setAttribute("PRIMARY_DOUBLING",resultSetTemp.getString("PRIMARY_DOUBLING"));
                ObjFeltSalesOrderDetails.setAttribute("PRIMARY_TWISTING",resultSetTemp.getString("PRIMARY_TWISTING"));
                ObjFeltSalesOrderDetails.setAttribute("WINDING_PRIMARY_ON_CONE",resultSetTemp.getString("WINDING_PRIMARY_ON_CONE"));
                ObjFeltSalesOrderDetails.setAttribute("STREAMING_OF_CONES",resultSetTemp.getString("STREAMING_OF_CONES"));
                ObjFeltSalesOrderDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                
                ObjFeltSalesOrderDetails.setAttribute("ACTUAL_WARP_PREP_ACT_KGS",resultSetTemp.getString("ACTUAL_WARP_PREP_ACT_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("ACTUAL_WARP_PREP_OUT_OF_KGS",resultSetTemp.getString("ACTUAL_WARP_PREP_OUT_OF_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("WINDING_CARDED_YARN_ACT_KGS",resultSetTemp.getString("WINDING_CARDED_YARN_ACT_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("WINDING_CARDED_YARN_OUT_OF_KGS",resultSetTemp.getString("WINDING_CARDED_YARN_OUT_OF_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("PRIMARY_DOUBLING_ACT_KGS",resultSetTemp.getString("PRIMARY_DOUBLING_ACT_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("PRIMARY_DOUBLING_OUT_OF_KGS",resultSetTemp.getString("PRIMARY_DOUBLING_OUT_OF_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("PRIMARY_TWISTING_ACT_KGS",resultSetTemp.getString("PRIMARY_TWISTING_ACT_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("PRIMARY_TWISTING_OUT_OF_KGS",resultSetTemp.getString("PRIMARY_TWISTING_OUT_OF_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("WINDING_PRIMARY_ON_CONE_ACT_KGS",resultSetTemp.getString("WINDING_PRIMARY_ON_CONE_ACT_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS",resultSetTemp.getString("WINDING_PRIMARY_ON_CONE_OUT_OF_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("STREAMING_OF_CONES_ACT_KGS",resultSetTemp.getString("STREAMING_OF_CONES_ACT_KGS"));
                ObjFeltSalesOrderDetails.setAttribute("STREAMING_OF_CONES_OUT_OF_KGS",resultSetTemp.getString("STREAMING_OF_CONES_OUT_OF_KGS"));
                
                hmFeltPerformanceTrackingDetails.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            
            serialNoCounter=0;
            
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean setHistoryData(String pProductionDate,String pDocNo) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        try {
           
            //Now Populate the collection, first clear the collection
            hmFeltPerformanceTrackingDetails.clear();
            
            String strSql = "SELECT * FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER_H WHERE WP_ACNE_NO = " + pDocNo + "";
            System.out.println("STR SQL : "+strSql);
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("WP_ACNE_NO",resultSet.getString("WP_ACNE_NO"));
            setAttribute("REVISION_NO",resultSet.getString("REVISION_NO"));
                setAttribute("UPDATED_BY",resultSet.getString("UPDATED_BY"));
                setAttribute("WP_ACNE_DATE",resultSet.getString("WP_ACNE_DATE"));
                setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
                
                setAttribute("WP_ACNE_NO",resultSet.getString("WP_ACNE_NO"));
                setAttribute("WP_ACNE_DATE",resultSet.getDate("WP_ACNE_DATE"));
                setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
                setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
                setAttribute("MACHINE_NO",resultSet.getString("MACHINE_NO"));
                setAttribute("POSITION",resultSet.getString("POSITION"));
            
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL_H WHERE WP_ACNE_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                clsWarpingPreparationOfWeavingLoom_ACNEDetails ObjFeltSalesOrderDetails = new clsWarpingPreparationOfWeavingLoom_ACNEDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("WP_ACNE_NO",resultSetTemp.getString("WP_ACNE_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("SUPPLIER",resultSetTemp.getString("SUPPLIER"));
                ObjFeltSalesOrderDetails.setAttribute("SUPPLIER_NAME",resultSetTemp.getString("SUPPLIER_NAME"));
                ObjFeltSalesOrderDetails.setAttribute("PIECE_NO",resultSetTemp.getString("PIECE_NO"));
                ObjFeltSalesOrderDetails.setAttribute("PRODUCT_GROUP",resultSetTemp.getString("PRODUCT_GROUP"));
                ObjFeltSalesOrderDetails.setAttribute("MOUNT_DATE",resultSetTemp.getString("MOUNT_DATE"));
                ObjFeltSalesOrderDetails.setAttribute("DEMOUNT_DATE",resultSetTemp.getString("DEMOUNT_DATE"));
                ObjFeltSalesOrderDetails.setAttribute("SHUT_DAYS",resultSetTemp.getString("SHUT_DAYS"));
                ObjFeltSalesOrderDetails.setAttribute("ACTUAL_LIFE_DAYS",resultSetTemp.getString("ACTUAL_LIFE_DAYS"));
                ObjFeltSalesOrderDetails.setAttribute("LIFE_TONNAGE",resultSetTemp.getString("LIFE_TONNAGE"));
                ObjFeltSalesOrderDetails.setAttribute("TYPE_OF_DAMAGE",resultSetTemp.getString("TYPE_OF_DAMAGE"));
                ObjFeltSalesOrderDetails.setAttribute("REMARKS",resultSetTemp.getString("REMARKS"));
                ObjFeltSalesOrderDetails.setAttribute("LENGTH",resultSetTemp.getString("LENGTH"));
                ObjFeltSalesOrderDetails.setAttribute("WIDTH",resultSetTemp.getString("WIDTH"));
                ObjFeltSalesOrderDetails.setAttribute("GSM",resultSetTemp.getString("GSM"));
                ObjFeltSalesOrderDetails.setAttribute("WEIGHT",resultSetTemp.getString("WEIGHT"));
                ObjFeltSalesOrderDetails.setAttribute("SQMTR",resultSetTemp.getString("SQMTR"));
                ObjFeltSalesOrderDetails.setAttribute("STYLE",resultSetTemp.getString("STYLE"));
                ObjFeltSalesOrderDetails.setAttribute("AVERAGE_LIFE",resultSetTemp.getString("AVERAGE_LIFE"));
                
               
               hmFeltPerformanceTrackingDetails.put(Integer.toString(serialNoCounter),ObjFeltSalesOrderDetails);
            }
            resultSetTemp.close();
            statementTemp.close();
            resultSet.close();
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
       // String stringProductionDate1 =EITLERPGLOBAL.formatDate(stringProductionDate);
        
        
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER_H WHERE WP_ACNE_NO='"+productionDocumentNo+"'");
            while(rsTmp.next()) {
                clsWarpingPreparationOfWeavingLoom_ACNE felt_order = new clsWarpingPreparationOfWeavingLoom_ACNE();
                
                felt_order.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                felt_order.setAttribute("MODIFIED_BY",rsTmp.getString("MODIFIED_BY"));
                felt_order.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                felt_order.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                felt_order.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                felt_order.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                felt_order.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),felt_order);
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
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER_H WHERE WP_ACNE_NO ='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT H.WP_ACNE_NO,H.WP_ACNE_DATE,RECEIVED_DATE FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.WP_ACNE_NO=H.WP_ACNE_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,H.WP_ACNE_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT H.WP_ACNE_NO,H.WP_ACNE_DATE,RECEIVED_DATE FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER H,  PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.WP_ACNE_NO=H.WP_ACNE_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,H.WP_ACNE_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT H.WP_ACNE_NO,H.WP_ACNE_DATE,RECEIVED_DATE FROM PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.WP_ACNE_NO=H.WP_ACNE_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY H.WP_ACNE_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsWarpingPreparationOfWeavingLoom_ACNE ObjDoc=new clsWarpingPreparationOfWeavingLoom_ACNE();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("WP_ACNE_NO",rsTmp.getString("WP_ACNE_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("WP_ACNE_DATE"));
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
}
