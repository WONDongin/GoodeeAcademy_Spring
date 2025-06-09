<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니</title>
</head>
<body>
	<h2>장바구니</h2>
	<table>
		<tr>
			<td colspan="4">장바구니 상품 목록</td>
		</tr>
		<tr>
			<td>상품명</td>
			<td>가격</td>
			<td>수량</td>
			<td>합계</td>
		</tr>
		<%-- 장바구니 리스트 --%>
		<c:forEach items="${cart.itemSetList}" var="set" varStatus="stat">
			<tr>
				<td>${set.item.name }</td>
				<td><fmt:formatNumber value="${set.item.price}" pattern="###,###" /></td>
				<td><fmt:formatNumber value="${set.quantity}" pattern="###,###" /></td>
				<td>
					<fmt:formatNumber value="${set.quantity * set.item.price}" pattern="###,###" />
					<a href="cartDelete?index=${stat.index}">ⓧ</a>
				</td>
			</tr>
		</c:forEach>
		
		<tr>
			<td colspan="4" align="right">총 구입 금액 : 
				<fmt:formatNumber value="${cart.total}" pattern="###,###" /> 원
			</td>
		</tr>
	</table>
	<br/>
	${message}
	<br/>
	<a href="../item/list">상품목록</a>
	<a href="checkout">주문하기</a>
</body>
</html>