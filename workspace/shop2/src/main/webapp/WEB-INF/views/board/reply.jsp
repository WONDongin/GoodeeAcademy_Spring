<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<title>게시판 답글 쓰기</title>
	</head>
<body>
	<form:form modelAttribute="board" action="reply" method="post" name="f">
    <form:hidden path="num" /> <%-- 원글의 글번호 --%>
    <form:hidden path="boardid" /> <%-- 원글의 게시판종료 --%>
    <form:hidden path="grp" /> <%-- 원글의 그룹 --%>
    <form:hidden path="grplevel" /> <%-- 원글 답변글 레벨 --%>
    <form:hidden path="grpstep" /> <%-- 원글의 출력 순서 --%>
	<h2>${boardName} 답글 등록</h2>
	<table class="w3-table">
		<tr>
			<td>글쓴이</td><td><input type="text" name="writer">
		    	<font color="red"><form:errors path="writer" /></font>
		    </td>
		</tr>
		<tr>
			<td>비밀번호</td>
			<td><form:password path="pass" />
			 	<font color="red"><form:errors path="pass" /></font>
		 	</td>
		</tr>
		<tr>
			<td>제목</td>
			<td>
				<form:input path="title"  value="RE:${board.title}"/>
				<font color="red"><form:errors path="title" /></font>
			</td>
		</tr>
		<tr>
			<td>내용</td>
				<td><textarea name="content" rows="15" cols="80" id="summernote"></textarea>
				<font color="red"><form:errors path="content" /></font>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<a href="javascript:document.f.submit()">[답변글등록]</a>
			</td>
		</tr>
	</table>
</form:form>
	<script type="text/javascript">
	    $(function(){
	        $("#summernote").summernote({
	            height: 300,
	            callbacks : {
	                onImageUpload : function(images){
	                    for(let i = 0; i < images.length; i++){
	                        sendFile(images[i]);
	                    }
	                }
	            }
	        });
	    });
	
	    // file : images[i]
	    function sendFile(file){
	        // 파일 업로드를 위한 데이터 컨테이너 생성
	        let data = new FormData(); // ← 오타 수정됨
	        data.append("image", file); // 컨테이너에 이미지 객체 추가
	
	        $.ajax({
	            url : "/ajax/uploadImage",
	            type : "post",
	            data : data,
	            processData : false,
	            contentType : false,
	            success : function(src){
	                $("#summernote").summernote("insertImage", src); // ← 오타 수정됨
	            },
	            error : function(e){
	                alert("이미지 업로드 실패 : " + e.status);
	            }
	        });
	    }
	</script>
</body>
</html>