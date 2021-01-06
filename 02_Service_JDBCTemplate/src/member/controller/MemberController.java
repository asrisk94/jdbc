package member.controller;

import java.util.List;

import member.model.service.MemberService;
import member.model.vo.Member;



public class MemberController {

	private MemberService memberService = new MemberService();
	
	
	
	public List<Member> selectAll() {
		
		return memberService.selectAll();
	}
	
	
	
	public Member selectOneMember(String memberId) {
		
		return memberService.selectOneMember(memberId);
	}
	
	
	
	public List<Member> selectMemberName(String memberName) {
		
		return memberService.selectMemberName(memberName);
	}
	
	
	
	public int insertMember(Member inputMember) {
		
		return memberService.insertMember(inputMember);
	}



	public int updateMember(Member member, String columnName, String content) {
		
		return memberService.updateMember(member, columnName, content);
	}



	public int deleteMember(String inputMemberId) {
		
		return memberService.deleteMember(inputMemberId);
	}
	
}
