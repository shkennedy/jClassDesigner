package jcd.data;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import jcd.controller.ControlMode;
import static jcd.controller.ControlMode.*;
import static jcd.data.ContainerType.ABSTRACT_CLASS;
import static jcd.data.ContainerType.CLASS;
import static jcd.data.ContainerType.INTERFACE;
import static jcd.data.Modifier.ABSTRACT;
import jcd.gui.ContainerPane;
import jcd.gui.LineSegment;
import jcd.gui.MethodRow;
import jcd.gui.UMLLink;
import jcd.gui.VariableRow;
import saf.AppTemplate;
import saf.components.AppDataComponent;

/**
 *
 * @author Shane Kennedy Richard McKenna
 */
public class DataManager implements AppDataComponent {

    AppTemplate app;

    // CANVAS COMPONENTS
    ObservableList<Node> diagramComponents;

    // DIAGRAM COMPONENTS
    ArrayList<ContainerPane> containers;
    ArrayList<UMLLink> links;
    HashMap<String, MethodRow> methodRows; // NOT GOING TO WORK
    HashMap<String, VariableRow> variableRows;
    double scale;

    // GUI ACTION COMPONENTS
    ContainerPane selectedContainer;
    Line selectedLineSegment;
    // BreakPoint selectedBreakPoint; MAYBE
    MethodRow selectedMethodRow;
    VariableRow selectedVariableRow;
    Effect selectedEffect;

    // CONTROL SETTING
    ControlMode controlMode;

    public DataManager(AppTemplate initApp) throws Exception {
        app = initApp;

        containers = new ArrayList();
        links = new ArrayList();

        methodRows = new HashMap();
        variableRows = new HashMap();
        scale = 1;

        selectedContainer = null;
        selectedMethodRow = null;
        selectedVariableRow = null;
        DropShadow dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
        dropShadowEffect.setOffsetY(0.0f);
        dropShadowEffect.setSpread(1.0);
        dropShadowEffect.setColor(Color.YELLOW);
        dropShadowEffect.setBlurType(BlurType.ONE_PASS_BOX);
        dropShadowEffect.setRadius(5);
        selectedEffect = dropShadowEffect;
    }

    public DataManager() {
        containers = new ArrayList();
        links = new ArrayList();

        methodRows = new HashMap();
        variableRows = new HashMap();
    }

    public void setDiagramComponents(ObservableList<Node> initComponents) {
        diagramComponents = initComponents;
    }

    public ContainerPane selectTopElement(int x, int y) {
        ContainerPane newSelectedContainer = null;
        for (Node component : diagramComponents) {
            if (component instanceof ContainerPane) {
                if (((ContainerPane) component).contains(x, y)) {
                    newSelectedContainer = (ContainerPane) component;
                }
            }
        }
        /* else if (component instanceof Line) {
               if (((Line)component).contains(x, y))
                   selectedLine = component;
           } */
        if (selectedContainer != null) {
            selectedContainer.setEffect(null);
        }
        selectedContainer = newSelectedContainer;
        if (newSelectedContainer != null) {
            selectedContainer.setEffect(selectedEffect);
        }
        return selectedContainer;
    }

    public ContainerPane selectTopElementForResize(int x, int y) {
        for (Node component : diagramComponents) {
            if (component instanceof ContainerPane) {
                if (((ContainerPane) component).resizeTabIsWithin(x, y)) {
                    selectedContainer = (ContainerPane) component;
                }
            }
        }
        return selectedContainer;
    }

    public ObservableList<Node> getDiagramComponents() {
        return diagramComponents;
    }

    public ContainerPane getContainer(String name, String packageName) {
        for (Node diagramComponent : diagramComponents) {
            if (diagramComponent instanceof ContainerPane) {
                if (((ContainerPane) diagramComponent).getName().equals(name)
                        && ((ContainerPane) diagramComponent).getPackage().equals(packageName)) {
                    return (ContainerPane) diagramComponent;
                }
            }
        }
        return null;
    }

    public ContainerPane getContainer(int i) {
        return containers.get(i);
    }

    public ArrayList<ContainerPane> getContainers() {
        return containers;
    }

    public ArrayList<UMLLink> getLinks() {
        return links;
    }

    public UMLLink getLink(int i) {
        return links.get(i);
    }

