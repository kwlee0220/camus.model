package camus.place;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import utils.CSV;


/**
 *
 * @author Kang-Woo Lee
 */
public class PlaceUtils {
    static final char DELIM = '/';
    private static final char ESC = '\\';
    
    private PlaceUtils() {
    	throw new AssertionError("Should not be called: class=" + PlaceUtils.class);
    }
    
    public static String formChildPlaceId(String parentPlaceId, String name) {
    	if ( parentPlaceId == null ) {
    		throw new IllegalArgumentException("placeId was null");
    	}
    	if ( name == null ) {
    		throw new IllegalArgumentException("subplace name was null");
    	}
    	
    	return parentPlaceId.equals(Places.ROOT_PLACE_ID)
		    			? "/" + name
		    			: parentPlaceId + "/" + name;
    }
    
    public static List<String> toNodeList(String path) {
        if ( path.charAt(0) == '/' ) {
            path = path.substring(1);
        }
        
        return CSV.parseCsv(path, DELIM, ESC).toList();
    }
    
    public static String getLastNodeName(String placeId) {
        if ( placeId.equals("/") ) {
            return "/";
        }
        else {
        	return CSV.parseCsv(placeId, DELIM, ESC).last().get();
        }
    }
    
    public static String replacePrefixPlaceId(String placeId, String fromPrefix, String toPrefix) {
    	if ( placeId.startsWith(fromPrefix) ) {
    		String tail = placeId.substring(fromPrefix.length());
    		return toPrefix + tail;
    	}
    	else {
    		throw new IllegalArgumentException("invalid placeid=" + placeId
    											+ ", from_prefix=" + fromPrefix
    											+ ", to_prefix=" + toPrefix);
    	}
    }
    
    public static List<String> getPathDownTo(String fromId, String toId) {
    	if ( !Places.isSuperPlaceOf(fromId, toId) ) {
    		return Collections.emptyList();
    	}
    	
    	String diff = toId.substring(fromId.length());
    	if ( diff.length() == 0 ) {
    		return Collections.emptyList();
    	}
    	
    	if ( diff.charAt(0) == '/' ) {
    		diff = diff.substring(1);
    	}
    	return CSV.parseCsv(diff, DELIM, ESC).toList();
    }
    
    public static String toPath(Collection<String> nodeList) {
        if ( nodeList.size() == 0 ) {
            return "/";
        }
        else {
            return "/" + CSV.get().withDelimiter(DELIM).withEscape(ESC).toString(nodeList);
        }
    }
}