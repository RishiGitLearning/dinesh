/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.Production.FeltDiscRateMaster;

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
import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URL;
import javax.swing.JOptionPane;
import java.io.*;
import java.sql.ResultSet;
import javax.swing.JTable;
import EITLERP.Production.ReportUI.JTextFieldHint;


public class frmPartySanc extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    private EITLTableCellRenderer RowFormat=new EITLTableCellRenderer();
    private clsExcelExporter exp = new clsExcelExporter();
    EITLTableModel DataModel= new EITLTableModel();
    //  private TReportEngine objEngine=new TReportEngine();
    
    /** Initializes the applet frmRptGRNInfo */
    
    public void init() {
        setSize(800,600);
        initComponents();
        formatGrid()  ;   // TODO add your handling code here:
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnparty = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TResult = new javax.swing.JTable();
        btnxls = new javax.swing.JButton();
        btnall = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtPartycode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setName("PARTYWISE SANCTION REPORT");
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("PARTYWISE SANCTION REPORT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(10, 5, 330, 20);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 740, 30);

        btnparty.setText("Show Selected Party Sanction ");
        btnparty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpartyActionPerformed(evt);
            }
        });

        getContentPane().add(btnparty);
        btnparty.setBounds(40, 100, 300, 25);

        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        TResult.setModel(new javax.swing.table.DefaultTableModel(
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
        TResult.getTableHeader().setFont(new Font("Plain", Font.BOLD, 12));
        jScrollPane1.setViewportView(TResult);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 140, 700, 290);

        btnxls.setText("Excel To Export");
        btnxls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxlsActionPerformed(evt);
            }
        });

        getContentPane().add(btnxls);
        btnxls.setBounds(20, 460, 140, 25);

        btnall.setText("Show All Parties Sanction ");
        btnall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnallActionPerformed(evt);
            }
        });

        getContentPane().add(btnall);
        btnall.setBounds(400, 100, 300, 25);

        jLabel8.setText("Party Code");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(20, 40, 80, 20);

        txtPartycode.setEditable(false);
        txtPartycode.setEnabled(false);
        txtPartycode = new JTextFieldHint(new JTextField(),"Search By F1");
        txtPartycode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartycodeFocusLost(evt);
            }
        });
        txtPartycode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartycodeKeyPressed(evt);
            }
        });

        getContentPane().add(txtPartycode);
        txtPartycode.setBounds(100, 40, 110, 20);

        jLabel3.setText("From Date");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(260, 40, 80, 20);

        jLabel2.setText("To Date");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(260, 70, 80, 20);

        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(340, 40, 110, 20);

        getContentPane().add(txtToDate);
        txtToDate.setBounds(340, 70, 110, 20);

    }//GEN-END:initComponents
    
    private void txtPartycodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartycodeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' ORDER BY PARTY_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            //aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtPartycode.setText(aList.ReturnVal);
                //txtPartyname.setText(clsFeltparty.getParyName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                //txtPartyname.setText(aList.ReturnVal);
                //System.out.println(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                //txtPartystation.setText(clsFeltparty.getStation(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                
            }
        }
    }//GEN-LAST:event_txtPartycodeKeyPressed
    
    private void txtPartycodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartycodeFocusLost
        // TODO add your handling code here:
        try{
            if(!txtPartycode.getText().equals("")){
                String strSQL="";
                ResultSet rsTmp;
                strSQL= "";
                //strSQL+="SELECT NAME,AD1,AD2,STATION,CHG_IND_2,TRANS_CD,INS_IND FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE = "+txtPartycode.getText().trim()+"";
                //strSQL+="SELECT PARTY_NAME,DISPATCH_STATION,CONTACT_PERSON FROM (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS FPM LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PARTY_EXTRA_INFO) AS FPEI ON FPM.PARTY_CODE=FPEI.PARTY_CODE WHERE FPM.PARTY_CODE="+txtPartycode.getText().trim()+"";
                strSQL+="SELECT PARTY_CODE,PARTY_NAME,DISPATCH_STATION FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE="+txtPartycode.getText().trim()+"";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                txtPartycode.setText(rsTmp.getString("PARTY_CODE"));
                //txtPartyname.setText(rsTmp.getString("PARTY_NAME"));
                //txtPartystation.setText(rsTmp.getString("DISPATCH_STATION"));
                //txtContact.setText(rsTmp.getString("CONTACT_PERSON"));
            }
        }
        catch(Exception e){
            
        }
    }//GEN-LAST:event_txtPartycodeFocusLost
    
    private void btnallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnallActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        GenerateForAll();
    }//GEN-LAST:event_btnallActionPerformed
    
    private void btnxlsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxlsActionPerformed
        try{
            
            exp.fillData(TResult,new File("/root/Desktop/PartySanc_Report.xls"));
            //exp.fillData(TableI,new File("C://ProductionProcess.xls"));
            exp.fillData(TResult,new File("D://PartySanc_Report.xls"));
            JOptionPane.showMessageDialog(null, "Data saved at " +
            //"'C: \\ result.xls' successfully", "Message",
            "'/root/Desktop/PartySanc_Report.xls' successfully in Linux PC or 'D://PartySanc_Report.xls' successfully in Windows PC    ", "Message",
            JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnxlsActionPerformed
    
    private void btnpartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpartyActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        GenerateReport();
    }//GEN-LAST:event_btnpartyActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TResult;
    private javax.swing.JButton btnall;
    private javax.swing.JButton btnparty;
    private javax.swing.JButton btnxls;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtPartycode;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        String condition="";
        
        if (txtPartycode.getText().trim().equalsIgnoreCase("")) {
            condition +="";
            JOptionPane.showMessageDialog(null,"Select PARTY CODE from list");
            txtPartycode.requestFocus();
            return;
        }
        else {
            condition +=" AND H.PARTY_CODE='" + txtPartycode.getText() + "' ";
        }
        if (txtFromDate.getText().trim().length() > 0) {
            condition += " AND H.SANCTION_DATE>='" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "'";
        }
        if (txtToDate.getText().trim().length() > 0) {
            condition += " AND H.SANCTION_DATE<='" + EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) + "'";
        }
        
        formatGrid()  ;
        
        try {
            
            //String strSQL="SELECT * FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' ";
            String strSQL="";
            
            //strSQL = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,H.EFFECTIVE_FROM,H.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD H,PRODUCTION.FELT_RATE_DISC_MASTER D WHERE H.MASTER_NO=D.MASTER_NO AND H.PARTY_CODE=D.PARTY_CODE AND H.PARTY_CODE='814024' AND H.SANCTION_DATE BETWEEN '2015-06-22' AND '2017-03-31'";
            strSQL = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO AND H.PARTY_CODE=D.PARTY_CODE AND D.APPROVAL=1 AND D.CANCELED=0 "+ condition + " ORDER BY H.PARTY_CODE,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.PRODUCT_CODE";
            System.out.println(strSQL);
            
            //String strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SEAM_CHARGE AS INV_SEAM_CHARGE,D.SEAM_VALUE AS SANC_SEAM_CHARGE,ROUND((D.SEAM_VALUE-F.SEAM_CHARGE),2) AS WORK_SEAM_CHARGE,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.TRD_DISCOUNT<((F.GROSS_AMOUNT*D.DISC_PER)/100) AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)";
            
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    
                    Object[] rowData=new Object[20];
                    
                    rowData[0]=UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                    rowData[1]=UtilFunctions.getString(rsTmp,"PARTY_NAME","");
                    rowData[2]=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"SANCTION_DATE",""));
                    rowData[3]=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"EFFECTIVE_FROM",""));
                    rowData[4]=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"EFFECTIVE_TO",""));
                    rowData[5]=UtilFunctions.getString(rsTmp,"PRODUCT_CODE","");
                    rowData[6]=UtilFunctions.getString(rsTmp,"PIECE_NO","");
                    rowData[7]=UtilFunctions.getString(rsTmp,"MACHINE_NO","");
                    rowData[8]=UtilFunctions.getString(rsTmp,"MACHINE_POSITION","");
                    rowData[9]=UtilFunctions.getString(rsTmp,"TURN_OVER_TARGET","");
                    rowData[10]=UtilFunctions.getString(rsTmp,"DISC_PER","");
                    rowData[11]=UtilFunctions.getString(rsTmp,"YRED_DISC_PER","");
                    rowData[12]=UtilFunctions.getString(rsTmp,"SEAM_VALUE","");
                    rowData[13]=UtilFunctions.getString(rsTmp,"YRED_SEAM_VALUE","");
                    
                    DataModel.addRow(rowData);
                    TResult.changeSelection(TResult.getRowCount() - 1, 1, false, false);
                    TResult.requestFocus();
                    
                    rsTmp.next();
                    
                }
            }
            
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
        
    private void GenerateForAll() {
        String condition="";
        
        if (txtFromDate.getText().trim().length() > 0) {
            condition += " AND H.SANCTION_DATE>='" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) + "'";
        }
        if (txtToDate.getText().trim().length() > 0) {
            condition += " AND H.SANCTION_DATE<='" + EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) + "'";
        }
        
        formatGrid()  ;
        
        try {
            
            //String strSQL="SELECT * FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' ";
            String strSQL="";
            
            //strSQL = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,H.EFFECTIVE_FROM,H.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEAD H,PRODUCTION.FELT_RATE_DISC_MASTER D WHERE H.MASTER_NO=D.MASTER_NO AND H.PARTY_CODE=D.PARTY_CODE AND H.PARTY_CODE='814024' AND H.SANCTION_DATE BETWEEN '2015-06-22' AND '2017-03-31'";
            strSQL = "SELECT H.PARTY_CODE,H.PARTY_NAME,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.EFFECTIVE_TO,D.PRODUCT_CODE,D.PIECE_NO,D.MACHINE_NO,D.MACHINE_POSITION,D.TURN_OVER_TARGET,D.DISC_PER,D.YRED_DISC_PER,D.SEAM_VALUE,D.YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H,PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D WHERE H.MASTER_NO=D.MASTER_NO AND H.PARTY_CODE=D.PARTY_CODE AND D.APPROVED=1 AND D.CANCELED=0 "+ condition + " ORDER BY H.PARTY_CODE,H.SANCTION_DATE,D.EFFECTIVE_FROM,D.PRODUCT_CODE";
            System.out.println(strSQL);
            
            //String strSQL="SELECT F.PARTY_NAME,F.PARTY_CODE,F.PIECE_NO,F.QUALITY_NO,F.INVOICE_NO,F.INVOICE_DATE,F.GROSS_KG,F.GROSS_SQ_MTR,F.WIDTH,F.LENGTH,F.RATE,F.GROSS_AMOUNT,ROUND((F.TRD_DISCOUNT*100/F.GROSS_AMOUNT),2) AS INV_DISC_PER,ROUND(D.DISC_PER,2) AS SANC_DISC_PER,ROUND((D.DISC_PER-(F.TRD_DISCOUNT*100/F.GROSS_AMOUNT)),2) AS WORK_DISC_PER,F.SEAM_CHARGE AS INV_SEAM_CHARGE,D.SEAM_VALUE AS SANC_SEAM_CHARGE,ROUND((D.SEAM_VALUE-F.SEAM_CHARGE),2) AS WORK_SEAM_CHARGE,ROUND((F.GROSS_AMOUNT*D.DISC_PER)/100-F.TRD_DISCOUNT,2) AS DISC_AMT  FROM PRODUCTION.FELT_INVOICE_DATA F LEFT JOIN PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL D ON F.QUALITY_NO=D.PRODUCT_CODE WHERE F.INVOICE_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtfromdate.getText())+"' AND F.INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txttodate.getText())+"' AND F.TRD_DISCOUNT<((F.GROSS_AMOUNT*D.DISC_PER)/100) AND F.PARTY_CODE IN (SELECT H.PARTY_CODE FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER H WHERE H.MASTER_NO=D.MASTER_NO) AND F.INVOICE_NO NOT IN (SELECT U.INVOICE_NO FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL U GROUP BY U.INVOICE_NO,U.PIECE_NO)";
            
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    
                    Object[] rowData=new Object[20];
                    
                    rowData[0]=UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                    rowData[1]=UtilFunctions.getString(rsTmp,"PARTY_NAME","");
                    rowData[2]=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"SANCTION_DATE",""));
                    rowData[3]=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"EFFECTIVE_FROM",""));
                    rowData[4]=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"EFFECTIVE_TO",""));
                    rowData[5]=UtilFunctions.getString(rsTmp,"PRODUCT_CODE","");
                    rowData[6]=UtilFunctions.getString(rsTmp,"PIECE_NO","");
                    rowData[7]=UtilFunctions.getString(rsTmp,"MACHINE_NO","");
                    rowData[8]=UtilFunctions.getString(rsTmp,"MACHINE_POSITION","");
                    rowData[9]=UtilFunctions.getString(rsTmp,"TURN_OVER_TARGET","");
                    rowData[10]=UtilFunctions.getString(rsTmp,"DISC_PER","");
                    rowData[11]=UtilFunctions.getString(rsTmp,"YRED_DISC_PER","");
                    rowData[12]=UtilFunctions.getString(rsTmp,"SEAM_VALUE","");
                    rowData[13]=UtilFunctions.getString(rsTmp,"YRED_SEAM_VALUE","");
                    
                    DataModel.addRow(rowData);
                    TResult.changeSelection(TResult.getRowCount() - 1, 1, false, false);
                    TResult.requestFocus();
                    
                    rsTmp.next();
                    
                }
            }
            
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
        
    private void formatGrid() {
        try {
            // objRow=objReportData.newRow();
            DataModel=new EITLTableModel();
            TResult.removeAll();
            TResult.setModel(DataModel);
            TResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.LIGHT_GRAY);
            
            
            DataModel.addColumn("Party Code"); //0
            DataModel.addColumn("Party Name"); //1
            DataModel.addColumn("Sanction Date");//2
            DataModel.addColumn("Effective From");//3
            DataModel.addColumn("Effective To");//4
            DataModel.addColumn("Product Code");//5
            DataModel.addColumn("Piece No"); //6
            DataModel.addColumn("Machine No");//7
            DataModel.addColumn("Machine Position");//8
            DataModel.addColumn("TurnOver Target");//9
            DataModel.addColumn("Disc %"); //10
            DataModel.addColumn("Yearend %");//11
            DataModel.addColumn("Seam Rate"); //12
            DataModel.addColumn("Yearend Seam Rate");//13
            
            DataModel.SetVariable(0,"PARTY_CODE");  //0
            DataModel.SetVariable(1,"PARTY_NAME"); //1
            DataModel.SetVariable(2,"SANCTION_DATE"); //1
            DataModel.SetVariable(3,"EFFECTIVE_FROM"); //1
            DataModel.SetVariable(4,"EFFECTIVE_TO"); //1
            DataModel.SetVariable(5,"PRODUCT_CODE"); //1
            DataModel.SetVariable(6,"PIECE_NO"); //1
            DataModel.SetVariable(7,"MACHINE_NO"); //2
            DataModel.SetVariable(8,"MACHINE_POSITION"); //3
            DataModel.SetVariable(9,"TURN_OVER_TARGET"); //8
            DataModel.SetVariable(10,"DISC_PER"); //4
            DataModel.SetVariable(11,"YRED_DISC_PER"); //6
            DataModel.SetVariable(12,"SEAM_VALUE"); //5
            DataModel.SetVariable(13,"YRED_SEAM_VALUE"); //7
            
            
            DataModel.SetReadOnly(0);
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
            
            
            
            for(int j=0;j<TResult.getColumnCount();j++) {
                TResult.getColumnModel().getColumn(j).setCellRenderer(Renderer);
            }
            
        }
        catch(Exception e) {
            
        }
        
    }
        
    private boolean Validate() {
        //Form level validations
        
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter To date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
    
}