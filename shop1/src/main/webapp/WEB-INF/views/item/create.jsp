<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/item/create.jsp
     1. 파일업로드
     2. 유효성검증 : 입력값 검증. 백엔드에서 실행 
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<!DOCTYPE html>
<html><head><meta charset="UTF-8">
<title>상품등록</title>
</head>
<body>
<%-- enctype="multipart/form-data" : method="post"임 
     modelAttribute="item" : item 객체를 사용함.
 --%>
<form:form modelAttribute="item" action="create" 
           enctype="multipart/form-data">
<h2>상품등록</h2>
<table>
<tr><td>상품명</td>
<%-- <form:input path="name" /> : 
    <input id="name" name="name" type="text" value="${item.name}"/>--%>
    <td><form:input path="name" /></td>
    <td><font color="red"><form:errors path="name"/></font></td></tr>
<tr><td>상품가격</td>
    <td><form:input path="price" /></td>
    <td><font color="red"><form:errors path="price"/></font></td></tr>
<tr><td>상품이미지</td>
    <td colspan="2"><input type="file" name="picture"></td></tr>
<tr><td>상품설명</td>
    <td><form:textarea path="description" cols="20" rows="5" /></td>
    <td><font color="red"><form:errors path="description"/></font></td></tr>
<tr><td colspan="3"><input type="submit" value="상품등록">
  <input type="button" value="상품목록" onclick="location.href='list'"></td></tr>    
</table></form:form></body></html>