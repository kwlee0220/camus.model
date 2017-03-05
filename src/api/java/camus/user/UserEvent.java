package camus.user;

import planet.idl.PlanetLocal;

import event.Event;
import event.EventProperty;

/**
 * <code>UserEvent</code>는 사용자와 관련된 이벤트의 공통 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface UserEvent extends Event {
	public static final String PROP_USER_ID = "userId";
	
	/**
	 * 사용자 식별자를 반환한다.
	 * 
	 * @return	사용자 식별자.
	 */
	@EventProperty(name=PROP_USER_ID)
    public String getUserId();
}
