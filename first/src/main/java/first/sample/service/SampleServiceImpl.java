package first.sample.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import first.common.util.FileUtils;
import first.sample.dao.SampleDAO;

@Service("sampleService")
public class SampleServiceImpl implements SampleService {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "fileUtils")
	private FileUtils fileUtils;

	@Resource(name = "sampleDAO")
	private SampleDAO sampleDAO;

	// select
	@Override
	public List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception {
		return sampleDAO.selectBoardList(map);
	}

	// insert
	@Override
	public void insertBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {
		sampleDAO.insertBoard(map);
		//      파일이 제데로 되는지 확인하는 log 코딩 -> 확인했으니 지우자.
		//		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		//		// iterator: 데이터들의 집합체에서 컬렉션으로부터 정보를 얻어올 수 있는 인터페이스
		//		Iterator<String> iterator = multipartHttpServletRequest.getFileNames(); // 모든 name을 이용.
		//		MultipartFile multipartFile = null;
		//		while (iterator.hasNext()) { //iterator인터페이스 hasNext메서드를 통해 iterator다음값이 있는 동안 반복.
		//			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
		//			if (multipartFile.isEmpty() == false) {
		//				log.debug("====file start=====");
		//				log.debug("name: " + multipartFile.getName());
		//				log.debug("filename : " + multipartFile.getOriginalFilename());
		//				log.debug("size : " + multipartFile.getSize());
		//				log.debug("====file end=====");
		//			}
		//		}

		List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(map, request); // fileUtils.java
		for (int i = 0, size = list.size(); i < size; i++) {
			sampleDAO.insertFile(list.get(i));
		}
	}

	// detail
	public Map<String, Object> selectBoardDetail(Map<String, Object> map) throws Exception {
		// return sampleDAO.selectBoardDetaile(map); 이게 아니다!!!
		sampleDAO.updateHitCnt(map); // 조회수도 같이 보내줘야 하니까.
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 게시판 정보
		Map<String, Object> tempMap = sampleDAO.selectBoardDetail(map);
		resultMap.put("map", tempMap);
		// 파일 정보
		List<Map<String, Object>> list = sampleDAO.selectFileList(map);
		resultMap.put("list", list);

		return resultMap;
	}

	// update
	public void updateBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {
		sampleDAO.updateBoard(map);// 기존의 내용은 여기까지. 게시글 리스트 내보내준다.

		sampleDAO.deleteFileList(map); // 해당 게시글에 해당하는 첨부파일 전체 삭제(DEL_GB = 'Y')
		List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(map, request); // request에 담겨있는 것을 list로 변환.
		Map<String, Object> tempMap = null;
		for (int i = 0; i < list.size(); i++) {
			tempMap = list.get(i);
			// 만약 새로운 파일 업로드면 insertFile해주어라
			if (tempMap.get("IS_NEW").equals("Y")) { // 신규파일
				sampleDAO.insertFile(tempMap);
			} else {
				sampleDAO.updateFile(tempMap);
			}
		}
	}

	// delete
	public void deleteBoard(Map<String, Object> map) throws Exception {
		sampleDAO.deleteBoard(map);
	}
}
