package etri.camus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import camus.place.PlaceEvent;
import camus.place.PlaceExistsException;
import camus.place.PlaceInfo;
import camus.place.PlaceNotFoundException;
import camus.place.Places;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import event.Event;
import event.EventChannel;
import event.EventSubscriber;
import event.support.EventChannelImpl;
import net.jcip.annotations.GuardedBy;
import utils.CSV;
import utils.Initializable;
import utils.LoggerSettable;
import utils.Utilities;


/**
 * 
 * @author Kang-Woo Lee
 */
public class InMemoryPlacesImpl implements Places, LoggerSettable, Initializable {
    private static final Logger s_logger = LoggerFactory.getLogger("PLACES");
	private static final String CLASSNAME = InMemoryPlacesImpl.class.getSimpleName();
	private static final String EM_PREFIX = "failed: " + CLASSNAME + "#";
	
	private InMemoryUsersImpl m_users;
	private volatile EventChannel m_channel;

	private final ReentrantLock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") private final Map<String,PlaceInfo> m_infos = Maps.newHashMap();
    private Logger m_logger = s_logger;

    public InMemoryPlacesImpl() {
    }

	@Override
	public void initialize() throws Exception {
		m_channel = EventChannelImpl.singleWorker(); 

    	Logger orgLogger = Utilities.getAndAppendLoggerName(this, ".INIT");
		m_lock.lock();
		try {
			PlaceInfo rootInfo = new PlaceInfo(ROOT_PLACE_ID, "top place");
			m_infos.put(ROOT_PLACE_ID, rootInfo);
		}
		finally {
			m_lock.unlock();
			Utilities.setLogger(this, orgLogger);
		}
		
	    s_logger.info("initialized: {}...",  getClass().getSimpleName());
	}

	@Override public void destroy() throws Exception { }
    
    public final void setUsers(InMemoryUsersImpl users) {
    	m_users = users;
    }
    
	@Override
    public Collection<String> getPlaceInfoIds() {
		m_lock.lock();
		try {
			return m_infos.keySet();
		}
		finally {
			m_lock.unlock();
		}
    }
    
	@Override
    public Collection<PlaceInfo> getPlaceInfos() {
		m_lock.lock();
		try {
			return m_infos.values();
		}
		finally {
			m_lock.unlock();
		}
    }
    
