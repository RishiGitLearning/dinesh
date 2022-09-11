<%@ page import="EITLERP.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>

<%

	clsUser a=new clsUser();

	out.println("created object of user. Done");

	/*String dbURL=request.getParameter("dbURL");
        int CompanyID=Integer.parseInt(request.getParameter("CompanyID"));
	String DocNo=request.getParameter("DocNo");

	String reportFile = "/EITLERP Report Source Ex/theTest.jasper";

	HashMap parameters = new HashMap();
	parameters.put("COMPANY_ID", new Integer(CompanyID));
	parameters.put("APPROVAL_NO", DocNo);

	String result=a.RunReport(reportFile,dbURL,parameters);

	out.println("result of call : "+result);*/

%>
