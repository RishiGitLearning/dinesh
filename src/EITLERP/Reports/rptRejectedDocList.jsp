<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** SUMMARY REPORT *******//
	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
	String DocDateField=request.getParameter("DocDateField");
	String DocNoField=request.getParameter("DocNoField");
        String FromDate=request.getParameter("FromDate");
	String ToDate=request.getParameter("ToDate");
        String ModuleName=request.getParameter("ModuleName");
        String TableName=request.getParameter("TableName");
        int UserID=Integer.parseInt(request.getParameter("UserID"));
        String DBName = request.getParameter("DBName");
        String DBName1 = request.getParameter("DBName1");
        int ModuleID = Integer.parseInt(request.getParameter("ModuleID"));
        String CompName = request.getParameter("CompName");
        String LoginID = request.getParameter("LoginID");
        
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/rpt_rejected_docs.jasper");

	Map parameters = new HashMap();
	parameters.put("COMPANY_ID", new Integer(CompanyID));
	parameters.put("DOC_DATE", DocDateField);
	parameters.put("DOC_NO", DocNoField);
	parameters.put("FROM_DATE", FromDate);
	parameters.put("MODULE_NAME", ModuleName);
	parameters.put("TABLE_NAME", TableName);
	parameters.put("TO_DATE", ToDate);
	parameters.put("USER_ID", new Integer(UserID));
        parameters.put("TABLE_SCHEMA",DBName);
        parameters.put("TABLE_SCHEMA1",DBName1);
        parameters.put("MODULE_ID",ModuleID);
        parameters.put("COMPNAME",CompName);
        parameters.put("LOGIN_ID",LoginID);
        
	
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
