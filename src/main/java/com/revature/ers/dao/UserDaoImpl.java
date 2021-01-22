package com.revature.ers.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.revature.ers.models.Role;
import com.revature.ers.models.User;
import com.revature.ers.util.ConnectionUtil;

public class UserDaoImpl implements CrudDao<User>{

	private static Logger logger = LogManager.getLogger(UserDaoImpl.class);
	
	@Override
	public User insert(User t) {
		User result = null;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "INSERT INTO users (username, password, first_name, last_name, role_id) VALUES "
						+ "(?,?,?,?,(SELECT role_id FROM roles WHERE role_name = ?)) RETURNING user_id;";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, t.getUsername());
			ps.setString(2, t.getPassword());
			ps.setString(3, t.getFirstName());
			ps.setString(4, t.getLastName());
			ps.setString(5, t.getRole().toString());
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = new User(rs.getInt("user_id"), t.getUsername(), t.getPassword(), t.getFirstName(), t.getLastName(), t.getRole());
			}
		}catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

	@Override
	public User selectById(int id) {
		User result = null;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users INNER JOIN roles ON users.role_id=roles.role_id WHERE user_id = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), Role.valueOf(rs.getString("role_name")));
			}
		}catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}
	
	public User selectByCredentials(String username, String password) {
		User result = null;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users INNER JOIN roles ON users.role_id=roles.role_id WHERE username = ? AND password = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), Role.valueOf(rs.getString("role_name")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<User> selectAll() {
		List<User> result = new ArrayList<User>();
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM users INNER JOIN roles ON users.role_id=roles.role_id";
			
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), Role.valueOf(rs.getString("role_name"))));
			}
		}catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

	@Override
	public boolean update(User t) {
		boolean result = false;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "UPDATE users SET username=?, password=?, first_name=?, last_name=?, role_id=(SELECT role_id FROM roles WHERE role_name = ?)"
					+ "WHERE user_id=?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, t.getUsername());
			ps.setString(2, t.getPassword());
			ps.setString(3, t.getFirstName());
			ps.setString(4, t.getLastName());
			ps.setString(5, t.getRole().toString());
			ps.setInt(6, t.getId());
			
			int resultInt = ps.executeUpdate();
			ps.close();
			if(resultInt > 0) {
				result = true;
			}
		}catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

	@Override
	public boolean delete(User t) {
		boolean result = false;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "DELETE FROM users WHERE user_id=? AND username=?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getId());
			ps.setString(2, t.getUsername());
			
			int resultInt = ps.executeUpdate();
			ps.close();
			if(resultInt > 0) {
				result = true;
			}
		}catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

}
