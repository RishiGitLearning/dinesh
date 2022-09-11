package EITLERP.FeltSales.Reports;

//import java.lang.*;
import EITLERP.data;
import javax.swing.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
            NumberFormat decimalNo1 = new NumberFormat("#0");
            WritableCellFormat numberFormat1 = new WritableCellFormat(decimalNo1);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    try {
                        sheet1.addCell(new jxl.write.Number(j, i + 1, Integer.valueOf(model.getValueAt(i, j).toString()), numberFormat1));
                    } catch (Exception a) {
                        try {
                            sheet1.addCell(new jxl.write.Number(j, i + 1, Double.valueOf(model.getValueAt(i, j).toString()), numberFormat));
                        } catch (Exception ex) {
                            Label row = new Label(j, i + 1,
                                    model.getValueAt(i, j).toString());
                            sheet1.addCell(row);
                        }
                    }
                }
            }
            workbook1.write();
            workbook1.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fillData(String sql, File file, String shtnm) {

        try {
            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            ResultSet rs = null;
            ResultSetMetaData rsInfo = null;
            String msql[] = sql.split("#");
            String mshtnm[] = shtnm.split("#");

            NumberFormat decimalNo = new NumberFormat("#0.00");
            WritableCellFormat numberFormat = new WritableCellFormat(decimalNo);
            NumberFormat decimalNo1 = new NumberFormat("#0");
            WritableCellFormat numberFormat1 = new WritableCellFormat(decimalNo1);

            for (int d = 0; d < msql.length; d++) {
                WritableSheet sheet1 = workbook1.createSheet(mshtnm[d], 0);
                //sheet1.setProtected(true);
                rs = data.getResult(msql[d]);
                rs.first();
                if (rs.getRow() > 0) {
                    rsInfo = rs.getMetaData();
                    int i = 1;
                    for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                        Label column = new Label(i - 1, 0, rsInfo.getColumnName(i));
                        sheet1.addCell(column);
                    }
                    rs.first();
                    int r = 1;
                    while (!rs.isAfterLast()) {
                        for (int j = 0; j < i - 1; j++) {
                            try {
                                sheet1.addCell(new jxl.write.Number(j, r, Integer.valueOf(rs.getString(j + 1).toString()), numberFormat1));
                            } catch (Exception a) {
                                try {
                                    sheet1.addCell(new jxl.write.Number(j, r, Double.valueOf(rs.getString(j + 1)), numberFormat));
                                } catch (Exception ex) {
                                    Label row = new Label(j, r, rs.getString(j + 1));
                                    sheet1.addCell(row);
                                }
                            }
                        }
                        r++;
                        rs.next();
                    }
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
