package camus.user;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;

/**
 * {@literal UserInfo}는 사용자 등록 정보를 클래스이다.
 *
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class UserInfo {
	/** 사용자 식별자 */
	@PlanetField(ordinal=0) public String id;
	/** 사용자 이름 */
	@PlanetField(ordinal=1) public String name;
	/** 사용자가 위치한 장소 식별자 */
	@PlanetField(ordinal=2) public String place;

	public UserInfo() { }
	public UserInfo(String id, String name, String placeId) {
		this.id = id;
		this.name = name;
		this.place = placeId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj == this ) {
			return true;
		}
		else if ( obj == null || obj.getClass() != getClass() ) {
			return false;
		}
		
		UserInfo other = (UserInfo)obj;
		return id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("[id=%s,name=%s,loc=%s]", id, name, place);
	}
}
