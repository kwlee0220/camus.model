package camus.user;

import java.util.Collection;

import camus.place.PlaceNotFoundException;

import event.EventChannel;



/**
 * {@literal Users}는 CAMUS 컨텍스트 등록소 중에서 사용자 등록 정보를
 * 관리하는 인터페이스이다.
 * 
 * @author Kang-Woo Lee
 */
public interface Users extends EventChannel {
	public static String ROOT_ID = "root";
	
	/**
	 * 주어진 식별자에 해당하는 사용자 등록 정보를 반환한다.
	 * 
	 * @param	userId	검색에 사용할 사용자 식별자.
	 * @return	검색된 사용자 등록 정보 객체.
	 * @throws	UserNotFoundException	식별자에 해당하는 사용자 등록 정보가 없는 경우.
	 * @throws	InvalidArgumentException <code>userId</code>가 <code>null</code>인 경우.
	 */
    public UserInfo getUserInfo(String userId) throws UserNotFoundException;
    
    /**
     * 주어진 식별자에 해당하는 사용자 정보의 등록 여부를 반환한다.
     * 
     * @param userId	사용자 식별자
     * @return	등록 여부.
	 * @throws	InvalidArgumentException <code>userId</code>가 <code>null</code>인 경우.
     */
    public boolean existsUserInfo(String userId);
    
    /**
     * 사용자 컨텍스트 정보 등록소에 등록된 모든 사용자의 식별자를 반환한다.
     * 
     * @return	등록 사용자들의 식별자 리스트.
     */
    public Collection<String> getUserInfoIds();
    public Collection<UserInfo> getUserInfos();
    
    /**
     * 지정된 장소에 위치한 모든 사용자의 식별자를 반환한다.
     * <p>
     * <code>cover</code>가 <code>true</code>인 경우는 반환 결과에는 주어진 장소 뿐만 아니라
     * 모든 하위 장소에 위치한 사용자의 식별자도 포함된다.
	 * 만일 사용자가 없는 경우는 빈 배열을 반환한다.
     * 
     * @param placeId	대상 장소 식별자.
	 * @param cover 	하위 장소의 장치 포함 여부. <code>true</code>인 경우는 하위 장소에 설치된 장치도
	 * 					포함되고, <code>false</code>인 경우는 포함되지 않는다.
     * @return 사용자 식별자들의 배열.
	 * @throws PlaceNotFoundException	<code>placeId</code>에 해당하는 장소가 없는 경우.
     * @throws InvalidArgumentException	<code>placeId</code>가 <code>null</code>인 경우.
     */
    public Collection<String> getUserIdsAtPlace(String placeId, boolean cover)
    	throws PlaceNotFoundException;
	
	/**
	 * 주어진 질의어의 처리 결과로 검색된 사용자들의 등록 정보를 반환한다.
	 * <p>
	 * 질의어는 SPARQL만을 지원하고, 결과는 반드시 'result'라는 변수명으로 바인딩 되도록
	 * 작성되어야 한다.
	 * 
	 * @param query	검색에 사용할 SPARQL 질의어.
	 * @return	검색된  사용자의 등록 정보 리스트
	 * @throws InvalidArgumentException <code>query</code>이 <code>null</code>인 경우.
	 */
	public Collection<UserInfo> findUserInfos(String query);

    /**
     * 새 사용자 정보를 사용자 컨텍스트 등록소에 등록한다.
     * <p>
     * 새로 동록된 사용자의 위치는 지정되지 않은 것으로 설정되기 때문에
     * 사용자의 위치를 설정하려는 경우는 등록 후 {@link #setUserLocation(String, String)}으로
     * 별도로 설정하여야 한다.
     * 
     * @param info		등록할 사용자 등록 정보.
     * @throws UserExistsException		동일 식별자의 사용자가 이미 존재하는 경우.	
     * @throws InvalidArgumentException	<code>info</code> 또는<code>info.id</code>이
     * 									<code>null</code>인 경우.
     */
    public void addUserInfo(UserCreateInfo info) throws UserExistsException;
    
    /**
     * 지정된 사용자 등록 정보를 사용자 컨텍스트 등록소에서 제거한다.
     * <p>
     * 해당 사용자가 없는 경우 <code>false</code>를 반환한다.
     * 
     * @param	userId	제거할 사용자 등록 정보 식별자.
     * @return 	사용자 제거 여부.
	 * @throws	InvalidArgumentException	<code>userId</code>가 <code>null</code>인 경우.
     */
    public boolean removeUserInfo(String userId);
    
