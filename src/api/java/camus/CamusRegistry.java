package camus;

import camus.device.Devices;
import camus.place.Places;
import camus.user.Users;


/**
 * <code>ContextManager</code>는 CAMUS 상황정보 서비스의 인터페이스를 정의한다.
 * <p>
 * 
 * <p>CAMUS 상황정보 서버를 통해 다음과 같은 상황 정보를 접근할 수 있다.
 * <ul>
 * 	<li>{@link Users}: 사용자 등록 정보 관리자. 사용자의 식별자, 위치 정보를 접근할 수 있다.
 * 	<li>{@link Places}: 장소 등록 정보 관리자. CAMUS에 등록된 장소 정보 및 장소 간의 관계
 * 						정보를 접근할 수 있다.
 * 	<li>{@link Devices}: 장치 등록 정보 관리자. CAMUS에 등록된 장치을 접근할 수 있는 인터페이스 제공.
 * </ul> 
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface CamusRegistry {
	/**
	 * 사용자 등록 정보 관리자를 반환한다.
	 * 
	 * @return 사용자 등록 정보 관리자 객체.
	 */
	public Users getUsers();
    
    /**
     * 장소 등록 정보 관리자 객체를 반환한다.
     * 
     * @return 장소 등록 정보 관리자 객체.
     */
	public Places getPlaces();
	
	/**
	 * 장치 등록 정보 관리자 객체를 반환한다.
	 * 
	 * @return	장치 등록 정보 관리자 객체.
	 */
	public Devices getDevices();
}