    public ContainerPane getSelectedContainer() {
        return selectedContainer;
    }

    public void setHighlightSelected(boolean enable) {
        if (enable) {
            if (selectedContainer != null) {
                selectedContainer.setEffect(selectedEffect);
            }
        } else if (selectedContainer != null) {
            selectedContainer.setEffect(null);
        }
    }

    public void addContainer(ContainerType type) {
        ContainerPane newContainer = new ContainerPane(type);
        containers.add(newContainer);
        diagramComponents.add(newContainer);
        selectContainer(newContainer);
    }

    public void addContainer(ContainerPane container) {
        containers.add(container);
        diagramComponents.add(container);
    }

    public void removeContainer() {
        // DELETE LINKS TO selectedContainer
        ArrayList<UMLLink> linksToRemove = new ArrayList();
        for (UMLLink link : links) {
            if (link.getLink().getFromContainer().equals(selectedContainer) || 
                    link.getLink().getToContainer().equals(selectedContainer))
                linksToRemove.add(link);
        }
        for (UMLLink link : linksToRemove)
            removeLink(link);
        // DELETE CONTAINER
        containers.remove(selectedContainer);
        diagramComponents.remove(selectedContainer);
        selectedContainer = null;
    }

    public void addLink(UMLLink link) {
        for (LineSegment segment : link.getLineSegments()) {
            diagramComponents.add(segment);
        }
        links.add(link);
    }

    public void addLink(LinkType linkType, ContainerPane from, ContainerPane to) {
        // CHECK IF LINK ALREADY EXISTS
        for (UMLLink link : links) {
            if (link.getLink().getFromContainer().equals(from)
                    && link.getLink().getToContainer().equals(to)
                    && link.getLink().getType().equals(linkType)) {
                return;
            }
        }
        // CHECK IF WEAKER OR STRONGER LINK EXISTS
        for (UMLLink link : links) {
            if (link.getLink().getFromContainer().equals(from)
                    && link.getLink().getToContainer().equals(to)) {
                // WEAKER LINK FOUND - DELETE IT
                if (LinkType.compare(linkType, link.getLink().getType())) {
                    for (LineSegment segment : link.getLineSegments()) {
                        for (Node diagramComponent : diagramComponents) {
                            if ((diagramComponent).equals(segment)) {
                                diagramComponents.remove(diagramComponent);
                            }
                        }
                    }
                } // STRONGER LINK FOUND - DISREGARD CURRENT
                else {
                    return;
                }
            }
        }
        // MAKE AND ADD LINK
        UMLLink newLink = new UMLLink(linkType, from, to);
        diagramComponents.add(newLink.getLineSegments().get(0));
        links.add(newLink);
    }

    public void removeLink(UMLLink link) {
        for (LineSegment segment : link.getLineSegments()) {
            for (Node diagramComponent : diagramComponents) {
                if (segment.equals(diagramComponent)) {
                    diagramComponents.remove(diagramComponent);
                    break;
                }
            }
        }
        links.remove(link);
    }

    public void removeLinkFromSelected(LinkType type, ContainerPane container) {
        UMLLink linkToRemove = null;
        for (UMLLink link : links) {
            if (link.getLink().getFromContainer().equals(selectedContainer)) {
                if (link.getLink().getToContainer().equals(container)) {
                    if (link.getLink().getType().equals(type)) {
                        linkToRemove = link;
                    }
                }
            }
        }
        removeLink(linkToRemove);
    }

    public void setSelectedName(String name) throws IllegalArgumentException {
        boolean identicalFound = false;
        for (ContainerPane containerPane : containers) {
            if (!containerPane.equals(selectedContainer)) {
                if (containerPane.getContainer().getName().equals(name)) {
                    if (containerPane.getContainer().getPackage()
                            .equals(selectedContainer.getContainer().getPackage())) {
                        identicalFound = true;
                    }
                }
            }
        }
        if (identicalFound) {
            throw new IllegalArgumentException("Error: An element with this name and package already exists.");
        } else {
            if (name.charAt(0) >= '0' && name.charAt(0) <= '9') {
                throw new IllegalArgumentException("Error: Names cannot begin with a number.");
            }
            selectedContainer.setName(name);
        }
    }

