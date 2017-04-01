package jcd.gui;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Shane
 */
public class LineSegment extends Line {
    public static final int DEFAULT_BREAKPOINT_RADIUS = 5;
    private Circle startPoint;
    private Circle endPoint;
    
    public LineSegment() {
        startPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS);
        endPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS); 
        startXProperty().bind(startPoint.centerXProperty());
        startYProperty().bind(startPoint.centerYProperty());
        endXProperty().bind(endPoint.centerXProperty());
        endYProperty().bind(endPoint.centerYProperty());
        setStrokeWidth(2);
        setStroke(Color.BLACK);
    }
    
    public LineSegment(Node bindNode1, Node bindNode2) {
        startPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS);
        endPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS); 
        bindStartPointTo(bindNode1);
        bindEndPointTo(bindNode2);
        startPoint.setFill(Color.GRAY);
        endPoint.setFill(Color.GRAY);
        // line
        startXProperty().bind(startPoint.centerXProperty());
        startYProperty().bind(startPoint.centerYProperty());
        endXProperty().bind(endPoint.centerXProperty());
        endYProperty().bind(endPoint.centerYProperty());
        setStrokeWidth(2);
        setStroke(Color.BLACK);
    }
    
    public Circle getStartPoint() {
        return startPoint;
    }
    
    public Circle getEndPoint() {
        return endPoint;
    }

    public void bindStartPointTo(Node node) {
        if (node == null) {
            startPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS); 
        } else if (node instanceof Circle) {
            startPoint = (Circle)node;
        } else if (node instanceof ContainerPane) {
            startPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS);
            startPoint.centerXProperty().bind(((ContainerPane)node).layoutXProperty());
            startPoint.centerYProperty().bind(((ContainerPane)node).layoutYProperty()
                    .add(((ContainerPane)node).heightProperty().divide(2)));
        }
    }    
    
    public void bindEndPointTo(Node node) {
        if (node == null) {
            endPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS); 
        } else if (node instanceof Circle) {
            endPoint = (Circle)node;
        } else if (node instanceof ContainerPane) {
            endPoint = new Circle(DEFAULT_BREAKPOINT_RADIUS);
            endPoint.centerXProperty().bind(((ContainerPane)node).layoutXProperty());
            endPoint.centerYProperty().bind(((ContainerPane)node).layoutYProperty()
                    .add(((ContainerPane)node).heightProperty().divide(2)));
        }
    }    

    public void setLocaions(int startX, int startY, int endX, int endY) {
        startPoint.setCenterX(startX);
        startPoint.setCenterY(startY);
        endPoint.setCenterX(endX);
        endPoint.setCenterY(endY);
    }
    
    public void setShowPoints(boolean show) {
        startPoint.setVisible(show);
        endPoint.setVisible(show);
    }
}
