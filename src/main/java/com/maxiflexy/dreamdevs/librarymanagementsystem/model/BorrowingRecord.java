package com.maxiflexy.dreamdevs.librarymanagementsystem.model;

import java.sql.Date;
import java.util.Objects;

public class BorrowingRecord {

    private Integer recordId;
    private Integer bookId;
    private Integer memberId;
    private Date borrowDate;
    private Date returnDate;

    // Additional fields for display purposes
    private String bookTitle;
    private String memberName;

    public BorrowingRecord() {
    }

    public BorrowingRecord(Integer recordId, Integer bookId, Integer memberId, Date borrowDate, Date returnDate) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public BorrowingRecord(Integer bookId, Integer memberId, Date borrowDate) {
        this(null, bookId, memberId, borrowDate, null);
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowingRecord that = (BorrowingRecord) o;
        return Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }

    @Override
    public String toString() {
        return "BorrowingRecord{" +
                "recordId=" + recordId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", bookTitle='" + bookTitle + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
