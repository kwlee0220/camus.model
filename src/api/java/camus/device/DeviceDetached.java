package camus.device;

import planet.idl.EventProperty;
import planet.idl.PlanetLocal;

import camus.device.Device;


/**
 * <code>DeviceDetached</code>는 장치({@link Device})가 CAMUS에서 단절될 때
 * 이벤트의 인터페이스를 정의한다.
 * <p>
 * <code>DeviceDetached는</code>는 {@link Devices} 이벤트 채널을 통해 발송된다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface DeviceDetached extends DeviceEvent {
	public static final String PROP_INFO = "deviceInfo";
	
	/**
	 * 단절된 {@link Device}의 등록 정보를 반환한다.
	 * 
	 * @return	장치 등록 정보.
	 */
	@EventProperty(name=PROP_INFO)
    public DeviceInfo getDeviceInfo();
}
