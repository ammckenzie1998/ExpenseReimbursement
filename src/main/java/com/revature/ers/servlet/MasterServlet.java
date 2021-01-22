package com.revature.ers.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.revature.ers.controller.RequestHelper;

public class MasterServlet extends HttpServlet{
	
	private static Logger logger = LogManager.getLogger(MasterServlet.class);
	
	@Override
	public void init() throws ServletException{
		super.init();
		logger.debug("Master servlet initialized.");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException{
		
		resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		resp.setHeader("Pragma","no-cache");
		resp.setDateHeader ("Expires", 0);	
		RequestHelper.process(req, resp);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		
		doGet(req, resp);
			
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
			
		doGet(req, resp);	
			
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
			
		doGet(req, resp);
			
	}
	
}
