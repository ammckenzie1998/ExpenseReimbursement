package com.revature.ers.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.revature.ers.models.Ticket;
import com.revature.ers.models.TicketStatus;
import com.revature.ers.models.TicketType;
import com.revature.ers.util.ConnectionUtil;

public class TicketDaoImpl implements CrudDao<Ticket>{

	private static Logger logger = LogManager.getLogger(TicketDaoImpl.class);
	
	@Override
	public Ticket insert(Ticket t) {
		Ticket result = null;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "INSERT INTO tickets(status, user_id, reimbursement_type, amount, description) VALUES "
						+ "(?,?,?,?,?) RETURNING ticket_id, timestamp;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, t.getStatus().toString());
			ps.setInt(2, t.getUserID());
			ps.setString(3, t.getTicketType().toString());
			ps.setBigDecimal(4, t.getAmount());
			ps.setString(5, t.getDescription());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = new Ticket(rs.getInt("ticket_id"), t.getStatus(), t.getUserID(), t.getTicketType(), t.getAmount(), t.getDescription(), rs.getTimestamp("timestamp"));
			}
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

	@Override
	public Ticket selectById(int id) {
		Ticket result = null;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM tickets WHERE ticket_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result = new Ticket(rs.getInt("ticket_id"), TicketStatus.valueOf(rs.getString("status")), rs.getInt("user_id"), TicketType.valueOf(rs.getString("reimbursement_type")), rs.getBigDecimal("amount"),rs.getString("description"), rs.getTimestamp("timestamp"));
			}
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}
	
	public List<Ticket> selectByUserId(int id) {
		List<Ticket> result = new ArrayList<Ticket>();
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM tickets WHERE user_id=? ORDER BY (CASE WHEN status='PENDING' THEN 1 ELSE 2 END), timestamp DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add(new Ticket(rs.getInt("ticket_id"), TicketStatus.valueOf(rs.getString("status")), rs.getInt("user_id"), TicketType.valueOf(rs.getString("reimbursement_type")), rs.getBigDecimal("amount"), rs.getString("description"), rs.getTimestamp("timestamp")));
			}
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}
	
	public List<Ticket> selectByStatus(String status){
		List<Ticket> result = new ArrayList<Ticket>();
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM tickets WHERE status=? ORDER BY timestamp DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add(new Ticket(rs.getInt("ticket_id"), TicketStatus.valueOf(rs.getString("status")), rs.getInt("user_id"), TicketType.valueOf(rs.getString("reimbursement_type")), rs.getBigDecimal("amount"), rs.getString("description"), rs.getTimestamp("timestamp")));
			}
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

	@Override
	public List<Ticket> selectAll() {
		List<Ticket> result = new ArrayList<Ticket>();
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM tickets ORDER BY (CASE WHEN status='PENDING' THEN 1 ELSE 2 END), timestamp DESC;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result.add(new Ticket(rs.getInt("ticket_id"), TicketStatus.valueOf(rs.getString("status")), rs.getInt("user_id"), TicketType.valueOf(rs.getString("reimbursement_type")), rs.getBigDecimal("amount"), rs.getString("description"), rs.getTimestamp("timestamp")));
			}
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

	@Override
	public boolean update(Ticket t) {
		boolean result = false;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "UPDATE tickets SET status=?, user_id=?, reimbursement_type=?, amount=?, description=?, timestamp=? "
						+ "WHERE ticket_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, t.getStatus().toString());
			ps.setInt(2, t.getUserID());
			ps.setString(3, t.getTicketType().toString());
			ps.setBigDecimal(4, t.getAmount());
			ps.setString(5, t.getDescription());
			ps.setTimestamp(5, null);
			ps.setInt(6, t.getId());
			
			int resultInt = ps.executeUpdate();
			ps.close();
			if(resultInt > 0) {
				result = true;
			}
			
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}
	
	public boolean approveTicket(Ticket t) {
		boolean result = true;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "CALL approve_ticket(?);";
			CallableStatement cs = conn.prepareCall(sql);
			
			cs.setInt(1, t.getId());
			cs.executeUpdate();
			cs.close();
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
			result = false;
		}
		return result;
	}
	
	public boolean denyTicket(Ticket t) {
		boolean result = true;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "CALL deny_ticket(?);";
			CallableStatement cs = conn.prepareCall(sql);
			
			cs.setInt(1, t.getId());
			cs.executeUpdate();
			cs.close();
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
			result = false;
		}
		return result;
	}

	@Override
	public boolean delete(Ticket t) {
		boolean result = false;
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "DELETE FROM tickets WHERE ticket_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, t.getId());
			int resultInt = ps.executeUpdate();
			ps.close();
			if(resultInt > 0) {
				result = true;
			}
		} catch(SQLException e) {
			logger.error("SQLException Occurred");
		}
		return result;
	}

}
