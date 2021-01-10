package member.model.exception;

/**
 * checked exception : 예외처리 강제화를 통해 컴파일 타임 체크가 가능.
 * 		extends Exception
 * unchecked exception : RuntimeException의 후손클래스
 *		extends RuntimeException
 */
public class MemberException extends Exception {

	public MemberException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MemberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
