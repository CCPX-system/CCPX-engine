<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*" import="model.Offer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html>
<head><H1>CCPX Project</H1>
</head>
<%
List<Offer> list=(List<Offer>)request.getAttribute("list");
%>
<body>
<h2 align="center">showRecommendationResultsList</h2>
<table width="700px" cellpadding="1" cellspacing="1" align="center" border="1">
				<tr style="font-size:30px"><td>USER_ID</td><td>SELLER_FROM</td><td>SELLER_TO</td><td>POINTSFROM</td><td>POINTSTOMIN</td></tr>
				<c:forEach items="${list}" var="list" >
				<tr bgcolor="#EEEEE0">
					<td><c:out value="${list.user_id }"></c:out></td>
					<td><c:out value="${list.seller_from}"></c:out></td>
					<td><c:out value="${list.seller_to}"></c:out></td>					
					<td><c:out value="${list.points_from}"></c:out></td>
					<td><c:out value="${list.points_to_min}"></c:out></td>
				</tr>				
				</c:forEach>
			</table>						
</body>
</html>



