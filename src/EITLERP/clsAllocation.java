/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP;


import java.util.*;
import java.sql.*;
import java.net.*;

/**
 *
 * @author  nrpatel
 * @version
 */

public class clsAllocation {
    
    private HashMap props;
    public boolean Ready = false;
    
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
    
    
    /** Creates new clsNoDataObject */
    public clsAllocation() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(0));
        props.put("DOC_DATE",new Variant(""));
        props.put("ITEM_ID",new Variant(""));
        props.put("STOCK_QTY",new Variant(0));
        props.put("ALLOCATED_QTY",new Variant(0));
        props.put("ISSUED_QTY",new Variant(0));
        props.put("DEPT_ID",new Variant(0));
        props.put("ISSUE_NO",new Variant(""));
        props.put("ISSUE_SR_NO",new Variant(0));
        props.put("INDENT_NO",new Variant(""));
        props.put("INDENT_SR_NO",new Variant(0));
        props.put("STATUS",new Variant("O"));
    }
    
    public static HashMap getAllocationList(int pCompanyID,String pCondition) {
        HashMap List=new HashMap();
        
        try {
            ResultSet rsTmp;
            
            String strSQL="SELECT * FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+pCompanyID+" AND ISSUED_QTY<ALLOCATED_QTY "+pCondition;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsAllocation ObjAllocation=new clsAllocation();
                    
                    ObjAllocation.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjAllocation.setAttribute("DOC_NO",rsTmp.getLong("DOC_NO"));
                    ObjAllocation.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                    ObjAllocation.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjAllocation.setAttribute("STOCK_QTY",rsTmp.getDouble("STOCK_QTY"));
                    ObjAllocation.setAttribute("ALLOCATED_QTY",rsTmp.getDouble("ALLOCATED_QTY"));
                    ObjAllocation.setAttribute("ISSUED_QTY",rsTmp.getDouble("ISSUED_QTY"));
                    ObjAllocation.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjAllocation.setAttribute("ISSUE_NO",rsTmp.getString("ISSUE_NO"));
                    ObjAllocation.setAttribute("ISSUE_SR_NO",rsTmp.getInt("ISSUE_SR_NO"));
                    ObjAllocation.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                    ObjAllocation.setAttribute("INDENT_SR_NO",rsTmp.getInt("INDENT_SR_NO"));
                    ObjAllocation.setAttribute("STATUS",rsTmp.getString("STATUS"));
                    
                    List.put(Integer.toString(List.size()+1),ObjAllocation);
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    

    
    
    public static double getAllocatedQty(int pCompanyID,int pDeptID,String pItemID) {
        double AllocatedQty=0;
        
        try {
            ResultSet rsTmp;
            
            String strSQL="SELECT SUM(ALLOCATED_QTY-ISSUED_QTY) AS TOTALQTY FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_ID='"+pItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                AllocatedQty=rsTmp.getDouble("TOTALQTY");
            }
            
        }
        catch(Exception e) {
            
        }
        
        return AllocatedQty;
    }
    

    
    
    public static clsAllocation getObject(int pCompanyID,long pDocNo) {
        clsAllocation ObjAllocation=new clsAllocation();
        
        try {
            ResultSet rsTmp;
            
            String strSQL="SELECT * FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO="+pDocNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ObjAllocation.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjAllocation.setAttribute("DOC_NO",rsTmp.getLong("DOC_NO"));
                ObjAllocation.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjAllocation.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjAllocation.setAttribute("STOCK_QTY",rsTmp.getDouble("STOCK_QTY"));
                ObjAllocation.setAttribute("ALLOCATED_QTY",rsTmp.getDouble("ALLOCATED_QTY"));
                ObjAllocation.setAttribute("ISSUED_QTY",rsTmp.getDouble("ISSUED_QTY"));
                ObjAllocation.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                ObjAllocation.setAttribute("ISSUE_NO",rsTmp.getString("ISSUE_NO"));
                ObjAllocation.setAttribute("ISSUE_SR_NO",rsTmp.getInt("ISSUE_SR_NO"));
                ObjAllocation.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjAllocation.setAttribute("INDENT_SR_NO",rsTmp.getInt("INDENT_SR_NO"));
                ObjAllocation.setAttribute("STATUS",rsTmp.getString("STATUS"));
            }
            
        }
        catch(Exception e) {
            
        }
        
        return ObjAllocation;
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean freeAllocation(int pCompanyID,int pDeptID,String pItemID,double pQty,String pIssueNo,int pIssueSrNo) {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            boolean Done=false;
            double UpdatedQty=0,DeAllocatedQty=0;
            String strSQL="";
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_ID='"+pItemID+"' AND ISSUED_QTY<ALLOCATED_QTY ORDER BY DOC_NO");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                UpdatedQty=pQty;
                while((!rsTmp.isAfterLast())&&(!Done)) {
                    
                    long DocNo=rsTmp.getLong("DOC_NO");
                    double RemainingQty=rsTmp.getDouble("ALLOCATED_QTY")-rsTmp.getDouble("ISSUED_QTY");
                    
                    if(UpdatedQty>RemainingQty) {
                        //Update Full
                        strSQL="UPDATE D_COM_STOCK_ALLOCATION SET ISSUED_QTY=ISSUED_QTY+"+RemainingQty+",ISSUE_NO='"+pIssueNo+"',ISSUE_SR_NO="+pIssueSrNo+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO="+DocNo;
                        data.Execute(strSQL);
                        
                        
                        //Deduct Updated Qty
                        UpdatedQty=UpdatedQty-RemainingQty;
                        DeAllocatedQty+=RemainingQty;
                        
                    }
                    else {
                        //Update Partial
                        strSQL="UPDATE D_COM_STOCK_ALLOCATION SET ISSUED_QTY=ISSUED_QTY+"+UpdatedQty+",ISSUE_NO='"+pIssueNo+"',ISSUE_SR_NO="+pIssueSrNo+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO="+DocNo;
                        
                        data.Execute(strSQL);
                        DeAllocatedQty+=UpdatedQty;
                        Done=true;
                    }
                    
                    rsTmp.next();
                }
                
                //Now update Item Master for DeAllocation
                strSQL="UPDATE D_INV_ITEM_MASTER SET ALLOCATED_QTY=ALLOCATED_QTY-"+DeAllocatedQty+",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND ITEM_ID='"+pItemID+"'";
                data.Execute(strSQL);
                
            }
            
            //Close it down
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            
            return true;
            
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    //Allocates the stock quantity for particular department
    public boolean doAllocation() {
        try {
            
            Connection Conn;
            Statement stAllocation,stTmp;
            ResultSet rsAllocation,rsTmp;
            
            //----- First check previous allocation records --------//
            rsTmp=data.getResult("SELECT * FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+(int)getAttribute("DEPT_ID").getVal()+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"' AND INDENT_NO='"+(String)getAttribute("INDENT_NO").getObj()+"' AND INDENT_SR_NO="+(int)getAttribute("INDENT_SR_NO").getVal());
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Found. This happens because we do allocation even if indent is not approved.
                //Revert back previous effect
                double AllocatedQty=rsTmp.getDouble("ALLOCATED_QTY");
                
                
                //Now Update the Item master
                String strSQL="UPDATE D_INV_ITEM_MASTER SET ALLOCATED_QTY=ALLOCATED_QTY-"+AllocatedQty+",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"'";
                data.Execute(strSQL);
                System.out.println(strSQL);
                
                //Delete previous record
                long PrevDocNo=rsTmp.getLong("DOC_NO");
                strSQL="DELETE FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+PrevDocNo;
                data.Execute(strSQL);
            }
            //================ Revert Back Complete ================//
            
            
            //==========Make new document no. ===============//
            long DocNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_STOCK_ALLOCATION","DOC_NO")+1;
            
            Conn=data.getConn();
            stAllocation=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsAllocation=stAllocation.executeQuery("SELECT * FROM D_COM_STOCK_ALLOCATION WHERE DOC_NO=1");
            rsAllocation.first();
            
            //Add a row
            rsAllocation.moveToInsertRow();
            rsAllocation.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsAllocation.updateLong("DOC_NO",DocNo);
            rsAllocation.updateString("DOC_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsAllocation.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsAllocation.updateDouble("STOCK_QTY",getAttribute("STOCK_QTY").getVal());
            rsAllocation.updateDouble("ALLOCATED_QTY",getAttribute("ALLOCATED_QTY").getVal());
            rsAllocation.updateDouble("ISSUED_QTY",0);
            rsAllocation.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsAllocation.updateString("ISSUE_NO",(String)getAttribute("ISSUE_NO").getObj());
            rsAllocation.updateInt("ISSUE_SR_NO",(int)getAttribute("ISSUE_SR_NO").getVal());
            rsAllocation.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
            rsAllocation.updateInt("INDENT_SR_NO",(int)getAttribute("INDENT_SR_NO").getVal());
            rsAllocation.updateString("STATUS","O");
            rsAllocation.updateBoolean("CHANGED",true);
            rsAllocation.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsAllocation.insertRow();
            
            //Close it down
            rsAllocation.close();
            stAllocation.close();
            
            
            //Now Update the Item master
            String strSQL="UPDATE D_INV_ITEM_MASTER SET ALLOCATED_QTY=ALLOCATED_QTY+"+getAttribute("ALLOCATED_QTY").getVal()+",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"'";
            data.Execute(strSQL);
            
            //Close the connection
            //Conn.close();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
}
