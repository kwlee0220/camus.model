package camus.device;

import camus.CamusException;


/**
 * <code>ServiceNotFoundException</code>는 서비스 관리자에 지정된 식별자의 서비스가 존재하지 않는 경우
 * 발생되는 예외 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class ServiceNotFoundException extends CamusException {
	private static final long serialVersionUID = 6810038798883768129L;
	
	public ServiceNotFoundException(String msg) {
		super(msg);
	}
}
