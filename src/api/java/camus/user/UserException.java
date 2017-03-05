package camus.user;

import camus.CamusException;


/**
 * <code>UserException</code>는 사용자와 관련 예외의 클래스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class UserException extends CamusException {
	private static final long serialVersionUID = 2882218513318704261L;

	public UserException(String details) {
		super(details);
	}
}
