package first.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component("fileUtils") // 빈을 생성해주어 연결하는 것과 같다.(fileUtils라는 이름의 beans 설정) - spring에서 객체 관리 해준다.
public class FileUtils {
	private static final String filePath = "C:\\dev\\file\\"; // 파일이 저장될 경로

	public List<Map<String, Object>> parseInsertFileInfo(Map<String, Object> map, HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames(); // 모든 name을 이용.
		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); //클라이언트에서 전송된 파일 정보를 담아서 반한해줄 list (다중 파일 전송 가능)
		Map<String, Object> listMap = null;

		String boardIdx = String.valueOf(map.get("IDX")); //게시판의 번호를 받아오는 부분
		String requestName = null;
		String idx = null;

		File file = new File(filePath);
		if (file.exists() == false) {
			file.mkdirs();
		}
		while (iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			if (multipartFile.isEmpty() == false) { //새로운 파일이라는 뜻. 신규저장 파일
				originalFileName = multipartFile.getOriginalFilename();
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); //무슨 파일인가? ex) file.txt면 text파일.
				storedFileName = CommonUtils.getRandomString() + originalFileExtension; //저장할 파일의 이름 랜덤 + 파일형식
				file = new File(filePath + storedFileName);
				multipartFile.transferTo(new File(filePath + storedFileName));// 지정된 경로에 파일을 생성하는 것.
				listMap = new HashMap<String, Object>();
				listMap.put("IS_NEW", "Y"); //새로운 파일이 들어왔을 경우를 위해 추가로 해준다.
				listMap.put("BOARD_IDX", boardIdx);
				listMap.put("ORIGINAL_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				list.add(listMap); //listMap이라는 객체를 만들어서 저 요소들을 다 넣어 list에 넣어준다.
			} //else추가
			else { //이미 있는 파일이라는 뜻. 
				requestName = multipartFile.getName(); //jsp에 file태그의 name값을 가져온다.
				idx = "IDX_" + requestName.substring(requestName.indexOf("_") + 1);
				if (map.containsKey(idx) == true && map.get(idx) != null) {
					listMap = new HashMap<String, Object>();
					listMap.put("IS_NEW", "N");
					listMap.put("FILE_IDX", map.get(idx));//이미있는 파일이므로 그냥 가져오기만 하면 된다.
					list.add(listMap);
				}
			}
		}

		return list;

	}
}
