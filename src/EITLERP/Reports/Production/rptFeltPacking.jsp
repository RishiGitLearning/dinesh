<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	//***** FELT PACKING REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        String baleNo=request.getParameter("BALE_NO");
	String baleDate=request.getParameter("BALE_DATE");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/Production/rptFeltPacking.jasper");

	Map parameters = new HashMap();
        parameters.put("BALE_NO", baleNo);
	parameters.put("BALE_DATE", baleDate);

	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, tmpConn);

	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
