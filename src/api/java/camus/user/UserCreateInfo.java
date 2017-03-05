package camus.user;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;

/**
 * {@literal UserCreateInfo}는 사용자 컨텍스트 등록소에 추가할 사람 등록 정보를 표현한 클래스이다.
 *
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class UserCreateInfo {
	/** 사용자 식별자 */
	@PlanetField(ordinal=0) public String id;
	/** 사용자 이름 */
	@PlanetField(ordinal=1) public String name;

	public UserCreateInfo() { }
	public UserCreateInfo(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj == this ) {
			return true;
		}
		else if ( obj == null || obj.getClass() != getClass() ) {
			return false;
		}
		
		UserCreateInfo other = (UserCreateInfo)obj;
		return id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("[id=%s,name=%s]", id, name);
	}
}
