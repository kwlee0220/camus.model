package camus.service.image;

import camus.service.geo.Size2d;
import camus.service.vision.Image;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public interface Screen {
	/**
	 * 스크린 크기(해상도)를 반환한다.
	 * 
	 * @return	해상도 크기.
	 */
	public Size2d getScreenSize();
	
	public void setVisible(boolean visible);
	public boolean getVisible();
	
	public void show(Image image);
	public void clear();
}
