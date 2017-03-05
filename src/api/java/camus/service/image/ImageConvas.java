package camus.service.image;

import java.awt.image.BufferedImage;

import planet.idl.PlanetLocal;

import camus.service.geo.Point;
import camus.service.geo.Polygon;
import camus.service.geo.Rectangle;
import camus.service.geo.Size2d;
import camus.service.vision.Image;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetLocal
public interface ImageConvas {
	/**
	 * 이미지 캔버스 크기를 반환한다.
	 * 
	 * @return	캔버스 크기.
	 */
	public Size2d getConvasSize();

	/**
	 * 화면 출력용 버퍼에 주어진 BufferedImage을 그린다.
	 * 
	 * @param convas	버퍼에 그릴 캔버스 객체.
	 */
	public void draw(BufferedImage bi);
	
	/**
	 * 화면 출력용 버퍼에 이미지를 그린다.
	 * 
	 * @param image	버퍼에 그릴 이미지 객체.
	 */
	public void drawImage(Image image);
	
	/**
	 * 주어진 좌표의 직선을 그린다.
	 * 
	 * @param fromPt	직선의 시작 좌표
	 * @param toPt		직선의 끝 좌표
	 * @param color		그려질 직선 색.
	 * @param thickness	직선 굵기.
	 */
	public void drawLine(Point fromPt, Point toPt, Color color, int thickness);
	
	/**
	 * 화면 출력용 버퍼에 주어진 사각형을 그린다.
	 * 
	 * @param rect	그려질 사각형 좌표 정보.
	 * @param color	그려질 사각형 색.
	 * @param thickness	사각형 선 굵기.
	 * 				만일 음수인 경우는 사각형 안을 다 채운다.
	 */
	public void drawRect(Rectangle rect, Color color, int thickness);
	
	/**
	 * 화면 출력용 버퍼에 주어진 다각형을 그린다.
	 * 
	 * @param polygon	그려질 다각형 좌표 정보.
	 * @param color	그려질 다각형 색.
	 * @param thickness	다각형 선 굵기.
	 */
	public void drawPolygon(Polygon polygon, Color color, int thickness);
	
	/**
	 * 화면 출력용 버퍼에 주어진 문자열을 쓴다.
	 * 그려진 이미지는 {@link #updateView()}가 호출될 때 화면에 출력된다.
	 * 
	 * @param str		쓰여질 문자열.
	 * @param loc		쓰여질 문자열 첫 글자의 좌표 정보.
	 * @param fontSize	쓰여질 문자열 폰트.
	 * @param color		쓰여질 문자열 색.
	 */
	public void drawString(String str, Point loc, int fontSize, Color color);

	/**
	 * 화면 출력용 버퍼을 모두 지운다. (검은색으로 모두 채운다)
	 */
	public void clear();
}
