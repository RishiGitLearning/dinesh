/*
 * rptNewPO.java
 *
 * Created on July 15, 2004, 3:12 PM
 */

package EITLERP.Purchase;

/*<APPLET CODE=frmNewPO.class WIDTH=404 HEIGHT=228></APPLET>*/
/**
 *
 * @author  root
 */
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import EITLERP.*;
//import EScriptlets.*;


public class rptNewPOOld extends javax.swing.JApplet {
    
    
    private EITLComboModel cmbMonthModel;
    private EITLComboModel cmbPOTypeModel;
    
    /** Initializes the applet rptNewPO */
    public void init() {
        setSize(410,294);
        initComponents();
        GenerateCombo();
        
        int Month=Integer.parseInt(EITLERPGLOBAL.getCurrentDate().substring(3,5));
        int Year=Integer.parseInt(EITLERPGLOBAL.FinToDateDB.substring(0,4));
        
        if(Month<=12&&Month>=4)
        {
         Year--;   
        }
                
        EITLERPGLOBAL.setComboIndex(cmbMonth,Month);
        lblYear.setText(Integer.toString(Year));
        
        
        if(EITLERPGLOBAL.gCompanyID==3) //3 - Ankleshwar
        {
            txtFile.setText("cp.txt");
        }
        else {
            txtFile.setText("g.txt");
        }
        
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbPOType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        cmdBrowse = new javax.swing.JButton();
        cmdGenerate = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        lblYear = new javax.swing.JLabel();
        cmbMonth = new javax.swing.JComboBox();
        chkPOType = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        opgBrief = new javax.swing.JRadioButton();
        opgShort = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setText("Print Text file for generated P.O. for current month");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 8, 342, 15);

        jLabel2.setText("Month");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(21, 55, 40, 15);

        cmbPOType.setEnabled(false);
        getContentPane().add(cmbPOType);
        cmbPOType.setBounds(152, 130, 222, 24);

        jLabel3.setText("File");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(29, 91, 33, 15);

        getContentPane().add(txtFile);
        txtFile.setBounds(65, 88, 271, 21);

