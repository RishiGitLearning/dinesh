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
        int POType=Integer.parseInt(request.getParameter("POType"));
        //String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS?zeroDateTimeBehavior=convertToNull";
        //int CompanyID=2;
        //String DocNo="B078868";
        //int POType=1;
	Connection tmpConn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        tmpConn=DriverManager.getConnection(dbURL,"root","SdmlErp@227");
    File reportFile = new File("/EITLERPReports/rpt_PUR_PO_GEN_GST1.jasper");
                                                
	Map parameters = new HashMap();
	parameters.put("PO_NUM", DocNo);
        out.print(DocNo);
	parameters.put("COMPANY_ID", new Integer(CompanyID));
        out.print(String.valueOf(new Integer(CompanyID)));
        parameters.put("PO_TYPE", new Integer(POType));
        out.print(String.valueOf(new Integer(POType)));
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
