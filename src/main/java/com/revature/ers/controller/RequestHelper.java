package com.revature.ers.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.revature.ers.util.UserUtil;

public class RequestHelper {

	private static Logger logger = LogManager.getLogger(RequestHelper.class);
	
	public static void process(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException{
		
		String endpoint = req.getRequestURI();
		logger.debug("Request directed to endpoint: " + endpoint);
		
		switch(endpoint) {
			case "/ExpenseReimbursementSystem/m/home":
				HomeController.getHomePage(req, resp);
				break;
			case "/ExpenseReimbursementSystem/m/login":
				LoginController.login(req, resp);
				break;
			case "/ExpenseReimbursementSystem/m/portal":
				PortalController.processPortal(req, resp);
				break;
			case "/ExpenseReimbursementSystem/m/portal/ticket":
				PortalController.processTicket(req, resp);
				break;
			case "/ExpenseReimbursementSystem/m/logout":
				UserUtil.logout();
				LoginController.logout(req, resp);
				break;
			default:
				HomeController.getHomePage(req, resp);
				break;
		}
	}

}
