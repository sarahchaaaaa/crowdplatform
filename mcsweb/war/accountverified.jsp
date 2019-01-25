<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Koios Platform</title>
    <!--script type="text/javascript" language="javascript" src="../hvr/hvr.nocache.js"></script-->
	<style>
	.button {
	    background-color: #0d1f36;
	    border: none;
	    color: white;
	    padding: 15px 32px;
	    text-align: center;
	    text-decoration: none;
	    display: inline-block;
	    font-size: 20px;
	    margin: 4px 2px;
	    cursor: pointer;
	}
	</style>
</head>
<body>
    <!--iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe-->
    
    <script>
		function loadHomepage() {
			try{
				window.history.pushState('page', 'Koios', '../');
				window.location.href = 'https://koiosplatform.com/mcsweb';
			}catch(error){
				window.alert(error.message);
			}
		}
	</script>
	<h1 style="text-align:center;">Koios: a context driven crowdsensing platform </h1>
	<hr/>
	<br/>
	<br/>
	<%
		int code = Integer.parseInt(request.getParameter("code"));
		if(code==0 || code==2){
	%>
		<div style="text-align:center;">
			<h3>Account Successfully Confirmed. </h3>
			<button class="button" onclick="loadHomepage()">Return to Login Page</button>
		</div>	
	<%
		} else if(code==1){
	%>
		<div style="text-align:center;">
			<h3>Invalid verification code.</h3>
			<button class="button" onclick="loadHomepage()">Return to Login Page</button>
		</div>		
	<%
		} else{
	%>
		<div style="text-align:center;">
			<h3>Service is not available, please try later.</h3>
		</div>		
	<%
		}
	%>

</body>
</html>