/*
 * frmQuotation.java
 *
 * Created on June 02, 2004, 1:30 PM
 */
package EITLERP.Purchase;

/**
 *
 * @author jadave
 */
/*<APPLET CODE=frmQuotation.class HEIGHT=474 WIDTH=758></APPLET>*/
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import java.lang.*;
import javax.swing.text.*;
import java.net.*;
import EITLERP.Utils.*;
import java.sql.ResultSet;

public class frmQuotation extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLTableModel DataModelH;
    private EITLTableModel DataModelL;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();

    private EITLTableModel DataModelD;
    private EITLTableModel DataModelP;
    private EITLTableModel DataModelO;

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    clsTaxColumn ObjTax = new clsTaxColumn();
    clsColumn ObjColumn = new clsColumn();

    private JEP myParser = new JEP();
    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private clsQuotation ObjQuotation;

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbTransportModel;
    private EITLComboModel cmbCurrencyModel;
    private EITLComboModel cmbStatusModel;
    private EITLComboModel cmbReasonModel;
    private EITLTableModel DataModelA;
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private boolean HistoryView = false;
    private String theDocNo = "";
    private EITLTableModel DataModelHS;

    String cellLastValueL = "";
    String cellLastValueH = "";

    /**
     * Creates new form frmQuotation
     */
    public void init() {
        System.gc();
        setSize(770, 530);
        initComponents();
        lblTitle.setForeground(Color.WHITE);

        //Now show the Images
        cmdTop.setIcon(EITLERPGLOBAL.getImage("TOP"));
        cmdBack.setIcon(EITLERPGLOBAL.getImage("BACK"));
        cmdNext.setIcon(EITLERPGLOBAL.getImage("NEXT"));
        cmdLast.setIcon(EITLERPGLOBAL.getImage("LAST"));
        cmdNew.setIcon(EITLERPGLOBAL.getImage("NEW"));
        cmdEdit.setIcon(EITLERPGLOBAL.getImage("EDIT"));
        cmdDelete.setIcon(EITLERPGLOBAL.getImage("DELETE"));
        cmdSave.setIcon(EITLERPGLOBAL.getImage("SAVE"));
        cmdCancel.setIcon(EITLERPGLOBAL.getImage("UNDO"));
        cmdFilter.setIcon(EITLERPGLOBAL.getImage("FIND"));
        cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));

        ObjTax.LoadData((int) EITLERPGLOBAL.gCompanyID);
        ObjColumn.LoadData((int) EITLERPGLOBAL.gCompanyID);

        FormatGrid();
        FormatGrid_H();
        SetNumberFormats();

        GenerateCombos();
        ObjQuotation = new clsQuotation();
        SetMenuForRights();

        if (getName().equals("Link")) {

        } else {
            if (ObjQuotation.LoadData(EITLERPGLOBAL.gCompanyID)) {
                ObjQuotation.MoveLast();
                DisplayData();

                ShowMessage("Ready ............");
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while loading data. Error is " + ObjQuotation.LastError);
            }
        }

        txtAuditRemarks.setVisible(false);
    }

    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel = new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);

        List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=19");

        if (EditMode == EITLERPGLOBAL.EDIT) {
            List = clsHierarchy.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=19");
        }

        for (int i = 1; i <= List.size(); i++) {
            clsHierarchy ObjHierarchy = (clsHierarchy) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text = (String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //

    }

    private void FormatGrid_H() {
        DataModelH = new EITLTableModel();

        EITLTableCellRenderer Renderer = new EITLTableCellRenderer();

        TableH.removeAll();
        TableH.setModel(DataModelH);

        Renderer.setColor(0, 0, Color.LIGHT_GRAY);

        //Set the table Readonly
        DataModelH.TableReadOnly(false);
        DataModelH.SetReadOnly(0);
        DataModelH.SetNumeric(1, true);

        //Add Default Columns
        DataModelH.addColumn("Column");
        DataModelH.addColumn("Value");

        TableH.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        SetupColumns_H();

        TableColumnModel ColModel = TableH.getColumnModel();
        TableH.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //------- Install Table List Selection Listener ------//
        TableH.getColumnModel().getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        int last = TableH.getSelectedColumn();
                        String strVar = DataModelH.getVariable(last);

                        //=============== Cell Editing Routine =======================//
                        try {
                            cellLastValueH = (String) TableH.getValueAt(TableH.getSelectedRow(), TableL.getSelectedColumn());

                            TableH.editCellAt(TableH.getSelectedRow(), TableH.getSelectedColumn());
                            if (TableH.getEditorComponent() instanceof JTextComponent) {
                                ((JTextComponent) TableH.getEditorComponent()).selectAll();
                            }
                        } catch (Exception cell) {
                        }
                //============= Cell Editing Routine Ended =================//

                    }
                }
        );
        //===================================================//

        //----- Install Table Model Event Listener -------//
        TableH.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {

                    //=========== Cell Update Prevention Check ===========//
                    String curValue = (String) TableH.getValueAt(TableH.getSelectedRow(), e.getColumn());
                    if (curValue.equals(cellLastValueH)) {
                        return;
                    }
                    //====================================================//

                    int col = e.getColumn();
                    int row = e.getLastRow();
                    if (!Updating_H) {
                        UpdateResults_H(row);
                    }
                }
            }
        });
    }

    private void SetupColumns_H() {
        HashMap List = new HashMap();
        Object[] rowData;

        List = clsColumn.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND MODULE_ID=19 AND HEADER_LINE='H' ORDER BY COL_ORDER");
        TableColumnModel ColModel = TableH.getColumnModel();

        TableH.removeAll();

        if (List.size() <= 0) {
            HeaderPane.setVisible(false);
        }

        for (int i = 1; i <= List.size(); i++) {
            clsColumn ObjColumn = (clsColumn) List.get(Integer.toString(i));
            int lTaxID = (int) ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID = (int) ObjColumn.getAttribute("SR_NO").getVal();

            clsTaxColumn ObjTax = (clsTaxColumn) clsTaxColumn.getObject((int) EITLERPGLOBAL.gCompanyID, lTaxID);
            if ((boolean) ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                //Add Percentage Column
                    //DataModelL.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj()+"%");

                    rowData = new Object[2];
                    rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj() + "%";
                    rowData[1] = "0.00";
                    DataModelH.addRow(rowData);

                    //Set Column ID
                    DataModelH.SetColID(TableH.getRowCount() - 1, lColID);

                    //Set Variable for % Column. It will be P_ID
                    DataModelH.SetVariable(TableH.getRowCount() - 1, "P_" + Integer.toString(lColID));

                    //Set the Operationg Add/Substract
                    DataModelH.SetOperation(TableH.getRowCount() - 1, "-");

                    //Set stat - Include it in calculation or not
                    DataModelH.SetInclude(TableH.getRowCount() - 1, true);

                    //Set Formula
                    DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                    rowData = new Object[2];
                    rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj();
                    rowData[1] = "0.00";
                    DataModelH.addRow(rowData);

                    //Set Column ID
                    DataModelH.SetColID(TableH.getRowCount() - 1, lColID);

                    //Set Variable
                    if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() != null) {
                        DataModelH.SetVariable(TableH.getRowCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                    } else {
                        DataModelH.SetVariable(TableH.getRowCount() - 1, "  ");
                    }

                    //Set the Operationg Add/Substract
                    DataModelH.SetOperation(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                    //Set stat - Include it in calculation or not
                    DataModelH.SetInclude(TableH.getRowCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                    //Set Formula
                    DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                } else {
                //DataModelH.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj());
                    //Set Column ID

                    rowData = new Object[2];
                    rowData[0] = (String) ObjColumn.getAttribute("CAPTION").getObj();
                    rowData[1] = "0.00";
                    DataModelH.addRow(rowData);

                    DataModelH.SetColID(TableH.getRowCount() - 1, lColID);

                    //Set Variable
                    if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() == null) {
                        DataModelH.SetVariable(TableH.getRowCount() - 1, "  ");
                    } else {
                        DataModelH.SetVariable(TableH.getRowCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                    }

                    //Set the Operationg Add/Substract
                    DataModelH.SetOperation(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                    //Set stat - Include it in calculation or not
                    DataModelH.SetInclude(TableH.getRowCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                    //Set Formula
                    DataModelH.SetFormula(TableH.getRowCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                }
            }
        }
    }

    private void FormatGrid() {
        HashMap ColList = new HashMap();

        try {

            txtGrossAmount.requestFocus();
            DataModelL = new EITLTableModel();

            TableL.removeAll();
            TableL.setModel(DataModelL);

            //Set the table Readonly
            DataModelL.TableReadOnly(false);
            //DataModelL.SetReadOnly(4);

            ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=19 AND HIDDEN=0 AND SHOW_LAST=0 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
            for (int i = 1; i <= ColList.size(); i++) {
                clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));

                //Add Column First
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //0

                if (ObjColumn.getAttribute("NUMERIC").getBool()) {
                    DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                } else {
                    DataModelL.SetNumeric(TableL.getColumnCount() - 1, false);
                }

                String Variable = (String) ObjColumn.getAttribute("VARIABLE").getObj();

                if (Variable.equals("QTY") || Variable.equals("RATE") || Variable.equals("GROSS_AMOUNT") || Variable.equals("ACCESS_AMOUNT")) {
                    DataModelL.SetColID(TableL.getColumnCount() - 1, -99);
                } else {
                    DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
                }

                DataModelL.SetVariable(TableL.getColumnCount() - 1, Variable.trim());
                DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
                DataModelL.SetInclude(TableL.getColumnCount() - 1, true);

                if (ObjColumn.getAttribute("READONLY").getBool()) {
                    DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
                }
            }

            SetupColumns();

            //Now hide the column 1
            TableColumnModel ColModel = TableL.getColumnModel();
            TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //------- Install Table List Selection Listener ------//
            TableL.getColumnModel().getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent e) {
                            int last = TableL.getSelectedColumn();
                            String strVar = DataModelL.getVariable(last);

                            //=============== Cell Editing Routine =======================//
                            try {
                                cellLastValueL = (String) TableL.getValueAt(TableL.getSelectedRow(), TableL.getSelectedColumn());

                                TableL.editCellAt(TableL.getSelectedRow(), TableL.getSelectedColumn());
                                if (TableL.getEditorComponent() instanceof JTextComponent) {
                                    ((JTextComponent) TableL.getEditorComponent()).selectAll();
                                }
                            } catch (Exception cell) {
                            }
                    //============= Cell Editing Routine Ended =================//

                        }
                    }
            );
            //===================================================//

            //----- Install Table Model Event Listener -------//
            TableL.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int col = e.getColumn();

                        //=========== Cell Update Prevention Check ===========//
                        String curValue = (String) TableL.getValueAt(TableL.getSelectedRow(), e.getColumn());
                        if (curValue.equals(cellLastValueL)) {
                            return;
                        }
                        //====================================================//

                        if (DoNotEvaluate) {
                            return;
                        }

                        if (!Updating) {
                            UpdateResults(col);
                        }

                        //If Item ID has changed
                        if (col == DataModelL.getColFromVariable("ITEM_ID")) {
                            try {
                                DoNotEvaluate = true; //Stops Formula Evaluation
                                String lItemID = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                                String lItemName = clsItem.getItemName((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lHsnSacCode = clsItem.getHsnSacCode((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lWareHouseID = clsItem.getItemWareHouseID((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                String lLocationID = clsItem.getItemLocationID((int) EITLERPGLOBAL.gCompanyID, lItemID);

                                TableL.setValueAt(lItemName, TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_NAME"));
                                TableL.setValueAt(lHsnSacCode, TableL.getSelectedRow(), DataModelL.getColFromVariable("HSN_SAC_CODE"));
                                TableL.setValueAt(lWareHouseID, TableL.getSelectedRow(), DataModelL.getColFromVariable("WAREHOUSE_ID"));
                                TableL.setValueAt(lLocationID, TableL.getSelectedRow(), DataModelL.getColFromVariable("LOCATION_ID"));

                                double UnitRate = clsItem.getRate((int) EITLERPGLOBAL.gCompanyID, lItemID);
                                if (Double.parseDouble((String) DataModelL.getValueByVariable("RATE", TableL.getSelectedRow())) <= 0) {
                                    TableL.setValueAt(Double.toString(UnitRate), TableL.getSelectedRow(), DataModelL.getColFromVariable("RATE"));
                                }

                                int lItemUnit = clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID, lItemID);
                                TableL.setValueAt(Integer.toString(lItemUnit), TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT"));
                                String lUnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", lItemUnit);
                                TableL.setValueAt(lUnitName, TableL.getSelectedRow(), DataModelL.getColFromVariable("UNIT_NAME"));
                                DoNotEvaluate = false;
                            } catch (Exception ex) {
                                DoNotEvaluate = false;
                            }
                        }

                        //New Change if Excise is changed then Update Cenvate Amount if Excise Gatepass given is true
                        if (col == DataModelL.getColFromVariable("EXCISE") || col == (DataModelL.getColFromVariable("EXCISE") - 1)) {
                            boolean GatepassGiven = DataModelL.getBoolValueByVariable("EXCISE_GATEPASS_GIVEN", TableL.getSelectedRow());
                            if (GatepassGiven && (!Updating)) {
                                try {
                                    DoNotEvaluate = true;
                                    DataModelL.setValueAt(DataModelL.getValueByVariable("EXCISE", TableL.getSelectedRow()), TableL.getSelectedRow(), DataModelL.getColFromVariable("CENVATE"));
                                    DataModelL.setValueAt("0", TableL.getSelectedRow(), DataModelL.getColFromVariable("CENVATE") - 1);
                                    DoNotEvaluate = false;
                                } catch (Exception q) {
                                    DoNotEvaluate = false;
                                }
                            }
                        }
                        // End of new change

                    }
                }
            });

            int ImportCol = DataModelL.getColFromVariable("EXCISE_GATEPASS_GIVEN");
            Renderer.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            TableL.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            TableL.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
        } catch (Exception e) {

        }
    }

    private void SetupColumns() {
        HashMap List = new HashMap();
        HashMap ColList = new HashMap();

        List = clsColumn.getList(" WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " AND MODULE_ID=19 AND HEADER_LINE='L' ORDER BY COL_ORDER");
        TableColumnModel ColModel = TableL.getColumnModel();

        for (int i = 1; i <= List.size(); i++) {
            clsColumn ObjColumn = (clsColumn) List.get(Integer.toString(i));
            int lTaxID = (int) ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID = (int) ObjColumn.getAttribute("SR_NO").getVal();

            clsTaxColumn ObjTax = (clsTaxColumn) clsTaxColumn.getObject((int) EITLERPGLOBAL.gCompanyID, lTaxID);
            if ((boolean) ObjTax.getAttribute("USE_PERCENT").getBool()) {
                
                String VariableName = (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj() ;
                
                //Add Percentage Column
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj() + "%");

                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);

                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);

                //Set Variable for % Column. It will be P_ID
                DataModelL.SetVariable(TableL.getColumnCount() - 1, "P_" + Integer.toString(lColID));

                //Set the Operationg Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");

                //Set stat - Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, true);

                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
                
//                if (VariableName.equals("EXCISE") || VariableName.equals("ST") || VariableName.equals("OCTROI") || VariableName.equals("CENVATE") || VariableName.equals("SERVICE_TAX")) {
//                    DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
//                }

                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj());

                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);

                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);

                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() != null) {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                } else {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, "  ");
                }

                //Set the Operation Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                //Set stat - Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
                
//                if (VariableName.equals("EXCISE") || VariableName.equals("ST") || VariableName.equals("OCTROI") || VariableName.equals("CENVATE") || VariableName.equals("SERVICE_TAX")) {
//                    DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
//                }
            } else {
                String VariableName = (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj() ;
                
                DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj());

                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
                //Set Column ID
                DataModelL.SetColID(TableL.getColumnCount() - 1, lColID);

                //Set Variable
                if (ObjColumn.getAttribute("VARIABLE_NAME").getObj() == null) {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, "  ");
                } else {
                    DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }

                //Set the Operation Add/Substract
                DataModelL.SetOperation(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("OPERATION").getObj());

                //Include it in calculation or not
                DataModelL.SetInclude(TableL.getColumnCount() - 1, (boolean) ObjTax.getAttribute("NO_CALCULATION").getBool());

                //Set Formula
                DataModelL.SetFormula(TableL.getColumnCount() - 1, (String) ObjTax.getAttribute("FORMULA").getObj());

                //Control Column Visibility
                if (!ObjTax.getAttribute("VISIBLE_ON_FORM").getBool()) {
                    ColModel.getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
                    ColModel.getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
                }
                
