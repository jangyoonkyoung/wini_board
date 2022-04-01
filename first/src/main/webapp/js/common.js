//null값을 확인 // 모든 form은 commonForm이라고 include-body에 선언하였다.
function gfn_isNull(str) {
	if (str == null) return true;
	if (str == "NaN") return true;
	if (new String(str).valueOf() == "undefined") return true;

	var chkStr = new String(str);

	if (chkStr.valueOf() == "undefined") return true;
	if (chkStr == null) return true;
	if (chkStr.toString().length == 0) return true;

	return false;
}

//SUBMIT하는 기능. 보통 form에 submit을 하지만 이 기능을 사용. 
//목록으로 이동하기, 작성하기 등 화면이동에서 사용하는 기능.
//commonForm이라는 가상폼을 만들어 쉽게 관리.
function ComSubmit(opt_formId) {
	this.formId = gfn_isNull(opt_formId) == true ? "commonForm" : opt_formId; //널이 아니라면 commonForm으로 가자. 
	this.url = "";

	if (this.formId == "commonForm") {
		$("#commonForm")[0].reset();
	}

	this.setUrl = function setUrl(url) { //url
		this.url = url;
	}

	this.addParam = function addParam(key, value) { //주소값에 formId값 + param값(key,value)를 넣어준다. 
		$("#" + this.formId).append($("<input type='hidden' name='" + key + "' id='" + key + "' value='" + value + "' >"))
	};

	this.submit = function submit() { //submit값을 한번 정의해놓고 불러서 사용하기. 
		var frm = $("#" + this.formId)[0];
		frm.action = this.url;
		frm.method = "post";
		frm.submit();
	}
}

//페이징 처리 기능
/* divId : 페이징 태그가 그려질 div
pageIndx : 현재 페이지 위치가 저장될 input 태그 id 
recordCount : 페이지당 레코드 수 
totalCount : 전체 조회 건수 
eventName : 페이징 하단의 숫자 등의 버튼이 클릭되었을 때 호출될 함수 이름 */

var gfv_ajaxCallback = "";

function ComAjax(opt_formId) {
	this.url = "";
	this.formId = gfn_isNull(opt_formId) == true ? "commonForm" : opt_formId; //널인지 먼저 체크.
	this.param = "";

	if (this.formId == "commonForm") {
		var frm = $("#commonForm");
		if (frm.length > 0) {
			frm.remove(); //지우고 다시 넣어주고를 반복하기 위해.
		}
		var str = "<form id='commonForm' name='commonForm'></form>";
		$('body').append(str); //다시 넣어주기.
	}

	this.setUrl = function setUrl(url) {
		this.url = url;
	}
	//ajax을 이용하여 데이터를 전송한 후 호출된 콜백함수의 이름을 지정하는 함수
	this.setCallback = function setCallback(callback) {
		fv_ajaxCallback = callback;
	}

	this.addParam = function addParam(key, value) {
		this.param = this.param + "&" + key + "=" + value;
	};

	this.ajax = function ajax() {
		if (this.formId != "commonForm") {
			this.param += "&" + $("#" + this.formId).serialize(); //폼의 모든 객체를 불어와준다.
		}
		$.ajax({
			url: this.url,
			type: "POST",
			data: this.param, 
			async: false,
			success: function(data, status) {
				if (typeof (fv_ajaxCallback) == "function") {
					fv_ajaxCallback(data);
				}
				else {
					eval(fv_ajaxCallback + "(data)"); //문자열 형태의 코드를 실행.
				}
			}
		})
	}
};

var gfv_pageIndex = null;
var gfv_eventName = null;
function gfn_renderPaging(params) { //파라미터 값으로 params를 준다.
	var divId = params.divId; //페이징이 그려질 div id
	gfv_pageIndex = params.pageIndex; //현재 위치가 저장될 input 태그
	var totalCount = params.totalCount; //전체 조회 건수
	var currentIndex = $("#" + params.pageIndex).val(); //현재 페이지 위치
	if ($("#" + params.pageIndex).length == 0 || gfn_isNull(currentIndex) == true) {
		currentIndex = 1;  //값이 없다면 페이지를 1페이지만 보내준다.
	}

	var recordCount = params.recordCount; //페이지당 레코드 수 ->나는 15로 지정.
	if (gfn_isNull(recordCount) == true) { 
		recordCount = 15;
	}

	var totalIndexCount = Math.ceil(totalCount / recordCount); // 전체 인덱스 수
	gfv_eventName = params.eventName;

	$("#" + divId).empty(); //비워주고 다시 채워줘야 함으로 비워준다!
	var preStr = "";
	var postStr = "";
	var str = "";

	var first = (parseInt((currentIndex - 1) / 10) * 10) + 1;
	var last = (parseInt(totalIndexCount / 10) == parseInt(currentIndex / 10)) ? totalIndexCount % 10 : 10;
	var prev = (parseInt((currentIndex - 1) / 10) * 10) - 9 > 0 ? (parseInt((currentIndex - 1) / 10) * 10) - 9 : 1;
	var next = (parseInt((currentIndex - 1) / 10) + 1) * 10 + 1 < totalIndexCount ? (parseInt((currentIndex - 1) / 10) + 1) * 10 + 1 : totalIndexCount;

	if (totalIndexCount > 10) { //전체 인덱스가 10이 넘을 경우, 맨앞, 앞 태그 작성 
		preStr += "<a href='#this' class='pad_5' onclick='_movePage(1)'> [<<] </a>" + "<a href='#this' class='pad_5' onclick='_movePage(" + prev + ")'> [<] </a>";
	}
	else if (totalIndexCount <= 10 && totalIndexCount > 1) { //전체 인덱스가 10보다 작을경우, 맨앞 태그 작성 
		preStr += "<a href='#this' class='pad_5' onclick='_movePage(1)'> [<<] </a>"; //10페이지씩 이동 안해도 되니까.
	}

	if (totalIndexCount > 10) { //전체 인덱스가 10이 넘을 경우, 맨뒤, 뒤 태그 작성 
		postStr += "<a href='#this' class='pad_5' onclick='_movePage(" + next + ")'>[>]</a>" +
			"<a href='#this' class='pad_5' onclick='_movePage(" + totalIndexCount + ")'>[>>]</a>";
	}
	else if (totalIndexCount <= 10 && totalIndexCount > 1) { //전체 인덱스가 10보다 작을경우, 맨뒤 태그 작성 
		postStr += "<a href='#this' class='pad_5' onclick='_movePage(" + totalIndexCount + ")'>[>>]</a>";
	}

	for (var i = first; i < (first + last); i++) {
		if (i != currentIndex) {
			str += "<a href='#this' class='pad_5' onclick='_movePage(" + i + ")'>" + i + "</a>";
		}
		else {
			str += "<b><a href='#this' class='pad_5' onclick='_movePage(" + i + ")'>" + i + "</a></b>";
		}
	}

	$("#" + divId).append(preStr + str + postStr);
}

//페이지 번호를 클릭할 시 이동.
function _movePage(value) {
	$("#" + gfv_pageIndex).val(value);
	if (typeof (gfv_eventName) == "function") {
		gfv_eventName(value);
	}
	else {
		eval(gfv_eventName + "(value)");
	}
}








