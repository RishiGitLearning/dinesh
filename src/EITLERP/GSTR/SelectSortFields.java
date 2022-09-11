/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.GSTR;

//import EITLERP.FeltSales.common.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author DAXESH PRAJAPATI
 */
public class SelectSortFields {

    HashMap<String, String> fields;
    final JRadioButton rdoAsce;
    final JRadioButton rdoDesc;

    public enum DEFAULT_ORDER {
        ASCENDING,
        DESCENDING
    }

    public SelectSortFields() {
        this.fields = new LinkedHashMap<String, String>();
        rdoAsce = new JRadioButton("Ascending");
        rdoDesc = new JRadioButton("Descending");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdoAsce);
        bg.add(rdoDesc);
        rdoAsce.setSelected(true);
    }

    public void setField(String FIELD_NAME, String CAPTION) {
        fields.put(FIELD_NAME, CAPTION);
    }
    
    
    public String getQuery()
    {
        String str = "";
        str = getQuery(SelectSortFields.DEFAULT_ORDER.ASCENDING);
        return str;
    }

    public String getQuery(DEFAULT_ORDER order) {
        String str = "";
        if (fields.isEmpty()) {
            System.out.println("SORTING NOT DONE BECAUSE OF FIELDS ARE NOT SETTED IN LIST");
            return "";
        }
        final JCheckBox[] chkField = new JCheckBox[fields.size()];
        final JPanel[] panels = new JPanel[(fields.size() / 5)+2];
        
        for (int i = 0; i <= (fields.size() / 5) + 1; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(new GridLayout(1, 5));
        }
        JLabel line = new JLabel("------------------------------------------------------------------------------------------------------------");
        JLabel line2 = new JLabel("-----------------------------------------------------------------------------------------------------------");
        final JLabel sort_text = new JLabel("");
        final JLabel blnk_lbl1 = new JLabel("\n\n\n\n\n");
        if (order == DEFAULT_ORDER.ASCENDING) {
            rdoAsce.setSelected(true);
        } else {
            rdoDesc.setSelected(true);
        }
        int i = 0;
        for (Map.Entry e: fields.entrySet()) {
            final String key = (String)e.getKey();
            final String value = (String)e.getValue();
            final int point = i;
            chkField[point] = new JCheckBox(value);
            panels[point / 5].add(chkField[point]);
            
            chkField[point].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blnk_lbl1.setText("");
                    if (chkField[point].isSelected()) {
                        if (rdoDesc.isSelected()) {
                            sort_text.setText(sort_text.getText() + "" + key + " DESC,");
                        } else {
                            sort_text.setText(sort_text.getText() + "" + key + ",");
                        }
                    } else {
                        String str = sort_text.getText();
                        str = str.replace(key + " DESC,", "");
                        str = str.replace(key + ",", "");
                        sort_text.setText(str);
                    }
                }
            });
            i++;
        }
        if(panels[i/5].getComponentCount()<5)
        {
            for(int j=panels[i/5].getComponentCount()+1;j<=5;j++)
            {
                panels[i/5].add(new JLabel(""));
            }
        }
        Object[] grp = {rdoAsce, rdoDesc, line2, panels, line, sort_text, blnk_lbl1};
        JOptionPane.showMessageDialog(null, grp, "SELECT FIELDS FOR SORTING", JOptionPane.PLAIN_MESSAGE);
        if (!sort_text.getText().equals("")) {
            str = sort_text.getText();
            str = " ORDER BY " + str.substring(0, str.length() - 1);
        }
        return str;
    }
}
