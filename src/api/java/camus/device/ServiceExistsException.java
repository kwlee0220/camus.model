package camus.device;

import camus.CamusException;


/**
 * <code>ServiceExistsException</code>는 서비스 관리자에 동일 식별자의 서비스가 존재하는 경우
 * 발생되는 예외 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class ServiceExistsException extends CamusException {
	private static final long serialVersionUID = -7636363995290917914L;

	public ServiceExistsException(String msg) {
		super(msg);
	}
}
