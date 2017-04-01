package jcd.gui;

import java.util.ArrayList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import jcd.data.LinkPrototype;
import jcd.data.LinkType;
import static jcd.data.LinkType.*;

/**
 *
 * @author Shane
 */
public class UMLLink extends Line {

    LinkPrototype link;
    ArrayList<LineSegment> lineSegments;
    boolean dashed;
    Shape tip;

    public UMLLink(LinkType type, ContainerPane from, ContainerPane to) {
        link = new LinkPrototype(type, from, to);
        if (type == EXTENDS || type == IMPLEMENTS) {
            tip = new Polygon(); // triangle
        } else if (type == COMPOSED || type == AGGREGATE) {
            tip = new Polygon(); // square
        } else if (type == ASSOCIATED || type == DEPENDS) {
            tip = new Polygon(); // arrow
        }
        lineSegments = new ArrayList();
        lineSegments.add(new LineSegment(link.getFromContainer(), link.getToContainer()));
        for (LineSegment segment : lineSegments)
            segment.setVisible(true);
    }

    public LinkPrototype getLink() {
        return link;
    }
    
    public ArrayList<LineSegment> getLineSegments() {
        return lineSegments;
    }

    public void split(LineSegment selectedSegment) {
        int i;
        for (i = 0; i < lineSegments.size(); ++i)
            if (selectedSegment.equals(lineSegments.get(i))) 
                break;      
        Circle newBreakPoint = new Circle(LineSegment.DEFAULT_BREAKPOINT_RADIUS);
        newBreakPoint.setCenterX((selectedSegment.getEndX() + selectedSegment.getStartX()) / 2);
        newBreakPoint.setCenterY((selectedSegment.getEndY() + selectedSegment.getStartY()) / 2);
        LineSegment newSegment1 = new LineSegment(selectedSegment.getStartPoint(), newBreakPoint);
        LineSegment newSegment2 = new LineSegment(selectedSegment.getEndPoint(), newBreakPoint);
        lineSegments.add(i, newSegment1);
        lineSegments.add(i + 1, newSegment2);
        lineSegments.remove(selectedSegment);
    }
    
    public void makeLineSegment() {
        
    }
    
    public void setEditDisplay(boolean editing) {
        for (LineSegment segment : lineSegments)
            segment.setShowPoints(editing);
    }
}
