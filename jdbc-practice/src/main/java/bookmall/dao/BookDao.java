package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bookmall.vo.BookVo;

public class BookDao {
	
	public void insert(BookVo bookVo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			
			String sql = "insert into book (title, price, category_no) values (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, bookVo.getTitle());
			pstmt.setInt(2, bookVo.getPrice());
			pstmt.setLong(3, bookVo.getCategoryNo());

			pstmt.executeUpdate();
					
			String sql2 = "select last_insert_id() from dual ";
			pstmt2 = conn.prepareStatement(sql2);
			rs = pstmt2.executeQuery();
			
			if(rs.next()) {
				bookVo.setNo(rs.getLong(1));
			}
		} catch(SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				System.out.println("error : " + e);
			}
		}
	}

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.0.13:3306/bookmall";
			conn = DriverManager.getConnection(url, "bookmall", "bookmall");
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패 : " + e);
		}
		
		return conn;	
	}

	public void deleteByNo(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "delete"
					   + "  from book"
					   + " where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);

			pstmt.executeUpdate();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				System.out.println("error : " + e);
			}
		}		
	}

	
}
