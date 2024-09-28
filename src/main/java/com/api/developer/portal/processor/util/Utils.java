package com.api.developer.portal.processor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.api.developer.portal.processor.model.User;

import jakarta.servlet.http.HttpServletRequest;

public class Utils {
	
	public static User populateUserParametersFromRequest(HttpServletRequest request) throws ParseException {
		User user = new User();
		user.setUsername(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
        return user;
	}

	public static java.sql.Date dateToSqlDate(java.util.Date date){
		return new java.sql.Date(date.toInstant().toEpochMilli());
	}
	
	public static java.util.Date stringToDate(String date) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		return format.parse(date);
	}
	
	public static String dateToString (java.util.Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        return format.format(date);
	}
}
