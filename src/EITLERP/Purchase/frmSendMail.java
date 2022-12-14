/*
 * frmClient.java
 *
 * Created on February 8, 2005, 2:30 PM
 */

package EITLERP.Purchase;

/**
 *
 * @author  root
 */

import javax.swing.*;
import java.awt.*;
import EITLERP.*;
import java.util.*;
import java.net.*;
import net.sf.jasperreports.engine.*;


public class frmSendMail extends javax.swing.JApplet {
    
    private EITLComboModel cmbModuleModel;
    private EITLListModel cmbListModel;
    private JDialog aDialog;
    public int ModuleID=0;
    public int SentBy=0;
    public String MailDocNo="";
    public HashMap colRecList=new HashMap();
    public String theURL="";
    public String theFile="";
    
    private EITLTableModel DataModel;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    private HashMap emaillist=new HashMap();
    
    private JTextField currentSelection=null;
    
    
    public frmSendMail() {
        setSize(410,300);
        initComponents();
        GenerateCombo();
        
        //get the list of address book
        emaillist=clsAddressBook.getEMailList();
        
        refreshList("");
        theList.setVisible(false);
        txtFrom.requestFocus();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        cmdSend = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmdExit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbModule = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSubject = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtFrom = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        txtBCC = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCC = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cmdAddressBook = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtTO = new javax.swing.JTextField();
        theList = new javax.swing.JScrollPane();
        addlist = new javax.swing.JList();

        getContentPane().setLayout(null);

        cmdSend.setText("Send");
        cmdSend.setNextFocusableComponent(cmdAddressBook);
        cmdSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSendActionPerformed(evt);
            }
        });

        getContentPane().add(cmdSend);
        cmdSend.setBounds(455, 38, 129, 25);

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setText("SEND DOCUMENT BY EMAIL");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(7, 6, 209, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 3, 644, 26);

        jLabel2.setText("EMail Message");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 355, 103, 15);

        cmdExit.setText("Exit");
        cmdExit.setNextFocusableComponent(cmbModule);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(458, 121, 125, 25);

        jLabel3.setText("Document");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(3, 49, 71, 15);

        cmbModule.setNextFocusableComponent(txtDocNo);
        getContentPane().add(cmbModule);
        cmbModule.setBounds(83, 43, 310, 24);

        jLabel4.setText("Doc. No.");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(19, 86, 61, 15);

        txtDocNo.setNextFocusableComponent(txtFrom);
        getContentPane().add(txtDocNo);
        txtDocNo.setBounds(83, 82, 121, 22);

        jLabel5.setText("Subject");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(27, 292, 54, 15);

        txtSubject.setNextFocusableComponent(txtMessage);
        getContentPane().add(txtSubject);
        txtSubject.setBounds(83, 289, 356, 22);

        jLabel6.setText("From");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(24, 121, 54, 15);

        txtFrom.setNextFocusableComponent(txtTO);
        txtFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFromFocusGained(evt);
            }
        });

        getContentPane().add(txtFrom);
        txtFrom.setBounds(83, 118, 356, 22);

        txtMessage.setNextFocusableComponent(cmdSend);
        jScrollPane1.setViewportView(txtMessage);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(19, 379, 425, 131);

        txtBCC.setEditable(false);
        txtBCC.setNextFocusableComponent(txtSubject);
        txtBCC.setEnabled(false);
        txtBCC.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBCCFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBCCFocusLost(evt);
            }
        });
        txtBCC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBCCKeyReleased(evt);
            }
        });

        getContentPane().add(txtBCC);
        txtBCC.setBounds(83, 242, 356, 22);

        jLabel8.setText("BCC");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(27, 245, 54, 15);

        txtCC.setEditable(false);
        txtCC.setNextFocusableComponent(txtBCC);
        txtCC.setEnabled(false);
        txtCC.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCCFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCCFocusLost(evt);
            }
        });
        txtCC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCCKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCCKeyReleased(evt);
            }
        });

        getContentPane().add(txtCC);
        txtCC.setBounds(83, 196, 356, 22);

        jLabel9.setText("CC");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(25, 199, 54, 15);

        cmdAddressBook.setText("Address Book");
        cmdAddressBook.setNextFocusableComponent(cmdExit);
        cmdAddressBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddressBookActionPerformed(evt);
            }
        });

        getContentPane().add(cmdAddressBook);
        cmdAddressBook.setBounds(457, 72, 127, 25);

        jLabel10.setText("TO");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(24, 153, 54, 15);

        txtTO.setNextFocusableComponent(txtCC);
        txtTO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTOFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTOFocusLost(evt);
            }
        });
        txtTO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTOKeyReleased(evt);
            }
        });

        getContentPane().add(txtTO);
        txtTO.setBounds(83, 150, 356, 22);

        addlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addlistMouseClicked(evt);
            }
        });

        theList.setViewportView(addlist);

        getContentPane().add(theList);
        theList.setBounds(329, 244, 356, 124);

    }//GEN-END:initComponents

    private void txtBCCFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBCCFocusLost
        // TODO add your handling code here:
        theList.setVisible(false);
    }//GEN-LAST:event_txtBCCFocusLost

    private void txtTOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTOFocusLost
        // TODO add your handling code here:
        theList.setVisible(false);        
    }//GEN-LAST:event_txtTOFocusLost

    private void txtBCCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBCCKeyReleased
        // TODO add your handling code here:
        refreshList(fetchInitString(currentSelection));
        
        try {
            if(evt.getKeyCode()==27) //Down Arrow Key pressed
            {
                theList.setVisible(false);
            }
            
            
            if(evt.getKeyCode()==40) //Down Arrow Key pressed
            {
                addlist.setSelectedIndex(addlist.getSelectedIndex()+1);
            }
            
            if(evt.getKeyCode()==38) //Up Arrow Key pressed
            {
                addlist.setSelectedIndex(addlist.getSelectedIndex()-1);
            }
            
            if(evt.getKeyCode()==10) {
                currentSelection.setText(fetchReplaceString(currentSelection)+(String)cmbListModel.getElementAt(addlist.getSelectedIndex())+",");
                refreshList(fetchInitString(currentSelection));
                theList.setVisible(false);
            }
            
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_txtBCCKeyReleased

    private void txtTOKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTOKeyReleased
        // TODO add your handling code here:
        refreshList(fetchInitString(currentSelection));

                
        
        try {
            if(evt.getKeyCode()==27) //Down Arrow Key pressed
            {
                theList.setVisible(false);
            }
            
            if(evt.getKeyCode()==40) //Down Arrow Key pressed
            {
                addlist.setSelectedIndex(addlist.getSelectedIndex()+1);
            }
            
            if(evt.getKeyCode()==38) //Up Arrow Key pressed
            {
                addlist.setSelectedIndex(addlist.getSelectedIndex()-1);
            }
            
            if(evt.getKeyCode()==10) {
                currentSelection.setText(fetchReplaceString(currentSelection)+(String)cmbListModel.getElementAt(addlist.getSelectedIndex())+",");
                refreshList(fetchInitString(currentSelection));
                theList.setVisible(false);
            }
            
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_txtTOKeyReleased

    private void txtCCFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCCFocusLost
        // TODO add your handling code here:
        theList.setVisible(false);
    }//GEN-LAST:event_txtCCFocusLost
    
    private void addlistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addlistMouseClicked
        // TODO add your handling code here:
        try
        {
        if(evt.getClickCount()==2) {
            currentSelection.setText(fetchReplaceString(currentSelection)+(String)cmbListModel.getElementAt(addlist.getSelectedIndex())+",");
            refreshList(fetchInitString(currentSelection));
            theList.setVisible(false);
        }
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_addlistMouseClicked
    
    private void txtBCCFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBCCFocusGained
        // TODO add your handling code here:
        currentSelection=txtBCC;
        theList.setLocation(txtBCC.getLocation().x, txtBCC.getLocation().y+txtBCC.getHeight());

    }//GEN-LAST:event_txtBCCFocusGained
    
    private void txtCCFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCCFocusGained
        // TODO add your handling code here:
        currentSelection=txtCC;
        theList.setLocation(txtCC.getLocation().x, txtCC.getLocation().y+txtCC.getHeight());
        
    }//GEN-LAST:event_txtCCFocusGained
    
    private void txtTOFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTOFocusGained
        // TODO add your handling code here:
        currentSelection=txtTO;
        theList.setLocation(txtTO.getLocation().x, txtTO.getLocation().y+txtTO.getHeight());
        
    }//GEN-LAST:event_txtTOFocusGained
    
    private void txtFromFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromFocusGained
        // TODO add your handling code here:
        currentSelection=txtFrom;
    }//GEN-LAST:event_txtFromFocusGained
    
    private void txtCCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCCKeyReleased
        // TODO add your handling code here:
        refreshList(fetchInitString(currentSelection));
        
        try {
            
            if(evt.getKeyCode()==27) //Down Arrow Key pressed
            {
                theList.setVisible(false);
            }
            
            
            if(evt.getKeyCode()==27) //Down Arrow Key pressed
            {
                theList.setVisible(false);
            }
            
            if(evt.getKeyCode()==40) //Down Arrow Key pressed
            {
                addlist.setSelectedIndex(addlist.getSelectedIndex()+1);
            }
            
            if(evt.getKeyCode()==38) //Up Arrow Key pressed
            {
                addlist.setSelectedIndex(addlist.getSelectedIndex()-1);
            }
            
            if(evt.getKeyCode()==10) {
                currentSelection.setText(fetchReplaceString(currentSelection)+(String)cmbListModel.getElementAt(addlist.getSelectedIndex())+",");
                refreshList(fetchInitString(currentSelection));
                theList.setVisible(false);
            }
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtCCKeyReleased
    
    private void txtCCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCCKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtCCKeyPressed
    
    private void cmdAddressBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddressBookActionPerformed
        // TODO add your handling code here:
        try {
            AppletFrame.startApplet("EITLERP.frmAddressBook","EITL ERP");
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdAddressBookActionPerformed
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        try {
            aDialog.dispose();
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSendActionPerformed
        // TODO add your handling code here:
        HashMap sendList=new HashMap();
        String strEMail="";
         
        if(txtTO.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"No email messages specified. Please specify atleast one email address");
            return;
        }
        
        if(txtSubject.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"No email subject specified. Please specify it.");
            return;
        }
        if(txtMessage.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"No email message specified. Please specify it.");
            return;
        }
        
        for(int i=0;i<=txtTO.getText().length()-1;i++) {
            if(txtTO.getText().substring(i,i+1).equals(",")) {
                if(!strEMail.trim().equals("")) {
                    sendList.put(Integer.toString(sendList.size()+1),strEMail);
                }
                strEMail="";
            }
            else {
                strEMail=strEMail+txtTO.getText().substring(i,i+1);
            }
        }
        
        //Last Element must be included
        if(!strEMail.trim().equals(""))
        {
          sendList.put(Integer.toString(sendList.size()+1),strEMail);  
        }
        
        
        try {
            //Now insert the record into doc mailer database
            clsDocMailer ObjMailer=new clsDocMailer();
            ObjMailer.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            ObjMailer.setAttribute("DOC_NO",0);
            ObjMailer.setAttribute("DOC_DATE",EITLERPGLOBAL.getCurrentDateDB());
            ObjMailer.setAttribute("MODULE_ID",ModuleID);
            ObjMailer.setAttribute("SENT_BY",SentBy);
            ObjMailer.setAttribute("DESCRIPTION",txtMessage.getText());
            ObjMailer.setAttribute("FROM",txtFrom.getText());
            ObjMailer.setAttribute("SUBJECT",txtSubject.getText());
            ObjMailer.setAttribute("MAIL_DOC_NO",MailDocNo);
            ObjMailer.setAttribute("CC",txtCC.getText());
            ObjMailer.setAttribute("BCC",txtBCC.getText());
            
            
            for(int i=1;i<=sendList.size();i++) {
                String email=(String)sendList.get(Integer.toString(i));
                ObjMailer.colEmail.put(Integer.toString(ObjMailer.colEmail.size()+1),email);
            }
            
            long MailNo=ObjMailer.Insert();
            
            if(MailNo!=0) {
                
                //Now depending upon the module id divert to different jsp pages
                URL MailDocument=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptDocMailer.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+MailNo+"&File="+this.theFile); //+".pdf"
                EITLERPGLOBAL.loginContext.showDocument(MailDocument,"_blank");
                
            }
            
        }
        catch(Exception e) {
            
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdSendActionPerformed
    
    public boolean ShowWindow() {
        try {
            GenerateCombo();
            setSize(632,570);
            
            DisplayData();
            
            Frame f=findParentFrame(this);
            aDialog=new JDialog(f,"Send EMail",false);
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(true);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
            
        }
        catch(Exception e) {
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList addlist;
    private javax.swing.JComboBox cmbModule;
    private javax.swing.JButton cmdAddressBook;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane theList;
    private javax.swing.JTextField txtBCC;
    private javax.swing.JTextField txtCC;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextArea txtMessage;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtTO;
    // End of variables declaration//GEN-END:variables
    
    
    private void GenerateCombo() {
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbType ------- //
        cmbModuleModel=new EITLComboModel();
        cmbModule.removeAllItems();
        cmbModule.setModel(cmbModuleModel);
        
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ORDER BY MODULE_ID";
        
        List=clsModules.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
            //Check that Module Access Rights are given
            int ModuleID=(int)ObjModules.getAttribute("MODULE_ID").getVal();
            int MenuID=clsMenu.getMenuIDFromModule(EITLERPGLOBAL.gCompanyID, ModuleID);
            
            ComboData aData=new ComboData();
            aData.Text=((String) ObjModules.getAttribute("MODULE_DESC").getObj());
            aData.Code=(int) ObjModules.getAttribute("MODULE_ID").getVal();
            cmbModuleModel.addElement(aData);
            
        }
        
        
        
    }
    
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
    
    
    
    private void DisplayData() {
        
        EITLERPGLOBAL.setComboIndex(cmbModule,ModuleID);
        txtFrom.setText(clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, SentBy));
        txtDocNo.setText(MailDocNo);
        
        
        txtTO.setText("");
        
        for(int i=1;i<=colRecList.size();i++) {
            String email=(String)colRecList.get(Integer.toString(i));

             if(txtTO.getText().trim().equals(""))
             {
             txtTO.setText(email);      
             }
             else
             {
             txtTO.setText(txtTO.getText()+","+email);  
             }
        }
        txtMessage.setText("\n\nSDML");
        txtSubject.setText("PO :"+MailDocNo);   
    }
    
    
    private void refreshList(String initString) {

        int ElementCount=0;
        
        theList.setVisible(true);
        theList.repaint();
                
        int oldSelection=0;
        oldSelection=addlist.getSelectedIndex();
        
        addlist.removeAll();
        cmbListModel = new EITLListModel();
        addlist.setModel(cmbListModel);
        cmbListModel.removeAllElements();
        
        
        for(int i=1;i<=emaillist.size();i++) {
            String email=(String)emaillist.get(Integer.toString(i));
            
            if(initString.trim().equals("")) {
                cmbListModel.addElement(email);
                ElementCount++;
            }
            else {
                if(email.startsWith(initString)) {
                    cmbListModel.addElement(email);
                    ElementCount++;
                }
            }
        }
        
        
        try {
            
            if(oldSelection<0)
            {
             oldSelection=0;   
            }
            
            if(oldSelection>ElementCount)
            {
              oldSelection=0;  
            }
            
            addlist.setSelectedIndex(oldSelection);
        }
        catch(Exception e) {
            addlist.setSelectedIndex(0);
            
        }
        
    }
    
    
    private String fetchInitString(JTextField aText) {
        
        String initString="";
        
        for(int i=aText.getText().length()-1;i>=0;i--) {
            if(aText.getText().substring(i,i+1).equals(",")) {
                return aText.getText().substring(i+1);
            }
            else {
                initString=initString+aText.getText().substring(i,i+1);
            }
        }
        
        return aText.getText();
    }
    
    
    private String fetchReplaceString(JTextField aText) {
        
        String replaceString="";
        
        for(int i=aText.getText().length()-1;i>=0;i--) {
            if(aText.getText().substring(i,i+1).equals(",")) {
                replaceString=aText.getText().substring(0,i+1);
                return replaceString;
            }
            else {
            }
        }
        
        return replaceString;
    }
    
    
}
