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
	String dbURL=request.getParameter("dbURL");   //Database URL
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID")); //Company ID
	String AsOnDate=request.getParameter("Date");  //Document No. Doc Mail which contains all the information
	long JobSrNo=Long.parseLong(request.getParameter("JobSrNo"));
        String File="FortnightStock.pdf";
	String CompanyName=request.getParameter("CompanyName");
        
        
	//=========== Variable Declaration ===============//
	HashMap recList=new HashMap();  //will contain list of receipients
	Connection tmpConn;
	Statement stJob;
	ResultSet rsJob;

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

        //=======Delete Old Records===============//
        stJob=tmpConn.createStatement();
	rsJob=stJob.executeQuery("SELECT * FROM D_COM_SCHEDULE_JOBS WHERE SR_NO="+JobSrNo);
	rsJob.first();
        
	if(rsJob.getRow()>0)
	{
                
		MailText=rsJob.getString("CONTENT");
		From="noreply@dineshmills.com";
		Subject=rsJob.getString("SUBJECT");

                String SendTo=rsJob.getString("SEND_TO");
                
                if(!SendTo.trim().equals(""))
                {
                   String[] SendToList=SendTo.split(",");
                   
                   for(int l=0;l<SendToList.length;l++)
                   {
                     recList.put(Integer.toString(recList.size()+1),SendToList[l]);  
                   }
                }
                
	}
	//========= We have extracted the data ==================//


        //========== Generate the Report and make pdf file ============//
	File reportFile = new File("/EITLERPReports/rptFortnightStock.jasper");

	Map parameters = new HashMap();
	
	parameters.put("COMPANY_NAME",CompanyName);
	parameters.put("AS_ON_DATE", AsOnDate);

	byte[] bytes =
		JasperRunManager.runReportToPdf(
			reportFile.getPath(),
			parameters,
			tmpConn
			);

	FileOutputStream theFile=new FileOutputStream(new File(File));
	theFile.write(bytes);
	theFile.flush();
	theFile.close();
        //=============================================================//

	//============= Now EMail the file as an attachement to all recipients ===========//
   	for(int i=1;i<=recList.size();i++)
	{

	String to=(String)recList.get(Integer.toString(i));

        // Get system properties
        Properties props = System.getProperties();

        // Setup mail server
        props.put("mail.smtp.host", smtpHost);
	props.put("mail.smtp.auth", "true");
        

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
        tr.connect(smtpHost,"eitl@dineshmills.com","123456");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
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
