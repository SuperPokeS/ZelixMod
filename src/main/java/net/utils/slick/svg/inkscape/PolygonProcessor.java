package net.utils.slick.svg.inkscape;

import java.util.ArrayList;
import java.util.StringTokenizer;

import net.utils.slick.geom.Polygon;
import net.utils.slick.geom.Shape;
import net.utils.slick.geom.Transform;
import net.utils.slick.svg.Diagram;
import net.utils.slick.svg.Figure;
import net.utils.slick.svg.Loader;
import net.utils.slick.svg.NonGeometricData;
import net.utils.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * A processor for the <polygon> and <path> elements marked as not an arc.
 *
 * @author kevin
 */
public class PolygonProcessor implements ElementProcessor {

	/**
	 * Process the points in a polygon definition
	 * 
	 * @param poly The polygon being built
	 * @param element The XML element being read
	 * @param tokens The tokens representing the path
	 * @return The number of points found
	 * @throws ParsingException Indicates an invalid token in the path
	 */
	private static int processPoly(Polygon poly, Element element, StringTokenizer tokens) throws ParsingException {
		int count = 0;
		
		ArrayList pts = new ArrayList();
		boolean moved = false;
		boolean closed = false;
		
		while (tokens.hasMoreTokens()) {
			String nextToken = tokens.nextToken();
			if (nextToken.equals("L")) {
				continue;
			}
			if (nextToken.equals("z")) {
				closed = true;
				break;
			}
			if (nextToken.equals("M")) {
				if (!moved) {
					moved = true;
					continue;
				}
				
				return 0;
			}
			if (nextToken.equals("C")) {
				return 0;
			}
			
			String tokenX = nextToken;
			String tokenY = tokens.nextToken();
			
			try {
				float x = Float.parseFloat(tokenX);
				float y = Float.parseFloat(tokenY);
				
				poly.addPoint(x,y);
				count++;
			} catch (NumberFormatException e) {
				throw new ParsingException(element.getAttribute("id"), "Invalid token in points list", e);
			}
		}
		
		poly.setClosed(closed);
		return count;
	}


	/**
	 * @see net.utils.slick.svg.inkscape.ElementProcessor#process(net.utils.slick.svg.Loader, org.w3c.dom.Element, net.utils.slick.svg.Diagram, net.utils.slick.geom.Transform)
	 */
	public void process(Loader loader, Element element, Diagram diagram, Transform t) throws ParsingException {
		Transform transform = Util.getTransform(element);
		transform = new Transform(t, transform); 
		
		String points = element.getAttribute("points");
		if (element.getNodeName().equals("path")) {
			points = element.getAttribute("d");
		}
		
		StringTokenizer tokens = new StringTokenizer(points, ", ");
		Polygon poly = new Polygon();
		int count = processPoly(poly, element, tokens);
		
		NonGeometricData data = Util.getNonGeometricData(element);
		if (count > 3) {
			Shape shape = poly.transform(transform);
			
			diagram.addFigure(new Figure(Figure.POLYGON, shape, data, transform));
		} 
	}

	/**
	 * @see net.utils.slick.svg.inkscape.ElementProcessor#handles(org.w3c.dom.Element)
	 */
	public boolean handles(Element element) {
		if (element.getNodeName().equals("polygon")) {
			return true;
		}
		
		if (element.getNodeName().equals("path")) {
			if (!"arc".equals(element.getAttributeNS(Util.SODIPODI, "type"))) {
				return true;
			}
		}
		
		return false;
	}
}
