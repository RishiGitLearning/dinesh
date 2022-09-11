package EITLERP.GSTR;

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
                    if (j==4 || j==10) {   
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

    public void fillData(JTable Table_b2b, File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
public void toExcel(JTable table, File file){
    try{
        TableModel model = table.getModel();
        FileWriter excel = new FileWriter(file);

        for(int i = 0; i < model.getColumnCount(); i++){
            excel.write(model.getColumnName(i) + "\t");
        }

        excel.write("\n");

        for(int i=0; i< model.getRowCount(); i++) {
            for(int j=0; j < model.getColumnCount(); j++) {
                excel.write(model.getValueAt(i,j).toString()+"\t");
            }
            excel.write("\n");
        }

        excel.close();

    }catch(IOException e){ System.out.println(e); }
}

}