	@Override
    public PlaceInfo getPlaceInfo(String placeId) throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getPlaceInfo(placeId): "
												+ "placeId is null");
		}
		
		m_lock.lock();
		try {
			PlaceInfo info = m_infos.get(placeId);
			if ( info == null ) {
				throw new PlaceNotFoundException(EM_PREFIX + "getPlaceInfo(placeId): "
													+ "placeId=" + placeId);
			}
			
			return info;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public boolean existsPlaceInfo(String placeId) {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "existsPlaceInfo(placeId): "
												+ "placeId is null");
		}
		
		m_lock.lock();
		try {
			return m_infos.containsKey(placeId);
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public boolean isSubPlaceOf(String subPlaceId, String placeId, boolean cover) {
		if ( subPlaceId == null ) {
			return false;
		}
		if ( placeId == null ) {
			return false;
		}
		if ( subPlaceId.equals(placeId) ) {
			return false;
		}
		
		m_lock.lock();
		try {
			PlaceInfo subPlace = m_infos.get(subPlaceId);
			if ( subPlace == null ) {
				return false;
			}
			PlaceInfo place = m_infos.get(placeId);
			if ( place == null ) {
				return false;
			}
			
			int generation = Places.getGeneration(placeId, subPlaceId);
			return ( generation == 1 || (cover && generation > 1 ) );
		}
		finally {
			m_lock.unlock();
		}
	}
    
	@Override
    public Collection<String> getSubPlaceIds(String placeId, boolean cover)
		throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getSubPlaceIds(placeId, cover): "
												+ "placeId is null");
		}
		
		List<String> placeIdList = new ArrayList<String>();
		m_lock.lock();
		try {
			PlaceInfo place = m_infos.get(placeId);
			if ( place == null ) {
				throw new PlaceNotFoundException(EM_PREFIX + "getSubPlaceIds(placeId, cover): "
												+ "placeId=" + placeId);
			}

			int depth = CSV.parse(placeId, '/', '\\').size();
			for ( String id : m_infos.keySet() ) {
				if ( !id.equals(placeId) && id.startsWith(placeId) ) {
					if ( cover || CSV.parse(placeId, '/', '\\').size() == depth+1 ) {
						placeIdList.add(id);
					}
				}
			}
			
			return placeIdList;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<PlaceInfo> findPlaceInfos(String query) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isLeafPlaceInfo(String placeId) {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "isLeafPlaceInfo(placeId): "
												+ "placeId is null");
		}

		m_lock.lock();
		try {
			int idLength = placeId.length();
			for ( String id : m_infos.keySet() ) {
				if ( id.length() > idLength && id.startsWith(placeId) ) {
					return false;
				}
			}
			
			return true;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public void addPlaceInfo(PlaceInfo info) throws PlaceExistsException, PlaceNotFoundException {
		if ( info == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "addPlaceInfo(info): "
											+ "info is null");
		}
		if ( info.id == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "addPlaceInfo(info): "
											+ "info.id is null");
		}
		
		String superPlcId = Places.getDirectSuperPlaceId(info.id);

		m_lock.lock();
		try {
			PlaceInfo place = m_infos.get(info.id);
			if ( place != null ) {
				throw new PlaceExistsException(EM_PREFIX + "addPlaceInfo(info): "
												+ "info.id=" + info.id);
			}
			PlaceInfo parent = m_infos.get(superPlcId);
			if ( parent == null ) {
				throw new PlaceNotFoundException(EM_PREFIX + "addPlaceInfo(info): "
												+ "new super placeId=" + superPlcId);
			}
			
			m_infos.put(info.id, new PlaceInfo(info.id, info.name));
			
			if ( m_logger.isInfoEnabled() ) {
				m_logger.info("added:Place: id=" + info.id + "]");
			}
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public boolean removePlaceInfo(String placeId) throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "removePlaceInfo(placeId): "
												+ "placeId is null");
		}
        else if ( placeId.equals(ROOT_PLACE_ID) ) {
			throw new IllegalArgumentException(EM_PREFIX + "removePlaceInfo(placeId): "
												+ "Cannot delete root location");
        }

		m_lock.lock();
		try {
			PlaceInfo place = m_infos.get(placeId);
			if ( place == null ) {
				return false;
			}
			
			String parentPlcId = Places.getDirectSuperPlaceId(placeId);
			
			removePlaceInGuard(placeId, parentPlcId);
			for ( String subPlcId: getSubPlaceIds(placeId, true) ) {
				removePlaceInGuard(subPlcId, parentPlcId);
			}
			
			if ( m_logger.isInfoEnabled() ) {
				m_logger.info("removed:Place: id=" + placeId + "]");
			}
			
			return true;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public void updatePlaceInfo(PlaceInfo info) throws PlaceNotFoundException {
		if ( info == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "updatePlaceInfo(info): "
											+ "info is null");
		}
		if ( info.id == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "updatePlaceInfo(info): "
											+ "info.id is null");
		}
		
		m_lock.lock();
		try {
			PlaceInfo place = m_infos.get(info.id);
			if ( place == null ) {
				throw new PlaceNotFoundException(EM_PREFIX + "updatePlaceInfo(info): "
												+ "info.id=" + info.id);
			}
			
			place.name = info.name;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public void publishEvent(Event event) {
		if ( !(event instanceof PlaceEvent) ) {
			throw new IllegalArgumentException(EM_PREFIX + "publishEvent(event): "
												+ "the event is not a PlaceEvent");
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
    
	PlaceInfo getPlaceInfoInGuard(String placeId, String errMsgPrefix) throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(errMsgPrefix + "null");
		}
		
		PlaceInfo info = m_infos.get(placeId);
		if ( info == null ) {
			throw new PlaceNotFoundException(errMsgPrefix + placeId);
		}
		
		return info;
	}
    
	List<String> getSubPlaceIdsInGuard(String placeId, boolean cover) {
		List<String> placeIdList = new ArrayList<String>();
		for ( String id : m_infos.keySet() ) {
			int generation = Places.getGeneration(placeId, id);
			if ( generation == 1 || (cover && generation > 1) ) {
				placeIdList.add(id);
			}
		}
		
		return placeIdList;
	}

	private void removePlaceInGuard(String placeId, String newPlaceId) {
		if ( m_infos.remove(placeId) != null ) {
        	for ( String userId: m_users.getUserIdAllAtPlaceInGuard(placeId) ) {
        		m_users.setUserLocationInGuard(userId, newPlaceId);
        	}
		}
		
		if ( m_logger.isDebugEnabled() ) {
			m_logger.debug("removed:Place: id=" + placeId + "]");
		}
	}
}