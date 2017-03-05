package camus.place;



/**
 * <code>PlaceNotFoundException</code>는 지정된 장소가 없는 경우 발생하는 예외 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class PlaceNotFoundException extends PlaceException {
	private static final long serialVersionUID = -7947720055409179720L;

	public PlaceNotFoundException(String msg) {
		super(msg);
	}
}
