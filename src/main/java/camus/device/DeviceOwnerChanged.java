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
public interface DeviceOwnerChanged extends DeviceEvent {
	public static final String PROP_OLD_OWNER = "oldOwner";
	public static final String PROP_NEW_OWNER = "newOwner";
	
	@EventProperty(name=PROP_OLD_OWNER)
    public String getOldOwner();
	
	@EventProperty(name=PROP_NEW_OWNER)
    public String getNewOwner();
}
