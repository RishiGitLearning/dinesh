

package EITLERP.Finance.ReportsUI;


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

public class frmRptInterestStatementWithMICRno extends javax.swing.JApplet {
    
    //private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbMonthModel;
    private EITLComboModel cmbConditionModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(425,300);
        initComponents();
        GenerateCombo();
        lblEffectivefrom.setVisible(false);
        lblEffectiveless.setVisible(false);
        lblEffectiveto.setVisible(false);
        txtFromDate.setVisible(false);
        txtToDate.setVisible(false);
        cmbCondition.setEnabled(false);
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
        jLabel4 = new javax.swing.JLabel();
        txtaccountno = new javax.swing.JTextField();
        lblCondition = new javax.swing.JLabel();
        cmbCondition = new javax.swing.JComboBox();
        lblEffectivefrom = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        lblEffectiveto = new javax.swing.JLabel();
        chkOnlyMaturityWarrant = new javax.swing.JCheckBox();
        lblEffectiveless = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("INTEREST STATEMENT WITH MICR NO.");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 350, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Month :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(5, 50, 50, 15);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Year :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(200, 50, 40, 15);

        txtYear.setColumns(10);
        getContentPane().add(txtYear);
        txtYear.setBounds(250, 48, 90, 20);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(110, 240, 130, 25);

        getContentPane().add(cmbMonth);
        cmbMonth.setBounds(60, 45, 120, 24);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Current A/C No :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(0, 80, 107, 20);

        txtaccountno.setColumns(10);
        getContentPane().add(txtaccountno);
        txtaccountno.setBounds(111, 82, 230, 20);

        lblCondition.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCondition.setText("Condition :");
        getContentPane().add(lblCondition);
        lblCondition.setBounds(10, 150, 75, 15);

