/*
 * frmRptAudit.java
 *
 * Created on August 3, 2006, 4:57 PM
 */

package EITLERP.ReportUI;

/**
 *
 * @author  root
 */
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.sql.*;


public class frmRptAudit extends javax.swing.JApplet {
    
    private EITLComboModel cmbModuleModel=new EITLComboModel();
    private EITLComboModel cmbUserModel=new EITLComboModel();
    private EITLComboModel cmbTypeModel=new EITLComboModel();
    private Connection Conn;
    private Statement Stmt;
    private ResultSet rsConn;
    /** Initializes the applet frmRptAudit */
    public void init() {
        setSize(423,300);
        initComponents();
        GenerateCombo();
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
        cmbType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbModule = new javax.swing.JComboBox();
        cmdPreview = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cmbUser = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("AUDIT REPORTS");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(9, 9, 300, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 3, 460, 30);

        jLabel2.setText("Module :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(9, 51, 70, 15);

        cmbType.setNextFocusableComponent(cmdPreview);
        getContentPane().add(cmbType);
        cmbType.setBounds(75, 167, 310, 24);

        jLabel3.setText("From Date");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(7, 96, 70, 15);

        txtFromDate.setNextFocusableComponent(txtToDate);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(78, 95, 110, 20);

        jLabel4.setText("To Date");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(197, 97, 70, 15);

        txtToDate.setNextFocusableComponent(cmbType);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(268, 96, 110, 20);

        jLabel5.setText("User :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(16, 135, 50, 15);

        cmbModule.setNextFocusableComponent(txtFromDate);
        getContentPane().add(cmbModule);
        cmbModule.setBounds(75, 47, 310, 24);

        cmdPreview.setText("Preview");
        cmdPreview.setNextFocusableComponent(cmdExit);
        cmdPreview.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmdPreviewItemStateChanged(evt);
            }
        });
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(163, 220, 110, 25);

        cmdExit.setText("Exit");
        cmdExit.setNextFocusableComponent(cmbModule);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(278, 219, 110, 25);

        jLabel6.setText("Type");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(23, 173, 40, 15);

