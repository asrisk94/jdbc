package member.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import member.controller.MemberController;
import member.model.vo.Member;

public class MemberMenu {

	private Scanner sc = new Scanner(System.in);
	private MemberController memberController = new MemberController(this);
	
	
	
	public void mainMenu() {
		
		String menu = "-------- <회원 관리 프로그램2>--------\n"
				+ "1. 회원 전체 조회\n"
				+ "2. 회원 아이디 조회\n"
				+ "3. 회원 이름 검색\n"
				+ "4. 회원 가입\n"
				+ "5. 회원 정보 수정\n"
				+ "6. 회원 탈퇴\n"
				+ "7. 탈퇴 회원 조회\n"
				+ "0. 프로그램 끝내기\n"
				+ "-------------------------------\n"
				+ "선택 : ";
		
		
		
		while(true) {
			System.out.print(menu);
			int choice = 100;
			
			try {
				choice = sc.nextInt();
			} catch(InputMismatchException e) {
				sc.next();
				//문자 버퍼 제거외 별도의 처리코드 없음.
			}
			
			int result = 0; //DML처리
			Member member = null;
			List<Member> list = null;
			String memberName = null;
			
			
			switch(choice) {
			case 1: 
				//1.controller요청
				list = memberController.selectAll();
				//2.회원목록 출력
				displayMemberList(list);
				break;
			case 2: 
				//1.사용자입력값(memberId)-컨트롤러 조회요청
				//2. member객체 혹은 null 화면 출력
				member = memberController.selectOneMember(inputMemberId());
				displayMember(member);
				break;
			case 3: 
				list = memberController.selectByName(inputMemberName());
				displayMemberList(list);
				break;
			case 4: 
				//1.사용자입력값 회원객체
				//2.컨트롤러에 insertMember요청
				//3.사용자피드백
				result = memberController.insertMember(inputMember());
				displayMsg(result > 0 ? "회원가입성공!" : "회원가입실패!");
				break;
			case 5: 
				updateMemberMenu();
				break;
			case 6:
				result = memberController.deleteMember(inputMemberId());
				displayMsg(result > 0 ? "회원 탈퇴 성공!" : "회원 탈퇴 실패!");
				break;
			case 7: 
				//탈퇴회원조회
				list = memberController.selectAllDeletedMember();
				displayMemberList(list);
				break;
			case 0: 
				System.out.print("정말 끝내시겠습니까?(y/n) : ");
				if(sc.next().charAt(0) == 'y') 
					return;
				break;
			default : System.out.println("잘못 입력하셨습니다.");
			}
		}
		
	} // 메인메뉴 생성자 괄호
	
	
	
	private void updateMemberMenu() {
		
		String menu = "****** 회원 정보 변경 메뉴******\r\n" + 
					  "1. 암호변경\r\n" + 
					  "2. 이메일변경\r\n" + 
					  "3. 전화번호변경\r\n" + 
					  "4. 주소 변경\r\n" + 
					  "9. 메인메뉴 돌아가기\r\n" + 
					  "입력 : ";
		
		String memberId = inputMemberId();
		
		int choice = 0;
		while(true){
			//회원정보 출력
			Member m = memberController.selectOneMember(memberId);
			
			//조회된 회원정보가 없는 경우, 리턴
			if(m == null) {
				System.out.println("해당 회원이 존재하지 않습니다.");
				return;
			}
			
			displayMember(m);
			System.out.print(menu);
			
			try {
				choice = sc.nextInt();
			} catch(InputMismatchException e) {
				sc.next();
				//문자 버퍼 제거외 별도의 처리코드 없음.
			}
			
			switch (choice) {
			case 1:
				System.out.print("변경할 암호 : ");
				m.setPassword(sc.next());
				break;
			case 2:
				System.out.print("변경할 이메일 : ");
				m.setEmail(sc.next());
				break;
			case 3:
				System.out.print("변경할 전화번호(-빼고 입력) : ");
				m.setPhone(sc.next());
				break;
			case 4:
				System.out.print("변경할 주소 : ");
				sc.nextLine();
				m.setAddress(sc.nextLine());
				break;
			case 9: return;
			default:
				System.out.println("잘못 입력하셨습니다.");
				continue;
			}
			
			int result = memberController.updateMember(m);
			displayMsg(result > 0 ? "정보 수정 성공!" : "정보 수정 실패!");
		}
	
	} // 업데이트멤버메뉴 메소드 괄호

	
	
	/**
	 * 조회된 회원 정보 출력
	 * @param m
	 */
	private void displayMember(Member m) {
		
		if(m == null) {
			System.out.println("조회된 행이 없습니다.");
		}
		else {
			System.out.println("------------------------------------------------------------------");
			System.out.println("Id\tName\tGender\tAge\tEmail\tPhone\tAddress\t\tHobby\tEnroll Date");
			System.out.println("------------------------------------------------------------------");
			System.out.println(m);
			System.out.println("------------------------------------------------------------------");
		}
	}

	
	
	private String inputMemberName() {
		
		System.out.print("조회할 이름 입력 : ");
		return sc.next();
	}

	
	
	/**
	 * DML처리 후에 사용자에게 피드백을 주는 메소드
	 * @param msg
	 */
	private void displayMsg(String msg) {
		
		System.out.println(msg);
	}

	
	
	/**
	 * 회원가입 메소드
	 * 사용자 입력처리
	 * @return
	 */
	private Member inputMember() {
		
		System.out.println("새로운 회원정보를 입력하세요.");
		System.out.println("-------------------------------");
		System.out.print("아이디 : ");
		String memberId = sc.next();
		System.out.println("비밀번호 : ");
		String password = sc.next();
		System.out.println("이름 : ");
		String memberName = sc.next();
		
		System.out.println("나이 : ");
		int age = 0; //기본값
		try {
			age = sc.nextInt();
		} catch(InputMismatchException e) {
			sc.next();
			//문자 버퍼 제거외 별도의 처리코드 없음.
		}
		
		System.out.println("성별(M/F) : ");
		String gender = sc.next().toUpperCase();
		if(!"M".equals(gender) && !"F".equals(gender))
			gender = null;
		
		System.out.println("이메일 : ");
		String email = sc.next();
		System.out.println("전화번호(-빼고 입력) : ");
		String phone = sc.next();
		sc.nextLine(); //개행문자 날리기용
		System.out.println("주소 : ");
		String address = sc.nextLine();
		System.out.println("취미(,으로 나열) : ");
		String hobby = sc.nextLine();
		
		return new Member(memberId, password, memberName, gender, 
						  age, email, phone, address, hobby, null);
	}
	
	
	
	private String inputMemberId() {
		
		System.out.print("조회할 아이디 입력 : ");
		return sc.next();
	}

	
	
	/**
	 * 회원 목록 조회메소드
	 * (여러명)
	 * @param list
	 */
	private void displayMemberList(List<Member> list) {
		
		System.out.println("==============================================================");
		
		//조회된 회원정보가 있을때
		if(list != null && !list.isEmpty()) {
			
			System.out.println("Id\tName\tGender\tAge\tEmail\t\tPhone\t\tAddress\t\tHobby\tEnroll Date\tDel Date");
			System.out.println("-----------------------------------------------------------------------------------------------");
			
			for(Member member : list)
				System.out.println(member);
		}
		
		//조회된 회원정보가 없을때
		else {
			System.out.println("조회된 회원이 없습니다.");
		}
		System.out.println("===============================================================");
	}

	
	
	/**
	 * 사용자 오류 알림!
	 * @param message
	 */
	public void displayError(String message) {
		
		System.err.println(message);
	}

}