        cmdBrowse.setText("...");
        cmdBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBrowseActionPerformed(evt);
            }
        });

        getContentPane().add(cmdBrowse);
        cmdBrowse.setBounds(340, 88, 34, 21);

        cmdGenerate.setText("Generate");
        cmdGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGenerateActionPerformed(evt);
            }
        });

        getContentPane().add(cmdGenerate);
        cmdGenerate.setBounds(72, 239, 102, 25);

        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(186, 239, 102, 25);

        getContentPane().add(lblYear);
        lblYear.setBounds(196, 54, 66, 21);

        cmbMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonthItemStateChanged(evt);
            }
        });

        getContentPane().add(cmbMonth);
        cmbMonth.setBounds(67, 51, 115, 24);

        chkPOType.setText("PO Type");
        chkPOType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkPOTypeItemStateChanged(evt);
            }
        });

        getContentPane().add(chkPOType);
        chkPOType.setBounds(68, 131, 76, 23);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        opgBrief.setSelected(true);
        opgBrief.setText("Brief Report");
        buttonGroup1.add(opgBrief);
        jPanel1.add(opgBrief);
        opgBrief.setBounds(8, 5, 99, 23);

        opgShort.setText("Short Report");
        buttonGroup1.add(opgShort);
        jPanel1.add(opgShort);
        opgShort.setBounds(120, 4, 114, 23);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(18, 191, 245, 32);

        jLabel4.setText("Report Format");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 174, 99, 15);

    }//GEN-END:initComponents

    private void cmbMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonthItemStateChanged
        // TODO add your handling code here:
        try
        {
        int Month=EITLERPGLOBAL.getComboCode(cmbMonth);
        int Year=Integer.parseInt(EITLERPGLOBAL.FinToDateDB.substring(0,4));
        
        if(Month<=12&&Month>=4)
        {
         Year--;   
        }

        lblYear.setText(Integer.toString(Year));
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_cmbMonthItemStateChanged
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null," W "+getWidth()+" H "+getHeight());
    }//GEN-LAST:event_formMouseClicked
    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formKeyPressed
    
    private void chkPOTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkPOTypeItemStateChanged
        // TODO add your handling code here:
        cmbPOType.setEnabled(chkPOType.isSelected());
    }//GEN-LAST:event_chkPOTypeItemStateChanged
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGenerateActionPerformed
        // TODO add your handling code here:
        EITLERPGLOBAL.ChangeCursorToWait(this);
        
        if(opgBrief.isSelected()) {
            GenerateFileBrief();
        }
        else {
            GenerateFileShort();
        }
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }//GEN-LAST:event_cmdGenerateActionPerformed
    
    private void cmdBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBrowseActionPerformed
        // TODO add your handling code here:
        FileDialog FileDialog=new FileDialog(findParentFrame(this));
        FileDialog.show();
        if(!FileDialog.getDirectory().equals("")) {
            txtFile.setText(FileDialog.getDirectory()+FileDialog.getFile());
        }
    }//GEN-LAST:event_cmdBrowseActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkPOType;
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JComboBox cmbPOType;
    private javax.swing.JButton cmdBrowse;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdGenerate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblYear;
    private javax.swing.JRadioButton opgBrief;
    private javax.swing.JRadioButton opgShort;
    private javax.swing.JTextField txtFile;
    // End of variables declaration//GEN-END:variables
    
    
    //Recurses through the hierarchy of classes
    //until it finds Frame
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    
    private void GenerateCombo() {
        
        //--- Generate Type Combo ------//
        cmbMonthModel=new EITLComboModel();
        cmbMonth.removeAllItems();
        cmbMonth.setModel(cmbMonthModel);
        
        ComboData aData=new ComboData();
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
        //===============================//
        
        
        //--- Generate Type Combo ------//
        cmbPOTypeModel=new EITLComboModel();
        cmbPOType.removeAllItems();
        cmbPOType.setModel(cmbPOTypeModel);
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="PO Gen.";
        cmbPOTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=2;
        aData.Text="PO Gen. without Indent";
        cmbPOTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="PO A Class";
        cmbPOTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=4;
        aData.Text="PO Raw Material";
        cmbPOTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=5;
        aData.Text="PO Spares";
        cmbPOTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=6;
        aData.Text="PO Capital";
        cmbPOTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=7;
        aData.Text="PO Contract";
        cmbPOTypeModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=8;
        aData.Text="Service Contarct";
        cmbPOTypeModel.addElement(aData);
        
        
    }
    
    
    private void GenerateFileShort() {
        try {
            Connection tmpConn=data.getConn();
            Statement stTmp=null,stTmp2=null;
            ResultSet rsTmp=null,rsTmp2=null,rsSupp=null;
            
            int selMonth=EITLERPGLOBAL.getComboCode(cmbMonth);
            String strSQL="";
            
            if(txtFile.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please specify the name of file");
                txtFile.requestFocus();
                return;
            }
            
            BufferedWriter aFile=new BufferedWriter(new FileWriter(new File(txtFile.getText())));
            
            
            strSQL="SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MONTH(PO_DATE)="+selMonth+" AND YEAR(PO_DATE)="+lblYear.getText()+" AND CANCELLED=0";
            
            if(chkPOType.isSelected()) {
                strSQL+=" AND PO_TYPE="+EITLERPGLOBAL.getComboCode(cmbPOType);
            }
            
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                String fLine="";
                
                //fLine=rsTmp.getString("PO_NO").trim();
                
                String PONO=rsTmp.getString("PO_NO");
                String SuppID=rsTmp.getString("SUPP_ID");
            
                
                if(EITLERPGLOBAL.gCompanyID==3 && rsTmp.getInt("PO_TYPE")==3) //3 - Ankleshwar- PO A CLASS
                {
                    fLine+="C";
                    fLine+="A";
                    fLine+=" ";
                                                    
                }
                else if(EITLERPGLOBAL.gCompanyID==2 && rsTmp.getInt("PO_TYPE")==3)  // Baroda - PO A CLASS
                {
                    fLine+="D ";
                    fLine+=" ";
                }
                else
                {
                    fLine+="RM";
                    fLine+=" ";
                    
                }   
                
                
                if(rsTmp.getInt("PO_TYPE")==3)//It is only for PO AClass.
                {
                    String FirstStr="";
                    
                    
                    if(PONO.length()==10) {
                        FirstStr=PONO.substring(2,8);
                    }
                    
                    if(PONO.length()==8){
                        FirstStr=PONO.substring(2,8);
                    }
                    
                    fLine+=FirstStr;
                }
                else if(rsTmp.getInt("PO_TYPE")==4)
                {
                    fLine+=PONO.substring(2,8);
                }
                else if(rsTmp.getInt("PO_TYPE")==1)//It is only for PO General.
                {
                    String SecondStr=PONO.substring(2,7);
                    fLine+=SecondStr;
                }
                
                
                //Supplier Information
                stTmp2=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp2=stTmp2.executeQuery("SELECT * FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPPLIER_CODE='"+rsTmp.getString("SUPP_ID")+"'");
                rsTmp2.first();
                
                String CreditDays="000";
                
                String ptc="00";
                
                if(rsTmp2.getRow()>0) {
                    int crDays=rsTmp2.getInt("PAYMENT_DAYS");
                    CreditDays=EITLERPGLOBAL.Replicate("0",3-Integer.toString(crDays).trim().length())+Integer.toString(crDays).trim();
                    
                    
                    rsSupp=data.getResult("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+SuppID+"' ");
                    rsSupp.first();
                    
                    if(rsSupp.getRow()>0)
                    {
                      int nSuppID=rsSupp.getInt("SUPP_ID");
                      rsSupp=data.getResult("SELECT * FROM D_COM_SUPP_TERMS WHERE SUPP_ID="+nSuppID+" AND TERM_TYPE='P' AND TERM_CODE<>0 ");
                      rsSupp.first();
                      
                      if(rsSupp.getRow()>0)
                      {
                        int TermCode=rsSupp.getInt("TERM_CODE");
                        ptc=Integer.toString(TermCode).trim();
                        ptc=EITLERPGLOBAL.Replicate("0", 2-ptc.trim().length())+ptc;
                      }
                    }
                    
                    //int ptCode=rsTmp2.getInt("PTC");
                    //ptc=EITLERPGLOBAL.Replicate("0", 2-Integer.toString(ptCode).trim().length())+Integer.toString(ptCode).trim();
                    
                    fLine+="     ";
                    fLine+=ptc;
                    
                    fLine+="     ";
                    fLine+=CreditDays;
                    
                }
                
                
                String PODate=rsTmp.getString("PO_DATE");
                
                String dYear=PODate.substring(0,4);
                String dMonth=PODate.substring(5,7);
                String dDay=PODate.substring(8,10);
                
                
                fLine+="     ";
                fLine=fLine+dDay+"/"+dMonth+"/"+dYear;
                
                
                aFile.write(fLine);
                aFile.newLine();
                rsTmp.next();
            }
            
            aFile.close();
            JOptionPane.showMessageDialog(null,"File has been created");
            
            //tmpConn.close();
            stTmp.close();
            stTmp2.close();
            rsTmp.close();
            rsTmp2.close();
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
    
    
    private void GenerateFileBrief() {
        try {
            Connection tmpConn=data.getConn();
            Statement stTmp=null,stTmp2=null;
            ResultSet rsTmp=null,rsTmp2=null,rsSupp=null;
            
            
            int selMonth=EITLERPGLOBAL.getComboCode(cmbMonth);
            String strSQL="";
            
            if(txtFile.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please specify the name of file");
                txtFile.requestFocus();
                return;
            }
            
            BufferedWriter aFile=new BufferedWriter(new FileWriter(new File(txtFile.getText())));
            
            strSQL="SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MONTH(PO_DATE)="+selMonth+" AND YEAR(PO_DATE)="+lblYear.getText()+" AND CANCELLED=0 ";
            
            if(chkPOType.isSelected()) {
                strSQL=strSQL+" AND PO_TYPE="+EITLERPGLOBAL.getComboCode(cmbPOType);
            }
                        
            strSQL=strSQL+"  ORDER BY PO_DATE"; 
            
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                String fLine="";
                String PONO="";
                String SuppID="";
                
                PONO=rsTmp.getString("PO_NO").trim();
                SuppID=rsTmp.getString("SUPP_ID").trim();
                
                if(EITLERPGLOBAL.gCompanyID==2 && rsTmp.getInt("PO_TYPE")==1)
                {
                    fLine+="B";
                }
                else if(EITLERPGLOBAL.gCompanyID==2 && rsTmp.getInt("PO_TYPE")==2)
                {
                    fLine+="W";
                }   
                else if(EITLERPGLOBAL.gCompanyID==3 && rsTmp.getInt("PO_TYPE")==1)
                {
                    fLine+="A";
                }   
                else if(EITLERPGLOBAL.gCompanyID==3 && rsTmp.getInt("PO_TYPE")==2)     
                {
                    fLine+="P";
                }
                    
                    
                if(rsTmp.getInt("PO_TYPE")==3)//It is only for PO AClass.
                {
                    String FirstStr="";
                                       
                    if(PONO.length()==10) {
                        FirstStr=PONO.substring(2,8);
                    }
                    
                    if(PONO.length()==8){
                        FirstStr=PONO.substring(2,8);
                    }
                    
                    fLine+=FirstStr;
                }
                else
                {
                    if(PONO.length()>=7)
                    {
                    fLine+=PONO.substring(2,7);    
                    }
                    else
                    {
                    fLine+=PONO.substring(2);    
                    }
                    
                }
                
                
               /* else if(rsTmp.getInt("PO_TYPE")==1)//It is only for PO General.
                {
                    String SecondStr="";
                    SecondStr=PONO.substring(0,1);
                    SecondStr+=PONO.substring(2,7);
                    fLine+=SecondStr;
                }*/
                
                if(EITLERPGLOBAL.gCompanyID==3) //3 - Ankleshwar
                {
                    fLine+="C";
                    fLine+="P";
                }
                else {
                    fLine+="G";
                    fLine+=" ";
                }
                
                
                String PODate=rsTmp.getString("PO_DATE");
                
                String dYear=PODate.substring(0,4);
                String dMonth=PODate.substring(5,7);
                String dDay=PODate.substring(8,10);
                
                fLine+=dYear;
                fLine+=dMonth;
                fLine+=dDay;
                
                //Supplier Information
                stTmp2=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp2=stTmp2.executeQuery("SELECT * FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPPLIER_CODE='"+rsTmp.getString("SUPP_ID")+"'");
                rsTmp2.first();
                
                String CreditDays="000";
                
                String ptc="00";
                
                if(rsTmp2.getRow()>0) {
                    int crDays=rsTmp2.getInt("PAYMENT_DAYS");
                    CreditDays=EITLERPGLOBAL.Replicate("0",3-Integer.toString(crDays).trim().length())+Integer.toString(crDays).trim();
                    
                    fLine+=CreditDays;
                    
                    rsSupp=data.getResult("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+SuppID+"'");
                    rsSupp.first();
                    
                    if(rsSupp.getRow()>0)
                    {
                      int nSuppID=rsSupp.getInt("SUPP_ID");
                      rsSupp=data.getResult("SELECT * FROM D_COM_SUPP_TERMS WHERE SUPP_ID="+nSuppID+" AND TERM_TYPE='P' AND TERM_CODE<>0");
                      rsSupp.first();
                      
                      if(rsSupp.getRow()>0)
                      {
                        int TermCode=rsSupp.getInt("TERM_CODE");
                        ptc=Integer.toString(TermCode).trim();
                        ptc=EITLERPGLOBAL.Replicate("0", 2-ptc.trim().length())+ptc;
                      }
                    }
                    
                    //int ptCode=rsTmp2.getInt("PTC");
                    //ptc=EITLERPGLOBAL.Replicate("0", 2-Integer.toString(ptCode).trim().length())+Integer.toString(ptCode).trim();
                    
                    fLine+=ptc;
                }
                
                
                
                aFile.write(fLine);
                aFile.newLine();
                rsTmp.next();
            }
            
            aFile.close();
            JOptionPane.showMessageDialog(null,"File has been created");
            
            //tmpConn.close();
            stTmp.close();
            stTmp2.close();
            rsTmp.close();
            rsTmp2.close();
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
    
}