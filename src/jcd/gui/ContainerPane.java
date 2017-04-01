package jcd.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import jcd.data.ContainerPrototype;
import jcd.data.ContainerType;
import static jcd.data.ContainerType.*;
import jcd.data.MethodPrototype;
import jcd.data.VariablePrototype;

/**
 *
 * @author Shane
 */
public class ContainerPane extends VBox {

    private static final int[] DEFAULT_DIMENSIONS = {50, 50, 100, 400};
    private static final String CLASS_CONTAINER_PANE = "container_pane";
    private static final String CLASS_CONTAINER_PANE_CHILD = "container_pane_child";
    private ContainerPrototype container;
    private VBox namePane;
    private Label typeLabel;
    private Label nameLabel;
    private Pane methodsPane;
    private Label methodsLabel;
    private Pane variablesPane;
    private Label variablesLabel;
    private int tempX;
    private int tempY;

    public ContainerPane(ContainerType type) {
        // Make things for all containers

        container = new ContainerPrototype(type);

        namePane = new VBox();
        nameLabel = new Label();
        namePane.getChildren().add(nameLabel);
        namePane.setAlignment(Pos.CENTER);
        methodsLabel = new Label();
        methodsPane = new Pane(methodsLabel);

        getChildren().add(namePane);

        // If class, make things for class
        if (type == CLASS) {
            variablesLabel = new Label();
            variablesPane = new Pane(variablesLabel);
            getChildren().add(variablesPane);
            variablesPane.getStyleClass().add(CLASS_CONTAINER_PANE_CHILD);
        }

        getChildren().add(methodsPane);
        namePane.getStyleClass().add(CLASS_CONTAINER_PANE_CHILD);
        methodsPane.getStyleClass().add(CLASS_CONTAINER_PANE_CHILD);
        getStyleClass().add(CLASS_CONTAINER_PANE);

        setWidth(DEFAULT_DIMENSIONS[0]);
        setHeight(DEFAULT_DIMENSIONS[1]);
        setLocation(DEFAULT_DIMENSIONS[2], DEFAULT_DIMENSIONS[3]);
        tempX = (int) getLayoutX();
        tempY = (int) getLayoutY();
        update();
        this.getStyleClass().add(CLASS_CONTAINER_PANE);
    }

    public ContainerPane(ContainerType type, int x, int y, int width, int height) {
        // Make things for all containers
        container = new ContainerPrototype(type);

        nameLabel = new Label();
        namePane = new VBox(nameLabel);
        namePane.setAlignment(Pos.CENTER);
        methodsLabel = new Label();
        methodsPane = new Pane(methodsLabel);
        getChildren().add(namePane);

        // If class, make things for class
        if (type == CLASS) {
            variablesLabel = new Label();
            variablesPane = new Pane(variablesLabel);
            getChildren().add(variablesPane);
            variablesPane.getStyleClass().add(CLASS_CONTAINER_PANE_CHILD);
        }

        getChildren().add(methodsPane);
        namePane.getStyleClass().add(CLASS_CONTAINER_PANE_CHILD);
        methodsPane.getStyleClass().add(CLASS_CONTAINER_PANE_CHILD);
        getStyleClass().add(CLASS_CONTAINER_PANE);

        setLocation(x, y);
        tempX = (int) getLayoutX();
        tempY = (int) getLayoutY();
        setWidth(width);
        setHeight(height);
        update();
        this.getStyleClass().add(CLASS_CONTAINER_PANE);
    }

    public ContainerPane copy() {
        ContainerPane thisCopy = new ContainerPane(container.getType(), (int) getLayoutX(),
                (int) getLayoutY(), (int) getWidth(), (int) getHeight());
        // Copy all attributes
        return thisCopy;
    }

    public ContainerPrototype getContainer() {
        return container;
    }

    public ContainerType getType() {
        return container.getType();
    }

