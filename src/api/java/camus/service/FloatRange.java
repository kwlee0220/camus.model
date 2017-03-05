package camus.service;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class FloatRange {
	public float low = Float.MIN_VALUE;
	public float high = Float.MAX_VALUE;
	
	public FloatRange() { }
	public FloatRange(float low, float high) {
		this.low = low;
		this.high = high;
	}
	
	public boolean isIn(float value) {
		return value >= low && value <= high;
	}
	
	public String toString() {
		return String.format("[%d:%d]", low, high);
	}
	
	public static FloatRange parseFloatRange(String str) {
		return parseFloatRange(str, "FloatRange");
	}
	
	protected static FloatRange parseFloatRange(String str, String msg) {
		int begin = str.indexOf('[');
		if ( begin < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		int end = str.indexOf(':', ++begin);
		if ( end < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		float low = Float.parseFloat(str.substring(begin, end-begin+1));
		
		end = str.indexOf(']', begin=end+1);
		if ( end < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		float high = Float.parseFloat(str.substring(begin, end-begin+1));
		
		return new FloatRange(low, high);
	}
}
