<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%
	//***** Freight Calculation Report*******//
	System.setProperty("-Djava.awt.headless","true");
	String dbURL=request.getParameter("dbURL");
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
	String FromDate=request.getParameter("FromDate");
	String ToDate=request.getParameter("ToDate");
	String Condition=request.getParameter("Condition");
	String FinFromDate=request.getParameter("FinFromDate");
	Condition=Condition.replace('~','(');
	Condition=Condition.replace('^',')');
        if(Condition.length()==0)
            {
                Condition = "AND B.COST_CENTER_ID NOT LIKE '4%'";
                }
        
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");
	File reportFile = new File("/EITLERPReports/rpt_STORE_ISSUE_COSTCENTERWISE5Ex.jasper");
	Map parameters = new HashMap();
	parameters.put("FROM_DATE", FromDate);
	parameters.put("TO_DATE",ToDate);
	parameters.put("CONDITION",Condition);
	parameters.put("FIN_FROM_DATE",FinFromDate);
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
