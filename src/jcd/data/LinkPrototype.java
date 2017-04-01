package jcd.data;

import jcd.gui.ContainerPane;

/**
 *
 * @author Shane
 */
public class LinkPrototype {

    private LinkType type;
    private ContainerPane fromContainer;
    private ContainerPane toContainer;

    public LinkPrototype(LinkType type, ContainerPane from, ContainerPane to) {
        this.type = type;
        fromContainer = from;
        toContainer = to;
    }

    public LinkType getType() {
        return type;
    }
    
    public ContainerPane getFromContainer() {
        return fromContainer;
    }

    public ContainerPane getToContainer() {
        return toContainer;
    }
}
