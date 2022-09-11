/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common;

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
public class SelectKeyFields {
    
    HashMap<String, String> fields;
    HashMap<String, String> othrfields;
    
    public SelectKeyFields() {
        this.fields = new LinkedHashMap<String, String>();
        this.othrfields = new LinkedHashMap<String, String>();
    }
    
    public void setField(String Key1, String CAPTION) {
        fields.put(Key1, CAPTION);
    }
    
    public void setFieldOther(String Key2, String Key1) {
        othrfields.put(Key2, Key1);
    }
    
    public String getQuery() {
        String str = "";
        str = getQuery1();
        return str;
    }
    
    public String getQuery1() {
        String str = "";
        if (fields.isEmpty()) {
            System.out.println("Key NOT DONE BECAUSE OF FIELDS ARE NOT SETTED IN LIST");
            return "";
        }
        final JCheckBox[] chkField = new JCheckBox[fields.size()];
        final JPanel[] panels = new JPanel[(fields.size() / 5) + 2];
        
        for (int i = 0; i <= (fields.size() / 5) + 1; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(new GridLayout(1, 5));
        }
        JLabel line = new JLabel("---------------------------------------------------------------------------------------------------------------------------------------");
        JLabel line2 = new JLabel("--------------------------------------------------------------------------------------------------------------------------------------");
        final JLabel sort_text = new JLabel("");
        final JLabel blnk_lbl1 = new JLabel("\n\n\n\n\n");
        
        int i = 0;
        for (Map.Entry e : fields.entrySet()) {
            final String key = (String) e.getKey();
            final String value = (String) e.getValue();
            
            final int point = i;
            chkField[point] = new JCheckBox(value);
            panels[point / 5].add(chkField[point]);
            
            chkField[point].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blnk_lbl1.setText("");
                    if (chkField[point].isSelected()) {
                        sort_text.setText(sort_text.getText() + "" + key + ",");
                    } else {
                        String str = sort_text.getText();
                        str = str.replace(key + ",", "");
                        sort_text.setText(str);
                    }
                }
            });
            i++;
        }
        if (panels[i / 5].getComponentCount() < 5) {
            for (int j = panels[i / 5].getComponentCount() + 1; j <= 5; j++) {
                panels[i / 5].add(new JLabel(""));
            }
        }
        Object[] grp = {line2, panels, line, sort_text, blnk_lbl1};
        int mcnf=JOptionPane.showConfirmDialog(null, grp, "SELECT FIELDS FOR Key", JOptionPane.PLAIN_MESSAGE);
        if(mcnf==0){
        if (!sort_text.getText().equals("")) {
            str = sort_text.getText();
            str = str.substring(0, str.length() - 1);
        }
        }
        else{
            str="";
        }
        return str;
    }
    
    public String getOtherKey(String key1) {
        String OtherKey = "";
        if (key1.length() == 0) {
            return "";
        } else {
            String[] mOtherkey;
            mOtherkey = key1.split(",");
            for (String s : mOtherkey) {
                for (Map.Entry e : othrfields.entrySet()) {
                    final String key = (String) e.getKey();
                    final String value = (String) e.getValue();
                    if (s.equals(value)) {
                        OtherKey = OtherKey + key + ",";
                    }
                }
            }
            return OtherKey.substring(0, OtherKey.length() - 1);
            //return OtherKey;
        }
    }
}
