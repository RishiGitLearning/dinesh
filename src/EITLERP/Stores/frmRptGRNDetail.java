

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
import java.math.BigDecimal;

public class frmRptGRNDetail extends javax.swing.JApplet {
    
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
        jLabel6.setText("GRN BIFERGATION");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 350, 15);

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
        cmdPreview.setBounds(160, 100, 130, 25);

        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("From Date :");
        getContentPane().add(lblFromDate);
        lblFromDate.setBounds(28, 73, 77, 15);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(110, 70, 90, 20);

        lblBar.setText("...");
        getContentPane().add(lblBar);
        lblBar.setBounds(10, 130, 200, 15);

        getContentPane().add(Bar);
        Bar.setBounds(10, 150, 200, 14);

        cmbType.setAutoscrolls(true);
        getContentPane().add(cmbType);
        cmbType.setBounds(110, 40, 190, 24);

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
                    double TotalGrossAmount = 0D;
                    double TotalFreight = 0D;
                    double TotalDiscount = 0D;
                    double TotalCenvate = 0D;
                    double TotalCustomsDuty = 0D;
                    double TotalExcise = 0D;
                    double TotalST = 0D;
                    double TotalInsurance = 0D;
                    double TotalClearanceCharges = 0D;
                    double TotalOthers = 0D;
                    double TotalFinalAmount = 0D;
                    double TotalPF=0D;
                    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en","IN"));
                    
                    TReportWriter.SimpleDataProvider.TRow objRow;
                    TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
                    objReportData.AddColumn("GRN_NO");
                    objReportData.AddColumn("GROSS_AMOUNT");
                    objReportData.AddColumn("DISCOUNT");
                    objReportData.AddColumn("FREIGHT");
                    objReportData.AddColumn("CENVATE");
                    objReportData.AddColumn("CUSTOMS_DUTY");
                    objReportData.AddColumn("EXCISE");
                    objReportData.AddColumn("ST");
                    objReportData.AddColumn("INSURANCE");
                    objReportData.AddColumn("PF");
                    objReportData.AddColumn("CLEARANCE_CHARGES");
                    objReportData.AddColumn("OTHERS");
                    objReportData.AddColumn("FINAL_AMOUNT");
                    objReportData.AddColumn("PO_NO");
                    
                    TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
                    
                    objOpeningRow.setValue("GRN_NO","");
                    objOpeningRow.setValue("GROSS_AMOUNT","");
                    objOpeningRow.setValue("DISCOUNT","");
                    objOpeningRow.setValue("FREIGHT","");
                    objOpeningRow.setValue("CENVATE","");
                    objOpeningRow.setValue("CUSTOMS_DUTY","");
                    objOpeningRow.setValue("EXCISE","");
                    objOpeningRow.setValue("ST","");
                    objOpeningRow.setValue("INSURANCE","");
                    objOpeningRow.setValue("PF","");
                    objOpeningRow.setValue("CLEARANCE_CHARGES","");
                    objOpeningRow.setValue("OTHERS","");
                    objOpeningRow.setValue("FINAL_AMOUNT","");
                    objOpeningRow.setValue("PO_NO","");
                    String HTableName="";
                    String DTableName="";
                    String FieldName="";
                    int BarCounter = 0;
                    boolean GRNBool = false;
                    if(GRNType <= 2) {
                        GRNBool=true;
                        HTableName = "D_INV_GRN_HEADER";
                        DTableName = "D_INV_GRN_DETAIL";
                        FieldName = "GRN_NO";
                        BarCounter = data.getIntValueFromDB("SELECT COUNT(*) AS TOTAL FROM D_INV_GRN_HEADER WHERE GRN_DATE>='"+FromDate+"' AND GRN_DATE<='"+ToDate+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_TYPE="+GRNType+" AND APPROVED=1 AND CANCELLED=0 AND SUPP_ID NOT IN ('888888','999999') ORDER BY GRN_DATE");
                    }else if(GRNType ==3) {
                        GRNBool=true;
                        HTableName = "D_INV_GRN_HEADER";
                        DTableName = "D_INV_GRN_DETAIL";
                        FieldName = "GRN_NO";
                        BarCounter = data.getIntValueFromDB("SELECT COUNT(*) AS TOTAL FROM D_INV_GRN_HEADER WHERE GRN_DATE>='"+FromDate+"' AND GRN_DATE<='"+ToDate+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_TYPE="+GRNType+" AND APPROVED=1 AND CANCELLED=0 AND SUPP_ID IN ('888888','999999') ORDER BY GRN_DATE");
                    }
                    else {
                        HTableName = "D_INV_JOB_HEADER";
                        DTableName = "D_INV_JOB_DETAIL";
                        FieldName = "JOB_NO";
                        BarCounter = data.getIntValueFromDB("SELECT COUNT(*) AS TOTAL FROM D_INV_JOB_HEADER WHERE JOB_DATE>='"+FromDate+"' AND JOB_DATE<='"+ToDate+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 AND SUPP_ID NOT IN ('888888','999999') ORDER BY JOB_DATE"); //AND GRN_TYPE="+GRNType+"
                    }
                    
                    Bar.setVisible(true);
                    lblBar.setVisible(true);
                    Bar.setMaximum(BarCounter);
                    Bar.setMinimum(0);
                    Bar.setValue(0);
                    
                    if(GRNBool) {
                        if(GRNType <= 2) {
                            strSQL = "SELECT * FROM D_INV_GRN_HEADER WHERE GRN_DATE>='"+FromDate+"' AND GRN_DATE<='"+ToDate+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_TYPE="+GRNType+" AND APPROVED=1 AND CANCELLED=0 AND SUPP_ID NOT IN ('888888','999999') ORDER BY GRN_DATE";
                        } else if(GRNType == 3) {
                            strSQL = "SELECT * FROM D_INV_GRN_HEADER WHERE GRN_DATE>='"+FromDate+"' AND GRN_DATE<='"+ToDate+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_TYPE="+GRNType+" AND APPROVED=1 AND CANCELLED=0 AND SUPP_ID IN ('888888','999999') ORDER BY GRN_DATE";
                        }
                    } else {
                        strSQL = "SELECT * FROM D_INV_JOB_HEADER WHERE JOB_DATE>='"+FromDate+"' AND JOB_DATE<='"+ToDate+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 AND SUPP_ID NOT IN ('888888','999999') ORDER BY JOB_DATE";
                    }
                    
                    System.out.println(strSQL);
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
                            String GRNNo = "";
                            if(GRNBool) {
                                GRNNo = UtilFunctions.getString(rsGRN,"GRN_NO","");
                            } else {
                                GRNNo = UtilFunctions.getString(rsGRN,"JOB_NO","");
                            }
                            double HRate = UtilFunctions.getDouble(rsGRN,"CURRENCY_RATE",0);
                            if(HRate==0) {
                                HRate=1;
                            }
                            String SQL = "";
                            if(GRNBool) {
                                SQL = "SELECT * FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' ";
                            } else {
                                SQL = "SELECT * FROM D_INV_JOB_DETAIL WHERE JOB_NO='"+GRNNo+"' ";
                            }
                            if(GRNNo.equals("R000293")) {
                                System.out.println();
                            }
                            ResultSet rsDetail = data.getResult(SQL);
                            rsDetail.first();
                            double GrossAmount = 0;
                            while(!rsDetail.isAfterLast()) {
                                double qty = rsDetail.getDouble("QTY");
                                double rate = rsDetail.getDouble("RATE");
                                GrossAmount += EITLERPGLOBAL.round(rsDetail.getDouble("QTY") * rsDetail.getDouble("RATE") * HRate,2);
                                //System.out.println("QTY = " + qty + "Rate = " + rate + "ExRate = " + HRate + "Gross Amount = " + EITLERPGLOBAL.round(rsDetail.getDouble("QTY") * rsDetail.getDouble("RATE") * HRate,2));
                                rsDetail.next();
                            }
                            objRow.setValue("GRN_NO",GRNNo);
                            //System.out.println("Amount = " + EITLERPGLOBAL.round(GrossAmount,2));
                            objRow.setValue("GROSS_AMOUNT",Double.toString(EITLERPGLOBAL.round(GrossAmount,2)));
                            TotalGrossAmount += GrossAmount;
                            
                            double Discount = data.getDoubleValueFromDB("SELECT SUM(COLUMN_1_AMT) AS DISCOUNT FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(Discount == 0 ) {
                                Discount = UtilFunctions.getDouble(rsGRN,"COLUMN_1_AMT",0);
                            }
                            TotalDiscount += Discount;
                            objRow.setValue("DISCOUNT",Double.toString(EITLERPGLOBAL.round(Discount,2)));
                            
                            double Freight = 0;
                            if(GRNBool) {
                                Freight = data.getDoubleValueFromDB("SELECT SUM(COLUMN_6_AMT) AS FREIGHT FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            } else {
                                Freight = data.getDoubleValueFromDB("SELECT SUM(COLUMN_5_AMT) AS FREIGHT FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            }
                            if(Freight == 0 ) {
                                if(GRNBool) {
                                    Freight = UtilFunctions.getDouble(rsGRN,"COLUMN_6_AMT",0);
                                } else {
                                    Freight = UtilFunctions.getDouble(rsGRN,"COLUMN_5_AMT",0);
                                }
                            }
                            
                            double AirFreight = 0;
                            AirFreight = data.getDoubleValueFromDB("SELECT SUM(COLUMN_20_AMT) AS AIR_FREIGHT FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(AirFreight == 0 ) {
                                AirFreight = UtilFunctions.getDouble(rsGRN,"COLUMN_20_AMT",0);
                            }
                            Freight += AirFreight;
                            TotalFreight += Freight;
                            objRow.setValue("FREIGHT",Double.toString(EITLERPGLOBAL.round(Freight,2)));
                            
                            double cenvate = 0;
                            if(GRNBool) {
                                cenvate = data.getDoubleValueFromDB("SELECT SUM(COLUMN_8_AMT) AS CENVATE FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            } else {
                                cenvate = data.getDoubleValueFromDB("SELECT SUM(COLUMN_7_AMT) AS CENVATE FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            }
                            if(cenvate == 0 ) {
                                if(GRNBool) {
                                    cenvate = UtilFunctions.getDouble(rsGRN,"COLUMN_8_AMT",0);
                                } else {
                                    cenvate = UtilFunctions.getDouble(rsGRN,"COLUMN_7_AMT",0);
                                }
                            }
                            TotalCenvate += cenvate;
                            objRow.setValue("CENVATE",Double.toString(EITLERPGLOBAL.round(cenvate,2)));
                            
                            double CustomsDuty = 0;
                            CustomsDuty = data.getDoubleValueFromDB("SELECT SUM(COLUMN_11_AMT) AS CUSTOMS_DUTY FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(CustomsDuty == 0) {
                                CustomsDuty = UtilFunctions.getDouble(rsGRN,"COLUMN_11_AMT",0);
                            }
                            
                            double ImEduCess = data.getDoubleValueFromDB("SELECT SUM(COLUMN_12_AMT) AS IM_EDU_CESS FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(ImEduCess == 0 ) {
                                ImEduCess = UtilFunctions.getDouble(rsGRN,"COLUMN_12_AMT",0);
                            }
                            
                            double CVD = data.getDoubleValueFromDB("SELECT SUM(COLUMN_13_AMT) AS CVD FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(CVD == 0 ) {
                                CVD = UtilFunctions.getDouble(rsGRN,"COLUMN_13_AMT",0);
                            }
                            
                            double CustomsEduCess = data.getDoubleValueFromDB("SELECT SUM(COLUMN_15_AMT) AS CUSTOMS_EDU_CESS FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(CustomsEduCess == 0 ) {
                                CustomsEduCess = UtilFunctions.getDouble(rsGRN,"COLUMN_15_AMT",0);
                            }
                            
                            double SpAddDuty = data.getDoubleValueFromDB("SELECT SUM(COLUMN_16_AMT) AS SP_ADD_DUTY FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(SpAddDuty == 0 ) {
                                SpAddDuty = UtilFunctions.getDouble(rsGRN,"COLUMN_16_AMT",0);
                            }
                            
                            double Cess = data.getDoubleValueFromDB("SELECT SUM(COLUMN_4_AMT) AS CESS FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(Cess == 0 ) {
                                Cess = UtilFunctions.getDouble(rsGRN,"COLUMN_4_AMT",0);
                            }
                            
                            CustomsDuty = EITLERPGLOBAL.round(CustomsDuty + ImEduCess + CVD + CustomsEduCess + SpAddDuty + Cess,2);
                            TotalCustomsDuty += CustomsDuty;
                            objRow.setValue("CUSTOMS_DUTY",Double.toString(EITLERPGLOBAL.round(CustomsDuty,2)));
                            
                            double Excise = data.getDoubleValueFromDB("SELECT SUM(COLUMN_3_AMT) AS EXCISE FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(Excise == 0 ) {
                                Excise = UtilFunctions.getDouble(rsGRN,"COLUMN_3_AMT",0);
                            }
                            TotalExcise += Excise;
                            objRow.setValue("EXCISE",Double.toString(EITLERPGLOBAL.round(Excise,2)));
                            
                            double ST = 0;
                            if(GRNBool) {
                                ST = data.getDoubleValueFromDB("SELECT SUM(COLUMN_5_AMT) AS ST FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            } else {
                                ST = data.getDoubleValueFromDB("SELECT SUM(COLUMN_4_AMT) AS ST FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            }
                            if(ST == 0 ) {
                                if(GRNBool) {
                                    ST = UtilFunctions.getDouble(rsGRN,"COLUMN_5_AMT",0);
                                } else {
                                    ST = UtilFunctions.getDouble(rsGRN,"COLUMN_4_AMT",0);
                                }
                            }
                            TotalST += ST;
                            objRow.setValue("ST",Double.toString(EITLERPGLOBAL.round(ST,2)));
                            
                            double Insurance = data.getDoubleValueFromDB("SELECT SUM(COLUMN_18_AMT) AS INSURANCE FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            
                            if(Insurance == 0 ) {
                                Insurance = UtilFunctions.getDouble(rsGRN,"COLUMN_18_AMT",0);
                            }
                            TotalInsurance += Insurance;
                            objRow.setValue("INSURANCE",Double.toString(EITLERPGLOBAL.round(Insurance,2)));
                            
                            double PF = data.getDoubleValueFromDB("SELECT SUM(COLUMN_2_AMT) AS PF FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(PF == 0 ) {
                                PF = UtilFunctions.getDouble(rsGRN,"COLUMN_2_AMT",0);
                            }
                            TotalPF += PF;
                            objRow.setValue("PF",Double.toString(EITLERPGLOBAL.round(PF,2)));
                            
                            double ClearanceCharges = data.getDoubleValueFromDB("SELECT SUM(COLUMN_19_AMT) AS CLEARANCE_CHARGES FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(ClearanceCharges == 0 ) {
                                ClearanceCharges = UtilFunctions.getDouble(rsGRN,"COLUMN_19_AMT",0);
                            }
                            TotalClearanceCharges += ClearanceCharges;
                            objRow.setValue("CLEARANCE_CHARGES",Double.toString(EITLERPGLOBAL.round(ClearanceCharges,2)));
                            
                            double Others = data.getDoubleValueFromDB("SELECT SUM(COLUMN_21_AMT) AS OTHERS FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            if(Others == 0 ) {
                                Others = UtilFunctions.getDouble(rsGRN,"COLUMN_21_AMT",0);
                            }
                            TotalOthers += Others;
                            objRow.setValue("OTHERS",Double.toString(EITLERPGLOBAL.round(Others,2)));
                            double FinalAmount = EITLERPGLOBAL.round(GrossAmount + CustomsDuty + Freight + Others + ClearanceCharges + Insurance + Excise + ST + PF - cenvate - Discount ,2);
                            TotalFinalAmount += FinalAmount;
                            objRow.setValue("FINAL_AMOUNT",Double.toString(EITLERPGLOBAL.round(FinalAmount,2)));
                            String PONo = data.getStringValueFromDB("SELECT PO_NO FROM "+DTableName+" WHERE "+FieldName+"='"+GRNNo+"' ");
                            objRow.setValue("PO_NO",PONo);
                            objReportData.AddRow(objRow);
                            rsGRN.next();
                        }
                        
                        objRow=objReportData.newRow();
                        objRow.setValue("GROSS_AMOUNT_FOOTER",nf.format(TotalGrossAmount));
                        objRow.setValue("FREIGHT_FOOTER",nf.format(TotalFreight));
                        objRow.setValue("DISCOUNT_FOOTER",nf.format(TotalDiscount));
                        objRow.setValue("OTHERS_FOOTER",nf.format(TotalOthers));
                        objRow.setValue("CLEARANCE_CHARGES_FOOTER",nf.format(TotalClearanceCharges));
                        objRow.setValue("CENVATE_FOOTER",nf.format(TotalCenvate));
                        objRow.setValue("EXCISE_FOOTER",nf.format(TotalExcise));
                        objRow.setValue("ST_FOOTER",nf.format(TotalST));
                        objRow.setValue("INSURANCE_FOOTER",nf.format(TotalInsurance));
                        objRow.setValue("PF_FOOTER",nf.format(TotalPF));
                        objRow.setValue("CUSTOMS_DUTY_FOOTER",nf.format(TotalCustomsDuty));
                        objRow.setValue("FINAL_AMOUNT_FOOTER",nf.format(TotalFinalAmount));
                        objReportData.AddRow(objRow);
                        System.out.println(nf.format(TotalGrossAmount));
                        
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
            objData.Text="Raw Material STM";
            
            cmbTypeModel.addElement(objData);
            
            
            objData=new ComboData();
            objData.Code=4;
            objData.Text="Job Work";
            
            cmbTypeModel.addElement(objData);
        }
        catch(Exception e) {
        }
    }
}
