package camus.user;

import event.EventProperty;
import event.support.EventBuilder;



/**
 * <code>UserLocationChanged</code>는 사용자가 위치한 장소가 바뀌는 경우 발생되는
 * 이벤트를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface UserLocationChanged extends UserEvent {
	public static final String ID = UserLocationChanged.class.getName();
	public static final String PROP_FROM_PLACE = "fromPlace";
	public static final String PROP_TO_PLACE = "toPlace";
	
	@EventProperty(name=PROP_FROM_PLACE)
    public String getFromPlace();
	
	@EventProperty(name=PROP_TO_PLACE)
    public String getToPlace();
	
	public static UserLocationChanged create(String userId, String fromPlaceId, String toPlaceId) {
		EventBuilder builder = new EventBuilder(UserLocationChanged.class);
		builder.setProperty(UserLocationChanged.PROP_USER_ID, userId);
		builder.setProperty(UserLocationChanged.PROP_FROM_PLACE, fromPlaceId);
		builder.setProperty(UserLocationChanged.PROP_TO_PLACE, toPlaceId);
		return (UserLocationChanged)builder.build();
	}
}
