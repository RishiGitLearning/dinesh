<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** INDENT REPORT *******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
    //    int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
//	String InvoiceType=request.getParameter("InvoiceType");
  //      String FromDate=request.getParameter("FromDate");
  //      String partycode=request.getParameter("partycode");
  //      String ToDate=request.getParameter("ToDate");
      String MainCode=request.getParameter("MainCode");
System.out.println (Maincode) ;

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");

        if (MainCode =="STOCK") {
	File reportFile = new File("/EITLERPReports/Production/felt_stock_sheet.jasper");
        }
        
        
        if (MainCode =="TARGET") {
	File reportFile = new File("/EITLERPReports/Production/RPT_FELT_TARGET.jasper");
        }
 
	Map parameters = new HashMap();

//	parameters.put("COMPANY_ID", new Integer(CompanyID));
//	parameters.put("INVOICE_TYPE", InvoiceType);
 //       parameters.put("PROD_DATE", FromDate);
  //      parameters.put("INVOICE_DATE_TO", ToDate);
  //     parameters.put("MAIN_ACCOUNT_CODE", MainCode);
//    parameters.put("PARTY", partycode);
		
	
	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,tmpConn);
	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
