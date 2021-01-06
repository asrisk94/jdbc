package member.view;

import java.util.List;
import java.util.Scanner;

import member.controller.MemberController;
import member.model.vo.Member;



public class MemberMenu {
	
	private Scanner sc = new Scanner(System.in);
	private MemberController memberController = new MemberController();
	
	
	
	public void mainMemu() {
		
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
			System.out.println(menu);				// 메뉴 출력
			int choice = sc.nextInt();				// 번호 선택
			
			int result = 0; 						// DML 처리용
			Member member = null;
			List<Member> list = null;
			String memberId = null;
			int backToMain = -1000;					// 서브메뉴 뒤로가기에 사용
			
			switch(choice) {
			case 1: 
				list = memberController.selectAll();			// 전체 조회 메소드
				displayMemberList(list);						// 리스트 출력
				break;
			case 2: 
				memberId = inputMemberId();						// 아이디 입력
				member = memberController.selectOneMember(memberId);	// 조회
				displayMember(member);							// 멤버 출력
				break;
			case 3: 
				String memberName = inputMemberName();			// 이름 입력
				list = memberController.selectMemberName(memberName);	// 조회
				displayMemberList(list);						// 리스트 출력
				break;
			case 4:
				result = memberController.insertMember(inputMember());	// 멤버 추가 메소드
				displayMsg(result > 0 ? "회원가입 성공" : "회원가입 실패");
				break;
			case 5: 
				memberId = inputMemberId();								// 아이디 입력
				member = memberController.selectOneMember(memberId);	// 멤버 출력 메소드
				if(member.getMemberId() == null) {				// (member == null을 쓰기에는, DAO에서 new Member()를 담아버려서 안된다)
					System.out.println("대상이 없습니다.");			// 대상이 없다면 없다고 출력 후 멈춤 (그대로 와일문 반복)
					break;
				}
				displayMember(member);							// 멈추지 않았으면 출력하고 계속 진행
				result = subMenu(backToMain, member);			// 서브메뉴를 통해 업데이트 진행 메소드
				displayMsg(result == backToMain ? 				// 나올 일 없는 -1000을 지정하여 '되돌아감' 출력에 사용
							"메인으로 돌아갑니다." : 
							(result > 0 ? "수정 성공" : "수정 실패")
						  );
				break;
			case 6: 
				result = memberController.deleteMember(inputMemberId());	// 아이디 입력 후 삭제 메소드
				displayMsg(result > 0 ? "삭제 성공" : "삭제 실패");
				break;
			case 0:
				System.out.print("정말 끝내시겠습니까? (y/n) : ");
				if(sc.next().charAt(0) == 'y') {
					return;
				}
				break;
			default : System.out.println("잘못 입력하셨습니다."); break;
			}
		}
		
	}
	


	// 서브메뉴
	private int subMenu(int backToMain, Member member) {
		
		String columnName = null;							// 컬럼명
		String content = null;								// 바꿔서 넣을 내용
		
		while(true) {
			System.out.println("****** 회원 정보 변경 메뉴******\n"
					+ "1. 암호변경\n"
					+ "2. 이메일변경\n"
					+ "3. 전화번호변경\n"
					+ "4. 주소 변경\n"
					+ "9. 메인메뉴 돌아가기");
			
			System.out.print("선택 : ");
			int choice = sc.nextInt();
			
			switch(choice) {
			case 1:
				columnName = "password";
				break;
			case 2:
				columnName = "email";
				break;
			case 3:
				columnName = "phone";
				break;
			case 4:
				columnName = "address";
				break;
			case 9:
				return backToMain;					// 되돌아가기를 선택하면 -1000과 함께 메인으로 돌아감.
			default : 
				System.out.println("잘못 입력하셨습니다.");
				continue;							// 잘못 입력하면 이하 (업데이트)구문을 무시하고 와일문 반복 (서브메뉴 재출력)
			}
			
			sc.nextLine();								// 개행문자 처리
			System.out.print("새로 저장할 값을 입력해주세요 : ");
			content = sc.nextLine();					// 바뀔 내용 입력
			
			System.out.println(columnName + " = " + content);	// 확인용
			
			return memberController.updateMember(member, columnName, content);		// 대상객체, 컬럼명, 수정내용을 담아 업데이트 메소드 요청
		}
	}	// 서브메뉴 메소드 괄호



	// 이름 입력
	private String inputMemberName() {
		
		System.out.print("이름을 입력하세요 : ");
		
		return sc.next();
	}



	// 회원리스트 출력
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
		
	}



	// 아이디 입력 메소드
	private String inputMemberId() {
		
		System.out.print("아이디를 입력하세요 : ");
		
		return sc.next();
	}

	
	
	// 회원정보 출력
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
		sc.nextLine();
		System.out.print("주소 : ");
		String address = sc.nextLine();
		System.out.print("취미(,으로 나열) : ");
		String hobby = sc.nextLine();
		return new Member(memberId, password, memberName, gender, age, 
				email, phone, address, hobby, null);
	} // (4번) inputMember() 괄호
	
	
	
	// DML 처리 후에 사용자에게 피드백을 주는 메소드 (4번, 5번, 6번)
	private void displayMsg(String msg) {
		System.out.println(msg);
	}

} // 클래스 괄호
