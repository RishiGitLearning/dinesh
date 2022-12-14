/*
 * frmPOMIRLink.java
 *
 * Created on June 20, 2005, 3:52 PM
 */

package EITLERP.Stores;


import EITLERP.*;
import EITLERP.Purchase.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *
 * @author  root
 */
public class frmPOJobLink extends javax.swing.JApplet {
    
    private EITLComboModel cmbUserModel=new EITLComboModel();
    private EITLTableModel DataModel=new EITLTableModel();
    
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    
    
    /** Initializes the applet frmPOMIRLink */
    public void init() {
        setSize(530,525);
        initComponents();
        GenerateCombo();
        FormatGrid();
        
        EITLERPGLOBAL.setComboIndex(cmbUser,EITLERPGLOBAL.gUserID);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbUser = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdGo = new javax.swing.JButton();
        cmdLink = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        cmdSelectAll = new javax.swing.JButton();
        cmdDeSelectAll = new javax.swing.JButton();
        cmdShowPO = new javax.swing.JButton();
        cmdShowMIR = new javax.swing.JButton();
        chkUser = new javax.swing.JCheckBox();
        
        getContentPane().setLayout(null);
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        
        jPanel1.setLayout(null);
        
        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setText("JOBWORK ENTRY - PO/RGP LINK PROCESS");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(9, 7, 283, 15);
        
        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 1, 511, 30);
        
        jLabel2.setText("List of Jobwork documents without PO/RGP reference.");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(6, 43, 391, 15);
        
        cmbUser.setEnabled(false);
        getContentPane().add(cmbUser);
        cmbUser.setBounds(85, 70, 316, 24);
        
        jLabel3.setText("List of PO/RGP matched with the Jobwork documents");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(8, 108, 350, 15);
        
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
        
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(9, 132, 492, 297);
        
