package member.controller;

import java.util.List;

import member.model.dao.MemberDAO;
import member.model.vo.Member;



public class MemberController {
	
	private MemberDAO memberDAO = new MemberDAO();

	
	
	// 전체 조회 컨트롤 (1)
	public List<Member> selectAll() {
		
		return memberDAO.selectAll();
	}
	
	// id를 통한 회원 조회 컨트롤 (2)
	public Member selectMember(String memberId) {
		
		return memberDAO.selectMember(memberId);
	}
	
	// 이름을 통한 회원 조회 컨트롤 (3)
	public List<Member> selectName(String name) {
		
		return memberDAO.selectName(name);
	}
	
	// 회원 추가 컨트롤 (4)
	public int insertMember(Member member) {
		
		return memberDAO.insertMember(member);
	}

	// 회원 정보 수정 컨트롤 (5)
	public int updateMember(Member member) {
		
		return memberDAO.updateMember(member);
	}

	// 회원 삭제 컨트롤 (6)
	public int deleteMember(String memberId) {
		
		return memberDAO.deleteMember(memberId);
	}

}
