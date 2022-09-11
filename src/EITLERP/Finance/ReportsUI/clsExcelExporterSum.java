/*
 * clsExcelExporter.java
 * 
 * Created on January 19, 2015, 2:35 PM
 *@author  Dhaval Rahevar & Rajpalsinh
 */

package EITLERP.Finance.ReportsUI;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
//import java.awt.*;
import java.io.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.table.*;
import jxl.*;
import jxl.write.*;

/**
 *
 * @author  ashutosh
 * @version
 */

public class clsExcelExporterSum {
    
    public void fillData(JTable table, File file) {

        try {

            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
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
//                    Label row = new Label(j, i + 1,
//                            model.getValueAt(i, j).toString());
//                    sheet1.addCell(row);
                    if (j==7 || j==8 || j==9 || j==10 || j==11 || j==12 || j==13 || j==14 || j==15 || j==16 || j==17 || j==18 || j==19 || j==20 || j==21 || j==22 || j==23) {   
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
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
      }
}
