package com.example.project1;

import com.example.project1.config.HikariCPConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConfigurationUtil;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HikariCPConfigTest {

    private static final String DRIVER = "org.mariadb.jdbc.Driver";
    private static final String URL = "jdbc:mariadb://localhost:3307/project1?serverTimezone=Asia/Taipei&characterEncoding=utf-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    @Test
    void buildHikariDataSourceTest() throws SQLException {
        HikariDataSource hikariDataSource = HikariCPConfig.buildHikariDataSource(DRIVER, URL, USERNAME, PASSWORD);

        // 確保可以建立連接
        try (Connection connection = hikariDataSource.getConnection()) {
            assertNotNull(connection);
        } finally {
            hikariDataSource.close();
        }
    }

    @Test
    void getConnection() {
        HikariDataSource hikariDataSource = HikariCPConfig.buildHikariDataSource(DRIVER, URL, USERNAME, PASSWORD);

        try {
            Connection connection = hikariDataSource.getConnection();

            // 查詢 user 表
            PreparedStatement preparedStatementUser = connection.prepareStatement("select * from user;");
            ResultSet resultSetUser = preparedStatementUser.executeQuery();
            while (resultSetUser.next()) {
                // 處理 user 表的結果
                System.out.println("User ID: " + resultSetUser.getInt("user_id"));
            }

            // 查詢 role 表
            PreparedStatement preparedStatementRole = connection.prepareStatement("select * from role;");
            ResultSet resultSetRole = preparedStatementRole.executeQuery();
            while (resultSetRole.next()) {

                System.out.println("Role ID: " + resultSetRole.getInt("role_id"));
            }

            // 查詢 user_role 表
            PreparedStatement preparedStatementUserRole = connection.prepareStatement("select * from user_role;");
            ResultSet resultSetUserRole = preparedStatementUserRole.executeQuery();
            while (resultSetUserRole.next()) {

                System.out.println("User Role ID: " + resultSetUserRole.getInt("user_role_id"));
            }

            // 查詢 book 表
            PreparedStatement preparedStatementBook = connection.prepareStatement("select * from book;");
            ResultSet resultSetBook = preparedStatementBook.executeQuery();
            while (resultSetBook.next()) {

                System.out.println("Book ID: " + resultSetBook.getInt("book_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            hikariDataSource.close();
        }
    }

    public static List<String> executeQuery(Connection conn) throws SQLException {
        List<String> connId = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("select CONNECTION_ID()");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                connId.add(rs.getString(1));
            }
        }
        return connId;
    }
}