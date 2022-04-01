package first.sample.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SampleService {
	// 예외처리 해주기. 에러가 발생하면 날려준다.

	List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception; // select문

	void insertBoard(Map<String, Object> map, HttpServletRequest request) throws Exception; // insert문

	Map<String, Object> selectBoardDetail(Map<String, Object> map) throws Exception; // detail문

	void updateBoard(Map<String, Object> map, HttpServletRequest request) throws Exception; // update문

	void deleteBoard(Map<String, Object> map) throws Exception; // delete문

}
