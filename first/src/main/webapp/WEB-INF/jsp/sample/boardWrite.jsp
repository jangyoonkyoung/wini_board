<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
 <link href="<c:url value='/css/ui.css'/>" rel="stylesheet" type="text/css">
<meta charset="UTF-8">
<title>Insert title here</title>
<script src= "https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>
<body>
<form id="frm" name="frm" enctype="multipart/form-data">
	<table class="board_view">
		<colgroup>
			<col width="15%">
			<col width="*">
		</colgroup>
		<h1 class="title">게시글 작성</h1>
		<tbody>
			<tr>
				<th scope = "row">제목</th>
				<td><input type="text" id="TITLE" name="TITLE" class="wdp_90"></td>
			</tr>
			<tr>
				<td colspan="2" class="view_text">
					<textarea row="20" cols="100" title="내용" id="CONTENTS" name="CONTENTS"></textarea>
				</td>
			</tr>
		</tbody>	
	</table>
	<div id="fileDiv">
		<p>
			<input type="file" name="file_0" id="file">
			<a href = "#" class="btn" id="delete" name="delete" >삭제</a>		
		</p>
	</div>
	<br>	
	<a href = "#" class="btn" id="addFile">파일추가</a>
	<a href = "#" class="btn" id="write">작성하기</a>
	<a href = "#" class="btn" id="list">목록으로</a>
</form>
	
<%@ include file="/WEB-INF/include/include-body.jspf" %> 
	
	<script type="text/javascript"> 
		var gfv_count = 1; //새로운 파일 업로드 count
	
 		$(document).ready(function(){ 
 			$("#list").click(function(e){
 				//console.log("확인")
				e.preventDefault(); //해당 이벤트를 명시적으로 처리하지 않는 경우, 해당 이벤트에 대한 동작을 실행하지 않는다.
				fn_openBoardList();
			}); //list click
			
			$("#write").click(function(e){
				e.preventDefault(); 
				fn_insertBoard();
			});//write ready
			
			$("#addFile").click(function(e){
				e.preventDefault();
				fn_addFile();
			}); // addFile
			
			$("a[name='delete']").click(function(e){
				e.preventDefault();
				fn_deleteFile($(this));
			});
 		}); // ready function
		
		function fn_openBoardList(){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("/first/sample/openBoardList.do");
			comSubmit.submit(); //submit객체를 만든다면 form에 action을 취할 시 생기는 불필요한 폼작성, 동일 내용 반복을 줄일 수 있다.		
		}
		
		 function fn_insertBoard(){
			var comSubmit = new ComSubmit("frm"); //form의 아이디인 frm인자값을 넘겨준다.
			comSubmit.setUrl("/first/sample/insertBoard.do");
			comSubmit.submit(); //submit객체를 만든다면 form에 action을 취할 시 생기는 불필요한 폼작성, 동일 내용 반복을 줄일 수 있다.		
		}
		 function fn_addFile(){
			 var str = "<p><input type='file' name='file_"+(gfv_count++)+"'><a href='#' class='btn' name='delete'>삭제</a></p>"; 
				$("#fileDiv").append(str);
				$("a[name='delete']").click(function(e){
					e.preventDefault();
					fn_deleteFile($(this));
				});
		 }
 		function fn_deleteFile(obj){
 			obj.parent().remove();
 		}
	</script>

</body>
</html>