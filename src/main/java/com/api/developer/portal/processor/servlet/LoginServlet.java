package com.api.developer.portal.processor.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.api.developer.portal.processor.repository.UserRepository;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		getServletContext().log("LoginServlet init...");
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getMethod();
		String requestUrl = request.getRequestURL().toString();

		getServletContext().log("Incoming request: " + requestType + " " + requestUrl);

		super.service(request, response);
	}

	@Override
	public void destroy() {
		getServletContext().log("The login servlet is being terminated.");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (UserRepository.getUser(username, password)) {
				HttpSession session = request.getSession();
				session.setAttribute("username", username);
				response.sendRedirect("dashboard.html");
			}
		} catch (Exception e) {
			getServletContext().log(e.getMessage());
		}

	}

}
