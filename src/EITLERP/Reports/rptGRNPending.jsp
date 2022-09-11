<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** INDENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
	String FDate=request.getParameter("FDate");
        String TDate=request.getParameter("TDate");
        String GRN_TYPE=request.getParameter("GrnType");
        int ItemType = Integer.parseInt(request.getParameter("ItemType"));
        
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");


	File reportFile = new File("/EITLERPReports/rpt_GRN_Pending.jasper");

	Map parameters = new HashMap();

	parameters.put("COMPANY_ID",new Integer(CompanyID));
	parameters.put("FDATE",FDate);
	parameters.put("TDATE",TDate);
        parameters.put("GRN_TYPE",GRN_TYPE);
        parameters.put("ITEM_TYPE",new Integer(ItemType));
						
	
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
