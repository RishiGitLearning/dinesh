package EITLERP.Purchase.Reports.New;

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

public class clsExcelExporterB2BHSN {

    public void fillData(JTable table, File file, String shtnm) {

        try {

            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet(shtnm, 0);
            //sheet1.setProtected(true);
            TableModel model = table.getModel();
            NumberFormat decimalNo = new NumberFormat("#0.00"); 
            WritableCellFormat numberFormat = new WritableCellFormat(decimalNo);
            
            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    if (j==4 || j==5 || j==6 || j==7 || j==8 || j==9) {   
                        sheet1.addCell(new jxl.write.Number(j, i + 1,Double.valueOf(model.getValueAt(i, j).toString()),numberFormat));
                    } else {
                        Label row = new Label(j, i + 1,
                                model.getValueAt(i, j).toString());
                        sheet1.addCell(row);
                    }
                }
            }
            workbook1.write();
            workbook1.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
