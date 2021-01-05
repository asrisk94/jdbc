package member.view;

import java.util.List;
import java.util.Scanner;

import member.controller.MemberController;
import member.model.vo.Member;

/*
 * 2. 회원아이디 조회 : 사용자로부터 아이디를 입력받고 일치하는 회원 1명 조회			-- ok
 * 3. 회원이름 검색 : 사용자로부터 이름을 입력받고, 일부라도 일치하는 회원 n명 조회		-- ok
 * 5. 회원정보수정 : 변경할 정보를 입력받고, db에 반영하기 (아이디, 등록일은 변경불가)	-- ok
 * 6. 회원탈퇴 : 사용자로부터 아이디를 입력받고 일치하는 회원 1명 삭제				-- ok
 */

public class MemberMenu {
	
	Scanner sc = new Scanner(System.in);
	private MemberController controller = new MemberController();

	
	
	public void mainMenu() {
		String menu = "------- 회원 관리 프로그램 ------\n"
					+ "1. 회원 전체 조회\n"
					+ "2. 회원 아이디 조회\n"
					+ "3. 회원 이름 검색\n"
					+ "4. 회원 가입\n"
					+ "5. 회원 정보 수정\n"
					+ "6. 회원 탈퇴\n"
					+ "0. 프로그램 끝내기\n"
					+ "---------------------------\n"
					+ "선택 : ";
		
		while(true) {
			
			System.out.print(menu);					// 메뉴 출력
			int choice = sc.nextInt();				// 메뉴 선택
			Member member = null;
			int result = 0;							// DML 결과 출력용
			List<Member> list = null;
			String memberId = null;
			
			switch(choice) {
			
				case 1:	// 회원 전체 조회
					list = controller.selectAll();		// 1. controller 요청
					displayMemberList(list);			// 2. 회원목록 출력
					break;
					
				case 2: // 회원 아이디 조회
					member = selectMember();			// 1. controller에 요청 메소드
					displayMember(member);				// 2. 출력 메소드
					break;
					
				case 3: // 회원 이름 검색
					list = selectName();				// 1. 이름 검색 및 요청 메소드
					displayMemberList(list);			// 2. 출력 메소드 (1번 전체출력 메소드 활용)
					break;
					
				case 4: // 회원 가입
					member = inputMember();						// 1. 사용자 입력값 -> member 객체 생성
					result = controller.insertMember(member);	// 2. controller에 insert 요청
					displayMsg(result == 1 ? "회원가입 성공!" : "회원가입 실패!");
					break;
					
				case 5: // 회원 정보 수정
					memberId = inputId("수정");						// 1. 아이디 입력 받음 메소드
					member = controller.selectMember(memberId);		// 2. controller에 검색 요청 메소드
					displayMember(member);							// 3. 해당 회원 정보 출력 (2번 회원 출력 메소드 활용)
					if(member != null) {								// 검색된 회원 정보가 있다면
						result = updateMember(member.getMemberId());	// 4. 수정정보 입력받고 con에 요청 메소드
						displayMsg(result == 1 ? "수정 성공" : "수정 실패");
					}
					break;
					
				case 6: // 회원 탈퇴
					memberId = inputId("삭제");						// 1. 아이디 입력받음 메소드
					member = controller.selectMember(memberId);		// 2. controller에 검색 요청 메소드 (5번 활용)
					displayMember(member);							// 3. 해당 회원 정보 출력 (2번 회원 출력 메소드 활용)
					if(member != null) {								// 검색된 회원 정보가 있다면
						result = controller.deleteMember(memberId);		// 4. con에 삭제요청 메소드
						displayMsg(result == 1 ? "삭제 성공" : "삭제 실패");
					}
					break;
					
				case 0: // 프로그램 종료
					System.out.print("정말로 끝내시겠습니까? (y/n) : ");
					if(sc.next().charAt(0)=='y') return;			// y 선택시 main으로 리턴
					break;
				default: System.out.println("잘못 입력하셨습니다.");		// 메뉴에 없는 값 입력시
			}
			
		} // while문 괄호
		
	} // mainMenu() 메소드 괄호

	
	
