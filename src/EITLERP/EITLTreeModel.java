/*
 * EITLTreeModel.java
 *
 * Created on April 7, 2004, 9:42 AM
 */
 
package EITLERP;
 
/**
 *
 * @author  nhpatel
 * @version 
 */
import javax.swing.tree.*;
import java.awt.*;
import java.util.*;
 
public class EITLTreeModel 
{

public String Key;
public String Caption;

public EITLTreeModel(String pKey,String pCaption)
{
    Key=pKey;
    Caption=pCaption;
}

public String toString()
{
    return Caption;
}
   
    
}
