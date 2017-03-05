package camus.service;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class DoubleRange {
	public double low = Double.MIN_VALUE;
	public double high = Double.MAX_VALUE;
	
	public DoubleRange() { }
	public DoubleRange(double low, double high) {
		this.low = low;
		this.high = high;
	}
	
	public boolean isIn(int value) {
		return value >= low && value <= high;
	}
	
	public String toString() {
		return String.format("[%d:%d]", low, high);
	}
	
	public static DoubleRange parseDoubleRange(String str) {
		return parseDoubleRange(str, "DoubleRange");
	}
	
	protected static DoubleRange parseDoubleRange(String str, String msg) {
		int begin = str.indexOf('[');
		if ( begin < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		int end = str.indexOf(':', ++begin);
		if ( end < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		double low = Double.parseDouble(str.substring(begin, end-begin+1));
		
		end = str.indexOf(']', begin=end+1);
		if ( end < 0 ) {
			throw new IllegalArgumentException("invalid " + msg + ": str='" + str + "'");
		}
		double high = Double.parseDouble(str.substring(begin, end-begin+1));
		
		return new DoubleRange(low, high);
	}
}
