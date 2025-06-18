<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- /WEB-INF/views/admin/mail.jsp --%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>메일 보내기</title>
</head>
<body><h2>메일보내기</h2>
<form:form modelAttribute="mail" name="mailform"  action="mail" 
   enctype="multipart/form-data" >
   본인구글ID : <form:input  path="googleid" class="w3-input" />
   <form:errors path="googleid" class="w3-text-red"/><br>   
   본인구글밀번호 : <form:password path="googlepw" class="w3-input" />
   <form:errors path="googlepw" class="w3-text-red"/>
<table class="w3-table">
 <tr><td>보내는사람</td><td>${loginUser.email}</td></tr>
 <tr><td>받는사람</td>
 <td><form:input path="recipient" class="w3-input" /></td></tr>
 <tr><td>제목</td><td><form:input path="title"  class="w3-input" />
 <form:errors path="title" class="w3-text-red"/></td></tr>
 <tr><td>메시지형식</td><td><select name="mtype" class="w3-input">
  <option value="text/html; charset=utf-8">HTML</option>
  <option value="text/plain; charset=utf-8">TEXT</option></select></td></tr>
<tr><td>첨부파일1</td><td><input type="file" name="file1"></td></tr>
<tr><td>첨부파일2</td><td><input type="file" name="file1"></td></tr>
<tr><td colspan="2"><form:textarea path="contents" cols="120" 
    rows="10" class="w3-input" />
<form:errors path="contents" class="w3-text-red" style="color:red"/>
 </td></tr>
<tr><td colspan="2"><input type="submit" value="메일보내기"></td></tr>
</table></form:form>
<script type="text/javascript">
   $(function() {
	   $("#contents").summernote({
		   height:300,
		   placeholder : "메일로 전송할 내용을 입력해 주세요",
		   focus : true,
		   lang : "ko-KR",
		   toolbar :[   //image 버튼 제거 
			   ['style', ['bold', 'italic', 'underline', 'clear']],
			    ['font', ['strikethrough', 'superscript', 'subscript']],
			    ['fontsize', ['fontsize']],
			    ['color', ['color']],
			    ['para', ['ul', 'ol', 'paragraph']],
			    ['height', ['height']]			   
		   ]
	   })
   })
</script>
</body></html>