/*
 * clsFeltMonthlySalesPlan.java
 *
 * Created on December 8, 2017, 1:22 PM
 */

package EITLERP.FeltSales.MonthlySalesPlan;

/**
 *
 * @author  root
 */

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Order.clsFeltOrder;
import EITLERP.Variant;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;
import EITLERP.*;
import java.sql.SQLException;

public class clsFeltMonthlySalesPlan {
    
    private HashMap props;
    public boolean Ready = false;

    public HashMap hmSelection;

    private ResultSet resultSet;
    private static Connection connection;
    private Statement statement;
    public String LastError = "";

    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }

    public void setAttribute(String PropName, Object Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, int Value) {

        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, long Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, double Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, float Value) {
        props.put(PropName, new Variant(Value));
    }

    public void setAttribute(String PropName, boolean Value) {
        props.put(PropName, new Variant(Value));
    }
    
    /** Creates a new instance of clsFeltMonthlySalesPlan */
    public clsFeltMonthlySalesPlan() {
        props = new HashMap();

        props.put("SP_MONTH", new Variant(""));
        props.put("SP YEAR", new Variant(""));
        props.put("SP_PIECE_NO", new Variant(""));
        props.put("SP_PARTY_CODE", new Variant(""));
        props.put("SP_PARTY_NAME", new Variant(""));
        props.put("SP_PRODUCT_CODE", new Variant(""));
        props.put("SP_PRODUCT_NAME", new Variant(""));
        props.put("SP_GROUP", new Variant(""));
        props.put("SP_PIECE_STATUS", new Variant(""));
        props.put("SP_LENGTH", new Variant(""));
        props.put("SP_WIDTH", new Variant(""));
        props.put("SP_GSM", new Variant(""));
        props.put("SP_MACHINE_NO", new Variant(""));
        props.put("SP_POSITION", new Variant(""));
        props.put("SP_WEAVING_DATE", new Variant(""));
        props.put("SP_MENDING_DATE", new Variant(""));
        props.put("SP_NEEDLING_DATE", new Variant(""));
        props.put("SP_FINISHING_DATE", new Variant(""));
        props.put("SP_WEIGHT", new Variant(""));
        props.put("SP_NEW_PIECE_STAGE", new Variant(""));
        props.put("SP_NEW_MFR_STATUS", new Variant(""));
        props.put("SP_NEW_MFR_SINCE_DAYS", new Variant(""));
        props.put("SP_FIRST_PI_DATE", new Variant(""));
        props.put("SP_PI_SINCE_DAYS", new Variant(""));
        props.put("SP_STOCK_SINCE_DAYS", new Variant(""));
        props.put("SP_STOCK_DAYS_CRITERIA", new Variant(""));
        props.put("SP_INVOICE_NO", new Variant(""));
        props.put("SP_INVOICE_DATE", new Variant(""));
        props.put("SP_INVOICE_AMOUNT", new Variant(""));
        props.put("SP_ORDER_AMOUNT", new Variant(""));
        props.put("SP_BALE_NO", new Variant(""));
        props.put("SP_BALE_DATE", new Variant(""));
        props.put("SP_FIRST_PI_NO", new Variant(""));
        props.put("SP_LAST_PI_DATE", new Variant(""));
        props.put("SP_LAST_PI_NO", new Variant(""));
        props.put("SP_TOTAL_PI_SEND_NO", new Variant(""));
        props.put("SP_FIRST_PI_DAYS", new Variant(""));
        props.put("SP_LAST_PI_DAYS", new Variant(""));
        props.put("SP_ORDER_DATE", new Variant(""));
        props.put("SP_ENTRY_DATE", new Variant(""));
        props.put("SP_GROUP_NAME", new Variant(""));
        props.put("SP_POSITION_DESC", new Variant(""));        

        hmSelection = new HashMap();
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            connection = data.getConn();
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet=statement.executeQuery("SELECT * FROM PRODUCTION.FELT_MONTHLY_SALES_PLAN LIMIT 1");                               
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
    
    
    public boolean Insert() {
        ResultSet  resultSetSelection;
        Statement  statementSelection;
        try {
            // Production data connection
            connection = data.getConn();
            statementSelection = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSetSelection = statementSelection.executeQuery("SELECT * FROM  PRODUCTION.FELT_MONTHLY_SALES_PLAN WHERE SP_MONTH=''");            
            
            //Now Insert records into tables
            for(int i=1;i<=hmSelection.size();i++) {
                //clsFeltWeavingDetails ObjFeltProductionWeavingDetails=(clsFeltWeavingDetails) hmSelection.get(Integer.toString(i));
                clsFeltMonthlySalesPlan ObjItem=(clsFeltMonthlySalesPlan) hmSelection.get(Integer.toString(i));
                
                //Insert records into table
                resultSetSelection.moveToInsertRow();
                
                resultSetSelection.updateString("SP_MONTH", ObjItem.getAttribute("SP_MONTH").getString());
                resultSetSelection.updateString("SP_YEAR", ObjItem.getAttribute("SP_YEAR").getString());
                resultSetSelection.updateString("SP_PIECE_NO", ObjItem.getAttribute("SP_PIECE_NO").getString());
                resultSetSelection.updateString("SP_PARTY_CODE",ObjItem.getAttribute("SP_PARTY_CODE").getString());
                resultSetSelection.updateString("SP_PARTY_NAME",ObjItem.getAttribute("SP_PARTY_NAME").getString());
                resultSetSelection.updateString("SP_PIECE_STATUS",ObjItem.getAttribute("SP_PIECE_STATUS").getString());
                resultSetSelection.updateString("SP_MACHINE_NO",ObjItem.getAttribute("SP_MACHINE_NO").getString());
                resultSetSelection.updateString("SP_POSITION", ObjItem.getAttribute("SP_POSITION").getString());
                //resultSetSelection.updateFloat("SP_LENGTH",(float)EITLERPGLOBAL.round(ObjItem.getAttribute("SP_LENGTH").getVal(),2));
                resultSetSelection.updateDouble("SP_LENGTH",EITLERPGLOBAL.round(Double.parseDouble(ObjItem.getAttribute("SP_LENGTH").getString()),2));
                resultSetSelection.updateDouble("SP_WIDTH",EITLERPGLOBAL.round(Double.parseDouble(ObjItem.getAttribute("SP_WIDTH").getString()),2));
                resultSetSelection.updateDouble("SP_WEIGHT",EITLERPGLOBAL.round(Double.parseDouble(ObjItem.getAttribute("SP_WEIGHT").getString()),2));
                resultSetSelection.updateInt("SP_GSM",Integer.parseInt(ObjItem.getAttribute("SP_GSM").getString()));
                resultSetSelection.updateString("SP_PRODUCT_CODE",ObjItem.getAttribute("SP_PRODUCT_CODE").getString());
                resultSetSelection.updateString("SP_PRODUCT_NAME",data.getStringValueFromDB("SELECT DISTINCT PRODUCT_DESC FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+ObjItem.getAttribute("SP_PRODUCT_CODE").getString()+"'"));
                
                resultSetSelection.updateString("SP_WEAVING_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("SP_WEAVING_DATE").getString()));
                resultSetSelection.updateString("SP_NEEDLING_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("SP_NEEDLING_DATE").getString()));
                resultSetSelection.updateString("SP_FINISHING_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("SP_FINISHING_DATE").getString()));
                resultSetSelection.updateString("SP_MENDING_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("SP_MENDING_DATE").getString()));
                resultSetSelection.updateString("SP_GROUP",ObjItem.getAttribute("SP_GROUP").getString());
                //resultSetSelection.updateString("SP_GROUP",data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL"));
                resultSetSelection.updateString("SP_ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                resultSetSelection.insertRow();                                
            }
                               
            JOptionPane.showMessageDialog(null,""+hmSelection.size()+" Selection added.");
            resultSetSelection.close();
            statementSelection.close();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
     public static HashMap getInstockList() {
        HashMap List= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MONTHLY_SALES_PLAN' ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                List.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
      //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
    }
    
    public static HashMap getWIPList() {
        HashMap List= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID='MONTHLY_SALES_PLAN_W' ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                List.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
      //      LastError = e.getMessage();
            e.printStackTrace();
        }
        return List;
    }   
    
}
