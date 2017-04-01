/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jcd.controller.CanvasController;
import static jcd.controller.ControlMode.*;
import jcd.controller.DiagramEditController;
import static jcd.PropertyType.*;
import static jcd.data.ContainerType.INTERFACE;
import jcd.data.DataManager;
import jcd.data.MethodPrototype;
import jcd.data.VariablePrototype;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;
import saf.ui.AppGUI;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppYesNoCancelDialogSingleton;

/**
 *
 * @author Shane
 */
public class Workspace extends AppWorkspaceComponent {
    public static final int CANVAS_Y_OFFSET = 40;
    
    // Entire App
    AppTemplate app;
    
    // App's GUI
    AppGUI gui;
    
    // Additional File Buttons
    HBox fileToolbar;
    Button photoButton;
    Button exportCodeButton;
    
    // Edit Toolbar and Buttons
    HBox editToolbar;
    Button selectButton;
    Button resizeButton;
    Button addClassButton;
    Button addInterfaceButton;
    Button removeElementButton;
    Button undoButton;   
    Button redoButton;
    
    // View Toolbar and Buttons
    HBox viewToolbar;
    Button zoomInButton;
    Button zoomOutButton;
    VBox gridOptionsPane;
    CheckBox showGridCheckBox;
    CheckBox enableSnappingCheckBox;
    
    // Component Toolbar and Components
    VBox componentToolbar;
    
    // Name
    HBox namePane;
    Label nameLabel;
    TextField nameTextField;
    
    // Package
    HBox packagePane;
    Label packageLabel;
    TextField packageTextField;    
    
    // Parents
    HBox parentPane;
    Label parentLabel;
    Button parentSelectionButton; 
    
    // Variables
    VBox variablesPane;
    HBox variableButtonsPane;
    Label variablesLabel;
    Button addVariableButton;
    ScrollPane variablesScrollPane; 
    VBox variablesTable;
    
    // Methods
    VBox methodsPane;
    HBox methodButtonsPane;
    Label methodsLabel;
    Button addMethodButton;
    ScrollPane methodsScrollPane;   
    VBox methodsTable;
 
    // Render Area
    ScrollPane diagramScrollPane;
    Pane diagramPane;
    Canvas diagramCanvas;
    
    // Controllers
    DiagramEditController diagramEditController;
    CanvasController canvasController;
    ParentSelectionSingleton parentSelectionSingleton;
    VariableEditSingleton variableEditSingleton;
    MethodEditSingleton methodEditSingleton;
    PackageInquirySingleton packageInquirySingleton;
    
    // Dialogs
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    public Workspace(AppTemplate initApp) throws IOException {
	app = initApp;
	gui = app.getGUI();
        
        // Setup GUI
	layoutGUI();
	setupHandlers();
    }
    
    private void layoutGUI() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // Additional File Buttons
        fileToolbar = new HBox();
        photoButton = gui.initChildButton(fileToolbar, PHOTO_ICON.toString(), PHOTO_TOOLTIP.toString(), false);
        exportCodeButton = gui.initChildButton(fileToolbar, EXPORT_JAVA_ICON.toString(), EXPORT_JAVA_TOOLTIP.toString(), false);
    
