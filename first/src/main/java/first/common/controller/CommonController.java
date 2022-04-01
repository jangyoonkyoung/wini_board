package first.common.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import first.common.common.CommandMap;
import first.common.service.CommonService;

@Controller
public class CommonController {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "commonService")
	private CommonService commonService;

	@RequestMapping(value = "/common/downloadFile.do")
	public void downloadFile(CommandMap commanMap, HttpServletResponse response) throws Exception { // 파일의 정보를 내보내줘야 하니 response.
		Map<String, Object> map = commonService.selectFileInfo(commanMap.getMap());
		String storedFileName = (String) map.get("STORED_FILE_NAME");
		String originalFileName = (String) map.get("ORIGINAL_FILE_NAME");

		byte fileByte[] = FileUtils.readFileToByteArray(new File("C:\\dev\\file\\" + storedFileName)); // 파일을 바이트로 변환 후 지정된 경로에 파일이름으로 넣어준다.

		response.setContentType("application/octet-stream"); //아카이브 문서 (인코딩된 다중 파일)
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\";"); //content의 기질/성향 -> 첨부파일이라는 뜻.
		response.setHeader("Content-Transfer-Encoding", "binary"); //콘텐츠 전송 인코딩 - 이진법
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush(); //정리
		response.getOutputStream().close(); //닫아주기

	}

}
