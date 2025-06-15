<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/user/pwsearch.jsp--%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
</head><body><h3>비밀번호찾기</h3>
<form:form modelAttribute="user"  action="pwsearch" method="post">
  <spring:hasBindErrors name="user">
    <font color="red"><c:forEach items="${errors.globalErrors}" var="error">
         <spring:message code="${error.code}" />
      </c:forEach></font></spring:hasBindErrors>
  <table>
     <tr><th>아이디</th><td><input type="text" name="userid">
     <font color="red"><form:errors path="userid" /></font></td></tr>
     <tr><th>이메일</th><td><input type="text" name="email">
     <font color="red"><form:errors path="email" /></font></td></tr>
     <tr><th>전화번호</th><td><input type="text" name="phoneno">
     <font color="red"><form:errors path="phoneno" /></font></td></tr>
     <tr><td colspan="2" class="w3-center">
      <input type="submit" value="비밀번호찾기"></td></tr>
  </table></form:form></body></html>