package member.model.service;

import static common.JDBCTemplate.close;
import static common.JDBCTemplate.commit;
import static common.JDBCTemplate.getConnection;
import static common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import member.model.dao.MemberDAO;
import member.model.exception.MemberException;
import member.model.vo.Member;



/**
 * Service
 * 업무로직 담당클래스 (connection생성, 트랜잭션처리, dao업무요청)
 * 
 * 1. jdbc driver 클래스 등록(dbms별로 제공) : 최초 1회
 * 2. db connection객체 생성 : dbserver url, user, password
 * -> DAO담당
 * 6. 트랜잭션처리(commit, rollback)
 * 7. 자원반납(Connection) 
 */

public class MemberService {
	
	private MemberDAO memberDAO = new MemberDAO();

	
	
	public int insertMember(Member member) throws MemberException {
		
		//1. Connection 생성
		Connection conn = getConnection();
		//2. dao요청
		int result = memberDAO.insertMember(conn, member);
		//3. 트랜잭션 처리
		if(result  > 0) commit(conn);
		else rollback(conn);
		//4. 자원반납
		close(conn);
		return result;
	}

	
	
	public Member selectOneMember(String memberId) throws MemberException {
		
		//1.Connection생성
		Connection conn = getConnection();
		//2.dao요청
		Member member = memberDAO.selectOneMember(conn, memberId);
		//3.자원반납
		close(conn);
		return member;
	}
	
	
	
	public List<Member> selectAll() throws MemberException {
		
		Connection conn = getConnection();
		List<Member> list = memberDAO.selectAll(conn);
		close(conn);
		return list;
	}
	
	
	
	public List<Member> selectByName(String memberName) throws MemberException {

		Connection conn = getConnection();
		List<Member> list = memberDAO.selectByName(conn, memberName);
		close(conn);
		return list;
	}

	
	
	public int updateMember(Member m) throws MemberException {
		
		Connection conn = getConnection();
		int result = memberDAO.updateMember(conn, m);
		System.out.println("result@service = " + result);
		if(result>0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}
	
	
	
	public int deleteMember(String memberId) throws MemberException {
		
		Connection conn = getConnection();
		int result = memberDAO.deleteMember(conn, memberId);
		if(result>0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}
	
	
	
	public List<Member> selectAllDeletedMember() throws MemberException {
		
		Connection conn = getConnection();
		List<Member> list = memberDAO.selectAllDeletedMember(conn);
		close(conn);
		return list;
	}
	
}
