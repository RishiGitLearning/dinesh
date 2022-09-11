/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.FeltDesignMaster;

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
public class clsFeltDesignMaster {
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
    private static int ModuleID = 836;
    
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
    public clsFeltDesignMaster() {
        LastError = "";
        props=new HashMap();
        
        
        props.put("DESIGN_DOC_NO",new Variant(""));
        props.put("DESIGN_DOC_DATE",new Variant(""));
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
        
        props.put("DESIGN_DOC_NO",new Variant(""));
        props.put("DESIGN_DOC_DATE",new Variant(""));
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER ORDER BY DESIGN_DOC_NO");
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
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE DESIGN_DOC_NO='1'");
            
            // Felt order Updation data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL_H WHERE DESIGN_DOC_NO='1'");
            
            stHeader=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE DESIGN_DOC_NO='1'");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_HEADER_H WHERE DESIGN_DOC_NO='1'");

            //stTMP=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTMP=stTMP.executeQuery("SELECT * FROM  PRODUCTION.FELT_SALES_PERFORMANCE_TRACKING_SHEET_REGISTER WHERE PIECE_NO=''");
            
            //setAttribute("DESIGN_DOC_NO",);
                
            rsHeader.first();
            rsHeader.moveToInsertRow();
            
            rsHeader.updateString("DESIGN_DOC_NO",getAttribute("DESIGN_DOC_NO").getString());
            rsHeader.updateString("DESIGN_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DESIGN_DOC_DATE").getString()));
            
            rsHeader.updateString("DESIGN_CHANGED_BY",getAttribute("DESIGN_CHANGED_BY").getString());
            
            rsHeader.updateString("LAYER_TYPE",getAttribute("LAYER_TYPE").getString());
            rsHeader.updateString("DESIGN_REVISION_NO",getAttribute("DESIGN_REVISION_NO").getString());
            rsHeader.updateString("UPN_NO",getAttribute("UPN_NO").getString());
            rsHeader.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeader.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeader.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsHeader.updateString("POSITION_NO",getAttribute("POSITION_NO").getString());
            rsHeader.updateString("POSITION_DESIGN_NO",getAttribute("POSITION_DESIGN_NO").getString());
            rsHeader.updateString("REFERENCE",getAttribute("REFERENCE").getString());
            rsHeader.updateString("PRESS_CATEGORY",getAttribute("PRESS_CATEGORY").getString());
            rsHeader.updateString("STYLE",getAttribute("STYLE").getString());
            rsHeader.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            rsHeader.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHeader.updateString("LENGTH",getAttribute("LENGTH").getString());
            rsHeader.updateString("MFG_LENGTH",getAttribute("MFG_LENGTH").getString());
            rsHeader.updateString("MKG_WIDTH",getAttribute("MKG_WIDTH").getString());
            rsHeader.updateString("MFG_WIDTH",getAttribute("MFG_WIDTH").getString());
            
            rsHeader.updateString("WIDTH",getAttribute("WIDTH").getString());
            rsHeader.updateString("GSM",getAttribute("GSM").getString());
            rsHeader.updateString("SQMTR",getAttribute("SQMTR").getString());
            rsHeader.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            rsHeader.updateString("REASON_OF_REVISION",getAttribute("REASON_OF_REVISION").getString());
            rsHeader.updateString("WARP",getAttribute("WARP").getString());
            rsHeader.updateString("WEFT",getAttribute("WEFT").getString());
            rsHeader.updateString("ENDS_10CM",getAttribute("ENDS_10CM").getString());
            rsHeader.updateString("PICKS_10CM",getAttribute("PICKS_10CM").getString());
            rsHeader.updateString("WIDTH_FACT",getAttribute("WIDTH_FACT").getString());
            rsHeader.updateString("NO_ENDS",getAttribute("NO_ENDS").getString());
            rsHeader.updateString("REED",getAttribute("REED").getString());
            rsHeader.updateString("REED_SPACE",getAttribute("REED_SPACE").getString());
            rsHeader.updateString("WEAVE",getAttribute("WEAVE").getString());
            rsHeader.updateString("DRAW",getAttribute("DRAW").getString());
            rsHeader.updateString("SYN",getAttribute("SYN").getString());
            rsHeader.updateString("GSM_ORD",getAttribute("GSM_ORD").getString());
            rsHeader.updateString("GSM_MFG",getAttribute("GSM_MFG").getString());
            rsHeader.updateString("W_WGT",getAttribute("W_WGT").getString());
            rsHeader.updateString("WE_WGT",getAttribute("WE_WGT").getString());
            rsHeader.updateString("TK_UP",getAttribute("TK_UP").getString());
            rsHeader.updateString("THEO_WEIGHT",getAttribute("THEO_WEIGHT").getString());
            rsHeader.updateString("THEO_LENGTH",getAttribute("THEO_LENGTH").getString());
            rsHeader.updateString("THEO_PICKS",getAttribute("THEO_PICKS").getString());
            rsHeader.updateString("LENGTH_FACT",getAttribute("LENGTH_FACT").getString());
            rsHeader.updateString("END_LENGTH",getAttribute("END_LENGTH").getString());
            rsHeader.updateString("T_THICK",getAttribute("T_THICK").getString());
            rsHeader.updateString("THEO_CFM",getAttribute("THEO_CFM").getString());
            rsHeader.updateString("BASE_SKG_TOTAL",getAttribute("BASE_SKG_TOTAL").getString());
            rsHeader.updateString("TOTAL_SKG",getAttribute("TOTAL_SKG").getString());
            rsHeader.updateString("TRIM_WEIGHT",getAttribute("TRIM_WEIGHT").getString());
            rsHeader.updateString("WEIGHT_RANGE",getAttribute("WEIGHT_RANGE").getString());
            rsHeader.updateString("BILL_WEIGHT",getAttribute("BILL_WEIGHT").getString());
            rsHeader.updateString("KILLOS",getAttribute("KILLOS").getString());
                                     //FACE_SINGLE
            rsHeader.updateString("FACE_SINGLE",getAttribute("FACE_SINGLE").getString());
            rsHeader.updateString("BACK_SINGLE",getAttribute("BACK_SINGLE").getString());
            rsHeader.updateString("PER_COUNT",getAttribute("PER_COUNT").getString());
            rsHeader.updateString("WEAVING_INSTRUCTION",getAttribute("WEAVING_INSTRUCTION").getString());
            rsHeader.updateString("DRYER_WIDTH_MARK_WET_DRY",getAttribute("DRYER_WIDTH_MARK_WET_DRY").getString());
            rsHeader.updateString("TAG_INSTRUCTION",getAttribute("TAG_INSTRUCTION").getString());
            rsHeader.updateString("FINISHING_INSTRUCTION",getAttribute("FINISHING_INSTRUCTION").getString());
            rsHeader.updateString("NEEDLING_INSTRUCTION",getAttribute("NEEDLING_INSTRUCTION").getString());
            rsHeader.updateString("DRESS_DRAW_WEAVING_INSTRUCTION",getAttribute("DRESS_DRAW_WEAVING_INSTRUCTION").getString());
            rsHeader.updateString("BASE_GSM",getAttribute("BASE_GSM").getString());
            rsHeader.updateString("BASE_GSM_A",getAttribute("BASE_GSM_A").getString());
            rsHeader.updateString("BASE_GSM_B",getAttribute("BASE_GSM_B").getString());
            rsHeader.updateString("WEB_GSM",getAttribute("WEB_GSM").getString());
            rsHeader.updateString("TOTAL_GSM",getAttribute("TOTAL_GSM").getString());
            rsHeader.updateString("SAFETY",getAttribute("SAFETY").getString());
            rsHeader.updateString("NEEDLING_TANTION",getAttribute("NEEDLING_TANTION").getString());
            rsHeader.updateString("PER_WEB",getAttribute("PER_WEB").getString());
            rsHeader.updateString("PER_SYN_BASE",getAttribute("PER_SYN_BASE").getString());
            rsHeader.updateString("PER_SYN_WEB",getAttribute("PER_SYN_WEB").getString());
            rsHeader.updateString("WEB_ON_FACE",getAttribute("WEB_ON_FACE").getString());
            rsHeader.updateString("WEB_ON_BACK",getAttribute("WEB_ON_BACK").getString());
            rsHeader.updateString("PAINT_LINES",getAttribute("PAINT_LINES").getString());
            rsHeader.updateString("LOOM_NO",getAttribute("LOOM_NO").getString());
            rsHeader.updateString("NEEDLING_TEN_FACT",getAttribute("NEEDLING_TEN_FACT").getString());
            rsHeader.updateString("TW_BE_ND",getAttribute("TW_BE_ND").getString());
            rsHeader.updateString("TW_BE_ND_A",getAttribute("TW_BE_ND_A").getString());
            rsHeader.updateString("TW_BE_ND_B",getAttribute("TW_BE_ND_B").getString());
            rsHeader.updateString("T_WEB_WEIGHT",getAttribute("T_WEB_WEIGHT").getString());
            rsHeader.updateString("T_TOTAL_WEIGHT",getAttribute("T_TOTAL_WEIGHT").getString());
            rsHeader.updateString("REMARK",getAttribute("REMARK").getString());
            
            rsHeader.updateString("STYLE_MULTILAYER",getAttribute("STYLE_MULTILAYER").getString());
            rsHeader.updateString("PRODUCT_GROUP_MULTILAYER",getAttribute("PRODUCT_GROUP_MULTILAYER").getString());
            rsHeader.updateString("PRODUCT_CODE_MULTILAYER",getAttribute("PRODUCT_CODE_MULTILAYER").getString());
            rsHeader.updateString("LENGTH_MULTILAYER",getAttribute("LENGTH_MULTILAYER").getString());
            rsHeader.updateString("WIDTH_MULTILAYER",getAttribute("WIDTH_MULTILAYER").getString());
            rsHeader.updateString("GSM_MULTILAYER",getAttribute("GSM_MULTILAYER").getString());
            rsHeader.updateString("SQMTR_MULTILAYER",getAttribute("SQMTR_MULTILAYER").getString());
            rsHeader.updateString("WEIGHT_MULTILAYER",getAttribute("WEIGHT_MULTILAYER").getString());
            rsHeader.updateString("WEAVE_MULTILAYER",getAttribute("WEAVE_MULTILAYER").getString());
            rsHeader.updateString("WEFT_MULTILAYER",getAttribute("WEFT_MULTILAYER").getString());
            rsHeader.updateString("PICKS_10CM_MULTILAYER",getAttribute("PICKS_10CM_MULTILAYER").getString());
            rsHeader.updateString("WARP_MULTILAYER",getAttribute("WARP_MULTILAYER").getString());
            rsHeader.updateString("ENDS_10CM_MULTILAYER",getAttribute("ENDS_10CM_MULTILAYER").getString());
            rsHeader.updateString("WIDTH_FACT_MULTILAYER",getAttribute("WIDTH_FACT_MULTILAYER").getString());
            rsHeader.updateString("NO_ENDS_MULTILAYER",getAttribute("NO_ENDS_MULTILAYER").getString());
            rsHeader.updateString("REED_MULTILAYER",getAttribute("REED_MULTILAYER").getString());
            rsHeader.updateString("REED_SPACE_MULTILAYER",getAttribute("REED_SPACE_MULTILAYER").getString());
            rsHeader.updateString("REASON_OF_REVISION_MULTILAYER",getAttribute("REASON_OF_REVISION_MULTILAYER").getString());
            rsHeader.updateString("DRAW_MULTILAYER",getAttribute("DRAW_MULTILAYER").getString());
            rsHeader.updateString("GSM_ORD_MULTILAYER",getAttribute("GSM_ORD_MULTILAYER").getString());
            rsHeader.updateString("GSM_MFG_MULTILAYER",getAttribute("GSM_MFG_MULTILAYER").getString());
            rsHeader.updateString("W_WGT_MULTILAYER",getAttribute("W_WGT_MULTILAYER").getString());
            rsHeader.updateString("WE_WGT_MULTILAYER",getAttribute("WE_WGT_MULTILAYER").getString());
            rsHeader.updateString("TK_UP_MULTILAYER",getAttribute("TK_UP_MULTILAYER").getString());
            rsHeader.updateString("THEO_WEIGHT_MULTILAYER",getAttribute("THEO_WEIGHT_MULTILAYER").getString());
            rsHeader.updateString("THEO_LENGTH_MULTILAYER",getAttribute("THEO_LENGTH_MULTILAYER").getString());
            rsHeader.updateString("THEO_PICKS_MULTILAYER",getAttribute("THEO_PICKS_MULTILAYER").getString());
            rsHeader.updateString("LENGTH_FACT_MULTILAYER",getAttribute("LENGTH_FACT_MULTILAYER").getString());
            rsHeader.updateString("END_LENGTH_MULTILAYER",getAttribute("END_LENGTH_MULTILAYER").getString());
            rsHeader.updateString("T_THICK_MULTILAYER",getAttribute("T_THICK_MULTILAYER").getString());
            rsHeader.updateString("FACE_SINGLE_MULTILAYER",getAttribute("FACE_SINGLE_MULTILAYER").getString());
            rsHeader.updateString("KILLOS_MULTILAYER",getAttribute("KILLOS_MULTILAYER").getString());
            rsHeader.updateString("BILL_WEIGHT_MULTILAYER",getAttribute("BILL_WEIGHT_MULTILAYER").getString());
            rsHeader.updateString("TRIM_WEIGHT_MULTILAYER",getAttribute("TRIM_WEIGHT_MULTILAYER").getString());
            rsHeader.updateString("TOTAL_SKG_MULTILAYER",getAttribute("TOTAL_SKG_MULTILAYER").getString());
            rsHeader.updateString("BASE_SKG_TOTAL_MULTILAYER",getAttribute("BASE_SKG_TOTAL_MULTILAYER").getString());
            rsHeader.updateString("THEO_CFM_MULTILAYER",getAttribute("THEO_CFM_MULTILAYER").getString());
            rsHeader.updateString("WEIGHT_RANGE_MULTILAYER",getAttribute("WEIGHT_RANGE_MULTILAYER").getString());
            rsHeader.updateString("PER_COUNT_MULTILAYER",getAttribute("PER_COUNT_MULTILAYER").getString());
            rsHeader.updateString("FINISHING_INSTRUCTION_MULTILAYER",getAttribute("FINISHING_INSTRUCTION_MULTILAYER").getString());
            
            
            
            
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
            
            rsHeaderH.updateString("DESIGN_CHANGED_BY",getAttribute("DESIGN_CHANGED_BY").getString());
            
            rsHeaderH.updateString("LAYER_TYPE",getAttribute("LAYER_TYPE").getString());
            rsHeaderH.updateString("DESIGN_REVISION_NO",getAttribute("DESIGN_REVISION_NO").getString());
            rsHeaderH.updateString("UPN_NO",getAttribute("UPN_NO").getString());
            rsHeaderH.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsHeaderH.updateString("POSITION_NO",getAttribute("POSITION_NO").getString());
            rsHeaderH.updateString("POSITION_DESIGN_NO",getAttribute("POSITION_DESIGN_NO").getString());
            rsHeaderH.updateString("REFERENCE",getAttribute("REFERENCE").getString());
            rsHeaderH.updateString("PRESS_CATEGORY",getAttribute("PRESS_CATEGORY").getString());
            rsHeaderH.updateString("STYLE",getAttribute("STYLE").getString());
            rsHeaderH.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            rsHeaderH.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHeaderH.updateString("LENGTH",getAttribute("LENGTH").getString());
            rsHeaderH.updateString("MFG_LENGTH",getAttribute("MFG_LENGTH").getString());
            rsHeaderH.updateString("WIDTH",getAttribute("WIDTH").getString());
            rsHeaderH.updateString("GSM",getAttribute("GSM").getString());
            rsHeaderH.updateString("SQMTR",getAttribute("SQMTR").getString());
            rsHeaderH.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            rsHeaderH.updateString("REASON_OF_REVISION",getAttribute("REASON_OF_REVISION").getString());
            rsHeaderH.updateString("WARP",getAttribute("WARP").getString());
            rsHeaderH.updateString("WEFT",getAttribute("WEFT").getString());
            rsHeaderH.updateString("ENDS_10CM",getAttribute("ENDS_10CM").getString());
            rsHeaderH.updateString("PICKS_10CM",getAttribute("PICKS_10CM").getString());
            rsHeaderH.updateString("WIDTH_FACT",getAttribute("WIDTH_FACT").getString());
            rsHeaderH.updateString("NO_ENDS",getAttribute("NO_ENDS").getString());
            rsHeaderH.updateString("REED",getAttribute("REED").getString());
            rsHeaderH.updateString("REED_SPACE",getAttribute("REED_SPACE").getString());
            rsHeaderH.updateString("WEAVE",getAttribute("WEAVE").getString());
            rsHeaderH.updateString("DRAW",getAttribute("DRAW").getString());
            rsHeaderH.updateString("SYN",getAttribute("SYN").getString());
            rsHeaderH.updateString("GSM_ORD",getAttribute("GSM_ORD").getString());
            rsHeaderH.updateString("GSM_MFG",getAttribute("GSM_MFG").getString());
            rsHeaderH.updateString("W_WGT",getAttribute("W_WGT").getString());
            rsHeaderH.updateString("WE_WGT",getAttribute("WE_WGT").getString());
            rsHeaderH.updateString("TK_UP",getAttribute("TK_UP").getString());
            rsHeaderH.updateString("THEO_WEIGHT",getAttribute("THEO_WEIGHT").getString());
            rsHeaderH.updateString("THEO_LENGTH",getAttribute("THEO_LENGTH").getString());
            rsHeaderH.updateString("THEO_PICKS",getAttribute("THEO_PICKS").getString());
            rsHeaderH.updateString("LENGTH_FACT",getAttribute("LENGTH_FACT").getString());
            rsHeaderH.updateString("END_LENGTH",getAttribute("END_LENGTH").getString());
            rsHeaderH.updateString("T_THICK",getAttribute("T_THICK").getString());
            rsHeaderH.updateString("THEO_CFM",getAttribute("THEO_CFM").getString());
            rsHeaderH.updateString("BASE_SKG_TOTAL",getAttribute("BASE_SKG_TOTAL").getString());
            rsHeaderH.updateString("TOTAL_SKG",getAttribute("TOTAL_SKG").getString());
            rsHeaderH.updateString("TRIM_WEIGHT",getAttribute("TRIM_WEIGHT").getString());
            rsHeaderH.updateString("WEIGHT_RANGE",getAttribute("WEIGHT_RANGE").getString());
            rsHeaderH.updateString("BILL_WEIGHT",getAttribute("BILL_WEIGHT").getString());
            rsHeaderH.updateString("KILLOS",getAttribute("KILLOS").getString());
            rsHeaderH.updateString("FACE_SINGLE",getAttribute("FACE_SINGLE").getString());
            rsHeaderH.updateString("BACK_SINGLE",getAttribute("BACK_SINGLE").getString());
            rsHeaderH.updateString("PER_COUNT",getAttribute("PER_COUNT").getString());
            rsHeaderH.updateString("WEAVING_INSTRUCTION",getAttribute("WEAVING_INSTRUCTION").getString());
            rsHeaderH.updateString("DRYER_WIDTH_MARK_WET_DRY",getAttribute("DRYER_WIDTH_MARK_WET_DRY").getString());
            rsHeaderH.updateString("TAG_INSTRUCTION",getAttribute("TAG_INSTRUCTION").getString());
            rsHeaderH.updateString("FINISHING_INSTRUCTION",getAttribute("FINISHING_INSTRUCTION").getString());
            rsHeaderH.updateString("NEEDLING_INSTRUCTION",getAttribute("NEEDLING_INSTRUCTION").getString());
            rsHeaderH.updateString("DRESS_DRAW_WEAVING_INSTRUCTION",getAttribute("DRESS_DRAW_WEAVING_INSTRUCTION").getString());
            rsHeaderH.updateString("BASE_GSM",getAttribute("BASE_GSM").getString());
            rsHeaderH.updateString("WEB_GSM",getAttribute("WEB_GSM").getString());
            rsHeaderH.updateString("TOTAL_GSM",getAttribute("TOTAL_GSM").getString());
            rsHeaderH.updateString("SAFETY",getAttribute("SAFETY").getString());
            rsHeaderH.updateString("NEEDLING_TANTION",getAttribute("NEEDLING_TANTION").getString());
            rsHeaderH.updateString("PER_WEB",getAttribute("PER_WEB").getString());
            rsHeaderH.updateString("PER_SYN_BASE",getAttribute("PER_SYN_BASE").getString());
            rsHeaderH.updateString("PER_SYN_WEB",getAttribute("PER_SYN_WEB").getString());
            rsHeaderH.updateString("WEB_ON_FACE",getAttribute("WEB_ON_FACE").getString());
            rsHeaderH.updateString("WEB_ON_BACK",getAttribute("WEB_ON_BACK").getString());
            rsHeaderH.updateString("PAINT_LINES",getAttribute("PAINT_LINES").getString());
            rsHeaderH.updateString("LOOM_NO",getAttribute("LOOM_NO").getString());
            rsHeaderH.updateString("NEEDLING_TEN_FACT",getAttribute("NEEDLING_TEN_FACT").getString());
            rsHeaderH.updateString("TW_BE_ND",getAttribute("TW_BE_ND").getString());
            rsHeaderH.updateString("T_WEB_WEIGHT",getAttribute("T_WEB_WEIGHT").getString());
            rsHeaderH.updateString("T_TOTAL_WEIGHT",getAttribute("T_TOTAL_WEIGHT").getString());
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeaderH.updateString("MKG_WIDTH",getAttribute("MKG_WIDTH").getString());
            rsHeaderH.updateString("MFG_WIDTH",getAttribute("MFG_WIDTH").getString());
            rsHeaderH.updateString("BASE_GSM_A",getAttribute("BASE_GSM_A").getString());
            rsHeaderH.updateString("BASE_GSM_B",getAttribute("BASE_GSM_B").getString());
            rsHeaderH.updateString("TW_BE_ND_A",getAttribute("TW_BE_ND_A").getString());
            rsHeaderH.updateString("TW_BE_ND_B",getAttribute("TW_BE_ND_B").getString());
            
            rsHeaderH.updateString("STYLE_MULTILAYER",getAttribute("STYLE_MULTILAYER").getString());
            rsHeaderH.updateString("PRODUCT_GROUP_MULTILAYER",getAttribute("PRODUCT_GROUP_MULTILAYER").getString());
            rsHeaderH.updateString("PRODUCT_CODE_MULTILAYER",getAttribute("PRODUCT_CODE_MULTILAYER").getString());
            rsHeaderH.updateString("LENGTH_MULTILAYER",getAttribute("LENGTH_MULTILAYER").getString());
            rsHeaderH.updateString("WIDTH_MULTILAYER",getAttribute("WIDTH_MULTILAYER").getString());
            rsHeaderH.updateString("GSM_MULTILAYER",getAttribute("GSM_MULTILAYER").getString());
            rsHeaderH.updateString("SQMTR_MULTILAYER",getAttribute("SQMTR_MULTILAYER").getString());
            rsHeaderH.updateString("WEIGHT_MULTILAYER",getAttribute("WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("WEAVE_MULTILAYER",getAttribute("WEAVE_MULTILAYER").getString());
            rsHeaderH.updateString("WEFT_MULTILAYER",getAttribute("WEFT_MULTILAYER").getString());
            rsHeaderH.updateString("PICKS_10CM_MULTILAYER",getAttribute("PICKS_10CM_MULTILAYER").getString());
            rsHeaderH.updateString("WARP_MULTILAYER",getAttribute("WARP_MULTILAYER").getString());
            rsHeaderH.updateString("ENDS_10CM_MULTILAYER",getAttribute("ENDS_10CM_MULTILAYER").getString());
            rsHeaderH.updateString("WIDTH_FACT_MULTILAYER",getAttribute("WIDTH_FACT_MULTILAYER").getString());
            rsHeaderH.updateString("NO_ENDS_MULTILAYER",getAttribute("NO_ENDS_MULTILAYER").getString());
            rsHeaderH.updateString("REED_MULTILAYER",getAttribute("REED_MULTILAYER").getString());
            rsHeaderH.updateString("REED_SPACE_MULTILAYER",getAttribute("REED_SPACE_MULTILAYER").getString());
            rsHeaderH.updateString("REASON_OF_REVISION_MULTILAYER",getAttribute("REASON_OF_REVISION_MULTILAYER").getString());
            rsHeaderH.updateString("DRAW_MULTILAYER",getAttribute("DRAW_MULTILAYER").getString());
            rsHeaderH.updateString("GSM_ORD_MULTILAYER",getAttribute("GSM_ORD_MULTILAYER").getString());
            rsHeaderH.updateString("GSM_MFG_MULTILAYER",getAttribute("GSM_MFG_MULTILAYER").getString());
            rsHeaderH.updateString("W_WGT_MULTILAYER",getAttribute("W_WGT_MULTILAYER").getString());
            rsHeaderH.updateString("WE_WGT_MULTILAYER",getAttribute("WE_WGT_MULTILAYER").getString());
            rsHeaderH.updateString("TK_UP_MULTILAYER",getAttribute("TK_UP_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_WEIGHT_MULTILAYER",getAttribute("THEO_WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_LENGTH_MULTILAYER",getAttribute("THEO_LENGTH_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_PICKS_MULTILAYER",getAttribute("THEO_PICKS_MULTILAYER").getString());
            rsHeaderH.updateString("LENGTH_FACT_MULTILAYER",getAttribute("LENGTH_FACT_MULTILAYER").getString());
            rsHeaderH.updateString("END_LENGTH_MULTILAYER",getAttribute("END_LENGTH_MULTILAYER").getString());
            rsHeaderH.updateString("T_THICK_MULTILAYER",getAttribute("T_THICK_MULTILAYER").getString());
            rsHeaderH.updateString("FACE_SINGLE_MULTILAYER",getAttribute("FACE_SINGLE_MULTILAYER").getString());
            rsHeaderH.updateString("KILLOS_MULTILAYER",getAttribute("KILLOS_MULTILAYER").getString());
            rsHeaderH.updateString("BILL_WEIGHT_MULTILAYER",getAttribute("BILL_WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("TRIM_WEIGHT_MULTILAYER",getAttribute("TRIM_WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("TOTAL_SKG_MULTILAYER",getAttribute("TOTAL_SKG_MULTILAYER").getString());
            rsHeaderH.updateString("BASE_SKG_TOTAL_MULTILAYER",getAttribute("BASE_SKG_TOTAL_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_CFM_MULTILAYER",getAttribute("THEO_CFM_MULTILAYER").getString());
            rsHeaderH.updateString("WEIGHT_RANGE_MULTILAYER",getAttribute("WEIGHT_RANGE_MULTILAYER").getString());
            rsHeaderH.updateString("PER_COUNT_MULTILAYER",getAttribute("PER_COUNT_MULTILAYER").getString());
            rsHeaderH.updateString("FINISHING_INSTRUCTION_MULTILAYER",getAttribute("FINISHING_INSTRUCTION_MULTILAYER").getString());
            
            
            
            rsHeaderH.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHeaderH.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHeaderH.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeaderH.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            rsHeaderH.updateString("DESIGN_DOC_NO",getAttribute("DESIGN_DOC_NO").getString());
            rsHeaderH.updateString("DESIGN_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DESIGN_DOC_DATE").getString()));
            
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
                clsFeltDesignMasterDetails ObjFeltSalesOrderDetails = (clsFeltDesignMasterDetails) hmFeltPerformanceTrackingDetails.get(Integer.toString(i));
             
                //Insert records into Felt order Amendment table
                resultSetTemp.moveToInsertRow();
                
                resultSetTemp.updateString("SR_NO", i+"");
                resultSetTemp.updateString("DESIGN_DOC_NO", getAttribute("DESIGN_DOC_NO").getString());
                resultSetTemp.updateString("FACE_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_WEB").getString());
                resultSetTemp.updateString("FACE_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_NO_WEB").getString());
                resultSetTemp.updateString("BACK_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_WEB").getString());
                resultSetTemp.updateString("BACK_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_NO_WEB").getString());
                resultSetTemp.updateString("TAKE_UP", ObjFeltSalesOrderDetails.getAttribute("TAKE_UP").getString());
                resultSetTemp.updateString("PENETRATION", ObjFeltSalesOrderDetails.getAttribute("PENETRATION").getString());
                
                /*
                SR_NO, DESIGN_DOC_NO, , , , , , 
                */
                
                resultSetTemp.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                
                resultSetHistory.updateString("SR_NO", i+"");
                resultSetHistory.updateString("DESIGN_DOC_NO", getAttribute("DESIGN_DOC_NO").getString());
                resultSetHistory.updateString("FACE_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_WEB").getString());
                resultSetHistory.updateString("FACE_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_NO_WEB").getString());
                resultSetHistory.updateString("BACK_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_WEB").getString());
                resultSetHistory.updateString("BACK_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_NO_WEB").getString());
                resultSetHistory.updateString("TAKE_UP", ObjFeltSalesOrderDetails.getAttribute("TAKE_UP").getString());
                resultSetHistory.updateString("PENETRATION", ObjFeltSalesOrderDetails.getAttribute("PENETRATION").getString());
                
                resultSetHistory.insertRow();

                
                
            }
            
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DESIGN_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_DESIGN_MASTER_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("REJECTED_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DESIGN_DOC_NO";
            
            
            
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
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL_H WHERE DESIGN_DOC_NO='"+getAttribute("DESIGN_DOC_NO").getString()+"'");
            resultSetHistory.first();
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            //statementHistory.execute("DELETE FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE DESIGN_DOC_NO='"+getAttribute("DESIGN_DOC_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL_H WHERE DESIGN_DOC_NO=''");
            
            stHeaderH=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeaderH=stHeaderH.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_HEADER_H WHERE DESIGN_DOC_NO=''");
            
            
            
            
            //Now Update records into FELT_CN_TEMP_HEADER tables
            
            resultSet.updateString("DESIGN_DOC_NO",getAttribute("DESIGN_DOC_NO").getString());
            resultSet.updateString("DESIGN_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DESIGN_DOC_DATE").getString()));
            
            resultSet.updateString("DESIGN_REVISION_NO",getAttribute("DESIGN_REVISION_NO").getString());
            resultSet.updateString("UPN_NO",getAttribute("UPN_NO").getString());
            resultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            resultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            resultSet.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            resultSet.updateString("POSITION_NO",getAttribute("POSITION_NO").getString());
            resultSet.updateString("POSITION_DESIGN_NO",getAttribute("POSITION_DESIGN_NO").getString());
            resultSet.updateString("REFERENCE",getAttribute("REFERENCE").getString());
            resultSet.updateString("PRESS_CATEGORY",getAttribute("PRESS_CATEGORY").getString());
            resultSet.updateString("STYLE",getAttribute("STYLE").getString());
            resultSet.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            resultSet.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            resultSet.updateString("LENGTH",getAttribute("LENGTH").getString());
            resultSet.updateString("MFG_LENGTH",getAttribute("MFG_LENGTH").getString());
            resultSet.updateString("MKG_WIDTH",getAttribute("MKG_WIDTH").getString());
            resultSet.updateString("MFG_WIDTH",getAttribute("MFG_WIDTH").getString());
            resultSet.updateString("WIDTH",getAttribute("WIDTH").getString());
            resultSet.updateString("GSM",getAttribute("GSM").getString());
            resultSet.updateString("SQMTR",getAttribute("SQMTR").getString());
            resultSet.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            resultSet.updateString("REASON_OF_REVISION",getAttribute("REASON_OF_REVISION").getString());
            resultSet.updateString("WARP",getAttribute("WARP").getString());
            resultSet.updateString("WEFT",getAttribute("WEFT").getString());
            resultSet.updateString("ENDS_10CM",getAttribute("ENDS_10CM").getString());
            resultSet.updateString("PICKS_10CM",getAttribute("PICKS_10CM").getString());
            resultSet.updateString("WIDTH_FACT",getAttribute("WIDTH_FACT").getString());
            resultSet.updateString("NO_ENDS",getAttribute("NO_ENDS").getString());
            resultSet.updateString("REED",getAttribute("REED").getString());
            resultSet.updateString("REED_SPACE",getAttribute("REED_SPACE").getString());
            resultSet.updateString("WEAVE",getAttribute("WEAVE").getString());
            resultSet.updateString("DRAW",getAttribute("DRAW").getString());
            resultSet.updateString("SYN",getAttribute("SYN").getString());
            resultSet.updateString("GSM_ORD",getAttribute("GSM_ORD").getString());
            resultSet.updateString("GSM_MFG",getAttribute("GSM_MFG").getString());
            resultSet.updateString("W_WGT",getAttribute("W_WGT").getString());
            resultSet.updateString("WE_WGT",getAttribute("WE_WGT").getString());
            resultSet.updateString("TK_UP",getAttribute("TK_UP").getString());
            resultSet.updateString("THEO_WEIGHT",getAttribute("THEO_WEIGHT").getString());
            resultSet.updateString("THEO_LENGTH",getAttribute("THEO_LENGTH").getString());
            resultSet.updateString("THEO_PICKS",getAttribute("THEO_PICKS").getString());
            resultSet.updateString("LENGTH_FACT",getAttribute("LENGTH_FACT").getString());
            resultSet.updateString("END_LENGTH",getAttribute("END_LENGTH").getString());
            resultSet.updateString("T_THICK",getAttribute("T_THICK").getString());
            resultSet.updateString("THEO_CFM",getAttribute("THEO_CFM").getString());
            resultSet.updateString("BASE_SKG_TOTAL",getAttribute("BASE_SKG_TOTAL").getString());
            resultSet.updateString("TOTAL_SKG",getAttribute("TOTAL_SKG").getString());
            resultSet.updateString("TRIM_WEIGHT",getAttribute("TRIM_WEIGHT").getString());
            resultSet.updateString("WEIGHT_RANGE",getAttribute("WEIGHT_RANGE").getString());
            resultSet.updateString("BILL_WEIGHT",getAttribute("BILL_WEIGHT").getString());
            resultSet.updateString("KILLOS",getAttribute("KILLOS").getString());
            resultSet.updateString("FACE_SINGLE",getAttribute("FACE_SINGLE").getString());
            resultSet.updateString("BACK_SINGLE",getAttribute("BACK_SINGLE").getString());
            resultSet.updateString("PER_COUNT",getAttribute("PER_COUNT").getString());
            resultSet.updateString("WEAVING_INSTRUCTION",getAttribute("WEAVING_INSTRUCTION").getString());
            resultSet.updateString("DRYER_WIDTH_MARK_WET_DRY",getAttribute("DRYER_WIDTH_MARK_WET_DRY").getString());
            resultSet.updateString("TAG_INSTRUCTION",getAttribute("TAG_INSTRUCTION").getString());
            resultSet.updateString("FINISHING_INSTRUCTION",getAttribute("FINISHING_INSTRUCTION").getString());
            resultSet.updateString("NEEDLING_INSTRUCTION",getAttribute("NEEDLING_INSTRUCTION").getString());
            resultSet.updateString("DRESS_DRAW_WEAVING_INSTRUCTION",getAttribute("DRESS_DRAW_WEAVING_INSTRUCTION").getString());
            resultSet.updateString("BASE_GSM",getAttribute("BASE_GSM").getString());
            resultSet.updateString("WEB_GSM",getAttribute("WEB_GSM").getString());
            resultSet.updateString("TOTAL_GSM",getAttribute("TOTAL_GSM").getString());
            resultSet.updateString("SAFETY",getAttribute("SAFETY").getString());
            resultSet.updateString("NEEDLING_TANTION",getAttribute("NEEDLING_TANTION").getString());
            resultSet.updateString("PER_WEB",getAttribute("PER_WEB").getString());
            resultSet.updateString("PER_SYN_BASE",getAttribute("PER_SYN_BASE").getString());
            resultSet.updateString("PER_SYN_WEB",getAttribute("PER_SYN_WEB").getString());
            resultSet.updateString("WEB_ON_FACE",getAttribute("WEB_ON_FACE").getString());
            resultSet.updateString("WEB_ON_BACK",getAttribute("WEB_ON_BACK").getString());
            resultSet.updateString("PAINT_LINES",getAttribute("PAINT_LINES").getString());
            resultSet.updateString("LOOM_NO",getAttribute("LOOM_NO").getString());
            resultSet.updateString("NEEDLING_TEN_FACT",getAttribute("NEEDLING_TEN_FACT").getString());
            resultSet.updateString("TW_BE_ND",getAttribute("TW_BE_ND").getString());
            resultSet.updateString("T_WEB_WEIGHT",getAttribute("T_WEB_WEIGHT").getString());
            resultSet.updateString("T_TOTAL_WEIGHT",getAttribute("T_TOTAL_WEIGHT").getString());
            resultSet.updateString("REMARK",getAttribute("REMARK").getString());
            
            resultSet.updateString("BASE_GSM_A",getAttribute("BASE_GSM_A").getString());
            resultSet.updateString("BASE_GSM_B",getAttribute("BASE_GSM_B").getString());
            resultSet.updateString("TW_BE_ND_A",getAttribute("TW_BE_ND_A").getString());
            resultSet.updateString("TW_BE_ND_B",getAttribute("TW_BE_ND_B").getString());
            resultSet.updateString("LAYER_TYPE",getAttribute("LAYER_TYPE").getString());
            
            resultSet.updateString("DESIGN_CHANGED_BY",getAttribute("DESIGN_CHANGED_BY").getString());
            
            resultSet.updateString("STYLE_MULTILAYER",getAttribute("STYLE_MULTILAYER").getString());
            resultSet.updateString("PRODUCT_GROUP_MULTILAYER",getAttribute("PRODUCT_GROUP_MULTILAYER").getString());
            resultSet.updateString("PRODUCT_CODE_MULTILAYER",getAttribute("PRODUCT_CODE_MULTILAYER").getString());
            resultSet.updateString("LENGTH_MULTILAYER",getAttribute("LENGTH_MULTILAYER").getString());
            resultSet.updateString("WIDTH_MULTILAYER",getAttribute("WIDTH_MULTILAYER").getString());
            resultSet.updateString("GSM_MULTILAYER",getAttribute("GSM_MULTILAYER").getString());
            resultSet.updateString("SQMTR_MULTILAYER",getAttribute("SQMTR_MULTILAYER").getString());
            resultSet.updateString("WEIGHT_MULTILAYER",getAttribute("WEIGHT_MULTILAYER").getString());
            resultSet.updateString("WEAVE_MULTILAYER",getAttribute("WEAVE_MULTILAYER").getString());
            resultSet.updateString("WEFT_MULTILAYER",getAttribute("WEFT_MULTILAYER").getString());
            resultSet.updateString("PICKS_10CM_MULTILAYER",getAttribute("PICKS_10CM_MULTILAYER").getString());
            resultSet.updateString("WARP_MULTILAYER",getAttribute("WARP_MULTILAYER").getString());
            resultSet.updateString("ENDS_10CM_MULTILAYER",getAttribute("ENDS_10CM_MULTILAYER").getString());
            resultSet.updateString("WIDTH_FACT_MULTILAYER",getAttribute("WIDTH_FACT_MULTILAYER").getString());
            resultSet.updateString("NO_ENDS_MULTILAYER",getAttribute("NO_ENDS_MULTILAYER").getString());
            resultSet.updateString("REED_MULTILAYER",getAttribute("REED_MULTILAYER").getString());
            resultSet.updateString("REED_SPACE_MULTILAYER",getAttribute("REED_SPACE_MULTILAYER").getString());
            resultSet.updateString("REASON_OF_REVISION_MULTILAYER",getAttribute("REASON_OF_REVISION_MULTILAYER").getString());
            resultSet.updateString("DRAW_MULTILAYER",getAttribute("DRAW_MULTILAYER").getString());
            resultSet.updateString("GSM_ORD_MULTILAYER",getAttribute("GSM_ORD_MULTILAYER").getString());
            resultSet.updateString("GSM_MFG_MULTILAYER",getAttribute("GSM_MFG_MULTILAYER").getString());
            resultSet.updateString("W_WGT_MULTILAYER",getAttribute("W_WGT_MULTILAYER").getString());
            resultSet.updateString("WE_WGT_MULTILAYER",getAttribute("WE_WGT_MULTILAYER").getString());
            resultSet.updateString("TK_UP_MULTILAYER",getAttribute("TK_UP_MULTILAYER").getString());
            resultSet.updateString("THEO_WEIGHT_MULTILAYER",getAttribute("THEO_WEIGHT_MULTILAYER").getString());
            resultSet.updateString("THEO_LENGTH_MULTILAYER",getAttribute("THEO_LENGTH_MULTILAYER").getString());
            resultSet.updateString("THEO_PICKS_MULTILAYER",getAttribute("THEO_PICKS_MULTILAYER").getString());
            resultSet.updateString("LENGTH_FACT_MULTILAYER",getAttribute("LENGTH_FACT_MULTILAYER").getString());
            resultSet.updateString("END_LENGTH_MULTILAYER",getAttribute("END_LENGTH_MULTILAYER").getString());
            resultSet.updateString("T_THICK_MULTILAYER",getAttribute("T_THICK_MULTILAYER").getString());
            resultSet.updateString("FACE_SINGLE_MULTILAYER",getAttribute("FACE_SINGLE_MULTILAYER").getString());
            resultSet.updateString("KILLOS_MULTILAYER",getAttribute("KILLOS_MULTILAYER").getString());
            resultSet.updateString("BILL_WEIGHT_MULTILAYER",getAttribute("BILL_WEIGHT_MULTILAYER").getString());
            resultSet.updateString("TRIM_WEIGHT_MULTILAYER",getAttribute("TRIM_WEIGHT_MULTILAYER").getString());
            resultSet.updateString("TOTAL_SKG_MULTILAYER",getAttribute("TOTAL_SKG_MULTILAYER").getString());
            resultSet.updateString("BASE_SKG_TOTAL_MULTILAYER",getAttribute("BASE_SKG_TOTAL_MULTILAYER").getString());
            resultSet.updateString("THEO_CFM_MULTILAYER",getAttribute("THEO_CFM_MULTILAYER").getString());
            resultSet.updateString("WEIGHT_RANGE_MULTILAYER",getAttribute("WEIGHT_RANGE_MULTILAYER").getString());
            resultSet.updateString("PER_COUNT_MULTILAYER",getAttribute("PER_COUNT_MULTILAYER").getString());
            resultSet.updateString("FINISHING_INSTRUCTION_MULTILAYER",getAttribute("FINISHING_INSTRUCTION_MULTILAYER").getString());
            
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER_H WHERE DESIGN_DOC_NO='"+getAttribute("DESIGN_DOC_NO").getString()+"'");
            
            RevNo++;
           
            rsHeaderH.moveToInsertRow();
            
            rsHeaderH.updateString("REVISION_NO",RevNo+"");
            
            rsHeaderH.updateString("DESIGN_DOC_NO",getAttribute("DESIGN_DOC_NO").getString());
            rsHeaderH.updateString("DESIGN_DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DESIGN_DOC_DATE").getString()));
            
            rsHeaderH.updateString("DESIGN_REVISION_NO",getAttribute("DESIGN_REVISION_NO").getString());
            rsHeaderH.updateString("UPN_NO",getAttribute("UPN_NO").getString());
            rsHeaderH.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHeaderH.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHeaderH.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsHeaderH.updateString("POSITION_NO",getAttribute("POSITION_NO").getString());
            rsHeaderH.updateString("POSITION_DESIGN_NO",getAttribute("POSITION_DESIGN_NO").getString());
            rsHeaderH.updateString("REFERENCE",getAttribute("REFERENCE").getString());
            rsHeaderH.updateString("PRESS_CATEGORY",getAttribute("PRESS_CATEGORY").getString());
            rsHeaderH.updateString("STYLE",getAttribute("STYLE").getString());
            rsHeaderH.updateString("PRODUCT_GROUP",getAttribute("PRODUCT_GROUP").getString());
            rsHeaderH.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHeaderH.updateString("LENGTH",getAttribute("LENGTH").getString());
            rsHeaderH.updateString("MFG_LENGTH",getAttribute("MFG_LENGTH").getString());
            rsHeaderH.updateString("MKG_WIDTH",getAttribute("MKG_WIDTH").getString());
            rsHeaderH.updateString("MFG_WIDTH",getAttribute("MFG_WIDTH").getString());
            rsHeaderH.updateString("WIDTH",getAttribute("WIDTH").getString());
            rsHeaderH.updateString("GSM",getAttribute("GSM").getString());
            rsHeaderH.updateString("SQMTR",getAttribute("SQMTR").getString());
            rsHeaderH.updateString("WEIGHT",getAttribute("WEIGHT").getString());
            rsHeaderH.updateString("REASON_OF_REVISION",getAttribute("REASON_OF_REVISION").getString());
            rsHeaderH.updateString("WARP",getAttribute("WARP").getString());
            rsHeaderH.updateString("WEFT",getAttribute("WEFT").getString());
            rsHeaderH.updateString("ENDS_10CM",getAttribute("ENDS_10CM").getString());
            rsHeaderH.updateString("PICKS_10CM",getAttribute("PICKS_10CM").getString());
            rsHeaderH.updateString("WIDTH_FACT",getAttribute("WIDTH_FACT").getString());
            rsHeaderH.updateString("NO_ENDS",getAttribute("NO_ENDS").getString());
            rsHeaderH.updateString("REED",getAttribute("REED").getString());
            rsHeaderH.updateString("REED_SPACE",getAttribute("REED_SPACE").getString());
            rsHeaderH.updateString("WEAVE",getAttribute("WEAVE").getString());
            rsHeaderH.updateString("DRAW",getAttribute("DRAW").getString());
            rsHeaderH.updateString("SYN",getAttribute("SYN").getString());
            rsHeaderH.updateString("GSM_ORD",getAttribute("GSM_ORD").getString());
            rsHeaderH.updateString("GSM_MFG",getAttribute("GSM_MFG").getString());
            rsHeaderH.updateString("W_WGT",getAttribute("W_WGT").getString());
            rsHeaderH.updateString("WE_WGT",getAttribute("WE_WGT").getString());
            rsHeaderH.updateString("TK_UP",getAttribute("TK_UP").getString());
            rsHeaderH.updateString("THEO_WEIGHT",getAttribute("THEO_WEIGHT").getString());
            rsHeaderH.updateString("THEO_LENGTH",getAttribute("THEO_LENGTH").getString());
            rsHeaderH.updateString("THEO_PICKS",getAttribute("THEO_PICKS").getString());
            rsHeaderH.updateString("LENGTH_FACT",getAttribute("LENGTH_FACT").getString());
            rsHeaderH.updateString("END_LENGTH",getAttribute("END_LENGTH").getString());
            rsHeaderH.updateString("T_THICK",getAttribute("T_THICK").getString());
            rsHeaderH.updateString("THEO_CFM",getAttribute("THEO_CFM").getString());
            rsHeaderH.updateString("BASE_SKG_TOTAL",getAttribute("BASE_SKG_TOTAL").getString());
            rsHeaderH.updateString("TOTAL_SKG",getAttribute("TOTAL_SKG").getString());
            rsHeaderH.updateString("TRIM_WEIGHT",getAttribute("TRIM_WEIGHT").getString());
            rsHeaderH.updateString("WEIGHT_RANGE",getAttribute("WEIGHT_RANGE").getString());
            rsHeaderH.updateString("BILL_WEIGHT",getAttribute("BILL_WEIGHT").getString());
            rsHeaderH.updateString("KILLOS",getAttribute("KILLOS").getString());
            rsHeaderH.updateString("FACE_SINGLE",getAttribute("FACE_SINGLE").getString());
            rsHeaderH.updateString("BACK_SINGLE",getAttribute("BACK_SINGLE").getString());
            rsHeaderH.updateString("PER_COUNT",getAttribute("PER_COUNT").getString());
            rsHeaderH.updateString("WEAVING_INSTRUCTION",getAttribute("WEAVING_INSTRUCTION").getString());
            rsHeaderH.updateString("DRYER_WIDTH_MARK_WET_DRY",getAttribute("DRYER_WIDTH_MARK_WET_DRY").getString());
            rsHeaderH.updateString("TAG_INSTRUCTION",getAttribute("TAG_INSTRUCTION").getString());
            rsHeaderH.updateString("FINISHING_INSTRUCTION",getAttribute("FINISHING_INSTRUCTION").getString());
            rsHeaderH.updateString("NEEDLING_INSTRUCTION",getAttribute("NEEDLING_INSTRUCTION").getString());
            rsHeaderH.updateString("DRESS_DRAW_WEAVING_INSTRUCTION",getAttribute("DRESS_DRAW_WEAVING_INSTRUCTION").getString());
            rsHeaderH.updateString("BASE_GSM",getAttribute("BASE_GSM").getString());
            rsHeaderH.updateString("WEB_GSM",getAttribute("WEB_GSM").getString());
            rsHeaderH.updateString("TOTAL_GSM",getAttribute("TOTAL_GSM").getString());
            rsHeaderH.updateString("SAFETY",getAttribute("SAFETY").getString());
            rsHeaderH.updateString("NEEDLING_TANTION",getAttribute("NEEDLING_TANTION").getString());
            rsHeaderH.updateString("PER_WEB",getAttribute("PER_WEB").getString());
            rsHeaderH.updateString("PER_SYN_BASE",getAttribute("PER_SYN_BASE").getString());
            rsHeaderH.updateString("PER_SYN_WEB",getAttribute("PER_SYN_WEB").getString());
            rsHeaderH.updateString("WEB_ON_FACE",getAttribute("WEB_ON_FACE").getString());
            rsHeaderH.updateString("WEB_ON_BACK",getAttribute("WEB_ON_BACK").getString());
            rsHeaderH.updateString("PAINT_LINES",getAttribute("PAINT_LINES").getString());
            rsHeaderH.updateString("LOOM_NO",getAttribute("LOOM_NO").getString());
            rsHeaderH.updateString("NEEDLING_TEN_FACT",getAttribute("NEEDLING_TEN_FACT").getString());
            rsHeaderH.updateString("TW_BE_ND",getAttribute("TW_BE_ND").getString());
            rsHeaderH.updateString("T_WEB_WEIGHT",getAttribute("T_WEB_WEIGHT").getString());
            rsHeaderH.updateString("T_TOTAL_WEIGHT",getAttribute("T_TOTAL_WEIGHT").getString());
            rsHeaderH.updateString("REMARK",getAttribute("REMARK").getString());
            rsHeaderH.updateString("BASE_GSM_A",getAttribute("BASE_GSM_A").getString());
            rsHeaderH.updateString("BASE_GSM_B",getAttribute("BASE_GSM_B").getString());
            rsHeaderH.updateString("TW_BE_ND_A",getAttribute("TW_BE_ND_A").getString());
            rsHeaderH.updateString("TW_BE_ND_B",getAttribute("TW_BE_ND_B").getString());
            rsHeaderH.updateString("LAYER_TYPE",getAttribute("LAYER_TYPE").getString());
            
            rsHeaderH.updateString("DESIGN_CHANGED_BY",getAttribute("DESIGN_CHANGED_BY").getString());
            
            rsHeaderH.updateString("STYLE_MULTILAYER",getAttribute("STYLE_MULTILAYER").getString());
            rsHeaderH.updateString("PRODUCT_GROUP_MULTILAYER",getAttribute("PRODUCT_GROUP_MULTILAYER").getString());
            rsHeaderH.updateString("PRODUCT_CODE_MULTILAYER",getAttribute("PRODUCT_CODE_MULTILAYER").getString());
            rsHeaderH.updateString("LENGTH_MULTILAYER",getAttribute("LENGTH_MULTILAYER").getString());
            rsHeaderH.updateString("WIDTH_MULTILAYER",getAttribute("WIDTH_MULTILAYER").getString());
            rsHeaderH.updateString("GSM_MULTILAYER",getAttribute("GSM_MULTILAYER").getString());
            rsHeaderH.updateString("SQMTR_MULTILAYER",getAttribute("SQMTR_MULTILAYER").getString());
            rsHeaderH.updateString("WEIGHT_MULTILAYER",getAttribute("WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("WEAVE_MULTILAYER",getAttribute("WEAVE_MULTILAYER").getString());
            rsHeaderH.updateString("WEFT_MULTILAYER",getAttribute("WEFT_MULTILAYER").getString());
            rsHeaderH.updateString("PICKS_10CM_MULTILAYER",getAttribute("PICKS_10CM_MULTILAYER").getString());
            rsHeaderH.updateString("WARP_MULTILAYER",getAttribute("WARP_MULTILAYER").getString());
            rsHeaderH.updateString("ENDS_10CM_MULTILAYER",getAttribute("ENDS_10CM_MULTILAYER").getString());
            rsHeaderH.updateString("WIDTH_FACT_MULTILAYER",getAttribute("WIDTH_FACT_MULTILAYER").getString());
            rsHeaderH.updateString("NO_ENDS_MULTILAYER",getAttribute("NO_ENDS_MULTILAYER").getString());
            rsHeaderH.updateString("REED_MULTILAYER",getAttribute("REED_MULTILAYER").getString());
            rsHeaderH.updateString("REED_SPACE_MULTILAYER",getAttribute("REED_SPACE_MULTILAYER").getString());
            rsHeaderH.updateString("REASON_OF_REVISION_MULTILAYER",getAttribute("REASON_OF_REVISION_MULTILAYER").getString());
            rsHeaderH.updateString("DRAW_MULTILAYER",getAttribute("DRAW_MULTILAYER").getString());
            rsHeaderH.updateString("GSM_ORD_MULTILAYER",getAttribute("GSM_ORD_MULTILAYER").getString());
            rsHeaderH.updateString("GSM_MFG_MULTILAYER",getAttribute("GSM_MFG_MULTILAYER").getString());
            rsHeaderH.updateString("W_WGT_MULTILAYER",getAttribute("W_WGT_MULTILAYER").getString());
            rsHeaderH.updateString("WE_WGT_MULTILAYER",getAttribute("WE_WGT_MULTILAYER").getString());
            rsHeaderH.updateString("TK_UP_MULTILAYER",getAttribute("TK_UP_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_WEIGHT_MULTILAYER",getAttribute("THEO_WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_LENGTH_MULTILAYER",getAttribute("THEO_LENGTH_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_PICKS_MULTILAYER",getAttribute("THEO_PICKS_MULTILAYER").getString());
            rsHeaderH.updateString("LENGTH_FACT_MULTILAYER",getAttribute("LENGTH_FACT_MULTILAYER").getString());
            rsHeaderH.updateString("END_LENGTH_MULTILAYER",getAttribute("END_LENGTH_MULTILAYER").getString());
            rsHeaderH.updateString("T_THICK_MULTILAYER",getAttribute("T_THICK_MULTILAYER").getString());
            rsHeaderH.updateString("FACE_SINGLE_MULTILAYER",getAttribute("FACE_SINGLE_MULTILAYER").getString());
            rsHeaderH.updateString("KILLOS_MULTILAYER",getAttribute("KILLOS_MULTILAYER").getString());
            rsHeaderH.updateString("BILL_WEIGHT_MULTILAYER",getAttribute("BILL_WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("TRIM_WEIGHT_MULTILAYER",getAttribute("TRIM_WEIGHT_MULTILAYER").getString());
            rsHeaderH.updateString("TOTAL_SKG_MULTILAYER",getAttribute("TOTAL_SKG_MULTILAYER").getString());
            rsHeaderH.updateString("BASE_SKG_TOTAL_MULTILAYER",getAttribute("BASE_SKG_TOTAL_MULTILAYER").getString());
            rsHeaderH.updateString("THEO_CFM_MULTILAYER",getAttribute("THEO_CFM_MULTILAYER").getString());
            rsHeaderH.updateString("WEIGHT_RANGE_MULTILAYER",getAttribute("WEIGHT_RANGE_MULTILAYER").getString());
            rsHeaderH.updateString("PER_COUNT_MULTILAYER",getAttribute("PER_COUNT_MULTILAYER").getString());
            rsHeaderH.updateString("FINISHING_INSTRUCTION_MULTILAYER",getAttribute("FINISHING_INSTRUCTION_MULTILAYER").getString());
            
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
            String OrderNo=getAttribute("DESIGN_DOC_NO").getString();
            data.Execute("DELETE FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE DESIGN_DOC_NO='"+OrderNo+"'");
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE DESIGN_DOC_NO='1'");
             
            
            int RevNoH=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_DESIGN_MASTER_DETAIL_H WHERE DESIGN_DOC_NO='"+getAttribute("DESIGN_DOC_NO").getString()+"'");
            RevNoH++;

            resultSetHistory.moveToInsertRow();
            
            for(int i=1;i<=hmFeltPerformanceTrackingDetails.size();i++) {
                clsFeltDesignMasterDetails ObjFeltSalesOrderDetails=(clsFeltDesignMasterDetails) hmFeltPerformanceTrackingDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("SR_NO", i+"");
                resultSetTemp.updateString("DESIGN_DOC_NO", getAttribute("DESIGN_DOC_NO").getString());
                resultSetTemp.updateString("FACE_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_WEB").getString());
                resultSetTemp.updateString("FACE_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_NO_WEB").getString());
                resultSetTemp.updateString("BACK_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_WEB").getString());
                resultSetTemp.updateString("BACK_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_NO_WEB").getString());
                resultSetTemp.updateString("TAKE_UP", ObjFeltSalesOrderDetails.getAttribute("TAKE_UP").getString());
                resultSetTemp.updateString("PENETRATION", ObjFeltSalesOrderDetails.getAttribute("PENETRATION").getString());
                
                resultSetTemp.insertRow();
                
                
                resultSetHistory.updateInt("REVISION_NO",RevNoH);
                resultSetHistory.updateString("SR_NO", i+"");
                
                resultSetHistory.updateString("DESIGN_DOC_NO", getAttribute("DESIGN_DOC_NO").getString());
                resultSetHistory.updateString("FACE_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_WEB").getString());
                resultSetHistory.updateString("FACE_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("FACE_NO_WEB").getString());
                resultSetHistory.updateString("BACK_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_WEB").getString());
                resultSetHistory.updateString("BACK_NO_WEB", ObjFeltSalesOrderDetails.getAttribute("BACK_NO_WEB").getString());
                resultSetHistory.updateString("TAKE_UP", ObjFeltSalesOrderDetails.getAttribute("TAKE_UP").getString());
                resultSetHistory.updateString("PENETRATION", ObjFeltSalesOrderDetails.getAttribute("PENETRATION").getString());
                
                resultSetHistory.insertRow();
                
                
                
                // Final Approval and save to PIECE REGISTER 
                if(getAttribute("APPROVAL_STATUS").getString().equals("F") && !ObjFeltSalesOrderDetails.getAttribute("PIECE_NO").getString().equals(""))
                {
                      
                    
                    
                    
                }
            }
            
            
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=ModuleID; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("DESIGN_DOC_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_DESIGN_MASTER_HEADER";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="DESIGN_DOC_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            //if(getAttribute("APPROVAL_STATUS").getString().equals("A") || getAttribute("APPROVAL_STATUS").getString().equals("R")) {
//                    String Subject = "Felt Order Pending Document : "+getAttribute("DESIGN_DOC_NO").getString();
//                    String Message = "Document No : "+getAttribute("DESIGN_DOC_NO").getString()+" is added in your PENDING DOCUMENT"
//                            + "\n\n\n\n SDML-ERP : http://200.0.0.227:8080/SDMLERP";
//                 
//                    String send_to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, (int)getAttribute("TO").getVal());
//
//                    System.out.println("USERID : "+(int)getAttribute("TO").getVal()+", send_to : "+send_to);
//                    try{
//                        System.out.println(" Host IP : "+EITLERPGLOBAL.SMTPHostIP);
//                        JMail.SendMail(EITLERPGLOBAL.SMTPHostIP, "sdmlerp@dineshmills.com", send_to, Message, Subject, "sdmlerp@dineshmills.com");
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
            //}
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_DESIGN_MASTER_HEADER SET REJECTED=0,CHANGED=1 WHERE DESIGN_DOC_NO ='"+getAttribute("DESIGN_DOC_NO").getString()+"'");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE  DESIGN_DOC_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND USER_ID="+userID+" AND DESIGN_DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE "
                    + " DESIGN_DOC_DATE = '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND DESIGN_DOC_NO ='" + documentNo + "'";
                 
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE DESIGN_DOC_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
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
            String strSql = "SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER WHERE  " + stringFindQuery + "";
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
            
            setAttribute("DESIGN_DOC_NO",resultSet.getString("DESIGN_DOC_NO"));
            setAttribute("DESIGN_DOC_DATE",resultSet.getDate("DESIGN_DOC_DATE"));
            
            setAttribute("DESIGN_REVISION_NO",resultSet.getString("DESIGN_REVISION_NO"));
            setAttribute("UPN_NO",resultSet.getString("UPN_NO"));
            setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
            setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
            setAttribute("MACHINE_NO",resultSet.getString("MACHINE_NO"));
            setAttribute("POSITION_NO",resultSet.getString("POSITION_NO"));
            setAttribute("POSITION_DESIGN_NO",resultSet.getString("POSITION_DESIGN_NO"));
            setAttribute("REFERENCE",resultSet.getString("REFERENCE"));
            setAttribute("PRESS_CATEGORY",resultSet.getString("PRESS_CATEGORY"));
            setAttribute("STYLE",resultSet.getString("STYLE"));
            setAttribute("PRODUCT_GROUP",resultSet.getString("PRODUCT_GROUP"));
            setAttribute("PRODUCT_CODE",resultSet.getString("PRODUCT_CODE"));
            setAttribute("LENGTH",resultSet.getString("LENGTH"));
            setAttribute("MFG_LENGTH",resultSet.getString("MFG_LENGTH"));
            setAttribute("WIDTH",resultSet.getString("WIDTH"));
            setAttribute("GSM",resultSet.getString("GSM"));
            setAttribute("SQMTR",resultSet.getString("SQMTR"));
            setAttribute("WEIGHT",resultSet.getString("WEIGHT"));
            setAttribute("REASON_OF_REVISION",resultSet.getString("REASON_OF_REVISION"));
            setAttribute("WARP",resultSet.getString("WARP"));
            setAttribute("WEFT",resultSet.getString("WEFT"));
            setAttribute("ENDS_10CM",resultSet.getString("ENDS_10CM"));
            setAttribute("PICKS_10CM",resultSet.getString("PICKS_10CM"));
            setAttribute("WIDTH_FACT",resultSet.getString("WIDTH_FACT"));
            setAttribute("MKG_WIDTH",resultSet.getString("MKG_WIDTH"));
            setAttribute("MFG_WIDTH",resultSet.getString("MFG_WIDTH"));
            
            setAttribute("NO_ENDS",resultSet.getString("NO_ENDS"));
            setAttribute("REED",resultSet.getString("REED"));
            setAttribute("REED_SPACE",resultSet.getString("REED_SPACE"));
            setAttribute("WEAVE",resultSet.getString("WEAVE"));
            setAttribute("DRAW",resultSet.getString("DRAW"));
            setAttribute("SYN",resultSet.getString("SYN"));
            setAttribute("GSM_ORD",resultSet.getString("GSM_ORD"));
            setAttribute("GSM_MFG",resultSet.getString("GSM_MFG"));
            setAttribute("W_WGT",resultSet.getString("W_WGT"));
            setAttribute("WE_WGT",resultSet.getString("WE_WGT"));
            setAttribute("TK_UP",resultSet.getString("TK_UP"));
            setAttribute("THEO_WEIGHT",resultSet.getString("THEO_WEIGHT"));
            setAttribute("THEO_LENGTH",resultSet.getString("THEO_LENGTH"));
            setAttribute("THEO_PICKS",resultSet.getString("THEO_PICKS"));
            setAttribute("LENGTH_FACT",resultSet.getString("LENGTH_FACT"));
            setAttribute("END_LENGTH",resultSet.getString("END_LENGTH"));
            setAttribute("T_THICK",resultSet.getString("T_THICK"));
            setAttribute("THEO_CFM",resultSet.getString("THEO_CFM"));
            setAttribute("BASE_SKG_TOTAL",resultSet.getString("BASE_SKG_TOTAL"));
            setAttribute("TOTAL_SKG",resultSet.getString("TOTAL_SKG"));
            setAttribute("TRIM_WEIGHT",resultSet.getString("TRIM_WEIGHT"));
            setAttribute("WEIGHT_RANGE",resultSet.getString("WEIGHT_RANGE"));
            setAttribute("BILL_WEIGHT",resultSet.getString("BILL_WEIGHT"));
            setAttribute("KILLOS",resultSet.getString("KILLOS"));
            setAttribute("FACE_SINGLE",resultSet.getString("FACE_SINGLE"));
            setAttribute("BACK_SINGLE",resultSet.getString("BACK_SINGLE"));
            setAttribute("PER_COUNT",resultSet.getString("PER_COUNT"));
            setAttribute("WEAVING_INSTRUCTION",resultSet.getString("WEAVING_INSTRUCTION"));
            setAttribute("DRYER_WIDTH_MARK_WET_DRY",resultSet.getString("DRYER_WIDTH_MARK_WET_DRY"));
            setAttribute("TAG_INSTRUCTION",resultSet.getString("TAG_INSTRUCTION"));
            setAttribute("FINISHING_INSTRUCTION",resultSet.getString("FINISHING_INSTRUCTION"));
            setAttribute("NEEDLING_INSTRUCTION",resultSet.getString("NEEDLING_INSTRUCTION"));
            setAttribute("DRESS_DRAW_WEAVING_INSTRUCTION",resultSet.getString("DRESS_DRAW_WEAVING_INSTRUCTION"));
            setAttribute("BASE_GSM",resultSet.getString("BASE_GSM"));
            setAttribute("WEB_GSM",resultSet.getString("WEB_GSM"));
            setAttribute("TOTAL_GSM",resultSet.getString("TOTAL_GSM"));
            setAttribute("SAFETY",resultSet.getString("SAFETY"));
            setAttribute("NEEDLING_TANTION",resultSet.getString("NEEDLING_TANTION"));
            setAttribute("PER_WEB",resultSet.getString("PER_WEB"));
            setAttribute("PER_SYN_BASE",resultSet.getString("PER_SYN_BASE"));
            setAttribute("PER_SYN_WEB",resultSet.getString("PER_SYN_WEB"));
            setAttribute("WEB_ON_FACE",resultSet.getString("WEB_ON_FACE"));
            setAttribute("WEB_ON_BACK",resultSet.getString("WEB_ON_BACK"));
            setAttribute("PAINT_LINES",resultSet.getString("PAINT_LINES"));
            setAttribute("LOOM_NO",resultSet.getString("LOOM_NO"));
            setAttribute("NEEDLING_TEN_FACT",resultSet.getString("NEEDLING_TEN_FACT"));
            setAttribute("TW_BE_ND",resultSet.getString("TW_BE_ND"));
            setAttribute("T_WEB_WEIGHT",resultSet.getString("T_WEB_WEIGHT"));
            setAttribute("T_TOTAL_WEIGHT",resultSet.getString("T_TOTAL_WEIGHT"));
            setAttribute("REMARK",resultSet.getString("REMARK"));
            
            setAttribute("DESIGN_CHANGED_BY",resultSet.getString("DESIGN_CHANGED_BY"));
            
            setAttribute("LAYER_TYPE",resultSet.getString("LAYER_TYPE"));
            setAttribute("BASE_GSM_A",resultSet.getString("BASE_GSM_A"));
            setAttribute("BASE_GSM_B",resultSet.getString("BASE_GSM_B"));
            setAttribute("TW_BE_ND_A",resultSet.getString("TW_BE_ND_A"));
            setAttribute("TW_BE_ND_B",resultSet.getString("TW_BE_ND_B"));
            
             setAttribute("STYLE_MULTILAYER",resultSet.getString("STYLE_MULTILAYER"));
            setAttribute("PRODUCT_GROUP_MULTILAYER",resultSet.getString("PRODUCT_GROUP_MULTILAYER"));
            setAttribute("PRODUCT_CODE_MULTILAYER",resultSet.getString("PRODUCT_CODE_MULTILAYER"));
            setAttribute("LENGTH_MULTILAYER", resultSet.getString("LENGTH_MULTILAYER"));
            setAttribute("WIDTH_MULTILAYER", resultSet.getString("WIDTH_MULTILAYER"));
            setAttribute("GSM_MULTILAYER", resultSet.getString("GSM_MULTILAYER"));
            setAttribute("SQMTR_MULTILAYER", resultSet.getString("SQMTR_MULTILAYER"));
            setAttribute("WEIGHT_MULTILAYER", resultSet.getString("WEIGHT_MULTILAYER"));
            setAttribute("WEAVE_MULTILAYER", resultSet.getString("WEAVE_MULTILAYER"));
            setAttribute("WEFT_MULTILAYER", resultSet.getString("WEFT_MULTILAYER"));
            setAttribute("PICKS_10CM_MULTILAYER", resultSet.getString("PICKS_10CM_MULTILAYER"));
            setAttribute("WARP_MULTILAYER", resultSet.getString("WARP_MULTILAYER"));
            setAttribute("ENDS_10CM_MULTILAYER", resultSet.getString("ENDS_10CM_MULTILAYER"));
            setAttribute("WIDTH_FACT_MULTILAYER", resultSet.getString("WIDTH_FACT_MULTILAYER"));
            setAttribute("NO_ENDS_MULTILAYER", resultSet.getString("NO_ENDS_MULTILAYER"));
            setAttribute("REED_MULTILAYER", resultSet.getString("REED_MULTILAYER"));
            setAttribute("REED_SPACE_MULTILAYER", resultSet.getString("REED_SPACE_MULTILAYER"));
            setAttribute("REASON_OF_REVISION_MULTILAYER", resultSet.getString("REASON_OF_REVISION_MULTILAYER"));
            setAttribute("DRAW_MULTILAYER", resultSet.getString("DRAW_MULTILAYER"));
            setAttribute("GSM_ORD_MULTILAYER", resultSet.getString("GSM_ORD_MULTILAYER"));
            setAttribute("GSM_MFG_MULTILAYER", resultSet.getString("GSM_MFG_MULTILAYER"));
            setAttribute("W_WGT_MULTILAYER", resultSet.getString("W_WGT_MULTILAYER"));
            setAttribute("WE_WGT_MULTILAYER", resultSet.getString("WE_WGT_MULTILAYER"));
            setAttribute("TK_UP_MULTILAYER", resultSet.getString("TK_UP_MULTILAYER"));
            setAttribute("THEO_WEIGHT_MULTILAYER", resultSet.getString("THEO_WEIGHT_MULTILAYER"));
            setAttribute("THEO_LENGTH_MULTILAYER", resultSet.getString("THEO_LENGTH_MULTILAYER"));
            setAttribute("THEO_PICKS_MULTILAYER", resultSet.getString("THEO_PICKS_MULTILAYER"));
            setAttribute("LENGTH_FACT_MULTILAYER", resultSet.getString("LENGTH_FACT_MULTILAYER"));
            setAttribute("END_LENGTH_MULTILAYER",resultSet.getString("END_LENGTH_MULTILAYER"));
            setAttribute("T_THICK_MULTILAYER",resultSet.getString("T_THICK_MULTILAYER"));
            setAttribute("FACE_SINGLE_MULTILAYER", resultSet.getString("FACE_SINGLE_MULTILAYER"));
            setAttribute("KILLOS_MULTILAYER", resultSet.getString("KILLOS_MULTILAYER"));
            setAttribute("BILL_WEIGHT_MULTILAYER", resultSet.getString("BILL_WEIGHT_MULTILAYER"));
            setAttribute("TRIM_WEIGHT_MULTILAYER", resultSet.getString("TRIM_WEIGHT_MULTILAYER"));
            setAttribute("TOTAL_SKG_MULTILAYER", resultSet.getString("TOTAL_SKG_MULTILAYER"));
            setAttribute("BASE_SKG_TOTAL_MULTILAYER", resultSet.getString("BASE_SKG_TOTAL_MULTILAYER"));
            setAttribute("THEO_CFM_MULTILAYER", resultSet.getString("THEO_CFM_MULTILAYER"));
            setAttribute("WEIGHT_RANGE_MULTILAYER", resultSet.getString("WEIGHT_RANGE_MULTILAYER"));
            setAttribute("PER_COUNT_MULTILAYER",resultSet.getString("PER_COUNT_MULTILAYER"));
            setAttribute("FINISHING_INSTRUCTION_MULTILAYER",resultSet.getString("FINISHING_INSTRUCTION_MULTILAYER"));
            
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
                resultSetTemp=statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_DETAIL_H WHERE DESIGN_DOC_NO='"+resultSet.getString("DESIGN_DOC_NO")+"'  AND REVISION_NO="+RevNo+" ORDER BY SR_NO DESC");
            }else {
                resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_DETAIL WHERE DESIGN_DOC_NO='"+resultSet.getString("DESIGN_DOC_NO")+"'  ORDER BY SR_NO+0");
            }
            
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
  
                clsFeltDesignMasterDetails ObjFeltSalesOrderDetails = new clsFeltDesignMasterDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("DESIGN_DOC_NO",resultSetTemp.getString("DESIGN_DOC_NO"));
                
                ObjFeltSalesOrderDetails.setAttribute("FACE_WEB",resultSetTemp.getString("FACE_WEB"));
                ObjFeltSalesOrderDetails.setAttribute("FACE_NO_WEB",resultSetTemp.getString("FACE_NO_WEB"));
                ObjFeltSalesOrderDetails.setAttribute("BACK_WEB",resultSetTemp.getString("BACK_WEB"));
                ObjFeltSalesOrderDetails.setAttribute("BACK_NO_WEB",resultSetTemp.getString("BACK_NO_WEB"));
                ObjFeltSalesOrderDetails.setAttribute("TAKE_UP",resultSetTemp.getString("TAKE_UP"));
                ObjFeltSalesOrderDetails.setAttribute("PENETRATION",resultSetTemp.getString("PENETRATION"));
                
                
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
            
            String strSql = "SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER_H WHERE DESIGN_DOC_NO = " + pDocNo + "";
            System.out.println("STR SQL : "+strSql);
            connection=data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery(strSql);
            resultSet.first();
            setAttribute("DESIGN_DOC_NO",resultSet.getString("DESIGN_DOC_NO"));
            setAttribute("REVISION_NO",resultSet.getString("REVISION_NO"));
                setAttribute("UPDATED_BY",resultSet.getString("UPDATED_BY"));
                setAttribute("DESIGN_DOC_DATE",resultSet.getString("DESIGN_DOC_DATE"));
                setAttribute("HIERARCHY_ID",resultSet.getInt("HIERARCHY_ID"));
                
                setAttribute("DESIGN_DOC_NO",resultSet.getString("DESIGN_DOC_NO"));
                setAttribute("DESIGN_DOC_DATE",resultSet.getDate("DESIGN_DOC_DATE"));
                setAttribute("PARTY_CODE",resultSet.getString("PARTY_CODE"));
                setAttribute("PARTY_NAME",resultSet.getString("PARTY_NAME"));
                setAttribute("MACHINE_NO",resultSet.getString("MACHINE_NO"));
                setAttribute("POSITION",resultSet.getString("POSITION"));
            
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_DESIGN_MASTER_DETAIL_H WHERE DESIGN_DOC_NO='"+pDocNo+"'");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                clsFeltDesignMasterDetails ObjFeltSalesOrderDetails = new clsFeltDesignMasterDetails();
                
                ObjFeltSalesOrderDetails.setAttribute("SR_NO",resultSetTemp.getString("SR_NO"));
                ObjFeltSalesOrderDetails.setAttribute("DESIGN_DOC_NO",resultSetTemp.getString("DESIGN_DOC_NO"));
                
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

            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER_H WHERE DESIGN_DOC_NO='"+productionDocumentNo+"'");
            while(rsTmp.next()) {
                clsFeltDesignMaster felt_order = new clsFeltDesignMaster();
                
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
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER_H WHERE DESIGN_DOC_NO ='"+pDocNo+"'");
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
                strSQL="SELECT DISTINCT H.DESIGN_DOC_NO,H.DESIGN_DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.DESIGN_DOC_NO=H.DESIGN_DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,H.DESIGN_DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT H.DESIGN_DOC_NO,H.DESIGN_DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER H,  PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.DESIGN_DOC_NO=H.DESIGN_DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+" ORDER BY RECEIVED_DATE,H.DESIGN_DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT H.DESIGN_DOC_NO,H.DESIGN_DOC_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_DESIGN_MASTER_HEADER H, PRODUCTION.FELT_PROD_DOC_DATA D  WHERE D.DESIGN_DOC_NO=H.DESIGN_DOC_NO AND STATUS='W' AND MODULE_ID="+ModuleID+" AND CANCELED=0  AND USER_ID="+pUserID+"  ORDER BY H.DESIGN_DOC_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltDesignMaster ObjDoc=new clsFeltDesignMaster();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DESIGN_DOC_NO",rsTmp.getString("DESIGN_DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DESIGN_DOC_DATE"));
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
