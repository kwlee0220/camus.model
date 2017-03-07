package etri.camus;

import javax.annotation.Nonnull;

import camus.CamusRegistry;
import camus.device.Devices;
import camus.place.Places;
import camus.user.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.Initializable;
import utils.LoggerSettable;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class InMemoryCamusRegistryImpl implements CamusRegistry, LoggerSettable, Initializable {
    private static final Logger s_logger = LoggerFactory.getLogger("CAMUS");
    
	private final InMemoryPlacesImpl m_places;
	private final InMemoryUsersImpl m_users;
	private final InMemoryDevicesImpl m_devices;
    @Nonnull private Logger m_logger = s_logger;
    
    public InMemoryCamusRegistryImpl() {
		m_places = new InMemoryPlacesImpl();
		m_users = new InMemoryUsersImpl();
		m_devices = new InMemoryDevicesImpl();
    }

	@Override
	public void initialize() throws Exception {
		m_places.initialize();
		m_users.initialize();
		m_places.setUsers(m_users);
		m_users.setPlaces(m_places);
		
		m_devices.initialize();
		m_devices.setPlaces(m_places);
		m_devices.setUsers(m_users);
	}

	@Override
	public void destroy() throws Exception {
		m_devices.destroyQuietly();
		m_users.destroyQuietly();
		m_places.destroyQuietly();
	}

	@Override
	public Users getUsers() {
		return m_users;
	}

	@Override
	public Places getPlaces() {
		return m_places;
	}

	@Override
	public Devices getDevices() {
		return m_devices;
	}
	
	@Override
	public Logger getLogger() {
		return m_logger;
	}

	@Override
    public void setLogger(Logger logger) {
    	m_logger = logger != null ? logger : s_logger;
    }
}
