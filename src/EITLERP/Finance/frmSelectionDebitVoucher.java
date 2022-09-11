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

public class frmSelectionDebitVoucher extends javax.swing.JApplet {
    
    private EITLTableModel DataModel=new EITLTableModel();
    private EITLTableCellRenderer Render=new EITLTableCellRenderer();
    private JDialog aDialog;
    public boolean Cancelled=false;
    public clsVoucher objSelectedItem=new clsVoucher();
    public HashMap SelectedItems=new HashMap();
    public String PartyCode="";
    public String MainCode="";
    public String InvoiceNo="";
    public String PONo="";
    
    public frmSelectionDebitVoucher() {
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
        lblPartyCode = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();
        cmdNext = new javax.swing.JButton();
        lblMainCode = new javax.swing.JLabel();
        txtMainCode = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdSelect = new javax.swing.JButton();
        cmdClose = new javax.swing.JButton();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 153, 204));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" SELECT DEBIT NOTE FOR PAYMENT VOUCHER");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 1, 666, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        lblPartyCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPartyCode.setText("Party Code :");
        jPanel1.add(lblPartyCode);
        lblPartyCode.setBounds(5, 40, 80, 15);

        txtPartyCode.setEnabled(false);
        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(90, 40, 130, 19);

        txtPartyName.setEnabled(false);
        jPanel1.add(txtPartyName);
        txtPartyName.setBounds(90, 70, 500, 19);

        cmdNext.setText("Next >>");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });

        jPanel1.add(cmdNext);
        cmdNext.setBounds(546, 231, 88, 25);

        lblMainCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMainCode.setText("Main Code :");
        jPanel1.add(lblMainCode);
        lblMainCode.setBounds(5, 10, 80, 15);

        txtMainCode.setEnabled(false);
        jPanel1.add(txtMainCode);
        txtMainCode.setBounds(90, 10, 130, 19);

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

        Tab.addTab("Select Debit", jPanel2);

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
                    objItem.setAttribute("LINK_NO",DataModel.getValueByVariable("LINK_NO",r));
                    SelectedItems.put(Integer.toString(SelectedItems.size()+1),objItem);
                }
            }
            
            
            PartyCode=txtPartyCode.getText();
            
            Cancelled=false;
            aDialog.dispose();
            
        }
        catch(Exception e) {
            Cancelled=true;
            aDialog.dispose();
        }
    }//GEN-LAST:event_cmdSelectActionPerformed
    
    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        GenerateGridDebit();
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNextActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdClose;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdSelect;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMainCode;
    private javax.swing.JLabel lblPartyCode;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtMainCode;
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
            
            DataModel.addColumn("*"); //0
            DataModel.addColumn("Sr."); //1
            DataModel.addColumn("Voucher No."); //2
            DataModel.addColumn("Voucher Date"); //3
            DataModel.addColumn("Amount"); //4
            DataModel.addColumn("Invoice No.");//5
            DataModel.addColumn("Invoice Date");//6
            DataModel.addColumn("GRN No."); //7
            DataModel.addColumn("GRN Date"); //8
            DataModel.addColumn("PO No."); //9
            DataModel.addColumn("PO Date"); //10
            DataModel.addColumn("Remarks"); //11
            DataModel.addColumn("Module Id");//12
            DataModel.addColumn("Module Name");//13
            DataModel.addColumn("Company ID");//14
            DataModel.addColumn("Company Name");//15
            DataModel.addColumn("Link No.");//16
            
            DataModel.SetVariable(0,"SELECT");
            DataModel.SetVariable(1,"SR_NO");
            DataModel.SetVariable(2,"VOUCHER_NO");
            DataModel.SetVariable(3,"VOUCHER_DATE");
            DataModel.SetVariable(4,"AMOUNT");
            DataModel.SetVariable(5,"INVOICE_NO");
            DataModel.SetVariable(6,"INVOICE_DATE");
            DataModel.SetVariable(7,"GRN_NO");
            DataModel.SetVariable(8,"GRN_DATE");
            DataModel.SetVariable(9,"PO_NO");
            DataModel.SetVariable(10,"PO_DATE");
            DataModel.SetVariable(11,"REMARKS");
            DataModel.SetVariable(12,"MODULE_ID");
            DataModel.SetVariable(13,"MODULE_NAME");
            DataModel.SetVariable(14,"COMPANY_ID");
            DataModel.SetVariable(15,"COMPANY_NAME");
            DataModel.SetVariable(16,"LINK_NO");
            
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
            
            Render.setCustomComponent(0,"CheckBox");
            Table.getColumnModel().getColumn(0).setCellRenderer(Render);
            Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        } catch(Exception e) {
        }
    }
    
    private void GenerateGridDebit() {
        try {
            HashMap List = new HashMap();
            
            FormatGrid();
            
            List = clsAccount.getPartyDebitNotes(EITLERPGLOBAL.gCompanyID,txtMainCode.getText().trim(),txtPartyCode.getText().trim(), FinanceGlobal.TYPE_DEBIT_NOTE); //EITLERPGLOBAL.gCompanyID, 
            
            for(int i=1;i<=List.size();i++) {
                clsVoucher objItem=(clsVoucher)List.get(Integer.toString(i));
                Object[] rowData=new Object[1];
                DataModel.addRow(rowData);
                int NewIndex=Table.getRowCount()-1;
                
                DataModel.setValueByVariable("SELECT",new Boolean(false),NewIndex);
                DataModel.setValueByVariable("SR_NO",Integer.toString(i),NewIndex);
                DataModel.setValueByVariable("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString(),NewIndex);
                DataModel.setValueByVariable("VOUCHER_DATE",objItem.getAttribute("VOUCHER_DATE").getString(),NewIndex);
                DataModel.setValueByVariable("AMOUNT",Double.toString(objItem.getAttribute("AMOUNT").getDouble()) ,NewIndex);
                DataModel.setValueByVariable("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString(),NewIndex);
                DataModel.setValueByVariable("INVOICE_DATE",objItem.getAttribute("INVOICE_DATE").getString(),NewIndex);
                DataModel.setValueByVariable("GRN_NO",objItem.getAttribute("GRN_NO").getString(),NewIndex);
                DataModel.setValueByVariable("GRN_DATE",objItem.getAttribute("GRN_DATE").getString(),NewIndex);
                DataModel.setValueByVariable("PO_NO",objItem.getAttribute("PO_NO").getString(),NewIndex);
                DataModel.setValueByVariable("PO_DATE",objItem.getAttribute("PO_DATE").getString(),NewIndex);
                DataModel.setValueByVariable("REMARKS",objItem.getAttribute("REMARKS").getString(),NewIndex);
                DataModel.setValueByVariable("MODULE_ID",Integer.toString(objItem.getAttribute("MODULE_ID").getInt()),NewIndex);
                DataModel.setValueByVariable("MODULE_NAME",clsModules.getModuleName(EITLERPGLOBAL.gCompanyID,objItem.getAttribute("MODULE_ID").getInt()),NewIndex);
                DataModel.setValueByVariable("COMPANY_ID",Integer.toString(objItem.getAttribute("COMPANY_ID").getInt()),NewIndex);
                DataModel.setValueByVariable("COMPANY_NAME",clsCompany.getCompanyName(objItem.getAttribute("COMPANY_ID").getInt()),NewIndex);
                DataModel.setValueByVariable("LINK_NO",objItem.getAttribute("LINK_NO").getString(),NewIndex);
            }
        } catch(Exception e) {
        }
    }
    
    public boolean ShowDialog() {
        try {
            
            setSize(682,352);
            txtPartyCode.setText(PartyCode);
            txtMainCode.setText(MainCode);
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