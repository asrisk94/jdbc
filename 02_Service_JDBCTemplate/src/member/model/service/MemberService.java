package member.model.service;

// static 임포트
import static common.JDBCTemplate.close;
import static common.JDBCTemplate.commit;
import static common.JDBCTemplate.getConnection;
import static common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.model.dao.MemberDAO;
import member.model.vo.Member;

// Service
// 업무로직 담당클래스


/*
* 1. jdbc driver 클래스 등록 (dbms별로 제공) : 최초 1회
* 2. DB connection 객체 생성 : db server url, user, password 필요
* 
* -> 요 사이는 DAO 담당
* 
* 6. 트랜젝션 처리 (commit, rollback)
* 7. 자원반납(Connection)
*/

public class MemberService {
	
	private MemberDAO memberDAO = new MemberDAO();

	
	
	// 참고용 자료. (service까지만 적용한 경우)
	public int insertMember_(Member inputMember) {
		
		String driverClass = "oracle.jdbc.OracleDriver";
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "student";
		String password = "student";
		
		int result = 0;
		
		try {
			// 1. jdbc driver 클래스 등록 (dbms별로 제공) : 최초 1회
			Class.forName(driverClass);
			// 2. DB connection 객체 생성 : db server url, user, password 필요
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
			// -> 요 사이는 DAO 담당
			result = memberDAO.insertMember(conn, inputMember);
			
			// 6. 트랜젝션 처리 (commit, rollback)
			if(result > 0) conn.commit();
			else conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 7. 자원반납(Connection)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	
	// 전체 조회
	public List<Member> selectAll() {
		
		List<Member> list = null;
		
		Connection conn = getConnection();	// 드라이버 연결 및 계정 연결, 커밋펄스까지 (memberService)
		
		list = memberDAO.selectAll(conn);
		
		close(conn);						// rset과 pstmt는 DAO에서 닫고 나왔기 때문에 conn만 닫아주면 된다.
		
		
		return list;
	}
	
	
	
	// 아이디로 한명 조회
	public Member selectOneMember(String memberId) {
		
		// 1. Connection 생성
		Connection conn = getConnection();
		
		// 2. dao 요청
		Member member = memberDAO.selecOneMember(conn, memberId);
		
		// 3. 자원반납
		close(conn);
		
		return member;
	}

	
	
	// 이름으로 여럿 조회
	public List<Member> selectMemberName(String memberName) {
		
		List<Member> list = null;
		
		Connection conn = getConnection();
		
		list = memberDAO.selectMemberName(conn, memberName);
		
		close(conn);
		
		return list;
	}
	
	
	
	// 4번 회원 추가
	public int insertMember(Member inputMember) {
		
		// 1. Connection 생성
		Connection conn = getConnection();
		
		// 2. dao 요청
		int result = memberDAO.insertMember(conn, inputMember);
		
		// 3. 트랜잭션 처리
		if(result > 0) commit(conn);				// 수정이 일어나는 경우에는 커밋 롤백을 선택해줘야 한다.
		else rollback(conn);
		// 4. 자원반납
		close(conn);
		
		return result;
	}
	
	
	
	// 업데이트
	public int updateMember(Member member, String columnName, String content) {

		Connection conn = getConnection();
		
		int result = memberDAO.updateMember(conn, member, columnName, content);
		
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}



	// 삭제
	public int deleteMember(String inputMemberId) {
		
		Connection conn = getConnection();
		
		int result = memberDAO.deleteMember(conn, inputMemberId);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

}
