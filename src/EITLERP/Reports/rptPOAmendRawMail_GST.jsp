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
	String Approver=request.getParameter("APPROVER");
	String File=request.getParameter("File");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/rpt_PUR_PO_AMEND_RAW_GST.jasper");

	Map parameters = new HashMap();

	parameters.put("comp_id", new Integer(CompanyID));
	parameters.put("amend_no", DocNo);
	parameters.put("APPROVER", Approver);


	byte[] bytes =
		JasperRunManager.runReportToPdf(
			reportFile.getPath(),
			parameters,
			tmpConn
			);
	FileOutputStream theFile=new FileOutputStream(new File(File));
	theFile.write(bytes);
	theFile.flush();
	theFile.close();
%>
