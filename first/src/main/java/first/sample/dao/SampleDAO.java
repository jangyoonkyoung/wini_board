package first.sample.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import first.common.dao.AbstractDAO;

//sample. ->sample.--라는 id를 가진 쿼리가 실행되었음.

@Repository("sampleDAO")
public class SampleDAO extends AbstractDAO {
	// 조회
	@SuppressWarnings("unchecked") //경고를 제외할 때 쓰는 것.
	public List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) selectPagingList("sample.selectBoardList", map);
	}

	// 추가
	public void insertBoard(Map<String, Object> map) throws Exception {
		insert("sample.insertBoard", map);
	}

	// 두개의 동작을 하나의 트랙잭션으로 수행하는것이 중요
	// 조회수 dao
	public void updateHitCnt(Map<String, Object> map) throws Exception {
		update("sample.updateHitCnt", map);
	}

	// detail
	@SuppressWarnings("unchecked") // 이거 안해주면 노란색이 뜬다
	public Map<String, Object> selectBoardDetail(Map<String, Object> map) throws Exception {
		return (Map<String, Object>) selectOne("sample.selectBoardDetail", map);
	}

	// update
	public void updateBoard(Map<String, Object> map) throws Exception {
		update("sample.updateBoard", map);
	}

	// delete
	public void deleteBoard(Map<String, Object> map) throws Exception {
		update("sample.deleteBoard", map);
	}

	// file upload
	public void insertFile(Map<String, Object> map) throws Exception {
		insert("sample.insertFile", map);
	}

	// 저장한 파일들 보여주기
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectFileList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) selectList("sample.selectFileList", map);
	}

	// 업로드된 파일 삭제
	public void deleteFileList(Map<String, Object> map) throws Exception {
		update("sample.deleteFileList", map);
	}

	// 파일 수정
	public void updateFile(Map<String, Object> map) throws Exception {
		update("sample.updateFile", map);
	}
}
