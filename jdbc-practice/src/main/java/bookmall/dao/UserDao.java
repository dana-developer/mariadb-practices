package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.UserVo;

public class UserDao {

	public void insert(UserVo userVo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			
			String sql = "insert into user (name, phone_number, email, password) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userVo.getName());
			pstmt.setString(2, userVo.getPhoneNumber());
			pstmt.setString(3, userVo.getEmail());
			pstmt.setString(4, userVo.getPassword());

			pstmt.executeUpdate();
			
			String sql2 = "select last_insert_id() from dual ";
			pstmt2 = conn.prepareStatement(sql2);
			rs = pstmt2.executeQuery();
			
			if(rs.next()) {
				userVo.setNo(rs.getLong(1));
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
	
	public List<UserVo> findAll() {
		List<UserVo> result = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select no, name, phone_number, email, password" +
						  " from user" + 
					  " order by no desc";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString(2);
				String phoneNumber = rs.getString(3);
				String email = rs.getString(4);
				String password = rs.getString(5);
				
				UserVo vo = new UserVo(name, email, password, phoneNumber);
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
					   + "  from user"
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
