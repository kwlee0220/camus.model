package camus.service.media;

/**
 * <code>MediaPlayTimeControl</code>는 미디어 재생 시간을 제어하는 Capability의
 * 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface MediaPlayTimeControl {
	/**
	 * 현재 재생 시간을 반환한다.
	 * <p>재생 시간은 음악이 시작한 후 경과 시간을 milli-second 단위로 반환한다.
	 * 
	 * @return	재생 시간.
	 * @throws	MediaPlayerException	장치 문제로 재생 시간 반환이 실패한 경우.
	 */
	public long getPlayTimeInMillis() throws MediaPlayerException;
	
	/**
	 * 미디어 재생 시작 시간을 설정한다.
	 * <p>본 메소드는 실제 음악이 시작되지 전에 설정하여야 하고, 재생이 시작된 후 호출을
	 * 구현에 따라 결과가 달라질 수 있다. 설정 시간은 milli-second 단위로 지정된다.
	 * 
	 * @param time		설정할 음악 재생 시작 시간 값.
	 * @throws MediaPlayerException	장치 문제로 음악 재생 시작 시간 설정 설정에 실패한 경우.
	 * @throws InvalidArgumentException	설정할 재생 시간이 음수인 경우.
	 * @throws IllegalStateException	미디어 재생 시간을 설정할 수 없는 상태인 경우.
	 */
	public void setPlayTimeInMillis(long time) throws MediaPlayerException, IllegalArgumentException;
}
