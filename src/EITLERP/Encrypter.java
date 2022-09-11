/*
 * Encrypter.java
 *
 * Created on December 21, 2004, 12:50 PM
 */

package EITLERP;

/**
 *
 * @author  root
 */

import javax.crypto.*;




public class Encrypter {
    
    private Cipher ecipher;
    private Cipher dcipher;
    /** Creates a new instance of Encrypter */
    public Encrypter(SecretKey key) {
        try {
            ecipher=Cipher.getInstance("DES");
            dcipher=Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE,key);
            dcipher.init(Cipher.DECRYPT_MODE,key);
        }
        catch(Exception e) {
            
        }

        
        
    }
    
    public String encrypt(String str) {
        try {
            byte[] utf8=str.getBytes("UTF8");
            
            byte[] enc=ecipher.doFinal(utf8);
            return new sun.misc.BASE64Encoder().encode(enc);
            
        }
        catch(Exception e) {
            
        }
        return "";
        
        
    }
    
    public String decrypt(String str) {
        
        try {
            byte[] dec=new sun.misc.BASE64Decoder().decodeBuffer(str);
            
            byte[] utf8=dcipher.doFinal(dec);
            
            return new String(utf8,"UTF8");
            
        }
        catch(Exception e) {
            
        }
        return "";
        
    }
    
    
    
}
