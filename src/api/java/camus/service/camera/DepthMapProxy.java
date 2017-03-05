package camus.service.camera;



/**
 *
 * @author Kang-Woo Lee
 */
public interface DepthMapProxy extends ImageProxy {
	public ImageProxy toColoredGray();
}