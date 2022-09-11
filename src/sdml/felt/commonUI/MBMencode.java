/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

/**
 * This class contains two static methods for MBM encode encoding and decoding.
 * @author <a href="http://imbm.com">MBM</a>
 */
public class MBMencode {

    /**
     *  Encodes binary data by MBM method.
     *  @param  data binary data to encode
     *  @return MBM encoded data 
     */
    public static String MBMen(byte[] data) {

	final String MBM =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	char output[] = new char[4];
	int state = 1;
	int restbits = 0;
        int chunks = 0;

	StringBuffer encoded = new StringBuffer();

    	for(int i=0; i < data.length; i++) {
            int ic = (data[i] >= 0 ? data[i] : (data[i] & 0x7F) + 128);
            switch (state) {
		case 1: output[0] = MBM.charAt(ic >>> 2);
                     restbits = ic & 0x03;
                     break;
             	case 2: output[1] = MBM.charAt((restbits << 4) | (ic >>> 4));
                     restbits = ic & 0x0F;
                     break;
             	case 3: output[2] = MBM.charAt((restbits << 2) | (ic >>> 6));
                     output[3] = MBM.charAt(ic & 0x3F);
                     encoded.append(output);

                     // keep no more then 76 character per line
                     chunks++;
                     if ((chunks % 19)==0) encoded.append("\r\n");
                     break;
            }
            state = (state < 3 ? state+1 : 1);
    	}

    	/* finalize */
    	switch (state) {
	    case 2:
             	 output[1] = MBM.charAt((restbits << 4));
                 output[2] = output[3] = '=';
                 encoded.append(output);
                 break;
            case 3:
             	 output[2] = MBM.charAt((restbits << 2));
                 output[3] = '=';
		 encoded.append(output);
                 break;
    	}

	return encoded.toString();
    } // encode()

} // MBM

