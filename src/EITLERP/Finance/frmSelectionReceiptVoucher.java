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

public class frmSelectionReceiptVoucher extends javax.swing.JApplet {
    
    private EITLTableModel DataModel=new EITLTableModel();
    private EITLTableCellRenderer Render=new EITLTableCellRenderer();
    private JDialog aDialog;
    public boolean Cancelled=false;
    public clsVoucher objSelectedItem=new clsVoucher();
    public HashMap SelectedItems=new HashMap();
    public String PartyCode="";
    public String MainCode="";
    private EITLComboModel cmbFromModel;
    
    
    public frmSelectionReceiptVoucher() {
        initComponents();
        GenerateYearCombo();
        cmbFromYear.setEnabled(false);
        chkYearOption.setSelected(false);
    }
    
    /** Initializes the applet frmSelectionPaymentVoucher */
    public void init() {
        setSize(700,475);
        initComponents();
        GenerateYearCombo();
        cmbFromYear.setEnabled(false);
        chkYearOption.setSelected(false);
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
        jLabel6 = new javax.swing.JLabel();
        cmbFromYear = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        txtToYear = new javax.swing.JTextField();
        lblMainCode = new javax.swing.JLabel();
        txtMainAccountCode = new javax.swing.JTextField();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        chkYearOption = new javax.swing.JCheckBox();
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
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Party Code :");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(5, 32, 80, 15);

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
        txtPartyCode.setBounds(90, 30, 130, 19);

        txtPartyName.setEnabled(false);
        jPanel1.add(txtPartyName);
        txtPartyName.setBounds(90, 55, 500, 19);

        cmdNext.setText("Next >>");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });

        jPanel1.add(cmdNext);
        cmdNext.setBounds(550, 230, 88, 25);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Invoice No. :");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(5, 82, 80, 15);

        jPanel1.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(90, 80, 130, 19);

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 12));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("From Date :");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(5, 107, 80, 15);

        cmbFromYear.setOpaque(false);
        cmbFromYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFromYearItemStateChanged(evt);
            }
        });

        jPanel1.add(cmbFromYear);
        cmbFromYear.setBounds(90, 165, 102, 24);

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 12));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("To Date :");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(205, 108, 60, 15);

        txtToYear.setEditable(false);
        txtToYear.setOpaque(false);
        jPanel1.add(txtToYear);
        txtToYear.setBounds(240, 168, 102, 19);

        lblMainCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMainCode.setText("Main Code :");
        jPanel1.add(lblMainCode);
        lblMainCode.setBounds(5, 7, 80, 15);

        jPanel1.add(txtMainAccountCode);
        txtMainAccountCode.setBounds(90, 5, 130, 19);

        jPanel1.add(txtFromDate);
        txtFromDate.setBounds(90, 105, 100, 19);

        jPanel1.add(txtToDate);
        txtToDate.setBounds(275, 105, 100, 19);

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 12));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Year :");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(5, 170, 80, 15);

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 12));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("To :");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(205, 170, 24, 15);

        chkYearOption.setText(" Year Option");
        chkYearOption.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkYearOptionItemStateChanged(evt);
            }
        });

        jPanel1.add(chkYearOption);
        chkYearOption.setBounds(10, 140, 130, 20);

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
    
    private void chkYearOptionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkYearOptionItemStateChanged
        // TODO add your handling code here:
        if(chkYearOption.isSelected()) {
            cmbFromYear.setEnabled(true);
            txtToYear.setEnabled(true);
            txtToYear.setText("");
            txtFromDate.setText("");
            txtFromDate.setEnabled(false);
            txtFromDate.setEditable(false);
            txtToDate.setText("");
            txtToDate.setEnabled(false);
            txtToDate.setEditable(false);
            GenerateYearCombo();
        } else {
            cmbFromYear.setEnabled(false);
            txtToYear.setEnabled(false);
            txtToYear.setText("");
            txtFromDate.setText("");
            txtFromDate.setEnabled(true);
            txtFromDate.setEditable(true);
            txtToDate.setText("");
            txtToDate.setEnabled(true);
            txtToDate.setEditable(true);
        }
    }//GEN-LAST:event_chkYearOptionItemStateChanged
    
    private void cmbFromYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFromYearItemStateChanged
        // TODO add your handling code here:
        int ToYear=Integer.parseInt((String)cmbFromYear.getSelectedItem())+1;
        txtToYear.setText(Integer.toString(ToYear));
    }//GEN-LAST:event_cmbFromYearItemStateChanged
    
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
            MainCode=txtMainAccountCode.getText();
            
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
        if(!txtMainAccountCode.getText().trim().equals("")) {
            txtPartyName.setText(clsPartyMaster.getAccountName(txtMainAccountCode.getText().trim(),txtPartyCode.getText()));
        } else {
            txtPartyName.setText(clsPartyMaster.getAccountName("",txtPartyCode.getText()));
        }
        
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
    private javax.swing.JCheckBox chkYearOption;
    private javax.swing.JComboBox cmbFromYear;
    private javax.swing.JButton cmdClose;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMainCode;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtMainAccountCode;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtToYear;
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
            
            Render.setCustomComponent(0,"CheckBox");
            Table.getColumnModel().getColumn(0).setCellRenderer(Render);
            Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void GenerateGridSJ() {
        try {
            
            if(txtMainAccountCode.getText().trim().equals("") || txtPartyCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this,"Insert Main Code and Party Code");
                return;
            }
            String AgentAlpha=clsSalesParty.getAgentAlpha(txtPartyCode.getText());
            HashMap List=new HashMap();
            FormatGrid();
            String FromDate="";
            String ToDate="";
            if(chkYearOption.isSelected()) {
                FromDate=EITLERPGLOBAL.getComboCode(cmbFromYear) + "-04-01";
                ToDate=txtToYear.getText().trim() + "-03-31";
            } else {
                FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim());
                ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
            }
            
            List=clsAccount.getPartyReceivableDetail(EITLERPGLOBAL.gCompanyID, txtMainAccountCode.getText().trim(),txtPartyCode.getText().trim(),  txtInvoiceNo.getText(),FromDate, ToDate,"","");
            
            for(int i=1;i<=List.size();i++) {
                clsVoucher objItem=(clsVoucher)List.get(Integer.toString(i));
                Object[] rowData=new Object[19];
                rowData[0]=new Boolean(false);
                rowData[1]=Integer.toString(i);
                rowData[2]=objItem.getAttribute("VOUCHER_NO").getString();
                rowData[3]=objItem.getAttribute("VOUCHER_DATE").getString();
                rowData[4]=Double.toString(objItem.getAttribute("AMOUNT").getDouble());
                rowData[5]=Double.toString(objItem.getAttribute("AMOUNT_PAID").getDouble());
                rowData[6]=AgentAlpha+"/"+data.getStringValueFromDB("SELECT AGENT_SR_NO FROM D_SAL_INVOICE_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INVOICE_NO ='"+objItem.getAttribute("INVOICE_NO").getString()+"' AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString())+"'");
                rowData[7]=objItem.getAttribute("INVOICE_NO").getString();
                rowData[8]=objItem.getAttribute("INVOICE_DATE").getString();
                rowData[9]=objItem.getAttribute("PO_NO").getString();
                rowData[10]=objItem.getAttribute("PO_DATE").getString();
                rowData[11]=objItem.getAttribute("REMARKS").getString();
                rowData[12]=objItem.getAttribute("DUE_DATE").getString();
                rowData[13]=objItem.getAttribute("GRN_NO").getString();
                rowData[14]=objItem.getAttribute("GRN_DATE").getString();
                rowData[15]=Integer.toString(objItem.getAttribute("MODULE_ID").getInt());
                rowData[16]=clsModules.getModuleName(EITLERPGLOBAL.gCompanyID,objItem.getAttribute("MODULE_ID").getInt());
                rowData[17]=Integer.toString(objItem.getAttribute("COMPANY_ID").getInt());
                rowData[18]=clsCompany.getCompanyName(objItem.getAttribute("COMPANY_ID").getInt());
                
                DataModel.addRow(rowData);
            }
        } catch(Exception e) {
        }
    }
    
    public boolean ShowDialog() {
        try {
            
            setSize(682,352);
            txtPartyCode.setText(PartyCode);
            GenerateYearCombo();
            cmbFromYear.setEnabled(false);
            chkYearOption.setSelected(false);
            EITLERPGLOBAL.setComboIndex(cmbFromYear,EITLERPGLOBAL.FinYearFrom);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Select SJ",true);
            
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
    
    private void GenerateYearCombo() {
        HashMap List=new HashMap();
        
        cmbFromModel=new EITLComboModel();
        cmbFromYear.setModel(cmbFromModel);
        cmbFromYear.removeAllItems();
        
        List=clsFinYear.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        
        for(int i=1;i<=List.size();i++) {
            clsFinYear ObjYear =(clsFinYear) List.get(Integer.toString(i));
            
            ComboData cmbData=new ComboData();
            cmbData.Text=Integer.toString((int)ObjYear.getAttribute("YEAR_FROM").getVal());
            cmbData.Code=(int)ObjYear.getAttribute("YEAR_FROM").getVal();
            cmbData.strCode =Integer.toString((int)ObjYear.getAttribute("YEAR_FROM").getVal());
            cmbFromModel.addElement(cmbData);
        }
    }
    
}
