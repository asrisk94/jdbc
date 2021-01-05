package member.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.model.vo.Member;



/*
 * DAO
 * 
 * Data Access Object
 * 
 * 1. jdbc driver 클래스 등록 (dbms별로 제공) : 최초 1회
 * 2. DB connection 객체 생성 : db server url, user, password 필요
 * 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
 * 4. 쿼리 전송 (실행) - 결과값 받아냄
 *   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
 * 5. 트랜젝션 처리 (commit, rollback)
 * 6. 자원반납
 * 
 * 자바 DAO 클래스 코딩시 순서대로 따라가면 된다.
 */

public class MemberDAO {
	
	// 드라이버
	private String driverClass = "oracle.jdbc.OracleDriver";
	
	// url 주소 : 사용드라이버@ip주소:port:sid  (sid는 접속 db명을 의미)
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	
	// sql 쿼리문을 실행해줄 계정 id 및 password
	private String user = "student";
	private String password = "student";
	
	
	
//	1. jdbc driver 클래스 등록 (dbms별로 제공) : 최초 1회
	public MemberDAO() {
		try {
			Class.forName(driverClass);		// 클래스 가져옴.
		} catch (ClassNotFoundException e) {
			System.out.println("ojdbc6.jar를 확인하세요.");
			e.printStackTrace();
		}
	}
	
	// 이후 필요에 따라 아래 과정을 적절히 구현한다.
	
//	 2. DB connection 객체 생성 : db server url, user, password 필요
//	 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
//	 4. 쿼리 전송 (실행) - 결과값 받아냄
//	   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
//	 5. 트랜젝션 처리 (commit, rollback)
//	 6. 자원반납
	
	
	
