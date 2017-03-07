package camus.service.sound;

import camus.CamusException;


/**
 * <code>PcmPlayerException</code>는 PCM 재생 서비스 호출시 발생되는 예외를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class PcmPlayerException extends CamusException {
	private static final long serialVersionUID = -5937564394670250360L;
	
	public PcmPlayerException(String msg) {
		super(msg);
	}
}
