package com.revature.ers.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.revature.ers.service.ExpenseReimbursementService;

public class LoginController {

	private static Logger logger = LogManager.getLogger(LoginController.class);
	
	public static void login(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		
		if(req.getMethod().equals("POST")) {
			if(ExpenseReimbursementService.login(req.getParameter("username"), req.getParameter("password")) != null) {
				logger.info("Logged in as: " + req.getParameter("username"));
				HttpSession sesh = req.getSession();
				sesh.setAttribute("MasterAccess", true);
			} else {
				resp.setStatus(403);
			}
		}
		else {
			resp.setStatus(405);
		}
		resp.sendRedirect("http://localhost:8080/ExpenseReimbursementSystem/m/home");
	}

	public static void logout(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		logger.info("Logged out.");
		req.getSession().invalidate();
		resp.sendRedirect("http://localhost:8080/ExpenseReimbursementSystem/m/home");
		
	}

}
