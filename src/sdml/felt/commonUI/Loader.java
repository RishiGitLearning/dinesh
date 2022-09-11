/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;
import java.applet.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;


public class Loader {

private JApplet ObjApplet;    
private JApplet ObjParent;
private boolean Model;

    //Returns the Applet Object
    public Object getObj()
   {
       return ObjApplet;
   }

    //This function moves the browser to the URL specified
    //but in the same window 
    public static void openURL(JApplet pApplet,String pURL)
    {
        try
        {
            URL mURL=new URL(pURL);
            java.applet.AppletContext AppCntx=pApplet.getAppletContext();
            AppCntx.showDocument(mURL);
        }
        catch(Exception e)
        {
        }
    }

    //This function moves the browser to the URL specified
    //but with target option
    //use "_blank" to open new browser window
    //You can use name of target frame 

    public static void openURL(JApplet pApplet,String pURL,String pTarget)
    {
        try
        {
            URL mURL=new URL(pURL);
            java.applet.AppletContext AppCntx=pApplet.getAppletContext();
            AppCntx.showDocument(mURL,pTarget);
        }
        catch(Exception e)
        {
        }
    }   
    
    /** Creates new Loader */
    public Loader(JApplet pParent,String pForm,boolean pModel) 
    {
       ObjParent=pParent;
       Model=pModel;
       
       Dimension appletSize;
       
       try 
       {
          // create an instance of your applet class
          ObjApplet = (JApplet) Class.forName(pForm).newInstance();
       }
       catch (ClassNotFoundException e) { return; }
       catch (InstantiationException e) { return; }
       catch (IllegalAccessException e) { return; }

       // initialize the applet
       ObjApplet.init();
       ObjApplet.start();
      
       //Find the parent frame of parent applet
       Frame f=findParentFrame(pParent);              
       
       JDialog aDialog;
       
       if(f != null)
       {
          aDialog=new JDialog(f,"SDML",pModel);
          
          aDialog.getContentPane().add("Center",ObjApplet);
          appletSize =  ObjApplet.getSize();
          aDialog.setSize(appletSize);
          aDialog.setResizable(true);
                
          //Place it to center of the screen
          Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
          
          aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
          
          aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
          aDialog.show();  
       }
    }
 
//Recurses through the hierarchy of classes 
//until it finds Frame 
private Frame findParentFrame(JApplet pApplet)
{ 
    Container c = (Container) pApplet; 
    while(c != null){ 
      if (c instanceof Frame) 
        return (Frame)c; 

      c = c.getParent(); 
    } 
    return (Frame)null; 
  } 
}

