package camus.service;




/**
 * <code>PauseControl</code>은 CAMUS 서비스 수행 중 일시 중지 및 재개를 정의한
 * Capability 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface PauseControl {
	/**
	 * 현재 작업의 일시 중지 상태 여부를 반환한다.
	 * 
	 * @return 일시 중지된 상태이면 <code>true</code>, 그렇지 않으면 <code>false</code>.
	 */
	public boolean isPaused();
	
	/**
	 * 현재 진행 중인 작업을 일시 중지시킨다.
	 * 
	 * <p>만일 작업의 상태가 일시 중지가 불가능한 상태인 경우는 호출이 무시된다.
	 * 멈춘 작업은 이후 {@link #resume()} 호출을 통해 재개될 수 있다.
	 * 
	 * @throws PauseControlException	장치 문제로 작업 일시 중지에 실패한 경우.
	 * @throws IllegalStateException	이미 일시 중지인 경우.
	 */
	public void pause() throws PauseControlException;
	
	/**
	 * 일시 중지된 미디어 재생을 시작시킨다.
	 * 
	 * <p>작업이 재개되면, 일시 중지된 부분부터 다시 작업이 재개된다.
	 * 본 메소드는 작업이 일시 중지된 상태에서만 호출될 수 있으며, 그렇지 않은 경우는 호출은 무시된다.
	 * 
	 * @throws PauseControlException	장치 문제로 작업 재개가 실패한 경우.
	 * @throws IllegalStateException 	이미 수행 중인 경우
	 */
	public void resume() throws PauseControlException;
}
