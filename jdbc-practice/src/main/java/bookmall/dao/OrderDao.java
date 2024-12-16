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
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			
			String sql = "insert into orders (number, status, payment, delivery_address, user_no) values (?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, orderVo.getNumber());
			pstmt.setString(2, orderVo.getStatus());
			pstmt.setInt(3, orderVo.getPayment());
			pstmt.setString(4, orderVo.getShipping());
			pstmt.setLong(5, orderVo.getUserNo());

			pstmt.executeUpdate();
			
			String sql2 = "select last_insert_id() from dual ";
			pstmt2 = conn.prepareStatement(sql2);
			rs = pstmt2.executeQuery();
			
			if(rs.next()) {
				orderVo.setNo(rs.getLong(1));
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
	
	public void insertBook(OrderBookVo orderBookVo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "insert into orders_book (book_no, orders_no, quantity, price) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, orderBookVo.getBookNo());
			pstmt.setLong(2, orderBookVo.getOrderNo());
			pstmt.setInt(3, orderBookVo.getQuantity());
			pstmt.setInt(4, orderBookVo.getPrice());

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
					   + "  from orders"
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

	public void deleteBooksByNo(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "delete"
					   + "  from orders_book"
					   + " where orders_no = ?";
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

	public OrderVo findByNoAndUserNo(Long no, Long userNo) {
		OrderVo result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select number, status, payment, delivery_address" +
						  " from orders" +
						 " where no = ? and user_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String number = rs.getString(1);
				String status = rs.getString(2);
				int payment = rs.getInt(3);
				String shipping = rs.getString(4);
				
				OrderVo vo = new OrderVo(no, userNo, number, status, payment, shipping);
				result = vo;
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

	public List<OrderBookVo> findBooksByNoAndUserNo(Long no, Long userNo) {
		List<OrderBookVo> result = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();

			String sql = "select b.orders_no, b.price, b.book_no, b.quantity, c.title" +
						  " from orders a" +
						  " join orders_book b on a.no = b.orders_no" +
						  " join book c on b.book_no = c.no" +
						 " where a.no = ? and a.user_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long orderNo = rs.getLong(1);
				int price = rs.getInt(2);
				Long bookNo = rs.getLong(3);
				int quantity = rs.getInt(4);
				String bookTitle = rs.getString(5);
				
				OrderBookVo vo = new OrderBookVo(no,orderNo, bookNo, quantity, price, bookTitle);
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
