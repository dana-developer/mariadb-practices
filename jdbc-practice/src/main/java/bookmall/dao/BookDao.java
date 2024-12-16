package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bookmall.vo.BookVo;

public class BookDao {
	
	public void insert(BookVo bookVo) {
		
		try(
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into book (title, price, category_no) values (?, ?, ?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {			
			pstmt1.setString(1, bookVo.getTitle());
			pstmt1.setInt(2, bookVo.getPrice());
			pstmt1.setLong(3, bookVo.getCategoryNo());

			pstmt1.executeUpdate();
					
			ResultSet rs = pstmt2.executeQuery();
			bookVo.setNo(rs.next() ? rs.getLong(1) : null);
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
	}

	public void deleteByNo(Long no) {
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from book where no = ?");
		){
			pstmt.setLong(1, no);
			pstmt.executeUpdate();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
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
}
