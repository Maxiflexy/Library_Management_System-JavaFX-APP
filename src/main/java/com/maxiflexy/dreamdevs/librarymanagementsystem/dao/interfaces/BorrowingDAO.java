package com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces;


import com.maxiflexy.dreamdevs.librarymanagementsystem.model.BorrowingRecord;

import java.util.List;

public interface BorrowingDAO {

    void addBorrowingRecord(BorrowingRecord record);
    void updateReturnDate(int recordId, java.sql.Date returnDate);
    List<BorrowingRecord> getAllBorrowingRecords();
    List<BorrowingRecord> getBorrowingRecordsByMember(int memberId);
    List<BorrowingRecord> getBorrowingRecordsByBook(int bookId);
    BorrowingRecord getBorrowingRecordById(int recordId);
    List<BorrowingRecord> getActiveBorrowingRecords();
}