	// 정보 전체 조회 메소드 (1)
	public List<Member> selectAll() {
		
		Connection conn = null;				// db 계정 연결 클래스
		PreparedStatement pstmt = null;		// 향상된 statement 클래스. sql구문 전달 역할
		String sql = "select * from member order by enroll_date desc";
		ResultSet rset = null;
		List<Member> list = null;
		
		try {
//		 2. DB connection 객체 생성 : db server url, user, password 필요
			conn = DriverManager.getConnection(url, user, password);
			
			// 단순 조회이기 때문에 setAutoCommit(false) 안해도 된다.
			
//				 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql); // 미완성 쿼리 전달
			
//				 4. 쿼리 전송 (실행) - 결과값 받아냄
			// DQL select문인 경우에는 executeQuery() 호출 -> ResultSet 반환
			rset = pstmt.executeQuery();
			
//				   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
			// resultSet은 connection 자원 반납시에 같이 닫힘. 따라서 list에 옮겨 담아놓을 필요.
			list = new ArrayList<>();
			while(rset.next()) {				// resultSet은 한 행씩 접근 가능.
				Member member = new Member();
				member.setMemberId(rset.getString("member_id"));	// db 컬럼명을 대소문자 구분없이 사용
				member.setPassword(rset.getString("password"));
				member.setMemberName(rset.getString("member_name"));
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
				
				// list에 추가
				list.add(member);
			}
			
//				 5. 트랜젝션 처리 (commit, rollback)
			// db상에 바뀐게 없으므로 불필요
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
//		 6. 자원반납
				rset.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
		
	} // (1) 메소드 괄호
	
	
	
	// 회원 조회 메소드 (2)
	public Member selectMember(String memberId) {
		
		Connection conn = null;				// db 계정 연결 클래스
		PreparedStatement pstmt = null;		// 향상된 statement 클래스. sql구문 전달 역할
		String sql = "select * from member where member_id = ?";
		ResultSet rset = null;
		Member member = null;
		
		try {
//		 2. DB connection 객체 생성 : db server url, user, password 필요
			conn = DriverManager.getConnection(url, user, password);
			
//		 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, memberId);
			
//		 4. 쿼리 전송 (실행) - 결과값 받아냄
			rset = pstmt.executeQuery();
			System.out.println(rset);
			
//		   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
			member = new Member();						// ***** 안해주면 member 안이 비어서 안 담긴다 *****
			
			rset.next();
			member.setMemberId(rset.getString("member_id"));	// db 컬럼명을 대소문자 구분없이 사용
			member.setPassword(rset.getString("password"));
			member.setMemberName(rset.getString("member_name"));
			member.setGender(rset.getString("gender"));
			member.setAge(rset.getInt("age"));
			member.setEmail(rset.getString("email"));
			member.setPhone(rset.getString("phone"));
			member.setAddress(rset.getString("address"));
			member.setHobby(rset.getString("hobby"));
			member.setEnrollDate(rset.getDate("enroll_date"));
			
		} catch (NullPointerException e) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
//		 6. 자원반납
				rset.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return member;
		
	} // (2) 메소드 괄호
	
	
	
	// 회원 이름 조회 메소드 (3)
	public List<Member> selectName(String name) {
		
		Connection conn = null;				// db 계정 연결 클래스
		PreparedStatement pstmt = null;		// 향상된 statement 클래스. sql구문 전달 역할
		String sql = "select * from member where member_name = ? order by member_id";
		ResultSet rset = null;
		List<Member> list = null;
		
		try {
//		 2. DB connection 객체 생성 : db server url, user, password 필요
			conn = DriverManager.getConnection(url, user, password);
			
			// 단순 조회이기 때문에 setAutoCommit(false) 안해도 된다.
			
//				 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql); // 미완성 쿼리 전달
			pstmt.setString(1, name);
			
//				 4. 쿼리 전송 (실행) - 결과값 받아냄
			// DQL select문인 경우에는 executeQuery() 호출 -> ResultSet 반환
			rset = pstmt.executeQuery();
			
//				   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
			// resultSet은 connection 자원 반납시에 같이 닫힘. 따라서 list에 옮겨 담아놓을 필요.
			list = new ArrayList<>();
			while(rset.next()) {				// resultSet은 한 행씩 접근 가능.
				Member member = new Member();
				member.setMemberId(rset.getString("member_id"));	// db 컬럼명을 대소문자 구분없이 사용
				member.setPassword(rset.getString("password"));
				member.setMemberName(rset.getString("member_name"));
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
				
				// list에 추가
				list.add(member);
			}
			
//				 5. 트랜젝션 처리 (commit, rollback)
			// db상에 바뀐게 없으므로 불필요
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
//		 6. 자원반납
				rset.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
		
	} // (3) 메소드 괄호
	
	
	// 회원가입 (sql 행추가) 메소드 (4)
	public int insertMember(Member member) {
		
		Connection conn = null;				// db 계정 연결 클래스
		PreparedStatement pstmt = null;		// 향상된 statement 클래스. sql구문 전달 역할
		String sql = "insert into member values(?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate)";
		int result = 0;
		
		try {
//		 2. DB connection 객체 생성 : db server url, user, password 필요
			conn = DriverManager.getConnection(url, user, password);
			// 자동커밋 사용 안함. (트랜잭션 처리는 java 앱에서 주도적으로 처리하겠음.)
			conn.setAutoCommit(false);
			
//		 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql); // 미완성 쿼리 전달
			
			// Statement 객체 생성후 ?에 값대입 쿼리 완성
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender());
			pstmt.setInt(5, member.getAge());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getAddress());
			pstmt.setString(9, member.getHobby());
			
//		 4. 쿼리 전송 (실행) - 결과값 받아냄
			// DML인 경우는 int값이 리턴됨 (처리된 행의 수)
			result = pstmt.executeUpdate();	// DML인 경우 executeUpdate() - 쿼리 전송,실행 및 int값 반환 메소드
											// create, drop에서는 -1을 반환.
//		   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
			// 지금은 select문이 아니므로 필요 없음.
			
//		 5. 트랜젝션 처리 (commit, rollback)
			if(result > 0) conn.commit();
			else conn.rollback();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
//		 6. 자원반납 (생셩 역순. pstmt - conn 순으로 반납)
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {		// close때에는 그냥 Exception으로 해주면 좋다.
				e.printStackTrace();
			}
		}
		
		return result;	// 처리된 행 수를 나타내는 result값을 반환 (잘 적용됐는지 판별할 용도)
		
	} // (4) 메소드 괄호



