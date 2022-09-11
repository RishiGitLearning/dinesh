<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        String schemeID=request.getParameter("SCHEME_ID");
        String reportID=request.getParameter("REPORT_ID");
        String runDate=request.getParameter("RUN_DATE");
        String runBy=request.getParameter("RUN_BY");
        
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	String reportFileName="";
        if(reportID.equals("1")) reportFileName="/EITLERPReports/rptKhakiEligiblityCheck.jasper";
        else if(reportID.equals("2")) reportFileName="/EITLERPReports/rptRetailEligiblityCheck.jasper";
        else if(reportID.equals("3")) reportFileName="/EITLERPReports/rptBonanzaEligiblityCheck.jasper";
        else if(reportID.equals("4")) reportFileName="/EITLERPReports/rptBonanzaDetail.jasper";
        else if(reportID.equals("5")) reportFileName="/EITLERPReports/rptKhakiSales.jasper";
        else if(reportID.equals("6")) reportFileName="/EITLERPReports/rptKhakiSalesAll.jasper";
        else if(reportID.equals("7")) reportFileName="/EITLERPReports/rptRetailSales.jasper";
        else if(reportID.equals("8")) reportFileName="/EITLERPReports/rptRetailSalesAll.jasper";
        else if(reportID.equals("9")) reportFileName="/EITLERPReports/rptSQLSales.jasper";
        else if(reportID.equals("10")) reportFileName="/EITLERPReports/rptSQLSalesAll.jasper";
        else if(reportID.equals("11")) reportFileName="/EITLERPReports/rptSPLSales.jasper";
        else if(reportID.equals("12")) reportFileName="/EITLERPReports/rptSPLSalesAll.jasper";
        else if(reportID.equals("13")) reportFileName="/EITLERPReports/rptDhanvarshaEligiblityCheck.jasper";
        else if(reportID.equals("14")) reportFileName="/EITLERPReports/rptDhanVarshaSales.jasper";
        else if(reportID.equals("15")) reportFileName="/EITLERPReports/rptDhanVarshaSalesAll.jasper";
        
        File reportFile = new File(reportFileName);
        
	Map parameters = new HashMap();
	parameters.put("SCHEME_ID", schemeID);
        parameters.put("RUN_DATE", runDate);
        parameters.put("RUN_BY", runBy);
        
	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,tmpConn);
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
