package camus.service;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class SizeRange extends IntRange {
	public SizeRange() {
		super(0, Integer.MAX_VALUE);
	}
	public SizeRange(int low, int high) {
		super(low, high);
	}
	public SizeRange(int low) {
		super(low, Integer.MAX_VALUE);
	}
	public SizeRange(IntRange r) {
		super(r.low, r.high);
	}
	
	public String toString() {
		return String.format("[%d:%d]", low, high);
	}
	
	public static SizeRange parseSizeRange(String str) {
		return new SizeRange(parseIntRange(str, "SizeRange"));
	}
}
