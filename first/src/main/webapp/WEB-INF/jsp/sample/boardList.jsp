<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src= "https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link href="<c:url value='/css/ui.css'/>" rel="stylesheet" type="text/css">


<%@ include file="/WEB-INF/include/include-header.jspf" %>
</head>
<body>
	<h1 class="title">게시판 목록</h1>
	<table class="board_list" style="border: 1px solid #ccc; width: 80%; text-align: center; margin-left:auto; margin-right:auto;">
			<colgroup>
				<col width="10%" />
				<col width="*" />
				<col width="15%" />
				<col width="30%" />
			</colgroup>
			<thead class="t_head"> 
			 	<tr>
			 		<th scope="col">글번호</th>
			 		<th scope="col">제목</th>
			 		<th scope="col">조회수</th>
			 		<th scope="col">작성일</th>
			 	</tr>
			</thead>
			<tbody class="t_body">
<%-- 				<c:choose> --%>
<%-- 					<c:when test = "${fn:length(list)>0}"> --%>
<%-- 						<c:forEach items="${list}" var="row"> --%>
<!-- 							<tr> -->
<%-- 								<td>${row.IDX}</td> --%>
<!-- 								<td class="title"> -->
<%-- 									<a href="#" name="title">${row.TITLE}</a> --%>
<%-- 									<input type="hidden" id="IDX" value="${row.IDX}"> --%>
<!-- 								</td> -->
<%-- 								<td>${row.HIT_CNT}</td> --%>
<%-- 								<td>${row.CREA_DTM}</td> --%>
<!-- 							</tr>	 -->
<%-- 						</c:forEach> --%>
<%-- 					</c:when> --%>
<%-- 					<c:otherwise> --%>
<!-- 						<tr> -->
<!-- 							<td clospan="4">조회된 데이터가 없습니다.</td> -->
<!-- 						</tr> -->
<%-- 					</c:otherwise> --%>
<%-- 				</c:choose> --%>
		</tbody>
	</table>
	<div id="PAGE_NAVI"></div>
	<input type="hidden" id="PAGE_INDEX" name="PAGE_INDEX">
	<br>
	<a href = "#" class="btn" id="write">글쓰기</a>
	
	<%@ include file="/WEB-INF/include/include-body.jspf" %> 
	<script type="text/javascript">
	 $(document).ready(function(){ 
		 fn_selectBoardList(1);

		$("#write").click(function(e){
			e.preventDefault();
			fn_openBoardWrite();
		}); //글쓰기 버튼 클릭 시 이동
		
		$("a[name='title']").click(function(e){ //제목 클릭 시 
			e.preventDefault();
			fn_openBoardDetail($(this));//a태그 속 제목을 의미하는 this.
		});  
	});// ready function
	
	function fn_openBoardWrite(){
		var comSubmit = new ComSubmit();
		comSubmit.setUrl("/first/sample/openBoardWrite.do");
		comSubmit.submit(); //submit객체를 만든다면 form에 action을 취할 시 생기는 불필요한 폼작성, 동일 내용 반복을 줄일 수 있다.		
	} //글쓰기 페이지로 이동 function
	
	//상세보기
	function fn_openBoardDetail(obj){
		var comSubmit = new ComSubmit(); //form의 아이디인 frm인자값을 넘겨준다.
		comSubmit.setUrl("/first/sample/openBoardDetail.do");
		comSubmit.addParam("IDX", obj.parent().find("#IDX").val()); //a태그의 부모 노드 내에서 IDX라는 값을 가진 태그를 찾아 가져온다.
		comSubmit.submit(); 
	} 
	
	function fn_selectBoardList(pageNo){ //파라미터로 pageNo를 받아온다. 호출하고자 하는 페이지 번호를 의미.
		var comAjax = new ComAjax(); //common.js에서 가져온다.
		comAjax.setUrl("/first/sample/selectBoardList.do");
		comAjax.setCallback("fn_selectBoardListCallback"); //콜백함수로 페이징 기능을 불러온다.
		comAjax.addParam("PAGE_INDEX", pageNo);  //페이지 하나 하나
		comAjax.addParam("PAGE_ROW", 15); //한 페이지당 보이는 게시글 갯수
		comAjax.ajax();
	}
	
	function fn_selectBoardListCallback(data){ //페이징 function
			var total = data.TOTAL;
			var body = $("table>tbody");
			body.empty();
			if(total == 0){
				var str = "<tr>" + 
								"<td colspan='4'>조회된 결과가 없습니다.</td>" + 
							"</tr>";
				body.append(str);
			}
			else{
				var params = {
					divId : "PAGE_NAVI",
					pageIndex : "PAGE_INDEX",
					totalCount : total,
					eventName : "fn_selectBoardList"
				};
				gfn_renderPaging(params);
				
				var str = "";
				$.each(data.list, function(key, value){
					str += "<tr>" + 
								"<td>" + value.IDX + "</td>" + 
								"<td class='title'>" +
									"<a href='#this' name='title'>" + value.TITLE + "</a>" +
									"<input type='hidden' id='IDX' name='title' value="+ value.IDX + ">" + 
								"</td>" +
								"<td>" + value.HIT_CNT + "</td>" + 
								"<td>" + value.CREA_DTM + "</td>" + 
							"</tr>";
				});
				body.append(str);
			
			$("a[name='title']").click(function(e){ //제목 
				e.preventDefault(); 
				fn_openBoardDetail($(this)); 
				});
		}
	}
	
	</script>
</body>
</html>