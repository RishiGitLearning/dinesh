/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Purchase.*;
import EITLERP.Stores.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsItemAmend {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    public HashMap colRNDApprovers=new HashMap();
    
    private HashMap props;
    public boolean Ready = false;
    
    //History Related properties
    private boolean HistoryView=false;
    
    
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
    
    
    /** Creates new clsBusinessObject */
    public clsItemAmend() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("AMEND_ID",new Variant(0));
        props.put("AMEND_DATE",new Variant(""));
        props.put("AMEND_REASON",new Variant(""));
        props.put("ITEM_ID",new Variant(""));
        props.put("ITEM_DESCRIPTION",new Variant(""));
        props.put("GROUP_CODE",new Variant(0));
        props.put("SEARCH_KEY",new Variant(""));
        props.put("WAREHOUSE_ID",new Variant(""));
        props.put("CATEGORY_ID",new Variant(0));
        props.put("LOCATION_ID",new Variant(""));
        props.put("DESC",new Variant(0));
        props.put("MAKE",new Variant(0));
        props.put("SIZE",new Variant(0));
        props.put("ABC",new Variant(""));
        props.put("XYZ",new Variant(""));
        props.put("VEN",new Variant(""));
        props.put("FSN",new Variant(""));
        props.put("MF",new Variant(""));
        props.put("MAINTAIN_STOCK",new Variant(false));
        props.put("AVG_QTY",new Variant(0));
        props.put("MIN_QTY",new Variant(0));
        props.put("MAX_QTY",new Variant(0));
        props.put("UNIT",new Variant(0));
        props.put("UNIT_RATE",new Variant(0));
        props.put("DNP",new Variant(0));
        props.put("AVG_RATE",new Variant(0));
        props.put("QTR1_RATE",new Variant(0));
        props.put("QTR2_RATE",new Variant(0));
        props.put("QTR3_RATE",new Variant(0));
        props.put("QTR4_RATE",new Variant(0));
        props.put("REBATE",new Variant(0));
        
        props.put("TAX_CODE_TYPE",new Variant(""));
        props.put("HSN_SAC_CODE",new Variant(""));
        
        props.put("EXCISE_APPLICABLE",new Variant(false));
        props.put("EXCISE",new Variant(0));
        props.put("CHAPTER_NO",new Variant(0));
        props.put("TAXABLE",new Variant(false));
        props.put("OPENING_QTY",new Variant(0));
        props.put("OPENING_VALUE",new Variant(0));
        props.put("TOTAL_RECEIPT_QTY",new Variant(0));
        props.put("TOTAL_ISSUED_QTY",new Variant(0));
        props.put("LAST_RECEIPT_DATE",new Variant(""));
        props.put("LAST_ISSUED_DATE",new Variant(""));
        props.put("ZERO_RECEIPT_QTY",new Variant(0));
        props.put("ZERO_ISSUED_QTY",new Variant(0));
        props.put("ZERO_VALUE_QTY", new Variant(0));
        props.put("REJECTED_QTY",new Variant(0));
        props.put("ON_HAND_QTY",new Variant(0));
        props.put("AVAILABLE_QTY",new Variant(0));
        props.put("ALLOCATED_QTY",new Variant(0));
        props.put("LAST_TRANS_DATE",new Variant(0));
        props.put("LAST_GRN_NO",new Variant(""));
        props.put("LAST_GRN_DATE",new Variant(""));
        props.put("LAST_PO_NO",new Variant(""));
        props.put("LAST_PO_DATE",new Variant(""));
        props.put("CAPTIVE_CONSUMABLE",new Variant(false));
        props.put("BLOCKED",new Variant(false));
        props.put("REORDER_LEVEL",new Variant(0));
        props.put("SUPPLIER_ID",new Variant(0));
        props.put("ITEM_HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("TOLERANCE_LIMIT",new Variant(0));
        props.put("SPECIAL_APPROVAL",new Variant(""));
        props.put("ONETIME",new Variant(false));
        props.put("RND_APPROVAL",new Variant(false));
        props.put("CANCELLED",new Variant(false));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            HistoryView=false;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+Long.toString(pCompanyID));
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        }
        catch(Exception e) {
            
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsResultSet.isAfterLast()||rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            }
            else {
                rsResultSet.next();
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            }
            else {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    
    
    public boolean Insert() {
        Statement stTmp,stHistory,stRND,stRNDH,stOItem,stORND;
        ResultSet rsTmp,rsHistory,rsRND,rsRNDH,rsOItem,rsORND;
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRND=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRNDH=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_AMEND_H WHERE ITEM_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_AMEND WHERE ITEM_ID='1'");
            rsTmp.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate new system id ------------
            setAttribute("AMEND_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_INV_ITEM_MASTER_AMEND","AMEND_ID"));
            //-------------------------------------------------
            
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                String ItemID=(String)getAttribute("ITEM_ID").getObj();
                clsItem ObjItem=(clsItem)clsItem.getObjectEx(EITLERPGLOBAL.gCompanyID,ItemID);
                
                
                //Delete previous data
                data.Execute("DELETE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ItemID+"'");
                data.Execute("DELETE FROM D_INV_ITEM_RND_APPROVERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_SYS_ID="+(long)ObjItem.getAttribute("ITEM_SYS_ID").getVal()); 
                
                
                stOItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOItem=stOItem.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE ITEM_ID='1'");
                rsOItem.first();
                
                rsOItem.moveToInsertRow();
                rsOItem.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsOItem.updateLong("ITEM_SYS_ID",(long)ObjItem.getAttribute("ITEM_SYS_ID").getVal());
                rsOItem.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
                rsOItem.updateBoolean("ONETIME",getAttribute("ONETIME").getBool());
                rsOItem.updateBoolean("RND_APPROVAL",getAttribute("RND_APPROVAL").getBool());
                rsOItem.updateString("ITEM_DESCRIPTION",(String)getAttribute("ITEM_DESCRIPTION").getObj());
                rsOItem.updateLong("GROUP_CODE",(long)getAttribute("GROUP_CODE").getVal());
                rsOItem.updateString("SEARCH_KEY",(String)getAttribute("SEARCH_KEY").getObj());
                rsOItem.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
                rsOItem.updateLong("CATEGORY_ID",(long)getAttribute("CATEGORY_ID").getVal());
                rsOItem.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
                rsOItem.updateLong("DESC",(long)getAttribute("DESC").getVal());
                rsOItem.updateLong("MAKE",(long)getAttribute("MAKE").getVal());
                rsOItem.updateLong("SIZE",(long)getAttribute("SIZE").getVal());
                rsOItem.updateString("ABC",(String)getAttribute("ABC").getObj());
                rsOItem.updateString("XYZ",(String)getAttribute("XYZ").getObj());
                rsOItem.updateString("VEN",(String)getAttribute("VEN").getObj());
                rsOItem.updateString("FSN",(String)getAttribute("FSN").getObj());
                rsOItem.updateString("MF",(String)getAttribute("MF").getObj());
                rsOItem.updateBoolean("MAINTAIN_STOCK",(boolean)getAttribute("MAINTAIN_STOCK").getBool());
                rsOItem.updateDouble("MIN_QTY",(double)getAttribute("MIN_QTY").getVal());
                rsOItem.updateDouble("MAX_QTY",(double)getAttribute("MAX_QTY").getVal());
                rsOItem.updateDouble("TOLERANCE_LIMIT",(double)getAttribute("TOLERANCE_LIMIT").getVal());
                rsOItem.updateLong("UNIT",(long)getAttribute("UNIT").getVal());
                rsOItem.updateDouble("UNIT_RATE",(double)getAttribute("UNIT_RATE").getVal());
                rsOItem.updateDouble("DNP",(double)getAttribute("DNP").getVal());
                rsOItem.updateDouble("QTR1_RATE",(double)getAttribute("QTR1_RATE").getVal());
                rsOItem.updateDouble("QTR2_RATE",(double)getAttribute("QTR2_RATE").getVal());
                rsOItem.updateDouble("QTR3_RATE",(double)getAttribute("QTR3_RATE").getVal());
                rsOItem.updateDouble("QTR4_RATE",(double)getAttribute("QTR4_RATE").getVal());
                rsOItem.updateDouble("REBATE",(double)getAttribute("REBATE").getVal());
                
                rsOItem.updateString("TAX_CODE_TYPE",(String)getAttribute("TAX_CODE_TYPE").getObj());
                rsOItem.updateString("HSN_SAC_CODE",(String)getAttribute("HSN_SAC_CODE").getObj());
                
                rsOItem.updateBoolean("EXCISE_APPLICABLE",(boolean)getAttribute("EXCISE_APPLICABLE").getBool());
                rsOItem.updateDouble("EXCISE",(double)getAttribute("EXCISE").getVal());
                rsOItem.updateLong("CHAPTER_NO",(long)getAttribute("CHAPTER_NO").getVal());
                rsOItem.updateBoolean("TAXABLE",(boolean)getAttribute("TAXABLE").getBool());
                rsOItem.updateBoolean("CAPTIVE_CONSUMABLE",(boolean)getAttribute("CAPTIVE_CONSUMABLE").getBool());
                rsOItem.updateBoolean("BLOCKED",(boolean)getAttribute("BLOCKED").getBool());
                rsOItem.updateDouble("REORDER_LEVEL",(double)getAttribute("REORDER_LEVEL").getVal());
                rsOItem.updateLong("SUPPLIER_ID",(long)getAttribute("SUPPLIER_ID").getVal());
                rsOItem.updateLong("ITEM_HIERARCHY_ID",(long)getAttribute("ITEM_HIERARCHY_ID").getVal());
                rsOItem.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsOItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsOItem.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsOItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsOItem.updateString("SPECIAL_APPROVAL",(String)getAttribute("SPECIAL_APPROVAL").getObj());
                rsOItem.updateBoolean("CHANGED",true);
                rsOItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOItem.updateBoolean("APPROVED",true);
                rsOItem.updateString("APPROVED_DATE",(String)ObjItem.getAttribute("APPROVED_DATE").getObj());
                rsOItem.updateBoolean("REJECTED",ObjItem.getAttribute("REJECTED").getBool());
                rsOItem.updateString("REJECTED_DATE",(String)ObjItem.getAttribute("REJECTED_DATE").getObj());
                rsOItem.updateBoolean("CANCELLED",false);
                rsOItem.insertRow();
                
                
                stORND=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsORND=stORND.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS WHERE ITEM_SYS_ID=1");
                rsORND.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjItems=(clsItemRNDApprover) colRNDApprovers.get(Integer.toString(i));
                    
                    rsORND.moveToInsertRow();
                    rsORND.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsORND.updateLong("ITEM_SYS_ID",(long)ObjItem.getAttribute("ITEM_SYS_ID").getVal());
                    rsORND.updateInt("SR_NO",i);
                    rsORND.updateInt("USER_ID",(int)ObjItems.getAttribute("USER_ID").getVal());
                    rsORND.updateBoolean("CHANGED",true);
                    rsORND.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsORND.insertRow();
                }
                

                
                
                setAttribute("COMPANY_ID",ObjItem.getAttribute("COMPANY_ID").getVal());
                setAttribute("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                setAttribute("ITEM_DESCRIPTION",(String)ObjItem.getAttribute("ITEM_DESCRIPTION").getObj());
                setAttribute("ONETIME",ObjItem.getAttribute("ONETIME").getBool());
                setAttribute("RND_APPROVAL",ObjItem.getAttribute("RND_APPROVAL").getBool());
                setAttribute("GROUP_CODE",(int)ObjItem.getAttribute("GROUP_CODE").getVal());
                setAttribute("SEARCH_KEY",(String)ObjItem.getAttribute("SEARCH_KEY").getObj());
                setAttribute("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                setAttribute("CATEGORY_ID",(int)ObjItem.getAttribute("CATEGORY_ID").getVal());
                setAttribute("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                setAttribute("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                setAttribute("DESC",(int)ObjItem.getAttribute("DESC").getVal());
                setAttribute("MAKE",(int)ObjItem.getAttribute("MAKE").getVal());
                setAttribute("SIZE",(int)ObjItem.getAttribute("SIZE").getVal());
                setAttribute("ABC",(String)ObjItem.getAttribute("ABC").getObj());
                setAttribute("XYZ",(String)ObjItem.getAttribute("XYZ").getObj());
                setAttribute("VEN",(String)ObjItem.getAttribute("VEN").getObj());
                setAttribute("FSN",(String)ObjItem.getAttribute("FSN").getObj());
                setAttribute("MF",(String)ObjItem.getAttribute("MF").getObj());
                setAttribute("MAINTAIN_STOCK",ObjItem.getAttribute("MAINTAIN_STOCK").getBool());
                setAttribute("AVG_QTY",ObjItem.getAttribute("AVG_QTY").getVal());
                setAttribute("MIN_QTY",ObjItem.getAttribute("MIN_QTY").getVal());
                setAttribute("MAX_QTY",ObjItem.getAttribute("MAX_QTY").getVal());
                setAttribute("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                setAttribute("DNP",ObjItem.getAttribute("DNP").getVal());
                setAttribute("UNIT_RATE",ObjItem.getAttribute("UNIT_RATE").getVal());
                setAttribute("QTR1_RATE",ObjItem.getAttribute("QTR1_RATE").getVal());
                setAttribute("QTR2_RATE",ObjItem.getAttribute("QTR2_RATE").getVal());
                setAttribute("QTR3_RATE",ObjItem.getAttribute("QTR3_RATE").getVal());
                setAttribute("QTR4_RATE",ObjItem.getAttribute("QTR4_RATE").getVal());
                setAttribute("REBATE",ObjItem.getAttribute("REBATE").getVal());
                
                setAttribute("TAX_CODE_TYPE",(String)ObjItem.getAttribute("TAX_CODE_TYPE").getObj());
                setAttribute("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                
                setAttribute("EXCISE_APPLICABLE",ObjItem.getAttribute("EXCISE_APPLICABLE").getBool());
                setAttribute("EXCISE",ObjItem.getAttribute("EXCISE").getVal());
                setAttribute("CHAPTER_NO",(int)ObjItem.getAttribute("CHAPTER_NO").getVal());
                setAttribute("TAXABLE",ObjItem.getAttribute("TAXABLE").getBool());
                setAttribute("OPENING_QTY",ObjItem.getAttribute("OPENING_QTY").getVal());
                setAttribute("OPENING_VALUE",ObjItem.getAttribute("OPENING_VALUE").getVal());
                setAttribute("TOTAL_RECEIPT_QTY",ObjItem.getAttribute("TOTAL_RECEIPT_QTY").getVal());
                setAttribute("TOTAL_ISSUED_QTY",ObjItem.getAttribute("TOTAL_ISSUED_QTY").getVal());
                setAttribute("LAST_RECEIPT_DATE",(String)ObjItem.getAttribute("LAST_RECEIPT_DATE").getObj());
                setAttribute("LAST_ISSUED_DATE",(String)ObjItem.getAttribute("LAST_ISSUED_DATE").getObj());
                setAttribute("ZERO_RECEIPT_QTY",ObjItem.getAttribute("ZERO_RECEIPT_QTY").getVal());
                setAttribute("ZERO_ISSUED_QTY",ObjItem.getAttribute("ZERO_ISSUED_QTY").getVal());
                setAttribute("ZERO_VALUE_QTY",ObjItem.getAttribute("ZERO_VALUE_QTY").getVal());
                setAttribute("REJECTED_QTY",ObjItem.getAttribute("REJECTED_QTY").getVal());
                setAttribute("ON_HAND_QTY",ObjItem.getAttribute("ON_HAND_QTY").getVal());
                setAttribute("AVAILABLE_QTY",ObjItem.getAttribute("AVAILABLE_QTY").getVal());
                setAttribute("ALLOCATED_QTY",ObjItem.getAttribute("ALLOCATED_QTY").getVal());
                setAttribute("LAST_TRANS_DATE",(String)ObjItem.getAttribute("LAST_TRANS_DATE").getObj());
                setAttribute("LAST_GRN_NO",(String)ObjItem.getAttribute("LAST_GRN_NO").getObj());
                setAttribute("LAST_GRN_DATE",(String)ObjItem.getAttribute("LAST_GRN_DATE").getObj());
                setAttribute("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                setAttribute("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                setAttribute("CAPTIVE_CONSUMABLE",ObjItem.getAttribute("CAPTIVE_CONSUMABLE").getBool());
                setAttribute("BLOCKED",(String)ObjItem.getAttribute("BLOCKED").getObj());
                setAttribute("REORDER_LEVEL",ObjItem.getAttribute("REORDER_LEVEL").getVal());
                setAttribute("SUPPLIER_ID",(int)ObjItem.getAttribute("SUPPLIER_ID").getVal());
                //setAttribute("ITEM_HIERARCHY_ID",(int)ObjItem.getAttribute("ITEM_HIERARCHY_ID").getVal());
                setAttribute("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                setAttribute("APPROVED_DATE",(String)ObjItem.getAttribute("APPROVED_DATE").getObj());
                setAttribute("REJECTED",ObjItem.getAttribute("REJECTED").getBool());
                setAttribute("REJECTED_DATE",(String)ObjItem.getAttribute("REJECTED_DATE").getObj());
                setAttribute("CREATED_BY",(int)ObjItem.getAttribute("CREATED_BY").getVal());
                setAttribute("CREATED_DATE",(String)ObjItem.getAttribute("CREATED_DATE").getObj());
                setAttribute("MODIFIED_BY",(int)ObjItem.getAttribute("MODIFIED_BY").getVal());
                setAttribute("MODIFIED_DATE",(String)ObjItem.getAttribute("MODIFIED_DATE").getObj());
                setAttribute("SPECIAL_APPROVAL",(String)ObjItem.getAttribute("SPECIAL_APPROVAL").getObj());
                
                // ----------------- End of Header Fields ------------------------------------//
                colRNDApprovers.clear();
                
                for(int i=1;i<=ObjItem.colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjRND=(clsItemRNDApprover)  ObjItem.colRNDApprovers.get(Integer.toString(i));
                    
                    clsItemRNDApprover NewItem=new clsItemRNDApprover();
                    
                    NewItem.setAttribute("COMPANY_ID",(int)ObjRND.getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("AMEND_ID",(int)ObjRND.getAttribute("AMEND_ID").getVal());
                    NewItem.setAttribute("SR_NO",(int)ObjRND.getAttribute("SR_NO").getVal());
                    NewItem.setAttribute("USER_ID",(int)ObjRND.getAttribute("USER_ID").getVal());
                    
                    colRNDApprovers.put(Integer.toString(colRNDApprovers.size()+1),NewItem);
                }
                
                
                //==========Update Location in Stock Table, if location has changed =============//
                ItemID=(String)getAttribute("ITEM_ID").getObj();
                String NewLocationID=(String)getAttribute("LOCATION_ID").getObj();
                data.Execute("UPDATE D_INV_ITEM_LOT_MASTER SET LOCATION_ID='"+NewLocationID+"' WHERE ITEM_ID='"+ItemID+"'");
                //==============================================================================//
                
                
            }
            
            
            
            //----------- Save Header Record ----------------
            rsTmp.moveToInsertRow();
            rsTmp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsTmp.updateLong("AMEND_ID",(long)getAttribute("AMEND_ID").getVal());
            rsTmp.updateString("AMEND_DATE",(String)getAttribute("AMEND_DATE").getObj());
            rsTmp.updateString("AMEND_REASON",(String)getAttribute("AMEND_REASON").getObj());
            rsTmp.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsTmp.updateBoolean("ONETIME",getAttribute("ONETIME").getBool());
            rsTmp.updateBoolean("RND_APPROVAL",getAttribute("RND_APPROVAL").getBool());
            rsTmp.updateString("ITEM_DESCRIPTION",(String)getAttribute("ITEM_DESCRIPTION").getObj());
            rsTmp.updateLong("GROUP_CODE",(long)getAttribute("GROUP_CODE").getVal());
            rsTmp.updateString("SEARCH_KEY",(String)getAttribute("SEARCH_KEY").getObj());
            rsTmp.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
            rsTmp.updateLong("CATEGORY_ID",(long)getAttribute("CATEGORY_ID").getVal());
            rsTmp.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
            rsTmp.updateLong("DESC",(long)getAttribute("DESC").getVal());
            rsTmp.updateLong("MAKE",(long)getAttribute("MAKE").getVal());
            rsTmp.updateLong("SIZE",(long)getAttribute("SIZE").getVal());
            rsTmp.updateString("ABC",(String)getAttribute("ABC").getObj());
            rsTmp.updateString("XYZ",(String)getAttribute("XYZ").getObj());
            rsTmp.updateString("VEN",(String)getAttribute("VEN").getObj());
            rsTmp.updateString("FSN",(String)getAttribute("FSN").getObj());
            rsTmp.updateString("MF",(String)getAttribute("MF").getObj());
            rsTmp.updateBoolean("MAINTAIN_STOCK",(boolean)getAttribute("MAINTAIN_STOCK").getBool());
            rsTmp.updateDouble("MIN_QTY",(double)getAttribute("MIN_QTY").getVal());
            rsTmp.updateDouble("MAX_QTY",(double)getAttribute("MAX_QTY").getVal());
            rsTmp.updateDouble("TOLERANCE_LIMIT",(double)getAttribute("TOLERANCE_LIMIT").getVal());
            rsTmp.updateLong("UNIT",(long)getAttribute("UNIT").getVal());
            rsTmp.updateDouble("UNIT_RATE",(double)getAttribute("UNIT_RATE").getVal());
            rsTmp.updateDouble("DNP",(double)getAttribute("DNP").getVal());
            rsTmp.updateDouble("QTR1_RATE",(double)getAttribute("QTR1_RATE").getVal());
            rsTmp.updateDouble("QTR2_RATE",(double)getAttribute("QTR2_RATE").getVal());
            rsTmp.updateDouble("QTR3_RATE",(double)getAttribute("QTR3_RATE").getVal());
            rsTmp.updateDouble("QTR4_RATE",(double)getAttribute("QTR4_RATE").getVal());
            rsTmp.updateDouble("REBATE",(double)getAttribute("REBATE").getVal());
            
            rsTmp.updateString("TAX_CODE_TYPE",(String)getAttribute("TAX_CODE_TYPE").getObj());
            rsTmp.updateString("HSN_SAC_CODE",(String)getAttribute("HSN_SAC_CODE").getObj());
            
            rsTmp.updateBoolean("EXCISE_APPLICABLE",(boolean)getAttribute("EXCISE_APPLICABLE").getBool());
            rsTmp.updateDouble("EXCISE",(double)getAttribute("EXCISE").getVal());
            rsTmp.updateLong("CHAPTER_NO",(long)getAttribute("CHAPTER_NO").getVal());
            rsTmp.updateBoolean("TAXABLE",(boolean)getAttribute("TAXABLE").getBool());
            rsTmp.updateBoolean("CAPTIVE_CONSUMABLE",(boolean)getAttribute("CAPTIVE_CONSUMABLE").getBool());
            rsTmp.updateBoolean("BLOCKED",(boolean)getAttribute("BLOCKED").getBool());
            rsTmp.updateDouble("REORDER_LEVEL",(double)getAttribute("REORDER_LEVEL").getVal());
            rsTmp.updateLong("SUPPLIER_ID",(long)getAttribute("SUPPLIER_ID").getVal());
            rsTmp.updateLong("ITEM_HIERARCHY_ID",(long)getAttribute("ITEM_HIERARCHY_ID").getVal());
            rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsTmp.updateString("SPECIAL_APPROVAL",(String)getAttribute("SPECIAL_APPROVAL").getObj());
            rsTmp.updateBoolean("APPROVED",false);
            rsTmp.updateBoolean("CANCELLED",false);
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.insertRow();
            
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("AMEND_ID",(long)getAttribute("AMEND_ID").getVal());
            rsHistory.updateString("AMEND_DATE",(String)getAttribute("AMEND_DATE").getObj());
            rsHistory.updateString("AMEND_REASON",(String)getAttribute("AMEND_REASON").getObj());
            rsHistory.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsHistory.updateString("ITEM_DESCRIPTION",(String)getAttribute("ITEM_DESCRIPTION").getObj());
            rsHistory.updateBoolean("ONETIME",getAttribute("ONETIME").getBool());
            rsHistory.updateBoolean("RND_APPROVAL",getAttribute("RND_APPROVAL").getBool());
            rsHistory.updateLong("GROUP_CODE",(long)getAttribute("GROUP_CODE").getVal());
            rsHistory.updateString("SEARCH_KEY",(String)getAttribute("SEARCH_KEY").getObj());
            rsHistory.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
            rsHistory.updateLong("CATEGORY_ID",(long)getAttribute("CATEGORY_ID").getVal());
            rsHistory.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
            rsHistory.updateLong("DESC",(long)getAttribute("DESC").getVal());
            rsHistory.updateLong("MAKE",(long)getAttribute("MAKE").getVal());
            rsHistory.updateLong("SIZE",(long)getAttribute("SIZE").getVal());
            rsHistory.updateString("ABC",(String)getAttribute("ABC").getObj());
            rsHistory.updateString("XYZ",(String)getAttribute("XYZ").getObj());
            rsHistory.updateString("VEN",(String)getAttribute("VEN").getObj());
            rsHistory.updateString("FSN",(String)getAttribute("FSN").getObj());
            rsHistory.updateString("MF",(String)getAttribute("MF").getObj());
            rsHistory.updateBoolean("MAINTAIN_STOCK",(boolean)getAttribute("MAINTAIN_STOCK").getBool());
            rsHistory.updateDouble("MIN_QTY",(double)getAttribute("MIN_QTY").getVal());
            rsHistory.updateDouble("MAX_QTY",(double)getAttribute("MAX_QTY").getVal());
            rsHistory.updateDouble("TOLERANCE_LIMIT",(double)getAttribute("TOLERANCE_LIMIT").getVal());
            rsHistory.updateLong("UNIT",(long)getAttribute("UNIT").getVal());
            rsHistory.updateDouble("UNIT_RATE",(double)getAttribute("UNIT_RATE").getVal());
            rsHistory.updateDouble("DNP",(double)getAttribute("DNP").getVal());
            rsHistory.updateDouble("QTR1_RATE",(double)getAttribute("QTR1_RATE").getVal());
            rsHistory.updateDouble("QTR2_RATE",(double)getAttribute("QTR2_RATE").getVal());
            rsHistory.updateDouble("QTR3_RATE",(double)getAttribute("QTR3_RATE").getVal());
            rsHistory.updateDouble("QTR4_RATE",(double)getAttribute("QTR4_RATE").getVal());
            rsHistory.updateDouble("REBATE",(double)getAttribute("REBATE").getVal());
            
            rsHistory.updateString("TAX_CODE_TYPE",(String)getAttribute("TAX_CODE_TYPE").getObj());
            rsHistory.updateString("HSN_SAC_CODE",(String)getAttribute("HSN_SAC_CODE").getObj());
            
            rsHistory.updateBoolean("EXCISE_APPLICABLE",(boolean)getAttribute("EXCISE_APPLICABLE").getBool());
            rsHistory.updateDouble("EXCISE",(double)getAttribute("EXCISE").getVal());
            rsHistory.updateLong("CHAPTER_NO",(long)getAttribute("CHAPTER_NO").getVal());
            rsHistory.updateBoolean("TAXABLE",(boolean)getAttribute("TAXABLE").getBool());
            rsHistory.updateBoolean("CAPTIVE_CONSUMABLE",(boolean)getAttribute("CAPTIVE_CONSUMABLE").getBool());
            rsHistory.updateBoolean("BLOCKED",(boolean)getAttribute("BLOCKED").getBool());
            rsHistory.updateDouble("REORDER_LEVEL",(double)getAttribute("REORDER_LEVEL").getVal());
            rsHistory.updateLong("SUPPLIER_ID",(long)getAttribute("SUPPLIER_ID").getVal());
            rsHistory.updateLong("ITEM_HIERARCHY_ID",(long)getAttribute("ITEM_HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateString("SPECIAL_APPROVAL",(String)getAttribute("SPECIAL_APPROVAL").getObj());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            
            //=========Now Updating RND Approvers -----------------//
            rsRND=stRND.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_AMEND");
            rsRNDH=stRNDH.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_AMEND_H");
            
            //Only Update the records if RND Approval Required
            if(getAttribute("RND_APPROVAL").getBool()) {
                for(int i=1;i<=colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjApprover=(clsItemRNDApprover)colRNDApprovers.get(Integer.toString(i));
                    
                    rsRND.moveToInsertRow();
                    rsRND.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRND.updateLong("AMEND_ID",(long)getAttribute("AMEND_ID").getVal());
                    rsRND.updateInt("SR_NO",i);
                    rsRND.updateInt("USER_ID",(int)ObjApprover.getAttribute("USER_ID").getVal());
                    rsRND.updateBoolean("CHANGED",true);
                    rsRND.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsRND.insertRow();
                    
                    rsRNDH.moveToInsertRow();
                    rsRNDH.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRNDH.updateLong("AMEND_ID",(long)getAttribute("AMEND_ID").getVal());
                    rsRNDH.updateInt("SR_NO",i);
                    rsRNDH.updateInt("REVISION_NO",1);
                    rsRNDH.updateInt("USER_ID",(int)ObjApprover.getAttribute("USER_ID").getVal());
                    rsRNDH.updateBoolean("CHANGED",true);
                    rsRNDH.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsRNDH.insertRow();
                }
            }
            //=============================================================//
            
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=51; //Item Master
            ObjFlow.DocNo=Integer.toString((int)getAttribute("AMEND_ID").getVal());
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_ITEM_MASTER_AMEND";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("ITEM_HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="AMEND_ID";
            
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    
    //Updates current record
    public boolean Update() {
        Statement stTmp,stHistory,stRND,stRNDH,stOItem,stORND;
        ResultSet rsTmp,rsHistory,rsRND,rsRNDH,rsOItem,rsORND;
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRND=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRNDH=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_AMEND_H WHERE ITEM_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Check for the Duplication of Login ID ------
            long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            long lItemSysID=(long) getAttribute("AMEND_ID").getVal();
            //------- Duplication check over ----------------------
            
            
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                String ItemID=(String)getAttribute("ITEM_ID").getObj();
                clsItem ObjItem=(clsItem)clsItem.getObjectEx(EITLERPGLOBAL.gCompanyID,ItemID);
                
                
                //Delete previous data
                data.Execute("DELETE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ItemID+"'");
                data.Execute("DELETE FROM D_INV_ITEM_RND_APPROVERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_SYS_ID="+(long)ObjItem.getAttribute("ITEM_SYS_ID").getVal()); 
                
                
                stOItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOItem=stOItem.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE ITEM_ID='1'");
                rsOItem.first();
                
                rsOItem.moveToInsertRow();
                rsOItem.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsOItem.updateLong("ITEM_SYS_ID",(long)ObjItem.getAttribute("ITEM_SYS_ID").getVal());
                rsOItem.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
                rsOItem.updateBoolean("ONETIME",getAttribute("ONETIME").getBool());
                rsOItem.updateBoolean("RND_APPROVAL",getAttribute("RND_APPROVAL").getBool());
                rsOItem.updateString("ITEM_DESCRIPTION",(String)getAttribute("ITEM_DESCRIPTION").getObj());
                rsOItem.updateLong("GROUP_CODE",(long)getAttribute("GROUP_CODE").getVal());
                rsOItem.updateString("SEARCH_KEY",(String)getAttribute("SEARCH_KEY").getObj());
                rsOItem.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
                rsOItem.updateLong("CATEGORY_ID",(long)getAttribute("CATEGORY_ID").getVal());
                rsOItem.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
                rsOItem.updateLong("DESC",(long)getAttribute("DESC").getVal());
                rsOItem.updateLong("MAKE",(long)getAttribute("MAKE").getVal());
                rsOItem.updateLong("SIZE",(long)getAttribute("SIZE").getVal());
                rsOItem.updateString("ABC",(String)getAttribute("ABC").getObj());
                rsOItem.updateString("XYZ",(String)getAttribute("XYZ").getObj());
                rsOItem.updateString("VEN",(String)getAttribute("VEN").getObj());
                rsOItem.updateString("FSN",(String)getAttribute("FSN").getObj());
                rsOItem.updateString("MF",(String)getAttribute("MF").getObj());
                rsOItem.updateBoolean("MAINTAIN_STOCK",(boolean)getAttribute("MAINTAIN_STOCK").getBool());
                rsOItem.updateDouble("MIN_QTY",(double)getAttribute("MIN_QTY").getVal());
                rsOItem.updateDouble("MAX_QTY",(double)getAttribute("MAX_QTY").getVal());
                rsOItem.updateDouble("TOLERANCE_LIMIT",(double)getAttribute("TOLERANCE_LIMIT").getVal());
                rsOItem.updateLong("UNIT",(long)getAttribute("UNIT").getVal());
                rsOItem.updateDouble("UNIT_RATE",(double)getAttribute("UNIT_RATE").getVal());
                rsOItem.updateDouble("DNP",(double)getAttribute("DNP").getVal());
                rsOItem.updateDouble("QTR1_RATE",(double)getAttribute("QTR1_RATE").getVal());
                rsOItem.updateDouble("QTR2_RATE",(double)getAttribute("QTR2_RATE").getVal());
                rsOItem.updateDouble("QTR3_RATE",(double)getAttribute("QTR3_RATE").getVal());
                rsOItem.updateDouble("QTR4_RATE",(double)getAttribute("QTR4_RATE").getVal());
                rsOItem.updateDouble("REBATE",(double)getAttribute("REBATE").getVal());
                
                rsOItem.updateString("TAX_CODE_TYPE",(String)getAttribute("TAX_CODE_TYPE").getObj());
                rsOItem.updateString("HSN_SAC_CODE",(String)getAttribute("HSN_SAC_CODE").getObj());
                
                rsOItem.updateBoolean("EXCISE_APPLICABLE",(boolean)getAttribute("EXCISE_APPLICABLE").getBool());
                rsOItem.updateDouble("EXCISE",(double)getAttribute("EXCISE").getVal());
                rsOItem.updateLong("CHAPTER_NO",(long)getAttribute("CHAPTER_NO").getVal());
                rsOItem.updateBoolean("TAXABLE",(boolean)getAttribute("TAXABLE").getBool());
                rsOItem.updateBoolean("CAPTIVE_CONSUMABLE",(boolean)getAttribute("CAPTIVE_CONSUMABLE").getBool());
                rsOItem.updateBoolean("BLOCKED",(boolean)getAttribute("BLOCKED").getBool());
                rsOItem.updateDouble("REORDER_LEVEL",(double)getAttribute("REORDER_LEVEL").getVal());
                rsOItem.updateLong("SUPPLIER_ID",(long)getAttribute("SUPPLIER_ID").getVal());
                rsOItem.updateLong("ITEM_HIERARCHY_ID",(long)getAttribute("ITEM_HIERARCHY_ID").getVal());
                rsOItem.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsOItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsOItem.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsOItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsOItem.updateString("SPECIAL_APPROVAL",(String)getAttribute("SPECIAL_APPROVAL").getObj());
                rsOItem.updateBoolean("CHANGED",true);
                rsOItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOItem.updateBoolean("APPROVED",true);
                rsOItem.updateString("APPROVED_DATE",(String)ObjItem.getAttribute("APPROVED_DATE").getObj());
                rsOItem.updateBoolean("REJECTED",ObjItem.getAttribute("REJECTED").getBool());
                rsOItem.updateString("REJECTED_DATE",(String)ObjItem.getAttribute("REJECTED_DATE").getObj());
                rsOItem.updateBoolean("CANCELLED",false);
                rsOItem.insertRow();
                
                
                stORND=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsORND=stORND.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS WHERE ITEM_SYS_ID=1");
                rsORND.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjItems=(clsItemRNDApprover) colRNDApprovers.get(Integer.toString(i));
                    
                    rsORND.moveToInsertRow();
                    rsORND.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsORND.updateLong("ITEM_SYS_ID",(long)ObjItem.getAttribute("ITEM_SYS_ID").getVal());
                    rsORND.updateInt("SR_NO",i);
                    rsORND.updateInt("USER_ID",(int)ObjItems.getAttribute("USER_ID").getVal());
                    rsORND.updateBoolean("CHANGED",true);
                    rsORND.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsORND.insertRow();
                }
                
                //==========Update Location in Stock Table, if location has changed =============//
                ItemID=(String)getAttribute("ITEM_ID").getObj();
                String NewLocationID=(String)getAttribute("LOCATION_ID").getObj();
                data.Execute("UPDATE D_INV_ITEM_LOT_MASTER SET LOCATION_ID='"+NewLocationID+"' WHERE ITEM_ID='"+ItemID+"'");
                //==============================================================================//
                
                
                setAttribute("COMPANY_ID",ObjItem.getAttribute("COMPANY_ID").getVal());
                setAttribute("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                setAttribute("ITEM_DESCRIPTION",(String)ObjItem.getAttribute("ITEM_DESCRIPTION").getObj());
                setAttribute("ONETIME",ObjItem.getAttribute("ONETIME").getBool());
                setAttribute("RND_APPROVAL",ObjItem.getAttribute("RND_APPROVAL").getBool());
                setAttribute("GROUP_CODE",(int)ObjItem.getAttribute("GROUP_CODE").getVal());
                setAttribute("SEARCH_KEY",(String)ObjItem.getAttribute("SEARCH_KEY").getObj());
                setAttribute("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                setAttribute("CATEGORY_ID",(int)ObjItem.getAttribute("CATEGORY_ID").getVal());
                setAttribute("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                setAttribute("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                setAttribute("DESC",(int)ObjItem.getAttribute("DESC").getVal());
                setAttribute("MAKE",(int)ObjItem.getAttribute("MAKE").getVal());
                setAttribute("SIZE",(int)ObjItem.getAttribute("SIZE").getVal());
                setAttribute("ABC",(String)ObjItem.getAttribute("ABC").getObj());
                setAttribute("XYZ",(String)ObjItem.getAttribute("XYZ").getObj());
                setAttribute("VEN",(String)ObjItem.getAttribute("VEN").getObj());
                setAttribute("FSN",(String)ObjItem.getAttribute("FSN").getObj());
                setAttribute("MF",(String)ObjItem.getAttribute("MF").getObj());
                setAttribute("MAINTAIN_STOCK",ObjItem.getAttribute("MAINTAIN_STOCK").getBool());
                setAttribute("AVG_QTY",ObjItem.getAttribute("AVG_QTY").getVal());
                setAttribute("MIN_QTY",ObjItem.getAttribute("MIN_QTY").getVal());
                setAttribute("MAX_QTY",ObjItem.getAttribute("MAX_QTY").getVal());
                setAttribute("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                setAttribute("DNP",ObjItem.getAttribute("DNP").getVal());
                setAttribute("UNIT_RATE",ObjItem.getAttribute("UNIT_RATE").getVal());
                setAttribute("QTR1_RATE",ObjItem.getAttribute("QTR1_RATE").getVal());
                setAttribute("QTR2_RATE",ObjItem.getAttribute("QTR2_RATE").getVal());
                setAttribute("QTR3_RATE",ObjItem.getAttribute("QTR3_RATE").getVal());
                setAttribute("QTR4_RATE",ObjItem.getAttribute("QTR4_RATE").getVal());
                setAttribute("REBATE",ObjItem.getAttribute("REBATE").getVal());
                
                setAttribute("TAX_CODE_TYPE",(String)ObjItem.getAttribute("TAX_CODE_TYPE").getObj());
                setAttribute("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());                
                
                setAttribute("EXCISE_APPLICABLE",ObjItem.getAttribute("EXCISE_APPLICABLE").getBool());
                setAttribute("EXCISE",ObjItem.getAttribute("EXCISE").getVal());
                setAttribute("CHAPTER_NO",(int)ObjItem.getAttribute("CHAPTER_NO").getVal());
                setAttribute("TAXABLE",ObjItem.getAttribute("TAXABLE").getBool());
                setAttribute("OPENING_QTY",ObjItem.getAttribute("OPENING_QTY").getVal());
                setAttribute("OPENING_VALUE",ObjItem.getAttribute("OPENING_VALUE").getVal());
                setAttribute("TOTAL_RECEIPT_QTY",ObjItem.getAttribute("TOTAL_RECEIPT_QTY").getVal());
                setAttribute("TOTAL_ISSUED_QTY",ObjItem.getAttribute("TOTAL_ISSUED_QTY").getVal());
                setAttribute("LAST_RECEIPT_DATE",(String)ObjItem.getAttribute("LAST_RECEIPT_DATE").getObj());
                setAttribute("LAST_ISSUED_DATE",(String)ObjItem.getAttribute("LAST_ISSUED_DATE").getObj());
                setAttribute("ZERO_RECEIPT_QTY",ObjItem.getAttribute("ZERO_RECEIPT_QTY").getVal());
                setAttribute("ZERO_ISSUED_QTY",ObjItem.getAttribute("ZERO_ISSUED_QTY").getVal());
                setAttribute("ZERO_VALUE_QTY",ObjItem.getAttribute("ZERO_VALUE_QTY").getVal());
                setAttribute("REJECTED_QTY",ObjItem.getAttribute("REJECTED_QTY").getVal());
                setAttribute("ON_HAND_QTY",ObjItem.getAttribute("ON_HAND_QTY").getVal());
                setAttribute("AVAILABLE_QTY",ObjItem.getAttribute("AVAILABLE_QTY").getVal());
                setAttribute("ALLOCATED_QTY",ObjItem.getAttribute("ALLOCATED_QTY").getVal());
                setAttribute("LAST_TRANS_DATE",(String)ObjItem.getAttribute("LAST_TRANS_DATE").getObj());
                setAttribute("LAST_GRN_NO",(String)ObjItem.getAttribute("LAST_GRN_NO").getObj());
                setAttribute("LAST_GRN_DATE",(String)ObjItem.getAttribute("LAST_GRN_DATE").getObj());
                setAttribute("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                setAttribute("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                setAttribute("CAPTIVE_CONSUMABLE",ObjItem.getAttribute("CAPTIVE_CONSUMABLE").getBool());
                setAttribute("BLOCKED",(String)ObjItem.getAttribute("BLOCKED").getObj());
                setAttribute("REORDER_LEVEL",ObjItem.getAttribute("REORDER_LEVEL").getVal());
                setAttribute("SUPPLIER_ID",(int)ObjItem.getAttribute("SUPPLIER_ID").getVal());
                //setAttribute("ITEM_HIERARCHY_ID",(int)ObjItem.getAttribute("ITEM_HIERARCHY_ID").getVal());
                setAttribute("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                setAttribute("APPROVED_DATE",(String)ObjItem.getAttribute("APPROVED_DATE").getObj());
                setAttribute("REJECTED",ObjItem.getAttribute("REJECTED").getBool());
                setAttribute("REJECTED_DATE",(String)ObjItem.getAttribute("REJECTED_DATE").getObj());
                setAttribute("CREATED_BY",(int)ObjItem.getAttribute("CREATED_BY").getVal());
                setAttribute("CREATED_DATE",(String)ObjItem.getAttribute("CREATED_DATE").getObj());
                setAttribute("MODIFIED_BY",(int)ObjItem.getAttribute("MODIFIED_BY").getVal());
                setAttribute("MODIFIED_DATE",(String)ObjItem.getAttribute("MODIFIED_DATE").getObj());
                setAttribute("SPECIAL_APPROVAL",(String)ObjItem.getAttribute("SPECIAL_APPROVAL").getObj());
                
                // ----------------- End of Header Fields ------------------------------------//
                
                
                colRNDApprovers.clear();
                
                for(int i=1;i<=ObjItem.colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjRND=(clsItemRNDApprover)  ObjItem.colRNDApprovers.get(Integer.toString(i));
                    
                    clsItemRNDApprover NewItem=new clsItemRNDApprover();
                    
                    NewItem.setAttribute("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("AMEND_ID",(int)getAttribute("AMEND_ID").getVal());
                    NewItem.setAttribute("SR_NO",(int)ObjRND.getAttribute("SR_NO").getVal());
                    NewItem.setAttribute("USER_ID",(int)ObjRND.getAttribute("USER_ID").getVal());
                    
                    colRNDApprovers.put(Integer.toString(colRNDApprovers.size()+1),NewItem);
                }
            }
            
            
            
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_AMEND WHERE AMEND_ID="+lItemSysID);
            rsTmp.first();
            
            //----------- Save Header Record ----------------
            rsTmp.updateString("AMEND_DATE",(String)getAttribute("AMEND_DATE").getObj());
            rsTmp.updateString("AMEND_REASON",(String)getAttribute("AMEND_REASON").getObj());
            rsTmp.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsTmp.updateString("ITEM_DESCRIPTION",(String)getAttribute("ITEM_DESCRIPTION").getObj());
            rsTmp.updateLong("GROUP_CODE",(long)getAttribute("GROUP_CODE").getVal());
            rsTmp.updateString("SEARCH_KEY",(String)getAttribute("SEARCH_KEY").getObj());
            rsTmp.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
            rsTmp.updateLong("CATEGORY_ID",(long)getAttribute("CATEGORY_ID").getVal());
            rsTmp.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
            rsTmp.updateBoolean("ONETIME",getAttribute("ONETIME").getBool());
            rsTmp.updateBoolean("RND_APPROVAL",getAttribute("RND_APPROVAL").getBool());
            rsTmp.updateLong("DESC",(long)getAttribute("DESC").getVal());
            rsTmp.updateLong("MAKE",(long)getAttribute("MAKE").getVal());
            rsTmp.updateLong("SIZE",(long)getAttribute("SIZE").getVal());
            rsTmp.updateString("ABC",(String)getAttribute("ABC").getObj());
            rsTmp.updateString("XYZ",(String)getAttribute("XYZ").getObj());
            rsTmp.updateString("VEN",(String)getAttribute("VEN").getObj());
            rsTmp.updateString("FSN",(String)getAttribute("FSN").getObj());
            rsTmp.updateString("MF",(String)getAttribute("MF").getObj());
            rsTmp.updateBoolean("MAINTAIN_STOCK",(boolean)getAttribute("MAINTAIN_STOCK").getBool());
            rsTmp.updateDouble("MIN_QTY",(double)getAttribute("MIN_QTY").getVal());
            rsTmp.updateDouble("MAX_QTY",(double)getAttribute("MAX_QTY").getVal());
            rsTmp.updateDouble("TOLERANCE_LIMIT",(double)getAttribute("TOLERANCE_LIMIT").getVal());
            rsTmp.updateInt("UNIT",(int)getAttribute("UNIT").getVal());
            rsTmp.updateDouble("UNIT_RATE",(double)getAttribute("UNIT_RATE").getVal());
            rsTmp.updateDouble("DNP",(double)getAttribute("DNP").getVal());
            rsTmp.updateDouble("QTR1_RATE",(double)getAttribute("QTR1_RATE").getVal());
            rsTmp.updateDouble("QTR2_RATE",(double)getAttribute("QTR2_RATE").getVal());
            rsTmp.updateDouble("QTR3_RATE",(double)getAttribute("QTR3_RATE").getVal());
            rsTmp.updateDouble("QTR4_RATE",(double)getAttribute("QTR4_RATE").getVal());
            rsTmp.updateDouble("REBATE",(double)getAttribute("REBATE").getVal());
            
            rsTmp.updateString("TAX_CODE_TYPE",(String)getAttribute("TAX_CODE_TYPE").getObj());
            rsTmp.updateString("HSN_SAC_CODE",(String)getAttribute("HSN_SAC_CODE").getObj());
            
            rsTmp.updateBoolean("EXCISE_APPLICABLE",(boolean)getAttribute("EXCISE_APPLICABLE").getBool());
            rsTmp.updateDouble("EXCISE",(double)getAttribute("EXCISE").getVal());
            rsTmp.updateLong("CHAPTER_NO",(long)getAttribute("CHAPTER_NO").getVal());
            rsTmp.updateBoolean("TAXABLE",(boolean)getAttribute("TAXABLE").getBool());
            rsTmp.updateBoolean("CAPTIVE_CONSUMABLE",(boolean)getAttribute("CAPTIVE_CONSUMABLE").getBool());
            rsTmp.updateBoolean("BLOCKED",(boolean)getAttribute("BLOCKED").getBool());
            rsTmp.updateDouble("REORDER_LEVEL",(double)getAttribute("REORDER_LEVEL").getVal());
            rsTmp.updateLong("SUPPLIER_ID",(long)getAttribute("SUPPLIER_ID").getVal());
            rsTmp.updateLong("ITEM_HIERARCHY_ID",(long)getAttribute("ITEM_HIERARCHY_ID").getVal());
            rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsTmp.updateString("SPECIAL_APPROVAL",(String)getAttribute("SPECIAL_APPROVAL").getObj());
            rsTmp.updateBoolean("CANCELLED",false);
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_ITEM_MASTER_AMEND_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("ITEM_ID").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("AMEND_ID",(long)getAttribute("AMEND_ID").getVal());
            rsHistory.updateString("AMEND_DATE",(String)getAttribute("AMEND_DATE").getObj());
            rsHistory.updateString("AMEND_REASON",(String)getAttribute("AMEND_REASON").getObj());
            rsHistory.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsHistory.updateString("ITEM_DESCRIPTION",(String)getAttribute("ITEM_DESCRIPTION").getObj());
            rsHistory.updateBoolean("ONETIME",getAttribute("ONETIME").getBool());
            rsHistory.updateBoolean("RND_APPROVAL",getAttribute("RND_APPROVAL").getBool());
            rsHistory.updateLong("GROUP_CODE",(long)getAttribute("GROUP_CODE").getVal());
            rsHistory.updateString("SEARCH_KEY",(String)getAttribute("SEARCH_KEY").getObj());
            rsHistory.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
            rsHistory.updateLong("CATEGORY_ID",(long)getAttribute("CATEGORY_ID").getVal());
            rsHistory.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
            rsHistory.updateLong("DESC",(long)getAttribute("DESC").getVal());
            rsHistory.updateLong("MAKE",(long)getAttribute("MAKE").getVal());
            rsHistory.updateLong("SIZE",(long)getAttribute("SIZE").getVal());
            rsHistory.updateString("ABC",(String)getAttribute("ABC").getObj());
            rsHistory.updateString("XYZ",(String)getAttribute("XYZ").getObj());
            rsHistory.updateString("VEN",(String)getAttribute("VEN").getObj());
            rsHistory.updateString("FSN",(String)getAttribute("FSN").getObj());
            rsHistory.updateString("MF",(String)getAttribute("MF").getObj());
            rsHistory.updateBoolean("MAINTAIN_STOCK",(boolean)getAttribute("MAINTAIN_STOCK").getBool());
            rsHistory.updateDouble("MIN_QTY",(double)getAttribute("MIN_QTY").getVal());
            rsHistory.updateDouble("MAX_QTY",(double)getAttribute("MAX_QTY").getVal());
            rsHistory.updateDouble("TOLERANCE_LIMIT",(double)getAttribute("TOLERANCE_LIMIT").getVal());
            rsHistory.updateLong("UNIT",(long)getAttribute("UNIT").getVal());
            rsHistory.updateDouble("UNIT_RATE",(double)getAttribute("UNIT_RATE").getVal());
            rsHistory.updateDouble("DNP",(double)getAttribute("DNP").getVal());
            rsHistory.updateDouble("QTR1_RATE",(double)getAttribute("QTR1_RATE").getVal());
            rsHistory.updateDouble("QTR2_RATE",(double)getAttribute("QTR2_RATE").getVal());
            rsHistory.updateDouble("QTR3_RATE",(double)getAttribute("QTR3_RATE").getVal());
            rsHistory.updateDouble("QTR4_RATE",(double)getAttribute("QTR4_RATE").getVal());
            rsHistory.updateDouble("REBATE",(double)getAttribute("REBATE").getVal());
            
            rsHistory.updateString("TAX_CODE_TYPE",(String)getAttribute("TAX_CODE_TYPE").getObj());
            rsHistory.updateString("HSN_SAC_CODE",(String)getAttribute("HSN_SAC_CODE").getObj());
            
            rsHistory.updateBoolean("EXCISE_APPLICABLE",(boolean)getAttribute("EXCISE_APPLICABLE").getBool());
            rsHistory.updateDouble("EXCISE",(double)getAttribute("EXCISE").getVal());
            rsHistory.updateLong("CHAPTER_NO",(long)getAttribute("CHAPTER_NO").getVal());
            rsHistory.updateBoolean("TAXABLE",(boolean)getAttribute("TAXABLE").getBool());
            rsHistory.updateBoolean("CAPTIVE_CONSUMABLE",(boolean)getAttribute("CAPTIVE_CONSUMABLE").getBool());
            rsHistory.updateBoolean("BLOCKED",(boolean)getAttribute("BLOCKED").getBool());
            rsHistory.updateDouble("REORDER_LEVEL",(double)getAttribute("REORDER_LEVEL").getVal());
            rsHistory.updateLong("SUPPLIER_ID",(long)getAttribute("SUPPLIER_ID").getVal());
            rsHistory.updateLong("ITEM_HIERARCHY_ID",(long)getAttribute("ITEM_HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateString("SPECIAL_APPROVAL",(String)getAttribute("SPECIAL_APPROVAL").getObj());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            //=========Now Updating RND Approvers -----------------//
            stRND.executeUpdate("DELETE FROM D_INV_ITEM_RND_APPROVERS_AMEND WHERE COMPANY_ID="+lCompanyID+" AND AMEND_ID="+lItemSysID);
            stRNDH.executeUpdate("DELETE FROM D_INV_ITEM_RND_APPROVERS_AMEND_H WHERE COMPANY_ID="+lCompanyID+" AND AMEND_ID="+lItemSysID);
            
            rsRND=stRND.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_AMEND");
            rsRNDH=stRNDH.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_AMEND_H");
            
            //Only Update the records if RND Approval Required
            if(getAttribute("RND_APPROVAL").getBool()) {
                for(int i=1;i<=colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjApprover=(clsItemRNDApprover)colRNDApprovers.get(Integer.toString(i));
                    
                    rsRND.moveToInsertRow();
                    rsRND.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRND.updateLong("AMEND_ID",(long)getAttribute("AMEND_ID").getVal());
                    rsRND.updateInt("SR_NO",i);
                    rsRND.updateInt("USER_ID",(int)ObjApprover.getAttribute("USER_ID").getVal());
                    rsRND.updateBoolean("CHANGED",true);
                    rsRND.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsRND.insertRow();
                    
                    rsRNDH.moveToInsertRow();
                    rsRNDH.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRNDH.updateLong("AMEND_ID",(long)getAttribute("AMEND_ID").getVal());
                    rsRNDH.updateInt("SR_NO",i);
                    rsRNDH.updateInt("REVISION_NO",RevNo);
                    rsRNDH.updateInt("USER_ID",(int)ObjApprover.getAttribute("USER_ID").getVal());
                    rsRNDH.updateBoolean("CHANGED",true);
                    rsRNDH.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsRNDH.insertRow();
                }
            }
            //=============================================================//
            
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=51; //Item Master
            ObjFlow.DocNo=Integer.toString((int)getAttribute("AMEND_ID").getVal());
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_ITEM_MASTER_AMEND";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("ITEM_HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="AMEND_ID";            
            //==== Handling Rejected Documents ==========//
            AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_ITEM_MASTER_AMEND SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=51 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            if(ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
                if(IsRejected) {
                    ObjFlow.Status="A";
                    ObjFlow.To=ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            
            //--------- Approval Flow Update complete -----------
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
      
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pItemID,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=51 AND DOC_NO='"+pItemID+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
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
            return false;
        }
    }
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pItemID,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND AMEND_ID='"+pItemID+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=51 AND DOC_NO='"+pItemID+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
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
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete() {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lItemID=Integer.toString((int)getAttribute("AMEND_ID").getVal());
            int lUserID=(int)getAttribute("FROM").getVal();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lItemID,lUserID)) {
                strSQL="DELETE FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+Integer.toString(lCompanyID)+" AND AMEND_ID='"+lItemID+"'";
                data.Execute(strSQL);
                MoveLast();
                return true;
            }
            else {
                LastError="Item cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public Object getObject(int pCompanyID,String pItemID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'";
        clsItemAmend ObjItem = new clsItemAmend();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate)
            {
                strSQL="SELECT D_INV_ITEM_MASTER_AMEND.ITEM_ID,D_INV_ITEM_MASTER_AMEND.COMPANY_ID,D_INV_ITEM_MASTER_AMEND.AMEND_ID,D_INV_ITEM_MASTER_AMEND.ITEM_DESCRIPTION,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_ITEM_MASTER_AMEND,D_COM_DOC_DATA WHERE D_INV_ITEM_MASTER_AMEND.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_ITEM_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ITEM_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=51 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";                
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate)
            {
               strSQL="SELECT D_INV_ITEM_MASTER_AMEND.ITEM_ID,D_INV_ITEM_MASTER_AMEND.COMPANY_ID,D_INV_ITEM_MASTER_AMEND.AMEND_ID,D_INV_ITEM_MASTER_AMEND.ITEM_DESCRIPTION,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_ITEM_MASTER_AMEND,D_COM_DOC_DATA WHERE D_INV_ITEM_MASTER_AMEND.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_ITEM_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ITEM_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=51 ORDER BY D_INV_ITEM_MASTER_AMEND.ITEM_ID"; 
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo)
            {
               strSQL="SELECT D_INV_ITEM_MASTER_AMEND.ITEM_ID,D_INV_ITEM_MASTER_AMEND.COMPANY_ID,D_INV_ITEM_MASTER_AMEND.AMEND_ID,D_INV_ITEM_MASTER_AMEND.ITEM_DESCRIPTION,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_ITEM_MASTER_AMEND,D_COM_DOC_DATA WHERE D_INV_ITEM_MASTER_AMEND.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_ITEM_MASTER_AMEND.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ITEM_MASTER_AMEND.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=51 ORDER BY D_INV_ITEM_MASTER_AMEND.ITEM_ID";  
            }
            
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsItem ObjItem=new clsItem();
                
                //------------- Header Fields --------------------//
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("AMEND_ID",rsTmp.getInt("AMEND_ID"));
                ObjItem.setAttribute("ITEM_DESCRIPTION",rsTmp.getString("ITEM_DESCRIPTION"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
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
    
    
    
    
    
    
    public static HashMap getPendingApprovalsDept(int pCompanyID,int pDeptID) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            strSQL="SELECT * FROM D_INV_ITEM_MASTER_AMEND WHERE APPROVED=0 AND COMPANY_ID="+pCompanyID+" AND CREATED_BY IN (SELECT USER_ID FROM D_COM_USER_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID+")";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsItem ObjItem=new clsItem();
                
                //------------- Header Fields --------------------//
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("AMEND_ID"));
                ObjItem.setAttribute("DOC_DATE","0000-00-00");
                
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
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
    
    
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            //String strSql = "" + pCondition ;
            String strSql = "SELECT * FROM D_INV_ITEM_MASTER_AMEND " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement();
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
            }
            else {
                Ready=false;
            }
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        int RevNo=0;
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            //------------- Header Fields --------------------//
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("ITEM_ID",rsResultSet.getString("ITEM_ID"));
            setAttribute("AMEND_ID",rsResultSet.getInt("AMEND_ID"));
            setAttribute("AMEND_DATE",rsResultSet.getString("AMEND_DATE"));
            setAttribute("AMEND_REASON",rsResultSet.getString("AMEND_REASON"));
            setAttribute("ITEM_DESCRIPTION",rsResultSet.getString("ITEM_DESCRIPTION"));
            setAttribute("ONETIME",rsResultSet.getBoolean("ONETIME"));
            setAttribute("RND_APPROVAL",rsResultSet.getBoolean("RND_APPROVAL"));
            setAttribute("GROUP_CODE",rsResultSet.getInt("GROUP_CODE"));
            setAttribute("SEARCH_KEY",rsResultSet.getString("SEARCH_KEY"));
            setAttribute("WAREHOUSE_ID",rsResultSet.getString("WAREHOUSE_ID"));
            setAttribute("CATEGORY_ID",rsResultSet.getInt("CATEGORY_ID"));
            setAttribute("UNIT",rsResultSet.getInt("UNIT"));
            setAttribute("LOCATION_ID",rsResultSet.getString("LOCATION_ID"));
            setAttribute("DESC",rsResultSet.getInt("DESC"));
            setAttribute("MAKE",rsResultSet.getInt("MAKE"));
            setAttribute("SIZE",rsResultSet.getInt("SIZE"));
            setAttribute("ABC",rsResultSet.getString("ABC"));
            setAttribute("XYZ",rsResultSet.getString("XYZ"));
            setAttribute("VEN",rsResultSet.getString("VEN"));
            setAttribute("FSN",rsResultSet.getString("FSN"));
            setAttribute("MF",rsResultSet.getString("MF"));
            setAttribute("MAINTAIN_STOCK",rsResultSet.getBoolean("MAINTAIN_STOCK"));
            setAttribute("AVG_QTY",rsResultSet.getDouble("AVG_QTY"));
            setAttribute("MIN_QTY",rsResultSet.getDouble("MIN_QTY"));
            setAttribute("MAX_QTY",rsResultSet.getDouble("MAX_QTY"));
            setAttribute("TOLERANCE_LIMIT",rsResultSet.getDouble("TOLERANCE_LIMIT"));
            setAttribute("DNP",rsResultSet.getDouble("DNP"));
            setAttribute("UNIT_RATE",rsResultSet.getDouble("UNIT_RATE"));
            setAttribute("QTR1_RATE",rsResultSet.getDouble("QTR1_RATE"));
            setAttribute("QTR2_RATE",rsResultSet.getDouble("QTR2_RATE"));
            setAttribute("QTR3_RATE",rsResultSet.getDouble("QTR3_RATE"));
            setAttribute("QTR4_RATE",rsResultSet.getDouble("QTR4_RATE"));
            setAttribute("REBATE",rsResultSet.getDouble("REBATE"));
            
            setAttribute("TAX_CODE_TYPE",rsResultSet.getString("TAX_CODE_TYPE"));
            setAttribute("HSN_SAC_CODE",rsResultSet.getString("HSN_SAC_CODE"));
            
            setAttribute("EXCISE_APPLICABLE",rsResultSet.getBoolean("EXCISE_APPLICABLE"));
            setAttribute("EXCISE",rsResultSet.getDouble("EXCISE"));
            setAttribute("CHAPTER_NO",rsResultSet.getInt("CHAPTER_NO"));
            setAttribute("TAXABLE",rsResultSet.getBoolean("TAXABLE"));
            setAttribute("OPENING_QTY",rsResultSet.getDouble("OPENING_QTY"));
            setAttribute("OPENING_VALUE",rsResultSet.getDouble("OPENING_VALUE"));
            setAttribute("TOTAL_RECEIPT_QTY",rsResultSet.getDouble("TOTAL_RECEIPT_QTY"));
            setAttribute("TOTAL_ISSUED_QTY",rsResultSet.getDouble("TOTAL_ISSUED_QTY"));
            setAttribute("LAST_RECEIPT_DATE",rsResultSet.getString("LAST_RECEIPT_DATE"));
            setAttribute("LAST_ISSUED_DATE",rsResultSet.getString("LAST_ISSUED_DATE"));
            setAttribute("ZERO_RECEIPT_QTY",rsResultSet.getDouble("ZERO_RECEIPT_QTY"));
            setAttribute("ZERO_ISSUED_QTY",rsResultSet.getDouble("ZERO_ISSUED_QTY"));
            setAttribute("ZERO_VALUE_QTY",rsResultSet.getDouble("ZERO_VALUE_QTY"));
            setAttribute("REJECTED_QTY",rsResultSet.getDouble("REJECTED_QTY"));
            setAttribute("ON_HAND_QTY",rsResultSet.getDouble("ON_HAND_QTY"));
            setAttribute("AVAILABLE_QTY",rsResultSet.getDouble("AVAILABLE_QTY"));
            setAttribute("ALLOCATED_QTY",rsResultSet.getDouble("ALLOCATED_QTY"));
            setAttribute("LAST_TRANS_DATE",rsResultSet.getString("LAST_TRANS_DATE"));
            setAttribute("LAST_GRN_NO",rsResultSet.getString("LAST_GRN_NO"));
            setAttribute("LAST_GRN_DATE",rsResultSet.getString("LAST_GRN_DATE"));
            setAttribute("LAST_PO_NO",rsResultSet.getString("LAST_PO_NO"));
            setAttribute("LAST_PO_DATE",rsResultSet.getString("LAST_PO_DATE"));
            setAttribute("CAPTIVE_CONSUMABLE",rsResultSet.getBoolean("CAPTIVE_CONSUMABLE"));
            setAttribute("BLOCKED",rsResultSet.getBoolean("BLOCKED"));
            setAttribute("REORDER_LEVEL",rsResultSet.getDouble("REORDER_LEVEL"));
            setAttribute("SUPPLIER_ID",rsResultSet.getInt("SUPPLIER_ID"));
            setAttribute("ITEM_HIERARCHY_ID",rsResultSet.getInt("ITEM_HIERARCHY_ID"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("SPECIAL_APPROVAL",rsResultSet.getString("SPECIAL_APPROVAL"));
            
            // ----------------- End of Header Fields ------------------------------------//
            
            
            //  ---------- Item R&D Approvers List --------------
            colRNDApprovers.clear();
            
            int CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            long ItemSysID=(long)getAttribute("AMEND_ID").getVal();
            
            tmpStmt=Conn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_AMEND_H WHERE COMPANY_ID="+CompanyID+" AND AMEND_ID="+ItemSysID+" AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_AMEND WHERE COMPANY_ID="+CompanyID+" AND AMEND_ID="+ItemSysID);
            }
            
            rsTmp.first();
            
            int Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsItemRNDApprover ObjApprover=new clsItemRNDApprover();
                
                ObjApprover.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjApprover.setAttribute("AMEND_ID",rsTmp.getLong("AMEND_ID"));
                ObjApprover.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjApprover.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                
                colRNDApprovers.put(Long.toString(Counter),ObjApprover);
                rsTmp.next();
            }
            
            
            //All data have benn filled
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_AMEND_H WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pDocNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_AMEND_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND AMEND_ID='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsItem ObjItem=new clsItem();
                
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_DATE","0000-00-00");
                ObjItem.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjItem.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjItem.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjItem.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjItem.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjItem);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
        }
    }
    

    
    public static int getAmendmentNo(int pCompanyID,String pItemID) {
        String strSQL="";
       
        ResultSet rsTmp;
        
        int AmendNo=0;
        
        try {
            rsTmp=data.getResult("SELECT COUNT(*) AS THECOUNT FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pItemID+"' AND ITEM_ID IN (SELECT ITEM_ID FROM D_INV_ITEM_MASTER WHERE ITEM_ID='"+pItemID+"' AND APPROVED=1)");
            rsTmp.first();
            
            if(rsTmp.getRow()>0)
            {
              AmendNo=rsTmp.getInt("THECOUNT");  
            }
            
        }
        catch(Exception e) {
        }
        
        return AmendNo;
    }
    

    
    public static int getUnderApprovalCount(int pCompanyID,String pItemID) {
        String strSQL="";
        ResultSet rsTmp;
        int AmendCount=0;
        try {
            rsTmp=data.getResult("SELECT COUNT(*) AS THECOUNT FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pItemID+"' AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
              AmendCount=rsTmp.getInt("THECOUNT");  
            }
        } catch(Exception e) {
        }
        return AmendCount;
    }

    
    public static boolean CanCancel(int pCompanyID,String pItemID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT ITEM_ID FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+pCompanyID+" AND AMEND_ID='"+pItemID+"' AND CANCELLED=0 AND APPROVED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                    canCancel=true;
            }
            rsTmp.close();
        } catch(Exception e) {
        }
        return canCancel;
    }
    
    public static boolean CancelItemAmendment(int pCompanyID,String pAmendID) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pAmendID)) {
                
                boolean ApprovedAmendment=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_ITEM_MASTER_AMEND WHERE COMPANY_ID="+pCompanyID+" AND AMEND_ID='"+pAmendID+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedAmendment=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedAmendment) {
                    
                }
                else {
                    int ModuleID=51;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pAmendID+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_ITEM_MASTER_AMEND SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND AMEND_ID='"+pAmendID+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
}
