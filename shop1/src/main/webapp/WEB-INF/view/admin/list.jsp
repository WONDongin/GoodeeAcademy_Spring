<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%-- /WEB-INF/view/admin/list.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 목록</title>
<script type="text/javascript">
   function allchkbox(allchk) {
	   $(".idchks").prop("checked",allchk.checked)
   }
</script>
<style>
   .noline { text-decoration: none;}
</style>
</head>
<body>
<h2>회원목록</h2>
<form action="mailForm" method="post">
  <table>
  <tr><th>아이디</th>
      <th>이름</th>
      <th>전화</th>
      <th>생일</th>
      <th>이메일</th>
  <th><input type="checkbox" name="allchk" onchange="allchkbox(this)"></th></tr>
  <c:forEach items="${list}" var="user">
  <tr><td>${user.userid}</td><td>${user.username}</td><td>${user.phoneno}</td>
  <td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd" /></td>
  <td>${user.email}</td><td>
  <a href="../user/update?userid=${user.userid}">수정</a>
  <a href="../user/delete?userid=${user.userid}">강제탈퇴</a>
  <a href="../user/mypage?userid=${user.userid}">회원정보</a></td>
  <td><input type="checkbox" name="idchks" class="idchks" value="${user.userid}"></td></tr>
  </c:forEach>
  <tr><td colspan="7"><input type="submit" value="메일보내기"></td></tr>
  </table></form></body></html>