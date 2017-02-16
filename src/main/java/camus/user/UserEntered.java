package camus.user;

import camus.place.PlaceEvent;

import event.support.EventBuilder;



/**
 * <code>UserEntered</code>는 사용자가 특정 장소로 진입하는 경우 발생되는
 * 이벤트를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface UserEntered extends UserEvent, PlaceEvent {
	public static UserEntered create(String userId, String placeId) {
		EventBuilder builder = new EventBuilder(UserEntered.class);
		builder.setProperty(UserEntered.PROP_USER_ID, userId);
		builder.setProperty(UserEntered.PROP_PLACE, placeId);
		return (UserEntered)builder.build();
	}
}
