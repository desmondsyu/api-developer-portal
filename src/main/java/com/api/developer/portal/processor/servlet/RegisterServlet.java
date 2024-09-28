package com.api.developer.portal.processor.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.api.developer.portal.processor.model.User;
import com.api.developer.portal.processor.repository.UserRepository;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;;

	public RegisterServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		getServletContext().log("RegisterServlet init...");
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
		getServletContext().log("The registeration servlet is being terminated.");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			User user = new User();
			user.setUsername(request.getParameter("username"));
			user.setPassword(request.getParameter("password"));
			user.setEmail(request.getParameter("email"));

			if (UserRepository.getUser(user.getUsername())) {
				throw new Exception("User already exists");
			}

			UserRepository.addUser(user);
			getServletContext().log("User created");
			response.sendRedirect("login.html");
		} catch (Exception e) {
			getServletContext().log(e.getMessage());
		}
	}

	private String hashPassword(String password) {
		String hashedPassword = null;

		return hashedPassword;
	}
}