	// 전체 조회 메소드 (1번)
	private void displayMemberList(List<Member> list) {
		
		System.out.println("===============================");
		
		// 조회된 회원정보가 있을 때
		if(list != null && !list.isEmpty()) {
			System.out.println("MemberId\tMemberName\tGender\tAge\tEmail\t"
					+ "Phone\tAddress\t\tHobby\tEnrollDate");
			System.out.println("---------------------------------------");
			for(Member m : list) {
				System.out.println(m);
			}
		}
		// 조회된 회원정보가 없을 때
		else {
			System.out.println("조회된 회원이 없습니다.");
		}
		System.out.println("===============================");
		
	} // (1번) displayMemberList(List<Member> list) 메소드 괄호
	
	
	
	// 회원 조회 메소드 (2번)
	private Member selectMember() {
		
		System.out.print("아이디를 입력해주세요 : ");
		String memberId = sc.next();
		
		return controller.selectMember(memberId);
	}
	
	// 조회된 회원 출력 메소드 (2번)
	private void displayMember(Member member) {
		
		if(member != null) {
			System.out.println("MemberId\tMemberName\tGender\tAge\tEmail\t"
					+ "Phone\tAddress\t\tHobby\tEnrollDate");
			System.out.println("---------------------------------------");
			System.out.println(member);
		} else {
			System.out.println("해당하는 회원이 없습니다.");
		}
	}
	
	
	
	// 이름 회원 조회 메소드 (3번)
	private List<Member> selectName() {
		
		System.out.print("검색할 이름을 입력해주세요 : ");
		String name = sc.next();
		
		return controller.selectName(name);
	}
	
	
	
	// 회원가입용 정보 입력 메소드 (4번)
	private Member inputMember() {
		System.out.println("새로운 회원정보를 입력하세요.");
		System.out.println("------------------------------");
		System.out.print("아이디 : ");
		String memberId = sc.next();
		System.out.print("비밀번호 : ");
		String password = sc.next();
		System.out.print("이름 : ");
		String memberName = sc.next();
		System.out.print("나이 : ");
		int age = sc.nextInt();
		System.out.print("이메일 : ");
		String email = sc.next();
		System.out.print("성별(M/F) : ");
		String gender = sc.next().toUpperCase();
		System.out.print("전화번호(-빼고 입력) : ");
		String phone = sc.next();
		System.out.print("주소 : ");
		String address = sc.nextLine();
		sc.nextLine();	// 개행문자 날리기용
		System.out.print("취미(,으로 나열) : ");
		String hobby = sc.nextLine();
		
		return new Member(memberId, password, memberName, gender, age, 
				email, phone, address, hobby, null);
	} // (4번) inputMember() 괄호
	
	
	
	// 아이디 입력 메소드 (5번)
	private String inputId(String content) {
		System.out.print(content + "할 아이디를 입력해주세요 : ");
		String memberId = sc.next();
		
		return memberId;
	}
	
	// 회원 수정 메소드 (5번)
	private int updateMember(String memberId) {
		
		System.out.println("바뀐 회원정보를 입력하세요.");
		System.out.println("------------------------------");
		System.out.print("비밀번호 : ");
		String password = sc.next();
		System.out.print("이름 : ");
		String memberName = sc.next();
		System.out.print("나이 : ");
		int age = sc.nextInt();
		System.out.print("이메일 : ");
		String email = sc.next();
		System.out.print("성별(M/F) : ");
		String gender = sc.next().toUpperCase();
		System.out.print("전화번호(-빼고 입력) : ");
		String phone = sc.next();
		System.out.print("주소 : ");
		String address = sc.nextLine();
		sc.nextLine();	// 개행문자 날리기용
		System.out.print("취미(,으로 나열) : ");
		String hobby = sc.nextLine();
		
		Member member = new Member(memberId, password, memberName, gender, age, 
					email, phone, address, hobby, null);
		
		return controller.updateMember(member);
	}

	
	
	// DML 처리 후에 사용자에게 피드백을 주는 메소드 (4번, 5번, 6번)
	private void displayMsg(String msg) {
		System.out.println(msg);
	}

}
