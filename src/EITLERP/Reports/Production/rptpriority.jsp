<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** INDENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
    //    int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
//	String InvoiceType=request.getParameter("InvoiceType");
    //    String FromDate=request.getParameter("FromDate");
  //      String ToDate=request.getParameter("ToDate");
   //     String MainCode=request.getParameter("MainCode");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");


	File reportFile = new File("/EITLERPReports/Production/RPT_FELT_PRIORITY.jasper");

	Map parameters = new HashMap();

//	parameters.put("COMPANY_ID", new Integer(CompanyID));
//	parameters.put("INVOICE_TYPE", InvoiceType);
    //    parameters.put("PROD_DATE", FromDate);
  //      parameters.put("INVOICE_DATE_TO", ToDate);
    //    parameters.put("MAIN_ACCOUNT_CODE", MainCode);
		
	
	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,tmpConn);
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
