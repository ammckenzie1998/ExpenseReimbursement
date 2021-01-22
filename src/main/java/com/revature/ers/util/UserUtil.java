package com.revature.ers.util;

import com.revature.ers.models.User;

public class UserUtil {
	
	private static User user;
	
	public static User getUser() {
		return user;
	}
	
	public static void login(User u) {
		user = u;
	}
	
	public static void logout() {
		user = null;
	}

}
