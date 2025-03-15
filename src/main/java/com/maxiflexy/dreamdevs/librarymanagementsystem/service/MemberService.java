package com.maxiflexy.dreamdevs.librarymanagementsystem.service;

import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.impl.MemberDAOImpl;
import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.MemberDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Member;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.CSVExporter;
import com.maxiflexy.dreamdevs.librarymanagementsystem.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private final MemberDAO memberDAO;
    private final Logger logger;
    private final CSVExporter csvExporter;

    // In-memory cache for members
    private List<Member> membersCache;

    // Default constructor
    public MemberService() {
        this(new MemberDAOImpl());
    }

    // Constructor for dependency injection (useful for testing)
    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
        this.logger = new Logger();
        this.csvExporter = new CSVExporter();
        this.membersCache = new ArrayList<>();
        refreshCache();
    }

    public void addMember(Member member) {
        memberDAO.addMember(member);
        refreshCache();
    }

    public void updateMember(Member member) {
        memberDAO.updateMember(member);
        refreshCache();
    }

    public void deleteMember(int memberId) {
        memberDAO.deleteMember(memberId);
        refreshCache();
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(membersCache);
    }

    // Added pagination support
    public List<Member> getMembersByPage(int page, int pageSize) {
        int fromIndex = page * pageSize;
        if (fromIndex >= membersCache.size()) {
            return new ArrayList<>();
        }

        int toIndex = Math.min(fromIndex + pageSize, membersCache.size());
        return new ArrayList<>(membersCache.subList(fromIndex, toIndex));
    }

    public int getTotalMembers() {
        return membersCache.size();
    }

    public Member getMemberById(int memberId) {
        return membersCache.stream()
                .filter(member -> member.getMemberId() == memberId)
                .findFirst()
                .orElseGet(() -> memberDAO.getMemberById(memberId));
    }

    public void exportMembersToCSV() {
        csvExporter.exportMembersToCSV(membersCache);
    }

    public void refreshCache() {
        membersCache = memberDAO.getAllMembers();
    }
}