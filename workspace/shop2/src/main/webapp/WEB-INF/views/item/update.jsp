<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/item/update.jsp --%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 수정</title>
</head>
<body>
<form:form modelAttribute="item" action="update" 
           enctype="multipart/form-data">
<form:hidden path="id" /><form:hidden path="pictureUrl" />
<h2>상품 정보 수정</h2>
<table><tr><td>상품명</td><td><form:input path="name" /></td>
    <td><font color="red"><form:errors path="name" /></font></td></tr>
  <tr><td>상품가격</td><td><form:input path="price" /></td>
    <td><font color="red"><form:errors path="price" /></font></td></tr>    
  <tr><td>상품이미지</td><td><input type="file" name="picture" /></td>
    <td>${item.pictureUrl}</td>
  <tr><td>상품설명</td>
    <td><form:textarea path="description" cols="20" rows="5"/></td>
    <td><font color="red"><form:errors path="description" /></font></td></tr>
  <tr><td colspan="3"><input type="submit" value="수정등록">&nbsp;
    <input type="button" value="상품목록" onclick="location.href='list'">
  </td></tr></table></form:form></body></html>