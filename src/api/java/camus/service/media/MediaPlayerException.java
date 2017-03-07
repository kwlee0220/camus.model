package camus.service.media;

import camus.CamusException;


/**
 * <code>MediaPlayerException</code>는 {@link MediaPlayer}와 관련된 예외 객체를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class MediaPlayerException extends CamusException {
	private static final long serialVersionUID = 9095873265945935883L;
	
	public MediaPlayerException(String msg) {
		super(msg);
	}
}
