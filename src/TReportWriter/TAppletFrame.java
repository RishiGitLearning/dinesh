package TReportWriter;

import java.awt.Frame;
import java.awt.Event;
import java.awt.Dimension;
import java.applet.Applet;
import java.awt.AWTEvent;
//import java.util.*;
import javax.swing.JFrame;
import javax.swing.JApplet;
import java.awt.Toolkit;
//import EITLERP.Purchase.*;
import java.applet.AppletContext;
import java.net.URL;
   
  

// Applet to Application Frame window
public class TAppletFrame extends JFrame
{
    public Object ObjApplet;
    
        
    public static void ShowDoc(URL aURL,AppletContext theContext)
    {
        
       AppletContext appletContext=theContext;
       TAppletFrame f=new TAppletFrame("");
       f.show(true);
       appletContext.showDocument(aURL,"_self");  
    }
    
    public static void startAppletByObject(String className,String title,Object pApplet)
    {
       // local variables
       JApplet a=null;
       Dimension appletSize;
       
       try
       {
       // Cast passed object to JApplet
       a = (JApplet) pApplet;

       // initialize the applet
       a.init();
       a.start();
       
       // create new application frame window
       TAppletFrame f = new TAppletFrame(title);
       
       // add applet to frame window
       f.getContentPane().add("Center",a);
       
       // resize frame window to fit applet
       // assumes that the applet sets its own size
       // otherwise, you should set a specific size here.
       
       Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
      
       appletSize =  a.getSize();
       f.setSize(a.getWidth(),a.getHeight());
       f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       f.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);              
       f.show(true);
       }
       catch(Exception e)
       {}
    }  // end startApplet()

    
    public void startAppletEx(String className,String title)
    {
       // local variables
       JApplet a=null;
       Dimension appletSize;
       
       try 
       {
          // create an instance of your applet class
          a = (JApplet) Class.forName(className).newInstance();
       }
       
       catch (ClassNotFoundException e) {}
       catch (InstantiationException e) {}
       catch (IllegalAccessException e) {}

       // initialize the applet
       a.setName("Link");
       a.init();
       a.start();
       
       ObjApplet=a;
       
       // create new application frame window
       TAppletFrame f = new TAppletFrame(title);
       
       // add applet to frame window
       f.getContentPane().add("Center",a);
       
       // resize frame window to fit applet
       // assumes that the applet sets its own size
       // otherwise, you should set a specific size here.
       
       Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();       
       
       appletSize =  a.getSize();
       f.setSize(a.getWidth(),a.getHeight());
       f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       f.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height -appletSize.getHeight())/2);              
       f.show(true);
    }  // end startApplet()

    
    public static void startApplet(String className,String title)
    {
       // local variables
       JApplet a=null;
       Dimension appletSize;
       
       try 
       {
          // create an instance of your applet class
          a = (JApplet) Class.forName(className).newInstance();
       }
       
       catch (ClassNotFoundException e) {return;}
       catch (InstantiationException e) {return; }
       catch (IllegalAccessException e) {return;}

       // initialize the applet
       a.init();
       a.start();
       
       // create new application frame window
       TAppletFrame f = new TAppletFrame(title);
       
       // add applet to frame window
       f.getContentPane().add("Center",a);
       
       // resize frame window to fit applet
       // assumes that the applet sets its own size
       // otherwise, you should set a specific size here.
       appletSize =  a.getSize();
       f.setSize(a.getWidth(),a.getHeight());
       f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

       Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
       
       f.setLocation((int)(screenSize.width -appletSize.getWidth())/2,(int)(screenSize.height -appletSize.getHeight())/2);       
       f.show(true);
       
    }  // end startApplet()
  

    
    
    // constructor needed to pass window title to class Frame
    public TAppletFrame(String name)
    {
       // call java.awt.Frame(String) constructor
       super(name);
    }

    // needed to allow window close
    public void processEvent(AWTEvent e)
    {
        
       // Window Destroy event
       if (e.getID() == Event.WINDOW_DESTROY)
       {
            dispose();
           // exit the program
          //System.exit(0);
       }    
   }  // end handleEvent()

}   // end class AppletFrame






