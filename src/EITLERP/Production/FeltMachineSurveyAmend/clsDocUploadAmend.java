/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.FeltMachineSurveyAmend;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import sdml.felt.commonUI.data;

/**
 *
 * @author root
 */
public class clsDocUploadAmend {
    String AMEND_NO;
    String CD_NAME;
    FileInputStream CD_CIRCUIT_DIAGRAM;
    String CD_MACHINE_DOC_NO;
    String CD_DOC_NAME;

    public String getAMEND_NO() {
        return AMEND_NO;
    }

    public void setAMEND_NO(String AMEND_NO) {
        this.AMEND_NO = AMEND_NO;
    }

    public String getCD_NAME() {
        return CD_NAME;
    }

    public void setCD_NAME(String CD_NAME) {
        this.CD_NAME = CD_NAME;
    }

    public FileInputStream getCD_CIRCUIT_DIAGRAM() {
        return CD_CIRCUIT_DIAGRAM;
    }

    public void setCD_CIRCUIT_DIAGRAM(FileInputStream CD_CIRCUIT_DIAGRAM) {
        this.CD_CIRCUIT_DIAGRAM = CD_CIRCUIT_DIAGRAM;
    }

    public String getCD_MACHINE_DOC_NO() {
        return CD_MACHINE_DOC_NO;
    }

    public void setCD_MACHINE_DOC_NO(String CD_MACHINE_DOC_NO) {
        this.CD_MACHINE_DOC_NO = CD_MACHINE_DOC_NO;
    }

    public String getCD_DOC_NAME() {
        return CD_DOC_NAME;
    }

    public void setCD_DOC_NAME(String CD_DOC_NAME) {
        this.CD_DOC_NAME = CD_DOC_NAME;
    }
    
    public void saveMachineAmendFile(int length)
    {
        try{   
            PreparedStatement statement = data.getConn().prepareStatement("INSERT INTO DOC_MGMT.FELT_MACHINE_AMEND_CIRCUIT_DIAGRAM (AMEND_NO,CD_NAME,CD_CIRCUIT_DIAGRAM,CD_MACHINE_DOC_NO,CD_DOC_NAME) values (?,?,?,?,?)");
            statement.setString(1,AMEND_NO);
            statement.setString(2,CD_NAME);
            statement.setBinaryStream(3,CD_CIRCUIT_DIAGRAM,length);
            statement.setString(4,CD_MACHINE_DOC_NO);
            statement.setString(5,CD_DOC_NAME);
            statement.executeUpdate();
            statement.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ArrayList<clsDocUploadAmend> getStatus(String CD_MACHINE_DOC_NO)
    {
        ArrayList<clsDocUploadAmend> dataList = new ArrayList<clsDocUploadAmend>();
        try{
                ResultSet rs;
                rs=data.getResult("SELECT * FROM DOC_MGMT.FELT_MACHINE_AMEND_CIRCUIT_DIAGRAM where CD_MACHINE_DOC_NO='"+CD_MACHINE_DOC_NO+"'");

                
                rs.first();
                System.out.println("Row no."+rs.getRow());
                
                if (rs.getRow() > 0) {
                    int cnt = 0;
        
                    while (!rs.isAfterLast()) {

                        clsDocUploadAmend Obj = new clsDocUploadAmend();    

                        Obj.setCD_NAME(rs.getString("CD_NAME"));
                        Obj.setCD_DOC_NAME(rs.getString("CD_DOC_NAME"));
                        //rs.getString("CD_MACHINE_DOC_NO");
                        dataList.add(Obj);
                        rs.next();
                    }
                    System.out.println("Completed : Row"+(cnt++));
                }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return dataList;
    }
}
