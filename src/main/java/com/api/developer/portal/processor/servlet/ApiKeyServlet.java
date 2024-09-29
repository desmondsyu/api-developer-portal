package com.api.developer.portal.processor.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import com.api.developer.portal.processor.model.Key;
import com.api.developer.portal.processor.repository.ApiKeyRepository;
import com.api.developer.portal.processor.util.Utils;

public class ApiKeyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ApiKeyServlet() {
		super();
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		getServletContext().log("ApiKeyServlet init...");
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
		getServletContext().log("The API key servlet is being terminated.");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);
		if (session == null) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }
		
		try {
			List<Key> keysList = ApiKeyRepository.getAllKeys((Integer) session.getAttribute("userId"));

			for (Key key : keysList) {
				out.println("<tr>" 
						+ "<td>" + key.getApiKey() + "</td>" 
						+ "<td>" + key.getStatus() + "</td>" 
						+ "<td>" + key.getCreatedAt() + "</td>" 
						+ "<td><input type='radio' name='selectedKey' class='keyRadio' data-key-id='" + key.getApiKey() + "' /></td>"
						+ "</tr>");
			}
			
		} catch (Exception e) {
			getServletContext().log(e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    response.setContentType("application/json");
		HttpSession session = request.getSession(false);
		
		if (session == null) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }
		
		try {
			Key newKey = new Key();
			newKey.setApiKey(generateKey());
			newKey.setUserId((Integer) session.getAttribute("userId"));
			newKey.setStatus("Active");
			
			ApiKeyRepository.addKey(newKey);
			
			response.setStatus(HttpServletResponse.SC_CREATED);
	        response.getWriter().write("{\"apiKey\": \"" + newKey.getApiKey() + "\"}");
		} catch (Exception e) {
			getServletContext().log(e.getMessage());
		}
	}

	private String generateKey() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String keyId = request.getParameter("keyId");
			String apiKey = request.getParameter("apiKey");
			String status = request.getParameter("status");
			String createdAt = request.getParameter("createdAt");
			
			Key updatedKey = new Key();
			updatedKey.setKeyId(Integer.parseInt(keyId));
			updatedKey.setApiKey(apiKey);
	        updatedKey.setStatus(status);
	        updatedKey.setCreatedAt(Utils.stringToDate(createdAt));
	        
			ApiKeyRepository.updateKey(updatedKey);
		} catch (Exception e) {
			getServletContext().log(e.getMessage());
		}
	}
}
