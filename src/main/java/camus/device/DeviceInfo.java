package camus.device;

/**
 * {@literal DeviceInfo}는 CAMUS에서 수행되는 장치의 등록 정보를 정의하는 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class DeviceInfo {
	/** 장치 식별자 */
	public String id;
	/** 장치  타입 */
	public DeviceType type;
	/** 장치 소유자. 별도의 소유자가 지정되지 않은 경우는 <code>null</code>. */
	public String owner;
	/** 장치 설명자 */
	public String description;
	/** 장치  장소. 별도의 위치가 부여되지 않는 경우는 <code>null</code>. */
	public String place;
	/** 장치의 접속 여부. */
	public boolean connected;
	
	public DeviceInfo() { }
	
	@Override
	public String toString() {
		String ownerStr = (this.owner != null) ? owner : "none";
		String placeStr = (this.place != null) ? place : "n/a";
		
		return getClass().getSimpleName() + "[id=" + this.id + ",type=" + this.type
				+ ",owner=" + ownerStr + ",place=" + placeStr
				+ ",connected=" + connected + "]";
	}
}
