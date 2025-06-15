<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/search.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>${title}찾기</title>
<script type="text/javascript">
   function sendclose() {
	   opener.document.loginform.userid.value='${result}';
	   self.close();
   }
</script></head>
<body><table>
  <tr><th>${title} : </th><td>${result}</td></tr>
  <tr><td colspan="2">
     <c:if test="${title=='아이디'}">
        <input type="button" value="아이디전송" onclick="sendclose()"></c:if>
     <c:if test="${title!='아이디'}">
      <input type="button" value="닫기" onclick="self.close()"></c:if></td></tr>
</table></body></html>