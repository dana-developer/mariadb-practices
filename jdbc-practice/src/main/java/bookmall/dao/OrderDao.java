package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;

public class OrderDao {

	public void insert(OrderVo orderVo) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into orders (number, status, payment, delivery_address, user_no) values (?,?,?,?,?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		){						
			pstmt1.setString(1, orderVo.getNumber());
			pstmt1.setString(2, orderVo.getStatus());
			pstmt1.setInt(3, orderVo.getPayment());
			pstmt1.setString(4, orderVo.getShipping());
			pstmt1.setLong(5, orderVo.getUserNo());

			pstmt1.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			orderVo.setNo(rs.next() ? rs.getLong(1) : null);
			rs.close();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
		
	}
	
	public void insertBook(OrderBookVo orderBookVo) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("insert into orders_book (book_no, orders_no, quantity, price) values (?,?,?,?)");
		){			
			pstmt.setLong(1, orderBookVo.getBookNo());
			pstmt.setLong(2, orderBookVo.getOrderNo());
			pstmt.setInt(3, orderBookVo.getQuantity());
			pstmt.setInt(4, orderBookVo.getPrice());

			pstmt.executeUpdate();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
	}

	public void deleteByNo(Long no) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from orders where no = ?");
		){						
			pstmt.setLong(1, no);
			pstmt.executeUpdate();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}		
	}

	public void deleteBooksByNo(Long no) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from orders_book where orders_no = ?");
		){			
			pstmt.setLong(1, no);
			pstmt.executeUpdate();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
		} 
	}

	public OrderVo findByNoAndUserNo(Long no, Long userNo) {
		OrderVo result = null;

		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select number, status, payment, delivery_address from orders where no = ? and user_no = ?");
		){						
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String number = rs.getString(1);
				String status = rs.getString(2);
				int payment = rs.getInt(3);
				String shipping = rs.getString(4);
				
				OrderVo vo = new OrderVo(no, userNo, number, status, payment, shipping);
				result = vo;
			}
			
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
		
		return result;		
	}

	public List<OrderBookVo> findBooksByNoAndUserNo(Long no, Long userNo) {
		List<OrderBookVo> result = new ArrayList<>();
		
		String sql = "select b.orders_no, b.price, b.book_no, b.quantity, c.title" +
				      " from orders a" +
				      " join orders_book b on a.no = b.orders_no" +
				      " join book c on b.book_no = c.no" +
				     " where a.no = ? and a.user_no = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
		){
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long orderNo = rs.getLong(1);
				int price = rs.getInt(2);
				Long bookNo = rs.getLong(3);
				int quantity = rs.getInt(4);
				String bookTitle = rs.getString(5);
				
				OrderBookVo vo = new OrderBookVo(no,orderNo, bookNo, quantity, price, bookTitle);
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
