/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import TReportWriter.*;
import java.util.*;

public class frmReceiptLegacyNo extends javax.swing.JApplet {
    
    private TReportEngine objEngine=new TReportEngine();
    private EITLComboModel cmbSelectTypeModel;
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(404,250);
        initComponents();
        GenerateCombo();
        chkLockPeriod.setVisible(false);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblToDate = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        cmdGenerateNo = new javax.swing.JButton();
        chkLockPeriod = new javax.swing.JCheckBox();
        lblBookCode = new javax.swing.JLabel();
        txtBookCode = new javax.swing.JTextField();
        lblBookName = new javax.swing.JLabel();
        lblLegacyInfo = new javax.swing.JLabel();
        lblSelectType = new javax.swing.JLabel();
        cmbSelectType = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("Receipt Voucher Numbering");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(1, 2, 800, 30);

        lblToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDate.setText("As On Date :");
        getContentPane().add(lblToDate);
        lblToDate.setBounds(10, 72, 85, 15);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(100, 70, 90, 20);

        cmdGenerateNo.setText("Generate No.");
        cmdGenerateNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGenerateNoActionPerformed(evt);
            }
        });

        getContentPane().add(cmdGenerateNo);
        cmdGenerateNo.setBounds(50, 170, 130, 25);

        chkLockPeriod.setText(" Lock Period");
        getContentPane().add(chkLockPeriod);
        chkLockPeriod.setBounds(250, 170, 110, 20);

        lblBookCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBookCode.setText("Book Code:");
        getContentPane().add(lblBookCode);
        lblBookCode.setBounds(10, 120, 85, 15);

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
        txtBookCode.setBounds(100, 120, 90, 19);

        lblBookName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBookName.setText("...");
        getContentPane().add(lblBookName);
        lblBookName.setBounds(200, 120, 210, 15);

        lblLegacyInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblLegacyInfo.setText("...");
        getContentPane().add(lblLegacyInfo);
        lblLegacyInfo.setBounds(10, 145, 390, 15);

        lblSelectType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSelectType.setText("Select Type :");
        getContentPane().add(lblSelectType);
        lblSelectType.setBounds(15, 100, 80, 15);

        getContentPane().add(cmbSelectType);
        cmbSelectType.setBounds(100, 95, 256, 21);

    }//GEN-END:initComponents

    private void txtBookCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookCodeFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBookCodeFocusGained
    
    private void txtBookCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookCodeFocusLost
        // TODO add your handling code here:
        
        String VoucherType = "";
        if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_RECEIPT) {
            VoucherType = Long.toString(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex()));
        } else if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_DEBIT_NOTE) {
            VoucherType = Long.toString(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex()));
        } else {
            VoucherType = "9,11,5";
        }
        if(!txtBookCode.getText().trim().equals("")) {
            lblBookName.setText(clsBook.getBookName(EITLERPGLOBAL.gCompanyID, txtBookCode.getText()));
            
            String strSQL = "SELECT MAX(CONVERT(LEGACY_NO,SIGNED)) AS NEXT_LEGACY_NO FROM D_FIN_VOUCHER_HEADER " +
            "WHERE VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' " +
            "AND BOOK_CODE="+txtBookCode.getText().trim()+" AND VOUCHER_TYPE IN ("+VoucherType + ") AND APPROVED=1 AND CANCELLED=0";
            
            String Info = "LAST LEGACY NO = " + data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL) + " FOR BOOK CODE " + txtBookCode.getText().trim();
            lblLegacyInfo.setText(Info);
        }
    }//GEN-LAST:event_txtBookCodeFocusLost
    
    private void txtBookCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBookCodeKeyPressed
        // TODO add your handling code here:
        try {
            int BookType = 0;
            String Condition="";
            if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_RECEIPT) {
                BookType=1;
                Condition=" BOOK_CODE NOT IN (30,70,26) ";
            } else if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_JOURNAL) {
                BookType=2;
                Condition=" BOOK_CODE IN (21,22,25,45) ";
            }
            if(BookType!=0) {
                if(evt.getKeyCode()==112) {
                    LOV aList=new LOV();
                    
                    aList.SQL="SELECT BOOK_CODE,BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_TYPE="+BookType+" AND "+Condition+" ORDER BY BOOK_CODE";
                    aList.ReturnCol=1;
                    aList.ShowReturnCol=true;
                    aList.DefaultSearchOn=2;
                    aList.UseSpecifiedConn=true;
                    aList.dbURL=FinanceGlobal.FinURL;
                    
                    if(aList.ShowLOV()) {
                        txtBookCode.setText(aList.ReturnVal);
                        lblBookName.setText(clsBook.getBookName(EITLERPGLOBAL.gCompanyID, txtBookCode.getText()));
                    }
                }
            }
        }
        catch(Exception e) {
        }
    }//GEN-LAST:event_txtBookCodeKeyPressed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdGenerateNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGenerateNoActionPerformed
        // TODO add your handling code here:
        GenerateNo();
    }//GEN-LAST:event_cmdGenerateNoActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkLockPeriod;
    private javax.swing.JComboBox cmbSelectType;
    private javax.swing.JButton cmdGenerateNo;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBookCode;
    private javax.swing.JLabel lblBookName;
    private javax.swing.JLabel lblLegacyInfo;
    private javax.swing.JLabel lblSelectType;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JTextField txtBookCode;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateNo() {
        String FromDate = "";
        String ToDate = "";
        ResultSet rsData = null, rsLock = null;
        Connection Conn = null;
        Statement stmt = null;
        String VoucherType = "";
        try {
            if(!Validate()) {
                return;
            }
            
            FromDate = EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()));
            ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
            if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_RECEIPT) {
                //VoucherType = Long.toString(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex()));
                VoucherType = "6,12";
                System.out.println(VoucherType);
            } else if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_DEBIT_NOTE) {
                VoucherType = Long.toString(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex()));
            } else {
                VoucherType = "9,11,3,5";
            }

            
            String strSQL = "SELECT MAX(CONVERT(LEGACY_NO,SIGNED)) AS NEXT_LEGACY_NO FROM D_FIN_VOUCHER_HEADER " +
            "WHERE VOUCHER_NO NOT LIKE 'M%' AND VOUCHER_DATE>='"+FromDate+"' AND VOUCHER_DATE<='"+ToDate+"' " +
            "AND BOOK_CODE='"+txtBookCode.getText().trim()+"' AND VOUCHER_TYPE IN ("+VoucherType+ ") AND APPROVED=1 AND CANCELLED=0";
            int LastLegacyNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
            
            strSQL = "SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO NOT LIKE 'M%' " +
            "AND VOUCHER_DATE>='"+FromDate+"' AND VOUCHER_DATE<='"+ToDate+"' AND BOOK_CODE='"+txtBookCode.getText().trim()+"' " +
            "AND VOUCHER_TYPE IN ("+VoucherType+ ") AND (LEGACY_NO='' OR LEGACY_NO IS NULL) " +
            "AND APPROVED=1 AND CANCELLED=0 ORDER BY VOUCHER_DATE,VOUCHER_NO";
            rsData = data.getResult(strSQL,FinanceGlobal.FinURL);
            rsData.first();
            
            if(rsData.getRow() > 0 ) {
                while(!rsData.isAfterLast()) {
                    LastLegacyNo++;
                    String VoucherNo = rsData.getString("VOUCHER_NO");
                    String VoucherDate = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                    strSQL = "UPDATE D_FIN_VOUCHER_HEADER SET LEGACY_NO='"+LastLegacyNo+"', LEGACY_DATE='"+VoucherDate+"', " +
                    "CHANGED=1, CHANGED_DATE=CURDATE() WHERE VOUCHER_NO='"+VoucherNo+"'";
                    data.Execute(strSQL,FinanceGlobal.FinURL);
                    rsData.next();
                }
            }
        } catch (Exception e) {
        }
    }
    
    private boolean Validate() {
        try {
            
            String EndDate = EITLERPGLOBAL.FinToDateDB;
            
            if(txtToDate.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,"Please specify period.");
                return false;
            }
            
            if(java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())).after(java.sql.Date.valueOf(EndDate))) {
                JOptionPane.showMessageDialog(this,"Wrong Date.");
                return false;
            }
            
            if(!EITLERPGLOBAL.isDate(txtToDate.getText().trim())) {
                JOptionPane.showMessageDialog(this,"Please specify date in DD/MM/YYYY format.");
                return false;
            }
            
            if(txtBookCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,"Please specify Book Code.");
                return false;
            }
            
            if(cmbSelectType.getSelectedIndex()==0) {
                JOptionPane.showMessageDialog(this,"Select Voucher Type.");
                return false;
            }
            
            int BookType = 0;
            String Condition="";
            if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_RECEIPT) {
                BookType=1;
                Condition=" BOOK_CODE NOT IN (30,70,26) ";
            } else if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_DEBIT_NOTE) {
                BookType=3;
                Condition=" BOOK_CODE IN (12,16,18,20) ";
            } else if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_JOURNAL) {
                BookType=2;
                Condition=" BOOK_CODE IN (21,22,25,45) ";
            }
            
            //String SQL = "SELECT * FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+txtBookCode.getText().trim()+"' AND BOOK_TYPE=1 AND BOOK_CODE NOT IN (30,70,26)";
            String SQL = "SELECT BOOK_CODE,BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_TYPE="+BookType+" AND "+Condition+" ORDER BY BOOK_CODE";
            if(!data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                JOptionPane.showMessageDialog(this,"Please specify proper Bank Book Code.");
                return false;
            }
            String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
            String FromDate = EITLERPGLOBAL.getFinYearStartDate(ToDate);
            //String MonthStartDate = ToDate.substring(0,8)+"01";
            String BookCode = txtBookCode.getText().trim();
            String VoucherType = "";
            if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_RECEIPT) {
                VoucherType = Long.toString(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex()));
            } else if(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex())==FinanceGlobal.TYPE_DEBIT_NOTE) {
                VoucherType = Long.toString(cmbSelectTypeModel.getCode(cmbSelectType.getSelectedIndex()));
            } else {
                VoucherType = "9,11,3,5";
            }
            
            String strSQL = "SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_DATE>='"+FromDate+"' AND VOUCHER_DATE<='"+ToDate+"' " +
            "AND APPROVED=0 AND CANCELLED=0 AND BOOK_CODE='"+BookCode+"' AND VOUCHER_TYPE IN ('"+VoucherType+"') ";
            boolean Found=false;
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                Found=true;
            }
            if(Found) {
                JOptionPane.showMessageDialog(this,"Unapproved Voucher exists between "+EITLERPGLOBAL.formatDate(FromDate)+ " " +
                "and "+EITLERPGLOBAL.formatDate(ToDate) +".\n Please clear those vouchers and re-run legacy numbering.");
                return false;
            }
        }catch(Exception e) {
            return false;
        }
        return true;
    }
    
    private void GenerateCombo() {
        cmbSelectTypeModel=new EITLComboModel();
        cmbSelectType.removeAllItems();
        cmbSelectType.setModel(cmbSelectTypeModel);
        
        ComboData aData=new ComboData();
        aData.Code=0;
        aData.Text="Select Voucher Type";
        cmbSelectTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=FinanceGlobal.TYPE_RECEIPT;
        aData.Text="Receipt Voucher";
        cmbSelectTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=FinanceGlobal.TYPE_JOURNAL;
        aData.Text="Journal Voucher";
        cmbSelectTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=FinanceGlobal.TYPE_DEBIT_NOTE;
        aData.Text="DebitNote Voucher";
        cmbSelectTypeModel.addElement(aData);
    }
}