        cmdGo.setText("Show");
        cmdGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGoActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdGo);
        cmdGo.setBounds(408, 69, 88, 25);
        
        cmdLink.setText("Link");
        cmdLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLinkActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdLink);
        cmdLink.setBounds(304, 439, 91, 25);
        
        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdExit);
        cmdExit.setBounds(404, 439, 89, 25);
        
        cmdSelectAll.setText("Select All");
        cmdSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectAllActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdSelectAll);
        cmdSelectAll.setBounds(10, 439, 109, 25);
        
        cmdDeSelectAll.setText("DeSelect All");
        cmdDeSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeSelectAllActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdDeSelectAll);
        cmdDeSelectAll.setBounds(10, 467, 108, 25);
        
        cmdShowPO.setText("Show PO");
        cmdShowPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowPOActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdShowPO);
        cmdShowPO.setBounds(139, 440, 125, 25);
        
        cmdShowMIR.setText("Show Jobwork");
        cmdShowMIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowMIRActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdShowMIR);
        cmdShowMIR.setBounds(139, 468, 125, 25);
        
        chkUser.setText("User");
        chkUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkUserItemStateChanged(evt);
            }
        });
        
        getContentPane().add(chkUser);
        chkUser.setBounds(13, 70, 67, 23);
        
    }//GEN-END:initComponents
    
    private void chkUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkUserItemStateChanged
        // TODO add your handling code here:
        cmbUser.setEnabled(chkUser.isSelected());
    }//GEN-LAST:event_chkUserItemStateChanged
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLinkActionPerformed
        // TODO add your handling code here:
        String strSQL="";
        
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to link selected Jobwork entries with POs ?","Confirmation",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            for(int i=0;i<Table.getRowCount();i++) {
                if( ((Boolean)Table.getValueAt(i,8)).equals(new Boolean(true))) {
                    
                    String JobNo=(String)Table.getValueAt(i,2);
                    int JobSrNo=0;
                    
                    if(EITLERPGLOBAL.IsNumber((String)Table.getValueAt(i, 11))) {
                        JobSrNo=Integer.parseInt((String)Table.getValueAt(i, 11));
                    }
                    
                    String PONo=(String)Table.getValueAt(i, 4);
                    int POSrNo=0;
                    int POType=0;
                    
                    if(EITLERPGLOBAL.IsNumber((String)Table.getValueAt(i, 9))) {
                        POSrNo=Integer.parseInt((String)Table.getValueAt(i, 9));
                    }
                    
                    if(EITLERPGLOBAL.IsNumber((String)Table.getValueAt(i, 10))) {
                        POType=Integer.parseInt((String)Table.getValueAt(i, 10));
                    }

                    
                    String RGPNo=(String)Table.getValueAt(i, 6);
                    int RGPSrNo=0;
                    
                    
                    if(EITLERPGLOBAL.IsNumber((String)Table.getValueAt(i, 12))) {
                        RGPSrNo=Integer.parseInt((String)Table.getValueAt(i, 12));
                    }
                    
                    
                    if((!JobNo.trim().equals(""))&&(!PONo.trim().equals(""))) {
                        strSQL="UPDATE D_INV_JOB_DETAIL SET PO_NO='"+PONo+"',PO_SR_NO="+POSrNo+",PO_TYPE="+POType+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND JOB_NO='"+JobNo+"' AND SR_NO="+JobSrNo;
                        data.Execute(strSQL);
                    }
                    

                    if((!JobNo.trim().equals(""))&&(!RGPNo.trim().equals(""))) {
                        strSQL="UPDATE D_INV_JOB_DETAIL SET RGP_NO='"+RGPNo+"',RGP_SR_NO="+RGPSrNo+" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND JOB_NO='"+JobNo+"' AND SR_NO="+JobSrNo;
                        data.Execute(strSQL);
                    }
                    
                }
            }
            GenerateGrid();
        }
        
    }//GEN-LAST:event_cmdLinkActionPerformed
    
    private void cmdDeSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeSelectAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<Table.getRowCount()-1;i++) {
            Table.setValueAt(new Boolean(false), i, 8);
        }
        
    }//GEN-LAST:event_cmdDeSelectAllActionPerformed
    
    private void cmdSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<Table.getRowCount()-1;i++) {
            Table.setValueAt(new Boolean(true), i, 8);
        }
    }//GEN-LAST:event_cmdSelectAllActionPerformed
    
    private void cmdShowMIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowMIRActionPerformed
        // TODO add your handling code here:
        String JobNo=(String)Table.getValueAt(Table.getSelectedRow(), 2);
        
        if(!JobNo.trim().equals("")) {
            AppletFrame aFrame=new AppletFrame("Jobwork");
            aFrame.startAppletEx("EITLERP.Stores.frmJobwork","Jobwork");
            frmJobwork ObjDoc=(frmJobwork) aFrame.ObjApplet;
            ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,JobNo);
        }
        
    }//GEN-LAST:event_cmdShowMIRActionPerformed
    
    private void cmdShowPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowPOActionPerformed
        // TODO add your handling code here:
        String PONo=(String)Table.getValueAt(Table.getSelectedRow(), 4);
        
        if(!PONo.trim().equals("")) {
            AppletFrame aFrame=new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen","Purchase Order");
            frmPOGen ObjDoc=(frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType=clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID, PONo) ;
            ObjDoc.FindEx((int)EITLERPGLOBAL.gCompanyID,PONo);
        }
    }//GEN-LAST:event_cmdShowPOActionPerformed
    
    private void cmdGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGoActionPerformed
        // TODO add your handling code here:
        GenerateGrid();
    }//GEN-LAST:event_cmdGoActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JCheckBox chkUser;
    private javax.swing.JComboBox cmbUser;
    private javax.swing.JButton cmdDeSelectAll;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdGo;
    private javax.swing.JButton cmdLink;
    private javax.swing.JButton cmdSelectAll;
    private javax.swing.JButton cmdShowMIR;
    private javax.swing.JButton cmdShowPO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombo() {
        
        HashMap UserList=new HashMap();
        
        cmbUser.removeAllItems();
        cmbUserModel=new EITLComboModel();
        cmbUser.setModel(cmbUserModel);
        
        UserList=new clsUser().getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        
        
        for(int i=1;i<=UserList.size();i++) {
            clsUser ObjUser=(clsUser)UserList.get(Integer.toString(i));
            
            ComboData aData=new ComboData();
            aData.Code=(int)ObjUser.getAttribute("USER_ID").getVal();
            aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
            
            cmbUserModel.addElement(aData);
        }
        cmbUser.setSelectedIndex(0);
        
    }
    
    
    private void FormatGrid() {
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        DataModel.addColumn("Sr."); //0
        DataModel.addColumn("Item ID"); //1
        DataModel.addColumn("Job No."); //2
        DataModel.addColumn("Job Date"); //3
        DataModel.addColumn("PO No."); //4
        DataModel.addColumn("PO Date"); //5
        DataModel.addColumn("RGP No."); //6
        DataModel.addColumn("RGP Date"); //7
        DataModel.addColumn("Link"); //8
        DataModel.addColumn("PO Sr No."); //9
        DataModel.addColumn("PO Type"); //10
        DataModel.addColumn("MIR Sr No."); //11
        DataModel.addColumn("RGP Sr No."); //12
        
        Rend.setCustomComponent(8,"CheckBox");
        Table.getColumnModel().getColumn(6).setCellRenderer(Rend);
        Table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        DataModel.SetReadOnly(0);
        DataModel.SetReadOnly(1);
        DataModel.SetReadOnly(2);
        DataModel.SetReadOnly(3);
        DataModel.SetReadOnly(4);
        DataModel.SetReadOnly(5);
        DataModel.SetReadOnly(6);
        DataModel.SetReadOnly(7);
        DataModel.SetReadOnly(9);
        DataModel.SetReadOnly(10);
        DataModel.SetReadOnly(11);
        DataModel.SetReadOnly(12);
        
    }
    
    
    
    private void GenerateGrid() {
        Connection tmpConn;
        Statement stTmp,stJob,stPO,stRGP;
        ResultSet rsTmp,rsJob,rsPO,rsRGP;
        
        HashMap JobList=new HashMap();
        
        String JobNo="";
        String JobDate="";
        String ItemID="";
        String SuppID="";
        String PONo="";
        String PODate="";
        int POSrNo=0;
        int JobSrNo=0;
        int POType=0;
        String RGPNo="";
        String RGPDate="";
        int RGPSrNo=0;
        int cnt=0,reccnt=0;
        
        try {
            tmpConn=data.getConn();
            
            FormatGrid();
            
            if(chkUser.isSelected()) {
                JobList=clsJobwork.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 1, EITLERPGLOBAL.OnDocDate,EITLERPGLOBAL.FinYearFrom);
            }
            else {
                JobList=clsJobwork.getPendingApprovals(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID,1, EITLERPGLOBAL.OnDocDate,EITLERPGLOBAL.FinYearFrom);
            }
            
            
            for(int i=1;i<=JobList.size();i++) {
                
                JobSrNo=0;
                Object[] rowData=new Object[13];
                clsJobwork ObjJob=(clsJobwork)JobList.get(Integer.toString(i));
                
                JobNo=(String)ObjJob.getAttribute("JOB_NO").getObj();
                JobDate=(String)ObjJob.getAttribute("JOB_DATE").getObj();
                
                rsTmp=data.getResult("SELECT SUPP_ID FROM D_INV_JOB_HEADER WHERE JOB_NO='"+JobNo+"'");
                rsTmp.first();
                
                SuppID=rsTmp.getString("SUPP_ID");
                
                // Get the List of Items of the MIR
                stJob=tmpConn.createStatement();
                rsJob=stJob.executeQuery("SELECT ITEM_ID,SR_NO FROM D_INV_JOB_DETAIL WHERE JOB_NO='"+JobNo+"' AND PO_NO='' AND RGP_NO=''");
                rsJob.first();
                
                if(rsJob.getRow()>0) {
                    
                    while(!rsJob.isAfterLast()) {
                        ItemID=rsJob.getString("ITEM_ID");
                        JobSrNo=rsJob.getInt("SR_NO");
                        
                        PONo="";
                        PODate="";
                        POSrNo=0;
                        POType=0;
                        
                        reccnt=0;
                        
                        stPO=tmpConn.createStatement();
                        rsPO=stPO.executeQuery("SELECT A.PO_NO,A.PO_DATE,A.SUPP_ID,B.ITEM_ID,B.SR_NO,B.PO_TYPE FROM D_PUR_PO_HEADER A,D_PUR_PO_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.PO_NO=B.PO_NO AND A.PO_TYPE=B.PO_TYPE AND A.SUPP_ID='"+SuppID+"' AND B.ITEM_ID='"+ItemID+"' AND B.RECD_QTY<QTY AND A.APPROVED=1");
                        rsPO.first();
                        
                        if(rsPO.getRow()>0) {
                            while(!rsPO.isAfterLast()) {
                                reccnt++;
                                rsPO.next();
                            }
                        }
                        
                        
                        rsPO.first();
                        if(rsPO.getRow()>0) {
                            if(reccnt==1) {
                                PONo=rsPO.getString("PO_NO");
                                PODate=rsPO.getString("PO_DATE");
                                POSrNo=rsPO.getInt("SR_NO");
                                POType=rsPO.getInt("PO_TYPE");
                            }
                        }
                        
                        
                        // PO not found  //
                        if(reccnt<=0) {
                            
                            
                            //Find RGP now
                            stRGP=tmpConn.createStatement();
                            rsRGP=stRGP.executeQuery("SELECT A.GATEPASS_NO,A.GATEPASS_DATE,A.SUPP_ID,B.ITEM_CODE,B.SR_NO FROM D_INV_RGP_HEADER A,D_INV_RGP_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GATEPASS_NO=B.GATEPASS_NO AND A.SUPP_ID='"+SuppID+"' AND B.ITEM_CODE='"+ItemID+"' AND B.RETURNED_QTY<QTY AND A.APPROVED=1");
                            rsRGP.first();
                            
                            if(rsRGP.getRow()>0) {
                                while(!rsRGP.isAfterLast()) {
                                    reccnt++;
                                    rsRGP.next();
                                }
                            }
                            
                            rsRGP.first();
                            if(rsRGP.getRow()>0) {
                                if(reccnt==1) {
                                    RGPNo=rsPO.getString("GATEPASS_NO");
                                    RGPDate=rsPO.getString("GATEPASS_DATE");
                                    RGPSrNo=rsPO.getInt("SR_NO");
                                }
                            }
                        }
                        
                        
                        cnt++;
                        rowData[0]=Integer.toString(cnt);
                        rowData[1]=ItemID;
                        rowData[2]=JobNo;
                        rowData[3]=EITLERPGLOBAL.formatDate(JobDate);
                        rowData[4]=PONo;
                        rowData[5]=EITLERPGLOBAL.formatDate(PODate);
                        rowData[6]=RGPNo;
                        rowData[7]=EITLERPGLOBAL.formatDate(RGPDate);
                        rowData[8]=new Boolean(false);
                        rowData[9]=Integer.toString(POSrNo);
                        rowData[10]=Integer.toString(POType);
                        rowData[11]=Integer.toString(JobSrNo);
                        rowData[12]=Integer.toString(RGPSrNo);
                        
                        DataModel.addRow(rowData);
                        
                        rsJob.next();
                    }
                }
            }
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
    }
    
    
}
