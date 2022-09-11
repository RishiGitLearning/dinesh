/*
 * frmGroup.java
 *
 * Created on July 9, 2007, 12:47 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;


public class frmVariable extends javax.swing.JApplet {
    
    public TVariable objVar=new TVariable();
    public TReport objReport;
    public boolean cancelled=false;
    
    private JDialog aDialog;
    
    private TEITLComboModel cmbFunctionModel=new TEITLComboModel();
    private TEITLComboModel cmbResetOnModel=new TEITLComboModel();
    private TEITLComboModel cmbResetGroupNameModel=new TEITLComboModel();
    private TEITLComboModel cmbEvaluationTimeModel=new TEITLComboModel();
    private TEITLComboModel cmbEvalGroupNameModel=new TEITLComboModel();
    
    /** Initializes the applet frmGroup */
    public frmVariable() {
        setSize(492,349);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        txtVariableName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtExpression = new javax.swing.JTextField();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbResetGroupName = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cmbFunction = new javax.swing.JComboBox();
        cmbResetOn = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbEvaluationTime = new javax.swing.JComboBox();
        cmbEvalGroupName = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setText("Variable Name");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(14, 24, 100, 15);

        getContentPane().add(txtVariableName);
        txtVariableName.setBounds(110, 22, 250, 19);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Variable Expression");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(16, 279, 130, 15);

        getContentPane().add(txtExpression);
        txtExpression.setBounds(16, 299, 350, 19);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(383, 33, 80, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(383, 68, 80, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Function");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel3);
        jLabel3.setBounds(26, 14, 90, 15);

        jPanel1.add(cmbResetGroupName);
        cmbResetGroupName.setBounds(121, 71, 220, 20);

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Reset On");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel6);
        jLabel6.setBounds(26, 44, 90, 15);

        jPanel1.add(cmbFunction);
        cmbFunction.setBounds(119, 10, 220, 20);

        jPanel1.add(cmbResetOn);
        cmbResetOn.setBounds(120, 42, 220, 20);

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Group Name");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel1.add(jLabel7);
        jLabel7.setBounds(26, 73, 90, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(11, 51, 350, 120);

        jPanel2.setLayout(null);

        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setText("Evaluation Time");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(17, 17, 100, 15);

        jPanel2.add(cmbEvaluationTime);
        cmbEvaluationTime.setBounds(117, 14, 220, 20);

        jPanel2.add(cmbEvalGroupName);
        cmbEvalGroupName.setBounds(119, 51, 220, 20);

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText("Group Name");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(35, 54, 80, 15);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(13, 180, 350, 90);

    }//GEN-END:initComponents
    
    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        cancelled=false;
        
        objVar.VariableName=txtVariableName.getText();
        objVar.Expression=txtExpression.getText();
        objVar.Function=cmbFunction.getSelectedItem().toString();
        objVar.EvaluationTime=TGLOBAL.getComboCode(cmbEvaluationTime);
        objVar.EvaluationGroup=cmbEvalGroupName.getSelectedItem().toString();
        objVar.ResetOn=TGLOBAL.getComboCode(cmbResetOn);
        objVar.ResetGroupName=cmbResetGroupName.getSelectedItem().toString();
        objVar.Expression=txtExpression.getText();
        
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbEvalGroupName;
    private javax.swing.JComboBox cmbEvaluationTime;
    private javax.swing.JComboBox cmbFunction;
    private javax.swing.JComboBox cmbResetGroupName;
    private javax.swing.JComboBox cmbResetOn;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtExpression;
    private javax.swing.JTextField txtVariableName;
    // End of variables declaration//GEN-END:variables
    
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null){
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    
    public void ShowDialog() {
        setSize(492,349);
        
        GenerateCombo();
        
        txtVariableName.setText(objVar.VariableName);
        txtExpression.setText(objVar.Expression);
        cmbFunction.setSelectedItem(objVar.Function);
        TGLOBAL.setComboIndex(cmbEvaluationTime,objVar.EvaluationTime);
        cmbEvalGroupName.setSelectedItem(objVar.EvaluationGroup);
        TGLOBAL.setComboIndex(cmbResetOn,objVar.ResetOn);
        cmbResetGroupName.setSelectedItem(objVar.ResetGroupName);
        txtExpression.setText(objVar.Expression);
        
        
        
        Frame f=findParentFrame(this);
        
        aDialog=new JDialog(f,"Report Variable",true);
        
        aDialog.getContentPane().add("Center",this);
        Dimension appletSize = this.getSize();
        aDialog.setSize(appletSize);
        aDialog.setResizable(false);
        
        //Place it to center of the screen
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        aDialog.setLocation((int)(screenSize.getWidth()-appletSize.getWidth())/2,(int)(screenSize.getHeight()-appletSize.getHeight())/2);
        
        aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        aDialog.show();
    }
    
    
    private void GenerateCombo() {
        
        try {
            
            //Function Combo Box
            cmbFunctionModel=new TEITLComboModel();
            cmbFunction.removeAllItems();
            cmbFunction.setModel(cmbFunctionModel);

            TComboData aData=new TComboData();
            aData.Text="None";
            aData.strCode="None";
            
            cmbFunctionModel.addElement(aData);
            
            
            aData=new TComboData();
            aData.Text="Sum";
            aData.strCode="Sum";
            
            cmbFunctionModel.addElement(aData);
            
            
            //Reset On Combo Box
            
            cmbResetOnModel=new TEITLComboModel();
            cmbResetOn.removeAllItems();
            cmbResetOn.setModel(cmbResetOnModel);
            
            
            aData=new TComboData();
            aData.Code=0;
            aData.Text="Now";
            cmbResetOnModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=1;
            aData.Text="Report";
            cmbResetOnModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=2;
            aData.Text="Group";
            cmbResetOnModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=3;
            aData.Text="Page";
            cmbResetOnModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=4;
            aData.Text="None";
            cmbResetOnModel.addElement(aData);
            
            
            //Reset Group Combo Box
            cmbResetGroupNameModel=new TEITLComboModel();
            cmbResetGroupName.removeAllItems();
            cmbResetGroupName.setModel(cmbResetGroupNameModel);
            
            aData=new TComboData();
            aData.strCode="";
            aData.Text=" ";
            cmbResetGroupNameModel.addElement(aData);

            
            for(int i=1;i<=objReport.colBands.size();i++) {
                TBand objBand=(TBand)objReport.colBands.get(Integer.toString(i));
                
                if(objBand.BandType==TBand.GroupHeader) {
                    aData=new TComboData();
                    aData.strCode=objBand.BandName;
                    aData.Text=objBand.BandName;
                    cmbResetGroupNameModel.addElement(aData);
                }
            }
            
            
            //Evaluation Time Combo Box
            cmbEvaluationTimeModel=new TEITLComboModel();
            cmbEvaluationTime.removeAllItems();
            cmbEvaluationTime.setModel(cmbEvaluationTimeModel);
            
            aData=new TComboData();
            aData.Code=0;
            aData.Text="Now";
            cmbEvaluationTimeModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=1;
            aData.Text="Report";
            cmbEvaluationTimeModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=2;
            aData.Text="Group";
            cmbEvaluationTimeModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=3;
            aData.Text="Page";
            cmbEvaluationTimeModel.addElement(aData);
            
            aData=new TComboData();
            aData.Code=4;
            aData.Text="None";
            cmbEvaluationTimeModel.addElement(aData);
            
            
            //Evaluation Group Name Combo Box
            cmbEvalGroupNameModel=new TEITLComboModel();
            cmbEvalGroupName.removeAllItems();
            cmbEvalGroupName.setModel(cmbEvalGroupNameModel);
            
            aData=new TComboData();
            aData.strCode="";
            aData.Text=" ";
            cmbEvalGroupNameModel.addElement(aData);
            
            for(int i=1;i<=objReport.colBands.size();i++) {
                TBand objBand=(TBand)objReport.colBands.get(Integer.toString(i));
                
                if(objBand.BandType==TBand.GroupHeader) {
                    aData=new TComboData();
                    aData.strCode=objBand.BandName;
                    aData.Text=objBand.BandName;
                    cmbEvalGroupNameModel.addElement(aData);
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    
}
