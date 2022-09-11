<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Properties" %>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.activation.*" %>

<%
	try
	{
	
	//Read the Parameters passed

	System.setProperty("-Djava.awt.headless","true");
	String dbURL=request.getParameter("dbURL");   //Database URL
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID")); //Company ID
	int DocNo=Integer.parseInt(request.getParameter("DocNo"));  //Document No. Doc Mail which contains all the information

	//=========== Variable Declaration ===============//
	HashMap recList=new HashMap();  //will contain list of receipients
	Connection tmpConn;
	Statement stHeader,stList,stTmp;
	ResultSet rsHeader,rsList,rsTmp;

	String PONo="";
	String MailText="";
	String smtpHost="200.0.0.17";
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

		PONo=rsHeader.getString("MAIL_DOC_NO");;
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
		

	
	//====== Now generate report and export it to pdf file =========//
	File reportFile = new File("/EITLERPReports/rpt_PUR_Enquiry.jasper");
        String sourceFile="/EITLERPReports/rpt_PUR_PO_GEN.jasper";
	
	Map parameters = new HashMap();
	
	parameters.put("comp_id", new Integer(CompanyID));
	parameters.put("inquiry_no", PONo);
	
	
	// Exporting Report to pdf file
	// FileName convention : "doc"+ModuleID+DocNo+".pdf"
	String exportFile="doc"+"1"+PONo+".pdf";

	byte[] bytes = 
		JasperRunManager.runReportToPdf(
			reportFile.getPath(), 
			parameters, 
			tmpConn
			);

	FileOutputStream theFile=new FileOutputStream(new File(exportFile));
	theFile.write(bytes);
	theFile.flush();
	theFile.close();
	//========= Finished Exporting the file ========================//


	//============= Now EMail the file as an attachement to all recipients ===========//
   	for(int i=1;i<=recList.size();i++)
	{
 
	String to=(String)recList.get(Integer.toString(i));

        // Get system properties
        Properties props = System.getProperties();
        
        // Setup mail server
        props.put("mail.smtp.host", smtpHost);
        
        // Get session
        Session Objsession =
        Session.getDefaultInstance(props, null);
       	 
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
        DataSource source = new FileDataSource(exportFile);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(exportFile);
        multipart.addBodyPart(messageBodyPart);
        
        // Put parts in message
        message.setContent(multipart);
        
        
        // Send message
        Transport.send(message);

	}
	//===============================================================================//


	out.println("<HR><B>EMail sent successfully</B><HR>");

	}
catch(Exception e)
{
	out.println("Error occured while sending mail. "+e.getMessage());
}

%>
