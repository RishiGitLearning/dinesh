/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.EITLERPGLOBAL;

/**
 *
 * @author  nrpatel
 * @version
 */

public class clsNoteGLMapping {
    
    private HashMap props;
    public boolean Ready = false;
    public static String LastError = "";
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
    public clsNoteGLMapping() {
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("ENTRY_NO",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("ITEM_CRITERIA_TYPE",new Variant(0));
        props.put("FROM_ITEM_ID",new Variant(""));
        props.put("TO_ITEM_ID",new Variant(""));
        props.put("APPROVAL_AUTHORITY",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    
    
    public static clsNoteGLMapping getObject(int pCompanyID,int pEntryNo) {
        clsNoteGLMapping ObjNoteGLMapping=new clsNoteGLMapping();
        
        try {
            ResultSet rsTmp;
            
            String strSQL="SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+pCompanyID+" AND ENTRY_NO="+pEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ObjNoteGLMapping.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjNoteGLMapping.setAttribute("ENTRY_NO",rsTmp.getInt("ENTRY_NO"));
                ObjNoteGLMapping.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjNoteGLMapping.setAttribute("DEPT_ID",rsTmp.getInt("COMPANY_ID"));
                ObjNoteGLMapping.setAttribute("ITEM_CRITERIA_TYPE",rsTmp.getInt("ITEM_CRITERIA_TYPE"));
                ObjNoteGLMapping.setAttribute("FROM_ITEM_ID",rsTmp.getString("FROM_ITEM_ID"));
                ObjNoteGLMapping.setAttribute("TO_ITEM_ID",rsTmp.getString("TO_ITEM_ID"));
                ObjNoteGLMapping.setAttribute("APPROVAL_AUTHORITY",rsTmp.getString("APPROVAL_AUTHORITY"));
                ObjNoteGLMapping.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjNoteGLMapping.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjNoteGLMapping.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjNoteGLMapping.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
            }
            
        }
        catch(Exception e) {
        }
        
        return ObjNoteGLMapping;
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Insert() {
        try {
            
            String SQL = "SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE NOTE="+getAttribute("NOTE").getInt()+" AND SUB_NOTE="+getAttribute("SUB_NOTE").getInt()+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND MAIN_ACCOUNT_CODE='"+getAttribute("MAIN_ACCOUNT_CODE").getString()+"' ";
            if(data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                LastError = "Main Account Code already exists in same Year with same Note, Sub Note and indicator.";
                return false;
            }
            
            
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            //Generate Entry No.
            
            int EntryNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_NOTE_GL_MAPPING WHERE YEAR_FROM="+getAttribute("YEAR_FROM").getInt(),FinanceGlobal.FinURL)+1;
            setAttribute("SR_NO",EntryNo);
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            rsTmp.first();
            
            
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
            rsTmp.updateInt("SR_NO",getAttribute("SR_NO").getInt());
            rsTmp.updateInt("YEAR_FROM",getAttribute("YEAR_FROM").getInt());
            rsTmp.updateInt("YEAR_TO",getAttribute("YEAR_TO").getInt());
            rsTmp.updateInt("NOTE",getAttribute("NOTE").getInt());
            rsTmp.updateInt("SUB_NOTE",getAttribute("SUB_NOTE").getInt());
            rsTmp.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsTmp.updateString("INDICATOR",getAttribute("INDICATOR").getString());
            rsTmp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsTmp.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsTmp.insertRow();
            
            
            rsTmp=data.getResult("SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt() + " ORDER BY NOTE,SUB_NOTE,SR_NO",FinanceGlobal.FinURL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                int Counter = 0;
                while(!rsTmp.isAfterLast()) {
                    Counter++;
                    int SrNo = rsTmp.getInt("SR_NO");
                    stTmp.addBatch("UPDATE D_FIN_NOTE_GL_MAPPING SET SR_NO="+(Counter+1000)+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND SR_NO="+SrNo);
                    //data.Execute("UPDATE D_FIN_NOTE_GL_MAPPING SET SR_NO="+(Counter+1000)+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                    rsTmp.next();
                }
                stTmp.executeBatch();
                rsTmp=data.getResult("SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt() + " ORDER BY NOTE,SUB_NOTE,SR_NO",FinanceGlobal.FinURL);
                rsTmp.first();
                Counter=0;;
                while(!rsTmp.isAfterLast()) {
                    Counter++;
                    int SrNo = rsTmp.getInt("SR_NO");
                    stTmp.addBatch("UPDATE D_FIN_NOTE_GL_MAPPING SET SR_NO="+(Counter)+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND SR_NO="+SrNo);
                    rsTmp.next();
                }
                stTmp.executeBatch();
            }
            //Close it down
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            
            return true;
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return false;
        }
    }
    
    public boolean InsertCopy(int YearFromCopy,int YearToCopy) {
        try {
            
            String SQL = "SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE YEAR_FROM="+YearFromCopy+" ORDER BY NOTE,SUB_NOTE";
            ResultSet rsMappingOld = data.getResult(SQL,FinanceGlobal.FinURL);
            rsMappingOld.first();
            
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            //Generate Entry No.
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            rsTmp.first();
            if(rsMappingOld.getRow() > 0) {
                while(!rsMappingOld.isAfterLast()) {
                    rsTmp.moveToInsertRow();
                    rsTmp.updateInt("COMPANY_ID",rsMappingOld.getInt("COMPANY_ID"));
                    rsTmp.updateInt("SR_NO",rsMappingOld.getInt("SR_NO"));
                    rsTmp.updateInt("YEAR_FROM",YearToCopy);
                    rsTmp.updateInt("YEAR_TO",(YearToCopy+1));
                    rsTmp.updateInt("NOTE",rsMappingOld.getInt("NOTE"));
                    rsTmp.updateInt("SUB_NOTE",rsMappingOld.getInt("SUB_NOTE"));
                    rsTmp.updateString("MAIN_ACCOUNT_CODE",rsMappingOld.getString("MAIN_ACCOUNT_CODE"));
                    rsTmp.updateString("INDICATOR",rsMappingOld.getString("INDICATOR"));
                    rsTmp.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID); 
                    rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                    rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmp.insertRow();

                    rsMappingOld.next();
                }
            }
            //Close it down
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            
            return true;
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return false;
        }
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Remove(int pCompanyID,int SrNo, int YearFrom) {
        try {
            data.Execute("DELETE FROM D_FIN_NOTE_GL_MAPPING WHERE COMPANY_ID="+pCompanyID+" AND SR_NO="+SrNo + " AND YEAR_FROM="+YearFrom,FinanceGlobal.FinURL);
            
            Connection tmpConn=data.getConn(FinanceGlobal.FinURL);
            Statement stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmp=data.getResult("SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt() + " ORDER BY NOTE,SUB_NOTE,SR_NO",FinanceGlobal.FinURL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                int Counter = 0;
                while(!rsTmp.isAfterLast()) {
                    Counter++;
                    int SrNo1 = rsTmp.getInt("SR_NO");
                    stTmp.addBatch("UPDATE D_FIN_NOTE_GL_MAPPING SET SR_NO="+(Counter+1000)+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND SR_NO="+SrNo1);
                    //data.Execute("UPDATE D_FIN_NOTE_GL_MAPPING SET SR_NO="+(Counter+1000)+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                    rsTmp.next();
                }
                stTmp.executeBatch();
                rsTmp=data.getResult("SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt() + " ORDER BY NOTE,SUB_NOTE,SR_NO",FinanceGlobal.FinURL);
                rsTmp.first();
                Counter=0;
                while(!rsTmp.isAfterLast()) {
                    Counter++;
                    int SrNo2 = rsTmp.getInt("SR_NO");
                    stTmp.addBatch("UPDATE D_FIN_NOTE_GL_MAPPING SET SR_NO="+(Counter)+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND SR_NO="+SrNo2);
                    //data.Execute("UPDATE D_FIN_NOTE_GL_MAPPING SET SR_NO="+(Counter+1000)+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+getAttribute("YEAR_FROM").getInt()+" AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                    rsTmp.next();
                }
                stTmp.executeBatch();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static String getApprovalAuthority(String pItemID,int pDeptID) {
        Connection tmpConn;
        Statement stTmp,stStartsWith;
        ResultSet rsTmp,rsStartsWith;
        String Authority="";
        boolean ItemFound=false;
        
        try {
            
            
            //First check the item flag in item master. It overrides all other settings
            
            //first check for the department
            tmpConn=data.getConn();
            
            //First check for M.D. Flag. Overrides department logic
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT SPECIAL_APPROVAL FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+pItemID+"' AND SPECIAL_APPROVAL='M' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Authority=rsTmp.getString("SPECIAL_APPROVAL");
                return Authority;
            }
            
            
            
            //======= (4) Audit - New Condition  =========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_ID = SUBSTRING('"+pItemID+"',1,LENGTH(ITEM_ID)) AND ITEM_CRITERIA_TYPE=2 AND APPROVAL_AUTHORITY='A' "); //Starts with - 2
            rsStartsWith.first();
            
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            
            
            
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT APPROVAL_AUTHORITY FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) //Yes Found
            {
                Authority=rsTmp.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            
            //All Department
            //======= (1) Check item code starts with condition =========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_ID = SUBSTRING('"+pItemID+"',1,LENGTH(ITEM_ID)) AND ITEM_CRITERIA_TYPE=2"); //Starts with - 2
            rsStartsWith.first();
            
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (2) Check Exact item code --------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_ID = '"+pItemID+"' AND ITEM_CRITERIA_TYPE=1"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (3) Check  item code  Range ------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE '"+pItemID+"'>=FROM_ITEM_ID AND '"+pItemID+"'<=IF(TO_ITEM_ID='','999999999999',TO_ITEM_ID) AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_CRITERIA_TYPE=3"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            
            //======= (1) Check item code starts with condition =========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_ID = SUBSTRING('"+pItemID+"',1,"+pItemID.trim().length()+") AND ITEM_CRITERIA_TYPE=2"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (2) Check Exact item code --------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_ID = '"+pItemID+"' AND ITEM_CRITERIA_TYPE=1"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (3) Check  item code  Range ------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE '"+pItemID+"'>=FROM_ITEM_ID AND '"+pItemID+"'<=IF(TO_ITEM_ID='','999999999999',TO_ITEM_ID) AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_CRITERIA_TYPE=3"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT SPECIAL_APPROVAL FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+pItemID+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Authority=rsTmp.getString("SPECIAL_APPROVAL");
            }
            
            return Authority;
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        return Authority;
    }
    
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_FIN_NOTE_GL_MAPPING " + pCondition,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsNoteGLMapping ObjCriteria=new clsNoteGLMapping();
                    
                    ObjCriteria.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjCriteria.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjCriteria.setAttribute("YEAR_FROM",rsTmp.getInt("YEAR_FROM"));
                    ObjCriteria.setAttribute("YEAR_TO",rsTmp.getInt("YEAR_TO"));
                    ObjCriteria.setAttribute("NOTE",rsTmp.getInt("NOTE"));
                    ObjCriteria.setAttribute("SUB_NOTE",rsTmp.getInt("SUB_NOTE"));
                    ObjCriteria.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                    ObjCriteria.setAttribute("INDICATOR",rsTmp.getString("INDICATOR"));
                    List.put(Integer.toString(List.size()+1), ObjCriteria);
                    rsTmp.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return List;
    }
    
}
