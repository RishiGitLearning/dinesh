/*
 * Test.java
 *
 * Created on June 14, 2004, 1:29 PM
 */  

package EITLERP;

import javax.swing.*;
import java.awt.*;

/** 
 *
 * @author  nrpithva
 */
public class Test {
    
    /** Creates a new instance of Test */
    public Test() {
        JOptionPane.showMessageDialog(null," Blank constructor called");
    }

    public Test(int pParam)
    {
       JOptionPane.showMessageDialog(null," Parameter passed is"+pParam); 
    }
    
    
}
