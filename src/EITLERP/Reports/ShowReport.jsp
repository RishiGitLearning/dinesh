<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<html>
<head>
<title>EITLERP Print Preview</title>
</head>
<body>
<%

//Read the filename from the parameter
String pdfFile=request.getParameter("File");

//Open the File
File theFile=new File(pdfFile);

InputStream is = new FileInputStream(theFile);

// Get the size of the file
long length = theFile.length();

byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Close the input stream and return bytes
        is.close();
        

	response.setContentType("application/pdf");
	response.setContentLength(bytes.length);
	ServletOutputStream ouputStream = response.getOutputStream();
	ouputStream.write(bytes, 0, bytes.length);
	ouputStream.flush();
	ouputStream.close();


%>
</body>
</html>

