/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.FeltSales.Order;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.*;
import EITLERP.FeltSales.common.LOV;
import EITLERP.Finance.UtilFunctions;
import TReportWriter.TReportEngine;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.text.MaskFormatter;


public class frmReportFeltOrder extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
    private TReportEngine objEngine=new TReportEngine();
    
    public void init() {
        System.gc();
        setSize(550,235);
        initComponents();
         try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(S_O_DATE_TO);
            
            MaskFormatter dateMask1 = new MaskFormatter("##/##/####");
            dateMask1.setPlaceholderCharacter('_');
            dateMask1.install(S_O_DATE_FROM);
        } catch (ParseException ex) {
            System.out.println("Error on Mask : "+ex.getLocalizedMessage());
        }
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        S_O_DATE_TO = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        PARTY_CODE = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        S_O_DATE_FROM = new javax.swing.JFormattedTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Report :: Felt Sales Order");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 190, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Show Report");
        cmdFind.setToolTipText("");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setMargin(new java.awt.Insets(0, 14, 0, 14));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(320, 170, 110, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(450, 170, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);
        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(140, 10, 160, 27);

        jLabel3.setDisplayedMnemonic('G');
        jLabel3.setText("Document No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 10, 130, 17);
        jPanel1.add(S_O_DATE_TO);
        S_O_DATE_TO.setBounds(340, 40, 160, 30);

        jLabel2.setText("S.O.Date   -  From");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 47, 130, 20);

        PARTY_CODE.setToolTipText("Press F1 key for search Party Code");
        PARTY_CODE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PARTY_CODEFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PARTY_CODEFocusLost(evt);
            }
        });
        PARTY_CODE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PARTY_CODEKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PARTY_CODEKeyTyped(evt);
            }
        });
        jPanel1.add(PARTY_CODE);
        PARTY_CODE.setBounds(140, 80, 160, 30);

        jLabel9.setText("Party Code");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 80, 100, 30);

        jLabel4.setText("To");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(310, 47, 30, 20);
        jPanel1.add(S_O_DATE_FROM);
        S_O_DATE_FROM.setBounds(140, 40, 160, 30);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 520, 120);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
       
        ReportShow();
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed

    private void PARTY_CODEFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusGained
        //TODO add your handling code here:
    }//GEN-LAST:event_PARTY_CODEFocusGained

    private void PARTY_CODEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusLost
        
    }//GEN-LAST:event_PARTY_CODEFocusLost

    private void PARTY_CODEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyPressed

        if (evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010'";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    PARTY_CODE.setText(aList.ReturnVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_PARTY_CODEKeyPressed
    private void ReportShow() {
        
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            
            objReportData.AddColumn("S_ORDER_NO");
            objReportData.AddColumn("S_ORDER_DATE");
            objReportData.AddColumn("PARTY_CODE");
            objReportData.AddColumn("PARTY_NAME");
            objReportData.AddColumn("REGION");
            objReportData.AddColumn("MACHINE_NO");
            objReportData.AddColumn("POSITION");
            objReportData.AddColumn("PIECE_NO");
            objReportData.AddColumn("PRODUCT_CODE");
            objReportData.AddColumn("S_GROUP");
            objReportData.AddColumn("SYN_PER");
            objReportData.AddColumn("LENGTH");
            objReportData.AddColumn("WIDTH");
            objReportData.AddColumn("GSM");
            objReportData.AddColumn("OV_RATE");
            objReportData.AddColumn("OV_AMT");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("S_ORDER_NO","");
            objOpeningRow.setValue("S_ORDER_DATE","");
            objOpeningRow.setValue("PARTY_CODE","");
            objOpeningRow.setValue("PARTY_NAME","");
            objOpeningRow.setValue("REGION","");
            objOpeningRow.setValue("MACHINE_NO","");
            objOpeningRow.setValue("POSITION","");
            objOpeningRow.setValue("PIECE_NO","");
            objOpeningRow.setValue("PRODUCT_CODE","");
            objOpeningRow.setValue("S_GROUP","");
            objOpeningRow.setValue("SYN_PER","");
            objOpeningRow.setValue("LENGTH","");
            objOpeningRow.setValue("WIDTH","");
            objOpeningRow.setValue("GSM","");
            objOpeningRow.setValue("OV_RATE","");
            objOpeningRow.setValue("OV_AMT","");
            
            String query_str="";
            
            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            DateFormat df1 = new SimpleDateFormat("yyyy-mm-dd");
            
            if(!"".equals(txtDocNo.getText()))
            {
                query_str = query_str + " AND B.S_ORDER_NO = "+txtDocNo.getText();
            }
                
            if(!"".equals(PARTY_CODE.getText()))
            {
                query_str = query_str + " AND A.PARTY_CODE = "+PARTY_CODE.getText();
            }
            if(!S_O_DATE_FROM.getText().equals("__/__/____") && !S_O_DATE_TO.getText().equals("__/__/____"))
            {    
                if((EITLERPGLOBAL.formatDateDB(S_O_DATE_FROM.getText()) != null) && EITLERPGLOBAL.formatDateDB(S_O_DATE_TO.getText()) != null)
                {
                    query_str = query_str + " AND (A.S_ORDER_DATE  BETWEEN  '"+df1.format(df.parse(S_O_DATE_FROM.getText()))+"' AND '" + df1.format(df.parse(S_O_DATE_TO.getText())) + "') ";
                }
            }
            else if(!S_O_DATE_FROM.getText().equals("__/__/____")) 
            {
                if(EITLERPGLOBAL.formatDateDB(S_O_DATE_FROM.getText()) != null)
                {
                    query_str = query_str + " AND A.S_ORDER_DATE  >=  '"+df1.format(df.parse(S_O_DATE_FROM.getText()))+"'";
                }
            }
            else if(!S_O_DATE_TO.getText().equals("__/__/____")) 
            {
                if(EITLERPGLOBAL.formatDateDB(S_O_DATE_TO.getText()) != null)
                {
                    query_str = query_str + " AND A.S_ORDER_DATE  <=  '"+df1.format(df.parse(S_O_DATE_TO.getText()))+"'";  
                }
            }
            //String strSQL="SELECT A.PKG_BALE_NO,A.PKG_BALE_DATE,A.PKG_PARTY_CODE,A.PKG_PARTY_NAME,PKG_TRANSPORT_MODE, A.PKG_STATION,A.PKG_BOX_SIZE, B.PKG_PIECE_NO,B.PKG_LENGTH,B.PKG_WIDTH,B.PKG_GSM,B.PKG_SQM,B.PKG_ORDER_NO, B.PKG_ORDER_DATE,B.PKG_MCN_POSITION_DESC,B.PKG_STYLE,B.PKG_SYN_PER,B.PKG_PRODUCT_CODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER A,PRODUCTION.FELT_PKG_SLIP_DETAIL B WHERE A.PKG_PARTY_CODE='"+txtPartyCode.getText().trim()+"' GROUP BY A.PKG_PARTY_CODE;";
            String strSQL="SELECT A.S_ORDER_DATE,A.PARTY_CODE,substr(A.PARTY_NAME,1,25) AS PARTY_NAME,A.REGION, B.MACHINE_NO,B.POSITION,B.PIECE_NO,B.PRODUCT_CODE,B.S_GROUP,B.SYN_PER,B.LENGTH,B.WIDTH,B.GSM,B.OV_RATE,B.OV_AMT  FROM PRODUCTION.FELT_SALES_ORDER_HEADER A,PRODUCTION.FELT_SALES_ORDER_DETAIL B where A.APPROVED=1 AND B.PIECE_NO!=''  AND A.PARTY_CODE!='' "+query_str+" and A.S_ORDER_NO = B.S_ORDER_NO";
            
            System.out.println(strSQL);
            
            ResultSet rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            int Counter = 0;
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter ++;
                    objRow=objReportData.newRow();
                    
                    objRow.setValue("S_ORDER_NO",UtilFunctions.getString(rsTmp,"S_ORDER_NO",""));
                    objRow.setValue("S_ORDER_DATE",UtilFunctions.getString(rsTmp,"S_ORDER_DATE",""));
                    objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE",""));
                    objRow.setValue("PARTY_NAME",UtilFunctions.getString(rsTmp,"PARTY_NAME",""));
                    objRow.setValue("REGION",UtilFunctions.getString(rsTmp,"REGION",""));
                    objRow.setValue("MACHINE_NO",UtilFunctions.getString(rsTmp,"MACHINE_NO",""));
                    objRow.setValue("POSITION",UtilFunctions.getString(rsTmp,"POSITION",""));
                    objRow.setValue("PIECE_NO",UtilFunctions.getString(rsTmp,"PIECE_NO",""));
                    objRow.setValue("PRODUCT_CODE",UtilFunctions.getString(rsTmp,"PRODUCT_CODE",""));
                    objRow.setValue("S_GROUP",UtilFunctions.getString(rsTmp,"S_GROUP",""));
                    objRow.setValue("SYN_PER",UtilFunctions.getString(rsTmp,"SYN_PER",""));
                    objRow.setValue("LENGTH",UtilFunctions.getString(rsTmp,"LENGTH",""));
                    objRow.setValue("WIDTH",UtilFunctions.getString(rsTmp,"WIDTH",""));
                    objRow.setValue("GSM",UtilFunctions.getString(rsTmp,"GSM",""));
                    objRow.setValue("OV_RATE",UtilFunctions.getString(rsTmp,"OV_RATE",""));
                    objRow.setValue("OV_AMT",UtilFunctions.getString(rsTmp,"OV_AMT",""));
                    
                    objReportData.AddRow(objRow);
                    
                    rsTmp.next();
                }
            }
            
            int Comp_ID = EITLERPGLOBAL.gCompanyID;
            
            HashMap Parameters=new HashMap();
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            //System.out.println("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptFeltOrder.rpt");
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptFeltOrder.rpt",Parameters,objReportData);
            
        }
        catch(Exception e) {
            System.out.println("Error : "+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    private void PARTY_CODEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyTyped

    }//GEN-LAST:event_PARTY_CODEKeyTyped
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField PARTY_CODE;
    private javax.swing.JFormattedTextField S_O_DATE_FROM;
    private javax.swing.JFormattedTextField S_O_DATE_TO;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDocNo;
    // End of variables declaration//GEN-END:variables
}
