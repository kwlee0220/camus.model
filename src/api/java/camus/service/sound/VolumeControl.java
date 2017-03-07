package camus.service.sound;

/**
 * <code>VolumeControl</code>는 볼륨 제어 관련 Capability 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface VolumeControl {
	/**
	 * 현재 볼륨 값을 반환한다.
	 * <p>반환되는 값은 최소 {@link #getMinVolumeLevel()}부터 최대 {@link #getMaxVolumeLevel()} 사이의
	 * 값이고, 값이 클 수록 높은 볼륨을 의미한다.
	 * 
	 * @return 현재 설정된 볼륨 값.
	 * @throws SoundException	볼륨 값 읽기에 실패한 경우.
	 */
	public int getVolumeLevel() throws SoundException;
	
	/**
	 * 지정된 값으로 볼륨 값을 설정한다.
	 * <p>설정할 값은 반드시 최소 {@link #getMinVolumeLevel()}부터 최대 {@link #getMaxVolumeLevel()}
	 * 사이의 값이어야 한다. 값이 클 수록 높은 볼륨을 의미한다.
	 * 만일 이 범위에서 벗어나 값으로 호출되면 {@link InvalidArgumentException} 예외가 발생된다.
	 * 
	 * @param level	설정할 볼륨 값.
	 * @throws SoundException	장치 문제로 볼륨 값 설정에 실패한 경우.
	 * @throws InvalidArgumentException	설정할 볼륨 값이 허용된 범위를 벗어난 경우.
	 */
	public void setVolumeLevel(int level) throws SoundException, IllegalArgumentException;
	
	/**
	 * 허용 가능한 최대 볼륨 값을 반환한다.
	 * 
	 * @return 볼륨 값
	 */
	public int getMaxVolumeLevel();
	
	/**
	 * 허용 가능한 최소 볼륨 값을 반환한다.
	 * 
	 * @return 볼륨 값
	 */
	public int getMinVolumeLevel();
}
