/*
 * clsExcelExporter.java
 * 
 * Created on January 19, 2015, 2:35 PM
 *@author  Dhaval Rahevar & Rajpalsinh
 */

package EITLERP.FilterFabric;

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

public class clsExcelExporter {
    
    public void fillData(JTable table, File file) {

        try {

            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0);
            //sheet1.setProtected(true);
            TableModel model = table.getModel();

            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    Label row = new Label(j, i + 1,
                            model.getValueAt(i, j).toString());
                    sheet1.addCell(row);
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
