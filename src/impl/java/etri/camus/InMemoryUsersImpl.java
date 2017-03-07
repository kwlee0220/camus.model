package etri.camus;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import camus.place.PlaceNotFoundException;
import camus.place.PlaceUtils;
import camus.place.Places;
import camus.user.UserCreateInfo;
import camus.user.UserEntered;
import camus.user.UserEvent;
import camus.user.UserExistsException;
import camus.user.UserInfo;
import camus.user.UserLeft;
import camus.user.UserLocationChanged;
import camus.user.UserNotFoundException;
import camus.user.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import event.Event;
import event.EventChannel;
import event.EventSubscriber;
import event.support.EventChannelImpl;
import net.jcip.annotations.GuardedBy;
import utils.Initializable;
import utils.LoggerSettable;
import utils.Utilities;


/**
 * 
 * @author Kang-Woo Lee
 */
public class InMemoryUsersImpl implements Users, LoggerSettable, Initializable {
    private static final Logger s_logger = LoggerFactory.getLogger("USERS");
	private static final String CLASSNAME = InMemoryUsersImpl.class.getSimpleName();
	private static final String EM_PREFIX = "failed: " + CLASSNAME + "#";
	
	private volatile InMemoryPlacesImpl m_places;
	private volatile EventChannel m_channel;

	private final ReentrantLock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") private final Map<String,UserInfo> m_infos = Maps.newHashMap();
    private Logger m_logger = s_logger;
    
    public InMemoryUsersImpl() {
    }

	@Override
	public void initialize() throws Exception {
		m_channel = EventChannelImpl.singleWorker(); 
		
    	Logger orgLogger = Utilities.getAndAppendLoggerName(this, ".INIT");
		try {
	    	// root 사용자를 등록시킨다.
			m_infos.put(ROOT_ID, new UserInfo(ROOT_ID, "Administrator", Places.ROOT_PLACE_ID));
		}
		finally {
			Utilities.setLogger(this, orgLogger);
		}
		
		if ( s_logger.isInfoEnabled() ) {
	    	s_logger.info("initialized: " + getClass().getSimpleName() + "...");
		}
	}
	@Override public void destroy() throws Exception { }
	
	void setPlaces(InMemoryPlacesImpl places) {
		m_places = places;
	}

	@Override
	public UserInfo getUserInfo(String userId) throws UserNotFoundException {
    	if ( userId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getUserInfo(userId): "
												+ "userId is null");
    	}
		
		m_lock.lock();
		try {
			return getUserInfoInGuard(userId, EM_PREFIX + "getUserInfo(userId): userId=");
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public boolean existsUserInfo(String userId) {
		if ( userId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "existsUserInfo(userId): "
												+ "userId is null");
		}
		
		m_lock.lock();
		try {
			return m_infos.containsKey(userId);
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
    public Collection<String> getUserInfoIds() {
		m_lock.lock();
		try {
			return m_infos.keySet();
		}
		finally {
			m_lock.unlock();
		}
    }

	@Override
    public Collection<UserInfo> getUserInfos() {
		m_lock.lock();
		try {
			return m_infos.values();
		}
		finally {
			m_lock.unlock();
		}
    }

	@Override
	public Collection<String> getUserIdsAtPlace(String placeId, boolean cover)
		throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getUserIdsAtPlace(placeId,cover): "
												+ "placeId is null");
		}

