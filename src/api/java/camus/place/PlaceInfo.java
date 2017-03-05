package camus.place;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;

/**
 * <code>PlaceInfo</code>는 장소 등록 정보를 클래스이다.
 *
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class PlaceInfo {
	/** 장소 식별자 */
	@PlanetField(ordinal=0) public String id;
	/** 장소 이름 */
	@PlanetField(ordinal=1) public String name;

	public PlaceInfo() { }
	public PlaceInfo(String id, String name) {
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
		
		PlaceInfo other = (PlaceInfo)obj;
		return id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ",name=" + name + "]";
	}
}
