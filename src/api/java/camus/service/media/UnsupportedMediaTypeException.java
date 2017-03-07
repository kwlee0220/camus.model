package camus.service.media;


/**
 * <code>UnsupportedMediaTypeException</code>는 지원되지 않은 형식의 미디어로 인해
 * 재생이 실패한 경우 발생되는 예외 클래스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class UnsupportedMediaTypeException extends MediaPlayerException {
	private static final long serialVersionUID = 4681788090230017126L;
	
	public UnsupportedMediaTypeException(String msg) {
		super(msg);
	}
}
