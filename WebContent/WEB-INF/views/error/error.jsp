<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<article id="error" class="nopad">
	<div id="main-page">

		<table class="noborder">
			<tr>
				<td width="2%">&nbsp;</td>
				<td>
					<div class="tabNav">
						<table class="noborder" style="table-layout: fixed">
							<tr>
								<td align="left">
									<ul id="tabList">
										<li class="current"><a class="tabLink" id='1' href="#"><img
												src="${contextPath}/content/img/arrowOr.gif" border="0">&nbsp;<font
												color="#FF9900">Error&nbsp;&nbsp;</font></a></li>
									</ul>
								</td>
							</tr>
						</table>
					</div>
					<div>&nbsp;</div>
					<div class="errorBox">
						<div class="errorHeader">ERROR:</div>
						<div class="errorContent">Application has encountered an error for URL: ${url}</div>
					</div>
				</td>
				<td width="2%">&nbsp;</td>
			</tr>
		</table>
		<!-- 
	Failed URL: ${url}
	Exception: ${exception.message}
	<c:forEach items="${exception.stackTrace}" var="ste">
	${ste}</c:forEach>
	-->
	</div>
</article>