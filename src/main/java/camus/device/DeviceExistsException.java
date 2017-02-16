package camus.device;

import camus.CamusException;


/**
 * <code>DeviceExistsException</code>는 동일 식별자의 장치가 존재하는 경우
 * 발생하는 예외를 정의하는 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class DeviceExistsException extends CamusException {
	private static final long serialVersionUID = -7732438693029267193L;

	public DeviceExistsException(String details) {
		super(details);
	}
}