    public void setName(String name) {
        name = name.trim();
        nameLabel.setText(name);
        container.setName(name);
    }

    public void setPackage(String packageName) {
        packageName = packageName.trim();
        container.setPackage(packageName);
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getPackage() {
        return container.getPackage();
    }

    public void setLocation(int x, int y) {
        setLayoutX(x);
        setLayoutY(y);
        tempX = x;
        tempY = y;
    }

    public void drag(int x, int y) {
        setLayoutX(x - (tempX - x));
        setLayoutY(y - (tempY - y));
        tempX = x - (tempX - x);
        tempY = y - (tempY - y);
    }

    public void setSize(int newWidth, int newHeight) {
        setPrefWidth(newWidth);
        if (methodsPane == null && variablesPane == null) {
            namePane.setPrefHeight(newHeight);
        } else if (methodsPane != null && variablesPane == null) {
            namePane.setPrefHeight(newHeight / 2);
            methodsPane.setPrefHeight(newHeight / 2);
        } else {
            namePane.setPrefHeight(newHeight / 3);
            methodsPane.setPrefHeight(newHeight / 3);
            variablesPane.setPrefHeight(newHeight / 3);
        }
    }

    public void resize(int x, int y) {
        if (x > tempX - 5) {
            setPrefWidth(x - tempX);
        }
        if (y > tempY - 5) {
            int newHeight = y - tempY;
            if (methodsPane == null && variablesPane == null) {
                namePane.setPrefHeight(newHeight);
            } else if (methodsPane != null && variablesPane == null) {
                namePane.setPrefHeight(newHeight / 2);
                methodsPane.setPrefHeight(newHeight / 2);
            } else {
                namePane.setPrefHeight(newHeight / 3);
                methodsPane.setPrefHeight(newHeight / 3);
                variablesPane.setPrefHeight(newHeight / 3);
            }
        }
    }

    public boolean resizeTabIsWithin(int x, int y) {
        if (x > (getWidth() + getLayoutX() - 10) && x < (getWidth() + getLayoutX() + 10)) {
            if (y > (getHeight() + getLayoutY() - 10) && y < (getHeight() + getLayoutY()) + 10) {
                return true;
            }
        }
        return false;
    }

    public void setScale(double scale) {

    }

    public void update() {
        String labelText = "";
        if (container.getType().equals(CLASS)) {
            if (typeLabel != null) {
                namePane.getChildren().remove(typeLabel);
                typeLabel = null;
            }
        } else {
            if (typeLabel == null) {
                typeLabel = new Label();
                namePane.getChildren().add(0, typeLabel);
            }
            typeLabel.setText("<<" + ContainerType.getString(container.getType()).toLowerCase() + ">>");
        }
        nameLabel.setText(container.getName());
        if (variablesPane != null) {
            for (VariablePrototype variable : container.getVariables()) {
                labelText += variable.toString() + '\n';
            }
            if (labelText.equals("")) {
                if (variablesPane != null) {
                    variablesLabel.setText("");
                    getChildren().remove(variablesPane);
                }
            } else {
                if (!getChildren().contains(variablesPane)) {
                    getChildren().add(variablesPane);
                }
                variablesLabel.setText(labelText);
            }
        }
        labelText = "";
        for (MethodPrototype method : container.getMethods()) {
            labelText += method.toString() + "\n";
        }
        if (labelText.equals("")) {
            if (methodsPane != null) {
                methodsLabel.setText("");
                getChildren().remove(methodsPane);
            }
        } else {
            if (!getChildren().contains(methodsPane)) {
                getChildren().add(methodsPane);
            }
            methodsLabel.setText(labelText);
        }
    }

    public boolean contains(int x, int y) {
        if (x > getLayoutX() && x < (getLayoutX() + getWidth())) {
            if (y > getLayoutY() && y < (getLayoutY() + getHeight())) {
                return true;
            }
        }
        return false;
    }
}