        // Edit Toolbar and Buttons
        editToolbar = new HBox();
        selectButton = gui.initChildButton(editToolbar, SELECT_ICON.toString(), SELECT_TOOLTIP.toString(), false);
        resizeButton = gui.initChildButton(editToolbar, RESIZE_ICON.toString(), RESIZE_TOOLTIP.toString(), false);
        addClassButton = gui.initChildButton(editToolbar, ADD_CLASS_ICON.toString(), ADD_CLASS_TOOLTIP.toString(), false);
        addInterfaceButton = gui.initChildButton(editToolbar, ADD_INTERFACE_ICON.toString(), ADD_INTERFACE_TOOLTIP.toString(), false);
        removeElementButton = gui.initChildButton(editToolbar, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), true);
        undoButton = gui.initChildButton(editToolbar, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), true);   
        redoButton = gui.initChildButton(editToolbar, REDO_ICON.toString(), REDO_TOOLTIP.toString(), true);
    
        // View Toolbar and Buttons
        viewToolbar = new HBox();
        zoomInButton = gui.initChildButton(viewToolbar, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), false);
        zoomOutButton = gui.initChildButton(viewToolbar, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), false);
        showGridCheckBox = new CheckBox(props.getProperty(SHOW_GRID_LABEL));
        enableSnappingCheckBox = new CheckBox(props.getProperty(ENABLE_SNAPPING_LABEL));
        gridOptionsPane = new VBox();
        gridOptionsPane.getChildren().add(showGridCheckBox);
        gridOptionsPane.getChildren().add(enableSnappingCheckBox);
        viewToolbar.getChildren().add(gridOptionsPane);
    
        // Component Toolbar and Components
        componentToolbar = new VBox();
        
        // Name
        nameLabel = new Label(props.getProperty(NAME_LABEL));
        nameTextField = new TextField();
        namePane = new HBox();
        namePane.setAlignment(Pos.CENTER_LEFT);
        namePane.getChildren().add(nameLabel);
        namePane.getChildren().add(nameTextField);
        componentToolbar.getChildren().add(namePane);
    
        // Package
        packageLabel = new Label(props.getProperty(PACKAGE_LABEL));
        packageTextField = new TextField(); 
        packagePane = new HBox();
        packagePane.setAlignment(Pos.CENTER_LEFT);
        packagePane.getChildren().add(packageLabel);
        packagePane.getChildren().add(packageTextField);
        componentToolbar.getChildren().add(packagePane);
        packagePane.setLayoutY(300);
    
        // Parents
        parentLabel = new Label(props.getProperty(PARENT_LABEL));
        parentSelectionButton = new Button("Edit");
        parentSelectionButton.setDisable(true);
        parentPane = new HBox();
        parentPane.setAlignment(Pos.CENTER_LEFT);
        parentPane.getChildren().add(parentLabel);
        parentPane.getChildren().add(parentSelectionButton);
        componentToolbar.getChildren().add(parentPane);
    
        // Variables
        variablesLabel = new Label(props.getProperty(VARIABLES_LABEL));
        variableButtonsPane = new HBox();
        variableButtonsPane.setAlignment(Pos.CENTER_LEFT);
        variableButtonsPane.getChildren().add(variablesLabel);
        addVariableButton = gui.initChildButton(variableButtonsPane, ADD_COMPONENT_ICON.toString(), ADD_VARIABLE_TOOLTIP.toString(), true);
        variablesTable = new VBox();
        variablesScrollPane = new ScrollPane(variablesTable);
        variablesScrollPane.setMinHeight(196);
        variablesPane = new VBox();
        variablesPane.getChildren().add(variableButtonsPane);
        variablesPane.getChildren().add(variablesScrollPane);
        componentToolbar.getChildren().add(variablesPane);
    
        // Methods
        methodsLabel = new Label(props.getProperty(METHODS_LABEL));
        methodButtonsPane = new HBox();
        methodButtonsPane.setAlignment(Pos.CENTER_LEFT);
        methodButtonsPane.getChildren().add(methodsLabel);
        addMethodButton = gui.initChildButton(methodButtonsPane, ADD_COMPONENT_ICON.toString(), ADD_METHOD_TOOLTIP.toString(), true);
        methodsTable = new VBox();
        methodsScrollPane = new ScrollPane(methodsTable); 
        methodsScrollPane.setMinHeight(196);
        methodsPane = new VBox();
        methodsPane.getChildren().add(methodButtonsPane);
        methodsPane.getChildren().add(methodsScrollPane);
        componentToolbar.getChildren().add(methodsPane);
        
        // Render Area        
        diagramPane = new Pane();
        diagramScrollPane = new ScrollPane(diagramPane);
        
        // Add everything to gui
        gui.getTopPane().getChildren().add(fileToolbar);
        gui.getTopPane().getChildren().add(editToolbar);
        gui.getTopPane().getChildren().add(viewToolbar);
        gui.getAppPane().setRight(componentToolbar);
        gui.getAppPane().setCenter(diagramScrollPane);
        
        // Sync DataManager with gui
        ((DataManager)app.getDataComponent()).setDiagramComponents(diagramPane.getChildren());
    
        // Dialogs
        messageDialog = AppMessageDialogSingleton.getSingleton();
        yesNoCancelDialog = AppYesNoCancelDialogSingleton.getSingleton(); 
    }
    
    private void setupHandlers() {
        
        // NON CANVAS INTERACTIONS
        diagramEditController = new DiagramEditController(app);        
        photoButton.setOnAction(e -> {
            diagramEditController.handleSnapshotRequest();
        });        
        exportCodeButton.setOnAction(e -> {
            diagramEditController.handleExportCode();
        });        
        selectButton.setOnAction(e -> {
            diagramEditController.handleSelectionTool();
        });        
        resizeButton.setOnAction(e -> {
            diagramEditController.handleResizeTool();
        });       
        addClassButton.setOnAction(e -> {
            diagramEditController.handleAddClass();
        });        
        addInterfaceButton.setOnAction(e -> {
            diagramEditController.handleAddInterface();
        });        
        removeElementButton.setOnAction(e -> {
            diagramEditController.handleRemoveElement();
        });        
        undoButton.setOnAction(e -> {
            diagramEditController.handleUndo();
        });       
        redoButton.setOnAction(e -> {
            diagramEditController.handleRedo();
        });        
        zoomInButton.setOnAction(e -> {
            diagramEditController.handleZoomIn();
        });        
        zoomOutButton.setOnAction(e -> {
            diagramEditController.handleZoomOut();
        });        
        showGridCheckBox.setOnAction(e -> {
            setDisplayGrid(showGridCheckBox.selectedProperty().get());
        });                
        nameTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                diagramEditController.handleNameChange(nameTextField.getText());
        });
        packageTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                diagramEditController.handlePackageChange(packageTextField.getText());
        });       
        parentSelectionButton.setOnAction(e -> {
            diagramEditController.handleParentEdit(); 
        });        
        addVariableButton.setOnAction(e -> {
            diagramEditController.handleAddVariable();
        });               
        addMethodButton.setOnAction(e -> {
            diagramEditController.handleAddMethod(); 
        });        
        
        // CANVAS INTERACTIONS
        canvasController = new CanvasController(app);
        enableSnappingCheckBox.setOnAction(e -> {
            canvasController.handleSnappingChange(enableSnappingCheckBox.selectedProperty());
        });
        diagramPane.setOnMousePressed(e->{
	    canvasController.handleMousePress((int)e.getX(), (int)e.getY());
	});
	diagramPane.setOnMouseReleased(e->{
	    canvasController.handleMouseRelease((int)e.getX(), (int)e.getY());
	});
	diagramPane.setOnMouseDragged(e->{
	    canvasController.handleMouseDragged((int)e.getX(), (int)e.getY());
	});
	diagramPane.setOnMouseExited(e->{
	    canvasController.handleMouseExited((int)e.getX(), (int)e.getY());
	});
        
        // VARIABLE INTERACTIONS
        variableEditSingleton = VariableEditSingleton.getSingleton();
        variableEditSingleton.init(gui.getWindow(), (DataManager)app.getDataComponent());
        
        // METHOD INTERACTION
        methodEditSingleton = MethodEditSingleton.getSingleton();
        methodEditSingleton.init(gui.getWindow(), (DataManager)app.getDataComponent());
        
        // PACKAGE INQUIRER
        packageInquirySingleton = PackageInquirySingleton.getSingleton();
        packageInquirySingleton.init(gui.getWindow());
        
        // PARENT SELECTION
        parentSelectionSingleton = ParentSelectionSingleton.getSingleton();
        parentSelectionSingleton.init(gui.getWindow());
    }
    
    @Override
    public void initStyle() {
        // Top Toolbar
        fileToolbar.getStyleClass().add("edit_toolbar_row");
        editToolbar.getStyleClass().add("edit_toolbar_row");
        viewToolbar.getStyleClass().add("edit_toolbar_row");
        
        // Component Toolbar
        namePane.getStyleClass().add("name_pane");
        packagePane.getStyleClass().add("package_pane");
        parentPane.getStyleClass().add("parent_pane");
        variableButtonsPane.getStyleClass().add("variables_methods_pane");
        variablesScrollPane.getStyleClass().add("bordered_pane");
        methodButtonsPane.getStyleClass().add("variables_methods_pane");
        methodsScrollPane.getStyleClass().add("bordered_pane");
        componentToolbar.getStyleClass().add("components_toolbar");
               
        // Canvas Area
        diagramScrollPane.getStyleClass().add("canvas_pane");
        diagramPane.getStyleClass().add("max_pane");
    }
    
    @Override
    public void reloadWorkspace() {
        DataManager dataManager = (DataManager)app.getDataComponent();
        ContainerPane selectedContainer = dataManager.getSelectedContainer();
        if (selectedContainer != null) {
            nameTextField.setText(selectedContainer.getName());
            packageTextField.setText(selectedContainer.getPackage());
        }
        toggleGUIComponents();
        updateVarMethodPanes();
    }
    
    private void toggleGUIComponents() {
        DataManager dataManager = (DataManager)app.getDataComponent();
        if (dataManager.getSelectedContainer() == null) {
            nameTextField.setDisable(true);
            nameTextField.setText("");
            packageTextField.setDisable(true);
            packageTextField.setText("");
            parentSelectionButton.setDisable(true);
            addVariableButton.setDisable(true);
            addMethodButton.setDisable(true);
        } else {
            removeElementButton.setDisable(false);
            nameTextField.setDisable(false);
            packageTextField.setDisable(false);
            parentSelectionButton.setDisable(false);
            if (dataManager.getSelectedContainer().getType().equals(INTERFACE))
                addVariableButton.setDisable(true);
            else
                addVariableButton.setDisable(false);
            addMethodButton.setDisable(false);
        }
        if (dataManager.currControlModeIs(SELECTING)) {
            selectButton.setDisable(true);
            resizeButton.setDisable(false);            
            if (dataManager.getSelectedContainer() != null) {
                removeElementButton.setDisable(false);
                nameTextField.setDisable(false);
                packageTextField.setDisable(false);
            } else {
                removeElementButton.setDisable(true);
            }
        } else if (dataManager.currControlModeIs(PRE_SIZING) || dataManager.currControlModeIs(SIZING)) {
            selectButton.setDisable(false);
            resizeButton.setDisable(true);   
            removeElementButton.setDisable(true);
            nameTextField.setDisable(true);
            packageTextField.setDisable(true);
            parentSelectionButton.setDisable(true);
            addVariableButton.setDisable(true);
            addMethodButton.setDisable(true);
        } else if (dataManager.currControlModeIs(NEW)) {
            app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT);
            showGridCheckBox.selectedProperty().set(false);
            enableSnappingCheckBox.selectedProperty().set(false);
            selectButton.setDisable(false);
            resizeButton.setDisable(false);
            packageTextField.setDisable(true);
            nameTextField.setDisable(true);
            nameTextField.setText("");
            packageTextField.setDisable(true);
            packageTextField.setText("");
        }
    }
    
    private void updateVarMethodPanes() {
        variablesTable.getChildren().clear();
        methodsTable.getChildren().clear();
        DataManager dataManager = (DataManager)app.getDataComponent();
        if (dataManager.getSelectedContainer() != null && (!dataManager.currControlModeIs(PRE_SIZING) 
                || !dataManager.currControlModeIs(SIZING))) {   
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            // MAKE ROWS FOR EACH
            for (VariablePrototype variable : dataManager.getSelectedContainer().getContainer().getVariables()) {
                ImageView removeIcon = new ImageView(new Image(FILE_PROTOCOL + PATH_IMAGES + props.getProperty(SUBTRACT_COMPONENT_ICON.toString())));
                VariableRow varRow = new VariableRow(variable);
                varRow.setDeleteButtonSettings(removeIcon, () -> {
                    diagramEditController.handleRemoveVariable(variable);
                });
                varRow.setEditButtonSettings(null, () -> {
                    diagramEditController.handleEditVariable(variable);
                });
                variablesTable.getChildren().add(varRow);
            }
            for (MethodPrototype method : dataManager.getSelectedContainer().getContainer().getMethods()) {
                ImageView removeIcon = new ImageView(new Image(FILE_PROTOCOL + PATH_IMAGES + props.getProperty(SUBTRACT_COMPONENT_ICON.toString())));
                MethodRow methodRow = new MethodRow(method);
                methodRow.setDeleteButtonSettings(removeIcon, () -> {
                    diagramEditController.handleRemoveMethod(method);
                });
                methodRow.setEditButtonSettings(null, () -> {
                    diagramEditController.handleEditMethod(method);
                });
                methodsTable.getChildren().add(methodRow);
            }
        }
    }
    
    public void setDisplayGrid(boolean display) {
        if (display) {
            diagramCanvas = new Canvas(diagramPane.getWidth(), diagramPane.getHeight());
            diagramPane.getChildren().add(0, diagramCanvas);
            GraphicsContext gc = diagramCanvas.getGraphicsContext2D();
            gc.setLineWidth(0.2);
            gc.setStroke(Color.DARKGRAY);
            gc.beginPath();

            int gridScale = 15;
            int height = (int)diagramPane.getHeight();
            int width = (int)diagramPane.getWidth();
            // VERTICAL LINES
            for (int horLoc = 0; horLoc < width; horLoc += gridScale) {
                gc.moveTo(horLoc, 0);
                gc.lineTo(horLoc, height);
                gc.stroke();
            } 
            // HORIZONTAL LINES
            for (int vertLoc = 0; vertLoc < height; vertLoc += gridScale) {
                gc.moveTo(0, vertLoc);
                gc.lineTo(width, vertLoc);
                gc.stroke();
            }
        } else {
            diagramPane.getChildren().remove(diagramCanvas);
            diagramCanvas = null;
        }
    }
    
    public Pane getDiagramCanvas() {
        return diagramPane;
    }
}
