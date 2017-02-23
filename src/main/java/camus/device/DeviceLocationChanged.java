package camus.device;

import planet.idl.EventProperty;
import planet.idl.PlanetLocal;




/**
 * <code>DeviceLocationChanged</code>는 장치의 이동으로 위치한 장소가
 * 변경되는 경우 발생되는 이벤트를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface DeviceLocationChanged extends DeviceEvent {
	public static final String PROP_FROM_PLACE = "fromPlace";
	public static final String PROP_TO_PLACE = "toPlace";
	
	@EventProperty(name=PROP_FROM_PLACE)
    public String getFromPlace();
	
	@EventProperty(name=PROP_TO_PLACE)
    public String getToPlace();
}
