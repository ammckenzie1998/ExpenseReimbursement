package com.revature.ers.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.revature.ers.models.Role;
import com.revature.ers.models.Ticket;
import com.revature.ers.service.ExpenseReimbursementService;
import com.revature.ers.util.UserUtil;

public class PortalController {

	private static Logger logger = LogManager.getLogger(PortalController.class);
	
	public static void processPortal(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		if(req.getSession(false) != null && UserUtil.getUser() != null) {
			if(UserUtil.getUser().getRole() == Role.EMPLOYEE) {
				RequestDispatcher redis = req.getRequestDispatcher("/EmployeePortal.html");
				redis.forward(req, resp);
			} else if(UserUtil.getUser().getRole() == Role.FINANCIAL_MANAGER) {
				RequestDispatcher redis = req.getRequestDispatcher("/ManagerPortal.html");
				redis.forward(req, resp);
			} else{
				resp.sendRedirect("http://localhost:8080/ExpenseReimbursementSystem/m/logout");
			}
		} else {
			resp.sendRedirect("http://localhost:8080/ExpenseReimbursementSystem/m/login");
		}
		
	}
	
	public static void processTicket(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		if(req.getSession(false) != null && UserUtil.getUser() != null) {
			switch(req.getMethod()) {
				case "POST": doPost(req, resp);
					break;
				case "GET" : doGet(req, resp);
					break;
				case "PUT" : doPut(req, resp);
					break;
				case "DELETE": doDelete(req, resp);
					break;
				default: resp.setStatus(405);	// METHOD NOT ALLOWED
					break;
				}
		} else {
			resp.setStatus(403);	// FORBIDDEN
		}
	}
	
	private static void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		try {
			ObjectMapper om = new ObjectMapper();
			Ticket t = om.readValue(req.getReader(), com.revature.ers.models.Ticket.class);
			if(ExpenseReimbursementService.addTicket(t)) {
				resp.setStatus(201);	// CREATED
				logger.info("Submitted a ticket.");
			} else {
				resp.setStatus(403);	// FORBIDDEN
			}
		} catch(InvalidFormatException e) {
			resp.setStatus(406);	// NOT ACCEPTABLE
		}
	}
	
	
	private static void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		List<Ticket> ticketList = null;
		resp.setContentType("application/json");
		
		if((UserUtil.getUser().getRole() == Role.FINANCIAL_MANAGER) && (req.getParameter("show") != null)){
			ticketList = ExpenseReimbursementService.viewAllTickets(req.getParameter("show"));
			logger.info("Obtained all tickets.");
		} else {
			ticketList = ExpenseReimbursementService.viewMyTickets();
			logger.info("Obtained user's tickets.");
		}
		ObjectMapper om = new ObjectMapper();
		resp.getWriter().write(om.writeValueAsString(ticketList));
	}
	
	private static void doPut(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		if(UserUtil.getUser().getRole() == Role.FINANCIAL_MANAGER) {
			try {
				ObjectMapper om = new ObjectMapper();
				Ticket t = om.readValue(req.getReader(), com.revature.ers.models.Ticket.class);
				if(ExpenseReimbursementService.updateTicket(t)) {
					resp.setStatus(201);	// CREATED
					logger.info("Updated a ticket.");
				} else {
					resp.setStatus(406);	// NOT ACCEPTABLE
				}
			} catch(InvalidFormatException e) {
				resp.setStatus(400);	// BAD REQUEST
			}
		} else {
			resp.setStatus(403); // FORBIDDEN
		}
	}
	
	private static void doDelete(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
	}
}
