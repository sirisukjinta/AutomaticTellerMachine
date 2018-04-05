<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<article id="dispense">
	<table>
		<tr>
		<td>&nbsp;</td>
		<td>
	<div id="main-page">
		<div>
			<table class="noborder"><tr><td>
			<form:form id="dispenseEntry" name="dispenseEntry" commandName="dispenseEntry">
			<table class="noborder">  
				<tr>
					<td class="iReport" valign="top">
						<table class="noborder" style="table-layout:fixed">
							<tbody>
								<tr height="10">
									<td></td>
								</tr>
								<tr>
									<td><span id="globalError_error" class="required"></span></td>
								</tr>
								<tr>
									<td>
									<div>
										<table class="noborder">
											<tr height="25">
												<td align="right"><b>Amount: </b></td>
												<td><form:input path="amount" cssClass="textBox" maxlength="5" />&nbsp;<font color="red">*</font></td>
											</tr>
											<tr height="25"><td colspan="2" height="9"></td></tr>
											<tr>
												<td colspan="2" height="9" align="center">
													<input type="button" name="btn_submit" class="buttonSubmit" onclick="dispenseAction(); return false;"> 
													<input type="reset" name="btn_reset" value="" class="buttonCancel">
												</td>
											</tr>
										</table>
									</div>
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
		</form:form>
		</td></tr></table>
		</div>
	</div>
	</td>
	<td>&nbsp;</td>
	</tr>
	</table>
</article>
<content tag="javascript-include">
   <script src="${pageContext.request.contextPath}/content/js/atm/dispense.js" type="text/javascript" charset="utf-8"></script>
</content>