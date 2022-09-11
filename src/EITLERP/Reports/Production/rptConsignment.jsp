<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** CONSIGNMENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
       // String FromDate=request.getParameter("CON_DATE");
       // String ToDate=request.getParameter("CON_DATE");
        String PartyCode=request.getParameter("CON_PARTY_CODE");
//	String InvoiceType=request.getParameter("InvoiceType");    
        out.println(ConsignmentDate);
    //    out.println(ProformaNo);
        String File=request.getParameter("File");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");


	File reportFile = new File("/EITLERPReports/Production/rptCONSIGNMENT.jasper");

	Map parameters = new HashMap();

//	parameters.put("COMPANY_ID", new Integer(CompanyID));
//	parameters.put("INVOICE_TYPE", InvoiceType);
    //    parameters.put("PROD_DATE", FromDate);
      //  Parameters.put("CON_DATE",txtFromDate.getText());
      //  Parameters.put("CON_DATE",txtToDate.getText());       

        Parameters.put("CON_PARTY_CODE",txtPartyCode.getText());    
    		
	
	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,tmpConn);
       //byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,data.getConn());
	response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        //FileOutputStream theFile=new FileOutputStream(new File("/root/Desktop/abc.pdf"));
	//theFile.write(bytes);
	//theFile.flush();
	//theFile.close();
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
