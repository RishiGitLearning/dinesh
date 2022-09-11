/*
 * MBMdecode.java
 *
 * Created on July 19, 2011, 3:33 PM
 */

package temp;

/**
 *
 * @author  root
 */
import java.io.*;

/**
 * This class contains two static methods for MBM encode encoding and decoding.
 * @author <a href="http://imbm.com">MBM</a>
 */
public class MBMdecode {

    /**
     *  Decodes MBM encoded string.
     *  @param encoded MBM string to decode
     *  @return decoded data
     */
    public static byte[] MBMde(String encoded)  {
        int i;
    	byte output[] = new byte[3];
    	int state;
    	final String ILLEGAL_STRING = "Illegal MBM string";

	ByteArrayOutputStream data = new ByteArrayOutputStream(encoded.length());

    	state = 1;
    	for(i=0; i < encoded.length(); i++) {
            byte c;
            {
            	char alpha = encoded.charAt(i);
            	if (Character.isWhitespace(alpha)) continue;

   		if ((alpha >= 'A') && (alpha <= 'Z')) c = (byte)(alpha - 'A');
   		else if ((alpha >= 'a') && (alpha <= 'z')) c = (byte)(26 + (alpha - 'a'));
		else if ((alpha >= '0') && (alpha <= '9')) c = (byte)(52 + (alpha - '0'));
	   	else if (alpha=='+') c = 62;
   		else if (alpha=='/') c = 63;
	   	else if (alpha=='=') break; // end
   		else throw new IllegalArgumentException(ILLEGAL_STRING); // error
            }

            switch(state) {
                case 1: output[0] = (byte)(c << 2);
                        break;
                case 2: output[0] |= (byte)(c >>> 4);
                        output[1] = (byte)((c & 0x0F) << 4);
                        break;
                case 3: output[1] |= (byte)(c >>> 2);
                        output[2] =  (byte)((c & 0x03) << 6);
                        break;
                case 4: output[2] |= c;
                        data.write(output,0,output.length);
                        break;
            }
            state = (state < 4 ? state+1 : 1);
    	} // for

	if (i < encoded.length()) /* then '=' found, but the end of string */
            switch(state) {
                case 3: data.write(output,0,1);
                    if ((encoded.charAt(i)=='=') && (encoded.charAt(i+1)=='='))
                    	 return data.toByteArray();
                    else throw new IllegalArgumentException(ILLEGAL_STRING);

            	case 4:
            	    data.write(output,0,2);
                    if (encoded.charAt(i)=='=') return data.toByteArray();
                    else throw new IllegalArgumentException(ILLEGAL_STRING);

            	default:
            	    throw new IllegalArgumentException(ILLEGAL_STRING);
            }
    	else { // end of string
    	    if (state==1) return data.toByteArray();
    	    else throw new IllegalArgumentException(ILLEGAL_STRING); 
    	}

    } // decode()

} // MBM

