package first.sample.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import first.common.common.CommandMap;
import first.sample.service.SampleService;

@Controller
public class SampleController {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "sampleService")
	private SampleService sampleService;

	// 리스트 불러오기
	@RequestMapping(value = "/sample/openBoardList.do")
	public ModelAndView openBoardList(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardList");
		return mv;
	}

	@RequestMapping(value = "/sample/selectBoardList.do")
	public ModelAndView selectBoardList(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView"); // 원래는 /sample/boardWrite로 구성해주었었다. 뒤에 jsonView로 변경 -> 여기서 acion-servlet에서 만든 bean을 사용.

		List<Map<String, Object>> list = sampleService.selectBoardList(commandMap.getMap());
		mv.addObject("list", list);
		if (list.size() > 0) {
			mv.addObject("TOTAL", list.get(0).get("TOTAL_COUNT"));
		} else {
			mv.addObject("TOTAL", 0);
		}
		return mv;
	}

	// HandlerMethodArgumentResolver 설정
	@RequestMapping(value = "/sample/testMapArgumentResolver.do")
	public ModelAndView testMapArgumentResolver(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("");

		if (commandMap.isEmpty() == false) {
			Iterator<Entry<String, Object>> iterator = commandMap.getMap().entrySet().iterator();
			Entry<String, Object> entry = null;
			while (iterator.hasNext()) {
				entry = iterator.next();
				log.debug("key: " + entry.getKey() + ", value: " + entry.getValue());
			}
		}
		return mv;
	}

	// 새로운 게시글 작성 페이지 이동
	@RequestMapping(value = "/sample/openBoardWrite.do")
	public ModelAndView openBoardWrite(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardWrite");
		return mv;
	}

	// 새로운 게시글 작성 insert
	// commandMap에는 모든 정보가 들어가지만 파일정보는 x ->HttpServletReqest를 추가하여 파일정보를 전달.
	@RequestMapping(value = "/sample/insertBoard.do")
	public ModelAndView insertBoard(CommandMap commandMap, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardList.do"); // 저장하기 누르면 메인페이지로 이동.
		sampleService.insertBoard(commandMap.getMap(), request);
		return mv;
	}

	// 상세 보기
	//	@RequestMapping(value = "/sample/openBoardDetail.do")
	//	public ModelAndView openBoardDetail(CommandMap commandMap) throws Exception {
	//		ModelAndView mv = new ModelAndView("/sample/boardDetail");
	//		// 상세보기는 리스트 출력과 다르게 1개의 결과값을 출력해야 하기 때문에 map형태로 출력해야 한다.
	//		Map<String, Object> map = sampleService.selectBoardDetail(commandMap.getMap());
	//		// mv.addObject("map", map); //원래 리턴값을 그대로 map으로 화면에 전송.
	//		mv.addObject("map", map.get("map")); // 기존 상세정보
	//		mv.addObject("list", map.get("list")); // 첨부파일 목록
	//
	//		return mv;
	//	}

	// 상세 보기
	@RequestMapping(value = "/sample/openBoardDetail.do")
	public ModelAndView openBoardDetail(@RequestParam Map<String, Object> commandMap) throws Exception { // 바로 commandMap이라는 객체 생성.
		ModelAndView mv = new ModelAndView("/sample/boardDetail");
		// 상세보기는 리스트 출력과 다르게 1개의 결과값을 출력해야 하기 때문에 map형태로 출력해야 한다.
		Map<String, Object> map = sampleService.selectBoardDetail(commandMap);
		// mv.addObject("map", map); //원래 리턴값을 그대로 map으로 화면에 전송.
		mv.addObject("map", map.get("map")); // 기존 상세정보
		mv.addObject("list", map.get("list")); // 첨부파일 목록

		return mv;
	}

	// 수정 페이지로 이동
	@RequestMapping(value = "/sample/openBoardUpdate.do")
	public ModelAndView openBoardUpdate(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardUpdate");

		Map<String, Object> map = sampleService.selectBoardDetail(commandMap.getMap()); //상세보기를 클릭 시 text파일로 수정 가능한 페이지가 열린다
		mv.addObject("map", map.get("map"));
		mv.addObject("list", map.get("list"));
		return mv;
	}

	// 수정
	@RequestMapping(value = "/sample/updateBoard.do")
	public ModelAndView updateBoard(CommandMap commandMap, HttpServletRequest request) throws Exception { //파일 수정하ㅣ 위해 HttpServletRequest 추가
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardDetail.do"); // 다시 로드 해서 수정한 부분 적용하기
		sampleService.updateBoard(commandMap.getMap(), request);

		mv.addObject("IDX", commandMap.get("IDX")); // addParam으로 추가해주었던 idx값을 찾아온다.
		return mv;
	}

	// 삭제 버튼
	@RequestMapping(value = "/sample/deleteBoard.do")
	public ModelAndView deleteBoard(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardList.do");
		sampleService.deleteBoard(commandMap.getMap()); //그냥 통채로 삭제하면 된다.

		return mv;
	}

}