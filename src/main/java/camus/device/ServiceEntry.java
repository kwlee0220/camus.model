package camus.device;

/**
 * <code>ServiceEntry</code>는 {@link ServiceDirectory}에 등록되어 수행되는 서비스의
 * 정보를 정의하는 클래스이다.
 *
 * <p><code>ServiceEntry</code>는 다음과 같은 항목으로 정의된다.
 * <ul>
 * 	<li> 서비스 식별자: 서비스 엔트리의 식별자이다.
 * 	<li> 서비스 이름: 서비스에 부여된 이름. 이름은 <code>ServiceDirectory</code>내에서 중복될 수 있다.
 * 	<li> 서비스 타입: 서비스가 지원하는 대표 서비스 타입의 식별자.
 * 	<li> 서비스 참조: 서비스 객체를 가리키는 참조.
 * </ul>
 *
 * @author Kang-Woo Lee (ETRI)
 */
public final class ServiceEntry {
	/** 서비스 식별자 */
	public String id;
	/** 서비스 이름 */
	public String name;
	/** 서비스 타입 식별자 */
	public String typeId;
	/** 서비스 참조 */
	public String uri;

	public ServiceEntry() { }

	public String toString() {
		return String.format("%s[id=%s,name=%s,type=%s,remote=%s]",
							getClass().getSimpleName(), id, name, typeId, uri);
	}
}
