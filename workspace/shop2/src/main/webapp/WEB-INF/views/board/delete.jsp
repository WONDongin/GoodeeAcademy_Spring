<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%-- /WEB-INF/views/board/delete.jsp --%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%--
   삭제 구현하기
    1. Controller
       비밀번호가 일치하면 num 해당하는 게시물 삭제.
       비밀번호 오류시 globalError 방식으로 처리하기
    2. service.boardDelete 
       boardDao.delete 메서드 명으로 처리하기   
 --%>
<title>게시판 삭제 화면</title></head>
<body>
<form action="delete"  method="post"  name="f">
<spring:hasBindErrors name="board">
	<font color="red"><c:forEach items="${errors.globalErrors}"
	var="error"><spring:message code="${error.code }" /></c:forEach>
	</font></spring:hasBindErrors>
<input type="hidden" name="num" value="${param.num}">
<input type="hidden" name="boardid" value="${param.boardid}">
<table class="w3-table-all">
    <caption>${boardName}글 삭제 화면</caption>
    <tr><td>제목</td><td>${board.title}</td></tr>
	<tr><td>게시글비밀번호</td>
		<td><input type="password" name="pass" class="w3-input w3-border" /></td></tr>
	<tr><td colspan="2">
<a href="javascript:document.f.submit()">[게시글삭제]</a></td></tr>
</table></form></body></html>