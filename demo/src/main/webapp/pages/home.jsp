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
<form style="padding-top: 20px; padding-right: 20px; padding-bottom: 20px; padding-left: 40px" method="POST" action="uploadFile" enctype="multipart/form-data">
        File1 to upload: <input type="file" name="file"><br /> 
        <br />
        WaitTime(Optional): <input type="text" name="name"><br /> <br /> 
        
		<label><input type="checkbox" name ="parallel" value = "parallel"> Parallel</label>
		
		<label><input type="checkbox" name ="UI" value = "UI"> UI</label>
	
		<label><input type="checkbox" name ="Service" value = "Service"> Service</label>
	
        
        <input type="submit" value="Upload"> Press here to upload the input file!
    </form>
</body>
</html>

