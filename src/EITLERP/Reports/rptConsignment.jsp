<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%
	
	//***** PROFORMA INVOICE*******//

	System.setProperty("-Djava.awt.headless","true");

	String dbURL=request.getParameter("dbURL");
    //    int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
//	String InvoiceType=request.getParameter("InvoiceType");
    //    String FromDate=request.getParameter("FromDate");
//        String ConsignmentDate=request.getParameter("ConsignmentDate");
      //  String ProformaNo=request.getParameter("ProformaNo");
        //String ProformaNo=request.getParameter("DocNo");
  //      out.println(ProformaNo);
        String File=request.getParameter("File");

	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");


	File reportFile = new File("/EITLERPReports/Production/CONSIGNMENT.jasper");

	Map parameters = new HashMap();

//	parameters.put("COMPANY_ID", new Integer(CompanyID));
//	parameters.put("INVOICE_TYPE", InvoiceType);
    //    parameters.put("PROD_DATE", FromDate);
  //     parameters.put("CON_DATE", ConsignmentDate);
     //  parameters.put("PROFORMA_DATE", ProformaDate);
		
	
	byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,tmpConn);
       //byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(),parameters,data.getConn());
	response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        //FileOutputStream theFile=new FileOutputStream(new File("/root/Desktop/abc.pdf"));
	//theFile.write(bytes);
	//theFile.flush();
	//theFile.close();
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();
%>
