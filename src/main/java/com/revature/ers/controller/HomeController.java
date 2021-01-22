package com.revature.ers.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.ers.util.UserUtil;

public class HomeController {

	public static void getHomePage(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {
		
		if(req.getSession(false) != null && UserUtil.getUser() != null) { //A session exists without creating one and isn't null
			resp.sendRedirect("http://localhost:8080/ExpenseReimbursementSystem/m/portal");
		} else {
			RequestDispatcher resdis = req.getRequestDispatcher("/login.html");
			resdis.forward(req, resp);
		}
		
	}

}
