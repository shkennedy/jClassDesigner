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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.data.DataManager;
import jcd.data.MethodPrototype;
import jcd.data.Modifier;
import static jcd.data.Modifier.*;
import jcd.data.VariablePrototype;
import saf.ui.AppMessageDialogSingleton;

/**
 *
 * @author Shane
 */
public class MethodEditSingleton extends Stage {

    private static MethodEditSingleton singleton = null;

    DataManager dataManager;

    ArrayList<String> usedClasses;

    MethodPrototype method;

    private Scene editScene;
    private ScrollPane editScrollPane;
    private VBox editPane;

    private HBox namePane;
    private Label nameLabel;
    private TextField nameTF;

    private HBox returnTypePane;
    private Label returnTypeLabel;
    private TextField returnTypeTF;

    private HBox modifierPane;
    private HBox accessPane;
    private Label accessLabel;
    private ChoiceBox accessChoiceBox;
    private ObservableList<String> accessChoices;
    private HBox otherModifiersPane;
    private CheckBox staticCheckBox;
    private CheckBox abstractCheckBox;

    private VBox argumentsPane;
    private HBox argumentsHeaderPane;
    private Label argumentsLabel;
    private Button addArgumentButton;

    private HBox buttonsPane;
    private Button applyButton;
    private Button cancelButton;

    private MethodEditSingleton() {
    }

    public static MethodEditSingleton getSingleton() {
        if (singleton == null) {
            singleton = new MethodEditSingleton();
        }
        return singleton;
    }

    public void init(Stage owner, DataManager dataManager) {
        usedClasses = new ArrayList();

        setTitle("Edit Method");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);

        method = new MethodPrototype();

        this.dataManager = dataManager;

        nameLabel = new Label("Name:");
        nameTF = new TextField();
        namePane = new HBox();
        namePane.getChildren().add(nameLabel);
        namePane.getChildren().add(nameTF);

        returnTypeLabel = new Label("Return Type:");
        returnTypeTF = new TextField();
        returnTypePane = new HBox();
        returnTypePane.getChildren().add(returnTypeLabel);
        returnTypePane.getChildren().add(returnTypeTF);

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
        abstractCheckBox = new CheckBox("abstract");
        otherModifiersPane.getChildren().add(staticCheckBox);
        otherModifiersPane.getChildren().add(abstractCheckBox);

        modifierPane = new HBox();
        modifierPane.getChildren().add(accessPane);
        modifierPane.getChildren().add(otherModifiersPane);

        argumentsLabel = new Label("Arguments:");
        addArgumentButton = new Button("Add Argument");
        argumentsHeaderPane = new HBox();
        argumentsHeaderPane.getChildren().add(argumentsLabel);
        argumentsHeaderPane.getChildren().add(addArgumentButton);
        argumentsPane = new VBox();
        argumentsPane.getChildren().add(argumentsHeaderPane);

        buttonsPane = new HBox();
        applyButton = new Button("Apply");
        cancelButton = new Button("Cancel");
        buttonsPane.getChildren().add(cancelButton);
        buttonsPane.getChildren().add(applyButton);

        editPane = new VBox();
        editPane = new VBox();
        editPane.getChildren().add(namePane);
        editPane.getChildren().add(returnTypePane);
        editPane.getChildren().add(modifierPane);
        editPane.getChildren().add(argumentsPane);
        editPane.getChildren().add(buttonsPane);
        editScrollPane = new ScrollPane(editPane);

        editScene = new Scene(editScrollPane);
        setScene(editScene);

