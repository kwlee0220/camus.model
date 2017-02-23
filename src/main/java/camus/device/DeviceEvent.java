package camus.device;

import planet.idl.EventProperty;
import planet.idl.PlanetLocal;

import event.Event;


/**
 * <code>DeviceEvent</code>는 장치 이벤트 인터페이스를 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface DeviceEvent extends Event {
	public static final String PROP_DEVICE_ID = "deviceId";
	
	/**
	 * 이벤트에 해당하는 장치 플랫폼의 식별자를 반환한다.
	 * 
	 * @return 장치 플랫폼 식별자.
	 */
	@EventProperty(name=PROP_DEVICE_ID)
    public String getDeviceId();
}
