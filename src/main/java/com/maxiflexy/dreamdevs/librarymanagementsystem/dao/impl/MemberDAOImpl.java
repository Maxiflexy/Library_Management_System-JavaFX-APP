package com.maxiflexy.dreamdevs.librarymanagementsystem.dao.impl;

import com.maxiflexy.dreamdevs.librarymanagementsystem.config.DatabaseConnection;
import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.MemberDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Member;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {
    private static final Logger logger = new Logger();

    @Override
    public void addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating member failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setMemberId(generatedKeys.getInt(1));
                    logger.log("Member added: " + member.getName());
                } else {
                    throw new SQLException("Creating member failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.log("Error adding member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateMember(Member member) {
        String sql = "UPDATE members SET name = ?, email = ?, phone = ? WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setInt(4, member.getMemberId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.log("Member updated: " + member.getName());
            } else {
                logger.log("Member update failed: No member found with ID " + member.getMemberId());
            }
        } catch (SQLException e) {
            logger.log("Error updating member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.log("Member deleted with ID: " + memberId);
            } else {
                logger.log("Member deletion failed: No member found with ID " + memberId);
            }
        } catch (SQLException e) {
            logger.log("Error deleting member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));

                members.add(member);
            }
        } catch (SQLException e) {
            logger.log("Error retrieving all members: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    @Override
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMemberId(rs.getInt("member_id"));
                    member.setName(rs.getString("name"));
                    member.setEmail(rs.getString("email"));
                    member.setPhone(rs.getString("phone"));

                    return member;
                }
            }
        } catch (SQLException e) {
            logger.log("Error retrieving member by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
