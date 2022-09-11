/*
 * frmRptStockStatement.java
 *
 * Created on August 13, 2005, 3:21 PM
 */

package EITLERP.Stores;

/**
 *
 * @author  root
 */
import EITLERP.*;
import java.sql.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Utils.*;
import java.text.*;
import java.net.*;

public class frmRptNonMoving extends javax.swing.JApplet {
    
    private EITLComboModel cmbBaseModel=new EITLComboModel();
    
    private int lineCounter=0;
    private int pageCounter=1;
    
    private String LeftMargin=EITLERPGLOBAL.Replicate(" ", 5);
    
    String strFromDate=EITLERPGLOBAL.FinFromDate;
    //String strFromDate="01/04/2005";
    
    private double pageTotal=0;
    private double cumTotal=0;
    private double finalTotal=0;
    
    
    /** Initializes the applet frmRptStockStatement */
    public void init() {
        setSize(573, 320);
        initComponents();
        Bar.setVisible(false);
        chkReprocess.setVisible(false);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromItemID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtToItemID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        Bar = new javax.swing.JProgressBar();
        lblStatus = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        chkReprocess = new javax.swing.JCheckBox();
        chkValue = new javax.swing.JCheckBox();
        txtValueFrom = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtValueTo = new javax.swing.JTextField();
        txtQtyTo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtQtyFrom = new javax.swing.JTextField();
        chkQty = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtUptoDate = new javax.swing.JTextField();
        chkShowWriteOff = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NON MOVING ITEM LIST");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(8, 8, 187, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(1, 0, 581, 30);

        jLabel2.setText("From Item Code");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(12, 48, 112, 15);

        getContentPane().add(txtFromItemID);
        txtFromItemID.setBounds(122, 46, 112, 20);

        jLabel3.setText("To Item Code");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(12, 78, 112, 15);

        getContentPane().add(txtToItemID);
        txtToItemID.setBounds(122, 76, 112, 20);

        jLabel4.setText("Not moved after ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(12, 134, 105, 15);

        getContentPane().add(txtToDate);
        txtToDate.setBounds(121, 133, 112, 20);

        cmdPrint.setText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPrint);
        cmdPrint.setBounds(429, 51, 119, 29);

        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(430, 89, 116, 25);

        getContentPane().add(Bar);
        Bar.setBounds(8, 289, 200, 18);

        lblStatus.setText(".");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(8, 271, 383, 15);

        jLabel6.setText("(Keep both item code blank for all items)");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(15, 105, 260, 15);

        chkReprocess.setText("Reprocess Stock");
        getContentPane().add(chkReprocess);
        chkReprocess.setBounds(7, 220, 160, 20);

        chkValue.setText("Value From");
        chkValue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkValueStateChanged(evt);
            }
        });

        getContentPane().add(chkValue);
        chkValue.setBounds(11, 163, 100, 23);

        txtValueFrom.setEnabled(false);
        getContentPane().add(txtValueFrom);
        txtValueFrom.setBounds(122, 164, 112, 20);

        jLabel8.setText("To");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(243, 167, 20, 15);

        txtValueTo.setEnabled(false);
        getContentPane().add(txtValueTo);
        txtValueTo.setBounds(263, 164, 112, 20);

        txtQtyTo.setEnabled(false);
        getContentPane().add(txtQtyTo);
        txtQtyTo.setBounds(265, 192, 112, 20);

        jLabel9.setText("To");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(242, 195, 20, 15);

        txtQtyFrom.setEnabled(false);
        getContentPane().add(txtQtyFrom);
        txtQtyFrom.setBounds(123, 192, 112, 20);

        chkQty.setText("Qty");
        chkQty.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkQtyStateChanged(evt);
            }
        });

        getContentPane().add(chkQty);
        chkQty.setBounds(11, 191, 100, 23);

        jLabel5.setText("To");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(241, 136, 20, 15);

        getContentPane().add(txtUptoDate);
        txtUptoDate.setBounds(262, 134, 110, 19);

        chkShowWriteOff.setText("Show Write Off Report");
        getContentPane().add(chkShowWriteOff);
        chkShowWriteOff.setBounds(192, 220, 180, 20);

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setText("<html> <b> <font color='RED'> NOTE :  Please reprocess stock ledger before  run this report. </font></b></html>");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(10, 245, 540, 20);

    }//GEN-END:initComponents
    
    private void chkQtyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkQtyStateChanged
        // TODO add your handling code here:
        try {
            
            txtQtyFrom.setEnabled(chkQty.isSelected());
            txtQtyTo.setEnabled(chkQty.isSelected());
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_chkQtyStateChanged
    
    private void chkValueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkValueStateChanged
        // TODO add your handling code here:
        try {
            
            txtValueFrom.setEnabled(chkValue.isSelected());
            txtValueTo.setEnabled(chkValue.isSelected());
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_chkValueStateChanged
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(null," W "+getWidth()+" H "+getHeight());
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
        processReport();
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar Bar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkQty;
    private javax.swing.JCheckBox chkReprocess;
    private javax.swing.JCheckBox chkShowWriteOff;
    private javax.swing.JCheckBox chkValue;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtFromItemID;
    private javax.swing.JTextField txtQtyFrom;
    private javax.swing.JTextField txtQtyTo;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtToItemID;
    private javax.swing.JTextField txtUptoDate;
    private javax.swing.JTextField txtValueFrom;
    private javax.swing.JTextField txtValueTo;
    // End of variables declaration//GEN-END:variables
    private void processReport() {
        
        new Thread() {
            public void run() {
                
                try {
                    ResultSet rsItem,rsIssue,rsTmp;
                    
                    boolean Continue=false;
                    double stockQty=0;
                    double stockValue=0;
                    String AfterDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
                    String uptoDate=EITLERPGLOBAL.formatDateDB(txtUptoDate.getText());
                    String UnitName="";
                    String ItemName="";
                    int ItemCount=0;
                    int Counter=0;
                    
                    double ValueFrom = EITLERPGLOBAL.ConvertToDouble(txtValueFrom.getText());
                    double ValueTo = EITLERPGLOBAL.ConvertToDouble(txtValueTo.getText());
                    double QtyFrom = EITLERPGLOBAL.ConvertToDouble(txtQtyFrom.getText());
                    double QtyTo = EITLERPGLOBAL.ConvertToDouble(txtQtyTo.getText());
                    
                    
                    Statement stReport=EITLERPGLOBAL.gConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rsReport=stReport.executeQuery("SELECT * FROM TMP_NON_MOVING");
                    
                    if(txtToDate.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(null,"Please specify the date");
                        return;
                    }
                    
                    String strSQL="SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND ONETIME=0 ";
                    String strCount="SELECT COUNT(ITEM_ID) AS THECOUNT FROM D_INV_ITEM_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND ONETIME=0 ";
                    
                    if(!txtFromItemID.getText().trim().equals("")) {
                        strSQL+=" AND ITEM_ID>='"+txtFromItemID.getText().trim()+"' ";
                        strCount+=" AND ITEM_ID>='"+txtFromItemID.getText().trim()+"' ";
                    }
                    
                    if(!txtToItemID.getText().trim().equals("")) {
                        strSQL+=" AND ITEM_ID<='"+txtToItemID.getText().trim()+"' ";
                        strCount+=" AND ITEM_ID<='"+txtFromItemID.getText().trim()+"' ";
                    }
                    
                    
                    lblStatus.setText("Processing...");
                    final clsItemStock objItemStock=new clsItemStock();
                    
//                    if(chkReprocess.isSelected()) {
//                        objItemStock.ProcessDone=false;
//                        
//                        objItemStock.ProcessLedger(EITLERPGLOBAL.FinFromDateDB, EITLERPGLOBAL.getCurrentDateDB());
//                        
//                        while(!objItemStock.ProcessDone) {
//                            lblStatus.setText("Processing ... ");
//                        }
//                    }
                    
                    lblStatus.setText("");
                    
                    
                    rsItem=data.getResult(strCount);
                    rsItem.first();
                    if(rsItem.getRow()>0) {
                        ItemCount=rsItem.getInt("THECOUNT");
                    }
                    
                    Bar.setVisible(true);
                    Bar.setMaximum(ItemCount);
                    Bar.setMinimum(0);
                    
                    Counter=0;
                    
                    rsItem=data.getResult(strSQL);
                    rsItem.first();
                    
                    if(rsItem.getRow()>0) {
                        data.Execute("DELETE FROM TMP_NON_MOVING");
                        
                        while(!rsItem.isAfterLast()) {
                            Counter++;
                            Bar.setValue(Counter);
                            
                            String ItemID=rsItem.getString("ITEM_ID");
                            lblStatus.setText("Processing : "+ItemID);
                            
                            clsStockInfo objStock=objItemStock.getOnHandQtyOn(EITLERPGLOBAL.gCompanyID,ItemID,EITLERPGLOBAL.getCurrentDateDB());
                            stockQty=objStock.StockQty;
                            
                            if(stockQty>0) {
                                stockValue=objItemStock.getOnHandValueOn(EITLERPGLOBAL.gCompanyID,ItemID,EITLERPGLOBAL.getCurrentDateDB());
                                UnitName=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT",clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID,ItemID));
                                ItemName=clsItem.getItemName(EITLERPGLOBAL.gCompanyID,ItemID);
                                
                                rsIssue=data.getResult("SELECT A.ISSUE_NO FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+AfterDate+"' AND A.APPROVED=1 ");
                                rsIssue.first();
                                if(rsIssue.getRow()<=0) {
                                    
                                    String LastIssueNo="";
                                    //String LastIssueDate="";
                                    String LastIssueDate=null;
                                    String LastGRNNo="";
                                    //String LastGRNDate="";
                                    String LastGRNDate=null;
                                    
                                    rsTmp=data.getResult("SELECT A.ISSUE_NO,A.ISSUE_DATE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND B.ITEM_CODE='"+ItemID+"' AND A.APPROVED=1 ORDER BY ISSUE_DATE DESC LIMIT 1");
                                    rsTmp.first();
                                    if(rsTmp.getRow()>0) {
                                        LastIssueNo=rsTmp.getString("ISSUE_NO");
                                        LastIssueDate=rsTmp.getString("ISSUE_DATE");
                                    }
                                    
                                    rsTmp=data.getResult("SELECT A.GRN_NO,A.GRN_DATE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND B.ITEM_ID='"+ItemID+"' AND A.APPROVED=1 ORDER BY GRN_DATE DESC LIMIT 1");
                                    rsTmp.first();
                                    if(rsTmp.getRow()>0) {
                                        LastGRNNo=rsTmp.getString("GRN_NO");
                                        LastGRNDate=rsTmp.getString("GRN_DATE");
                                    }
                                    
                                    Continue=true;
                                                                        
                                    rsTmp=data.getResult("SELECT A.GRN_NO FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND B.ITEM_ID='"+ItemID+"' AND A.APPROVED=1 AND A.GRN_DATE>='"+uptoDate+"' LIMIT 1");
                                    rsTmp.first();
                                    
                                    if(rsTmp.getRow()>0)
                                    {
                                      Continue=false;  
                                    }
                                    
                                    
                                    if(chkQty.isSelected()) {
                                        if(stockQty>=QtyFrom&&stockQty<=QtyTo) {
                                        }
                                        else {
                                            Continue=false;
                                        }
                                    }
                                    
                                    if(chkValue.isSelected()) {
                                        if(stockValue>=ValueFrom&&stockValue<=ValueTo) {
                                        }
                                        else {
                                            Continue=false;
                                        }
                                    }
                                    
                                    if(Continue) {
                                        //Include this record
                                        rsReport.moveToInsertRow();
                                        rsReport.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                        rsReport.updateString("ITEM_ID",ItemID);
                                        rsReport.updateString("ITEM_NAME",ItemName);
                                        rsReport.updateDouble("QTY",stockQty);
                                        rsReport.updateString("UNIT_NAME",UnitName);
                                        rsReport.updateDouble("STOCK_VALUE",stockValue);
                                        rsReport.updateString("LAST_ISSUE_NO",LastIssueNo);
                                        rsReport.updateString("LAST_ISSUE_DATE",LastIssueDate);
                                        rsReport.updateString("LAST_GRN_NO",LastGRNNo);
                                        rsReport.updateString("LAST_GRN_DATE",LastGRNDate);
                                        rsReport.updateInt("CHANGED",1);
                                        rsReport.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                        rsReport.insertRow();
                                    }
                                }
                            }
                            
                            rsItem.next();
                        }
                        
                    }
                    
                    Bar.setVisible(false);
                    lblStatus.setText("Done ...");
                    
                    try {
                        if(chkShowWriteOff.isSelected()) {
                            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptNonMoving2.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&AfterDate="+EITLERPGLOBAL.formatDate(AfterDate));
                            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                        }
                        else {
                            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptNonMoving.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&AfterDate="+EITLERPGLOBAL.formatDate(AfterDate));
                            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                        }
                        
                    }
                    catch(Exception e) {
                        JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
                    }
                    
                }
                catch(Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,e.getMessage());
                }
            }
        }.start();
        
    }
    
    
    
    
    
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    
    
    
/*SELECT A.MIR_NO,DATE_FORMAT(A.MIR_DATE,'%d/%m/%Y') MIR_DATE,B.APPROVER_REMARKS,B.REVISION_NO,IF(A.SUPP_ID='000000',A.PARTY_NAME,SUPP.SUPP_NAME) SUPP_NAME,
C.ITEM_ID,I.ITEM_DESCRIPTION,C.ITEM_EXTRA_DESC
FROM
D_INV_MIR_HEADER A
LEFT JOIN D_INV_MIR_HEADER_H B ON (A.MIR_NO=B.MIR_NO AND A.MIR_TYPE=B.MIR_TYPE)
LEFT JOIN D_COM_DOC_DATA DOC ON (DOC.DOC_NO=A.MIR_NO)
LEFT JOIN D_COM_SUPP_MASTER SUPP ON (A.SUPP_ID=SUPP.SUPPLIER_CODE),
D_INV_MIR_DETAIL C
LEFT JOIN D_INV_ITEM_MASTER I ON (I.ITEM_ID=C.ITEM_ID)
WHERE A.MIR_TYPE=1 AND A.APPROVED=0
AND A.MIR_NO=C.MIR_NO AND A.MIR_TYPE=C.MIR_TYPE AND A.MIR_TYPE=1
AND DOC.STATUS='W'
AND DOC.USER_ID=15
AND DOC.MODULE_ID=5
AND A.MIR_DATE>='2005-04-01'
ORDER BY A.MIR_NO,B.REVISION_NO DESC*/
    
}
