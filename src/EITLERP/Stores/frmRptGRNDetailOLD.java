

package EITLERP.Stores;


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

public class frmRptGRNDetailOLD extends javax.swing.JApplet {
    
    private EITLComboModel cmbTypeModel=new EITLComboModel();
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        GenerateCombo();
        Bar.setVisible(false);
        lblBar.setVisible(false);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblToDate = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        lblFromDate = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        lblBar = new javax.swing.JLabel();
        Bar = new javax.swing.JProgressBar();
        cmbType = new javax.swing.JComboBox();
        lblType = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("INTEREST PROVISION FOR CUMULATIVE DEPOSIT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 350, 14);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        lblToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDate.setText("To Date :");
        getContentPane().add(lblToDate);
        lblToDate.setBounds(220, 73, 60, 15);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(285, 70, 90, 20);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(160, 100, 130, 23);

        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("From Date :");
        getContentPane().add(lblFromDate);
        lblFromDate.setBounds(35, 73, 70, 15);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(110, 70, 90, 20);

        lblBar.setText("...");
        getContentPane().add(lblBar);
        lblBar.setBounds(10, 130, 200, 14);

        getContentPane().add(Bar);
        Bar.setBounds(10, 150, 200, 18);

        cmbType.setAutoscrolls(true);
        getContentPane().add(cmbType);
        cmbType.setBounds(110, 40, 190, 22);

        lblType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblType.setText("Type :");
        getContentPane().add(lblType);
        lblType.setBounds(65, 43, 40, 15);

    }//GEN-END:initComponents
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        }
        GenerateReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar Bar;
    private javax.swing.JComboBox cmbType;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBar;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JLabel lblType;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        new Thread(){
            
            public void run(){
                
                try {
                    String strSQL = "";
                    String FromDate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim());
                    String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
                    int GRNType = EITLERPGLOBAL.getComboCode(cmbType);
                    
                    TReportWriter.SimpleDataProvider.TRow objRow;
                    TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
                    objReportData.AddColumn("GRN_NO");
                    objReportData.AddColumn("GR0SS_AMOUNT");
                    objReportData.AddColumn("NET_AMOUNT");
                    objReportData.AddColumn("DISCOUNT");
                    objReportData.AddColumn("PF");
                    objReportData.AddColumn("EXCISE");
                    objReportData.AddColumn("CESS");
                    objReportData.AddColumn("ST");
                    objReportData.AddColumn("FREIGHT");
                    objReportData.AddColumn("OCTROI");
                    objReportData.AddColumn("CENVATE");
                    objReportData.AddColumn("FCA_FOB_FREIGHT");
                    objReportData.AddColumn("ASSESSABLE");
                    objReportData.AddColumn("CUSTOMS_DUTY");
                    objReportData.AddColumn("IM_EDU_CESS");
                    objReportData.AddColumn("CVD");
                    objReportData.AddColumn("SURCHARGE");
                    objReportData.AddColumn("CUSTOMS_EDU_CESS");
                    objReportData.AddColumn("SP_ADD_DUTY");
                    objReportData.AddColumn("SP_EXCISE");
                    objReportData.AddColumn("INSURANCE");
                    objReportData.AddColumn("CLEARANCE_CHARGES");
                    objReportData.AddColumn("AIR_FREIGHT");
                    objReportData.AddColumn("OTHERS");
                    objReportData.AddColumn("LESS_CUSTOM_DUTY");
                    objReportData.AddColumn("ADD_1_PER");
                    objReportData.AddColumn("F_SHIPPING");
                    
                    TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
                    
                    objOpeningRow.setValue("GRN_NO","");
                    objOpeningRow.setValue("GR0SS_AMOUNT","");
                    objOpeningRow.setValue("NET_AMOUNT","");
                    objOpeningRow.setValue("DISCOUNT","");
                    objOpeningRow.setValue("PF","");
                    objOpeningRow.setValue("EXCISE","");
                    objOpeningRow.setValue("CESS","");
                    objOpeningRow.setValue("ST","");
                    objOpeningRow.setValue("FREIGHT","");
                    objOpeningRow.setValue("OCTROI","");
                    objOpeningRow.setValue("CENVATE","");
                    objOpeningRow.setValue("FCA_FOB_FREIGHT","");
                    objOpeningRow.setValue("ASSESSABLE","");
                    objOpeningRow.setValue("CUSTOMS_DUTY","");
                    objOpeningRow.setValue("IM_EDU_CESS","");
                    objOpeningRow.setValue("CVD","");
                    objOpeningRow.setValue("SURCHARGE","");
                    objOpeningRow.setValue("CUSTOMS_EDU_CESS","");
                    objOpeningRow.setValue("SP_ADD_DUTY","");
                    objOpeningRow.setValue("SP_EXCISE","");
                    objOpeningRow.setValue("INSURANCE","");
                    objOpeningRow.setValue("CLEARANCE_CHARGES","");
                    objOpeningRow.setValue("AIR_FREIGHT","");
                    objOpeningRow.setValue("OTHERS","");
                    objOpeningRow.setValue("LESS_CUSTOM_DUTY","");
                    objOpeningRow.setValue("ADD_1_PER","");
                    objOpeningRow.setValue("F_SHIPPING","");
                    
                    int BarCounter = data.getIntValueFromDB("SELECT COUNT(*) AS TOTAL FROM D_INV_GRN_HEADER WHERE GRN_DATE>='"+FromDate+"' AND GRN_DATE<='"+ToDate+"' AND GRN_TYPE="+GRNType+" AND APPROVED=1 AND CANCELLED=0");
                    Bar.setVisible(true);
                    lblBar.setVisible(true);
                    Bar.setMaximum(BarCounter);
                    Bar.setMinimum(0);
                    Bar.setValue(0);
                    
                    strSQL = "SELECT A.GRN_NO, A.GRN_DATE,SUM(B.QTY*B.RATE) AS GROSS_TOTAL, SUM(B.NET_AMOUNT) AS NET_TOTAL, " +
                    " SUM(A.COLUMN_1_AMT) AS DISCOUNT_H, SUM(A.COLUMN_2_AMT) AS PF_H, SUM(A.COLUMN_3_AMT) AS EXCISE_H, SUM(A.COLUMN_4_AMT) AS CESS_H, SUM(A.COLUMN_5_AMT) AS ST_H, SUM(A.COLUMN_6_AMT) AS FREIGHT_H," +
                    " SUM(B.COLUMN_1_AMT) AS DISCOUNT_L, SUM(B.COLUMN_2_AMT) AS PF_L, SUM(B.COLUMN_3_AMT) AS EXCISE_L, SUM(B.COLUMN_4_AMT) AS CESS_L, SUM(B.COLUMN_5_AMT) AS ST_L, SUM(B.COLUMN_6_AMT) AS FREIGHT_L," +
                    " SUM(A.COLUMN_7_AMT) AS OCTROI_H, SUM(A.COLUMN_8_AMT) AS CENVATE_H, SUM(A.COLUMN_9_AMT) AS FCA_FOB_FREIGHT_H, SUM(A.COLUMN_10_AMT) AS ASSESSABLE_H, SUM(A.COLUMN_11_AMT) AS CUSTOMS_DUTY_H," +
                    " SUM(B.COLUMN_7_AMT) AS OCTROI_L, SUM(B.COLUMN_8_AMT) AS CENVATE_L, SUM(B.COLUMN_9_AMT) AS FCA_FOB_FREIGHT_L, SUM(B.COLUMN_10_AMT) AS ASSESSABLE_L, SUM(B.COLUMN_11_AMT) AS CUSTOMS_DUTY_L," +
                    " SUM(A.COLUMN_12_AMT) AS IM_EDU_CESS_H, SUM(A.COLUMN_13_AMT) AS CVD_H, SUM(A.COLUMN_14_AMT) AS SURCHARGE_H, SUM(A.COLUMN_15_AMT) AS CUSTOMS_EDU_CESS_H, SUM(A.COLUMN_16_AMT) AS SP_ADD_DUTY_H," +
                    " SUM(B.COLUMN_12_AMT) AS IM_EDU_CESS_L, SUM(B.COLUMN_13_AMT) AS CVD_L, SUM(B.COLUMN_14_AMT) AS SURCHARGE_L, SUM(B.COLUMN_15_AMT) AS CUSTOMS_EDU_CESS_L, SUM(B.COLUMN_16_AMT) AS SP_ADD_DUTY_L," +
                    " SUM(A.COLUMN_17_AMT) AS SP_EXCISE_H, SUM(A.COLUMN_18_AMT) AS INSURANCE_H, SUM(A.COLUMN_19_AMT) AS CLEARANCE_CHARGES_H, SUM(A.COLUMN_20_AMT) AS AIR_FREIGHT_H," +
                    " SUM(B.COLUMN_17_AMT) AS SP_EXCISE_L, SUM(B.COLUMN_18_AMT) AS INSURANCE_L, SUM(B.COLUMN_19_AMT) AS CLEARANCE_CHARGES_L, SUM(B.COLUMN_20_AMT) AS AIR_FREIGHT_L," +
                    " SUM(A.COLUMN_21_AMT) AS OTHERS_H, SUM(A.COLUMN_22_AMT) AS LESS_CUSTOM_DUTY_H, SUM(A.COLUMN_23_AMT) AS ADD_1_PER_H, SUM(A.COLUMN_24_AMT) AS F_SHIPPING_H," +
                    " SUM(B.COLUMN_21_AMT) AS OTHERS_L, SUM(B.COLUMN_22_AMT) AS LESS_CUSTOM_DUTY_L, SUM(B.COLUMN_23_AMT) AS ADD_1_PER_L, SUM(B.COLUMN_24_AMT) AS F_SHIPPING_L" +
                    " FROM D_INV_GRN_HEADER A, D_INV_GRN_DETAIL B" +
                    " WHERE A.GRN_NO=B.GRN_NO AND A.GRN_DATE>='"+FromDate+"' AND A.GRN_DATE<='"+ToDate+"' AND A.GRN_TYPE="+GRNType+" AND A.APPROVED=1 AND A.CANCELLED=0" +
                    " GROUP BY A.GRN_NO ORDER BY A.GRN_NO,A.GRN_DATE";
                    
                    //System.out.println(strSQL);
                    ResultSet rsGRN=data.getResult(strSQL);
                    rsGRN.first();
                    
                    int Counter = 0;
                    int RecordCounter = 0;
                    
                    if(rsGRN.getRow()>0) {
                        while(!rsGRN.isAfterLast()) {
                            RecordCounter++;
                            Bar.setValue(RecordCounter);
                            //System.out.println("Counter = " + RecordCounter);
                            lblBar.setText("Processing Record "+RecordCounter);
                            objRow=objReportData.newRow();
                            
                            objRow.setValue("GRN_NO",UtilFunctions.getString(rsGRN,"GRN_NO",""));
                            //System.out.println("Amount = " + UtilFunctions.getDouble(rsGRN,"GROSS_TOTAL",0));
                            objRow.setValue("GR0SS_AMOUNT",Double.toString(UtilFunctions.getDouble(rsGRN,"GROSS_TOTAL",0)));
                            objRow.setValue("NET_AMOUNT",Double.toString(UtilFunctions.getDouble(rsGRN,"NET_TOTAL",0)));
                            
                            if(UtilFunctions.getDouble(rsGRN,"DISCOUNT_L",0) > 0 ) {
                                objRow.setValue("DISCOUNT",Double.toString(UtilFunctions.getDouble(rsGRN,"DISCOUNT_L",0)));
                            } else {
                                objRow.setValue("DISCOUNT",Double.toString(UtilFunctions.getDouble(rsGRN,"DISCOUNT_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"PF_L",0) > 0 ) {
                                objRow.setValue("PF",Double.toString(UtilFunctions.getDouble(rsGRN,"PF_L",0)));
                            } else {
                                objRow.setValue("PF",Double.toString(UtilFunctions.getDouble(rsGRN,"PF_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"EXCISE_L",0) > 0 ) {
                                objRow.setValue("EXCISE",Double.toString(UtilFunctions.getDouble(rsGRN,"EXCISE_L",0)));
                            } else {
                                objRow.setValue("EXCISE",Double.toString(UtilFunctions.getDouble(rsGRN,"EXCISE_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"CESS_L",0) > 0 ) {
                                objRow.setValue("CESS",Double.toString(UtilFunctions.getDouble(rsGRN,"CESS_L",0)));
                            } else {
                                objRow.setValue("CESS",Double.toString(UtilFunctions.getDouble(rsGRN,"CESS_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"ST_L",0) > 0 ) {
                                objRow.setValue("ST",Double.toString(UtilFunctions.getDouble(rsGRN,"ST_L",0)));
                            } else {
                                objRow.setValue("ST",Double.toString(UtilFunctions.getDouble(rsGRN,"ST_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"FREIGHT_L",0) > 0 ) {
                                objRow.setValue("FREIGHT",Double.toString(UtilFunctions.getDouble(rsGRN,"FREIGHT_L",0)));
                            } else {
                                objRow.setValue("FREIGHT",Double.toString(UtilFunctions.getDouble(rsGRN,"FREIGHT_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"OCTROI_L",0) > 0 ) {
                                objRow.setValue("OCTROI",Double.toString(UtilFunctions.getDouble(rsGRN,"OCTROI_L",0)));
                            } else {
                                objRow.setValue("OCTROI",Double.toString(UtilFunctions.getDouble(rsGRN,"OCTROI_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"CENVATE_L",0) > 0 ) {
                                objRow.setValue("CENVATE",Double.toString(UtilFunctions.getDouble(rsGRN,"CENVATE_L",0)));
                            } else {
                                objRow.setValue("CENVATE",Double.toString(UtilFunctions.getDouble(rsGRN,"CENVATE_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"FCA_FOB_FREIGHT_L",0) > 0 ) {
                                objRow.setValue("FCA_FOB_FREIGHT",Double.toString(UtilFunctions.getDouble(rsGRN,"FCA_FOB_FREIGHT_L",0)));
                            } else {
                                objRow.setValue("FCA_FOB_FREIGHT",Double.toString(UtilFunctions.getDouble(rsGRN,"FCA_FOB_FREIGHT_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"ASSESSABLE_L",0) > 0 ) {
                                objRow.setValue("ASSESSABLE",Double.toString(UtilFunctions.getDouble(rsGRN,"ASSESSABLE_L",0)));
                            } else {
                                objRow.setValue("ASSESSABLE",Double.toString(UtilFunctions.getDouble(rsGRN,"ASSESSABLE_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"CUSTOMS_DUTY_L",0) > 0 ) {
                                objRow.setValue("CUSTOMS_DUTY",Double.toString(UtilFunctions.getDouble(rsGRN,"CUSTOMS_DUTY_L",0)));
                            } else {
                                objRow.setValue("CUSTOMS_DUTY",Double.toString(UtilFunctions.getDouble(rsGRN,"CUSTOMS_DUTY_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"IM_EDU_CESS_L",0) > 0 ) {
                                objRow.setValue("IM_EDU_CESS",Double.toString(UtilFunctions.getDouble(rsGRN,"IM_EDU_CESS_L",0)));
                            } else {
                                objRow.setValue("IM_EDU_CESS",Double.toString(UtilFunctions.getDouble(rsGRN,"IM_EDU_CESS_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"CVD_L",0) > 0 ) {
                                objRow.setValue("CVD",Double.toString(UtilFunctions.getDouble(rsGRN,"CVD_L",0)));
                            } else {
                                objRow.setValue("CVD",Double.toString(UtilFunctions.getDouble(rsGRN,"CVD_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"SURCHARGE_L",0) > 0 ) {
                                objRow.setValue("SURCHARGE",Double.toString(UtilFunctions.getDouble(rsGRN,"SURCHARGE_L",0)));
                            } else {
                                objRow.setValue("SURCHARGE",Double.toString(UtilFunctions.getDouble(rsGRN,"SURCHARGE_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"CUSTOMS_EDU_CESS_L",0) > 0 ) {
                                objRow.setValue("CUSTOMS_EDU_CESS",Double.toString(UtilFunctions.getDouble(rsGRN,"CUSTOMS_EDU_CESS_L",0)));
                            } else {
                                objRow.setValue("CUSTOMS_EDU_CESS",Double.toString(UtilFunctions.getDouble(rsGRN,"CUSTOMS_EDU_CESS_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"SP_ADD_DUTY_L",0) > 0 ) {
                                objRow.setValue("SP_ADD_DUTY",Double.toString(UtilFunctions.getDouble(rsGRN,"SP_ADD_DUTY_L",0)));
                            } else {
                                objRow.setValue("SP_ADD_DUTY",Double.toString(UtilFunctions.getDouble(rsGRN,"SP_ADD_DUTY_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"SP_EXCISE_L",0) > 0 ) {
                                objRow.setValue("SP_EXCISE",Double.toString(UtilFunctions.getDouble(rsGRN,"SP_EXCISE_L",0)));
                            } else {
                                objRow.setValue("SP_EXCISE",Double.toString(UtilFunctions.getDouble(rsGRN,"SP_EXCISE_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"INSURANCE_L",0) > 0 ) {
                                objRow.setValue("INSURANCE",Double.toString(UtilFunctions.getDouble(rsGRN,"INSURANCE_L",0)));
                            } else {
                                objRow.setValue("INSURANCE",Double.toString(UtilFunctions.getDouble(rsGRN,"INSURANCE_H",0)));
                            }
                                                        
                            if(UtilFunctions.getDouble(rsGRN,"CLEARANCE_CHARGES_L",0) > 0 ) {
                                objRow.setValue("CLEARANCE_CHARGES",Double.toString(UtilFunctions.getDouble(rsGRN,"CLEARANCE_CHARGES_L",0)));
                            } else {
                                objRow.setValue("CLEARANCE_CHARGES",Double.toString(UtilFunctions.getDouble(rsGRN,"CLEARANCE_CHARGES_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"AIR_FREIGHT_L",0) > 0 ) {
                                objRow.setValue("AIR_FREIGHT",Double.toString(UtilFunctions.getDouble(rsGRN,"AIR_FREIGHT_L",0)));
                            } else {
                                objRow.setValue("AIR_FREIGHT",Double.toString(UtilFunctions.getDouble(rsGRN,"AIR_FREIGHT_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"OTHERS_L",0) > 0 ) {
                                objRow.setValue("OTHERS",Double.toString(UtilFunctions.getDouble(rsGRN,"OTHERS_L",0)));
                            } else {
                                objRow.setValue("OTHERS",Double.toString(UtilFunctions.getDouble(rsGRN,"OTHERS_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"LESS_CUSTOM_DUTY_L",0) > 0 ) {
                                objRow.setValue("LESS_CUSTOM_DUTY",Double.toString(UtilFunctions.getDouble(rsGRN,"LESS_CUSTOM_DUTY_L",0)));
                            } else {
                                objRow.setValue("LESS_CUSTOM_DUTY",Double.toString(UtilFunctions.getDouble(rsGRN,"LESS_CUSTOM_DUTY_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"ADD_1_PER_L",0) > 0 ) {
                                objRow.setValue("ADD_1_PER",Double.toString(UtilFunctions.getDouble(rsGRN,"ADD_1_PER_L",0)));
                            } else {
                                objRow.setValue("ADD_1_PER",Double.toString(UtilFunctions.getDouble(rsGRN,"ADD_1_PER_H",0)));
                            }
                            
                            if(UtilFunctions.getDouble(rsGRN,"F_SHIPPING_L",0) > 0 ) {
                                objRow.setValue("F_SHIPPING",Double.toString(UtilFunctions.getDouble(rsGRN,"F_SHIPPING_L",0)));
                            } else {
                                objRow.setValue("F_SHIPPING",Double.toString(UtilFunctions.getDouble(rsGRN,"F_SHIPPING_H",0)));
                            }
                            objReportData.AddRow(objRow);
                            
                            rsGRN.next();
                        }
                    }
                    
                    Bar.setVisible(false);
                    //lblBar.setVisible(false);
                    lblBar.setText("Completed...");
                    
                    HashMap Parameters=new HashMap();
                    Parameters.put("COMPANY_ID",Integer.toString(EITLERPGLOBAL.gCompanyID));
                    Parameters.put("FROM_DATE",EITLERPGLOBAL.formatDate(FromDate));
                    Parameters.put("TO_DATE",EITLERPGLOBAL.formatDate(ToDate));
                    Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
                    
                    objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptGRNDetail.rpt",Parameters,objReportData);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
    
    private boolean Validate() {
        //Form level validations
        
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter From Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Please Varify From Date");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter To Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Please Varify From Date");
            return false;
        }
        return true;
    }
    
    private void GenerateCombo() {
        try {
            cmbTypeModel=new EITLComboModel();
            cmbType.removeAll();
            cmbType.setModel(cmbTypeModel);
            
            ComboData objData=new ComboData();
            objData.Code=1;
            objData.Text="General";
            
            cmbTypeModel.addElement(objData);
            
            objData=new ComboData();
            objData.Code=2;
            objData.Text="Raw Material";
            
            cmbTypeModel.addElement(objData);
            
            objData=new ComboData();
            objData.Code=3;
            objData.Text="Job Work";
            
            cmbTypeModel.addElement(objData);
        }
        catch(Exception e) {
        }
    }
}
