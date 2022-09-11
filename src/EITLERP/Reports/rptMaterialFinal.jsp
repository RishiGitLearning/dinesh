<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** SUMMARY REPORT *******//
	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        int COMPANY_ID=Integer.parseInt(request.getParameter("COMPANY_ID"));
	String PO_NO=request.getParameter("PO_NO");
	
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/rpt_Material_Final.jasper");

	Map parameters = new HashMap();
	parameters.put("COMPANY_ID", new Integer(COMPANY_ID));
	parameters.put("PO_NO", PO_NO);
	
	
	byte[] bytes = 
		JasperRunManager.runReportToPdf(
			reportFile.getPath(), 
			parameters, 
			tmpConn
			);
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
