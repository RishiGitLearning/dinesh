/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common.file_management;

import EITLERP.EITLERPGLOBAL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import sdml.felt.commonUI.data;

/**
 *
 * @author root
 */
public class clsDocMissedPunchCCTV {
    
    int DOC_ID;
    String DOC_NAME;
    FileInputStream DOCUMENT;
    String DOCUMENT_DOC_NO;
    Blob Doc;

    public int getDOC_ID() {
        return DOC_ID;
    }

    public void setDOC_ID(int DOC_ID) {
        this.DOC_ID = DOC_ID;
    }

    public String getDOC_NAME() {
        return DOC_NAME;
    }

    public void setDOC_NAME(String DOC_NAME) {
        this.DOC_NAME = DOC_NAME;
    }

    public FileInputStream getDOCUMENT() {
        return DOCUMENT;
    }

    public void setDOCUMENT(FileInputStream DOCUMENT) {
        this.DOCUMENT = DOCUMENT;
    }

    public String getDOCUMENT_DOC_NO() {
        return DOCUMENT_DOC_NO;
    }

    public void setDOCUMENT_DOC_NO(String DOCUMENT_DOC_NO) {
        this.DOCUMENT_DOC_NO = DOCUMENT_DOC_NO;
    }

    public Blob getDoc() {
        return Doc;
    }

    public void setDoc(Blob Doc) {
        this.Doc = Doc;
    }

    
    
    public void saveDocumentFile(int length)
    {
        try{   
            PreparedStatement statement = data.getConn().prepareStatement("INSERT INTO DOC_MGMT.MISSED_PUNCH_FOOTAGE (DOC_NAME,DOCUMENT,DOCUMENT_DOC_NO) values (?,?,?)");
            
            statement.setString(1,DOC_NAME);
            statement.setBinaryStream(2,DOCUMENT,length);
            statement.setString(3,DOCUMENT_DOC_NO);
            statement.executeUpdate();
            statement.close();
            System.out.println("INSERT INTO DOC_MGMT.MISSED_PUNCH_FOOTAGE (DOCUMENT_DOC_NO,DOC_NAME,DATE_OF_ENTRY,ACTION,USERID) VALUES ('"+DOCUMENT_DOC_NO+"','"+DOC_NAME+"','"+EITLERPGLOBAL.getCurrentDateTimeDB()+"','ADD','"+EITLERPGLOBAL.gUserID+"')");
            data.Execute("INSERT INTO DOC_MGMT.MISSED_PUNCH_FOOTAGE (DOCUMENT_DOC_NO,DOC_NAME,DATE_OF_ENTRY,ACTION,USERID) VALUES ('"+DOCUMENT_DOC_NO+"','"+DOC_NAME+"','"+EITLERPGLOBAL.getCurrentDateTimeDB()+"','ADD','"+EITLERPGLOBAL.gUserID+"')");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
    public ArrayList<clsDocMissedPunchCCTV> getStatus(String DOC_NO)
    {
        ArrayList<clsDocMissedPunchCCTV> dataList = new ArrayList<clsDocMissedPunchCCTV>();
        try{
                ResultSet rs;
                rs=data.getResult("SELECT * FROM DOC_MGMT.MISSED_PUNCH_FOOTAGE where DOCUMENT_DOC_NO='"+DOC_NO+"'");

                
                rs.first();
                System.out.println("Row no."+rs.getRow());
                
                if (rs.getRow() > 0) {
                    int cnt = 0;
        
                    while (!rs.isAfterLast()) {

                        clsDocMissedPunchCCTV Obj = new clsDocMissedPunchCCTV();    

                        Obj.setDOC_ID(rs.getInt("DOC_ID"));
                        Obj.setDOC_NAME(rs.getString("DOC_NAME"));
                        Obj.setDOCUMENT_DOC_NO(rs.getString("DOCUMENT_DOC_NO"));
                        //Obj.setDOCUMENT(rs.getBytes("DOCUMENT"));
                        Obj.setDoc(rs.getBlob("DOCUMENT"));
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
