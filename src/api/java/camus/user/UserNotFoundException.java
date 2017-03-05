package camus.user;


/**
 * <code>UserNotFoundException</code>는 사용자 존재하지 않는 경우 발생되는 예외 클래스를 정의한다.
 * 
 * @author Kang-Woo Lee
 */
public class UserNotFoundException extends UserException {
	private static final long serialVersionUID = 5524596570580065302L;

	public UserNotFoundException(String details) {
		super(details);
	}
}
