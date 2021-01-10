package member.model.dao;

import static common.JDBCTemplate.close;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import member.model.exception.DuplicateMemberIdException;
import member.model.exception.MemberException;
import member.model.vo.Member;
import member.model.vo.MemberDel;



/**
 * DAO
 * 3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
 * 4. 쿼리전송(실행) - 결과값
 * 4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
 * 5. 자원반납(PreparedStetement, ResultSet)
 */

public class MemberDAO {
	
	private Properties prop = new Properties();
	
	public MemberDAO() {
		try {
			prop.load(new FileReader("resources/query.properties"));	// 저장해둔 쿼리문 키-밸류 형태로 가져옴
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	public int insertMember(Connection conn, Member member) throws MemberException {

		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertMember");
		
		try {
			//3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender());
			pstmt.setInt(5, member.getAge());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getAddress());
			pstmt.setString(9, member.getHobby());
			
			//4. 쿼리전송(실행) - 결과값
			result = pstmt.executeUpdate();
		
		} catch(SQLIntegrityConstraintViolationException e) {
			if(e.getMessage().contains("STUDENT.PK_MEMBER_ID"))
				throw new DuplicateMemberIdException("중복된 아이디 : " + member.getMemberId(), e);
				
		} catch (Exception e) {
			throw new MemberException("회원가입 오류! 관리자에게 문의하세요.", e);
			
		} finally {
			//5. 자원반납(PreparedStetement, ResultSet)
			close(pstmt);
		}
		return result;
	}

	
	
	public Member selectOneMember(Connection conn, String memberId) throws MemberException{

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectOneMember");
		Member member = null;
		
		try {
			//1.PreparedStatement생성, 미완성 쿼리 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			//2.실행 및 ResultSet값 -> member객체
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				member = new Member();
				member.setMemberId(memberId);
				member.setPassword(rset.getString("password"));
				member.setMemberName(rset.getString("member_name"));
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
			}
			
		} catch (Exception e) {
			throw new MemberException("회원아이디조회 오류! 관리자에게 문의하세요.", e);
			
		} finally {
			//3.자원반납
			close(rset);
			close(pstmt);
		}
//		System.out.println("member@dao = " + member);
		return member;
	}
	
	
	
	public List<Member> selectAll(Connection conn) throws MemberException{

		List<Member> list = null;
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectAll");
		
		try {
			//1. 쿼리문을 실행할 statement객체 생성
			pstmt = conn.prepareStatement(query);
			
			//2. 쿼리문 전송, 실행결과 받기
			rset = pstmt.executeQuery();
			
			//3. 받은 결과값들을 객체에 옮겨 저장하기
			list = new ArrayList<Member>();
			
			while(rset.next()){
				Member m = new Member();
				m.setMemberId(rset.getString("member_id"));
				m.setPassword(rset.getString("password"));
				m.setMemberName(rset.getString("member_name"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enroll_date"));
				list.add(m);
			}
			
		} catch (Exception e){
//			e.printStackTrace();
			//1.해당 예외를 다시 던지기
//			throw e;
			//2.구체적인 커스텀예외클래스를 생성해서 던지기
			throw new MemberException("회원전체조회 오류! 관리자에게 문의하세요.", e);
			
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}

	
	
	public List<Member> selectByName(Connection conn, String memberName) throws MemberException{

		ArrayList<Member> list = null;
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectByName");
		
		try {
			//3. 쿼리문을 실행할 statement객체 생성
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%"+memberName+"%");
			//4. 쿼리문 전송, 실행결과 받기
			rset = pstmt.executeQuery();
			
			//5. 받은 결과값들을 객체에 옮겨 저장하기
			list = new ArrayList<Member>();
			
			while(rset.next()){
				Member m = new Member();
				m.setMemberId(rset.getString("member_id"));
				m.setPassword(rset.getString("password"));
				m.setMemberName(rset.getString("member_name"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enroll_date"));

				list.add(m);
			}
			
		} catch (Exception e){
			throw new MemberException("회원이름 조회 오류! 관리자에게 문의하세요.", e);
			
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}

	
	
	public int updateMember(Connection conn, Member m) throws MemberException{

		int result = 0;
		PreparedStatement pstmt = null;
		
		String query = prop.getProperty("updateMember");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, m.getPassword());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getMemberId());
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new MemberException("회원수정 오류! 관리자에게 문의하세요.", e);
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	
	
	public int deleteMember(Connection conn, String memberId) throws MemberException{

		int result = 0;
		PreparedStatement pstmt = null;
		
		String query = prop.getProperty("deleteMember");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, memberId);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new MemberException("회원삭제 오류! 관리자에게 문의하세요.", e);
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	
	
	/**
	 * 삭제회원 조회용메소드
	 * 
	 * @param conn
	 * @return
	 */
	public List<Member> selectAllDeletedMember(Connection conn) throws MemberException{

		List<Member> list = new ArrayList<>();
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectAllDeletedMember");
		
		try {
			//1. 쿼리문을 실행할 statement객체 생성
			pstmt = conn.prepareStatement(query);
			
			//2. 쿼리문 전송, 실행결과 받기
			rset = pstmt.executeQuery(query);
			
			//3. 받은 결과값들을 객체에 옮겨 저장하기
			while(rset.next()){
				MemberDel m = new MemberDel();
				m.setMemberId(rset.getString("member_id"));
				m.setPassword(rset.getString("password"));
				m.setMemberName(rset.getString("member_name"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enroll_date"));
				m.setDelDate(rset.getDate("del_date"));
				list.add(m);
			}
			
		} catch (Exception e){
			throw new MemberException("탈퇴회원 조회 오류! 관리자에게 문의하세요.", e);
			
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}

}