		m_lock.lock();
		m_places.lock();
		try {
			m_places.getPlaceInfoInGuard(placeId, EM_PREFIX
										+ "getUserIdsAtPlace(placeId, cover): placeId=");
			
			return m_infos.values()
							.stream()
							.filter(userInfo -> isResident(userInfo, placeId, cover))
							.map(userInfo -> userInfo.id)
							.collect(Collectors.toList());
		}
		finally {
			m_places.unlock();
			m_lock.unlock();
		}
	}

	@Override
	public Collection<UserInfo> findUserInfos(String query) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addUserInfo(UserCreateInfo cinfo) throws UserExistsException {
    	if ( cinfo == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "addUserInfo(UserCreateInfo): "
												+ "UserCreateInfo is null");
    	}
    	if ( cinfo.id == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "addUserInfo(UserCreateInfo): "
												+ "UserCreateInfo.id is null");
    	}

		m_lock.lock();
		try {
			UserInfo info = new UserInfo(cinfo.id, cinfo.name, Places.ROOT_PLACE_ID);
			UserInfo prev = m_infos.put(info.id, info);
			if ( prev != null ) {
				m_infos.put(info.id, prev);
				
				throw new UserExistsException(EM_PREFIX + "addUserInfo(UserCreateInfo): "
												+ "UserCreateInfo.id=" + cinfo.id);
			}
			
//			m_channels.put(info.id, new MultiWorkerEventChannel(m_executor));
			
			if ( getLogger().isInfoEnabled() ) {
				getLogger().info("added:User: info=" + cinfo);
			}
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public boolean removeUserInfo(String userId) {
		if ( userId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "removeUserInfo(userId): "
												+ "userId is null");
		}
    	else if ( userId.equals(ROOT_ID) ) {
    		// 루트 사용자는 제거할 수 없다
    		return false;
    	}

		m_lock.lock();
		try {
			UserInfo info = m_infos.remove(userId);
			if ( info == null ) {
				return false;
			}

//			m_channels.remove(userId);
			
			if ( getLogger().isDebugEnabled() ) {
				getLogger().debug("removed:User: id=" + userId);
			}
			
			return true;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public void setUserLocation(String userId, String placeId)
		throws UserNotFoundException, PlaceNotFoundException {
    	if ( userId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "setUserLocation(userId,placeId): "
												+ "userId is null");
    	}
    	if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "setUserLocation(userId,placeId): "
												+ "placeId is null");
    	}
    	
    	m_lock.lock();
    	m_places.lock();
    	try {
    		getUserInfoInGuard(userId, EM_PREFIX + "setUserLocation(userId,placeId): "
												+ "userId=");
    		if ( !m_places.existsPlaceInfo(placeId) ) {
				throw new PlaceNotFoundException(EM_PREFIX + "setUserLocation(userId,placeId): "
												+ "placeId=" + placeId);
    		}
    		
    		setUserLocationInGuard(userId, placeId);
			
			if ( getLogger().isDebugEnabled() ) {
				getLogger().debug("changed:UserLocation: id=" + userId + ", loc.id=" + placeId);
			}
		}
		finally {
			m_places.unlock();
			m_lock.unlock();
		}
	}

	@Override
	public void setUserLeftLocation(String userId, String placeId)
		throws UserNotFoundException, PlaceNotFoundException {
    	if ( userId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "setUserLeftLocation(userId,placeId): "
												+ "userId is null");
    	}
    	if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "setUserLeftLocation(userId,placeId): "
												+ "placeId is null");
    	}
    	
    	m_lock.lock();
    	m_places.lock();
    	try {
    		getUserInfoInGuard(userId, EM_PREFIX + "setUserLeftLocation(userId,placeId): "
												+ "userId=");
    		if ( !m_places.existsPlaceInfo(placeId) ) {
				throw new PlaceNotFoundException(EM_PREFIX + "setUserLeftLocation(userId,placeId): "
												+ "placeId=" + placeId);
    		}
    		
    		String newPlaceId = setUserLeftPlaceInGuard(userId, placeId);
			if ( getLogger().isDebugEnabled() ) {
				getLogger().debug("changed:UserLocation: id=" + userId + ", loc.id=" + newPlaceId);
			}
		}
		finally {
			m_places.unlock();
			m_lock.unlock();
		}
	}

	@Override
	public void publishEvent(Event event) {
		if ( !(event instanceof UserEvent) ) {
			throw new IllegalArgumentException(EM_PREFIX + "publishEvent(event): "
												+ "the event is not a UserEvent");
		}
		
		m_channel.publishEvent(event);
	}

	@Override
	public String subscribe(EventSubscriber subscriber) {
        if ( subscriber == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "subscribe(subscriber): "
												+ "subscriber is null");
        }
        
		return m_channel.subscribe(subscriber);
	}

	@Override
	public String subscribe(String filter, EventSubscriber subscriber) {
        if ( subscriber == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "subscribe(filter, subscriber): "
												+ "subscriber is null");
        }
        
		return m_channel.subscribe(filter, subscriber);
	}

	@Override
	public boolean unsubscribe(String subscriberId) {
        if ( subscriberId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "unsubscribe(subscriber): "
												+ "subscriber is null");
        }
        
		return m_channel.unsubscribe(subscriberId);
	}
	
	@Override
	public Logger getLogger() {
		return m_logger;
	}

	@Override
    public void setLogger(Logger logger) {
    	m_logger = logger;
    }

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
	void lock() {
		m_lock.lock();
	}
	
	void unlock() {
		m_lock.unlock();
	}

	UserInfo getUserInfoInGuard(String userId, String errMsgPrefix) throws UserNotFoundException {
		UserInfo info = m_infos.get(userId);
		if ( info == null ) {
			throw new UserNotFoundException(errMsgPrefix + userId);
		}
		
		return info;
	}
	
	String setUserLeftPlaceInGuard(String userId, String placeId) {
		UserInfo info = m_infos.get(userId);
		
		if ( Places.ROOT_PLACE_ID.equals(placeId) ) {
			throw new IllegalArgumentException("Cannot leave root place");
		}
		if ( !info.place.startsWith(placeId) ) {
			if ( getLogger().isDebugEnabled() ) {
				getLogger().debug("ignore invalid place: current=" + info.place
									+ ", new.place=" + placeId);
			}
			return null;
		}
		String prevLoc = info.place;
		
		// 현재 장소에서 최소 공동 조상 장소 바로 전까지 나간다.
		String pid = info.place;
		while ( pid.startsWith(placeId) || pid.equals(placeId) ) {
			UserLeft ul = UserLeft.create(userId, pid);
			if ( getLogger().isDebugEnabled() ) {
				getLogger().debug("publishing event: " + ul);
			}
			m_channel.publishEvent(ul);
			
			pid = Places.getDirectSuperPlaceId(pid);
		}
		
		info.place = pid;
		UserLocationChanged ulc = UserLocationChanged.create(userId, prevLoc, pid);
		if ( getLogger().isInfoEnabled() ) {
			getLogger().info("publishing event: " + ulc);
		}
		m_channel.publishEvent(ulc);
		
		return info.place;
	}

	void setUserLocationInGuard(String userId, String placeId) {
		UserInfo info = m_infos.get(userId);

		// 비정상적인 위치 설정은 필터링한다.
		if ( info.place.equals(placeId) ) {
			getLogger().debug("[ignored] invalid location setting: user({}) left the place({})",
								userId, placeId);
			return;
		}
		
		String prevLoc = info.place;
		String ancestorId = Places.getCommonAncestorPathOf(info.place, placeId);
		
		// 현재 장소에서 최소 공동 조상 장소 바로 전까지 나간다.
		for ( String pid = info.place; !pid.equals(ancestorId);
			pid = Places.getDirectSuperPlaceId(pid) ) {
			
			UserLeft ul = UserLeft.create(userId, pid);
			getLogger().debug("publishing event: {}", ul);
			m_channel.publishEvent(ul);
		}
		
		// 최소 공동 조상 장소에서 새 장소까지 진입한다.
		List<String> downPath = PlaceUtils.getPathDownTo(ancestorId, placeId);
		String pid = ancestorId;
		for ( String name: downPath ) {
			pid = PlaceUtils.formChildPlaceId(pid, name);

			UserEntered ue = UserEntered.create(userId, pid);
			getLogger().debug("publishing event: {}", ue);
			m_channel.publishEvent(ue);
		}
		
		info.place = placeId;
		
		UserLocationChanged ulc = UserLocationChanged.create(userId, prevLoc, placeId);
		getLogger().info("publishing event: {}", ulc);
		m_channel.publishEvent(ulc);
	}
	
	Collection<String> getUserIdAllAtPlaceInGuard(final String placeId) {
		return m_infos.values().parallelStream()
					.filter(info -> info.place.equals(placeId))
					.map(info -> info.id)
					.collect(Collectors.toList());
	}
	
	private boolean isResident(UserInfo user, String placeId, boolean cover) {
		return (cover) ? user.place.startsWith(placeId) : user.place.equals(placeId);
	}
}