package camus.place;

import camus.CamusException;


/**
 * <code>PlaceException</code>는 장소 관련된 예외 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class PlaceException extends CamusException {
	private static final long serialVersionUID = 5400562935406806813L;

	public PlaceException(String name) {
		super(name);
	}
}
