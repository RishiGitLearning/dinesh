<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	//***** FELT NEEDLING REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");

	String productionDate=request.getParameter("PROD_DATE");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/Production/rptFeltNeedling.jasper");

	Map parameters = new HashMap();

	parameters.put("PROD_DATE", productionDate);

	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, tmpConn);

	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
