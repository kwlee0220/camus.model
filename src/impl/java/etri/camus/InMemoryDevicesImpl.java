package etri.camus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.GuardedBy;

import planet.DisconnectionHandler;
import planet.PlanetUtils;
import planet.transport.Connection;

import camus.device.Device;
import camus.device.DeviceAttached;
import camus.device.DeviceDetached;
import camus.device.DeviceExistsException;
import camus.device.DeviceInfo;
import camus.device.DeviceLocationChanged;
import camus.device.DeviceNotFoundException;
import camus.device.DeviceOwnerChanged;
import camus.device.DeviceType;
import camus.device.Devices;
import camus.place.PlaceNotFoundException;
import camus.user.UserInfo;
import camus.user.UserNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import event.EventChannel;
import event.support.EventBuilder;
import event.support.EventChannelImpl;
import utils.Initializable;
import utils.LoggerSettable;
import utils.Utilities;


/**
 * 
 * @author Kang-Woo Lee
 */
public class InMemoryDevicesImpl implements Devices, LoggerSettable, Initializable {
    private static final Logger s_logger = LoggerFactory.getLogger("DEVICES");
	private static final String CLASSNAME = InMemoryDevicesImpl.class.getSimpleName();
	private static final String EM_PREFIX = "failed: " + CLASSNAME + "#";

	private volatile InMemoryUsersImpl m_users;
	private volatile InMemoryPlacesImpl m_places;
	private volatile EventChannel m_channel;
	
	final ReentrantLock m_lock = new ReentrantLock();
	@GuardedBy("m_lock") final Map<String,DeviceInfo> m_infos;
	@GuardedBy("m_lock") final Map<String,DeviceHandler> m_handlers;
    private Logger m_logger = s_logger;

    public InMemoryDevicesImpl() {
    	m_infos = new HashMap<String,DeviceInfo>();
		m_handlers = new HashMap<String,DeviceHandler>();
    }

	@Override
	public void initialize() throws Exception {
		m_channel = EventChannelImpl.singleWorker();

		Logger orgLogger = Utilities.getAndAppendLoggerName(this, ".INIT");
		try {
			DeviceInfo gsdInfo = new DeviceInfo();
			gsdInfo.id = "global";
			gsdInfo.type = DeviceType.GLOBAL_SERVICE_DIRECTORY;
			gsdInfo.owner = "root";
			gsdInfo.place = null;
			gsdInfo.description = "Global Service Device";
			
			m_infos.put(gsdInfo.id, gsdInfo);
		}
		finally {
			Utilities.setLogger(this, orgLogger);
		}
		
	    s_logger.info("initialized: {}...", getClass().getSimpleName());
	}
	@Override public void destroy() throws Exception { }
    
    public final void setUsers(InMemoryUsersImpl users) {
    	m_users = users;
    }
    
    public final void setPlaces(InMemoryPlacesImpl places) {
    	m_places = places;
    }

