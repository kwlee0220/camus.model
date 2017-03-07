package camus.service;



/**
 * <code>PowerControlException</code>은 정원 상태 관련 연산 중 발생되는 예외 클래스를 정의한다. 
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class PowerControlException extends RuntimeException {
	private static final long serialVersionUID = 5649842293769598428L;

	public PowerControlException() {
	    super();
	}
	
	public PowerControlException(String msg) {
		super(msg);
	}
}
