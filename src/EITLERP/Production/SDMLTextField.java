/*
 * SDMLTextField.java
 *
 * Created on September 19, 2013, 3:58 PM
 */

package EITLERP.Production;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.RenderingHints;

/**
 *
 * @author  Vivek Kumar
 */
public class SDMLTextField  extends JTextField implements FocusListener {
    
    private int length = 0;
    private String hint;
    
    /** Creates a new instance of SDMLTextField
     *  Creates a TextField with a fixed length of string input.
     */
    public SDMLTextField(int length) {
        super(new FixedLengthPlainDocument(length), "", length);
    }
    
    /** Creates a new instance of SDMLTextField
     *  Creates a TextField with a fixed length of string input and hint.
     */
    public SDMLTextField(int length, String hint){
        super(new FixedLengthPlainDocument(length), "", length);
        this.hint = hint;
        addFocusListener(this);
    }
    
    /** Creates a new instance of SDMLTextField
     *  Creates a TextField with hint.
     */
    public SDMLTextField(String hint){
        this.hint = hint;
        addFocusListener(this);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        setMargin(new Insets(2, 2, 2, 2));
        
        if ( this.getText().equals("")) {
            int width = this.getWidth();
            int height = this.getHeight();
            Font font = g.getFont();
            Font italic = font.deriveFont(Font.ITALIC);
            Font bold = font.deriveFont(Font.BOLD);
            Font set = font.deriveFont(Font.CENTER_BASELINE);
            
            Color prevColor = g.getColor();
            g.setFont(italic);
            g.setFont(bold);
            g.setFont(set);
            g.setColor(UIManager.getColor("textInactiveText"));
            
            int h = g.getFontMetrics().getHeight();
            int textBottom = (height - h) / 2 + h - 4;
            int x = this.getInsets().left;
            
            Graphics2D g2d = (Graphics2D) g;
            
            RenderingHints hints = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawString(hint, x, textBottom);
            g2d.setRenderingHints(hints);
            g.setFont(font);
            g.setColor(prevColor);
        }
    }
    
    public void focusGained(FocusEvent focusEvent) {
        this.repaint();
    }
    
    public void focusLost(FocusEvent focusEvent) {
        this.repaint();
    }
    
}