	@Override
	public DeviceInfo getDeviceInfo(String deviceId) throws DeviceNotFoundException {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getDeviceInfo(deviceId): "
												+ "deviceId is null");
		}
		
		m_lock.lock();
		try {
			DeviceInfo info = m_infos.get(deviceId);
			if ( info == null ) {
				throw new DeviceNotFoundException("deviceId=" + deviceId);
			}
			
			return info;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<String> getDeviceInfoIds() {
		m_lock.lock();
		try {
			return m_infos.keySet();
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<DeviceInfo> getDeviceInfos() {
		m_lock.lock();
		try {
			return m_infos.values();
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<String> getDeviceIdsOfOwner(String ownerId) throws UserNotFoundException {
		if ( ownerId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getDeviceIdsOfOwner(ownerId): "
												+ "ownerId is null");
		}
		
		List<String> idList = new ArrayList<String>();
		
		m_lock.lock();
		try {
			for ( DeviceInfo info: m_infos.values() ) {
				if ( ownerId.equals(info.owner) ) {
					idList.add(info.id);
				}
			}
			
			return idList;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<String> getDeviceIdsAtPlace(String placeId, boolean cover)
		throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getDeviceIdsAtPlace(placeId,cover): "
												+ "placeId is null");
		}
		
		m_places.lock();
		m_lock.lock();
		try {
			return getDeviceIdsAtPlaceInGuard(placeId, cover,
										EM_PREFIX + "getDeviceIdsAtPlace(placeId,cover): ");
		}
		finally {
			m_lock.unlock();
			m_places.unlock();
		}
	}
	
	@Override
	public Collection<String> getDeviceIdsAtOwnerPlace(String ownerId, boolean cover)
		throws UserNotFoundException {
		if ( ownerId == null ) {
			throw new IllegalArgumentException(EM_PREFIX
								+ "getDeviceIdsAtOwnerPlace(ownerId,cover): ownerId is null");
		}

		m_users.lock();
		m_places.lock();
		m_lock.lock();
		try {
			UserInfo userInfo = m_users.getUserInfoInGuard(ownerId, EM_PREFIX
												+ "getDeviceIdsAtOwnerPlace(ownerId,cover): ownerId");
			
			return getDeviceIdsAtPlaceInGuard(userInfo.place, cover,
										EM_PREFIX + "getDeviceIdsAtOwnerPlace(ownerId,cover): ");
		}
		catch ( PlaceNotFoundException neverHappens ) {
			throw new RuntimeException(EM_PREFIX + "getDeviceIdsAtOwnerPlace(ownerId,cover)");
		}
		finally {
			m_lock.unlock();
			m_places.unlock();
			m_users.unlock();
		}
	}

	@Override
	public Collection<DeviceInfo> findDeviceInfos(String query) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addDeviceInfo(DeviceInfo info)
		throws DeviceExistsException, UserNotFoundException, PlaceNotFoundException {
		if ( info == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "addDeviceInfo(info): "
												+ "info is null");
		}
		if ( info.id == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "addDeviceInfo(info): "
												+ "info.id is null");
		}

		m_lock.lock();
		try {
			if ( info.owner != null ) {
				m_users.getUserInfoInGuard(info.owner, EM_PREFIX + "addDeviceInfo(info): info.owner=");
			}
			if ( info.place != null ) {
				m_places.getPlaceInfoInGuard(info.place,
											EM_PREFIX + "addDeviceInfo(info): info.location=");
			}
			
			DeviceInfo prev = m_infos.put(info.id, info);
			if ( prev != null ) {
				m_infos.put(info.id, prev);

				throw new DeviceExistsException(EM_PREFIX + "addDeviceInfo(info): "
													+ "info.id=" + info.id);
			}
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public boolean removeDeviceInfo(String deviceId) {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "removeDeviceInfo(deviceId): "
												+ "deviceId is null");
		}

		m_lock.lock();
		try {
			DeviceInfo info = m_infos.remove(deviceId);
			if ( info == null ) {
				return false;
			}
			
			if ( s_logger.isInfoEnabled() ) {
				s_logger.info("removed:Device: id=" + deviceId);
			}
			
			return true;
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public void setDeviceOwner(String deviceId, String ownerId)
		throws DeviceNotFoundException, UserNotFoundException {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "setDeviceOwner(deviceId,ownerId): "
												+ "deviceId is null");
		}

		m_users.lock();
		m_lock.lock();
		try {
			DeviceInfo info = m_infos.get(deviceId);
			if ( info == null ) {
				throw new DeviceNotFoundException(EM_PREFIX + "setDeviceOwner(deviceId,ownerId): "
													+ "deviceId=" + deviceId);
			}
			
			String prevOwnerId = info.owner;
			
			if ( ownerId != null ) {
				m_users.getUserInfoInGuard(ownerId, EM_PREFIX
											+ "setDeviceOwner(deviceId,ownerId): ownerId=");
			}
			info.owner = ownerId;
			
			EventBuilder builder = new EventBuilder(DeviceOwnerChanged.class);
			builder.setProperty(DeviceOwnerChanged.PROP_DEVICE_ID, deviceId);
			builder.setProperty(DeviceOwnerChanged.PROP_OLD_OWNER, prevOwnerId);
			builder.setProperty(DeviceOwnerChanged.PROP_NEW_OWNER, ownerId);
			m_channel.publishEvent(builder.build());
		}
		finally {
			m_lock.unlock();
			m_users.unlock();
		}
	}

	@Override
	public void setDeviceLocation(String deviceId, String placeId)
		throws DeviceNotFoundException, PlaceNotFoundException {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "setDeviceLocation(deviceId,placeId): "
												+ "deviceId is null");
		}

		m_places.lock();
		m_lock.lock();
		try {
			DeviceInfo info = m_infos.get(deviceId);
			if ( info == null ) {
				throw new DeviceNotFoundException(EM_PREFIX
									+ "setDeviceLocation(deviceId,ownerId): deviceId=" + deviceId);
			}
			
			String prevLoc = info.place;
			
			if ( placeId != null ) {
				m_places.getPlaceInfoInGuard(placeId, EM_PREFIX
											+ "setDeviceLocation(deviceId,ownerId): placeId=");
			}
			info.place = placeId;
			
			EventBuilder builder = new EventBuilder(DeviceLocationChanged.class);
			builder.setProperty(DeviceLocationChanged.PROP_DEVICE_ID, deviceId);
			builder.setProperty(DeviceLocationChanged.PROP_FROM_PLACE, prevLoc);
			builder.setProperty(DeviceLocationChanged.PROP_TO_PLACE, placeId);
			m_channel.publishEvent(builder.build());
		}
		finally {
			m_lock.unlock();
			m_places.unlock();
		}
	}

	@Override
	public Device getDevice(String deviceId) throws DeviceNotFoundException {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getDevice(deviceId): "
												+ "deviceId is null");
		}

		m_lock.lock();
		try {
			DeviceHandler handler = m_handlers.get(deviceId);
			if ( handler != null ) {
				return handler.m_device;
			}
			
			throw new DeviceNotFoundException(EM_PREFIX
										+ "getDevice(deviceId): deviceId=" + deviceId);
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Device getDeviceIfConnected(String deviceId) {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getDevice(deviceId): "
												+ "deviceId is null");
		}

		m_lock.lock();
		try {
			DeviceHandler handler = m_handlers.get(deviceId);
			if ( handler != null ) {
				return handler.m_device;
			}
			else {
				return null;
			}
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<String> getConnectedDeviceIds() {
		m_lock.lock();
		try {
			Collection<DeviceHandler> descColl = m_handlers.values();
			
			List<String> idList = new ArrayList<String>();
			for ( DeviceHandler desc: descColl ) {
				if ( desc.m_device != null ) {
					idList.add(desc.m_deviceId);
				}
			}
			
			return idList;
		}
		finally {
			m_lock.unlock();
		}
	}
	
	@Override
	public Collection<Device> getDevicesOfOwner(String ownerId) throws UserNotFoundException {
		if ( ownerId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getDevicesOfOwner(ownerId): "
												+ "ownerId is null");
		}
		
		m_lock.lock();
		try {
			return getDevicesInGuard(getDeviceIdsOfOwner(ownerId));
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<Device> getDevicesAtPlace(String placeId, boolean cover)
		throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "getDevicesAtPlace(placeId,cover): "
												+ "placeId is null");
		}
		
		m_lock.lock();
		try {
			return getDevicesInGuard(getDeviceIdsAtPlace(placeId, cover));
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public Collection<Device> getDevicesAtOwnerPlace(String ownerId, boolean cover)
			throws UserNotFoundException {
		if ( ownerId == null ) {
			throw new IllegalArgumentException(EM_PREFIX
									+ "getDevicesAtOwnerPlace(ownerId,cover): userId is null");
		}
		
		m_lock.lock();
		try {
			return getDevicesInGuard(getDeviceIdsAtOwnerPlace(ownerId, cover));
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public void onDeviceConnected(String deviceId, Device device)
		throws DeviceExistsException, DeviceNotFoundException {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "onDeviceConnected(deviceId,device): "
												+ "deviceId is null");
		}
		if ( device == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "onDeviceConnected(deviceId,device): "
												+ "device is null");
		}
		
		m_lock.lock();
		try {
			DeviceInfo info = m_infos.get(deviceId);
			if ( info == null ) {
				throw new DeviceNotFoundException(EM_PREFIX
									+ "onDeviceConnected(deviceId,device): unregistered device at Devices, deviceId=" + deviceId);
			}
			info.connected = true;
			
			DeviceHandler handler = new DeviceHandler(deviceId, device);
			m_handlers.put(deviceId, handler);
			PlanetUtils.addDisconnectionHandler(device, handler);
			
			EventBuilder builder = new EventBuilder(DeviceAttached.class);
			builder.setProperty(DeviceAttached.PROP_DEVICE_ID, deviceId);
			builder.setProperty(DeviceAttached.PROP_INFO, info);
			m_channel.publishEvent(builder.build());
			
			if ( s_logger.isInfoEnabled() ) {
				s_logger.info("connected: device=" + deviceId);
			}
		}
		finally {
			m_lock.unlock();
		}
	}

	@Override
	public void onDeviceDisconnected(String deviceId) {
		if ( deviceId == null ) {
			throw new IllegalArgumentException(EM_PREFIX + "onDeviceDisconnected(deviceId): "
												+ "deviceId is null");
		}

		m_lock.lock();
		try {
			m_lock.lock();
			try {
				DeviceHandler handler = m_handlers.remove(deviceId);
				if ( handler != null ) {
					PlanetUtils.removeDisconnectionHandler(handler.m_device, handler);
				}
			}
			finally {
				m_lock.unlock();
			}

			DeviceInfo info = m_infos.get(deviceId);
			if ( info == null ) {
				throw new DeviceNotFoundException(EM_PREFIX
									+ "onDeviceDisconnected(deviceId,device): deviceId=" + deviceId);
			}
			info.connected = false;
			
			EventBuilder builder = new EventBuilder(DeviceDetached.class);
			builder.setProperty(DeviceDetached.PROP_DEVICE_ID, deviceId);
			builder.setProperty(DeviceDetached.PROP_INFO, info);
			m_channel.publishEvent(builder.build());
			
			if ( s_logger.isInfoEnabled() ) {
				s_logger.info("disconnected: device=" + deviceId);
			}
		}
		catch ( DeviceNotFoundException ignored ) { }
		finally {
			m_lock.unlock();
		}
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

	Collection<String> getDeviceIdsAtPlaceInGuard(String placeId, boolean cover, String errMsgPrefix)
		throws PlaceNotFoundException {
		if ( placeId == null ) {
			throw new IllegalArgumentException(errMsgPrefix + "placeId is null");
		}
		
		m_places.getPlaceInfoInGuard(placeId, errMsgPrefix);
		
		Set<String> plcIds = new HashSet<String>();
		plcIds.add(placeId);
		if ( cover ) {
			plcIds.addAll(m_places.getSubPlaceIdsInGuard(placeId, true));
		}
		
		List<String> deviceIdList = new ArrayList<String>();
		for ( DeviceInfo info: m_infos.values() ) {
			if ( plcIds.contains(info.place) ) {
				deviceIdList.add(info.id);
			}
		}
		
		return deviceIdList;
	}
	
	private Collection<Device> getDevicesInGuard(Collection<String> deviceIds) {
		List<Device> deviceList = new ArrayList<Device>();
		for ( String deviceId: deviceIds ) {
			DeviceHandler handler = m_handlers.get(deviceId);
			if ( handler != null ) {
				deviceList.add(handler.m_device);
			}
		}
		
		return deviceList;
	}
	
	class DeviceHandler implements DisconnectionHandler {
		final String m_deviceId;
		final Device m_device;
		
		DeviceHandler(String deviceId, Device device) {
			m_deviceId = deviceId;
			m_device = device;
		}

		@Override
		public void onDisconnected(Connection conn) {
			try {
				onDeviceDisconnected(m_deviceId);
			}
			catch ( Exception ignored ) { }
		}
	}
}