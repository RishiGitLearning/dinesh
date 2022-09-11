/*
 * TDTextField.java
 *
 * Created on July 7, 2007, 5:05 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import javax.swing.JTextField;

public class TDTextField extends javax.swing.JTextField {
    
    public int BandIndex=0;
    public int TextIndex=0;
    public String FieldType="Text";
        
    /** Creates a new instance of TDTextField */
    public TDTextField() {
        this.setEditable(false);
        this.setFont(new java.awt.Font("Monospaced", 1, 14));
    }
    
}
