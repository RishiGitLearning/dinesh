<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** INDENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");
        System.out.println("Phase1");
	String dbURL=request.getParameter("dbURL");
        
        String AsOnDate=request.getParameter("AsOnDate");
	String SaleType=request.getParameter("SaleType");
        String UserId=request.getParameter("UserId");
        
        System.out.println("Phase2");
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");


	File reportFile = new File("/EITLERPReports/finance/rptDebtorsOutstanding.jasper");

	Map parameters = new HashMap();

	
	parameters.put("AS_ON_DATE", AsOnDate);
        parameters.put("SALE_TYPE", SaleType);
        parameters.put("USER_ID", UserId);
        				
	System.out.println("Phase3");
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
