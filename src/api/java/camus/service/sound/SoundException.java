package camus.service.sound;

import camus.CamusException;


/**
 * <code>SoundException</code>는 사운드 제어 연산 중 발생되는 예외를 정의한
 * 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class SoundException extends CamusException {
	private static final long serialVersionUID = -3272566994166109610L;
	
	public SoundException(String msg) {
		super(msg);
	}
}