    public void setSelectedPackage(String packageName) throws IllegalArgumentException {
        boolean identicalFound = false;
        for (ContainerPane containerPane : containers) {
            if (!containerPane.equals(selectedContainer)) {
                if (containerPane.getContainer().getName().equals(packageName)) {
                    if (containerPane.getContainer().getName()
                            .equals(selectedContainer.getContainer().getName())) {
                        identicalFound = true;
                    }
                }
            }
        }
        if (identicalFound) {
            throw new IllegalArgumentException("Error: An element with this name and package already exists.");
        } else {
            selectedContainer.setPackage(packageName);
        }
    }

    public void addVariableToSelected(VariablePrototype variable) {
        selectedContainer.getContainer().addVariable(variable);
        selectedContainer.update();
    }

    public void addMethodToSelected(MethodPrototype method) {
        selectedContainer.getContainer().addMethod(method);
        if (method.getModifiers().contains(ABSTRACT) && !selectedContainer.getType().equals(INTERFACE))
            selectedContainer.getContainer().setType(ABSTRACT_CLASS);
        selectedContainer.update();
    }

    public void removeVariableFromSelected(VariablePrototype variable) {
        // CHECK IF TYPE IS NON PRIMITIVE
        if (!VariablePrototype.PRIMITIVES.contains(variable.getType())) {
            for (UMLLink link : links) {
                if (link.getLink().getFromContainer().getName().equals(variable.getType())) {
                    removeLink(link);
                }
            }
        }
        selectedContainer.getContainer().removeVariable(variable);
        selectedContainer.update();
    }

    public void removeMethodFromSelected(MethodPrototype method) {
        // CHECK IF ABSTRACT AND IF OTHER METHODS ARE ABSTRACT
        if (method.getModifiers().contains(ABSTRACT) && !selectedContainer.getType().equals(INTERFACE)) {
            boolean noAbstractFound = true;
            for (MethodPrototype otherMethod : selectedContainer.getContainer().getMethods()) {
                if (!otherMethod.equals(method) && otherMethod.getModifiers().contains(ABSTRACT))
                    noAbstractFound = false;
            }
            if (noAbstractFound)
                selectedContainer.getContainer().setType(CLASS);
        }
        // CHECK IF NON_PRIMITIVES ARE USED
        ArrayList<UMLLink> linksToRemove = new ArrayList();
        if (!VariablePrototype.PRIMITIVES.contains(method.getReturnType())) {
            for (UMLLink link : links) {
                if (link.getLink().getFromContainer().getName().equals(method.getReturnType())) {
                    linksToRemove.add(link);
                    break;
                }
            }
        }
        
        for (VariablePrototype arg : method.getArguments()) {
            if (!VariablePrototype.PRIMITIVES.contains(arg.getType())) {
                for (UMLLink link : links) {
                    if (link.getLink().getFromContainer().getName().equals(arg.getType())) {
                        linksToRemove.add(link);
                        break;
                    }
                }
            }
        }
        for (UMLLink link : linksToRemove) {
            removeLink(link);
        }
        selectedContainer.getContainer().removeMethod(method);
        selectedContainer.update();
    }

    public void selectContainer(ContainerPane container) {
        if (selectedContainer != null) {
            selectedContainer.setEffect(null);
        }
        selectedContainer = container;
        if (container != null) {
            selectedContainer.setEffect(selectedEffect);
        }
    }

    public void splitLine() {
        // ((Line)selectedLineSegment).split();
    }

    public void setScale(double newScale) {
        scale = newScale;
        for (Node component : diagramComponents) {
            if (component instanceof ContainerPane) {
                ((ContainerPane) component).setScale(scale);
            }
            // else
        }
    }

    public double getScale() {
        return scale;
    }

    public void undoChange() {
        // TODO - MOVE DATASTATE BACK 1
    }

    public void redoChange() {
        // TODO - MOVE DATASTATE UP 1
    }

    public void setControlMode(ControlMode mode) {
        controlMode = mode;
    }

    public boolean currControlModeIs(ControlMode mode) {
        return mode == controlMode;
    }

    @Override
    public void reset() {
        diagramComponents.clear();

        containers.clear();
        links.clear();
        methodRows.clear();
        variableRows.clear();

        selectedContainer = null;
        selectedMethodRow = null;
        selectedVariableRow = null;

        controlMode = NEW;
    }
}
