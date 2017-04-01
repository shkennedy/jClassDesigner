package jcd.gui;

import java.net.URL;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static jcd.data.ContainerType.*;
import jcd.data.DataManager;
import static jcd.data.LinkType.IMPLEMENTS;

/**
 *
 * @author Shane
 */
public class ParentSelectionSingleton extends Stage {

    private static ParentSelectionSingleton singleton;

    private String selectedExtends;
    private ArrayList<String> selectedImplements;

    private Scene selectionScene;

    private VBox selectionPane;
    private HBox extendPane;
    private Label extendLabel;
    private ChoiceBox extendCB;
    private VBox implementPane;
    private Label implementLabel;
    private ArrayList<CheckBox> implementCBs;
    private HBox buttonPane;
    private Button cancelButton;
    private Button applyButton;

    private ParentSelectionSingleton() {
    }

    public static ParentSelectionSingleton getSingleton() {
        if (singleton == null) {
            singleton = new ParentSelectionSingleton();
        }
        return singleton;
    }

    public void init(Stage owner) {
        setTitle("Package Inquiry");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);

        selectedExtends = "";
        selectedImplements = new ArrayList();

        extendLabel = new Label("Extend:");
        extendCB = new ChoiceBox();
        extendPane = new HBox();
        extendPane.alignmentProperty().set(Pos.CENTER);
        extendPane.getChildren().add(extendLabel);
        extendPane.getChildren().add(extendCB);

        implementLabel = new Label("Implement:");
        implementPane = new VBox(implementLabel);
        implementCBs = new ArrayList();

        cancelButton = new Button("Cancel");
        applyButton = new Button("Apply");
        buttonPane = new HBox();
        buttonPane.alignmentProperty().set(Pos.CENTER);
        buttonPane.getChildren().add(cancelButton);
        buttonPane.getChildren().add(applyButton);

        selectionPane = new VBox();
        selectionPane.getChildren().add(extendPane);
        selectionPane.getChildren().add(implementPane);
        selectionPane.getChildren().add(buttonPane);
        selectionScene = new Scene(selectionPane);
        setScene(selectionScene);

        setupHandlers();
        initStyle();
    }

    private void setupHandlers() {
        cancelButton.setOnAction(e -> {
            selectedExtends = "CANCELLED EDIT";
            ParentSelectionSingleton.this.close();
        });
    }

    private void initStyle() {
        URL stylesheetURL = getClass().getResource("../css/jcd_style.css");
        String stylesheetPath = stylesheetURL.toExternalForm();
        selectionScene.getStylesheets().add(stylesheetPath);
        selectionPane.getStyleClass().add("components_toolbar");
        extendPane.getStyleClass().add("name_pane");
    }

    public void show(DataManager dataManager) {
        if (dataManager.getSelectedContainer().getContainer().getType().equals(CLASS)
                || dataManager.getSelectedContainer().getContainer().getType().equals(ABSTRACT_CLASS)) {
            extendCB.setDisable(false);
        } else {
            extendCB.setDisable(true);
        }
        selectedExtends = "";
        selectedImplements = new ArrayList();
        extendCB.getItems().clear();
        applyButton.setOnAction(e -> {
        });
        implementCBs.clear();
        int implementPaneSize = implementPane.getChildren().size();
        for (int i = 1; i < implementPaneSize; ++i) {
            implementPane.getChildren().remove(1);
        }

        // ADD CLASSES TO extendCB + INTERFACES TO implementCBs
        extendCB.getItems().add("");
        for (ContainerPane container : dataManager.getContainers()) {
            if (!container.getContainer().getName().equals(dataManager.getSelectedContainer().getName())) {
                // EXTEND FROM
                if (container.getType().equals(CLASS) || container.getType().equals(ABSTRACT_CLASS)) {
                    extendCB.getItems().add(container.getName());
                    if (dataManager.getSelectedContainer().getContainer().getExtendClass().equals(container.getContainer().getName()))
                    extendCB.getSelectionModel().select(container.getName());
                } // IMPLEMENT 
                else if (container.getType().equals(INTERFACE)) {
                    final CheckBox interfaceCB = new CheckBox(container.getContainer().getName());
                    implementPane.getChildren().add(interfaceCB);
                    implementCBs.add(interfaceCB);
                    interfaceCB.selectedProperty().set(dataManager.getSelectedContainer()
                            .getContainer().getImplementInterfaces().contains(interfaceCB.getText()));
                }
            }
        }
        applyButton.setOnAction(e -> {
            if (!extendCB.getSelectionModel().isEmpty()) {
                selectedExtends = (String) extendCB.getSelectionModel().getSelectedItem();
            }
            for (CheckBox implementCB : implementCBs) {
                if (implementCB.isSelected()) {
                    selectedImplements.add(implementCB.getText());
                }
            }
            ParentSelectionSingleton.this.close();
        });
        showAndWait();
    }

    public String getSelectedExtends() {
        return selectedExtends;
    }

    public ArrayList<String> getSelectedImplements() {
        return selectedImplements;
    }
}
