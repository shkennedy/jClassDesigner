package jcd.gui;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jcd.data.MethodPrototype;
import jcd.data.Modifier;
import static jcd.data.Modifier.*;
import jcd.data.VariablePrototype;

/**
 *
 * @author Shane
 */
public class MethodRow extends HBox {
    private MethodPrototype method;
    
    private VBox buttonPane;
    private Button deleteButton;
    private Button editButton;
    private HBox namePane;
    private Label nameLabel;
    private HBox returnPane;
    private Label returnLabel;
    private HBox staticPane;
    private Label staticLabel;
    private HBox abstractPane;
    private Label abstractLabel;
    private HBox accessPane;
    private Label accessLabel;
    private ArrayList<HBox> args;
    
    public MethodRow(MethodPrototype method) {
        this.method = method;
        deleteButton = new Button("Delete");
        deleteButton.setTooltip(new Tooltip("Delete Method"));
        editButton = new Button("Edit");
        editButton.setTooltip(new Tooltip("EditVariable"));
        buttonPane = new VBox();
        buttonPane.getChildren().add(editButton);
        buttonPane.getChildren().add(deleteButton);
        buttonPane.alignmentProperty().set(Pos.CENTER);
        nameLabel = new Label(method.getName());
        namePane = new HBox(nameLabel);       
        returnLabel = new Label(method.getReturnType());
        returnPane = new HBox(returnLabel);        
        staticLabel = new Label(method.getModifiers().contains(STATIC)? "yes" : "no");
        staticPane = new HBox(staticLabel);       
        abstractLabel = new Label(method.getModifiers().contains(ABSTRACT)? "yes" : "no");
        abstractPane = new HBox(abstractLabel);        
        accessLabel = new Label(Modifier.getString(method.getModifiers().get(0)));
        accessPane = new HBox(accessLabel);
        args = new ArrayList();
        getChildren().add(buttonPane);
        getChildren().add(namePane);
        getChildren().add(returnPane);
        getChildren().add(staticPane);
        getChildren().add(abstractPane);
        getChildren().add(accessPane);
        
        makeArgs();
        
        for (HBox hbox : getComponents()) {
            hbox.alignmentProperty().set(Pos.CENTER);
            hbox.getStyleClass().add("name_pane");
        }
        getStyleClass().add("components_toolbar");
    }
    
    private void makeArgs() {
        HBox argPane;
        Label argLabel;
        for (VariablePrototype arg : method.getArguments()) {
            argLabel = new Label(arg.toString());
            argPane = new HBox(argLabel);
            argPane.alignmentProperty().set(Pos.CENTER);
            args.add(argPane);
            getChildren().add(argPane);
        }
    }
    
    public ArrayList<HBox> getComponents() {
        ArrayList<HBox> methodRow = new ArrayList();
        methodRow.add(namePane);
        methodRow.add(returnPane);
        methodRow.add(staticPane);
        methodRow.add(abstractPane);
        methodRow.add(accessPane);
        methodRow.addAll(args);
        return methodRow;
    }
    
    public void setDeleteButtonSettings(ImageView icon, Runnable handler) {
      //  deleteButton.setGraphic(icon);
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
    
    public void update(){}
}
