<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.*" %>

<%@ page import="net.sf.jasperreports.engine.util.JRLoader" %>
<%@ page import="net.sf.jasperreports.engine.JRAbstractExporter.*" %>
<%@ page import="net.sf.jasperreports.engine.export.JRXlsAbstractExporter.*" %>
<%@ page import="net.sf.jasperreports.engine.JRDataSource" %>
<%@ page import="net.sf.jasperreports.engine.export.*" %>
<%@ page import="net.sf.jasperreports.engine.JRExporterParameter.*" %>
<%@ page import="net.sf.jasperreports.engine.data.*" %>

<%@ page import="net.sf.jasperreports.engine.JRException.*" %>
<%@ page import="net.sf.jasperreports.engine.JRPrintPage.*" %>
<%@ page import="net.sf.jasperreports.engine.JasperFillManager.*" %>
<%@ page import="net.sf.jasperreports.engine.JasperPrint.*" %>
<%@ page import="net.sf.jasperreports.engine.data.JRBeanArrayDataSource.*" %>
<%@ page import="net.sf.jasperreports.engine.export.JRXlsExporter.*" %>
<%@ page import="net.sf.jasperreports.engine.export.JRXlsExporterParameter.*" %>

<%

	//***** Freight Calculation Report*******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
	String AsOnDate=request.getParameter("AsOnDate");
	String AccNo=request.getParameter("AccNo");
	int Month=Integer.parseInt(request.getParameter("Month"));
        int Year=Integer.parseInt(request.getParameter("Year"));
	
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

	File reportFile = new File("/EITLERPReports/rptInterestStatement_MICRNo.jasper");

	Map parameters = new HashMap();

	parameters.put("ACC_NO", AccNo);
	parameters.put("ASON_DATE",AsOnDate);
	parameters.put("COMP_ID",new Integer(CompanyID));
        parameters.put("MONTH",new Integer(Month));
        parameters.put("YEAR",new Integer(Year));

        ServletContext context = this.getServletConfig().getServletContext();
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile.getPath());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, tmpConn);
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE); 
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE); 

        exporter.exportReport();
        System.out.println("Sixe of byte array:"+xlsReport.size());
        
        byte[] bytes = xlsReport.toByteArray();
        response.setContentType("application/vnd.ms-excel");
        response.setContentLength(bytes.length);
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length);
        ouputStream.flush();
        ouputStream.close();

%>
