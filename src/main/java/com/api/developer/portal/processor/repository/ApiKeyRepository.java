package com.api.developer.portal.processor.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.api.developer.portal.db.util.DatabaseUtil;
import com.api.developer.portal.processor.model.Key;

public class ApiKeyRepository {
	public static List<Key> getAllKeys(String username) throws SQLException {
		List<Key> keysList = new ArrayList<Key>();
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(
						"SELECT * FROM api_keys WHERE user_id = (SELECT id FROM users WHERE username = ?)")) {
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Key key = new Key();
				key.setKeyId(Integer.parseInt(resultSet.getString("id")));
				key.setUserId(Integer.parseInt(resultSet.getString("user_id")));
				key.setApiKey(resultSet.getString("api_key"));
				key.setStatus(resultSet.getString("status"));
				key.setCreatedAt(resultSet.getDate("created_at"));
				keysList.add(key);
			}
		} catch (SQLException e) {
			throw e;
		}
		return keysList;
	}

	public static void addKey(Key key) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO api_keys (user_id, api_key, status) VALUES (?,?,?)")) {
			preparedStatement.setInt(1, key.getUserId());
			preparedStatement.setString(2, key.getApiKey());
			preparedStatement.setString(3, key.getStatus());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void updateKey(int id) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("UPDATE api_keys SET status = 'Disabled' WHERE id = ?")) {
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

}
