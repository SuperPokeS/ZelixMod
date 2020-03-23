package net.utils.slick.svg.inkscape;

import net.utils.slick.geom.Shape;
import net.utils.slick.geom.Transform;
import net.utils.slick.svg.Diagram;
import net.utils.slick.svg.Figure;
import net.utils.slick.svg.Loader;
import net.utils.slick.svg.NonGeometricData;
import net.utils.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * Processor for the "use", a tag that allows references to other elements
 * and cloning.
 * 
 * @author kevin
 */
public class UseProcessor implements ElementProcessor {

	/**
	 * @see net.utils.slick.svg.inkscape.ElementProcessor#handles(org.w3c.dom.Element)
	 */
	public boolean handles(Element element) {
		return element.getNodeName().equals("use");
	}

	/**
	 * @see net.utils.slick.svg.inkscape.ElementProcessor#process(net.utils.slick.svg.Loader, org.w3c.dom.Element, net.utils.slick.svg.Diagram, net.utils.slick.geom.Transform)
	 */
	public void process(Loader loader, Element element, Diagram diagram,
			Transform transform) throws ParsingException {

		String ref = element.getAttributeNS("http://www.w3.org/1999/xlink", "href");
		String href = Util.getAsReference(ref);
		
		Figure referenced = diagram.getFigureByID(href);
		if (referenced == null) {
			throw new ParsingException(element, "Unable to locate referenced element: "+href);
		}
		
		Transform local = Util.getTransform(element);
		Transform trans = local.concatenate(referenced.getTransform());
		
		NonGeometricData data = Util.getNonGeometricData(element);
		Shape shape = referenced.getShape().transform(trans);
		data.addAttribute(NonGeometricData.FILL, referenced.getData().getAttribute(NonGeometricData.FILL));
		data.addAttribute(NonGeometricData.STROKE, referenced.getData().getAttribute(NonGeometricData.STROKE));
		data.addAttribute(NonGeometricData.OPACITY, referenced.getData().getAttribute(NonGeometricData.OPACITY));
		data.addAttribute(NonGeometricData.STROKE_WIDTH, referenced.getData().getAttribute(NonGeometricData.STROKE_WIDTH));
		
		Figure figure = new Figure(referenced.getType(), shape, data, trans);
		diagram.addFigure(figure);
	}

}
