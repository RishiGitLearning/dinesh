<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** INDENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
       
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");


	File reportFile = new File("/iReport-5.5.0/jprint/report2.jrprint");
        
        jasperPrint jasperprint = (Jasperprint) JRLoader.loadObject(reportFile);

	//Map parameters = new HashMap();
				
	/*
	byte[] bytes = 
		JasperRunManager.runReportToPdf(
                 JasperRunManager.
			reportFile.getPath(), 
			parameters, 
			tmpConn
			);
        */
        
        
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ";");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperprint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.exportReport();
        output = baos.toByteArray();
        
        
        
	//response.setContentType("application/pdf");
	// response.setContentLength(bytes.length);
        
        response.setContentType("text/plain");
        response.setContentLength(output.length);       
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(output);
        
        
        
	//ServletOutputStream ouputStream = response.getOutputStream();
	//ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>


