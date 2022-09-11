package EITLERP;

/*
 * Variant.java
 *
 * Created on March 24, 2004, 10:04 AM
 */
 

public class Variant
{
    private Object objVal;
    private double numVal;
    private boolean boolVal;
    
    
    public Variant(int Val)
    {
        numVal=Val;
    }
    
    public Variant(long Val)
    {
      numVal=Val;  
    }

    public Variant(float Val)
    {
      numVal=Val;  
    }

    public Variant(double Val)
    {
      numVal=Val;  
    }
    
    public Variant(boolean Val)
    {
      boolVal=Val;  
    }
    
    
    public Variant(Object Val)
    {
        objVal=Val;
    }
    
    
    public Variant()
    {
        
    }
    
    public void set(int Val)
    {
        numVal=Val;
    }
    
    public void set(long Val)
    {
        numVal=Val;
    }
    
    public void set(double Val)
    {
        numVal=Val;
    }
    
    
    
    public void set(float Val)
    {
        numVal=Val;
    }
    
    public void set(Object Val)
    {
        objVal=Val;
    }

    public void set(boolean Val)
    {
        boolVal=Val;
    }
        
    public double getVal()
    {
       return numVal; 
    }

    public Object getObj()
    {
       return objVal; 
    }
       
    public String getString()
    {
       if(objVal==null)
       {
       return "";    
       }
       else
       {
       return objVal.toString(); 
       }
    }
    
    public int getInt()
    {
      return (int)numVal;  
    }
    
    public long getLong()
    {
      return (long)numVal; 
    }
    
    public double getDouble()
    {
      return numVal;  
    }
     
    
    public boolean getBool()
    {
        return boolVal;
    }
}
