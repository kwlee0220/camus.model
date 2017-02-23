package camus.device;

import planet.idl.EventProperty;
import planet.idl.PlanetLocal;

import camus.device.Device;


/**
 * <code>DeviceAttached</code>는 장치({@link Device})의 참조가
 * {@link Devices}에 등록될 때 발생되는 이벤트의 인터페이스를 정의한다.
 * <p>
 * <code>DeviceAttached</code>는 {@link Devices} 이벤트 채널을 통해 발송된다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface DeviceAttached extends DeviceEvent {
	public static final String PROP_INFO = "deviceInfo";
	
	/**
	 * 접속된 {@link Device}의 등록 정보를 반환한다.
	 * 
	 * @return	장치 등록 정보.
	 */
	@EventProperty(name=PROP_INFO)
    public DeviceInfo getDeviceInfo();
}
