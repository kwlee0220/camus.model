package camus.device;

import camus.CamusException;


/**
 * <code>DeviceNotFoundException</code>는 대상 장치가 존재하지 않는 경우
 * 발생하는 예외를 정의하는 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class DeviceNotFoundException extends CamusException {
	private static final long serialVersionUID = -1437345962980050313L;

	public DeviceNotFoundException(String details) {
		super(details);
	}
}
