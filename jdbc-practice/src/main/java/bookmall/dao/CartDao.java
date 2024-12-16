package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.CartVo;

public class CartDao {

	public void insert(CartVo cartVo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			
			String sql = "insert into cart (user_no, book_no, quantity) values (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, cartVo.getUserNo());
			pstmt.setLong(2, cartVo.getBookNo());
			pstmt.setInt(3, cartVo.getQuantity());

			pstmt.executeUpdate();
			
			String sql2 = "select last_insert_id() from dual ";
			pstmt2 = conn.prepareStatement(sql2);
			rs = pstmt2.executeQuery();
			
			if(rs.next()) {
				cartVo.setNo(rs.getLong(1));
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

	public void deleteByUserNoAndBookNo(Long userNo, Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "delete"
					   + "  from cart"
					   + " where user_no = ? and book_no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, userNo);
			pstmt.setLong(2, no);

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

	public List<CartVo> findByUserNo(Long userNo) {
		List<CartVo> result = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select cart.no, book_no, quantity, book.title" +
						  " from cart" +
						  " join book on cart.book_no = book.no" +
						 " where cart.user_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, userNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long no = rs.getLong(1);
				Long bookNo = rs.getLong(2);
				int quantity = rs.getInt(3);
				String bookTitle = rs.getString(4);
				
				CartVo vo = new CartVo(no, userNo, bookNo, quantity, bookTitle);
				result.add(vo);
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
		
		return result;		
	}
}
