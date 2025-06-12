<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/item/delete.jsp --%>    
<!DOCTYPE html>
<html><head><meta charset="UTF-8">
<title>상품 삭제 전 확인</title></head>
<body>
<h2>상품 삭제 전 확인</h2>
<table>
  <tr><td><img src="../img/${item.pictureUrl}"></td>
  <td><table>
    <tr><td>상품명</td><td>${item.name}</td></tr>
    <tr><td>가격</td><td>${item.price}</td></tr>
    <tr><td>상품설명</td><td>${item.description}</td></tr>
    <tr><td colspan="2">
      <form action="delete" method="post">
         <input type="hidden" name="id" value="${item.id}">
         <input type="submit" value="상품삭제">
         <input type="button" value="상품목록" onclick="location.href='list'">
      </form>
    </td></tr></table></td></tr></table></body></html>