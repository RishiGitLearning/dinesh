/*
 * EnterLot_NRGP.java
 *
 * Created on May 29, 2004, 9:51 AM
 */
/*<APPLET CODE=SelectRJN.Class HEIGHT=310 WIDTH=520></APPLET>*/

package EITLERP.Stores;
 
/**  
 * 
 * @author  nhpatel
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Utils.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;

public class EnterLot_NRGP extends javax.swing.JApplet {
    
    private EITLTableModel DataModel;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    
    public boolean Cancelled=true;
    public HashMap colLot=new HashMap();
    private JDialog aDialog;
    
    public int ModuleID=0;
    private String SelNo="";
    
    public String SQL;
    public static String Docno = "";
    public static int DocSrno = 0;
    public static String Type = "";
    public int ReturnCol;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    String ReturnVal="";
    
    public EnterLot_NRGP()
    {
        System.gc();
        initComponents();
        FormatGrid();
        GenerateGrid();
    }
    
    /** Initializes the applet EnterLot_NRGP */
    public void init() {
        setSize(520,310);
        initComponents();
        //GenerateGrid();
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
        cmdSelectAl = new javax.swing.JButton();
        cmdRead = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Select Lot no. and Qty");
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
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 46, 378, 226);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(418, 50, 80, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(418, 82, 79, 25);

        cmdSelectAl.setText("Select All");
        cmdSelectAl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectAlActionPerformed(evt);
            }
        });

        getContentPane().add(cmdSelectAl);
        cmdSelectAl.setBounds(10, 278, 104, 25);

        cmdRead.setText("Read Weighing F5");
        cmdRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReadActionPerformed(evt);
            }
        });

        getContentPane().add(cmdRead);
        cmdRead.setBounds(118, 278, 152, 25);

    }//GEN-END:initComponents

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==116)
        {
        try
        {
           if(Table.getRowCount()>0) 
           {
               Table.setValueAt(SimpleRead.getWeight("COM1"), Table.getSelectedRow(), 3 );
           }
            
        }
        catch(Exception e)
        {
            
        }
            
            
        }
    }//GEN-LAST:event_TableKeyPressed

    private void cmdReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReadActionPerformed
        // TODO add your handling code here:
        try
        {
           if(Table.getRowCount()>0) 
           {
               Table.setValueAt(SimpleRead.getWeight("COM1"), Table.getSelectedRow(), 3);
           }
            
        }
        catch(Exception e)
        {
            
        }
        
    }//GEN-LAST:event_cmdReadActionPerformed

    private void cmdSelectAlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectAlActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<Table.getRowCount();i++)
        {
           DataModel.setValueAt(Boolean.valueOf(true), i, 0); 
        }
    }//GEN-LAST:event_cmdSelectAlActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        if(Table.getRowCount()<=0) {
            Cancelled=true;
        }
        else {
            SetList();
            Cancelled=false;
        }
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JButton cmdRead;
    private javax.swing.JButton cmdSelectAl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

private void FormatGrid()    
{
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(int i=1;i<=5;i++)
        {
            DataModel.SetReadOnly(i);
        }
        
        //Add Columns to it
        DataModel.addColumn(""); //0 Selection
        DataModel.addColumn("Sr.No.");//1
        DataModel.addColumn("Lot No.");//3
        DataModel.addColumn("Lot Qty");//4
                
        Rend.setCustomComponent(0,"CheckBox");
        Table.getColumnModel().getColumn(0).setCellRenderer(Rend);
        Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
}

private boolean Validate()
{
   int ValidEntryCount=0;
   
   for(int i=0;i<Table.getRowCount();i++)
   {

      if((Table.getValueAt(i,0)!=null)&&(Table.getValueAt(i,1)!=null))
      {
          double LotQty=Double.parseDouble((String)Table.getValueAt(i,2));          
          if(LotQty>0)
          {
            ValidEntryCount++;
          }
      }
   }
    
   if(ValidEntryCount<=0)
   {
      JOptionPane.showMessageDialog(null,"Please enter lot no. and qty");
      return false;
   }
   
   return true;
}


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
      HashMap List=new HashMap();
      if (Type == "RJN")
      {
       colLot.clear();   
       List = clsRJN.getRJNLotList(EITLERPGLOBAL.gCompanyID, Docno,DocSrno,false);
       for(int i=1;i<=List.size();i++)
       {
           clsRJNLot ObjItem=(clsRJNLot) List.get(Integer.toString(i));
           Object[] rowData=new Object[4];

               rowData[0]=Boolean.valueOf(true); //By default not selected
               rowData[1]=Integer.toString((int)ObjItem.getAttribute("SR_NO").getVal());
               rowData[2]=(String)ObjItem.getAttribute("ITEM_LOT_NO").getObj();
               rowData[3]=Double.toString(ObjItem.getAttribute("LOT_QTY").getVal());
               DataModel.addRow(rowData);
       }
      } 
      //    ============== Rejection portion completed
      //  ================ Gatepass is there thrn
      if (Type == "GPR")
      {
       colLot.clear();   
       List = clsGPR.getGPRLotList(EITLERPGLOBAL.gCompanyID, Docno,DocSrno,false);
       for(int i=1;i<=List.size();i++)
       {
           clsGPRLot ObjItem=(clsGPRLot) List.get(Integer.toString(i));
           Object[] rowData=new Object[4];

               rowData[0]=Boolean.valueOf(true); //By default not selected
               rowData[1]=Integer.toString((int)ObjItem.getAttribute("SR_NO").getVal());
               rowData[2]=(String)ObjItem.getAttribute("ITEM_LOT_NO").getObj();
               rowData[3]=Double.toString(ObjItem.getAttribute("LOT_QTY").getVal());
               DataModel.addRow(rowData);
       }
      } 
}
    
private void SetList()
{
    int SrNo=0;
    HashMap List=new HashMap();
    colLot.clear();
      //Search in the table for SrNo.
       for(int j=0;j<Table.getRowCount();j++)
       {
             if(Table.getValueAt(j,0).toString().equals("true"))  
             {
               //Selected Item  
                 clsNRGPItemDetail ObjNewLot=new clsNRGPItemDetail();
                 ObjNewLot.setAttribute("SR_NO",(String) Table.getValueAt(j,1));
                 ObjNewLot.setAttribute("LOT_NO",(String) Table.getValueAt(j,2));
                 ObjNewLot.setAttribute("LOT_QTY",EITLERPGLOBAL.round(Double.parseDouble((String) Table.getValueAt(j,3)),3));
                 colLot.put(Integer.toString(colLot.size()+1),ObjNewLot);
             }
       }
}

public boolean ShowList() {
        try {
            FormatGrid();
            GenerateGrid();
            
            setSize(520,310);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Select RJN Lot Items",true);
            
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
            e.printStackTrace();
        }
        return !Cancelled;
    }    


}