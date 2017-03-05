package camus.service.vision;


/**
 * <code>ImageEncoding</code>은 이미지의 인코딩 방식을 정의한다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public enum ImageEncoding {
	/** JPEG 이미지 */
	JPEG("jpg"),
	/** RGB24 rastor 이미지 */
	RGB("rgb"),
	/** Depth (CV_16SC1) 이미지 */
	DEPTH("depth"),
	/** YUV422 이미지 */
	YUV422("yuv422"),
	/** MPEG 이미지 (Livezen사 코덱 사용) */
	LVZ("lvz"),
	/** H.2634 이미지 */
	H263("h263");
	
	private String m_name;
	
	private ImageEncoding(String name) {
		m_name = name;
	}
	
	public String getName() {
		return m_name;
	}
	
	public String toString() {
		return m_name;
	}
}
