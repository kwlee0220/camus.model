package camus.user;


/**
 * <code>UserExistsException</code>는 사용자 존재하는 경우 발생되는 예외 클래스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class UserExistsException extends UserException {
	private static final long serialVersionUID = -6081545302011680088L;

	public UserExistsException(String details) {
		super(details);
	}
}
