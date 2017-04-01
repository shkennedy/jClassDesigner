package jcd.gui;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jcd.data.Modifier;
import static jcd.data.Modifier.STATIC;
import jcd.data.VariablePrototype;

/**
 *
 * @author Shane
 */
public class VariableRow extends HBox {
    private VariablePrototype variable;
    
    private VBox buttonPane;
    private Button deleteButton;
    private Button editButton;
    private HBox namePane;
    private Label nameLabel;
    private HBox typePane;
    private Label typeLabel;
    private HBox staticPane;
    private Label staticLabel;
    private HBox accessPane;
    private Label accessLabel;
    
    public VariableRow(VariablePrototype variable) {
        this.variable = variable;
        deleteButton = new Button("Delete");
        deleteButton.setTooltip(new Tooltip("Delete Variable"));
        editButton = new Button("Edit");
        editButton.setTooltip(new Tooltip("EditVariable"));
        buttonPane = new VBox();
        buttonPane.getChildren().add(editButton);
        buttonPane.getChildren().add(deleteButton);
        buttonPane.alignmentProperty().set(Pos.CENTER);
        nameLabel = new Label(variable.getName());
        namePane = new HBox(nameLabel);
        typeLabel = new Label(variable.getType());
        typePane = new HBox(typeLabel);
        staticLabel = new Label(variable.getModifiers().contains(STATIC)? "yes" : "no");
        staticPane = new HBox(staticLabel);
        accessLabel = new Label(Modifier.getString(variable.getModifiers().get(0)));
        accessPane = new HBox(accessLabel);
        getChildren().add(buttonPane);
        getChildren().add(namePane);
        getChildren().add(typePane);
        getChildren().add(staticPane);
        getChildren().add(accessPane);
        
        for (HBox hbox : getComponents()) {
            hbox.alignmentProperty().set(Pos.CENTER);
            hbox.getStyleClass().add("name_pane");
        }

        getStyleClass().add("components_toolbar");
    }
    
    public ArrayList<HBox> getComponents() {
        ArrayList<HBox> variableRow = new ArrayList();
        variableRow.add(namePane);
        variableRow.add(typePane);
        variableRow.add(staticPane);
        variableRow.add(accessPane);
        return variableRow;
    }
    
    public void setDeleteButtonSettings(ImageView icon, Runnable handler) {
        //deleteButton.setGraphic(icon);
        deleteButton.setOnAction(e -> {
            handler.run();
        });
    }
    
    public void setEditButtonSettings(ImageView icon, Runnable handler) {
        //  editButton.setGraphic(icon);
        editButton.setOnAction(e -> {
            handler.run();
        });
    }
    
    public void update() {}
}
