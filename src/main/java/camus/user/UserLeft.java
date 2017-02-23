package camus.user;

import planet.idl.PlanetLocal;

import camus.place.PlaceEvent;

import event.support.EventBuilder;





/**
 * <code>UserLeft</code>는 사용자가 특정 장소에서 나가는 경우 발생되는
 * 이벤트를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface UserLeft extends UserEvent, PlaceEvent {
	public static UserLeft create(String userId, String placeId) {
		EventBuilder builder = new EventBuilder(UserLeft.class);
		builder.setProperty(UserEntered.PROP_USER_ID, userId);
		builder.setProperty(UserEntered.PROP_PLACE, placeId);
		return (UserLeft)builder.build();
	}
}
