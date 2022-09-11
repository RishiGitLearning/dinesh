<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** AUDITRAIL RJN RAWMATERIAL *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
	String fDate=request.getParameter("FDATE");
	String tDate= request.getParameter("TDATE");
	String Dept = request.getParameter("DEPT");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/rpt_Pending_Indent_Both.jasper");

	Map parameters = new HashMap();

	parameters.put("COMPANY", new Integer(CompanyID));
	parameters.put("FROM_DATE",fDate);
	parameters.put("TO_DATE",tDate);
      parameters.put("DEPT",new Integer(Dept));

					
	
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
