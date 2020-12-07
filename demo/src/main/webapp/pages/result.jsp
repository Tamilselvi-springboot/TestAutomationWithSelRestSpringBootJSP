<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
    background-image: url("images/darkbluebackground.jpg");
}
.no-background {
    background-image: url("images/blank.jpg");
}
</style>
<meta charset="ISO-8859-1">
<title>Automation Tool</title>
</head>

<body>
<h1 >Welcome to Automation Tool!</h1> 
<form method="POST" action="downloadLogFile" enctype="multipart/form-data">
         
   Result : <input type="submit" value="Download Result"> Press here to download the input file!
    </form>
</body>

</html>

