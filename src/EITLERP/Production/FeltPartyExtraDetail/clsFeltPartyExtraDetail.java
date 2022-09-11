/*
 * clsFeltPartyExtra.java
 *
 * Created on June 15, 2013, 1:08 PM
 */

package EITLERP.Production.FeltPartyExtraDetail;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Variant;
import EITLERP.data;
import EITLERP.ComboData;

/**
 *
 * @author  Vivek Kumar
 * @version
 */

class clsFeltPartyExtraDetail {
    public String LastError="";
    private static Connection connection;
    //Felt Party Extra Details Collection
    public HashMap hmFeltPartyExtraDetails;
    
    public clsFeltPartyExtraDetail() {
        LastError = "";
        hmFeltPartyExtraDetails=new HashMap();
        connection=data.getConn();
    }
    
    //Updates current record
    public boolean Insert(String partyCode) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        try {
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PARTY_EXTRA_INFO WHERE PARTY_CODE='"+ partyCode +"'");
            
            for(int i=1;i<=hmFeltPartyExtraDetails.size();i++) {
                //Now Update records into Felt Party Extra Info tables
                clsFeltPartyExtraDetails ObjFeltPartyExtraDetails=(clsFeltPartyExtraDetails) hmFeltPartyExtraDetails.get(Integer.toString(i));
                
                resultSetTemp.moveToInsertRow();
                resultSetTemp.updateString("PARTY_CODE",ObjFeltPartyExtraDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("CONTACT_PERSON",ObjFeltPartyExtraDetails.getAttribute("CONTACT_PERSON").getString());
                resultSetTemp.updateString("MOBILE",ObjFeltPartyExtraDetails.getAttribute("MOBILE").getString());
                resultSetTemp.updateString("LANDLINE",ObjFeltPartyExtraDetails.getAttribute("LANDLINE").getString());
                resultSetTemp.updateString("EMAIL",ObjFeltPartyExtraDetails.getAttribute("EMAIL").getString());
                resultSetTemp.updateString("FAX",ObjFeltPartyExtraDetails.getAttribute("FAX").getString());
                resultSetTemp.updateString("OFFICE_ADD",ObjFeltPartyExtraDetails.getAttribute("OFFICE_ADDRESS").getString());
                resultSetTemp.updateString("WORK_ADD",ObjFeltPartyExtraDetails.getAttribute("WORK_ADDRESS").getString());
                resultSetTemp.insertRow();
            }
            getData(partyCode);
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
    
    //Updates current record
    public boolean Update(String partyCode) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        try {
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM  PRODUCTION.FELT_PARTY_EXTRA_INFO WHERE PARTY_CODE='"+ partyCode +"'");
            int i=1;
            while(resultSetTemp.next()){
                //Now Update records into Felt Party Extra Info tables
                clsFeltPartyExtraDetails ObjFeltPartyExtraDetails=(clsFeltPartyExtraDetails) hmFeltPartyExtraDetails.get(Integer.toString(i));
                
                resultSetTemp.updateString("PARTY_CODE",ObjFeltPartyExtraDetails.getAttribute("PARTY_CODE").getString());
                resultSetTemp.updateString("CONTACT_PERSON",ObjFeltPartyExtraDetails.getAttribute("CONTACT_PERSON").getString());
                resultSetTemp.updateString("MOBILE",ObjFeltPartyExtraDetails.getAttribute("MOBILE").getString());
                resultSetTemp.updateString("LANDLINE",ObjFeltPartyExtraDetails.getAttribute("LANDLINE").getString());
                resultSetTemp.updateString("EMAIL",ObjFeltPartyExtraDetails.getAttribute("EMAIL").getString());
                resultSetTemp.updateString("FAX",ObjFeltPartyExtraDetails.getAttribute("FAX").getString());
                resultSetTemp.updateString("OFFICE_ADD",ObjFeltPartyExtraDetails.getAttribute("OFFICE_ADDRESS").getString());
                resultSetTemp.updateString("WORK_ADD",ObjFeltPartyExtraDetails.getAttribute("WORK_ADDRESS").getString());
                
                resultSetTemp.updateRow();
                i++;
            }
            
            getData(partyCode);
            resultSetTemp.close();
            statementTemp.close();
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Updates incharge code and zone in sales party master
    public boolean Update(String partyCode, int inchargeCode, String zone) {
        try {
            connection.createStatement().executeUpdate("UPDATE D_SAL_PARTY_MASTER SET INCHARGE_CD='"+inchargeCode+"', ZONE='"+zone+"' WHERE PARTY_CODE='"+partyCode+"' AND MAIN_ACCOUNT_CODE='210010'");            
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
        
    public boolean getData(String partyCode) {
        ResultSet  resultSetTemp;
        Statement  statementTemp;
        int serialNoCounter=0;
        
        try {
            hmFeltPartyExtraDetails.clear();
            
            statementTemp = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSetTemp = statementTemp.executeQuery("SELECT * FROM (SELECT * FROM  PRODUCTION.FELT_PARTY_EXTRA_INFO WHERE PARTY_CODE='"+ partyCode +"') E LEFT JOIN (SELECT INCHARGE_CD, ZONE, PARTY_CODE  FROM  D_SAL_PARTY_MASTER) P ON E.PARTY_CODE=P.PARTY_CODE");
            
            while(resultSetTemp.next()) {
                serialNoCounter++;
                
                clsFeltPartyExtraDetails ObjFeltPartyExtraDetails = new clsFeltPartyExtraDetails();
                
                ObjFeltPartyExtraDetails.setAttribute("PARTY_CODE",resultSetTemp.getString("PARTY_CODE"));
                ObjFeltPartyExtraDetails.setAttribute("CONTACT_PERSON",resultSetTemp.getString("CONTACT_PERSON"));
                ObjFeltPartyExtraDetails.setAttribute("MOBILE",resultSetTemp.getString("MOBILE"));
                ObjFeltPartyExtraDetails.setAttribute("LANDLINE",resultSetTemp.getString("LANDLINE"));
                ObjFeltPartyExtraDetails.setAttribute("EMAIL",resultSetTemp.getString("EMAIL"));
                ObjFeltPartyExtraDetails.setAttribute("FAX",resultSetTemp.getString("FAX"));
                ObjFeltPartyExtraDetails.setAttribute("OFFICE_ADDRESS",resultSetTemp.getString("OFFICE_ADD"));
                ObjFeltPartyExtraDetails.setAttribute("WORK_ADDRESS",resultSetTemp.getString("WORK_ADD"));
                
                hmFeltPartyExtraDetails.put(Integer.toString(serialNoCounter),ObjFeltPartyExtraDetails);
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
    
    // it creates list of Felt Incharge
    public HashMap getInchargeNameList(){
        HashMap hmInchargeNameList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT INCHARGE_CD, INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("INCHARGE_CD");
                aData.Text=rsTmp.getString("INCHARGE_NAME");
                hmInchargeNameList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            LastError = e.getMessage();
            e.printStackTrace();
        }
        return hmInchargeNameList;
    }
    
    /**
     * checkes party code in party master table
     * @return true if exist else false
     */
    public boolean checkPartyCode(String partyCode){
        if(data.getIntValueFromDB("SELECT COUNT(PARTY_CODE+0) FROM D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE+0='"+partyCode+"'")==1) return true;
        else return false;
    }
    
    public String[] getPartyDetails(String partyCode){
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String []partyDetails = new String[3] ;
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT PARTY_NAME,COALESCE(INCHARGE_CD,0) INCHARGE_CD,ZONE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE+0='"+partyCode+"' AND MAIN_ACCOUNT_CODE=210010");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                partyDetails[0] = rsTmp.getString("PARTY_NAME");
                partyDetails[1] = rsTmp.getString("INCHARGE_CD");
                partyDetails[2] = rsTmp.getString("ZONE");
            }
            
            Stmt.close();
            rsTmp.close();
            
            return partyDetails;
        }
        catch(Exception e) {
            e.printStackTrace();
            return partyDetails;
        }
    }
    
}