//                if (VariableName.equals("EXCISE") || VariableName.equals("ST") || VariableName.equals("OCTROI") || VariableName.equals("CENVATE") || VariableName.equals("SERVICE_TAX")) {
//                    DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
//                }
            }
        }

        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableL.setRowSelectionAllowed(true);
        TableL.setColumnSelectionAllowed(true);

        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=19 AND HIDDEN=0 AND SHOW_LAST=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for (int i = 1; i <= ColList.size(); i++) {
            clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));

            //Add Column First
            DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //0

            if (ObjColumn.getAttribute("NUMERIC").getBool()) {
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, true);
            } else {
                DataModelL.SetNumeric(TableL.getColumnCount() - 1, false);
            }

            String Variable = (String) ObjColumn.getAttribute("VARIABLE").getObj();

            if (Variable.equals("QTY") || Variable.equals("RATE") || Variable.equals("GROSS_AMOUNT") || Variable.equals("ACCESS_AMOUNT")) {
                DataModelL.SetColID(TableL.getColumnCount() - 1, -99);
            } else {
                DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
            }

            DataModelL.SetVariable(TableL.getColumnCount() - 1, Variable.trim());
            DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
            DataModelL.SetInclude(TableL.getColumnCount() - 1, true);

            if (ObjColumn.getAttribute("READONLY").getBool()) {
                DataModelL.SetReadOnly(TableL.getColumnCount() - 1);
            }
        }

        ColList = clsSystemColumn.getList(" WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=19 AND HIDDEN=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for (int i = 1; i <= ColList.size(); i++) {
            clsSystemColumn ObjColumn = (clsSystemColumn) ColList.get(Integer.toString(i));

            //Add Column First
            DataModelL.addColumn((String) ObjColumn.getAttribute("CAPTION").getObj()); //
            DataModelL.SetColID(TableL.getColumnCount() - 1, 0);
            DataModelL.SetVariable(TableL.getColumnCount() - 1, (String) ObjColumn.getAttribute("VARIABLE").getObj());
            DataModelL.SetOperation(TableL.getColumnCount() - 1, "-");
            DataModelL.SetInclude(TableL.getColumnCount() - 1, true);
            DataModelL.SetNumeric(TableL.getColumnCount() - 1, ObjColumn.getAttribute("NUMERIC").getBool());

            DataModelL.SetReadOnly(TableL.getColumnCount() - 1);

            //Hide the Column
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setPreferredWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMaxWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setMinWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount() - 1).setWidth(0);
        }
    }

    private void UpdateAmounts() {

        //== Final Pass - Update the Net Amount ==
        double lnNetAmount = 0;
        double lnColValue = 0;
        double lnGrossAmount = 0, lnSumNetAmount = 0;
        int NetAmountCol = 0, GrossAmountCol = 0;

        NetAmountCol = DataModelL.getColFromVariable("ACCESS_AMOUNT");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            if (TableL.getValueAt(i, NetAmountCol) != null) {
                lnSumNetAmount = lnSumNetAmount + Double.parseDouble((String) TableL.getValueAt(i, NetAmountCol));
            }
        }

        for (int c = 0; c < TableH.getRowCount(); c++) {
            if (DataModelH.getInclude(c) == false) {
                //Read column value
                if (TableH.getValueAt(c, 1).toString().equals("")) {
                    lnColValue = 0;
                } else {
                    lnColValue = Double.parseDouble((String) TableH.getValueAt(c, 1));
                }

                if (DataModelH.getOperation(c).equals("+")) //Add
                {
                    lnGrossAmount = lnGrossAmount + lnColValue;
                } else //Substract
                {
                    lnGrossAmount = lnGrossAmount - lnColValue;
                }
            }
        }

        txtGrossAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumNetAmount, 5)));
        txtNetAmount.setText(Double.toString(EITLERPGLOBAL.round(lnSumNetAmount + lnGrossAmount, 5)));

        lnGrossAmount = 0;
        GrossAmountCol = DataModelL.getColFromVariable("GROSS_AMOUNT");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            if (TableL.getValueAt(i, GrossAmountCol) != null) {
                lnGrossAmount = lnGrossAmount + Double.parseDouble((String) TableL.getValueAt(i, GrossAmountCol));
            }
        }

    }

    private void SetNumberFormats() {
        /* DecimalFormat decimalFormat=new DecimalFormat("0.00");
         NumberFormatter ObjFormater=new NumberFormatter(decimalFormat);
         ObjFormater.setAllowsInvalid(false);
         txtCurrencyRate.setFormatterFactory(new DefaultFormatterFactory(ObjFormater)*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        ToolBar = new javax.swing.JToolBar();
        cmdTop = new javax.swing.JButton();
        cmdBack = new javax.swing.JButton();
        cmdNext = new javax.swing.JButton();
        cmdLast = new javax.swing.JButton();
        cmdNew = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdFilter = new javax.swing.JButton();
        cmdPreview = new javax.swing.JButton();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        cmdAmend = new javax.swing.JButton();
        cmdCopy = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtQTID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSuppCode = new javax.swing.JTextField();
        txtSuppName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtQTNO = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtINQNO = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        cmdInsert = new javax.swing.JButton();
        cmdNext1 = new javax.swing.JButton();
        lblRevNo = new javax.swing.JLabel();
        cmdRemarksBig = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableL = new javax.swing.JTable();
        HeaderPane = new javax.swing.JScrollPane();
        TableH = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cmdNext2 = new javax.swing.JButton();
        cmdNext4 = new javax.swing.JButton();
        txtGrossAmount = new javax.swing.JTextField();
        txtNetAmount = new javax.swing.JTextField();
        cmdShowInquiry = new javax.swing.JButton();
        Tab2 = new javax.swing.JPanel();
        cmdNext5 = new javax.swing.JButton();
        cmdBack2 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtPaymentTerm = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPriceBasisTerm = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtDiscountTerm = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtExciseTerm = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtSTTerm = new javax.swing.JTextField();
        txtFreightTerm = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtPFTerm = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtOctroiTerm = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txtFOBTerm = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtCIETerm = new javax.swing.JTextField();
        txtTCCTerm = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtInsuranceTerm = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtCenvatTerm = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDespatchTerm = new javax.swing.JTextArea();
        jLabel38 = new javax.swing.JLabel();
        txtServiceTaxTerm = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtOthersTerm = new javax.swing.JTextField();
        Tab3 = new javax.swing.JPanel();
        cmdNext6 = new javax.swing.JButton();
        cmdBack3 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        txtCGSTTerm = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtSGSTTerm = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtGSTCompCessTerm = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtIGSTTerm = new javax.swing.JTextField();
        txtCompositionTerm = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtRCMTerm = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        cmbHierarchy = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        txtFrom = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtFromRemarks = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        OpgApprove = new javax.swing.JRadioButton();
        OpgFinal = new javax.swing.JRadioButton();
        OpgReject = new javax.swing.JRadioButton();
        OpgHold = new javax.swing.JRadioButton();
        jLabel33 = new javax.swing.JLabel();
        cmbSendTo = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        txtToRemarks = new javax.swing.JTextField();
        cmdNext3 = new javax.swing.JButton();
        cmdFromRemarksBig = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdPreviewA = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        ToolBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        ToolBar.setRollover(true);

        cmdTop.setToolTipText("First Record");
        cmdTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTopActionPerformed(evt);
            }
        });
        ToolBar.add(cmdTop);

        cmdBack.setToolTipText("Previous Record");
        cmdBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackActionPerformed(evt);
            }
        });
        ToolBar.add(cmdBack);

        cmdNext.setToolTipText("Next Record");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });
        ToolBar.add(cmdNext);

        cmdLast.setToolTipText("Last Record");
        cmdLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLastActionPerformed(evt);
            }
        });
        ToolBar.add(cmdLast);

        cmdNew.setToolTipText("Add Record");
        cmdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewActionPerformed(evt);
            }
        });
        ToolBar.add(cmdNew);

        cmdEdit.setToolTipText("Edit Record");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });
        ToolBar.add(cmdEdit);

        cmdDelete.setToolTipText("Delete Record");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        ToolBar.add(cmdDelete);

        cmdSave.setToolTipText("Save Record");
        cmdSave.setEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        ToolBar.add(cmdSave);

        cmdCancel.setToolTipText("Cancel");
        cmdCancel.setEnabled(false);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        ToolBar.add(cmdCancel);

        cmdFilter.setToolTipText("Filter Records");
        cmdFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFilterActionPerformed(evt);
            }
        });
        ToolBar.add(cmdFilter);

        cmdPreview.setToolTipText("Print Preview");
        ToolBar.add(cmdPreview);

        cmdPrint.setToolTipText("Print");
        ToolBar.add(cmdPrint);

        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        ToolBar.add(cmdExit);

        cmdAmend.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cmdAmend.setText("Amend");
        cmdAmend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAmendActionPerformed(evt);
            }
        });
        ToolBar.add(cmdAmend);

        cmdCopy.setText("Copy");
        cmdCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCopyActionPerformed(evt);
            }
        });
        ToolBar.add(cmdCopy);

        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("QUOTATION");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 804, 25);

        Tab1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.setLayout(null);

        jLabel2.setText("Quote ID");
        Tab1.add(jLabel2);
        jLabel2.setBounds(24, 24, 56, 17);

        txtQTID.setEditable(false);
        Tab1.add(txtQTID);
        txtQTID.setBounds(84, 24, 114, 27);

        jLabel3.setText("Date");
        Tab1.add(jLabel3);
        jLabel3.setBounds(245, 24, 30, 17);

        txtDate.setName("QUOT_DATE"); // NOI18N
        txtDate.setNextFocusableComponent(txtQTNO);
        txtDate.setEnabled(false);
        txtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateFocusGained(evt);
            }
        });
        Tab1.add(txtDate);
        txtDate.setBounds(285, 24, 100, 27);

        jLabel4.setText("Supplier");
        Tab1.add(jLabel4);
        jLabel4.setBounds(30, 95, 52, 17);

        txtSuppCode.setEnabled(false);
        txtSuppCode.setName("SUPP_ID"); // NOI18N
        txtSuppCode.setNextFocusableComponent(txtQTNO);
        txtSuppCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusLost(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppCodeKeyPressed(evt);
            }
        });
        Tab1.add(txtSuppCode);
        txtSuppCode.setBounds(84, 95, 114, 27);

        txtSuppName.setEditable(false);
        Tab1.add(txtSuppName);
        txtSuppName.setBounds(212, 95, 302, 27);

        jLabel5.setText("Quote No");
        Tab1.add(jLabel5);
        jLabel5.setBounds(20, 130, 60, 17);

        txtQTNO.setName("QUOT_NO"); // NOI18N
        txtQTNO.setNextFocusableComponent(txtRemarks);
        txtQTNO.setEnabled(false);
        txtQTNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQTNOFocusGained(evt);
            }
        });
        Tab1.add(txtQTNO);
        txtQTNO.setBounds(84, 130, 114, 27);

        jLabel6.setText("Inquiry No");
        Tab1.add(jLabel6);
        jLabel6.setBounds(10, 62, 70, 17);

        txtINQNO.setName("INQUIRY_NO"); // NOI18N
        txtINQNO.setNextFocusableComponent(cmdInsert);
        txtINQNO.setEnabled(false);
        txtINQNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtINQNOFocusGained(evt);
            }
        });
        txtINQNO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtINQNOKeyPressed(evt);
            }
        });
        Tab1.add(txtINQNO);
        txtINQNO.setBounds(84, 60, 114, 27);

        jLabel21.setText("Remarks");
        Tab1.add(jLabel21);
        jLabel21.setBounds(24, 164, 60, 17);

        txtRemarks.setName("REMARKS"); // NOI18N
        txtRemarks.setNextFocusableComponent(cmdNext1);
        txtRemarks.setEnabled(false);
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRemarksFocusGained(evt);
            }
        });
        Tab1.add(txtRemarks);
        txtRemarks.setBounds(84, 164, 430, 27);

        cmdInsert.setText("Insert from Inquiry");
        cmdInsert.setName("INQUIRY_NO"); // NOI18N
        cmdInsert.setNextFocusableComponent(txtSuppCode);
        cmdInsert.setEnabled(false);
        cmdInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInsertActionPerformed(evt);
            }
        });
        Tab1.add(cmdInsert);
        cmdInsert.setBounds(212, 60, 152, 29);

        cmdNext1.setText("Next >>");
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });
        Tab1.add(cmdNext1);
        cmdNext1.setBounds(640, 320, 90, 29);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(201, 26, 37, 17);

        cmdRemarksBig.setText("...");
        cmdRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemarksBigActionPerformed(evt);
            }
        });
        Tab1.add(cmdRemarksBig);
        cmdRemarksBig.setBounds(516, 164, 40, 21);

        jTabbedPane1.addTab("Header", null, Tab1, "Quotation Header");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(null);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("Quotation Items");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(7, 14, 108, 15);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.add(jPanel4);
        jPanel4.setBounds(115, 21, 430, 4);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jPanel2.add(cmdAdd);
        cmdAdd.setBounds(554, 8, 88, 29);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setEnabled(false);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        jPanel2.add(cmdRemove);
        cmdRemove.setBounds(646, 8, 92, 29);

        jScrollPane1.setNextFocusableComponent(cmdNext4);

        TableL.setModel(new javax.swing.table.DefaultTableModel(
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
        TableL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TableLFocusLost(evt);
            }
        });
        TableL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableLKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TableL);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(6, 38, 732, 176);

        TableH.setModel(new javax.swing.table.DefaultTableModel(
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
        HeaderPane.setViewportView(TableH);

        jPanel2.add(HeaderPane);
        HeaderPane.setBounds(6, 240, 254, 120);

        jLabel18.setText("Gross Amount");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(494, 225, 99, 17);

        jLabel19.setText("Net Amount");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(509, 259, 82, 17);

        cmdNext2.setText("Next >>");
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });
        jPanel2.add(cmdNext2);
        cmdNext2.setBounds(640, 320, 90, 29);

        cmdNext4.setText("<<Back");
        cmdNext4.setNextFocusableComponent(cmdNext2);
        cmdNext4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext4ActionPerformed(evt);
            }
        });
        jPanel2.add(cmdNext4);
        cmdNext4.setBounds(540, 320, 100, 29);

        txtGrossAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtGrossAmount.setEditable(false);
        txtGrossAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(txtGrossAmount);
        txtGrossAmount.setBounds(598, 224, 140, 27);

        txtNetAmount.setBackground(new java.awt.Color(255, 255, 204));
        txtNetAmount.setEditable(false);
        txtNetAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(txtNetAmount);
        txtNetAmount.setBounds(598, 256, 138, 27);

        cmdShowInquiry.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        cmdShowInquiry.setText("Show Inquiry");
        cmdShowInquiry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowInquiryActionPerformed(evt);
            }
        });
        jPanel2.add(cmdShowInquiry);
        cmdShowInquiry.setBounds(317, 218, 118, 25);

        jTabbedPane1.addTab("Item Information", null, jPanel2, "Quotation Item");

        Tab2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.setLayout(null);

        cmdNext5.setText("Next >>");
        cmdNext5.setNextFocusableComponent(cmdBack2);
        cmdNext5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext5ActionPerformed(evt);
            }
        });
        Tab2.add(cmdNext5);
        cmdNext5.setBounds(640, 330, 102, 29);

        cmdBack2.setText("<< Back");
        cmdBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack2ActionPerformed(evt);
            }
        });
        Tab2.add(cmdBack2);
        cmdBack2.setBounds(530, 330, 102, 29);

        jLabel16.setText("Payment Terms");
        Tab2.add(jLabel16);
        jLabel16.setBounds(8, 14, 96, 17);

        txtPaymentTerm.setEnabled(false);
        txtPaymentTerm.setName("PAYMENT_TERM"); // NOI18N
        txtPaymentTerm.setNextFocusableComponent(txtPriceBasisTerm);
        Tab2.add(txtPaymentTerm);
        txtPaymentTerm.setBounds(118, 10, 310, 27);

        jLabel17.setText("Price Basis");
        Tab2.add(jLabel17);
        jLabel17.setBounds(8, 42, 96, 17);

        txtPriceBasisTerm.setEnabled(false);
        txtPriceBasisTerm.setName("PRICE_BASIS_TERM"); // NOI18N
        txtPriceBasisTerm.setNextFocusableComponent(txtDiscountTerm);
        txtPriceBasisTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriceBasisTermKeyPressed(evt);
            }
        });
        Tab2.add(txtPriceBasisTerm);
        txtPriceBasisTerm.setBounds(122, 38, 190, 27);

        jLabel22.setText("Discount");
        Tab2.add(jLabel22);
        jLabel22.setBounds(324, 41, 96, 17);

        txtDiscountTerm.setName("DISCOUNT_TERM"); // NOI18N
        txtDiscountTerm.setNextFocusableComponent(txtExciseTerm);
        txtDiscountTerm.setEnabled(false);
        txtDiscountTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscountTermKeyPressed(evt);
            }
        });
        Tab2.add(txtDiscountTerm);
        txtDiscountTerm.setBounds(422, 38, 206, 27);

        jLabel23.setText("Excise");
        Tab2.add(jLabel23);
        jLabel23.setBounds(10, 68, 96, 17);

        txtExciseTerm.setEnabled(false);
        txtExciseTerm.setName("EXCISE_TERM"); // NOI18N
        txtExciseTerm.setNextFocusableComponent(txtSTTerm);
        txtExciseTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExciseTermKeyPressed(evt);
            }
        });
        Tab2.add(txtExciseTerm);
        txtExciseTerm.setBounds(120, 60, 190, 27);

        jLabel24.setText("S.T. ");
        Tab2.add(jLabel24);
        jLabel24.setBounds(325, 65, 96, 17);

        txtSTTerm.setName("ST_TERM"); // NOI18N
        txtSTTerm.setNextFocusableComponent(txtPFTerm);
        txtSTTerm.setEnabled(false);
        txtSTTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSTTermKeyPressed(evt);
            }
        });
        Tab2.add(txtSTTerm);
        txtSTTerm.setBounds(422, 64, 206, 27);

        txtFreightTerm.setName("FREIGHT_TERM"); // NOI18N
        txtFreightTerm.setNextFocusableComponent(txtOctroiTerm);
        txtFreightTerm.setEnabled(false);
        txtFreightTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFreightTermKeyPressed(evt);
            }
        });
        Tab2.add(txtFreightTerm);
        txtFreightTerm.setBounds(422, 90, 206, 27);

        jLabel27.setText("Freight");
        Tab2.add(jLabel27);
        jLabel27.setBounds(324, 93, 96, 17);

        txtPFTerm.setEnabled(false);
        txtPFTerm.setName("PF_TERM"); // NOI18N
        txtPFTerm.setNextFocusableComponent(txtFreightTerm);
        txtPFTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPFTermKeyPressed(evt);
            }
        });
        Tab2.add(txtPFTerm);
        txtPFTerm.setBounds(122, 90, 190, 27);

        jLabel28.setText("P. & F.");
        Tab2.add(jLabel28);
        jLabel28.setBounds(10, 94, 96, 17);

        jLabel29.setText("Pattern Cost(Rs.)");
        Tab2.add(jLabel29);
        jLabel29.setBounds(10, 120, 110, 17);

        txtOctroiTerm.setEnabled(false);
        txtOctroiTerm.setName("OCTROI_TERM"); // NOI18N
        txtOctroiTerm.setNextFocusableComponent(txtServiceTaxTerm);
        txtOctroiTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOctroiTermKeyPressed(evt);
            }
        });
        Tab2.add(txtOctroiTerm);
        txtOctroiTerm.setBounds(122, 116, 190, 27);

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab2.add(jPanel8);
        jPanel8.setBounds(10, 144, 676, 6);

        jLabel30.setText("FOB");
        Tab2.add(jLabel30);
        jLabel30.setBounds(14, 164, 96, 17);

        txtFOBTerm.setName("FOB_TERM"); // NOI18N
        txtFOBTerm.setNextFocusableComponent(txtCIETerm);
        txtFOBTerm.setEnabled(false);
        txtFOBTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFOBTermKeyPressed(evt);
            }
        });
        Tab2.add(txtFOBTerm);
        txtFOBTerm.setBounds(106, 160, 206, 27);

        jLabel37.setText("CIE");
        Tab2.add(jLabel37);
        jLabel37.setBounds(330, 162, 96, 17);

        txtCIETerm.setName("CIE_TERM"); // NOI18N
        txtCIETerm.setNextFocusableComponent(txtInsuranceTerm);
        txtCIETerm.setEnabled(false);
        txtCIETerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCIETermKeyPressed(evt);
            }
        });
        Tab2.add(txtCIETerm);
        txtCIETerm.setBounds(422, 158, 206, 27);

        txtTCCTerm.setName("TCC_TERM"); // NOI18N
        txtTCCTerm.setNextFocusableComponent(txtCenvatTerm);
        txtTCCTerm.setEnabled(false);
        txtTCCTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTCCTermKeyPressed(evt);
            }
        });
        Tab2.add(txtTCCTerm);
        txtTCCTerm.setBounds(422, 184, 206, 27);

        jLabel42.setText("TCC");
        Tab2.add(jLabel42);
        jLabel42.setBounds(330, 188, 96, 17);

        txtInsuranceTerm.setName("INSURANCE_TERM"); // NOI18N
        txtInsuranceTerm.setNextFocusableComponent(txtTCCTerm);
        txtInsuranceTerm.setEnabled(false);
        txtInsuranceTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInsuranceTermKeyPressed(evt);
            }
        });
        Tab2.add(txtInsuranceTerm);
        txtInsuranceTerm.setBounds(106, 186, 206, 27);

        jLabel43.setText("Insurance");
        Tab2.add(jLabel43);
        jLabel43.setBounds(14, 190, 96, 17);

        jLabel44.setText("Cenvat");
        Tab2.add(jLabel44);
        jLabel44.setBounds(16, 216, 96, 17);

        txtCenvatTerm.setName("CENVAT_TERM"); // NOI18N
        txtCenvatTerm.setNextFocusableComponent(txtDespatchTerm);
        txtCenvatTerm.setEnabled(false);
        txtCenvatTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCenvatTermKeyPressed(evt);
            }
        });
        Tab2.add(txtCenvatTerm);
        txtCenvatTerm.setBounds(106, 212, 206, 27);

        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab2.add(jPanel9);
        jPanel9.setBounds(10, 250, 676, 6);

        jLabel45.setText("Despatch Instructions");
        Tab2.add(jLabel45);
        jLabel45.setBounds(10, 260, 173, 17);

        txtDespatchTerm.setEnabled(false);
        txtDespatchTerm.setName("DESPATCH_TERM"); // NOI18N
        txtDespatchTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDespatchTermKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtDespatchTerm);

        Tab2.add(jScrollPane3);
        jScrollPane3.setBounds(10, 280, 382, 80);

        jLabel38.setText("Service Tax");
        Tab2.add(jLabel38);
        jLabel38.setBounds(323, 119, 96, 17);

        txtServiceTaxTerm.setName("OCTROI_TERM"); // NOI18N
        txtServiceTaxTerm.setNextFocusableComponent(txtFOBTerm);
        txtServiceTaxTerm.setEnabled(false);
        Tab2.add(txtServiceTaxTerm);
        txtServiceTaxTerm.setBounds(422, 116, 206, 27);

        jLabel46.setText("Others");
        Tab2.add(jLabel46);
        jLabel46.setBounds(329, 220, 96, 17);

        txtOthersTerm.setEnabled(false);
        txtOthersTerm.setName("TCC_TERM"); // NOI18N
        txtOthersTerm.setNextFocusableComponent(txtCenvatTerm);
        Tab2.add(txtOthersTerm);
        txtOthersTerm.setBounds(421, 216, 206, 27);

        jTabbedPane1.addTab("Other Information", Tab2);

        Tab3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.setLayout(null);

        cmdNext6.setText("Next >>");
        cmdNext6.setNextFocusableComponent(cmdBack2);
        cmdNext6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext6ActionPerformed(evt);
            }
        });
        Tab3.add(cmdNext6);
        cmdNext6.setBounds(640, 330, 102, 29);

        cmdBack3.setText("<< Back");
        cmdBack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBack3ActionPerformed(evt);
            }
        });
        Tab3.add(cmdBack3);
        cmdBack3.setBounds(530, 330, 102, 29);

        jLabel25.setText("CGST");
        Tab3.add(jLabel25);
        jLabel25.setBounds(10, 10, 96, 17);

        txtCGSTTerm.setEnabled(false);
        txtCGSTTerm.setName("CGST_TERM"); // NOI18N
        txtCGSTTerm.setNextFocusableComponent(txtSGSTTerm);
        Tab3.add(txtCGSTTerm);
        txtCGSTTerm.setBounds(130, 10, 210, 27);

        jLabel39.setText("SGST");
        Tab3.add(jLabel39);
        jLabel39.setBounds(10, 50, 96, 17);

        txtSGSTTerm.setEnabled(false);
        txtSGSTTerm.setName("SGST_TERM"); // NOI18N
        txtSGSTTerm.setNextFocusableComponent(txtIGSTTerm);
        txtSGSTTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSGSTTermKeyPressed(evt);
            }
        });
        Tab3.add(txtSGSTTerm);
        txtSGSTTerm.setBounds(130, 50, 210, 27);

        jLabel40.setText("GST Comp Cess");
        Tab3.add(jLabel40);
        jLabel40.setBounds(10, 210, 120, 17);

        txtGSTCompCessTerm.setEnabled(false);
        txtGSTCompCessTerm.setName("GST_COMPENSATION_CESS_TERM"); // NOI18N
        txtGSTCompCessTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGSTCompCessTermKeyPressed(evt);
            }
        });
        Tab3.add(txtGSTCompCessTerm);
        txtGSTCompCessTerm.setBounds(130, 210, 210, 27);

        jLabel41.setText("IGST");
        Tab3.add(jLabel41);
        jLabel41.setBounds(10, 90, 96, 17);

        txtIGSTTerm.setEnabled(false);
        txtIGSTTerm.setName("IGST_TERM"); // NOI18N
        txtIGSTTerm.setNextFocusableComponent(txtCompositionTerm);
        txtIGSTTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIGSTTermKeyPressed(evt);
            }
        });
        Tab3.add(txtIGSTTerm);
        txtIGSTTerm.setBounds(130, 90, 210, 27);

        txtCompositionTerm.setEnabled(false);
        txtCompositionTerm.setName("COMPOSITION_TERM"); // NOI18N
        txtCompositionTerm.setNextFocusableComponent(txtRCMTerm);
        txtCompositionTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCompositionTermKeyPressed(evt);
            }
        });
        Tab3.add(txtCompositionTerm);
        txtCompositionTerm.setBounds(130, 130, 210, 27);

        jLabel49.setText("Composition");
        Tab3.add(jLabel49);
        jLabel49.setBounds(10, 130, 96, 17);

        jLabel50.setText("RCM");
        Tab3.add(jLabel50);
        jLabel50.setBounds(10, 170, 96, 17);

        txtRCMTerm.setEnabled(false);
        txtRCMTerm.setName("RCM_TERM"); // NOI18N
        txtRCMTerm.setNextFocusableComponent(txtGSTCompCessTerm);
        txtRCMTerm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRCMTermKeyPressed(evt);
            }
        });
        Tab3.add(txtRCMTerm);
        txtRCMTerm.setBounds(130, 170, 210, 27);

        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel10);
        jPanel10.setBounds(10, 250, 676, 6);

        jTabbedPane1.addTab("GST Term Information", Tab3);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(null);

        jLabel31.setText("Hierarchy ");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(16, 18, 66, 17);

        cmbHierarchy.setNextFocusableComponent(txtFrom);
        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbHierarchyFocusGained(evt);
            }
        });
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbHierarchy);
        cmbHierarchy.setBounds(86, 14, 184, 27);

        jLabel32.setText("From");
        jPanel3.add(jLabel32);
        jLabel32.setBounds(20, 52, 56, 17);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        txtFrom.setNextFocusableComponent(txtFromRemarks);
        jPanel3.add(txtFrom);
        txtFrom.setBounds(86, 50, 182, 27);

        jLabel35.setText("Remarks");
        jPanel3.add(jLabel35);
        jLabel35.setBounds(20, 82, 62, 17);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        txtFromRemarks.setNextFocusableComponent(OpgApprove);
        txtFromRemarks.setEnabled(false);
        jPanel3.add(txtFromRemarks);
        txtFromRemarks.setBounds(86, 78, 518, 27);

        jLabel36.setText("Your Action  ");
        jPanel3.add(jLabel36);
        jLabel36.setBounds(8, 124, 76, 17);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setLayout(null);

        buttonGroup1.add(OpgApprove);
        OpgApprove.setText("Approve & Forward");
        OpgApprove.setEnabled(false);
        OpgApprove.setNextFocusableComponent(OpgFinal);
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 166, 22);

        buttonGroup1.add(OpgFinal);
        OpgFinal.setText("Final Approve");
        OpgFinal.setEnabled(false);
        OpgFinal.setNextFocusableComponent(OpgReject);
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });
        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        buttonGroup1.add(OpgReject);
        OpgReject.setText("Reject");
        OpgReject.setEnabled(false);
        OpgReject.setNextFocusableComponent(OpgHold);
        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        buttonGroup1.add(OpgHold);
        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        OpgHold.setEnabled(false);
        OpgHold.setNextFocusableComponent(cmbSendTo);
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        jPanel3.add(jPanel6);
        jPanel6.setBounds(88, 116, 182, 100);

        jLabel33.setText("Send To");
        jPanel3.add(jLabel33);
        jLabel33.setBounds(18, 228, 60, 17);

        cmbSendTo.setNextFocusableComponent(txtToRemarks);
        cmbSendTo.setEnabled(false);
        cmbSendTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSendToFocusGained(evt);
            }
        });
        cmbSendTo.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                cmbSendToHierarchyChanged(evt);
            }
        });
        jPanel3.add(cmbSendTo);
        cmbSendTo.setBounds(86, 226, 184, 27);

        jLabel34.setText("Remarks");
        jPanel3.add(jLabel34);
        jLabel34.setBounds(16, 264, 60, 17);

        txtToRemarks.setNextFocusableComponent(cmdNext3);
        txtToRemarks.setEnabled(false);
        txtToRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToRemarksFocusGained(evt);
            }
        });
        jPanel3.add(txtToRemarks);
        txtToRemarks.setBounds(86, 260, 516, 27);

        cmdNext3.setText("<< Back");
        cmdNext3.setNextFocusableComponent(cmdSave);
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });
        jPanel3.add(cmdNext3);
        cmdNext3.setBounds(630, 320, 100, 29);

        cmdFromRemarksBig.setText("...");
        cmdFromRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFromRemarksBigActionPerformed(evt);
            }
        });
        jPanel3.add(cmdFromRemarksBig);
        cmdFromRemarksBig.setBounds(610, 77, 37, 20);

        jTabbedPane1.addTab("Approval", null, jPanel3, "Quotation Approval");

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(null);

        jLabel26.setText("Document Approval Status");
        jPanel5.add(jLabel26);
        jLabel26.setBounds(12, 10, 242, 17);

        TableA.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(TableA);

        jPanel5.add(jScrollPane2);
        jScrollPane2.setBounds(12, 40, 594, 144);

        lblDocumentHistory.setText("Document Update History");
        jPanel5.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(12, 191, 182, 17);

        TableHS.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(TableHS);

        jPanel5.add(jScrollPane6);
        jScrollPane6.setBounds(12, 207, 447, 128);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });
        jPanel5.add(cmdViewHistory);
        cmdViewHistory.setBounds(475, 237, 129, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });
        jPanel5.add(cmdNormalView);
        cmdNormalView.setBounds(475, 267, 129, 24);

        cmdPreviewA.setText("Preview Report");
        cmdPreviewA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewAActionPerformed(evt);
            }
        });
        jPanel5.add(cmdPreviewA);
        cmdPreviewA.setBounds(475, 207, 129, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });
        jPanel5.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(475, 297, 129, 24);

        txtAuditRemarks.setEnabled(false);
        jPanel5.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(476, 335, 129, 27);

        jTabbedPane1.addTab("Status", jPanel5);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 70, 760, 398);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 470, 820, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCopyActionPerformed
        // TODO add your handling code here:
        if (EditMode != 0) {
            try {
                clsQuotation objQuot = new clsQuotation();
                clsQuotation objNewQuot = new clsQuotation();

                objQuot.LoadData(EITLERPGLOBAL.gCompanyID);

                String QuotID = JOptionPane.showInputDialog(null, "Enter quotation no.", "");

                if (!QuotID.trim().equals("")) {

                    if (clsQuotation.getDocStatus(EITLERPGLOBAL.gCompanyID, QuotID).trim().equals("")) {

                    } else {
                        JOptionPane.showMessageDialog(null, "Document does not exist / Document is under approval");
                        return;
                    }

                    objNewQuot = (clsQuotation) objQuot.getObject(EITLERPGLOBAL.gCompanyID, QuotID);

                    FormatGrid();

                    DoNotEvaluate = true;

                    for (int i = 1; i <= objNewQuot.colQuotationItems.size(); i++) {
                        //Insert New Row
                        Object[] rowData = new Object[1];
                        DataModelL.addRow(rowData);
                        int NewRow = TableL.getRowCount() - 1;

                        clsQuotationItem ObjItem = (clsQuotationItem) objNewQuot.colQuotationItems.get(Integer.toString(i));

                        DataModelL.setValueByVariable("SR_NO", Integer.toString(i), NewRow);
                        DataModelL.setValueByVariable("INQUIRY_NO", (String) ObjItem.getAttribute("INQUIRY_NO").getObj(), NewRow);
                        DataModelL.setValueByVariable("INQUIRY_SRNO", Integer.toString((int) ObjItem.getAttribute("INQUIRY_SRNO").getVal()), NewRow);
                        DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                        DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), NewRow);
                        DataModelL.setValueByVariable("MAKE", (String) ObjItem.getAttribute("MAKE").getObj(), NewRow);
                        DataModelL.setValueByVariable("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj(), NewRow);
                        DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DELIVERY_DATE").getObj()), NewRow);
                        DataModelL.setValueByVariable("UNIT", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                        String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                        DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                        DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                        DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), NewRow);
                        DataModelL.setValueByVariable("GROSS_AMOUNT", Double.toString(ObjItem.getAttribute("TOTAL_AMOUNT").getVal()), NewRow);
                        DataModelL.setValueByVariable("ACCESS_AMOUNT", Double.toString(ObjItem.getAttribute("ACCESS_AMOUNT").getVal()), NewRow);
                        DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);
                        String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                        DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                        String HsnSacCode = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                        DataModelL.setValueByVariable("HSN_SAC_CODE", HsnSacCode, NewRow);
                        DataModelL.setValueByVariable("SUPP_ITEM_DESC", (String) ObjItem.getAttribute("SUPP_ITEM_DESC").getObj(), NewRow);
                        DataModelL.setValueByVariable("EXCISE_GATEPASS_GIVEN", Boolean.valueOf(ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool()), NewRow);
                    }

                    DoNotEvaluate = false;

                    for (int i = 0; i < TableL.getRowCount(); i++) {
                        TableL.changeSelection(i, 1, false, false);
                        UpdateResults(DataModelL.getColFromVariable("QTY"));
                    }
                }
            } catch (Exception e) {
                DoNotEvaluate = false;
            }
        }
    }//GEN-LAST:event_cmdCopyActionPerformed

    private void cmdAmendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAmendActionPerformed
        // TODO add your handling code here:
        if (EditMode == 0) {
            Amend();
        }
    }//GEN-LAST:event_cmdAmendActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if (TableHS.getRowCount() > 0 && TableHS.getSelectedRow() >= 0) {
            txtAuditRemarks.setText((String) TableHS.getValueAt(TableHS.getSelectedRow(), 4));
            BigEdit bigEdit = new BigEdit();
            bigEdit.theText = txtAuditRemarks;
            bigEdit.ShowEdit();
        }

    }//GEN-LAST:event_cmdShowRemarksActionPerformed

     private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
         // TODO add your handling code here:
         if (!OpgFinal.isEnabled()) {
             OpgHold.setSelected(true);
         }
     }//GEN-LAST:event_OpgFinalMouseClicked

     private void cmdFromRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFromRemarksBigActionPerformed
         // TODO add your handling code here:
         BigEdit bigEdit = new BigEdit();
         bigEdit.theText = txtFromRemarks;
         bigEdit.ShowEdit();
     }//GEN-LAST:event_cmdFromRemarksBigActionPerformed

     private void cmdRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemarksBigActionPerformed
         // TODO add your handling code here:
         BigEdit bigEdit = new BigEdit();
         bigEdit.theText = txtRemarks;
         bigEdit.ShowEdit();
     }//GEN-LAST:event_cmdRemarksBigActionPerformed

    private void txtDespatchTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDespatchTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='DESPATCH_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtDespatchTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtDespatchTermKeyPressed

    private void txtCenvatTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCenvatTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='CENVAT_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtCenvatTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtCenvatTermKeyPressed

    private void txtTCCTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTCCTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='TCC_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtTCCTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtTCCTermKeyPressed

    private void txtInsuranceTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInsuranceTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='INSURANCE_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtInsuranceTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtInsuranceTermKeyPressed

    private void txtCIETermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCIETermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='CIE_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtCIETerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtCIETermKeyPressed

    private void txtFOBTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFOBTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='FOB_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtFOBTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtFOBTermKeyPressed

    private void txtOctroiTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOctroiTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='OCTROI_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtOctroiTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtOctroiTermKeyPressed

    private void txtFreightTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFreightTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='FREIGHT_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtFreightTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtFreightTermKeyPressed

    private void txtPFTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPFTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='PF_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtPFTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtPFTermKeyPressed

    private void txtSTTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSTTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='ST_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtSTTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtSTTermKeyPressed

    private void txtExciseTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExciseTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='EXCISE_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtExciseTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtExciseTermKeyPressed

    private void txtDiscountTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='DISCOUNT_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtDiscountTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtDiscountTermKeyPressed

    private void txtPriceBasisTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceBasisTermKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND  PARA_ID='PRICE_BASIS_TERM' ORDER BY D_COM_PARAMETER_MAST.DESC";
            aList.ReturnCol = 2;
            aList.ShowReturnCol = false;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {

                txtPriceBasisTerm.setText(aList.ReturnVal);
            }
        }

    }//GEN-LAST:event_txtPriceBasisTermKeyPressed

    private void cmdPreviewAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewAActionPerformed
        // TODO add your handling code here:
        PreviewAuditReport();
    }//GEN-LAST:event_cmdPreviewAActionPerformed

    private void cmdShowInquiryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowInquiryActionPerformed
        // TODO add your handling code here:
        try {
            String DocNo = DataModelL.getValueByVariable("INQUIRY_NO", TableL.getSelectedRow());

            if (!DocNo.trim().equals("")) {
                AppletFrame aFrame = new AppletFrame("Inquiry");
                aFrame.startAppletEx("EITLERP.Purchase.FrmInquiry", "Inquiry");
                FrmInquiry ObjDoc = (FrmInquiry) aFrame.ObjApplet;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            } else {
                JOptionPane.showMessageDialog(null, "Inquiry no. not specified");
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_cmdShowInquiryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjQuotation.LoadData(EITLERPGLOBAL.gCompanyID);
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo = txtQTID.getText();
        ObjQuotation.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdBack2ActionPerformed

    private void cmdNext5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext5ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_cmdNext5ActionPerformed

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        // TODO add your handling code here:
        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();

        if (clsHierarchy.CanSkip((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }

        if (clsHierarchy.CanFinalApprove((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void txtToRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter reamrks for further Approval process ............");
    }//GEN-LAST:event_txtToRemarksFocusGained

    private void cmbSendToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSendToFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Person Name from SendTo list ............");
    }//GEN-LAST:event_cmbSendToFocusGained

    private void cmbSendToHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_cmbSendToHierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSendToHierarchyChanged

    private void cmbHierarchyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbHierarchyFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Hieararchy Name from given list ............");
    }//GEN-LAST:event_cmbHierarchyFocusGained

    private void txtRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Extra Remarks for Quotation ............");
    }//GEN-LAST:event_txtRemarksFocusGained

    private void txtINQNOFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtINQNOFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Inquiry No. from given button ............");
    }//GEN-LAST:event_txtINQNOFocusGained

    private void txtSuppCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Press F1 Key for selection of suppliers ............");
    }//GEN-LAST:event_txtSuppCodeFocusGained

    private void txtQTNOFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQTNOFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Suppliers Quotation No. ............");
    }//GEN-LAST:event_txtQTNOFocusGained

    private void txtDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Quotation Date in DD/MM/YYYY ............");
    }//GEN-LAST:event_txtDateFocusGained

    private void cmdNext4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext4ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_cmdNext4ActionPerformed

    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext3ActionPerformed

    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext2ActionPerformed

    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed

    private void cmdInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInsertActionPerformed
        // TODO add your handling code here:
        SelectInquiry ObjInquiry = new SelectInquiry();

        if (ObjInquiry.ShowList()) {
            DoNotEvaluate = true;

            if (ObjInquiry.colSelItems.size() > 0) {
                clsInquiryItem ObjItem = (clsInquiryItem) ObjInquiry.colSelItems.get(Integer.toString(1));
                String InqNo = (String) ObjItem.getAttribute("INQUIRY_NO").getObj();
                txtINQNO.setText((String) InqNo);
            }

            //It will contain Inquiry Item Objects
            for (int i = 1; i <= ObjInquiry.colSelItems.size(); i++) {
                clsInquiryItem ObjItem = (clsInquiryItem) ObjInquiry.colSelItems.get(Integer.toString(i));

                //Add Blank Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);

                int NewRow = TableL.getRowCount() - 1;

                DataModelL.setValueByVariable("SR_NO", Integer.toString((int) ObjItem.getAttribute("SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_CODE").getObj(), NewRow);
                DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("MAKE", (String) ObjItem.getAttribute("MAKE").getObj(), NewRow);
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_CODE").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                String HsnSacCode = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_CODE").getObj());
                DataModelL.setValueByVariable("HSN_SAC_CODE", HsnSacCode, NewRow);

                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("UNIT", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                DataModelL.setValueByVariable("INQUIRY_NO", (String) ObjItem.getAttribute("INQUIRY_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("INQUIRY_SRNO", Integer.toString((int) ObjItem.getAttribute("SR_NO").getVal()), NewRow);
                DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DELIVERY_DATE").getObj()), NewRow);
                UpdateResults(DataModelL.getColFromVariable("QTY"));
            }
            UpdateSrNo();
            DoNotEvaluate = false;
        }
    }//GEN-LAST:event_cmdInsertActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if (TableL.getRowCount() > 0) {
            DataModelL.removeRow(TableL.getSelectedRow());
            UpdateSrNo();
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void TableLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableLFocusLost
        // TODO add your handling code here:
        //Update Header Custom Columns
        for (int i = 0; i < TableH.getRowCount(); i++) {
            UpdateResults_H(i);
        }
    }//GEN-LAST:event_TableLFocusLost

    private void TableLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyPressed
        // TODO add your handling code here:
        if (EditMode != 0) {

            //=========== Item List ===============
            if (TableL.getSelectedColumn() == DataModelL.getColFromVariable("ITEM_ID")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    LOV aList = new LOV();

                    aList.SQL = "SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND APPROVED=1 AND CANCELLED=0 ORDER BY ITEM_ID";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 2;

                    if (aList.ShowLOV()) {
                        if (TableL.getCellEditor() != null) {
                            TableL.getCellEditor().stopCellEditing();
                        }
                        TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                    }
                }
            }
            //=========================================

            //            if(evt.getKeyCode()==155)//Insert Key Pressed
            //            {
            //                Object[] rowData=new Object[1];
            //                DataModelL.addRow(rowData);
            //                DataModelL.SetUserObject(TableL.getRowCount()-1,new HashMap());
            //                TableL.changeSelection(TableL.getRowCount()-1, 1, false,false);
            //                UpdateSrNo();
            //            }
        }
    }//GEN-LAST:event_TableLKeyPressed

    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:

        if (!txtSuppCode.getText().trim().equals("")) {
            txtPaymentTerm.setEnabled(true);
            int PartyID = clsParty.getPartyIDFromSupplier(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText());
            if (PartyID != 0) {
                txtSuppCode.setText(Integer.toString(PartyID));
                txtSuppName.setText(clsParty.getPartyNameByPartyID(EITLERPGLOBAL.gCompanyID, PartyID));

                //It is a Registered Supplier
                String SuppCode = clsParty.getSupplierCode(EITLERPGLOBAL.gCompanyID, PartyID);
                if (!SuppCode.trim().equals("") && clsSupplier.IsValidSuppCodeEx(EITLERPGLOBAL.gCompanyID, SuppCode)) {

                    clsSupplier tmpObj = new clsSupplier();
                    tmpObj.LoadData(EITLERPGLOBAL.gCompanyID);

                    clsSupplier ObjSupp = (clsSupplier) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, SuppCode);
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        //Insert New Row
                        Object[] rowData = new Object[3];
                        clsSuppTerms ObjItem = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            txtPaymentTerm.setEnabled(false);
                            txtPaymentTerm.setText((String) ObjItem.getAttribute("TERM_DESC").getObj());
                        }
                    }

                }

            } else {
                txtSuppName.setText(clsParty.getPartyNameByPartyID(EITLERPGLOBAL.gCompanyID, Integer.parseInt(txtSuppCode.getText())));

                //Now find out if this has supplier code
                txtPaymentTerm.setText(""); //No Payment term found
                PartyID = Integer.parseInt(txtSuppCode.getText());
                String SuppCode = clsParty.getSupplierCode(EITLERPGLOBAL.gCompanyID, PartyID);
                if (!SuppCode.trim().equals("") && clsSupplier.IsValidSuppCodeEx(EITLERPGLOBAL.gCompanyID, SuppCode)) {

                    clsSupplier tmpObj = new clsSupplier();
                    tmpObj.LoadData(EITLERPGLOBAL.gCompanyID);

                    clsSupplier ObjSupp = (clsSupplier) tmpObj.getObject(EITLERPGLOBAL.gCompanyID, SuppCode);
                    for (int i = 1; i <= ObjSupp.colSuppTerms.size(); i++) {
                        //Insert New Row
                        Object[] rowData = new Object[3];
                        clsSuppTerms ObjItem = (clsSuppTerms) ObjSupp.colSuppTerms.get(Integer.toString(i));
                        String TermType = (String) ObjItem.getAttribute("TERM_TYPE").getObj();

                        if (TermType.equals("P")) {
                            txtPaymentTerm.setEnabled(false);
                            txtPaymentTerm.setText((String) ObjItem.getAttribute("TERM_DESC").getObj());
                        }
                    }

                }

            }
        }


    }//GEN-LAST:event_txtSuppCodeFocusLost

    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT SUPPLIER_CODE,PARTY_NAME,PARTY_ID FROM D_COM_PARTY WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ORDER BY PARTY_NAME";
            //aList.ReturnCol = 1;
            aList.ReturnCol = 3;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 3;

            if (aList.ShowLOV()) {
                txtSuppCode.setText(aList.ReturnVal);
                txtSuppName.setText(clsParty.getPartyNameByPartyID(EITLERPGLOBAL.gCompanyID, Integer.parseInt(aList.ReturnVal)));
            }
        }
        //=========================================

    }//GEN-LAST:event_txtSuppCodeKeyPressed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        Object[] RowData = new Object[1];
        DataModelL.addRow(RowData);
        TableL.changeSelection(TableL.getRowCount() - 1, 1, false, false);
        UpdateSrNo();
        TableL.requestFocus();

    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjQuotation.Close();
        ObjColumn.Close();
        ObjTax.Close();
        ((JFrame) getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed

    private void Add() {

        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        //Now Generate new document no.
        SelectFirstFree aList = new SelectFirstFree();
        aList.ModuleID = 19;

        if (aList.ShowList()) {
            EditMode = EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SelPrefix = aList.Prefix; //Selected Prefix;
            SelSuffix = aList.Suffix;
            FFNo = aList.FirstFreeNo;
            SetupApproval();
            //Display newly generated document no.
            txtQTID.setText(clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, 19, FFNo, false));
            txtDate.setText(EITLERPGLOBAL.getCurrentDate());
            txtINQNO.requestFocus();

            lblTitle.setText("QUOTATION - " + txtQTID.getText());
            lblTitle.setBackground(Color.BLUE);

            txtServiceTaxTerm.setText("N/A");
            txtPriceBasisTerm.setText("N/A");
            txtDiscountTerm.setText("N/A");
            txtExciseTerm.setText("N/A");
            txtSTTerm.setText("N/A");
            txtPFTerm.setText("N/A");
            txtFreightTerm.setText("N/A");
            txtOctroiTerm.setText("N/A");
            txtFOBTerm.setText("N/A");
            txtCIETerm.setText("N/A");
            txtInsuranceTerm.setText("N/A");
            txtTCCTerm.setText("N/A");
            txtCenvatTerm.setText("N/A");
            txtOthersTerm.setText("N/A");

            txtCGSTTerm.setText("N/A");
            txtSGSTTerm.setText("N/A");
            txtIGSTTerm.setText("N/A");
            txtCompositionTerm.setText("N/A");
            txtRCMTerm.setText("N/A");
            txtGSTCompCessTerm.setText("N/A");

        } else {
            JOptionPane.showMessageDialog(null, "You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
    }

    private void Edit() {

        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String lQTNo = (String) ObjQuotation.getAttribute("QUOT_ID").getObj();
        if (ObjQuotation.IsEditable(EITLERPGLOBAL.gCompanyID, lQTNo, EITLERPGLOBAL.gNewUserID)) {
            EITLERPGLOBAL.ChangeCursorToWait(this);
            EditMode = EITLERPGLOBAL.EDIT;
            GenerateCombos();

            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//

            if (ApprovalFlow.IsCreator(19, lQTNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 282)) {
                SetFields(true);
            } else {
                EnableApproval();
            }

            DisableToolbar();
            EITLERPGLOBAL.ChangeCursorToDefault(this);
        } else {
            JOptionPane.showMessageDialog(null, "You cannot edit this record. It is either approved/rejected or waiting approval for other user");
        }
    }

    private void Delete() {

        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String lDocNo = (String) ObjQuotation.getAttribute("QUOT_ID").getObj();

        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record ?", "SDML ERP", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (ObjQuotation.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
                if (ObjQuotation.Delete()) {
                    MoveLast();
                } else {
                    JOptionPane.showMessageDialog(null, "Error occured while deleting. Error is " + ObjQuotation.LastError);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You cannot delete this record. It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }

    private void Save() {
        //Form level validations
        if (Validate() == false) {
            return; //Validation failed
        }

        EITLERPGLOBAL.ChangeCursorToWait(this);

        SetData();

        if (EditMode == EITLERPGLOBAL.ADD) {
            if (ObjQuotation.Insert(SelPrefix, SelSuffix)) {
                MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjQuotation.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.EDIT) {
            if (ObjQuotation.Update()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjQuotation.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }

        if (EditMode == EITLERPGLOBAL.AMEND) {
            if (ObjQuotation.Amend()) {
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null, "Error occured while saving. Error is " + ObjQuotation.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }

        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        ShowMessage("Ready ..........");
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void SetMenuForRights() {
        // --- Add Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 281)) {
            cmdNew.setEnabled(true);
        } else {
            cmdNew.setEnabled(false);
        }

        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,282))
         {
         cmdEdit.setEnabled(true);
         }
         else
         {
         cmdEdit.setEnabled(false);
         }*/

        // --- Delete Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 283)) {
            cmdDelete.setEnabled(true);
        } else {
            cmdDelete.setEnabled(false);
        }

        // --- Print Rights --
        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 284)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        } else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }

        if (clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 287)) {
            cmdAmend.setEnabled(true);
        } else {
            cmdAmend.setEnabled(false);
        }

    }

    private boolean Validate() {
        int ValidEntryCount = 0;
        try {
            //Validates Item Entries
            if (TableL.getRowCount() <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter at least one item");
                return false;
            }

            if (txtSuppCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter party code");
                return false;
            }

            //Search in Table
            int ItemCol = DataModelL.getColFromVariable("ITEM_ID");
            int RateCol = DataModelL.getColFromVariable("RATE");
            int QtyCol = DataModelL.getColFromVariable("QTY");
            int BOECol = DataModelL.getColFromVariable("BOE_NO");

            for (int i = 0; i < TableL.getRowCount(); i++) {
                String ItemID = "";
                double Rate = 0, Qty = 0;

                if (TableL.getValueAt(i, ItemCol) != null && TableL.getValueAt(i, RateCol) != null && TableL.getValueAt(i, QtyCol) != null) {
                    ItemID = (String) TableL.getValueAt(i, ItemCol);
                    Rate = Double.parseDouble((String) TableL.getValueAt(i, RateCol));
                    Qty = Double.parseDouble((String) TableL.getValueAt(i, QtyCol));

                    boolean ValidDate = true;
                    String theDate = DataModelL.getValueByVariable("DELIVERY_DATE", i);

                    if (!EITLERPGLOBAL.isDate(theDate) || theDate.trim().equals("")) {
                        ValidDate = false;
                    }

                    if (clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID, ItemID) && Rate > 0 && Qty > 0 && ValidDate) {
                        ValidEntryCount++;
                    } else {
                        JOptionPane.showMessageDialog(null, "Item entry is not valid. Please be sure to enter following information. \nValid Item ID,Rate,Quantity and Delivery Date");
                        TableL.changeSelection(i, 1, false, false);
                        return false;
                    }

                }

                //Replace X wherever BOE No. is Blank
                if (DataModelL.getValueAt(i, BOECol).toString().trim().equals("")) {
                    DataModelL.setValueAt("X", i, BOECol);
                }
            }

            if (ValidEntryCount == 0) {
                JOptionPane.showMessageDialog(null, "Item entry is not valid. Please verify that you entered following information \nItem ID, Rate, Qty and Delivery Date(DD/MM/YYYY) ");
                return false;
            }

            //Now Header level validations
            if (txtDate.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter Quotation Date");
                return false;
            }
            if (txtQTNO.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter Quote No");
                return false;
            }

            if (cmbHierarchy.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, "Please select the hierarchy.");
                return false;
            }

            if ((!OpgApprove.isSelected()) && (!OpgReject.isSelected()) && (!OpgFinal.isSelected()) && (!OpgHold.isSelected())) {
                JOptionPane.showMessageDialog(null, "Please select the Approval Action");
                return false;
            }

            if (!clsParty.IsValidPartyID(EITLERPGLOBAL.gCompanyID, Integer.parseInt(txtSuppCode.getText()))) {
                JOptionPane.showMessageDialog(null, "Please enter valid supplier code");
                return false;
            }

            if (!EITLERPGLOBAL.isDate(txtDate.getText())) {
                JOptionPane.showMessageDialog(null, "Invalid Quotation Date");
                return false;
            }

            if (OpgReject.isSelected() && txtToRemarks.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter the remarks for rejection");
                return false;
            }

            String docStatus = clsInquiry.getDocStatus(EITLERPGLOBAL.gCompanyID, txtINQNO.getText().trim());

            if (!docStatus.trim().equals("")) {
                JOptionPane.showMessageDialog(null, docStatus);
                return false;
            }

            String SQL = "";
            if (OpgFinal.isSelected()) {
                for (int i = 0; i < TableL.getRowCount(); i++) {
                    String ItemID = DataModelL.getValueByVariable("ITEM_ID", i);
                    String InquiryNo = DataModelL.getValueByVariable("INQUIRY_NO", i);
                    int InquirySrNo = Integer.parseInt(DataModelL.getValueByVariable("INQUIRY_SRNO", i));
                    SQL = "SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE ITEM_CODE='" + ItemID + "' AND INQUIRY_NO='" + InquiryNo + "' "
                            + "AND SR_NO='" + InquirySrNo + "'";
                    if (!data.IsRecordExist(SQL)) {
                        JOptionPane.showMessageDialog(this, "Item code(" + ItemID + ") with Inquiry no(" + InquiryNo + ") and  "
                                + "serial no(" + InquirySrNo + ") does not match in inquiry(" + InquiryNo + ").");
                        return false;
                    } else {
                        ResultSet rsInquiry = data.getResult(SQL);
                        String IndentNo = rsInquiry.getString("INDENT_NO");
                        String IndentSrNo = rsInquiry.getString("INDENT_SRNO");
                        if (!IndentNo.trim().equals("")) {
                            if (!data.IsRecordExist("SELECT * FROM D_INV_INDENT_HEADER WHERE INDENT_NO='" + IndentNo + "' AND APPROVED=1 AND CANCELED=0")) {
                                JOptionPane.showMessageDialog(this, "Indent no(" + IndentNo + ") linked with inquiry no(" + InquiryNo + ") is not final approved.");
                                return false;
                            }
                            SQL = "SELECT * FROM D_INV_INDENT_DETAIL WHERE ITEM_CODE='" + ItemID + "' AND INDENT_NO='" + IndentNo + "' "
                                    + "AND SR_NO='" + IndentSrNo + "'";
                            if (!data.IsRecordExist(SQL)) {
                                JOptionPane.showMessageDialog(this, "Item code(" + ItemID + ") in inquiry no(" + InquiryNo + ") linked with Indent no(" + IndentNo + ") and indent "
                                        + "serial no(" + IndentSrNo + ") does not match.");
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void Cancel() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        DisplayData();
        EditMode = 0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        ShowMessage("Ready ........");
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void Find() {
        Loader ObjLoader = new Loader(this, "EITLERP.Purchase.frmQuoteFind", true);
        frmQuoteFind ObjReturn = (frmQuoteFind) ObjLoader.getObj();

        if (ObjReturn.Cancelled == false) {
            if (!ObjQuotation.Filter(ObjReturn.strQuery)) {
                JOptionPane.showMessageDialog(null, "No records found.");
            }
            MoveLast();
        }
    }

    private void MoveFirst() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjQuotation.MoveFirst();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void MovePrevious() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjQuotation.MovePrevious();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void MoveNext() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjQuotation.MoveNext();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    private void MoveLast() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        ObjQuotation.MoveLast();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

    public void FindEx(int pCompanyID, String pDocNo) {
        ObjQuotation.Filter(" WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND QUOT_ID='" + pDocNo + "'");
        ObjQuotation.MoveLast();
        DisplayData();
    }

    private void txtINQNOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtINQNOKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtINQNOKeyPressed

    private void cmdNext6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext6ActionPerformed

    private void cmdBack3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBack3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdBack3ActionPerformed

    private void txtSGSTTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSGSTTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSGSTTermKeyPressed

    private void txtGSTCompCessTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTCompCessTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGSTCompCessTermKeyPressed

    private void txtIGSTTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIGSTTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIGSTTermKeyPressed

    private void txtCompositionTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCompositionTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCompositionTermKeyPressed

    private void txtRCMTermKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRCMTermKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRCMTermKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane HeaderPane;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableH;
    private javax.swing.JTable TableHS;
    private javax.swing.JTable TableL;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdAmend;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdBack2;
    private javax.swing.JButton cmdBack3;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdCopy;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFromRemarksBig;
    private javax.swing.JButton cmdInsert;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNext4;
    private javax.swing.JButton cmdNext5;
    private javax.swing.JButton cmdNext6;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewA;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemarksBig;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowInquiry;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtCGSTTerm;
    private javax.swing.JTextField txtCIETerm;
    private javax.swing.JTextField txtCenvatTerm;
    private javax.swing.JTextField txtCompositionTerm;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextArea txtDespatchTerm;
    private javax.swing.JTextField txtDiscountTerm;
    private javax.swing.JTextField txtExciseTerm;
    private javax.swing.JTextField txtFOBTerm;
    private javax.swing.JTextField txtFreightTerm;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGSTCompCessTerm;
    private javax.swing.JTextField txtGrossAmount;
    private javax.swing.JTextField txtIGSTTerm;
    private javax.swing.JTextField txtINQNO;
    private javax.swing.JTextField txtInsuranceTerm;
    private javax.swing.JTextField txtNetAmount;
    private javax.swing.JTextField txtOctroiTerm;
    private javax.swing.JTextField txtOthersTerm;
    private javax.swing.JTextField txtPFTerm;
    private javax.swing.JTextField txtPaymentTerm;
    private javax.swing.JTextField txtPriceBasisTerm;
    private javax.swing.JTextField txtQTID;
    private javax.swing.JTextField txtQTNO;
    private javax.swing.JTextField txtRCMTerm;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtSGSTTerm;
    private javax.swing.JTextField txtSTTerm;
    private javax.swing.JTextField txtServiceTaxTerm;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    private javax.swing.JTextField txtTCCTerm;
    private javax.swing.JTextField txtToRemarks;
    // End of variables declaration//GEN-END:variables

    private void EnableToolbar() {
        //Puts toolbar in enable mode
        cmdTop.setEnabled(true);
        cmdBack.setEnabled(true);
        cmdNext.setEnabled(true);
        cmdLast.setEnabled(true);
        cmdNew.setEnabled(true);
        cmdEdit.setEnabled(true);
        cmdDelete.setEnabled(true);
        cmdSave.setEnabled(false);
        cmdCancel.setEnabled(false);
        cmdFilter.setEnabled(true);
        cmdPreview.setEnabled(true);
        cmdPrint.setEnabled(true);
        cmdExit.setEnabled(true);
    }

    private void DisableToolbar() {
        //Puts toolbar in enable mode
        cmdTop.setEnabled(false);
        cmdBack.setEnabled(false);
        cmdNext.setEnabled(false);
        cmdLast.setEnabled(false);
        cmdNew.setEnabled(false);
        cmdEdit.setEnabled(false);
        cmdDelete.setEnabled(false);
        cmdSave.setEnabled(true);
        cmdCancel.setEnabled(true);
        cmdFilter.setEnabled(false);
        cmdPreview.setEnabled(false);
        cmdPrint.setEnabled(false);
        cmdExit.setEnabled(false);
    }

    //Didplay data on the Screen
    private void DisplayData() {

        //=========== Color Indication ===============//
        try {
            if (EditMode == 0) {
                if (ObjQuotation.getAttribute("APPROVED").getInt() == 1) {
                    lblTitle.setBackground(Color.BLUE);
                }

                if (ObjQuotation.getAttribute("APPROVED").getInt() != 1) {
                    lblTitle.setBackground(Color.GRAY);
                }

                if (ObjQuotation.getAttribute("CANCELLED").getInt() == 1) {
                    lblTitle.setBackground(Color.RED);
                }

            }
        } catch (Exception c) {

        }
        //============================================//

        //========= Authority Delegation Check =====================//
        if (EITLERPGLOBAL.gAuthorityUserID != EITLERPGLOBAL.gUserID) {
            int ModuleID = 19;

            if (clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, EITLERPGLOBAL.gAuthorityUserID, ModuleID)) {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gAuthorityUserID;
            } else {
                EITLERPGLOBAL.gNewUserID = EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//

        try {
            ClearFields();

            txtQTID.setText((String) ObjQuotation.getAttribute("QUOT_ID").getObj());
            lblTitle.setText("QUOTATION - " + txtQTID.getText());
            lblRevNo.setText(Integer.toString((int) ObjQuotation.getAttribute("REVISION_NO").getVal()));
            txtDate.setText(EITLERPGLOBAL.formatDate((String) ObjQuotation.getAttribute("QUOT_DATE").getObj()));
            txtQTNO.setText((String) ObjQuotation.getAttribute("QUOT_NO").getObj());
            txtSuppCode.setText((String) ObjQuotation.getAttribute("SUPP_ID").getObj());
            txtSuppName.setText(clsParty.getPartyNameByPartyID(EITLERPGLOBAL.gCompanyID, Integer.parseInt(txtSuppCode.getText())));

            txtINQNO.setText((String) ObjQuotation.getAttribute("INQUIRY_NO").getObj());
            txtRemarks.setText((String) ObjQuotation.getAttribute("REMARKS").getObj());

            EITLERPGLOBAL.setComboIndex(cmbHierarchy, (int) ObjQuotation.getAttribute("HIERARCHY_ID").getVal());

            txtPaymentTerm.setText((String) ObjQuotation.getAttribute("PAYMENT_TERM").getObj());
            txtPriceBasisTerm.setText((String) ObjQuotation.getAttribute("PRICE_BASIS_TERM").getObj());
            txtDiscountTerm.setText((String) ObjQuotation.getAttribute("DISCOUNT_TERM").getObj());
            txtExciseTerm.setText((String) ObjQuotation.getAttribute("EXCISE_TERM").getObj());
            txtSTTerm.setText((String) ObjQuotation.getAttribute("ST_TERM").getObj());
            txtPFTerm.setText((String) ObjQuotation.getAttribute("PF_TERM").getObj());
            txtFreightTerm.setText((String) ObjQuotation.getAttribute("FREIGHT_TERM").getObj());
            txtOctroiTerm.setText((String) ObjQuotation.getAttribute("OCTROI_TERM").getObj());
            txtFOBTerm.setText((String) ObjQuotation.getAttribute("FOB_TERM").getObj());
            txtCIETerm.setText((String) ObjQuotation.getAttribute("CIE_TERM").getObj());
            txtInsuranceTerm.setText((String) ObjQuotation.getAttribute("INSURANCE_TERM").getObj());
            txtTCCTerm.setText((String) ObjQuotation.getAttribute("TCC_TERM").getObj());
            txtCenvatTerm.setText((String) ObjQuotation.getAttribute("CENVAT_TERM").getObj());
            txtDespatchTerm.setText((String) ObjQuotation.getAttribute("DESPATCH_TERM").getObj());
            txtServiceTaxTerm.setText((String) ObjQuotation.getAttribute("SERVICE_TAX_TERM").getObj());
            txtOthersTerm.setText((String) ObjQuotation.getAttribute("OTHERS_TERM").getObj());

            txtCGSTTerm.setText((String) ObjQuotation.getAttribute("CGST_TERM").getObj());
            txtSGSTTerm.setText((String) ObjQuotation.getAttribute("SGST_TERM").getObj());
            txtIGSTTerm.setText((String) ObjQuotation.getAttribute("IGST_TERM").getObj());
            txtCompositionTerm.setText((String) ObjQuotation.getAttribute("COMPOSITION_TERM").getObj());
            txtRCMTerm.setText((String) ObjQuotation.getAttribute("RCM_TERM").getObj());
            txtGSTCompCessTerm.setText((String) ObjQuotation.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());

            //============= Display Custom Columns ========================
//            for(int i=1;i<=10;i++) {
            for (int i = 1; i <= 16; i++) {
                int ColID = (int) ObjQuotation.getAttribute("COLUMN_" + Integer.toString(i) + "_ID").getVal();
                int Col = DataModelH.getColFromID(ColID);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);

                if (ColID != 0) {
                    //Set the Formula
                    if (ObjQuotation.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj() != null) {
                        DataModelH.SetFormula(Col, (String) ObjQuotation.getAttribute("COLUMN_" + Integer.toString(i) + "_FORMULA").getObj());
                    } else {
                        DataModelH.SetFormula(Col, "");
                    }

                    //Set the Percentage. If there
                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                        DataModelH.setValueByVariableEx("P_" + ColID, Double.toString(ObjQuotation.getAttribute("COLUMN_" + Integer.toString(i) + "_PER").getVal()), 1);
                    }

                    //Set the Value
                    DataModelH.setValueByVariableEx(Variable, Double.toString(ObjQuotation.getAttribute("COLUMN_" + Integer.toString(i) + "_AMT").getVal()), 1);
                }

            }
            //=================================================================//
            //========= Display Line Items =============//
            FormatGrid();

            DoNotEvaluate = true;

            for (int i = 1; i <= ObjQuotation.colQuotationItems.size(); i++) {
                //Insert New Row
                Object[] rowData = new Object[1];
                DataModelL.addRow(rowData);
                int NewRow = TableL.getRowCount() - 1;

                clsQuotationItem ObjItem = (clsQuotationItem) ObjQuotation.colQuotationItems.get(Integer.toString(i));

                DataModelL.setValueByVariable("SR_NO", Integer.toString(i), NewRow);
                DataModelL.setValueByVariable("INQUIRY_NO", (String) ObjItem.getAttribute("INQUIRY_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("INQUIRY_SRNO", Integer.toString((int) ObjItem.getAttribute("INQUIRY_SRNO").getVal()), NewRow);
                DataModelL.setValueByVariable("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj(), NewRow);
                DataModelL.setValueByVariable("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("MAKE", (String) ObjItem.getAttribute("MAKE").getObj(), NewRow);
                DataModelL.setValueByVariable("PRICE_LIST_NO", (String) ObjItem.getAttribute("PRICE_LIST_NO").getObj(), NewRow);
                DataModelL.setValueByVariable("DELIVERY_DATE", EITLERPGLOBAL.formatDate((String) ObjItem.getAttribute("DELIVERY_DATE").getObj()), NewRow);
                DataModelL.setValueByVariable("UNIT", Integer.toString((int) ObjItem.getAttribute("UNIT").getVal()), NewRow);
                String UnitName = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("UNIT_NAME", UnitName, NewRow);
                DataModelL.setValueByVariable("QTY", Double.toString(ObjItem.getAttribute("QTY").getVal()), NewRow);
                DataModelL.setValueByVariable("RATE", Double.toString(ObjItem.getAttribute("RATE").getVal()), NewRow);
                DataModelL.setValueByVariable("GROSS_AMOUNT", Double.toString(ObjItem.getAttribute("TOTAL_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("ACCESS_AMOUNT", Double.toString(ObjItem.getAttribute("ACCESS_AMOUNT").getVal()), NewRow);
                DataModelL.setValueByVariable("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj(), NewRow);
                String ItemName = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_NAME", ItemName, NewRow);
                String HsnSacCode = clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("HSN_SAC_CODE", HsnSacCode, NewRow);

                DataModelL.setValueByVariable("SUPP_ITEM_DESC", (String) ObjItem.getAttribute("SUPP_ITEM_DESC").getObj(), NewRow);
                DataModelL.setValueByVariable("EXCISE_GATEPASS_GIVEN", Boolean.valueOf(ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool()), NewRow);
                //============= Display Custom Columns ========================
//                for(int c=1;c<=10;c++) {
                for (int c = 1; c <= 16; c++) {
                    int ColID = (int) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_ID").getVal();
                    int Col = DataModelL.getColFromID(ColID);
                    int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                    String Variable = ObjColumn.getVariableName(EITLERPGLOBAL.gCompanyID, ColID);

                    if (ColID != 0) {
                        //Set the Formula
                        if (ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj() != null) {
                            DataModelL.SetFormula(Col, (String) ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_FORMULA").getObj());
                        } else {
                            DataModelL.SetFormula(Col, "");
                        }

                        //Set the Percentage. If there
                        if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                            DataModelL.setValueByVariable("P_" + ColID, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_PER").getVal()), NewRow);
                        }

                        //Set the Value
                        DataModelL.setValueByVariable(Variable, Double.toString(ObjItem.getAttribute("COLUMN_" + Integer.toString(c) + "_AMT").getVal()), NewRow);
                    }
                }
                //=================================================================//
            }

            //============= P.O. Terms ==============//
            //========= Display Line Items =============//
            /*FormatTermsGrid();
             for(int i=1;i<=ObjQuotation.colQuotTerms.size();i++) {
             //Insert New Row
             Object[] rowData=new Object[3];
             clsQuotTerms ObjItem=(clsQuotTerms)ObjQuotation.colQuotTerms.get(Integer.toString(i));
             String TermType=(String)ObjItem.getAttribute("TERM_TYPE").getObj();
             
             if(TermType.equals("P")) {
             
             rowData[0]=Integer.toString(TableP.getRowCount()+1);
             rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
             rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
             DataModelP.addRow(rowData);
             }
             
             if(TermType.equals("D")) {
             
             rowData[0]=Integer.toString(TableD.getRowCount()+1);
             rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
             rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
             DataModelD.addRow(rowData);
             }
             
             if(TermType.equals("O")) {
             
             rowData[0]=Integer.toString(TableO.getRowCount()+1);
             rowData[1]=Integer.toString((int)ObjItem.getAttribute("TERM_CODE").getVal());
             rowData[2]=(String)ObjItem.getAttribute("TERM_DESC").getObj();
             DataModelO.addRow(rowData);
             }
             }*/
            //=======================================//
            DoNotEvaluate = false;

            UpdateResults_H(0);
            UpdateAmounts();
            UpdateSrNo();

            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List = new HashMap();
            String DocNo = (String) ObjQuotation.getAttribute("QUOT_ID").getObj();
            List = ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, 19, DocNo);
            for (int i = 1; i <= List.size(); i++) {
                clsDocFlow ObjFlow = (clsDocFlow) List.get(Integer.toString(i));
                Object[] rowData = new Object[7];

                rowData[0] = Integer.toString(i);
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2] = (String) ObjFlow.getAttribute("STATUS").getObj();
                rowData[3] = clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int) ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5] = EITLERPGLOBAL.formatDate((String) ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6] = (String) ObjFlow.getAttribute("REMARKS").getObj();

                DataModelA.addRow(rowData);
            }
            //============================================================//

            if (EditMode == 0) {
                DataModelL.TableReadOnly(true);
                DataModelH.TableReadOnly(true);
            }

            //Showing Audit Trial History
            FormatGridHS();
            HashMap History = clsQuotation.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for (int i = 1; i <= History.size(); i++) {
                clsQuotation ObjHistory = (clsQuotation) History.get(Integer.toString(i));
                Object[] rowData = new Object[5];

                rowData[0] = Integer.toString((int) ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1] = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, (long) ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2] = EITLERPGLOBAL.formatDate((String) ObjHistory.getAttribute("ENTRY_DATE").getObj());

                String ApprovalStatus = "";

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("A")) {
                    ApprovalStatus = "Approved";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("F")) {
                    ApprovalStatus = "Final Approved";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("W")) {
                    ApprovalStatus = "Waiting";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("R")) {
                    ApprovalStatus = "Rejected";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("P")) {
                    ApprovalStatus = "Pending";
                }

                if (((String) ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("C")) {
                    ApprovalStatus = "Skiped";
                }

                rowData[3] = ApprovalStatus;
                rowData[4] = (String) ObjHistory.getAttribute("APPROVER_REMARKS").getObj();

                DataModelHS.addRow(rowData);
            }
            //=========================================//

        } catch (Exception e) {

        }

    }

    private void UpdateSrNo() {
        int SrCol = DataModelL.getColFromVariable("SR_NO");

        for (int i = 0; i < TableL.getRowCount(); i++) {
            TableL.setValueAt(Integer.toString(i + 1), i, SrCol);
        }
    }

    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjQuotation.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
        ObjQuotation.setAttribute("FFNO", FFNo);
        ObjQuotation.setAttribute("QUOT_NO", txtQTNO.getText().trim());
        ObjQuotation.setAttribute("QUOT_DATE", EITLERPGLOBAL.formatDateDB(txtDate.getText()));
        ObjQuotation.setAttribute("INQUIRY_NO", txtINQNO.getText().trim());
        ObjQuotation.setAttribute("SUPP_ID", txtSuppCode.getText().trim());
        ObjQuotation.setAttribute("REMARKS", txtRemarks.getText().trim());

        //----- Update Approval Specific Fields -----------//
        ObjQuotation.setAttribute("HIERARCHY_ID", EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjQuotation.setAttribute("FROM", EITLERPGLOBAL.gNewUserID);
        ObjQuotation.setAttribute("TO", EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjQuotation.setAttribute("FROM_REMARKS", txtToRemarks.getText());

        if (OpgApprove.isSelected()) {
            ObjQuotation.setAttribute("APPROVAL_STATUS", "A");
        }

        if (OpgFinal.isSelected()) {
            ObjQuotation.setAttribute("APPROVAL_STATUS", "F");
        }

        if (OpgReject.isSelected()) {
            ObjQuotation.setAttribute("APPROVAL_STATUS", "R");
        }

        if (OpgHold.isSelected()) {
            ObjQuotation.setAttribute("APPROVAL_STATUS", "H");
        }
        //-------------------------------------------------//

        if (EditMode == EITLERPGLOBAL.ADD) {
            ObjQuotation.setAttribute("CREATED_BY", EITLERPGLOBAL.gNewUserID);
            ObjQuotation.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        } else {
            ObjQuotation.setAttribute("MODIFIED_BY", EITLERPGLOBAL.gNewUserID);
            ObjQuotation.setAttribute("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
        }

        //============= Set Custom Columns ========================
        int ColCounter = 0;

        for (int i = 0; i < TableH.getRowCount(); i++) {
            double lnPercentValue = 0;
            int ColID = DataModelH.getColID(i);
            int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
            String Variable = DataModelH.getVariable(i);

            if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                ColCounter++;
                ObjQuotation.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_ID", ColID);
                ObjQuotation.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_FORMULA", DataModelH.getFormula(i));

                if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                    lnPercentValue = Double.parseDouble(DataModelH.getValueByVariableEx("P_" + ColID, 1));
                    ObjQuotation.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_PER", EITLERPGLOBAL.round(lnPercentValue, 3));
                }
                ObjQuotation.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_AMT", EITLERPGLOBAL.round(Double.parseDouble(DataModelH.getValueByVariableEx(Variable, 1)), 5));
                ObjQuotation.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_CAPTION", (String) TableH.getValueAt(i, 0));
            }
        }
        //=================================================================

        //=================== Setting up Line Items ==================//
        ObjQuotation.colQuotationItems.clear();

        for (int i = 0; i < TableL.getRowCount(); i++) {
            clsQuotationItem ObjItem = new clsQuotationItem();

            ObjItem.setAttribute("SR_NO", Integer.parseInt(DataModelL.getValueByVariable("SR_NO", i)));
            ObjItem.setAttribute("INQUIRY_NO", DataModelL.getValueByVariable("INQUIRY_NO", i));
            ObjItem.setAttribute("INQUIRY_SRNO", Integer.parseInt(DataModelL.getValueByVariable("INQUIRY_SRNO", i)));
            ObjItem.setAttribute("ITEM_ID", DataModelL.getValueByVariable("ITEM_ID", i));
            ObjItem.setAttribute("ITEM_EXTRA_DESC", DataModelL.getValueByVariable("ITEM_EXTRA_DESC", i));
            ObjItem.setAttribute("MAKE", DataModelL.getValueByVariable("MAKE", i));
            ObjItem.setAttribute("PRICE_LIST_NO", DataModelL.getValueByVariable("PRICE_LIST_NO", i));
            ObjItem.setAttribute("DELIVERY_DATE", EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DELIVERY_DATE", i)));
            ObjItem.setAttribute("UNIT", Integer.parseInt(DataModelL.getValueByVariable("UNIT", i)));
            ObjItem.setAttribute("QTY", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("QTY", i)), 3));
            ObjItem.setAttribute("RATE", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("RATE", i)), 5));
            ObjItem.setAttribute("TOTAL_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("GROSS_AMOUNT", i)), 5));
            ObjItem.setAttribute("ACCESS_AMOUNT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable("ACCESS_AMOUNT", i)), 5));
            ObjItem.setAttribute("REMARKS", DataModelL.getValueByVariable("REMARKS", i));
            ObjItem.setAttribute("SUPP_ITEM_DESC", DataModelL.getValueByVariable("SUPP_ITEM_DESC", i));
            ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN", DataModelL.getBoolValueByVariable("EXCISE_GATEPASS_GIVEN", i));
            //============= Set Custom Columns ========================//
            ColCounter = 0;

            for (int c = 0; c < TableL.getColumnCount() - 1; c++) {
                double lnPercentValue = 0;
                int ColID = DataModelL.getColID(c);
                int TaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                String Variable = DataModelL.getVariable(c);

                if ((ColID != 0) && (ColID != -99) && (!Variable.substring(0, 2).equals("P_"))) {
                    ColCounter++;
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_ID", ColID);
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_FORMULA", DataModelL.getFormula(c));

                    if (ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                        lnPercentValue = Double.parseDouble(DataModelL.getValueByVariable("P_" + ColID, i));
                        ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_PER", EITLERPGLOBAL.round(lnPercentValue, 3));
                    }
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_AMT", EITLERPGLOBAL.round(Double.parseDouble(DataModelL.getValueByVariable(Variable, i)), 5));
                    ObjItem.setAttribute("COLUMN_" + Integer.toString(ColCounter) + "_CAPTION", TableL.getColumnName(c));
                }
            }
            //===========================================================//
            ObjQuotation.colQuotationItems.put(Integer.toString(ObjQuotation.colQuotationItems.size() + 1), ObjItem);
        }
        //======================Completed ===========================//

        int TermCounter = 0;

        ObjQuotation.colQuotTerms.clear();

        /*for(int i=0;i<TableP.getRowCount();i++) {
         TermCounter++;
         
         clsQuotTerms ObjItem=new clsQuotTerms();
         
         ObjItem.setAttribute("TERM_TYPE","P");
         if(EITLERPGLOBAL.IsNumber((String)TableP.getValueAt(i, 1))) {
         ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableP.getValueAt(i, 1)));
         }
         else {
         ObjItem.setAttribute("TERM_CODE",0);
         }
         ObjItem.setAttribute("TERM_DESC",(String)TableP.getValueAt(i,2));
         
         ObjQuotation.colQuotTerms.put(Integer.toString(TermCounter), ObjItem);
         }
         
         for(int i=0;i<TableD.getRowCount();i++) {
         TermCounter++;
         
         clsQuotTerms ObjItem=new clsQuotTerms();
         
         ObjItem.setAttribute("TERM_TYPE","D");
         if(EITLERPGLOBAL.IsNumber((String)TableD.getValueAt(i, 1))) {
         ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableD.getValueAt(i, 1)));
         }
         else {
         ObjItem.setAttribute("TERM_CODE",0);
         }
         ObjItem.setAttribute("TERM_DESC",(String)TableD.getValueAt(i,2));
         
         ObjQuotation.colQuotTerms.put(Integer.toString(TermCounter), ObjItem);
         }
         
         for(int i=0;i<TableO.getRowCount();i++) {
         TermCounter++;
         
         clsQuotTerms ObjItem=new clsQuotTerms();
         
         ObjItem.setAttribute("TERM_TYPE","O");
         if(EITLERPGLOBAL.IsNumber((String)TableO.getValueAt(i, 1))) {
         ObjItem.setAttribute("TERM_CODE",Integer.parseInt((String)TableO.getValueAt(i, 1)));
         }
         else {
         ObjItem.setAttribute("TERM_CODE",0);
         }
         ObjItem.setAttribute("TERM_DESC",(String)TableO.getValueAt(i,2));
         
         ObjQuotation.colQuotTerms.put(Integer.toString(TermCounter), ObjItem);
         }*/
        ObjQuotation.setAttribute("PAYMENT_TERM", txtPaymentTerm.getText());
        ObjQuotation.setAttribute("PRICE_BASIS_TERM", txtPriceBasisTerm.getText());
        ObjQuotation.setAttribute("DISCOUNT_TERM", txtDiscountTerm.getText());
        ObjQuotation.setAttribute("EXCISE_TERM", txtExciseTerm.getText());
        ObjQuotation.setAttribute("ST_TERM", txtSTTerm.getText());
        ObjQuotation.setAttribute("PF_TERM", txtPFTerm.getText());
        ObjQuotation.setAttribute("FREIGHT_TERM", txtFreightTerm.getText());
        ObjQuotation.setAttribute("OCTROI_TERM", txtOctroiTerm.getText());
        ObjQuotation.setAttribute("FOB_TERM", txtFOBTerm.getText());
        ObjQuotation.setAttribute("CIE_TERM", txtCIETerm.getText());
        ObjQuotation.setAttribute("INSURANCE_TERM", txtInsuranceTerm.getText());
        ObjQuotation.setAttribute("TCC_TERM", txtTCCTerm.getText());
        ObjQuotation.setAttribute("CENVAT_TERM", txtCenvatTerm.getText());
        ObjQuotation.setAttribute("DESPATCH_TERM", txtDespatchTerm.getText());
        ObjQuotation.setAttribute("SERVICE_TAX_TERM", txtServiceTaxTerm.getText());
        ObjQuotation.setAttribute("OTHERS_TERM", txtOthersTerm.getText());

        ObjQuotation.setAttribute("CGST_TERM", txtCGSTTerm.getText());
        ObjQuotation.setAttribute("SGST_TERM", txtSGSTTerm.getText());
        ObjQuotation.setAttribute("IGST_TERM", txtIGSTTerm.getText());
        ObjQuotation.setAttribute("COMPOSITION_TERM", txtCompositionTerm.getText());
        ObjQuotation.setAttribute("RCM_TERM", txtRCMTerm.getText());
        ObjQuotation.setAttribute("GST_COMPENSATION_CESS_TERM", txtGSTCompCessTerm.getText());

    }

    private void ClearFields() {
        txtDate.setText("");
        txtQTNO.setText("");
        txtSuppCode.setText("");
        txtSuppName.setText("");
        txtINQNO.setText("");
        txtRemarks.setText("");
        txtToRemarks.setText("");

        txtOthersTerm.setText("");
        txtServiceTaxTerm.setText("");
        txtPaymentTerm.setText("");
        txtPriceBasisTerm.setText("");
        txtDiscountTerm.setText("");
        txtExciseTerm.setText("");
        txtSTTerm.setText("");
        txtPFTerm.setText("");
        txtFreightTerm.setText("");
        txtOctroiTerm.setText("");
        txtFOBTerm.setText("");
        txtCIETerm.setText("");
        txtInsuranceTerm.setText("");
        txtTCCTerm.setText("");
        txtCenvatTerm.setText("");
        txtDespatchTerm.setText("");

        txtCGSTTerm.setText("");
        txtSGSTTerm.setText("");
        txtIGSTTerm.setText("");
        txtCompositionTerm.setText("");
        txtRCMTerm.setText("");
        txtGSTCompCessTerm.setText("");

        FormatGrid();
        FormatGrid_H();
        FormatGridA();
        FormatGridHS();

        txtGrossAmount.setText("0.00");
        txtNetAmount.setText("0.00");

    }

    private void SetFields(boolean pStat) {
        txtDate.setEnabled(pStat);
        txtQTNO.setEnabled(pStat);
        txtSuppCode.setEnabled(pStat);
        txtINQNO.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);

        txtOthersTerm.setEnabled(pStat);
        txtPaymentTerm.setEnabled(pStat);
        txtServiceTaxTerm.setEnabled(pStat);
        txtPriceBasisTerm.setEnabled(pStat);
        txtDiscountTerm.setEnabled(pStat);
        txtExciseTerm.setEnabled(pStat);
        txtSTTerm.setEnabled(pStat);
        txtPFTerm.setEnabled(pStat);
        txtFreightTerm.setEnabled(pStat);
        txtOctroiTerm.setEnabled(pStat);
        txtFOBTerm.setEnabled(pStat);
        txtCIETerm.setEnabled(pStat);
        txtInsuranceTerm.setEnabled(pStat);
        txtTCCTerm.setEnabled(pStat);
        txtCenvatTerm.setEnabled(pStat);
        txtDespatchTerm.setEnabled(pStat);

        txtCGSTTerm.setEnabled(pStat);
        txtSGSTTerm.setEnabled(pStat);
        txtIGSTTerm.setEnabled(pStat);
        txtCompositionTerm.setEnabled(pStat);
        txtRCMTerm.setEnabled(pStat);
        txtGSTCompCessTerm.setEnabled(pStat);

        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);

        cmdInsert.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);

        SetupApproval();

        DataModelH.TableReadOnly(!pStat);
        DataModelL.TableReadOnly(!pStat);

        //cmdPaymentAdd.setEnabled(pStat);
        //cmdPaymentRemove.setEnabled(pStat);
        //cmdDeliveryAdd.setEnabled(pStat);
        //cmdDeliveryRemove.setEnabled(pStat);
        //cmdOtherAdd.setEnabled(pStat);
        //cmdOtherRemove.setEnabled(pStat);
        //DataModelP.TableReadOnly(!pStat);
        //DataModelD.TableReadOnly(!pStat);
        //DataModelO.TableReadOnly(!pStat);
    }

    private void SetupApproval() {
        // --- Hierarchy Change Rights Check --------
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,75)) {
         cmbHierarchy.setEnabled(true);
         }
         else {
         cmbHierarchy.setEnabled(false);
         
         if(cmbHierarchy.getItemCount()>1) {
         cmbHierarchy.setEnabled(true);
         }
         }*/
        if (EditMode == EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
        } else {
            cmbHierarchy.setEnabled(false);
        }

        //Set Default Hierarchy ID for User
        int DefaultID = clsHierarchy.getDefaultHierarchy((int) EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy, DefaultID);

        if (EditMode == EITLERPGLOBAL.ADD) {
            lnFromID = (int) EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        } else {

            int FromUserID = ApprovalFlow.getFromID((int) EITLERPGLOBAL.gCompanyID, 19, (String) ObjQuotation.getAttribute("QUOT_ID").getObj());
            lnFromID = FromUserID;
            String strFromUser = clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks = ApprovalFlow.getFromRemarks((int) EITLERPGLOBAL.gCompanyID, 19, FromUserID, (String) ObjQuotation.getAttribute("QUOT_ID").getObj());

            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }

        SelHierarchyID = EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();

        if (clsHierarchy.CanSkip((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        } else {
            cmbSendTo.setEnabled(false);
        }

        if (clsHierarchy.CanFinalApprove((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int) EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        } else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }

        //In Edit Mode Hierarchy Should be disabled
        if (EditMode == EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
        }

        if (EditMode == 0) {
            //Disable all hierarchy controls if not in Add/Edit Mode
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            txtFromRemarks.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }
    }

    private void GenerateFromCombo() {
        //Generates Combo Boxes
        HashMap List = new HashMap();

        try {
            if (EditMode == EITLERPGLOBAL.ADD) {
                //----- Generate cmbType ------- //
                cmbToModel = new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);

                List = clsHierarchy.getUserList((int) EITLERPGLOBAL.gCompanyID, SelHierarchyID, EITLERPGLOBAL.gNewUserID);
                for (int i = 1; i <= List.size(); i++) {
                    clsUser ObjUser = (clsUser) List.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();

                    if (ObjUser.getAttribute("USER_ID").getVal() == EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    } else {
                        cmbToModel.addElement(aData);
                    }
                }
                //------------------------------ //
            } else {
                //----- Generate cmbType ------- //
                cmbToModel = new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);

                List = ApprovalFlow.getRemainingUsers((int) EITLERPGLOBAL.gCompanyID, 19, (String) ObjQuotation.getAttribute("QUOT_ID").getObj());
                for (int i = 1; i <= List.size(); i++) {
                    clsUser ObjUser = (clsUser) List.get(Integer.toString(i));
                    ComboData aData = new ComboData();
                    aData.Code = (int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text = (String) ObjUser.getAttribute("USER_NAME").getObj();
                    cmbToModel.addElement(aData);
                }
                //------------------------------ //
            }
        } catch (Exception e) {
        }

    }

    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    private void UpdateResults(int pCol) {
        if (!DoNotEvaluate) {
            try {
                int ColID = 0, TaxID = 0, UpdateCol = 0;
                String strFormula = "", strItemID = "", strVariable = "", srcVariable = "", srcVar2 = "";
                double lnPercentValue = 0, lnFinalResult = 0, lnNetAmount = 0;
                Object result;
                boolean updateIt = true;
                int QtyCol = 0, RateCol = 0, GAmountCol = 0;

                Updating = true; //Stops Recursion

                srcVariable = DataModelL.getVariable(pCol); //Variable name of currently updated Column

                //If this column is percentage column. Variable name would be P_XXX
                //We shoule use actual variable name, it will be found on it's associated next column
                if (srcVariable.substring(0, 2).equals("P_")) {
                    srcVariable = DataModelL.getVariable(pCol + 1);
                }

                QtyCol = DataModelL.getColFromVariable("QTY"); //Index of Qty Column
                RateCol = DataModelL.getColFromVariable("RATE"); //Index of Rate Column
                GAmountCol = DataModelL.getColFromVariable("GROSS_AMOUNT"); //Index of Gross Amount Column

                //======= Read the Item ID - To be used when accessing item specific formula ===//
                String cellValue = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                if (cellValue == null) {
                    strItemID = "";
                } else {
                    strItemID = (String) DataModelL.getValueAt(TableL.getSelectedRow(), DataModelL.getColFromVariable("ITEM_ID"));
                }
                //================================================================================

                GatherVariableValues();

                //====== Update Gross Amount =======
                myParser.parseExpression("QTY*RATE");
                result = myParser.getValueAsObject();
                if (result != null) {
                    String RoundNum = Double.toString(EITLERPGLOBAL.round(Double.parseDouble(result.toString()), 5));
                    DataModelL.setValueByVariable("GROSS_AMOUNT", RoundNum, TableL.getSelectedRow());
                }
                //=================================

                for (int i = 0; i < TableL.getColumnCount(); i++) {
                    strVariable = DataModelL.getVariable(i);

                    ColID = DataModelL.getColID(i);

                    TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);

                    //Exclude Percentage Columns and System Columns
                    if ((!strVariable.substring(0, 2).equals("P_")) && (ColID != 0) && (ColID != -99)) {
                        //If percentage is used
                        if (ObjTax.getUsePercentage((int) EITLERPGLOBAL.gCompanyID, TaxID)) {

                            //Load the Formula for calculation
                            if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                                strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                            } else {
                                strFormula = DataModelL.getFormula(i);
                            }

                            //Now Read Associated Percentage Column
                            lnPercentValue = Double.parseDouble(DataModelL.getValueByVariable("P_" + Integer.toString(ColID), TableL.getSelectedRow()));

                            //Now Parse Main expression
                            myParser.parseExpression(strFormula);
                            result = myParser.getValueAsObject();
                            if (result != null) {
                                //Now get the percentage of the main result
                                lnFinalResult = (Double.parseDouble(result.toString()) * lnPercentValue) / 100;
                                //Update the Column
                                srcVar2 = DataModelL.getVariable(pCol + 1);

                                UpdateCol = DataModelL.getColFromVariable(strVariable);

                                updateIt = false;

                                if (UpdateCol != pCol) {
                                    if (UpdateCol == pCol + 1) {
                                        updateIt = true;
                                    } else {
                                        if ((strFormula.indexOf(srcVariable) != -1)) { //If this column is dependent on updated column
                                            updateIt = true; //Then update it
                                        } else {

                                            //Check whether the formula is dependent on any system Columns
                                            boolean Dependent = false;
                                            int dCol = 0;

                                            for (int d = 0; d <= TableL.getColumnCount() - 1; d++) {
                                                if (DataModelL.getColID(d) == 0) //It's System Column
                                                {
                                                    String dVariable = DataModelL.getVariable(d);
                                                    if (strFormula.indexOf(dVariable) != -1) {
                                                        if (pCol == d) {
                                                            Dependent = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }

                                    //============ New Change In Parser =============//
                                    //Now Condition. First check whether percentage has been entered
                                    if (lnPercentValue > 0) {
                                        //Yes Percentage Entered. Then we must update the associated column
                                        updateIt = true;
                                    } else {
                                        //If not Percentage entered than check whether any value is there
                                        //Otherwise go with the Dependent decision
                                    }
                                    //=================================================//

                                }

                                if (updateIt) {
                                    DataModelL.setValueByVariable(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), TableL.getSelectedRow());
                                }
                                //Re Gather Fresh Variable Values
                                GatherVariableValues();
                            }
                        } else //Percentage Not Used
                        {
                            //Load the Formula for calculation
                            if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                                strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID, strItemID);
                            } else {
                                strFormula = DataModelL.getFormula(i);
                            }

                            //Now Parse Main expression
                            myParser.parseExpression(strFormula);
                            result = myParser.getValueAsObject();
                            if (result != null) {
                                //Now get the percentage of the main result
                                lnFinalResult = Double.parseDouble(result.toString());
                                //Update the Column
                                UpdateCol = DataModelL.getColFromVariable(strVariable);

                                updateIt = false;

                                if (UpdateCol != pCol) {
                                    if (strFormula.indexOf(srcVariable) != -1) {
                                        updateIt = true;
                                    } else {

                                        //Check whether the formula is dependent on any system Columns
                                        boolean Dependent = false;
                                        int dCol = 0;

                                        for (int d = 0; d <= TableL.getColumnCount() - 1; d++) {
                                            if (DataModelL.getColID(d) == 0) //It's System Column
                                            {
                                                String dVariable = DataModelL.getVariable(d);

                                                if (strFormula.indexOf(dVariable) != -1) {
                                                    if (pCol == d) {
                                                        Dependent = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        if (Dependent) {
                                            updateIt = true;
                                        }

                                    }
                                }

                                if (updateIt) {
                                    DataModelL.setValueByVariable(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), TableL.getSelectedRow());
                                }
                                //Re Gather Fresh Variable Values
                                GatherVariableValues();
                            }
                        }
                    }
                }

                //== Final Pass - Update the Net Amount ==
                lnNetAmount = 0;
                double lnColValue = 0;
                double lnGrossAmount = 0;

                lnGrossAmount = Double.parseDouble((String) DataModelL.getValueAt(TableL.getSelectedRow(), GAmountCol));

                for (int c = 0; c < TableL.getColumnCount(); c++) {

                    //To be included in Calculation or not
                    if (DataModelL.getInclude(c) == false) {
                        //Read column value
                        if (TableL.getValueAt(TableL.getSelectedRow(), c).toString().equals("")) {
                            lnColValue = 0;
                        } else {
                            lnColValue = Double.parseDouble((String) TableL.getValueAt(TableL.getSelectedRow(), c));
                        }

                        if (DataModelL.getOperation(c).equals("+")) //Add
                        {
                            lnGrossAmount = lnGrossAmount + lnColValue;
                        } else //Substract
                        {
                            lnGrossAmount = lnGrossAmount - lnColValue;
                        }
                    }
                }

                //Now update the Net Amount
                DataModelL.setValueByVariable("ACCESS_AMOUNT", Double.toString(EITLERPGLOBAL.round(lnGrossAmount, 5)), TableL.getSelectedRow());

                Updating = false;
                UpdateAmounts();
            } catch (Exception e) {
                Updating = false;
            }
        }// Do not Evaluate
    }

    private void GatherVariableValues_H() {
        String strVariable = "";
        int varCol = 0;
        double lnValue = 0, lnSum = 0;

        //Scan the table and gather values for variables
        colVariables_H.clear();

        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table

        for (int i = 0; i < TableH.getRowCount(); i++) {
            double lValue = 0;
            if (DataModelH.getVariable(i) != null) {
                if (!DataModelH.getVariable(i).equals("")) //If Variable not blank
                {
                    colVariables_H.put(DataModelH.getVariable(i), (String) TableH.getValueAt(i, 1));

                    //Add variable Value to Parser Table
                    if ((TableH.getValueAt(i, 1) != null) && (!TableH.getValueAt(i, 1).toString().equals(""))) {
                        lValue = Double.parseDouble((String) TableH.getValueAt(i, 1));
                    } else {
                        lValue = 0;
                    }
                    myParser.addVariable(DataModelH.getVariable(i), lValue);
                }
            }
        }

        //Gather Variables - sum of line columns
        for (int c = 0; c < TableL.getColumnCount(); c++) {
            strVariable = DataModelL.getVariable(c);
            strVariable = strVariable.trim();

            if ((strVariable != null) && (!strVariable.equals(""))) {
                varCol = DataModelL.getColFromVariable(strVariable);

                //Do the sum
                lnSum = 0;

                try {
                    for (int r = 0; r < TableL.getRowCount(); r++) {
                        String theVal = (String) DataModelL.getValueAt(r, varCol);

                        if (theVal == null) {
                        } else {
                            lnValue = Double.parseDouble(TableL.getValueAt(r, varCol).toString());
                            lnSum = lnSum + lnValue;
                        }
                    }
                } catch (Exception e) {
                }
                //Sum Complete. Add to Parser Table
                myParser.addVariable("SUM_" + strVariable, lnSum);
            }
        }
    }

    private void UpdateResults_H(int pCol) {
        try {
            int ColID = 0, TaxID = 0, UpdateCol = 0;
            String strFormula = "", strItemID = "", strVariable = "", srcVariable = "", srcVar2 = "";
            double lnPercentValue = 0, lnFinalResult = 0, lnNetAmount = 0;
            Object result;
            boolean updateIt = true;
            int QtyCol = 0, RateCol = 0, GAmountCol = 0;

            Updating_H = true; //Stops Recursion

            srcVariable = DataModelH.getVariable(pCol); //Variable name of currently updated Column

            //If this column is percentage column. Variable name would be P_XXX
            //We shoule use actual variable name, it will be found on it's associated next column
            if (srcVariable.substring(0, 2).equals("P_")) {
                srcVariable = DataModelH.getVariable(pCol + 1);
            }

            GatherVariableValues_H();

            for (int i = 0; i < TableH.getRowCount(); i++) {
                strVariable = DataModelH.getVariable(i);

                ColID = DataModelH.getColID(i);

                TaxID = ObjColumn.getTaxID((int) EITLERPGLOBAL.gCompanyID, ColID);

                //Exclude Percentage Columns and System Columns
                if ((!strVariable.substring(0, 2).equals("P_")) && (ColID != 0)) {
                    //If percentage is used
                    if (ObjTax.getUsePercentage((int) EITLERPGLOBAL.gCompanyID, TaxID)) {

                        //Load the Formula for calculation
                        if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                            strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID);
                        } else {
                            strFormula = DataModelH.getFormula(i);
                        }

                        //Now Read Associated Percentage Column
                        lnPercentValue = Double.parseDouble(DataModelH.getValueByVariableEx("P_" + Integer.toString(ColID), 1));

                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result = myParser.getValueAsObject();
                        if (result != null) {
                            //Now get the percentage of the main result
                            lnFinalResult = (Double.parseDouble(result.toString()) * lnPercentValue) / 100;
                            //Update the Column
                            srcVar2 = DataModelH.getVariable(pCol + 1);

                            UpdateCol = DataModelH.getColFromVariable(strVariable);

                            updateIt = false;

                            if (UpdateCol != pCol) {
                                if (UpdateCol == pCol + 1) {
                                    updateIt = true;
                                } else {
                                    if ((strFormula.indexOf(srcVariable) != -1)) { //If this column is dependent on updated column
                                        updateIt = true; //Then update it
                                    } else {
                                        if ((strFormula.indexOf("QTY") != -1) || (strFormula.indexOf("RATE") != -1) || (strFormula.indexOf("GROSS_AMOUNT") != -1)) {
                                            if (pCol == QtyCol || pCol == RateCol || pCol == GAmountCol) {
                                                updateIt = true;
                                            }
                                        }
                                    }
                                }

                                //============ New Change In Parser =============//
                                //Now Condition. First check whether percentage has been entered
                                if (lnPercentValue > 0) {
                                    //Yes Percentage Entered. Then we must update the associated column
                                    updateIt = true;
                                } else {
                                    //If not Percentage entered than check whether any value is there
                                    //Otherwise go with the Dependent decision
                                }
                                //=================================================//

                            }
                            if (updateIt) {
                                DataModelH.setValueByVariableEx(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), 1);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    } else //Percentage Not Used
                    {

                        //Load the Formula for calculation
                        if ((!EITLERPGLOBAL.UseCurrentFormula) && (EditMode != EITLERPGLOBAL.ADD)) {
                            strFormula = clsTaxColumn.getFormula((int) EITLERPGLOBAL.gCompanyID, TaxID);
                        } else {
                            strFormula = DataModelH.getFormula(i);
                        }

                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result = myParser.getValueAsObject();
                        if (result != null) {
                            //Now get the percentage of the main result
                            lnFinalResult = Double.parseDouble(result.toString());
                            //Update the Column
                            UpdateCol = DataModelH.getColFromVariable(strVariable);

                            updateIt = false;

                            if (UpdateCol != pCol) {
                                if (strFormula.indexOf(srcVariable) != -1) {
                                    updateIt = true;
                                } else {
                                    updateIt = true;
                                }
                            }

                            if (updateIt) {
                                DataModelH.setValueByVariableEx(strVariable, Double.toString(EITLERPGLOBAL.round(lnFinalResult, 3)), 1);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    }
                }
            }
            Updating_H = false;
            UpdateAmounts();
        } catch (Exception e) {
            Updating_H = false;
        }
    }

    private void GatherVariableValues() {
        //Scan the table and gather values for variables
        colVariables.clear();

        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table

        for (int i = 0; i < TableL.getColumnCount(); i++) {
            double lValue = 0;
            if (DataModelL.getVariable(i) != null) {
                //if((!DataModelL.getVariable(i).trim().equals(""))&&(DataModelL.getColID(i)!=0))    //If Variable not blank
                if ((!DataModelL.getVariable(i).trim().equals(""))) {
                    //colVariables.put(DataModelL.getVariable(i),(String)DataModelL.getValueAt(TableL.getSelectedRow(), i));

                    //Add variable Value to Parser Table
                    if ((TableL.getValueAt(TableL.getSelectedRow(), i) != null) && (!TableL.getValueAt(TableL.getSelectedRow(), i).toString().equals(""))) {
                        if (TableL.getValueAt(TableL.getSelectedRow(), i) instanceof Boolean) {
                            if (DataModelL.getBoolValueByVariable(DataModelL.getVariable(i), TableL.getSelectedRow())) {
                                lValue = 1;
                            } else {
                                lValue = 0;
                            }
                        } else {
                            if (EITLERPGLOBAL.IsNumber((String) TableL.getValueAt(TableL.getSelectedRow(), i))) {
                                lValue = Double.parseDouble((String) TableL.getValueAt(TableL.getSelectedRow(), i));
                            }
                        }
                    } else {
                        lValue = 0;
                    }
                    myParser.addVariable(DataModelL.getVariable(i), lValue);
                }
            }
        }

        myParser.addFunction("IIF", new IIF(myParser));
    }

    private void FormatGridA() {
        DataModelA = new EITLTableModel();

        TableA.removeAll();
        TableA.setModel(DataModelA);

        //Set the table Readonly
        DataModelA.TableReadOnly(true);

        //Add the columns
        DataModelA.addColumn("Sr.");
        DataModelA.addColumn("User");
        DataModelA.addColumn("Status");
        DataModelA.addColumn("Department");
        DataModelA.addColumn("Received Date");
        DataModelA.addColumn("Action Date");
        DataModelA.addColumn("Remarks");

        TableA.setAutoResizeMode(TableA.AUTO_RESIZE_OFF);
        TableA.getColumnModel().getColumn(0).setCellRenderer(Paint);
        Paint.setColor(1, 1, Color.CYAN);

    }

    private void FormatTermsGrid() {

        //--- PO Terms Formatting -----//
        /*DataModelD=new EITLTableModel();
         
         TableD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableD.removeAll();
         TableD.setModel(DataModelD);
         
         DataModelD.addColumn("Sr.");
         DataModelD.addColumn("Code");
         DataModelD.addColumn("Description");
         
         DataModelD.SetNumeric(0, true);
         DataModelD.SetNumeric(1, true);
         
         //----- Install Table Model Event Listener -------//
         TableD.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableD.getValueAt(TableD.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"DELIVERY_CODE",theCode);
         TableD.setValueAt(Desc, TableD.getSelectedRow(), 2);
         }
         }
         }
         });
         //=============================================//
         
         
         
         //--- PO Terms Formatting -----//
         DataModelP=new EITLTableModel();
         
         TableP.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableP.removeAll();
         TableP.setModel(DataModelP);
         
         DataModelP.addColumn("Sr.");
         DataModelP.addColumn("Code");
         DataModelP.addColumn("Description");
         
         DataModelP.SetNumeric(0, true);
         DataModelP.SetNumeric(1, true);
         
         //----- Install Table Model Event Listener -------//
         TableP.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableP.getValueAt(TableP.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"PAYMENT_CODE",theCode);
         TableP.setValueAt(Desc, TableP.getSelectedRow(), 2);
         }
         }
         }
         });
         //=============================================//
         
         
         //--- PO Terms Formatting -----//
         DataModelO=new EITLTableModel();
         
         TableO.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         
         TableO.removeAll();
         TableO.setModel(DataModelO);
         
         DataModelO.addColumn("Sr.");
         DataModelO.addColumn("Code");
         DataModelO.addColumn("Description");
         
         DataModelO.SetNumeric(0, true);
         DataModelO.SetNumeric(1, true);
         
         //----- Install Table Model Event Listener -------//
         TableO.getModel().addTableModelListener(new TableModelListener() {
         public void tableChanged(TableModelEvent e) {
         if (e.getType() == TableModelEvent.UPDATE) {
         int col = e.getColumn();
         
         if(col==1) {
         int theCode=Integer.parseInt((String)TableO.getValueAt(TableO.getSelectedRow(), 1));
         
         String Desc=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"OTHER_CODE",theCode);
         TableO.setValueAt(Desc, TableO.getSelectedRow(), 2);
         }
         }
         }
         });*/
        //=============================================//
    }

    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();

        //========== Setting Up Header Fields ================//
        String FieldName = "";
        int SelHierarchy = EITLERPGLOBAL.getComboCode(cmbHierarchy);

        for (int i = 0; i < Tab1.getComponentCount() - 1; i++) {
            if (Tab1.getComponent(i).getName() != null) {

                FieldName = Tab1.getComponent(i).getName();
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {

                    Tab1.getComponent(i).setEnabled(true);
                }

            }
        }

        for (int i = 0; i < Tab2.getComponentCount() - 1; i++) {
            if (Tab2.getComponent(i).getName() != null) {

                FieldName = Tab2.getComponent(i).getName();
                if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {

                    Tab2.getComponent(i).setEnabled(true);
                }

            }
        }
        //=============== Header Fields Setup Complete =================//

        //=============== Setting Table Fields ==================//
        DataModelL.ClearAllReadOnly();
        for (int i = 0; i < TableL.getColumnCount(); i++) {
            FieldName = DataModelL.getVariable(i);

            if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
                //Do Nothing
            } else {
                DataModelL.SetReadOnly(i);
            }
        }

        //Disabling Grids
        if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", "DELIVERY_TERMS")) {
            DataModelD.TableReadOnly(false);
        } else {
            DataModelD.TableReadOnly(true);
        }

        if (clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", "OTHER_TERMS")) {
            DataModelO.TableReadOnly(false);
        } else {
            DataModelO.TableReadOnly(true);
        }
        //=======================================================//

    }

    private void FormatGridHS() {
        DataModelHS = new EITLTableModel();

        TableHS.removeAll();
        TableHS.setModel(DataModelHS);

        //Set the table Readonly
        DataModelHS.TableReadOnly(true);

        //Add the columns
        DataModelHS.addColumn("Rev No.");
        DataModelHS.addColumn("User");
        DataModelHS.addColumn("Date");
        DataModelHS.addColumn("Status");
        DataModelHS.addColumn("Remarks");

        TableHS.setAutoResizeMode(TableHS.AUTO_RESIZE_OFF);
    }

    private void PreviewAuditReport() {
        try {
            URL ReportFile = new URL("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptQuotationA.jsp?dbURL=" + EITLERPGLOBAL.DatabaseURL + "&CompanyID=" + EITLERPGLOBAL.gCompanyID + "&DocNo=" + txtQTID.getText());
            EITLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Previwing " + e.getMessage());
        }
    }

    private void Amend() {

        //== Financial Year Validation-------------//
        if (!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null, "The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//

        String lQTNo = (String) ObjQuotation.getAttribute("QUOT_ID").getObj();
        EITLERPGLOBAL.ChangeCursorToWait(this);
        EditMode = EITLERPGLOBAL.AMEND;
        GenerateCombos();

        //---New Change ---//
        GenerateCombos();
        DisplayData();
        //----------------//

        if (ApprovalFlow.IsCreator(19, lQTNo) || clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0, 282)) {
            SetFields(true);
        } else {
            EnableApproval();
        }

        DisableToolbar();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }

}
