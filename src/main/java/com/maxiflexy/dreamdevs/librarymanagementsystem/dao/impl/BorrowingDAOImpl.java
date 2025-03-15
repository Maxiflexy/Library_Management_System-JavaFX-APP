package com.maxiflexy.dreamdevs.librarymanagementsystem.dao.impl;

import com.maxiflexy.dreamdevs.librarymanagementsystem.config.DatabaseConnection;
import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.BorrowingDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.BorrowingRecord;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAOImpl implements BorrowingDAO {
    private static final Logger logger = new Logger();

    @Override
    public void addBorrowingRecord(BorrowingRecord record) {
        String sql = "INSERT INTO borrowing_records (book_id, member_id, borrow_date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, record.getBookId());
            stmt.setInt(2, record.getMemberId());
            stmt.setDate(3, record.getBorrowDate());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating borrowing record failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record.setRecordId(generatedKeys.getInt(1));
                    logger.log("Book borrowed: Book ID " + record.getBookId() + " by Member ID " + record.getMemberId());
                } else {
                    throw new SQLException("Creating borrowing record failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.log("Error adding borrowing record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateReturnDate(int recordId, Date returnDate) {
        String sql = "UPDATE borrowing_records SET return_date = ? WHERE record_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, returnDate);
            stmt.setInt(2, recordId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.log("Book returned: Record ID " + recordId);
            } else {
                logger.log("Book return failed: No borrowing record found with ID " + recordId);
            }
        } catch (SQLException e) {
            logger.log("Error updating return date: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<BorrowingRecord> getAllBorrowingRecords() {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, b.title as book_title, m.name as member_name " +
                "FROM borrowing_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "JOIN members m ON br.member_id = m.member_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BorrowingRecord record = new BorrowingRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setBookId(rs.getInt("book_id"));
                record.setMemberId(rs.getInt("member_id"));
                record.setBorrowDate(rs.getDate("borrow_date"));
                record.setReturnDate(rs.getDate("return_date"));
                record.setBookTitle(rs.getString("book_title"));
                record.setMemberName(rs.getString("member_name"));

                records.add(record);
            }
        } catch (SQLException e) {
            logger.log("Error retrieving all borrowing records: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public List<BorrowingRecord> getBorrowingRecordsByMember(int memberId) {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, b.title as book_title, m.name as member_name " +
                "FROM borrowing_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "JOIN members m ON br.member_id = m.member_id " +
                "WHERE br.member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BorrowingRecord record = new BorrowingRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setBookId(rs.getInt("book_id"));
                    record.setMemberId(rs.getInt("member_id"));
                    record.setBorrowDate(rs.getDate("borrow_date"));
                    record.setReturnDate(rs.getDate("return_date"));
                    record.setBookTitle(rs.getString("book_title"));
                    record.setMemberName(rs.getString("member_name"));

                    records.add(record);
                }
            }
        } catch (SQLException e) {
            logger.log("Error retrieving borrowing records by member: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public List<BorrowingRecord> getBorrowingRecordsByBook(int bookId) {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, b.title as book_title, m.name as member_name " +
                "FROM borrowing_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "JOIN members m ON br.member_id = m.member_id " +
                "WHERE br.book_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BorrowingRecord record = new BorrowingRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setBookId(rs.getInt("book_id"));
                    record.setMemberId(rs.getInt("member_id"));
                    record.setBorrowDate(rs.getDate("borrow_date"));
                    record.setReturnDate(rs.getDate("return_date"));
                    record.setBookTitle(rs.getString("book_title"));
                    record.setMemberName(rs.getString("member_name"));

                    records.add(record);
                }
            }
        } catch (SQLException e) {
            logger.log("Error retrieving borrowing records by book: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public BorrowingRecord getBorrowingRecordById(int recordId) {
        String sql = "SELECT br.*, b.title as book_title, m.name as member_name " +
                "FROM borrowing_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "JOIN members m ON br.member_id = m.member_id " +
                "WHERE br.record_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BorrowingRecord record = new BorrowingRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setBookId(rs.getInt("book_id"));
                    record.setMemberId(rs.getInt("member_id"));
                    record.setBorrowDate(rs.getDate("borrow_date"));
                    record.setReturnDate(rs.getDate("return_date"));
                    record.setBookTitle(rs.getString("book_title"));
                    record.setMemberName(rs.getString("member_name"));

                    return record;
                }
            }
        } catch (SQLException e) {
            logger.log("Error retrieving borrowing record by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<BorrowingRecord> getActiveBorrowingRecords() {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, b.title as book_title, m.name as member_name " +
                "FROM borrowing_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "JOIN members m ON br.member_id = m.member_id " +
                "WHERE br.return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BorrowingRecord record = new BorrowingRecord();
                record.setRecordId(rs.getInt("record_id"));
                record.setBookId(rs.getInt("book_id"));
                record.setMemberId(rs.getInt("member_id"));
                record.setBorrowDate(rs.getDate("borrow_date"));
                record.setReturnDate(rs.getDate("return_date"));
                record.setBookTitle(rs.getString("book_title"));
                record.setMemberName(rs.getString("member_name"));

                records.add(record);
            }
        } catch (SQLException e) {
            logger.log("Error retrieving active borrowing records: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }
}
