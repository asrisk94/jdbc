package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * DB Connection 객체 생성
 * 
 * 트랜잭션처리
 * 자원반납
 * 관련한 공통코드를 작성(예외처리 포함)
 * 
 * static 메소드로 작성해서, 객체생성없이 바로 호출 가능하도록.
 * 
 */



public class JDBCTemplate {
	
	
	
	public static Connection getConnection() {
		
		String driverClass = "oracle.jdbc.OracleDriver";
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "student";
		String password = "student";
		
		try {
			// 1. jdbc driver 클래스 등록 (dbms별로 제공) : 최초 1회
			Class.forName(driverClass);
			// 2. DB connection 객체 생성 : db server url, user, password 필요
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
		
	} // 겟커넥션 메소드 괄호

	
	// 커밋
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed())	// 내용물이 널이 아니고 아직 닫히지 않았을 때
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 롤백
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed())
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	
	// 클로즈 (커넥션)
	public static void close(Connection conn) {

		try {
			if(conn != null && !conn.isClosed())
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 클로즈 pstmt
	public static void close(PreparedStatement pstmt) {
		
		try {
			if(pstmt != null && !pstmt.isClosed())
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 클로즈 리절트셋
	public static void close(ResultSet rset) {
		
		try {
			if(rset != null && !rset.isClosed())
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
} // 클래스 괄호
