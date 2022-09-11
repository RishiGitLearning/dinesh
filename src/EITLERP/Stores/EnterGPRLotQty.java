/*
 * EnterLotQty.java
 *
 * Created on May 12, 2004, 4:54 PM
 */

package EITLERP.Stores;
  
/** 
 * 
 * @author  nrpithva
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;


public class EnterGPRLotQty extends javax.swing.JApplet {

    private EITLTableModel DataModel;

    public boolean Cancelled=true;
    public HashMap colLot=new HashMap();
    private JDialog aDialog;
    
    public String GPRNo="";
    public int GPRSrNo=0;
        
    public EnterGPRLotQty()
    {
        System.gc();
        initComponents();
    }
    
    /** Initializes the applet EnterLotQty */
    public void init() {
        initComponents();
        GenerateGrid();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Enter Lot no. and Qty");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 212, 15);

        jPanel4.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jPanel4);
        jPanel4.setBounds(6, 30, 662, 6);

        Table.setModel(new javax.swing.table.DefaultTableModel(
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
        Table.setNextFocusableComponent(cmdOK);
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 46, 378, 226);

        cmdOK.setText("OK");
        cmdOK.setNextFocusableComponent(cmdCancel);
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(418, 50, 80, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setNextFocusableComponent(cmdOK);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(418, 82, 79, 25);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setNextFocusableComponent(Table);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        getContentPane().add(cmdAdd);
        cmdAdd.setBounds(12, 278, 80, 25);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setNextFocusableComponent(cmdAdd);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        getContentPane().add(cmdRemove);
        cmdRemove.setBounds(94, 278, 90, 25);

        jLabel2.setText("Press Insert - to add new row, Delete - to delete row");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(14, 320, 302, 16);

    }//GEN-END:initComponents

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // TODO add your handling code here:
            if(evt.getKeyCode()==155)//Insert Key Pressed
            {
                Object[] rowData=new Object[3];
                DataModel.addRow(rowData);
                UpdateSrNo();
                Table.changeSelection(Table.getRowCount()-1, 1, false,false);
                Table.requestFocus();
            }
            
            if(evt.getKeyCode()==127) //Delete key pressed
            {
                if(Table.getRowCount()>0) {
                    DataModel.removeRow(Table.getSelectedRow());
                    UpdateSrNo();
                }
            }
    }//GEN-LAST:event_TableKeyPressed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        if(Validate())
        {
            SetLot();
            Cancelled=false;
            aDialog.dispose();
        }
    }//GEN-LAST:event_cmdOKActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        DataModel.removeRow(Table.getSelectedRow());
        UpdateSrNo();
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        Object[] rowData=new Object[3];
        DataModel.addRow(rowData);
        UpdateSrNo();
        Table.changeSelection(Table.getRowCount()-1, 1, false,false);
        Table.requestFocus();
    }//GEN-LAST:event_cmdAddActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

private void FormatGrid()    
{
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        DataModel.addColumn("Sr.");
        DataModel.addColumn("Lot No.");
        DataModel.addColumn("Qty");
        
        DataModel.SetNumeric(2,true);
        DataModel.SetReadOnly(0);
        
}

private boolean Validate()
{
   int ValidEntryCount=0;
   return true;
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

private void GenerateGrid()    
{
   //Generates Grid from the  
    FormatGrid();
    
   for(int i=1;i<=colLot.size();i++) 
   {
     clsGPRLot ObjLot=(clsGPRLot)colLot.get(Integer.toString(i));
     Object[] rowData=new Object[3];
     
     rowData[0]=Integer.toString((int)ObjLot.getAttribute("SR_NO").getVal());
     rowData[1]=(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj();
     rowData[2]=Double.toString(ObjLot.getAttribute("LOT_QTY").getVal());
    
     DataModel.addRow(rowData);
   }
    UpdateSrNo();
}

private void SetLot()
{
   colLot.clear(); 
   for(int i=0;i<Table.getRowCount();i++) 
   {
     clsGPRLot ObjLot=new clsGPRLot();
     
     String LotNo=(String)Table.getValueAt(i, 1);
     double LotQty=Double.parseDouble((String)Table.getValueAt(i, 2));
     
     ObjLot.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
     ObjLot.setAttribute("GATEPASS_REQ_NO",GPRNo);
     ObjLot.setAttribute("GATEPASS_SR_NO",GPRSrNo);
     ObjLot.setAttribute("SR_NO",i);
     ObjLot.setAttribute("ITEM_LOT_NO",LotNo);
     ObjLot.setAttribute("LOT_QTY",EITLERPGLOBAL.round(LotQty,3));
     
     colLot.put(Integer.toString(colLot.size()+1),ObjLot);
   }
}

    
public boolean ShowList() {
       try {
           GenerateGrid();
            
            setSize(530 ,370);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Enter Lot Nos.",true);
            
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
        }
        catch(Exception e) {
        }
        return !Cancelled;
    }    


private void UpdateSrNo()
{
    for(int i=0;i<Table.getRowCount();i++)
    {
      Table.setValueAt(Integer.toString(i+1),i,0);
    }
}

}
