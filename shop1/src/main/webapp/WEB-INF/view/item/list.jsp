<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품목록</title>
</head>
<body>
<table>
	<thead>
		<tr>
			<th width="80">상품ID</th>
			<th width="320">상품명</th>
			<th width="100">가격</th>
		</tr>
	</thead>
	<c:forEach items="${itemList}" var="item">
		<tr>
			<td align="center">${item.id}</td>
			<td align="left"><a href="detail?id=${item.id}">${item.name}</a></td>
			<td align="right">${item.price}</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>