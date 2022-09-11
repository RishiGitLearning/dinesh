/*
 * GUIManager.java
 *
 * Created on December 28, 2004, 1:56 PM
 */

package EITLERP;

/**
 *
 * @author  root
 */


import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class GUIManager {
  
    public static boolean DoNotShowWarning=false;
    
    /** Creates a new instance of GUIManager */
    public GUIManager() {
    }
    
    public static String GTK="com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    public static String Windows="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    public static String Mac="com.sun.java.swing.plaf.mac.MacLookAndFeel";
    public static String Motif="com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    public static String Metal="javax.swing.plaf.metal.MetalLookAndFeel";
    
    public static boolean isAvailableLookAndFeel(String laf) {
        try {
            Class lnfClass = Class.forName(laf);
            LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
            return newLAF.isSupportedLookAndFeel();
        } catch(Exception e) { // If ANYTHING weird happens, return false
            return false;
        }
    }
    
    public static void SetLookFeel(String className) {
        try {
            if(isAvailableLookAndFeel(className)) {
                UIManager.setLookAndFeel(className);
            }
        }
        catch(Exception e) {
            
        }
    }
    
    public static void UpdateComponents(Object Container) {
        
        try {
            int MainCount=0;
            if(Container instanceof JPanel) {
                MainCount=((JPanel)Container).getComponentCount();
            }
            
            if(Container instanceof JApplet) {
                MainCount=((JApplet)Container).getComponentCount();
            }
            
            
            if(Container instanceof JScrollPane) {
                MainCount=((JScrollPane)Container).getComponentCount();
            }
            
            if(Container instanceof JFrame) {
                MainCount=((JFrame)Container).getComponentCount();
            }
            
            
            for(int m=0;m<MainCount;m++) {
                Component theComp=null;
                if(Container instanceof JPanel) {
                    theComp=((JPanel)Container).getComponent(m);
                    
                }
                
                if(Container instanceof JApplet) {
                    theComp=((JApplet)Container).getComponent(m);
                }
                
                
                if(Container instanceof JScrollPane) {
                    theComp=((JScrollPane)Container).getComponent(m);
                }
                
                if(Container instanceof JFrame) {
                    theComp=((JFrame)Container).getComponent(m);
                }
                
                
                if(theComp instanceof JPanel||theComp instanceof JApplet||theComp instanceof JScrollPane||theComp instanceof JFrame) {
                    UpdateComponents(Container);
                }
                
                else {
                    SwingUtilities.updateComponentTreeUI(theComp);
                }
                
            }
        }
        catch(Exception e) {
            
        }
        
    }

    
    
    public static void ForcefullyFocus(JApplet theApplet,Component focusComponent) {
        MakeFocusable(theApplet, false);
        focusComponent.setFocusable(true);
        SwingUtilities.updateComponentTreeUI(focusComponent);
        focusComponent.requestFocus();
    }
    
    
    public static void MakeFocusable(Object theContainer,boolean focusable) {
        
        try {
            int MainCount=0;
            if(theContainer instanceof Container) {
                MainCount=((Container)theContainer).getComponentCount();
            }
            
            for(int m=0;m<MainCount;m++) {
                Component theComp=null;
                
                if(theContainer instanceof Container) {
                    theComp=((Container)theContainer).getComponent(m);
                }
                
                theComp.setFocusable(focusable);
                SwingUtilities.updateComponentTreeUI(theComp);
                
                
                if(theComp instanceof Container) {
                    MakeFocusable(theComp,focusable);
                }
                
                
            }
        }
        catch(Exception e) {
            
        }
        
    }
    
}
