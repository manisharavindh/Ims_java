package com.lms.service;

import com.lms.dao.MemberDAO;
import com.lms.model.Member;

import java.util.List;
import java.util.regex.Pattern;

public class MemberService {
    private final MemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAO();
    }

    public void addMember(Member member) throws LibraryException {
        validateMember(member);
        
        // Check for duplicate Email
        for (Member m : memberDAO.getAllMembers()) {
            if (m.getEmail().equalsIgnoreCase(member.getEmail())) {
                throw new LibraryException("A member with email " + member.getEmail() + " already exists.");
            }
        }

        memberDAO.addMember(member);
    }

    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    public Member getMemberById(int memberId) {
        return memberDAO.getMemberById(memberId);
    }

    public void updateMember(Member member) throws LibraryException {
        validateMember(member);

        // Check for duplicate Email
        for (Member m : memberDAO.getAllMembers()) {
            if (m.getEmail().equalsIgnoreCase(member.getEmail()) && m.getMemberId() != member.getMemberId()) {
                throw new LibraryException("Another member with email " + member.getEmail() + " already exists.");
            }
        }

        memberDAO.updateMember(member);
    }

    public void deleteMember(int memberId) {
        memberDAO.deleteMember(memberId);
    }

    public List<Member> searchMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMembers();
        }
        return memberDAO.searchMembers(keyword.trim());
    }

    private void validateMember(Member member) throws LibraryException {
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new LibraryException("Member name cannot be empty.");
        }
        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new LibraryException("Member email cannot be empty.");
        }
        
        // Basic email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.compile(emailRegex).matcher(member.getEmail()).matches()) {
            throw new LibraryException("Invalid email format.");
        }

        if (member.getPhone() != null && !member.getPhone().trim().isEmpty()) {
            // Keep it simple: remove non-digits and check length
            String digits = member.getPhone().replaceAll("\\D", "");
            if (digits.length() < 7 || digits.length() > 15) {
                throw new LibraryException("Phone number should contain a reasonable number of digits (7-15).");
            }
        }

        if (member.getDepartment() == null || member.getDepartment().trim().isEmpty()) {
            throw new LibraryException("Department cannot be empty.");
        }
    }
}
