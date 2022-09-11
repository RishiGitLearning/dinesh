/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP;

import java.sql.*;

/**
 *
 * @author Dharmendra
 */
public class PendingDocList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            Connection Conn;
            Statement stmt;
            PreparedStatement pstm = null;
            ResultSet tmprs;
            Conn = data.getConn();
            stmt = Conn.createStatement();
            Conn.setAutoCommit(false);
            pstm = Conn.prepareStatement("INSERT INTO `PRODUCTION`.`TEMP_TABLE` (`DOC_NO`,`MODULE_ID`,`RECEIVED_DATE`,`MODULE_DESC`,`HIERARCHY_ID`,`CREATED_DATE`,`PARTY_CODE`,`PARTY_NAME`,`USER`,`STATUS`) VALUES (?,?,?,?,?,?,?,?,?,?)");
            String query = "SELECT DISTINCT DOC_NO,MODULE_ID,RECEIVED_DATE,D.USER_ID,M.USER_NAME,D.STATUS FROM DINESHMILLS.D_COM_DOC_DATA D,DINESHMILLS.D_COM_USER_MASTER M WHERE STATUS IN ('W','P') AND D.USER_ID=M.USER_ID AND M.DEPT_ID=29 ";
            data.Execute("TRUNCATE PRODUCTION.TEMP_TABLE");
            ResultSet rs = data.getResult(query);
            rs.first();
            int i = 0;
            rs.previous();
            while (rs.next()) {
                String DOC_NO = rs.getString("DOC_NO");
                String MODULE_ID = rs.getString("MODULE_ID");
                String RECEIVED_DATE = rs.getString("RECEIVED_DATE");
                String USER_NAME = rs.getString("USER_NAME");
                String database = "";
                String STATUS = rs.getString("STATUS");
                if (STATUS.equals("W")) {
                    STATUS = "WAITING";
                } else {
                    STATUS = "PENDING";
                }
                String MODULE_desc = "";
                tmprs = stmt.executeQuery("SELECT MODULE_DESC FROM DINESHMILLS.D_COM_MODULES WHERE MODULE_ID=" + MODULE_ID);
                tmprs.first();
                MODULE_desc = tmprs.getString("MODULE_DESC");
                tmprs = stmt.executeQuery("SELECT HEADER_TABLE_NAME,DOC_NO_FIELD,DOC_DATE_FIELD  FROM DINESHMILLS.D_COM_MODULES where MODULE_ID='" + MODULE_ID + "'");
                tmprs.first();
                String header_table = tmprs.getString("HEADER_TABLE_NAME");
                String doc_no_field = tmprs.getString("DOC_NO_FIELD");
                String doc_date_field = tmprs.getString("DOC_DATE_FIELD");
                System.out.println("Document   " + DOC_NO);
                if (header_table.contains(".")) {

                } else {
                    try {
                        tmprs = stmt.executeQuery("SELECT * FROM DINESHMILLS." + header_table);

                        if (tmprs.getRow() > 0) {
                            database = "DINESHMILLS";
                        }
                    } catch (Exception e) {
                        try {
                            tmprs = stmt.executeQuery("SELECT * FROM PRODUCTION." + header_table);
                            tmprs.first();
                            if (tmprs.getRow() > 0) {
                                database = "PRODUCTION";
                            }
                        } catch (Exception a) {
                            try {
                                tmprs = stmt.executeQuery("SELECT * FROM FINANCE." + header_table);
                                tmprs.first();
                                if (tmprs.getRow() > 0) {
                                    database = "FINANCE";
                                }
                            } catch (Exception b) {
                                System.out.println("No Database Found  = " + header_table);
                            }
                        }
                    }
                    header_table = database + "." + header_table;
                }
                tmprs = stmt.executeQuery("SELECT * FROM " + header_table + " WHERE " + doc_no_field + "='" + DOC_NO + "'");
                tmprs.first();
                String Hierarchy_id = tmprs.getString("HIERARCHY_ID");
                String created_date = tmprs.getString("CREATED_DATE");
                String party_code;
                try {
                    party_code = tmprs.getString("PARTY_CODE");
                } catch (Exception e) {
                    party_code = "";
                }
                pstm.setString(1, DOC_NO);
                pstm.setString(2, MODULE_ID);
                pstm.setString(3, RECEIVED_DATE);
                pstm.setString(4, MODULE_desc);
                pstm.setString(5, Hierarchy_id);
                pstm.setString(6, created_date);
                pstm.setString(7, party_code);
                pstm.setString(8, "");
                pstm.setString(9, USER_NAME);
                pstm.setString(10, STATUS);
                pstm.addBatch();
                i++;
                if ((i + 1) % 1000 == 0) {
                    pstm.executeBatch();
                    pstm.executeBatch();
                    Conn.commit();
                }
            }
            pstm.executeBatch();
            pstm.executeBatch();
            Conn.commit();
            Conn.setAutoCommit(true);
            data.Execute("UPDATE PRODUCTION.TEMP_TABLE T,DINESHMILLS.D_SAL_PARTY_MASTER M SET T.PARTY_NAME=M.PARTY_NAME WHERE T.PARTY_CODE=M.PARTY_CODE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
