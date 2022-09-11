/*
 * frmSelectionPaymentVoucher.java
 *
 * Created on September 28, 2007, 12:01 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import EITLERP.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import java.awt.Frame;
import java.awt.*;
import EITLERP.Sales.*;
import java.sql.*;


public class frmSelectionExportSJVoucher extends javax.swing.JApplet {
    
    private EITLTableModel DataModel=new EITLTableModel();
    private EITLTableCellRenderer Render=new EITLTableCellRenderer();
    private JDialog aDialog;
    public boolean Cancelled=false;
    public clsVoucher objSelectedItem=new clsVoucher();
    public HashMap SelectedItems=new HashMap();
    public String PartyCode="";
    public String MainCode="";
    
    public frmSelectionExportSJVoucher() {
        initComponents();
        
    }
    
    /** Initializes the applet frmSelectionPaymentVoucher */
    public void init() {
        setSize(682,352);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();
        cmdNext = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdSelect = new javax.swing.JButton();
        cmdClose = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lblTitle.setBackground(new java.awt.Color(0, 153, 204));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" SELECT SJ FOR RECEIPT VOUCHER");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 1, 666, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setText("Party Code :");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(16, 15, 80, 15);

        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(95, 14, 130, 19);

        txtPartyName.setEnabled(false);
        jPanel1.add(txtPartyName);
        txtPartyName.setBounds(96, 43, 500, 19);

        cmdNext.setText("Next >>");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });

        jPanel1.add(cmdNext);
        cmdNext.setBounds(546, 231, 88, 25);

        jLabel2.setText("Invoice No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(12, 72, 80, 15);

        jPanel1.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(97, 70, 130, 19);

        Tab.addTab("Select Party", jPanel1);

        jPanel2.setLayout(null);

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(Table);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(9, 37, 620, 180);

        cmdSelect.setText("Select");
        cmdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectActionPerformed(evt);
            }
        });

        jPanel2.add(cmdSelect);
        cmdSelect.setBounds(424, 232, 100, 25);

        cmdClose.setText("Close");
        cmdClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCloseActionPerformed(evt);
            }
        });

        jPanel2.add(cmdClose);
        cmdClose.setBounds(533, 232, 100, 25);

        Tab.addTab("Select SJ", jPanel2);

        getContentPane().add(Tab);
        Tab.setBounds(3, 32, 650, 290);

    }//GEN-END:initComponents
    
    private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseActionPerformed
        // TODO add your handling code here:
        Cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCloseActionPerformed
    
    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed
        // TODO add your handling code here:
        try {
            
            SelectedItems.clear();
            
            for(int r=0;r<Table.getRowCount();r++) {
                if(Table.getValueAt(r,0).equals(new Boolean(true))) {
                    clsVoucher objItem=new clsVoucher();
                    
                    
                    objItem.setAttribute("VOUCHER_NO",DataModel.getValueByVariable("VOUCHER_NO",r));
                    objItem.setAttribute("VOUCHER_DATE",DataModel.getValueByVariable("VOUCHER_DATE",r));
                    objItem.setAttribute("AMOUNT",UtilFunctions.CDbl(DataModel.getValueByVariable("AMOUNT",r)));
                    objItem.setAttribute("AMOUNT_PAID",UtilFunctions.CDbl(DataModel.getValueByVariable("AMOUNT_PAID",r)));
                    objItem.setAttribute("GRN_NO",DataModel.getValueByVariable("GRN_NO",r));
                    objItem.setAttribute("GRN_DATE",DataModel.getValueByVariable("GRN_DATE",r));
                    objItem.setAttribute("PO_NO",DataModel.getValueByVariable("PO_NO",r));
                    objItem.setAttribute("PO_DATE",DataModel.getValueByVariable("PO_DATE",r));
                    objItem.setAttribute("REMARKS",DataModel.getValueByVariable("REMARKS",r));
                    objItem.setAttribute("DUE_DATE",DataModel.getValueByVariable("DUE_DATE",r));
                    objItem.setAttribute("INVOICE_NO",DataModel.getValueByVariable("INVOICE_NO",r));
                    objItem.setAttribute("INVOICE_DATE",DataModel.getValueByVariable("INVOICE_DATE",r));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.CInt(DataModel.getValueByVariable("MODULE_ID",r)));
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.CInt(DataModel.getValueByVariable("COMPANY_ID",r)));
                    
                    SelectedItems.put(Integer.toString(SelectedItems.size()+1),objItem);
                }
            }
            
            
            PartyCode=txtPartyCode.getText();
            
            
            Cancelled=false;
            aDialog.dispose();
            
        }
        catch(Exception e) {
            e.printStackTrace();
            Cancelled=true;
            aDialog.dispose();
        }
    }//GEN-LAST:event_cmdSelectActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        GenerateGridSJ();
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNextActionPerformed
    
    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
        txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));
    }//GEN-LAST:event_txtPartyCodeFocusLost
    
    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 ORDER BY PARTY_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));
            }
            
        }
        
    }//GEN-LAST:event_txtPartyCodeKeyPressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdClose;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyName;
    // End of variables declaration//GEN-END:variables
    
    private void FormatGrid() {
        try {
            
            DataModel=new EITLTableModel();
            Table.removeAll();
            
            Table.setModel(DataModel);
            TableColumnModel ColModel=Table.getColumnModel();
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
           /* DataModel.addColumn("*");
            DataModel.addColumn("Sr."); //0 - Read Only
            DataModel.addColumn("Voucher No."); //1
            DataModel.addColumn("Voucher Date"); //2 //Read Only
            DataModel.addColumn("Amount"); //3
            DataModel.addColumn("Amount Paid"); //3
            DataModel.addColumn("GRN No."); //4
            DataModel.addColumn("GRN Date"); //5
            DataModel.addColumn("PO No."); //6
            DataModel.addColumn("PO Date"); //7
            DataModel.addColumn("Remarks"); //8
            DataModel.addColumn("Due Date"); //8
            DataModel.addColumn("Invoice No.");
            DataModel.addColumn("Invoice Date");
            DataModel.addColumn("Module Id");
            DataModel.addColumn("Module Name");
            DataModel.addColumn("Company ID");
            DataModel.addColumn("Company Name");
            */
            
            DataModel.addColumn("*");
            DataModel.addColumn("Sr."); //0 - Read Only
            DataModel.addColumn("Voucher No."); //1
            DataModel.addColumn("Voucher Date"); //2 //Read Only
            DataModel.addColumn("Invoice Amount"); //3
            DataModel.addColumn("Amount Paid"); //3
            DataModel.addColumn("Agent Alpha"); //3
            DataModel.addColumn("Invoice No."); //4
            DataModel.addColumn("Invoice Date"); //5
            DataModel.addColumn("PO No."); //6
            DataModel.addColumn("PO Date"); //7
            DataModel.addColumn("Remarks"); //8
            DataModel.addColumn("Due Date"); //8
            DataModel.addColumn("GRN No.");
            DataModel.addColumn("GRN Date");
            DataModel.addColumn("Module Id");
            DataModel.addColumn("Module Name");
            DataModel.addColumn("Company ID");
            DataModel.addColumn("Company Name");
            DataModel.addColumn("Link No");
            
            DataModel.SetVariable(0,"SELECT"); //0 - Read Only
            DataModel.SetVariable(1,"SR_NO"); //0 - Read Only
            DataModel.SetVariable(2,"VOUCHER_NO"); //1
            DataModel.SetVariable(3,"VOUCHER_DATE"); //2 //Read Only
            DataModel.SetVariable(4,"AMOUNT"); //2 //Read Only
            DataModel.SetVariable(5,"AMOUNT_PAID"); //2 //Read Only
            DataModel.SetVariable(6,"AGENT_SR");
            DataModel.SetVariable(7,"INVOICE_NO"); //2 //Read Only
            DataModel.SetVariable(8,"INVOICE_DATE"); //2 //Read Only
            DataModel.SetVariable(9,"PO_NO"); //2 //Read Only
            DataModel.SetVariable(10,"PO_DATE"); //2 //Read Only
            DataModel.SetVariable(11,"REMARKS"); //2 //Read Only
            DataModel.SetVariable(12,"DUE_DATE"); //2 //Read Only
            DataModel.SetVariable(13,"GRN_NO"); //2 //Read Only
            DataModel.SetVariable(14,"GRN_DATE"); //2 //Read Only
            DataModel.SetVariable(15,"MODULE_ID"); //2 //Read Only
            DataModel.SetVariable(16,"MODULE_NAME"); //2 //Read Only
            DataModel.SetVariable(17,"COMPANY_ID"); //2 //Read Only
            DataModel.SetVariable(18,"COMPANY_NAME"); //2 //Read Only
            DataModel.SetVariable(19,"LINK_NO"); //2 //Read Only
            
            DataModel.TableReadOnly(false);
            
            DataModel.SetReadOnly(1);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);
            DataModel.SetReadOnly(5);
            DataModel.SetReadOnly(6);
            DataModel.SetReadOnly(7);
            DataModel.SetReadOnly(8);
            DataModel.SetReadOnly(9);
            DataModel.SetReadOnly(10);
            DataModel.SetReadOnly(11);
            DataModel.SetReadOnly(12);
            DataModel.SetReadOnly(13);
            DataModel.SetReadOnly(14);
            DataModel.SetReadOnly(15);
            DataModel.SetReadOnly(16);
            DataModel.SetReadOnly(17);
            DataModel.SetReadOnly(18);
            DataModel.SetReadOnly(19);
            
            Render.setCustomComponent(0,"CheckBox");
            Table.getColumnModel().getColumn(0).setCellRenderer(Render);
            Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void GenerateGridSJ() {
        try {
            
            String AgentAlpha=clsSalesParty.getAgentAlpha(txtPartyCode.getText());
            
            HashMap List=new HashMap();
            
            FormatGrid();
            String SQL = "SELECT A.COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" AND A.CANCELLED=0 AND A.BOOK_CODE=10 " +
            "AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
            "AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 ";//
            ResultSet rsData = data.getResult(SQL,FinanceGlobal.FinURL);
            rsData.first();
            int i=0;
            if(rsData.getRow()>0) {
                while(!rsData.isAfterLast()) {
                    i++;
                    String VoucherNo = rsData.getString("VOUCHER_NO");
                    String VoucherDate = rsData.getString("VOUCHER_DATE");
                    SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE GRN_NO='"+VoucherNo+"' AND GRN_DATE='"+VoucherDate+"' AND EFFECT='C' AND CANCELLED=0 ";
                    double ReceiptAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);
                    double SJAmount = rsData.getDouble("AMOUNT");
                    if(SJAmount==ReceiptAmount) {
                        i--;
                        rsData.next();
                        continue;
                    }
                    if(ReceiptAmount < SJAmount) {
                        Object[] rowData=new Object[20];
                        rowData[0]=new Boolean(false);
                        rowData[1]=Integer.toString(i);
                        rowData[2]=rsData.getString("VOUCHER_NO");
                        rowData[3]=EITLERPGLOBAL.formatDate(rsData.getString("VOUCHER_DATE"));
                        rowData[4]=Double.toString(rsData.getDouble("AMOUNT"));
                        rowData[5]=Double.toString(ReceiptAmount);
                        rowData[6]=AgentAlpha+"/"+data.getStringValueFromDB("SELECT AGENT_SR_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INVOICE_NO ='"+rsData.getString("INVOICE_NO")+"' AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(rsData.getString("INVOICE_DATE"))+"'");
                        rowData[7]=rsData.getString("INVOICE_NO");
                        rowData[8]=EITLERPGLOBAL.formatDate(rsData.getString("INVOICE_DATE"));
                        rowData[9]=rsData.getString("PO_NO");
                        rowData[10]=EITLERPGLOBAL.formatDate(rsData.getString("PO_DATE"));
                        rowData[11]=rsData.getString("REMARKS");
                        rowData[12]="";
                        rowData[13]=rsData.getString("GRN_NO");
                        rowData[14]=EITLERPGLOBAL.formatDate(rsData.getString("GRN_DATE"));
                        rowData[15]=Integer.toString(clsVoucher.SalesJournalVoucherModuleID);
                        rowData[16]=clsModules.getModuleName(EITLERPGLOBAL.gCompanyID,rsData.getInt("MODULE_ID"));
                        rowData[17]=Integer.toString(rsData.getInt("COMPANY_ID"));
                        rowData[18]=clsCompany.getCompanyName(rsData.getInt("COMPANY_ID"));
                        rowData[19]=rsData.getString("LINK_NO");
                        DataModel.addRow(rowData);
                    }
                    rsData.next();
                }
            }
        }
        catch(Exception e) {
            
        }
    }
    
    public boolean ShowDialog() {
        try {
            
            setSize(682,352);
            txtPartyCode.setText(PartyCode);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Select PJV",true);
            
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
        }
        catch(Exception e) {
        }
        return !Cancelled;
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
    
}