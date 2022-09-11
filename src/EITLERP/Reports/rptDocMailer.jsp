<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URL"%>
<%@page import="javax.management.j2ee.statistics.JavaMailStats"%>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Properties" %>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.activation.*" %>
<%@ page import="com.sun.mail.smtp.*" %>
<%
	try
	{


	//Read the Parameters passed
	System.setProperty("-Djava.awt.headless","true");
        //System.setProperty("javax.net.ssl.trustStore","$JAVA_HOME/jre/lib/security/cacerts");
        //System.setProperty("javax.net.ssl.trustStore","/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.121-0.b13.el7_3.x86_64/jre/lib/security/cacerts");
        //System.setProperty("javax.net.ssl.trustStorePassword","changeit");
        //System.setProperty("java.net.preferIPv4Stack","true");
        //System.setProperty("javax.net.ssl.trustStore","/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.121-0.b13.el7_3.x86_64/jre/lib/security/cacerts");
        
	String dbURL=request.getParameter("dbURL");   //Database URL
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID")); //Company ID
	int DocNo=Integer.parseInt(request.getParameter("DocNo"));  //Document No. Doc Mail which contains all the information
	String File=request.getParameter("File"); //File to attach with this mail

        out.println("File Print : "+File);
	//=========== Variable Declaration ===============//
	HashMap recList=new HashMap();  //will contain list of receipients
	Connection tmpConn;
	Statement stHeader,stList,stTmp;
	ResultSet rsHeader,rsList,rsTmp;

	String PONo="";
	String MailText="";
//	//String smtpHost=EITLERPGLOBAL.SMTPHostIP;
        String smtpHost="34.206.245.89";
        //String smtpHost="ssl://mx4.dineshmills.com";
        //String smtpHost="mx4.dineshmills.com";
	String Subject="";
	String From="";
	String CC="";
	String BCC="";

	//========= Database Stuff ==============//
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	//===== Start Extracting Information from D_COM_MAIL =======//
	stHeader=tmpConn.createStatement();
	rsHeader=stHeader.executeQuery("SELECT * FROM D_COM_MAIL WHERE DOC_NO="+DocNo);
	rsHeader.first();


	if(rsHeader.getRow()>0)
	{

		PONo=rsHeader.getString("MAIL_DOC_NO");
		MailText=rsHeader.getString("DESCRIPTION");
		From=rsHeader.getString("FROM");
		Subject=rsHeader.getString("SUBJECT");
		CC=rsHeader.getString("CC");
		BCC=rsHeader.getString("BCC");

		//Now populate list of email recipient
		stList=tmpConn.createStatement();
		rsList=stList.executeQuery("SELECT * FROM D_COM_MAIL_LIST WHERE DOC_NO="+DocNo);
		rsList.first();

		if(rsList.getRow()>0)
		{
			while(!rsList.isAfterLast())
			{
			String email=rsList.getString("EMAIL");
			recList.put(Integer.toString(recList.size()+1),email);
			rsList.next();
			}
		}
	}
	//========= We have extracted the data ==================//



	//============= Now EMail the file as an attachement to all recipients ===========//
   	for(int i=1;i<=recList.size();i++)
	{

            String to=(String)recList.get(Integer.toString(i));
            //JavaMail.SendMailwithAttachment("", MailText, Subject, "daxesh@dineshmills.com", File, PONo+".pdf");
            
            //MAIL USING PHP - START
            
            URL url = new URL("http://200.0.0.227/MAILING/index.php");
                Map<String,Object> params = new LinkedHashMap<String,Object>();
                params.put("from_mailid", "purchase@dineshmills.com");
                params.put("to_mailid", to);
                params.put("cc", CC);
                params.put("subject", Subject);
                params.put("message", MailText);
                out.println("File Path : "+"/usr/local/glassfish-4.1/glassfish/domains/domain1/config/"+File);
                params.put("attachment_path", "/usr/local/glassfish-4.1/glassfish/domains/domain1/config/"+File);                
                params.put("attachment_name", PONo+".pdf");
                //purchase@dineshmills.com","purchase1234!@#$"
                params.put("sender_username","purchase@dineshmills.com");
                params.put("sender_password","K.0-H%dmc20ks.00"); //purchase1234!@#$
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
                
                String response1 = "";
                for (int c; (c = in.read()) >= 0;)
                    response1 = response1 + ((char)c);
            
                //out.println(response1);
            //MAIL USING PHP - STOP
            
            
            /*
            // Get system properties
            Properties props = System.getProperties();

            // Setup mail server

            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", "true");

            props.put("mail.smtp.port","465");        
            props.put("mail.smtp.socketFactory.port","465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

            //props.put("mail.smtp.ssl.trust", "*");                
            props.put("mail.smtp.socketFactory.fallback","false");         
            //props.put("mail.smtp.starttls.enable", "true");




            // Get session
            Session Objsession =
            Session.getInstance(props); 

            // Define message
            MimeMessage message = new MimeMessage(Objsession);
            message.setFrom(new InternetAddress(From));


            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

            if(!CC.trim().equals(""))
            {
              message.addRecipient(Message.RecipientType.CC,new InternetAddress(CC));
            }

            if(!BCC.trim().equals(""))
            {
              message.addRecipient(Message.RecipientType.BCC,new InternetAddress(BCC));
            }

            message.setSubject(Subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(MailText);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);


            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(File);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(File);
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            message.setContent(multipart);

            // Send message
            Transport tr=Objsession.getTransport("smtp");
            //tr.connect(smtpHost,"purchase@dineshmills.com","purchase1234!@#$");        
            //tr.connect(smtpHost,465,"purchase@dineshmills.com","purchase1234!@#$");        
            tr.connect(smtpHost,465,"purchase@dineshmills.com","K.0-H%dmc20ks.00");        
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();
                    
            */        
            }
            //===============================================================================//
                    
            out.println("<HR><B>EMail sent successfully</B><HR>");

	}

catch(Exception e)
{
	out.println("Error occured while sending mail. "+e.getMessage());
	out.println(e);
}

%>
