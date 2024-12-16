package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.CategoryVo;

public class CategoryDao {

	public void insert(CategoryVo categoryVo) {
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("insert into category (name) values (?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		){
			pstmt.setString(1, categoryVo.getName());
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			categoryVo.setNo(rs.next() ? rs.getLong(1) : null);
			rs.close();
			
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
	}

	public void deleteByNo(Long no) {
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from category where no = ?");
		){			
			pstmt.setLong(1, no);
			pstmt.executeUpdate();
						
		} catch(SQLException e) {
			System.out.println("error : " + e);
		}
	}

	public List<CategoryVo> findAll() {
		List<CategoryVo> result = new ArrayList<>();

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select no, name from category order by no desc");
		){			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString(2);
				CategoryVo vo = new CategoryVo(name);
				
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
