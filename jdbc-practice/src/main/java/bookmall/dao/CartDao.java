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
	
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into cart (user_no, book_no, quantity) values (?, ?, ?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		){	
			pstmt1.setLong(1, cartVo.getUserNo());
			pstmt1.setLong(2, cartVo.getBookNo());
			pstmt1.setInt(3, cartVo.getQuantity());
			
			pstmt1.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			cartVo.setNo(rs.next() ? rs.getLong(1) : null);
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
	}

	public void deleteByUserNoAndBookNo(Long userNo, Long no) {
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from cart where user_no = ? and book_no = ?");
		){
			pstmt.setLong(1, userNo);
			pstmt.setLong(2, no);
			pstmt.executeUpdate();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
	}

	public List<CartVo> findByUserNo(Long userNo) {
		List<CartVo> result = new ArrayList<>();
		
		String sql = "select cart.no, book_no, quantity, book.title" +
				      " from cart" +
				      " join book on cart.book_no = book.no" +
				     " where cart.user_no = ?";
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
		){			
			pstmt.setLong(1, userNo);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long no = rs.getLong(1);
				Long bookNo = rs.getLong(2);
				int quantity = rs.getInt(3);
				String bookTitle = rs.getString(4);
				
				CartVo vo = new CartVo(no, userNo, bookNo, quantity, bookTitle);
				result.add(vo);
			}
			
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
		
		return result;		
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
}
