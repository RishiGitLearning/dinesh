/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common.file_management;

import EITLERP.EITLERPGLOBAL;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import sdml.felt.commonUI.data;

/**
 *
 * @author root
 */
public class clsDocPONoUpdation {

    int DOC_ID;
    String DOC_NAME;
    FileInputStream DOCUMENT;
    String DOCUMENT_DOC_NO;
    String Doc_Sr_No;
    String Doc_Remark;
    String Doc_type;

    public String getDoc_type() {
        return Doc_type;
    }

    public void setDoc_type(String Doc_type) {
        this.Doc_type = Doc_type;
    }

    public String getDoc_Remark() {
        return Doc_Remark;
    }

    public void setDoc_Remark(String Doc_Remark) {
        this.Doc_Remark = Doc_Remark;
    }

    public String getDoc_Sr_No() {
        return Doc_Sr_No;
    }

    public void setDoc_Sr_No(String Doc_Sr_No) {
        this.Doc_Sr_No = Doc_Sr_No;
    }
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

    public void saveDocumentFile(int length) {
        try {
            
            PreparedStatement statement = data.getConn().prepareStatement("INSERT INTO DOC_MGMT.PO_NO_UPDATION "
                    + "(DOC_NAME,DOCUMENT,DOCUMENT_DOC_NO,DOCUMENT_SR_NO,DOC_REMARK,DOC_TYPE) "
                    + "values (?,?,?,?,?,?)");

            statement.setString(1, DOC_NAME);
            statement.setBinaryStream(2, DOCUMENT, length);
            statement.setString(3, DOCUMENT_DOC_NO);
            statement.setString(4, Doc_Sr_No);
            statement.setString(5, Doc_Remark);
            statement.setString(6, Doc_type);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<clsDocPONoUpdation> getStatus(String DOC_NO) {
        ArrayList<clsDocPONoUpdation> dataList = new ArrayList<clsDocPONoUpdation>();
        try {
            ResultSet rs;
            rs = data.getResult("SELECT * FROM DOC_MGMT.PO_NO_UPDATION where DOCUMENT_DOC_NO='" + DOC_NO + "'");

            rs.first();
            System.out.println("Row no." + rs.getRow());

            if (rs.getRow() > 0) {
                int cnt = 0;

                while (!rs.isAfterLast()) {

                    clsDocPONoUpdation Obj = new clsDocPONoUpdation();

                    Obj.setDOC_ID(rs.getInt("DOC_ID"));
                    Obj.setDOC_NAME(rs.getString("DOC_NAME"));
                    Obj.setDOCUMENT_DOC_NO(rs.getString("DOCUMENT_DOC_NO"));
                    //Obj.setDOCUMENT(rs.getBytes("DOCUMENT"));
                    Obj.setDoc(rs.getBlob("DOCUMENT"));
                    Obj.setDoc_Sr_No(rs.getString("DOCUMENT_SR_NO"));
                    Obj.setDoc_Remark(rs.getString("DOC_REMARK"));
                    Obj.setDoc_type(rs.getString("DOC_TYPE"));
                    //rs.getString("CD_MACHINE_DOC_NO");
                    dataList.add(Obj);
                    rs.next();
                }
                System.out.println("Completed : Row" + (cnt++));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
