<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%-- /WEB-INF/views/board/update.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html><html><head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<script type="text/javascript">
    function file_delete() {
    	document.f.fileurl.value = ""
	    file_desc.style.display = "none";
    }
</script></head><body>
<form:form modelAttribute="board" action="update"  
          enctype="multipart/form-data" name="f">
   <form:hidden path="num" />
   <form:hidden path="boardid" />
   <table><tr><td>글쓴이</td><td><form:input path="writer" />
   <font color="red"><form:errors path="writer" /></font></td></tr>
   <tr><td>비밀번호</td><td><form:password path="pass" />
   <font color="red"><form:errors path="pass" /></font></td></tr>
   <tr><td>제목</td><td><form:input path="title" />
   <font color="red"><form:errors path="title" /></font></td></tr>
   <tr><td>내용</td><td><form:textarea path="content" rows="15" cols="80" id="summernote" />
   <font color="red"><form:errors path="content" /></font></td></tr>
   <tr><td>첨부파일</td>
       <td><c:if test="${!empty board.fileurl}">
     <div id="file_desc">
       <a href="file/${board.fileurl}">${board.fileurl}</a>
       <a href="javascript:file_delete()">[첨부파일삭제]</a>
     </div></c:if>
   <form:hidden path="fileurl"/><input type="file" name="file1"></td></tr>
   <tr><td colspan="2"><a href="javascript:document.f.submit()">[게시글수정]</a>
       <a href="list">[게시글목록]</a></td></tr></table></form:form>
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
</body></html>