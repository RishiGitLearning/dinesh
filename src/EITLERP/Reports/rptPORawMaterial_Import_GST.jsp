<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** INDENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
	String DocNo=request.getParameter("DocNo");
	String Approver=request.getParameter("Approver");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");


	File reportFile = new File("/EITLERPReports/rpt_PUR_Raw_Material_Import_GST.jasper");

	Map parameters = new HashMap();


	parameters.put("PO_NUM", DocNo);
	parameters.put("COMPANY_ID", new Integer(CompanyID));
	parameters.put("APPROVER", Approver);
	
	
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
