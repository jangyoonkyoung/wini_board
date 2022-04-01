<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
<meta charset="UTF-8">
<title>상세 페이지</title>
</head>
<body>
<table class="board_view">
	<colgroup>
		<col width="15%" />
		<col width="35%" />
		<col width="15%" />
		<col width="35%" />
	</colgroup>
	<h1 class="title">게시글 상세 페이지</h1>
	<tbody>
		<tr>
			<th scope="row">글 번호</th>
			<td>${map.IDX}</td>
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
			<td colspan="3">${map.TITLE}</td>
		</tr>
		<tr>
			<td colspan="4">${map.CONTENTS}</td>
		</tr>
		<tr>
			<th scope="row">첨부파일</th>
			<td colspan="3">
				<c:forEach var="row" items="${list}">
					<input type="hidden" id="IDX" value="${row.IDX}">
					<a href = "#this" name="file">${row.ORIGINAL_FILE_NAME}</a>
					(${row.FILE_SIZE}kb)				
				</c:forEach>
			</td>
		</tr>
	</tbody>	
</table>

<a href = "#" class="btn" id="list">목록으로</a>
<a href = "#" class="btn" id="update">수정하기</a>

<%@ include file="/WEB-INF/include/include-body.jspf" %> 

<script type="text/javascript">
	$(document).ready(function(){
		$("#list").click(function(e){ //목록으로 돌아가기 버튼
			e.preventDefault();
			fn_openBoardList();
		})//list click
		
		$("#update").click(function(e){ //수정하기 버튼
			e.preventDefault();
			fn_openBoardUpdate();
		})//update click
		
		$("a[name='file']").click(function(e){ //파일 다운 버튼
			e.preventDefault();
			fn_downloadFile($(this)); //파일을 의미하는 this
		})//파일 업로드
		
	})//ready function

	function fn_openBoardList(){
		var comSubmit = new ComSubmit();
		comSubmit.setUrl("/first/sample/openBoardList.do");
		comSubmit.submit();
	}
	
	function fn_openBoardUpdate(){
		var idx = "${map.IDX}"; //controller에서 map으로 내보내주기 때문.
		var comSubmit = new ComSubmit();
		comSubmit.setUrl("/first/sample/openBoardUpdate.do");
		comSubmit.addParam("IDX", idx); ////addParam으로 뒤에 idx값을 넣어준다.
		comSubmit.submit();
	}
	
	function fn_downloadFile(obj){
		var idx = obj.parent().find("#IDX").val();
		var comSubmit = new ComSubmit();
		comSubmit.setUrl("/first/common/downloadFile.do");
		comSubmit.addParam("IDX", idx); //idx값을 parameter로 추가하여 넣어준다.
		comSubmit.submit();
	}
</script>
</body>
</html>