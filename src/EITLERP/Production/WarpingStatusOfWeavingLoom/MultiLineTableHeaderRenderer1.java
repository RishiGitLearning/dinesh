/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.WarpingStatusOfWeavingLoom;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
//import javax.swing.LookAndFeel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author root
 */
public class MultiLineTableHeaderRenderer1 extends JTextArea implements TableCellRenderer {

    public MultiLineTableHeaderRenderer1() {
        setEditable(false);
        setLineWrap(true);
        setOpaque(false);
        setFocusable(false);
        setWrapStyleWord(true);
//        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
//        LookAndFeel.installBorder(this, "TableHeader.cellBorder");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int width = table.getColumnModel().getColumn(column).getWidth();
        setText((String) value);
        setSize(width, getPreferredSize().height);
        return this;
    }
}
