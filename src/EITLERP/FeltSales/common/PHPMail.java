/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author root
 */
public class PHPMail {
    
    public String to_mailid;
    public String cc;
    public String subject;
    public String message;
    public String attachment_path="";
    public String attachment_name="";
    
    public String SendMail_usingPHP()
    {
        try{
            
            if(to_mailid.equals(""))
            {
                return "To Mail Id is EMPTY, Please set first";
            }
            if(subject.equals(""))
            {
                return "Subject is EMPTY, Please set first";
            }
            if(message.equals(""))
            {
                return "Message is EMPTY, Please set first";
            }
            //attachment_path = "/root/Desktop/proforma.pdf";
            //attachment_name = "proforma.pdf";
//            if(attachment_path.equals(""))
//            {
//                return "Attachment Path is EMPTY, Please set first";
//            }
//            if(attachment_name.equals(""))
//            {
//                return "Attachment Name is EMPTY, Please set first";
//            }
            
               // URL url = new URL("http://200.0.0.230/MAILING/index.php");
               URL url = new URL("http://200.0.0.227/MAILING/index.php");
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("from_mailid", "sdmlerp@dineshmills.com");
                params.put("to_mailid", to_mailid);
                params.put("cc", cc);
                params.put("subject", subject);
                params.put("message", message);
                params.put("attachment_path", attachment_path);
                params.put("attachment_name", attachment_name);
                
                params.put("sender_username","sdmlerp@dineshmills.com");
                params.put("sender_password","K.0-H%dmc20ks.00");   //Sdml@390020
                params.put("host","34.206.245.89");
                params.put("port","25");
                //''
                //'proforma.pdf'
                //""
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                
                String response = "";
                for (int c; (c = in.read()) >= 0;)
                    response = response + ((char)c);
                    
                return response;
                
        }catch(Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
    }
    
    public String SendMailFeltOC_usingPHP() {
        try{
            
            if(to_mailid.equals(""))
            {
                return "To Mail Id is EMPTY, Please set first";
            }
            if(subject.equals(""))
            {
                return "Subject is EMPTY, Please set first";
            }
            if(message.equals(""))
            {
                return "Message is EMPTY, Please set first";
            }
            //attachment_path = "/root/Desktop/proforma.pdf";
            //attachment_name = "proforma.pdf";
//            if(attachment_path.equals(""))
//            {
//                return "Attachment Path is EMPTY, Please set first";
//            }
//            if(attachment_name.equals(""))
//            {
//                return "Attachment Name is EMPTY, Please set first";
//            }
            
                URL url = new URL("http://200.0.0.227/MAILING/index.php");
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("from_mailid", "feltoc@dineshmills.com");
                params.put("to_mailid", to_mailid);
                params.put("cc", cc);
                params.put("subject", subject);
                params.put("message", message);
                params.put("attachment_path", attachment_path);
                params.put("attachment_name", attachment_name);
                
                params.put("sender_username","feltoc@dineshmills.com");
                params.put("sender_password","Feltoc@Sdm1");
                params.put("host","34.206.245.89");
                params.put("port","25");
                //'' feltoa@dineshmills.com
                //'proforma.pdf'  Feltoa@Sdm1
                //""
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                
                String response = "";
                for (int c; (c = in.read()) >= 0;)
                    response = response + ((char)c);
                    
                return response;
                
        }catch(Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
    }
    
    public String SendMailFeltOA_usingPHP() {
        try{
            
            if(to_mailid.equals(""))
            {
                return "To Mail Id is EMPTY, Please set first";
            }
            if(subject.equals(""))
            {
                return "Subject is EMPTY, Please set first";
            }
            if(message.equals(""))
            {
                return "Message is EMPTY, Please set first";
            }
            //attachment_path = "/root/Desktop/proforma.pdf";
            //attachment_name = "proforma.pdf";
//            if(attachment_path.equals(""))
//            {
//                return "Attachment Path is EMPTY, Please set first";
//            }
//            if(attachment_name.equals(""))
//            {
//                return "Attachment Name is EMPTY, Please set first";
//            }
            
                URL url = new URL("http://200.0.0.227/MAILING/index.php");
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("from_mailid", "feltoa@dineshmills.com");
                params.put("to_mailid", to_mailid);
                params.put("cc", cc);
                params.put("subject", subject);
                params.put("message", message);
                params.put("attachment_path", attachment_path);
                params.put("attachment_name", attachment_name);
                
                params.put("sender_username","feltoa@dineshmills.com");
                params.put("sender_password","Feltoa@Sdm1");
                params.put("host","34.206.245.89");
                params.put("port","25");
                //'' feltoa@dineshmills.com
                //'proforma.pdf'  Feltoa@Sdm1
                //""
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                
                String response = "";
                for (int c; (c = in.read()) >= 0;)
                    response = response + ((char)c);
                    
                return response;
                
        }catch(Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
    }
    
    public String SendMailFelts_usingPHP() {
        try{
            
            if(to_mailid.equals(""))
            {
                return "To Mail Id is EMPTY, Please set first";
            }
            if(subject.equals(""))
            {
                return "Subject is EMPTY, Please set first";
            }
            if(message.equals(""))
            {
                return "Message is EMPTY, Please set first";
            }
            //attachment_path = "/root/Desktop/proforma.pdf";
            //attachment_name = "proforma.pdf";
//            if(attachment_path.equals(""))
//            {
//                return "Attachment Path is EMPTY, Please set first";
//            }
//            if(attachment_name.equals(""))
//            {
//                return "Attachment Name is EMPTY, Please set first";
//            }
            
                URL url = new URL("http://200.0.0.227/MAILING/index.php");
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("from_mailid", "felts@dineshmills.com");
                params.put("to_mailid", to_mailid);
                params.put("cc", cc);
                params.put("subject", subject);
                params.put("message", message);
                params.put("attachment_path", attachment_path);
                params.put("attachment_name", attachment_name);
                
                params.put("sender_username","felts@dineshmills.com");
                params.put("sender_password","Felts@123#");
                params.put("host","34.206.245.89");
                params.put("port","25");
                //'' feltoa@dineshmills.com
                //'proforma.pdf'  Feltoa@Sdm1
                //""
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                
                String response = "";
                for (int c; (c = in.read()) >= 0;)
                    response = response + ((char)c);
                    
                return response;
                
        }catch(Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
    }
}
