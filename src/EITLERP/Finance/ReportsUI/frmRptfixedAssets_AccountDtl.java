/*
 * frmRptGRNInfo.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.Finance.ReportsUI;

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

public class frmRptfixedAssets_AccountDtl extends javax.swing.JApplet {
    
    private HashMap props=new HashMap();
    public HashMap colVoucherItems=new HashMap();
    
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData = null;//new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        Format();
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
        txtAccountCode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtAccountName = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
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
        jLabel6.setText("FIXED ASSETS ACCOUNT RELATED VOUCHER DETAILS");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 370, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Account No : ");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 70, 100, 20);

        txtAccountCode.setColumns(10);
        txtAccountCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountCodeFocusLost(evt);
            }
        });
        txtAccountCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAccountCodeKeyPressed(evt);
            }
        });

        getContentPane().add(txtAccountCode);
        txtAccountCode.setBounds(120, 70, 90, 19);

        jLabel3.setText("Account Name :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 100, 100, 20);

        txtAccountName.setColumns(10);
        txtAccountName.setEditable(false);
        getContentPane().add(txtAccountName);
        txtAccountName.setBounds(120, 100, 290, 20);

        cmdPreview.setText("Preview ");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(310, 180, 90, 25);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("From Date : ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 130, 100, 20);

        txtFromDate.setColumns(10);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(120, 130, 90, 19);

        jLabel5.setText("To Date :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(220, 130, 60, 20);

        txtToDate.setColumns(10);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(280, 130, 90, 20);

    }//GEN-END:initComponents
    
    private void txtAccountCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountCodeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) {
            LOV aList=new LOV();
            
            aList.SQL="SELECT MAIN_ACCOUNT_CODE,ACCOUNT_NAME FROM D_FIN_GL WHERE  APPROVED=1 ORDER BY ACCOUNT_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                txtAccountCode.setText(aList.ReturnVal);
                txtAccountName.setText(clsAccount.getAccountName(aList.ReturnVal,""));
            }
            
        }
    }//GEN-LAST:event_txtAccountCodeKeyPressed
    
    private void txtAccountCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountCodeFocusLost
        // TODO add your handling code here:
        String AccountName = "";
        AccountName = clsAccount.getAccountName(txtAccountCode.getText(),"");
        txtAccountName.setText(AccountName);
    }//GEN-LAST:event_txtAccountCodeFocusLost
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        //System.out.println("Call....1");
        if ( ! Validate()) {
            return;
        }
        GenerateReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtAccountCode;
    private javax.swing.JTextField txtAccountName;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void Format() {
        try {
            props=new HashMap();
            props.put("SR_NO",new Variant(0));
            props.put("COMPANY_ID",new Variant(0));
            props.put("VOUCHER_NO",new Variant(""));
            props.put("VOUCHER_DATE",new Variant(""));
            props.put("VOUCHER_TYPE",new Variant(""));
            props.put("BOOK_CODE",new Variant(""));
            props.put("MAIN_ACCOUNT_CODE",new Variant(""));
            props.put("SUB_ACCOUNT_CODE",new Variant(""));
            props.put("EFFECT",new Variant(""));
            props.put("C_AMOUNT",new Variant(""));
            props.put("D_AMOUNT",new Variant(""));
            props.put("REMARKS",new Variant(""));
            props.put("REF_NO",new Variant(""));
            props.put("REF_DATE",new Variant(""));
            props.put("PO_NO",new Variant(""));
            props.put("INVOICE_NO",new Variant(""));
            props.put("INVOICE_DATE",new Variant(""));
            props.put("ITEM_ID",new Variant(""));
            props.put("ITEM_DESCRIPTION",new Variant(""));
            props.put("QTY",new Variant(0));
            props.put("ACCOUNT_NAME",new Variant(""));
            props.put("PARTY_NAME",new Variant(""));
            props.put("GROUP",new Variant(""));
            
        }
        catch(Exception e) {
            
        }
    }
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return (new Variant(""));
        }
        else {
            return (Variant) props.get(PropName);
        }
    }
    
    public void setAttribute(String PropName,String Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    private void GenerateReport() {
        try {
            //System.out.println("Call....2");
            String strQry = "";
            strQry = "SELECT VOUHEAD.COMPANY_ID,VOUHEAD.VOUCHER_NO,VOUHEAD.VOUCHER_DATE,VOUHEAD.LEGACY_NO,VOUHEAD.BOOK_CODE, " +
            "VOUDTL.EFFECT,VOUDTL.SR_NO,VOUDTL.MAIN_ACCOUNT_CODE,VOUDTL.SUB_ACCOUNT_CODE, "+
            "CASE WHEN VOUDTL.EFFECT = 'C' THEN VOUDTL.AMOUNT ELSE '0' END C_AMOUNT, "+
            "CASE WHEN VOUDTL.EFFECT = 'D' THEN VOUDTL.AMOUNT ELSE '0' END D_AMOUNT, VOUDTL.AMOUNT, "+
            "CASE WHEN VOUHEAD.REMARKS = 'PJV Generated by System' THEN '' ELSE VOUHEAD.REMARKS END REMARKS,  "+
            "VOUDTL.GRN_NO,VOUDTL.INVOICE_NO ,VOUDTL.INVOICE_DATE, VOUHEAD.VOUCHER_TYPE , VOUDTL.REF_COMPANY_ID, "+
            "VOUDTL.MODULE_ID,VOUDTL.EFFECT,VOUDTL.BLOCK_NO,VOUDTL.PO_NO "+
            "FROM FINANCE.D_FIN_VOUCHER_HEADER VOUHEAD, FINANCE.D_FIN_VOUCHER_DETAIL_EX VOUDTL "+
            "WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 "+
            "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO  "+
            "AND VOUDTL.MAIN_ACCOUNT_CODE= '"+ txtAccountCode.getText().trim() +"' " +
            "AND VOUHEAD.VOUCHER_DATE>= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) +"' "+
            "AND VOUHEAD.VOUCHER_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) +"'   "+
            //"GROUP BY VOUHEAD.VOUCHER_NO,VOUDTL.MAIN_ACCOUNT_CODE " +
            "ORDER BY VOUHEAD.VOUCHER_DATE,VOUHEAD.VOUCHER_NO ";
            ResultSet rsVouData = data.getResult(strQry);
            
            rsVouData.first();
            int Counter = 0;
            int cnt=0;
            double LineAmount=0;
            String LastVoucherNo="";
            double Total_credit=0.0,Total_debit=0.0;
            colVoucherItems.clear();
            
            if(rsVouData.getRow()>0) {
                while(!rsVouData.isAfterLast()) {
                    String VoucherNo = UtilFunctions.getString(rsVouData,"VOUCHER_NO","");
                    
                    if (! VoucherNo.trim().equals(LastVoucherNo.trim())) {
                        
                        String Ref_Com_Id = "", Ref_No="", URL = "",Module_Id="",Qry="";
                        ResultSet rsItem = null;
                        
                        Ref_Com_Id = UtilFunctions.getString(rsVouData,"REF_COMPANY_ID","");
                        Ref_No = UtilFunctions.getString(rsVouData,"GRN_NO","");
                        Module_Id = UtilFunctions.getString(rsVouData,"MODULE_ID","");
                        
                        URL = data.getStringValueFromDB("SELECT DATABASE_URL FROM D_COM_FIN_YEAR WHERE COMPANY_ID=" + Ref_Com_Id +" AND YEAR_FROM= YEAR('"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"') ");
                        
                        String SubAccCode = data.getStringValueFromDB("select  DISTINCT SUB_ACCOUNT_CODE " +
                            "FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX "+
                            "WHERE VOUCHER_NO= '"+ VoucherNo +"' AND SUB_ACCOUNT_CODE<> ''");
                        
                        String Partyname = "";
                        if (! SubAccCode.trim().equals("")) {
                            Partyname = data.getStringValueFromDB("SELECT PARTY_NAME FROM FINANCE.D_FIN_PARTY_MASTER WHERE PARTY_CODE = '"+ SubAccCode +"' ");
                        }
                        else {
                            Partyname = "";
                        }
                        
                        if ((! Module_Id.trim().equals("0")) && (! Module_Id.trim().equals("63")) && (! Module_Id.trim().equals("90")) && (! Module_Id.trim().equals("89")) && (! Module_Id.trim().equals("80")) && (! Module_Id.trim().equals("69")) && (! Module_Id.trim().equals("91")) && (! Module_Id.trim().equals("94"))) {
                            
                            if ((Module_Id.trim().equals("5")) || (Module_Id.trim().equals("6"))) {
                                //MIR
                                Qry = "SELECT MIRHEAD.MIR_DATE as REF_DATE,MIRDTL.ITEM_ID as ITEM_ID, "+
                                "MIRDTL.ITEM_EXTRA_DESC as ITEM_DESCRIPTION,MIRDTL.QTY as QTY,MIRDTL.QTY*MIRDTL.LANDED_RATE AMOUNT "+
                                "FROM D_INV_MIR_HEADER MIRHEAD , D_INV_MIR_DETAIL MIRDTL "+
                                "WHERE MIRHEAD.COMPANY_ID = "+ Ref_Com_Id +" AND MIRHEAD.APPROVED = 1 AND MIRHEAD.CANCELLED = 0  "+
                                "AND MIRHEAD.MIR_NO = '" + Ref_No + "'  "+
                                "AND MIRHEAD.COMPANY_ID = MIRDTL.COMPANY_ID AND MIRHEAD.MIR_NO = MIRDTL.MIR_NO";
                                
                                rsItem = data.getResult(Qry,URL);
                                
                            }
                            
                            if ((Module_Id.trim().equals("7")) || (Module_Id.trim().equals("8"))) {
                                //GRN
                                Qry = "SELECT GRNHEAD.GRN_DATE as REF_DATE,GRNDTL.ITEM_ID as ITEM_ID, "+
                                "GRNDTL.ITEM_EXTRA_DESC as ITEM_DESCRIPTION,GRNDTL.QTY as QTY,GRNDTL.QTY*GRNDTL.LANDED_RATE AMOUNT "+
                                "FROM D_INV_GRN_HEADER GRNHEAD , D_INV_GRN_DETAIL GRNDTL "+
                                "WHERE GRNHEAD.COMPANY_ID = "+ Ref_Com_Id +" AND GRNHEAD.APPROVED = 1 AND GRNHEAD.CANCELLED = 0  "+
                                "AND GRNHEAD.GRN_NO = '" + Ref_No + "'  "+
                                "AND GRNHEAD.COMPANY_ID = GRNDTL.COMPANY_ID AND GRNHEAD.GRN_NO = GRNDTL.GRN_NO";
                                
                                rsItem = data.getResult(Qry,URL);
                                
                            }
                            
                            if (Module_Id.trim().equals("48")) {
                                //JOB WORK
                                Qry = "SELECT JOBHEAD.JOB_DATE as REF_DATE,JOBDTL.ITEM_ID as ITEM_ID, "+
                                "JOBDTL.ITEM_EXTRA_DESC as ITEM_DESCRIPTION,JOBDTL.QTY as QTY,JOBDTL.QTY*JOBDTL.LANDED_RATE AMOUNT "+
                                "FROM D_INV_JOB_HEADER JOBHEAD , D_INV_JOB_DETAIL JOBDTL "+
                                "WHERE JOBHEAD.COMPANY_ID = "+ Ref_Com_Id +" AND JOBHEAD.APPROVED = 1 AND JOBHEAD.CANCELLED = 0  "+
                                "AND JOBHEAD.JOB_NO = '" + Ref_No + "'  "+
                                "AND JOBHEAD.COMPANY_ID = JOBDTL.COMPANY_ID AND JOBHEAD.JOB_NO = JOBDTL.JOB_NO";
                                
                                rsItem = data.getResult(Qry,URL);
                            }
                            
                            if(rsItem.getRow()>0) {
                                rsItem.first();
                                int cntItem=0;
                                while(!rsItem.isAfterLast()) {
                                    frmRptfixedAssets_AccountDtl objItem = new frmRptfixedAssets_AccountDtl();
                    
                                    Counter++;
                                    cntItem++;
                                    
                                    objItem.setAttribute("SR_NO",Integer.toString(Counter));
                                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsVouData,"COMPANY_ID",0));
                                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsVouData,"VOUCHER_NO",""));
                                    objItem.setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsVouData,"VOUCHER_DATE","0000-00-00"));
                                    objItem.setAttribute("VOUCHER_TYPE",UtilFunctions.getString(rsVouData,"VOUCHER_TYPE",""));
                                    objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsVouData,"BOOK_CODE",""));
                                    objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsVouData,"MAIN_ACCOUNT_CODE",""));
                                    objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsVouData,"SUB_ACCOUNT_CODE",""));
                                    objItem.setAttribute("EFFECT",UtilFunctions.getString(rsVouData,"EFFECT",""));
                                    objItem.setAttribute("LEGACY_NO",UtilFunctions.getString(rsVouData,"LEGACY_NO",""));
                                    
                                    
                                    double C_Amount=0,D_Amount=0;
                                    
                                    String str = "SELECT VOUHEAD.COMPANY_ID,VOUHEAD.VOUCHER_NO,VOUHEAD.VOUCHER_DATE,VOUHEAD.LEGACY_NO,VOUHEAD.BOOK_CODE, "+
                                    "VOUDTL.MAIN_ACCOUNT_CODE,VOUDTL.SUB_ACCOUNT_CODE, VOUDTL.EFFECT, "+
                                    "CASE WHEN VOUDTL.EFFECT = 'C' THEN SUM(VOUDTL.AMOUNT) ELSE '0' END C_AMOUNT,  "+
                                    "CASE WHEN VOUDTL.EFFECT = 'D' THEN SUM(VOUDTL.AMOUNT) ELSE '0' END D_AMOUNT "+
                                    "FROM FINANCE.D_FIN_VOUCHER_HEADER VOUHEAD, FINANCE.D_FIN_VOUCHER_DETAIL_EX VOUDTL  "+
                                    "WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 "+
                                    "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO  "+
                                    "AND VOUDTL.MAIN_ACCOUNT_CODE='"+ txtAccountCode.getText().trim() +"' " +
                                    "AND VOUHEAD.VOUCHER_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) +"' "+
                                    "AND VOUHEAD.VOUCHER_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) +"'   "+
                                    "AND VOUHEAD.VOUCHER_NO= '"+ VoucherNo +"' "+
                                    "GROUP BY VOUHEAD.VOUCHER_NO ,VOUDTL.EFFECT "+
                                    "ORDER BY VOUHEAD.VOUCHER_DATE,VOUHEAD.VOUCHER_NO  ";
                                    ResultSet rsVouAmt = data.getResult(str);
                                    String Effect = UtilFunctions.getString(rsVouAmt,"EFFECT","");
                                    
                                    rsVouAmt.first();
                                    if (rsVouAmt.getRow() > 0) {
                                        while(!rsVouAmt.isAfterLast()) {
                                            if (Effect.trim().equals("C")) {
                                                C_Amount += UtilFunctions.getDouble(rsVouAmt,"C_AMOUNT",0);
                                                D_Amount += UtilFunctions.getDouble(rsVouAmt,"D_AMOUNT",0);
                                            }
                                            else {
                                                C_Amount += UtilFunctions.getDouble(rsVouAmt,"C_AMOUNT",0);
                                                D_Amount += UtilFunctions.getDouble(rsVouAmt,"D_AMOUNT",0);
                                            }
                                            rsVouAmt.next();
                                        }
                                    }
                                    
                                    if (cntItem == 1) {
                                        Total_credit+=C_Amount;
                                        Total_debit+=D_Amount;
                                    }
                                    
                                    //System.out.println("VoucherNo="+VoucherNo+" cAmount="+C_Amount + " dAmount="+D_Amount);
                                    objItem.setAttribute("C_AMOUNT", Double.toString(EITLERPGLOBAL.round(C_Amount,2)));
                                    objItem.setAttribute("D_AMOUNT",Double.toString(EITLERPGLOBAL.round(D_Amount,2)));
                                    
                                    objItem.setAttribute("REMARKS",UtilFunctions.getString(rsVouData,"REMARKS",""));
                                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsVouData,"INVOICE_NO",""));
                                    objItem.setAttribute("INVOICE_DATE",UtilFunctions.getString(rsVouData,"INVOICE_DATE","0000-00-00"));
                                    objItem.setAttribute("ACCOUNT_NAME",txtAccountName.getText().trim());
                                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsVouData,"PO_NO",""));
                                    objItem.setAttribute("PARTY_NAME",Partyname);
                                    
                                    objItem.setAttribute("REF_NO",UtilFunctions.getString(rsVouData,"GRN_NO",""));
                                    
                                    objItem.setAttribute("REF_DATE",UtilFunctions.getString(rsItem,"REF_DATE","0000-00-00"));
                                    objItem.setAttribute("ITEM_ID",UtilFunctions.getString(rsItem,"ITEM_ID",""));
                                    
                                    String Item_Desc = UtilFunctions.getString(rsItem,"ITEM_DESCRIPTION","");
                                    if (! Item_Desc.trim().equals("")) {
                                        Item_Desc = Item_Desc;
                                    }
                                    else {
                                        Item_Desc = data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID = '"+ UtilFunctions.getString(rsItem,"ITEM_ID","") +"' AND COMPANY_ID = "+ Ref_Com_Id +" AND CANCELLED=0 ",URL);
                                    }
                                    
                                    objItem.setAttribute("ITEM_DESCRIPTION",Item_Desc.trim());
                                    objItem.setAttribute("QTY",UtilFunctions.getInt(rsItem,"QTY",0));
                                    
                                    objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsVouData,"INVOICE_NO","")+UtilFunctions.getString(rsVouData,"PO_NO","")+UtilFunctions.getString(rsVouData,"CHEQUE_NO",""));
                                    
                                    colVoucherItems.put(Long.toString(Counter),objItem);
                                    
                                    LastVoucherNo = VoucherNo;
                                    
                                    rsItem.next();
                                }
                            }
                        }
                        else {
                            frmRptfixedAssets_AccountDtl objItem = new frmRptfixedAssets_AccountDtl();
                    
                            Counter++;
                            
                            objItem.setAttribute("SR_NO",Integer.toString(Counter));
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsVouData,"COMPANY_ID",0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsVouData,"VOUCHER_NO",""));
                            objItem.setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsVouData,"VOUCHER_DATE","0000-00-00"));
                            objItem.setAttribute("VOUCHER_TYPE",UtilFunctions.getString(rsVouData,"VOUCHER_TYPE",""));
                            objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsVouData,"BOOK_CODE",""));
                            objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsVouData,"MAIN_ACCOUNT_CODE",""));
                            objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsVouData,"SUB_ACCOUNT_CODE",""));
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsVouData,"EFFECT",""));
                            objItem.setAttribute("LEGACY_NO",UtilFunctions.getString(rsVouData,"LEGACY_NO",""));
                            
                            
                            String str = "SELECT VOUHEAD.COMPANY_ID,VOUHEAD.VOUCHER_NO,VOUHEAD.VOUCHER_DATE,VOUHEAD.LEGACY_NO,VOUHEAD.BOOK_CODE, "+
                            "VOUDTL.MAIN_ACCOUNT_CODE,VOUDTL.SUB_ACCOUNT_CODE, VOUDTL.EFFECT, "+
                            "CASE WHEN VOUDTL.EFFECT = 'C' THEN SUM(VOUDTL.AMOUNT) ELSE '0' END C_AMOUNT,  "+
                            "CASE WHEN VOUDTL.EFFECT = 'D' THEN SUM(VOUDTL.AMOUNT) ELSE '0' END D_AMOUNT "+
                            "FROM FINANCE.D_FIN_VOUCHER_HEADER VOUHEAD, FINANCE.D_FIN_VOUCHER_DETAIL_EX VOUDTL  "+
                            "WHERE VOUHEAD.APPROVED=1 AND VOUHEAD.CANCELLED=0 "+
                            "AND VOUHEAD.COMPANY_ID=VOUDTL.COMPANY_ID AND VOUHEAD.VOUCHER_NO=VOUDTL.VOUCHER_NO  "+
                            "AND VOUDTL.MAIN_ACCOUNT_CODE='"+ txtAccountCode.getText().trim() +"' " +
                            "AND VOUHEAD.VOUCHER_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim()) +"' "+
                            "AND VOUHEAD.VOUCHER_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim()) +"'   "+
                            "AND VOUHEAD.VOUCHER_NO= '"+ VoucherNo +"' "+
                            "GROUP BY VOUHEAD.VOUCHER_NO ,VOUDTL.EFFECT "+
                            "ORDER BY VOUHEAD.VOUCHER_DATE,VOUHEAD.VOUCHER_NO  ";
                            ResultSet rsVouAmt = data.getResult(str);
                            double C_Amount=0,D_Amount=0;
                            String Effect = UtilFunctions.getString(rsVouAmt,"EFFECT","");
                            
                            rsVouAmt.first();
                            if (rsVouAmt.getRow() > 0) {
                                while(!rsVouAmt.isAfterLast()) {
                                    if (Effect.trim().equals("C")) {
                                        C_Amount += UtilFunctions.getDouble(rsVouAmt,"C_AMOUNT",0);
                                        D_Amount += UtilFunctions.getDouble(rsVouAmt,"D_AMOUNT",0);
                                    }
                                    else {
                                        C_Amount += UtilFunctions.getDouble(rsVouAmt,"C_AMOUNT",0);
                                        D_Amount += UtilFunctions.getDouble(rsVouAmt,"D_AMOUNT",0);
                                    }
                                    rsVouAmt.next();
                                }
                            }
                            
                            Total_credit+=C_Amount;
                            Total_debit+=D_Amount;
                            
                            System.out.println("VoucherNo="+VoucherNo+" cAmount="+C_Amount + " dAmount="+D_Amount);
                            objItem.setAttribute("C_AMOUNT", Double.toString(EITLERPGLOBAL.round(C_Amount,2)));
                            objItem.setAttribute("D_AMOUNT",Double.toString(EITLERPGLOBAL.round(D_Amount,2)));
                            
                            objItem.setAttribute("REMARKS",UtilFunctions.getString(rsVouData,"REMARKS",""));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsVouData,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",UtilFunctions.getString(rsVouData,"INVOICE_DATE","0000-00-00"));
                            objItem.setAttribute("ACCOUNT_NAME",txtAccountName.getText().trim());
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsVouData,"PO_NO",""));
                            
                            objItem.setAttribute("PARTY_NAME",Partyname);
                            
                            objItem.setAttribute("REF_NO",UtilFunctions.getString(rsVouData,"GRN_NO",""));
                            
                            objItem.setAttribute("REF_DATE","0000-00-00");
                            objItem.setAttribute("ITEM_ID","");
                            
                            objItem.setAttribute("ITEM_DESCRIPTION","");
                            objItem.setAttribute("QTY",0);
                            
                            objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsVouData,"INVOICE_NO","")+UtilFunctions.getString(rsVouData,"PO_NO","")+UtilFunctions.getString(rsVouData,"CHEQUE_NO",""));
                            
                            colVoucherItems.put(Long.toString(Counter),objItem);
                            
                            LastVoucherNo = VoucherNo;
                        }
                        
                    }
                    rsVouData.next();
                }
            }
            rsVouData.close();
            objData = new TReportWriter.SimpleDataProvider.TTable();
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData = new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("COMPANY_ID");
            objReportData.AddColumn("VOUCHER_NO");
            objReportData.AddColumn("VOUCHER_DATE");
            objReportData.AddColumn("BOOK_CODE");
            objReportData.AddColumn("MAIN_ACCOUNT_CODE");
            objReportData.AddColumn("SUB_ACCOUNT_CODE");
            objReportData.AddColumn("C_AMOUNT");
            objReportData.AddColumn("D_AMOUNT");
            objReportData.AddColumn("REMARKS");
            objReportData.AddColumn("REF_NO");
            objReportData.AddColumn("REF_DATE");
            objReportData.AddColumn("PO_NO");
            objReportData.AddColumn("INVOICE_NO");
            objReportData.AddColumn("INVOICE_DATE");
            objReportData.AddColumn("ITEM_ID");
            objReportData.AddColumn("ITEM_DESCRIPTION");
            objReportData.AddColumn("QTY");
            objReportData.AddColumn("ACCOUNT_NAME");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("GROUP");
            objReportData.AddColumn("LEGACY_NO");
            
            Counter = 0;
            frmRptfixedAssets_AccountDtl ObjItem = null;
            //System.out.println("Size = " + colVoucherItems.size());
            if(colVoucherItems.size()>0) {
                for(int i=1;i<=colVoucherItems.size();i++) {
                    ObjItem=(frmRptfixedAssets_AccountDtl)colVoucherItems.get(Integer.toString(i));
                    
                    Counter ++;
                    //System.out.println("Counter = " + Counter);
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("COMPANY_ID",ObjItem.getAttribute("COMPANY_ID").getString());
                    objRow.setValue("VOUCHER_NO",ObjItem.getAttribute("VOUCHER_NO").getString());
                    objRow.setValue("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ObjItem.getAttribute("VOUCHER_DATE").getString()));
                    objRow.setValue("BOOK_CODE",ObjItem.getAttribute("BOOK_CODE").getString());
                    objRow.setValue("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                    objRow.setValue("SUB_ACCOUNT_CODE",ObjItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                    objRow.setValue("C_AMOUNT",ObjItem.getAttribute("C_AMOUNT").getString());
                    objRow.setValue("D_AMOUNT",ObjItem.getAttribute("D_AMOUNT").getString());
                    objRow.setValue("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                    objRow.setValue("REF_NO",ObjItem.getAttribute("REF_NO").getString());
                    objRow.setValue("REF_DATE",EITLERPGLOBAL.formatDate(ObjItem.getAttribute("REF_DATE").getString()));
                    objRow.setValue("PO_NO",ObjItem.getAttribute("PO_NO").getString());
                    objRow.setValue("INVOICE_NO",ObjItem.getAttribute("INVOICE_NO").getString());
                    objRow.setValue("INVOICE_DATE",EITLERPGLOBAL.formatDate(ObjItem.getAttribute("INVOICE_DATE").getString()));
                    objRow.setValue("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                    objRow.setValue("ITEM_DESCRIPTION",ObjItem.getAttribute("ITEM_DESCRIPTION").getString());
                    objRow.setValue("QTY",Integer.toString(ObjItem.getAttribute("QTY").getInt()));
                    objRow.setValue("ACCOUNT_NAME",ObjItem.getAttribute("ACCOUNT_NAME").getString());
                    objRow.setValue("PARTY_NAME",ObjItem.getAttribute("PARTY_NAME").getString());
                    objRow.setValue("GROUP", ObjItem.getAttribute("GROUP").getString());
                    objRow.setValue("LEGACY_NO", ObjItem.getAttribute("LEGACY_NO").getString());
                    
                    objReportData.AddRow(objRow);
                    
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_ID",Integer.toString(Comp_ID));
            Parameters.put("MAIN_ACCOUNT_CODE",txtAccountCode.getText().trim() + "(" + txtAccountName.getText().trim() + ")");
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("TOTAL_CREDIT",Double.toString(EITLERPGLOBAL.round(Total_credit,2)));
            Parameters.put("TOTAL_DEBIT",Double.toString(EITLERPGLOBAL.round(Total_debit,2)));
            Parameters.put("NET_AMOUNT",Double.toString(EITLERPGLOBAL.round(Total_debit,2) - EITLERPGLOBAL.round(Total_credit,2)));
            //System.out.println("Call....3");
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptAccount_FixedAssetsVoucherDtl.rpt",Parameters,objReportData);
            //objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptAccount_LedgerVoucherDtl1.rpt",Parameters,objReportData);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean Validate() {
        //Form level validations
        if(txtAccountCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Account Code");
            return false;
        }
        
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
