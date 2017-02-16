package camus.device;

import event.EventPublisher;


/**
 * <code>Device</code>는 CAMUS 미들웨어를 통해 제공되는 장치 인터페이스를 제공한다.
 * <p>
 * 장치는 하나 이상의 장치 서비스를 제공한다. 장치 서비스는 전역 서비스 (global service)와
 * 달리 서비스 효과가 소속 장치에서 발현된다. 예를 들어 "<code>irobiq</code>"라는 장치에서
 * 카메라 서비스를 제공하는 경우 이를 통해 해당 장치에 장착된 카메라를 제어하게 된다.
 * <p>
 * 모든 장치는 시작되면 {@link Devices}에 자신의 참조를 등록하기 때문에,
 * CAMUS에 연결된 모든 장치는 {@link Devices}를 통해 접근할 수 있다. 또한 장치의 등록 정보
 * ({@link DeviceInfo})를 통해 장치의 소유자, 위치 등의 정보를 기반으로 응용에 적합한
 * 장치를{@link Devices}  검색할 수 있다.
 * 장치의 참조가 {@link Devices}에 등록되거나, 삭제되는 경우는 각각  {@link DeviceAttached}
 * 이벤트와 {@link DeviceDetached} 이벤트가 {@link Devices}를 통해 발송되기 때문에
 * {@link Devices}에 이벤트 구독자를 구독을 통해 장치의 접속/단절과 연관된 작업을 수행하는
 * 응용을 작성할 수 있다.
 * <p>
 * 모든 CAMUS 장치는 그것의 등록 정보가 {@link Devices}에서 관리되기 때문에 각 장치의
 * 설치 위치, 소유자, 및 현재 위치 정보를 얻을 수 있다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface Device extends ServiceDirectory, EventPublisher {
	/**
	 * 장치 식별자를 반환한다.
	 * <p>
	 * 장치 식별자는 CAMUS 미들웨어에 등록된 모든 장치에 대해 유일한 값이다.
	 * 
	 * @return	장치 플랫폼 식별자.
	 */
	public String getId();
}