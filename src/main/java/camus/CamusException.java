package camus;

/**
 * <code>CamusException</code>는 CAMUS 관련 최상위 checked 예외 클래스이다.
 * <p>
 * 모든 checked CAMUS 관련 예외 클래스는 모두 <code>CamusException</code>를 상속받아
 * 정의하여야 한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class CamusException extends RuntimeException {
	private static final long serialVersionUID = 6123616847747730474L;

	public CamusException() {
	}

	public CamusException(String msg) {
		super(msg);
	}
}