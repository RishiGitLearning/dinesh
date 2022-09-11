package EITLERP.FeltSales.Budget;

import EITLERP.FeltSales.MonthlySalesPlan.*;
import EITLERP.Production.*;
import java.util.*;
//import java.lang.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.table.*;
import jxl.*;
import jxl.write.*;

public class clsExcelExporter {

    public void fillData(JTable table, File file, String shtnm) {

        try {

            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet(shtnm, 0);
            //sheet1.setProtected(true);
            WritableSheet sheet2 = workbook1.createSheet("Party Status", 1);
            //sheet2.setProtected(true);
            TableModel model = table.getModel();
            NumberFormat decimalNo = new NumberFormat("#0.00"); 
            WritableCellFormat numberFormat = new WritableCellFormat(decimalNo);
            
            Label column = new Label(0, 0, "Party Status");
            sheet2.addCell(column);
            column = new Label(1, 0, "Code");
            sheet2.addCell(column);
            
            column = new Label(0, 2, "Budget Party");
            sheet2.addCell(column);
            column = new Label(1, 2, "BP");
            sheet2.addCell(column);
            
            column = new Label(0, 3, "Revived Party");
            sheet2.addCell(column);
            column = new Label(1, 3, "RP");
            sheet2.addCell(column);
            
            column = new Label(0, 4, "BD Party");
            sheet2.addCell(column);
            column = new Label(1, 4, "BD");
            sheet2.addCell(column);
            
            column = new Label(0, 5, "Walk in Customer");
            sheet2.addCell(column);
            column = new Label(1, 5, "WC");
            sheet2.addCell(column);
            
            column = new Label(0, 6, "Revision of Budget Party");
            sheet2.addCell(column);
            column = new Label(1, 6, "RB");
            sheet2.addCell(column);
            
            for (int i = 0; i < model.getColumnCount(); i++) {
                column = new Label(i, 0, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    try{
                        if(j==0 || j==2 || j==3 || j==15){
                            Label row = new Label(j, i + 1,
                                model.getValueAt(i, j).toString());
                        sheet1.addCell(row);
                        }
                        else{
                        sheet1.addCell(new jxl.write.Number(j, i + 1,Double.valueOf(model.getValueAt(i, j).toString()),numberFormat));
                        }
                    }catch(Exception ex){
                        Label row = new Label(j, i + 1,
                                model.getValueAt(i, j).toString());
                        sheet1.addCell(row);
                    }
//                    if (j>5 && j!=15 && j<24) {   
//                        sheet1.addCell(new jxl.write.Number(j, i + 1,Double.valueOf(model.getValueAt(i, j).toString()),numberFormat));
//                    } else {
//                        Label row = new Label(j, i + 1,
//                                model.getValueAt(i, j).toString());
//                        sheet1.addCell(row);
//                    }
                }
            }
            workbook1.write();
            workbook1.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void fillData(JTable TableWIP, File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
