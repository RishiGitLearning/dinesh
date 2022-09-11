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

public class clsItem {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    public HashMap colItemStock=new HashMap();
    public HashMap colRNDApprovers=new HashMap();
    
    private HashMap props;
    public boolean Ready = false;
    public boolean useSpecificURL=false;
    public String specifiedURL="";
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
    public clsItem() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("ITEM_SYS_ID",new Variant(0));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID));
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean LoadDataToTransfer(int pCompanyID) {
        Ready=false;
        try {
            HistoryView=false;
            String dbURL=clsFinYear.getDBURL(pCompanyID,EITLERPGLOBAL.FinYearFrom);
            Conn=data.getConn(dbURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID));
            Ready=true;
            MoveLast();
            return true;
        } catch(Exception e) {
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
        Statement stTmp,stHistory,stRND,stRNDH,stStock;
        ResultSet rsTmp,rsHistory,rsRND,rsRNDH,rsStock;
        
        try {
            
            if(!Validate()) {
                return false;
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRND=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRNDH=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_H WHERE ITEM_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE ITEM_ID='1'");
            rsTmp.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Check for the Duplication of Login ID ------
            /*long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
             
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Long.toString(lCompanyID)+" AND ITEM_ID='"+(String) getAttribute("ITEM_ID").getObj()+"' AND CANCELLED=0";
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
             
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="ITEM ID already exist in the database. Please specify other item id";
                return false;
            }*/
            //------- Duplication check over ----------------------
            
            
            //--------- Generate new system id ------------
            if(useSpecificURL) {
                setAttribute("ITEM_SYS_ID", data.getMaxID(getAttribute("COMPANY_ID").getInt(),"D_INV_ITEM_MASTER","ITEM_SYS_ID",specifiedURL));
            } else {
                setAttribute("ITEM_SYS_ID", data.getMaxID(getAttribute("COMPANY_ID").getInt(),"D_INV_ITEM_MASTER","ITEM_SYS_ID"));
            }
            //-------------------------------------------------
            
            
            //----------- Save Header Record ----------------
            rsTmp.moveToInsertRow();
            rsTmp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsTmp.updateLong("ITEM_SYS_ID",(long)getAttribute("ITEM_SYS_ID").getVal());
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
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateBoolean("CANCELLED",false);
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
            rsHistory.updateLong("ITEM_SYS_ID",(long)getAttribute("ITEM_SYS_ID").getVal());
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
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            //========== Insert Blank record in stock master ==========================//
            if(((String)getAttribute("APPROVAL_STATUS").getObj()).equals("F")) {
                
                // Delete Old Records
                if(useSpecificURL) {
                    data.Execute("DELETE FROM D_INV_ITEM_LOT_MASTER WHERE ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"' AND LOT_NO='X' AND BOE_NO='X' AND WAREHOUSE_ID='"+(String)getAttribute("WAREHOUSE_ID").getObj()+"' AND LOCATION_ID='"+(String)getAttribute("LOCATION_ID").getObj()+"'",specifiedURL);
                } else {
                    data.Execute("DELETE FROM D_INV_ITEM_LOT_MASTER WHERE ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"' AND LOT_NO='X' AND BOE_NO='X' AND WAREHOUSE_ID='"+(String)getAttribute("WAREHOUSE_ID").getObj()+"' AND LOCATION_ID='"+(String)getAttribute("LOCATION_ID").getObj()+"'");
                }
                stStock=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsStock=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE ITEM_ID='1'");
                rsStock.moveToInsertRow();
                rsStock.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsStock.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
                rsStock.updateString("LOT_NO","X");
                rsStock.updateString("BOE_NO","X");
                rsStock.updateString("BOE_SR_NO"," ");
                rsStock.updateString("BOE_DATE","0000-00-00");
                rsStock.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
                rsStock.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
                rsStock.updateDouble("OPENING_QTY",0);
                rsStock.updateDouble("OPENING_RATE",0);
                rsStock.updateDouble("TOTAL_RECEIPT_QTY",0);
                rsStock.updateDouble("TOTAL_ISSUED_QTY",0);
                rsStock.updateString("LAST_RECEIPT_DATE","0000-00-00");
                rsStock.updateString("LAST_ISSUED_DATE","0000-00-00");
                rsStock.updateDouble("ZERO_OPENING_QTY",0);
                rsStock.updateDouble("ZERO_RECEIPT_QTY",0);
                rsStock.updateDouble("ZERO_ISSUED_QTY",0);
                rsStock.updateDouble("ZERO_VAL_QTY",0);
                rsStock.updateDouble("REJECTED_QTY",0);
                rsStock.updateDouble("ON_HAND_QTY",0);
                rsStock.updateDouble("AVAILABLE_QTY",0);
                rsStock.updateDouble("ALLOCATED_QTY",0);
                rsStock.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsStock.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsStock.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsStock.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsStock.updateBoolean("CHANGED",true);
                rsStock.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsStock.insertRow();
                
            }
            // =====================================================================//
            
            
            //=========Now Updating RND Approvers -----------------//
            rsRND=stRND.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS");
            rsRNDH=stRNDH.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_H");
            
            //Only Update the records if RND Approval Required
            if(getAttribute("RND_APPROVAL").getBool()) {
                for(int i=1;i<=colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjApprover=(clsItemRNDApprover)colRNDApprovers.get(Integer.toString(i));
                    
                    rsRND.moveToInsertRow();
                    rsRND.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRND.updateLong("ITEM_SYS_ID",(long)getAttribute("ITEM_SYS_ID").getVal());
                    rsRND.updateInt("SR_NO",i);
                    rsRND.updateInt("USER_ID",(int)ObjApprover.getAttribute("USER_ID").getVal());
                    rsRND.updateBoolean("CHANGED",true);
                    rsRND.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsRND.insertRow();
                    
                    rsRNDH.moveToInsertRow();
                    rsRNDH.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRNDH.updateLong("ITEM_SYS_ID",(long)getAttribute("ITEM_SYS_ID").getVal());
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
            ObjFlow.ModuleID=1; //Item Master
            ObjFlow.DocNo=(String)getAttribute("ITEM_ID").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_ITEM_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("ITEM_HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="ITEM_ID";
            
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if(useSpecificURL) {
                    ObjFlow.UseSpecifiedURL=true;
                    ObjFlow.SpecificURL = specifiedURL;
                    if(!ObjFlow.UpdateFlow(specifiedURL)) {
                        
                        LastError=ObjFlow.LastError;
                    }
                } else {
                    if(!ObjFlow.UpdateFlow()) {
                        LastError=ObjFlow.LastError;
                    }
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
        Statement stTmp,stHistory,stRND,stRNDH,stStock;
        ResultSet rsTmp,rsHistory,rsRND,rsRNDH,rsStock;
        
        try {
            
            if(!Validate()) {
                return false;
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRND=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stRNDH=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_H WHERE ITEM_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Check for the Duplication of Login ID ------
            long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            long lItemSysID=(long) getAttribute("ITEM_SYS_ID").getVal();
            
            
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Long.toString(lCompanyID)+" AND ITEM_ID='"+(String) getAttribute("ITEM_ID").getObj()+"' AND ITEM_SYS_ID<>"+Long.toString(lItemSysID);
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
            
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="ITEM ID already exist in the database. Please specify other item id";
                return false;
            }
            //------- Duplication check over ----------------------
            
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE ITEM_SYS_ID="+lItemSysID);
            rsTmp.first();
            
            //----------- Save Header Record ----------------
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
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateBoolean("CANCELLED",false);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_ITEM_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("ITEM_ID").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("ITEM_SYS_ID",(long)getAttribute("ITEM_SYS_ID").getVal());
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
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            //========== Insert Blank record in stock master ==========================//
            if(((String)getAttribute("APPROVAL_STATUS").getObj()).equals("F")) {
                
                // Delete Old Records
                data.Execute("DELETE FROM D_INV_ITEM_LOT_MASTER WHERE ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"' AND LOT_NO='X' AND BOE_NO='X' AND WAREHOUSE_ID='"+(String)getAttribute("WAREHOUSE_ID").getObj()+"' AND LOCATION_ID='"+(String)getAttribute("LOCATION_ID").getObj()+"'");
                
                stStock=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsStock=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE ITEM_ID='1'");
                rsStock.moveToInsertRow();
                rsStock.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsStock.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
                rsStock.updateString("LOT_NO","X");
                rsStock.updateString("BOE_NO","X");
                rsStock.updateString("BOE_SR_NO"," ");
                rsStock.updateString("BOE_DATE","0000-00-00");
                rsStock.updateString("WAREHOUSE_ID",(String)getAttribute("WAREHOUSE_ID").getObj());
                rsStock.updateString("LOCATION_ID",(String)getAttribute("LOCATION_ID").getObj());
                rsStock.updateDouble("OPENING_QTY",0);
                rsStock.updateDouble("OPENING_RATE",0);
                rsStock.updateDouble("TOTAL_RECEIPT_QTY",0);
                rsStock.updateDouble("TOTAL_ISSUED_QTY",0);
                rsStock.updateString("LAST_RECEIPT_DATE","0000-00-00");
                rsStock.updateString("LAST_ISSUED_DATE","0000-00-00");
                rsStock.updateDouble("ZERO_OPENING_QTY",0);
                rsStock.updateDouble("ZERO_RECEIPT_QTY",0);
                rsStock.updateDouble("ZERO_ISSUED_QTY",0);
                rsStock.updateDouble("ZERO_VAL_QTY",0);
                rsStock.updateDouble("REJECTED_QTY",0);
                rsStock.updateDouble("ON_HAND_QTY",0);
                rsStock.updateDouble("AVAILABLE_QTY",0);
                rsStock.updateDouble("ALLOCATED_QTY",0);
                rsStock.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsStock.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsStock.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsStock.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsStock.updateBoolean("CHANGED",true);
                rsStock.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsStock.insertRow();
                
            }
            // =====================================================================//
            
            
            
            
            
            //=========Now Updating RND Approvers -----------------//
            stRND.executeUpdate("DELETE FROM D_INV_ITEM_RND_APPROVERS WHERE COMPANY_ID="+lCompanyID+" AND ITEM_SYS_ID="+lItemSysID);
            stRNDH.executeUpdate("DELETE FROM D_INV_ITEM_RND_APPROVERS_H WHERE COMPANY_ID="+lCompanyID+" AND ITEM_SYS_ID="+lItemSysID);
            
            
            rsRND=stRND.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS");
            rsRNDH=stRNDH.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_H");
            
            //Only Update the records if RND Approval Required
            if(getAttribute("RND_APPROVAL").getBool()) {
                for(int i=1;i<=colRNDApprovers.size();i++) {
                    clsItemRNDApprover ObjApprover=(clsItemRNDApprover)colRNDApprovers.get(Integer.toString(i));
                    
                    rsRND.moveToInsertRow();
                    rsRND.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRND.updateLong("ITEM_SYS_ID",(long)getAttribute("ITEM_SYS_ID").getVal());
                    rsRND.updateInt("SR_NO",i);
                    rsRND.updateInt("USER_ID",(int)ObjApprover.getAttribute("USER_ID").getVal());
                    rsRND.updateBoolean("CHANGED",true);
                    rsRND.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsRND.insertRow();
                    
                    rsRNDH.moveToInsertRow();
                    rsRNDH.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                    rsRNDH.updateLong("ITEM_SYS_ID",(long)getAttribute("ITEM_SYS_ID").getVal());
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
            ObjFlow.ModuleID=1; //Item Master
            ObjFlow.DocNo=(String)getAttribute("ITEM_ID").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_ITEM_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("ITEM_HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="ITEM_ID";
            
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
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
                data.Execute("UPDATE D_INV_ITEM_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=1 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=1 AND DOC_NO='"+pItemID+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=1 AND DOC_NO='"+pItemID+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            String lItemID=(String)getAttribute("ITEM_ID").getObj();
            int lUserID=(int)getAttribute("FROM").getVal();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lItemID,lUserID)) {
                strSQL="DELETE FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(lCompanyID)+" AND ITEM_ID='"+lItemID+"'";
                data.Execute(strSQL);
                strSQL="DELETE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(lCompanyID)+" AND ITEM_ID='"+lItemID+"'";
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
        clsItem ObjItem= new clsItem();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    
    public static Object getObjectEx(int pCompanyID,String pItemID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'";
        clsItem ObjItem= new clsItem();
        ObjItem.LoadData(pCompanyID);
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
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_ITEM_MASTER.ITEM_ID,D_INV_ITEM_MASTER.COMPANY_ID,D_INV_ITEM_MASTER.ITEM_SYS_ID,D_INV_ITEM_MASTER.ITEM_DESCRIPTION,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_ITEM_MASTER,D_COM_DOC_DATA WHERE D_INV_ITEM_MASTER.ITEM_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_ITEM_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ITEM_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=1 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_ITEM_MASTER.ITEM_ID,D_INV_ITEM_MASTER.COMPANY_ID,D_INV_ITEM_MASTER.ITEM_SYS_ID,D_INV_ITEM_MASTER.ITEM_DESCRIPTION,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_ITEM_MASTER,D_COM_DOC_DATA WHERE D_INV_ITEM_MASTER.ITEM_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_ITEM_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ITEM_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=1 ORDER BY D_INV_ITEM_MASTER.ITEM_ID";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_ITEM_MASTER.ITEM_ID,D_INV_ITEM_MASTER.COMPANY_ID,D_INV_ITEM_MASTER.ITEM_SYS_ID,D_INV_ITEM_MASTER.ITEM_DESCRIPTION,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_ITEM_MASTER,D_COM_DOC_DATA WHERE D_INV_ITEM_MASTER.ITEM_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_ITEM_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ITEM_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=1 ORDER BY D_INV_ITEM_MASTER.ITEM_ID";
            }
            
            //strSQL="SELECT D_INV_ITEM_MASTER.ITEM_ID,D_INV_ITEM_MASTER.COMPANY_ID,D_INV_ITEM_MASTER.ITEM_SYS_ID,D_INV_ITEM_MASTER.ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER,D_COM_DOC_DATA WHERE D_INV_ITEM_MASTER.ITEM_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_ITEM_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_ITEM_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=1";
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
                ObjItem.setAttribute("ITEM_SYS_ID",rsTmp.getInt("ITEM_SYS_ID"));
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
    
    
    
    
    public static HashMap getRNDApprovers(int pCompanyID,String pItemID) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        long ItemSysID=getItemSystemID(pCompanyID, pItemID);
        
        try {
            strSQL="SELECT * FROM D_INV_ITEM_RND_APPROVERS WHERE COMPANY_ID="+pCompanyID+" AND ITEM_SYS_ID="+ItemSysID;
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsItemRNDApprover ObjItem=new clsItemRNDApprover();
                
                //------------- Header Fields --------------------//
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("ITEM_SYS_ID",rsTmp.getInt("ITEM_SYS_ID"));
                ObjItem.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
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
            strSQL="SELECT * FROM D_INV_ITEM_MASTER WHERE APPROVED=0 AND COMPANY_ID="+pCompanyID+" AND CREATED_BY IN (SELECT USER_ID FROM D_COM_USER_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID+")";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsItem ObjItem=new clsItem();
                
                //------------- Header Fields --------------------//
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("ITEM_ID"));
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
    
    
    public HashMap getList(String pCondition) {
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp2;
        Statement tmpStmt2;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_MASTER "+pCondition);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsItem ObjItem=new clsItem();
                
                //------------- Header Fields --------------------//
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_SYS_ID",rsTmp.getInt("ITEM_SYS_ID"));
                ObjItem.setAttribute("ITEM_DESCRIPTION",rsTmp.getString("ITEM_DESCRIPTION"));
                ObjItem.setAttribute("GROUP_CODE",rsTmp.getInt("GROUP_CODE"));
                ObjItem.setAttribute("SEARCH_KEY",rsTmp.getString("SEARCH_KEY"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsTmp.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("CATEGORY_ID",rsTmp.getInt("CATEGORY_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsTmp.getString("LOCATION_ID"));
                ObjItem.setAttribute("DESC",rsTmp.getInt("DESC"));
                ObjItem.setAttribute("MAKE",rsTmp.getInt("MAKE"));
                ObjItem.setAttribute("SIZE",rsTmp.getInt("SIZE"));
                ObjItem.setAttribute("ABC",rsTmp.getString("ABC"));
                ObjItem.setAttribute("XYZ",rsTmp.getString("XYZ"));
                ObjItem.setAttribute("VEN",rsTmp.getString("VEN"));
                ObjItem.setAttribute("FSN",rsTmp.getString("FSN"));
                ObjItem.setAttribute("MF",rsTmp.getString("MF"));
                ObjItem.setAttribute("MAINTAIN_STOCK",rsTmp.getBoolean("MAINTAIN_STOCK"));
                ObjItem.setAttribute("AVG_QTY",rsTmp.getDouble("AVG_QTY"));
                ObjItem.setAttribute("QTR1_RATE",rsTmp.getDouble("QTR1_RATE"));
                ObjItem.setAttribute("QTR2_RATE",rsTmp.getDouble("QTR2_RATE"));
                ObjItem.setAttribute("QTR3_RATE",rsTmp.getDouble("QTR3_RATE"));
                ObjItem.setAttribute("QTR4_RATE",rsTmp.getDouble("QTR4_RATE"));
                ObjItem.setAttribute("REBATE",rsTmp.getDouble("REBATE"));
                
                ObjItem.setAttribute("TAX_CODE_TYPE",rsTmp.getString("TAX_CODE_TYPE"));
                ObjItem.setAttribute("HSN_SAC_CODE",rsTmp.getString("HSN_SAC_CODE"));
                
                
                ObjItem.setAttribute("EXCISE_APPLICABLE",rsTmp.getBoolean("EXCISE_APPLICABLE"));
                ObjItem.setAttribute("EXCISE",rsTmp.getDouble("EXCISE"));
                ObjItem.setAttribute("CHAPTER_NO",rsTmp.getInt("CHAPTER_NO"));
                ObjItem.setAttribute("TAXABLE",rsTmp.getBoolean("TAXABLE"));
                ObjItem.setAttribute("OPENING_QTY",rsTmp.getDouble("OPENING_QTY"));
                ObjItem.setAttribute("OPENING_VALUE",rsTmp.getDouble("OPENING_VALUE"));
                ObjItem.setAttribute("TOTAL_RECEIPT_QTY",rsTmp.getDouble("TOTAL_RECEIPT_QTY"));
                ObjItem.setAttribute("TOTAL_ISSUED_QTY",rsTmp.getDouble("TOTAL_ISSUED_QTY"));
                ObjItem.setAttribute("LAST_RECEIPT_DATE",rsTmp.getString("LAST_RECEIPT_DATE"));
                ObjItem.setAttribute("LAST_ISSUED_DATE",rsTmp.getString("LAST_ISSUED_DATE"));
                ObjItem.setAttribute("ZERO_RECEIPT_QTY",rsTmp.getDouble("ZERO_RECEIPT_QTY"));
                ObjItem.setAttribute("ZERO_ISSUED_QTY",rsTmp.getDouble("ZERO_ISSUED_QTY"));
                ObjItem.setAttribute("ZERO_VALUE_QTY",rsTmp.getDouble("ZERO_VALUE_QTY"));
                ObjItem.setAttribute("REJECTED_QTY",rsTmp.getDouble("REJECTED_QTY"));
                ObjItem.setAttribute("ON_HAND_QTY",rsTmp.getDouble("ON_HAND_QTY"));
                ObjItem.setAttribute("AVAILABLE_QTY",rsTmp.getDouble("AVAILABLE_QTY"));
                ObjItem.setAttribute("ALLOCATED_QTY",rsTmp.getDouble("ALLOCATED_QTY"));
                ObjItem.setAttribute("LAST_TRANS_DATE",rsTmp.getString("LAST_TRANS_DATE"));
                ObjItem.setAttribute("LAST_GRN_NO",rsTmp.getString("LAST_GRN_NO"));
                ObjItem.setAttribute("LAST_GRN_DATE",rsTmp.getString("LAST_GRN_DATE"));
                ObjItem.setAttribute("LAST_PO_NO",rsTmp.getString("LAST_PO_NO"));
                ObjItem.setAttribute("LAST_PO_DATE",rsTmp.getString("LAST_PO_DATE"));
                ObjItem.setAttribute("CAPTIVE_CONSUMABLE",rsTmp.getBoolean("CAPTIVE_CONSUMABLE"));
                ObjItem.setAttribute("BLOCKED",rsTmp.getBoolean("BLOCKED"));
                ObjItem.setAttribute("REORDER_LEVEL",rsTmp.getDouble("REORDER_LEVEL"));
                ObjItem.setAttribute("SUPPLIER_ID",rsTmp.getInt("SUPPLIER_ID"));
                ObjItem.setAttribute("ITEM_HIERARCHY_ID",rsTmp.getInt("ITEM_HIERARCHY_ID"));
                ObjItem.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjItem.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjItem.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjItem.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                
                //Now Populate the collection
                //first clear the collection
                ObjItem.colItemStock.clear();
                
                String mCompanyID=Long.toString((long) ObjItem.getAttribute("COMPANY_ID").getVal());
                String mItemID=(String)ObjItem.getAttribute("USER_ID").getObj();
                
                tmpStmt2=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+mCompanyID+" AND ITEM_ID='"+mItemID+"'");
                
                rsTmp2.first();
                
                if(rsTmp2.getRow()>0) {
                    int Counter2=0;
                    while(!rsTmp2.isAfterLast()) {
                        Counter2=Counter2+1;
                        clsItemStock ObjStock=new clsItemStock();
                        
                        ObjStock.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                        ObjStock.setAttribute("ITEM_ID",rsTmp2.getString("ITEM_ID"));
                        ObjStock.setAttribute("LOT_NO",rsTmp2.getString("LOT_NO"));
                        ObjStock.setAttribute("BOE_NO",rsTmp2.getString("BOE_NO"));
                        ObjStock.setAttribute("BOE_SR_NO",rsTmp2.getString("BOE_SR_NO"));
                        ObjStock.setAttribute("OPENING_QTY",rsTmp2.getDouble("OPENING_QTY"));
                        ObjStock.setAttribute("OPENING_RATE",rsTmp2.getDouble("OPENING_RATE"));
                        ObjStock.setAttribute("TOTAL_RECEIPT_QTY",rsTmp2.getDouble("TOTAL_RECEIPT_QTY"));
                        ObjStock.setAttribute("TOTAL_ISSUED_QTY",rsTmp2.getDouble("TOTAL_ISSUED_QTY"));
                        ObjStock.setAttribute("LAST_RECEIPT_DATE",rsTmp2.getString("LAST_RECEIPT_DATE"));
                        ObjStock.setAttribute("LAST_ISSUED_DATE",rsTmp2.getString("LAST_ISSUED_DATE"));
                        ObjStock.setAttribute("ZERO_RECEIPT_QTY",rsTmp2.getDouble("ZERO_RECEIPT_QTY"));
                        ObjStock.setAttribute("ZERO_ISSUED_QTY",rsTmp2.getDouble("ZERO_ISSUED_QTY"));
                        ObjStock.setAttribute("REJECTED_QTY",rsTmp2.getDouble("REJECTED_QTY"));
                        ObjStock.setAttribute("ON_HAND_QTY",rsTmp2.getDouble("ON_HAND_QTY"));
                        ObjStock.setAttribute("AVAILABLE_QTY",rsTmp2.getDouble("AVAIALBLE_QTY"));
                        ObjStock.setAttribute("ALLOCATED_QTY",rsTmp2.getDouble("ALLOCATED_QTY"));
                        ObjStock.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                        ObjStock.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                        ObjStock.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                        ObjStock.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                        
                        ObjItem.colItemStock.put(Long.toString(Counter2),ObjStock);
                        rsTmp2.next();
                    }// Innser while
                }//If Condition
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
        }
        catch(Exception e) {
            LastError=e.getMessage();
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_ITEM_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement();
            
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if (rsResultSet.getRow()>0) {
                MoveFirst();
            }
            
            return true;
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
        int Counter=0;
        
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
            setAttribute("ITEM_SYS_ID",rsResultSet.getLong("ITEM_SYS_ID"));
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
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("SPECIAL_APPROVAL",rsResultSet.getString("SPECIAL_APPROVAL"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            
            // ----------------- End of Header Fields ------------------------------------//
            
            
            
            //  ---------- Item R&D Approvers List --------------
            colRNDApprovers.clear();
            
            int CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            long ItemSysID=(long)getAttribute("ITEM_SYS_ID").getVal();
            
            tmpStmt=Conn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS_H WHERE COMPANY_ID="+CompanyID+" AND ITEM_SYS_ID="+ItemSysID+" AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_RND_APPROVERS WHERE COMPANY_ID="+CompanyID+" AND ITEM_SYS_ID="+ItemSysID);
            }
            
            rsTmp.first();
            
            Counter=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsItemRNDApprover ObjApprover=new clsItemRNDApprover();
                
                ObjApprover.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjApprover.setAttribute("ITEM_SYS_ID",rsTmp.getLong("ITEM_SYS_ID"));
                ObjApprover.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjApprover.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                
                colRNDApprovers.put(Long.toString(Counter),ObjApprover);
                rsTmp.next();
            }
            
            
            //  ---------- Item Stock Collection Fields --------------
            colItemStock.clear();
            
            String mCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mItemID=(String)getAttribute("ITEM_ID").getObj();
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+mCompanyID+" AND ITEM_ID='"+mItemID+"'");
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsItemStock ObjStock=new clsItemStock();
                
                ObjStock.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjStock.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjStock.setAttribute("ITEM_SYS_ID",rsTmp.getLong("ITEM_SYS_ID"));
                ObjStock.setAttribute("LOT_NO",rsTmp.getString("LOT_NO"));
                ObjStock.setAttribute("BOE_NO",rsTmp.getString("BOE_NO"));
                ObjStock.setAttribute("BOE_SR_NO",rsTmp.getString("BOE_SR_NO"));
                ObjStock.setAttribute("OPENING_QTY",rsTmp.getDouble("OPENING_QTY"));
                ObjStock.setAttribute("OPENING_RATE",rsTmp.getDouble("OPENING_RATE"));
                ObjStock.setAttribute("TOTAL_RECEIPT_QTY",rsTmp.getDouble("TOTAL_RECEIPT_QTY"));
                ObjStock.setAttribute("TOTAL_ISSUED_QTY",rsTmp.getDouble("TOTAL_ISSUED_QTY"));
                ObjStock.setAttribute("LAST_RECEIPT_DATE",rsTmp.getString("LAST_RECEIPT_DATE"));
                ObjStock.setAttribute("LAST_ISSUED_DATE",rsTmp.getString("LAST_ISSUED_DATE"));
                ObjStock.setAttribute("ZERO_RECEIPT_QTY",rsTmp.getDouble("ZERO_RECEIPT_QTY"));
                ObjStock.setAttribute("ZERO_ISSUED_QTY",rsTmp.getDouble("ZERO_ISSUED_QTY"));
                ObjStock.setAttribute("REJECTED_QTY",rsTmp.getDouble("REJECTED_QTY"));
                ObjStock.setAttribute("ON_HAND_QTY",rsTmp.getDouble("ON_HAND_QTY"));
                ObjStock.setAttribute("AVAILABLE_QTY",rsTmp.getDouble("AVAILABLE_QTY"));
                ObjStock.setAttribute("ALLOCATED_QTY",rsTmp.getDouble("ALLOCATED_QTY"));
                ObjStock.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjStock.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjStock.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjStock.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                colItemStock.put(Long.toString(Counter),ObjStock);
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
    
    public static String getItemName(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lItemName="";
        
        try {
            tmpConn=data.getConn(clsFinYear.getDBURL(pCompanyID,EITLERPGLOBAL.FinYearFrom));
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lItemName=rsTmp.getString("ITEM_DESCRIPTION");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lItemName;
        }
        catch(Exception e) {
            return lItemName;
        }
    }

         public static String getHSN(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lHSN="";
        
        try {
            tmpConn=data.getConn(clsFinYear.getDBURL(pCompanyID,EITLERPGLOBAL.FinYearFrom));
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT HSN_SAC_CODE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lHSN=rsTmp.getString("HSN_SAC_CODE");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lHSN;
        }
        catch(Exception e) {
            return lHSN;
        }
    }
    
public static String getHsnSacCode(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lHsnSacCode="";
        
        try {
            tmpConn=data.getConn(clsFinYear.getDBURL(pCompanyID,EITLERPGLOBAL.FinYearFrom));
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT HSN_SAC_CODE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lHsnSacCode=rsTmp.getString("HSN_SAC_CODE");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lHsnSacCode;
        }
        catch(Exception e) {
            return lHsnSacCode;
        }
    }
    
    
    
    public static double getAvgRate(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double AvgRate=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT UNIT_RATE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                AvgRate=rsTmp.getDouble("AVG_RATE");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return AvgRate;
        }
        catch(Exception e) {
            return AvgRate;
        }
    }
    
    public static boolean IsOneTime(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        boolean oneTime=false;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ONETIME FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                oneTime=rsTmp.getBoolean("ONETIME");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return oneTime;
        }
        catch(Exception e) {
            return oneTime;
        }
    }
    
    
    
    public static boolean RNDApprovalRequired(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        boolean Required=false;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT RND_APPROVAL FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Required=rsTmp.getBoolean("RND_APPROVAL");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return Required;
        }
        catch(Exception e) {
            return Required;
        }
    }
    
    
    public static boolean IsNewItem(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        boolean IsNew=false;
        String strSQL="";
        
        try {
            tmpConn=data.getConn();
            
            strSQL="SELECT IF(CREATED_DATE IS NULL,'0000-00-00',CREATED_DATE) AS CREATED_DATE, DATE_FORMAT(SYSDATE(),'%Y-%m-%d') AS CUR_DATE FROM D_INV_ITEM_MASTER WHERE ITEM_ID='"+pItemID+"'";
            
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                String CreatedDate=rsTmp.getString("CREATED_DATE");
                String CurrentDate=rsTmp.getString("CUR_DATE");
                
                int DayDiff=EITLERPGLOBAL.getDayDifference(CurrentDate, CreatedDate,"yyyy-MM-dd");
                
                String strDiff=Integer.toString(DayDiff);
                
                if(strDiff.substring(0,1).equals("-")) {
                    DayDiff=Integer.parseInt(strDiff.substring(1));
                }
                
                
                if(DayDiff<180) {
                    IsNew=true;
                }
                
                
                
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return IsNew;
        }
        catch(Exception e) {
            
            return IsNew;
        }
    }
    
    
    public static String getItemAuthority(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String Authority="";
        
        try {
            
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT SPECIAL_APPROVAL FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Authority=rsTmp.getString("SPECIAL_APPROVAL");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return Authority;
        }
        catch(Exception e) {
            return Authority;
        }
    }
    
    
    public static boolean IsItemExcisable(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        boolean Excisable=false;
        
        try {
            
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT EXCISE_APPLICABLE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Excisable=rsTmp.getBoolean("EXCISE_APPLICABLE");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return Excisable;
        }
        catch(Exception e) {
            return Excisable;
        }
    }
    
    
    public static String getItemName(int pCompanyID,String pItemID,String pURL) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lItemName="";
        
        try {
            tmpConn=data.getConn(pURL);
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lItemName=rsTmp.getString("ITEM_DESCRIPTION");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lItemName;
        }
        catch(Exception e) {
            return lItemName;
        }
    }
    
    
    
    public static long getItemSystemID(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        long ItemSysID=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ITEM_SYS_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ItemSysID=rsTmp.getLong("ITEM_SYS_ID");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return ItemSysID;
        }
        catch(Exception e) {
            return ItemSysID;
        }
    }
    
    
    public static double getToleranceLimit(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double ToleranceLimit=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT TOLERANCE_LIMIT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ToleranceLimit=rsTmp.getDouble("TOLERANCE_LIMIT");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return ToleranceLimit;
        }
        catch(Exception e) {
            return ToleranceLimit;
        }
    }
    
    
    public static String getBOESrNo(int pCompanyID,String pItemID,String pBOENo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lBOESrNo="";
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT BOE_SR_NO FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID.trim()+"' AND BOE_NO='"+pBOENo.trim()+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lBOESrNo=rsTmp.getString("BOE_SR_NO");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lBOESrNo;
        }
        catch(Exception e) {
            return lBOESrNo;
        }
    }
    
    
    public static String getBOEDate(int pCompanyID,String pItemID,String pBOENo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lBOEDate="";
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT BOE_DATE FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID.trim()+"' AND BOE_NO='"+pBOENo.trim()+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lBOEDate=rsTmp.getString("BOE_DATE");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lBOEDate;
        }
        catch(Exception e) {
            return lBOEDate;
        }
    }
    
    
    public static int getItemUnit(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        int lItemUnit=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT UNIT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lItemUnit=rsTmp.getInt("UNIT");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lItemUnit;
        }
        catch(Exception e) {
            return lItemUnit;
        }
    }
    
    
    public static double getRate(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double lItemRate=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT UNIT_RATE FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lItemRate=rsTmp.getDouble("UNIT_RATE");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lItemRate;
        }
        catch(Exception e) {
            return lItemRate;
        }
    }
    
    public static double getOnHandQty_H(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double lItemRate=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ON_HAND_QTY,ALLOCATED_QTY FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lItemRate=rsTmp.getDouble("ON_HAND_QTY") - rsTmp.getDouble("ALLOCATED_QTY");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lItemRate;
        }
        catch(Exception e) {
            return lItemRate;
        }
    }
    
    public static String getItemWareHouseID(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lWareHouseID="";
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT WAREHOUSE_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lWareHouseID=rsTmp.getString("WAREHOUSE_ID");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lWareHouseID;
        }
        catch(Exception e) {
            return lWareHouseID;
        }
    }
    
    
    public static String getItemLocationID(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lLocationID="";
        
        try {
            //tmpConn=data.getConn("");
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT LOCATION_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lLocationID=rsTmp.getString("LOCATION_ID");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lLocationID;
        }
        catch(Exception e) {
            return lLocationID;
        }
    }
    
    
    public static boolean getMaintainStock(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        boolean Maintain=false;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT MAINTAIN_STOCK FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Maintain=rsTmp.getBoolean("MAINTAIN_STOCK");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return Maintain;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static double getMaxQty(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double MaxQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT MAX_QTY FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MaxQty=EITLERPGLOBAL.round(rsTmp.getDouble("MAX_QTY"),3);
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return MaxQty;
        }
        catch(Exception e) {
            return MaxQty;
        }
    }
    
    
    public static double getRecommConsumption(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double MaxQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT RECOMM_CONSUMPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MaxQty=rsTmp.getDouble("RECOMM_CONSUMPTION");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return MaxQty;
        }
        catch(Exception e) {
            return MaxQty;
        }
    }
    
    
    
    public static double getRecommLevel(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double MaxQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT RECOMM_LEVEL FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MaxQty=rsTmp.getDouble("RECOMM_LEVEL");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return MaxQty;
        }
        catch(Exception e) {
            return MaxQty;
        }
    }
    
    public static double getMinQty(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double MinQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT MIN_QTY FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MinQty=EITLERPGLOBAL.round(rsTmp.getDouble("MIN_QTY"),3);
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return MinQty;
        }
        catch(Exception e) {
            return MinQty;
        }
    }
    
    public static double getAvailableQty(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double AvailableQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT SUM(AVAILABLE_QTY) AS SUMQTY FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                AvailableQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return AvailableQty;
        }
        catch(Exception e) {
            return AvailableQty;
        }
    }
    
    public static double getAllocatedQty(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double AllocatedQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ALLOCATED_QTY AS SUMQTY FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                AllocatedQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return AllocatedQty;
        }
        catch(Exception e) {
            return AllocatedQty;
        }
    }
    
    public static double getAvgConsumption(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double AvgQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT AVG_CONSUMPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                AvgQty=rsTmp.getDouble("AVG_CONSUMPTION");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return AvgQty;
        }
        catch(Exception e) {
            return AvgQty;
        }
    }
    
    public static double getAvgConsumption(int pCompanyID,String pItemID,String Type,int Year_Month) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double AvgQty=0;
        try {
            int nToFinYear = EITLERPGLOBAL.FinYearFrom;
            int nFromFinYear = EITLERPGLOBAL.FinYearFrom - Year_Month;
            String strQry = "";
            if (Type.trim().equals("YEARLY")) {
                strQry = "SELECT SUM(B.QTY)/"+Year_Month+" AS AVG_CONSUMPTION FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B "+
                " WHERE A.COMPANY_ID="+pCompanyID+" AND A.APPROVED=1 AND A.CANCELED=0 AND A.COMPANY_ID=B.COMPANY_ID AND " +
                " A.ISSUE_NO=B.ISSUE_NO AND B.ITEM_CODE LIKE '"+pItemID+"%' AND A.ISSUE_DATE <='"+nToFinYear+"-03-31' " +
                " AND A.ISSUE_DATE >= '"+nFromFinYear+"-04-01' ";
            }
            else if (Type.trim().equals("MONTHLY")) {
                strQry = "SELECT SUM(B.QTY)/12 AS AVG_CONSUMPTION FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B "+
                " WHERE A.COMPANY_ID="+pCompanyID+" AND A.APPROVED=1 AND A.CANCELED=0 AND A.COMPANY_ID=B.COMPANY_ID AND " +
                " A.ISSUE_NO=B.ISSUE_NO AND B.ITEM_CODE LIKE '"+pItemID+"%' AND A.ISSUE_DATE <='"+nToFinYear+"-03-31' " +
                " AND A.ISSUE_DATE >= '"+nFromFinYear+"-04-01' ";
            }
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery(strQry);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                AvgQty=rsTmp.getDouble("AVG_CONSUMPTION");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            return AvgQty;
        }
        catch(Exception e) {
            return AvgQty;
        }
    }
    
    
    
    
    public static double getOnHandQtyOn(String pItemID,String pBOENo,String pDate) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double OnHandQty=0;
        
        try {
            
            
            long StockEntryNo=0;
            String StockEntryDate="";
            String strSQL="";
            
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            //================================================================//
            
            
            // ============== Calculating Opening Qty and Rate ===============//
            double OpeningQty=0;
            
            strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+pItemID+"' AND ENTRY_NO="+StockEntryNo+" AND BOE_NO='"+pBOENo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=rsTmp.getDouble("OPENING_QTY");
            }
            
            // Get the Inwards from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+pDate+"' AND B.ITEM_ID='"+pItemID+"' AND B.BOE_NO='"+pBOENo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            // Get the Issues from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+pDate+"' AND B.ITEM_CODE='"+pItemID+"' AND B.BOE_NO='"+pBOENo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            // Get the Issues from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+pDate+"' AND B.ITEM_ID='"+pItemID+"' AND B.BOE_NO='"+pBOENo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            // =================== End Calculating Opening Stock Qty =====================//
            
            
            double ReceiptQty=0;
            double IssueQty=0;
            
            //================= Calculating Total Receipt during the period ===============//
            rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS RCP_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND ITEM_ID='"+pItemID+"' AND BOE_NO='"+pBOENo+"' AND GRN_DATE>='"+pDate+"' AND GRN_DATE<='"+pDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ReceiptQty=rsTmp.getDouble("RCP_QTY");
            }
            //=============================================================================//
            
            
            //============== Calculating Total Issue during the period =====================//
            rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY))-IF(SUM(EXCESS_ISSUE_QTY) IS NULL,0,SUM(EXCESS_ISSUE_QTY)) AS ISS_QTY FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO  AND A.CANCELED=0 AND A.APPROVED=1 AND ITEM_CODE='"+pItemID+"' AND BOE_NO='"+pBOENo+"' AND ISSUE_DATE>='"+pDate+"' AND ISSUE_DATE<='"+pDate+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IssueQty=rsTmp.getDouble("ISS_QTY");
            }
            //==============================================================================//
            
            
            //============== Calculating Total Issue during the period =====================//
            rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS ISS_QTY FROM D_INV_STM_HEADER A, D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO  AND A.CANCELLED=0 AND A.APPROVED=1 AND ITEM_ID='"+pItemID+"' AND BOE_NO='"+pBOENo+"' AND STM_DATE>='"+pDate+"' AND STM_DATE<='"+pDate+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IssueQty+=rsTmp.getDouble("ISS_QTY");
            }
            //==============================================================================//
            
            
            //============== Calculating Closing Stock ===============//
            double OpeningStock=OpeningQty;
            double ClosingStock=OpeningStock+(ReceiptQty-IssueQty);
            //=======================================================//
            
            
            
            return ClosingStock;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static double getOnHandQty(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double OnHandQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT SUM(ON_HAND_QTY) AS SUMQTY FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND LOT_NO='X'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OnHandQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return OnHandQty;
        }
        catch(Exception e) {
            return OnHandQty;
        }
    }
    
    
    public static double getOnHandQty(int pCompanyID,String pItemID,String pBOENo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double OnHandQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT SUM(ON_HAND_QTY) AS SUMQTY FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND BOE_NO='"+pBOENo+"' AND LOT_NO='X'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OnHandQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return OnHandQty;
        }
        catch(Exception e) {
            return OnHandQty;
        }
    }
    
    
    public static double getOnHandQty(int pCompanyID,String pItemID,String pBOENo,String pLotNo) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double OnHandQty=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT SUM(ON_HAND_QTY) AS SUMQTY FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND BOE_NO='"+pBOENo+"' AND LOT_NO='"+pLotNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OnHandQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return OnHandQty;
        }
        catch(Exception e) {
            return OnHandQty;
        }
    }
    
    
    public static String getLastSupplier(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String SuppID="";
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String strSQL="SELECT D_PUR_PO_HEADER.SUPP_ID FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_PUR_PO_HEADER.PO_DATE DESC";
            rsTmp=Stmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SuppID=rsTmp.getString("SUPP_ID");
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return SuppID;
        }
        catch(Exception e) {
            return SuppID;
        }
    }
    
    
    public static boolean IsValidItemID(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ITEM_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND APPROVED=1 AND BLOCKED=0");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                
                return true;
            }
            else {
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsValidItemID(int pCompanyID,String pItemID,String pdburl) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        try {
            tmpConn=data.getConn(pdburl);
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ITEM_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND ITEM_ID='"+pItemID+"' AND APPROVED=1 AND BLOCKED=0");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                return true;
            }
            else {
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsEntryExist(int pCompanyID,String pItemID,String pBOENo,String pLotNo,String pWareHouseID,String pLocationID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT ITEM_ID FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pItemID.trim()+"' AND WAREHOUSE_ID='"+pWareHouseID.trim()+"' AND LOCATION_ID='"+pLocationID.trim()+"' AND BOE_NO='"+pBOENo.trim()+"' AND LOT_NO='"+pLotNo.trim()+"'");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                
                return true;
            }
            else {
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsEntryExist(int pCompanyID,String pItemID,String pBOENo,String pWareHouseID,String pLocationID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            System.out.println("SELECT ITEM_ID FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pItemID.trim()+"' AND WAREHOUSE_ID='"+pWareHouseID.trim()+"' AND LOCATION_ID='"+pLocationID.trim()+"' AND BOE_NO='"+pBOENo.trim()+"'");
            
            rsTmp=Stmt.executeQuery("SELECT ITEM_ID FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pItemID.trim()+"' AND WAREHOUSE_ID='"+pWareHouseID.trim()+"' AND LOCATION_ID='"+pLocationID.trim()+"' AND BOE_NO='"+pBOENo.trim()+"'");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                
                return true;
            }
            else {
                
                //tmpConn.close();
                Stmt.close();
                rsTmp.close();
                
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static double getLastLeadTime(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double AllocatedQty=0;
        java.sql.Date IndentDate=null,MIRDate=null;
        
        try {
            tmpConn=data.getConn();
            
            //Get last Indent whose PO is made.
            clsIndent ObjIndent=(clsIndent)clsIndent.getLastIndent(pCompanyID, pItemID);
            String IndentNo=(String)ObjIndent.getAttribute("INDENT_NO").getObj();
            IndentDate=java.sql.Date.valueOf((String)ObjIndent.getAttribute("INDENT_DATE").getObj());
            
            //Get the Last PO
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String strSQL="SELECT D_PUR_PO_HEADER.PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_DETAIL.INDENT_NO='"+IndentNo+"' AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID;
            rsTmp=Stmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //----- PO Found ----//
                String PONo=rsTmp.getString("PO_NO");
                
                //----- Find the MIR --
                Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_HEADER.APPROVED=1 AND D_INV_MIR_HEADER.CANCELLED=0 AND D_INV_MIR_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.PO_NO='"+PONo+"' AND D_INV_MIR_DETAIL.ITEM_ID='"+pItemID+"'";
                rsTmp=Stmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    MIRDate=java.sql.Date.valueOf(rsTmp.getString("MIR_DATE"));
                }
                
                
            }
            
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return EITLERPGLOBAL.DateDiff(IndentDate, MIRDate);
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+pDocNo+"' ORDER BY REVISION_NO");
            
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
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT ITEM_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pDocNo+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
            
        }
        
        return canCancel;
        
    }
    
    
    public static boolean CancelDoc(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pDocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=1;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_ITEM_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pDocNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    
    private clsItemStockInfo getItemStockInfo(String pItemID,String pFromDate,String pToDate) {
        
        clsItemStockInfo objItemStock=new clsItemStockInfo();
        objItemStock.ItemID=pItemID;
        objItemStock.FromDate=pFromDate;
        objItemStock.ToDate=pToDate;
        
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue;
        String strSQL="";
        String strCondition="";
        
        //======== Some Report writing Fields ==========//
        String strLine="";
        String ItemID="";
        String ItemName="";
        String UnitName="";
        double OpeningQty=0;
        double OpeningRate=0;
        double OpeningValue=0;
        double InwardQty=0;
        double InwardRate=0;
        double InwardValue=0;
        double OutwardQty=0;
        double OutwardRate=0;
        double OutwardValue=0;
        double ClosingQty=0;
        double ClosingRate=0;
        double ClosingValue=0;
        double IssueQty=0;
        double IssueValue=0;
        double IssueRate=0;
        double DiffQty=0;
        boolean Done=false;
        
        String strOpeningQty="";
        String strOpeningRate="";
        String strOpeningValue="";
        String strRcptQty="";
        String strRcptRate="";
        String strRcptValue="";
        String strIssueQty="";
        String strIssueRate="";
        String strIssueValue="";
        String strClosingQty="";
        String strClosingRate="";
        String strClosingValue="";
        String strDocNo="";
        String strDocDate="";
        
        int Max=0;
        int Min=0;
        int Row=0;
        
        long StockEntryNo=0;
        String StockEntryDate="";
        
        try {
            
            if(pFromDate.trim().equals("")||pToDate.trim().equals("")) {
                return objItemStock;
            }
            
            if((!EITLERPGLOBAL.isDate(pFromDate))||(!EITLERPGLOBAL.isDate(pToDate))) {
                JOptionPane.showMessageDialog(null,"Invalid date. Please enter valid dates");
                return objItemStock;
            }
            
            
            //Prepare our temp table for temporary query //
            data.Execute("DELETE FROM STOCK_LEDGER_TEMP");
            
            
            strCondition="INSERT INTO STOCK_LEDGER_TEMP (ITEM_ID) ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_ID) FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(pFromDate)+"' ";
            strCondition=strCondition+" UNION ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_CODE) FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(pToDate)+"'";
            
            
            data.Execute(strCondition);
            
            strCondition=" AND ITEM_ID='"+pItemID+"'";
            
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+EITLERPGLOBAL.formatDateDB(pFromDate)+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            //================================================================//
            
            
            //========= Get the count ============//
            strSQL="SELECT COUNT(*) AS THECOUNT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAINTAIN_STOCK=1 AND APPROVED=1 AND CANCELLED=0 "+strCondition;
            rsItem=data.getResult(strSQL);
            
            rsItem.first();
            
            if(rsItem.getRow()>0) {
                Max=rsItem.getInt("THECOUNT");
            }
            
            //========= Create a text file ==============//
            strSQL="SELECT ITEM_ID,ITEM_DESCRIPTION,UNIT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAINTAIN_STOCK=1 AND APPROVED=1 AND CANCELLED=0 "+strCondition;
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            if(rsItem.getRow()<=0) {
                return objItemStock;
            }
            
            //Loop through each item
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                
                //Fill Up the variables first
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                ItemName=rsItem.getString("ITEM_DESCRIPTION");
                
                
                if(ItemName.length()>20) {
                    ItemName=ItemName.substring(0,20);
                }
                
                UnitName=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT",rsItem.getInt("UNIT"));
                
                OpeningQty=0;
                OpeningRate=0;
                OpeningValue=0;
                
                //First Find the latest cut-off closing stock.
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_QTY"),2);
                    OpeningRate=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_RATE"),2);
                    OpeningValue=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_VALUE"),2);
                    
                    
                    if(OpeningQty==0) {
                        OpeningRate=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                    }
                    
                    
                    //Initialize the Closing Qty.
                    ClosingQty=OpeningQty;
                    ClosingValue=OpeningValue;
                    ClosingRate=ClosingRate;
                }
                
                data.Execute("UPDATE D_INV_GRN_DETAIL SET TMP_ISSUED_QTY=0 WHERE GRN_NO IN (SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND GRN_DATE>='"+StockEntryDate+"' AND GRN_DATE<='"+EITLERPGLOBAL.formatDateDB(pToDate)+"') AND ITEM_ID='"+pItemID+"'");
                
                //Take the record between Cut-off stock date and till from date specified here.
                strSQL="SELECT A.ISSUE_NO,B.SR_NO,A.ISSUE_DATE,B.ITEM_CODE,ITEM_EXTRA_DESC,WAREHOUSE_ID,LOCATION_ID,BOE_NO,QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND A.ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(pToDate)+"' AND ITEM_CODE='"+pItemID+"' ORDER BY ISSUE_DATE";
                
                rsIssue=data.getResult(strSQL);
                rsIssue.first();
                
                while((!rsIssue.isAfterLast())&&rsIssue.getRow()>0) {
                    
                    String IssueDate=rsIssue.getString("ISSUE_DATE");
                    IssueQty=rsIssue.getDouble("QTY");
                    String IssueNo=rsIssue.getString("ISSUE_NO");
                    int IssueSrNo=rsIssue.getInt("SR_NO");
                    
                    if(IssueQty<=OpeningQty) {
                        OpeningQty=OpeningQty-IssueQty;
                        IssueValue=EITLERPGLOBAL.round(OpeningRate*IssueQty,2);
                        
                        //Update the rate in Issue.
                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET RATE="+OpeningRate+", ISSUE_VALUE="+IssueValue+" WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
                        //No GRN Updation required now.
                    }
                    else {
                        //Initialize the Issue Value
                        IssueValue=0;
                        IssueRate=0;
                        
                        //Get the Difference
                        DiffQty=OpeningQty;
                        
                        
                        IssueValue=EITLERPGLOBAL.round(DiffQty*OpeningRate,2);
                        
                        //Decrease the IssueQty
                        IssueQty=IssueQty-OpeningQty;
                        
                        OpeningQty=0;
                        
                        //Now for rest of the Qty. we have to search the GRN in FIFO order.
                        strSQL="SELECT A.GRN_NO,B.SR_NO,A.GRN_DATE,B.LANDED_RATE,B.QTY,B.TMP_ISSUED_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.TMP_ISSUED_QTY<B.QTY AND ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+StockEntryDate+"' AND A.GRN_DATE<='"+IssueDate+"' ORDER BY A.GRN_DATE";
                        rsRcpt=data.getResult(strSQL);
                        rsRcpt.first();
                        
                        if(rsRcpt.getRow()>0) {
                            Done=false;
                            while((!Done)&&(!rsRcpt.isAfterLast())) {
                                String GRNNo=rsRcpt.getString("GRN_NO");
                                int GRNSrNo=rsRcpt.getInt("SR_NO");
                                double Qty=rsRcpt.getDouble("QTY")-rsRcpt.getDouble("TMP_ISSUED_QTY");
                                double Rate=rsRcpt.getDouble("LANDED_RATE");
                                
                                if(IssueQty<Qty) //Is GRN Qty sufficient to fulfill the issue ?
                                {
                                    DiffQty=Qty-IssueQty;
                                    
                                    IssueValue=EITLERPGLOBAL.round(IssueValue+(IssueQty*Rate),2);
                                    
                                    IssueQty=0;
                                    
                                    //We have to update the GRN.
                                    data.Execute("UPDATE D_INV_GRN_DETAIL SET TMP_ISSUED_QTY=TMP_ISSUED_QTY+"+DiffQty+" WHERE GRN_NO='"+GRNNo+"' AND SR_NO="+GRNSrNo);
                                }
                                else //GRN Qty. is not sufficient
                                {
                                    DiffQty=IssueQty-Qty;
                                    
                                    IssueValue=EITLERPGLOBAL.round(IssueValue+(Qty*Rate),2);
                                    
                                    IssueQty=IssueQty-Qty;
                                    
                                    //Update the GRN with full qty.
                                    data.Execute("UPDATE D_INV_GRN_DETAIL SET TMP_ISSUED_QTY="+rsRcpt.getDouble("QTY")+" WHERE GRN_NO='"+GRNNo+"' AND SR_NO="+GRNSrNo);
                                }
                                
                                if(rsRcpt.isAfterLast()||IssueQty==0) {
                                    Done=true;
                                }
                                
                                if(!rsRcpt.isAfterLast()) {
                                    rsRcpt.next();
                                }
                            }
                        }
                        
                        
                        //Now check that Full Issue Qty has been used
                        if(IssueQty>0) {
                            //Not enough receipts found for this much issue qty.
                            
                            //In this case Take the Avg. Rate
                            if((rsIssue.getDouble("QTY")-IssueQty)==0) {
                                IssueRate=0;
                            }
                            else {
                                IssueRate=EITLERPGLOBAL.round(IssueValue/(rsIssue.getDouble("QTY")-IssueQty),2);
                            }
                            
                            
                            IssueValue=EITLERPGLOBAL.round(rsIssue.getDouble("QTY")*IssueRate,2);
                            
                        }
                        else {
                            
                            //Otherwise devide the value
                            IssueRate=EITLERPGLOBAL.round(IssueValue/rsIssue.getDouble("QTY"),2);
                        }
                        
                        //Update the Issue itself
                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET RATE="+IssueRate+", ISSUE_VALUE="+IssueValue+" WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
                    }
                    
                    rsIssue.next();
                }
                
                rsItem.next();
            }
            
            
            
            
            
            //========= Create a text file ==============//
            strSQL="SELECT ITEM_ID,ITEM_DESCRIPTION,UNIT FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAINTAIN_STOCK=1 AND APPROVED=1 AND CANCELLED=0 "+strCondition;
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            
            
            //Fill Up the variables first
            ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
            ItemName=rsItem.getString("ITEM_DESCRIPTION");
            
            if(ItemName.length()>20) {
                ItemName=ItemName.substring(0,20);
            }
            
            
            //Now Decide the Opening Stock and Value
            OpeningQty=0;
            OpeningValue=0;
            
            strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=rsTmp.getDouble("OPENING_QTY");
                OpeningValue=rsTmp.getDouble("OPENING_VALUE");
                
                if(OpeningQty==0) {
                    OpeningValue=0;
                    OpeningRate=0;
                }
                else {
                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                }
                
            }
            
            // Get the Inwards from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>='"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(pFromDate)+"' AND B.ITEM_ID='"+pItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            // Get the Issues from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>='"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(pFromDate)+"' AND B.ITEM_CODE='"+pItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            //we have Opening Stock and Qty.
            //Take the Average Rate
            if(OpeningQty==0) {
                OpeningRate=0;
            }
            else {
                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
            }
            
            ClosingQty=OpeningQty;
            ClosingValue=OpeningValue;
            ClosingRate=OpeningRate;
            
            
            //Get the transactions between date
            strSQL="SELECT A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,B.QTY,B.LANDED_RATE*B.QTY AS VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(pFromDate)+"' AND GRN_DATE<='"+EITLERPGLOBAL.formatDateDB(pToDate)+"'";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+"  SELECT A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,B.QTY,B.ISSUE_VALUE AS VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+pItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(pFromDate)+"' AND ISSUE_DATE<='"+EITLERPGLOBAL.formatDateDB(pToDate)+"'";
            strSQL=strSQL+"  ORDER BY DOC_DATE";
            
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    String Operation=rsTmp.getString("OPERATION");
                    
                    if(Operation.equals("+")) //Inward Found
                    {
                        InwardQty=InwardQty+rsTmp.getDouble("QTY");
                        InwardValue=InwardValue+rsTmp.getDouble("VALUE");
                        
                        ClosingQty=ClosingQty+rsTmp.getDouble("QTY");
                        ClosingValue=ClosingValue+rsTmp.getDouble("VALUE");
                    }
                    
                    
                    
                    if(Operation.equals("-")) {
                        
                        OutwardQty=OutwardQty+rsTmp.getDouble("QTY");
                        OutwardValue=OutwardValue+rsTmp.getDouble("VALUE");
                        
                        ClosingQty=ClosingQty-rsTmp.getDouble("QTY");
                        ClosingValue=ClosingValue-rsTmp.getDouble("VALUE");
                    }
                    
                    
                    rsTmp.next();
                    
                }
            }
            
            
            //update the object
            objItemStock.OpeningQty=OpeningQty;
            objItemStock.OpeningValue=OpeningValue;
            objItemStock.ReceiptQty=InwardQty;
            objItemStock.ReceiptValue=InwardValue;
            objItemStock.IssueQty=OutwardQty;
            objItemStock.IssueValue=OutwardValue;
            objItemStock.ClosingQty=ClosingQty;
            objItemStock.ClosingValue=ClosingValue;
            
            return objItemStock;
        }
        catch(Exception e) {
            
            return objItemStock;
        }
        
    }
    
    
    public static double getOnHandQtyOn(String pItemID,String pDate) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        double OnHandQty=0;
        
        try {
            
            
            long StockEntryNo=0;
            String StockEntryDate="";
            String strSQL="";
            
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            //================================================================//
            
            
            // ============== Calculating Opening Qty and Rate ===============//
            double OpeningQty=0;
            
            strSQL="SELECT SUM(OPENING_QTY) AS OPENING_QTY FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+pItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=rsTmp.getDouble("OPENING_QTY");
            }
            
            // Get the Inwards from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+pDate+"' AND B.ITEM_ID='"+pItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            // Get the Issues from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+pDate+"' AND B.ITEM_CODE='"+pItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            // Get the Issues from opening stock date to upto from date
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+pDate+"' AND B.ITEM_ID='"+pItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            // =================== End Calculating Opening Stock Qty =====================//
            
            
            double ReceiptQty=0;
            double IssueQty=0;
            
            //================= Calculating Total Receipt during the period ===============//
            rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS RCP_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND ITEM_ID='"+pItemID+"' AND GRN_DATE>='"+StockEntryDate+"' AND GRN_DATE<='"+pDate+"'");
            //rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS RCP_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND ITEM_ID='"+pItemID+"' AND GRN_DATE>='"+pDate+"' AND GRN_DATE<='"+pDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ReceiptQty=rsTmp.getDouble("RCP_QTY");
            }
            //=============================================================================//
            
            
            //============== Calculating Total Issue during the period =====================//
            rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY))-IF(SUM(EXCESS_ISSUE_QTY) IS NULL,0,SUM(EXCESS_ISSUE_QTY)) AS ISS_QTY FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO  AND A.CANCELED=0 AND A.APPROVED=1 AND ITEM_CODE='"+pItemID+"' AND ISSUE_DATE>='"+StockEntryDate+"' AND ISSUE_DATE<='"+pDate+"' ");
            //rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY))-IF(SUM(EXCESS_ISSUE_QTY) IS NULL,0,SUM(EXCESS_ISSUE_QTY)) AS ISS_QTY FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO  AND A.CANCELED=0 AND A.APPROVED=1 AND ITEM_CODE='"+pItemID+"' AND ISSUE_DATE>='"+pDate+"' AND ISSUE_DATE<='"+pDate+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IssueQty=rsTmp.getDouble("ISS_QTY");
            }
            //==============================================================================//
            
            
            //============== Calculating Total Issue during the period =====================//
            rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS ISS_QTY FROM D_INV_STM_HEADER A, D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO  AND A.CANCELLED=0 AND A.APPROVED=1 AND ITEM_ID='"+pItemID+"' AND STM_DATE>='"+StockEntryDate+"' AND STM_DATE<='"+pDate+"' ");
            //rsTmp=data.getResult("SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS ISS_QTY FROM D_INV_STM_HEADER A, D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO  AND A.CANCELLED=0 AND A.APPROVED=1 AND ITEM_ID='"+pItemID+"' AND STM_DATE>='"+pDate+"' AND STM_DATE<='"+pDate+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IssueQty+=rsTmp.getDouble("ISS_QTY");
            }
            //==============================================================================//
            
            
            //============== Calculating Closing Stock ===============//
            double OpeningStock=OpeningQty;
            double ClosingStock=OpeningStock+(ReceiptQty-IssueQty);
            //=======================================================//
            
            
            
            return ClosingStock;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static String getMappedItemID(int pCompanyID,String pItemID) {
        ResultSet rsTmp;
        String ItemID="";
        
        try {
            rsTmp=data.getResult("SELECT SECOND_ITEM_ID FROM D_COM_ITEM_MAPPING WHERE COMPANY_ID="+pCompanyID+" AND FIRST_ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ItemID=rsTmp.getString("SECOND_ITEM_ID");
            }
            
            
        }
        catch(Exception e) {
            
        }
        return ItemID;
        
    }
    
    //Komal ***
    private boolean IsAlphabet(String str) {
        char str1 = str.charAt(0);
        char str2 = str.charAt(1);
        int nstr1 = (int)str1;
        int nstr2 = (int)str2;
        
        if (! ((nstr1 >= 65 && nstr1<=90) || (nstr1 >= 97 && nstr1<=122))) {
            return false;
        }
        if (! ((nstr2 >= 65 && nstr2<=90) || (nstr2 >= 97 && nstr2<=122))) {
            return false;
        }
        return true;
    }
    //*** Komal
    
    //*** Komal
    private boolean Validate() {
        //** Validations **//
        
        
        String ItemID = (String)getAttribute("ITEM_ID").getObj();
        if (! ItemID.equals("")) {
            int len = ItemID.length();
            int ndot = ItemID.indexOf('.');
            
            //8 digit + 1 dot + 2 digit
            if (len < 8) {
                LastError=" Please enter Valid Item Code";
                return false;
            }
            //double mCategory=(double) getAttribute("CATEGORY_ID").getVal();
            if (len == 8) {
                if (getAttribute("CATEGORY_ID").getVal()==1) {
                    if (!EITLERPGLOBAL.IsNumber(ItemID)) {
                        LastError=" Item Code Must be Numeric";
                        return false;
                    }
                }
                
            }
            if (len == 9) {
                LastError=" Please enter Valid Item Code";
                return false;
            }
            if (len > 11) {
                LastError=" Please enter Valid Item Code";
                return false;
            }
            if ((len == 10) || (len == 11)) {
                if (ndot >= 0) {
                    if (ndot == 8) {
                        boolean chk1 = EITLERPGLOBAL.IsNumber(ItemID.substring(0,8));
                        //boolean chk2 = EITLERPGLOBAL.IsNumber(ItemID.substring(9));
                        boolean chk2 = true;
                        
                        if (! ((chk1 == true) && (chk2 == true))) {
                            LastError=" Please enter valid Item Code";
                            return false;
                        }
                    }
                    else if (ndot >= 0 ) {
                        LastError=" Please enter valid Item Code";
                        return false;
                    }
                }
            }
            //end 8 digit + 1 dot + 2 digit
            
            //2 char + 8 digit
            if ((len == 10) && (ndot < 0)) {
                String spart1= ItemID.substring(0,2);
                String spart2 =ItemID.substring(3);
                
                boolean chk1 = IsAlphabet(spart1);
                boolean chk2 = EITLERPGLOBAL.IsNumber(spart2);
                
                if (! ((chk1 == true) && (chk2 == true))) {
                    LastError=" Please enter valid Item Code";
                    return false;
                }
            }
            //end 2 char + 8 digit
        }
        else {
            LastError="Please enter Item Code";
            return false;
        }
        
        
        
        
        return true;
    }
    //Komal ***
    
}
