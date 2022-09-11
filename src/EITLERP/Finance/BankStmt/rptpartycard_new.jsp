<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** INDENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
        String FromDate=request.getParameter("FromDate");
        String stype=request.getParameter("stype");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

        File reportFile=null;
	if(stype.equals("1")){            
            reportFile = new File("/EITLERPReports/finance/CHEQUEISSUED.jasper");
            }
        else if(stype.equals("2")){            
            reportFile = new File("/EITLERPReports/finance/WEACCOUNTED.jasper");
            }
        else if(stype.equals("3")){            
            reportFile = new File("/EITLERPReports/finance/bankDEBITED.jasper");
            }
        else if(stype.equals("4")){
               reportFile = new File("/EITLERPReports/finance/bankcredited.jasper");
            }

       Map parameters = new HashMap();
       parameters.put("asondate", FromDate);

	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,tmpConn);
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>

