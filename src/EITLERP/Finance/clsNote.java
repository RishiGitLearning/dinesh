/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;


public class clsNote {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    
    public boolean Ready = false;
    
    public HashMap colSubNote=new HashMap();
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
    
    
    /** Creates new clsMaterialRequisition */
    public clsNote() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("NOTE",new Variant(0));
        props.put("NOTE_NAME",new Variant(""));
        props.put("YEAR_FROM",new Variant(0));
        props.put("YEAR_TO",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        colSubNote=new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_NOTE_MASTER ORDER BY YEAR_FROM,NOTE ");
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
        try {
            Statement stDetail;
            ResultSet rsDetail;
            //*** Validations ***//
            if(getAttribute("NOTE").getInt()==0) {
                LastError="Please specify book code";
                return false;
            }
            
            if(getAttribute("NOTE_NAME").getObj().equals("")) {
                LastError="Please specify book name";
                return false;
            }
            
            String strSQL="SELECT NOTE FROM D_FIN_NOTE_MASTER WHERE NOTE="+getAttribute("NOTE").getInt()+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+"";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                LastError="Note No. with specified from year already exist";
                return false;
            }
            
            //Conn.setAutoCommit(false);
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateInt("NOTE",getAttribute("NOTE").getInt());
            rsResultSet.updateString("NOTE_NAME",getAttribute("NOTE_NAME").getString());
            
            rsResultSet.updateInt("YEAR_FROM",getAttribute("YEAR_FROM").getInt());
            rsResultSet.updateInt("YEAR_TO",getAttribute("YEAR_TO").getInt());
            
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.insertRow();
            
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FIN_NOTE_DETAIL WHERE NOTE=1");
            rsDetail.first();
            
            for(int i=1;i<=colSubNote.size();i++) {
                clsSubNote objSubNote=(clsSubNote) colSubNote.get(Integer.toString(i));
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetail.updateInt("NOTE",getAttribute("NOTE").getInt());
                rsDetail.updateInt("YEAR_FROM",getAttribute("YEAR_FROM").getInt());
                rsDetail.updateInt("SR_NO",objSubNote.getAttribute("SR_NO").getInt());
                rsDetail.updateString("SUB_NOTE_NAME",objSubNote.getAttribute("SUB_NOTE_NAME").getString());
                rsDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetail.insertRow();
            }
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
            }catch(Exception c){}
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean InsertCopy(int FromCopy,int ToCopy) {
        try {
            
            ResultSet rsNoteOld = data.getResult("SELECT * FROM D_FIN_NOTE_MASTER WHERE YEAR_FROM="+FromCopy+" ORDER BY NOTE",FinanceGlobal.FinURL);
            rsNoteOld.first();
            
            Connection Conn = data.getConn(FinanceGlobal.FinURL);
            Statement stNote = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsNoteNew = stNote.executeQuery("SELECT * FROM D_FIN_NOTE_MASTER WHERE NOTE=1");
            
            
            Statement stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetail=stDetail.executeQuery("SELECT * FROM D_FIN_NOTE_DETAIL WHERE NOTE=1");
            if(rsNoteOld.getRow()>0) {
                while(!rsNoteOld.isAfterLast()) {
                    rsNoteNew.moveToInsertRow();
                    rsNoteNew.updateInt("COMPANY_ID", rsNoteOld.getInt("COMPANY_ID"));
                    rsNoteNew.updateInt("NOTE",rsNoteOld.getInt("NOTE"));
                    rsNoteNew.updateString("NOTE_NAME",rsNoteOld.getString("NOTE_NAME"));

                    rsNoteNew.updateInt("YEAR_FROM",ToCopy);
                    rsNoteNew.updateInt("YEAR_TO",(ToCopy+1));

                    rsNoteNew.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsNoteNew.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsNoteNew.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsNoteNew.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsNoteNew.insertRow();

                    ResultSet rsNoteDetailOld = data.getResult("SELECT * FROM D_FIN_NOTE_DETAIL WHERE YEAR_FROM="+FromCopy+" AND NOTE="+rsNoteOld.getString("NOTE")+" ORDER BY SR_NO",FinanceGlobal.FinURL);
                    rsNoteDetailOld.first();
                    
                    if(rsNoteDetailOld.getRow()>0) {
                        while(!rsNoteDetailOld.isAfterLast()) {
                            rsDetail.moveToInsertRow();
                            rsDetail.updateInt("COMPANY_ID",rsNoteDetailOld.getInt("COMPANY_ID"));
                            rsDetail.updateInt("NOTE",rsNoteDetailOld.getInt("NOTE"));
                            rsDetail.updateInt("YEAR_FROM",ToCopy);
                            rsDetail.updateInt("SR_NO",rsNoteDetailOld.getInt("SR_NO"));
                            rsDetail.updateString("SUB_NOTE_NAME",rsNoteDetailOld.getString("SUB_NOTE_NAME"));
                            rsDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                            rsDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                            rsDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsDetail.insertRow();
                            rsNoteDetailOld.next();
                        }
                    }
                    rsNoteOld.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    //Updates current record
    public boolean Update() {
        Statement stDetail;
        ResultSet rsDetail;
        try {
            if(getAttribute("NOTE").getInt()==0) {
                LastError="Please specify Note";
                return false;
            }
            
            if(getAttribute("NOTE_NAME").getString().equals("")) {
                LastError="Please specify Note Name";
                return false;
            }
            
            rsResultSet.updateInt("NOTE",getAttribute("NOTE").getInt());
            rsResultSet.updateString("NOTE_NAME",getAttribute("NOTE_NAME").getString());
            
            rsResultSet.updateInt("YEAR_FROM",getAttribute("YEAR_FROM").getInt());
            rsResultSet.updateInt("YEAR_TO",getAttribute("YEAR_TO").getInt());
            
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateRow();
            
            int Note=getAttribute("NOTE").getInt();
            String sql =  "DELETE FROM D_FIN_NOTE_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND NOTE="+Note+" ";
            data.Execute(sql,FinanceGlobal.FinURL);
            
            //Now Insert records into detail table
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_FIN_NOTE_DETAIL WHERE NOTE=1");
            rsDetail.first();
            //System.out.println("test3");
            for(int i=1;i<=colSubNote.size();i++) {
                clsSubNote objSubNote=(clsSubNote) colSubNote.get(Integer.toString(i));
                
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsDetail.updateInt("NOTE",getAttribute("NOTE").getInt());
                rsDetail.updateInt("SR_NO",objSubNote.getAttribute("SR_NO").getInt());
                rsDetail.updateInt("YEAR_FROM",getAttribute("YEAR_FROM").getInt());
                rsDetail.updateString("SUB_NOTE_NAME",objSubNote.getAttribute("SUB_NOTE_NAME").getString());
                rsDetail.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                rsDetail.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsDetail.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
                rsDetail.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                rsDetail.insertRow();
            }
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
            }catch(Exception c){}
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean Delete() {
        try {
            int Note=getAttribute("NOTE").getInt();
            int YearFrom=getAttribute("YEAR_FROM").getInt();
            data.Execute("DELETE FROM D_FIN_NOTE_MASTER WHERE NOTE="+Note+" AND YEAR_FROM="+YearFrom+"",FinanceGlobal.FinURL);
            data.Execute("DELETE FROM D_FIN_NOTE_DETAIL WHERE NOTE="+Note+" AND YEAR_FROM="+YearFrom+"",FinanceGlobal.FinURL);
            MoveLast();
            return true;
            
        } catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_NOTE_MASTER " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_NOTE_MASTER ORDER BY NOTE ";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
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
    
    
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        try {
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("NOTE",UtilFunctions.getInt(rsResultSet,"NOTE",0));
            setAttribute("NOTE_NAME",UtilFunctions.getString(rsResultSet,"NOTE_NAME",""));
            
            setAttribute("YEAR_FROM",UtilFunctions.getInt(rsResultSet,"YEAR_FROM",0));
            setAttribute("YEAR_TO",UtilFunctions.getInt(rsResultSet,"YEAR_TO",0));
            
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            colSubNote.clear();
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            int Note=getAttribute("NOTE").getInt();
            int YearFrom = getAttribute("YEAR_FROM").getInt();
            tmpStmt=tmpConn.createStatement();
            //show Data of Detail Table
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_NOTE_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND YEAR_FROM="+YearFrom+" AND NOTE=" + Note+ " ORDER BY SR_NO");
            
            
            rsTmp.first();
            
            int Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSubNote objItem = new clsSubNote();
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("NOTE",UtilFunctions.getInt(rsTmp,"NOTE", 0));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO", 0));
                objItem.setAttribute("YEAR_FROM",UtilFunctions.getInt(rsTmp,"YEAR_FROM", 0));
                objItem.setAttribute("SUB_NOTE_NAME",UtilFunctions.getString(rsTmp,"SUB_NOTE_NAME",""));
                colSubNote.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public static String getNoteName(int Note,int YearFrom) {
        return data.getStringValueFromDB("SELECT NOTE_NAME FROM D_FIN_NOTE_MASTER WHERE NOTE="+Note+" AND YEAR_FROM="+YearFrom,FinanceGlobal.FinURL);
    }
    
    public static String getSubNoteName(int SubNote,int Note,int YearFrom) {
        return data.getStringValueFromDB("SELECT SUB_NOTE_NAME FROM D_FIN_NOTE_DETAIL WHERE NOTE="+Note+" AND YEAR_FROM="+YearFrom + " AND SR_NO="+SubNote,FinanceGlobal.FinURL);
    }
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp=null;
        Statement tmpStmt=null;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_NOTE_MASTER "+pCondition);
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsNote ObjNote =new clsNote();
                ObjNote.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjNote.setAttribute("NOTE",rsTmp.getInt("NOTE"));
                ObjNote.setAttribute("NOTE_NAME",rsTmp.getString("NOTE_NAME"));
                ObjNote.setAttribute("YEAR_FROM",rsTmp.getInt("YEAR_FROM"));
                ObjNote.setAttribute("YEAR_TO",rsTmp.getInt("YEAR_TO"));
                List.put(Long.toString(Counter),ObjNote);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    public static HashMap getSubList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp=null;
        Statement tmpStmt=null;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_NOTE_DETAIL "+pCondition);
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSubNote ObjSubNote =new clsSubNote();
                ObjSubNote.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjSubNote.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjSubNote.setAttribute("SUB_NOTE_NAME",rsTmp.getString("SUB_NOTE_NAME"));
                ObjSubNote.setAttribute("YEAR_FROM",rsTmp.getInt("YEAR_FROM"));
                
                List.put(Long.toString(Counter),ObjSubNote);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
}
