package camus.service;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class IntRange {
	public int low = Integer.MIN_VALUE;
	public int high = Integer.MAX_VALUE;
	
	public IntRange() { }
	public IntRange(int low, int high) {
		this.low = low;
		this.high = high;
	}
	
	public boolean isIn(int value) {
		return value >= low && value <= high;
	}
	
	public String toString() {
		return String.format("[%d:%d]", low, high);
	}
	
	public static IntRange parseIntRange(String str) {
		return parseIntRange(str, "IntRange");
	}
	
	protected static IntRange parseIntRange(String str, String msg) {
		int begin = str.indexOf('[');
		if ( begin < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		int end = str.indexOf(':', ++begin);
		if ( end < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		int low = Integer.parseInt(str.substring(begin, end-begin+1));
		
		end = str.indexOf(']', begin=end+1);
		if ( end < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		int high = Integer.parseInt(str.substring(begin, end-begin+1));
		
		return new IntRange(low, high);
	}
}
