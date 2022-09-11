/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;
 
/**
 *
 * @author  nhpatel
 * @version 
 */
import javax.swing.tree.*;
import java.awt.*;
import java.util.*;
 
public class SDMLTreeModel 
{

public String Key;
public String Caption;

public SDMLTreeModel(String pKey,String pCaption)
{
    Key=pKey;
    Caption=pCaption;
}

public String toString()
{
    return Caption;
}
   
    
}