	// 회원 수정 메소드 (5번)
	public int updateMember(Member member) {
		
		Connection conn = null;				// db 계정 연결 클래스
		PreparedStatement pstmt = null;		// 향상된 statement 클래스. sql구문 전달 역할
		String sql = "update member set "
				+ "password = ?, "
				+ "member_name = ?, "
				+ "gender = ?, "
				+ "age = ?, "
				+ "email = ?, "
				+ "phone = ?, "
				+ "address = ?, "
				+ "hobby = ? "
				+ "where member_id = ?";		// set절에 id와 enroll_date는 없기 때문에 안바뀐다.
		int result = 0;
		
		try {
//		 2. DB connection 객체 생성 : db server url, user, password 필요
			conn = DriverManager.getConnection(url, user, password);
			
			// 단순 조회이기 때문에 setAutoCommit(false) 안해도 된다.
			conn.setAutoCommit(false);
			
//				 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql); // 미완성 쿼리 전달
			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getMemberName());
			pstmt.setString(3, member.getGender());
			pstmt.setInt(4, member.getAge());
			pstmt.setString(5, member.getEmail());
			pstmt.setString(6, member.getPhone());
			pstmt.setString(7, member.getAddress());
			pstmt.setString(8, member.getHobby());
			pstmt.setString(9, member.getMemberId());	// where절 조회 위한 아이디
			
//				 4. 쿼리 전송 (실행) - 결과값 받아냄
			// DQL select문인 경우에는 executeQuery() 호출 -> ResultSet 반환
			result = pstmt.executeUpdate();
			
//				   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
			// select 아니므로 필요 없음.
			
//				 5. 트랜젝션 처리 (commit, rollback)
			if(result > 0) {
				conn.rollback();	// 테스트용. 실제 삭제시에는 commit으로 바꿀 것.
			}
			else {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
//		 6. 자원반납
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	} // (5) 메소드 괄호



	// 회원 삭제 메소드 (6번)
	public int deleteMember(String memberId) {
		
		Connection conn = null;				// db 계정 연결 클래스
		PreparedStatement pstmt = null;		// 향상된 statement 클래스. sql구문 전달 역할
		String sql = "delete from member where member_id = ?";
		int result = 0;
		
			try {
//		 2. DB connection 객체 생성 : db server url, user, password 필요
				conn = DriverManager.getConnection(url, user, password);
				
				// 단순 조회이기 때문에 setAutoCommit(false) 안해도 된다.
				conn.setAutoCommit(false);
				
//				 3. 쿼리문 생성 및 Statement 객체(PreparedStatement) 생성
				pstmt = conn.prepareStatement(sql); // 미완성 쿼리 전달
				pstmt.setString(1, memberId);
				
//				 4. 쿼리 전송 (실행) - 결과값 받아냄
				// DQL select문인 경우에는 executeQuery() 호출 -> ResultSet 반환
				result = pstmt.executeUpdate();
				
//				   4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
				// select 아니므로 필요 없음.
				
//				 5. 트랜젝션 처리 (commit, rollback)
				if(result > 0) {
					conn.rollback();		// 테스트용. 실제 삭제시에는 commit으로 바꿀 것.
				}
				else {
					conn.rollback();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
//		 6. 자원반납
					pstmt.close();
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		return result;
		
	} // (6) 메소드 괄호
	
}
