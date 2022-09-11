

package EITLERP.Finance.ReportsUI;


import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import java.text.*;
import TReportWriter.*;

public class frmRptDepositRenewalJournalTR39 extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private EITLComboModel cmbMonthModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        
        GenerateCombo();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        cmbMonth = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("TR-39 DEPOSIT RENEWAL JOURNAL");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(10, 10, 310, 14);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Month :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 70, 50, 20);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Year :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(190, 70, 40, 20);

        txtYear.setColumns(10);
        getContentPane().add(txtYear);
        txtYear.setBounds(240, 70, 90, 20);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(80, 120, 130, 23);

        getContentPane().add(cmbMonth);
        cmbMonth.setBounds(70, 70, 90, 22);

    }//GEN-END:initComponents
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        GenerateReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        try {
            
            
            int nMonth = cmbMonth.getSelectedIndex() + 1;
            
            String strSQL = "";
            String strSQLNew = "";
            
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("DATE");
            //objReportData.AddColumn("REF_NO");
            objReportData.AddColumn("OLD_ACCOUNT_CODE");
            objReportData.AddColumn("NEW_ACCOUNT_CODE");
            objReportData.AddColumn("OLD_DEPOSITER_CATEGORY");
            objReportData.AddColumn("NEW_DEPOSITER_CATEGORY");
            objReportData.AddColumn("OLD_PARTY_CODE");
            objReportData.AddColumn("NEW_PARTY_CODE");
            objReportData.AddColumn("OLD_RECEIPT_NO");
            objReportData.AddColumn("NEW_RECEIPT_NO");
            objReportData.AddColumn("LEGACY_NO");
            objReportData.AddColumn("OLD_RECEIPT_DATE");
            objReportData.AddColumn("NEW_RECEIPT_DATE");
            objReportData.AddColumn("OLD_PARTY_NAME");
            objReportData.AddColumn("NEW_PARTY_NAME");
            objReportData.AddColumn("AMOUNT");
            
            //objReportData.AddColumn("GROUP_ID");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("DATE","");
            //objOpeningRow.setValue("REF_NO","");
            objOpeningRow.setValue("OLD_ACCOUNT_CODE","");
            objOpeningRow.setValue("NEW_ACCOUNT_CODE","");
            objOpeningRow.setValue("OLD_DEPOSITER_CATEGORY","");
            objOpeningRow.setValue("NEW_DEPOSITER_CATEGORY","");
            objOpeningRow.setValue("OLD_PARTY_CODE","");
            objOpeningRow.setValue("NEW_PARTY_CODE","");
            objOpeningRow.setValue("OLD_RECEIPT_NO","");
            objOpeningRow.setValue("NEW_RECEIPT_NO","");
            objOpeningRow.setValue("LEGACY_NO","");
            objOpeningRow.setValue("OLD_RECEIPT_DATE","0000-00-00");
            objOpeningRow.setValue("NEW_RECEIPT_DATE","0000-00-00");
            objOpeningRow.setValue("OLD_PARTY_NAME","");
            objOpeningRow.setValue("NEW_PARTY_NAME","");
            objOpeningRow.setValue("AMOUNT","");
            //objOpeningRow.setValue("CR_AMOUNT","");
            //objOpeningRow.setValue("GROUP_ID","");
            
            /*//      QUERY FOR OLD RECORD
            strSQL = "SELECT DMST.MAIN_ACCOUNT_CODE,DMST.PARTY_CODE,DMST.OLD_RECEIPT_NO,DMST.APPLICANT_NAME,DMST.AMOUNT, DMST.RECEIPT_DATE,SMST.SCHEME_ID, "+
            "CASE WHEN DMST.DEPOSITER_CATEGORY = 1 THEN 'EMPLOYEE' "+
            "WHEN DMST.DEPOSITER_CATEGORY = 2 THEN 'SHARE HOLDER' "+
            "WHEN DMST.DEPOSITER_CATEGORY = 3 THEN 'COMPANY' "+
            "WHEN DMST.DEPOSITER_CATEGORY = 4 THEN 'DIRECTORS' "+
            "WHEN DMST.DEPOSITER_CATEGORY = 5 THEN 'OTHERS' "+
            "ELSE '' END NAME2, "+
            "CASE WHEN SMST.SCHEME_TYPE=1 THEN 'FD' "+
            "WHEN SMST.SCHEME_TYPE=2 THEN 'LD' "+
            "WHEN SMST.SCHEME_TYPE=3 THEN 'CD' "+
            "ELSE '' END NAME1,DMST.RECEIPT_NO "+
            "FROM D_FD_DEPOSIT_MASTER DMST,D_FD_SCHEME_MASTER SMST "+
            "WHERE DMST.SCHEME_ID=SMST.SCHEME_ID "+
            "AND DMST.COMPANY_ID = SMST.COMPANY_ID "+
            "AND DMST.REJECTED = 0 "+
            "AND DMST.CANCELLED=0 "+
            "AND DMST.APPROVED=1 "+
            "AND DMST.COMPANY_ID= " + EITLERPGLOBAL.gCompanyID + " "+
            "AND DMST.RECEIPT_NO <> '' "+
            "AND DMST.DEPOSIT_ENTRY_TYPE=2 "+
            "AND DMST.RECEIPT_DATE>=CONCAT('"+ txtYear.getText().trim() + "','-','"+ nMonth +"-01') "+
            "AND DMST.RECEIPT_DATE<=LAST_DAY(CONCAT('"+ txtYear.getText().trim() + "','-','"+ nMonth +"-01'))";*/
            
            String qry="SELECT LAST_DAY(CONCAT('"+ txtYear.getText().trim() + "','-','"+ nMonth +"-01')) FROM DUAL ";
            String LastDate = data.getStringValueFromDB(qry,FinanceGlobal.FinURL);
            
            strSQL = "SELECT NEW.MAIN_ACCOUNT_CODE AS NMAIN,OLD.MAIN_ACCOUNT_CODE AS OMAIN, " +
            "NEW.RECEIPT_DATE AS NRDATE ,OLD.RECEIPT_DATE AS ORDATE,NEW.RECEIPT_NO AS NRE,NEW.LEGACY_NO,OLD.RECEIPT_NO AS ORE, " +
            "NEW.PARTY_CODE AS NP,OLD.PARTY_CODE AS OP, NEW.APPLICANT_NAME AS NA,OLD.APPLICANT_NAME AS OA, NEW.AMOUNT, " +
            "CASE WHEN OLD.DEPOSITER_CATEGORY = 1 THEN 'EMPLOYEE' WHEN OLD.DEPOSITER_CATEGORY = 2 THEN 'SHARE HOLDER' " +
            "WHEN OLD.DEPOSITER_CATEGORY = 3 THEN 'COMPANY' WHEN OLD.DEPOSITER_CATEGORY = 4 THEN 'DIRECTORS' " +
            "WHEN OLD.DEPOSITER_CATEGORY = 5 THEN 'OTHERS' ELSE ''  END AS OD, " +
            "CASE WHEN NEW.DEPOSITER_CATEGORY = 1 THEN 'EMPLOYEE' WHEN NEW.DEPOSITER_CATEGORY = 2 THEN 'SHARE HOLDER' " +
            "WHEN NEW.DEPOSITER_CATEGORY = 3 THEN 'COMPANY' WHEN NEW.DEPOSITER_CATEGORY = 4 THEN 'DIRECTORS' " +
            "WHEN NEW.DEPOSITER_CATEGORY = 5 THEN 'OTHERS' ELSE ''  END AS ND, " +
            "CASE WHEN NSC.SCHEME_TYPE=1 THEN 'FD' WHEN NSC.SCHEME_TYPE=2 THEN 'LD' WHEN NSC.SCHEME_TYPE=3 THEN 'CD' " +
            "ELSE '' END AS SNEWTYPE, CASE WHEN OSC.SCHEME_TYPE=1 THEN 'FD' WHEN OSC.SCHEME_TYPE=2 THEN 'LD' " +
            "WHEN OSC.SCHEME_TYPE=3 THEN 'CD' ELSE '' END AS SOLDTYPE " +
            "FROM D_FD_DEPOSIT_MASTER NEW,D_FD_DEPOSIT_MASTER OLD, D_FD_SCHEME_MASTER NSC,  D_FD_SCHEME_MASTER OSC " +
            "WHERE NEW.EFFECTIVE_DATE>=CONCAT('"+ txtYear.getText().trim() + "','-','"+ nMonth +"-01') AND NEW.EFFECTIVE_DATE<='"+LastDate+"' AND NEW.DEPOSIT_ENTRY_TYPE=2 "+
            "AND NEW.OLD_RECEIPT_NO=OLD.RECEIPT_NO AND NEW.SCHEME_ID=NSC.SCHEME_ID AND NEW.COMPANY_ID = NSC.COMPANY_ID " +
            "AND OLD.SCHEME_ID=OSC.SCHEME_ID AND OLD.COMPANY_ID = OSC.COMPANY_ID AND NEW.REJECTED = 0 AND NEW.CANCELLED=0 AND NEW.APPROVED=1 " +
            "ORDER BY NEW.MAIN_ACCOUNT_CODE ";
            
            
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            System.out.println(strSQL);
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    objRow=objReportData.newRow();
                    
                    ++Counter;
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("DATE",LastDate);
                    objRow.setValue("OLD_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"OMAIN",""));
                    objRow.setValue("NEW_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"NMAIN",""));
                    objRow.setValue("OLD_DEPOSITER_CATEGORY",UtilFunctions.getString(rsTmp,"OD",""));
                    objRow.setValue("NEW_DEPOSITER_CATEGORY",UtilFunctions.getString(rsTmp,"ND",""));
                    objRow.setValue("OLD_PARTY_CODE",UtilFunctions.getString(rsTmp,"OP",""));
                    objRow.setValue("NEW_PARTY_CODE",UtilFunctions.getString(rsTmp,"NP",""));
                    objRow.setValue("OLD_RECEIPT_NO",UtilFunctions.getString(rsTmp,"SOLDTYPE","") + UtilFunctions.getString(rsTmp,"ORE",""));
                    objRow.setValue("NEW_RECEIPT_NO",UtilFunctions.getString(rsTmp,"SNEWTYPE","") + UtilFunctions.getString(rsTmp,"NRE",""));
                    objRow.setValue("LEGACY_NO",UtilFunctions.getString(rsTmp,"LEGACY_NO",""));
                    objRow.setValue("OLD_RECEIPT_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ORDATE","")));
                    objRow.setValue("NEW_RECEIPT_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"NRDATE","")));
                    objRow.setValue("OLD_PARTY_NAME",UtilFunctions.getString(rsTmp,"OA",""));
                    objRow.setValue("NEW_PARTY_NAME",UtilFunctions.getString(rsTmp,"NA",""));
                    objRow.setValue("AMOUNT",UtilFunctions.getString(rsTmp,"AMOUNT",""));
                    
                    /*//      QUERY FOR NEW RECORD
                    strSQLNew = "SELECT DMST.MAIN_ACCOUNT_CODE AS NEW_ACCOUNT_CODE ,DMST.PARTY_CODE AS NEW_PARTY_CODE, "+
                    "DMST.RECEIPT_NO AS NEW_RECEIPT_NO,DMST.APPLICANT_NAME AS NEW_APPLICANT_NAME ,DMST.AMOUNT AS CR,DMST.RECEIPT_DATE AS NEW_RECEIPT_DATE, "+
                    "CASE WHEN DMST.DEPOSITER_CATEGORY = 1 THEN 'EMPLOYEE' WHEN DMST.DEPOSITER_CATEGORY = 2 THEN 'SHARE HOLDER' "+
                    "WHEN DMST.DEPOSITER_CATEGORY = 3 THEN 'COMPANY'  WHEN DMST.DEPOSITER_CATEGORY = 4 THEN 'DIRECTORS' "+
                    "WHEN DMST.DEPOSITER_CATEGORY = 5 THEN 'OTHERS' ELSE '' END NEW_NAME2, "+
                    "CASE WHEN SMST.SCHEME_TYPE=1 THEN 'FD'  WHEN SMST.SCHEME_TYPE=2 THEN 'LD' "+
                    "WHEN SMST.SCHEME_TYPE=3 THEN 'CD' ELSE '' END NEW_NAME1 "+
                    "FROM D_FD_DEPOSIT_MASTER DMST,D_FD_SCHEME_MASTER SMST "+
                    "WHERE DMST.SCHEME_ID=SMST.SCHEME_ID "+
                    "AND DMST.COMPANY_ID = SMST.COMPANY_ID "+
                    "AND DMST.DEPOSIT_STATUS=0 "+
                    "AND DMST.REJECTED = 0 "+
                    "AND DMST.COMPANY_ID='2' "+
                    "AND DMST.OLD_RECEIPT_NO='" + UtilFunctions.getString(rsTmp,"OLD_RECEIPT_NO","") + "' "; */
                    
                    
                    //ResultSet rsTmpNew=data.getResult(strSQLNew,FinanceGlobal.FinURL);
                    //rsTmpNew.first();
                    
                    /*objRow.setValue("NEW_ACCOUNT_CODE",UtilFunctions.getString(rsTmpNew,"NEW_ACCOUNT_CODE",""));
                    objRow.setValue("NEW_ACCOUNT_NAME",UtilFunctions.getString(rsTmpNew,"NEW_NAME1","") + "-" + UtilFunctions.getString(rsTmpNew,"NEW_NAME2",""));
                    objRow.setValue("NEW_PARTY_CODE",UtilFunctions.getString(rsTmpNew,"NEW_PARTY_CODE",""));
                    objRow.setValue("NEW_RECEIPT_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmpNew,"NEW_RECEIPT_DATE","0000-00-00")));
                    objRow.setValue("NEW_RECEIPT_NO",UtilFunctions.getString(rsTmpNew,"NEW_NAME1","")+UtilFunctions.getString(rsTmpNew,"NEW_RECEIPT_NO",""));
                    objRow.setValue("NEW_PARTY_NAME",UtilFunctions.getString(rsTmpNew,"NEW_APPLICANT_NAME",""));
                    objRow.setValue("CR_AMOUNT",UtilFunctions.getString(rsTmpNew,"CR",""));*/
                    
                    
                    
                    /*String Group=UtilFunctions.getString(rsTmp,"NAME1","").trim() + "-" + UtilFunctions.getString(rsTmpNew,"NEW_NAME1","").trim();
                    objRow.setValue("GROUP_ID",Group);*/
                    
                    
                    //rsTmpNew.close();
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
                
                
                int Comp_ID = EITLERPGLOBAL.gCompanyID;
                String month_year = cmbMonth.getSelectedItem() + " " + txtYear.getText().trim();
                
                HashMap Parameters=new HashMap();
                //Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
                Parameters.put("MONTH_YEAR",month_year);
                Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
                
                objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptDepositRenewalJournalTR39.rpt",Parameters,objReportData);
                
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private boolean Validate() {
        //Form level validations
        
        if(txtYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Year");
            return false;
        } else if(txtYear.getText().trim().length() != 4 ) {
            
            JOptionPane.showMessageDialog(null,"Invalid Year in YYYY format.");
            return false;
        }
        
        return true;
    }
    
    private void GenerateCombo() {
        
        //--- Generate Type Combo ------//
        ComboData aData=new ComboData();
        
        cmbMonthModel=new EITLComboModel();
        cmbMonth.removeAllItems();
        cmbMonth.setModel(cmbMonthModel);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="January";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="February";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="March";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=4;
        aData.Text="April";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=5;
        aData.Text="May";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=6;
        aData.Text="June";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=7;
        aData.Text="July";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=8;
        aData.Text="August";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=9;
        aData.Text="September";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=10;
        aData.Text="October";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=11;
        aData.Text="November";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=12;
        aData.Text="December";
        cmbMonthModel.addElement(aData);
        
    }
    
}