<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="author" content="Thomson Reuters" />
		<meta name="description" content="Thomson Reuters is the world's leading source of intelligent information for businesses and professionals." />
		<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
	
		<title>Automatic Teller Machine</title>
	
		<link href="${pageContext.request.contextPath}/content/css/baseline.css" rel="stylesheet" media="screen, projection" type="text/css" />
		<link href="${pageContext.request.contextPath}/content/css/jquery-ui-1.11.3.custom.min.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/content/css/application.css" rel="stylesheet" type="text/css" />

		<script>window.pageContext = '${pageContext.request.contextPath}';</script>
		<script src="${pageContext.request.contextPath}/content/js/general.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath}/content/js/jquery-1.11.0.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath}/content/js/jquery-ui-1.11.3.custom.min.js" type="text/javascript" charset="utf-8"></script>
	
		<!--[if lt IE 9]>
		<script src="${pageContext.request.contextPath}/content/js/html5shiv-3.7.2.min.js"></script>
		<![endif]-->
	</head>
	<body>
		<div id="wrap" class="clearfix">
			<div id="header">
				<div id="nav"></div>
			</div>
			<div id="content">
			<decorator:body />
			</div>
	
			<footer id="footer-wrap">
				<div id="footer"></div>
			</footer>
		</div>

		<decorator:getProperty property="page.javascript-include" default=""/>
 
	</body>
</html>
