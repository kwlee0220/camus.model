package camus.device;

import java.util.Collection;

import camus.place.PlaceNotFoundException;
import camus.user.UserNotFoundException;



/**
 * {@literal Devices}는 CAMUS 등록소 중에서 장치 등록 정보 및 연결된 장치의 참조를
 * 관리하는 인터페이스이다.
 * <p>
 * 모든 CAMUS 장치는 그것이 시작되면 {@literal Devices}에 자신의 참조를 등록하게 되고,
 * 이때 {@literal Devices}를 통해 {@link DeviceAttached} 이벤트를 발송하게 된다.
 * 반대로 장치가 종료되거나 장치와의 연결이 단절되는 경우는 해당 참조가 {@literal Devices}에서
 * 제거되고 {@link DeviceAttached} 이벤트가  {@literal Devices}를 통해 발송된다.
 * <br>
 * 사용자는 {@literal Devices}를 이용하여 다음과 같은 기준으로 장치를 검색할 수 있다.
 * <ul>
 * 	<li> 장치 식별자: 검색하고자 하는 장치의 식별자를 통한 장치 등록 정보 및 장치 참조의 검색
 * 	<li> 장치 소유자: 지정된 사용자가 소유한 장치들의 등록 정보 및 참조의 검색
 * 	<li> 장치 위치: 지정된 장소에 위치한 장치들의 등록 정보 및 참조의 검색
 * 	<li> 사용자 위치: 지정된 사용자의 위치한 장소에 위치한 장치들의 등록 정보 및 참조의 검색
 * </ul>
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface Devices {
	/** 전역 서비스 디렉토리 식별자. */
	public static final String GSD_ID = "global";
	
	/**
	 * 주어진 식별자에 해당하는 장치의 등록 정보를 반환한다.
	 * 
	 * @param deviceId	설정 정보를 획득할 대상 장치의 식별자.
	 * @return	해당 장치의 등록 정보.
	 * @throws DeviceNotFoundException	<code>deviceId</code>에 해당하는 장치가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>deviceId</code>가 <code>null</code>인 경우.
	 */
	public DeviceInfo getDeviceInfo(String deviceId) throws DeviceNotFoundException;
	
	/**
	 * 장치 등록소에 등록된 모든 장치의 식별자를 반환한다.
	 * 
	 * @return	장치 등록 정보 객체 리스트.
	 */
	public Collection<String> getDeviceInfoIds();
	public Collection<DeviceInfo> getDeviceInfos();
	
	/**
	 * 주어진 사용자 소유의 장치들의 식별자를 모두 반환한다.
	 * 
	 * @param ownerId	사용자 식별자.
	 * @return	장치 식별자 리스트.
	 * @throws UserNotFoundException <code>ownerId</code>에 해당하는 사용자가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>ownerId</code>가 <code>null</code>인 경우.
	 */
	public Collection<String> getDeviceIdsOfOwner(String ownerId) throws UserNotFoundException;
	
	/**
	 * 컨텍스트 등록소에 등록된 장치들 중에서 주어진 장소에 위치한(포함된) 모든 장치의 식별자들을 반환한다.
	 * 
	 * @param placeId	 대상 장소 식별자.
	 * @param cover 	하위 장소 장치 포함 여부. <code>true</code>인 경우는 하위 장소에 설치된 장치도
	 * 					포함되고, <code>false</code>인 경우는 포함되지 않는다.
	 * @return	장치 식별자 리스트.
	 * @throws PlaceNotFoundException <code>placeId</code>에 해당하는 장소가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>placeId</code>가 <code>null</code>인 경우.
	 */
	public Collection<String> getDeviceIdsAtPlace(String placeId, boolean cover)
		throws PlaceNotFoundException;
	
	/**
	 * 주어진 사용자가 위치한 장소에 있는 모든 등록된 장치의 식별자를 반환한다.
	 * <p>
	 * 만일 사용자의 장소가 지정되어 있지 않은 경우는 빈 리스트를 반환한다.
	 * 
	 * @param ownerId	사용자 식별자.
	 * @param cover 	하위 장소 장치 포함 여부. <code>true</code>인 경우는 하위 장소에 설치된 장치도
	 * 					포함되고, <code>false</code>인 경우는 포함되지 않는다.
	 * @return	장치 식별자 리스트.
	 * @throws UserNotFoundException <code>ownerId</code>에 해당하는 사용자가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>ownerId</code>가 <code>null</code>인 경우.
	 */
	public Collection<String> getDeviceIdsAtOwnerPlace(String ownerId, boolean cover)
		throws UserNotFoundException;
	
	/**
	 * 주어진 질의어의 처리 결과로 검색된 장치들의 등록 정보를 반환한다.
	 * <p>
	 * 질의어는 SPARQL만을 지원하고, 결과는 반드시 'result'라는 변수명으로 바인딩 되도록
	 * 작성되어야 한다.
	 * 
	 * @param query	검색에 사용할 SPARQL 질의어.
	 * @return	검색된 장치의 등록 정보 리스트
	 * @throws InvalidArgumentException <code>query</code>이 <code>null</code>인 경우.
	 */
	public Collection<DeviceInfo> findDeviceInfos(String query);

	/**
	 * 새 장치을 장치 등록소에 등록시킨다.
	 * <p>
	 * 등록할 장치의 소유자가 정의되지 않은 경우는 <code>info.owner</code>는 <code>null</code>로
	 * 설정되어야 하고, 장치의 위치가 지정되지 않은 경우는 <code>info.location</code>는
	 * <code>null</code>로 설정되어야 한다.
	 * <br>
	 * 새로 등록된 장치는 무조건 서버에 접속되지 않은 것으로  등록되기 때문에
	 * 장치 정보는 해당 장치가 접속되기 전에 등록하여야 한다.
	 * 
	 * @param info	추가될 장치의 등록 정보
	 * @throws DeviceExistsException 	동일 식별자의 장치이 이미 등록되어 있는 경우.
	 * @throws UserNotFoundException 	<code>info.owner</code>가 <code>null</code>이 아닌 경우,
	 * 									해당 사용자 식별자에 해당하는 사용자가 등록되어 있지 않은 경우.
	 * @throws PlaceNotFoundException	<code>info.location</code>가 <code>null</code>이 아닌 경우,
	 * 									해당 장소 식별자에 해당하는 장소가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>info</code> 또는 <code>info.id</code>가
	 * 									<code>null</code>인 경우.
	 */
	public void addDeviceInfo(DeviceInfo info)
		throws DeviceExistsException, UserNotFoundException, PlaceNotFoundException;
	
	/**
	 * 장치 등록소에서 주어진 식별자의 장치를 제거한다.
	 * 
	 * @param deviceId	제거할 장치의 식별자.
	 * @return 장치 제거 여부. <code>deviceId</code>에 해당하는 장치가 없거나 기타 이유로 장치가 제거되지
	 * 			않은 경우는 <code>false</code>.
	 * @throws InvalidArgumentException	<code>deviceId</code>가 <code>null</code>인 경우.
	 */
	public boolean removeDeviceInfo(String deviceId);
	
	/**
	 * 주어진 장치의 소유자를 변경시킨다.
	 * <p>
	 * 만일 새 소유자의 식별자가 <code>null</code>인 경우는 장치의 소유자 정보를 제거하는 것을 의미한다.
	 * 
	 * @param deviceId 변경대상 장치의 식별자
	 * @param ownerId 변경될 소유자의 식별자
	 * @throws DeviceNotFoundException	식별자에 해당하는 장치가 등록되어 있지 않은 경우
	 * @throws UserNotFoundException	<code>ownerId</code>가 <code>null</code>이 아닌 경우,
	 * 									해당 사용자 식별자에 해당하는 사용자가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>deviceId</code>가 <code>null</code>인 경우
	 */
	public void setDeviceOwner(String deviceId, String ownerId)
		throws DeviceNotFoundException, UserNotFoundException;
	
	/**
	 * 주어진 장치의 위치를 변경시킨다.
	 * <p>
	 * 만일 새 장소의 식별자가 <code>null</code>인 경우는 장치의 위치 정보를 제거하는 것을 의미한다.
	 * 
	 * @param deviceId	변경 대상 장치의 식별자.
	 * @param placeId	변경될 장소의 식별자
	 * @throws DeviceNotFoundException	식별자에 해당하는 장치가 등록되어 있지 않은 경우.
	 * @throws PlaceNotFoundException	<code>placeId</code>가 <code>null</code>이 아닌 경우,
	 * 									해당 장소 식별자에 해당하는 장소가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>deviceId</code>가 <code>null</code>인 경우.
	 */
	public void setDeviceLocation(String deviceId, String placeId)
		throws DeviceNotFoundException, PlaceNotFoundException;
	
	/**
	 * CAMUS 서버에 접속된 장치들 중에서 주어진 식별자에 해당하는 장치 객체를 반환한다.
	 * 
	 * @param deviceId	대상 장치 식별자.
	 * @return	검색된 장치 객체.
	 * @throws DeviceNotFoundException	<code>deviceId</code>에 해당하는 장치가 등록되어 있지
	 * 									않거나 접속되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>deviceId</code>가 <code>null</code>인 경우.
	 */
	public Device getDevice(String deviceId) throws DeviceNotFoundException;
	
	public Device getDeviceIfConnected(String deviceId);
	
	/**
	 * CAMUS 서버에 접속된 모든 장치를 반환한다.
	 * 
	 * @return	접속된 장치의 식별자 리스트.
	 */
	public Collection<String> getConnectedDeviceIds();
	
	/**
	 * 접속된 장치들 중에서 주어진 사용자 소유의 장치를 모두 반환한다.
	 * 
	 * @param ownerId	사용자 식별자.
	 * @return	장치 리스트.
	 * @throws UserNotFoundException <code>ownerId</code>에 해당하는 사용자가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>ownerId</code>가 <code>null</code>인 경우.
	 */
	public Collection<Device> getDevicesOfOwner(String ownerId) throws UserNotFoundException;
	
	/**
	 * 접속된 장치들 중에서 주어진 장소에 위치한 장치를 모두 반환한다.
	 * 
	 * @param placeId	 장소 식별자.
	 * @param cover 	하위 장소의 장치 포함 여부. <code>true</code>인 경우는 하위 장소에 설치된 장치도
	 * 					포함되고, <code>false</code>인 경우는 포함되지 않는다.
	 * @return	장치 리스트.
	 * @throws PlaceNotFoundException	<code>placeId</code>에 해당하는 장소가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>placeId</code>가 <code>null</code>인 경우.
	 */
	public Collection<Device> getDevicesAtPlace(String placeId, boolean cover)
		throws PlaceNotFoundException;
	
	/**
	 * 주어진 사용자가 위치한 장소에 접속되어 있는 모든 장치를 반환한다.
	 * <p>
	 * 만일 사용자의 장소가 지정되어 있지 않은 경우는 빈 리스트를 반환한다.
	 * 
	 * @param ownerId	사용자 식별자.
	 * @param cover 	하위 장소의 장치 포함 여부. <code>true</code>인 경우는 하위 장소에 설치된 장치도
	 * 					포함되고, <code>false</code>인 경우는 포함되지 않는다.
	 * @return	장치 리스트.
	 * @throws UserNotFoundException <code>ownerId</code>에 해당하는 사용자가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	<code>ownerId</code>가 <code>null</code>인 경우.
	 */
	public Collection<Device> getDevicesAtOwnerPlace(String ownerId, boolean cover)
		throws UserNotFoundException;
	/**
	 * 주어진 장치가 접속되었음을 장치 등록소에 통보한다.
	 * 
	 * @param deviceId	등록될 장치의 식별자.
	 * @param device	등록될 장치 객체.
	 * @throws DeviceExistsException	동일 식별자를 갖는 장치가 접속된 경우.
	 * @throws DeviceNotFoundException 식별자에 해당한는 장치가 등록되어 있지 않은 경우.
	 * @throws InvalidArgumentException	{@literal deviceId} 또는 {@literal device}가
	 * 									<code>null</code>인 경우.
	 */
	public void onDeviceConnected(String deviceId, Device device)
		throws DeviceExistsException, DeviceNotFoundException;
	
	/**
	 * 장치 등록소에 등록된 장치 중에서 주어진 식별자에 해당하는 장치를 접속 해제시킨다.
	 * 지정된 식별자에 해당하는 장치가 없는 경우는 호출은 무시된다.
	 * 
	 * @param deviceId	접속 해제시킬 장치의 식별자.
	 * @throws InvalidArgumentException	{@literal deviceid}가 <code>null</code>인 경우.
	 */
	public void onDeviceDisconnected(String deviceId);
}
