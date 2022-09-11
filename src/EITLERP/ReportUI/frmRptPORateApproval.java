

package EITLERP.ReportUI;


import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import java.text.*;
import TReportWriter.*;

public class frmRptPORateApproval extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private EITLComboModel cmbMonthModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        
        GenerateCombo();
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
        jLabel3 = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        cmbMonth = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("PO RATE CHANGE APPROVAL DURING THE MONTH");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 350, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Month :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 90, 20);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Year :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(220, 70, 60, 20);

        txtYear.setColumns(10);
        getContentPane().add(txtYear);
        txtYear.setBounds(285, 70, 90, 20);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(140, 160, 130, 25);

        getContentPane().add(cmbMonth);
        cmbMonth.setBounds(120, 70, 90, 24);

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
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        try {
            
            String nMonth = EITLERPGLOBAL.getCombostrCode(cmbMonth);
            
            String strSQL = "";
            
            
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("PO_NO");
            objReportData.AddColumn("PO_DATE");
            objReportData.AddColumn("SUPPLIER_NAME");
            objReportData.AddColumn("ITEM_ID");
            objReportData.AddColumn("ITEM_DESC");
            objReportData.AddColumn("PER_INCRESE");
            objReportData.AddColumn("OLD_RATE");
            objReportData.AddColumn("NEW_RATE");
            objReportData.AddColumn("RATE_DIFF");
            objReportData.AddColumn("ANNUAL_QTY");
            objReportData.AddColumn("ANNUAL_VALUE");
            objReportData.AddColumn("REMARKS");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("PO_NO","");
            objOpeningRow.setValue("PO_DATE","0000-00-00");
            objOpeningRow.setValue("SUPPLIER_NAME","");
            objOpeningRow.setValue("ITEM_ID","");
            objOpeningRow.setValue("ITEM_DESC","");
            objOpeningRow.setValue("PER_INCRESE","");
            objOpeningRow.setValue("OLD_RATE","");
            objOpeningRow.setValue("NEW_RATE","");
            objOpeningRow.setValue("RATE_DIFF","");
            objOpeningRow.setValue("ANNUAL_QTY","");
            objOpeningRow.setValue("ANNUAL_VALUE","");
            objOpeningRow.setValue("REMARKS","");
            
            
            String FromDate =  txtYear.getText().trim()+"-"+nMonth+"-01";
            String ToDate   =  data.getStringValueFromDB("SELECT LAST_DAY('"+FromDate+"') FROM DUAL");
            
            
            strSQL ="SELECT A.PO_NO,A.PO_DATE,A.SUPP_NAME, B.ITEM_ID,B.ITEM_DESC, D.RATE_DIFFERENCE_PER, D.LAST_PO_RATE, D.CURRENT_RATE, D.RATE_DIFFERENCE_RATE, C.REMARKS" +
            " FROM D_PUR_PO_HEADER A, D_PUR_PO_DETAIL B, D_PUR_RATE_APPROVAL_HEADER C ,D_PUR_RATE_APPROVAL_DETAIL D" +
            " WHERE A.PO_DATE>='"+FromDate+"' AND A.PO_DATE<='"+ToDate+"' AND A.PO_NO=B.PO_NO AND A.COMPANY_ID=B.COMPANY_ID AND C.COMPANY_ID=D.COMPANY_ID" +
            " AND A.COMPANY_ID=C.COMPANY_ID AND B.INDENT_NO=C.INDENT_NO AND C.APPROVAL_NO=D.APPROVAL_NO AND B.ITEM_ID=D.ITEM_ID" +
            " AND A.COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" AND A.APPROVED=1 AND A.CANCELLED=0  AND C.APPROVED=1 AND C.CANCELLED=0  AND D.LAST_PO_RATE>0 AND A.PO_TYPE=1 ORDER BY A.PO_DATE";
            
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objRow.setValue("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objRow.setValue("SUPPLIER_NAME",UtilFunctions.getString(rsTmp,"SUPP_NAME",""));
                    objRow.setValue("ITEM_ID",UtilFunctions.getString(rsTmp,"ITEM_ID",""));
                    String ItemID = UtilFunctions.getString(rsTmp,"ITEM_ID","");
                    String ItemName=clsItem.getItemName(EITLERPGLOBAL.gCompanyID, ItemID);
                    objRow.setValue("ITEM_DESC",ItemName);
                    objRow.setValue("PER_INCRESE",Double.toString(UtilFunctions.getDouble(rsTmp,"RATE_DIFFERENCE_PER",0)));
                    objRow.setValue("OLD_RATE",Double.toString(UtilFunctions.getDouble(rsTmp,"LAST_PO_RATE",0)));
                    objRow.setValue("NEW_RATE",Double.toString(UtilFunctions.getDouble(rsTmp,"CURRENT_RATE",0)));
                    objRow.setValue("RATE_DIFF",Double.toString(UtilFunctions.getDouble(rsTmp,"RATE_DIFFERENCE_RATE",0)));
                    
                    String pFromDate = clsCalcInterest.addMonthToDate(FromDate, -12);
                    String pToDate = clsDepositMaster.deductDays(FromDate,1);
                    
                    //String ItemID =UtilFunctions.getString(rsTmp,"ITEM_ID","");
                    String SQLQuery ="SELECT SUM(B.QTY) AS QTY,SUM(B.ISSUE_VALUE) AS ISSUE_VALUE FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_DETAIL B" +
                    " WHERE A.APPROVED =1 AND A.CANCELED =0 AND A.ISSUE_NO =B.ISSUE_NO AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID +
                    " AND A.ISSUE_DATE >='"+pFromDate+"' AND A.ISSUE_DATE <='"+pToDate+"' AND B.ITEM_CODE='"+ItemID+"'";
                    
                    ResultSet rsResult = data.getResult(SQLQuery);
                    rsResult.first();
                    if(rsResult.getRow() > 0) {
                        objRow.setValue("ANNUAL_QTY",Double.toString(UtilFunctions.getDouble(rsResult,"QTY",0)));
                        objRow.setValue("ANNUAL_VALUE",Double.toString(UtilFunctions.getDouble(rsResult,"ISSUE_VALUE",0)));
                    } else {
                        objRow.setValue("ANNUAL_QTY","0");
                        objRow.setValue("ANNUAL_VALUE","0");
                    }
                    objRow.setValue("REMARKS","REMARKS : " + UtilFunctions.getString(rsTmp,"REMARKS",""));
                    
                    rsResult.close();
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            
            HashMap Parameters=new HashMap();
            //Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("FROM_DATE",EITLERPGLOBAL.formatDate(FromDate));
            Parameters.put("TO_DATE",EITLERPGLOBAL.formatDate(ToDate));
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptPORateApproval.rpt",Parameters,objReportData);
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void GenerateCombo() {
        
        //--- Generate Type Combo ------//
        ComboData aData=new ComboData();
        
        cmbMonthModel=new EITLComboModel();
        cmbMonth.removeAllItems();
        cmbMonth.setModel(cmbMonthModel);
        
        aData=new ComboData();
        aData.strCode="01";
        aData.Text="January";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="02";
        aData.Text="February";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="03";
        aData.Text="March";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="04";
        aData.Text="April";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="05";
        aData.Text="May";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="06";
        aData.Text="June";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="07";
        aData.Text="July";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="08";
        aData.Text="August";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="09";
        aData.Text="September";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="10";
        aData.Text="October";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="11";
        aData.Text="November";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="12";
        aData.Text="December";
        cmbMonthModel.addElement(aData);
        
        
    }
    
    
    private boolean Validate() {
        //Form level validations
        
        if(txtYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Year");
            return false;
        } else if(txtYear.getText().trim().length() != 4 ) {
            
            JOptionPane.showMessageDialog(null,"Invalid Year in YYYY format.");
            return false;
        }
        
        return true;
    }
    
    
}