/*
 * clsRGP.java
 *
 * Created on April 23, 2004, 3:25 PM
 */

package EITLERP.Stores;

import java.net.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import EITLERP.*;
  
/**
 * 
 * @author  nhpatel 
 * @version 
 */
public class clsRGP {

    public String LastError="";
    private ResultSet rsRGP;
    private Connection Conn;
    private Statement Stmt;

    public HashMap colLineItems;
    private HashMap props;    

    public boolean Ready = false;

    //History Related properties
    private boolean HistoryView=false;
    
   
    //Flag Indicating whether user has entered the document no.
    public boolean UserDocNo=false;
    
    public Variant getAttribute(String PropName)
    {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,int Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,double Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,float Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,boolean Value)
    {
         props.put(PropName,new Variant(Value));
    }

    
    /** Creates new clsRGP */
    public clsRGP() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("GATEPASS_DATE",new Variant(""));
        props.put("FOR_DEPT",new Variant(0));        
        props.put("GATEPASS_TYPE",new Variant(""));
        props.put("RGP_WITH_LOT",new Variant(false));
        props.put("SUPP_ID",new Variant(""));        
        props.put("MODE_TRANSPORT",new Variant(0));        
        props.put("TRANPORTER",new Variant(0));        
        props.put("TOTAL_AMOUNT",new Variant(0));    
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("REJECTED",new Variant(false));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));        
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        props.put("PACKING",new Variant(""));
        props.put("PURPOSE",new Variant(""));

        props.put("DESPATCH_MODE",new Variant(""));
        props.put("GROSS_WEIGHT",new Variant(""));
        
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
        props.put("USER_ID",new Variant(0));
        props.put("PARTY_NAME",new Variant(""));
        props.put("ADD1",new Variant(""));
        props.put("ADD2",new Variant(""));
        props.put("ADD3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("EXP_RETURN_DATE",new Variant(""));
        
        
        //Create a new object for line items
        colLineItems=new HashMap();
    }

    public boolean LoadData(long pCompanyID)
    {
      Ready=false;
      try
        {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsRGP=Stmt.executeQuery("SELECT * FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GATEPASS_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GATEPASS_NO");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }

    public void Close()
    {
      try
      {
        //Conn.close();
        Stmt.close();
        rsRGP.close();
      }
      catch(Exception e)
      {
          
      }
    }
    
    //Navigation Methods
    public boolean MoveFirst()
    {
        try
        {
          rsRGP.first();  
          setData();
          return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext()
    {
        try
        {
           if(rsRGP.isAfterLast()||rsRGP.isLast())
           {
               //Move pointer at last record
               //If it is beyond eof
               rsRGP.last();
           }
           else
           {
            rsRGP.next();
                if(rsRGP.isAfterLast())
                {
                    rsRGP.last();
                }
           }
           setData();
           return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious()
    {
        try
        {
            if(rsRGP.isFirst()||rsRGP.isBeforeFirst())
            {
               rsRGP.first(); 
            }
            else
            {
                rsRGP.previous();
                if(rsRGP.isBeforeFirst())
                {
                    rsRGP.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast()
    {
        try
        {
            rsRGP.last();
            setData();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }

   public boolean Insert(String pPrefix,String pDocno) 
    {
	Statement stTmp,stHistory,stHDetail,stHLot;
        ResultSet rsTmp,rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        String strSQL = "",Gatepassno = "",Declno = "";
        String RJNNo = "";
        int RJNSrno = 0,GatepassSrno = 0,DeclSrno = 0;
        
        try
        {                       
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_RGP_HEADER_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_RGP_HEADER WHERE GATEPASS_NO='1'");
            //rsHeader.first();
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            

            //=========== Check the Quantities entered against Rejection.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem) colLineItems.get(Integer.toString(i));

                RJNNo=(String) ObjItem.getAttribute("RJN_NO").getObj();
                RJNSrno=(int) ObjItem.getAttribute("RJN_SRNO").getVal();
                double RJNQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!RJNNo.trim().equals(""))&&(RJNSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        RJNQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND RJN_SRNO="+RJNSrno+" AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > RJNQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds RJN No. "+RJNNo+" Sr. No. "+RJNSrno+" qty "+RJNQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            //=========== Check the Quantities entered against Gatepass Requisition.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem) colLineItems.get(Integer.toString(i));

                Gatepassno=(String) ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                GatepassSrno=(int) ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                double GatepassQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Gatepassno.trim().equals(""))&&(GatepassSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        GatepassQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASSREQ_NO='"+Gatepassno+"' AND GATEPASSREQ_SRNO="+GatepassSrno+" AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > GatepassQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds GatepassNo. "+Gatepassno+" Sr. No. "+GatepassSrno+" qty "+GatepassQty+". Please verify the input.";
                        return false;
                    }
                }
            }

            //=========== Check the Quantities entered against Declaration.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem) colLineItems.get(Integer.toString(i));

                Declno=(String) ObjItem.getAttribute("DECLARATION_ID").getObj();
                DeclSrno=(int) ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                double DeclQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Declno.trim().equals(""))&&(DeclSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT RECD_QTY FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        DeclQty=rsTmp.getDouble("RECD_QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND DECLARATION_SRNO="+DeclSrno+" AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > DeclQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Decl.No "+Declno+" Sr. No. "+DeclSrno+" qty "+DeclQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("GATEPASS_TYPE").getObj();
            boolean LotItem = (boolean) getAttribute("RGP_WITH_LOT").getBool();
            
           // Stock Updataion started over here 
           //=======================================//
            if(AStatus.equals("F"))
            {
            //-------- First Update the stock -------------//
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem)colLineItems.get(Integer.toString(i));
                
                RJNNo=(String)ObjItem.getAttribute("RJN_NO").getObj();
                RJNSrno=(int)ObjItem.getAttribute("RJN_SRNO").getVal();
                double Qty=ObjItem.getAttribute("QTY").getVal();
                String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                Gatepassno=(String)ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                GatepassSrno=(int)ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                Declno=(String)ObjItem.getAttribute("DECLARATION_ID").getObj();
                DeclSrno=(int)ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                Statement tmpStmt;
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

                
                    if((!Gatepassno.trim().equals(""))&&GatepassSrno>0) {
                        strSQL="UPDATE D_INV_GATEPASS_REQ_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_GATEPASS_REQ_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!Declno.trim().equals(""))&&DeclSrno>0) {
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET RETURNED_QTY=RETURNED_QTY+"+Qty+",BAL_QTY=RECD_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!RJNNo.trim().equals(""))&&RJNSrno>0) {
                        strSQL="UPDATE D_INV_RJN_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_RJN_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"'";
                        data.Execute(strSQL);
                    }
                
                }// For Loop of Line items is completed
              } // Approval Final Approval If Comndition completed
            
              // Stock Updataion is Completed over here 
              //=======================================//
            
            //Generating new Indent No by using Max(Indent_no)+1

            
            //--------- Generate New MIR No. ------------
            if(UserDocNo) {
                rsTmp=data.getResult("SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE GATEPASS_NO='"+((String)getAttribute("GATEPASS_NO").getObj()).trim()+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    LastError="Document no. already exist. Please specify other document no.";
                    return false;
                }
            }
            else {
                setAttribute("GATEPASS_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,12, (String)getAttribute("PREFIX").getObj(),(String)getAttribute("SUFFIX").getObj(),true));
            }
            //-------------------------------------------------
            
            //And Assigning it to class property
            

            rsRGP.moveToInsertRow();
            rsRGP.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsRGP.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsRGP.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsRGP.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsRGP.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsRGP.updateBoolean("RGP_WITH_LOT",(boolean) getAttribute("RGP_WITH_LOT").getBool());
            rsRGP.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsRGP.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsRGP.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsRGP.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsRGP.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsRGP.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsRGP.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsRGP.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
//            rsRGP.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
//            rsRGP.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsRGP.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsRGP.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsRGP.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsRGP.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsRGP.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsRGP.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsRGP.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsRGP.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsRGP.updateBoolean("CHANGED",true);
            rsRGP.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsRGP.updateBoolean("APPROVED",false);
            rsRGP.updateString("APPROVED_DATE","0000-00-00");
            rsRGP.updateBoolean("REJECTED",false);
            rsRGP.updateString("REJECTED_DATE","0000-00-00");
            rsRGP.updateBoolean("CANCELED",false);
            rsRGP.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsRGP.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsRGP.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsRGP.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            rsRGP.insertRow();           

            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateBoolean("RGP_WITH_LOT",(boolean) getAttribute("RGP_WITH_LOT").getBool());
            rsHistory.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsHistory.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
//            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
//            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsHistory.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            rsHistory.insertRow();           
            
            // Inserting records in RGP Details

            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND GATEPASS_NO='1'");
            String nRGPno=(String) getAttribute("GATEPASS_NO").getObj();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            String Gatepass_type=(String) getAttribute("GATEPASS_TYPE").getObj();
            
            for(int i=1;i<=colLineItems.size();i++)
            {
                clsRGPItem ObjRGPItem=(clsRGPItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("GATEPASS_NO",nRGPno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("WAREHOUSE_ID",(String) ObjRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsTmp.updateString("LOCATION_ID",(String) ObjRGPItem.getAttribute("LOCATION_ID").getObj());
                rsTmp.updateString("ITEM_CODE",(String) ObjRGPItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateLong("UNIT",(long) ObjRGPItem.getAttribute("UNIT").getVal());
                rsTmp.updateString("RGP_DESC",(String) ObjRGPItem.getAttribute("RGP_DESC").getObj());
                rsTmp.updateDouble("QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsTmp.updateDouble("BAL_QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsTmp.updateDouble("RETURNED_QTY",0);
                rsTmp.updateDouble("RATE",ObjRGPItem.getAttribute("RATE").getVal());
                rsTmp.updateString("RJN_NO",(String) ObjRGPItem.getAttribute("RJN_NO").getObj());
                rsTmp.updateLong("RJN_SRNO",(long) ObjRGPItem.getAttribute("RJN_SRNO").getVal());
                rsTmp.updateString("GATEPASSREQ_NO",(String) ObjRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsTmp.updateLong("GATEPASSREQ_SRNO",(long) ObjRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsTmp.updateString("DECLARATION_ID",(String) ObjRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsTmp.updateLong("DECLARATION_SRNO",(long) ObjRGPItem.getAttribute("DECLARATION_SRNO").getVal());
                rsTmp.updateBoolean("CANCELED",(boolean) ObjRGPItem.getAttribute("CANCELED").getBool());
                rsTmp.updateString("REMARKS",(String) ObjRGPItem.getAttribute("REMARKS").getObj());
                rsTmp.updateLong("CREATED_BY",(long) ObjRGPItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) ObjRGPItem.getAttribute("CREATED_DATE").getObj());
//                rsTmp.updateLong("MODIFIED_BY",(long) ObjRGPItem.getAttribute("MODIFIED_BY").getVal());
//                rsTmp.updateString("MODIFIED_DATE",(String) ObjRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateString("PACKING",(String)ObjRGPItem.getAttribute("PACKING").getObj());
                rsTmp.updateDouble("RETURNED_QTY",0);
                rsTmp.updateDouble("BAL_QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();

                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("GATEPASS_NO",nRGPno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("WAREHOUSE_ID",(String) ObjRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String) ObjRGPItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("ITEM_CODE",(String) ObjRGPItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjRGPItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("RGP_DESC",(String) ObjRGPItem.getAttribute("RGP_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("BAL_QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RETURNED_QTY",0);
                rsHDetail.updateDouble("RATE",ObjRGPItem.getAttribute("RATE").getVal());
                rsHDetail.updateString("RJN_NO",(String) ObjRGPItem.getAttribute("RJN_NO").getObj());
                rsHDetail.updateLong("RJN_SRNO",(long) ObjRGPItem.getAttribute("RJN_SRNO").getVal());
                rsHDetail.updateString("GATEPASSREQ_NO",(String) ObjRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsHDetail.updateLong("GATEPASSREQ_SRNO",(long) ObjRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsHDetail.updateString("DECLARATION_ID",(String) ObjRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsHDetail.updateLong("DECLARATION_SRNO",(long) ObjRGPItem.getAttribute("DECLARATION_SRNO").getVal());
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjRGPItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateString("REMARKS",(String) ObjRGPItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(long) ObjRGPItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjRGPItem.getAttribute("CREATED_DATE").getObj());
//                rsHDetail.updateLong("MODIFIED_BY",(long) ObjRGPItem.getAttribute("MODIFIED_BY").getVal());
//                rsHDetail.updateString("MODIFIED_DATE",(String) ObjRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("PACKING",(String)ObjRGPItem.getAttribute("PACKING").getObj());
                rsHDetail.updateDouble("RETURNED_QTY",0);
                rsHDetail.updateDouble("BAL_QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                //} // Entries inserting into Line file

            //  Inserting records in Items Detail
                ResultSet rsLot;
                Statement tmpLot;
                tmpLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsLot=tmpLot.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND GATEPASS_NO='1'");
            
                if (LotItem) 
                {    
                    for(int l=1;l<=ObjRGPItem.colItemLot.size();l++)
                    {
                        clsRGPItemDetail ObjRGPItem_Detail=(clsRGPItemDetail)  ObjRGPItem.colItemLot.get(Integer.toString(l));
                        rsLot.moveToInsertRow();
                        rsLot.updateLong("COMPANY_ID",nCompanyID);
                        rsLot.updateString("GATEPASS_NO",nRGPno);
                        rsLot.updateLong("SR_NO",i);
                        rsLot.updateLong("SRNO",l);
                        rsLot.updateString("LOT_NO",(String) ObjRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsLot.updateDouble("LOT_QTY",ObjRGPItem_Detail.getAttribute("LOT_QTY").getVal());
                        rsLot.updateLong("CREATED_BY",(long) ObjRGPItem_Detail.getAttribute("CREATED_BY").getVal());
                        rsLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
//                        rsLot.updateLong("MODIFIED_BY",(long) ObjRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
//                        rsLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsLot.updateBoolean("CHANGED",true);
                        rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsLot.insertRow();
                        
                        rsHLot.moveToInsertRow();
                        rsHLot.updateInt("REVISION_NO",1);
                        rsHLot.updateLong("COMPANY_ID",nCompanyID);
                        rsHLot.updateString("GATEPASS_NO",nRGPno);
                        rsHLot.updateLong("SR_NO",i);
                        rsHLot.updateLong("SRNO",l);
                        rsHLot.updateString("LOT_NO",(String) ObjRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsHLot.updateDouble("LOT_QTY",ObjRGPItem_Detail.getAttribute("LOT_QTY").getVal());
                        rsHLot.updateLong("CREATED_BY",(long) ObjRGPItem_Detail.getAttribute("CREATED_BY").getVal());
                        rsHLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
//                        rsHLot.updateLong("MODIFIED_BY",(long) ObjRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
//                        rsHLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsHLot.updateBoolean("CHANGED",true);
                        rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsHLot.insertRow();
                     }    
                 }  
            }      

            
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=12; //QUOTATION MODULE ID
            ObjFlow.DocNo=(String)getAttribute("GATEPASS_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_RGP_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="GATEPASS_NO";
            
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
            
            
            // Specified procedure over here

            MoveLast();
            return true;
        }                
        catch(Exception e)
        {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }   
    }        

    public boolean Update() 
    { 
        Statement stHistory,stHDetail,stHLot,stTmp;
        ResultSet rsHistory,rsHDetail,rsHLot,rsTmp;
        Statement stHeader;
        ResultSet rsHeader;
        String strSQL = "",Gatepassno = "",Declno = "";
        String RJNNo = "";
        int RJNSrno = 0,GatepassSrno = 0,DeclSrno = 0;
        
        
        try
        {         
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_RGP_HEADER_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("GATEPASS_NO").getObj();
            String GatepassNo=(String)getAttribute("GATEPASS_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_NO)='"+theDocNo+"'");
            //rsHeader.first();
           
            //=========== Check the Quantities entered against Rejection.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem) colLineItems.get(Integer.toString(i));
                
                RJNNo=(String) ObjItem.getAttribute("RJN_NO").getObj();
                RJNSrno=(int) ObjItem.getAttribute("RJN_SRNO").getVal();
                double RJNQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!RJNNo.trim().equals(""))&&(RJNSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        RJNQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND RJN_SRNO="+RJNSrno+" AND GATEPASS_NO<>'"+GatepassNo+"' AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > RJNQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds RJN No. "+RJNNo+" Sr. No. "+RJNSrno+" qty "+RJNQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            //=========== Check the Quantities entered against Gatepass Requisition.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem) colLineItems.get(Integer.toString(i));
                
                Gatepassno=(String) ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                GatepassSrno=(int) ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                double GatepassQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Gatepassno.trim().equals(""))&&(GatepassSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        GatepassQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASSREQ_NO='"+Gatepassno+"' AND GATEPASSREQ_SRNO="+GatepassSrno+" AND GATEPASS_NO<>'"+GatepassNo+"' AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > GatepassQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds GatepassNo. "+Gatepassno+" Sr. No. "+GatepassSrno+" qty "+GatepassQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            //=========== Check the Quantities entered against Declaration.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem) colLineItems.get(Integer.toString(i));
                
                Declno=(String) ObjItem.getAttribute("DECLARATION_ID").getObj();
                DeclSrno=(int) ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                double DeclQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Declno.trim().equals(""))&&(DeclSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT RECD_QTY FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        DeclQty=rsTmp.getDouble("RECD_QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND DECLARATION_SRNO="+DeclSrno+" AND GATEPASS_NO<>'"+GatepassNo+"' AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > DeclQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Decl.No "+Declno+" Sr. No. "+DeclSrno+" qty "+DeclQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            
            
            //No Primary Keys will be updated & Updating of Header starts
            ApprovalFlow ObjFlow=new ApprovalFlow();

            
            rsRGP.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsRGP.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsRGP.updateBoolean("RGP_WITH_LOT",(boolean) getAttribute("RGP_WITH_LOT").getBool());
            rsRGP.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsRGP.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsRGP.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsRGP.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsRGP.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsRGP.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
//            rsRGP.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
//            rsRGP.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsRGP.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsRGP.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsRGP.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsRGP.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsRGP.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsRGP.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsRGP.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsRGP.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsRGP.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsRGP.updateBoolean("CHANGED",true);
            rsRGP.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsRGP.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsRGP.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsRGP.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsRGP.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            rsRGP.updateBoolean("CANCELED",false);
            rsRGP.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_RGP_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+(String)getAttribute("GATEPASS_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("GATEPASS_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateBoolean("RGP_WITH_LOT",(boolean) getAttribute("RGP_WITH_LOT").getBool());
            rsHistory.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsHistory.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
//            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
//            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsHistory.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            rsHistory.insertRow();           
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("GATEPASS_TYPE").getObj();
            boolean LotItem = (boolean) getAttribute("RGP_WITH_LOT").getBool();
            
           // Stock Updataion started over here 
           //=======================================//
            if(AStatus.equals("F"))
            {
            //-------- First Update the stock -------------//
            for(int i=1;i<=colLineItems.size();i++) {
                clsRGPItem ObjItem=(clsRGPItem)colLineItems.get(Integer.toString(i));
                
                RJNNo=(String)ObjItem.getAttribute("RJN_NO").getObj();
                RJNSrno=(int)ObjItem.getAttribute("RJN_SRNO").getVal();
                double Qty=ObjItem.getAttribute("QTY").getVal();
                String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                Gatepassno=(String)ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                GatepassSrno=(int)ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                Declno=(String)ObjItem.getAttribute("DECLARATION_ID").getObj();
                DeclSrno=(int)ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                Statement tmpStmt;
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

                
                    if((!Gatepassno.trim().equals(""))&&GatepassSrno>0) {
                        strSQL="UPDATE D_INV_GATEPASS_REQ_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_GATEPASS_REQ_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!Declno.trim().equals(""))&&DeclSrno>0) {
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET RETURNED_QTY=RETURNED_QTY+"+Qty+",BAL_QTY=RECD_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!RJNNo.trim().equals(""))&&RJNSrno>0) {
                        strSQL="UPDATE D_INV_RJN_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_RJN_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"'";
                        data.Execute(strSQL);
                    }
                
                
                }// For Loop of Line items is completed
              } // Approval Final Approval If Comndition completed
            
              // Stock Updataion is Completed over here 
              //=======================================//

            // Inserting records in RGP Details
            
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String nRGPno=(String) getAttribute("GATEPASS_NO").getObj();

            String Del_H = "DELETE FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+nRGPno.trim()+"'";
            data.Execute(Del_H);
            String Del_L = "DELETE FROM D_INV_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+nRGPno.trim()+"'";
            data.Execute(Del_L);

            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='1'");
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            String Gatepass_type=(String) getAttribute("GATEPASS_TYPE").getObj();
            
            for(int i=1;i<=colLineItems.size();i++)
            {
                clsRGPItem ObjRGPItem=(clsRGPItem) colLineItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("GATEPASS_NO",nRGPno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("WAREHOUSE_ID",(String) ObjRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsTmp.updateString("LOCATION_ID",(String) ObjRGPItem.getAttribute("LOCATION_ID").getObj());
                rsTmp.updateString("ITEM_CODE",(String) ObjRGPItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateLong("UNIT",(long) ObjRGPItem.getAttribute("UNIT").getVal());
                rsTmp.updateString("RGP_DESC",(String) ObjRGPItem.getAttribute("RGP_DESC").getObj());
                rsTmp.updateDouble("QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsTmp.updateDouble("BAL_QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsTmp.updateDouble("RETURNED_QTY",0);
                rsTmp.updateDouble("RATE",ObjRGPItem.getAttribute("RATE").getVal());
                rsTmp.updateString("RJN_NO",(String) ObjRGPItem.getAttribute("RJN_NO").getObj());
                rsTmp.updateLong("RJN_SRNO",(long) ObjRGPItem.getAttribute("RJN_SRNO").getVal());
                rsTmp.updateString("GATEPASSREQ_NO",(String) ObjRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsTmp.updateLong("GATEPASSREQ_SRNO",(long) ObjRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsTmp.updateString("DECLARATION_ID",(String) ObjRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsTmp.updateLong("DECLARATION_SRNO",(long) ObjRGPItem.getAttribute("DECLARATION_SRNO").getVal());
                rsTmp.updateBoolean("CANCELED",(boolean) ObjRGPItem.getAttribute("CANCELED").getBool());
                rsTmp.updateString("REMARKS",(String) ObjRGPItem.getAttribute("REMARKS").getObj());
//                rsTmp.updateLong("CREATED_BY",(long) ObjRGPItem.getAttribute("CREATED_BY").getVal());
//                rsTmp.updateString("CREATED_DATE",(String) ObjRGPItem.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) ObjRGPItem.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) ObjRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateString("PACKING",(String)ObjRGPItem.getAttribute("PACKING").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();

                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("GATEPASS_NO",nRGPno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("WAREHOUSE_ID",(String) ObjRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String) ObjRGPItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("ITEM_CODE",(String) ObjRGPItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjRGPItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("RGP_DESC",(String) ObjRGPItem.getAttribute("RGP_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjRGPItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RATE",ObjRGPItem.getAttribute("RATE").getVal());
                rsHDetail.updateString("RJN_NO",(String) ObjRGPItem.getAttribute("RJN_NO").getObj());
                rsHDetail.updateLong("RJN_SRNO",(long) ObjRGPItem.getAttribute("RJN_SRNO").getVal());
                rsHDetail.updateString("GATEPASSREQ_NO",(String) ObjRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsHDetail.updateLong("GATEPASSREQ_SRNO",(long) ObjRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsHDetail.updateString("DECLARATION_ID",(String) ObjRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsHDetail.updateLong("DECLARATION_SRNO",(long) ObjRGPItem.getAttribute("DECLARATION_SRNO").getVal());
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjRGPItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateString("REMARKS",(String) ObjRGPItem.getAttribute("REMARKS").getObj());
//                rsHDetail.updateLong("CREATED_BY",(long) ObjRGPItem.getAttribute("CREATED_BY").getVal());
//                rsHDetail.updateString("CREATED_DATE",(String) ObjRGPItem.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjRGPItem.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) ObjRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("PACKING",(String)ObjRGPItem.getAttribute("PACKING").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
                //  Inserting records in Items Detail
                ResultSet rsLot;
                Statement tmpLot;
                tmpLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsLot=tmpLot.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='1'");

                if (LotItem) 
                {    
                    for(int l=1;l<=ObjRGPItem.colItemLot.size();l++)
                    {
                        clsRGPItemDetail ObjRGPItem_Detail=(clsRGPItemDetail)  ObjRGPItem.colItemLot.get(Integer.toString(l));
                        rsLot.moveToInsertRow();
                        rsLot.updateLong("COMPANY_ID",nCompanyID);
                        rsLot.updateString("GATEPASS_NO",nRGPno);
                        rsLot.updateLong("SR_NO",i);
                        rsLot.updateLong("SRNO",l);
                        rsLot.updateString("LOT_NO",(String) ObjRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsLot.updateDouble("LOT_QTY",ObjRGPItem_Detail.getAttribute("LOT_QTY").getVal());
//                        rsLot.updateLong("CREATED_BY",(long) ObjRGPItem_Detail.getAttribute("CREATED_BY").getVal());
//                        rsLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                        rsLot.updateLong("MODIFIED_BY",(long) ObjRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
                        rsLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsLot.updateBoolean("CHANGED",true);
                        rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsLot.insertRow();
                        
                        rsHLot.moveToInsertRow();
                        rsHLot.updateInt("REVISION_NO",RevNo);
                        rsHLot.updateLong("COMPANY_ID",nCompanyID);
                        rsHLot.updateString("GATEPASS_NO",nRGPno);
                        rsHLot.updateLong("SR_NO",i);
                        rsHLot.updateLong("SRNO",l);
                        rsHLot.updateString("LOT_NO",(String) ObjRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsHLot.updateDouble("LOT_QTY",ObjRGPItem_Detail.getAttribute("LOT_QTY").getVal());
//                        rsHLot.updateLong("CREATED_BY",(long) ObjRGPItem_Detail.getAttribute("CREATED_BY").getVal());
//                        rsHLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                        rsHLot.updateLong("MODIFIED_BY",(long) ObjRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
                        rsHLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsHLot.updateBoolean("CHANGED",true);
                        rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsHLot.insertRow();
                     }    
                }  
            }    
            
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=12; //QUOTATION MODULE ID
            ObjFlow.DocNo=(String)getAttribute("GATEPASS_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_RGP_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="GATEPASS_NO";

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_RGP_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=12 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
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
            
            
            // updataion process is over
            
            
            return true;
        }
      catch(Exception e)
       {
          return false;
       }   
    }       

    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pGatepassno,int pUserID)
    {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try
        {
            if(HistoryView)
            {
              return false;  
            }
            //First check that document is approved or not 
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_NO='"+pGatepassno+"' AND APPROVED=true";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else
            {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=1 AND DOC_NO='"+pGatepassno+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0)
                {
                    //Yes document is waiting for this user
                    return true;
                }
                else
                {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            return false;
        }
    }

    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pGatepassno,int pUserID)
    {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try
        {
            if(HistoryView)
            {
              return false;  
            }
            //First check that document is approved or not 
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_NO='"+pGatepassno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else
            {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=12 AND DOC_NO='"+pGatepassno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0)
                {
                    //Yes document is waiting for this user
                    return true;
                }
                else
                {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    
  //Deletes current record
   public boolean Delete(int pUserID)
   {
        try
        {   
            int lCompanyID=(int) getAttribute("COMPANY_ID").getVal();
            String lGatepassno=(String) getAttribute("GATEPASS_NO").getObj();
            
          if(CanDelete(lCompanyID,lGatepassno,pUserID))
           {
            String strQry = "DELETE FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
            data.Execute(strQry);
            
            strQry = "DELETE FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
            data.Execute(strQry);

            strQry = "DELETE FROM D_INV_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
            data.Execute(strQry);
	   }

            rsRGP.refreshRow();
            return MoveLast();                
        }                
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }        
   }

    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND GATEPASS_REQ_NO='"+pDocNo+"'";
        clsRGP ObjRGP = new clsRGP();
        ObjRGP.Filter(strCondition,pCompanyID);
        return ObjRGP;
    }

    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_RGP_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsRGP=Stmt.executeQuery(strSql);
            
            if(!rsRGP.first()) {
//                strSql = "SELECT * FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GATEPASS_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GATEPASS_NO";
//                rsRGP=Stmt.executeQuery(strSql);
//                Ready=true;
//                MoveLast();
                JOptionPane.showMessageDialog(null, "No Record found.");
                return false;
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
    

public HashMap getList(String pCondition)
{
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp1;
        Statement tmpStmt1;
        
        ResultSet rsTmp2;
        Statement tmpStmt2;
      
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            tmpStmt=Conn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_HEADER"+pCondition);

            Counter=0;
            while(! rsTmp.isAfterLast())
            {
            Counter=Counter+1;
            clsRGP ObjRGP=new clsRGP();

            //Populate the user
            ObjRGP.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));            
            ObjRGP.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
            ObjRGP.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
            ObjRGP.setAttribute("GATEPASS_TYPE",rsTmp.getString("GATEPASS_TYPE"));
            ObjRGP.setAttribute("FOR_DEPT",rsTmp.getLong("FOR_DEPT"));
            ObjRGP.setAttribute("RGP_WITH_LOT",rsTmp.getBoolean("RGP_WITH_LOT"));
            ObjRGP.setAttribute("MODE_TRANSPORT",rsTmp.getLong("MODE_TRANSPORT"));
            ObjRGP.setAttribute("TRANSPORTER",rsTmp.getLong("TRANSPORTER"));
            ObjRGP.setAttribute("SUPP_ID",rsTmp.getString("SUPP_ID"));
            ObjRGP.setAttribute("TOTAL_AMOUNT",rsTmp.getLong("TOTAL_AMOUNT"));
            ObjRGP.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
            ObjRGP.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
            ObjRGP.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
            ObjRGP.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
            ObjRGP.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
            ObjRGP.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
            ObjRGP.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
            ObjRGP.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
            ObjRGP.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
            ObjRGP.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));    

            //Now Populate the collection
            //first clear the collection
            ObjRGP.colLineItems.clear();

            String mCompanyID=Long.toString( (long) ObjRGP.getAttribute("COMPANY_ID").getVal());
            String mGatepassno=(String) ObjRGP.getAttribute("GATEPASS_NO").getObj();
            
            tmpStmt2=Conn.createStatement();
            rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_RGP_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mGatepassno.trim()+"'");
    
            Counter=0;
            clsRGPItem ObjRGPItem1=new clsRGPItem();
            ObjRGPItem1.colItemLot.clear();
            
            while(! rsTmp2.isAfterLast())
            {
                Counter=Counter+1;
                clsRGPItem ObjRGPItem= new clsRGPItem();
                
                ObjRGPItem.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                ObjRGPItem.setAttribute("GATEPASS_NO",rsTmp2.getString("GATEPASS_NO"));
                ObjRGPItem.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjRGPItem.setAttribute("WAREHOUSE_ID",rsTmp2.getString("WAREHOUSE_ID"));
                ObjRGPItem.setAttribute("LOCATION_ID",rsTmp2.getLong("LOCATION_ID"));
                ObjRGPItem.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                ObjRGPItem.setAttribute("RGP_DESC",rsTmp2.getLong("RGP_DESC"));
                ObjRGPItem.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                ObjRGPItem.setAttribute("QTY",rsTmp2.getLong("QTY"));
                ObjRGPItem.setAttribute("RJN_NO",rsTmp2.getString("RJN_NO"));
                ObjRGPItem.setAttribute("RJN_SRNO",rsTmp2.getLong("RJN_SRNO"));
                ObjRGPItem.setAttribute("GATEPASSREQ_NO",rsTmp2.getString("GATEPASSREQ_NO"));
                ObjRGPItem.setAttribute("GATEPASSREQ_SRNO",rsTmp2.getLong("GATEPASSREQ_SRNO"));
                ObjRGPItem.setAttribute("DECLARATAION_ID",rsTmp2.getString("DECLARATION_ID"));
                ObjRGPItem.setAttribute("DECLARATION_SRNO",rsTmp2.getLong("DECLARATION_SRNO"));
                ObjRGPItem.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
                ObjRGPItem.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                ObjRGPItem.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjRGPItem.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjRGPItem.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjRGPItem.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                
                    // Lot Items starting from here
                tmpStmt1=Conn.createStatement();
                rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND SR_NO="+rsTmp2.getInt("SR_NO")+"");
                
                int Counter1 = 0;
                while(! rsTmp1.isAfterLast())
                {
                    Counter1=Counter1+1;
                    clsRGPItemDetail ObjRGPItem_Detail=new clsRGPItemDetail();

                    ObjRGPItem_Detail.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                    ObjRGPItem_Detail.setAttribute("GATEPASS_NO",rsTmp1.getString("GATEPASS_NO"));
                    ObjRGPItem_Detail.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                    ObjRGPItem_Detail.setAttribute("SRNO",rsTmp1.getLong("SRNO"));
                    ObjRGPItem_Detail.setAttribute("LOT_NO",rsTmp1.getString("LOT_NO"));
                    ObjRGPItem_Detail.setAttribute("LOT_QTY",rsTmp1.getLong("LOT_QTY"));
                    ObjRGPItem_Detail.setAttribute("CANCELED",rsTmp1.getInt("CANCELED"));
                    ObjRGPItem_Detail.setAttribute("CREATED_BY",rsTmp1.getLong("CREATED_BY"));
                    ObjRGPItem_Detail.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                    ObjRGPItem_Detail.setAttribute("MODIFIED_BY",rsTmp1.getLong("MODIFIED_BY"));
                    ObjRGPItem_Detail.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                
                    ObjRGPItem.colItemLot.put(Long.toString(Counter1),ObjRGPItem_Detail);
                    rsTmp1.next();
                }
                ObjRGP.colLineItems.put(Long.toString(Counter),ObjRGPItem);
                rsTmp2.next();
            }// Innser while
             //Put the prepared user object into list    
            rsTmp.next();
            List.put(Long.toString(Counter),ObjRGP);
         }//Out While
       }
       catch(Exception e)
       {
       }
        
      return List; 
}

private boolean setData() 
{
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try
        {
            if(HistoryView)
            {
              RevNo=rsRGP.getInt("REVISION_NO");  
              setAttribute("REVISION_NO",rsRGP.getInt("REVISION_NO"));  
            }
            else
            {
              setAttribute("REVISION_NO",0);  
            }
            
            setAttribute("COMPANY_ID",rsRGP.getLong("COMPANY_ID"));            
            setAttribute("GATEPASS_NO",rsRGP.getString("GATEPASS_NO"));
            setAttribute("GATEPASS_DATE",rsRGP.getString("GATEPASS_DATE"));
            setAttribute("GATEPASS_TYPE",rsRGP.getString("GATEPASS_TYPE"));
            setAttribute("FOR_DEPT",rsRGP.getLong("FOR_DEPT"));
            setAttribute("RGP_WITH_LOT",rsRGP.getBoolean("RGP_WITH_LOT"));
            setAttribute("MODE_TRANSPORT",rsRGP.getLong("MODE_TRANSPORT"));
            setAttribute("TRANSPORTER",rsRGP.getLong("TRANSPORTER"));
            setAttribute("SUPP_ID",rsRGP.getString("SUPP_ID"));
            setAttribute("TOTAL_AMOUNT",rsRGP.getLong("TOTAL_AMOUNT"));
            setAttribute("APPROVED",rsRGP.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsRGP.getString("APPROVED_DATE"));
            setAttribute("REJECTED_DATE",rsRGP.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsRGP.getString("REJECTED_REMARKS"));
            setAttribute("REMARKS",rsRGP.getString("REMARKS"));
            setAttribute("CANCELED",rsRGP.getInt("CANCELED"));
            setAttribute("CREATED_BY",rsRGP.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsRGP.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsRGP.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsRGP.getString("MODIFIED_DATE"));    
            setAttribute("USER_ID",rsRGP.getInt("USER_ID"));
            setAttribute("PARTY_NAME",rsRGP.getString("PARTY_NAME"));
            setAttribute("ADD1",rsRGP.getString("ADD1"));
            setAttribute("ADD2",rsRGP.getString("ADD2"));
            setAttribute("ADD3",rsRGP.getString("ADD3"));
            setAttribute("CITY",rsRGP.getString("CITY"));
            setAttribute("EXP_RETURN_DATE",rsRGP.getString("EXP_RETURN_DATE"));
            setAttribute("HIERARCHY_ID",rsRGP.getInt("HIERARCHY_ID"));
            setAttribute("PACKING",rsRGP.getString("PACKING"));
            setAttribute("PURPOSE",rsRGP.getString("PURPOSE"));
            setAttribute("DESPATCH_MODE",rsRGP.getString("DESPATCH_MODE"));
            setAttribute("GROSS_WEIGHT",rsRGP.getString("GROSS_WEIGHT"));
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            clsRGPItem ObjRGPItem1 = new clsRGPItem();
            ObjRGPItem1.colItemLot.clear();
            
            String mCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mGatepassno=(String) getAttribute("GATEPASS_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView)
            {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else
            {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' ORDER BY SR_NO");    
            }
            Counter=0;
            rsTmp.first();
            while(! rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsRGPItem ObjRGPItem=new clsRGPItem();
                
                ObjRGPItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjRGPItem.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjRGPItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjRGPItem.setAttribute("WAREHOUSE_ID",rsTmp.getString("WAREHOUSE_ID"));
                ObjRGPItem.setAttribute("LOCATION_ID",rsTmp.getString("LOCATION_ID"));
                ObjRGPItem.setAttribute("ITEM_CODE",rsTmp.getString("ITEM_CODE"));
                ObjRGPItem.setAttribute("RGP_DESC",rsTmp.getString("RGP_DESC"));
                ObjRGPItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                ObjRGPItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                ObjRGPItem.setAttribute("RATE",rsTmp.getDouble("RATE"));
                ObjRGPItem.setAttribute("RJN_NO",rsTmp.getString("RJN_NO"));
                ObjRGPItem.setAttribute("RJN_SRNO",rsTmp.getInt("RJN_SRNO"));
                ObjRGPItem.setAttribute("GATEPASSREQ_NO",rsTmp.getString("GATEPASSREQ_NO"));
                ObjRGPItem.setAttribute("GATEPASSREQ_SRNO",rsTmp.getInt("GATEPASSREQ_SRNO"));
                ObjRGPItem.setAttribute("DECLARATAION_ID",rsTmp.getString("DECLARATION_ID"));
                ObjRGPItem.setAttribute("DECLARATION_SRNO",rsTmp.getInt("DECLARATION_SRNO"));
                ObjRGPItem.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjRGPItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjRGPItem.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjRGPItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjRGPItem.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjRGPItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjRGPItem.setAttribute("PACKING",rsTmp.getString("PACKING"));
                ObjRGPItem.setAttribute("RETURNED_QTY",rsTmp.getString("RETURNED_QTY"));
            
                ResultSet rsTmp1;
                tmpStmt=tmpConn.createStatement();
                if(HistoryView)
                {
                    rsTmp1=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND SR_NO="+rsTmp.getInt("SR_NO")+" AND REVISION_NO="+RevNo);
                }
                else
                {
                    rsTmp1=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND SR_NO="+rsTmp.getInt("SR_NO")+"");    
                }
                rsTmp1.first();
                
                int Counter1=0;
                while(rsTmp1.next())
                {
                    Counter1=Counter1+1;
                    clsRGPItemDetail ObjRGPItem_Detail =new clsRGPItemDetail();
                    
                    ObjRGPItem_Detail.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                    ObjRGPItem_Detail.setAttribute("GATEPASS_NO",rsTmp1.getString("GATEPASS_NO"));
                    ObjRGPItem_Detail.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                    ObjRGPItem_Detail.setAttribute("SRNO",rsTmp1.getLong("SRNO"));
                    ObjRGPItem_Detail.setAttribute("LOT_NO",rsTmp1.getString("LOT_NO"));
                    ObjRGPItem_Detail.setAttribute("LOT_QTY",rsTmp1.getLong("LOT_QTY"));
                    ObjRGPItem_Detail.setAttribute("CANCELED",rsTmp1.getInt("CANCELED"));
                    ObjRGPItem_Detail.setAttribute("CREATED_BY",rsTmp1.getLong("CREATED_BY"));
                    ObjRGPItem_Detail.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                    ObjRGPItem_Detail.setAttribute("MODIFIED_BY",rsTmp1.getLong("MODIFIED_BY"));
                    ObjRGPItem_Detail.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));

                    ObjRGPItem.colItemLot.put(Long.toString(Counter1),ObjRGPItem_Detail);
                }
                colLineItems.put(Long.toString(Counter),ObjRGPItem);
                rsTmp.next();
            }
            return true;
           }
          catch(Exception e)
          {
          return false;
          }
      }

// updating records related to Line & Line Line Items       
   public boolean rollback()
   {
     try
     {
         Statement Stmt;
         ResultSet rsItem;
         ResultSet rsItemDetail;
         
         // Assigning Variables Header Fields
         String nGatepasstype = (String) getAttribute("GATEPASS_TYPE").getObj();
         boolean LotItem=(boolean) getAttribute("WITH_LOT_ITEM").getBool();
         String mGatepassno = (String) getAttribute("GATEPASS_NO").getObj();
         
         Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
         rsItem=Stmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
                                    mGatepassno.trim()+"' ORDER BY SR_NO");
         
         if(rsItem.isAfterLast() || rsItem.isBeforeFirst())
         {
             rsItem.first();
         }    
         while(! rsItem.isAfterLast())
         {
             // Gatepass type is General or Gatepass requistion
             if(nGatepasstype.equals("GEN") || nGatepasstype.equals("GPR"))
             {   
               ResultSet rsOther;
               String Upd_Item = "UPDATE D_INV_ITEM_MASTER SET ON_HAND_QTY= ON_HAND_QTY + "+
                                              rsItem.getLong("QTY")+", TOTAL_ISSUE_QTY= TOTAL_ISSUE_QTY - "+
                                              rsItem.getLong("QTY")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+
                                              rsItem.getString("ITEM_CODE")+"'";
               Stmt.executeUpdate(Upd_Item);                               
             }
           
             // Gatepass type is Rejection Memo
             if(nGatepasstype.equals("RJN"))
             {
                ResultSet rsRJN;
                //updating Rejection form
                String Upd_Rejn = "UPDATE D_INV_REJECTION_DETAIL SET BAL_QTY=BAL_QTY - "+
                                                rsItem.getLong("QTY")+",RETURNED_QTY=RETURNED_QTY - "+
                                                rsItem.getLong("QTY")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+
                                                rsItem.getString("RJN_NO")+"' AND SR_NO="+rsItem.getLong("SR_NO")+" AND ITEM_CODE="+
                                                rsItem.getString("ITEM_CODE")+"'";
                Stmt.executeUpdate(Upd_Rejn);
             }
             
             //Gatepass type is Declaration Form
             if(nGatepasstype.equals("DFO"))
             {
                 
                ResultSet rsDFO;
                //updating Declaration form
                String Upd_Decl = "UPDATE D_INV_DECLARATION_DETAIL SET BAL_QTY= BAL_QTY - "+
                                               rsItem.getLong("QTY")+",RETURNED_QTY= RETURNED_QTY - "+
                                               rsItem.getLong("QTY")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+
                                               rsItem.getString("DECLARATION_ID")+"' AND SR_NO="+rsItem.getLong("SR_NO")+" AND ITEM_CODE="+
                                               rsItem.getString("ITEM_CODE")+"'";
                Stmt.executeUpdate(Upd_Decl);
             }
             
            rsItem.next();
         } 
         // While Loop is completed  
         
         // Lot Items Enties Roll Backed from here
         if(LotItem)
         {   
             rsItemDetail=Stmt.executeQuery("SELECT * FROM D_INV_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
                                        mGatepassno.trim()+"' ORDER BY SR_NO,SRNO");

             if(rsItemDetail.isAfterLast() || rsItemDetail.isBeforeFirst())
             {
                 rsItemDetail.first();
             }    
             while(! rsItemDetail.isAfterLast())
             {
                   ResultSet rsItemLot;
                    if(nGatepasstype.equals("GPR") || nGatepasstype.equals("GEN"))
                     {
                        String Upd_ItemLot = "UPDATE D_INV_ITEM_LOT_MASTER SET TOTAL_ISSUED_QTY= TOTAL_ISSUED_QTY - " +
                                                         rsItemDetail.getLong("LOT_QTY")+", ON_HAND_QTY= ON_HAND_QTY + " +
                                                         rsItemDetail.getLong("LOT_QTY")+",LAST_ISSUED_DATE=" + 
                                                         rsItemDetail.getString("MODIFIED_DATE")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+
                                                         Long.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+ rsItemDetail.getString("ITEM_CODE")+"' AND LOT_NO='" +
                                                         rsItemDetail.getString("LOT_NO")+"'";
                        Stmt.executeUpdate(Upd_ItemLot);                                 
                     }   
                     
                   //Updating Declaration Form Item Lot tables
                     if(nGatepasstype.equals("DFO"))
                     {
                       ResultSet rsDFOLot;
                        
                       String Upd_DeclLot = "UPDATE D_INV_DECLARATAION_DETAIL_DETAIL SET TOTAL_ISSUED_QTY = TOTAL_ISSUED_QTY - " +
                                                       rsItemDetail.getLong("LOT_QTY")+", BAL_QTY = BAL_QTY +" + rsItemDetail.getLong("BAL_QTY") + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+
                                                       Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='" + rsItemDetail.getString("DECLARATION_ID")+"' AND SR_NO=" +
                                                       rsItemDetail.getLong("SR_NO")+" AND ITEM_CODE='"+ rsItemDetail.getString("ITEM_CODE")+"'";
                       Stmt.executeUpdate(Upd_DeclLot);
                      }

                   //Updating Rejection Form Item Lot tables
                     if(nGatepasstype.equals("RJN"))
                     {
                       ResultSet rsRJNLot;
                       
                       String Upd_RejnLot = "UPDATE D_INV_REJECTION_DETAIL_DETAIL SET TOTAL_ISSUED_QTY = TOTAL_ISSUED_QTY - " +
                                                      rsItemDetail.getLong("LOT_QTY")+", BAL_QTY = BAL_QTY +" + rsItemDetail.getLong("BAL_QTY") + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+
                                                      Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='" + rsItemDetail.getString("DECLARATION_ID")+"' AND SR_NO=" +
                                                      rsItemDetail.getLong("SR_NO")+" AND ITEM_CODE='"+ rsItemDetail.getString("ITEM_CODE")+"'";
                       Stmt.executeUpdate(Upd_RejnLot);
                     }
                   
                rsItemDetail.next(); 
             } // While Ends for Line items    
         }  // LotItem Condition is completed over here
         
         rsItem=Stmt.executeQuery("DELETE FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
                                    mGatepassno.trim()+"'");
         rsItemDetail=Stmt.executeQuery("DELETE FROM D_INV_RGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
                                    mGatepassno.trim()+"'");
         
         return true;
     }
     catch(Exception e)
     {
         return false;
     }    
   }    

public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom)    
{
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;
      
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate)
            {
            strSQL="SELECT D_INV_RGP_HEADER.GATEPASS_NO,D_INV_RGP_HEADER.GATEPASS_DATE,RECEIVED_DATE,D_INV_RGP_HEADER.FOR_DEPT AS DEPT_ID FROM D_INV_RGP_HEADER,D_COM_DOC_DATA WHERE D_INV_RGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=12 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";    
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate)
            {
            strSQL="SELECT D_INV_RGP_HEADER.GATEPASS_NO,D_INV_RGP_HEADER.GATEPASS_DATE,RECEIVED_DATE,D_INV_RGP_HEADER.FOR_DEPT AS DEPT_ID FROM D_INV_RGP_HEADER,D_COM_DOC_DATA WHERE D_INV_RGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=12 ORDER BY D_INV_RGP_HEADER.GATEPASS_DATE";    
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo)
            {
            strSQL="SELECT D_INV_RGP_HEADER.GATEPASS_NO,D_INV_RGP_HEADER.GATEPASS_DATE,RECEIVED_DATE FROM D_INV_RGP_HEADER,D_COM_DOC_DATA,D_INV_RGP_HEADER.FOR_DEPT AS DEPT_ID WHERE D_INV_RGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=12 ORDER BY D_INV_RGP_HEADER.GATEPASS_NO";    
            }
            
            //strSQL="SELECT D_INV_RGP_HEADER.GATEPASS_NO,D_INV_RGP_HEADER.GATEPASS_DATE FROM D_INV_RGP_HEADER,D_COM_DOC_DATA WHERE D_INV_RGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=12";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                
            if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("GATEPASS_DATE"),FinYearFrom))    
            {
            Counter=Counter+1;
            clsRGP ObjDoc=new clsRGP();
                
            //------------- Header Fields --------------------//
            ObjDoc.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
            ObjDoc.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
            ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
            ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
            // ----------------- End of Header Fields ------------------------------------//
            
            //Put the prepared user object into list    
            List.put(Long.toString(Counter),ObjDoc);
            }
            
            if(!rsTmp.isAfterLast())
            {
            rsTmp.next();
            }
           }//Out While
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
            
        }
        catch(Exception e)
        {
        }
        
      return List; 
}
   
   
   
   public static HashMap getRGPItemList(int pCompanyID,String pRGPNo,boolean pAllData)
    {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        Statement stLot=null;
        ResultSet rsLot=null;
                
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        int Counter1 = 0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSql;
        
        try
        {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            if(pAllData)
            {
                strSql = "SELECT * FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+pRGPNo+"' AND GATEPASS_NO IN (SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE GATEPASS_NO='"+pRGPNo+"' AND APPROVED=1) ORDER BY GATEPASS_NO";
            }
            else{
                strSql = "SELECT * FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+pRGPNo.trim()+"' AND RETURNED_QTY<QTY AND GATEPASS_NO IN (SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE GATEPASS_NO='"+pRGPNo+"' AND APPROVED=1) ORDER BY SR_NO";
            }
            
            rsTmp2=tmpStmt.executeQuery(strSql);
            rsTmp2.first();
       
            Counter1=0;            
            while(! rsTmp2.isAfterLast())
            {
               Counter1++;
               clsRGPItem ObjMRItems=new clsRGPItem();

                   ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                   ObjMRItems.setAttribute("GATEPASS_NO",rsTmp2.getString("GATEPASS_NO"));
                   ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                   ObjMRItems.setAttribute("WAREHOUSE_ID",rsTmp2.getString("WAREHOUSE_ID"));
                   ObjMRItems.setAttribute("LOCATION_ID",rsTmp2.getString("LOCATION_ID"));
                   ObjMRItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                   ObjMRItems.setAttribute("RGP_DESC",rsTmp2.getString("RGP_DESC"));
                   ObjMRItems.setAttribute("UNIT",rsTmp2.getInt("UNIT"));
                   ObjMRItems.setAttribute("QTY",rsTmp2.getDouble("QTY"));
                   ObjMRItems.setAttribute("RETURNED_QTY",rsTmp2.getDouble("RETURNED_QTY"));
                   ObjMRItems.setAttribute("BAL_QTY",rsTmp2.getDouble("BAL_QTY"));
                   ObjMRItems.setAttribute("RJN_NO",rsTmp2.getString("RJN_NO"));
                   ObjMRItems.setAttribute("RJN_SRNO",rsTmp2.getInt("RJN_SRNO"));
                   ObjMRItems.setAttribute("GATEPASSREQ_NO",rsTmp2.getString("GATEPASSREQ_NO"));
                   ObjMRItems.setAttribute("GATEPASSREQ_SRNO",rsTmp2.getInt("GATEPASSREQ_SRNO"));
                   ObjMRItems.setAttribute("DECLARATION_ID",rsTmp2.getString("DECLARATION_ID"));
                   ObjMRItems.setAttribute("DECLARATION_SRNO",rsTmp2.getInt("DECLARATION_SRNO"));
                   ObjMRItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                   ObjMRItems.setAttribute("PACKING",rsTmp2.getString("PACKING"));
                   ObjMRItems.setAttribute("RATE",rsTmp2.getDouble("RATE"));
                   ObjMRItems.setAttribute("RETURNED_QTY",rsTmp2.getDouble("RETURNED_QTY"));

                   
                List.put(Integer.toString(Counter1),ObjMRItems);
                rsTmp2.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
        stLot.close();
        rsLot.close();
                
        rsTmp2.close();
        tmpStmt2.close();
            
        }
        catch(Exception e)
        {            
        }
        
        return List; 
    }
 
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsRGP=Stmt.executeQuery("SELECT * FROM D_INV_RGP_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_RGP_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsRGP ObjRGP=new clsRGP();
                
                ObjRGP.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjRGP.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
                ObjRGP.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjRGP.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjRGP.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjRGP.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjRGP.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjRGP);
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
 
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT GATEPASS_NO,APPROVED,CANCELED FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELED"))
                    {
                     strMessage="Document is cancelled";   
                    }
                    else
                    {
                    strMessage="";
                    }
                }
                else {
                    strMessage="Document is created but is under approval";
                }
                
            }
            else {
                strMessage="Document does not exist";
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return strMessage;
        
    }
 
    
public static boolean IsRGPExist(int pCompanyID,String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                isExist=true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        }
        catch(Exception e) {
            return isExist;
        }
    }        
 

    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT GATEPASS_NO FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"' AND CANCELED=0");
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
    
    
    public static boolean CancelRGP(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            Connection tmpConn;
            Statement stSTM;
            
            if(CanCancel(pCompanyID,pDocNo)) {
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_RGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                        
                    
                }
                else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID=12");
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_RGP_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
                
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