        cmbUser.setNextFocusableComponent(cmbType);
        getContentPane().add(cmbUser);
        cmbUser.setBounds(74, 129, 310, 24);

    }//GEN-END:initComponents
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        try {
            String DocDateField="";
            String DocNoField="";
            int ModuleID=0;
            int UserID=0;
            String FromDate="";
            String ToDate="";
            String ModuleName="";
            String TableName="";
            String DBName="",DBName1="";
            int ReportType=0;
            String CompName = "";
            String LoginID = "";
            ModuleID=EITLERPGLOBAL.getComboCode(cmbModule);
            FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
            ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
            
            ModuleName=clsModules.getModuleName(EITLERPGLOBAL.gCompanyID, ModuleID);
            UserID=EITLERPGLOBAL.getComboCode(cmbUser);
            DocDateField=clsModules.getDocDateFieldName(EITLERPGLOBAL.gCompanyID, ModuleID);
            DocNoField=clsModules.getDocNoFieldName(EITLERPGLOBAL.gCompanyID, ModuleID);
            TableName=clsModules.getHeaderTableName(EITLERPGLOBAL.gCompanyID, ModuleID)+"_H";
            LoginID = data.getStringValueFromDB("SELECT LOGIN_ID FROM D_COM_USER_MASTER WHERE  USER_ID = "+ UserID +"");
            
            ReportType=EITLERPGLOBAL.getComboCode(cmbType);
            
            String strSql = "SELECT TABLE_SCHEMA FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='FINANCE' " +
            "AND TABLE_NAME= '"+TableName+"'";
            if(data.IsRecordExist(strSql)) {
                DBName = data.getStringValueFromDB(strSql);
                DBName1 = EITLERPGLOBAL.DBName;
            }
            else {
                DBName = EITLERPGLOBAL.DBName;
                DBName1 = EITLERPGLOBAL.DBName;
            }
            
            if(EITLERPGLOBAL.gCompanyID==2)
            {
                CompName = "BARODA";
            }
            else
            if(EITLERPGLOBAL.gCompanyID==3)
            {
                CompName = "ANKLESHWAR";
            }    
            
            
            if(ReportType==1) {
                try {
                    
                    
                    URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptRejectedDocList.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocDateField="+DocDateField+"&DocNoField="+DocNoField+"&FromDate="+FromDate+"&ToDate="+ToDate+"&ModuleName="+ModuleName+"&TableName="+TableName+"&UserID="+UserID+"&DBName="+DBName+"&DBName1="+DBName1+"&ModuleID="+ModuleID+"&CompName="+CompName+"&LoginID="+LoginID);
                    System.out.println(ReportFile);
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
                }
                
                
            }
            
            
            //Approved Document List
            if(ReportType==2) {
                try {
                    URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptApprovedDocList.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocDateField="+DocDateField+"&DocNoField="+DocNoField+"&FromDate="+FromDate+"&ToDate="+ToDate+"&ModuleName="+ModuleName+"&TableName="+TableName+"&UserID="+UserID+"&DBName="+DBName+"&DBName1="+DBName1+"&ModuleID="+ModuleID+"&CompName="+CompName+"&LoginID="+LoginID);
                    System.out.println(ReportFile);
                    EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
                }
                
            }
            
            
            
            
            
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void cmdPreviewItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmdPreviewItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPreviewItemStateChanged
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbModule;
    private javax.swing.JComboBox cmbType;
    private javax.swing.JComboBox cmbUser;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombo() {
        
        try {
            HashMap List=new HashMap();
            String strCondition="";
            
            //----- Generate cmbType ------- //
            cmbModuleModel=new EITLComboModel();
            cmbModule.removeAllItems();
            cmbModule.setModel(cmbModuleModel);
            
            strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ORDER BY MODULE_ID";
            
            List=clsModules.getList(strCondition);
            for(int i=1;i<=List.size();i++) {
                clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
                //Check that Module Access Rights are given
                int ModuleID=(int)ObjModules.getAttribute("MODULE_ID").getVal();
                int MenuID=clsMenu.getMenuIDFromModule(EITLERPGLOBAL.gCompanyID, ModuleID);
                
                ComboData aData=new ComboData();
                aData.Text=(String) ObjModules.getAttribute("MODULE_DESC").getObj();
                aData.Code=(int) ObjModules.getAttribute("MODULE_ID").getVal();
                cmbModuleModel.addElement(aData);
            }
            
            
            //-------- Generating Buyer Combo --------//
            cmbUserModel=new EITLComboModel();
            cmbUser.removeAllItems();
            cmbUser.setModel(cmbUserModel);
            clsUser ObjUser=new clsUser();
            
            List=(new clsUser()).getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" "); //AND DEPT_ID = 16
            for(int i=1;i<=List.size();i++) {
                ObjUser=(clsUser)List.get(Integer.toString(i));
                
                ComboData aData=new ComboData();
                
                aData.Text=(String) ObjUser.getAttribute("USER_NAME").getObj();
                aData.Code=(long)ObjUser.getAttribute("USER_ID").getVal();
                
                cmbUserModel.addElement(aData);
            }
            //----------------------------------------//
            
            
            //-------- Generating Buyer Combo --------//
            cmbTypeModel=new EITLComboModel();
            cmbType.removeAllItems();
            cmbType.setModel(cmbTypeModel);
            
            ComboData aData=new ComboData();
            
            aData.Text="Rejected Document List";
            aData.Code=1;
            
            cmbTypeModel.addElement(aData);
            
            aData=new ComboData();
            
            aData.Text="Approved Document List";
            aData.Code=2;
            
            cmbTypeModel.addElement(aData);
            //----------------------------------------//
            
        }
        catch(Exception e) {
            
        }
    }
    
    
}
