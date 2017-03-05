package camus.place;

import planet.idl.PlanetLocal;

import event.Event;
import event.EventProperty;


/**
 * <code>PlaceEvent</code>는 장소에 관련된 이벤트의 공통 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface PlaceEvent extends Event {
	public static final String PROP_PLACE = "place";
	
	/**
	 * 장소 식별자를 반환한다.
	 * 
	 * @return	장소 식별자.
	 */
	@EventProperty(name=PROP_PLACE)
    public String getPlace();
}