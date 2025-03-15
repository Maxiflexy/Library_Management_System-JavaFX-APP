package com.maxiflexy.dreamdevs.librarymanagementsystem.service;

import com.maxiflexy.dreamdevs.librarymanagementsystem.dao.interfaces.MemberDAO;
import com.maxiflexy.dreamdevs.librarymanagementsystem.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberDAO memberDAO;

    private MemberService memberService;

    private Member member1;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setUp() {
        // Set up default mock behavior for the constructor's refreshCache() call
        when(memberDAO.getAllMembers()).thenReturn(new ArrayList<>());

        memberService = new MemberService(memberDAO);

        // Create test members
        member1 = new Member(1, "John Doe", "john@example.com", "555-1234");
        member2 = new Member(2, "Jane Smith", "jane@example.com", "555-5678");
        member3 = new Member(3, "Bob Johnson", "bob@example.com", "555-9012");

        // Clear initial interaction from constructor
        clearInvocations(memberDAO);
    }

    @Test
    void testGetAllMembers() {
        // Arrange
        List<Member> mockMembers = Arrays.asList(member1, member2, member3);
        when(memberDAO.getAllMembers()).thenReturn(mockMembers);

        // Need to trigger a refresh to use our new mock data
        memberService.refreshCache();

        // Act
        List<Member> result = memberService.getAllMembers();

        // Assert
        assertEquals(3, result.size());
        assertEquals(mockMembers, result);
        verify(memberDAO).getAllMembers();
    }

    @Test
    void testGetMembersByPage() {
        // Arrange
        List<Member> mockMembers = Arrays.asList(member1, member2, member3);
        when(memberDAO.getAllMembers()).thenReturn(mockMembers);

        // Need to trigger a refresh
        memberService.refreshCache();

        // Act - Get first page with 2 items per page
        List<Member> page1 = memberService.getMembersByPage(0, 2);

        // Assert
        assertEquals(2, page1.size());
        assertEquals(member1, page1.get(0));
        assertEquals(member2, page1.get(1));

        // Act - Get second page with 2 items per page
        List<Member> page2 = memberService.getMembersByPage(1, 2);

        // Assert
        assertEquals(1, page2.size());
        assertEquals(member3, page2.get(0));
    }

    @Test
    void testAddMember() {
        // Arrange
        Member newMember = new Member("New Member", "new@example.com", "555-0000");

        // Act
        memberService.addMember(newMember);

        // Assert
        verify(memberDAO).addMember(newMember);
    }

    @Test
    void testUpdateMember() {
        // Arrange
        Member memberToUpdate = new Member(1, "Updated Name", "updated@example.com", "555-1111");

        // Act
        memberService.updateMember(memberToUpdate);

        // Assert
        verify(memberDAO).updateMember(memberToUpdate);
    }

    @Test
    void testDeleteMember() {
        // Arrange
        int memberId = 1;

        // Act
        memberService.deleteMember(memberId);

        // Assert
        verify(memberDAO).deleteMember(memberId);
    }

    @Test
    void testGetMemberById() {
        // Arrange
        int memberId = 1;

        // Setup the mock members in the cache
        List<Member> mockMembers = Arrays.asList(member1, member2, member3);
        when(memberDAO.getAllMembers()).thenReturn(mockMembers);
        memberService.refreshCache();

        // Act
        Member result = memberService.getMemberById(memberId);

        // Assert
        assertEquals(member1, result);
    }

    @Test
    void testGetMemberByIdFromCache() {
        // Arrange - member exists in cache
        int memberId = 1;
        List<Member> mockMembers = Arrays.asList(member1, member2, member3);
        when(memberDAO.getAllMembers()).thenReturn(mockMembers);
        memberService.refreshCache();

        // Act
        Member result = memberService.getMemberById(memberId);

        // Assert
        assertEquals(member1, result);
        // No need to verify DAO calls as it should be served from cache
    }

    @Test
    void testGetMemberByIdFromDao() {
        // Arrange - member does NOT exist in cache
        int nonCachedMemberId = 99;
        Member nonCachedMember = new Member(nonCachedMemberId, "Non Cached", "non@example.com", "555-9999");

        // Empty cache
        when(memberDAO.getAllMembers()).thenReturn(new ArrayList<>());
        memberService.refreshCache();

        // Setup the mock for getMemberById
        when(memberDAO.getMemberById(nonCachedMemberId)).thenReturn(nonCachedMember);

        // Act
        Member result = memberService.getMemberById(nonCachedMemberId);

        // Assert
        assertEquals(nonCachedMember, result);
        verify(memberDAO).getMemberById(nonCachedMemberId);
    }

    @Test
    void testGetTotalMembers() {
        // Arrange
        List<Member> mockMembers = Arrays.asList(member1, member2, member3);
        when(memberDAO.getAllMembers()).thenReturn(mockMembers);

        // Need to trigger a refresh
        memberService.refreshCache();

        // Act
        int totalMembers = memberService.getTotalMembers();

        // Assert
        assertEquals(3, totalMembers);
    }
}
