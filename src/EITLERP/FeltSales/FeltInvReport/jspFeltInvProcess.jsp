
<%@ page import="EITLERP.FeltSales.FeltInvReport.*" %>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.awt.*" %>


<%
    
    try{
	//Read the Parameters passed
	System.setProperty("java.awt.headless","true");
        clsFeltJSPInvProcess tc = new clsFeltJSPInvProcess();
        out.print("Process Start");
        tc.ProcessJSPInvoice();

        String dbURL=request.getParameter("dbURL");
        String curDate=request.getParameter("CUR_DATE");
        String curTime=request.getParameter("CUR_TIME");
        String curDateDB=request.getParameter("CUR_DATE_DB");
        
        String lotNo = tc.LotNo();
        
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/FeltSales/FeltPostInvReason.jasper");

	Map parameters = new HashMap();
	parameters.put("RUNDATE", curDate + " " + curTime);
        parameters.put("FROM_DATE", curDate);
        parameters.put("TO_DATE", curDate);
        parameters.put("PRO_FROM_DATE", curDateDB);
        parameters.put("PRO_TO_DATE", curDateDB);
        parameters.put("LOT_NO", lotNo);


	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, tmpConn);

	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();

    } catch (Exception e) {
        out.println("Error : "+e.getMessage());
        out.println(e);
    }
%>