package camus.device;

import java.util.Collection;



/**
 * <code>ServiceDirectory</code>는 CAMUS 서비스를 관리하는 서비스 디렉토리 인터페이스를 정의한다.
 * <p>
 * 일반적으로 <code>ServiceDirectory</code>를 직접 사용하기는 경우 보다는 
 * <code>ServiceDirectory</code>를 상속받은 {@link Device} 또는 {@link DeviceNode}
 * 인터페이스를 통해 사용한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface ServiceDirectory {
	/**
	 * 주어진 식별자에 해당하는 {@link ServiceEntry}를 반환한다.
	 * 
	 * @param serviceId	서비스 식별자.
	 * @return	{@link ServiceEntry} 객체.
	 * @throws ServiceNotFoundException	식별자에 해당하는 {@link ServiceEntry}가 없는 경우.
	 * @throws InvalidArgumentException	서비스 식별자가 <code>null</code>인 경우.
	 */
	public ServiceEntry getServiceEntry(String serviceId) throws ServiceNotFoundException;
	
	/**
	 * {@literal ServiceDirectory}에 속한 모든 서비스의 {@link ServiceEntry}를 모두 반환한다.
	 * 
	 * @return	{@link ServiceEntry} 배열.
	 */
	public Collection<ServiceEntry> getServiceEntries();
    
    /**
     * {@literal ServiceDirectory}에 속한 서비스 들 중에서 주어진 서비스 타입을 제공하는 것을 모두 반환한다.
     * <p>
     * 반환되는 서비스는 주어진 서비스 타입 뿐만 아니라 주어지 타입을 상속받은 타입의
     * 서비스들도 포함된다.
     * 
     * @param typeId	검색 대상 서비스의 타입.
     * @return			검색된 서비스 등록 정보 배열.
	 * @throws InvalidArgumentException	서비스 타입이 <code>null</code>인 경우.
     */
	public Collection<ServiceEntry> getServiceEntriesOfType(String typeId);
}
