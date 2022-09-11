

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
import TReportWriter.*;

public class frmRptUnmatchedDepositInterest extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbMonthModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(410,245);
        
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
        lblMonth = new javax.swing.JLabel();
        lblYear = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        cmbMonth = new javax.swing.JComboBox();
        lblBookCode = new javax.swing.JLabel();
        txtBookCode = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("UNMATCHED DEPOSITS INTEREST WARRANTS");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 350, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        lblMonth.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMonth.setText("Month :");
        getContentPane().add(lblMonth);
        lblMonth.setBounds(10, 55, 60, 15);

        lblYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblYear.setText("Year :");
        getContentPane().add(lblYear);
        lblYear.setBounds(210, 54, 40, 14);

        txtYear.setColumns(10);
        getContentPane().add(txtYear);
        txtYear.setBounds(260, 50, 90, 20);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(120, 110, 130, 25);

        getContentPane().add(cmbMonth);
        cmbMonth.setBounds(75, 50, 110, 24);

        lblBookCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBookCode.setText("Book Code :");
        getContentPane().add(lblBookCode);
        lblBookCode.setBounds(108, 83, 80, 15);

        txtBookCode.setColumns(10);
        txtBookCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBookCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBookCodeFocusLost(evt);
            }
        });
        txtBookCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBookCodeKeyPressed(evt);
            }
        });

        getContentPane().add(txtBookCode);
        txtBookCode.setBounds(195, 80, 45, 20);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 190, 400, 20);

    }//GEN-END:initComponents
    
    private void txtBookCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookCodeFocusLost
        // TODO add your handling code here:
        lblStatus.setText("");
    }//GEN-LAST:event_txtBookCodeFocusLost
    
    private void txtBookCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBookCodeKeyPressed
        // TODO add your handling code here:
        
        if(evt.getKeyCode() == 112) {
            LOV aList=new LOV();
            
            aList.SQL="SELECT BOOK_CODE, MAIN_ACCOUNT_CODE, BOOK_NAME AS BANK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE<>'' ORDER BY BOOK_CODE";
            aList.ReturnCol=1;
            aList.SecondCol=2;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                txtBookCode.setText(aList.ReturnVal);
            }
        }
    }//GEN-LAST:event_txtBookCodeKeyPressed
    
    private void txtBookCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookCodeFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Press F1 from the List of Book Code");
    }//GEN-LAST:event_txtBookCodeFocusGained
    
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBookCode;
    private javax.swing.JLabel lblMonth;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblYear;
    private javax.swing.JTextField txtBookCode;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
    
    
    
    private void GenerateReport() {
        
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("BOOK_CODE");
            objReportData.AddColumn("WARRANT_NO");
            objReportData.AddColumn("LEGACY_WARRANT_NO");
            objReportData.AddColumn("WARRANT_DATE");
            objReportData.AddColumn("RECEIPT_NO");
            objReportData.AddColumn("LEGACY_NO");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("DEPOSITORS_NAME");
            objReportData.AddColumn("AMOUNT");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("BOOK_CODE","");
            objOpeningRow.setValue("WARRANT_NO","");
            objOpeningRow.setValue("LEGACY_WARRANT_NO","");
            objOpeningRow.setValue("WARRANT_DATE","0000-00-00");
            objOpeningRow.setValue("RECEIPT_NO","");
            objOpeningRow.setValue("LEGACY_NO","");
            objOpeningRow.setValue("PARTY_CODE","");
            objOpeningRow.setValue("DEPOSITORS_NAME","");
            objOpeningRow.setValue("AMOUNT","");
            
            int nMonth = cmbMonth.getSelectedIndex() + 1;
            String strSQL = "";
            strSQL= "SELECT INTCALDTL.RECEIPT_NO,INTCALDTL.WARRANT_NO,INTCALDTL.LEGACY_WARRANT_NO,INTCALDTL.WARRANT_DATE,INTCALDTL.PARTY_CODE, " +
            "INTCALDTL.WARRANT_CLEAR, INTCALDTL.NET_INTEREST AS AMOUNT " +
            "FROM D_FD_INT_CALC_DETAIL INTCALDTL,D_FD_INT_CALC_HEADER INTCALHD " +
            "WHERE INTCALDTL.DOC_NO=INTCALHD.DOC_NO AND INTCALDTL.COMPANY_ID = '"+EITLERPGLOBAL.gCompanyID+"' AND INTCALDTL.WARRANT_CLEAR='I' " +
            "AND INTCALHD.BOOK_CODE ='"+txtBookCode.getText().trim()+"' AND INTCALDTL.WARRANT_DATE<=LAST_DAY(CONCAT('"+ txtYear.getText().trim() + "','-','"+ nMonth +"-01')) " +
            "AND INTCALHD.APPROVED=1 AND INTCALHD.CANCELLED=0 ORDER BY INTCALDTL.WARRANT_NO ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    String PartyCode = UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                    String ReceiptNo = UtilFunctions.getString(rsTmp,"RECEIPT_NO","");
                    String ApplicantName = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                    String LegacyNo = data.getStringValueFromDB("SELECT LEGACY_NO FROM D_FD_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("BOOK_CODE",txtBookCode.getText().trim());
                    objRow.setValue("WARRANT_NO",UtilFunctions.getString(rsTmp,"WARRANT_NO",""));
                    objRow.setValue("LEGACY_WARRANT_NO",Integer.toString(UtilFunctions.getInt(rsTmp,"LEGACY_WARRANT_NO",0)));
                    objRow.setValue("WARRANT_DATE", EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"WARRANT_DATE","0000-00-00")));
                    objRow.setValue("RECEIPT_NO",UtilFunctions.getString(rsTmp,"RECEIPT_NO",""));
                    objRow.setValue("LEGACY_NO",LegacyNo);
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                    objRow.setValue("DEPOSITORS_NAME",ApplicantName);
                    
                    strSQL = "SELECT * FROM D_FD_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND RECEIPT_NO='"+ReceiptNo+"' AND DEPOSIT_TYPE_ID=2";
                    if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                        strSQL = "SELECT SUM(B.NET_INTEREST) FROM D_FD_INT_CALC_HEADER A, D_FD_INT_CALC_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND B.PARTY_CODE='"+PartyCode+"' AND B.RECEIPT_NO='"+ReceiptNo+"' AND B.WARRANT_CLEAR IN ('I','N') AND A.TDS_ONLY=0 ";
                        double Amount = data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        objRow.setValue("AMOUNT",Double.toString(Amount));
                    } else {
                        objRow.setValue("AMOUNT",Double.toString(UtilFunctions.getDouble(rsTmp,"AMOUNT",0)));
                    }
                    objReportData.AddRow(objRow);
                    rsTmp.next();
                }
            }
            
            String qry="SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE= '" + txtBookCode.getText().trim() + "' ";
            String BookName = data.getStringValueFromDB(qry,FinanceGlobal.FinURL);
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            String sDate;
            if(cmbMonth.getSelectedIndex()+1 >= 1 &&  cmbMonth.getSelectedIndex()+1 <= 9) {
                sDate = txtYear.getText().trim() + "-0" + Integer.toString(cmbMonth.getSelectedIndex()+1) + "-01";
            }
            else {
                sDate = txtYear.getText().trim() + "-" + Integer.toString(cmbMonth.getSelectedIndex()+1) + "-01";
            }
            String LastDate = data.getStringValueFromDB("SELECT LAST_DAY('"+ sDate +"') FROM DUAL");
            
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("FROM_DATE", EITLERPGLOBAL.formatDate(LastDate));
            Parameters.put("TO_DATE","0000-00-00");
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("BOOK_NAME",txtBookCode.getText().trim()+"-"+BookName);
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptUnmatchedDepositInterest.rpt",Parameters,objReportData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
        
        
        
        cmbReceiptTypeModel=new EITLComboModel();
        //        cmbReceiptType.removeAllItems();
        //      cmbReceiptType.setModel(cmbReceiptTypeModel);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="FD TO FD";
        cmbReceiptTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="CD TO CD";
        cmbReceiptTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="LD TO LD";
        cmbReceiptTypeModel.addElement(aData);
        
    }
    
    private boolean Validate() {
        //Form level validations
        
        if(txtYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Year.");
            return false;
        } else if(txtYear.getText().trim().length() != 4 ) {
            
            JOptionPane.showMessageDialog(null,"Invalid Year in YYYY format.");
            return false;
        }
        if(txtBookCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enetere the Book Code.");
            return false;
            
        }
        
        return true;
    }
}
