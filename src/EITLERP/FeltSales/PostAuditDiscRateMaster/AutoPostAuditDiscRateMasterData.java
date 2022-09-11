/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PostAuditDiscRateMaster;

import EITLERP.clsFirstFree;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class AutoPostAuditDiscRateMasterData {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        Auto_PA_Data();
    }

    private static void Auto_PA_Data() {
        String sql="",pDocNo="",pDocDate="",SubDate="";
        
        try {
//            SubDate = data.getStringValueFromDB("SELECT SUBDATE(CURDATE(), INTERVAL 90 DAY) FROM DUAL");
            SubDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
            
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE APPROVED=1 AND CANCELED=0 AND APPROVED_DATE='"+SubDate+"' ");
//            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE APPROVED=1 AND CANCELED=0 AND APPROVED_DATE>='2020-05-01' AND APPROVED_DATE<'2020-06-01' ");
            rsData1.first();
            
            if (rsData1.getRow() > 0) {
                pDocNo = clsFirstFree.getNextFreeNo(2, 635, 360, true);
                pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
            
                while (!rsData1.isAfterLast()) {
                    
                    String pMasterNo = rsData1.getString("MASTER_NO");
                    String pSanctionDate = rsData1.getString("SANCTION_DATE");
                    String pPartyCode = rsData1.getString("PARTY_CODE");
                    String pPartyName = rsData1.getString("PARTY_NAME");
                    String pGroupCode = rsData1.getString("GROUP_CODE");
                    String pGroupName = rsData1.getString("GROUP_NAME");
                    String pEffectiveFromDate = rsData1.getString("EFFECTIVE_FROM");
                    String pEffectiveToDate = rsData1.getString("EFFECTIVE_TO");
                    
                    sql = "INSERT INTO PRODUCTION.POST_AUDIT_DISC_RATE_MASTER_DATA "                            
                            + "(DOC_NO, DOC_DATE, SELECTED_FLAG, MASTER_NO, SANCTION_DATE, PARTY_CODE, PARTY_NAME, GROUP_CODE, GROUP_NAME, "
                            + "EFFECTIVE_FROM, EFFECTIVE_TO, AUDIT_REMARK, FINAL_APPROVAL_DATE, "
                            + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,"
                            + "HIERARCHY_ID,CHANGED,CHANGED_DATE) "
                            + "VALUES('" + pDocNo + "','" + pDocDate + "','0','" + pMasterNo + "','" + pSanctionDate + "','" + pPartyCode + "','" + pPartyName + "','" + pGroupCode + "','" + pGroupName + "',"
                            + "'" + pEffectiveFromDate + "','" + pEffectiveToDate + "','','0000-00-00',"
                            + "9,'" + pDocDate + "',NULL,NULL,0,'0000-00-00',0,'0000-00-00',0,"
                            + "4419,1,'" + pDocDate + "')";
                    System.out.println("Insert Into Auto PAD Data :" + sql);
                    data.Execute(sql);
                    
                    sql = "INSERT INTO PRODUCTION.FELT_AUTO_PI_DATA_H "                            
                            + "(REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,"
                            + "DOC_NO, DOC_DATE, SELECTED_FLAG, MASTER_NO, SANCTION_DATE, PARTY_CODE, PARTY_NAME, GROUP_CODE, GROUP_NAME, "
                            + "EFFECTIVE_FROM, EFFECTIVE_TO, AUDIT_REMARK, FINAL_APPROVAL_DATE,"
                            + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,"
                            + "HIERARCHY_ID,CHANGED,CHANGED_DATE) "
                            + "VALUES (1,9,'" + pDocDate + "','W','',"
                            + "'" + pDocNo + "','" + pDocDate + "','0','" + pMasterNo + "','" + pSanctionDate + "','" + pPartyCode + "','" + pPartyName + "','" + pGroupCode + "','" + pGroupName + "',"
                            + "'" + pEffectiveFromDate + "','" + pEffectiveToDate + "','','0000-00-00',"
                            + "9,'" + pDocDate + "',NULL,NULL,0,'0000-00-00',0,'0000-00-00',0,"
                            + "4419,1,'" + pDocDate + "')";
                    System.out.println("Insert Into History of Auto PAD Data :" + sql);
                    data.Execute(sql);
                    
                    sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                            + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,ACTION_DATE,CHANGED,CHANGED_DATE) "
                            + "VALUES(635,'" + pDocNo + "','" + pDocDate + "',9,'W','C','ERP SYSTEM AUTO GENERATED',1,0,NULL,'" + pDocDate + "','0000-00-00',1,'" + pDocDate + "')";
                    System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                    data.Execute(sql);
                    
                    rsData1.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error while Saving : " + e.getMessage());
        }
    }
}
