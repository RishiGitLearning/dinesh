<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	//***** FELT EXPORT(REAL AND DEEMED) INVOICE *******//
	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
	String invoiceNo=request.getParameter("INV_NO");
        String invoiceDate=request.getParameter("INV_DATE");
        String amountWord=request.getParameter("AMT_WORD");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = null;        
        if(invoiceNo.charAt(1)=='7'){ 
          reportFile = new File("/EITLERPReports/Production/rptFeltDeemedExportInvoice.jasper");
          }
        else if(invoiceNo.charAt(1)=='8'){
          reportFile = new File("/EITLERPReports/Production/rptFeltRealExportInvoice.jasper");
          }

	Map parameters = new HashMap();
	parameters.put("INV_NO", invoiceNo);
        parameters.put("INV_DATE", invoiceDate);
        parameters.put("AMT_WORD", amountWord);

	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, tmpConn);
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
