package camus.place;

import java.util.Collection;

import event.EventChannel;



/**
 * {@literal Places}는 CAMUS 등록소 중에서 장소 등록 정보를
 * 관리하는 인터페이스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface Places extends EventChannel {
    public static final String ROOT_PLACE_ID = "/";
    public static final char DELIM = '/';
    
	/**
	 * 주어진  식별자에 해당하는 장소 등록 정보를 반환한다.
	 * 
	 * @param	placeId	대상 장소 식별자.
	 * @return	검색된 장소 정보.
	 * @throws PlaceNotFoundException	장소 식별자에 해당하는 장소가 없는 경우.
	 * @throws IllegalArgumentException	<code>placeId</code>가 <code>null</code>인 경우.
	 */
	public PlaceInfo getPlaceInfo(String placeId) throws PlaceNotFoundException;
	
	/**
	 * 주어진 장소 식별자에 해당하는 장소의 등록 여부를 반환한다.
	 * 
	 * @param placeId	대상 장소 식별자.
	 * @return	장소 등록 여부.
	 * @throws IllegalArgumentException	<code>placeId</code>가 <code>null</code>인 경우.
	 */
	public boolean existsPlaceInfo(String placeId);
	
	/**
	 * <code>subPlaceId</code>에 해당하는 장소가 <code>placeId</code>에 해당하는 장소의
	 * 하위 장소인지 여부를 반환한다.
	 * <p>
	 * 만일 <code>subPlaceId</code> 또는 <code>placeId</code>가 <code>null</code>인 경우 혹은
	 * 각각에 해당하는 장소 정보가 등록되어 있지 않은 경우는 <code>false</code>를 반환한다.
	 * 
	 * @param subPlaceId	예상 하위 장소의 식별자.
	 * @param placeId		예상 상위 장소의 식별자.
	 * @param cover TODO
	 * @return	하위 장소인 경우는 <code>true</code>, 그렇지 않은 경우는 <code>false</code>.
	 */
	public boolean isSubPlaceOf(String subPlaceId, String placeId, boolean cover);
	
	/**
	 * 장소 컨텍스트 등록소에 등록된 모든 장소들의 식별자를 반환한다.
	 * 
	 * @return	장소 식별자의 리스트.
	 */
	public Collection<String> getPlaceInfoIds();
	
	public Collection<PlaceInfo> getPlaceInfos();
	
	/**
	 * 주어진 식별자에 해당하는 장소의 말단(leaf) 여부를 반환한다.
	 * 하위 장소가 존재하지 않은 장소는 말단(leaf) 장소라 정의한다.
	 * 
	 * @param placeId	대상 장소의 식별자
	 * @return	말단 장소 여부.
	 */
	public boolean isLeafPlaceInfo(String placeId);
	
	/**
	 * 주어진 식별자에 해당하는 장소의 모든 하위 장소의 식별자를 반환한다.
	 * 
	 * @param placeId	대상 장소의 식별자
	 * @param cover 	<code>true</code>인 경우는 모든 하위 장소를 모두 포함되고,
	 * 					<code>false</code>인 경우는 바로 1단계 하위  장소만 포함. 
	 * @return 하위 장소 식별자 리스트. 하위 장소가 없는 경우는 길이 0의 리스트를 반환한다.
	 * @throws PlaceNotFoundException	<code>placeId</code>에 해당하는 장소가 없는 경우.
	 * @throws IllegalArgumentException	<code>placeId</code>가 <code>null</code>인 경우.
	 */
	public Collection<String> getSubPlaceIds(String placeId, boolean cover)
		throws PlaceNotFoundException;
	
	/**
	 * 주어진 질의어의 처리 결과로 검색된 장소들의 등록 정보를 반환한다.
	 * <p>
	 * 질의어는 SPARQL만을 지원하고, 결과는 반드시 'result'라는 변수명으로 바인딩 되도록
	 * 작성되어야 한다.
	 * 
	 * @param query	검색에 사용할 SPARQL 질의어.
	 * @return	검색된 장소의 등록 정보 리스트
	 * @throws IllegalArgumentException <code>query</code>이 <code>null</code>인 경우.
	 */
	public Collection<PlaceInfo> findPlaceInfos(String query);

	/** 
	 * 주어진 장소 등록 정보를 장소 컨텍스트 등록소에 등록시킨다.
	 * <p>
	 * 인자로 주어진 {@link PlaceInfo}에서 {@link PlaceInfo#id}은 생성될 장소에게
	 * 부여될 식별자로 사용되며 <code>null</code>일 수 없다.
	 * 
	 * @param info	생성할 장소의 등록 정보.
	 * @throws PlaceExistsException	<code>info.id</code>에 해당하는 장소가 이미
	 * 									존재하는 경우.
	 * @throws PlaceNotFoundException	<code>info.id</code>에 해당하는 장소의 상위 장소가
	 * 									등록되어 있지 않은 경우.
	 * @throws IllegalArgumentException	<code>info</code> 또는 <code>info.id</code>가
	 * 									<code>null</code>인 경우.
	 */
    public void addPlaceInfo(PlaceInfo info)
    	throws PlaceExistsException, PlaceNotFoundException;
    
    /**
     * 주어진 장소 식별자에 해당하는 장소 등록 정보를 제거한다.
     * <p>
     * 만일 주어진 장소가 하위 장소를 포함하는 경우는 모든 하위 장소의 등록 정보도 제거된다.
     * 
     * @param placeId	제거할 장소 식별자.
     * @return 성공적으로 장소가 제공된 경우는 <code>true</code>, 그렇지 않은 경우는 <code>false</code>
	 * @throws PlaceNotFoundException	<code>placeId</code>에 해당하는 장소가 등록되어 있지 않은 경우.
     * @throws IllegalArgumentException	<code>placeId</code>가 <code>null</code>인 경우.
     */
    public boolean removePlaceInfo(String placeId) throws PlaceNotFoundException;
	
	/**
	 * 주어진 장소의 등록정보를 변경시킨다.
	 * <p>
	 * 변경할 대상 장소는 <code>info.id</code>를 이용하고, 변경 대상은 {@link PlaceInfo#name}만
	 * 가능하다.
	 *  
	 * @param info	변경될 새 장소 정보 값.
	 * @throws PlaceNotFoundException	장소 식별자(<code>info.id</code>)에 해당하는 장소가 없는 경우.	
	 * @throws IllegalArgumentException	<code>info</code> 또는 <code>info.id</code>가
	 * 									<code>null</code>인 경우.
	 */
	public void updatePlaceInfo(PlaceInfo info) throws PlaceNotFoundException;

	public static int getGeneration(String placeId, String subPlaceId) {
		if ( !subPlaceId.startsWith(placeId) ) {
			return -1;
		}
		
		return PlaceUtils.toNodeList(subPlaceId).size() - PlaceUtils.toNodeList(placeId).size();
	}

	/**
	 * 주어진 장소의 상위 장소의 식별자를 반환한다.
	 * 
	 * @param placeId	대상 장소의 식별자.
	 * @return	상위 장소 식별자.
	 * @throws IllegalArgumentException	장소 식별자가 <code>null</code>이거나 잘못된 형식인 경우.
	 */
	public static String getDirectSuperPlaceId(String placeId) {
		if ( placeId == null ) {
			throw new IllegalArgumentException("placeId was null");
		}
		
		int index = placeId.lastIndexOf(PlaceUtils.DELIM);
		if ( index == 0 ) {
			if ( placeId.equals("/") ) {
				return null;
			}
			else {
				return "/";
			}
		}
		else if ( index > 0 ) {
			return placeId.substring(0, index);
		}
		else {
			throw new IllegalArgumentException("place id=" + placeId);
		}
	}

	/**
	 * 주어진 식별자 {@literal locId}의  장소가 주어진 장소 {@literal subLocId}의 상위 장소인지
	 * 여부를 반환한다.
	 * 
	 * @param placeId		대상 상위 객체.
	 * @param subPlaceId	대상 하위 객체.
	 * @return {@literal placeId}에 해당하는 장소가 {@literal subPlaceId}에 해당하는 장소의
	 * 			상위 장소인 경우는 true, 그렇지 않거나 또는 {@literal subPlaceId}가
	 * 			<code>null</code>인 경우는 false.
	 * @throws IllegalArgumentException	{@literal placeId}가 <code>null</code>인 경우.
	 */
	public static boolean isDirectSuperPlaceOf(String placeId, String subPlaceId)
		throws IllegalArgumentException {
		if ( placeId == null ) {
			throw new IllegalArgumentException("placeId was null");
		}
		
		if ( subPlaceId == null || !subPlaceId.startsWith(placeId)
			|| subPlaceId.equals(placeId) ) {
			return false;
		}
		
		return subPlaceId.substring(placeId.length()).indexOf(PlaceUtils.DELIM) == 0;
	}

	/**
	 * 주어진 식별자 {@literal placeId}의 장소가 주어진 장소 {@literal subPlaceId}의 조상 장소인지
	 * 여부를 반환한다.
	 * 
	 * @param placeId	대상 조상 장소 식별자.
	 * @param subPlaceId	대상 후손 장소 식별자.
	 * @return {@literal placeId}에 해당하는 장소가 {@literal subPlaceId}에 해당하는 장소의
	 * 			조상 장소인 경우는 true, 그렇지 않거나 또는 {@literal subPlaceId}가
	 * 			<code>null</code>인 경우는 false.
	 * @throws IllegalArgumentException	{@literal placeId}가 <code>null</code>인 경우.
	 */
	public static boolean isSuperPlaceOf(String placeId, String subPlaceId)
		throws IllegalArgumentException {
		if ( placeId == null ) {
			throw new IllegalArgumentException("placeId was null");
		}
		
		if ( subPlaceId == null || !subPlaceId.startsWith(placeId)
			|| subPlaceId.equals(placeId) ) {
			return false;
		}
		else {
			return true;
		}
	}

	public static String getCommonAncestorPathOf(String place1, String place2) {
		final char[] path1 = place1.toCharArray();
		final char[] path2 = place2.toCharArray();
	
		final int minLength = Math.min(path1.length, path2.length);
		
		int i;
		for ( i =0; i < minLength; ++i ) {
			if ( path1[i] != path2[i] ) {
				--i;
				break;
			}
		}
		
		if ( i == 0 ) {
			i = 1;
		}
		return new String(path1, 0, i);
	}
}