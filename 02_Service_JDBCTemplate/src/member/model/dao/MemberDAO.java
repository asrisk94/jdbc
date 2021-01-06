package member.model.dao;

import static common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.model.vo.Member;

/*
 * -> 초반 처리 Service 담당
 * 
* 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
* 4. 쿼리 전송 (실행) - 결과값 받아냄
*   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
* 5. 자원반납(PreparedStatement, ResultSet) Connection은 service에서
* 
* -> 끝 처리 Service 담당
*/

public class MemberDAO {
	
	
	
	// 전체 조회
	public List<Member> selectAll(Connection conn) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = null;
		String sql = "select * from member";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			list = new ArrayList<>();
			
			while(rset.next()) {
				Member member = new Member();
				member.setMemberId(rset.getString("member_id"));
				member.setPassword(rset.getString("password"));
				member.setMemberName(rset.getString("member_name"));
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
				
				list.add(member);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

	
	
	// 아이디로 한명 조회
	public Member selecOneMember(Connection conn, String memberId) {
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where member_id = ?";
		Member member = new Member();
		
		try {
			// 1. PreparedStatement 생성, 미완성 쿼리 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			// 2. 실행 및 ResultSet 값 -> member 객체
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 3. 자원반납
			close(rset);
			close(pstmt);
		}
		
		return member;
	}


	
	// 이름으로 여럿 조회
	public List<Member> selectMemberName(Connection conn, String memberName) {
		
		PreparedStatement pstmt = null;
		List<Member> list = null;
		ResultSet rset = null;
		String sql = "select * from member where member_name like \'%\' || ? || \'%\'";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberName);
			
			rset = pstmt.executeQuery();
			
			list = new ArrayList<>();
			while(rset.next()) {
				Member member = new Member();
				member.setMemberId(rset.getString("member_id"));
				member.setPassword(rset.getString("password"));
				member.setMemberName(rset.getString("member_name"));
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
				
				list.add(member);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}
	
	
	
	// 회원 추가
	public int insertMember(Connection conn, Member inputMember) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "insert into member "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, default)";
		
		try {
			// 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, inputMember.getMemberId());
			pstmt.setString(2, inputMember.getPassword());
			pstmt.setString(3, inputMember.getMemberName());
			pstmt.setString(4, inputMember.getGender());
			pstmt.setInt(5, inputMember.getAge());
			pstmt.setString(6, inputMember.getEmail());
			pstmt.setString(7, inputMember.getPhone());
			pstmt.setString(8, inputMember.getAddress());
			pstmt.setString(9, inputMember.getHobby());
			
			// 4. 쿼리 전송 (실행) - 결과값 받아냄
			result = pstmt.executeUpdate();
			//   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 5. 자원반납(PreparedStatement, ResultSet) Connection은 service에서
			close(pstmt);
		}
		
		return result;
	}

	
	
	// 수정
	public int updateMember(Connection conn, Member member, String columnName, String content) {

		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "update member set " + columnName + " = ? where member_id = ?"; 
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content);					// 새로 들어갈 내용
			pstmt.setString(2, member.getMemberId());		// 행 식별을 위한 대상 아이디
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
	
	// 회원 삭제
	public int deleteMember(Connection conn, String inputMemberId) {
		
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "delete from member where member_id = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputMemberId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

}
