package camus.service.sound;



/**
 * <code>MuteControl</code>는 음소거와 관련된 서비스 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface MuteControl {
	/**
	 * 현재 음소거 상태를 반환한다.
	 * <p><code>true</code>인 경우는 음소거 상태이고, 그렇지 않은 경우는 <code>false</code> 이다.
	 * 
	 * @return	현 음소거 상태.
	 * @throws	SoundException	장치 문제로 음소거 상태 값 읽기가 실패한 경우.
	 */
	public boolean getMute() throws SoundException;
	
	/**
	 * 현 음소거 상태를 설정한다.
	 * <p>음소거를 시키려면 <code>true</code>, 그러지 않으려면 <code>false</code>로 설정한다.
	 * 
	 * @param mute		설정할 음소거 값.
	 * @throws SoundException	장치 문제로 음소거 설정에 실패한 경우.
	 */
	public void setMute(boolean mute) throws SoundException;
}
