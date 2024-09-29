package com.api.developer.portal.processor.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.api.developer.portal.db.util.DatabaseUtil;
import com.api.developer.portal.processor.model.User;
import com.api.developer.portal.processor.util.Utils;

public class UserRepository {
	public static void addUser(User newUser) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO users (username, password, email) VALUES(?,?,?)")) {
			preparedStatement.setString(1, newUser.getUsername());
			preparedStatement.setString(2, newUser.getPassword());
			preparedStatement.setString(3, newUser.getEmail());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public static boolean getUser(String username) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
			boolean isExist = false;

			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int result = resultSet.getInt(1);
				if (result != 0) {
					isExist = true;
				}
			}
			return isExist;
		} catch (SQLException e) {
			throw e;
		}
	}

	public static User getUser(String username, String password) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			User user = new User();

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {

				user.setUserId(Integer.parseInt(resultSet.getString("id")));
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
			}
			return user;
		} catch (SQLException e) {
			throw e;
		}
	}
}