    /**
     * 사용자의 위치를 변경한다.
     * <p>
     * 기존 사용자의 위치를 제거하는 경우는 'placeId' 인자 값으로 <code>null</code> 사용하여 호출한다.
     * 
	 * @param userId	위치를 설정할 대상 사용자 등록 정보의 식별자.
	 * @param placeId	설정할 위치의 장소 식별자. 사용자의 위치를 제거하려는 경우는 <code>null</code>.
	 * @throws UserNotFoundException	<code>userId</code>에 해당하는 사용자가 존재하지 않는 경우.
	 * @throws PlaceNotFoundException	'placeId'가 <code>null</code>이 아닌 경우, 장소 식별자에
	 * 									해당하는 장소가 등록되어 있지 않은 경우.
     * @throws InvalidArgumentException	<code>userId</code>가 <code>null</code>인 경우.
     */
    public void setUserLocation(String userId, String placeId)
    	throws UserNotFoundException, PlaceNotFoundException;
    
	public void setUserLeftLocation(String userId, String placeId)
		throws UserNotFoundException, PlaceNotFoundException;
	
//    /**
//     * 주어진 사용자 이벤트 채널을 통해 이벤트를 발송한다.
//     * 
//     * @param userId	사용자 식별자.
//     * @param event		발송할 이벤트 객체.
//     * @throws UserNotFoundException	사용자 식별자에 해당하는 사용자가 등록되어 있지 않은 경우.
//     * @throws InvalidArgumentException	<code>userId</code> 또는 <code>event</code>가
//     * 									<code>null</code>인 경우.
//     */
//	public void publishEvent(String userId, Event event) throws UserNotFoundException;
//    
//    /**
//     * 주어진 사용자에 해당하는 이벤트 채널의 이벤트를 구독한다.
//     * <p>
//     * 사용자 이벤트 채널을 구독하게 되면 해당  채널을 통해 발송되는 모든 이벤트를
//     * 전달 받게 된다.
//     * 
//     * @param userId	사용자 식별자.
//     * @param subscriber	등록 대상 이벤트 구독자
//     * @throws	이벤트 구독자 등록 여부.
//     * @param subscriber 등록시킬 이벤트 구독자 객체.
//     * @throws UserNotFoundException	사용자 식별자에 해당하는 사용자가 등록	되어 있지 않은 경우.
//     * @throws InvalidArgumentException	<code>userId</code> 또는 <code>subscriber</code>가
//     * 								<code>null</code>인 경우.
//     */
//	public boolean subscribeEvent(String userId, EventSubscriber subscriber)
//		throws UserNotFoundException;
//	
//    /**
//     * 주어진 사용자에 해당하는 이벤트 채널의 이벤트를 구독한다.
//     * <p>
//     * 사용자 이벤트 채널을 구독하게 되면 해당 사용자 이벤트 채널을 통해 발송되는 모든 이벤트
//     * 중에서 주어진 필터를 만족하는 이벤트를 전달 받게 된다.
//     * 
//     * @param userId	사용자 식별자.
//     * @param filter	이벤트 필터
//     * @param subscriber	등록 대상 이벤트 구독자
//     * @throws	이벤트 구독자 등록 여부.
//     * @param subscriber 등록시킬 이벤트 구독자 객체.
//     * @throws UserNotFoundException	사용자 식별자에 해당하는 사용자가 등록되어 있지 않은 경우.
//     * @throws InvalidArgumentException	<code>userId</code>,  <code>filter</code> 또는
//     * 								 <code>subscriber</code>가 <code>null</code>인 경우.
//     */
//	public boolean subscribeEvent(String userId, String filter, EventSubscriber subscriber)
//		throws UserNotFoundException;
//	
//    /**
//     * 주어진 사용자의 이벤트 채널의 구독을 중지한다.
//     * <p>
//     * 사용자 식별자에 해당하는 사용자 등록되어 있지 않는 경우는 호출이 무시된다.
//     * 
//     * @param userId	사용자 식별자.
//     * @param subscriber	제거 대상 이벤트 구독자
//     * @throws	이벤트 구독자 등록 여부.
//     * @param subscriber 제거시킬 이벤트 구독자 객체.
//     * @throws InvalidArgumentException	<code>userId</code> 또는  <code>subscriber</code>가
//     * 								<code>null</code>인 경우.
//     */
//	public boolean unsubscribeEvent(String userId, EventSubscriber subscriber);
}
