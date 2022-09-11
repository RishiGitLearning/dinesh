/*
 * clsFeltWeavingLoom.java
 *
 * Created on March 12, 2013, 3:10 PM
 *
 * @author  Dhaval Rahevar
 */

package EITLERP.Production.FeltWeavingLoom;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.*;
import EITLERP.ComboData;
import EITLERP.Production.clsFeltProductionApprovalFlow;

public class clsFeltWeavingLoom {
    
    public String LastError="";
    private static Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public HashMap props;
    public boolean Ready = false;
    //Felt Order Collection
    public HashMap hmFeltWvgLoomEffDetails;
    
    //History Related properties
    public boolean HistoryView=false;
    private String historyWvgDate="";
    private String historyWvgProdNo="";
    public static int ModuleID=723;
    
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
    public clsFeltWeavingLoom() {
        LastError = "";
        props=new HashMap();
        props.put("WLO_WEAVING_DATE", new Variant(""));
        props.put("WLO_WVGPROD_NO", new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("RECEIVED_DATE",new Variant(""));
        // -- Approval Specific Fields --
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        hmFeltWvgLoomEffDetails=new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,WLO_SHIFT FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WEAVING_DATE >= '"+EITLERPGLOBAL.FinFromDateDB+"' AND WLO_WEAVING_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY WLO_WEAVING_DATE,WLO_WVGPROD_NO");
            //resultSet=statement.executeQuery("SELECT DISTINCT WLO_WVGPROD_NO,WVG_WLO_PROD_DATE,WLO_SHIFT FROM PRODUCTION.FELT_WVG_LOOM_EFF ORDER BY WLO_WVGPROD_NO");
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
            if(HistoryView) setHistoryData(historyWvgDate, historyWvgProdNo);
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
            if(HistoryView) setHistoryData(historyWvgDate, historyWvgProdNo);
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
            if(HistoryView) setHistoryData(historyWvgDate, historyWvgProdNo);
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
            if(HistoryView) setHistoryData(historyWvgDate, historyWvgProdNo);
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
        ResultSet  resultSetDetail,resultSetHistory;
        Statement  statementDetail, statementHistory;
        try {
            // Felt ShiftWise Weaving loom Efficiency data connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WVGPROD_NO =''");
            
            // FFelt ShiftWise Weaving loom Efficiency  history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_LOOM_EFF_H WHERE WLO_WVGPROD_NO=''");
            
            //Now Insert records into FELT_WVG_LOOM_EFF & History tables
            for(int i=1;i<=hmFeltWvgLoomEffDetails.size();i++) {
                clsFeltWeavingLoomDetails ObjFeltWeavingLoomDetails = (clsFeltWeavingLoomDetails) hmFeltWvgLoomEffDetails.get(Integer.toString(i));
                
                //Insert records into Felt Weaving Loom table
                resultSetDetail.moveToInsertRow();
                
                resultSetDetail.updateString("WLO_WEAVING_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("WLO_WEAVING_DATE").getString()));
                resultSetDetail.updateString("WLO_WVGPROD_NO", getAttribute("WLO_WVGPROD_NO").getString());
                resultSetDetail.updateString("WLO_LOOM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOOM_NO").getString());
                resultSetDetail.updateString("WLO_SHIFT", getAttribute("WLO_SHIFT").getString());
                resultSetDetail.updateString("WLO_RPM",ObjFeltWeavingLoomDetails.getAttribute("WLO_RPM").getString());
                resultSetDetail.updateString("WLO_WARP_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_NO").getString());
                resultSetDetail.updateString("WLO_READ_SPACE",ObjFeltWeavingLoomDetails.getAttribute("WLO_READ_SPACE").getString());
                resultSetDetail.updateString("WLO_GROUP",ObjFeltWeavingLoomDetails.getAttribute("WLO_GROUP").getString());
                resultSetDetail.updateString("WLO_THEORITICAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_THEORITICAL_PICS").getString());
                resultSetDetail.updateString("WLO_ACTUAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_ACTUAL_PICS").getString());
                resultSetDetail.updateString("WLO_TARGATEDEFF",ObjFeltWeavingLoomDetails.getAttribute("WLO_TARGATEDEFF").getString());
                resultSetDetail.updateString("WLO_NOWARP_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWARP_TIME").getString());
                resultSetDetail.updateString("WLO_NOWEFT",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEFT").getString());
                resultSetDetail.updateString("WLO_NOPOWER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOPOWER_TIME").getString());
                resultSetDetail.updateString("WLO_NOAIR",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOAIR").getString());
                resultSetDetail.updateString("WLO_BEAMGAITING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAMGAITING_TIME").getString());
                resultSetDetail.updateString("WLO_NOWEAVER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEAVER_TIME").getString());
                resultSetDetail.updateString("WLO_BEAM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAM_NO").getString());
                resultSetDetail.updateString("WLO_CHANGE_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CHANGE_TIME").getString());
                resultSetDetail.updateString("WLO_OTHER",ObjFeltWeavingLoomDetails.getAttribute("WLO_OTHER").getString());
                resultSetDetail.updateString("WLO_WEAVING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVING_TIME").getString());
                resultSetDetail.updateString("WLO_LESS",ObjFeltWeavingLoomDetails.getAttribute("WLO_LESS").getString());
                resultSetDetail.updateString("WLO_SHORT",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHORT").getString());
                resultSetDetail.updateString("SUPER_NO",ObjFeltWeavingLoomDetails.getAttribute("SUPER_NO").getString());
                resultSetDetail.updateString("NAME_SUPER",ObjFeltWeavingLoomDetails.getAttribute("NAME_SUPER").getString());
                resultSetDetail.updateString("WLO_WEAVER_CARDNO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVER_CARDNO").getString());
                resultSetDetail.updateString("WLO_REMARKS",ObjFeltWeavingLoomDetails.getAttribute("WLO_REMARKS").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR1").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR2").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR3").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR4").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR5").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR6").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR1").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR2").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR3").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR4").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR1").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR2").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR3").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR4").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR5").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR6").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR7",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR7").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR8",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR8").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR1").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR2").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR3").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR4").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR5").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR1").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR2").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR3").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR4").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR5").getString());
                resultSetDetail.updateString("WLO_MISC_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_MISC_TIME").getString());
                resultSetDetail.updateString("WLO_LOSS_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TIME").getString());
                resultSetDetail.updateString("WLO_LOSS_TOTAL",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TOTAL").getString());
                
                resultSetDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                resultSetDetail.updateString("CREATED_DATE",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetDetail.updateInt("MODIFIED_BY",0);
                resultSetDetail.updateString("MODIFIED_DATE","0000-00-00");
                resultSetDetail.updateBoolean("APPROVED",false);
                resultSetDetail.updateString("APPROVED_DATE","0000-00-00");
                resultSetDetail.updateBoolean("REJECTED",false);
                resultSetDetail.updateString("REJECTED_DATE","0000-00-00");
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into Felt Order Amendment History Table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",1);
                resultSetHistory.updateString("WLO_WEAVING_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("WLO_WEAVING_DATE").getString()));
                resultSetHistory.updateString("WLO_WVGPROD_NO", getAttribute("WLO_WVGPROD_NO").getString());
                resultSetHistory.updateString("WLO_LOOM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOOM_NO").getString());
                resultSetHistory.updateString("WLO_SHIFT", getAttribute("WLO_SHIFT").getString());
                resultSetHistory.updateString("WLO_RPM",ObjFeltWeavingLoomDetails.getAttribute("WLO_RPM").getString());
                resultSetHistory.updateString("WLO_WARP_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_NO").getString());
                resultSetHistory.updateString("WLO_READ_SPACE",ObjFeltWeavingLoomDetails.getAttribute("WLO_READ_SPACE").getString());
                resultSetHistory.updateString("WLO_GROUP",ObjFeltWeavingLoomDetails.getAttribute("WLO_GROUP").getString());
                resultSetHistory.updateString("WLO_THEORITICAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_THEORITICAL_PICS").getString());
                resultSetHistory.updateString("WLO_ACTUAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_ACTUAL_PICS").getString());
                resultSetHistory.updateString("WLO_TARGATEDEFF",ObjFeltWeavingLoomDetails.getAttribute("WLO_TARGATEDEFF").getString());
                resultSetHistory.updateString("WLO_NOWARP_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWARP_TIME").getString());
                resultSetHistory.updateString("WLO_NOWEFT",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEFT").getString());
                resultSetHistory.updateString("WLO_NOPOWER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOPOWER_TIME").getString());
                resultSetHistory.updateString("WLO_NOAIR",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOAIR").getString());
                resultSetHistory.updateString("WLO_BEAMGAITING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAMGAITING_TIME").getString());
                resultSetHistory.updateString("WLO_NOWEAVER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEAVER_TIME").getString());
                resultSetHistory.updateString("WLO_BEAM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAM_NO").getString());
                resultSetHistory.updateString("WLO_CHANGE_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CHANGE_TIME").getString());
                resultSetHistory.updateString("WLO_OTHER",ObjFeltWeavingLoomDetails.getAttribute("WLO_OTHER").getString()); resultSetHistory.updateString("WLO_WEAVING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVING_TIME").getString());
                resultSetHistory.updateString("WLO_LESS",ObjFeltWeavingLoomDetails.getAttribute("WLO_LESS").getString());
                resultSetHistory.updateString("WLO_SHORT",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHORT").getString());
                resultSetHistory.updateString("SUPER_NO",ObjFeltWeavingLoomDetails.getAttribute("SUPER_NO").getString());
                resultSetHistory.updateString("NAME_SUPER",ObjFeltWeavingLoomDetails.getAttribute("NAME_SUPER").getString());
                resultSetHistory.updateString("WLO_WEAVER_CARDNO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVER_CARDNO").getString());
                resultSetHistory.updateString("WLO_REMARKS",ObjFeltWeavingLoomDetails.getAttribute("WLO_REMARKS").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR1").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR2").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR3").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR4").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR5").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR6").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR1").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR2").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR3").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR4").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR1").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR2").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR3").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR4").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR5").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR6").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR7",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR7").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR8",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR8").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR1").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR2").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR3").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR4").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR5").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR1").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR2").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR3").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR4").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR5").getString());
                resultSetHistory.updateString("WLO_MISC_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_MISC_TIME").getString());
                resultSetHistory.updateString("WLO_LOSS_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TIME").getString());
                resultSetHistory.updateString("WLO_LOSS_TOTAL",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TOTAL").getString());
                
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
                
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=723; //Felt Weaving Entry
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("WLO_WVGPROD_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFeltProductionApprovalFlow.TableName="FELT_WVG_LOOM_EFF";
            ObjFeltProductionApprovalFlow.IsCreator=true;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="WLO_WVGPROD_NO";
            
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
            
            LoadData();
            resultSetDetail.close();
            statementDetail.close();
            //  statementTemp.close();
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
        ResultSet  resultSetDetail, resultSetTemp, resultSetDetailHistory, resultSetHistory;
        Statement  statementDetail, statementTemp, statementDetailHistory, statementHistory;
        int revisionNo =1;
        try {
            // Production data connection
            //statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WVGPROD_NO=''");
            
             statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WVGPROD_NO=''");
          
            
            
            // Production data history connection
            statementHistory=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //Get the Maximum Revision No in History Table.
            resultSetHistory=statementHistory.executeQuery("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_WVG_LOOM_EFF_H WHERE WLO_WEAVING_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("WLO_WEAVING_DATE").getString())+"' AND WLO_WVGPROD_NO='"+getAttribute("WLO_WVGPROD_NO").getString()+"'");
            resultSetHistory.first();
            
            if(resultSetHistory.getRow()>0){
                revisionNo=resultSetHistory.getInt(1);
                revisionNo++;
            }
            //delete records from production data table before insert
            statementHistory.execute("DELETE FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WEAVING_DATE='"+EITLERPGLOBAL.formatDateDB(getAttribute("WLO_WEAVING_DATE").getString())+"' AND WLO_WVGPROD_NO='"+getAttribute("WLO_WVGPROD_NO").getString()+"'");
            
            resultSetHistory=statementHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_LOOM_EFF_H WHERE WLO_WVGPROD_NO=''");
            
            
         /*   
            // Consignment detail connection
            statementDetail = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementDetail.executeQuery("SELECT * FROM  PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WVGPROD_NO=''");
            
          */  
            
            
            
            
            
            //Now Update records into FELT_WVG_LOOM_EFF tables
            for(int i=1;i<=hmFeltWvgLoomEffDetails.size();i++) {
                clsFeltWeavingLoomDetails ObjFeltWeavingLoomDetails=(clsFeltWeavingLoomDetails) hmFeltWvgLoomEffDetails.get(Integer.toString(i));
                
                resultSetDetail.moveToInsertRow();
                resultSetDetail.updateString("WLO_WVGPROD_NO", getAttribute("WLO_WVGPROD_NO").getString());
                resultSetDetail.updateString("WLO_WEAVING_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("WLO_WEAVING_DATE").getString()));
                resultSetDetail.updateString("WLO_LOOM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOOM_NO").getString());
                resultSetDetail.updateString("WLO_SHIFT", getAttribute("WLO_SHIFT").getString());
                resultSetDetail.updateString("WLO_RPM",ObjFeltWeavingLoomDetails.getAttribute("WLO_RPM").getString());
                resultSetDetail.updateString("WLO_WARP_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_NO").getString());
                resultSetDetail.updateString("WLO_READ_SPACE",ObjFeltWeavingLoomDetails.getAttribute("WLO_READ_SPACE").getString());
                resultSetDetail.updateString("WLO_GROUP",ObjFeltWeavingLoomDetails.getAttribute("WLO_GROUP").getString());
                resultSetDetail.updateString("WLO_THEORITICAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_THEORITICAL_PICS").getString());
                resultSetDetail.updateString("WLO_ACTUAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_ACTUAL_PICS").getString());
                resultSetDetail.updateString("WLO_TARGATEDEFF",ObjFeltWeavingLoomDetails.getAttribute("WLO_TARGATEDEFF").getString());
                resultSetDetail.updateString("WLO_NOWARP_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWARP_TIME").getString());
                resultSetDetail.updateString("WLO_NOWEFT",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEFT").getString());
                resultSetDetail.updateString("WLO_NOPOWER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOPOWER_TIME").getString());
                resultSetDetail.updateString("WLO_NOAIR",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOAIR").getString());
                resultSetDetail.updateString("WLO_BEAMGAITING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAMGAITING_TIME").getString());
                resultSetDetail.updateString("WLO_NOWEAVER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEAVER_TIME").getString());
                resultSetDetail.updateString("WLO_BEAM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAM_NO").getString());
                resultSetDetail.updateString("WLO_CHANGE_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CHANGE_TIME").getString());
                resultSetDetail.updateString("WLO_OTHER",ObjFeltWeavingLoomDetails.getAttribute("WLO_OTHER").getString());
                resultSetDetail.updateString("WLO_WEAVING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVING_TIME").getString());
                resultSetDetail.updateString("WLO_LESS",ObjFeltWeavingLoomDetails.getAttribute("WLO_LESS").getString());
                resultSetDetail.updateString("WLO_SHORT",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHORT").getString());
                resultSetDetail.updateString("SUPER_NO",ObjFeltWeavingLoomDetails.getAttribute("SUPER_NO").getString());
                resultSetDetail.updateString("NAME_SUPER",ObjFeltWeavingLoomDetails.getAttribute("NAME_SUPER").getString());
                resultSetDetail.updateString("WLO_WEAVER_CARDNO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVER_CARDNO").getString());
                resultSetDetail.updateString("WLO_REMARKS",ObjFeltWeavingLoomDetails.getAttribute("WLO_REMARKS").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR_TIME").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR1").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR2").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR3").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR4").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR5").getString());
                resultSetDetail.updateString("WLO_CLOTH_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR6").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR1").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR2").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR3").getString());
                resultSetDetail.updateString("WLO_SHUTTLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR4").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR1").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR2").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR3").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR4").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR5").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR6").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR7",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR7").getString());
                resultSetDetail.updateString("WLO_PICKING_REPAIR8",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR8").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR1").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR2").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR3").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR4").getString());
                resultSetDetail.updateString("WLO_WARP_END_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR5").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR1").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR2").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR3").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR4").getString());
                resultSetDetail.updateString("WLO_TEMPLE_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR5").getString());
                resultSetDetail.updateString("WLO_MISC_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_MISC_TIME").getString());
                resultSetDetail.updateString("WLO_LOSS_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TIME").getString());
                resultSetDetail.updateString("WLO_LOSS_TOTAL",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TOTAL").getString());
                
                resultSetDetail.updateBoolean("APPROVED",false);
                resultSetDetail.updateString("APPROVED_DATE","0000-00-00");
                resultSetDetail.updateBoolean("REJECTED",false);
                resultSetDetail.updateString("REJECTED_DATE","0000-00-00");
                resultSetDetail.updateInt("CANCELED",0);
                resultSetDetail.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
                resultSetDetail.updateInt("CHANGED",1);
                resultSetDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetDetail.insertRow();
                
                //Insert records into FELT_WVG_LOOM_EFF_H table
                resultSetHistory.moveToInsertRow();
                resultSetHistory.updateInt("REVISION_NO",revisionNo);
                resultSetHistory.updateString("WLO_WEAVING_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("WLO_WEAVING_DATE").getString()));
                resultSetHistory.updateString("WLO_WVGPROD_NO", getAttribute("WLO_WVGPROD_NO").getString());
                resultSetHistory.updateString("WLO_LOOM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOOM_NO").getString());
                resultSetHistory.updateString("WLO_SHIFT", getAttribute("WLO_SHIFT").getString());
                resultSetHistory.updateString("WLO_RPM",ObjFeltWeavingLoomDetails.getAttribute("WLO_RPM").getString());
                resultSetHistory.updateString("WLO_WARP_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_NO").getString());
                resultSetHistory.updateString("WLO_READ_SPACE",ObjFeltWeavingLoomDetails.getAttribute("WLO_READ_SPACE").getString());
                resultSetHistory.updateString("WLO_GROUP",ObjFeltWeavingLoomDetails.getAttribute("WLO_GROUP").getString());
                resultSetHistory.updateString("WLO_THEORITICAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_THEORITICAL_PICS").getString());
                resultSetHistory.updateString("WLO_ACTUAL_PICS",ObjFeltWeavingLoomDetails.getAttribute("WLO_ACTUAL_PICS").getString());
                resultSetHistory.updateString("WLO_TARGATEDEFF",ObjFeltWeavingLoomDetails.getAttribute("WLO_TARGATEDEFF").getString());
                resultSetHistory.updateString("WLO_NOWARP_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWARP_TIME").getString());
                resultSetHistory.updateString("WLO_NOWEFT",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEFT").getString());
                resultSetHistory.updateString("WLO_NOPOWER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOPOWER_TIME").getString());
                resultSetHistory.updateString("WLO_NOAIR",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOAIR").getString());
                resultSetHistory.updateString("WLO_BEAMGAITING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAMGAITING_TIME").getString());
                resultSetHistory.updateString("WLO_NOWEAVER_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_NOWEAVER_TIME").getString());
                resultSetHistory.updateString("WLO_BEAM_NO",ObjFeltWeavingLoomDetails.getAttribute("WLO_BEAM_NO").getString());
                resultSetHistory.updateString("WLO_CHANGE_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CHANGE_TIME").getString());
                resultSetHistory.updateString("WLO_OTHER",ObjFeltWeavingLoomDetails.getAttribute("WLO_OTHER").getString());
                resultSetHistory.updateString("WLO_WEAVING_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVING_TIME").getString());
                resultSetHistory.updateString("WLO_LESS",ObjFeltWeavingLoomDetails.getAttribute("WLO_LESS").getString());
                resultSetHistory.updateString("WLO_SHORT",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHORT").getString());
                resultSetHistory.updateString("SUPER_NO",ObjFeltWeavingLoomDetails.getAttribute("SUPER_NO").getString());
                resultSetHistory.updateString("NAME_SUPER",ObjFeltWeavingLoomDetails.getAttribute("NAME_SUPER").getString());
                resultSetHistory.updateString("WLO_WEAVER_CARDNO",ObjFeltWeavingLoomDetails.getAttribute("WLO_WEAVER_CARDNO").getString());
                resultSetHistory.updateString("WLO_REMARKS",ObjFeltWeavingLoomDetails.getAttribute("WLO_REMARKS").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR_TIME").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR1").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR2").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR3").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR4").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR5").getString());
                resultSetHistory.updateString("WLO_CLOTH_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_CLOTH_REPAIR6").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR1").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR2").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR3").getString());
                resultSetHistory.updateString("WLO_SHUTTLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_SHUTTLE_REPAIR4").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR1").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR2").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR3").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR4").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR5").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR6",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR6").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR7",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR7").getString());
                resultSetHistory.updateString("WLO_PICKING_REPAIR8",ObjFeltWeavingLoomDetails.getAttribute("WLO_PICKING_REPAIR8").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR1").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR2").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR3").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR4").getString());
                resultSetHistory.updateString("WLO_WARP_END_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_WARP_END_REPAIR5").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR1",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR1").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR2",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR2").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR3",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR3").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR4",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR4").getString());
                resultSetHistory.updateString("WLO_TEMPLE_REPAIR5",ObjFeltWeavingLoomDetails.getAttribute("WLO_TEMPLE_REPAIR5").getString());
                resultSetHistory.updateString("WLO_MISC_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_MISC_TIME").getString());
                resultSetHistory.updateString("WLO_LOSS_TIME",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TIME").getString());
                resultSetHistory.updateString("WLO_LOSS_TOTAL",ObjFeltWeavingLoomDetails.getAttribute("WLO_LOSS_TOTAL").getString());
                
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
            }
            
            //======== Update the Approval Flow =========
            clsFeltProductionApprovalFlow ObjFeltProductionApprovalFlow = new clsFeltProductionApprovalFlow();
            ObjFeltProductionApprovalFlow.ModuleID=723; //Felt Order Updation Module
            ObjFeltProductionApprovalFlow.DocNo=getAttribute("WLO_WVGPROD_NO").getString();
            ObjFeltProductionApprovalFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFeltProductionApprovalFlow.From=(int)getAttribute("FROM").getVal();
            ObjFeltProductionApprovalFlow.To=(int)getAttribute("TO").getVal();
            ObjFeltProductionApprovalFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFeltProductionApprovalFlow.TableName="PRODUCTION.FELT_WVG_LOOM_EFF";
            ObjFeltProductionApprovalFlow.IsCreator=false;
            ObjFeltProductionApprovalFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFeltProductionApprovalFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFeltProductionApprovalFlow.FieldName="WLO_WVGPROD_NO";
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("R")) {
                ObjFeltProductionApprovalFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFeltProductionApprovalFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_WVG_LOOM_EFF SET REJECTED=0,CHANGED=1 WHERE WLO_WVGPROD_NO ='"+getAttribute("WLO_WVGPROD_NO").getString()+"'");
                //Remove Old Records from FELT DOCUMET APPROVAL TABLE
                //data.Execute("DELETE FROM PRODUCTION.`FELT_PROD_DOC_DATA` WHERE MODULE_ID=723 AND DOC_NO='"+getAttribute("PRODUCTION_DOCUMENT_NO").getString()+"'");
                
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
            
            
            setData();
            resultSetDetail.close();
        //    statementTemp.close();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE  WLO_WVGPROD_NO='"+documentNo +"' AND APPROVED="+1;
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                LastError = "Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false; //Discard the request
            }else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=723 AND USER_ID="+userID+" AND DOC_NO='"+ documentNo +"' AND STATUS='W'";
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    strSQL = "DELETE FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE "
                    + " AND WLO_WEAVING_DATE= '"+ EITLERPGLOBAL.formatDateDB(stringProductionDate) +"'"
                    + " AND WLO_WVGPROD_NO='" + documentNo + "'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WVGPROD_NO='"+ orderupdDocumentNo +"' AND APPROVED=1";
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0){  //Item is Approved
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=723 AND USER_ID="+userID+" AND DOC_NO='"+ orderupdDocumentNo +"' AND STATUS='W'";
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
            String strSql = "SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,WLO_SHIFT FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE  " + stringFindQuery + " ORDER BY WLO_WEAVING_DATE";
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
        double totalWeight, previousWeight;
       try {
         if (HistoryView) {
                RevNo = resultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", resultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
         }
            setAttribute("WLO_WVGPROD_NO",resultSet.getString("WLO_WVGPROD_NO"));
            setAttribute("WLO_WEAVING_DATE",resultSet.getDate("WLO_WEAVING_DATE"));
            setAttribute("WLO_SHIFT",resultSet.getString("WLO_SHIFT"));
            
            hmFeltWvgLoomEffDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WVGPROD_NO = '"+ resultSet.getString("WLO_WVGPROD_NO") +"' AND WLO_WEAVING_DATE='"+ resultSet.getDate("WLO_WEAVING_DATE") +"' ORDER BY WLO_WVGPROD_NO DESC");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                setAttribute("CREATED_DATE",resultSetTemp.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",resultSetTemp.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",resultSetTemp.getString("MODIFIED_DATE"));
                setAttribute("APPROVED",resultSetTemp.getInt("APPROVED"));
                setAttribute("APPROVED_DATE",resultSetTemp.getString("APPROVED_DATE"));
                setAttribute("REJECTED",resultSetTemp.getBoolean("REJECTED"));
                setAttribute("REJECTED_DATE",resultSetTemp.getString("REJECTED_DATE"));
                setAttribute("CANCELED",resultSetTemp.getInt("CANCELED"));
                setAttribute("HIERARCHY_ID",resultSetTemp.getInt("HIERARCHY_ID"));
                
                clsFeltWeavingLoomDetails ObjFeltWeavingLoomDetails = new clsFeltWeavingLoomDetails();
                
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LOOM_NO",resultSetTemp.getString("WLO_LOOM_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_RPM",resultSetTemp.getString("WLO_RPM"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_NO",resultSetTemp.getString("WLO_WARP_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_READ_SPACE",resultSetTemp.getString("WLO_READ_SPACE"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_GROUP",resultSetTemp.getString("WLO_GROUP"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_THEORITICAL_PICS",resultSetTemp.getString("WLO_THEORITICAL_PICS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_ACTUAL_PICS",resultSetTemp.getString("WLO_ACTUAL_PICS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TARGATEDEFF",resultSetTemp.getString("WLO_TARGATEDEFF"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOWARP_TIME",resultSetTemp.getString("WLO_NOWARP_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOWEFT",resultSetTemp.getString("WLO_NOWEFT"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOPOWER_TIME",resultSetTemp.getString("WLO_NOPOWER_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOAIR",resultSetTemp.getString("WLO_NOAIR"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_BEAMGAITING_TIME",resultSetTemp.getString("WLO_BEAMGAITING_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOWEAVER_TIME",resultSetTemp.getString("WLO_NOWEAVER_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_BEAM_NO",resultSetTemp.getString("WLO_BEAM_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CHANGE_TIME",resultSetTemp.getString("WLO_CHANGE_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_OTHER",resultSetTemp.getString("WLO_OTHER"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WEAVING_TIME",resultSetTemp.getString("WLO_WEAVING_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LESS",resultSetTemp.getString("WLO_LESS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHORT",resultSetTemp.getString("WLO_SHORT"));
                ObjFeltWeavingLoomDetails.setAttribute("SUPER_NO",resultSetTemp.getString("SUPER_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("NAME_SUPER",resultSetTemp.getString("NAME_SUPER"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WEAVER_CARDNO",resultSetTemp.getString("WLO_WEAVER_CARDNO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_REMARKS",resultSetTemp.getString("WLO_REMARKS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR_TIME",resultSetTemp.getString("WLO_SHUTTLE_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR_TIME",resultSetTemp.getString("WLO_CLOTH_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR_TIME",resultSetTemp.getString("WLO_PICKING_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR_TIME",resultSetTemp.getString("WLO_WARP_END_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR_TIME",resultSetTemp.getString("WLO_TEMPLE_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR1",resultSetTemp.getString("WLO_CLOTH_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR2",resultSetTemp.getString("WLO_CLOTH_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR3",resultSetTemp.getString("WLO_CLOTH_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR4",resultSetTemp.getString("WLO_CLOTH_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR5",resultSetTemp.getString("WLO_CLOTH_REPAIR5"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR6",resultSetTemp.getString("WLO_CLOTH_REPAIR6"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR1",resultSetTemp.getString("WLO_SHUTTLE_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR2",resultSetTemp.getString("WLO_SHUTTLE_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR3",resultSetTemp.getString("WLO_SHUTTLE_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR4",resultSetTemp.getString("WLO_SHUTTLE_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR1",resultSetTemp.getString("WLO_PICKING_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR2",resultSetTemp.getString("WLO_PICKING_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR3",resultSetTemp.getString("WLO_PICKING_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR4",resultSetTemp.getString("WLO_PICKING_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR5",resultSetTemp.getString("WLO_PICKING_REPAIR5"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR6",resultSetTemp.getString("WLO_PICKING_REPAIR6"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR7",resultSetTemp.getString("WLO_PICKING_REPAIR7"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR8",resultSetTemp.getString("WLO_PICKING_REPAIR8"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR1",resultSetTemp.getString("WLO_WARP_END_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR2",resultSetTemp.getString("WLO_WARP_END_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR3",resultSetTemp.getString("WLO_WARP_END_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR4",resultSetTemp.getString("WLO_WARP_END_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR5",resultSetTemp.getString("WLO_WARP_END_REPAIR5"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR1",resultSetTemp.getString("WLO_TEMPLE_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR2",resultSetTemp.getString("WLO_TEMPLE_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR3",resultSetTemp.getString("WLO_TEMPLE_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR4",resultSetTemp.getString("WLO_TEMPLE_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR5",resultSetTemp.getString("WLO_TEMPLE_REPAIR5"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_MISC_TIME",resultSetTemp.getString("WLO_MISC_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LOSS_TIME",resultSetTemp.getString("WLO_LOSS_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LOSS_TOTAL",resultSetTemp.getString("WLO_LOSS_TOTAL"));
                
                hmFeltWvgLoomEffDetails.put(Integer.toString(serialNoCounter),ObjFeltWeavingLoomDetails);
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
    
    public boolean setHistoryData(String pProductionDate,String pDocNo) {
        ResultSet  resultSetDetail;
        Statement  statementTemp;
        int serialNoCounter=0;
        int RevNo=0;
        try {
            RevNo=resultSet.getInt("REVISION_NO");
            setAttribute("REVISION_NO",resultSet.getInt("REVISION_NO"));
            
            //Now Populate the collection, first clear the collection
            hmFeltWvgLoomEffDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetDetail = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_WVG_LOOM_EFF_H WHERE WLO_WEAVING_DATE='"+ pProductionDate+"' AND WLO_WVGPROD_NO='"+pDocNo+"' AND REVISION_NO='"+ resultSet.getInt("REVISION_NO") +"' ORDER BY REVISION_NO");
            
            while(resultSetDetail.next()) {
                serialNoCounter++;
                setAttribute("WLO_WVGPROD_NO",resultSetDetail.getString("WLO_WVGPROD_NO"));
                setAttribute("UPDATED_BY",resultSetDetail.getInt("UPDATED_BY"));
                setAttribute("ENTRY_DATE",resultSetDetail.getString("ENTRY_DATE"));
                setAttribute("HIERARCHY_ID",resultSetDetail.getInt("HIERARCHY_ID"));
                
                clsFeltWeavingLoomDetails ObjFeltWeavingLoomDetails = new clsFeltWeavingLoomDetails();
                
                
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LOOM_NO",resultSetDetail.getString("WLO_LOOM_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_RPM",resultSetDetail.getString("WLO_RPM"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_NO",resultSetDetail.getString("WLO_WARP_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_READ_SPACE",resultSetDetail.getString("WLO_READ_SPACE"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_GROUP",resultSetDetail.getString("WLO_GROUP"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_THEORITICAL_PICS",resultSetDetail.getString("WLO_THEORITICAL_PICS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_ACTUAL_PICS",resultSetDetail.getString("WLO_ACTUAL_PICS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TARGATEDEFF",resultSetDetail.getString("WLO_TARGATEDEFF"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOWARP_TIME",resultSetDetail.getString("WLO_NOWARP_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOWEFT",resultSetDetail.getString("WLO_NOWEFT"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOPOWER_TIME",resultSetDetail.getString("WLO_NOPOWER_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOAIR",resultSetDetail.getString("WLO_NOAIR"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_BEAMGAITING_TIME",resultSetDetail.getString("WLO_BEAMGAITING_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_NOWEAVER_TIME",resultSetDetail.getString("WLO_NOWEAVER_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_BEAM_NO",resultSetDetail.getString("WLO_BEAM_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CHANGE_TIME",resultSetDetail.getString("WLO_CHANGE_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_OTHER",resultSetDetail.getString("WLO_OTHER"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WEAVING_TIME",resultSetDetail.getString("WLO_WEAVING_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LESS",resultSetDetail.getString("WLO_LESS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHORT",resultSetDetail.getString("WLO_SHORT"));
                ObjFeltWeavingLoomDetails.setAttribute("SUPER_NO",resultSetDetail.getString("SUPER_NO"));
                ObjFeltWeavingLoomDetails.setAttribute("NAME_SUPER",resultSetDetail.getString("NAME_SUPER"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WEAVER_CARDNO",resultSetDetail.getString("WLO_WEAVER_CARDNO"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_REMARKS",resultSetDetail.getString("WLO_REMARKS"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR_TIME",resultSetDetail.getString("WLO_SHUTTLE_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR_TIME",resultSetDetail.getString("WLO_CLOTH_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR_TIME",resultSetDetail.getString("WLO_PICKING_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR_TIME",resultSetDetail.getString("WLO_WARP_END_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR_TIME",resultSetDetail.getString("WLO_TEMPLE_REPAIR_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR1",resultSetDetail.getString("WLO_CLOTH_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR2",resultSetDetail.getString("WLO_CLOTH_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR3",resultSetDetail.getString("WLO_CLOTH_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR4",resultSetDetail.getString("WLO_CLOTH_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR5",resultSetDetail.getString("WLO_CLOTH_REPAIR5"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_CLOTH_REPAIR6",resultSetDetail.getString("WLO_CLOTH_REPAIR6"));
                
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR1",resultSetDetail.getString("WLO_SHUTTLE_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR2",resultSetDetail.getString("WLO_SHUTTLE_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR3",resultSetDetail.getString("WLO_SHUTTLE_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_SHUTTLE_REPAIR4",resultSetDetail.getString("WLO_SHUTTLE_REPAIR4"));
                
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR1",resultSetDetail.getString("WLO_PICKING_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR2",resultSetDetail.getString("WLO_PICKING_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR3",resultSetDetail.getString("WLO_PICKING_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR4",resultSetDetail.getString("WLO_PICKING_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR5",resultSetDetail.getString("WLO_PICKING_REPAIR5"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR6",resultSetDetail.getString("WLO_PICKING_REPAIR6"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR7",resultSetDetail.getString("WLO_PICKING_REPAIR7"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_PICKING_REPAIR8",resultSetDetail.getString("WLO_PICKING_REPAIR8"));
                
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR1",resultSetDetail.getString("WLO_WARP_END_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR2",resultSetDetail.getString("WLO_WARP_END_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR3",resultSetDetail.getString("WLO_WARP_END_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR4",resultSetDetail.getString("WLO_WARP_END_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_WARP_END_REPAIR5",resultSetDetail.getString("WLO_WARP_END_REPAIR5"));
                
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR1",resultSetDetail.getString("WLO_TEMPLE_REPAIR1"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR2",resultSetDetail.getString("WLO_TEMPLE_REPAIR2"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR3",resultSetDetail.getString("WLO_TEMPLE_REPAIR3"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR4",resultSetDetail.getString("WLO_TEMPLE_REPAIR4"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_TEMPLE_REPAIR5",resultSetDetail.getString("WLO_TEMPLE_REPAIR5"));
                
                ObjFeltWeavingLoomDetails.setAttribute("WLO_MISC_TIME",resultSetDetail.getString("WLO_MISC_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LOSS_TIME",resultSetDetail.getString("WLO_LOSS_TIME"));
                ObjFeltWeavingLoomDetails.setAttribute("WLO_LOSS_TOTAL",resultSetDetail.getString("WLO_LOSS_TOTAL"));
                
                hmFeltWvgLoomEffDetails.put(Integer.toString(serialNoCounter),ObjFeltWeavingLoomDetails);
            }
            resultSetDetail.close();
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
        String stringProductionDate1 = EITLERPGLOBAL.formatDateDB(stringProductionDate);
        
        
        try {
            stTmp=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            //            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_WVG_LOOM_EFF_H WHERE WLO_WEAVING_DATE='"+ stringProductionDate1+"' AND WLO_WVGPROD_NO='"+productionDocumentNo+"' GROUP BY REVISION_NO");
            rsTmp=stTmp.executeQuery("SELECT DISTINCT REVISION_NO,UPDATED_BY,MAX(ENTRY_DATE)AS ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,FROM_IP FROM PRODUCTION.FELT_WVG_LOOM_EFF_H WHERE WLO_WEAVING_DATE='"+ stringProductionDate1+"' AND WLO_WVGPROD_NO='"+productionDocumentNo+"'GROUP BY REVISION_NO,UPDATED_BY,APPROVAL_STATUS,APPROVER_REMARKS");
            
            while(rsTmp.next()) {
                clsFeltWeavingLoom objFeltWeavingLoom = new clsFeltWeavingLoom();
                
                objFeltWeavingLoom.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                objFeltWeavingLoom.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                objFeltWeavingLoom.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                objFeltWeavingLoom.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                objFeltWeavingLoom.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                objFeltWeavingLoom.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                
                hmHistoryList.put(Integer.toString(hmHistoryList.size()+1),objFeltWeavingLoom);
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
    
    public boolean ShowHistory(String pProductionDate,String pDocNo) {
        Ready=false;
        try {
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            resultSet=statement.executeQuery("SELECT DISTINCT REVISION_NO FROM PRODUCTION.FELT_WVG_LOOM_EFF_H WHERE WLO_WEAVING_DATE='"+ pProductionDate+"' AND WLO_WVGPROD_NO ='"+pDocNo+"'");
            Ready=true;
            historyWvgDate = pProductionDate;
            historyWvgProdNo = pDocNo;
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
                strSQL="SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_LOOM_EFF,PRODUCTION.FELT_PROD_DOC_DATA WHERE WLO_WVGPROD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=723 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_LOOM_EFF,PRODUCTION.FELT_PROD_DOC_DATA WHERE WLO_WVGPROD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=723 AND CANCELED=0 ORDER BY WLO_WEAVING_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_LOOM_EFF,PRODUCTION.FELT_PROD_DOC_DATA WHERE WLO_WVGPROD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=723 AND CANCELED=0 ORDER BY WLO_WVGPROD_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                
                Counter=Counter+1;
                clsFeltWeavingLoom ObjDoc=new clsFeltWeavingLoom();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("WLO_WVGPROD_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("WLO_WEAVING_DATE"));
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
    
   public static HashMap getPendingApprovals(int pUserID,int pOrder,int FinYearFrom) {
        String strSQL="";
        
        HashMap List=new HashMap();
        int Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_LOOM_EFF,PRODUCTION.FELT_PROD_DOC_DATA WHERE WLO_WVGPROD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=723 AND CANCELED=0 ORDER BY RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_LOOM_EFF,PRODUCTION.FELT_PROD_DOC_DATA WHERE WLO_WVGPROD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=723 AND CANCELED=0 ORDER BY WLO_WEAVING_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT DISTINCT WLO_WVGPROD_NO,WLO_WEAVING_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_WVG_LOOM_EFF,PRODUCTION.FELT_PROD_DOC_DATA WHERE WLO_WVGPROD_NO=DOC_NO AND USER_ID="+pUserID+" AND STATUS='W' AND MODULE_ID=723 AND CANCELED=0 ORDER BY WLO_WVGPROD_NO";
            }
            
            ResultSet rsTmp=data.getConn().createStatement().executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("WLO_WEAVING_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsFeltWeavingLoom ObjDoc=new clsFeltWeavingLoom();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("WLO_WVGPROD_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("WLO_WEAVING_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Integer.toString(Counter),ObjDoc);
                }
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
    
    public boolean checkProductionDocumentNoInDB(String pProdDocNo){
        int count=data.getIntValueFromDB("SELECT COUNT(WLO_WVGPROD_NO) FROM PRODUCTION.FELT_WVG_LOOM_EFF WHERE WLO_WVGPROD_NO='"+pProdDocNo+"'");
        if(count>0) return true;
        else return false;
    }
    
    
    
}
