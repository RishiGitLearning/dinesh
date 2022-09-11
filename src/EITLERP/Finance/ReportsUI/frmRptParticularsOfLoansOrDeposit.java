/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.Finance.ReportsUI;

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
import java.util.*;
import TReportWriter.*;

public class frmRptParticularsOfLoansOrDeposit extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        ProgressBar.setVisible(false);
        lblPartyCode.setVisible(false);
        
        
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
        txtFromDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdPreview = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtAnnexure = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMinimumBalance = new javax.swing.JTextField();
        ProgressBar = new javax.swing.JProgressBar();
        lblPartyCode = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel6.setText("PARTICULARS OF LOANS OR DEPOSITS TAKEN / ACCEPTED & PAYMENT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, -2, 380, 30);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("From Date :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 130, 120, 20);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(140, 130, 90, 19);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(230, 130, 60, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(290, 130, 90, 20);

        jLabel1.setText("Period");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(14, 46, 90, 15);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(260, 210, 130, 25);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(" Annexure :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(5, 70, 130, 15);

        getContentPane().add(txtAnnexure);
        txtAnnexure.setBounds(140, 70, 90, 19);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(" Minimum Balance :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(5, 100, 130, 15);

        getContentPane().add(txtMinimumBalance);
        txtMinimumBalance.setBounds(140, 100, 90, 19);

        getContentPane().add(ProgressBar);
        ProgressBar.setBounds(10, 180, 380, 20);

        lblPartyCode.setText("....");
        getContentPane().add(lblPartyCode);
        lblPartyCode.setBounds(15, 160, 180, 15);

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
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblPartyCode;
    private javax.swing.JTextField txtAnnexure;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtMinimumBalance;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        
        new Thread() {
            
            public void run() {
                
                
                try {
                    TReportWriter.SimpleDataProvider.TRow objRow;
                    TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
                    
                    objReportData.AddColumn("SR_NO");
                    objReportData.AddColumn("PARTY_CODE");
                    objReportData.AddColumn("PARTY_NAME");
                    objReportData.AddColumn("ADDRESS1");
                    objReportData.AddColumn("ADDRESS2");
                    objReportData.AddColumn("ADDRESS3");
                    objReportData.AddColumn("CITY");
                    objReportData.AddColumn("PINCODE");
                    objReportData.AddColumn("OPENING_BALANCE");
                    objReportData.AddColumn("ACCEPTED_BALANCE");
                    objReportData.AddColumn("PAID_BALANCE");
                    objReportData.AddColumn("MAXIMUM_BALANCE");
                    objReportData.AddColumn("ACCOUNT_SQUARED");
                    objReportData.AddColumn("ACCEPTED_CASH");
                    
                    
                    TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
                    objOpeningRow.setValue("SR_NO","");
                    objOpeningRow.setValue("PARTY_CODE","");
                    objOpeningRow.setValue("PARTY_NAME","");
                    objOpeningRow.setValue("ADDRESS1","");
                    objOpeningRow.setValue("ADDRESS2","");
                    objOpeningRow.setValue("ADDRESS3","");
                    objOpeningRow.setValue("CITY","");
                    objOpeningRow.setValue("PINCODE","");
                    objOpeningRow.setValue("OPENING_BALANCE","");
                    objOpeningRow.setValue("ACCEPTED_BALANCE","");
                    objOpeningRow.setValue("PAID_BALANCE","");
                    objOpeningRow.setValue("MAXIMUM_BALANCE","");
                    objOpeningRow.setValue("ACCOUNT_SQUARED","");
                    objOpeningRow.setValue("ACCEPTED_CASH","");
                    
                    
                    double MinBalance= Double.parseDouble(txtMinimumBalance.getText());
                    int SrNo=0,ProgressSrNo=0;
                    
                    ResultSet rsParty,rsPartyinfo;
                    String strSQL = "";
                    String FromDate = txtFromDate.getText();
                    String ToDate = txtToDate.getText();
                    
                    
                    String LastDate = txtFromDate.getText().substring(0,6) + String.valueOf(Integer.parseInt(txtFromDate.getText().substring(6)) - 7);
                    
                    double OpeningBalance=0.0,AcceptedAmount=0.0,PaidAmount=0.0,MaxBalance=0.0,RenewAmount=0.0,ClosingBalance=0.0;
                    String PartyCode="";
                    String Qry = "SELECT DISTINCT PARTY_CODE AS PARTY_CODE FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" AND RECEIPT_DATE >= '"+ EITLERPGLOBAL.formatDateDB(LastDate) +"' ORDER BY PARTY_CODE";
                    rsParty = data.getResult(Qry,EITLERP.Finance.FinanceGlobal.FinURL);
                    
                    int Max=data.getIntValueFromDB("SELECT COUNT(DISTINCT PARTY_CODE) AS PARTY_CODE FROM D_FD_DEPOSIT_MASTER WHERE COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" AND RECEIPT_DATE >= '"+ EITLERPGLOBAL.formatDateDB(LastDate) +"'  ORDER BY PARTY_CODE",EITLERP.Finance.FinanceGlobal.FinURL);
                    ProgressBar.setMaximum(Max);
                    ProgressBar.setMinimum(0);
                    ProgressBar.setStringPainted(true);
                    ProgressBar.setVisible(true);
                    lblPartyCode.setVisible(true);
                    
                    if(rsParty.getRow()>0) {
                        while(!rsParty.isAfterLast()) {
                            
                            PartyCode = rsParty.getString("PARTY_CODE");
                            
                            OpeningBalance = clsAccount.getOpeningBalanceOfDepositParty("('115012','115153','115029','115036','115177','115218','115225')",PartyCode,EITLERPGLOBAL.formatDateDB(FromDate));
                            
                            Qry = "SELECT SUM(AMOUNT) AS AMOUNT FROM D_FD_DEPOSIT_MASTER WHERE PARTY_CODE = '"+PartyCode+"' AND RECEIPT_DATE >='" + EITLERPGLOBAL.formatDateDB(FromDate) + "' AND RECEIPT_DATE <='" + EITLERPGLOBAL.formatDateDB(ToDate) + "'  " +
                            "AND APPROVED = 1 AND CANCELLED = 0 AND COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" ";
                            AcceptedAmount = data.getDoubleValueFromDB(Qry,FinanceGlobal.FinURL);
                            
                            Qry = "SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) AS AMOUNT "+
                            "FROM  "+
                            "( "+
                            "SELECT AMOUNT "+
                            "FROM D_FD_DEPOSIT_REFUND  "+
                            "WHERE SUB_ACCOUNT_CODE='"+PartyCode+"'  "+
                            "AND REFUND_DATE >='" + EITLERPGLOBAL.formatDateDB(FromDate) + "' AND REFUND_DATE <='" + EITLERPGLOBAL.formatDateDB(ToDate) + "'  AND APPROVED =1 AND CANCELLED =0 "+
                            "UNION ALL  "+
                            "SELECT AMOUNT "+
                            "FROM D_FD_DEPOSIT_MASTER  "+
                            "WHERE PM_DATE<>'0000-00-00' AND PM_DATE >='" + EITLERPGLOBAL.formatDateDB(FromDate) + "' AND PM_DATE <='" + EITLERPGLOBAL.formatDateDB(ToDate) + "' AND APPROVED =1 AND CANCELLED =0 "+
                            "AND PARTY_CODE = '"+PartyCode+"' "+
                            ") A ";
                            
                            PaidAmount = data.getDoubleValueFromDB(Qry,FinanceGlobal.FinURL);
                            
                            Qry = "SELECT SUM(T.AMOUNT) AS AMOUNT "+
                            "FROM  "+
                            "(SELECT SUM(DMST.AMOUNT) AS AMOUNT "+
                            "FROM D_FD_DEPOSIT_MASTER DMST, D_FD_SCHEME_MASTER SMST  "+
                            "WHERE DMST.COMPANY_ID ='" + EITLERPGLOBAL.gCompanyID + "' AND DMST.APPROVED= 1  AND DMST.DEPOSIT_ENTRY_TYPE = '2'  "+
                            "AND DMST.CANCELLED=0  AND DMST.COMPANY_ID = SMST.COMPANY_ID AND DMST.SCHEME_ID = SMST.SCHEME_ID  "+
                            "AND DMST.PARTY_CODE = '"+PartyCode+"' "+
                            "AND DMST.RECEIPT_DATE >= '" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "' AND DMST.RECEIPT_DATE <= '" + EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) + "'  "+
                            "ORDER BY DMST.RECEIPT_DATE) AS T ";
                            
                            RenewAmount = data.getDoubleValueFromDB(Qry,FinanceGlobal.FinURL);
                            PaidAmount += RenewAmount;
                            
                            ProgressSrNo++;
                            ProgressBar.setValue(ProgressSrNo);
                            if(OpeningBalance>=MinBalance || AcceptedAmount >=MinBalance || PaidAmount>=MinBalance) {
                                //System.out.println("Party Code = " + PartyCode + "  OB=" + OpeningBalance + " Accepted Amount=" + AcceptedAmount + "  Paid Amount =" + PaidAmount);
                                
                                
                                objRow=objReportData.newRow();
                                
                                lblPartyCode.setText("Party Code=" + PartyCode);
                                
                                SrNo++;
                                
                                //Integer.toString()
                                objRow.setValue("SR_NO",Integer.toString(SrNo));
                                objRow.setValue("PARTY_CODE",PartyCode);
                                
                                Qry = "SELECT APPLICANT_NAME,ADDRESS1,ADDRESS2,ADDRESS3,CITY,PINCODE FROM D_FD_DEPOSIT_MASTER WHERE PARTY_CODE = '"+PartyCode+"' "+
                                "AND COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" "+
                                "ORDER BY RECEIPT_DATE DESC LIMIT 1 ";
                                rsPartyinfo = data.getResult(Qry,FinanceGlobal.FinURL);
                                
                                
                                
                                objRow.setValue("PARTY_NAME",rsPartyinfo.getString("APPLICANT_NAME"));
                                objRow.setValue("ADDRESS1",rsPartyinfo.getString("ADDRESS1"));
                                objRow.setValue("ADDRESS2",rsPartyinfo.getString("ADDRESS2"));
                                objRow.setValue("ADDRESS3",rsPartyinfo.getString("ADDRESS3"));
                                objRow.setValue("CITY",rsPartyinfo.getString("CITY"));
                                objRow.setValue("PINCODE",rsPartyinfo.getString("PINCODE"));
                                objRow.setValue("OPENING_BALANCE",Double.toString(OpeningBalance));
                                objRow.setValue("ACCEPTED_BALANCE",Double.toString(AcceptedAmount));
                                objRow.setValue("PAID_BALANCE",Double.toString(PaidAmount));
                                
                                MaxBalance=0;
                                ClosingBalance=0;
                                String MonthLastDate = data.getStringValueFromDB("SELECT LAST_DAY('" + EITLERPGLOBAL.formatDateDB(FromDate) + "') AS LAST_DATE FROM DUAL");
                                while(EITLERPGLOBAL.compareDate(EITLERPGLOBAL.formatDate(MonthLastDate),ToDate)== -1 || EITLERPGLOBAL.compareDate(EITLERPGLOBAL.formatDate(MonthLastDate),ToDate)== 0) {
                                    int i = EITLERPGLOBAL.compareDate(EITLERPGLOBAL.formatDate(MonthLastDate),ToDate);
                                    
                                    //java.sql.Date dt = "";
                                    ClosingBalance = clsAccount.getOpeningBalanceOfDepositParty("('115012','115153','115029','115036','115177','115218','115225')",PartyCode,data.getStringValueFromDB("SELECT DATE_ADD('" + MonthLastDate + "',INTERVAL 1 DAY ) FROM DUAL"));
                                    
                                    if(ClosingBalance >= MaxBalance) {
                                        MaxBalance = ClosingBalance;
                                    }
                                    //System.out.println("i=" + i +" " + EITLERPGLOBAL.formatDate(MonthLastDate)  + "   " + ToDate + "  " + ClosingBalance);
                                    MonthLastDate = data.getStringValueFromDB("SELECT LAST_DAY(DATE_ADD('" + MonthLastDate + "', INTERVAL 1 MONTH)) FROM DUAL");
                                }
                                /*MaxBalance = OpeningBalance;
                                if(MaxBalance < AcceptedAmount)
                                    MaxBalance = AcceptedAmount;
                                if(MaxBalance < PaidAmount)
                                    MaxBalance = PaidAmount;
                                 */
                                
                                objRow.setValue("MAXIMUM_BALANCE",Double.toString(MaxBalance));
                                
                                if((OpeningBalance + AcceptedAmount - PaidAmount) == 0)
                                    objRow.setValue("ACCOUNT_SQUARED","YES");
                                else
                                    objRow.setValue("ACCOUNT_SQUARED","NO");
                                
                                objRow.setValue("ACCEPTED_CASH","NO");
                                objReportData.AddRow(objRow);
                            }
                            rsParty.next();
                        }
                        
                        
                        HashMap Parameters=new HashMap();
                        Parameters.put("FROM_DATE",txtFromDate.getText());
                        Parameters.put("TO_DATE",txtToDate.getText());
                        Parameters.put("ANNEXURE",txtAnnexure.getText());
                        Parameters.put("MINIMUM_BALANCE", txtMinimumBalance.getText().trim());
                        Parameters.put("ACCOUNTING_YEAR",txtFromDate.getText().substring(6) + "-" + txtToDate.getText().substring(6));
                        Parameters.put("ASSESMENT_YEAR",txtToDate.getText().substring(6)+"-" + String.valueOf((Integer.parseInt(txtToDate.getText().substring(6))) + 1));
                        Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
                        
                        lblPartyCode.setText("Done.........");
                        objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptParticularsOfLoansOrDeposit.rpt",Parameters,objReportData);
                        ProgressBar.setVisible(false);
                        
                    }
                    
                    
                }
                catch(Exception e) {
                    e.printStackTrace();
                    ProgressBar.setVisible(false);
                }
                
            };
        }.start();
    }
    
    private boolean Validate() {
        //Form level validations
        
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter from Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter To Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtMinimumBalance.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter the Minimum Balance");
            return false;
        }
        
        return true;
    }
    
}