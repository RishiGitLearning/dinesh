<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Properties" %>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.activation.*" %>
<%@ page import="com.sun.mail.smtp.*" %>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<%
	try{
	//Read the Parameters passed
	System.setProperty("-Djava.awt.headless","true");
	String dbURL=request.getParameter("dbURL");//Database URL
	int DocNo=Integer.parseInt(request.getParameter("DocNo"));  //Document No. Doc Mail which contains all the information
	
	//=========== Variable Declaration ===============//
	HashMap recList=new HashMap();  //will contain list of receipients
	Connection tmpConn;
	Statement stHeader,stList,stTmp;
	ResultSet rsHeader,rsList,rsTmp;

	String MailText="";
	//String smtpHost=EITLERPGLOBAL.SMTPHostIP;
        //String smtpHost="184.106.240.198";
        //String smtpHost="192.237.247.26";
        String smtpHost="34.206.245.89";
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

	if(rsHeader.getRow()>0){
		MailText=rsHeader.getString("DESCRIPTION");
		From=rsHeader.getString("FROM");
		Subject=rsHeader.getString("SUBJECT");
		CC=rsHeader.getString("CC");
		BCC=rsHeader.getString("BCC");

		//Now populate list of email recipient
		stList=tmpConn.createStatement();
		rsList=stList.executeQuery("SELECT * FROM D_COM_MAIL_LIST WHERE DOC_NO="+DocNo);
		rsList.first();

		if(rsList.getRow()>0){
			while(!rsList.isAfterLast()){
			String email=rsList.getString("EMAIL");
			recList.put(Integer.toString(recList.size()+1),email);
			rsList.next();
			}
		}
	}
	//========= We have extracted the data ==================//


	//============= Now EMail the file as an attachement to all recipients ===========//
   	for(int i=1;i<=recList.size();i++){
	String to=(String)recList.get(Integer.toString(i));

        // Get system properties
        Properties props = System.getProperties();

        // Setup mail server
        props.put("mail.smtp.host", smtpHost);
	props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.port","465");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback","false"); 

        // Get session
        Session Objsession =Session.getInstance(props);

        // Define message
	MimeMessage message = new MimeMessage(Objsession);
 	message.setFrom(new InternetAddress(From));

	message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

	if(!CC.trim().equals("")){
	  message.addRecipient(Message.RecipientType.CC,new InternetAddress(CC));
	}

	if(!BCC.trim().equals("")){
	  message.addRecipient(Message.RecipientType.BCC,new InternetAddress(BCC));
	}

        message.setSubject(Subject);
        message.setContent(MailText,"text/html");
        
        // Send message
	Transport tr=Objsession.getTransport("smtp");
        //tr.connect(smtpHost,"felts@dineshmills.com","!@#$flt_123#");
        tr.connect(smtpHost,465,"felts@dineshmills.com","!@#$flt_123#");  
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
	}
	//===============================================================================//
	out.println("<HR><B>E-Mail Sent Successfully</B><HR>");
	}catch(Exception e){
	out.println("Error occured while sending mail. "+e.getMessage());
	out.println(e);
}
%>
