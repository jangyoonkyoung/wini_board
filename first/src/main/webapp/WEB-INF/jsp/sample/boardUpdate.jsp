<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src= "https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
<meta charset="UTF-8">
<title>상세 페이지</title>
</head>
<body>
<form id="frm" name="frm" enctype="multipart/form-data"> 
<h1 class="title">게시판 상세 페이지</h1>
<table class="board_view">
	<colgroup>
		<col width="15%" />
		<col width="35%" />
		<col width="15%" />
		<col width="35%" />
	</colgroup>
	<title>게시글 상세</title>
	<tbody>
		<tr>
			<th scope="row">글 번호</th>
			<td>${map.IDX}<input type="hidden" id="IDX" name="IDX" value="${map.IDX}"></td>
			<th scope="row">조회수</th>
			<td>${map.HIT_CNT}</td>
		</tr>
		<tr>
			<th scope="row">작성자</th>
			<td>${map.CREA_ID}</td>
			<th scope="row">작성시간</th>
			<td>${map.CREA_DTM}</td>
		</tr>
		<tr>
			<th scope="row">제목</th>
			<td colspan="3"><input type="text" id="TITLE" name="TITLE" class="wdp_90" value="${map.TITLE}"></td> 
		</tr>
		<tr>
			<td colspan="4" class="view_text"><textarea rows="20" cols="100" title="내용" id="CONTENTS" name="CONTENTS">${map.CONTENTS}</textarea></td>
		</tr>
		<tr> 
		<th scope="row">첨부파일</th> 
			<td colspan="3"> 
			<div id="fileDiv"> 
				<c:forEach var="row" items="${list}" varStatus="var"> 
					<p> 
						<input type="hidden" id="IDX" name="IDX_${var.index}" value="${row.IDX}"> 
						<a href="#" id="name_${var.index}" name="name_${var.index}">${row.ORIGINAL_FILE_NAME}</a> 
						<input type="file" id="file_${var.index}" name="file_${var.index}"> (${row.FILE_SIZE}kb) 
						<a href="#" class="btn" id="delete_${var.index}" name="delete_${var.index}">삭제</a> 
					</p> 
				</c:forEach> 
			</div> 
			</td> 
		</tr>
	</tbody>	
</table>
</form>
<a href = "#" class="btn" id="addFile">파일추가</a>
<a href = "#" class="btn" id="list">목록으로</a>
<a href = "#" class="btn" id="update">저장하기</a>
<a href = "#" class="btn" id="delete">삭제하기</a>

<%@ include file="/WEB-INF/include/include-body.jspf" %> 

<script type="text/javascript">
	var gfv_count = '${fn:length(list)+1}';

	$(document).ready(function(){
		$("#list").click(function(e){ //목록으로 버튼
			console.log("클릭")
			e.preventDefault();
			fn_openBoardList();
		})//list click
		
		$("#update").click(function(e){ //저장하기 버튼
			console.log("클릭")
			e.preventDefault();
			fn_updateBoard();
		})//update click
		
		$("#delete").click(function(e){ //삭제 버튼
			e.preventDefault();
			fn_deleteBoard();
		})//delete click
		
		$("#addFile").click(function(e){ 
			e.preventDefault();
			fn_addFile();
		});//파일 추가하기
		
		$("a[name^='delete']").click(function(e){
			e.preventDefault(); 
			fn_deleteFile($(this)); 
		});

	})//ready function

	function fn_openBoardList(){
		var comSubmit = new ComSubmit();
		comSubmit.setUrl("/first/sample/openBoardList.do");
		comSubmit.submit();
	}
	
	function fn_updateBoard(){
		var comSubmit = new ComSubmit("frm"); //form이름이 frm이다. 한마디로 frm전체 모두를 보내주겠다는 뜻.
		comSubmit.setUrl("/first/sample/updateBoard.do");
		comSubmit.submit();
	}
	
	function fn_deleteBoard(){
		var comSubmit = new ComSubmit();
		comSubmit.setUrl("/first/sample/deleteBoard.do");
		comSubmit.addParam("IDX", $("#IDX").val()); //addParam으로 뒤에 idx값을 넣어준다.
		comSubmit.submit();
	}
	function fn_addFile(){ 
		var str = "<p>" + "<input type='file' id='file_"+(gfv_count)+"' name='file_"+(gfv_count)+"'>"
		+ "<a href='#this' class='btn' id='delete_"+(gfv_count)+"' name='delete_"+(gfv_count)+"'>삭제</a>" 
		+ "</p>"; 
		$("#fileDiv").append(str); 
			$("#delete_"+(gfv_count++)).click(function(e){
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