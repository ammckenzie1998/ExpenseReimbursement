package com.revature.ers.service;

import java.util.List;

import com.revature.ers.dao.TicketDaoImpl;
import com.revature.ers.dao.UserDaoImpl;
import com.revature.ers.models.Role;
import com.revature.ers.models.Ticket;
import com.revature.ers.models.TicketStatus;
import com.revature.ers.models.TicketType;
import com.revature.ers.models.User;
import com.revature.ers.util.UserUtil;

public class ExpenseReimbursementService {
	
	public static TicketDaoImpl ticketDao = new TicketDaoImpl();
	public static UserDaoImpl userDao = new UserDaoImpl();
	
	
	public static User login(String username, String password) {
		User u = userDao.selectByCredentials(username, password);
		if(u != null) {
			UserUtil.login(u);
		}
		return u;
	}
	
	public static boolean addTicket(Ticket t) {
		boolean success = false;
		t.setUserID(UserUtil.getUser().getId());
		
		if(t.getStatus() == null) {
			t.setStatus(TicketStatus.PENDING);
		}
		
		if(t.getTicketType() == null) {
			t.setTicketType(TicketType.OTHER);
		}
		
		if(ticketDao.insert(t) != null) {
			success = true;
		}
		return success;
	}
	
	public static List<Ticket> viewMyTickets() {
		List<Ticket> result = null;
		result = ticketDao.selectByUserId(UserUtil.getUser().getId());
		return result;
	}
	
	public static List<Ticket> viewAllTickets(String arg){
		List<Ticket> result = null;
		if(UserUtil.getUser().getRole() == Role.FINANCIAL_MANAGER) {
			switch(arg) {
				case "all": result = ticketDao.selectAll();
					break;
				case "pending": result = ticketDao.selectByStatus("PENDING");
					break;
				case "approved": result = ticketDao.selectByStatus("APPROVED");
					break;
				case "denied": result = ticketDao.selectByStatus("DENIED");
					break;
				default: result = viewMyTickets();
					break;	
			}
		}
		return result;
	}
	
	public static boolean updateTicket(Ticket t) {
		boolean result = false;
		if(t.getStatus() == null) {
			result = ticketDao.update(t);
		} else if(t.getStatus() == TicketStatus.APPROVED) {
			result = ticketDao.approveTicket(t);
		} else if(t.getStatus() == TicketStatus.DENIED) {
			result = ticketDao.denyTicket(t);
		}
		return result;
	}

}
