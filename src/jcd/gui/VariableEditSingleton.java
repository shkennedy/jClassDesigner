package jcd.gui;

import java.net.URL;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.data.DataManager;
import jcd.data.Modifier;
import static jcd.data.Modifier.*;
import jcd.data.VariablePrototype;
import saf.ui.AppMessageDialogSingleton;

/**
 *
 * @author Shane
 */
public class VariableEditSingleton extends Stage {

    private static VariableEditSingleton singleton = null;

    DataManager dataManager;

    String usedClass;

    VariablePrototype variable;

    private Scene editScene;
    private VBox editPane;

    private HBox namePane;
    private Label nameLabel;
    private TextField nameTF;

    private HBox typePane;
    private Label typeLabel;
    private TextField typeTF;

    private HBox modifierPane;
    private HBox accessPane;
    private Label accessLabel;
    private ChoiceBox accessChoiceBox;
    private ObservableList<String> accessChoices;
    private HBox otherModifiersPane;
    private CheckBox staticCheckBox;
    private CheckBox finalCheckBox;

    private HBox buttonsPane;
    private Button applyButton;
    private Button cancelButton;

    private VariableEditSingleton() {
    }

    public static VariableEditSingleton getSingleton() {
        if (singleton == null) {
            singleton = new VariableEditSingleton();
        }
        return singleton;
    }

    public void init(Stage owner, DataManager dataManager) {
        setTitle("Edit Variable");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);

        variable = new VariablePrototype();

        this.dataManager = dataManager;

        nameLabel = new Label("Name:");
        nameTF = new TextField();
        namePane = new HBox();
        namePane.getChildren().add(nameLabel);
        namePane.getChildren().add(nameTF);

        typeLabel = new Label("Type:");
        typeTF = new TextField();
        typePane = new HBox();
        typePane.getChildren().add(typeLabel);
        typePane.getChildren().add(typeTF);

        accessLabel = new Label("Access:");
        accessChoices = FXCollections.observableArrayList();
        accessChoices.add("public");
        accessChoices.add("protected");
        accessChoices.add("private");
        accessChoiceBox = new ChoiceBox(accessChoices);
        accessPane = new HBox();
        accessPane.getChildren().add(accessLabel);
        accessPane.getChildren().add(accessChoiceBox);

        otherModifiersPane = new HBox();
        staticCheckBox = new CheckBox("static");
        finalCheckBox = new CheckBox("final");
        otherModifiersPane.getChildren().add(staticCheckBox);
        otherModifiersPane.getChildren().add(finalCheckBox);

        modifierPane = new HBox();
        modifierPane.getChildren().add(accessPane);
        modifierPane.getChildren().add(otherModifiersPane);

        buttonsPane = new HBox();
        applyButton = new Button("Apply");
        cancelButton = new Button("Cancel");
        buttonsPane.getChildren().add(cancelButton);
        buttonsPane.getChildren().add(applyButton);

        editPane = new VBox();
        editPane.getChildren().add(namePane);
        editPane.getChildren().add(typePane);
        editPane.getChildren().add(modifierPane);
        editPane.getChildren().add(buttonsPane);

        editScene = new Scene(editPane);
        setScene(editScene);

        setupHandlers();
        initStyle();
    }

    private void setupHandlers() {
        applyButton.setOnAction(e -> {
            // NAME
            if (nameTF.getText().trim().equals("")) {
                AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
                messageDialog.show("Edit Variable Error", "Variable name must be entered.");
                return;
            }
            variable.setName(nameTF.getText().trim());

            // TYPE
            if (typeTF.getText().trim().equals("")) {
                AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
                messageDialog.show("Edit Variable Error", "Variable type must be entered.");
                return;
            }
            variable.setType(typeTF.getText().trim());

            // IF VARIABLE IS NON-PRIMITIVE SET usedClass
            if (!VariablePrototype.PRIMITIVES.contains(variable.getType())) {
                usedClass = variable.getType();
            }

            // ACCESS
            variable.getModifiers().remove(PUBLIC);
            variable.getModifiers().remove(PROTECTED);
            variable.getModifiers().remove(PRIVATE);
            variable.addModifier(0, Modifier.get(accessChoiceBox.getSelectionModel().getSelectedIndex()));

            // STATIC
            if (staticCheckBox.isSelected()) {
                if (!variable.getModifiers().contains(STATIC)) {
                    variable.addModifier(STATIC);
                } else {
                    if (!variable.getModifiers().contains(STATIC)) {
                        variable.getModifiers().remove(STATIC);
                    }
                }
            }

            // FINAL
            if (finalCheckBox.isSelected()) {
                if (!variable.getModifiers().contains(FINAL)) {
                    variable.addModifier(FINAL);
                } else {
                    if (!variable.getModifiers().contains(FINAL)) {
                        variable.getModifiers().remove(FINAL);
                    }
                }
            }

            // ADD IT TO DATAMANAGER
            if (!dataManager.getSelectedContainer().getContainer().getVariables().contains(variable)) {
                dataManager.addVariableToSelected(variable);
            }

            VariableEditSingleton.this.close();
        });

        cancelButton.setOnAction(e -> {
            usedClass = "";
            VariableEditSingleton.this.close();
        });
    }

    private void initStyle() {
        URL stylesheetURL = getClass().getResource("../css/jcd_style.css");
        String stylesheetPath = stylesheetURL.toExternalForm();
        editScene.getStylesheets().add(stylesheetPath);
        editPane.getStyleClass().add("components_toolbar");
        namePane.setAlignment(Pos.CENTER);
        namePane.getStyleClass().add("name_pane");
        typePane.setAlignment(Pos.CENTER);
        typePane.getStyleClass().add("name_pane");
        accessPane.setAlignment(Pos.CENTER);
        accessPane.getStyleClass().add("package_pane");
        otherModifiersPane.setAlignment(Pos.CENTER);
        otherModifiersPane.getStyleClass().add("name_pane");
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.getStyleClass().add("name_pane");
    }

    public void show(VariablePrototype initVariable) {
        usedClass = "";
        if (initVariable != null) {
            variable = initVariable;
            // SET SETTINGS TO MATCH 
            nameTF.setText(variable.getName());
            typeTF.setText(variable.getType());
            accessChoiceBox.getSelectionModel().select(variable.getModifiers()
                    .get(0).ordinal());
            staticCheckBox.setSelected(variable.getModifiers().contains(STATIC));
            finalCheckBox.setSelected(variable.getModifiers().contains(FINAL));
        } else {
            variable = new VariablePrototype();
            nameTF.setText("");
            typeTF.setText("");
            accessChoiceBox.getSelectionModel().select(0);
            staticCheckBox.setSelected(false);
            finalCheckBox.setSelected(false);
        }
        showAndWait();
    }

    public String getUsedClass() {
        return usedClass;
    }
}
