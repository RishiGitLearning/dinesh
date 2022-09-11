/*
 * frmRptPendingIndent.java
 *
 * Created on January 19, 2005, 1:07 PM
 */

package EITLERP.Purchase;

/**
 *
 * @author  root
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import java.net.*;
import java.sql.*;
import java.io.*;
import EITLERP.Utils.*;
import EITLERP.Finance.UtilFunctions;
import TReportWriter.*;


public class frmRptPendingPO extends javax.swing.JApplet {
    
    private EITLComboModel cmbDeptModel=new EITLComboModel();
    private EITLComboModel cmbBuyerModel=new EITLComboModel();
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptPendingIndent */
    public void init() {
        setSize(450,300);
        initComponents();
        GenerateBuyerCombo();
        GenerateDeptCombo();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        cmdPreview = new javax.swing.JButton();
        cmdExi = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        txtSuppCode = new javax.swing.JTextField();
        chkSupp = new javax.swing.JCheckBox();
        txtSuppName = new javax.swing.JTextField();
        cmbBuyer = new javax.swing.JComboBox();
        chkBuyer = new javax.swing.JCheckBox();
        chkDept = new javax.swing.JCheckBox();
        cmbDept = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pending PO List");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(1, 3, 479, 27);

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(251, 208, 82, 25);

        cmdExi.setText("Exit");
        cmdExi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExiActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExi);
        cmdExi.setBounds(336, 207, 82, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jLabel3.setText("Date From");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(35, 10, 70, 20);

        jPanel1.add(txtFromDate);
        txtFromDate.setBounds(112, 10, 114, 21);

        jLabel2.setText("To");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(232, 10, 25, 20);

        jPanel1.add(txtToDate);
        txtToDate.setBounds(262, 10, 114, 21);

        txtSuppCode.setName("SUPP_ID");
        txtSuppCode.setEnabled(false);
        txtSuppCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusLost(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppCodeKeyPressed(evt);
            }
        });

        jPanel1.add(txtSuppCode);
        txtSuppCode.setBounds(112, 40, 115, 19);

        chkSupp.setText("Supplier");
        chkSupp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSuppItemStateChanged(evt);
            }
        });

        jPanel1.add(chkSupp);
        chkSupp.setBounds(12, 40, 89, 23);

        txtSuppName.setEditable(false);
        jPanel1.add(txtSuppName);
        txtSuppName.setBounds(112, 70, 283, 19);

        cmbBuyer.setEnabled(false);
        cmbBuyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBuyerActionPerformed(evt);
            }
        });

        jPanel1.add(cmbBuyer);
        cmbBuyer.setBounds(112, 100, 180, 20);

        chkBuyer.setText("Buyer");
        chkBuyer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkBuyerItemStateChanged(evt);
            }
        });

        jPanel1.add(chkBuyer);
        chkBuyer.setBounds(12, 95, 89, 20);

        chkDept.setText("Department");
        chkDept.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDeptItemStateChanged(evt);
            }
        });

        jPanel1.add(chkDept);
        chkDept.setBounds(12, 130, 100, 20);

        cmbDept.setEnabled(false);
        jPanel1.add(cmbDept);
        cmbDept.setBounds(112, 130, 180, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(8, 40, 410, 160);

    }//GEN-END:initComponents
    
    private void cmbBuyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBuyerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBuyerActionPerformed
    
    private void chkDeptItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDeptItemStateChanged
        // TODO add your handling code here:
        cmbDept.setEnabled(chkDept.isSelected());
    }//GEN-LAST:event_chkDeptItemStateChanged
    
    private void chkBuyerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkBuyerItemStateChanged
        // TODO add your handling code here:
        cmbBuyer.setEnabled(chkBuyer.isSelected());
    }//GEN-LAST:event_chkBuyerItemStateChanged
    
    private void chkSuppItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSuppItemStateChanged
        // TODO add your handling code here:
        txtSuppCode.setEnabled(chkSupp.isSelected());
    }//GEN-LAST:event_chkSuppItemStateChanged
    
    private void GenerateBuyerCombo() {
        cmbBuyerModel=new EITLComboModel();
        cmbBuyer.removeAllItems();
        cmbBuyer.setModel(cmbBuyerModel);
        
        
        HashMap List=new HashMap();
        List=(new clsUser()).getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        
        for(int i=1;i<=List.size();i++) {
            clsUser ObjUser=(clsUser)List.get(Integer.toString(i));
            
            ComboData aData=new ComboData();
            aData.Code=(int)ObjUser.getAttribute("USER_ID").getVal();
            aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
            
            cmbBuyerModel.addElement(aData);
        }
    }
    
    private void GenerateDeptCombo() {
        cmbDeptModel=new EITLComboModel();
        cmbDept.removeAllItems();
        cmbDept.setModel(cmbDeptModel);
        
        
        HashMap List=new HashMap();
        List=(new clsDepartment()).getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        
        for(int i=1;i<=List.size();i++) {
            clsDepartment ObjDepartment=(clsDepartment)List.get(Integer.toString(i));
            
            ComboData aData=new ComboData();
            aData.Code=(int)ObjDepartment.getAttribute("DEPT_ID").getVal();
            aData.Text=(String)ObjDepartment.getAttribute("DEPT_DESC").getObj();
            
            cmbDeptModel.addElement(aData);
        }
    }
    
    
    
    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:
        if(!txtSuppCode.getText().trim().equals("")) {
            txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
        }
        
    }//GEN-LAST:event_txtSuppCodeFocusLost
    
    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtSuppCode.setText(aList.ReturnVal);
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        //=========================================
        
    }//GEN-LAST:event_txtSuppCodeKeyPressed
    
    private void cmdExiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExiActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExiActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter to date");
            return;
        }
        
        if(!EITLERPGLOBAL.isDate(txtFromDate.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Please enter valid From Date");
            return;
        }
        
        if(!EITLERPGLOBAL.isDate(txtToDate.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Please enter valid To Date");
            return;
        }
        
        
        if(chkSupp.isSelected()) {
            if(txtSuppCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please specify supplier code");
                return;
            }
        }
        
        
        
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        String SuppCode=txtSuppCode.getText();
        String Buyer=Integer.toString(EITLERPGLOBAL.getComboCode(cmbBuyer));
        String Dept=Integer.toString(EITLERPGLOBAL.getComboCode(cmbDept));
        String strQuery="";
        
        if(chkSupp.isSelected()) {
            strQuery=strQuery+" AND A.SUPP_ID='"+txtSuppCode.getText()+"' ";
        }
        
        if(chkBuyer.isSelected()) {
            strQuery=strQuery+" AND A.BUYER="+EITLERPGLOBAL.getComboCode(cmbBuyer)+" ";
        }
        
        if(chkDept.isSelected()) {
            strQuery=strQuery+" AND B.DEPT_ID="+EITLERPGLOBAL.getComboCode(cmbDept)+" ";
        }
        
        
        try {
            //CompanyID
            //FDATE
            //TDATE
            //SUPPLIER
            //BUYER
            //DEPARTMENT
            //QUERY
            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPendingPOBoth.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&FDATE="+FromDate+"&TDATE="+ToDate+"&SUPPLIER="+SuppCode+"&BUYER="+Buyer+"&DEPARTMENT="+Dept+"&QUERY="+strQuery);
            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
            //GenerateReport(strQuery);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
        }
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkBuyer;
    private javax.swing.JCheckBox chkDept;
    private javax.swing.JCheckBox chkSupp;
    private javax.swing.JComboBox cmbBuyer;
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JButton cmdExi;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport(String strQuery) {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("PO_NO");
            objReportData.AddColumn("PO_DATE");
            objReportData.AddColumn("DEPT_ID");
            objReportData.AddColumn("DEPT_DESC");
            objReportData.AddColumn("USER_ID");
            objReportData.AddColumn("USER_NAME");
            objReportData.AddColumn("SUPP_ID");
            objReportData.AddColumn("SUPP_NAME");
            objReportData.AddColumn("ITEM_ID");
            objReportData.AddColumn("ITEM_DESC");
            objReportData.AddColumn("ITEM_DESCRIPTION");
            objReportData.AddColumn("BAL");
            objReportData.AddColumn("UNIT_DESC");
            objReportData.AddColumn("PO_SR_NO");
            objReportData.AddColumn("DELIVERY_DATE");
            objReportData.AddColumn("PURPOSE");
            objReportData.AddColumn("REMARKS");
            objReportData.AddColumn("FDATE");
            objReportData.AddColumn("TDATE");
            objReportData.AddColumn("QTY");
            objReportData.AddColumn("RECD_QTY");
            objReportData.AddColumn("BAL_QTY");
            objReportData.AddColumn("INSP_QTY");
            objReportData.AddColumn("TOLERANCE_LIMIT");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("PO_NO","");
            objOpeningRow.setValue("PO_DATE","0000-00-00");
            objOpeningRow.setValue("DEPT_ID","");
            objOpeningRow.setValue("DEPT_DESC","");
            objOpeningRow.setValue("USER_ID","");
            objOpeningRow.setValue("USER_NAME","");
            objOpeningRow.setValue("SUPP_ID","");
            objOpeningRow.setValue("SUPP_NAME","");
            objOpeningRow.setValue("ITEM_ID","");
            objOpeningRow.setValue("ITEM_DESC","");
            objOpeningRow.setValue("ITEM_DESCRIPTION","");
            objOpeningRow.setValue("BAL","");
            objOpeningRow.setValue("UNIT_DESC","");
            objOpeningRow.setValue("PO_SR_NO","");
            objOpeningRow.setValue("DELIVERY_DATE","0000-00-00");
            objOpeningRow.setValue("PURPOSE","");
            objOpeningRow.setValue("REMARKS","");
            objOpeningRow.setValue("FDATE","");
            objOpeningRow.setValue("TDATE","");
            objOpeningRow.setValue("QTY","");
            objOpeningRow.setValue("RECD_QTY","");
            objOpeningRow.setValue("BAL_QTY","");
            objOpeningRow.setValue("INSP_QTY","");
            objOpeningRow.setValue("TOLERANCE_LIMIT","");
            
            String strSQL = "";
            double Bal_Qty=0,Qty=0,Recd_Qty=0,Insp_Qty=0,MQty=0,NQty=0,Tolerance_Limit=0;
            
            strSQL = "SELECT A.PO_NO,A.PO_DATE,B.DEPT_ID,A.SUPP_ID, "+
            "B.ITEM_ID,B.ITEM_DESC,D.ITEM_DESCRIPTION,(B.QTY-B.RECD_QTY) BAL , E.DESC UNIT_DESC, "+
            "C.DEPT_DESC,F.USER_ID,F.USER_NAME,B.SR_NO,B.DELIVERY_DATE,A.PURPOSE,A.REMARKS, "+
            "G.SUPP_NAME, SUM(IF(N.QTY IS NULL,0,N.QTY))+SUM(IF(M.QTY IS NULL,0,M.QTY)) AS RECD_QTY,B.QTY, "+
            "D.TOLERANCE_LIMIT,SUM(IF(N.QTY IS NULL,0,N.QTY)) AS NQTY,SUM(IF(M.QTY IS NULL,0,M.QTY)) AS MQTY "+
            "FROM D_PUR_PO_HEADER A "+
            "LEFT JOIN D_COM_USER_MASTER AS F ON (A.COMPANY_ID = F.COMPANY_ID AND A.BUYER = F.USER_ID) "+
            "LEFT JOIN D_COM_SUPP_MASTER AS G ON (A.COMPANY_ID = G.COMPANY_ID AND A.SUPP_ID = G.SUPPLIER_CODE), "+
            "D_PUR_PO_DETAIL B "+
            "LEFT JOIN D_COM_DEPT_MASTER AS C ON (B.COMPANY_ID = C.COMPANY_ID AND B.DEPT_ID =C.DEPT_ID) "+
            "LEFT JOIN D_INV_ITEM_MASTER AS D ON (B.COMPANY_ID = D.COMPANY_ID AND B.ITEM_ID = D.ITEM_ID) "+
            "LEFT JOIN D_COM_PARAMETER_MAST AS E ON (B.COMPANY_ID = E.COMPANY_ID AND E.PARA_ID = 'UNIT' AND B.UNIT = E.PARA_CODE) "+
            "LEFT JOIN D_INV_JOB_DETAIL N ON (N.PO_NO=B.PO_NO  AND N.PO_TYPE=B.PO_TYPE AND N.PO_SR_NO=B.SR_NO AND N.JOB_NO IN (SELECT JOB_NO FROM D_INV_JOB_HEADER WHERE JOB_NO=N.JOB_NO  AND APPROVED=1 AND CANCELLED=0)) "+
            "LEFT JOIN D_INV_MIR_DETAIL M ON (M.PO_NO=B.PO_NO  AND M.PO_TYPE=B.PO_TYPE AND M.PO_SR_NO=B.SR_NO AND M.MIR_NO IN (SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE MIR_NO=M.MIR_NO  AND APPROVED=1 AND CANCELLED=0)) "+
            "WHERE B.COMPANY_ID = "+ EITLERPGLOBAL.gCompanyID +" AND "+
            "A.COMPANY_ID = B.COMPANY_ID AND "+
            "B.PO_NO = A.PO_NO AND A.APPROVED =1 AND A.CANCELLED=0 AND  "+
            "A.PO_DATE >='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) +"' AND "+
            "A.PO_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) +"' " +
            " " + strQuery + " " +
            //"GROUP BY B.PO_NO,B.PO_TYPE,B.SR_NO,B.DEPT_ID,A.SUPP_ID,F.USER_ID "+
            "GROUP BY F.USER_ID,A.SUPP_ID,B.DEPT_ID,A.PO_NO,B.SR_NO "+            
            "ORDER BY F.USER_ID,A.SUPP_ID,B.DEPT_ID,A.PO_NO,B.SR_NO";
            
            //(B.QTY -(SUM(IF(N.QTY IS NULL,0,N.QTY))+SUM(IF(M.QTY IS NULL,0,M.QTY)))) > ((B.QTY*D.TOLERANCE_LIMIT) / 100)
            
            System.out.println("sql="+strSQL);
            
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    Bal_Qty=0;
                    Qty=0;
                    Recd_Qty=0;
                    Insp_Qty=0;
                    NQty=0;
                    MQty=0;
                    Tolerance_Limit=0;
                    
                    MQty=EITLERPGLOBAL.round(Double.parseDouble(UtilFunctions.getString(rsTmp,"MQTY","")),2);
                    NQty=EITLERPGLOBAL.round(Double.parseDouble(UtilFunctions.getString(rsTmp,"NQTY","")),2);
                    Qty=EITLERPGLOBAL.round(Double.parseDouble(UtilFunctions.getString(rsTmp,"QTY","")),2);
                    Tolerance_Limit = EITLERPGLOBAL.round(UtilFunctions.getDouble(rsTmp,"TOLERANCE_LIMIT",0),2);
                    String PONo = UtilFunctions.getString(rsTmp,"PO_NO","");
                    String ItemID = UtilFunctions.getString(rsTmp,"ITEM_ID","");
                    String POSrNo = UtilFunctions.getString(rsTmp,"SR_NO","");
                    
                    if ((Qty - (NQty+MQty)) > ((Qty * Tolerance_Limit) / 100)) {
                        
                        
                        
                        String str = "SELECT SUM(A.MIRSUM + B.JOBSUM) AS REV_QTY "+
                        "FROM (SELECT IF(SUM(MIRDTL.RECEIVED_QTY-MIRDTL.REJECTED_QTY) IS NULL,0,SUM(MIRDTL.RECEIVED_QTY-MIRDTL.REJECTED_QTY)) AS MIRSUM "+
                        "FROM D_INV_MIR_HEADER MIRHEAD , D_INV_MIR_DETAIL MIRDTL "+
                        "WHERE MIRHEAD.COMPANY_ID = MIRDTL.COMPANY_ID "+
                        "AND MIRHEAD.MIR_NO=MIRDTL.MIR_NO AND MIRHEAD.APPROVED=1 AND MIRHEAD.CANCELLED = 0 "+
                        "AND MIRDTL.PO_SR_NO=" + POSrNo + " " +
                        "AND MIRDTL.PO_NO = '" + PONo + "' AND MIRDTL.ITEM_ID = '"+ ItemID + "') A, "+
                        "(SELECT IF(SUM(JOBDTL.RECEIVED_QTY-JOBDTL.REJECTED_QTY) IS NULL,0,SUM(JOBDTL.RECEIVED_QTY-JOBDTL.REJECTED_QTY)) AS JOBSUM "+
                        "FROM D_INV_JOB_HEADER JOBHEAD , D_INV_JOB_DETAIL JOBDTL "+
                        "WHERE JOBHEAD.COMPANY_ID = JOBDTL.COMPANY_ID "+
                        "AND JOBHEAD.JOB_NO=JOBDTL.JOB_NO "+
                        "AND JOBHEAD.APPROVED=1 AND JOBHEAD.CANCELLED = 0 "+
                        "AND JOBDTL.PO_SR_NO=" + POSrNo + " " +
                        "AND JOBDTL.PO_NO = '" + PONo + "' AND JOBDTL.ITEM_ID = '"+ ItemID + "') B" ;
                        double Rev_Qty = data.getDoubleValueFromDB(str);
                        
                        
                        Bal_Qty=Qty - Rev_Qty;
                        Recd_Qty = Rev_Qty;
                        str = "SELECT SUM(A.MIRSUM + B.JOBSUM) AS QTY " +
                        "FROM (SELECT IF(SUM(MIRDTL.RECEIVED_QTY-MIRDTL.REJECTED_QTY) IS NULL,0,SUM(MIRDTL.RECEIVED_QTY-MIRDTL.REJECTED_QTY)) AS MIRSUM "+
                        "FROM D_INV_MIR_HEADER MIRHEAD , D_INV_MIR_DETAIL MIRDTL "+
                        "WHERE MIRHEAD.COMPANY_ID = MIRDTL.COMPANY_ID "+
                        "AND MIRHEAD.MIR_NO=MIRDTL.MIR_NO "+
                        "AND MIRDTL.PO_NO = '" + PONo + "' "+
                        "AND MIRDTL.PO_SR_NO=" + POSrNo + " " +
                        "AND APPROVED =0  AND CANCELLED=0 "+
                        "AND MIRDTL.ITEM_ID = '"+ ItemID + "' ) A, "+
                        "(SELECT IF(SUM(JOBDTL.RECEIVED_QTY-JOBDTL.REJECTED_QTY) IS NULL,0,SUM(JOBDTL.RECEIVED_QTY-JOBDTL.REJECTED_QTY)) AS JOBSUM "+
                        "FROM D_INV_JOB_HEADER JOBHEAD , D_INV_JOB_DETAIL JOBDTL "+
                        "WHERE JOBHEAD.COMPANY_ID = JOBDTL.COMPANY_ID "+
                        "AND JOBHEAD.JOB_NO=JOBDTL.JOB_NO AND JOBDTL.PO_NO = '" + PONo + "' "+
                        "AND JOBDTL.PO_SR_NO=" + POSrNo + " " +
                        "AND APPROVED =0  AND CANCELLED=0 "+
                        "AND JOBDTL.ITEM_ID = '"+ ItemID + "' ) B";
                        
                        Insp_Qty=data.getDoubleValueFromDB(str);
                        
                        
                        objRow=objReportData.newRow();
                        
                        objRow.setValue("SR_NO",Integer.toString(Counter));
                        objRow.setValue("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                        objRow.setValue("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                        objRow.setValue("DEPT_ID",UtilFunctions.getString(rsTmp,"DEPT_ID",""));
                        objRow.setValue("DEPT_DESC",UtilFunctions.getString(rsTmp,"DEPT_DESC",""));
                        objRow.setValue("USER_ID",UtilFunctions.getString(rsTmp,"USER_ID",""));
                        objRow.setValue("USER_NAME",UtilFunctions.getString(rsTmp,"USER_NAME",""));
                        objRow.setValue("SUPP_ID",UtilFunctions.getString(rsTmp,"SUPP_ID",""));
                        objRow.setValue("SUPP_NAME",UtilFunctions.getString(rsTmp,"SUPP_NAME",""));
                        objRow.setValue("ITEM_ID",UtilFunctions.getString(rsTmp,"ITEM_ID",""));
                        objRow.setValue("ITEM_DESC",UtilFunctions.getString(rsTmp,"ITEM_DESC",""));
                        objRow.setValue("ITEM_DESCRIPTION",UtilFunctions.getString(rsTmp,"ITEM_DESCRIPTION",""));
                        objRow.setValue("BAL",Double.toString(EITLERPGLOBAL.round(Double.parseDouble(UtilFunctions.getString(rsTmp,"BAL","")),2)));
                        objRow.setValue("UNIT_DESC",UtilFunctions.getString(rsTmp,"UNIT_DESC",""));
                        objRow.setValue("PO_SR_NO",UtilFunctions.getString(rsTmp,"SR_NO",""));
                        objRow.setValue("DELIVERY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DELIVERY_DATE","0000-00-00")));
                        objRow.setValue("PURPOSE",UtilFunctions.getString(rsTmp,"PURPOSE",""));
                        objRow.setValue("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                        objRow.setValue("FDATE",txtFromDate.getText().trim());
                        objRow.setValue("TDATE",txtToDate.getText().trim());
                        objRow.setValue("QTY",Double.toString(Qty));
                        objRow.setValue("RECD_QTY",Double.toString(Recd_Qty));
                        objRow.setValue("BAL_QTY",Double.toString(Bal_Qty));
                        objRow.setValue("INSP_QTY",Double.toString(Insp_Qty));
                        objRow.setValue("TOLERANCE_LIMIT",Double.toString(EITLERPGLOBAL.round(UtilFunctions.getDouble(rsTmp,"TOLERANCE_LIMIT",0),2)));
                        
                        objReportData.AddRow(objRow);
                        
                    }
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            String City ="";
            if (Comp_ID == 2) {
                City = "VADODARA";
            }
            else {
                City = "ANKLESHWAR";
            }
            
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("CITY",City);
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPendingPOList1.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }

}


