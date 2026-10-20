package com.gabriela.gym.dao;

import com.gabriela.gym.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {


    public ScenarioDao() {
        createTable();
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS gym_members (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
                    tier TEXT NOT NULL,
                    monthly_fee REAL NOT NULL,
                    status TEXT NOT NULL
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println("Error creating database table: " + e.getMessage());
        }
    }

    public boolean addMember(ScenarioEntity member) {
        String sql = """
                INSERT INTO gym_members (full_name, tier, monthly_fee, status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, member.fullName());
            pstmt.setString(2, member.tier());
            pstmt.setDouble(3, member.monthlyFee());
            pstmt.setString(4, member.status());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
            return false;
        }
    }

    public List<ScenarioEntity> getAllMembers() {
        List<ScenarioEntity> members = new ArrayList<>();

        String sql = """
                SELECT id, full_name, tier, monthly_fee, status
                FROM gym_members
                ORDER BY id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ScenarioEntity member = new ScenarioEntity(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("tier"),
                        rs.getDouble("monthly_fee"),
                        rs.getString("status")
                );

                members.add(member);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving members: " + e.getMessage());
        }

        return members;
    }
}
