package camus.service;

import camus.CamusException;


/**
 * <code>PauseControlException</code>은 {@link PauseControl}을 통해 작업의
 * 일시 중지 또는 재개가 실패한 경우 발생되는 예외 클래스를 정의한다. 
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class PauseControlException extends CamusException {
	private static final long serialVersionUID = 5649842293769598428L;
	
	public PauseControlException(String msg) {
		super(msg);
	}
}
