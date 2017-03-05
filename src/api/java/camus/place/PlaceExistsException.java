package camus.place;



/**
 * <code>PlaceExistsException</code>는 지정된 장소가 이미 존재하는 경우
 * 발생하는 예외 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class PlaceExistsException extends PlaceException {
	private static final long serialVersionUID = 1878658949810973646L;

	public PlaceExistsException(String details) {
		super(details);
	}
}
