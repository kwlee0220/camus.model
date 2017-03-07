package camus.service;


/**
 * <code>PowerControl</code>는 전원 제어 capability의 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface PowerControl {
	/**
	 * 장치의 전원 상태를 반환한다.
	 * 
	 * @return 전원을 껸 상태이면 <code>true</code>, 그렇지 않으면 <code>false</code>.
	 * @throws PowerControlException	장치 문제로 전원 상태 획득에 실패한 경우.
	 */
	public boolean getPower() throws PowerControlException;
	
	/**
	 * 장치의 전원 상태를 바꾼다.
	 * 
	 * <p><code>power</code>가  <code>true</code>인 경우는 전원을 켜는 것을 의미하고,
	 * <code>false</code>인 경우는 전원을 끄는 것을 의미한다.
	 * 
	 * @param power	설정한 전원 상태.
	 * @throws PowerControlException	장치 문제로 전원 상태 설정에 실패한 경우.
	 */
	public boolean setPower(boolean power) throws PowerControlException;
}