        cmbCondition.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbConditionItemStateChanged(evt);
            }
        });

        getContentPane().add(cmbCondition);
        cmbCondition.setBounds(95, 145, 245, 22);

        lblEffectivefrom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffectivefrom.setText("Effective date from :");
        getContentPane().add(lblEffectivefrom);
        lblEffectivefrom.setBounds(20, 180, 163, 15);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(190, 180, 100, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(190, 210, 100, 20);

        lblEffectiveto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffectiveto.setText("Effective date to :");
        getContentPane().add(lblEffectiveto);
        lblEffectiveto.setBounds(20, 210, 163, 17);

        chkOnlyMaturityWarrant.setSelected(true);
        chkOnlyMaturityWarrant.setText("Only Maturity Warrant");
        chkOnlyMaturityWarrant.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkOnlyMaturityWarrantItemStateChanged(evt);
            }
        });

        getContentPane().add(chkOnlyMaturityWarrant);
        chkOnlyMaturityWarrant.setBounds(90, 110, 171, 19);

        lblEffectiveless.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEffectiveless.setText("Effective date less then :");
        getContentPane().add(lblEffectiveless);
        lblEffectiveless.setBounds(20, 180, 163, 15);

    }//GEN-END:initComponents

    private void chkOnlyMaturityWarrantItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkOnlyMaturityWarrantItemStateChanged
        // TODO add your handling code here:
        if(chkOnlyMaturityWarrant.isSelected()) {
            lblEffectivefrom.setVisible(false);
            lblEffectiveless.setVisible(false);
            lblEffectiveto.setVisible(false);
            txtFromDate.setVisible(false);
            txtToDate.setVisible(false);
            txtFromDate.setText("");
            txtToDate.setText("");
            cmbCondition.setEnabled(false);
            cmbCondition.setSelectedIndex(0);
        } else {
            cmbCondition.setSelectedIndex(0);
            cmbCondition.setEnabled(true);
            txtFromDate.setText("");
            txtToDate.setText("");
        }
    }//GEN-LAST:event_chkOnlyMaturityWarrantItemStateChanged

    private void cmbConditionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbConditionItemStateChanged
        // TODO add your handling code here:
        if(cmbCondition.getSelectedIndex()==0) {
            lblEffectivefrom.setVisible(false);
            lblEffectiveto.setVisible(false);
            lblEffectiveless.setVisible(false);
            txtFromDate.setVisible(false);
            txtToDate.setVisible(false);
            txtFromDate.setText("");
            txtToDate.setText("");
        } else if(cmbCondition.getSelectedIndex()==1) {
            lblEffectivefrom.setVisible(false);
            lblEffectiveto.setVisible(false);
            lblEffectiveless.setVisible(true);
            txtFromDate.setVisible(true);
            txtToDate.setVisible(false);
            txtFromDate.setText("");
            txtToDate.setText("");
        } else if(cmbCondition.getSelectedIndex()==2) {
            lblEffectivefrom.setVisible(true);
            lblEffectiveto.setVisible(true);
            lblEffectiveless.setVisible(false);
            txtFromDate.setVisible(true);
            txtToDate.setVisible(true);
            txtFromDate.setText("");
            txtToDate.setText("");
        }
    }//GEN-LAST:event_cmbConditionItemStateChanged
    
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
    private javax.swing.JCheckBox chkOnlyMaturityWarrant;
    private javax.swing.JComboBox cmbCondition;
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblCondition;
    private javax.swing.JLabel lblEffectivefrom;
    private javax.swing.JLabel lblEffectiveless;
    private javax.swing.JLabel lblEffectiveto;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtYear;
    private javax.swing.JTextField txtaccountno;
    // End of variables declaration//GEN-END:variables
    
    
    private void GenerateReport() {
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("DEPOSITORS_NAME");
            objReportData.AddColumn("WARRANT_NO");
            objReportData.AddColumn("MICR_NO");
            objReportData.AddColumn("NET_INTEREST");
            objReportData.AddColumn("WARRANT_DATE");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("DEPOSITORS_NAME","");
            objOpeningRow.setValue("WARRANT_NO","");
            objOpeningRow.setValue("MICR_NO","");
            objOpeningRow.setValue("PARTY_CODE","");
            objOpeningRow.setValue("WARRANT_DATE","0000-00-00");
            
            //int ReceiptType = cmbReceiptType.getSelectedIndex() + 1;
            int nMonth = cmbMonth.getSelectedIndex();
            String strSQL = "";
            String Condition="";
            String StartDate = txtYear.getText().trim()+"-"+EITLERPGLOBAL.padLeft(2, Integer.toString(nMonth),"0")+"-01";
            String EndDate = data.getStringValueFromDB("SELECT LAST_DAY('"+StartDate+"') FROM DUAL");
            
            strSQL= "SELECT INTCALCDTL.RECEIPT_NO,INTCALCDTL.PARTY_CODE,DMST.APPLICANT_NAME,INTCALCDTL.WARRANT_NO,INTCALCDTL.LEGACY_WARRANT_NO," +
            "INTCALCDTL.MICR_NO,INTCALCDTL.NET_INTEREST,INTCALCDTL.WARRANT_DATE,DMST.DEPOSIT_TYPE_ID,DMST.MATURITY_DATE "+
            "FROM D_FD_INT_CALC_DETAIL INTCALCDTL,D_FD_DEPOSIT_MASTER DMST "+
            "WHERE INTCALCDTL.PARTY_CODE = DMST.PARTY_CODE AND DMST.COMPANY_ID= " + EITLERPGLOBAL.gCompanyID + " "+
            "AND DMST.CANCELLED=0 AND DMST.APPROVED=1 AND INTCALCDTL.RECEIPT_NO = DMST.RECEIPT_NO AND MONTH(INTCALCDTL.WARRANT_DATE) = "+ nMonth +" "+
            "AND YEAR(INTCALCDTL.WARRANT_DATE) =  "+ txtYear.getText().trim() +" AND INTCALCDTL.WARRANT_NO<>'0000000' " ;
            
            if(chkOnlyMaturityWarrant.isSelected() && !cmbCondition.isEnabled()) {
                Condition += "AND DMST.MATURITY_DATE>='"+StartDate+"' AND DMST.MATURITY_DATE<='"+EndDate+"' ";
            } else {
                if(!txtFromDate.getText().trim().equals("")) {
                    if(lblEffectiveless.isVisible()) {
                        Condition+="AND DMST.RECEIPT_DATE<='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
                    } else {
                        Condition+="AND DMST.RECEIPT_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
                    }
                }
                if(!txtToDate.getText().trim().equals("")) {
                    Condition+="AND DMST.RECEIPT_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
                }
                Condition += "AND DMST.MATURITY_DATE>='"+EITLERPGLOBAL.addDaysToDate(EndDate,1,"yyyy-MM-dd")+"' ";
            }
            
            strSQL += Condition + "ORDER BY INTCALCDTL.LEGACY_WARRANT_NO ";
            
            System.out.println(strSQL);
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("DEPOSITORS_NAME",UtilFunctions.getString(rsTmp,"APPLICANT_NAME",""));
                    objRow.setValue("WARRANT_NO",UtilFunctions.getString(rsTmp,"LEGACY_WARRANT_NO",""));
                    objRow.setValue("MICR_NO",UtilFunctions.getString(rsTmp,"MICR_NO",""));
                    int DepsoitTypeID = UtilFunctions.getInt(rsTmp,"DEPOSIT_TYPE_ID",0);
                    
                    String WarrantDate = UtilFunctions.getString(rsTmp,"WARRANT_DATE","0000-00-00");
                    String MaturityDate = UtilFunctions.getString(rsTmp,"MATURITY_DATE","0000-00-00");
                    if(DepsoitTypeID!=2) {
                        if (java.sql.Date.valueOf(WarrantDate).compareTo(java.sql.Date.valueOf(MaturityDate))!=0) {
                            WarrantDate = WarrantDate;
                        } else {
                            WarrantDate = clsDepositMaster.deductDays(WarrantDate, 1);
                        }
                    } 
                    
                    
                    objRow.setValue("WARRANT_DATE", EITLERPGLOBAL.formatDate(WarrantDate));
                    String ReceiptNo = rsTmp.getString("RECEIPT_NO");
                    String PartyCode = rsTmp.getString("PARTY_CODE");
                    int SchemeType = data.getIntValueFromDB("SELECT B.SCHEME_TYPE FROM D_FD_DEPOSIT_MASTER A,D_FD_SCHEME_MASTER B WHERE A.SCHEME_ID=B.SCHEME_ID AND A.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                    double netInterest = 0;
                    if(SchemeType==3) {
                        double grossInterest = data.getDoubleValueFromDB("SELECT SUM(A.INTEREST_AMOUNT) FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                        double TDSAmount = data.getDoubleValueFromDB("SELECT SUM(A.TDS_AMOUNT) FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO ",FinanceGlobal.FinURL);
                        netInterest = EITLERPGLOBAL.round(grossInterest - TDSAmount,2);
                    } else {
                        netInterest = UtilFunctions.getDouble(rsTmp,"NET_INTEREST",0);
                    }
                    objRow.setValue("NET_INTEREST",Double.toString(netInterest));
                    objReportData.AddRow(objRow);
                    System.out.println(Counter + "~" + UtilFunctions.getString(rsTmp,"APPLICANT_NAME","") + "~" + UtilFunctions.getString(rsTmp,"LEGACY_WARRANT_NO","") + "~" + UtilFunctions.getString(rsTmp,"MICR_NO","") + "~" + Double.toString(netInterest) +"~"+ EITLERPGLOBAL.formatDate(WarrantDate));
                    rsTmp.next();
                }
            }
            
            
            String OnDate =  cmbMonth.getSelectedItem().toString().toUpperCase() + "'" + txtYear.getText().trim();
            
            HashMap Parameters=new HashMap();
            
            Parameters.put("ON_DATE",OnDate);
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("AC_NO",txtaccountno.getText().trim());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptInterestStatement.rpt",Parameters,objReportData);
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
        aData.Code=0;
        aData.Text="Select Month";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="January";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="February";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="March";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=4;
        aData.Text="April";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=5;
        aData.Text="May";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=6;
        aData.Text="June";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=7;
        aData.Text="July";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=8;
        aData.Text="August";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=9;
        aData.Text="September";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=10;
        aData.Text="October";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=11;
        aData.Text="November";
        cmbMonthModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=12;
        aData.Text="December";
        cmbMonthModel.addElement(aData);
        
        
        
//        cmbReceiptTypeModel=new EITLComboModel();
//        //        cmbReceiptType.removeAllItems();
//        //      cmbReceiptType.setModel(cmbReceiptTypeModel);
//        
//        aData=new ComboData();
//        aData.Code=1;
//        aData.Text="FD TO FD";
//        cmbReceiptTypeModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.Code=2;
//        aData.Text="CD TO CD";
//        cmbReceiptTypeModel.addElement(aData);
//        
//        aData=new ComboData();
//        aData.Code=3;
//        aData.Text="LD TO LD";
//        cmbReceiptTypeModel.addElement(aData);
        
        cmbConditionModel=new EITLComboModel();
        cmbCondition.removeAllItems();
        cmbCondition.setModel(cmbConditionModel);
        
        aData=new ComboData();
        aData.Code=0;
        aData.Text="Select Condition";
        cmbConditionModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="Effective Date less then";
        cmbConditionModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="Effective Date between";
        cmbConditionModel.addElement(aData);
        
    }
    
    private boolean Validate() {
        //Form level validations
        
        if(txtYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Year.");
            return false;
        } else if(txtYear.getText().trim().length() != 4 ) {
            
            JOptionPane.showMessageDialog(null,"Invalid Year in YYYY format.");
            return false;
        }
        if(txtaccountno.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enetere the A/C No.");
            return false;
            
        }
        
        if(cmbMonth.getSelectedIndex()==0 ) {
            JOptionPane.showMessageDialog(this,"Please select Month & Condition...");
            return false;
        }
        
        if(cmbCondition.isEnabled()) {
            if(cmbCondition.getSelectedIndex()==0) {
                JOptionPane.showMessageDialog(this,"Please select Month & Condition...");
                return false;
            }
            
            if(cmbCondition.getSelectedIndex()==1) {
                if(txtFromDate.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(this,"Please insert effective date less then...");
                    return false;
                } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText().trim())) {
                    JOptionPane.showMessageDialog(this,"Please insert proper effective date less then...");
                    return false;
                }
            }
            
            if(cmbCondition.getSelectedIndex()==2) {
                if(txtFromDate.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(this,"Please insert effective date from...");
                    return false;
                } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText().trim())) {
                    JOptionPane.showMessageDialog(this,"Please insert proper effective date from...");
                    return false;
                }
                
                if(txtToDate.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(this,"Please insert effective date to...");
                    return false;
                } else if(!EITLERPGLOBAL.isDate(txtToDate.getText().trim())) {
                    JOptionPane.showMessageDialog(this,"Please insert proper effective date to...");
                    return false;
                }
            }
        }
        return true;
    }
}