        setupHandlers();
        initStyle();
    }

    private void setupHandlers() {
        addArgumentButton.setOnAction(e -> {
            addArgument("", "");
        });

        applyButton.setOnAction(e -> {

            // NAME
            nameTF.setText(nameTF.getText().trim());
            if (nameTF.getText().equals("")) {
                AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
                messageDialog.show("Edit Method Error", "Method name must be entered.");
                return;
            }
            method.setName(nameTF.getText());

            // TYPE
            returnTypeTF.setText(returnTypeTF.getText().trim());
            if (returnTypeTF.getText().equals("")) {
                AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
                messageDialog.show("Edit Method Error", "Method return type must be entered.");
                return;
            }
            method.setReturnType(returnTypeTF.getText());
            if (!VariablePrototype.PRIMITIVES.contains(method.getReturnType()))
                usedClasses.add(method.getReturnType());

            // ACCESS
            method.getModifiers().remove(PUBLIC);
            method.getModifiers().remove(PROTECTED);
            method.getModifiers().remove(PRIVATE);
            method.addModifier(0, Modifier.get(accessChoiceBox.getSelectionModel().getSelectedIndex()));

            // STATIC
            if (staticCheckBox.isSelected()) {
                if (!method.getModifiers().contains(STATIC)) {
                    method.addModifier(STATIC);
                } else {
                    if (!method.getModifiers().contains(STATIC)) {
                        method.getModifiers().remove(STATIC);
                    }
                }
            }

            // ABSTRACT            
            if (abstractCheckBox.isSelected()) {
                if (!method.getModifiers().contains(ABSTRACT)) {
                    method.addModifier(ABSTRACT);
                } else {
                    if (!method.getModifiers().contains(ABSTRACT)) {
                        method.getModifiers().remove(ABSTRACT);
                    }
                }
            }

            // ARGUMENTS
            String name, type;
            ArrayList<VariablePrototype> args = new ArrayList();
            for (int i = 1; i < argumentsPane.getChildren().size(); ++i) {
                name = (((TextField) ((HBox) ((VBox) ((HBox) argumentsPane.getChildren()
                        .get(i)).getChildren().get(0)).getChildren().get(0)).getChildren()
                        .get(1)).getText());
                type = (((TextField) ((HBox) ((VBox) ((HBox) argumentsPane.getChildren()
                        .get(i)).getChildren().get(0)).getChildren().get(1)).getChildren()
                        .get(1)).getText());
                args.add(new VariablePrototype(name, type, null));
                if (!VariablePrototype.PRIMITIVES.contains(type)) {
                    usedClasses.add(type);
                }
            }
            method.setArguments(args);

            // ADD IT TO DATAMANAGER
            if (!dataManager.getSelectedContainer().getContainer().getMethods().contains(method)) {
                dataManager.addMethodToSelected(method);
            }

            MethodEditSingleton.this.close();
        });

        cancelButton.setOnAction(e -> {
            usedClasses.clear();
            MethodEditSingleton.this.close();
        });
    }

    private void initStyle() {
        URL stylesheetURL = getClass().getResource("../css/jcd_style.css");
        String stylesheetPath = stylesheetURL.toExternalForm();
        editScene.getStylesheets().add(stylesheetPath);
        editPane.getStyleClass().add("components_toolbar");
        namePane.setAlignment(Pos.CENTER);
        namePane.getStyleClass().add("name_pane");
        returnTypePane.setAlignment(Pos.CENTER);
        returnTypePane.getStyleClass().add("package_pane");
        accessPane.setAlignment(Pos.CENTER);
        accessPane.getStyleClass().add("package_pane");
        otherModifiersPane.setAlignment(Pos.CENTER);
        otherModifiersPane.getStyleClass().add("name_pane");
        argumentsPane.setAlignment(Pos.CENTER);
        argumentsPane.getStyleClass().add("components_toolbar");
        argumentsHeaderPane.setAlignment(Pos.CENTER);
        argumentsHeaderPane.getStyleClass().add("name_pane");
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.getStyleClass().add("name_pane");
    }

    private void addArgument(String name, String type) {
        HBox newArgumentPane = new HBox();
        VBox textFieldPane = new VBox();

        HBox namePane = new HBox();
        Label nameLabel = new Label("Name:");
        TextField nameTF = new TextField(name);
        namePane.getChildren().add(nameLabel);
        namePane.getChildren().add(nameTF);
        textFieldPane.getChildren().add(namePane);

        HBox typePane = new HBox();
        Label typeLabel = new Label("Type:");
        TextField typeTF = new TextField(type);
        typePane.getChildren().add(typeLabel);
        typePane.getChildren().add(typeTF);
        textFieldPane.getChildren().add(typePane);

        Button removeButton = new Button("Delete");
        removeButton.setOnAction(e -> {
            argumentsPane.getChildren().remove(newArgumentPane);
        });

        newArgumentPane.getChildren().add(textFieldPane);
        newArgumentPane.getChildren().add(removeButton);

        namePane.setAlignment(Pos.CENTER);
        namePane.getStyleClass().add("name_pane");
        typePane.setAlignment(Pos.CENTER);
        typePane.getStyleClass().add("name_pane");
        textFieldPane.setAlignment(Pos.CENTER);
        textFieldPane.getStyleClass().add("name_Pane");
        newArgumentPane.setAlignment(Pos.CENTER);

        argumentsPane.getChildren().add(newArgumentPane);
    }

    public void show(MethodPrototype initMethod) {
        usedClasses.clear();
        for (int i = 1; i < argumentsPane.getChildren().size(); ++i)
            argumentsPane.getChildren().remove(i);
        if (initMethod != null) {
            method = initMethod;
            // SET SETTINGS TO MATCH 
            
            for (VariablePrototype arg : method.getArguments())
                addArgument(arg.getName(), arg.getType());
            nameTF.setText(method.getName());
            returnTypeTF.setText(method.getReturnType());
            accessChoiceBox.getSelectionModel().select(method.getModifiers()
                    .get(0).ordinal());
            staticCheckBox.setSelected(method.getModifiers().contains(STATIC));
            abstractCheckBox.setSelected(method.getModifiers().contains(ABSTRACT));
        } else {
            method = new MethodPrototype();
            nameTF.setText("");
            returnTypeTF.setText("");
            accessChoiceBox.getSelectionModel().select(0);
            staticCheckBox.setSelected(false);
            abstractCheckBox.setSelected(false);
        }
        showAndWait();
    }
    
    public ArrayList<String> getUsedClasses() {
        return usedClasses;
    }
}
