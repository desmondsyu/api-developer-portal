package com.api.developer.portal.processor.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.api.developer.portal.db.util.DatabaseUtil;
import com.api.developer.portal.processor.model.Key;
import com.api.developer.portal.processor.util.Utils;

public class ApiKeyRepository {
	public static List<Key> getAllKeys(int userId) throws SQLException, ParseException {
		List<Key> keysList = new ArrayList<Key>();
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM api_keys WHERE user_id = ?")) {
			preparedStatement.setInt(1, userId);
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

	public static void deactivateKey(int keyId) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("UPDATE api_keys SET status = 'Invalid' WHERE id = ?")) {
			preparedStatement.setInt(1, keyId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public static void regenerateKey(int keyId, String newKey) throws SQLException {
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("UPDATE api_keys SET api_key = ?, status = 'Active', created_at = ?  WHERE id = ?")) {
			preparedStatement.setString(1, newKey);
			preparedStatement.setTimestamp(2, Utils.dateToSqlDate(new Date()));
			preparedStatement.setInt(3, keyId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public static Key getKey(int keyId) throws SQLException, ParseException {
		Key key = new Key();
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM api_keys WHERE id = ?")) {

			preparedStatement.setInt(1, keyId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				key.setKeyId(Integer.parseInt(resultSet.getString("id")));
				key.setUserId(Integer.parseInt(resultSet.getString("user_id")));
				key.setApiKey(resultSet.getString("api_key"));
				key.setStatus(resultSet.getString("status"));
				key.setCreatedAt(resultSet.getDate("created_at"));
			}
		} catch (SQLException e) {
			throw e;
		}
		return key;
	}

}
